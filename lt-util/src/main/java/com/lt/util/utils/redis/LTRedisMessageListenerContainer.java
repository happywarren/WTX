/*
 * PROJECT NAME: lt-util
 * PACKAGE NAME: com.lt.util.utils.redis
 * FILE    NAME: LTRedisMessageListenerContainer.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.util.utils.redis;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.lt.util.error.LTException;

/**
 * TODO 自定义Redis产品行情订阅容器
 * @author XieZhibing
 * @date 2017年1月10日 下午3:12:06
 * @version <b>1.0.0</b>
 */
public class LTRedisMessageListenerContainer extends RedisMessageListenerContainer {
	
	private static Logger logger = LoggerFactory.getLogger(LTRedisMessageListenerContainer.class);
	
	/** 订阅监听适配器 */
	private MessageListenerAdapter messageListener;
	/** redis模板 */
	private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * TODO 构造添加订阅的产品
	 * @author XieZhibing
	 * @date 2017年1月10日 上午10:56:08
	 */
	public void addQuotaTopic() {
		//开始时间
		long startTime = System.currentTimeMillis();
		
		logger.info("添加订阅的产品开始");
		
		//产品信息
		BoundHashOperations<String, String, Object> boundHashOps = redisTemplate.boundHashOps("PRODUCT_INFO");
		Map<String, Object> entries = boundHashOps.entries();
		if(entries == null){
			throw new LTException("没有查询到resis中的产品信息");
		}
		logger.info("已查询到resis中的产品信息{}条", entries.size());
		
		//订阅的产品
		Set<String> productCodeSet = entries.keySet();
		for (String productCode : productCodeSet) {
			//订阅产品主题
			ChannelTopic topic = new ChannelTopic(productCode);
			this.addMessageListener(messageListener, topic);
		}		
		logger.info("加载需要订阅的产品信息, 用时:{}ms", (System.currentTimeMillis() - startTime));
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年1月10日 上午11:01:47
	 * @see org.springframework.data.redis.listener.RedisMessageListenerContainer#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		// TODO Auto-generated method stub		
		//继承父类方法
		super.afterPropertiesSet();				
		//添加订阅的产品
		this.addQuotaTopic();		
	}

	/** 
	 * 获取 订阅监听适配器 
	 * @return messageListener 
	 */
	public MessageListenerAdapter getMessageListener() {
		return messageListener;
	}

	/** 
	 * 设置 订阅监听适配器 
	 * @param messageListener 订阅监听适配器 
	 */
	public void setMessageListener(MessageListenerAdapter messageListener) {
		this.messageListener = messageListener;
	}

	/** 
	 * 获取 redis模板 
	 * @return redisTemplate 
	 */
	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	/** 
	 * 设置 redis模板 
	 * @param redisTemplate redis模板 
	 */
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
