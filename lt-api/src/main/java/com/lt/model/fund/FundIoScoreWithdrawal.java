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
 * TODO 现金提现明细表
 * @author XieZhibing
 * @date 2016年11月30日 上午11:57:26
 * @version <b>1.0.0</b>
 */
public class FundIoScoreWithdrawal implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 9093656145741046414L;
	/** 主键ID */
	private Long id;
	/** 用户ID */
	private String userId;
	/** 一级资金业务码 */
	private String firstOptCode;
	/** 二级资金业务码 */
	private String secondOptCode;
	/** 三级资金业务码(提现)：支付宝、快钱 */
	private String thirdOptCode;
	/**	充值金额(用户提交，作参考) */
	private Double amount = 0.00;
	/** 余额 */
	private Double balance = 0.00;
	/** 手续费 */
	private Double tax = 2.00;
	/** 实际手续费 */
	private Double factTax = 0.00;

	/** 创建时间 */
	private Date createDate;
	/** 审核时间 */
	private Date auditDate;
	/** 到账时间 */
	private Date doneDate;
	/** 修改时间 */
	private Date modifyDate;
	/**	修改人ID */
	private Integer modifyUserId;
	/** 备注 */
	private String remark;
	/** 状态: 0 待审核, 1 待转账, 2 提现拒绝, 3 转账中, 4 转账失败, 5 转账成功, 6 提现撤销 */
	private Integer status = 0;
		
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月30日 下午1:47:31
	 */
	public FundIoScoreWithdrawal() {
		super();
	}
	
	
	public FundIoScoreWithdrawal(String userId, FundOptCode fundOptCode, Double amount,
			Double balance, Double tax, Double factTax) {
		super();
		this.userId = userId;
		this.firstOptCode = fundOptCode.getFirstOptCode();
		this.secondOptCode = fundOptCode.getSecondOptCode();
		this.thirdOptCode = fundOptCode.getThirdOptCode();
		this.amount = DoubleUtils.scaleFormatEnd(amount,2);
		this.balance = DoubleUtils.scaleFormat(balance,8) ;
		this.tax = DoubleUtils.scaleFormat(tax,2);
		this.factTax =  DoubleUtils.scaleFormat(factTax,2);
	}

	public FundIoScoreWithdrawal(Long id , String remark, Integer modifyUserId,
			Integer status,Date doneDate) {
		super();
		this.id = id;
		this.status = status;
		this.remark = remark;
		this.modifyUserId = modifyUserId;
		this.doneDate = doneDate;
		this.auditDate = new Date();
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

	/** 
	 * 获取 用户ID 
	 * @return userId 
	 */
	public String getUserId() {
		return userId;
	}

	/** 
	 * 设置 用户ID 
	 * @param userId 用户ID 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/** 
	 * 获取 一级资金业务码 
	 * @return firstOptCode 
	 */
	public String getFirstOptCode() {
		return firstOptCode;
	}

	/** 
	 * 设置 一级资金业务码 
	 * @param firstOptCode 一级资金业务码 
	 */
	public void setFirstOptCode(String firstOptCode) {
		this.firstOptCode = firstOptCode;
	}

	/** 
	 * 获取 二级资金业务码 
	 * @return secondOptCode 
	 */
	public String getSecondOptCode() {
		return secondOptCode;
	}

	/** 
	 * 设置 二级资金业务码 
	 * @param secondOptCode 二级资金业务码 
	 */
	public void setSecondOptCode(String secondOptCode) {
		this.secondOptCode = secondOptCode;
	}

	/** 
	 * 获取 三级资金业务码(提现)：支付宝、快钱 
	 * @return thirdOptCode 
	 */
	public String getThirdOptCode() {
		return thirdOptCode;
	}

	/** 
	 * 设置 三级资金业务码(提现)：支付宝、快钱 
	 * @param thirdOptCode 三级资金业务码(提现)：支付宝、快钱 
	 */
	public void setThirdOptCode(String thirdOptCode) {
		this.thirdOptCode = thirdOptCode;
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
		this.balance = DoubleUtils.scaleFormat(balance,8);
	}

	/** 
	 * 获取 手续费 
	 * @return tax 
	 */
	public Double getTax() {
		return tax;
	}

	/** 
	 * 设置 手续费 
	 * @param tax 手续费 
	 */
	public void setTax(Double tax) {
		this.tax = DoubleUtils.scaleFormat(tax,2);
	}

	/** 
	 * 获取 实际手续费 
	 * @return factTax 
	 */
	public Double getFactTax() {
		return factTax;
	}

	/** 
	 * 设置 实际手续费 
	 * @param factTax 实际手续费 
	 */
	public void setFactTax(Double factTax) {
		this.factTax = DoubleUtils.scaleFormat(factTax,2);
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
	 * 获取 审核时间 
	 * @return auditDate 
	 */
	public Date getAuditDate() {
		return auditDate;
	}

	/** 
	 * 设置 审核时间 
	 * @param auditDate 审核时间 
	 */
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	/** 
	 * 获取 到账时间 
	 * @return doneDate 
	 */
	public Date getDoneDate() {
		return doneDate;
	}

	/** 
	 * 设置 到账时间 
	 * @param doneDate 到账时间 
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
	 * 获取 状态: 0 待审核 1 待转账 2 提现拒绝 3 转账中 4 转账失败 5 转账成功 6 提现撤销 
	 * @return status 
	 */
	public Integer getStatus() {
		return status;
	}

	/** 
	 * 设置 状态: 0 待审核 1 待转账 2 提现拒绝 3 转账中 4 转账失败 5 转账成功 6 提现撤销 
	 * @param status 状态: 0 待审核 1 待转账 2 提现拒绝 3 转账中 4 转账失败 5 转账成功 6 提现撤销 
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 上午11:27:57
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundIoCashWithdrawal [id=").append(id)
				.append(", userId=").append(userId).append(", firstOptCode=")
				.append(firstOptCode).append(", secondOptCode=")
				.append(secondOptCode).append(", thirdOptCode=")
				.append(thirdOptCode).append(", amount=").append(amount)
				.append(", balance=").append(balance).append(", tax=")
				.append(tax).append(", factTax=").append(factTax)
				.append(", createDate=").append(createDate)
				.append(", auditDate=").append(auditDate).append(", doneDate=")
				.append(doneDate).append(", modifyDate=").append(modifyDate)
				.append(", modifyUserId=").append(modifyUserId)
				.append(", remark=").append(remark).append(", status=")
				.append(status).append("]");
		return builder.toString();
	}

}
