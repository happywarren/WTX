package com.lt.vo.trade;

import java.io.Serializable;
/**
 * 跟单对象
 * @author sky
 *
 */
public class FollowerVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	/**
	 * 跟单资金类型，0:现金和1:积分
	 */
	private Integer followType;
	
	private String orderId;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getFollowType() {
		return followType;
	}
	public void setFollowType(Integer followType) {
		this.followType = followType;
	}
	
	

}
