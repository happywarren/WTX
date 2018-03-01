package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：BankChargeMapper   
* 类描述：银行关联渠道信息   
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午3:19:19      
*/
public class BankChargeMapper implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键id*/
	private Integer id ;
	/** 银行编号*/
	private String bankCode ;
	/** 渠道id*/
	private String channelId;
	/** 单词入金限额*/
	private Double singleLimit ;
	/** 单日入金限额*/
	private Double dailyLimit;
	/** 创建时间*/
	private Date createDate;
	/** 修改时间*/
	private Date updateDate;
	/**通道名称 */
	private String channelName;
	/**groupid*/
	private String groupId;
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
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the singleLimit
	 */
	public Double getSingleLimit() {
		return singleLimit;
	}
	/**
	 * @param singleLimit the singleLimit to set
	 */
	public void setSingleLimit(Double singleLimit) {
		this.singleLimit = singleLimit;
	}
	/**
	 * @return the dailyLimit
	 */
	public Double getDailyLimit() {
		return dailyLimit;
	}
	/**
	 * @param dailyLimit the dailyLimit to set
	 */
	public void setDailyLimit(Double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public BankChargeMapper() {
		super();
	}
	public BankChargeMapper(String bankCode, String channelId, Double singleLimit, Double dailyLimit) {
		super();
		this.bankCode = bankCode;
		this.channelId = channelId;
		this.singleLimit = singleLimit;
		this.dailyLimit = dailyLimit;
	}
	
}
