package com.lt.enums.advertise;

/**   
* 项目名称：lt-api   
* 类名称：AdvertiseCommentTypeEnum   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月4日 上午9:17:17      
*/
public enum AdvertiseCommentTypeEnum {
	
	NO_COMMENT(0,"不存在内容"),
	/** 1: 首页弹窗 */
	H5_URL(1, "H5链接"),
	/** 2: 活动广告 */
	CONTENT(2, "文本内容");
	
	
	/** 值 */
	private int value;
	/** 描述 */
	private String name;
	
	AdvertiseCommentTypeEnum(int value, String name){
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
