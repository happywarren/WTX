package com.lt.util.utils.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 *
 * 描述:MQ消息生产者
 *
 * @author  zhengb@cainiu.com
 * @created 2015年5月15日 下午7:57:12
 * @since   v0.0.1
 */
public class MessageQueueProducer {

	private JmsTemplate jmsTemplate;
	private Destination dest;

	public void sendMessage(final JSONObject json) {
		jmsTemplate.send(dest, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage msg = session.createTextMessage(json.toJSONString());
				return msg;
			}
		});
	}

	public void sendMessage(final String json) {
		jmsTemplate.send(dest, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage msg = session.createTextMessage(json);
				return msg;
			}
		});
	}
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDest() {
		return dest;
	}

	public void setDest(Destination dest) {
		this.dest = dest;
	}

}
