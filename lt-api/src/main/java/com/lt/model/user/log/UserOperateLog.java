package com.lt.model.user.log;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

/**   
* 项目名称：lt-api   
* 类名称：UserOperateLog   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年12月1日 上午9:16:12      
*/
public class UserOperateLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserOperateLog(){
		
	}
	
	private Integer id;
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 操作类型，参考LoggerType
	 */
	private Integer operateType;

	/**
	 * 操作类型说明，参考LoggerType
	 */
	private String operateName;

	/**
	 * 操作是否成功：true:成功，false：失败
	 */
	private Boolean isSuccessed;

	/**
	 * 日志内容
	 */
	private String content;

	/**
	 * 渠道名称
	 */
	private String channelName;

	/**
	 * 详细类型：第三方登陆时 QQ 微信 支付宝等 ，  手机登陆时  手机号 
	 */
	private String behaviorType;

	/**
	 * 客户端版本，包版本号
	 */
	private String clientVersion;

	/**
	 * ip
	 */
	private String ip;


	/**
	 * 系统版本，例：4.0
	 */
	private String deviceVersion;

	/**
	 * 设备串号
	 */
	private String deviceImei;

	/**
	 * 设备型号,例GT-9300
	 */
	private String deviceModel;
	private String recordVersion;//  软件版本号
	private String recordIP   ;
	private String recordLoginMode ;// 登录方式
	private String recordImei;//
	private String recordDevice  ;//        设备型号
	private String recordCarrierOperator ;// 运行商
	private String recordAccessMode ;//    访问方式
	
	private String time;//在线时长
	
	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}

	@Field
	private Date createTime;


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


	public UserOperateLog( String userId, Integer operateType,
			String operateName, Boolean isSuccessed, String content,
			String channelName, String behaviorType, String clientVersion,
			String ip, String deviceVersion, String deviceImei,
			String deviceModel, String recordVersion, String recordIP,
			String recordLoginMode, String recordImei, String recordDevice,
			String recordCarrierOperator, String recordAccessMode) {
		this.userId = userId;
		this.operateType = operateType;
		this.operateName = operateName;
		this.isSuccessed = isSuccessed;
		this.content = content;
		this.channelName = channelName;
		this.behaviorType = behaviorType;
		this.clientVersion = clientVersion;
		this.ip = ip;
		this.deviceVersion = deviceVersion;
		this.deviceImei = deviceImei;
		this.deviceModel = deviceModel;
		this.recordVersion = recordVersion;
		this.recordIP = recordIP;
		this.recordLoginMode = recordLoginMode;
		this.recordImei = recordImei;
		this.recordDevice = recordDevice;
		this.recordCarrierOperator = recordCarrierOperator;
		this.recordAccessMode = recordAccessMode;
		this.createTime = new Date();
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the operateType
	 */
	public Integer getOperateType() {
		return operateType;
	}

	/**
	 * @param operateType the operateType to set
	 */
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	/**
	 * @return the operateName
	 */
	public String getOperateName() {
		return operateName;
	}

	/**
	 * @param operateName the operateName to set
	 */
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	/**
	 * @return the isSuccessed
	 */
	public Boolean getIsSuccessed() {
		return isSuccessed;
	}

	/**
	 * @param isSuccessed the isSuccessed to set
	 */
	public void setIsSuccessed(Boolean isSuccessed) {
		this.isSuccessed = isSuccessed;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @param channelName the channelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * @return the behaviorType
	 */
	public String getBehaviorType() {
		return behaviorType;
	}

	/**
	 * @param behaviorType the behaviorType to set
	 */
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}

	/**
	 * @return the clientVersion
	 */
	public String getClientVersion() {
		return clientVersion;
	}

	/**
	 * @param clientVersion the clientVersion to set
	 */
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the deviceVersion
	 */
	public String getDeviceVersion() {
		return deviceVersion;
	}

	/**
	 * @param deviceVersion the deviceVersion to set
	 */
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	/**
	 * @return the deviceImei
	 */
	public String getDeviceImei() {
		return deviceImei;
	}

	/**
	 * @param deviceImei the deviceImei to set
	 */
	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}

	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}

	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
	
}
