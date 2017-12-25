package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;
/**
 * 用户渠道交易
 * @date 2017年7月27日
 * @author yubei
 *
 */
public class UserChannelTrans implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**id**/
	private Integer id;
	/**用户id**/
	private String userId;
	/**渠道id**/
	private String channelId;
	/**创建时间**/
	private Date createTime;
	/**创建日期**/
	private Date createDate;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
