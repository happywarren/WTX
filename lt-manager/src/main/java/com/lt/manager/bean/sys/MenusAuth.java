package com.lt.manager.bean.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:菜单权限关联表
 */
public class MenusAuth implements Serializable{
	
	/**
	 * Description: 
	 */
	private static final long serialVersionUID = 2579112660427007301L;
	
	private Integer id;
	private Integer menusId;
	private Integer authId;
	private Date createDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMenusId() {
		return menusId;
	}
	public void setMenusId(Integer menusId) {
		this.menusId = menusId;
	}
	public Integer getAuthId() {
		return authId;
	}
	public void setAuthId(Integer authId) {
		this.authId = authId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
