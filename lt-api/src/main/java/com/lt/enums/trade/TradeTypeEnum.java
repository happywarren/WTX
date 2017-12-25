package com.lt.enums.trade;
/**
 * 交易类型
 * @author jingwb
 *
 */
public enum TradeTypeEnum {
	/** 1 开仓 */
	BUY(1, "开仓"),
	/** 2平仓 */
	SELL(2 , "平仓");

	/** 值 */
	private int value;
	/** 名称 */
	private String name;
	
	TradeTypeEnum(int value,String name){
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
