package com.lt.manager.dao.trade;

import java.util.List;

import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.trade.OrderCashSuccessInfo;

/**
 * 成交订单dao
 * @author jingwb
 *
 */
public interface OrderCashSuccessInfoDao {

	/**
	 * 查询成交记录--分页
	 * @param param
	 * @return
	 */
	public List<OrderParamVO> selectSuccessTradeOrderPage(OrderParamVO param);
	
	/**
	 * 查询成交数
	 * @param param
	 * @return
	 */
	public Integer selectSuccessTradeOrderCount(OrderParamVO param);
	
	/**
	 * 查询成交订单详情
	 * @param id
	 * @return
	 */
	public OrderParamVO selectCashSuccOrderInfo(Integer id);
	
	/**
	 * 添加成交记录
	 * @param orderCashSuccessInfo
	 */
	public void add(OrderCashSuccessInfo orderCashSuccessInfo);
}
