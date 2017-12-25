package com.lt.model.promote;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广员实体
 * @author jingwb
 *
 */
public class Promoter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4379903999110046003L;
	/**主键*/
	private Integer id;
	/**用户id*/
	private String userId;
	/**级别id*/
	private Integer levelId;
	/**创建时间*/
	private Date createTime;
	/**修改时间*/
	private Date modifyTime;
	/**推广员佣金*/
	private Double commisionBalance;
	private Integer flag;
	private Integer modifyUserId;
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
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Double getCommisionBalance() {
		return commisionBalance;
	}
	public void setCommisionBalance(Double commisionBalance) {
		this.commisionBalance = commisionBalance;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
}
