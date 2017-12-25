package com.lt.controller.user.bean;

import java.util.Date;

import com.lt.util.utils.DateTools;

/**   
* 项目名称：lt-transfer   
* 类名称：UserProductChoose   
* 类描述：用户自选股品种  
* 创建人：yuanxin   
* 创建时间：2017年5月18日 下午5:20:38      
*/
public class UserProductChoose {
	
	/** 用户id*/
	private String user_id;
	/** 产品id*/
	private Integer product_id;
	/** 创建时间*/
	private String create_date;
	
	public UserProductChoose(){
		
	}
	
	public UserProductChoose(String user_id,Integer product_id){
		this.user_id = user_id ;
		this.product_id = product_id ;
		this.create_date = DateTools.formatDate(new Date(), DateTools.FORMAT_LONG);
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
	 * @return the product_id
	 */
	public Integer getProduct_id() {
		return product_id;
	}
	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
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
