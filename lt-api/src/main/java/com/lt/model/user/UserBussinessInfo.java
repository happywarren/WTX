package com.lt.model.user;

import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserBussinessInfo   
* 类描述： 用户业务实体  
* 创建人：yuanxin   
* 创建时间：2016年12月8日 下午3:57:21      
*/
public class UserBussinessInfo extends UserBaseInfo {

	private static final long serialVersionUID = 1L;
	
	/** 银行号码*/
	private String bankCardNum;
	/** 银行名称*/
	private String bankName;
	/** 银行卡图片*/
	private String bankcardPic;
	/** 智付银行编码*/
	private String dinpayBankCode;
	/** 爸爸付银行编码*/
	private String daddypayBankCode;
	/** 九派银行编码**/
	private String jiupaiBankCode;
/*	*//**开户省*//*
	private String openBankProv;
	*//**开户市*//*
	private String openBankCity;*/
	/**支行名称*/
	//private String branchName;	
	/** 用户可用余额*/
	private Double usedAmt;
	
	private String provinceCode;
	private String cityCode;
	private Integer branchId;
	/** 用户实名审核状态*/
	private Integer realNameStatus; 
	/** 用户实名申请时间*/
	private Date applyTime;
	/** 用户实名完成时间*/
	private Date finishTime;
	public String getBankCardNum() {
		return bankCardNum;
	}
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
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
/*	public String getOpenBankProv() {
		return openBankProv;
	}
	public void setOpenBankProv(String openBankProv) {
		this.openBankProv = openBankProv;
	}
	public String getOpenBankCity() {
		return openBankCity;
	}
	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}*/
	/*public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}*/	
	/**
	 * @return the usedAmt
	 */
	public Double getUsedAmt() {
		return usedAmt;
	}
	/**
	 * @param usedAmt the usedAmt to set
	 */
	public void setUsedAmt(Double usedAmt) {
		this.usedAmt = usedAmt;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	/**
	 * @return the bankcardPic
	 */
	public String getBankcardPic() {
		return bankcardPic;
	}
	/**
	 * @param bankcardPic the bankcardPic to set
	 */
	public void setBankcardPic(String bankcardPic) {
		this.bankcardPic = bankcardPic;
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

	/**
	 * @return the realNameStatus
	 */
	public Integer getRealNameStatus() {
		return realNameStatus;
	}
	/**
	 * @param realNameStatus the realNameStatus to set
	 */
	public void setRealNameStatus(Integer realNameStatus) {
		this.realNameStatus = realNameStatus;
	}
	/**
	 * @return the applyTime
	 */
	public Date getApplyTime() {
		return applyTime;
	}
	/**
	 * @param applyTime the applyTime to set
	 */
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	/**
	 * @return the finishTime
	 */
	public Date getFinishTime() {
		return finishTime;
	}
	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getJiupaiBankCode() {
		return jiupaiBankCode;
	}

	public void setJiupaiBankCode(String jiupaiBankCode) {
		this.jiupaiBankCode = jiupaiBankCode;
	}
}
