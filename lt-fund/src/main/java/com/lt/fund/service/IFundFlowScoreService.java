package com.lt.fund.service;

import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundMainScore;
import com.lt.vo.fund.FundFlowVo;

public interface IFundFlowScoreService {
	
	
	/**
	 * 订单冻结解冻流水值相加的结果
	 * @param orderId
	 * @param userId
	 * @param list
	 * @return
	 */
	Double findFreezeFundFlowScoreSum(String orderId, String userId,
			List<String> list);
	
	/**
	 * 递延冻结扣款
	 * @param userFundMainScore 
	 * @param deferFund 递延保证金
	 * @param orderId 订单ID
	 * @param userId 用户ID
	 * @param productName
	 * @return
	 */
	boolean doDeferFundDeductions(FundMainScore userFundMainScore, double deferFund,
			String orderId, String userId, String productName);
	
	/**
	 * 递延解冻退款
	 * @param userFundMainScore 
	 * @param deferFund 递延保证金
	 * @param orderId 订单ID
	 * @param userId 用户ID
	 * @param productName 商品名称
	 * @return
	 */
	boolean doDeferFundRefund(FundMainScore userFundMainScore, double deferFund,
			String orderId, String userId, String productName);

	/**
	 * 解冻止盈止损保证金
	 * @param investorId 投资人ID
	 * @param userId    用户ID
	 * @param holdFund  止损保证金
	 * @param stopProfit 止盈保证金
	 * @param orderId  订单ID
	 * @param productName 商品名称
	 * @return
	 */
	boolean doUnfreezeProfitLoss(FundMainScore userFundMainScore,String investorId, String userId,
			double holdFund, double stopProfit, String orderId,
			String productName);

	/**
	 * 冻结止盈止损保证金
	 * @param userFundMainScore 
	 * @param investorId
	 * @param userId
	 * @param holdFund
	 * @param stopProfit
	 * @param orderId
	 * @param productName
	 * @return
	 */
	boolean doFreezeProfitLoss(FundMainScore userFundMainScore, String investorId, String userId,
			double holdFund, double stopProfit, String orderId,
			String productName);

	/**
	 * 查询用户资金明细
	 * @param map
	 * @return
	 */
	List<FundFlowVo> findFundFollowByUserId(Map<String, Object> map);


	/**
	 * 查询用户资金明细
	 * @param map
	 * @return
	 */
	List<FundFlowVo> findFundFollowByOrder(Map<String, Object> map);
}
