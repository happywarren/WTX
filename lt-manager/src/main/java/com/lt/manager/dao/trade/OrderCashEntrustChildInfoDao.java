package com.lt.manager.dao.trade;

import java.util.List;

import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.trade.OrderCashEntrustInfo;

/**
 * 委托记录dao
 * @author jingwb
 *
 */
public interface OrderCashEntrustChildInfoDao {

	/**
	 * 查询委托记录--分页
	 * @param param
	 * @return
	 */
	public List<OrderParamVO> selectEntrustTradeOrderPage(OrderParamVO param);
	
	/**
	 * 查询委托数量
	 * @param param
	 * @return
	 */
	public Integer selectEntrustTradeOrderCount(OrderParamVO param);
	
	/**
	 * 查询委托详情
	 * @param id
	 * @return
	 */
	public OrderParamVO selectCashEntrustOrderInfo(Integer id);
	
	/**
	 * 查询委托信息
	 * @param param
	 * @return
	 */
	public OrderCashEntrustInfo selectEntrustInfoOne(OrderParamVO param);
	
	public void add(OrderCashEntrustInfo orderCashEntrustInfo);
	
	public void update(OrderCashEntrustInfo orderCashEntrustInfo);
}
