package com.lt.trade.defer.bean;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-trade   
* 类名称：NextPeroidCaluateInfo   
* 类描述：递延订单对象   
* 创建人：yuanxin   
* 创建时间：2017年2月8日 下午3:26:16      
*/
public class NextPeroidOrderInfo implements Serializable{
	
	/**
	 */
	private static final long serialVersionUID = 1L;
	/** 订单ID*/
	private String orderId;
	/** 用户id*/
	private String userId;
	/** 券商id*/
	private String investorId;
	/** 展示id*/
	private String displayId;
	/** 利率*/
	private Double rate;
	/** 递延费*/
	private Double deferInterest;
	/** 递延次数*/
	private Integer deferTimes;
	/** 递延保证金*/
	private Double deferFund;
	/** 系统清仓时间*/
	private Date sysSetSellDate;
	/** 合约到期时间*/
	private Date expirationTime;
	/** 电话号码*/
	private String tele;
	/** 产品id*/
	private Integer productId;
	/** 交易方向*/
	private Integer tradeDirection;
	/** 内外盘标识*/
	private Integer plate;
	/** 交易手数*/
	private Integer holdCount;
	/** 交易所代码*/
	private String exchangeCode;
	/** 产品名称*/
	private String productName;
	/**　合约编码CL1706*/
	private String productCode;
	/** 每首递延费收取（原货币）*/
	private Double perDeferInterest;
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
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

	public String getInvestorId() {
		return investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	/**
	 * @return the displayId
	 */
	public String getDisplayId() {
		return displayId;
	}
	/**
	 * @param displayId the displayId to set
	 */
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}
	/**
	 * @return the rate
	 */
	public Double getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}
	/**
	 * @return the deferInterest
	 */
	public Double getDeferInterest() {
		return deferInterest;
	}
	/**
	 * @param deferInterest the deferInterest to set
	 */
	public void setDeferInterest(Double deferInterest) {
		this.deferInterest = deferInterest;
	}
	/**
	 * @return the deferTimes
	 */
	public Integer getDeferTimes() {
		return deferTimes;
	}
	/**
	 * @param deferTimes the deferTimes to set
	 */
	public void setDeferTimes(Integer deferTimes) {
		this.deferTimes = deferTimes;
	}
	
	/**
	 * @return the expirationTime
	 */
	public Date getExpirationTime() {
		return expirationTime;
	}
	/**
	 * @param expirationTime the expirationTime to set
	 */
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	/**
	 * @return the tele
	 */
	public String getTele() {
		return tele;
	}
	/**
	 * @param tele the tele to set
	 */
	public void setTele(String tele) {
		this.tele = tele;
	}
	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	/**
	 * @return the plate
	 */
	public Integer getPlate() {
		return plate;
	}
	/**
	 * @param plate the plate to set
	 */
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	
	/**
	 * @return the exchangeCode
	 */
	public String getExchangeCode() {
		return exchangeCode;
	}
	/**
	 * @param exchangeCode the exchangeCode to set
	 */
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the perDeferInterest
	 */
	public Double getPerDeferInterest() {
		return perDeferInterest;
	}
	/**
	 * @param perDeferInterest the perDeferInterest to set
	 */
	public void setPerDeferInterest(Double perDeferInterest) {
		this.perDeferInterest = perDeferInterest;
	}
	/**
	 * @return the deferFund
	 */
	public Double getDeferFund() {
		return deferFund;
	}
	/**
	 * @param deferFund the deferFund to set
	 */
	public void setDeferFund(Double deferFund) {
		this.deferFund = deferFund;
	}
	/**
	 * @return the sysSetSellDate
	 */
	public Date getSysSetSellDate() {
		return sysSetSellDate;
	}
	/**
	 * @param sysSetSellDate the sysSetSellDate to set
	 */
	public void setSysSetSellDate(Date sysSetSellDate) {
		this.sysSetSellDate = sysSetSellDate;
	}
	/**
	 * @return the tradeDirection
	 */
	public Integer getTradeDirection() {
		return tradeDirection;
	}
	/**
	 * @param tradeDirection the tradeDirection to set
	 */
	public void setTradeDirection(Integer tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	/**
	 * @return the holdCount
	 */
	public Integer getHoldCount() {
		return holdCount;
	}
	/**
	 * @param holdCount the holdCount to set
	 */
	public void setHoldCount(Integer holdCount) {
		this.holdCount = holdCount;
	}
	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
