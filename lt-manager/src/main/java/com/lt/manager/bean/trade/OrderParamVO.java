package com.lt.manager.bean.trade;

import com.lt.manager.bean.BaseBean;

public class OrderParamVO extends BaseBean{

	private static final long serialVersionUID = 1665674019784890661L;

	/**订单id*/
	private String orderId;
	/** 用户ID */
	private String userId;
	/**昵称*/
	private String nickName;
	/**手机号*/
	private String tele;
	/**品种编码：CL*/
	private String shortCode;	
	/** 券商ID */
	private String investorId;
	/** 订单展示ID */
	private String displayId;
	/** 交易类型: 1 看多, 2 看空 */
	private Integer tradeDirection;
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
	private Double actualHoldFund;
	private Double shouldHoldFund;
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
	/** 结算盈亏  */
	private Double lossProfitRate;
	/** 触发止损金额 */
	private Double stopLoss;
	/**每手止损金额（原币种）*/
	private Double perStopLoss;
	/** 每手止盈金额（原币种） */
	private Double perStopProfit;
	/** 单手手续费（原币种） */
	private Double perCounterFee;
	/** 单手保证金参数（原币种） */
	private Double perSurcharge;
	/** 单手递延保证金（原币种） */
	private Double perDeferFund;
	/** 单手递延费（原币种） */
	private Double perDeferInterest;
	/** 触发止盈金额 */
	private Double stopProfit;
	/** 止损价格 */
	private Double stopLossPrice;
	/** 止盈价 */
	private Double stopProfitPrice;
	/** 数量（手数） */
	private Integer buyEntrustCount;
	/** 单位 */
	private String unit;
	/** 用户提交买入价 */
	private Double userCommitBuyPrice;
	/** 用提交买入时间 */
	private String userCommitBuyDate;
	/** 系统提交买入价 */
	private Double entrustBuyPrice;
	/** 系统提交买入时间 */
	private String entrustBuyDate;
	/** 订单创建时间 */
	private String createDate;
	/** 订单修改时间 */
	private String modifyDate;
	/** 用户提交卖出价 */
	private Double userCommitSellPrice;
	/** 用户提交卖出时间 */
	private String userCommitSellDate;
	/** 系统提交卖出价 */
	private Double entrustSellPrice;
	/** 系统提交卖出时间 */
	private String entrustSellDate;
	/** 100客户触发，101管理员触发、102系统清仓、103风控触发、200其他 */
	private Integer sellTriggerType;
	/** 结算时间 */
	private String balanceDate;
	/** 系统清仓时间 */
	private String sysSetSellDate;
	/** 系统结算收益(卖的差价收益) */
	private Double sysLossProfit;
	/** 币种 */
	private String currency;
	/** 汇率(人民币汇率) */
	private Double rate;
	/** 持仓递延状态（1:递延; 0:非递延） */
	private Integer deferStatus;
	/** 递延保证金 */
	private Double shouldDeferFund;
	private Double actualDeferFund;
	/** 递延利息 */
	private Double deferInterest;
	/** 递延次数 */
	private Integer deferTimes;
	/** 移动止损开关: 0.关; 1.开 */
	private Integer trailStopLoss;
	/** 显示状态: 0 隐藏; 1 显示 */
	private Integer display = 1;
	/** 订单状态：1.开仓委托中; 2.平仓委托中; 3.持仓中; 4.完成,5失败 */
	private Integer status;
	/** 开仓成交均价(关联cash_order_count表) */
	private Double buyAvgPrice;
	/**系统卖出均价*/
	private Double sellAvgPrice;
	/** 开仓成交时间(关联cash_order_count表) */
	private String lastBuyDate;
	private String lastSellDate;
	private Integer holdCount;//持仓数量
	private Integer sellSuccessCount;//平仓成功手数
	private Integer successCount;//成交手数
	private Double successPrice;//成交价
	private Integer sellEntrustCount;//平仓委托手数
	private String exSuccessId;//交易所返回委托单号
	private String exEntrustId;
	private Integer totalSuccessCount;//已成交手数
	/**==========附加字段==============*/

	private String tradeTypeFmt;//看多 看空
	private String deferStatusFmt;//是否递延
	private String statusFmt;//状态（中文）
	private String entrustId;//委托单id
	private String successId;//成交编号
	private String successDate;//成交时间
	private String expirationTime;//合约到期时间
	private String trailStopLossFmt;//移动止损
	private String balanceTypeFmt;//结算方式
	private String displayFmt;//显示隐藏
	private Integer entrustCount;
	private String entrustDate;//委托时间
	private Integer triggerType;//触发方式
	private String triggerTypeFmt;//触发方式
	private Integer succCount;
	private Integer failCount;
	private String userTradeTypeFmt;//开仓，平仓
	private String remark;
	private String sign;
	private Double floatLossProfit;//浮动盈亏
	private Double floatLossProfitRate;//浮动盈亏
	private Double jumpPrice;
	private Double jumpValue;
	private Integer entrustType;
	private Double entrustPrice;
	private Integer accountId;
	
	private Double minBuyPrice;
	private Double maxBuyPrice;
	private Double minSellPrice;
	private Double maxSellPrice;
	private Double minHoldFund;
	private Double maxHoldFund;
	private Double minCounterFee;
	private Double maxCounterFee;
	private String beginBuyDate;
	private String endBuyDate;
	private String beginSellDate;
	private String endSellDate;
	private String beginEntrustDate;
	private String endEntrustDate;
	private String beginSuccessDate;
	private String endSuccessDate;
	private Integer buyFailCount;
	private Integer buySuccessCount;
	private Integer buyTriggerType;
	private Integer successStatus;
	//移动止损价格
	private Double moveStopLossPrice;
	//移动止损金额
	private Double moveStopLoss;
	/**记录下单方式：0:市价单 1:条件单 2:闪电单*/
	private Integer purchaseOrderType;
	
	/**错误编码*/
	private String errorCode;
	/**错误信息*/
	private String errorMsg;
	/**修改人*/
	private String modifyUserId;
	/**最小结算盈亏*/
	private Double minLossProfit;
	/**最大结算盈亏*/
	private Double maxLossProfit;
	/**最小实扣递延保证金*/
	private Double minActualDeferFund;
	/**最大实扣递延保证金*/
	private Double maxActualDeferFund;
	/**最小递延费*/
	private Double minDeferInterest;
	/**最大递延费*/
	private Double maxDeferInterest;
	//品牌 id
	private String brandId;
	//商品类型id
	private Integer productTypeId;

	public Integer getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public String getDisplayId() {
		return displayId;
	}
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
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
	public String getExchangeCode() {
		return exchangeCode;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public Integer getPlate() {
		return plate;
	}
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public Double getShouldCounterFee() {
		return shouldCounterFee;
	}
	public void setShouldCounterFee(Double shouldCounterFee) {
		this.shouldCounterFee = shouldCounterFee;
	}
	public Double getActualCounterFee() {
		return actualCounterFee;
	}
	public void setActualCounterFee(Double actualCounterFee) {
		this.actualCounterFee = actualCounterFee;
	}
	public Double getPlatformFee() {
		return platformFee;
	}
	public void setPlatformFee(Double platformFee) {
		this.platformFee = platformFee;
	}
	public Double getInvestorFee() {
		return investorFee;
	}
	public void setInvestorFee(Double investorFee) {
		this.investorFee = investorFee;
	}
	public Double getLossProfit() {
		return lossProfit;
	}
	public void setLossProfit(Double lossProfit) {
		this.lossProfit = lossProfit;
	}
	public Double getStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(Double stopLoss) {
		this.stopLoss = stopLoss;
	}
	public Double getStopProfit() {
		return stopProfit;
	}
	public void setStopProfit(Double stopProfit) {
		this.stopProfit = stopProfit;
	}
	public Double getStopLossPrice() {
		return stopLossPrice;
	}
	public void setStopLossPrice(Double stopLossPrice) {
		this.stopLossPrice = stopLossPrice;
	}
	public Double getStopProfitPrice() {
		return stopProfitPrice;
	}
	public void setStopProfitPrice(Double stopProfitPrice) {
		this.stopProfitPrice = stopProfitPrice;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getUserCommitBuyPrice() {
		return userCommitBuyPrice;
	}
	public void setUserCommitBuyPrice(Double userCommitBuyPrice) {
		this.userCommitBuyPrice = userCommitBuyPrice;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Double getSysLossProfit() {
		return sysLossProfit;
	}
	public void setSysLossProfit(Double sysLossProfit) {
		this.sysLossProfit = sysLossProfit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
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
	public Integer getDisplay() {
		return display;
	}
	public void setDisplay(Integer display) {
		this.display = display;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTradeTypeFmt() {
		return tradeTypeFmt;
	}
	public void setTradeTypeFmt(String tradeTypeFmt) {
		this.tradeTypeFmt = tradeTypeFmt;
	}
	public String getDeferStatusFmt() {
		return deferStatusFmt;
	}
	public void setDeferStatusFmt(String deferStatusFmt) {
		this.deferStatusFmt = deferStatusFmt;
	}
	public String getStatusFmt() {
		return statusFmt;
	}
	public void setStatusFmt(String statusFmt) {
		this.statusFmt = statusFmt;
	}
	
	public String getUserTradeTypeFmt() {
		return userTradeTypeFmt;
	}
	public void setUserTradeTypeFmt(String userTradeTypeFmt) {
		this.userTradeTypeFmt = userTradeTypeFmt;
	}
	public String getEntrustId() {
		return entrustId;
	}
	public void setEntrustId(String entrustId) {
		this.entrustId = entrustId;
	}
	public String getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	public String getTrailStopLossFmt() {
		return trailStopLossFmt;
	}
	public void setTrailStopLossFmt(String trailStopLossFmt) {
		this.trailStopLossFmt = trailStopLossFmt;
	}
	public String getBalanceTypeFmt() {
		return balanceTypeFmt;
	}
	public void setBalanceTypeFmt(String balanceTypeFmt) {
		this.balanceTypeFmt = balanceTypeFmt;
	}
	public String getDisplayFmt() {
		return displayFmt;
	}
	public void setDisplayFmt(String displayFmt) {
		this.displayFmt = displayFmt;
	}
	public Integer getHoldCount() {
		return holdCount;
	}
	public void setHoldCount(Integer holdCount) {
		this.holdCount = holdCount;
	}
	public Integer getEntrustCount() {
		return entrustCount;
	}
	public void setEntrustCount(Integer entrustCount) {
		this.entrustCount = entrustCount;
	}
	public Integer getSuccCount() {
		return succCount;
	}
	public void setSuccCount(Integer succCount) {
		this.succCount = succCount;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getSellEntrustCount() {
		return sellEntrustCount;
	}
	public void setSellEntrustCount(Integer sellEntrustCount) {
		this.sellEntrustCount = sellEntrustCount;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getUserCommitBuyDate() {
		return userCommitBuyDate;
	}
	public void setUserCommitBuyDate(String userCommitBuyDate) {
		this.userCommitBuyDate = userCommitBuyDate;
	}
	
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public String getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(String balanceDate) {
		this.balanceDate = balanceDate;
	}
	public Double getFloatLossProfit() {
		return floatLossProfit;
	}
	public void setFloatLossProfit(Double floatLossProfit) {
		this.floatLossProfit = floatLossProfit;
	}
	public Double getJumpPrice() {
		return jumpPrice;
	}
	public void setJumpPrice(Double jumpPrice) {
		this.jumpPrice = jumpPrice;
	}
	public Double getJumpValue() {
		return jumpValue;
	}
	public void setJumpValue(Double jumpValue) {
		this.jumpValue = jumpValue;
	}
	public Integer getTradeDirection() {
		return tradeDirection;
	}
	public void setTradeDirection(Integer tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	public Double getActualHoldFund() {
		return actualHoldFund;
	}
	public void setActualHoldFund(Double actualHoldFund) {
		this.actualHoldFund = actualHoldFund;
	}
	public Double getShouldHoldFund() {
		return shouldHoldFund;
	}
	public void setShouldHoldFund(Double shouldHoldFund) {
		this.shouldHoldFund = shouldHoldFund;
	}
	public Integer getBuyEntrustCount() {
		return buyEntrustCount;
	}
	public void setBuyEntrustCount(Integer buyEntrustCount) {
		this.buyEntrustCount = buyEntrustCount;
	}
	public Double getEntrustBuyPrice() {
		return entrustBuyPrice;
	}
	public void setEntrustBuyPrice(Double entrustBuyPrice) {
		this.entrustBuyPrice = entrustBuyPrice;
	}
	public String getEntrustBuyDate() {
		return entrustBuyDate;
	}
	public void setEntrustBuyDate(String entrustBuyDate) {
		this.entrustBuyDate = entrustBuyDate;
	}
	public Double getUserCommitSellPrice() {
		return userCommitSellPrice;
	}
	public void setUserCommitSellPrice(Double userCommitSellPrice) {
		this.userCommitSellPrice = userCommitSellPrice;
	}
	public String getUserCommitSellDate() {
		return userCommitSellDate;
	}
	public void setUserCommitSellDate(String userCommitSellDate) {
		this.userCommitSellDate = userCommitSellDate;
	}
	public Double getEntrustSellPrice() {
		return entrustSellPrice;
	}
	public void setEntrustSellPrice(Double entrustSellPrice) {
		this.entrustSellPrice = entrustSellPrice;
	}
	public String getEntrustSellDate() {
		return entrustSellDate;
	}
	public void setEntrustSellDate(String entrustSellDate) {
		this.entrustSellDate = entrustSellDate;
	}
	public Integer getSellTriggerType() {
		return sellTriggerType;
	}
	public void setSellTriggerType(Integer sellTriggerType) {
		this.sellTriggerType = sellTriggerType;
	}
	public String getSysSetSellDate() {
		return sysSetSellDate;
	}
	public void setSysSetSellDate(String sysSetSellDate) {
		this.sysSetSellDate = sysSetSellDate;
	}
	public Double getShouldDeferFund() {
		return shouldDeferFund;
	}
	public void setShouldDeferFund(Double shouldDeferFund) {
		this.shouldDeferFund = shouldDeferFund;
	}
	public Double getActualDeferFund() {
		return actualDeferFund;
	}
	public void setActualDeferFund(Double actualDeferFund) {
		this.actualDeferFund = actualDeferFund;
	}
	public Double getBuyAvgPrice() {
		return buyAvgPrice;
	}
	public void setBuyAvgPrice(Double buyAvgPrice) {
		this.buyAvgPrice = buyAvgPrice;
	}
	public Double getSellAvgPrice() {
		return sellAvgPrice;
	}
	public void setSellAvgPrice(Double sellAvgPrice) {
		this.sellAvgPrice = sellAvgPrice;
	}
	public String getLastBuyDate() {
		return lastBuyDate;
	}
	public void setLastBuyDate(String lastBuyDate) {
		this.lastBuyDate = lastBuyDate;
	}
	public Integer getSellSuccessCount() {
		return sellSuccessCount;
	}
	public void setSellSuccessCount(Integer sellSuccessCount) {
		this.sellSuccessCount = sellSuccessCount;
	}
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public Double getSuccessPrice() {
		return successPrice;
	}
	public void setSuccessPrice(Double successPrice) {
		this.successPrice = successPrice;
	}
	public String getExSuccessId() {
		return exSuccessId;
	}
	public void setExSuccessId(String exSuccessId) {
		this.exSuccessId = exSuccessId;
	}
	public String getExEntrustId() {
		return exEntrustId;
	}
	public void setExEntrustId(String exEntrustId) {
		this.exEntrustId = exEntrustId;
	}
	public Integer getTotalSuccessCount() {
		return totalSuccessCount;
	}
	public void setTotalSuccessCount(Integer totalSuccessCount) {
		this.totalSuccessCount = totalSuccessCount;
	}
	public String getSuccessId() {
		return successId;
	}
	public void setSuccessId(String successId) {
		this.successId = successId;
	}
	public String getSuccessDate() {
		return successDate;
	}
	public void setSuccessDate(String successDate) {
		this.successDate = successDate;
	}
	public Integer getTradeType() {
		return tradeType;
	}
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
	
	public String getEntrustDate() {
		return entrustDate;
	}
	public void setEntrustDate(String entrustDate) {
		this.entrustDate = entrustDate;
	}
	public Integer getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public String getTriggerTypeFmt() {
		return triggerTypeFmt;
	}
	public void setTriggerTypeFmt(String triggerTypeFmt) {
		this.triggerTypeFmt = triggerTypeFmt;
	}
	public Integer getEntrustType() {
		return entrustType;
	}
	public void setEntrustType(Integer entrustType) {
		this.entrustType = entrustType;
	}
	public Double getEntrustPrice() {
		return entrustPrice;
	}
	public void setEntrustPrice(Double entrustPrice) {
		this.entrustPrice = entrustPrice;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Double getMinBuyPrice() {
		return minBuyPrice;
	}
	public void setMinBuyPrice(Double minBuyPrice) {
		this.minBuyPrice = minBuyPrice;
	}
	public Double getMaxBuyPrice() {
		return maxBuyPrice;
	}
	public void setMaxBuyPrice(Double maxBuyPrice) {
		this.maxBuyPrice = maxBuyPrice;
	}
	public Double getMinSellPrice() {
		return minSellPrice;
	}
	public void setMinSellPrice(Double minSellPrice) {
		this.minSellPrice = minSellPrice;
	}
	public Double getMaxSellPrice() {
		return maxSellPrice;
	}
	public void setMaxSellPrice(Double maxSellPrice) {
		this.maxSellPrice = maxSellPrice;
	}
	public Double getMinHoldFund() {
		return minHoldFund;
	}
	public void setMinHoldFund(Double minHoldFund) {
		this.minHoldFund = minHoldFund;
	}
	public Double getMaxHoldFund() {
		return maxHoldFund;
	}
	public void setMaxHoldFund(Double maxHoldFund) {
		this.maxHoldFund = maxHoldFund;
	}
	public Double getMinCounterFee() {
		return minCounterFee;
	}
	public void setMinCounterFee(Double minCounterFee) {
		this.minCounterFee = minCounterFee;
	}
	public Double getMaxCounterFee() {
		return maxCounterFee;
	}
	public void setMaxCounterFee(Double maxCounterFee) {
		this.maxCounterFee = maxCounterFee;
	}
	public String getBeginBuyDate() {
		return beginBuyDate;
	}
	public void setBeginBuyDate(String beginBuyDate) {
		this.beginBuyDate = beginBuyDate;
	}
	public String getEndBuyDate() {
		return endBuyDate;
	}
	public void setEndBuyDate(String endBuyDate) {
		this.endBuyDate = endBuyDate;
	}
	public String getBeginSellDate() {
		return beginSellDate;
	}
	public void setBeginSellDate(String beginSellDate) {
		this.beginSellDate = beginSellDate;
	}
	public String getEndSellDate() {
		return endSellDate;
	}
	public void setEndSellDate(String endSellDate) {
		this.endSellDate = endSellDate;
	}
	public String getBeginEntrustDate() {
		return beginEntrustDate;
	}
	public void setBeginEntrustDate(String beginEntrustDate) {
		this.beginEntrustDate = beginEntrustDate;
	}
	public String getEndEntrustDate() {
		return endEntrustDate;
	}
	public void setEndEntrustDate(String endEntrustDate) {
		this.endEntrustDate = endEntrustDate;
	}
	public String getBeginSuccessDate() {
		return beginSuccessDate;
	}
	public void setBeginSuccessDate(String beginSuccessDate) {
		this.beginSuccessDate = beginSuccessDate;
	}
	public String getEndSuccessDate() {
		return endSuccessDate;
	}
	public void setEndSuccessDate(String endSuccessDate) {
		this.endSuccessDate = endSuccessDate;
	}
	public Integer getBuyFailCount() {
		return buyFailCount;
	}
	public void setBuyFailCount(Integer buyFailCount) {
		this.buyFailCount = buyFailCount;
	}
	public Integer getBuySuccessCount() {
		return buySuccessCount;
	}
	public void setBuySuccessCount(Integer buySuccessCount) {
		this.buySuccessCount = buySuccessCount;
	}
	public Integer getBuyTriggerType() {
		return buyTriggerType;
	}
	public void setBuyTriggerType(Integer buyTriggerType) {
		this.buyTriggerType = buyTriggerType;
	}
	public Integer getSuccessStatus() {
		return successStatus;
	}
	public void setSuccessStatus(Integer successStatus) {
		this.successStatus = successStatus;
	}
	
	public Double getMoveStopLossPrice() {
		return moveStopLossPrice;
	}
	public void setMoveStopLossPrice(Double moveStopLossPrice) {
		this.moveStopLossPrice = moveStopLossPrice;
	}
	public Double getMoveStopLoss() {
		return moveStopLoss;
	}
	public void setMoveStopLoss(Double moveStopLoss) {
		this.moveStopLoss = moveStopLoss;
	}
	public Double getPerStopLoss() {
		return perStopLoss;
	}
	public void setPerStopLoss(Double perStopLoss) {
		this.perStopLoss = perStopLoss;
	}
	public String getLastSellDate() {
		return lastSellDate;
	}
	public void setLastSellDate(String lastSellDate) {
		this.lastSellDate = lastSellDate;
	}
	public Double getPerStopProfit() {
		return perStopProfit;
	}
	public void setPerStopProfit(Double perStopProfit) {
		this.perStopProfit = perStopProfit;
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
	public Double getFloatLossProfitRate() {
		return floatLossProfitRate;
	}
	public void setFloatLossProfitRate(Double floatLossProfitRate) {
		this.floatLossProfitRate = floatLossProfitRate;
	}
	public Double getLossProfitRate() {
		return lossProfitRate;
	}
	public void setLossProfitRate(Double lossProfitRate) {
		this.lossProfitRate = lossProfitRate;
	}
	public Integer getPurchaseOrderType() {
		return purchaseOrderType;
	}
	public void setPurchaseOrderType(Integer purchaseOrderType) {
		this.purchaseOrderType = purchaseOrderType;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public Double getMinLossProfit() {
		return minLossProfit;
	}
	public void setMinLossProfit(Double minLossProfit) {
		this.minLossProfit = minLossProfit;
	}
	public Double getMaxLossProfit() {
		return maxLossProfit;
	}
	public void setMaxLossProfit(Double maxLossProfit) {
		this.maxLossProfit = maxLossProfit;
	}
	public Double getMinActualDeferFund() {
		return minActualDeferFund;
	}
	public void setMinActualDeferFund(Double minActualDeferFund) {
		this.minActualDeferFund = minActualDeferFund;
	}
	public Double getMaxActualDeferFund() {
		return maxActualDeferFund;
	}
	public void setMaxActualDeferFund(Double maxActualDeferFund) {
		this.maxActualDeferFund = maxActualDeferFund;
	}
	public Double getMinDeferInterest() {
		return minDeferInterest;
	}
	public void setMinDeferInterest(Double minDeferInterest) {
		this.minDeferInterest = minDeferInterest;
	}
	public Double getMaxDeferInterest() {
		return maxDeferInterest;
	}
	public void setMaxDeferInterest(Double maxDeferInterest) {
		this.maxDeferInterest = maxDeferInterest;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
