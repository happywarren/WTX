package com.lt.util.error;

public class ResponseCode {

	private String code = "200";
	
	private String msg = "操作成功";
	/**
	 * debug
	 * info:普通日誌
	 * error:錯誤日誌
	 * warn:需要發短信的日誌
	 */
	private String level = "info";
	
	/**
	 * 默認為0,1:發送短信；2：發送郵件
	 */
	private int type = 0;

	
	private String data;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ResponseCode() {
	}

	public ResponseCode(String code, String msg, String level, int type) {
		this.code = code;
		this.msg = msg;
		this.level = level;
		this.type = type;
	}
	
	
	public ResponseCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
		this.level = "error";
	}
	
	public void sendMsg(ResponseCode ecode){
		switch (ecode.getType()) {
		case 1:
			//發送短信
			break;
		case 2:
			//發送郵件
			break;
		default:
			break;
		}
	}
	
}
