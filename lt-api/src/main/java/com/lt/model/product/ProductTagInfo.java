package com.lt.model.product;

import java.util.Date;

/**
 * 标签实体
 * @author jingwb
 *
 */
public class ProductTagInfo {
	private Integer id;
	private String name;//标签名
	private Date createTime;//
	private Integer createUser;//
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

}
