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
public enum FundIoWithdrawalEnum {
	/** 0: 待审核 */
	AUDIT(0, "待审核"),
	/** 1: 待转账 */
	WAIT(1, "待转账"),
	/** 提现拒绝 */
	REPULSE(2, "提现拒绝"),
	/**
	 * 转账中
	 */
	PROCESS(3, "转账中"),
	/**
	 * 转账失败
	 */
	FAILURE(4, "转账失败"),
	/**
	 * 转账成功
	 */
	SUCCEED(5, " 转账成功"),
	/**
	 * 提现撤销
	 */
	REVOCATION(6, "提现撤销"),
	
	/**
	 * 转账驳回
	 */
	TREPULSE(7, "转账驳回");

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
	FundIoWithdrawalEnum(Integer value, String name) {
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
