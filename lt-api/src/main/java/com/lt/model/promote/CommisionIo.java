package com.lt.model.promote;

import java.io.Serializable;
import java.util.Date;

/**
 * 佣金存取明细实体
 * @author jingwb
 *
 */
public class CommisionIo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6477212997807123733L;
	/***/
	private Integer id;
	/***/
	private String userId;
	/**资金流转类型: -1 支出;  1 收入*/
	private Integer flowType;
	/**一级佣金业务码*/
	private String firstOptcode;
	/**二级佣金业务码*/
	private String secondOptcode;
	/**三级佣金业务码*/
	private String thirdOptcode;
	/**当前变动金额*/
	private Double amount;
	/**佣金余额*/
	private Double balance;
	/**创建时间*/
	private Date createDate;
	/**审核时间*/
	private Date auditDate;
	/**修改时间*/
	private Date modifyDate;
	/**修改人ID*/
	private Integer modifyUserId;
	/**详情说明*/
	private String remark;
	/**状态: 0 待审核， 1已通过， 2 已拒绝*/
	private Integer status;
	
	public CommisionIo(){
		
	}
	
	public CommisionIo(String userId,Integer flowType,String firstOptcode,String secondOptcode,
			String thirdOptcode,Double amount,Double balance,Integer modifyUserId,String remark,Integer status){
		this.userId = userId;
		this.flowType = flowType;
		this.firstOptcode = firstOptcode;
		this.secondOptcode = secondOptcode;
		this.thirdOptcode = thirdOptcode;
		this.amount = amount;
		this.balance = balance;
		this.modifyUserId = modifyUserId;
		this.remark = remark;
		this.status = status;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
