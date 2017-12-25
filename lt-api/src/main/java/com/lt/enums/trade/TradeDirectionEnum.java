package com.lt.enums.trade;
/**
 * 交易方向 
 * @author jingwb
 *
 */
public enum TradeDirectionEnum {

	/** 1 看多 */
	DIRECTION_UP(1, "看多"),
	/** 2看空 */
	DIRECTION_DOWN(2 , "看空");

	/** 值 */
	private int value;
	/** 名称 */
	private String name;
	
	TradeDirectionEnum(int value,String name){
		this.value = value;
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
