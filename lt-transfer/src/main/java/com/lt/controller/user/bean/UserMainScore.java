package com.lt.controller.user.bean;

/**   
* 项目名称：lt-transfer   
* 类名称：FundMainScore   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年5月17日 下午1:32:54      
*/
public class UserMainScore {
	
	/** 用户userId*/
	private String user_id ;
	/** 用户可用积分*/
	private Double balance ;
	/** 用户赠送积分*/
	private Double total_present_amount;
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
