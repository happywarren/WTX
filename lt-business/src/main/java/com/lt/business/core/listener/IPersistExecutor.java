package com.lt.business.core.listener;


import java.io.Serializable;

import com.lt.model.dispatcher.enums.RedisQuotaObject;

/**
 * TODO 持久化执行器
 * @version <b>1.0.0</b>
 */
public interface IPersistExecutor extends Serializable {

	/**
	 * 
	 * TODO 添加消息对象
	 * @return
	 */
	public boolean put(RedisQuotaObject obj);
	
	/**
	 * 
	 * TODO 执行消息解析
	 */
	public void execute();
	
}
