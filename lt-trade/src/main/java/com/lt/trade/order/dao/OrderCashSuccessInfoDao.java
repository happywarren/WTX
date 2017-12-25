/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.dao
 * FILE    NAME: CashOrderTradDao.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.dao;

import java.io.Serializable;
import java.util.List;

import com.lt.model.trade.OrderCashSuccessInfo;

/**
 * TODO 成交记录数据接口
 * @author XieZhibing
 * @date 2016年12月12日 下午1:56:43
 * @version <b>1.0.0</b>
 */
public interface OrderCashSuccessInfoDao extends Serializable {

	/**
	 * 
	 * TODO 添加成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午1:59:25
	 * @param tradRecord
	 */
	public void add(OrderCashSuccessInfo tradRecord);
	
	/**
	 * 
	 * TODO 更新成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午1:59:36
	 * @param tradRecord
	 */
	public void update(OrderCashSuccessInfo tradRecord);
	
	/**
	 * 
	 * TODO 根据ID查询成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午12:02:51
	 * @param id
	 * @return
	 */
	public OrderCashSuccessInfo queryById(Long id);
	
	/**
	 * 
	 * TODO 根据报给交易所的委托编码查询成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午12:02:47
	 * @param entrustCode
	 * @return
	 */
	public List<OrderCashSuccessInfo> queryByEntrustCode(Integer entrustCode);

}
