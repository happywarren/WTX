package com.lt.model.user;

import java.io.Serializable;

/**   
* 项目名称：lt-api   
* 类名称：UserService   
* 类描述： 用户相关服务对象  
* 创建人：yuanxin   
* 创建时间：2016年12月7日 下午4:43:48      
*/
public class UserService implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id;
	/** 服务编码*/
	private String serviceCode;
	/** 服务名称*/
	private String serviceName;
	/** 服务类型，1 功能性服务 2 账户性服务*/
	private String type;
	/** 是否被使用 0 可用 1不可用*/
	private String isUse;
	/** 创建时间*/
	private String createDate;
	/** 默认开启状态 0：默认开启 1：默认关闭*/
	private Integer defaultStatus;
	/** 用户id*/
	private String userId;
	/** 图片地址的url*/
	private String servicePicUrl;
	/** 服务的标题名称*/
	private String serviceTitle;
	
	
	/** 用户首页使用字段beg */
	private Double amt;
	
	private Double floatAmt;
	
	private Double holdAmt;
	
	private String detailUrl;
	/** 用户首页使用字段end */
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
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the isUse
	 */
	public String getIsUse() {
		return isUse;
	}
	/**
	 * @param isUse the isUse to set
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the defaultStatus
	 */
	public Integer getDefaultStatus() {
		return defaultStatus;
	}
	/**
	 * @param defaultStatus the defaultStatus to set
	 */
	public void setDefaultStatus(Integer defaultStatus) {
		this.defaultStatus = defaultStatus;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the servicePicUrl
	 */
	public String getServicePicUrl() {
		return servicePicUrl;
	}
	/**
	 * @param servicePicUrl the servicePicUrl to set
	 */
	public void setServicePicUrl(String servicePicUrl) {
		this.servicePicUrl = servicePicUrl;
	}
	/**
	 * @return the serviceTitle
	 */
	public String getServiceTitle() {
		return serviceTitle;
	}
	/**
	 * @param serviceTitle the serviceTitle to set
	 */
	public void setServiceTitle(String serviceTitle) {
		this.serviceTitle = serviceTitle;
	}
	/**
	 * @return the amt
	 */
	public Double getAmt() {
		return amt;
	}
	/**
	 * @param amt the amt to set
	 */
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	/**
	 * @return the floatAmt
	 */
	public Double getFloatAmt() {
		return floatAmt;
	}
	/**
	 * @param floatAmt the floatAmt to set
	 */
	public void setFloatAmt(Double floatAmt) {
		this.floatAmt = floatAmt;
	}
	/**
	 * @return the holdAmt
	 */
	public Double getHoldAmt() {
		return holdAmt;
	}
	/**
	 * @param holdAmt the holdAmt to set
	 */
	public void setHoldAmt(Double holdAmt) {
		this.holdAmt = holdAmt;
	}
	/**
	 * @return the detailUrl
	 */
	public String getDetailUrl() {
		return detailUrl;
	}
	/**
	 * @param detailUrl the detailUrl to set
	 */
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
	
}
