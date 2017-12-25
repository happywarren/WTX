package com.lt.model.product;

import java.io.Serializable;

/**
 * 商品杠杆配置
 * @author jingwb
 *
 */
public class ProductRiskConfig implements Serializable{

	private Integer id;
	/**商品id*/
	private Integer productId;
	/**风险等级 如1：极低、2：低、3：中、4：高、5：极高*/
	private Integer  riskLevel;
	/**默认保证金参数*/
	private Double surcharge;
	/**默认递延保证金*/
	private Double deferFund;
	/**默认递延费*/
	private Double deferFee;
	/**止盈区间 6 档*/
	private String stopProfitRange;
	/**止损配置6 档*/
	private String stopLossRange;
	/**手数6 档*/
	private String multipleRange;
	/**默认手数*/
	private Integer defaultCount;
	/** 默认止盈*/
	private Double defaultStopProfit;
	/**默认止损*/
	private Double defaultStopLoss;
	/**是否开启递延1：关闭 2：开启*/
	private Integer isDefer;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}
	public Double getSurcharge() {
		return surcharge;
	}
	public void setSurcharge(Double surcharge) {
		this.surcharge = surcharge;
	}
	public Double getDeferFund() {
		return deferFund;
	}
	public void setDeferFund(Double deferFund) {
		this.deferFund = deferFund;
	}
	public Double getDeferFee() {
		return deferFee;
	}
	public void setDeferFee(Double deferFee) {
		this.deferFee = deferFee;
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
	public Integer getIsDefer() {
		return isDefer;
	}
	public void setIsDefer(Integer isDefer) {
		this.isDefer = isDefer;
	}

}
