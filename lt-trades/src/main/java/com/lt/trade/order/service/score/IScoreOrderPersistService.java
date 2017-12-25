/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service
 * FILE    NAME: IScoreOrderPersistService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.score;

import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchBean;

import java.io.Serializable;

/**
 * TODO 现金订单数据持久化业务接口
 * @author XieZhibing
 * @date 2016年12月11日 下午8:44:23
 * @version <b>1.0.0</b>
 */
public interface IScoreOrderPersistService extends Serializable {

	/**
	 * 
	 * TODO 持久化现金订单、持久化委托单
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:48:13
	 * @param matchBean
	 */
	public void doPersist2012(FutureErrorBean errorBean);
	
	/**
	 * 
	 * TODO 更新失败数量、失败原因
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:49:49
	 * @param errorBean
	 */
	public void doPersist2002(FutureErrorBean errorBean);
	
	/**
	 * 
	 * TODO 更新订单持仓等数量
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:50:21
	 * @param matchBean
	 */
	public void doPersist2003(FutureMatchBean matchBean);
	
	/**
	 * 
	 * TODO 订单完全成交
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:50:44
	 * @param matchBean
	 */
	public void doPersist2004(FutureMatchBean matchBean);
}
