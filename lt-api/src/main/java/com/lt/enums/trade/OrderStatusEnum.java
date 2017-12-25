/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.trade
 * FILE    NAME: OrderStatusEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.trade;

/**
 * TODO 订单状态
 * @author XieZhibing
 * @date 2016年12月10日 下午2:45:53
 * @version <b>1.0.0</b>
 */
public enum OrderStatusEnum {

	/** 0: 初始化  */
	INITIALIZE(0, "初始化"), 
	/** 1: 委托中  */
	ENTRUST(1, "委托中"), 
	/** 2: 持仓中  */
	HOLD(2, "持仓中"),
	/** 3: 失败  */
	FAIL(3, "失败"),
	/** 4: 完成  */
	SUCCESS(4, "完成");

	/** 值 */
	private Integer value;
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
	OrderStatusEnum(Integer value, String name) {
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
