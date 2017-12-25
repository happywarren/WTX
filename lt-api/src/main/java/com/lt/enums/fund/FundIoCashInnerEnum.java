package com.lt.enums.fund;

/**
 * 资金内部存取明细 状态枚举
 * @author guodw
 *
 */
public enum FundIoCashInnerEnum {
	//  `status` int(3) DEFAULT NULL COMMENT '状态: 0 待审核， 1已通过， 2 已拒绝',
	/** 0: 待审核*/
	AUDIT(0, "待审核"),
	/** 1: 已通过 */
	SUCCESS(1, "已通过"),
	/**  已拒绝 */
	FAIL(2, " 已拒绝"),
	
	;

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
	FundIoCashInnerEnum(Integer value, String name) {
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
