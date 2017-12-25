/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.manager.bean.log;


public class OperationLogVo extends  OperationLog{
	/**
	 * 操作：0   查看 1
	 */
	private String operaType;
	
	private String menusId;
	/**
	 * 菜单名称
	 */
	private String menusName;
	
	private String name;

	public String getOperaType() {
		return operaType;
	}

	public void setOperaType(String operaType) {
		this.operaType = operaType;
	}

	public String getMenusId() {
		return menusId;
	}

	public void setMenusId(String menusId) {
		this.menusId = menusId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMenusName() {
		return menusName;
	}

	public void setMenusName(String menusName) {
		this.menusName = menusName;
	}
	
}
