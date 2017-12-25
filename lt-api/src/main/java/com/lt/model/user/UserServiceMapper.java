package com.lt.model.user;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserServiceMapper   
* 类描述：   用户关联服务对象
* 创建人：yuanxin   
* 创建时间：2016年12月7日 下午4:50:51      
*/
public class UserServiceMapper implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id;
	/** 用户id*/
	private String userId;
	/** 服务编号*/
	private String serviceCode;
	/** 开启时间*/
	private Date openTime;
	/** 运行状态 0：正常运行中，1：关闭*/
	private String status;
	/** 创建时间*/
	private Date createDate;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}
	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	/**
	 * @return the openTime
	 */
	public Date getOpenTime() {
		return openTime;
	}
	/**
	 * @param openTime the openTime to set
	 */
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
