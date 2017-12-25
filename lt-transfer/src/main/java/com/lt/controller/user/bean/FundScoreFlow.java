package com.lt.controller.user.bean;

/**   
* 项目名称：lt-transfer   
* 类名称：FundScoreFlow   
* 类描述：积分现金流   
* 创建人：yuanxin   
* 创建时间：2017年5月17日 上午11:10:09      
*/
public class FundScoreFlow {
	
	/** 用户id*/
	private String user_id ;
	/** 积分流转类型*/
	private Integer flow_type ;
	/** 一级业务码*/
	private String first_optcode;
	/** 二级业务码*/
	private String second_optcode;
	/** 三级业务码 */
	private String third_optcode;
	/** 金额*/
	private Double amount;
	/** 余额*/
	private Double balance;
	/** 备注*/
	private String remark;
	/** 创建日期*/
	private String create_date;
	
	public FundScoreFlow(){
		
	}
	
	public FundScoreFlow(String user_id,Integer flow_type ,String first_optcode,String second_optcode,
			String third_optcode, Double amount, Double balance,String remark,String create_date){
		this.user_id = user_id;
		this.flow_type = flow_type ;
		this.first_optcode = first_optcode ;
		this.second_optcode = second_optcode ;
		this.third_optcode = third_optcode;
		this.amount = amount ;
		this.balance = balance ;
		this.remark = remark ;
		this.create_date = create_date ;
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
	 * @return the flow_type
	 */
	public Integer getFlow_type() {
		return flow_type;
	}
	/**
	 * @param flow_type the flow_type to set
	 */
	public void setFlow_type(Integer flow_type) {
		this.flow_type = flow_type;
	}
	/**
	 * @return the first_optcode
	 */
	public String getFirst_optcode() {
		return first_optcode;
	}
	/**
	 * @param first_optcode the first_optcode to set
	 */
	public void setFirst_optcode(String first_optcode) {
		this.first_optcode = first_optcode;
	}
	/**
	 * @return the second_optcode
	 */
	public String getSecond_optcode() {
		return second_optcode;
	}
	/**
	 * @param second_optcode the second_optcode to set
	 */
	public void setSecond_optcode(String second_optcode) {
		this.second_optcode = second_optcode;
	}
	/**
	 * @return the third_optcode
	 */
	public String getThird_optcode() {
		return third_optcode;
	}
	/**
	 * @param third_optcode the third_optcode to set
	 */
	public void setThird_optcode(String third_optcode) {
		this.third_optcode = third_optcode;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the create_date
	 */
	public String getCreate_date() {
		return create_date;
	}
	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
}
