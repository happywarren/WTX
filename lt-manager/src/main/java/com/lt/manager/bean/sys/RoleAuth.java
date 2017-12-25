package com.lt.manager.bean.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * 描述:角色权限关联表  
 *
 * @author  郭达望
 * @created 2015年6月2日 上午10:59:13
 * @since   v1.0.0
 */
public class RoleAuth implements Serializable{
	
	
	/**
	 * Description: 
	 */
	private static final long serialVersionUID = 196948021306620366L;
	private Integer id;
	/**
	 * 角色id
	 */
	private Integer roleId;
	/**
	 * 权限id
	 */
	private Integer authId;
	private Date createDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
	public RoleAuth() {
		super();
	}
	public RoleAuth(Integer roleId, Integer authId, Date createDate) {
		super();
		this.roleId = roleId;
		this.authId = authId;
		this.createDate = createDate;
	}
	
}
