package com.lt.vo.trade;

import java.io.Serializable;
import java.util.Date;

/**
 * 结算订单列表返回实体
 * 
 * @author guodw
 *
 */
public class OrderBalanceVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6286276941012235390L;

	/**
	 * 小数单位
	 */
	private Integer decimalDigits;
	
	/** 交易类型: 1 买多, 2 买空, 3 卖多, 4 卖空 */
	private Integer tradeType;
	/** 商品编码: 如CL1701 */
	private String productCode;
	/** 商品名称: 如美原油 */
	private String productName;
	/** 内外盘标识：0 内盘, 1 外盘 */
	private Integer plate;
	/** 止损保证金=止损值+保证金参数(运营费用) */
	private Double holdFund;
	/** 结算盈亏 */
	private Double lossProfit;
	/** 数量（手数） */
	private Integer count;
	/** 系统提交买入时间 */
	private Date sysCommitBuyDate;
	/** 系统最后买入时间*/
	private Date sysLastBuyDate;
	/** 系统最后卖出时间*/
	private Date sysLastSellDate;
	/** 订单创建时间 */
	private Date createDate;
	/**修改时间*/
	private Date modifyDate;
	/** 系统提交卖出时间 */
	private Date sysCommitSaleDate;
	/** 结算类型: 1.定时清仓, 2.人工结算, 3.风控平仓, 4.用户结算, 5.策略平仓 */
	private Integer balanceType;
	/** 结算时间 */
	private Date balanceDate;
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
	/** 订单状态：1.开仓委托中; 2.平仓委托中; 3.持仓中; 4.完成;5.失败 */
	private Integer status;
	/** 开仓成交均价(关联cash_order_count表) */
	private Double sysBuyAvgPrice;
	/** 卖出均价 */
	private Double sysSaleAvgPrice;
	/** 订单类型，1积分 、0现金 */
	private Integer fundType;
	/** 实际手续费（实际扣除手续费，使用优惠券后的金额） */
	private Double actualCounterFee;
	/**状态*/
	private String statusFmt;
	private Integer buyTriggerType;
	private String orderId;
	private Integer buyEntrustType;
	private String investorId;
	
	public Integer getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(Integer decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public Date getSysLastBuyDate() {
		return sysLastBuyDate;
	}

	public void setSysLastBuyDate(Date sysLastBuyDate) {
		this.sysLastBuyDate = sysLastBuyDate;
	}

	public Date getSysLastSellDate() {
		return sysLastSellDate;
	}

	public void setSysLastSellDate(Date sysLastSellDate) {
		this.sysLastSellDate = sysLastSellDate;
	}

	public Double getActualCounterFee() {
		return actualCounterFee;
	}

	public void setActualCounterFee(Double actualCounterFee) {
		this.actualCounterFee = actualCounterFee;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getPlate() {
		return plate;
	}

	public void setPlate(Integer plate) {
		this.plate = plate;
	}

	public Double getHoldFund() {
		return holdFund;
	}

	public void setHoldFund(Double holdFund) {
		this.holdFund = holdFund;
	}


	public Double getLossProfit() {
		return lossProfit;
	}

	public void setLossProfit(Double lossProfit) {
		this.lossProfit = lossProfit;
	}


	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}


	public Date getSysCommitBuyDate() {
		return sysCommitBuyDate;
	}

	public void setSysCommitBuyDate(Date sysCommitBuyDate) {
		this.sysCommitBuyDate = sysCommitBuyDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getSysCommitSaleDate() {
		return sysCommitSaleDate;
	}

	public void setSysCommitSaleDate(Date sysCommitSaleDate) {
		this.sysCommitSaleDate = sysCommitSaleDate;
	}

	public Integer getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}

	public Date getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getDeferStatus() {
		return deferStatus;
	}

	public void setDeferStatus(Integer deferStatus) {
		this.deferStatus = deferStatus;
	}

	public Double getDeferFund() {
		return deferFund;
	}

	public void setDeferFund(Double deferFund) {
		this.deferFund = deferFund;
	}

	public Double getDeferInterest() {
		return deferInterest;
	}

	public void setDeferInterest(Double deferInterest) {
		this.deferInterest = deferInterest;
	}

	public Integer getDeferTimes() {
		return deferTimes;
	}

	public void setDeferTimes(Integer deferTimes) {
		this.deferTimes = deferTimes;
	}

	public Integer getTrailStopLoss() {
		return trailStopLoss;
	}

	public void setTrailStopLoss(Integer trailStopLoss) {
		this.trailStopLoss = trailStopLoss;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getSysBuyAvgPrice() {
		return sysBuyAvgPrice;
	}

	public void setSysBuyAvgPrice(Double sysBuyAvgPrice) {
		this.sysBuyAvgPrice = sysBuyAvgPrice;
	}

	public Double getSysSaleAvgPrice() {
		return sysSaleAvgPrice;
	}

	public void setSysSaleAvgPrice(Double sysSaleAvgPrice) {
		this.sysSaleAvgPrice = sysSaleAvgPrice;
	}

	public Integer getFundType() {
		return fundType;
	}

	public void setFundType(Integer fundType) {
		this.fundType = fundType;
	}


	public String getStatusFmt() {
		return statusFmt;
	}

	public void setStatusFmt(String statusFmt) {
		this.statusFmt = statusFmt;
	}

	public Integer getBuyTriggerType() {
		return buyTriggerType;
	}

	public void setBuyTriggerType(Integer buyTriggerType) {
		this.buyTriggerType = buyTriggerType;
	}

	public Integer getBuyEntrustType() {
		return buyEntrustType;
	}

	public void setBuyEntrustType(Integer buyEntrustType) {
		this.buyEntrustType = buyEntrustType;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getInvestorId() {
		return investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	
}
