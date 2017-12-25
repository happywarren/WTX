package com.lt.model.user;

import java.io.Serializable;

/**   
* 项目名称：lt-api   
* 类名称：BankInfo   
* 类描述：  银行信息实体 
* 创建人：yuanxin   
* 创建时间：2017年1月5日 上午9:18:23      
*/
public class BankInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id ;
	/** 银行编码*/
	private String bankCode;
	/** 银行名称*/
	private String bankName;
	/** 银行图案地址*/
	private String bankPic;
	/** 银行限额（用于显示）*/
	private String transActionLimit;
	/** 银行每日充值限制（用于显示）*/
	private String dayLimit;
	/** 每天充值金额*/
	private Double dailyAmt = 0.0;
	/** 单笔充值金额*/
	private Double singleAmt =0.0 ;
	/** 智付银行编码*/
	private String dinpayBankCode;
	/** daddyPay银行编码*/
	private String daddypayBankCode;
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
	 * @return the dailyAmt
	 */
	public Double getDailyAmt() {
		return dailyAmt;
	}
	/**
	 * @param dailyAmt the dailyAmt to set
	 */
	public void setDailyAmt(Double dailyAmt) {
		this.dailyAmt = dailyAmt;
	}
	/**
	 * @return the singleAmt
	 */
	public Double getSingleAmt() {
		return singleAmt;
	}
	/**
	 * @param singleAmt the singleAmt to set
	 */
	public void setSingleAmt(Double singleAmt) {
		this.singleAmt = singleAmt;
	}

	public String getDinpayBankCode() {
		return dinpayBankCode;
	}

	public void setDinpayBankCode(String dinpayBankCode) {
		this.dinpayBankCode = dinpayBankCode;
	}

	public String getDaddypayBankCode() {
		return daddypayBankCode;
	}

	public void setDaddypayBankCode(String daddypayBankCode) {
		this.daddypayBankCode = daddypayBankCode;
	}
}
