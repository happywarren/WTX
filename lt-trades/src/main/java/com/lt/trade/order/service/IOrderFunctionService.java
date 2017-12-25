package com.lt.trade.order.service;

import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.util.error.LTException;
import com.lt.vo.product.ProductVo;

/**   
* 项目名称：lt-trade   
* 类名称：IScoreOrderFunctionService
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年12月15日 下午8:26:56      
*/
public interface IOrderFunctionService {
	

	/**
	 * 根据订单id查询订单信息
	 * @param orderId 订单di
	 * @return    
	 * @return:       ScoreOrdersInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月15日 下午8:29:53
	 */
	public OrderScoreInfo getScoreOrderInfoById(String orderId) throws LTException;
	

	/**
	 * 修改订单信息
	 * @param  支持修改条件 order_id 支持修改的字段 止盈金额，止损金额，止盈价格，止损价格，递延状态，递延保证金，系统清仓时间
	 * @return:       void
	 * @throws
	 * @author        yuanxin
	 * @Date          2016年12月16日 上午9:19:42
	 */
	public void updateScoreOrderInfo(OrderScoreInfo scoreOrdersInfo) throws LTException;


	/**
	 * 设置订单的止盈(积分)
	 * @param stopProfit
	 * @param newOrder
	 * @param oldOrder
	 * @param feeCfg
	 * @param productVo
	 * @throws LTException
	 * @return:       void
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年2月17日 下午3:14:51
	 */
	public void setScoreOrderStopProfit(Double stopProfit, OrderScoreInfo newOrder, OrderScoreInfo oldOrder, InvestorFeeCfg feeCfg, ProductVo productVo) throws LTException;


	/**
	 * 设置订单的止损(积分)
	 * @param stopLoss
	 * @param newOrder
	 * @param oldOrder
	 * @param feeCfg
	 * @param productVo
	 * @param defstatu
	 * @param mainScore
	 * @param scoreAmt 浮动盈亏
	 * @throws LTException
	 * @return:       void
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年2月17日 下午3:08:30
	 */
	public void setScoreOrderStopLoss(Double stopLoss, OrderScoreInfo newOrder, OrderScoreInfo oldOrder, InvestorFeeCfg feeCfg, ProductVo productVo, Integer defstatu, Double mainScore, Double scoreAmt) throws LTException;


	/**
	 * 设置订单的递延状态（积分）
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
	public void setScoreOrderDef(Integer defstatu, OrderScoreInfo newOrder, OrderScoreInfo oldOrder, InvestorFeeCfg feeCfg, ProductVo productVo, Double mainScore) throws LTException;

	/**
	 * 修改移动止损状态（风控参数 积分）
	 * @param riskBean
	 * @param orderInfo
	 * @param opera 本次操作内容
	 * @throws LTException
	 * @return:       void
	 * @throws
	 * @author        yuanxin
	 * @Date          2017年2月17日 下午1:30:02
	 */
	public void setScoreRiskBeanTrailLoss(RiskControlBean riskBean, OrderScoreInfo orderInfo, String opera, Integer trailStopLoss) throws LTException;


    /**
     * 返回修改止盈止损，递延的操作内容
     * @param investorFeeCfg
     * @param
     * @param deferStatu 递延
     * @param stopLoss 止损
     * @param stopfit 止盈
     * @return    int    0，不做任何修改 1，修改止盈，2修改止损，3修改递延，12，修改止盈止损，13修改止盈，修改递延，23修改止损，修改递延 ，123修改止盈止损，并修改递延
     * @throws
     * @author        yuanxin
     * @Date          2016年12月15日 下午8:56:44
     */
    public int checkIfDeforLossAndProfit(InvestorFeeCfg investorFeeCfg,OrderScoreInfo scoreOrdersInfo, String deferStatu, String stopLoss,
                                         String stopfit, String trailStopLoss, Integer fundType, OrderLossProfitDefLog log) throws LTException;



}
