package com.lt.controller.promote.bean;



import java.util.Date;


/**
 * 
 *
 * 描述:推广详情返回对象
 *
 * @author 郭达望
 * @created 2015年6月8日 下午6:15:56
 * @author jiupeng
 * @modify 2015年8月27日 11:27:42
 * @since v1.0.0
 */
public class UserPromoteDetailVo extends UserPromoteInfo {

	private Integer id;

	private Integer promote_id;


	private Integer user_id;


	/**
	 * 总充值
	 */
	private Double store_amt;

	private Double counter_fee;

	private Integer consumer_sum; // 手数

	private Date create_date;

	
	public Double getStore_amt() {
		return store_amt;
	}

	public void setStore_amt(Double store_amt) {
		this.store_amt = store_amt;
	}

	public Double getCounter_fee() {
		return counter_fee;
	}

	public void setCounter_fee(Double counter_fee) {
		this.counter_fee = counter_fee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPromote_id() {
		return promote_id;
	}

	public void setPromote_id(Integer promote_id) {
		this.promote_id = promote_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getConsumer_sum() {
		return consumer_sum;
	}

	public void setConsumer_sum(Integer consumer_sum) {
		this.consumer_sum = consumer_sum;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	
}
