package com.lt.user.core.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayApproachThreadMap {

	private static Logger logger = LoggerFactory.getLogger(PayApproachThreadMap.class);
	//用于银联充值的同步、异步通知,防止多次通知造成用户资金多次并发累加
	private static Map<Long,ExecutorService>  approachMap = new ConcurrentHashMap<Long,ExecutorService>();
	
	private static final ExecutorService  executorService = Executors.newSingleThreadExecutor();
	
	public static ExecutorService getApproachTread(String id){
		try {
			if(!StringUtils.isNumeric(id)){
				return executorService;
			}
			Long index = Long.parseLong(id);
			long i = index%10l;
			ExecutorService service = approachMap.get(i);
			if(service == null){
				service = executorService;
			}
			return service;
		} catch (Exception e) {
			logger.error("支付反馈消息.id={}",id);
			e.printStackTrace();
		}
		return executorService;
	}
	
	public static void  addApproachThread(Long i, ExecutorService executor){
		approachMap.put(i, executor);
	}
}
