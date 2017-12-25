package com.lt.manager.dao.trade;

import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.trade.OrderScoreEntrustInfo;

import java.util.List;

/**
 * 委托记录dao
 * @author jingwb
 *
 */
public interface OrderScoreEntrustInfoDao {

	/**
	 * 查询委托记录--分页
	 * @param param
	 * @return
	 */
	public List<OrderParamVO> selectScoreEntrustTradeOrderPage(OrderParamVO param);

	/**
	 * 查询委托数量
	 * @param param
	 * @return
	 */
	public Integer selectScoreEntrustTradeOrderCount(OrderParamVO param);

	/**
	 * 查询委托详情
	 * @param id
	 * @return
	 */
	public OrderParamVO selectScoreEntrustOrderInfo(Integer id);

	/**
	 * 查询委托信息
	 * @param param
	 * @return
	 */
	public OrderScoreEntrustInfo selectScoreEntrustInfoOne(OrderParamVO param);
	
	public void add(OrderScoreEntrustInfo orderScoreEntrustInfo);
	
	public void update(OrderScoreEntrustInfo orderScoreEntrustInfo);
}
