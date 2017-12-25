package com.lt.manager.bean.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:菜单表
 */
public class Menus implements Serializable{
	
	
	/**
	 * Description: 
	 */
	private static final long serialVersionUID = 8840093099079590630L;
	
	private Integer id;//INT(10) NOT NULL AUTO_INCREMENT,
	private Integer pid;//INT(10) NOT NULL DEFAULT '-1' COMMENT '父级ID',
	private String name;//VARCHAR(100) NOT NULL COMMENT '菜单名称',
	private String desc;//VARCHAR(300) NULL DEFAULT NULL COMMENT '描述',
	private String url;
	private Integer createUserId;//INT(10) NOT NULL DEFAULT '0',
	private Integer modifyUserId;//INT(10) NOT NULL DEFAULT '0',
	private Date createDate;//TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	private Date modifyDate;//TIMESTAMP NULL DEFAULT NULL,
	private Integer msort;
	private Integer model;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getMsort() {
		return msort;
	}
	public void setMsort(Integer msort) {
		this.msort = msort;
	}
	public Integer getModel() {
		return model;
	}
	public void setModel(Integer model) {
		this.model = model;
	}
	
}
