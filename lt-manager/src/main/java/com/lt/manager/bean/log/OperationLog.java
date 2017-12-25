/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.manager.bean.log;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * 描述:
 *
 * @author  C.C
 * @created 2015年6月16日 下午7:50:31
 * @since   v1.0.0
 */
@Document(collection = "operationLog")
public class OperationLog {
	
	/**
	 * 员工id
	 */
	private Integer staffId;
	
	/**
	 * 员工姓名
	 */
	private String staffName;
	
	/**
	 * 访问路径
	 */
	private String url;
	
	/**
	 * 访问参数
	 */
	private String parameters;
	
	/**
	 * 访问时间
	 */
	private Date accessTime;
	
	/**
	 * 内网ip
	 */
	private String intranetIp;
	/**
	 * 外网ip
	 */
	private String outerNetIp;

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public String getIntranetIp() {
		return intranetIp;
	}

	public void setIntranetIp(String intranetIp) {
		this.intranetIp = intranetIp;
	}

	public String getOuterNetIp() {
		return outerNetIp;
	}

	public void setOuterNetIp(String outerNetIp) {
		this.outerNetIp = outerNetIp;
	}
	
}
