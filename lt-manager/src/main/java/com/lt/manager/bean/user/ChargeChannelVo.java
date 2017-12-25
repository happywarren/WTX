package com.lt.manager.bean.user;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-manager 
* 类名称：BankVo   
* 类描述：银行信息
* 创建人：yubei   
* 创建时间：2017年6月13日 下午3:19:19      
*/
public class ChargeChannelVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	/**用户id**/
	private String userId;
	/**单日限制次数**/
	private Integer dailyLimitCount;
	/**权重*/
	private Integer weight;
	
	/**商户号**/
	private String mchId;
	
	/**密钥**/
	private String secretKey;
	
	/**充值组id**/
	private String groupId;
	
	/**主键**/
	private Integer id;
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public Integer getIsStart() {
		return isStart;
	}
	public void setIsStart(Integer isStart) {
		this.isStart = isStart;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
