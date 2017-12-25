/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: FundTypeEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.fund;

/**
 * TODO（描述类的职责）
 * @author XieZhibing
 * @date 2016年11月28日 下午9:17:28
 * @version <b>1.0.0</b>
 */
public enum FundTypeEnum {
	/** 0: 现金 */
	CASH(0, "现金"),
	/** 1: 积分 */
	SCORE(1, "积分");
	
	
	/** 值 */
	private int value;
	/** 描述 */
	private String name;
	
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月28日 下午9:10:02
	 * @param value
	 * @param name
	 */
	FundTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	/** 
	 * 获取 值 
	 * @return value 
	 */
	public int getValue() {
		return value;
	}
	/** 
	 * 设置 值 
	 * @param value 值 
	 */
	public void setValue(int value) {
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
