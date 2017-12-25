/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.vo.fund
 * FILE    NAME: FundOrderVo.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.vo.fund;

import java.io.Serializable;

import com.lt.enums.fund.FundTypeEnum;

/**
 * TODO 资金订单结算参数, 用于开仓扣款、平仓结算、开仓失败退款
 * @author XieZhibing
 * @date 2016年12月7日 下午7:39:01
 * @version <b>1.0.0</b>
 */
public class FundOrderVo implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -6118075934283506969L;
	
	/** 资金类型 */
	private FundTypeEnum fundType;
	/** 商品名称 */
	private String productName;
	/** 订单ID */
	private String orderId;
	/** 用户ID */
	private String userId;
	/** 止损保证金 */
	private double holdFund;
	/** 递延保证金 */
	private double deferFund;
	/** 手续费 */
	private double actualCounterFee;
	/** 用户净利润*/
	private double userBenefit;	
	/** 止盈保证金 */
	private double stopProfit;
	/** 券商用户ID */
	private String investorId;
	/** 时间戳 */
	private long timestamp;
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月7日 下午7:47:16
	 */
	public FundOrderVo() {
		super();
	}
	
	/**
	 * 
	 * 构造 扣款或退款使用
	 * @author XieZhibing
	 * @date 2017年2月9日 下午8:09:02
	 * @param fundType
	 * @param productName
	 * @param orderId
	 * @param userId
	 * @param holdFund
	 * @param deferFund
	 * @param actualCounterFee
	 */
	public FundOrderVo(FundTypeEnum fundType, String productName, String orderId, 
			String userId, double holdFund, double deferFund, double actualCounterFee) {
		super();
		this.fundType = fundType;
		this.productName = productName;
		this.orderId = orderId;
		this.userId = userId;
		this.holdFund = holdFund;
		this.deferFund = deferFund;
		this.actualCounterFee = actualCounterFee;
	}
	
	/**
	 * 构造 结算使用
	 * @author XieZhibing
	 * @date 2016年12月13日 下午7:00:34
	 * @param fundType
	 * @param productName
	 * @param orderId
	 * @param userId
	 * @param holdFund
	 * @param deferFund
	 * @param userBenefit
	 * @param investorId
	 * @param stopProfit
	 * @param investorBenefit
	 * @param ratio
	 */
	public FundOrderVo(FundTypeEnum fundType, String productName,
			String orderId, String userId, double holdFund, double deferFund,
			double userBenefit, String investorId) {
		super();
		this.fundType = fundType;
		this.productName = productName;
		this.orderId = orderId;
		this.userId = userId;
		this.holdFund = holdFund;
		this.deferFund = deferFund;
		this.userBenefit = userBenefit;
		this.investorId = investorId;
	}
	
	/** 
	 * 获取 资金类型 
	 * @return fundType 
	 */
	public FundTypeEnum getFundType() {
		return fundType;
	}

	/** 
	 * 设置 资金类型 
	 * @param fundType 资金类型 
	 */
	public void setFundType(FundTypeEnum fundType) {
		this.fundType = fundType;
	}

	/** 
	 * 获取 商品名称 
	 * @return productName 
	 */
	public String getProductName() {
		return productName;
	}
	/** 
	 * 设置 商品名称 
	 * @param productName 商品名称 
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/** 
	 * 获取 订单ID 
	 * @return orderId 
	 */
	public String getOrderId() {
		return orderId;
	}
	/** 
	 * 设置 订单ID 
	 * @param orderId 订单ID 
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** 
	 * 获取 止损保证金 
	 * @return holdFund 
	 */
	public double getHoldFund() {
		return holdFund;
	}
	/** 
	 * 设置 止损保证金 
	 * @param holdFund 止损保证金 
	 */
	public void setHoldFund(double holdFund) {
		this.holdFund = holdFund;
	}
	/** 
	 * 获取 递延保证金 
	 * @return deferFund 
	 */
	public double getDeferFund() {
		return deferFund;
	}
	/** 
	 * 设置 递延保证金 
	 * @param deferFund 递延保证金 
	 */
	public void setDeferFund(double deferFund) {
		this.deferFund = deferFund;
	}
	
	/** 
	 * 获取 用户净利润 
	 * @return userBenefit 
	 */
	public double getUserBenefit() {
		return userBenefit;
	}

	/** 
	 * 设置 用户净利润 
	 * @param userBenefit 用户净利润 
	 */
	public void setUserBenefit(double userBenefit) {
		this.userBenefit = userBenefit;
	}

	/** 
	 * 获取 止盈保证金 
	 * @return stopProfit 
	 */
	public double getStopProfit() {
		return stopProfit;
	}

	/** 
	 * 设置 止盈保证金 
	 * @param stopProfit 止盈保证金 
	 */
	public void setStopProfit(double stopProfit) {
		this.stopProfit = stopProfit;
	}


	public String getInvestorId() {
		return investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	/** 
	 * 获取 手续费 
	 * @return actualCounterFee 
	 */
	public double getActualCounterFee() {
		return actualCounterFee;
	}

	/** 
	 * 设置 手续费 
	 * @param actualCounterFee 手续费 
	 */
	public void setActualCounterFee(double actualCounterFee) {
		this.actualCounterFee = actualCounterFee;
	}

	/** 
	 * 获取 时间戳 
	 * @return timestamp 
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/** 
	 * 设置 时间戳 
	 * @param timestamp 时间戳 
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月13日 下午6:02:26
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundOrderVo [fundType=").append(fundType)
				.append(", productName=").append(productName)
				.append(", orderId=").append(orderId).append(", userId=")
				.append(userId).append(", holdFund=").append(holdFund)
				.append(", deferFund=").append(deferFund)
				.append(", actualCounterFee=").append(actualCounterFee)
				.append(", userBenefit=").append(userBenefit)
				.append(", investorId=").append(investorId)
				.append(", timestamp=").append(timestamp).append("]");
		return builder.toString();
	}
	
}
