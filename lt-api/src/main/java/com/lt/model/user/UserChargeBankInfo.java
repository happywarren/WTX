package com.lt.model.user;

import java.io.Serializable;

/**   
* 项目名称：lt-api   
* 类名称：UserChargeBankInfo   
* 类描述： 用户出金银行信息  
* 创建人：yuanxin   
* 创建时间：2017年3月27日 下午4:37:55      
*/
public class UserChargeBankInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 银行卡信息id*/
	private Integer id;
	/** 用户id*/
	private String userId;
	/** 银行编号*/
	private String bankCode;
	/** 银行名称*/
	private String bankName;
	/** 银行图片*/
	private String bankPic;
	/** 银行卡尾号*/
	private String tailNumber;
	/** 银行卡限额*/
	private String transActionLimit;
	/** 银行每日限额*/
	private String dayLimit;
	/** 完整银行卡号*/
	private String bankCardNum;
	
	/***
	 * 充值金额
	 */
	private Double chargeAmt;
	
	
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
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the bankPic
	 */
	public String getBankPic() {
		return bankPic;
	}
	/**
	 * @param bankPic the bankPic to set
	 */
	public void setBankPic(String bankPic) {
		this.bankPic = bankPic;
	}
	/**
	 * @return the tailNumber
	 */
	public String getTailNumber() {
		return tailNumber;
	}
	/**
	 * @param tailNumber the tailNumber to set
	 */
	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}
	/**
	 * @return the transActionLimit
	 */
	public String getTransActionLimit() {
		return transActionLimit;
	}
	/**
	 * @param transActionLimit the transActionLimit to set
	 */
	public void setTransActionLimit(String transActionLimit) {
		this.transActionLimit = transActionLimit;
	}
	/**
	 * @return the dayLimit
	 */
	public String getDayLimit() {
		return dayLimit;
	}
	/**
	 * @param dayLimit the dayLimit to set
	 */
	public void setDayLimit(String dayLimit) {
		this.dayLimit = dayLimit;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the chargeAmt
	 */
	public Double getChargeAmt() {
		return chargeAmt;
	}
	/**
	 * @param chargeAmt the chargeAmt to set
	 */
	public void setChargeAmt(Double chargeAmt) {
		this.chargeAmt = chargeAmt;
	}
	
}
