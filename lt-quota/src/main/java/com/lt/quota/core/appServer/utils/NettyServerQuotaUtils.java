package com.lt.quota.core.appServer.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.spring.SpringUtils;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.jms.produce.UserOnlineProduce;
import com.lt.quota.core.model.Token;
import com.lt.quota.core.model.UserOperateLog;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.utils.TokenTools;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

public class NettyServerQuotaUtils {

    static Logger logger = LoggerFactory.getLogger(NettyServerQuotaUtils.class);

    /**
     * 日志记录
     *
     * @param DATA
     * @param type
     * @param userOnlineProduce
     * @return
     */
    public static String execute(String DATA, int type, UserOnlineProduce userOnlineProduce) {
        JSONObject jsons = (JSONObject) JSON.parse(DATA);
        String token = jsons.getString("token");
        Token tokenInfo = TokenTools.parseToken(token);
        if (!tokenInfo.isIsdeline() && DATA.contains("recordIP")) {
            saveLog(jsons, tokenInfo.getUserId(), type, userOnlineProduce);
        }
        return tokenInfo.getUserId();
    }

    /**
     * 通道删除
     *
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        //用戶通道刪除
        LTQuotaUtils.removeUserChannelMap(channel);
    }

    /**
     * 处理不发送通道
     *
     * @param jsons
     * @param channel
     */
    public static void noSendChannel(JSONObject jsons, Channel channel) {
        String DATA = jsons.getString("DATA");
        jsons = (JSONObject) JSON.parse(DATA);
        //用户注册哪个商品
        String product = jsons.getString("product");
        String token = jsons.getString("token");
        //token验证
        Token tokenInfo = TokenTools.parseToken(token);
        //验证用户状态是否正常
        if (!tokenInfo.isIsdeline()) {
            String userId = tokenInfo.getUserId();
            //删除商品用户关系
            if (Utils.isNotEmpty(product)) {
                LTQuotaUtils.removeUserChannelMap(product, userId);
            } else {
                LTQuotaUtils.removeProductChannelMap(userId);
            }
        }
    }

    /**
     * 注册处理
     *
     * @param channel
     * @param jsons
     */
    public static void registered(Channel channel, JSONObject jsons) {
        String DATA = jsons.getString("DATA");
        jsons = (JSONObject) JSON.parse(DATA);
        //用户注册哪个商品
        String product = jsons.getString("product");
        String token = jsons.getString("token");
        //token验证
        Token tokenInfo = TokenTools.parseToken(token);
        if (tokenInfo.isIsdeline()) {
            return;
        } else {
            String userId = tokenInfo.getUserId();
            LTQuotaUtils.saveUserChannelMap(product, userId, channel);
        }
        StringRedisTemplate stringRedisTemplate = SpringUtils.getBean("stringRedisTemplate", StringRedisTemplate.class);
        //获取redis 最新行情数据
        String msg = stringRedisTemplate.opsForValue().get(IConstants.REDIS_PRODUCT_LAST_QUOTA + product);
        if (Utils.isEmpty(msg)) {
            return;
        }
        QuotaBean quotaBean = JSON.parseObject(msg, QuotaBean.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CMDID", 1001);
        quotaBean.setSource(null);
        map.put("DATA", quotaBean);
        channel.writeAndFlush(JSONObject.toJSONString(map));
        logger.info("发送channel = {} 最新行情 map = {}完毕", channel, JSONObject.toJSONString(map));

    }

	/*{
          "CMDID" : "QU001",
		  "DATA" : {
		    "recordIP" : "192.168.31.159",
		    "recordImei" : "e71773c16639a0d3024fcee75f4de74b1ee0cd6f",
		    "recordLoginMode" : "手机号登录",
		    "recordCarrierOperator" : "中国移动",
		    "recordAccessMode" : "Wifi",
		    "recordDevice" : "iPhone10.3.2",
		    "token" : "ebac3ba59987b97be9fa80b395f2a3f662a73b0e78b4db18caef6a856bc8a2d60cb7ceee0ccdb994badb598c88ed950f4e5670bdc199227eff07bf9203cd5037",
		    "recordVersion" : "2.0.3.4",
		    "sign" : "b174b85a1aca23286b5c9944a25b97a6"
		  }*/

    /**
     * 日志保存
     *
     * @param jsons
     * @param userId
     * @param type
     */
    private static void saveLog(JSONObject jsons, String userId, Integer type, UserOnlineProduce userOnlineProduce) {
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
}
