package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 描述:新闻栏目
 * @author XieZhibing
 * @date 2017年2月3日 下午3:36:55
 * @version <b>1.0.0</b>
 */
public class NewsSection implements Serializable{
	private Integer id;
	// 栏目名称
	private String name;
	// 父级栏目ID，默认-1
	private Integer pid;
	// 权重默认0，数值越大排序越靠前
	private Integer orderWeight;
	// 状态 0无效1有效，默认1
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 创建人id
	 */
	private Integer createStaffId;
	
	/**
	 * 创建人名称
	 */
	private String createStaffName;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getPid() {
		return pid;
	}

	public Integer getOrderWeight() {
		return orderWeight;
	}

	public Integer getStatus() {
		return status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Integer getCreateStaffId() {
		return createStaffId;
	}

	public String getCreateStaffName() {
		return createStaffName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public void setOrderWeight(Integer orderWeight) {
		this.orderWeight = orderWeight;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateStaffId(Integer createStaffId) {
		this.createStaffId = createStaffId;
	}

	public void setCreateStaffName(String createStaffName) {
		this.createStaffName = createStaffName;
	}

}
