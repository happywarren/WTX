/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: FundTypeEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.enums.fund;

/**
 * TODO 提现状态
 * 
 */
public enum FundTransferEnum {
	/** 0: 待转中 */
	AUDIT(0, "转账中"),
	/** 1: 转账成功 */
	SUCCEED(1, "转账成功"),
	/** 转账失败 */
	FAILED(2, "转账失败"),
	
	/** 转账记录 已处理*/
	TREATED(1,"已处理"),
	/** 转账记录 未处理*/
	UNTREATED(0,"未处理");

	/** 值 */
	private Integer value;
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
	FundTransferEnum(Integer value, String name) {
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
	 * @param value
	 *            值
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
	 * @param name
	 *            描述
	 */
	public void setName(String name) {
		this.name = name;
	}
}
