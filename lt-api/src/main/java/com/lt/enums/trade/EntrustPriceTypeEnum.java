/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.trade
 * FILE    NAME: EntrustPriceTypeEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.trade;

/**
 * TODO（描述类的职责）
 * @author XieZhibing
 * @date 2016年12月11日 上午10:43:56
 * @version <b>1.0.0</b>
 */
public enum EntrustPriceTypeEnum {
	/** 1:市价 */
	MARKET_PRICE(1, "市价"),
	/** 2:限价 */
	LIMIT_PRICE(2, "限价");

	/** 值 */
	private Integer value;
	/** 描述 */
	private String name;

	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月11日 上午10:44:49
	 * @param value
	 * @param name
	 */
	EntrustPriceTypeEnum(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 获取 值
	 * 
	 * @return value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 设置 值
	 * 
	 * @param value 值
	 */
	public void setValue(Integer value) {
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
