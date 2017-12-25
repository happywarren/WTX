package com.lt.manager.bean.enums.product;

/**
 * 商品相关枚举类
 * @author jingwb
 *
 */
public enum ProductEnum {
	MARKET_STATUS_CLOSE(0,"闭市"),
	MARKET_STATUS_ONLY_OPEN(1,"仅开仓"),
	MARKET_STATUS_ONLY_SALE(2,"仅平仓"),
	MARKET_STATUS_OPEN(3,"开市"),
	MARKET_STATUS_REST(4,"休市"),
	
	PRODUCT_STATUS_DOWN(0,"下架"),
	PRODUCT_STATUS_UP(1,"上架"),
	PRODUCT_STATUS_PRESELL(2,"预售"),
	PRODUCT_STATUS_OVERDUE(3,"过期"),
	
	
	
	
	//此分号不要删除
	;
	private Integer value;
	
	private String name;
	
	private ProductEnum(Integer value,String name){
		this.value=value;
		this.name=name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
