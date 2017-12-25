package com.lt.model.promote;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广员与用户关系实体
 * @author jingwb
 *
 */
public class PromoterUserMapper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4919885520257461881L;
	/***/
	private Integer id;
	/**用户id*/
	private String userId;
	/**推广员的用户id*/
	private String promoterId;
	/**推广员与用户关系存在标识：0 破裂 1存在（该字段用于修改推广关系时使用）*/
	private Integer flag;	
	/***/
	private Date createTime;
	/***/
	private Date modifyTime;
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
	public String getPromoterId() {
		return promoterId;
	}
	public void setPromoterId(String promoterId) {
		this.promoterId = promoterId;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
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
	public Integer getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
}
