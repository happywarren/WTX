package com.lt.manager.dao.trade;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.trade.OrderCashInfo;

/**
 * 订单信息dao
 * @author jingwb
 *
 */
public interface OrderCashInfoDao {
	
	/**
	 * 查询订单--分页
	 * @param param
	 * @return
	 */
	public List<OrderParamVO> selectTradeOrderPage(OrderParamVO param);
	
	/**
	 * 查询订单数量
	 * @param param
	 * @return
	 */
	public Integer selectTradeOrderCount(OrderParamVO param);
	
	/**
	 * 查询订单汇总数据
	 * @param param
	 * @return
	 */
	public Map<String,String> selectTradeOrderDateMap(OrderParamVO param);
	
	/**
	 * 查询订单详情
	 * @param id
	 * @return
	 */
	public OrderParamVO selectCashOrderInfo(String id);
	
	/**
	 * 根据订单id查询买卖信息集合
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> selectOrderBuyOrSaleList(String id);
	
	/**
	 * 查询单子信息
	 * @param param
	 * @return
	 */
	public OrderCashInfo selectOrderCashInfoOne(OrderParamVO param);
	
	/**
	 * 修改订单信息
	 * @param orderCashInfo
	 */
	public void update(OrderCashInfo orderCashInfo);
	
	/**
	 * 修改卖出信息为null
	 * @param orderCashInfo
	 */
	public void updateSellInfoForNull(String orderId);
}
