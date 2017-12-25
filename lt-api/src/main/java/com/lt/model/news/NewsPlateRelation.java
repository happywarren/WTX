package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 描述:新闻板块关系
 * @author XieZhibing
 * @date 2017年2月3日 下午3:36:32
 * @version <b>1.0.0</b>
 */
public class NewsPlateRelation implements Serializable{
	// 新闻文章ID
	private Integer newsId;
	// 板块ID
	private Integer plateId;
	// 该新闻在该板块中的权重
	private Integer orderWeight;
	// 该新闻在该板块中的状态0无效，1有效，默认1
	private Integer status;
	private Date createDate;
	private Date modifyDate;
	private Integer createStaffId;
	private String createStaffName;
	private Integer modifyStaffId;
	private String modifyStaffName;

	public Integer getNewsId() {
		return newsId;
	}

	public Integer getPlateId() {
		return plateId;
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

	public Date getModifyDate() {
		return modifyDate;
	}

	public Integer getCreateStaffId() {
		return createStaffId;
	}

	public String getCreateStaffName() {
		return createStaffName;
	}

	public Integer getModifyStaffId() {
		return modifyStaffId;
	}

	public String getModifyStaffName() {
		return modifyStaffName;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public void setPlateId(Integer plateId) {
		this.plateId = plateId;
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

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setCreateStaffId(Integer createStaffId) {
		this.createStaffId = createStaffId;
	}

	public void setCreateStaffName(String createStaffName) {
		this.createStaffName = createStaffName;
	}

	public void setModifyStaffId(Integer modifyStaffId) {
		this.modifyStaffId = modifyStaffId;
	}

	public void setModifyStaffName(String modifyStaffName) {
		this.modifyStaffName = modifyStaffName;
	}

}
