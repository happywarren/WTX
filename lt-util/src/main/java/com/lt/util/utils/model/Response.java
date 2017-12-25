package com.lt.util.utils.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;


/**
 * 
 * 说明：消息体
 *
 */
public class Response implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	/* 消息类型 */
	public static final int MSG_TYPE_NORMAL = 0;         // 普通类型

	public String code;
	public String msg = "";
	public int msgType = MSG_TYPE_NORMAL;
	public String errparam = "";
	public Object data;
	public String ip;
	public String port;
	


	public Response(String code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Response(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = null;
	}
	
	public Response(String code, String msg,String ip,String port,Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.ip = ip;
		this.port = port;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
	
	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}


	public String getErrparam() {
		return errparam;
	}

	public void setErrparam(String errparam) {
		this.errparam = errparam;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月17日 下午1:59:42
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("code=").append(code).append(", msg=").append(msg);
		return builder.toString();
	}


}
