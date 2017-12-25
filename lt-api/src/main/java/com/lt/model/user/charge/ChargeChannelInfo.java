package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：ChargeChannelInfo   
* 类描述： 渠道基本信息 
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午3:23:56      
*/
public class ChargeChannelInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 主键id*/
	private Integer id;
	/** 渠道id*/
	private String channelId;
	/** 优先级*/
	private Integer priority;
	/** 是否默认开通 0 否 1 是*/
	private Integer isDefault;
	/** 是否开启该渠道 0 否 1是*/
	private Integer isStart;
	/** 备注*/
	private String remark;
	/** 通道名称*/
	private String channelName;
	/** 创建时间*/
	private Date createTime;
	/** 渠道总充值金额（汇总数据）*/
	private Double totalAmount;
	/**单日限制次数**/
	private Integer dailyLimitCount;
	/** 权重*/
	private Integer weight;
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
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	/**
	 * @return the isDefault
	 */
	public Integer getIsDefault() {
		return isDefault;
	}
	/**
	 * @param isDefault the isDefault to set
	 */
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * @return the isStart
	 */
	public Integer getIsStart() {
		return isStart;
	}
	/**
	 * @param isStart the isStart to set
	 */
	public void setIsStart(Integer isStart) {
		this.isStart = isStart;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	/**
	 * @return the totalAmount
	 */
	public Double getTotalAmount() {
		return totalAmount;
	}
	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getDailyLimitCount() {
		return dailyLimitCount;
	}
	public void setDailyLimitCount(Integer dailyLimitCount) {
		this.dailyLimitCount = dailyLimitCount;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
