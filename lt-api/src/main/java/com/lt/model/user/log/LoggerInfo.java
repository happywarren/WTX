package com.lt.model.user.log;

public class LoggerInfo {
	private String recordVersion;//  软件版本号
	private String recordIP   ;
	private String recordLoginMode ;// 登录方式
	private String recordImei;//
	private String recordDevice  ;//        设备型号
	private String recordCarrierOperator ;// 运行商
	private String recordAccessMode ;//    访问方式
	public String getRecordVersion() {
		return recordVersion;
	}
	public void setRecordVersion(String recordVersion) {
		this.recordVersion = recordVersion;
	}
	public String getRecordIP() {
		return recordIP;
	}
	public void setRecordIP(String recordIP) {
		this.recordIP = recordIP;
	}
	public String getRecordLoginMode() {
		return recordLoginMode;
	}
	public void setRecordLoginMode(String recordLoginMode) {
		this.recordLoginMode = recordLoginMode;
	}
	public String getRecordImei() {
		return recordImei;
	}
	public void setRecordImei(String recordImei) {
		this.recordImei = recordImei;
	}
	public String getRecordDevice() {
		return recordDevice;
	}
	public void setRecordDevice(String recordDevice) {
		this.recordDevice = recordDevice;
	}
	public String getRecordCarrierOperator() {
		return recordCarrierOperator;
	}
	public void setRecordCarrierOperator(String recordCarrierOperator) {
		this.recordCarrierOperator = recordCarrierOperator;
	}
	public String getRecordAccessMode() {
		return recordAccessMode;
	}
	public void setRecordAccessMode(String recordAccessMode) {
		this.recordAccessMode = recordAccessMode;
	}
	/*public LoggerInfo(String recordVersion, String recordIP,
			String recordLoginMode, String recordImei, String recordDevice,
			String recordCarrierOperator, String recordAccessMode) {
		this.recordVersion = recordVersion;
		this.recordIP = recordIP;
		this.recordLoginMode = recordLoginMode;
		this.recordImei = recordImei;
		this.recordDevice = recordDevice;
		this.recordCarrierOperator = recordCarrierOperator;
		this.recordAccessMode = recordAccessMode;
	}
	*/
}
