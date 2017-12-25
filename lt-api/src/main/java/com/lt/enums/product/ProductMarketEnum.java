/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: FundAccountType.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.enums.product;

/**
 * 商品市场状态枚举类
 * 
 * @author guodw
 *
 */
public enum ProductMarketEnum {
	/** 0: 闭市 */
	STOP_TRADING(0, "闭市"),
	/** 1: 可买不可卖（仅开仓） */
	ONLY_OPEN(1, "仅开仓"),
	/** 2：可卖不可买（仅平仓） */
	ONLY_LIQUIDATION(2, "仅平仓"),
	/**
	 * 可买卖 开市
	 */
	START_TRADING(3, "开市"),
	/**
	 * 休市
	 */
	REST(4, "休市"),
	/**
	 * 下架
	 */
	PRODUCT_STATUS_DOWN(0,"下架"),
	/**
	 * 上架
	 */
	PRODUCT_STATUS_UP(1,"上架"),
	/**
	 * 预售
	 */
	PRODUCT_STATUS_PRESELL(2,"预售"),
	/**
	 * 过期
	 */
	PRODUCT_STATUS_OVERDUE(3,"过期"),
	
	
	
	
	
	;
	/** 值 */
	private int value;
	/** 描述 */
	private String name;

	/**
	 * 构造
	 * 
	 * @author XieZhibing
	 * @date 2016年11月28日 下午9:10:02
	 * @param value
	 * @param name
	 */
	ProductMarketEnum(int value, String name) {
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
	 * @param value
	 *            值
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
	 * @param name
	 *            描述
	 */
	public void setName(String name) {
		this.name = name;
	}
}
