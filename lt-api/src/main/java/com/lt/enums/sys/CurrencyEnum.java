package com.lt.enums.sys;

/**   
* 项目名称：lt-api   
* 类名称：CurrencyEnum   
* 类描述： 币种枚举类  
* 创建人：yuanxin   
* 创建时间：2017年3月28日 上午9:16:16      
*/
public enum CurrencyEnum {
	
	/** 默认目标币种*/
	DEFAULT_CURRENCY("CNY","人民币");

	private String currencyCode;
	
	private String currencyName;
	
	CurrencyEnum(String currencyCode,String currencyName){
		this.currencyCode = currencyCode;
		this.currencyName = currencyName;
	}
	
	CurrencyEnum(){
		
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return the currencyName
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/**
	 * @param currencyName the currencyName to set
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
}
