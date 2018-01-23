package com.lt.vo.trade;

import java.io.Serializable;

/**
 * 持仓订单返回实体
 * 
 * @author guodw
 *
 */
public class PositionOrderVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1419295658705212675L;
	/**
	 * 小数位数
	 */
	private Integer decimalDigits;
	/** 汇率 */
	private String rate;
	/** 汇总ID */
	private String orderId;
	/** 手数 */
	private String count;
	private String sellEntrustCount;
	/** 买卖方向 */
	private String tradeType;//
	/** 是否隔夜 0日内持仓，1长线持仓，2长线持仓但资金不足即将被清仓 */
	private String deferStatus;//
	/** 止盈价格 */
	private String stopProfit;//
	/** 止损价格 */
	private String stopLoss;//
	/** 市场状态 */
	private String marketStatus;//
	/** 清仓时间 */
	private String sysSaleTime;//
	/** 合约交割时间 */
	private String expirationTime;//
	/** 状态 */
	private String status;//
	/** 持仓手数 */
	private String holdCount;//
	/** 卖出手数 */
	private String sellSuccCount;//
	/** 购买时间 */
	private String sysCommitBuyDate;//
	/** 买入均价 */
	private String sysBuyAvgPrice;//
	/** 保证金 */
	private String holdFund;//
	private String shouldHoldFund;//
	/** 手续费 */
	private String actualCounterFee;//
	/** 合约 */
	private String productCode;//
	/**
	 * 产品id
	 */
	private String productId;
	/** 名字 */
	private String productName;//
	/** 递延次数 */
	private String deferTimes;//
	/** 递延费 */
	private String deferInterest;//
	/** 买入类型 1:市价单 2:条件单 暂时不处理 */
	private String buyEntrustType;//

	/** 递延保证金 */
	private String deferFund;
	private String shouldDeferFund;
	/** 最小变动价格 */
	private String jumpPrice;
	/** 最小波动点 */
	private String jumpValue;
	/**
	 * 内外盘
	 */
	private Integer plate;
	/**
	 * 移动止损金额
	 */
	private String moveStopPrice;
	
	/**
	 * '移动止损开关: 0.关; 1.开',
	 */
	private Integer trailStopLoss;

	/**
	 * 开仓成功手数
	 */
	private String buySuccCount;
	/**
	 * 卖一价
	 */
	private String askPrice;
	/**
	 * 买一价
	 */
	private String bidPrice;
	
	/**单位*/
	private String unit;
	/**券商id**/
	private String investorId;
	/**
	 * 单手止盈
	 */
	private Double perStopProfit;
	/**
	 * 单手止损
	 */
	private Double perStopLoss;

	/**
	 *
	 * @return
	 */
	
	
	public String getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}

	public String getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getDeferStatus() {
		return deferStatus;
	}

	public void setDeferStatus(String deferStatus) {
		this.deferStatus = deferStatus;
	}

	public String getStopProfit() {
		return stopProfit;
	}

	public void setStopProfit(String stopProfit) {
		this.stopProfit = stopProfit;
	}

	public String getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(String stopLoss) {
		this.stopLoss = stopLoss;
	}

	public String getMarketStatus() {
		return marketStatus;
	}

	public void setMarketStatus(String marketStatus) {
		this.marketStatus = marketStatus;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHoldCount() {
		return holdCount;
	}

	public void setHoldCount(String holdCount) {
		this.holdCount = holdCount;
	}

	public String getActualCounterFee() {
		return actualCounterFee;
	}

	public void setActualCounterFee(String actualCounterFee) {
		this.actualCounterFee = actualCounterFee;
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

	public String getDeferTimes() {
		return deferTimes;
	}

	public void setDeferTimes(String deferTimes) {
		this.deferTimes = deferTimes;
	}

	public String getDeferInterest() {
		return deferInterest;
	}

	public void setDeferInterest(String deferInterest) {
		this.deferInterest = deferInterest;
	}
	public String getJumpPrice() {
		return jumpPrice;
	}

	public void setJumpPrice(String jumpPrice) {
		this.jumpPrice = jumpPrice;
	}

	public String getJumpValue() {
		return jumpValue;
	}

	public void setJumpValue(String jumpValue) {
		this.jumpValue = jumpValue;
	}

	public String getMoveStopPrice() {
		return moveStopPrice;
	}

	public void setMoveStopPrice(String moveStopPrice) {
		this.moveStopPrice = moveStopPrice;
	}

	public Integer getPlate() {
		return plate;
	}

	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	public Integer getTrailStopLoss() {
		return trailStopLoss;
	}

	public void setTrailStopLoss(Integer trailStopLoss) {
		this.trailStopLoss = trailStopLoss;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSellEntrustCount() {
		return sellEntrustCount;
	}

	public void setSellEntrustCount(String sellEntrustCount) {
		this.sellEntrustCount = sellEntrustCount;
	}

	public String getSysSaleTime() {
		return sysSaleTime;
	}

	public void setSysSaleTime(String sysSaleTime) {
		this.sysSaleTime = sysSaleTime;
	}

	public String getSellSuccCount() {
		return sellSuccCount;
	}

	public void setSellSuccCount(String sellSuccCount) {
		this.sellSuccCount = sellSuccCount;
	}

	public String getSysCommitBuyDate() {
		return sysCommitBuyDate;
	}

	public void setSysCommitBuyDate(String sysCommitBuyDate) {
		this.sysCommitBuyDate = sysCommitBuyDate;
	}

	public String getSysBuyAvgPrice() {
		return sysBuyAvgPrice;
	}

	public void setSysBuyAvgPrice(String sysBuyAvgPrice) {
		this.sysBuyAvgPrice = sysBuyAvgPrice;
	}

	public String getHoldFund() {
		return holdFund;
	}

	public void setHoldFund(String holdFund) {
		this.holdFund = holdFund;
	}

	public String getShouldHoldFund() {
		return shouldHoldFund;
	}

	public void setShouldHoldFund(String shouldHoldFund) {
		this.shouldHoldFund = shouldHoldFund;
	}

	public String getBuyEntrustType() {
		return buyEntrustType;
	}

	public void setBuyEntrustType(String buyEntrustType) {
		this.buyEntrustType = buyEntrustType;
	}

	public String getDeferFund() {
		return deferFund;
	}

	public void setDeferFund(String deferFund) {
		this.deferFund = deferFund;
	}

	public String getShouldDeferFund() {
		return shouldDeferFund;
	}

	public void setShouldDeferFund(String shouldDeferFund) {
		this.shouldDeferFund = shouldDeferFund;
	}

	public String getBuySuccCount() {
		return buySuccCount;
	}

	public void setBuySuccCount(String buySuccCount) {
		this.buySuccCount = buySuccCount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(Integer decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public Double getPerStopProfit() {
		return perStopProfit;
	}

	public void setPerStopProfit(Double perStopProfit) {
		this.perStopProfit = perStopProfit;
	}

	public Double getPerStopLoss() {
		return perStopLoss;
	}

	public void setPerStopLoss(Double perStopLoss) {
		this.perStopLoss = perStopLoss;
	}

	public String getInvestorId() {
		return investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
}
