package com.lt.manager.bean.log;


import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


/**
 * 
 *
 * 描述:后台操作日志
 *
 * @author  郭达望
 * @created 2015年6月29日 下午5:03:58
 * @since   v1.0.0
 */
@Document(collection = "lt_manager_log")
public class LtManagerLog implements Serializable{

	@Id
	private String id;

	@Field
	@Indexed
	private Integer staffId;
	
	private String userName;

	/**
	 * 操作类型，参考LoggerType
	 */
	private Integer type;

	/**
	 * 操作类型说明，参考LoggerType
	 */
	private String typeName;

	/**
	 * 操作是否成功：true:成功，false：失败
	 */
	private Boolean isSuccessed;

	/**
	 * 日志内容
	 */
	private String content;


	/**
	 * ip
	 */
	private String ip;


	@Field
	private Date createTime;
	
	private Integer lastLoginId;
	
	private String lastLoginName;

	
	public LtManagerLog() {
	}


	public LtManagerLog(Integer staffId, String userName, Integer type, String typeName, Boolean isSuccessed, String content, String ip, Integer lastLoginId, String lastLoginName) {
		this.staffId = staffId;
		this.userName = userName;
		this.type = type;
		this.typeName = typeName;
		this.isSuccessed = isSuccessed;
		this.content = content;
		this.ip = ip;
		this.createTime =  new Date();
		this.lastLoginId = lastLoginId;
		this.lastLoginName = lastLoginName;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Integer getstaffId() {
		return staffId;
	}

	public void setstaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Boolean getIsSuccessed() {
		return isSuccessed;
	}

	public void setIsSuccessed(Boolean isSuccessed) {
		this.isSuccessed = isSuccessed;
	}


	public Integer getStaffId() {
		return staffId;
	}


	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Integer getLastLoginId() {
		return lastLoginId;
	}


	public void setLastLoginId(Integer lastLoginId) {
		this.lastLoginId = lastLoginId;
	}


	public String getLastLoginName() {
		return lastLoginName;
	}


	public void setLastLoginName(String lastLoginName) {
		this.lastLoginName = lastLoginName;
	}

}
