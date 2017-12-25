/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.model.fund
 * FILE    NAME: FundFlowCash.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.model.fund;

import java.io.Serializable;
import java.util.Date;

import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.util.utils.DoubleUtils;

/**
 * TODO 资金流水表(积分、现金通用)
 * @author XieZhibing
 * @date 2016年11月30日 上午10:00:07
 * @version <b>1.0.0</b>
 */
public class FundFlow implements Serializable {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -4204941116713819811L;
	/** 主键ID */
	private Long id;
	/**	用户ID */
	private String userId;
	/** 资金流转类型, 参考FundFlowTypeEnum：1收入，-1支出(默认)  */
	private Integer flowType = FundFlowTypeEnum.OUTLAY.getValue();
	/** 一级资金业务码 */
	private String firstOptCode;
	/** 二级资金业务码 */
	private String secondOptCode;
	/** 三级资金业务码 */
	private String thirdOptCode;
	/** 收支额度 */
	private double amount = 0.00;
	/** 余额 */
	private double balance = 0.00;
	/** 关联订单号 */
	private String externalId;
	/** 备注 */
	private String remark;
	
	/** 创建时间(入账时间) */
	private Date createDate;
	/** 修改时间 */
	private Date modifyDate;
		
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月30日 下午1:48:12
	 */
	public FundFlow() {
		super();
	}
	
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月6日 下午2:35:02
	 * @param userId
	 * @param flowType
	 * @param firstOptCode
	 * @param secondOptCode
	 * @param thirdOptCode
	 * @param amount
	 * @param balance
	 * @param externalId
	 * @param remark
	 * @param createDate
	 * @param modifyDate
	 */
	public FundFlow(String userId, Integer flowType, String firstOptCode,
			String secondOptCode, String thirdOptCode, double amount,
			double balance, String externalId, String remark, Date createDate,
			Date modifyDate) {
		super();
		this.userId = userId;
		this.flowType = flowType;
		this.firstOptCode = firstOptCode;
		this.secondOptCode = secondOptCode;
		this.thirdOptCode = thirdOptCode;
		System.out.println("这应该对了==========="+ -DoubleUtils.scaleFormatEnd(-amount, 2));
		if(amount < 0){
			this.amount = -DoubleUtils.scaleFormatEnd(-amount, 2);
		}else{
			this.amount = DoubleUtils.scaleFormatEnd(amount, 2);
		}
		this.balance = DoubleUtils.scaleFormat(balance, 8);
		this.externalId = externalId;
		this.remark = remark;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
	}
	/**
	 * 
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月6日 下午8:18:03
	 * @param productName
	 * @param optCodeEnum
	 * @param userId
	 * @param amount
	 * @param balance
	 * @param externalId
	 * @param createDate
	 * @param modifyDate
	 */
	public FundFlow(String productName, IFundOptCode optCodeEnum, String userId, 
			double amount, double balance, String externalId, Date createDate, Date modifyDate) {
		super();
		this.userId = userId;
		this.flowType = optCodeEnum.getInout();
		this.firstOptCode = optCodeEnum.getFirstLevelCode();
		this.secondOptCode = optCodeEnum.getCode();
		this.thirdOptCode = optCodeEnum.getCode();
		System.out.println("===========syssysysysysysysysyamount"+amount+"=========");
		if(optCodeEnum.getInout()<0){
			this.amount = -DoubleUtils.scaleFormatEnd(amount, 2);
		}else{
			this.amount = DoubleUtils.scaleFormatEnd(amount, 2);
		}
		System.out.println(this.amount);
		this.balance = DoubleUtils.scaleFormatEnd(balance, 2); 
		this.externalId = externalId;		
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		
		this.remark = new StringBuffer(productName).append(optCodeEnum.getName()).toString();
	}
	/**
	 * 
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月6日 下午8:19:37
	 * @param productName
	 * @param optCodeEnum
	 * @param thirdOptCode
	 * @param userId
	 * @param amount
	 * @param balance
	 * @param externalId
	 * @param createDate
	 * @param modifyDate
	 */
	public FundFlow(String productName, IFundOptCode optCodeEnum, String thirdOptCode, String userId, 
			double amount, double balance, String externalId, Date createDate, Date modifyDate) {
		this(productName, optCodeEnum, userId, amount, balance, externalId, createDate, modifyDate);
		this.thirdOptCode = thirdOptCode;
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
	 * 获取 资金流转类型 参考FundFlowTypeEnum：1收入，-1支出 
	 * @return flowType 
	 */
	public Integer getFlowType() {
		return flowType;
	}
	/** 
	 * 设置 资金流转类型 参考FundFlowTypeEnum：1收入，-1支出 
	 * @param flowType 资金流转类型 参考FundFlowTypeEnum：1收入，-1支出 
	 */
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
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
	 * @return the thirdOptCode
	 */
	public String getThirdOptCode() {
		return thirdOptCode;
	}

	/**
	 * @param thirdOptCode the thirdOptCode to set
	 */
	public void setThirdOptCode(String thirdOptCode) {
		this.thirdOptCode = thirdOptCode;
	}

	/** 
	 * 获取 收支额度 
	 * @return amount 
	 */
	public double getAmount() {
		return amount;
	}
	/** 
	 * 设置 收支额度 
	 * @param amount 收支额度 
	 */
	public void setAmount(double amount) {
		this.amount = DoubleUtils.scaleFormatEnd(amount, 2);
	}
	/** 
	 * 获取 余额 
	 * @return balance 
	 */
	public double getBalance() {
		return balance;
	}
	/** 
	 * 设置 余额 
	 * @param balance 余额 
	 */
	public void setBalance(double balance) {
		this.balance = DoubleUtils.scaleFormat(balance, 8);
	}
	/** 
	 * 获取 关联订单号 
	 * @return externalId 
	 */
	public String getExternalId() {
		return externalId;
	}
	/** 
	 * 设置 关联订单号 
	 * @param externalId 关联订单号 
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
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
	 * 获取 创建时间(入账时间) 
	 * @return createDate 
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/** 
	 * 设置 创建时间(入账时间) 
	 * @param createDate 创建时间(入账时间) 
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月9日 下午4:36:42
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundFlow [id=").append(id).append(", userId=")
				.append(userId).append(", flowType=").append(flowType)
				.append(", firstOptCode=").append(firstOptCode)
				.append(", secondOptCode=").append(secondOptCode)
				.append(", thirdOptCode=").append(thirdOptCode)
				.append(", amount=").append(amount).append(", balance=")
				.append(balance).append(", externalId=").append(externalId)
				.append(", remark=").append(remark).append(", createDate=")
				.append(createDate).append(", modifyDate=").append(modifyDate)
				.append("]");
		return builder.toString();
	}
	
}
