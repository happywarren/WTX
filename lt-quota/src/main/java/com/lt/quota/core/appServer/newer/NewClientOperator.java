package com.lt.quota.core.appServer.newer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.appServer.bean.request.LoginOutRequest;
import com.lt.quota.core.appServer.bean.request.LoginRequest;
import com.lt.quota.core.appServer.bean.request.MktSubscribeRequest;
import com.lt.quota.core.appServer.bean.request.MktUnSubscribeRequest;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.constant.SystemDataCount;
import com.lt.quota.core.jms.produce.UserOnlineProduce;
import com.lt.quota.core.model.Token;
import com.lt.quota.core.model.UserOperateLog;
import com.lt.quota.core.quota.bean.NewQuotaBean;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.TokenTools;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;

@Component
public class NewClientOperator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserOnlineProduce userOnlineProduce;

    /**
     * 登录-上线
     */
    public boolean login(String token, String data, Channel channel) {
        LoginRequest loginRequest = FastJsonUtils.getJson(data, LoginRequest.class);
        String hostAddress = getHostFormChannel(channel);
        loginRequest.setRecordIP(hostAddress);
        Token tokenInfo = TokenTools.parseToken(token);
        if (tokenInfo.isIsdeline()) {
            return false;
        }
        String userId = tokenInfo.getUserId();
        if (Utils.isEmpty(userId)) {
            return false;
        }
        NewChannelManger.getInstance().add(null, userId, channel);
        //在线用户
        stringRedisTemplate.opsForSet().add(IConstants.REDIS_USER_ONLINE_LIST, userId);
        //用户上线
        saveLoginLog(loginRequest, userId);
        return true;
    }

    /**
     * 订阅
     */
    public boolean subscribe(String token, String data, Channel channel) {
        List<MktSubscribeRequest> requestList = FastJsonUtils.getArrayJson(data, MktSubscribeRequest.class);

        //token验证
        Token tokenInfo = TokenTools.parseToken(token);
        if (tokenInfo.isIsdeline()) {
            return false;
        }

        String userId = tokenInfo.getUserId();
        if (Utils.isEmpty(userId)) {
            return false;
        }

        for (MktSubscribeRequest mktSubscribeRequest : requestList) {
            //用户注册哪个商品
            String productCode = mktSubscribeRequest.getProductCode();

            NewChannelManger.getInstance().add(productCode, userId, channel);

            if (Utils.isEmpty(productCode)) {
                continue;
            }

            //获取redis 最新行情数据
            String msg = stringRedisTemplate.opsForValue().get(IConstants.REDIS_PRODUCT_LAST_QUOTA + productCode);

            if (Utils.isNotEmpty(msg)) {
                QuotaBean quotaBean = JSON.parseObject(msg, QuotaBean.class);
                NewQuotaBean newQuotaBean = new NewQuotaBean(quotaBean);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("CMD", IConstants.RES_MKT_DATA);
                map.put("DATA", newQuotaBean);
                channel.writeAndFlush(JSONObject.toJSONString(map));
                logger.info("发送channel = {} 最新行情 map = {}完毕", channel, JSONObject.toJSONString(map));
            }
        }
        return true;
    }

    /**
     * 取消订阅
     */
    public boolean cancelSubscribe(String token, String data) {
        List<MktUnSubscribeRequest> unSubscribeRequestList = FastJsonUtils.getArrayJson(data, MktUnSubscribeRequest.class);
        //token验证
        Token tokenInfo = TokenTools.parseToken(token);
        if (tokenInfo.isIsdeline()) {
            return false;
        }
        String userId = tokenInfo.getUserId();
        if (Utils.isEmpty(userId)) {
            return false;
        }
        for (MktUnSubscribeRequest unSubscribeRequest : unSubscribeRequestList) {
            //用户注册哪个商品
            String product = unSubscribeRequest.getProductCode();
            //删除商品用户关系
            if (Utils.isNotEmpty(product)) {
                NewChannelManger.getInstance().remove(userId, product);
            } else {
                NewChannelManger.getInstance().remove(userId);
            }
        }
        return true;
    }


    /**
     * 登出-下线
     */
    public boolean quit(String token, String data, Channel channel) {
        LoginOutRequest loginOut = FastJsonUtils.getJson(data, LoginOutRequest.class);
        String hostAddress = getHostFormChannel(channel);
        loginOut.setRecordIP(hostAddress);
        //token验证
        Token tokenInfo = TokenTools.parseToken(token);
        if (tokenInfo.isIsdeline()) {
            return false;
        }
        String userId = tokenInfo.getUserId();
        if (Utils.isEmpty(userId)) {
            return false;
        }
        stringRedisTemplate.opsForSet().remove(IConstants.REDIS_USER_ONLINE_LIST, userId);
        saveLogOutLog(loginOut, userId);
        NewChannelManger.getInstance().remove(channel);
        return true;
    }

    /**
     * 从 Netty Channel 里取出 Host
     *
     * @param channel Netty Channel
     * @return Host 地址
     */
    private String getHostFormChannel(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    public void exceptionQuit(Channel channel) {
        NewChannelManger.getInstance().removeChannelGroup(channel);
        String userId = NewChannelManger.getInstance().getUserId(channel);
        NewChannelManger.getInstance().remove(channel);
        if (Utils.isEmpty(userId)) {
            return;
        }
        stringRedisTemplate.opsForSet().remove(IConstants.REDIS_USER_ONLINE_LIST, userId);
    }

    /**
     * 上线----日志保存
     *
     * @param loginRequest 请求对象
     * @param userId       用户 ID
     */
    private void saveLoginLog(LoginRequest loginRequest, String userId) {

        String recordIP = loginRequest.getRecordIP();
        String recordImei = loginRequest.getRecordImei();
        String recordLoginMode = loginRequest.getRecordLoginMode();
        String recordCarrierOperator = loginRequest.getRecordCarrierOperator();
        String recordAccessMode = loginRequest.getRecordAccessMode();
        String recordDevice = loginRequest.getRecordDevice();
        String recordVersion = loginRequest.getRecordVersion();

        String operateName = "用户上线";
        UserOperateLog userLog = new UserOperateLog(userId,
                IConstants.OPERATE_USER_GO_LIVE_LOG,
                operateName, true, operateName,
                "长连接", "", recordVersion,
                recordIP, recordDevice, recordImei,
                recordDevice, recordVersion, recordIP,
                recordLoginMode, recordImei, recordDevice,
                recordCarrierOperator, recordAccessMode);
        //抛出MQ
        userOnlineProduce.sendOrderMsg(JSONObject.toJSONString(userLog));
    }

    /**
     * 下线----日志保存
     *
     * @param loginOutRequest 请求对象
     * @param userId          用户 ID
     */
    private void saveLogOutLog(LoginOutRequest loginOutRequest, String userId) {

        String recordIP = loginOutRequest.getRecordIP();
        String recordImei = loginOutRequest.getRecordImei();
        String recordLoginMode = loginOutRequest.getRecordLoginMode();
        String recordCarrierOperator = loginOutRequest.getRecordCarrierOperator();
        String recordAccessMode = loginOutRequest.getRecordAccessMode();
        String recordDevice = loginOutRequest.getRecordDevice();
        String recordVersion = loginOutRequest.getRecordVersion();

        String operateName = "用户下线";
        UserOperateLog userLog = new UserOperateLog(userId,
                IConstants.OPERATE_USER_OFFLINE_LOG,
                operateName, true, operateName,
                "长连接", "", recordVersion,
                recordIP, recordDevice, recordImei,
                recordDevice, recordVersion, recordIP,
                recordLoginMode, recordImei, recordDevice,
                recordCarrierOperator, recordAccessMode);
        //抛出MQ
        userOnlineProduce.sendOrderMsg(JSONObject.toJSONString(userLog));
    }

    public void sendQuotaData(QuotaBean quotaBean) {

        NewQuotaBean newQuotaBean = new NewQuotaBean(quotaBean);
        String product = newQuotaBean.getQ();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("CMD", IConstants.RES_MKT_DATA);
        dataMap.put("DATA", newQuotaBean);
        String json = JSONObject.toJSONString(dataMap);

        ChannelGroup channels = NewChannelManger.getInstance().getChannelGroup();
        if (Utils.isNotEmpty(channels)) {
            channels.writeAndFlush(json);
        }

        Set<String> set = NewChannelManger.getInstance().getUserSet(product);
        logger.debug("分发商品 {} 行情 {} ", product, json);
        if (!Utils.isNotEmpty(set)) {
            return;
        }
       // logger.info("订阅商品 {} 行情 {} 数量: {}", product, json, set.size());
        Set<String> rmSet = new HashSet<String>();
        for (String userId : set) {
            Channel channel = NewChannelManger.getInstance().getChannel(userId);
            if (Utils.isNotEmpty(channel)) {
                if (channel.isWritable()) {
                    ChannelFuture channelFuture = channel.writeAndFlush(json);
                    SystemDataCount.getInstance().countQuota(product);
                    channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                SystemDataCount.getInstance().countSuccessQuota(product);
                            }
                        }
                    });
                }
            } else {
                rmSet.add(userId);
            }
        }
        set.removeAll(rmSet);
    }

    public void sendCloseMessage() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("CMD", IConstants.RES_CLIENT_QUIT);
        dataMap.put("DATA", "close client");
        String message = JSON.toJSONString(dataMap);
        Map<String, Channel> userChannelMap = NewChannelManger.getInstance().getUserChannelMap();
        for (Map.Entry<String, Channel> entry : userChannelMap.entrySet()) {
            Channel channel = entry.getValue();
            if (!Utils.isNotEmpty(channel)) {
                continue;
            }
            channel.writeAndFlush(message);
            channel.close().awaitUninterruptibly();
        }
    }

    public void sendCloseMessage(String userId) {
        if (Utils.isEmpty(userId)) {
            return;
        }
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("CMD", IConstants.RES_CLIENT_QUIT);
        dataMap.put("DATA", "close client");
        String message = JSON.toJSONString(dataMap);
        Channel channel = NewChannelManger.getInstance().getChannel(userId);
        if (channel.isWritable()) {
            channel.writeAndFlush(message);
        }
    }

}
