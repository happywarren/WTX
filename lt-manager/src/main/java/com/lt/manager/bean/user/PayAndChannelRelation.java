package com.lt.manager.bean.user;

import java.io.Serializable;

public class PayAndChannelRelation  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public PayAndChannelRelation() {
		super();
	}

	public PayAndChannelRelation(String channelId, String payChannelId) {
		super();
		this.channelId = channelId;
		this.payChannelId = payChannelId;
	}

	/**
	 * 注册渠道id
	 */
	private String channelId;

	/**
	 * 支付渠道id
	 */
	private String payChannelId;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPayChannelId() {
		return payChannelId;
	}

	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}
	
	
	
}
