/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.trade
 * FILE    NAME: DeferStatusEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.enums.trade;

/**
 * TODO 递延状态枚举
 * 
 * @author XieZhibing
 * @date 2016年12月10日 上午11:15:15
 * @version <b>1.0.0</b>
 */
public enum DeferStatusEnum {
	
	/** 1: 递延 */
	DEFER(1, "递延不清仓"), 
	/** 0: 非递延 */
	NOT_DEFER(0, "非递延");

	/** 值 */
	private int value;
	/** 描述 */
	private String name;

	/**
	 * 
	 * 构造
	 * 
	 * @author XieZhibing
	 * @date 2016年12月10日 上午11:20:56
	 * @param value
	 * @param name
	 */
	DeferStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 获取 值
	 * 
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * 设置 值
	 * 
	 * @param value 值
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * 获取 描述
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 描述
	 * 
	 * @param name 描述
	 */
	public void setName(String name) {
		this.name = name;
	}

}
