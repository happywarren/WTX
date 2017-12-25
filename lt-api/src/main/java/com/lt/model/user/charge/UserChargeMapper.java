package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserChargeMapper   
* 类描述：用户关联渠道表   
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午3:31:05      
*/
public class UserChargeMapper implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键id*/
	private Integer id;
	/** 用户id*/
	private String userId;
	/** 渠道id*/
	private String channelId;
	/** 创建时间*/
	private Date createTime;
	
	public UserChargeMapper(){
		
	}
	
	public UserChargeMapper(String userId,String channelId){
		this.userId = userId ;
		this.channelId = channelId ;
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

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserChargeMapper){
			UserChargeMapper userChargeMapper = (UserChargeMapper) obj;
			if(this.getUserId().equals(userChargeMapper.getUserId())&& this.getChannelId().equals(userChargeMapper.getChannelId())){
				return true;
			}
		}
		return false;
	}
	
}
