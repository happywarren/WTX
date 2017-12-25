package com.lt.manager.bean.enums.product;

/**
 * 管理系统根据资金类型查询用户枚举
 * @author jingwb
 *
 */
public enum CashColumnEnum { 
	BALANCE("balance","01","可用余额"),
	HOLD_FUND("hold_fund","02","持仓保证金"),
	DEFER_FUND("defer_fund","03","递延保证金"),
	TOTAL_COUNTER_FEE("total_counter_fee","04","累计交易手续费"),
	TOTAL_INTEREST_AMOUNT("total_interest_amount","05","累计递延保证金利息"),
	TOTAL_RECHARGE_AMOUNT("total_recharge_amount","06","累计存入"),
	TOTAL_DRAW_AMOUNT("total_draw_amount","07","累计取出"),
	//此分号不要删除
	;
	private String value;
	
	private String code;
	
	private String name;
	
	private CashColumnEnum(String value,String code,String name){
		this.value=value;
		this.code = code;
		this.name=name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
