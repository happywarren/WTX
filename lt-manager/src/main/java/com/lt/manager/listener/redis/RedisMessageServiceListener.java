package com.lt.manager.listener.redis;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.StringTools;

/**
 * redis监听消息处理
 * @author guodw
 *
 */
@Service
public class RedisMessageServiceListener {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public static Map<String, RedisQuotaObject> quoteMap = new ConcurrentHashMap<String, RedisQuotaObject>();
	
	/**
	 * redis消息接收
	 * @param message
	 */
	public void handleMessage(String message) {
		//logger.debug("===========接收到行情信息={}",message);
		if(StringTools.isEmpty(message)){
			return ;
		}
		try {
			
			JSONObject jsonObj = JSONObject.parseObject(message);
			JSON data = jsonObj.getJSONObject("DATA");
			RedisQuotaObject obj = JSONObject.toJavaObject(data,RedisQuotaObject.class);
			//logger.debug("=======RedisQuotaObject={}=========",JSONObject.toJSONString(obj));
			if(null == obj){
				return;
			}
			//获取行情商品名称
			final String  product = obj.getProductName();
			if(null == product){
				return;
			}

			//内存保存最新的行情数据
			quoteMap.put(product, obj);
			
		} catch (Exception e) {
			logger.error("RedisMessageServiceListener ----->>handleMessage--->>error-->>",e);
		}
		
	}
	
}