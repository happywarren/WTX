/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: FundFlowTypeEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.fund;

/**
 * TODO 资金账号开启状态
 * @author XieZhibing
 * @date 2016年11月28日 下午9:02:17
 * @version <b>1.0.0</b>
 */
public enum FundFlowTypeEnum {
	/** 1: 收入 */
	INCOME(1, "收入"), 
	/** -1: 支出 */
	OUTLAY(-1, "支出");
	
	/** 值 */
	private Integer value;
	/** 描述 */
	private String name;
	
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月28日 下午9:10:02
	 * @param value
	 * @param name
	 */
	FundFlowTypeEnum(Integer value, String name) {
		this.value = value;
		this.name = name;
	}
	/** 
	 * 获取 值 
	 * @return value 
	 */
	public Integer getValue() {
		return value;
	}
	/** 
	 * 设置 值 
	 * @param value 值 
	 */
	public void setValue(Integer value) {
		this.value = value;
	}
	/** 
	 * 获取 描述 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	/** 
	 * 设置 描述 
	 * @param name 描述 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
