package com.lt.quota.core.appServer.old;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.appServer.newer.NewChannelManger;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.constant.SystemDataCount;
import com.lt.quota.core.jms.produce.UserOnlineProduce;
import com.lt.quota.core.model.Token;
import com.lt.quota.core.model.UserOperateLog;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.utils.TokenTools;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class OldClientOperator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserOnlineProduce userOnlineProduce;

    /**
     * 订阅
     */
    public boolean subscribe(String data, Channel channel) {
        JSONObject jsonObject = (JSONObject) JSON.parse(data);
        //用户注册哪个商品
        String product = jsonObject.getString("product");
        String token = jsonObject.getString("token");
        //token验证
        Token tokenInfo = TokenTools.parseToken(token);
        if (tokenInfo.isIsdeline()) {
            return false;
        }
        String userId = tokenInfo.getUserId();
        if (Utils.isEmpty(userId)) {
            return false;
        }

        NewChannelManger.getInstance().removeByUserId(userId);

        OldChannelManger.getInstance().add(product, userId, channel);
        //在线用户
        stringRedisTemplate.opsForSet().add(IConstants.REDIS_USER_ONLINE_LIST, userId);
        //用户上线
        if (data.contains("recordIP")) {
            saveLog(jsonObject, userId, IConstants.OPERATE_USER_GO_LIVE_LOG);
        }

        if (Utils.isEmpty(product)) {
            return true;
        }
        //获取redis 最新行情数据
        String msg = stringRedisTemplate.opsForValue().get(IConstants.REDIS_PRODUCT_LAST_QUOTA + product);
        if (Utils.isNotEmpty(msg)) {
            QuotaBean quotaBean = JSON.parseObject(msg, QuotaBean.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("CMDID", 1001);
            quotaBean.setSource(null);
            map.put("DATA", quotaBean);
            channel.writeAndFlush(JSONObject.toJSONString(map));
            logger.info("发送channel = {} 最新行情 map = {}完毕", channel, JSONObject.toJSONString(map));
        }
        return true;
    }

    /**
     * 取消订阅
     */
    public boolean cancelSubscribe(String data) {
        JSONObject jsonObject = (JSONObject) JSON.parse(data);
        //用户注册哪个商品
        String product = jsonObject.getString("product");
        String token = jsonObject.getString("token");
        //token验证
        Token tokenInfo = TokenTools.parseToken(token);
        if (tokenInfo.isIsdeline()) {
            return false;
        }
        String userId = tokenInfo.getUserId();
        if (Utils.isEmpty(userId)) {
            return false;
        }
        //删除商品用户关系
        if (Utils.isNotEmpty(product)) {
            OldChannelManger.getInstance().remove(userId, product);
        } else {
            OldChannelManger.getInstance().remove(userId);
        }
        return true;
    }


    public boolean quit(String data, Channel channel) {
        JSONObject jsonObject = (JSONObject) JSON.parse(data);
        //用户注册哪个商品
        String product = jsonObject.getString("product");
        String token = jsonObject.getString("token");
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
        if (data.contains("recordIP")) {
            saveLog(jsonObject, userId, IConstants.OPERATE_USER_OFFLINE_LOG);
        }
        OldChannelManger.getInstance().remove(channel);
        return true;
    }

    public void exceptionQuit(Channel channel) {
        String userId = OldChannelManger.getInstance().getUserId(channel);
        OldChannelManger.getInstance().remove(channel);
        if (Utils.isEmpty(userId)) {
            return;
        }
        stringRedisTemplate.opsForSet().remove(IConstants.REDIS_USER_ONLINE_LIST, userId);
    }


    /**
     * 日志保存
     *
     * @param jsons
     * @param userId
     * @param type
     */
    private void saveLog(JSONObject jsons, String userId, Integer type) {
        String recordIP = jsons.getString("recordIP");
        String recordImei = jsons.getString("recordImei");
        String recordLoginMode = jsons.getString("recordLoginMode");
        String recordCarrierOperator = jsons.getString("recordCarrierOperator");
        String recordAccessMode = jsons.getString("recordAccessMode");
        String recordDevice = jsons.getString("recordDevice");
        String recordVersion = jsons.getString("recordVersion");
        String operateName = type == IConstants.OPERATE_USER_GO_LIVE_LOG ? "用户上线" : "用户下线";
        UserOperateLog userLog = new UserOperateLog(userId,
                type,
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
        String product = quotaBean.getProductName();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("CMDID", 1001);
        dataMap.put("DATA", quotaBean);
        String json = JSONObject.toJSONString(dataMap);
        Set<String> set = OldChannelManger.getInstance().getUserSet(product);
        logger.debug("分发商品 {} 行情 {} ", product, json);
        if (!Utils.isNotEmpty(set)) {
            return;
        }
        logger.info("订阅商品 {} 行情 {} 数量: {}", product, json, set.size());
        for (String userId : set) {
            Channel channel = OldChannelManger.getInstance().getChannel(userId);
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
                OldChannelManger.getInstance().remove(userId, product);
            }
        }
    }
}
