/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service
 * FILE    NAME: IOrderSellTradeService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service;

import com.lt.model.trade.OrderScoreInfo;
import com.lt.util.error.LTException;
import com.lt.vo.trade.OrderVo;

import java.io.Serializable;

/**
 * TODO 订单卖出业务实现类
 * @author XieZhibing
 * @date 2016年12月12日 下午8:41:58
 * @version <b>1.0.0</b>
 */
public interface IOrderSellTradeService extends Serializable {

	/**
	 * 
	 * TODO 平仓
	 * @author XieZhibing
	 * @date 2016年12月12日 下午8:42:17
	 * @param orderVo
	 * @throws LTException
	 */
	public void doSell(OrderVo orderVo) throws LTException;	
	
	/**
	 * 
	 * TODO 批量平仓
	 * @author XieZhibing
	 * @date 2017年1月4日 上午10:21:45
	 * @param userId
	 * @param productCode
	 * @throws LTException
	 */
	public void doSell(String userId, String productCode) throws LTException;	
	
	/**
	 * 修改积分订单信息
	 * @param scoreOrdersInfo
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午8:29:29
	 */
	public void updateScoreOrder(OrderScoreInfo scoreOrdersInfo)throws LTException;
}
