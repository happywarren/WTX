/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.executor
 * FILE    NAME: IPersistExecutor.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.executor;

import com.lt.trade.tradeserver.bean.FutureMatchWrapper;

import java.io.Serializable;

/**
 * TODO 持久化执行器
 * @author XieZhibing
 * @date 2016年12月11日 下午8:29:58
 * @version <b>1.0.0</b>
 */
public interface IPersistExecutor extends Serializable {

	/**
	 * 
	 * TODO 添加消息对象
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:32:48
	 * @param futureMatchWrapper
	 * @return
	 */
	public boolean put(FutureMatchWrapper futureMatchWrapper);
	
	/**
	 * 
	 * TODO 执行消息解析
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:33:22
	 */
	public void execute();
	
}
