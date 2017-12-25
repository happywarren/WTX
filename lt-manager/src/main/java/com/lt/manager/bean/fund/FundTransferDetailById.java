package com.lt.manager.bean.fund;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**   
* 项目名称：lt-manager   
* 类名称：FundTransferDetailById   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年4月8日 下午1:39:06      
*/
public class FundTransferDetailById  extends BaseBean{
	
	/*----------------查询内容---------------*/
	
	/**	流水ID */
	private String flowId;
	/**	提现ID */
	private Integer ioId; 
	/**	用户ID */
	private String userId; 
	/**	昵称 */
	private String nickName;
	/** 姓名 */
	private String userName;
	/** 描述 */
	private String remark;
	/**	转账金额 */
	private Double amount = 0.00;
	/** 状态：0 转帐中 1成功 2失败 */
	private Integer status ;
	/** 操作状态*/
	private Integer operateStatu;
	/** 系统内部订单ID, 转账使用 */
	private String payId;
	/** 到账时间 */
	private Date doneDate;
	/** 转账操作人 */
	private Integer transferUserId;
	/** 转账类型*/
	private String transferType;
	/** 银行名称*/
	private String bankName;
	/** 银行卡号*/
	private String bankNum;
	/** 申请时间*/
	private Date createDate;
	/** 利率*/
	private Double rate;
	/** 开户市*/
	private String openBankCity;
	/** 开户省*/
	private String openBankProv;
	/** 开户市 名称*/
	private String openBankCityName;
	/** 开户省 名称*/
	private String openBankProvName;
	/** 支行名称*/
	private String branchName;
	/**操作人姓名**/
	private String modifyUserName;
	
	/**
	 * @return the flowId
	 */
	public String getFlowId() {
		return flowId;
	}
	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	/**
	 * @return the ioId
	 */
	public Integer getIoId() {
		return ioId;
	}
	/**
	 * @param ioId the ioId to set
	 */
	public void setIoId(Integer ioId) {
		this.ioId = ioId;
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the payId
	 */
	public String getPayId() {
		return payId;
	}
	/**
	 * @param payId the payId to set
	 */
	public void setPayId(String payId) {
		this.payId = payId;
	}
	/**
	 * @return the doneDate
	 */
	public Date getDoneDate() {
		return doneDate;
	}
	/**
	 * @param doneDate the doneDate to set
	 */
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}
	/**
	 * @return the transferUserId
	 */
	public Integer getTransferUserId() {
		return transferUserId;
	}
	/**
	 * @param transferUserId the transferUserId to set
	 */
	public void setTransferUserId(Integer transferUserId) {
		this.transferUserId = transferUserId;
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
	 * @return the bankNum
	 */
	public String getBankNum() {
		return bankNum;
	}
	/**
	 * @param bankNum the bankNum to set
	 */
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
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
	 * @return the transferType
	 */
	public String getTransferType() {
		return transferType;
	}
	/**
	 * @param transferType the transferType to set
	 */
	public void setTransferType(String transferType) {
		this.transferType = transferType;
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
	 * @return the operateStatu
	 */
	public Integer getOperateStatu() {
		return operateStatu;
	}
	/**
	 * @param operateStatu the operateStatu to set
	 */
	public void setOperateStatu(Integer operateStatu) {
		this.operateStatu = operateStatu;
	}
	/**
	 * @return the openBankCityName
	 */
	public String getOpenBankCityName() {
		return openBankCityName;
	}
	/**
	 * @param openBankCityName the openBankCityName to set
	 */
	public void setOpenBankCityName(String openBankCityName) {
		this.openBankCityName = openBankCityName;
	}
	/**
	 * @return the openBankProvName
	 */
	public String getOpenBankProvName() {
		return openBankProvName;
	}
	/**
	 * @param openBankProvName the openBankProvName to set
	 */
	public void setOpenBankProvName(String openBankProvName) {
		this.openBankProvName = openBankProvName;
	}
	public String getModifyUserName() {
		return modifyUserName;
	}
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}
	
}
