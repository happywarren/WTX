package com.lt.manager.bean.sys;

import java.util.Date;

/**
 * 
 * 说明：权限列表对应信息
 * 
 * @author zheng_zhi_rui@163com
 * @date 2015年4月7日
 *
 */
public class AuthMap {
	private Integer id;
	private Integer authId;
	private Integer staffId;
	private Date createDate;

	public AuthMap(Integer authId, Integer staffId) {
		super();
		this.authId = authId;
		this.staffId = staffId;
		this.createDate = new Date();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAuthId() {
		return authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

}
