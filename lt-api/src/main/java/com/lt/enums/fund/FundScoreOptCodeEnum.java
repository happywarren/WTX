/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: FundScoreOptCodeEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.fund;

/**
 * TODO 积分业务编码
 * @author XieZhibing
 * @date 2016年12月6日 上午11:11:03
 * @version <b>1.0.0</b>
 */
public enum FundScoreOptCodeEnum implements IFundOptCode {

	/************* 积分 收入 *************/
	/** 30101 兑换积分 */
	RECHARGE("30101", "兑换积分", "301", "兑换",  1),
	/** 30201 任务存入(赠送) */
	PRESENT("30201", "系统存入", "302", "赠送", 1),
	/** 30301 人工存入*/
	MANUALIN("30301", "人工存入", "303", "手动存入", 1),
	/** 30301 人工存入*/
	MANUALIN_REJECT("30302", "人工取出拒绝", "303", "手动存入", 1),
	/** 30401 结算盈利 */
	USER_PROFIT ("30401", "结算盈利", "304", "结盈", 1),
	/** 30501 止损保证金解冻 */
	UNFREEZE_HOLDFUND("30501",  "保证金解冻", "305", "解冻", 1),
	/** 30502 止盈金解冻 */
	UNFREEZE_STOPPROFIT("30502", "止盈金解冻", "305", "解冻", 1),
	/** 30501 递延金解冻 */
	UNFREEZE_DEFERFUND("30503", "递延金解冻", "305", "解冻", 1),
	/** 30601 手续费退款 */
	REFUND_PLATFORM_FEE("30601", "手续费退款", "306", "退款", 1),

	/************* 积分 支出  *************/	
	/** 40101 积分消费 */
	WITHDRAW("40101", "积分消费", "401", "消费", -1),
	/** 40201 结算亏损 */
	USER_LOSS("40201", "结算亏损", "402", "结亏", -1),
	/** 40301 人工取出 */
	MANUALOUT("40301", "人工取出", "403", "手动取出", -1),
	/** 30301 人工取出审核失败*/
	MANUALIN_FAIL("40302", "人工存入审核拒绝", "403", "手动存取", -1),
	/** 40401 止损保证金冻结 */
	FREEZE_HOLDFUND("40401", "保证金冻结", "404", "冻结", -1),
	/** 40402 止盈金冻结 */
	FREEZE_STOPPROFIT("40402", "止盈金冻结", "404", "冻结", -1),
	/** 40403 递延金冻结 */
	FREEZE_DEFERFUND("40403", "递延金冻结", "404", "冻结", -1),
	
	/** 40501 手续费扣除 */
	DEDUCT_PLATFORM_FEE("40501", "手续费扣除", "405", "支付", -1),	
	/** 40502 利息扣除 */
	DEDUCT_INTEREST("40502", "递延费扣除", "405", "支付", -1),	
	;
	
	/** 业务编码 */
	private String code;	
	/** 业务编码描述 */
	private String name;
	/** 一级业务编码 */
	private String firstLevelCode;
	/** 一级业务编码描述 */
	private String firstLevelname;
	/** 资金进出方向: -1 支出;  1 收入 */
	private Integer inout;
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月6日 上午11:27:01
	 * @param code
	 * @param name
	 * @param firstLevelCode
	 * @param firstLevelname
	 * @param inout
	 */
	FundScoreOptCodeEnum(String code, String name, String firstLevelCode,
			String firstLevelname, Integer inout) {
		this.code = code;
		this.name = name;
		this.firstLevelCode = firstLevelCode;
		this.firstLevelname = firstLevelname;
		this.inout = inout;
	}
	/** 
	 * 获取 业务编码 
	 * @return code 
	 */
	public String getCode() {
		return code;
	}
	/** 
	 * 设置 业务编码 
	 * @param code 业务编码 
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/** 
	 * 获取 业务编码描述 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	/** 
	 * 设置 业务编码描述 
	 * @param name 业务编码描述 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/** 
	 * 获取 一级业务编码 
	 * @return firstLevelCode 
	 */
	public String getFirstLevelCode() {
		return firstLevelCode;
	}
	/** 
	 * 设置 一级业务编码 
	 * @param firstLevelCode 一级业务编码 
	 */
	public void setFirstLevelCode(String firstLevelCode) {
		this.firstLevelCode = firstLevelCode;
	}
	/** 
	 * 获取 一级业务编码描述 
	 * @return firstLevelname 
	 */
	public String getFirstLevelname() {
		return firstLevelname;
	}
	/** 
	 * 设置 一级业务编码描述 
	 * @param firstLevelname 一级业务编码描述 
	 */
	public void setFirstLevelname(String firstLevelname) {
		this.firstLevelname = firstLevelname;
	}
	/** 
	 * 获取 资金进出方向: -1 支出;  1 收入 
	 * @return inout 
	 */
	public Integer getInout() {
		return inout;
	}
	/** 
	 * 设置 资金进出方向: -1 支出;  1 收入 
	 * @param inout 资金进出方向: -1 支出;  1 收入 
	 */
	public void setInout(Integer inout) {
		this.inout = inout;
	}
	
	
}
