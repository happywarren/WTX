package com.lt.manager.bean.user;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 *
 * 描述: 用户列表页面返回对象
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class UserTagMapper{
	/**
	 *  ID 
	 */
	private Integer id;
	/**
	 *  标签编码
	 */
	private Integer tagId;
	/**
	 *  标签名称
	 */
	private String userId;
	/**
	 *  标签名称
	 */
	private String name;
	/**
	 *  创建日期
	 */
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
 }


