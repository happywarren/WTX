/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.api.fund
 * FILE    NAME: IFundScoreService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.fund.service;

import java.io.Serializable;

import com.lt.model.fund.FundMainScore;
import com.lt.util.error.LTException;

/**
 * TODO 积分账户业务接口
 * @author XieZhibing
 * @date 2016年11月30日 下午3:42:26
 * @version <b>1.0.0</b>
 */
public interface IFundScoreService extends Serializable {
	/**
	 * 
	 * TODO 查询积分订单余额是否充足
	 * @author XieZhibing
	 * @date 2016年12月9日 下午3:02:05
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean isScoreBalanceEnough(String userId, double amount);
	/**
	 * 
	 * TODO 查询用户积分主账户
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:52:42
	 * @param userId
	 * @return
	 */
	public FundMainScore queryFundMainScore(String userId);
	
	/**
	 * 
	 * TODO 初始化积分主账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:12:55
	 * @param userId
	 * @return
	 */
	public boolean doInitFundScoreAccount(String userId) throws LTException;
	
	/**
	 * 
	 * TODO 查询积分主账余额是否充足
	 * @author XieZhibing
	 * @date 2016年11月30日 下午9:55:36
	 * @param userId	用户ID
	 * @param amount	购买时总费用 = (保证金 + 手续费 + 递延保证金 + 递延费) * 手数
	 * @return true 充足, false 不足
	 */
	public boolean doCheckFundMainBalance(String userId, double amount);
	
	/**
	 * 
	 * TODO 开仓扣款
	 * @author XieZhibing
	 * @date 2016年12月6日 下午1:34:00
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param actualCounterFee	总手续费
	 * @return  
	 */
	public boolean doBuy(String productName, String orderId, String userId,
			double holdFund, double deferFund, double actualCounterFee) throws LTException;
	
	/**
	 * 
	 * TODO 平仓结算更新积分账户
	 * @author XieZhibing
	 * @date 2016年12月7日 下午2:27:47
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param userBenefit	用户净利润
	 * @return
	 * @throws LTException
	 */
	public void doBalance(String productName, String orderId, String userId,
			double holdFund, double deferFund, double userBenefit) throws LTException;
	
	/**
	 * 
	 * TODO 开仓失败退款
	 * @author XieZhibing
	 * @date 2016年12月6日 下午1:34:00
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param actualCounterFee	总手续费
	 * @return  
	 */
	public void doRefund(String productName, String orderId, String userId,
			double holdFund, double deferFund, double actualCounterFee) throws LTException;
	/**
	 * 查询用户资金信息 for update
	 * @param userId
	 * @return
	 */
	public FundMainScore queryFundMainScoreForUpdate(String userId);
	
	/**
	 * 
	 * TODO 查询止损保证金流水汇总
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:22:05
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryHoldFundFlowSum(String orderId, String userId);
	
	/**
	 * 
	 * TODO 查询递延保证金流水汇总
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:22:36
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryDeferFundFlowSum(String orderId, String userId);
	
	/**
	 * 
	 * TODO 查询手续费流水汇总
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:22:43
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryActualCounterFeeFlowSum(String orderId, String userId);
	
	/**
	 * 递延结息
	 * @param userId
	 * @param productName
	 * @param orderId
	 * @param interest
	 */
	public void deductInterest(String userId, String productName, String orderId,
			double interest);
}
