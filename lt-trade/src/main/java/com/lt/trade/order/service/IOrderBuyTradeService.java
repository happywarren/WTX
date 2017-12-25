/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.rmi
 * FILE    NAME: IOrderBuyTradeService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service;

import java.io.Serializable;

import com.lt.util.error.LTException;
import com.lt.vo.trade.OrderVo;

/**
 * TODO 订单买入业务接口
 * @author XieZhibing
 * @date 2016年12月8日 下午8:55:52
 * @version <b>1.0.0</b>
 */
public interface IOrderBuyTradeService extends Serializable {

	/**
	 * 
	 * TODO 开仓买入
	 * @author XieZhibing
	 * @date 2016年12月8日 下午8:58:41
	 * @param orderVo
	 * @throws LTException
	 */
	public void doBuy(OrderVo orderVo) throws LTException;	
	
}
