package com.lt.manager.bean.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * 描述:角色表
 *
 * @author  郭达望
 * @created 2015年6月2日 上午10:47:57
 * @since   v1.0.0
 */
public class Role implements Serializable{
	
	/**
	 * Description: 
	 */
	private static final long serialVersionUID = -6351877505967772443L;
	private Integer id;
	private String name;
	private String desc;
	private Integer creator;
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public Role(String name, String desc) {
		super();
		this.name = name;
		this.desc = desc;
		this.createTime = new Date();
	}
	public Role(String name, String desc,Integer creator) {
		super();
		this.name = name;
		this.desc = desc;
		this.creator = creator;
	}
	
	public Role(Integer id,String name, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
	public Role() {
		super();
	}
	
}
