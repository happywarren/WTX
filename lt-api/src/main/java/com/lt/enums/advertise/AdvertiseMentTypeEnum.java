package com.lt.enums.advertise;

/**   
* 项目名称：lt-api   
* 类名称：AdvertiseMentType   
* 类描述： 活动广告类型  
* 创建人：yuanxin   
* 创建时间：2017年7月3日 下午8:14:07      
*/
public enum AdvertiseMentTypeEnum {
	
	/** 1: 首页弹窗 */
	HOME_PAGE(1, "首页弹窗"),
	/** 2: 活动广告 */
	ACTIVE_ADVI(2, "活动广告");
	
	
	/** 值 */
	private int value;
	/** 描述 */
	private String name;
	
	AdvertiseMentTypeEnum(int value, String name){
		this.value = value;
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
