/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.model.fund
 * FILE    NAME: FundTransferDetail.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.model.fund;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO（描述类的职责）
 * @author XieZhibing
 * @date 2016年11月30日 下午2:08:33
 * @version <b>1.0.0</b>
 */
public class FundTransferDetail implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1866540395734831472L;
	/**	主键ID */
	private Long id;
	/** 系统内部订单ID, 转账使用 */
	private String payId;
	/** 用户ID */
	private String userId;
	/** 提现申请ID */
	private Long ioId;
	/** 姓名 */
	private String userName;
	/** 银行卡号 */
	private String bankNum;
	/**	开户省 */
	private String openBankProv;
	/** 开户市 */
	private String openBankCity;
	/**	分行 */
	private String branchName;
	/** 银行名称 */
	private String bankName;
	/**	转账金额 */
	private Double amount = 0.00;
	
	private Double rmbAmt = 0.0 ;
	/** 创建时间 */
	private Date createDate;
	/** 到账时间 */
	private Date doneDate;
	/** 转账操作时间 */
	private Date transferDate;
	/** 转账操作人 */
	private Integer transferUserId;
	/** 描述 */
	private String remark;
	/** 状态：0 转帐中 1成功 2失败 */
	private Integer status = 0;
	/** 操作状态 0：未操作 1 已操作*/
	private Integer operateStatu;
		
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月30日 下午2:21:25
	 */
	public FundTransferDetail() {
		super();
	}
	
	/**
	 * @param payId 支付id 或商户id
	 * @param userId 用户id
	 * @param ioId 提现请求id
	 * @param userName 用户姓名
	 * @param bankNum 银行卡号
	 * @param bankName 银行名称
	 * @param amount 金额
	 * @param createDate 创建时间
	 * @param transferDate 转账时间
	 * @param transferUserId 转账用户
	 * @param remark 备注
	 * @param status 状态
	 */
	public FundTransferDetail(String payId, String userId, Long ioId, String userName, String bankNum,
			String bankName, Double amount, Integer transferUserId, String remark,
			Integer status) {
		super();
		this.payId = payId;
		this.userId = userId;
		this.ioId = ioId;
		this.userName = userName;
		this.bankNum = bankNum;
		this.bankName = bankName;
		this.amount = amount;
		this.transferUserId = transferUserId;
		this.remark = remark;
		this.status = status;
	}

	public FundTransferDetail(String payId,String userId,Long ioId,String userName,String bankNum,String openBankProv,
			String openBankCity,String branchName,String bankName,Double amount,
			Date transferDate,Integer transferUserId,String remark,Integer operate_statu,Integer status){
		this.payId = payId;
		this.userId = userId;
		this.ioId = ioId;
		this.userName = userName;
		this.bankNum = bankNum;
		this.openBankProv = openBankProv;
		this.openBankCity = openBankCity;
		this.branchName = branchName;
		this.bankName = bankName;
		this.amount = amount;
		this.transferDate = transferDate;
		this.transferUserId = transferUserId;
		this.remark = remark;
		this.status = status;
		this.operateStatu = operate_statu ;
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
	 * 获取 系统内部订单ID 转账使用 
	 * @return payId 
	 */
	public String getPayId() {
		return payId;
	}
	/** 
	 * 设置 系统内部订单ID 转账使用 
	 * @param payId 系统内部订单ID 转账使用 
	 */
	public void setPayId(String payId) {
		this.payId = payId;
	}

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
	
	public Long getIoId() {
		return ioId;
	}

	public void setIoId(Long ioId) {
		this.ioId = ioId;
	}

	/** 
	 * 获取 姓名 
	 * @return userName 
	 */
	public String getUserName() {
		return userName;
	}
	/** 
	 * 设置 姓名 
	 * @param userName 姓名 
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/** 
	 * 获取 银行卡号 
	 * @return bankNum 
	 */
	public String getBankNum() {
		return bankNum;
	}
	/** 
	 * 设置 银行卡号 
	 * @param bankNum 银行卡号 
	 */
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	/** 
	 * 获取 开户省 
	 * @return openBankProv 
	 */
	public String getOpenBankProv() {
		return openBankProv;
	}
	/** 
	 * 设置 开户省 
	 * @param openBankProv 开户省 
	 */
	public void setOpenBankProv(String openBankProv) {
		this.openBankProv = openBankProv;
	}
	/** 
	 * 获取 开户市 
	 * @return openBankCity 
	 */
	public String getOpenBankCity() {
		return openBankCity;
	}
	/** 
	 * 设置 开户市 
	 * @param openBankCity 开户市 
	 */
	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}
	/** 
	 * 获取 分行 
	 * @return branchName 
	 */
	public String getBranchName() {
		return branchName;
	}
	/** 
	 * 设置 分行 
	 * @param branchName 分行 
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/** 
	 * 获取 银行名称 
	 * @return bankName 
	 */
	public String getBankName() {
		return bankName;
	}
	/** 
	 * 设置 银行名称 
	 * @param bankName 银行名称 
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/** 
	 * 获取 转账金额 
	 * @return amount 
	 */
	public Double getAmount() {
		return amount;
	}
	/** 
	 * 设置 转账金额 
	 * @param amount 转账金额 
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
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
	 * 获取 转账操作时间 
	 * @return transferDate 
	 */
	public Date getTransferDate() {
		return transferDate;
	}
	/** 
	 * 设置 转账操作时间 
	 * @param transferDate 转账操作时间 
	 */
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	/** 
	 * 获取 转账操作人 
	 * @return transferUserId 
	 */
	public Integer getTransferUserId() {
		return transferUserId;
	}
	/** 
	 * 设置 转账操作人 
	 * @param transferUserId 转账操作人 
	 */
	public void setTransferUserId(Integer transferUserId) {
		this.transferUserId = transferUserId;
	}
	/** 
	 * 获取 描述 
	 * @return remark 
	 */
	public String getRemark() {
		return remark;
	}
	/** 
	 * 设置 描述 
	 * @param remark 描述 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/** 
	 * 获取 状态：0 转帐中 1成功 2失败 
	 * @return status 
	 */
	public Integer getStatus() {
		return status;
	}
	/** 
	 * 设置 状态：0 转帐中 1成功 2失败 
	 * @param status 状态：0 转帐中 1成功 2失败 
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
	 * 
	 * @author XieZhibing
	 * @date 2016年11月30日 下午2:21:13
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundTransferDetail [id=").append(id).append(", payId=")
				.append(payId).append(", userId=").append(userId)
				.append(", ioId=").append(ioId).append(", userName=")
				.append(userName).append(", bankNum=").append(bankNum)
				.append(", openBankProv=").append(openBankProv)
				.append(", openBankCity=").append(openBankCity)
				.append(", branchName=").append(branchName)
				.append(", bankName=").append(bankName).append(", amount=")
				.append(amount).append(", createDate=").append(createDate)
				.append(", doneDate=").append(doneDate)
				.append(", transferDate=").append(transferDate)
				.append(", transferUserId=").append(transferUserId)
				.append(", remark=").append(remark).append(", status=")
				.append(status).append("]");
		return builder.toString();
	}

}
