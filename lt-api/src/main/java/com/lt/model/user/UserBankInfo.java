package com.lt.model.user;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserBankInfo   
* 类描述：用户银行卡信息   
* 创建人：yuanxin   
* 创建时间：2016年12月9日 上午11:09:46      
*/
public class UserBankInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** id*/
	private Integer id;
	/** 用户id*/
	private String userId;
	/** 银行名称*/
	private String bankName;
	/** 银行账号*/
	private String bankCardNum;
	/** 银行开户省*/
	private String openBankProv;
	/** 银行开户市*/
	private String openBankCity;
	/** 银行名称*/
	private String branchName;
	/** 银行信息是否绑定*/
	private Integer bankStatus;
	/** 是否为默认银行卡*/
	private Integer isDefault;
	/** 创建时间*/
	private Date createDate;
	/** 修改时间*/
	private Date modifyDate;
	/** 银行编码*/
	private String bankCode;
	/**省份证号*/
	private String idCardNum;
	/**手机号*/
	private String tele;
	/***用户名*/
	private String userName;
	
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

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return the openBankProv
	 */
	public String getOpenBankProv() {
		return openBankProv;
	}
	/**
	 * @param openBankProv the openBankProv to set
	 */
	public void setOpenBankProv(String openBankProv) {
		this.openBankProv = openBankProv;
	}
	/**
	 * @return the openBankCity
	 */
	public String getOpenBankCity() {
		return openBankCity;
	}
	/**
	 * @param openBankCity the openBankCity to set
	 */
	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}
	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/**
	 * @return the bankStatus
	 */
	public Integer getBankStatus() {
		return bankStatus;
	}
	/**
	 * @param bankStatus the bankStatus to set
	 */
	public void setBankStatus(Integer bankStatus) {
		this.bankStatus = bankStatus;
	}
	/**
	 * @return the isDefault
	 */
	public Integer getIsDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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
	public String getIdCardNum() {
		return idCardNum;
	}
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
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
	
}
