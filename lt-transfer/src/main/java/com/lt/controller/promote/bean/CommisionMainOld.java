package com.lt.controller.promote.bean;


import java.util.Date;

/**
 * 佣金实体
 * @author jingwb
 *
 */
public class CommisionMainOld{

	private String user_id;
	/**佣金余额*/
	private Double commision_balance;
	/**已提现佣金*/
	private Double withdraw_commision;
	private Date create_time;
	private Date modify_time;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Double getCommision_balance() {
		return commision_balance;
	}
	public void setCommision_balance(Double commision_balance) {
		this.commision_balance = commision_balance;
	}
	public Double getWithdraw_commision() {
		return withdraw_commision;
	}
	public void setWithdraw_commision(Double withdraw_commision) {
		this.withdraw_commision = withdraw_commision;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	
}
