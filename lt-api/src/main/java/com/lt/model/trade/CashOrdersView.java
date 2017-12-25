/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.model.trade
 * FILE    NAME: OrderView.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.model.trade;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO 现金订单视图类
 * @author XieZhibing
 * @date 2017年1月3日 下午8:35:42
 * @version <b>1.0.0</b>
 */
public class CashOrdersView implements Serializable {
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 5329752537405277259L;
	/** 订单持仓数量信息ID */
	private Long id;
	/** 订单主信息ID */
	private Long orderId;
	/** 订单显示ID */
	private String displayId;
	
	/** 用户ID */
	private String userId;
	/** 券商ID */
	private String investorId;
	/** 交易类型: 1 买多, 2 买空, 3 卖多, 4 卖空  */
	private Integer tradeType;
	/** 商品ID */
	private Integer productId;
	/** 商品编码: 如CL1701 */
	private String productCode;
	/** 商品名称: 如美原油 */
	private String productName;
	/** 交易所编码 */
	private String exchangeCode;
	/** 内外盘标识：0 内盘, 1 外盘 */
	private Integer plate;
	/** 证券帐号 */
	private String securityCode;
	/** 止损保证金=止损值+保证金参数(运营费用) */
	private Double holdFund;
	/** 应扣手续费（优惠后，但使用优惠券前的金额） */
	private Double shouldCounterFee;
	/** 实际手续费（实际扣除手续费，使用优惠券后的金额） */
	private Double actualCounterFee;
	/** 平台手续费抽成 */
	private Double platformFee;
	/** 券商手续费抽成  */
	private Double investorFee;	
	/** 结算盈亏 */
	private Double lossProfit;
	/** 触发止损金额 */
	private Double stopLoss;
	/** 触发止盈金额 */
	private Double stopProfit;
	/** 止损价格 */
	private Double stopLossPrice;
	/** 止盈价 */
	private Double stopProfitPrice;
	/** 数量（手数） */
	private Integer count;
	/** 单位 */
	private String unit;
	/** 用户提交买入价 */
	private Double userCommitBuyPrice;
	/** 用提交买入时间 */
	private Date userCommitBuyDate;
	/** 系统提交买入价 */
	private Double sysCommitBuyPrice;
	/** 系统提交买入时间 */
	private Date sysCommitBuyDate;
	/** 订单创建时间 */
	private Date createDate;
	/** 订单修改时间 */
	private Date modifyDate;
	/** 用户提交卖出价 */
	private Double userCommitSalePrice;
	/** 用户提交卖出时间 */
	private Date userCommitSaleDate;
	/** 系统提交卖出价 */
	private Double sysCommitSalePrice;
	/** 系统提交卖出时间 */
	private Date sysCommitSaleDate;
	/** 结算类型: 1.定时清仓, 2.人工结算, 3.风控平仓, 4.用户结算, 5.策略平仓 */
	private Integer balanceType;
	/** 结算员工ID */
	private Integer balanceStaffId;
	/** 结算员工名称 */
	private String balanceStaffName;
	/** 结算时间 */
	private Date balanceDate;
	/** 系统清仓时间 */
	private Date sysSetSaleDate;
	/** 系统结算收益(卖的差价收益) */
	private Double sysLossProfit;
	/** 币种 */
	private String currency;
	/** 汇率(人民币汇率) */
	private Double rate;
	/** 持仓递延状态（1:递延; 0:非递延） */
	private Integer deferStatus;
	/** 递延保证金 */
	private Double deferFund;
	/** 递延利息 */
	private Double deferInterest;
	/** 递延次数 */
	private Integer deferTimes;
	/** 移动止损开关: 0.关; 1.开 */
	private Integer trailStopLoss;
	/** 显示状态: 0 隐藏; 1 显示 */
	private Integer display = 1;
	/** 订单状态：1.委托中; 2.持仓中; 3.失败; 4.完成; */
	private Integer status;
	
	/** 提交买入手数 */
	private Integer buyCommitCount;
	/** 开仓委托手数 */
	private Integer buyEntrustCount;
	/** 开仓成功手数 */
	private Integer buySuccCount;
	/** 开仓失败手数 */
	private Integer buyFailCount;
	/** 持仓手数 */
	private Integer holdCount;
	/** 平仓委托手数 */
	private Integer sellEntrustCount;
	/** 平仓处理中数量 */
	private Integer sellDealCount;
	/** 平仓成功手数 */
	private Integer sellSuccCount;
	/** 开仓成交均价 */
	private Double sysBuyAvgPrice;
	/** 开仓成交时间(毫秒级) */
	private String sysLastBuyDate;
	/** 平仓成交均价 */
	private Double sysSellAvgPrice;
	/** 平仓成功时间(毫秒级) */
	private String sysLastSellDate;
	/** 每手止损保证金参数(原币种) */
	private Double perSurcharge;
	/** 每手止损金额(原币种) */
	private Double perStopLoss;	
	/** 每手止盈金额(原币种) */
	private Double perStopProfit;
	/** 每手手续费(原币种) */
	private Double perCounterFee;
	/** 每手递延保证金(原币种) */
	private Double perDeferFund;
	/** 每手递延利息(原币种) */
	private Double perDeferInterest;
	/** 
	 * 获取 订单持仓数量信息ID 
	 * @return id 
	 */
	public Long getId() {
		return id;
	}
	/** 
	 * 设置 订单持仓数量信息ID 
	 * @param id 订单持仓数量信息ID 
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/** 
	 * 获取 订单主信息ID 
	 * @return orderId 
	 */
	public Long getOrderId() {
		return orderId;
	}
	/** 
	 * 设置 订单主信息ID 
	 * @param orderId 订单主信息ID 
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/** 
	 * 获取 订单显示ID 
	 * @return displayId 
	 */
	public String getDisplayId() {
		return displayId;
	}
	/** 
	 * 设置 订单显示ID 
	 * @param displayId 订单显示ID 
	 */
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}
	/** 
	 * 获取 用户ID 
	 * @return userId 
	 */
	public String getUserId() {
		return userId;
	}
	/** 
	 * 设置 用户ID 
	 * @param userId 用户ID 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/** 
	 * 获取 券商ID 
	 * @return investorId 
	 */
	public String getInvestorId() {
		return investorId;
	}
	/** 
	 * 设置 券商ID 
	 * @param investorId 券商ID 
	 */
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	/** 
	 * 获取 交易类型: 1 买多 2 买空 3 卖多 4 卖空 
	 * @return tradeType 
	 */
	public Integer getTradeType() {
		return tradeType;
	}
	/** 
	 * 设置 交易类型: 1 买多 2 买空 3 卖多 4 卖空 
	 * @param tradeType 交易类型: 1 买多 2 买空 3 卖多 4 卖空 
	 */
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
	/** 
	 * 获取 商品ID 
	 * @return productId 
	 */
	public Integer getProductId() {
		return productId;
	}
	/** 
	 * 设置 商品ID 
	 * @param productId 商品ID 
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/** 
	 * 获取 商品编码: 如CL1701 
	 * @return productCode 
	 */
	public String getProductCode() {
		return productCode;
	}
	/** 
	 * 设置 商品编码: 如CL1701 
	 * @param productCode 商品编码: 如CL1701 
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/** 
	 * 获取 商品名称: 如美原油 
	 * @return productName 
	 */
	public String getProductName() {
		return productName;
	}
	/** 
	 * 设置 商品名称: 如美原油 
	 * @param productName 商品名称: 如美原油 
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/** 
	 * 获取 交易所编码 
	 * @return exchangeCode 
	 */
	public String getExchangeCode() {
		return exchangeCode;
	}
	/** 
	 * 设置 交易所编码 
	 * @param exchangeCode 交易所编码 
	 */
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	/** 
	 * 获取 内外盘标识：0 内盘 1 外盘 
	 * @return plate 
	 */
	public Integer getPlate() {
		return plate;
	}
	/** 
	 * 设置 内外盘标识：0 内盘 1 外盘 
	 * @param plate 内外盘标识：0 内盘 1 外盘 
	 */
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	/** 
	 * 获取 证券帐号 
	 * @return securityCode 
	 */
	public String getSecurityCode() {
		return securityCode;
	}
	/** 
	 * 设置 证券帐号 
	 * @param securityCode 证券帐号 
	 */
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	/** 
	 * 获取 止损保证金=止损值+保证金参数(运营费用) 
	 * @return holdFund 
	 */
	public Double getHoldFund() {
		return holdFund;
	}
	/** 
	 * 设置 止损保证金=止损值+保证金参数(运营费用) 
	 * @param holdFund 止损保证金=止损值+保证金参数(运营费用) 
	 */
	public void setHoldFund(Double holdFund) {
		this.holdFund = holdFund;
	}
	/** 
	 * 获取 应扣手续费（优惠后，但使用优惠券前的金额） 
	 * @return shouldCounterFee 
	 */
	public Double getShouldCounterFee() {
		return shouldCounterFee;
	}
	/** 
	 * 设置 应扣手续费（优惠后，但使用优惠券前的金额） 
	 * @param shouldCounterFee 应扣手续费（优惠后，但使用优惠券前的金额） 
	 */
	public void setShouldCounterFee(Double shouldCounterFee) {
		this.shouldCounterFee = shouldCounterFee;
	}
	/** 
	 * 获取 实际手续费（实际扣除手续费，使用优惠券后的金额） 
	 * @return actualCounterFee 
	 */
	public Double getActualCounterFee() {
		return actualCounterFee;
	}
	/** 
	 * 设置 实际手续费（实际扣除手续费，使用优惠券后的金额） 
	 * @param actualCounterFee 实际手续费（实际扣除手续费，使用优惠券后的金额） 
	 */
	public void setActualCounterFee(Double actualCounterFee) {
		this.actualCounterFee = actualCounterFee;
	}
	/** 
	 * 获取 平台手续费抽成 
	 * @return platformFee 
	 */
	public Double getPlatformFee() {
		return platformFee;
	}
	/** 
	 * 设置 平台手续费抽成 
	 * @param platformFee 平台手续费抽成 
	 */
	public void setPlatformFee(Double platformFee) {
		this.platformFee = platformFee;
	}
	/** 
	 * 获取 券商手续费抽成 
	 * @return investorFee 
	 */
	public Double getInvestorFee() {
		return investorFee;
	}
	/** 
	 * 设置 券商手续费抽成 
	 * @param investorFee 券商手续费抽成 
	 */
	public void setInvestorFee(Double investorFee) {
		this.investorFee = investorFee;
	}
	/** 
	 * 获取 结算盈亏 
	 * @return lossProfit 
	 */
	public Double getLossProfit() {
		return lossProfit;
	}
	/** 
	 * 设置 结算盈亏 
	 * @param lossProfit 结算盈亏 
	 */
	public void setLossProfit(Double lossProfit) {
		this.lossProfit = lossProfit;
	}
	/** 
	 * 获取 触发止损金额 
	 * @return stopLoss 
	 */
	public Double getStopLoss() {
		return stopLoss;
	}
	/** 
	 * 设置 触发止损金额 
	 * @param stopLoss 触发止损金额 
	 */
	public void setStopLoss(Double stopLoss) {
		this.stopLoss = stopLoss;
	}
	/** 
	 * 获取 触发止盈金额 
	 * @return stopProfit 
	 */
	public Double getStopProfit() {
		return stopProfit;
	}
	/** 
	 * 设置 触发止盈金额 
	 * @param stopProfit 触发止盈金额 
	 */
	public void setStopProfit(Double stopProfit) {
		this.stopProfit = stopProfit;
	}
	/** 
	 * 获取 止损价格 
	 * @return stopLossPrice 
	 */
	public Double getStopLossPrice() {
		return stopLossPrice;
	}
	/** 
	 * 设置 止损价格 
	 * @param stopLossPrice 止损价格 
	 */
	public void setStopLossPrice(Double stopLossPrice) {
		this.stopLossPrice = stopLossPrice;
	}
	/** 
	 * 获取 止盈价 
	 * @return stopProfitPrice 
	 */
	public Double getStopProfitPrice() {
		return stopProfitPrice;
	}
	/** 
	 * 设置 止盈价 
	 * @param stopProfitPrice 止盈价 
	 */
	public void setStopProfitPrice(Double stopProfitPrice) {
		this.stopProfitPrice = stopProfitPrice;
	}
	/** 
	 * 获取 数量（手数） 
	 * @return count 
	 */
	public Integer getCount() {
		return count;
	}
	/** 
	 * 设置 数量（手数） 
	 * @param count 数量（手数） 
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/** 
	 * 获取 单位 
	 * @return unit 
	 */
	public String getUnit() {
		return unit;
	}
	/** 
	 * 设置 单位 
	 * @param unit 单位 
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/** 
	 * 获取 用户提交买入价 
	 * @return userCommitBuyPrice 
	 */
	public Double getUserCommitBuyPrice() {
		return userCommitBuyPrice;
	}
	/** 
	 * 设置 用户提交买入价 
	 * @param userCommitBuyPrice 用户提交买入价 
	 */
	public void setUserCommitBuyPrice(Double userCommitBuyPrice) {
		this.userCommitBuyPrice = userCommitBuyPrice;
	}
	/** 
	 * 获取 用提交买入时间 
	 * @return userCommitBuyDate 
	 */
	public Date getUserCommitBuyDate() {
		return userCommitBuyDate;
	}
	/** 
	 * 设置 用提交买入时间 
	 * @param userCommitBuyDate 用提交买入时间 
	 */
	public void setUserCommitBuyDate(Date userCommitBuyDate) {
		this.userCommitBuyDate = userCommitBuyDate;
	}
	/** 
	 * 获取 系统提交买入价 
	 * @return sysCommitBuyPrice 
	 */
	public Double getSysCommitBuyPrice() {
		return sysCommitBuyPrice;
	}
	/** 
	 * 设置 系统提交买入价 
	 * @param sysCommitBuyPrice 系统提交买入价 
	 */
	public void setSysCommitBuyPrice(Double sysCommitBuyPrice) {
		this.sysCommitBuyPrice = sysCommitBuyPrice;
	}
	/** 
	 * 获取 系统提交买入时间 
	 * @return sysCommitBuyDate 
	 */
	public Date getSysCommitBuyDate() {
		return sysCommitBuyDate;
	}
	/** 
	 * 设置 系统提交买入时间 
	 * @param sysCommitBuyDate 系统提交买入时间 
	 */
	public void setSysCommitBuyDate(Date sysCommitBuyDate) {
		this.sysCommitBuyDate = sysCommitBuyDate;
	}
	/** 
	 * 获取 订单创建时间 
	 * @return createDate 
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/** 
	 * 设置 订单创建时间 
	 * @param createDate 订单创建时间 
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/** 
	 * 获取 订单修改时间 
	 * @return modifyDate 
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/** 
	 * 设置 订单修改时间 
	 * @param modifyDate 订单修改时间 
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	/** 
	 * 获取 用户提交卖出价 
	 * @return userCommitSalePrice 
	 */
	public Double getUserCommitSalePrice() {
		return userCommitSalePrice;
	}
	/** 
	 * 设置 用户提交卖出价 
	 * @param userCommitSalePrice 用户提交卖出价 
	 */
	public void setUserCommitSalePrice(Double userCommitSalePrice) {
		this.userCommitSalePrice = userCommitSalePrice;
	}
	/** 
	 * 获取 用户提交卖出时间 
	 * @return userCommitSaleDate 
	 */
	public Date getUserCommitSaleDate() {
		return userCommitSaleDate;
	}
	/** 
	 * 设置 用户提交卖出时间 
	 * @param userCommitSaleDate 用户提交卖出时间 
	 */
	public void setUserCommitSaleDate(Date userCommitSaleDate) {
		this.userCommitSaleDate = userCommitSaleDate;
	}
	/** 
	 * 获取 系统提交卖出价 
	 * @return sysCommitSalePrice 
	 */
	public Double getSysCommitSalePrice() {
		return sysCommitSalePrice;
	}
	/** 
	 * 设置 系统提交卖出价 
	 * @param sysCommitSalePrice 系统提交卖出价 
	 */
	public void setSysCommitSalePrice(Double sysCommitSalePrice) {
		this.sysCommitSalePrice = sysCommitSalePrice;
	}
	/** 
	 * 获取 系统提交卖出时间 
	 * @return sysCommitSaleDate 
	 */
	public Date getSysCommitSaleDate() {
		return sysCommitSaleDate;
	}
	/** 
	 * 设置 系统提交卖出时间 
	 * @param sysCommitSaleDate 系统提交卖出时间 
	 */
	public void setSysCommitSaleDate(Date sysCommitSaleDate) {
		this.sysCommitSaleDate = sysCommitSaleDate;
	}
	/** 
	 * 获取 结算类型: 1.定时清仓 2.人工结算 3.风控平仓 4.用户结算 5.策略平仓 
	 * @return balanceType 
	 */
	public Integer getBalanceType() {
		return balanceType;
	}
	/** 
	 * 设置 结算类型: 1.定时清仓 2.人工结算 3.风控平仓 4.用户结算 5.策略平仓 
	 * @param balanceType 结算类型: 1.定时清仓 2.人工结算 3.风控平仓 4.用户结算 5.策略平仓 
	 */
	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}
	/** 
	 * 获取 结算员工ID 
	 * @return balanceStaffId 
	 */
	public Integer getBalanceStaffId() {
		return balanceStaffId;
	}
	/** 
	 * 设置 结算员工ID 
	 * @param balanceStaffId 结算员工ID 
	 */
	public void setBalanceStaffId(Integer balanceStaffId) {
		this.balanceStaffId = balanceStaffId;
	}
	/** 
	 * 获取 结算员工名称 
	 * @return balanceStaffName 
	 */
	public String getBalanceStaffName() {
		return balanceStaffName;
	}
	/** 
	 * 设置 结算员工名称 
	 * @param balanceStaffName 结算员工名称 
	 */
	public void setBalanceStaffName(String balanceStaffName) {
		this.balanceStaffName = balanceStaffName;
	}
	/** 
	 * 获取 结算时间 
	 * @return balanceDate 
	 */
	public Date getBalanceDate() {
		return balanceDate;
	}
	/** 
	 * 设置 结算时间 
	 * @param balanceDate 结算时间 
	 */
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	/** 
	 * 获取 系统清仓时间 
	 * @return sysSetSaleDate 
	 */
	public Date getSysSetSaleDate() {
		return sysSetSaleDate;
	}
	/** 
	 * 设置 系统清仓时间 
	 * @param sysSetSaleDate 系统清仓时间 
	 */
	public void setSysSetSaleDate(Date sysSetSaleDate) {
		this.sysSetSaleDate = sysSetSaleDate;
	}
	/** 
	 * 获取 系统结算收益(卖的差价收益) 
	 * @return sysLossProfit 
	 */
	public Double getSysLossProfit() {
		return sysLossProfit;
	}
	/** 
	 * 设置 系统结算收益(卖的差价收益) 
	 * @param sysLossProfit 系统结算收益(卖的差价收益) 
	 */
	public void setSysLossProfit(Double sysLossProfit) {
		this.sysLossProfit = sysLossProfit;
	}
	/** 
	 * 获取 币种 
	 * @return currency 
	 */
	public String getCurrency() {
		return currency;
	}
	/** 
	 * 设置 币种 
	 * @param currency 币种 
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/** 
	 * 获取 汇率(人民币汇率) 
	 * @return rate 
	 */
	public Double getRate() {
		return rate;
	}
	/** 
	 * 设置 汇率(人民币汇率) 
	 * @param rate 汇率(人民币汇率) 
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}
	/** 
	 * 获取 持仓递延状态（1:递延; 0:非递延） 
	 * @return deferStatus 
	 */
	public Integer getDeferStatus() {
		return deferStatus;
	}
	/** 
	 * 设置 持仓递延状态（1:递延; 0:非递延） 
	 * @param deferStatus 持仓递延状态（1:递延; 0:非递延） 
	 */
	public void setDeferStatus(Integer deferStatus) {
		this.deferStatus = deferStatus;
	}
	/** 
	 * 获取 递延保证金 
	 * @return deferFund 
	 */
	public Double getDeferFund() {
		return deferFund;
	}
	/** 
	 * 设置 递延保证金 
	 * @param deferFund 递延保证金 
	 */
	public void setDeferFund(Double deferFund) {
		this.deferFund = deferFund;
	}
	/** 
	 * 获取 递延利息 
	 * @return deferInterest 
	 */
	public Double getDeferInterest() {
		return deferInterest;
	}
	/** 
	 * 设置 递延利息 
	 * @param deferInterest 递延利息 
	 */
	public void setDeferInterest(Double deferInterest) {
		this.deferInterest = deferInterest;
	}
	/** 
	 * 获取 递延次数 
	 * @return deferTimes 
	 */
	public Integer getDeferTimes() {
		return deferTimes;
	}
	/** 
	 * 设置 递延次数 
	 * @param deferTimes 递延次数 
	 */
	public void setDeferTimes(Integer deferTimes) {
		this.deferTimes = deferTimes;
	}
	/** 
	 * 获取 移动止损开关: 0.关; 1.开 
	 * @return trailStopLoss 
	 */
	public Integer getTrailStopLoss() {
		return trailStopLoss;
	}
	/** 
	 * 设置 移动止损开关: 0.关; 1.开 
	 * @param trailStopLoss 移动止损开关: 0.关; 1.开 
	 */
	public void setTrailStopLoss(Integer trailStopLoss) {
		this.trailStopLoss = trailStopLoss;
	}
	/** 
	 * 获取 显示状态: 0 隐藏; 1 显示 
	 * @return display 
	 */
	public Integer getDisplay() {
		return display;
	}
	/** 
	 * 设置 显示状态: 0 隐藏; 1 显示 
	 * @param display 显示状态: 0 隐藏; 1 显示 
	 */
	public void setDisplay(Integer display) {
		this.display = display;
	}
	/** 
	 * 获取 订单状态：1.委托中; 2.持仓中; 3.失败; 4.完成; 
	 * @return status 
	 */
	public Integer getStatus() {
		return status;
	}
	/** 
	 * 设置 订单状态：1.委托中; 2.持仓中; 3.失败; 4.完成; 
	 * @param status 订单状态：1.委托中; 2.持仓中; 3.失败; 4.完成; 
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/** 
	 * 获取 提交买入手数 
	 * @return buyCommitCount 
	 */
	public Integer getBuyCommitCount() {
		return buyCommitCount;
	}
	/** 
	 * 设置 提交买入手数 
	 * @param buyCommitCount 提交买入手数 
	 */
	public void setBuyCommitCount(Integer buyCommitCount) {
		this.buyCommitCount = buyCommitCount;
	}
	/** 
	 * 获取 开仓委托手数 
	 * @return buyEntrustCount 
	 */
	public Integer getBuyEntrustCount() {
		return buyEntrustCount;
	}
	/** 
	 * 设置 开仓委托手数 
	 * @param buyEntrustCount 开仓委托手数 
	 */
	public void setBuyEntrustCount(Integer buyEntrustCount) {
		this.buyEntrustCount = buyEntrustCount;
	}
	/** 
	 * 获取 开仓成功手数 
	 * @return buySuccCount 
	 */
	public Integer getBuySuccCount() {
		return buySuccCount;
	}
	/** 
	 * 设置 开仓成功手数 
	 * @param buySuccCount 开仓成功手数 
	 */
	public void setBuySuccCount(Integer buySuccCount) {
		this.buySuccCount = buySuccCount;
	}
	/** 
	 * 获取 开仓失败手数 
	 * @return buyFailCount 
	 */
	public Integer getBuyFailCount() {
		return buyFailCount;
	}
	/** 
	 * 设置 开仓失败手数 
	 * @param buyFailCount 开仓失败手数 
	 */
	public void setBuyFailCount(Integer buyFailCount) {
		this.buyFailCount = buyFailCount;
	}
	/** 
	 * 获取 持仓手数 
	 * @return holdCount 
	 */
	public Integer getHoldCount() {
		return holdCount;
	}
	/** 
	 * 设置 持仓手数 
	 * @param holdCount 持仓手数 
	 */
	public void setHoldCount(Integer holdCount) {
		this.holdCount = holdCount;
	}
	/** 
	 * 获取 平仓委托手数 
	 * @return sellEntrustCount 
	 */
	public Integer getSellEntrustCount() {
		return sellEntrustCount;
	}
	/** 
	 * 设置 平仓委托手数 
	 * @param sellEntrustCount 平仓委托手数 
	 */
	public void setSellEntrustCount(Integer sellEntrustCount) {
		this.sellEntrustCount = sellEntrustCount;
	}
	/** 
	 * 获取 平仓处理中数量 
	 * @return sellDealCount 
	 */
	public Integer getSellDealCount() {
		return sellDealCount;
	}
	/** 
	 * 设置 平仓处理中数量 
	 * @param sellDealCount 平仓处理中数量 
	 */
	public void setSellDealCount(Integer sellDealCount) {
		this.sellDealCount = sellDealCount;
	}
	/** 
	 * 获取 平仓成功手数 
	 * @return sellSuccCount 
	 */
	public Integer getSellSuccCount() {
		return sellSuccCount;
	}
	/** 
	 * 设置 平仓成功手数 
	 * @param sellSuccCount 平仓成功手数 
	 */
	public void setSellSuccCount(Integer sellSuccCount) {
		this.sellSuccCount = sellSuccCount;
	}
	/** 
	 * 获取 开仓成交均价 
	 * @return sysBuyAvgPrice 
	 */
	public Double getSysBuyAvgPrice() {
		return sysBuyAvgPrice;
	}
	/** 
	 * 设置 开仓成交均价 
	 * @param sysBuyAvgPrice 开仓成交均价 
	 */
	public void setSysBuyAvgPrice(Double sysBuyAvgPrice) {
		this.sysBuyAvgPrice = sysBuyAvgPrice;
	}
	/** 
	 * 获取 开仓成交时间(毫秒级) 
	 * @return sysLastBuyDate 
	 */
	public String getSysLastBuyDate() {
		return sysLastBuyDate;
	}
	/** 
	 * 设置 开仓成交时间(毫秒级) 
	 * @param sysLastBuyDate 开仓成交时间(毫秒级) 
	 */
	public void setSysLastBuyDate(String sysLastBuyDate) {
		this.sysLastBuyDate = sysLastBuyDate;
	}
	/** 
	 * 获取 平仓成交均价 
	 * @return sysSellAvgPrice 
	 */
	public Double getSysSellAvgPrice() {
		return sysSellAvgPrice;
	}
	/** 
	 * 设置 平仓成交均价 
	 * @param sysSellAvgPrice 平仓成交均价 
	 */
	public void setSysSellAvgPrice(Double sysSellAvgPrice) {
		this.sysSellAvgPrice = sysSellAvgPrice;
	}
	/** 
	 * 获取 平仓成功时间(毫秒级) 
	 * @return sysLastSellDate 
	 */
	public String getSysLastSellDate() {
		return sysLastSellDate;
	}
	/** 
	 * 设置 平仓成功时间(毫秒级) 
	 * @param sysLastSellDate 平仓成功时间(毫秒级) 
	 */
	public void setSysLastSellDate(String sysLastSellDate) {
		this.sysLastSellDate = sysLastSellDate;
	}
	/** 
	 * 获取 每手止损保证金参数(原币种) 
	 * @return perSurcharge 
	 */
	public Double getPerSurcharge() {
		return perSurcharge;
	}
	/** 
	 * 设置 每手止损保证金参数(原币种) 
	 * @param perSurcharge 每手止损保证金参数(原币种) 
	 */
	public void setPerSurcharge(Double perSurcharge) {
		this.perSurcharge = perSurcharge;
	}
	/** 
	 * 获取 每手止损金额(原币种) 
	 * @return perStopLoss 
	 */
	public Double getPerStopLoss() {
		return perStopLoss;
	}
	/** 
	 * 设置 每手止损金额(原币种) 
	 * @param perStopLoss 每手止损金额(原币种) 
	 */
	public void setPerStopLoss(Double perStopLoss) {
		this.perStopLoss = perStopLoss;
	}
	/** 
	 * 获取 每手止盈金额(原币种) 
	 * @return perStopProfit 
	 */
	public Double getPerStopProfit() {
		return perStopProfit;
	}
	/** 
	 * 设置 每手止盈金额(原币种) 
	 * @param perStopProfit 每手止盈金额(原币种) 
	 */
	public void setPerStopProfit(Double perStopProfit) {
		this.perStopProfit = perStopProfit;
	}
	/** 
	 * 获取 每手手续费(原币种) 
	 * @return perCounterFee 
	 */
	public Double getPerCounterFee() {
		return perCounterFee;
	}
	/** 
	 * 设置 每手手续费(原币种) 
	 * @param perCounterFee 每手手续费(原币种) 
	 */
	public void setPerCounterFee(Double perCounterFee) {
		this.perCounterFee = perCounterFee;
	}
	/** 
	 * 获取 每手递延保证金(原币种) 
	 * @return perDeferFund 
	 */
	public Double getPerDeferFund() {
		return perDeferFund;
	}
	/** 
	 * 设置 每手递延保证金(原币种) 
	 * @param perDeferFund 每手递延保证金(原币种) 
	 */
	public void setPerDeferFund(Double perDeferFund) {
		this.perDeferFund = perDeferFund;
	}
	/** 
	 * 获取 每手递延利息(原币种) 
	 * @return perDeferInterest 
	 */
	public Double getPerDeferInterest() {
		return perDeferInterest;
	}
	/** 
	 * 设置 每手递延利息(原币种) 
	 * @param perDeferInterest 每手递延利息(原币种) 
	 */
	public void setPerDeferInterest(Double perDeferInterest) {
		this.perDeferInterest = perDeferInterest;
	}
	
	
}
