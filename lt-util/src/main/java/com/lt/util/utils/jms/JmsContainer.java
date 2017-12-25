package com.lt.util.utils.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * spring 提供的messageContainer需要为每个queue都配置一次，且对于初级程序员来讲，对其内部的确认管理，并发控制很难去管控。
 * 本类是对messageContainer的上层封装
 * @author boyce
 * 
 * 
 * 
 * 
 * <bean id="jmsContainer" class="com.luckin.stock.jms.JmsContainer" >
 *		<property name="connectionFactory" ref="connectionFactory"/>
 *		<property name="msgListeners">
 *		<map>
 *			<entry key-ref="queue1" value-ref="lisnter1" />
 *			<entry key-ref="queue2" value-ref="lisnter2"/>
 *		</map>
 *		</property>
 *	</bean>
 *
 */
public class JmsContainer implements BeanFactoryAware,InitializingBean{
	private Logger logger=LoggerFactory.getLogger(getClass());
	static AtomicInteger counter=new AtomicInteger();
	BeanFactory factory;
	ConnectionFactory connectionFactory;
	Map<Destination, Listener> msgListeners=new HashMap<Destination, Listener>();
	public void setMsgListeners(Map<Destination, Listener> msgListeners) {
		this.msgListeners = msgListeners;
	}
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	private class MsgListener implements MessageListener{
		Listener listener;
		Destination dest;
		public MsgListener(Destination dest,Listener listener){
			this.listener=listener;
			this.dest=dest;
		}
		@Override
		public void onMessage(Message message) {
			logger.debug("message {} arrived from destination {}!",message,dest);
			try {
				boolean flag = listener.onMessage(message);
				try {
					if(flag){
						message.acknowledge();
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				logger.error("error occuer while jms listenning message:"+message, e);
			}
		}
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.factory=beanFactory;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		//配置上下文加载好之后，加载jms消息侦听容器
		for (Iterator<Destination> iterator = msgListeners.keySet().iterator(); iterator.hasNext();) {
			Destination dest = (Destination) iterator.next();
			BeanDefinitionBuilder builder=BeanDefinitionBuilder.genericBeanDefinition(DefaultMessageListenerContainer.class);
			builder.setLazyInit(false);
			builder.addPropertyValue("connectionFactory", connectionFactory);
			builder.addPropertyValue("destination", dest);
			builder.addPropertyValue("sessionAcknowledgeMode", Session.CLIENT_ACKNOWLEDGE);
			builder.addPropertyValue("messageListener", new MsgListener(dest,msgListeners.get(dest)));
			builder.addPropertyValue("maxConcurrentConsumers", 10);
			builder.addPropertyValue("concurrentConsumers", 4);
			DefaultListableBeanFactory fac=(DefaultListableBeanFactory) factory;
			String beanName="consumer"+counter.incrementAndGet();
			fac.registerBeanDefinition(beanName, builder.getRawBeanDefinition());
			fac.getBean(beanName);
			logger.info("init a message listener {} for destination {}",msgListeners.get(dest).getClass(),dest);
		}
	}
}