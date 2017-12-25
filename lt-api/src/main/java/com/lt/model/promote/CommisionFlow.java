package com.lt.model.promote;

import java.io.Serializable;
import java.util.Date;

/**
 * 佣金流水实体
 * @author jingwb
 *
 */
public class CommisionFlow implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1649758937461357415L;
	/***/
	private Integer id;
	/***/
	private String userId;
	/**佣金流转类型：-1流出; 1进入*/
	private Integer flowType;
	
	/**一级佣金业务码*/
	private String firstOptcode;
	/**二级佣金业务码*/
	private String secondOptcode;
	/**三级佣金业务码*/
	private String thirdOptcode;
	/**变动金额*/
	private Double amount;
	/**剩余额度*/
	private Double balance;
	/**备注*/
	private String remark;
	/***/
	private Date createDate;
	/***/
	private Date modifyDate;
	
	public CommisionFlow(){
		
	}
	
	public CommisionFlow(String userId,Integer flowType,String firstOptcode,String secondOptcode,String thirdOptcode,
			Double amount,Double balance,String remark){
		this.userId = userId;
		this.flowType = flowType;
		this.firstOptcode = firstOptcode;
		this.secondOptcode = secondOptcode;
		this.thirdOptcode = thirdOptcode;
		this.amount = amount;
		this.balance = balance;
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getFlowType() {
		return flowType;
	}
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	
}
