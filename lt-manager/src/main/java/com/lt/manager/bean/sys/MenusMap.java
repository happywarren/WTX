package com.lt.manager.bean.sys;

import java.io.Serializable;

/**
 * 
 *
 * 描述:角色菜单关联表 
 *
 * @author  郭达望
 * @created 2015年6月2日 上午10:46:46
 * @since   v1.0.0
 */
public class MenusMap implements Serializable{

	/**
	 * Description: 
	 */
	private static final long serialVersionUID = 8653415534718809536L;
	private Integer id;//INT(5) NOT NULL AUTO_INCREMENT,
	private Integer roleId;//INT(5) NOT NULL,
	private Integer menuId;//INT(5) NOT NULL,
	private String createTime;//TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
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
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public MenusMap(Integer roleId, Integer menuId, String createTime) {
		super();
		this.roleId = roleId;
		this.menuId = menuId;
		this.createTime = createTime;
	}
	public MenusMap() {
		super();
	}
	
}
