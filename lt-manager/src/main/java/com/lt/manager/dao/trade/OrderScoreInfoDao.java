package com.lt.manager.dao.trade;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.trade.OrderScoreInfo;

public interface OrderScoreInfoDao {

	/**
	 * 查询订单--分页
	 * @param param
	 * @return
	 */
	public List<OrderParamVO> selectScoreTradeOrderPage(OrderParamVO param);
	
	/**
	 * 查询订单数量
	 * @param param
	 * @return
	 */
	public Integer selectScoreTradeOrderCount(OrderParamVO param);
	
	/**
	 * 查询订单汇总数据
	 * @param param
	 * @return
	 */
	public Map<String,String> selectScoreTradeOrderDateMap(OrderParamVO param);
	
	/**
	 * 查询订单详情
	 * @param id
	 * @return
	 */
	public OrderParamVO selectScoreOrderInfo(String id);
	
	/**
	 * 根据订单id查询买卖信息集合
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> selectScoreOrderBuyOrSaleList(String id);
	
	/**
	 * 查询单子信息
	 * @param param
	 * @return
	 */
	public OrderScoreInfo selectOrderScoreInfoOne(OrderParamVO param);
	
	/**
	 * 修改订单信息
	 * @param orderCashInfo
	 */
	public void updateScore(OrderScoreInfo orderScoreInfo);
	
	/**
	 * 修改卖出信息为null
	 * @param orderCashInfo
	 */
	public void updateScoreSellInfoForNull(String orderId);
}
