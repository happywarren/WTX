package com.lt.user.charge.bean;

import java.util.Date;

import com.lt.util.utils.CalendarTools;

/**   
* 项目名称：lt-user   
* 类名称：BaseChargeBean   
* 类描述：充值基础对象   
* 创建人：yuanxin   
* 创建时间：2017年6月29日 下午5:29:50      
*/
public class BaseChargeBean {
	
	/** 用户id*/
	private String userId ;
	/** 银行卡id*/
	private String bankCardId;
	/** 银行卡号*/
	private String bankCardNum;
	/** 银行编号*/
	private String bankCode;
	/** 美元金额*/
	private Double amt;
	/** 人民币金额*/
	private Double rmbAmt;
	/** 汇率*/
	private Double rate;
	/** 外部订单号*/
	private String payOrderId;
	/** 不同的充值对象*/
	private Object object ;
	/**支付渠道id**/
	private String channelId;
	private String tele;
	private String userName;
	private String idCardNum;
	
	
	public BaseChargeBean(String userId,String bankCardId,Double amt,Double rmbAmt,Double rate,String bankCardNum,String bankCode,Object object){
		this.userId = userId ;
		this.bankCardId = bankCardId ;
		this.amt = amt ;
		this.rmbAmt = rmbAmt ;
		this.rate = rate ;
		this.bankCardNum= bankCardNum ;
		this.bankCode = bankCode ;
		this.payOrderId = String.valueOf(CalendarTools.getMillis(new Date()));
		this.object = object ;
	}
	public BaseChargeBean(String userId,String bankCardId,Double amt,Double rmbAmt,Double rate,String bankCardNum,String bankCode,Object object,String channelId){
		this.userId = userId ;
		this.bankCardId = bankCardId ;
		this.amt = amt ;
		this.rmbAmt = rmbAmt ;
		this.rate = rate ;
		this.bankCardNum= bankCardNum ;
		this.bankCode = bankCode ;
		this.payOrderId = String.valueOf(CalendarTools.getMillis(new Date()));
		this.object = object ;
		this.channelId = channelId;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the bankCardId
	 */
	public String getBankCardId() {
		return bankCardId;
	}
	/**
	 * @param bankCardId the bankCardId to set
	 */
	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}
	/**
	 * @return the amt
	 */
	public Double getAmt() {
		return amt;
	}
	/**
	 * @param amt the amt to set
	 */
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	/**
	 * @return the rmbAmt
	 */
	public Double getRmbAmt() {
		return rmbAmt;
	}
	/**
	 * @param rmbAmt the rmbAmt to set
	 */
	public void setRmbAmt(Double rmbAmt) {
		this.rmbAmt = rmbAmt;
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
	 * @return the payOrderId
	 */
	public String getPayOrderId() {
		return payOrderId;
	}

	/**
	 * @param payOrderId the payOrderId to set
	 */
	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	/**
	 * @return the bankCardNum
	 */
	public String getBankCardNum() {
		return bankCardNum;
	}

	/**
	 * @param bankCardNum the bankCardNum to set
	 */
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdCardNum() {
		return idCardNum;
	}
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	
	
}
