/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.trade
 * FILE    NAME: EntrustStatusEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.news;

/**
 * TODO 委托单状态
 * @author XieZhibing
 * @date 2016年12月10日 下午2:45:53
 * @version <b>1.0.0</b>
 */
public enum NewsArticleEnum {
	/**------------新闻模块统一标记---------**/
	NEWS_COMMON_TRUE(1,"是"),
	NEWS_COMMON_FALSE(0,"否"),
	/**------------新闻图片模式-----------**/
	NEWS_PICFLAG_NONE(0,"无图"),
	NEWS_PICFLAG_SMALL(1,"小图"),
	NEWS_PICFLAG_BIG(2,"大图"),
	/**-----------新闻状态---------------**/
	NEWS_STATUS_INIT(0,"待审核"),
	NEWS_STATUS_OFFLINE(3,"下线"),
	NEWS_STATUS_REJECT(1,"审核拒绝"),
	NEWS_STATUS_SHOW(2,"审核通过"),
	
	/**------------新闻栏目--------------**/
	NEWS_SECTION_NEWS(55,"新闻"),
	NEWS_SECTION_STAGY(57,"策略");
	
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
	NewsArticleEnum(Integer value, String name) {
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
