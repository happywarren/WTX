package com.lt.model.user;

import java.io.Serializable;
import java.util.Date;



/**
 *
 * 描述: 券商费用配置页面返回对象
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class InvestorFeeCfg implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5002565418466339323L;
	/**
	 *  ID 
	 */
	private Integer id;
	/**
	 *  用户ID
	 */
	private String accountId;
	/**
	 *  产品ID
	 */
	private Integer productId;
	/**
	 *  证券账号编码
	 */
	private String securityCode;
	
	private Integer securityId;
	
	/**
	 *  证券账号编码
	 */
	private String serverIp;
	/**
	 *  证券账号编码
	 */
	private String serverPort;
	/**
	 *  证券账号编码
	 */
	private String passwd;
	
	/**
	 *  证券账号编码
	 */
	private Integer weight ;
	
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
	private Integer counterFeeType;
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
	 *  创建人
	 */
	private String creater;
	
	/**
	 *  修改人
	 */
	private Integer modifyDate;
	/**
	 *  修改日期
	 */
	private Double modifyId;

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
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Double getInvestorCounterfee() {
		return investorCounterfee;
	}
	public void setInvestorCounterfee(Double investorCounterfee) {
		this.investorCounterfee = investorCounterfee;
	}
	public Double getPlatformCounterfee() {
		return platformCounterfee;
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
	public Integer getCounterFeeType() {
		return counterFeeType;
	}
	public void setCounterFeeType(Integer counterFeeType) {
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
	public Integer getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Integer modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Double getModifyId() {
		return modifyId;
	}
	public void setModifyId(Double modifyId) {
		this.modifyId = modifyId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSecurityId() {
		return securityId;
	}
	public void setSecurityId(Integer securityId) {
		this.securityId = securityId;
	}

	@Override
	public String toString() {
		return "InvestorFeeCfg{" +
				"id=" + id +
				", accountId='" + accountId + '\'' +
				", productId=" + productId +
				", securityCode='" + securityCode + '\'' +
				", securityId=" + securityId +
				", serverIp='" + serverIp + '\'' +
				", serverPort='" + serverPort + '\'' +
				", passwd='" + passwd + '\'' +
				", weight=" + weight +
				", investorCounterfee=" + investorCounterfee +
				", platformCounterfee=" + platformCounterfee +
				", platformCounterFeeTarget=" + platformCounterFeeTarget +
				", counterFeeType=" + counterFeeType +
				", investorBouns=" + investorBouns +
				", platformBouns=" + platformBouns +
				", bounsType=" + bounsType +
				", platformBounsTarget=" + platformBounsTarget +
				", stopProfitRange='" + stopProfitRange + '\'' +
				", stopLossRange='" + stopLossRange + '\'' +
				", multipleRange='" + multipleRange + '\'' +
				", defaultCount=" + defaultCount +
				", defaultStopProfit=" + defaultStopProfit +
				", defaultStopLoss=" + defaultStopLoss +
				", surcharge=" + surcharge +
				", isSupportDefer=" + isSupportDefer +
				", deferFund=" + deferFund +
				", deferFee=" + deferFee +
				", createDate=" + createDate +
				", creater='" + creater + '\'' +
				", modifyDate=" + modifyDate +
				", modifyId=" + modifyId +
				'}';
	}
}


