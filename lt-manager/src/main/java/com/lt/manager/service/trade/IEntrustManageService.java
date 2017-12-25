package com.lt.manager.service.trade;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;

/**
 * 现金订单交易接口
 * @author jingwb
 *
 */
public interface IEntrustManageService {
	
	/**
	 * 查询委托记录--分页 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<OrderParamVO> queryEntrustTradeOrderPage(OrderParamVO param) throws Exception;
	
	
	/**
	 * 获取委托详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OrderParamVO getCashEntrustOrderInfo(Integer id) throws Exception;
	
	
	/**
	 * 查询委托信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public OrderCashEntrustInfo queryEntrustInfo(OrderParamVO param) throws Exception;
	
}
