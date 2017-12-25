/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.trade
 * FILE    NAME: PlateEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.trade;

/**
 * TODO 内外盘标识
 * @author XieZhibing
 * @date 2016年12月11日 下午4:40:05
 * @version <b>1.0.0</b>
 */
public enum PlateEnum {
	
	/** 0 内盘 */
	INNER_PLATE(0, "内盘"),
	/** 1 外盘 */
	OUTER_PLATE(1, "外盘"),
	/** 2 差价合约 */
	CONTRACT_FOR_DIFFERENCE(2, "差价合约"),
	;
	
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
	PlateEnum(Integer value, String name) {
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

	/**
	 * 根据名称获取值
	 *
	 * @param name 名称
	 */
	public static Integer getValueByName(String name){
		for(PlateEnum plateEnum : PlateEnum.values()){
			if(plateEnum.name.equals(name)){
				return plateEnum.value;
			}
		}

		//没配置则默认返回外盘
		return OUTER_PLATE.value;
	}
	
}
