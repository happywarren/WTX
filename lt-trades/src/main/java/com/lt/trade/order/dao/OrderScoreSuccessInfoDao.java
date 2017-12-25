/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.dao
 * FILE    NAME: ScoreOrderTradDao.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.dao;

import com.lt.model.trade.OrderScoreSuccessInfo;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 成交记录数据接口
 * @author XieZhibing
 * @date 2016年12月12日 下午1:56:43
 * @version <b>1.0.0</b>
 */
public interface OrderScoreSuccessInfoDao extends Serializable {

	/**
	 * 
	 * TODO 添加成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午1:59:25
	 * @param tradRecord
	 */
	public void add(OrderScoreSuccessInfo tradRecord);
	
	/**
	 * 
	 * TODO 更新成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午1:59:36
	 * @param tradRecord
	 */
	public void update(OrderScoreSuccessInfo tradRecord);
	
	/**
	 * 
	 * TODO 根据ID查询成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午12:02:51
	 * @param id
	 * @return
	 */
	public OrderScoreSuccessInfo queryById(Long id);
	
	/**
	 * 
	 * TODO 根据报给交易所的委托编码查询成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午12:02:47
	 * @param entrustCode
	 * @return
	 */
	public List<OrderScoreSuccessInfo> queryByEntrustCode(Integer entrustCode);

}
