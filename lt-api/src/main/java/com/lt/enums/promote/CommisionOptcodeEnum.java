package com.lt.enums.promote;


import java.io.Serializable;

/**
 * TODO 现金业务编码
 * @author XieZhibing
 * @date 2016年12月6日 上午11:11:21
 * @version <b>1.0.0</b>
 */
public enum CommisionOptcodeEnum implements Serializable{
	
	MANUALIN("101","内部操作","10101","内部存入","1010101","人工内部存入",1),
	MANUALOUT("101","内部操作","10102","内部取出","1010201","人工内部取出",-1),
	BALANCE("201","结算","20101","佣金结算","2010101","佣金结算",1),
	NOPASS("301","退款","30101","佣金退回","3010101","提现拒绝",1),
	APPLY("401","提现","40101","佣金提现","4010101","申请佣金提现",-1),
	
	;
	
	/** 一级业务编码 */
	private String firstOptcode;
	/** 一级业务编码描述 */
	private String firstOptname;
	/** 二级业务编码 */
	private String secondOptcode;
	/** 二级业务编码描述 */
	private String secondOptname;
	/** 三级业务编码 */
	private String thirdOptcode;
	/** 三级业务编码描述 */
	private String thirdOptname;
	/** 资金进出方向: -1 支出;  1 收入 */
	private Integer inout;
	
	private CommisionOptcodeEnum(String firstOptcode,String firstOptname,String secondOptcode,
			String secondOptname,String thirdOptcode,String thirdOptname,Integer inout){
		this.firstOptcode = firstOptcode;
		this.firstOptname = firstOptname;
		this.secondOptcode = secondOptcode;
		this.secondOptname = secondOptname;
		this.thirdOptcode = thirdOptcode;
		this.thirdOptname = thirdOptname;
		this.inout = inout;
	}
	
	
	public String getFirstOptcode() {
		return firstOptcode;
	}
	public void setFirstOptcode(String firstOptcode) {
		this.firstOptcode = firstOptcode;
	}
	public String getFirstOptname() {
		return firstOptname;
	}
	public void setFirstOptname(String firstOptname) {
		this.firstOptname = firstOptname;
	}
	public String getSecondOptcode() {
		return secondOptcode;
	}
	public void setSecondOptcode(String secondOptcode) {
		this.secondOptcode = secondOptcode;
	}
	public String getSecondOptname() {
		return secondOptname;
	}
	public void setSecondOptname(String secondOptname) {
		this.secondOptname = secondOptname;
	}
	public String getThirdOptcode() {
		return thirdOptcode;
	}
	public void setThirdOptcode(String thirdOptcode) {
		this.thirdOptcode = thirdOptcode;
	}
	public String getThirdOptname() {
		return thirdOptname;
	}
	public void setThirdOptname(String thirdOptname) {
		this.thirdOptname = thirdOptname;
	}
	public Integer getInout() {
		return inout;
	}
	public void setInout(Integer inout) {
		this.inout = inout;
	}
}
