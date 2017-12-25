package com.lt.vo.fund;

import java.io.Serializable;
import java.util.Date;

/**
 * 资金流水返回实体
 * @author guodw
 *
 */
public class FundFlowVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5694864433349629068L;
	/** 收支额度 */
	private double amount = 0.00;
	/** 备注 */
	private String remark;
	/** 创建时间(入账时间) */
	private Date createDate;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
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
	
	
	
}

