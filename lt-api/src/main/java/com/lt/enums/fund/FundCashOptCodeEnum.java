/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: FundCashOptCodeEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.fund;

import java.io.Serializable;

/**
 * TODO 现金业务编码
 * @author XieZhibing
 * @date 2016年12月6日 上午11:11:21
 * @version <b>1.0.0</b>
 */
public enum FundCashOptCodeEnum implements IFundOptCode,Serializable{

	/************* 现金 收入 *************/
	/** 10101 充值 */
	RECHARGE("10101", "入金存入", "101", "充值", 1),
	/** 10102 充值补单 */
	RECHARGENOTE("10102", "充值补单", "101", "充值", 1),
	/** 10201 赠送--任务存入 */
	PRESENT("10201", "系统存入", "102", "赠送", 1),
	/** 10201 赠送--任务存入 */
	TRANSFER("10202", "系统迁移", "102", "赠送", 1),
	/** 10301 人工存入*/
	MANUALIN("10301", "人工存入", "103", "手动存入", 1),
	/** 10301 人工存入*/
	MANUALOUT_REJECT("10302", "人工取出拒绝", "103", "手动存入", 1),
	/** 10401 用户结算盈利 */
	USER_PROFIT("10401", "结算盈利", "104", "结盈", 1),	
	/** 10501 保证金解冻 */
	UNFREEZE_HOLDFUND("10501", "保证金解冻", "105", "解冻", 1),
	/** 10501 递延金解冻 */
	UNFREEZE_DEFERFUND("10502", "递延金解冻", "105", "解冻", 1),
	/** 10601 平台抽成手续费退款 */
	REFUND_PLATFORM_FEE("10601", "手续费退款", "106", "退款", 1),	
	/** 10602 提现拒绝 */
	REJECT_WITHDRAW("10602", "出金拒绝", "106", "退款", 1),	
	/** 10603 提现撤销 */
	CANCLE_WITHDRAW("10603", "出金撤销", "106", "退款", 1),	
	
	/************* 现金 支出   *************/
	/** 20101 出金取出 */
	WITHDRAW("20101", "出金取出", "201", "提现", -1),
	/** 20201 用户结算亏损 */
	USER_LOSS("20201", "用户结算亏损", "202", "结亏", -1),	
	/** 20301  人工取出 */
	MANUALOUT("20301", "人工取出", "203", "手动取出", -1),
	/** 20401 保证金冻结 */
	FREEZE_HOLDFUND("20401", "保证金冻结", "204", "冻结", -1),
	/** 20403 递延金冻结 */
	FREEZE_DEFERFUND("20402", "递延金冻结", "204", "冻结", -1),
	
	/** 20501 平台抽成手续费扣除 */
	DEDUCT_PLATFORM_FEE("20501", "手续费扣除", "205", "支付", -1),
	/** 20503 利息扣除 */
	DEDUCT_INTEREST("20502", "递延利息扣除", "205", "支付", -1),
	/** 20503 兑换积分 */
	EXCHANGE_SCORE("20503", "兑换积分", "205", "支付", -1),
	/** 207回收*/
	GIFT_RECLY("20701", "自动取出", "207", "回收", -1),
	
	/************* 抽成手续费  *************/
	/** 10701 券商抽成手续费存入 */
	INCOME_INVESTOR_FEE("10701", "券商抽成手续费存入", "107", "抽成手续费存入", 1),
	/** 20701 券商抽成手续费退回 */
	OUTLAY_INVESTOR_FEE("20601", "平台手续费", "206", "抽成", -1),	
	
	/**************暂未使用 或 功能未支持************/
	/** 10502 止盈金解冻 */
	UNFREEZE_STOPPROFIT("10502", "止盈金解冻", "105", "解冻", 1),
	/** 20402 止盈金冻结 */
	FREEZE_STOPPROFIT("20402", "止盈金冻结", "204", "冻结", -1),
	
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
	 * @date 2016年12月6日 上午11:47:26
	 * @param code
	 * @param name
	 * @param firstLevelCode
	 * @param firstLevelname
	 * @param inout
	 */
	FundCashOptCodeEnum(String code, String name,
			String firstLevelCode, String firstLevelname, Integer inout) {
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
