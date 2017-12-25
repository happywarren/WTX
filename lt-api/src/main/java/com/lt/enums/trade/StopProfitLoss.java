package com.lt.enums.trade;

/**   
* 项目名称：lt-api   
* 类名称：StopProfitLoss   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年12月16日 下午1:51:13      
*/
public enum StopProfitLoss {

	
	/** 1: 止盈 */
	STOP_PROFIT(1, "止盈"), 
	/** 0: 止损 */
	STOP_LOSS(2, "止损");

	/** 值 */
	private Integer value;
	/** 描述 */
	private String name;

	/**
	 * 
	 * 构造
	 * 
	 * @author XieZhibing
	 * @date 2016年12月10日 上午11:20:56
	 * @param value
	 * @param name
	 */
	StopProfitLoss(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 获取 值
	 * 
	 * @return value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 设置 值
	 * 
	 * @param value 值
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * 获取 描述
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 描述
	 * 
	 * @param name 描述
	 */
	public void setName(String name) {
		this.name = name;
	}


}
