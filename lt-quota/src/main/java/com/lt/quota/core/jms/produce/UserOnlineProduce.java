package com.lt.quota.core.jms.produce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;


/**
 * 创建：cndym
 * 时间：2017/5/3 7:22
 */
@Service
public class UserOnlineProduce {

    @Autowired
    private JmsTemplate jmsMessagingTemplate;

    @Autowired
    private Queue userOnlineQueue;

    public void sendOrderMsg(String msg) {
        this.jmsMessagingTemplate.convertAndSend(userOnlineQueue, msg);
    }
}
