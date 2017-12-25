package com.lt.controller.user.bean;

/**   
* 项目名称：lt-transfer   
* 类名称：UserMainCash   
* 类描述： 用户资金主表 
* 创建人：yuanxin   
* 创建时间：2017年5月16日 下午7:45:56      
*/
public class UserMainCash {
	
	/** 可用余额*/
	private Double balance ;
	/** 累计存入金额*/
	private Double total_recharge_amount ;
//	/** 累计提现金额*/
//	private Double total_draw_amount ;
	/** 用户id*/
	private String user_id;
//	/** 累计盈亏*/
//	private Double total_benefit_amount;
//	/** 累计手续费*/
//	private Double total_counter_fee;
//	/** 累计递延费*/
//	private Double total_interest_amount;
	/** 累积赠送金额*/
	private Double total_present_amount ;
	
	
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
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the total_recharge_amount
	 */
	public Double getTotal_recharge_amount() {
		return total_recharge_amount;
	}
	/**
	 * @param total_recharge_amount the total_recharge_amount to set
	 */
	public void setTotal_recharge_amount(Double total_recharge_amount) {
		this.total_recharge_amount = total_recharge_amount;
	}
//	/**
//	 * @return the total_draw_amount
//	 */
//	public Double getTotal_draw_amount() {
//		return total_draw_amount;
//	}
//	/**
//	 * @param total_draw_amount the total_draw_amount to set
//	 */
//	public void setTotal_draw_amount(Double total_draw_amount) {
//		this.total_draw_amount = total_draw_amount;
//	}
//	/**
//	 * @return the total_benefit_amount
//	 */
//	public Double getTotal_benefit_amount() {
//		return total_benefit_amount;
//	}
//	/**
//	 * @param total_benefit_amount the total_benefit_amount to set
//	 */
//	public void setTotal_benefit_amount(Double total_benefit_amount) {
//		this.total_benefit_amount = total_benefit_amount;
//	}
//	/**
//	 * @return the total_counter_fee
//	 */
//	public Double getTotal_counter_fee() {
//		return total_counter_fee;
//	}
//	/**
//	 * @param total_counter_fee the total_counter_fee to set
//	 */
//	public void setTotal_counter_fee(Double total_counter_fee) {
//		this.total_counter_fee = total_counter_fee;
//	}
//	/**
//	 * @return the total_interest_amount
//	 */
//	public Double getTotal_interest_amount() {
//		return total_interest_amount;
//	}
//	/**
//	 * @param total_interest_amount the total_interest_amount to set
//	 */
//	public void setTotal_interest_amount(Double total_interest_amount) {
//		this.total_interest_amount = total_interest_amount;
//	}
	/**
	 * @return the total_present_amount
	 */
	public Double getTotal_present_amount() {
		return total_present_amount;
	}
	/**
	 * @param total_present_amount the total_present_amount to set
	 */
	public void setTotal_present_amount(Double total_present_amount) {
		this.total_present_amount = total_present_amount;
	}
	
}
