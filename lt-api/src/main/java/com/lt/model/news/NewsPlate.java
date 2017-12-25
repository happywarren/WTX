package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 描述:新闻板块
 * @author XieZhibing
 * @date 2017年2月3日 下午3:36:15
 * @version <b>1.0.0</b>
 */
public class NewsPlate implements Serializable{
	private Integer id;
	// 板块名称
	private String name;
	// 权重默认0，数值越大排序越靠前
	private Integer orderWeight;
	// 状态 0无效1有效，默认1
	private Integer status;
	private Date createDate;
	private Integer createStaffId;
	private String createStaffName;


	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
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

	public Integer getOrderWeight() {
		return orderWeight;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setOrderWeight(Integer orderWeight) {
		this.orderWeight = orderWeight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
