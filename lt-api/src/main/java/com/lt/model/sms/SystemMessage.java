package com.lt.model.sms;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：SystemMessage   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年11月29日 下午4:16:19      
*/
public class SystemMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id;
	/** 用户id*/
	private String userId;
	/** 发送目的地*/
	private String destination;
	/** 发送主题*/
	private String subject;
	/** 发送内容*/
	private String content;
	/** 发送原因*/
	private String cause;
	/** 通知类型（2短信 ，默认为短信）*/
	private Integer type;
	/** 信息类型 */
	private Integer smsType;
	/** 优先级*/
	private Integer priority;
	/** 通知状态*/
	private Integer status;
	/** 尝试发送次数*/
	private Integer tryCount;
	/** 创建时间*/
	private Date createDate;
	/** 发送时间*/
	private Date sendDate;
	/** 发送类型1 用户 0 系统*/
	private Integer userType;
	/** 操作者ip*/
	private String ip ;
	/** 品牌id*/
	private String  brandId ;
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
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the cause
	 */
	public String getCause() {
		return cause;
	}
	/**
	 * @param cause the cause to set
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the smsType
	 */
	public Integer getSmsType() {
		return smsType;
	}
	/**
	 * @param smsType the smsType to set
	 */
	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}
	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the tryCount
	 */
	public Integer getTryCount() {
		return tryCount;
	}
	/**
	 * @param tryCount the tryCount to set
	 */
	public void setTryCount(Integer tryCount) {
		this.tryCount = tryCount;
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
	/**
	 * @return the sendDate
	 */
	public Date getSendDate() {
		return sendDate;
	}
	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	/**
	 * @return the userType
	 */
	public Integer getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
