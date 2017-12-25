package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.CalendarTools;

/**   
* 项目名称：lt-api   
* 类名称：FundAlipaymRecharge   
* 类描述： 支付宝充值（手工）  
* 创建人：yuanxin   
* 创建时间：2017年7月11日 下午7:13:51      
*/
public class FundAlipaymRecharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id ;
	/** 支付宝订单id*/
	private String alipayId;
	/** 支付宝账号*/
	private String alipayNum ;
	/** 用户id*/
	private String userId;
	
	public FundAlipaymRecharge(){
		
	}
	
	/**
	 * 
	 * @param alipayNum 支付宝账号
	 * @param userId 用户id
	 */
	public FundAlipaymRecharge(String alipayNum,String userId){
		this.alipayId = "alipaym"+String.valueOf(CalendarTools.getMillis(new Date()));
		this.alipayNum = alipayNum ;
		this.userId = userId ;
	}
	/** 
	* 获取id  
	*/
	public Integer getId() {
		return id;
	}
	/** 
	* 获取id  
	*/
	public void setId(Integer id) {
		this.id = id;
	}
	/** 
	* 获取alipayId  
	*/
	public String getAlipayId() {
		return alipayId;
	}
	/** 
	* 获取alipayId  
	*/
	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
	}
	/** 
	* 获取alipayNum  
	*/
	public String getAlipayNum() {
		return alipayNum;
	}
	/** 
	* 获取alipayNum  
	*/
	public void setAlipayNum(String alipayNum) {
		this.alipayNum = alipayNum;
	}
	/** 
	* 获取userId  
	*/
	public String getUserId() {
		return userId;
	}
	/** 
	* 获取userId  
	*/
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
