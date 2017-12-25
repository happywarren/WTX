package com.lt.business.core.vo;

import java.io.Serializable;

public class PromoteVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3606558636319649124L;
	private String userId;
	private String nickName;
	private String createDate;
	private Integer handCount;
	private Integer firstHandCount;
	private Double commisionBalance;
	private Integer firstRegisterCount;
	private Integer firstTraderCount;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Integer getHandCount() {
		return handCount;
	}
	public void setHandCount(Integer handCount) {
		this.handCount = handCount;
	}
	public Integer getFirstHandCount() {
		return firstHandCount;
	}
	public void setFirstHandCount(Integer firstHandCount) {
		this.firstHandCount = firstHandCount;
	}
	public Double getCommisionBalance() {
		return commisionBalance;
	}
	public void setCommisionBalance(Double commisionBalance) {
		this.commisionBalance = commisionBalance;
	}
	public Integer getFirstRegisterCount() {
		return firstRegisterCount;
	}
	public void setFirstRegisterCount(Integer firstRegisterCount) {
		this.firstRegisterCount = firstRegisterCount;
	}
	public Integer getFirstTraderCount() {
		return firstTraderCount;
	}
	public void setFirstTraderCount(Integer firstTraderCount) {
		this.firstTraderCount = firstTraderCount;
	}
	
}
