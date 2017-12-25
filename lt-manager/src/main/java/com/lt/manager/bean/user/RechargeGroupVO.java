package com.lt.manager.bean.user;

public class RechargeGroupVO {
	/**
	 * 主键
	 */
	private int id;
	
	/**
	 * 分组id
	 */
	private String groupId;
	
	/**
	 * 描述
	 */
	private String description;

	/**
	 * 请求地址
	 */
	private String reqUrl;
	
	/**
	 * 回调地址
	 */
	private String notifyUrl;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
}
