package com.lt.model.user.log;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserUpdateInfoLog   
* 类描述：用户信息修改日志   
* 创建人：yuanxin   
* 创建时间：2016年12月5日 上午9:54:27      
*/
public class UserUpdateInfoLog  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 记录id*/
	private Integer id;
	/**  信息所属用户id*/
	private String userId;
	/**  修改类型*/
	private Integer update_type ;
	/**  操作用户*/
	private String modify_id;
	/**  修改内容*/
	private String content;
	/**  操作设备*/
	private String device_model;
	/**  修改ip*/
	private String ip;
	/**  修改时间*/
	private Date update_time;
	
	private String recordCarrierOperator ;// 运行商
	private String recordAccessMode ;//    访问方式
	
	public UserUpdateInfoLog(){
		
	} 

	public UserUpdateInfoLog(String userId, int update_type,
			String modifyId, String deviceModel, String ip,
			String recordCarrierOperator, String recordAccessMode) {
		this.userId = userId;
		this.update_type = update_type;
		this.modify_id = modifyId;
		this.device_model = deviceModel;
		this.ip = ip;
		this.update_time = new Date();
		this.recordCarrierOperator = recordCarrierOperator;
		this.recordAccessMode = recordAccessMode;
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
	 * @return the update_time
	 */
	public Date getUpdate_time() {
		return update_time;
	}

	/**
	 * @param update_time the update_time to set
	 */
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}


	/**
	 * @return the update_type
	 */
	public Integer getUpdate_type() {
		return update_type;
	}

	/**
	 * @param update_type the update_type to set
	 */
	public void setUpdate_type(Integer update_type) {
		this.update_type = update_type;
	}

	/**
	 * @return the modify_id
	 */
	public String getModify_id() {
		return modify_id;
	}

	/**
	 * @param modify_id the modify_id to set
	 */
	public void setModify_id(String modify_id) {
		this.modify_id = modify_id;
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
	 * @return the device_model
	 */
	public String getDevice_model() {
		return device_model;
	}

	/**
	 * @param device_model the device_model to set
	 */
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
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
