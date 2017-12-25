package com.lt.manager.bean.fund;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 * 积分现金流水入参
 * 
 * @author guodw
 *
 */
public class FundFlowVO extends BaseBean {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -4204941116713819811L;
	/** 用户ID */
	private String userId;
	/** 昵称 */
	private String nickName;
	/** 姓名 */
	private String userName;
	/** 手机号 */
	private String tele;
	/** 一级资金业务码 */
	private String firstOptcode;
	/** 二级资金业务码 */
	private String secondOptcode;
	/** 一级资金业务码 */
	private String firstOptname;
	/** 二级资金业务码 */
	private String secondOptname;
	/** 备注（详细说明） */
	private String remark;
	/** 创建时间(入账开始时间) */
	private String startDate;
	/** 创建时间(入账结束时间) */
	private String endDate;
	
	/** 关联id*/
	private String externalId;
	/** 流入流出方向*/
	private String flowType;
	
	/** 当前金额*/
	private Double amount;
	/** 可用余额*/
	private Double balance;
	/** 创建时间*/
	private Date createDate;
	/** 流水id*/
	private Integer flowId;
	//品牌 id
	private String brandId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTele() {
		return tele;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the flowType
	 */
	public String getFlowType() {
		return flowType;
	}

	/**
	 * @param flowType the flowType to set
	 */
	public void setFlowType(String flowType) {
		this.flowType = flowType;
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
	 * @return the firstOptcode
	 */
	public String getFirstOptcode() {
		return firstOptcode;
	}

	/**
	 * @param firstOptcode the firstOptcode to set
	 */
	public void setFirstOptcode(String firstOptcode) {
		this.firstOptcode = firstOptcode;
	}

	/**
	 * @return the secondOptcode
	 */
	public String getSecondOptcode() {
		return secondOptcode;
	}

	/**
	 * @param secondOptcode the secondOptcode to set
	 */
	public void setSecondOptcode(String secondOptcode) {
		this.secondOptcode = secondOptcode;
	}

	/**
	 * @return the externalId
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * @param externalId the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
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
	 * @param tele the tele to set
	 */
	public void setTele(String tele) {
		this.tele = tele;
	}
	

	public String getFirstOptname() {
		return firstOptname;
	}

	public void setFirstOptname(String firstOptname) {
		this.firstOptname = firstOptname;
	}

	public String getSecondOptname() {
		return secondOptname;
	}

	public void setSecondOptname(String secondOptname) {
		this.secondOptname = secondOptname;
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

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
