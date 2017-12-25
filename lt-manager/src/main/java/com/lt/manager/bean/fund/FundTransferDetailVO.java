package com.lt.manager.bean.fund;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**   
* 项目名称：lt-manager   
* 类名称：FundTransferDetailVO   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年1月10日 下午2:09:42      
*/
public class FundTransferDetailVO extends BaseBean{
	
	/*----------------查询内容---------------*/
	
	/**	流水ID */
	private Integer flowId;
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
	/** 系统内部订单ID, 转账使用 */
	private String payId;
	/** 到账时间 */
	private Date doneDate;
	/** 转账操作人 */
	private Integer transferUserId;
	/**转账操作人姓名**/
	private String modifyUserName;
	/** 操作状态*/
	private Integer operateStatu;
	
	/*-----------------查询条件------------*/
	/** 手机号 */
	private String tele;
	/** 到账开始时间*/
	private String doneBeginDate;
	/** 到账截止时间*/
	private String doneEndDate;
	
	private String startDate;
	
	private String endDate;
	
	/*-----------精确查询内容----------------*/
	/** 总的提现金额*/
	private Double totalDrawAmt;
	/** 总的手续费*/
	private Double totalTax;
	/** 身份证号*/
	private String idCardNum;
	/** 银行名称*/
	private String bankName;
	/** 银行卡号*/
	private String bankNum;
	/** 申请时间*/
	private Date createDate;
	/** 可用余额*/
	private Double balance;
	/** 人民币*/
	private Double rmbAmt;
	//品牌 id
	private String brandId;
	/**
	 * @return the flowId
	 */
	public Integer getFlowId() {
		return flowId;
	}
	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(Integer flowId) {
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
	 * @return the doneBeginDate
	 */
	public String getDoneBeginDate() {
		return doneBeginDate;
	}
	/**
	 * @param doneBeginDate the doneBeginDate to set
	 */
	public void setDoneBeginDate(String doneBeginDate) {
		this.doneBeginDate = doneBeginDate;
	}
	/**
	 * @return the doneEndDate
	 */
	public String getDoneEndDate() {
		return doneEndDate;
	}
	/**
	 * @param doneEndDate the doneEndDate to set
	 */
	public void setDoneEndDate(String doneEndDate) {
		this.doneEndDate = doneEndDate;
	}
	/**
	 * @return the totalDrawAmt
	 */
	public Double getTotalDrawAmt() {
		return totalDrawAmt;
	}
	/**
	 * @param totalDrawAmt the totalDrawAmt to set
	 */
	public void setTotalDrawAmt(Double totalDrawAmt) {
		this.totalDrawAmt = totalDrawAmt;
	}
	/**
	 * @return the totalTax
	 */
	public Double getTotalTax() {
		return totalTax;
	}
	/**
	 * @param totalTax the totalTax to set
	 */
	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}
	/**
	 * @return the idCardNum
	 */
	public String getIdCardNum() {
		return idCardNum;
	}
	/**
	 * @param idCardNum the idCardNum to set
	 */
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
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
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	public String getModifyUserName() {
		return modifyUserName;
	}
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
