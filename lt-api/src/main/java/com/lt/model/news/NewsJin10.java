package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 描述:金10数据实体
 * @author XieZhibing
 * @date 2017年2月3日 下午3:36:04
 * @version <b>1.0.0</b>
 */
public class NewsJin10 implements Serializable{

	private Integer id;

	private String content;

	private Integer status;// 0待审核，1上线，2下线

	private Date createDate;

	private Date modifyDate;

	private Integer modifyStaffId;

	private String modifyStaffName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getModifyStaffId() {
		return modifyStaffId;
	}

	public void setModifyStaffId(Integer modifyStaffId) {
		this.modifyStaffId = modifyStaffId;
	}

	public String getModifyStaffName() {
		return modifyStaffName;
	}

	public void setModifyStaffName(String modifyStaffName) {
		this.modifyStaffName = modifyStaffName;
	}

}
