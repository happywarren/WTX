package com.lt.manager.bean.log;

public enum LtManagerLoggerType {

	ADD(0, "新增"),
	DELETE(1, "删除"),
	UPDATE(2, "修改"),
	SELECT(3, "查询"),
	LOGIN(4, "登录"),
	UNLOGIN(5, "退出"),
	CANCELLED(7,"注销");
	
	
	

	private int code;

	private String name;

	private LtManagerLoggerType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.name() + this.getCode();
	}
}
