package com.lt.manager.bean.sys;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 
 * 说明：功能（权限）列表详情
 *
 */
public class Auth implements Serializable{
	/* ID */
	private Integer id;
	/* 父级ID */
	private Integer pid;
	/* 权限名称 */
	private String name;
	/* 权限功能地址 */
	private String url;
	/* 权限功能窗口ID */
	private String target;
	/* 功能类型 0：菜单展示 1：菜单不展示 */
	private Integer type;
	/* 权限描述 */
	private String desc;
	/* 菜单id*/
	private Integer menusId;
	/**
	 * 操作类型 （操作，查询）
	 */
	private Integer operaType;
	
	private Integer createUserId;
	private Integer modifyUserId;
	private Date createDate;
	private Date modifyDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
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

	public Integer getMenusId() {
		return menusId;
	}

	public void setMenusId(Integer menusId) {
		this.menusId = menusId;
	}

	public Integer getOperaType() {
		return operaType;
	}

	public void setOperaType(Integer operaType) {
		this.operaType = operaType;
	}

	public Auth(Integer pid, String name, String url,
			String target, Integer type, String desc, Integer menusId,
			Integer operaType, Integer createUserId, Integer modifyUserId
			) {
		super();
		this.pid = pid;
		this.name = name;
		this.url = url;
		this.target = target;
		this.type = type;
		this.desc = desc;
		this.menusId = menusId;
		this.operaType = operaType;
		this.createUserId = createUserId;
		this.modifyUserId = modifyUserId;
		this.createDate = new Date();
	}

	public Auth() {
		super();
	}
	
}
