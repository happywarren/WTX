package com.lt.trade.order.service;

import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.util.error.LTException;
import com.lt.vo.product.ProductVo;

/**   
* 项目名称：lt-trade   
* 类名称：ICashOrderFunctionService   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年12月15日 下午8:26:56      
*/
public interface IOrderFunctionService {
	
	/**
	 * 根据订单id查询订单信息
	 * @param orderId 订单di
	 * @return    
	 * @return:       CashOrdersInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月15日 下午8:29:53
	 */
	public OrderCashInfo getCashOrderInfoById(String orderId) throws LTException;
	

	/**
	 * 返回修改止盈止损，递延的操作内容
	 * @param investorFeeCfg
	 * @param cashOrdersInfo
	 * @param deferStatu 递延
	 * @param stopLoss 止损
	 * @param stopfit 止盈
	 * @return    int    0，不做任何修改 1，修改止盈，2修改止损，3修改递延，12，修改止盈止损，13修改止盈，修改递延，23修改止损，修改递延 ，123修改止盈止损，并修改递延    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月15日 下午8:56:44
	 */
	public int checkIfDeforLossAndProfit(InvestorFeeCfg investorFeeCfg,OrderCashInfo cashOrdersInfo,String deferStatu, String stopLoss,
			String stopfit,String trailStopLoss,Integer fundType,OrderLossProfitDefLog log) throws LTException;
	
	/**
	 * 修改订单信息
	 * @param cashOrdersInfo 支持修改条件 order_id 支持修改的字段 止盈金额，止损金额，止盈价格，止损价格，递延状态，递延保证金，系统清仓时间    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月16日 上午9:19:42
	 */
	public int updateCashOrderInfo(OrderCashInfo cashOrdersInfo) throws LTException;
	
	public int updateCashOrderInfo(OrderCashInfo cashOrdersInfo,Double hf) throws LTException;
	
	/**
	 * 设置订单的止盈(现金)
	 * @param stopProfit 新的止盈值
	 * @param newOrder 新订单信息
	 * @param oldOrder 老订单信息
	 * @param feeCfg 费用配置
	 * @param productVo 产品信息
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月17日 上午11:36:25
	 */
	public void setCashOrderStopProfit(Double stopProfit,OrderCashInfo newOrder,OrderCashInfo oldOrder,InvestorFeeCfg feeCfg,ProductVo productVo) throws LTException;
	
	/**
	 * 设置订单的止损(现金)
	 * @param stopLoss 止损
	 * @param newOrder 新订单信息
	 * @param oldOrder 老订单信息
	 * @param feeCfg 费用配置
	 * @param productVo 产品信息
	 * @param defstatu 递延状态
	 * @param mainCash 主账户值
	 * @throws LTException
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月6日 上午11:32:16
	 */
	public void setCashOrderStopLoss(Double stopLoss,OrderCashInfo newOrder,OrderCashInfo oldOrder,InvestorFeeCfg feeCfg,ProductVo productVo,Integer defstatu,Double mainCash,Double floatAmt) throws LTException;
	
	/**
	 * 设置订单的递延状态（现金）
	 * @param defstatu 递延状态
	 * @param newOrder 新订单
	 * @param oldOrder 老订单
	 * @param feeCfg 费用配置
	 * @param productVo 产品信息
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月17日 上午11:58:59
	 */
	public void setCashOrderDef(Integer defstatu,OrderCashInfo newOrder,OrderCashInfo oldOrder,InvestorFeeCfg feeCfg,ProductVo productVo,Double mainCash) throws LTException;
	
	/**
	 * 修改移动止损状态（风控参数 现金）
	 * @param riskBean
	 * @param orderInfo
	 * @param opera 本次操作内容
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月17日 下午1:30:02
	 */
	public void setCashRiskBeanTrailLoss(RiskControlBean riskBean,OrderCashInfo orderInfo,String opera,Integer trailStopLoss) throws LTException;

}
