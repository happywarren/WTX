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
public interface ITradeCashManageService {
	
	/**
	 * 查询订单--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<OrderParamVO> queryTradeOrderPage(OrderParamVO param) throws Exception;
	
	/**
	 * 查询订单数据汇总
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> queryTradeOrderDateMap(OrderParamVO param) throws Exception;
	
	/**
	 * 查询委托记录--分页 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<OrderParamVO> queryEntrustTradeOrderPage(OrderParamVO param) throws Exception;
	
	/**
	 * 查询成交记录--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<OrderParamVO> querySuccessTradeOrderPage(OrderParamVO param) throws Exception;
	
	/**
	 * 查询订单详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OrderParamVO getCashOrderInfo(String id) throws Exception;
	
	/**
	 * 查询订单买卖信息集合
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getOrderBuyOrSaleList(String id) throws Exception;
	
	/**
	 * 获取委托详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OrderParamVO getCashEntrustOrderInfo(Integer id) throws Exception;
	
	/**
	 * 获取成交单详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OrderParamVO getCashSuccOrderInfo(Integer id) throws Exception;
	
	/**
	 * 查询委托信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public OrderCashEntrustInfo queryEntrustInfo(OrderParamVO param) throws Exception;
	
	/**
	 * 查询订单信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public OrderCashInfo queryOrderInfo(OrderParamVO param) throws Exception;
	
	/**
	 * 强制成功
	 * @param param
	 * @throws Exception
	 */
	public void forceSuccess(OrderParamVO param) throws Exception;

	/**
	 * 强制失败
	 * @param param
	 * @throws Exception
	 */
	public void forceFail(OrderParamVO param) throws Exception;
	
	/**
	 * 强制平仓
	 * @param param
	 * @throws Exception
	 */
	public void forceSell(OrderParamVO param) throws Exception;

	/**
	 * 强制成功/强制失败/强制平仓-多选
	 * @param param
	 * @param type 操作类型 Success:强制成功 Fail:强制失败 Shell:强制平仓
	 * @throws Exception
	 */
	public void forceTypeMulti(OrderParamVO param, String type) throws Exception;
}
