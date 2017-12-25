package com.lt.model.promote;

import java.io.Serializable;
import java.util.Date;

/**
 * 佣金实体
 * @author jingwb
 *
 */
public class CommisionMain implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1631577112736841844L;
	private Integer id;
	private String userId;
	/**佣金余额*/
	private Double commisionBalance;
	/**已提现佣金*/
	private Double withdrawCommision;
	private Date createTime;
	private Date modifyTime;
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
	public Double getCommisionBalance() {
		return commisionBalance;
	}
	public void setCommisionBalance(Double commisionBalance) {
		this.commisionBalance = commisionBalance;
	}
	public Double getWithdrawCommision() {
		return withdrawCommision;
	}
	public void setWithdrawCommision(Double withdrawCommision) {
		this.withdrawCommision = withdrawCommision;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
