package com.lt.enums.settle;


import java.io.Serializable;

/**
 * 
* 项目名称：lt-api   
* 类名称：CommisionOptcodeEnum   
* 类描述：结算枚举类   
* 创建人：yuanxin   
* 创建时间：2017年3月15日 下午2:07:01
 */
public enum SettleEnum implements Serializable{
	
	FEE_SETTLE_TYPE(0,"tradeFee","交易手续费结算");
	
	/** 功能编码 */
	private int funcCode;
	/** 功能方法名 */
	private String funcValue;
	
	private String funcName;
	
	SettleEnum(int funcCode,String funcValue,String funcName){
		this.funcCode = funcCode;
		this.funcValue = funcValue;
		this.funcName = funcName;
	}

	/**
	 * @return the funcCode
	 */
	public int getFuncCode() {
		return funcCode;
	}

	/**
	 * @param funcCode the funcCode to set
	 */
	public void setFuncCode(int funcCode) {
		this.funcCode = funcCode;
	}

	/**
	 * @return the funcName
	 */
	public String getFuncName() {
		return funcName;
	}

	/**
	 * @param funcName the funcName to set
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * @return the funcValue
	 */
	public String getFuncValue() {
		return funcValue;
	}

	/**
	 * @param funcValue the funcValue to set
	 */
	public void setFuncValue(String funcValue) {
		this.funcValue = funcValue;
	}
	
}
