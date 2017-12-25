package com.lt.manager.bean.user;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 *
 * 描述: 券商费用配置页面返回对象
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class InvestorFeeCfg extends BaseBean {
	

	/**
	 *  用户ID
	 */
	private String accountId;
	/**
	 *  产品ID
	 */
	private Integer productId;
	/**
	 *  产品名称
	 */
	private String productName;
	
	/**
	 *  产品名称
	 */
	private String shortCode;
	/**
	 *  内外盘类型
	 */
	private Integer plate;
	/**
	 *  券商手续费
	 */
	private Double investorCounterfee;
	/**
	 *  平台收取手续费
	 */
	private Double platformCounterfee;
	/**
	 *  平台手续费收取方向
	 */
	private Double platformCounterFeeTarget;
	/**
	 *  手续费收取方式
	 */
	private Double counterFeeType;
	/**
	 *  券商分红
	 */
	private Double investorBouns;
	/**
	 *  平台分红
	 */
	private Double platformBouns;
	
	/**
	 *  分红方式
	 */
	private Integer bounsType;
	/**
	 *  平台分红收取方式
	 */
	private Integer platformBounsTarget;

	/**
	 *  券商止盈区间
	 */
	private String stopProfitRange;
	/**
	 *  券商止损配置区间
	 */
	private String stopLossRange;
	
	/**
	 *  券商手数区间
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
	/**
	 *  保证金参数
	 */
	private Double surcharge;
	/**
	 *  是否支持递延
	 */
	private  Integer supportDirect;
	/**
	 *  是否支持递延
	 */
	private  Integer isSupportDefer;
	
	/**
	 *  递延保证金
	 */
	private Double deferFund;
	/**
	 *  递延费
	 */
	private Double deferFee;
	/**
	 * 创建日期
	 */
	private Date createDate;
	/**
	 * 创建日期
	 */
	private Date startTime;
	/**
	 * 创建日期
	 */
	private Date endTime;
	/**
	 *  创建人
	 */
	private String creater;
	
	/**
	 *  修改日期
	 */
	private Date modifyDate;
	/**
	 *  修改人
	 */
	private Integer modifyId;

	/**
	 *  是否为配置模板
	 */
	private Integer isModel;
	
	private String productCode;
	
	private Integer productType;
	
	private Double minSurcharge;
	
	private Double maxSurcharge;
	
	private Double minCounterFee;
	private Double maxCounterFee;
	private Double minDeferFee;
	private Double maxDeferFee;
	private Double minDeferFund;
	private Double maxDeferFund;
	private String currency;
	private String unit;
	private String sign;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public Integer getPlate() {
		return plate;
	}
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	public Double getInvestorCounterfee() {
		return investorCounterfee;
	}
	public void setInvestorCounterfee(Double investorCounterfee) {
		this.investorCounterfee = investorCounterfee;
	}
	public Double getPlatformCounterfee() {
		return platformCounterfee == null?0:platformCounterfee;
	}
	public void setPlatformCounterfee(Double platformCounterfee) {
		this.platformCounterfee = platformCounterfee;
	}
	public Double getPlatformCounterFeeTarget() {
		return platformCounterFeeTarget;
	}
	public void setPlatformCounterFeeTarget(Double platformCounterFeeTarget) {
		this.platformCounterFeeTarget = platformCounterFeeTarget;
	}
	public Double getCounterFeeType() {
		return counterFeeType;
	}
	public void setCounterFeeType(Double counterFeeType) {
		this.counterFeeType = counterFeeType;
	}
	public Double getInvestorBouns() {
		return investorBouns;
	}
	public void setInvestorBouns(Double investorBouns) {
		this.investorBouns = investorBouns;
	}
	public Double getPlatformBouns() {
		return platformBouns;
	}
	public void setPlatformBouns(Double platformBouns) {
		this.platformBouns = platformBouns;
	}
	public Integer getBounsType() {
		return bounsType;
	}
	public void setBounsType(Integer bounsType) {
		this.bounsType = bounsType;
	}
	public Integer getPlatformBounsTarget() {
		return platformBounsTarget;
	}
	public void setPlatformBounsTarget(Integer platformBounsTarget) {
		this.platformBounsTarget = platformBounsTarget;
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
	public Integer getIsSupportDefer() {
		return isSupportDefer;
	}
	public void setIsSupportDefer(Integer isSupportDefer) {
		this.isSupportDefer = isSupportDefer;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getModifyId() {
		return modifyId;
	}
	public void setModifyId(Integer modifyId) {
		this.modifyId = modifyId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getIsModel() {
		return isModel;
	}
	public void setIsModel(Integer isModel) {
		this.isModel = isModel;
	}
	public Integer getSupportDirect() {
		return supportDirect;
	}
	public void setSupportDirect(Integer supportDirect) {
		this.supportDirect = supportDirect;
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

 }


