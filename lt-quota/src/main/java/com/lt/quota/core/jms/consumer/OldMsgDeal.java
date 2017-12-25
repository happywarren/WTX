package com.lt.quota.core.jms.consumer;

import com.lt.quota.core.appServer.old.OldChannelManger;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Description:  老的消息处理机制
 * Created by yanzhenyu on 2017/8/1.
 */
@Component
public class OldMsgDeal {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 收到订单模块发起的用户多品种订阅
     */
    void deal(String userId, List<String> list) {
        //通过userID查找通道
        Channel channel = OldChannelManger.getInstance().getChannel(userId);
        if (Utils.isNotEmpty(channel)) {
            for (String product : list) {
                //保存userID 还有product对应关系
                OldChannelManger.getInstance().add(userId, product);
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
        Channel channel = OldChannelManger.getInstance().getChannel(userId);
        if (Utils.isNotEmpty(channel)) {
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
        Map<String, Channel> channelMap = OldChannelManger.getInstance().getUserChannelMap();
        for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            Channel channel = entry.getValue();
            if (channel.isWritable()) {
                channel.writeAndFlush(msg);
            }
        }
    }
}