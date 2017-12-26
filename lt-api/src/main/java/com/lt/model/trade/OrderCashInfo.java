package com.lt.model.trade;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DoubleUtils;
/**
 * 订单现金信息实体
 * @author jwb
 *
 */
public class OrderCashInfo implements Serializable{

	private static final long serialVersionUID = -8911177771839915371L;
	/**id(不参与实际业务)*/
	private Integer id;
	/**订单ID(参与实际业务)*/
	private String orderId;
	/**条件单id	*/
	private String conditionId;
	/**用户ID*/
	private String userId;
	/**用户昵称*/
	private String nickName;
	/**券商ID*/
	private String investorId;
	/**证券账户ID*/
	private Integer accountId;
	/**证券账户*/
	private String securityCode;
	/**单手止盈金额（原币种）*/
	private Double perStopProfit;
	/**单手止损金额（原币种）*/
	private Double perStopLoss;
	/**单手手续费（原币种）*/
	private Double perCounterFee;
	/**保证金参数（原币种）*/
	private Double perSurcharge;
	/**单手递延保证金（原币种）*/
	private Double perDeferFund;
	/**单手递延费（原币种）*/
	private Double perDeferInterest;
	/**商品ID*/
	private Integer productId;
	/**商品编码*/
	private String productCode;
	/**商品名称*/
	private String productName;
	/**原币种*/
	private String currency;
	/**货币单位*/
	private String unit;	
	/**汇率*/
	private Double rate;
	/**0内盘 1外盘*/
	private Integer plate;	
	/**交易所编码*/
	private String exchangeCode;
	/**显示状态0隐藏  1显示*/
	private Integer display;
	/**递延状态0未开启  1已开启*/
	private Integer deferStatus;
	/**追踪止损状态0未开启  1已开启*/
	private Integer trailStopLoss;
	/**结算状态0 未结算  1已结算*/
	private Integer balanceStatus;
	/**结算时间*/
	private Date balanceDate;
	/**（开仓）触发方式:100客户触发、101管理员触发、200其他*/
	private Integer buyTriggerType;
	/**（开仓）委托方式1市价，2挂单*/
	private Integer buyEntrustType;
	/**（开仓）用户提交时间(系统接收时间)*/
	private Date userCommitBuyDate;
	/**（开仓）委托生成时间（报交易所时间）*/
	private Date entrustBuyDate;
	/**（开仓）最后成交时间*/
	private Date lastBuyDate;
	/**（开仓）用户提交价格*/
	private Double userCommitBuyPrice;
	/**（开仓）报单委托价(市价 0；挂单为委托价)*/
	private Double entrustBuyPrice;
	/**（开仓）成交均价*/
	private Double buyAvgPrice;
	/**（平仓）触发方式:100客户触发，101管理员触发、102系统清仓、103风控触发、200其他*/
	private Integer sellTriggerType;
	/**（平仓）委托方式1挂价，2市价*/
	private Integer sellEntrustType;
	/**（平仓）用户提交时间(系统接收时间)*/
	private Date userCommitSellDate;
	/**（平仓）委托生成时间（报交易所时间）*/
	private Date entrustSellDate;
	/**（平仓）最后成交时间*/
	private Date lastSellDate;
	/**（平仓）用户提交价格（包含风控触发止损价）*/
	private Double userCommitSellPrice;	
	/**（平仓）报单委托价*/
	private Double entrustSellPrice;
	/**（平仓）成交均价*/
	private Double sellAvgPrice;
	/**交易方向1 看多; 2 看空*/
	private Integer tradeDirection;
	/**系统清仓时间*/
	private Date sysSetSellDate;
	/**开仓委托数量*/
	private Integer buyEntrustCount;
	/**开仓成功数量*/
	private Integer buySuccessCount;
	/**开仓失败数量*/
	private Integer buyFailCount;
	/**持仓中数量*/
	private Integer holdCount;
	/**平仓委托数量*/
	private Integer sellEntrustCount;
	/**平仓成功数量*/
	private Integer sellSuccessCount;
	/**止损价格*/
	private Double stopLossPrice;
	/**止损金额*/
	private Double stopLoss;
	/**止盈价格*/
	private Double stopProfitPrice;	
	/**止盈金额*/
	private Double stopProfit;
	/**平仓盈亏（系统平仓真实盈亏）*/
	private Double sysLossProfit;
	/**结算盈亏（用户实际结算盈亏）*/
	private Double lossProfit;
	/**平仓总盈亏（系统平仓真实盈亏）*/
	private Double thisSysLossProfit;
	/**结算总盈亏（用户实际结算盈亏）*/
	private Double thisLossProfit;
	/**结算总盈亏（用户实际结算盈亏 原币种）*/
	private Double lossProfitRate;
	/**累计扣除递延费*/
	private Double deferInterest;
	/**累计扣除递延次数*/
	private Integer deferTimes;
	/**应扣保证金（合计）*/
	private Double shouldHoldFund;
	/**应扣递延保证金（合计）*/
	private Double shouldDeferFund;
	/**应扣手续费（合计）*/
	private Double shouldCounterFee;
	/**应扣优惠券抵扣金额（合计）*/
	private Double shouldCouponFund;
	/**实扣保证金（合计）*/
	private Double actualHoldFund;
	/**实扣递延保证金（合计）*/
	private Double actualDeferFund;
	/**实扣手续费（合计）*/
	private Double actualCounterFee;
	/**实扣优惠券抵扣金额（合计）*/
	private Double actualCouponFund;
	/**创建时间*/
	private Date createDate;
	/**修改时间*/
	private Date modifyDate;
	/**移动止损金额*/
	private Double moveStopLoss;
	/**移动止损价格*/
	private Double moveStopLossPrice;
	/**记录下单方式：0:市价单 1:条件单 2:闪电单*/
	private Integer purchaseOrderType;
	/**修改人*/
	private String modifyUserId;
	/**买入总价（用于计算均价）*/
	private Double buyPriceTotal;
	/**卖出总价（用于计算均价）*/
	private Double sellPriceTotal;
	/**买最小价*/
	private Double buyMinPrice;
	/***卖最小价*/
	private Double sellMinPrice;
	/***品牌 id*/
	private String brandId;
	/***角模式**/
	private Double mini;

	@Override
	public String toString() {
		return "OrderCashInfo{" +
				"id=" + id +
				", orderId='" + orderId + '\'' +
				", conditionId='" + conditionId + '\'' +
				", userId='" + userId + '\'' +
				", nickName='" + nickName + '\'' +
				", investorId='" + investorId + '\'' +
				", accountId=" + accountId +
				", securityCode='" + securityCode + '\'' +
				", perStopProfit=" + perStopProfit +
				", perStopLoss=" + perStopLoss +
				", perCounterFee=" + perCounterFee +
				", perSurcharge=" + perSurcharge +
				", perDeferFund=" + perDeferFund +
				", perDeferInterest=" + perDeferInterest +
				", productId=" + productId +
				", productCode='" + productCode + '\'' +
				", productName='" + productName + '\'' +
				", currency='" + currency + '\'' +
				", unit='" + unit + '\'' +
				", rate=" + rate +
				", plate=" + plate +
				", exchangeCode='" + exchangeCode + '\'' +
				", display=" + display +
				", deferStatus=" + deferStatus +
				", trailStopLoss=" + trailStopLoss +
				", balanceStatus=" + balanceStatus +
				", balanceDate=" + balanceDate +
				", buyTriggerType=" + buyTriggerType +
				", buyEntrustType=" + buyEntrustType +
				", userCommitBuyDate=" + userCommitBuyDate +
				", entrustBuyDate=" + entrustBuyDate +
				", lastBuyDate=" + lastBuyDate +
				", userCommitBuyPrice=" + userCommitBuyPrice +
				", entrustBuyPrice=" + entrustBuyPrice +
				", buyAvgPrice=" + buyAvgPrice +
				", sellTriggerType=" + sellTriggerType +
				", sellEntrustType=" + sellEntrustType +
				", userCommitSellDate=" + userCommitSellDate +
				", entrustSellDate=" + entrustSellDate +
				", lastSellDate=" + lastSellDate +
				", userCommitSellPrice=" + userCommitSellPrice +
				", entrustSellPrice=" + entrustSellPrice +
				", sellAvgPrice=" + sellAvgPrice +
				", tradeDirection=" + tradeDirection +
				", sysSetSellDate=" + sysSetSellDate +
				", buyEntrustCount=" + buyEntrustCount +
				", buySuccessCount=" + buySuccessCount +
				", buyFailCount=" + buyFailCount +
				", holdCount=" + holdCount +
				", sellEntrustCount=" + sellEntrustCount +
				", sellSuccessCount=" + sellSuccessCount +
				", stopLossPrice=" + stopLossPrice +
				", stopLoss=" + stopLoss +
				", stopProfitPrice=" + stopProfitPrice +
				", stopProfit=" + stopProfit +
				", sysLossProfit=" + sysLossProfit +
				", lossProfit=" + lossProfit +
				", thisSysLossProfit=" + thisSysLossProfit +
				", thisLossProfit=" + thisLossProfit +
				", lossProfitRate=" + lossProfitRate +
				", deferInterest=" + deferInterest +
				", deferTimes=" + deferTimes +
				", shouldHoldFund=" + shouldHoldFund +
				", shouldDeferFund=" + shouldDeferFund +
				", shouldCounterFee=" + shouldCounterFee +
				", shouldCouponFund=" + shouldCouponFund +
				", actualHoldFund=" + actualHoldFund +
				", actualDeferFund=" + actualDeferFund +
				", actualCounterFee=" + actualCounterFee +
				", actualCouponFund=" + actualCouponFund +
				", createDate=" + createDate +
				", modifyDate=" + modifyDate +
				", moveStopLoss=" + moveStopLoss +
				", moveStopLossPrice=" + moveStopLossPrice +
				", purchaseOrderType=" + purchaseOrderType +
				", modifyUserId='" + modifyUserId + '\'' +
				", buyPriceTotal=" + buyPriceTotal +
				", sellPriceTotal=" + sellPriceTotal +
				", buyMinPrice=" + buyMinPrice +
				", sellMinPrice=" + sellMinPrice +
				", brandId='" + brandId + '\'' +
				", mini='" + mini + '\'' +
				'}';
	}

	public OrderCashInfo(){}
	
	public OrderCashInfo(String userId, String investorId, String orderId,
						  Integer tradeDirection, Integer productId, String productCode,
						  String productName, Integer buyEntrustCount, String exchangeCode,
						  Integer plate, String unit, Double userCommitBuyPrice,
						  Date userCommitBuyDate,Date sysSetSellDate, String currency, Double rate,
						  Integer trailStopLoss, Double perSurcharge,Double perStopLoss, 
						  Double perStopProfit, Double perCounterFee,Double perDeferFund, 
						  Double perDeferInterest,Double shouldHoldFund,Double actualHoldFund,
						  Double shouldCounterFee, Double actualCounterFee,Double stopLoss,
						  Double stopProfit, Integer deferStatus, Double shouldDeferFund,
						  Double actualDeferFund,Date createDate,Date modifyDate,Integer display,
						  Integer buySuccessCount,Integer buyFailCount,Integer holdCount) {
		this.userId = userId;
		this.investorId = investorId;
		this.orderId = orderId;
		this.tradeDirection = tradeDirection;
		this.productId = productId;
		this.productCode = productCode;
		this.productName = productName;
		this.buyEntrustCount = buyEntrustCount;
		this.exchangeCode = exchangeCode;
		this.plate = plate;
		this.unit = unit;
		this.userCommitBuyPrice = userCommitBuyPrice;
		this.userCommitBuyDate = userCommitBuyDate;
		this.sysSetSellDate = sysSetSellDate;
		this.currency = currency;
		this.rate = rate;
		this.trailStopLoss = trailStopLoss;
		this.perSurcharge = perSurcharge;
		this.perStopLoss = perStopLoss;
		this.perStopProfit = perStopProfit;
		this.perCounterFee = perCounterFee;		
		this.perDeferFund = perDeferFund;
		this.perDeferInterest = perDeferInterest;
		this.shouldHoldFund = shouldHoldFund;
		this.actualHoldFund = actualHoldFund;
		this.shouldCounterFee = shouldCounterFee;
		this.actualCounterFee = actualCounterFee;
		this.stopLoss = stopLoss;
		this.stopProfit = stopProfit;
		this.deferStatus = deferStatus;
		this.shouldDeferFund = shouldDeferFund;
		this.actualDeferFund = actualDeferFund;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.display = display;
		this.buySuccessCount = buySuccessCount;
		this.buyFailCount = buyFailCount;
		this.holdCount = holdCount;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getConditionId() {
		return conditionId;
	}
	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getInvestorId() {
		return investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
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
	public Double getPerCounterFee() {
		return perCounterFee;
	}
	public void setPerCounterFee(Double perCounterFee) {
		this.perCounterFee = perCounterFee;
	}
	
	public Double getPerSurcharge() {
		return perSurcharge;
	}
	public void setPerSurcharge(Double perSurcharge) {
		this.perSurcharge = perSurcharge;
	}
	public Double getPerDeferFund() {
		return perDeferFund;
	}
	public void setPerDeferFund(Double perDeferFund) {
		this.perDeferFund = perDeferFund;
	}
	public Double getPerDeferInterest() {
		return perDeferInterest;
	}
	public void setPerDeferInterest(Double perDeferInterest) {
		this.perDeferInterest = perDeferInterest;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getPlate() {
		return plate;
	}
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	public String getExchangeCode() {
		return exchangeCode;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public Integer getDisplay() {
		return display;
	}
	public void setDisplay(Integer display) {
		this.display = display;
	}
	public Integer getDeferStatus() {
		return deferStatus;
	}
	public void setDeferStatus(Integer deferStatus) {
		this.deferStatus = deferStatus;
	}
	public Integer getTrailStopLoss() {
		return trailStopLoss;
	}
	public void setTrailStopLoss(Integer trailStopLoss) {
		this.trailStopLoss = trailStopLoss;
	}
	public Integer getBalanceStatus() {
		return balanceStatus;
	}
	public void setBalanceStatus(Integer balanceStatus) {
		this.balanceStatus = balanceStatus;
	}

	public Integer getBuyEntrustType() {
		return buyEntrustType;
	}
	public void setBuyEntrustType(Integer buyEntrustType) {
		this.buyEntrustType = buyEntrustType;
	}
	public Date getUserCommitBuyDate() {
		return userCommitBuyDate;
	}
	public void setUserCommitBuyDate(Date userCommitBuyDate) {
		this.userCommitBuyDate = userCommitBuyDate;
	}
	public Date getEntrustBuyDate() {
		return entrustBuyDate;
	}
	public void setEntrustBuyDate(Date entrustBuyDate) {
		this.entrustBuyDate = entrustBuyDate;
	}
	public Date getLastBuyDate() {
		return lastBuyDate;
	}
	public void setLastBuyDate(Date lastBuyDate) {
		this.lastBuyDate = lastBuyDate;
	}
	public Double getUserCommitBuyPrice() {
		return userCommitBuyPrice;
	}
	public void setUserCommitBuyPrice(Double userCommitBuyPrice) {
		this.userCommitBuyPrice = userCommitBuyPrice;
	}
	public Double getEntrustBuyPrice() {
		return entrustBuyPrice;
	}
	public void setEntrustBuyPrice(Double entrustBuyPrice) {
		this.entrustBuyPrice = entrustBuyPrice;
	}
	public Double getBuyAvgPrice() {
		return buyAvgPrice;
	}
	public void setBuyAvgPrice(Double buyAvgPrice) {
		this.buyAvgPrice = buyAvgPrice;
	}

	public Integer getSellEntrustType() {
		return sellEntrustType;
	}
	public void setSellEntrustType(Integer sellEntrustType) {
		this.sellEntrustType = sellEntrustType;
	}
	public Date getUserCommitSellDate() {
		return userCommitSellDate;
	}
	public void setUserCommitSellDate(Date userCommitSellDate) {
		this.userCommitSellDate = userCommitSellDate;
	}
	public Date getEntrustSellDate() {
		return entrustSellDate;
	}
	public void setEntrustSellDate(Date entrustSellDate) {
		this.entrustSellDate = entrustSellDate;
	}
	public Date getLastSellDate() {
		return lastSellDate;
	}
	public void setLastSellDate(Date lastSellDate) {
		this.lastSellDate = lastSellDate;
	}
	public Double getUserCommitSellPrice() {
		return userCommitSellPrice;
	}
	public void setUserCommitSellPrice(Double userCommitSellPrice) {
		this.userCommitSellPrice = userCommitSellPrice;
	}
	public Double getEntrustSellPrice() {
		return entrustSellPrice;
	}
	public void setEntrustSellPrice(Double entrustSellPrice) {
		this.entrustSellPrice = entrustSellPrice;
	}
	public Double getSellAvgPrice() {
		return sellAvgPrice;
	}
	public void setSellAvgPrice(Double sellAvgPrice) {
		this.sellAvgPrice = sellAvgPrice;
	}
	
	public Integer getTradeDirection() {
		return tradeDirection;
	}
	public void setTradeDirection(Integer tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	public Date getSysSetSellDate() {
		return sysSetSellDate;
	}
	public void setSysSetSellDate(Date sysSetSellDate) {
		this.sysSetSellDate = sysSetSellDate;
	}
	public Integer getBuyEntrustCount() {
		return buyEntrustCount;
	}
	public void setBuyEntrustCount(Integer buyEntrustCount) {
		this.buyEntrustCount = buyEntrustCount;
	}
	public Integer getBuySuccessCount() {
		return buySuccessCount;
	}
	public void setBuySuccessCount(Integer buySuccessCount) {
		this.buySuccessCount = buySuccessCount;
	}
	public Integer getBuyFailCount() {
		return buyFailCount;
	}
	public void setBuyFailCount(Integer buyFailCount) {
		this.buyFailCount = buyFailCount;
	}
	public Integer getHoldCount() {
		return holdCount;
	}
	public void setHoldCount(Integer holdCount) {
		this.holdCount = holdCount;
	}
	public Integer getSellEntrustCount() {
		return sellEntrustCount;
	}
	public void setSellEntrustCount(Integer sellEntrustCount) {
		this.sellEntrustCount = sellEntrustCount;
	}
	public Integer getSellSuccessCount() {
		return sellSuccessCount;
	}
	public void setSellSuccessCount(Integer sellSuccessCount) {
		this.sellSuccessCount = sellSuccessCount;
	}
	public Double getStopLossPrice() {
		return stopLossPrice;
	}
	public void setStopLossPrice(Double stopLossPrice) {
		this.stopLossPrice = stopLossPrice;
	}
	public Double getStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(Double stopLoss) {
		this.stopLoss = stopLoss;
	}
	public Double getStopProfitPrice() {
		return stopProfitPrice;
	}
	public void setStopProfitPrice(Double stopProfitPrice) {
		this.stopProfitPrice = stopProfitPrice;
	}
	public Double getStopProfit() {
		return stopProfit;
	}
	public void setStopProfit(Double stopProfit) {
		this.stopProfit = stopProfit;
	}
	public Double getSysLossProfit() {
		return sysLossProfit;
	}
	public void setSysLossProfit(Double sysLossProfit) {
		this.sysLossProfit = DoubleUtils.scaleFormat(sysLossProfit, 2);
	}
	public Double getLossProfit() {
		return lossProfit;
	}
	public void setLossProfit(Double lossProfit) {
		this.lossProfit = DoubleUtils.scaleFormat(lossProfit, 2);
	}
	public Double getDeferInterest() {
		return deferInterest;
	}
	public void setDeferInterest(Double deferInterest) {
		this.deferInterest = DoubleUtils.scaleFormat(deferInterest, 2);
	}
	public Integer getDeferTimes() {
		return deferTimes;
	}
	public void setDeferTimes(Integer deferTimes) {
		this.deferTimes = deferTimes;
	}
	public Double getShouldHoldFund() {
		return shouldHoldFund;
	}
	public void setShouldHoldFund(Double shouldHoldFund) {
		this.shouldHoldFund = DoubleUtils.scaleFormat(shouldHoldFund,2);
	}
	public Double getShouldDeferFund() {
		return shouldDeferFund;
	}
	public void setShouldDeferFund(Double shouldDeferFund) {
		this.shouldDeferFund = DoubleUtils.scaleFormat(shouldDeferFund,2);
	}
	public Double getShouldCounterFee() {
		return shouldCounterFee;
	}
	public void setShouldCounterFee(Double shouldCounterFee) {
		this.shouldCounterFee = DoubleUtils.scaleFormat(shouldCounterFee,2);
	}
	public Double getShouldCouponFund() {
		return shouldCouponFund;
	}
	public void setShouldCouponFund(Double shouldCouponFund) {
		this.shouldCouponFund = DoubleUtils.scaleFormat(shouldCouponFund,2);
	}
	public Double getActualHoldFund() {
		return actualHoldFund;
	}
	public void setActualHoldFund(Double actualHoldFund) {
		this.actualHoldFund = DoubleUtils.scaleFormat(actualHoldFund,2);
	}
	public Double getActualDeferFund() {
		return actualDeferFund;
	}
	public void setActualDeferFund(Double actualDeferFund) {
		this.actualDeferFund = DoubleUtils.scaleFormat(actualDeferFund,2);
	}
	public Double getActualCounterFee() {
		return actualCounterFee;
	}
	public void setActualCounterFee(Double actualCounterFee) {
		this.actualCounterFee = DoubleUtils.scaleFormat(actualCounterFee,2);
	}
	public Double getActualCouponFund() {
		return actualCouponFund;
	}
	public void setActualCouponFund(Double actualCouponFund) {
		this.actualCouponFund =  DoubleUtils.scaleFormat(actualCouponFund,2);
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public Integer getBuyTriggerType() {
		return buyTriggerType;
	}
	public void setBuyTriggerType(Integer buyTriggerType) {
		this.buyTriggerType = buyTriggerType;
	}
	public Integer getSellTriggerType() {
		return sellTriggerType;
	}
	public void setSellTriggerType(Integer sellTriggerType) {
		this.sellTriggerType = sellTriggerType;
	}	
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Double getMoveStopLoss() {
		return moveStopLoss;
	}
	public void setMoveStopLoss(Double moveStopLoss) {
		this.moveStopLoss = moveStopLoss;
	}
	public Double getMoveStopLossPrice() {
		return moveStopLossPrice;
	}
	public void setMoveStopLossPrice(Double moveStopLossPrice) {
		this.moveStopLossPrice = moveStopLossPrice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getThisSysLossProfit() {
		return thisSysLossProfit;
	}

	public void setThisSysLossProfit(Double thisSysLossProfit) {
		this.thisSysLossProfit =DoubleUtils.scaleFormat(thisSysLossProfit, 2);
	}

	public Double getThisLossProfit() {
		return thisLossProfit;
	}

	public void setThisLossProfit(Double thisLossProfit) {
		this.thisLossProfit = DoubleUtils.scaleFormat(thisLossProfit, 2);
	}

	public Integer getPurchaseOrderType() {
		return purchaseOrderType;
	}

	public void setPurchaseOrderType(Integer purchaseOrderType) {
		this.purchaseOrderType = purchaseOrderType;
	}

	public Double getLossProfitRate() {
		return lossProfitRate;
	}

	public void setLossProfitRate(Double lossProfitRate) {
		this.lossProfitRate = lossProfitRate;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Double getBuyPriceTotal() {
		return buyPriceTotal;
	}

	public void setBuyPriceTotal(Double buyPriceTotal) {
		this.buyPriceTotal = buyPriceTotal;
	}

	public Double getSellPriceTotal() {
		return sellPriceTotal;
	}

	public void setSellPriceTotal(Double sellPriceTotal) {
		this.sellPriceTotal = sellPriceTotal;
	}

	public Double getBuyMinPrice() {
		return buyMinPrice;
	}

	public void setBuyMinPrice(Double buyMinPrice) {
		this.buyMinPrice = buyMinPrice;
	}

	public Double getSellMinPrice() {
		return sellMinPrice;
	}

	public void setSellMinPrice(Double sellMinPrice) {
		this.sellMinPrice = sellMinPrice;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public Double getMini() {
		return mini;
	}

	public void setMini(Double mini) {
		this.mini = mini;
	}
}
