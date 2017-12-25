package com.lt.api.dispatcher;

import com.lt.model.dispatcher.enums.RedisQuotaObject;

import java.util.List;

public interface IDispatcherApiService {

	/**
	 * 发送短信
	 * @param msg
	 * @param userId
	 */
	void sendMsg(String msg, String userId);

	
	/**
	 * 批量注册   String = productCode
	 * @param list
	 */
	void registerChannel(List<String> list,String userId);


	/**
	 * 是否在线
	 * @param userId
	 * @return
	 */
	boolean onLine(String userId);

	// 获取行情数据(主要供h5端)
	RedisQuotaObject getQuoteData(String productName);


	/**
	 * 获取在线用户数
	 * @param 
	 * @return
	 */
	Integer onLineNumber();
	
}
