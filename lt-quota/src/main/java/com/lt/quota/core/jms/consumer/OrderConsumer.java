package com.lt.quota.core.jms.consumer;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.model.JmsTopicOrderMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 创建：cndym
 * 时间：2017/5/3 7:25
 */
@Component
public class OrderConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    final String USERID = "userId";
    final String LIST = "list";
    final String ALL = "ALL";

    @Autowired
    private NewMsgDeal newMsgDeal;
    @JmsListener(containerFactory = "topicFactory", destination = IConstants.QUEUE_ORDER_MESSAGE)
    public void receiveOrderQueue(String msg) {
        logger.info("收到消息：{}", msg);
        JmsTopicOrderMsg obj = JSONObject.parseObject(msg, JmsTopicOrderMsg.class);
        if (obj.getUserId().equals(ALL)) {
            newMsgDeal.execSendALLMsg(obj.getMsg());
            return;
        }
        newMsgDeal.execSendMsg(obj.getUserId(), obj.getMsg());
    }

    /**
     * 收到订单模块发起的用户多品种订阅 09:56:16,423
     *
     * @param msg
     */
    @JmsListener(containerFactory = "topicFactory", destination = IConstants.QUEUE_ORDER_MESSAGE_QUOTA_SUBSCRIPTION)
    public void receiveOrderSubscriptionQueue(String msg) {
        logger.info("收到消息：{}", msg);
        Map<String, Object> obj = JSONObject.parseObject(msg);
        String userId = (String) obj.get(USERID);
        List<String> list = (List<String>) obj.get(LIST);
        if (list == null || list.size() <= 0 || userId == null) {
            logger.error("参数为空");
            return;
        }
        newMsgDeal.deal(userId, list);
    }
}
