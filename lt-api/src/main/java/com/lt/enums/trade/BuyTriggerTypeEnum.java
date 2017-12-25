package com.lt.enums.trade;
/**
 * 买的触发方式
 * @author jingwb
 *
 */
public enum BuyTriggerTypeEnum {

	CUSTOMER(100,"用户触发"),
	ADMINISTRATOR(101,"人工触发"),
	OTHER(200,"其他方式")
	;
	
	/** 值 */
	private Integer value;
	/** 备注 */
	private String remark;
	
	BuyTriggerTypeEnum(Integer value,String remark){
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
