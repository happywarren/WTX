package com.lt.enums.trade;
/**
 * 卖的触发方式
 * @author jingwb
 *
 */
public enum SellTriggerTypeEnum {

	CUSTOMER(100,"用户触发"),
	ADMINISTRATOR(101,"人工触发"),
	CLEARANCE(102,"系统清仓"),
	RISK(103,"风控触发"),
	STOPPROFIT(104,"止盈"),
	STOPLOSS(105,"止损"),
	MOVESTOPLOSS(106,"移动止损"),
	OTHER(200,"其他"),
	
	;
	/** 值 */
	private Integer value;
	/** 备注 */
	private String remark;
	
	SellTriggerTypeEnum(Integer value,String remark){
		this.value = value;
		this.remark = remark;
	}
	
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
