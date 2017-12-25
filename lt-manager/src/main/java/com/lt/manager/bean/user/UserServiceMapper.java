package com.lt.manager.bean.user;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 *
 * 描述: 用户服务和用户映射表
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class UserServiceMapper{
	/**
	 *  ID 
	 */
	private int id;
	/**
	 *  用户ID 
	 */
	private String userId;
	/**
	 *  F服务编码
	 */
	private String serviceCode;
	
	/**
	 *  F服务名称
	 */
	private String serviceName;
	/**
	 *  状态
	 */
	private int status;
	
	/**
	 *  激活时间
	 */
	private Date openTime;
	/**
	 *  创建日期
	 */
	private Date createDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
 }


