/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.model.fund
 * FILE    NAME: FundIoCashRecharge.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.model.fund;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DoubleUtils;

/**
 * TODO 现金充值明细表
 * @author XieZhibing
 * @date 2016年11月30日 上午11:57:26
 * @version <b>1.0.0</b>
 */
public class FundIoCashRecharge implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 7304079126983956602L;
	/** 主键ID */
	private Long id;
	/** 用户ID */
	private String userId;
	/** 系统内部订单ID */
	private String payId;
	/** 一级资金业务码 */
	private String firstOptcode;
	/** 二级资金业务码 */
	private String secondOptcode;
	/** 三级资金业务码(充值)：支付宝、快钱 */
	private String thirdOptcode;
	/**	充值金额(用户提交，作参考) */
	private Double amount ;
	/**	到账金额(实际金额) */
	private Double actualAmount;
	/** 到账金额（实际人民币）*/
	private Double actualRmbAmount;
	/** 余额 */
	private Double balance ;
	/**	外部流水号: 第三方订单ID */
	private String externalId;
	/**	转账账户，银行账号 */
	private String transferNumber; 
	/** 银行编号*/
	private String bankCode;
	/** 支付宝账号*/
	private String alipayNum = "";
	/** 创建时间 */
	private Date createDate;
	/** 到账时间 */
	private Date doneDate;
	/** 修改时间 */
	private Date modifyDate;
	/**	修改人ID */
	private Integer modifyUserId;
	/** 备注 */
	private String remark;
	/** 失败原因 */
	private String failReason;
	/** 状态: 0 未处理, 1 成功, -1失败 */
	private Integer status ;
	/**充值标识码：目前只有银生宝能用到*/
	private String rechargeIdentification;
	/** 转换后人民币金额 */
	private Double rmbAmt;
	/** 充值时的美元兑人民币利率  */
	private Double rate;
	
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月30日 下午1:47:31
	 */
	public FundIoCashRecharge() {
		super();
	}
	
	public FundIoCashRecharge(String userId,String payId,String firstOptcode,String secondOptcode,String thirdOptcode,
			Double amount,Double actualAmount,Double balance,String externalId,String transferNumber,String bankCode,String remark,
			Integer modifyUserId,Integer status,String rechargeIdentification,Double rmbAmt,Double rate) {
		this.userId=userId;
		this.payId=payId;
		this.firstOptcode=firstOptcode;
		this.secondOptcode=secondOptcode;
		this.thirdOptcode=thirdOptcode;
		this.amount=DoubleUtils.scaleFormatEnd(amount, 2);
		this.actualAmount=DoubleUtils.scaleFormat(actualAmount, 2);
		this.balance=DoubleUtils.scaleFormat(balance, 8);
		this.externalId=externalId;
		this.transferNumber=transferNumber;
		this.bankCode = bankCode ;
		this.remark=remark;
		this.status=status;
		this.modifyUserId=modifyUserId;
		this.rechargeIdentification=rechargeIdentification;
		this.rmbAmt = rmbAmt ;
		this.rate = rate ;
	}
	
	/** 
	 * 获取 主键ID 
	 * @return id 
	 */
	public Long getId() {
		return id;
	}
	/** 
	 * 设置 主键ID 
	 * @param id 主键ID 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** 
	 * 获取 系统内部订单ID 
	 * @return payId 
	 */
	public String getPayId() {
		return payId;
	}
	/** 
	 * 设置 系统内部订单ID 
	 * @param payId 系统内部订单ID 
	 */
	public void setPayId(String payId) {
		this.payId = payId;
	}
	
	
	public String getFirstOptcode() {
		return firstOptcode;
	}

	public void setFirstOptcode(String firstOptcode) {
		this.firstOptcode = firstOptcode;
	}

	public String getSecondOptcode() {
		return secondOptcode;
	}

	public void setSecondOptcode(String secondOptcode) {
		this.secondOptcode = secondOptcode;
	}

	public String getThirdOptcode() {
		return thirdOptcode;
	}

	public void setThirdOptcode(String thirdOptcode) {
		this.thirdOptcode = thirdOptcode;
	}

	/** 
	 * 获取 充值金额(用户提交，作参考) 
	 * @return amount 
	 */
	public Double getAmount() {
		return amount;
	}
	/** 
	 * 设置 充值金额(用户提交，作参考) 
	 * @param amount 充值金额(用户提交，作参考) 
	 */
	public void setAmount(Double amount) {
		this.amount = DoubleUtils.scaleFormatEnd(amount,2);
	}
	/** 
	 * 获取 到账金额(实际金额) 
	 * @return actualAmount 
	 */
	public Double getActualAmount() {
		return actualAmount;
	}
	/** 
	 * 设置 到账金额(实际金额) 
	 * @param actualAmount 到账金额(实际金额) 
	 */
	public void setActualAmount(Double actualAmount) {
		this.actualAmount = DoubleUtils.scaleFormat(actualAmount,2);
	}
	/** 
	 * 获取 余额 
	 * @return balance 
	 */
	public Double getBalance() {
		return balance;
	}
	/** 
	 * 设置 余额 
	 * @param balance 余额 
	 */
	public void setBalance(Double balance) {
		this.balance = DoubleUtils.scaleFormatEnd(balance,8);
	}
	/** 
	 * 获取 外部流水号: 第三方订单ID 
	 * @return externalId 
	 */
	public String getExternalId() {
		return externalId;
	}
	/** 
	 * 设置 外部流水号: 第三方订单ID 
	 * @param externalId 外部流水号: 第三方订单ID 
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	/** 
	 * 获取 转账账户，支付宝或者银行账号 
	 * @return transferNumber 
	 */
	public String getTransferNumber() {
		return transferNumber;
	}
	/** 
	 * 设置 转账账户，支付宝或者银行账号 
	 * @param transferNumber 转账账户，支付宝或者银行账号 
	 */
	public void setTransferNumber(String transferNumber) {
		this.transferNumber = transferNumber;
	}
	/** 
	 * 获取 创建时间 
	 * @return createDate 
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/** 
	 * 设置 创建时间 
	 * @param createDate 创建时间 
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/** 
	 * 获取 到账日期 
	 * @return doneDate 
	 */
	public Date getDoneDate() {
		return doneDate;
	}
	/** 
	 * 设置 到账日期 
	 * @param doneDate 到账日期 
	 */
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}
	/** 
	 * 获取 修改时间 
	 * @return modifyDate 
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/** 
	 * 设置 修改时间 
	 * @param modifyDate 修改时间 
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	/** 
	 * 获取 修改人ID 
	 * @return modifyUserId 
	 */
	public Integer getModifyUserId() {
		return modifyUserId;
	}
	/** 
	 * 设置 修改人ID 
	 * @param modifyUserId 修改人ID 
	 */
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	/** 
	 * 获取 备注 
	 * @return remark 
	 */
	public String getRemark() {
		return remark;
	}
	/** 
	 * 设置 备注 
	 * @param remark 备注 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 
	 * 获取 状态: 0 未处理 1 成功 -1失败 
	 * @return status 
	 */
	public Integer getStatus() {
		return status;
	}
	/** 
	 * 设置 状态: 0 未处理 1 成功 -1失败 
	 * @param status 状态: 0 未处理 1 成功 -1失败 
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getRechargeIdentification() {
		return rechargeIdentification;
	}

	public void setRechargeIdentification(String rechargeIdentification) {
		this.rechargeIdentification = rechargeIdentification;
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
	 * @return the actualRmbAmount
	 */
	public Double getActualRmbAmount() {
		return actualRmbAmount;
	}

	/**
	 * @param actualRmbAmount the actualRmbAmount to set
	 */
	public void setActualRmbAmount(Double actualRmbAmount) {
		this.actualRmbAmount = actualRmbAmount;
	}
	
	/**
	 * @return the alipayNum
	 */
	public String getAlipayNum() {
		return alipayNum;
	}

	/**
	 * @param alipayNum the alipayNum to set
	 */
	public void setAlipayNum(String alipayNum) {
		this.alipayNum = alipayNum;
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
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 上午11:27:04
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundIoCashRecharge [id=").append(id)
				.append(", userId=").append(userId).append(", payId=")
				.append(payId).append(", firstOptCode=").append(firstOptcode)
				.append(", secondOptCode=").append(secondOptcode)
				.append(", thirdOptCode=").append(thirdOptcode)
				.append(", amount=").append(amount).append(", actualAmount=")
				.append(actualAmount).append(", balance=").append(balance)
				.append(", externalId=").append(externalId)
				.append(", transferNumber=").append(transferNumber)
				.append(", createDate=").append(createDate)
				.append(", doneDate=").append(doneDate).append(", modifyDate=")
				.append(modifyDate).append(", modifyUserId=")
				.append(modifyUserId).append(", remark=").append(remark)
				.append(", fail_reason=").append(failReason)
				.append(", status=").append(status).append("]");
		return builder.toString();
	}
	
	
}
