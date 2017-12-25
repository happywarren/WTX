package com.lt.manager.bean.user;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 *
 * 描述: 用户列表页面返回对象
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class UserService{
	/**
	 *  ID 
	 */
	private int id;
	/**
	 *  用户ID 
	 */
	private String serviceCode;
	/**
	 *  真实姓名 
	 */
	private String serviceName;
	/**
	 *  实名状态
	 */
	private int type;
	
	/**
	 *  实名状态
	 */
	private int isUse;	
	/**
	 *  实名状态
	 */
	private Date create_date;
	/**
	 *  实名状态
	 */
	private int defaultStatus;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsUse() {
		return isUse;
	}
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public int getDefaultStatus() {
		return defaultStatus;
	}
	public void setDefaultStatus(int defaultStatus) {
		this.defaultStatus = defaultStatus;
	}
 }


