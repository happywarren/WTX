package com.lt.controller.promote.bean;

import java.util.Date;

/**
 * 用户被推广信息详细
 *
 * @author  jiupeng
 * @created 2015年6月4日 下午5:19:47
 * @since   v1.0.0
 */
public class UserPromoteInfo {

	private Integer id;

	private Integer userId; //用户ID

	private Integer promoteId; //推广员ID

	private Date createDate;

	private Date modifyDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPromoteId() {
		return promoteId;
	}

	public void setPromoteId(Integer promoteId) {
		this.promoteId = promoteId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
