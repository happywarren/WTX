package com.lt.manager.bean.user;

import java.util.Date;

/**
 *
 * 描述: 用户列表页面返回对象
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class BankCard{
	/**
	 *  ID 
	 */
	private int id;
	/**
	 *  开户城市ID 
	 */
	
	private String cityCode;
	/**
	 *  用户ID 
	 */
	private String userId;
	/**
	 *  真实姓名 
	 */
	private String userName;
	/**
	 *  身份证
	 */
	private String idCardNum;
	/**
	 *  银行编码
	 */
	private String bankCode;
	/**
	 *  银行图片地址
	 */
	private String bankPic;
	/**
	 *  银行图片地址
	 */
	private String cardBackground;
	/**
	 *  实名状态
	 */
	private Integer status;
	/**
	 *  银行名称
	 */
	private String bankName;
	/**
	 *  银行卡号
	 */
	private String bankCardNum;
	/**
	 *  开户行所在省
	 */
	private String provinceCode;
	/**
	 *  开户行所在省
	 */
	private String openBankProv;
	/**
	 *  开户行所在市
	 */
	private String openBankCity;
	
	/**
	 *  开户行支行名称
	 */
	private Integer  branchId;
	/**
	 *  开户行支行名称
	 */
	private String  branchName;
	/**
	 *  银行卡绑定状态
	 */
	private Integer bankStatus;
	/**
	 *  是否默认银行卡
	 */
	private Integer isDefault;
	/**
	 *  创建日期
	 */
	private Date createDate;
	/**
	 *  修改日期
	 */
	private Date modifyDate;
	
	/**修改的银行卡**/
	private String newBankCardNum;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getBankPic() {
		return bankPic;
	}
	public void setBankPic(String bankPic) {
		this.bankPic = bankPic;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCardNum() {
		return bankCardNum;
	}
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}
	public String getCardBackground() {
		return cardBackground;
	}
	public void setCardBackground(String cardBackground) {
		this.cardBackground = cardBackground;
	}
	public String getOpenBankProv() {
		return openBankProv;
	}
	public void setOpenBankProv(String openBankProv) {
		this.openBankProv = openBankProv;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getOpenBankCity() {
		return openBankCity;
	}
	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Integer getBankStatus() {
		return bankStatus;
	}
	public void setBankStatus(Integer bankStatus) {
		this.bankStatus = bankStatus;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIdCardNum() {
		return idCardNum;
	}
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	public String getNewBankCardNum() {
		return newBankCardNum;
	}
	public void setNewBankCardNum(String newBankCardNum) {
		this.newBankCardNum = newBankCardNum;
	}
	
 }


