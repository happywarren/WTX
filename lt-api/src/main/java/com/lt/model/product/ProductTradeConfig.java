package com.lt.model.product;

import java.util.Date;


/**
 * 商品配资表
 * @author jingwb
 *
 */
public class ProductTradeConfig{

	private Integer id;
	private String ids;
	private Integer productId; //商品id
	private Double maxStopProfit;//最大止盈
	private Double maxStopLoss;//最大止损
	private Integer maxMultiple;//最大手数
	private Date createDate;//创建时间
	private Integer createUserId;//创建人
	private Date modifyDate;//修改时间
	private String modifyUserId;//修改人
	private Double jumpPrice;//最小变动价格
	private Double minStopProfit;//最小止盈
	private Double minStopLoss;//最小止损
	private Double minSurcharge;//最小保证金参数
	private Double maxSurcharge;//最大保证金参数
	private Double jumpValue;//最小波动点，如 0.01
	private Integer minMultiple;//最小手数
	private Double minDeferFund;//最小递延保证金
	private Double maxDeferFund;//最大递延保证金
	private Double minDeferFee;//最小递延费
	private Double maxDeferFee;//最大递延费
	private Double minCounterFee;//最小手续费
	private Double maxCounterFee;//最大手续费
	private Integer isDefer;//是否开启递延0：关闭1：开启
	private Double floatLimit;//行情浮动限制
	private Integer isMarketPrice;//是否支持市价，0：否 1：是
	private Double limitedPriceValue;//限价加减点位
	/**
	 *  止盈区间 6 档
	 */
	private String stopProfitRange;
	/**
	 *  止损配置6 档
	 */
	private String stopLossRange;
	
	/**
	 *  手数6 档
	 */
	
	private String multipleRange;
	/**
	 *  默认手数
	 */
	private  Integer defaultCount;
	/**
	 *  默认止盈
	 */
	private Double defaultStopProfit;
	/**
	 *  默认止损
	 */
	private Double defaultStopLoss;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Double getMaxStopProfit() {
		return maxStopProfit;
	}
	public void setMaxStopProfit(Double maxStopProfit) {
		this.maxStopProfit = maxStopProfit;
	}
	public Double getMaxStopLoss() {
		return maxStopLoss;
	}
	public void setMaxStopLoss(Double maxStopLoss) {
		this.maxStopLoss = maxStopLoss;
	}
	public Integer getMaxMultiple() {
		return maxMultiple;
	}
	public void setMaxMultiple(Integer maxMultiple) {
		this.maxMultiple = maxMultiple;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public Double getJumpPrice() {
		return jumpPrice;
	}
	public void setJumpPrice(Double jumpPrice) {
		this.jumpPrice = jumpPrice;
	}
	public Double getMinStopProfit() {
		return minStopProfit;
	}
	public void setMinStopProfit(Double minStopProfit) {
		this.minStopProfit = minStopProfit;
	}
	public Double getMinStopLoss() {
		return minStopLoss;
	}
	public void setMinStopLoss(Double minStopLoss) {
		this.minStopLoss = minStopLoss;
	}
	public Double getMinSurcharge() {
		return minSurcharge;
	}
	public void setMinSurcharge(Double minSurcharge) {
		this.minSurcharge = minSurcharge;
	}
	public Double getMaxSurcharge() {
		return maxSurcharge;
	}
	public void setMaxSurcharge(Double maxSurcharge) {
		this.maxSurcharge = maxSurcharge;
	}
	public Double getJumpValue() {
		return jumpValue;
	}
	public void setJumpValue(Double jumpValue) {
		this.jumpValue = jumpValue;
	}
	public Integer getMinMultiple() {
		return minMultiple;
	}
	public void setMinMultiple(Integer minMultiple) {
		this.minMultiple = minMultiple;
	}
	public Double getMinDeferFund() {
		return minDeferFund;
	}
	public void setMinDeferFund(Double minDeferFund) {
		this.minDeferFund = minDeferFund;
	}
	public Double getMaxDeferFund() {
		return maxDeferFund;
	}
	public void setMaxDeferFund(Double maxDeferFund) {
		this.maxDeferFund = maxDeferFund;
	}
	public Double getMinDeferFee() {
		return minDeferFee;
	}
	public void setMinDeferFee(Double minDeferFee) {
		this.minDeferFee = minDeferFee;
	}
	public Double getMaxDeferFee() {
		return maxDeferFee;
	}
	public void setMaxDeferFee(Double maxDeferFee) {
		this.maxDeferFee = maxDeferFee;
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
	public Integer getIsDefer() {
		return isDefer;
	}
	public void setIsDefer(Integer isDefer) {
		this.isDefer = isDefer;
	}
	public Double getFloatLimit() {
		return floatLimit;
	}
	public void setFloatLimit(Double floatLimit) {
		this.floatLimit = floatLimit;
	}
	public Integer getIsMarketPrice() {
		return isMarketPrice;
	}
	public void setIsMarketPrice(Integer isMarketPrice) {
		this.isMarketPrice = isMarketPrice;
	}
	public Double getLimitedPriceValue() {
		return limitedPriceValue;
	}
	public void setLimitedPriceValue(Double limitedPriceValue) {
		this.limitedPriceValue = limitedPriceValue;
	}
	public String getStopProfitRange() {
		return stopProfitRange;
	}
	public void setStopProfitRange(String stopProfitRange) {
		this.stopProfitRange = stopProfitRange;
	}
	public String getStopLossRange() {
		return stopLossRange;
	}
	public void setStopLossRange(String stopLossRange) {
		this.stopLossRange = stopLossRange;
	}
	public String getMultipleRange() {
		return multipleRange;
	}
	public void setMultipleRange(String multipleRange) {
		this.multipleRange = multipleRange;
	}
	public Integer getDefaultCount() {
		return defaultCount;
	}
	public void setDefaultCount(Integer defaultCount) {
		this.defaultCount = defaultCount;
	}
	public Double getDefaultStopProfit() {
		return defaultStopProfit;
	}
	public void setDefaultStopProfit(Double defaultStopProfit) {
		this.defaultStopProfit = defaultStopProfit;
	}
	public Double getDefaultStopLoss() {
		return defaultStopLoss;
	}
	public void setDefaultStopLoss(Double defaultStopLoss) {
		this.defaultStopLoss = defaultStopLoss;
	}

}
