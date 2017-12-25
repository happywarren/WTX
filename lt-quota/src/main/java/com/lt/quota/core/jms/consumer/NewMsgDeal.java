package com.lt.quota.core.jms.consumer;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.appServer.newer.NewChannelManger;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  新的消息处理机制
 * Created by yanzhenyu on 2017/8/1.
 */
@Component
public class NewMsgDeal {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 收到订单模块发起的用户多品种订阅
     */
    void deal(String userId, List<String> list) {
        //通过userID查找通道
        Channel channel = NewChannelManger.getInstance().getChannel(userId);
        if (Utils.isNotEmpty(channel)) {
            for (String product : list) {
                //保存userID 还有product对应关系
                NewChannelManger.getInstance().add(userId, product);
            }
        } else {
            logger.error("用户ID" + userId + "不存在通道");
        }
    }

    /**
     * 执行发送信息
     *
     * @param userId
     * @param msg
     */
    void execSendMsg(String userId, String msg) {
        if (msg.contains("QU003")) {
            Map<String, Object> map = new HashMap<>();
            map.put("CMD", IConstants.RES_CLOSING_TRANSACTION);
            map.put("DATA", "");
            msg = JSONObject.toJSONString(map);
        }
        Channel channel = NewChannelManger.getInstance().getChannel(userId);
        if (!Utils.isNotEmpty(channel)) {
            logger.info("用户 {} 通道不存在", userId);
        } else {
            logger.info("用户 {} 下发消息 {} ", userId, msg);
            if (channel.isWritable()) {
                channel.writeAndFlush(msg);
            }
        }
    }

    /**
     * 推送给所有人信息
     *
     * @param msg
     */
    void execSendALLMsg(String msg) {
        if (msg.contains("QU003")) {
            Map<String, Object> map = new HashMap<>();
            map.put("CMD", IConstants.RES_CLOSING_TRANSACTION);
            map.put("DATA", "");
            msg = JSONObject.toJSONString(map);
        }
        Map<String, Channel> channelMap = NewChannelManger.getInstance().getUserChannelMap();
        for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            Channel channel = entry.getValue();
            if (channel.isWritable()) {
                channel.writeAndFlush(msg);
            }
        }
    }
}