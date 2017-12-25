package com.lt.manager.dao.trade;


import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.trade.OrderScoreSuccessInfo;

import java.util.List;

/**
 * 成交订单dao
 * @author jingwb
 *
 */
public interface OrderScoreSuccessInfoDao {
	
	/**
	 * 添加成交记录
	 * @param orderScoreSuccessInfo
	 */
	public void addScore(OrderScoreSuccessInfo orderScoreSuccessInfo);

	/**
	 * 查询成交记录--分页
	 * @param param
	 * @return
	 */
	public List<OrderParamVO> selectScoreSuccessTradeOrderPage(OrderParamVO param);

	/**
	 * 查询成交数
	 * @param param
	 * @return
	 */
	public Integer selectScoreSuccessTradeOrderCount(OrderParamVO param);

	/**
	 * 查询成交订单详情
	 * @param id
	 * @return
	 */
	public OrderParamVO selectScoreSuccOrderInfo(Integer id);
}
