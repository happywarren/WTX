/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.manager.bean.log;


/**
 *
 * 描述:
 *
 * @author  C.C
 * @created 2015年6月16日 下午8:04:55
 * @since   v1.0.0
 */
public class OperationLogQuery {
	
	private Integer staffId;
	
	private String url;
	
	private String parameters;
	
	private String startAccessTime;
	
	private String endAccessTime;
	
	
	
	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
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

	public String getStartAccessTime() {
		return startAccessTime;
	}

	public void setStartAccessTime(String startAccessTime) {
		this.startAccessTime = startAccessTime;
	}

	public String getEndAccessTime() {
		return endAccessTime;
	}

	public void setEndAccessTime(String endAccessTime) {
		this.endAccessTime = endAccessTime;
	}

	
}
