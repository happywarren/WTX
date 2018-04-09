package com.lt.fund.service;

import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundMainCash;
import com.lt.vo.fund.FundFlowVo;

public interface IFundFlowCashService {
	
	
	/**
	 * 订单冻结解冻流水值相加的结果
	 * @param orderId
	 * @param userId
	 * @param list
	 * @return
	 */
	Double findFreezeFundFlowCashSum(String orderId, String userId,
			List<String> list);
	
	/**
	 * 递延冻结扣款
	 * @param deferFund 递延保证金
	 * @param orderId 订单ID
	 * @param userId 用户ID
	 * @param productName
	 * @return
	 */
	boolean doDeferFundDeductions(double deferFund,
			String orderId, String userId, String productName);
	
	/**
	 * 递延解冻退款
	 * @param deferFund 递延保证金
	 * @param orderId 订单ID
	 * @param userId 用户ID
	 * @param productName 商品名称
	 * @return
	 */
	boolean doDeferFundRefund(double deferFund,
			String orderId, String userId, String productName);

	/**
	 * 解冻止盈止损保证金
	 * @param userFundMainCash 
	 * @param investorId 投资人ID
	 * @param userId    用户ID
	 * @param holdFund  止损保证金
	 * @param orderId  订单ID
	 * @param productName 商品名称
	 * @return 
	 */
	boolean doUnfreezeProfitLoss( String userId,
			double holdFund, String orderId,	String productName);

	/**
	 * 冻结止盈止损保证金
	 * @param userFundMainCash 
	 * @param investorId 投资人ID
	 * @param userId    用户ID
	 * @param holdFund  止损保证金
	 * @param orderId  订单ID
	 * @param productName 商品名称
	 * @return
	 */
	boolean doFreezeProfitLoss( String userId,
			double holdFund, String orderId,
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
	
	/**
	 * 根据用户id查询当日最大充值剩余金额
	 * @param userId
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月10日 下午1:15:40
	 */
	public Double getUserDailyRestReChargeAmt(String userId);
	
	/**
	 * 获取该用户今年剩余可使用平安银行充值金额
	 * @param userId
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午7:13:04
	 */
	public Double getPingAnRechargeAmtInYear(String userId);
	
	/**
	 * 根据订单id查询冻结保证金
	 * @param orderId
	 * @return
	 */
	public Double getHoldFundByOrderId(String orderId);

	/**
	 * 分页查询充值记录
	 * @param params
	 * @return
	 */
	public List<FundIoCashRecharge> findRechargeByUserId(Map<String,Object> params);

}
