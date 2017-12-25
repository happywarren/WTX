package com.lt.model.logs;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志
 * 谁在什么时间操作了什么方法，内容是什么，哪个IP访问的 ,哪种设备 ，设备号是什么
 * @author guodw
 *
 */
public class LogInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1362269101519958900L;
	// id  
	private  Integer id;
	// createDate 时间
	private Date createDate;
	// userId 操作人
	private String userId;
	// 模块 方法
	private String cmd;
	//方法
	private String func;
	// content 内容
	private String msg;
	// 具体参数数据
	private String data;
	// ip 访问IP
	private String ip;
	// deviceType 设备类型
	private String deviceType;
	// deviceNum 设备号
	private String deviceNum;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public LogInfo(Integer id, String userId, String cmd,
			String func, String msg,String data, String ip, String deviceType,
			String deviceNum) {
		super();
		this.id = id;
		this.createDate = new Date();
		this.userId = userId;
		this.cmd = cmd;
		this.func = func;
		this.data = data;
		this.msg = msg;
		this.ip = ip;
		this.deviceType = deviceType;
		this.deviceNum = deviceNum;
	}
	public LogInfo() {
		
	}
	
}
