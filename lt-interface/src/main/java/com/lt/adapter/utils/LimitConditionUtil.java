package com.lt.adapter.utils;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.StringTools;

public class LimitConditionUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(LimitConditionUtil.class);
	
	/**
	 * 判断信息是否在黑名单中
	 * @param pmap
	 * @return
	 */
	public static boolean isLimit(Map<String,Object> pmap){
		RedisTemplate<String, String> redisTemplate = (RedisTemplate<String, String>)SpringUtils.getBean("redisTemplate");
		BoundHashOperations<String, String, String> limitRedis = redisTemplate.boundHashOps(RedisUtil.LIMIT_CONDITION);
		String limitCondition = limitRedis.get(RedisUtil.LIMIT_CONDITION);
		if(StringTools.isEmpty(limitCondition)){
			return false;
		}
		
		//遍历map
		if(LimitConditionUtil.contains(pmap, limitCondition)){
			return true;
		}
		
        //遍历data      
        if(pmap.get("data") != null && "".equals(pmap.get("data"))){
        	Map<String,Object> dataMap = JSONObject.parseObject(String.valueOf(pmap.get("data")), Map.class);
        	if(LimitConditionUtil.contains(dataMap, limitCondition)){
    			return true;
    		}
        }

        return false;
	}
	
	/**
	 * 判断limitCondition中是否包含map中的值
	 * @param dataMap
	 * @param limitCondition
	 * @return
	 */
	private static boolean contains(Map<String,Object> dataMap,String limitCondition){
		Iterator<String> iterator = dataMap.keySet().iterator();
		 while(iterator.hasNext()){
	        	String key = iterator.next();
	        	String value = String.valueOf(dataMap.get(key));
	        	if(!StringTools.isEmpty(value)){
	        		if(limitCondition.contains("$"+value+"$")){//如果包含
	        			return true;
	        		}
	        	}
	        	
	        }
		 return false;
	}

}
