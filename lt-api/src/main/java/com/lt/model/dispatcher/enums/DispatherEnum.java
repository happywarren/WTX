package com.lt.model.dispatcher.enums;

/**
 * 中间件枚举类
 * @author guodw
 *
 */
public enum DispatherEnum {

	
	SINGLE_QUOTA(1, "单个行情"),
	NOSEND_QUOTA(2, "不发送任何数据"),
	;
	/**
	 * 基本参数，只在获取所有行情时使用
	 */
	public static final String ALL ="ALL";
	public static final String NOSEND ="NOSEND";
	
	private int value;
	private String name;

	private DispatherEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static DispatherEnum getEnumByValue(int value) {
		for (DispatherEnum type : DispatherEnum.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}

}
