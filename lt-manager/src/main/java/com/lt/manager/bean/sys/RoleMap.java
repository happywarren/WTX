package com.lt.manager.bean.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * 描述:用户角色关联表
 *
 * @author  郭达望
 * @created 2015年6月2日 上午10:48:18
 * @since   v1.0.0
 */
public class RoleMap implements Serializable{
	
	/**
	 * Description: 
	 */
	private static final long serialVersionUID = 9172467814369918895L;
	private Integer id;
	/**
	 * 角色id
	 */
	private Integer roleId;
	/**
	 * 员工id
	 */
	private Integer staffId;
	private Date createDate;
	
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getStaffId() {
		return staffId;
	}
	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public RoleMap(Integer roleId, Integer staffId, Date createDate) {
		super();
		this.roleId = roleId;
		this.staffId = staffId;
		this.createDate = createDate;
	}
	public RoleMap() {
		super();
	}

}
