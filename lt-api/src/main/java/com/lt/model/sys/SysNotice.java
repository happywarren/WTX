package com.lt.model.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统公告
 * @author jingwb
 *
 */
public class SysNotice implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3383271202876507958L;
	private Integer id;
	/**标题*/
	private String title;
	/**内容*/
	private String content;
	/**失效类型，1：手动 2：自动*/
	private Integer invalidType;
	/**失效天数(从创建时间到设置的天数失效）*/
	private Integer invalidDay;
	/**创建时间*/
	private Date createDate;
	/**创建人*/
	private String createUserId;
	/**修改时间*/
	private Date modifyDate;
	/**修改人*/
	private String modifyUserId;
	/**状态，1:待生效，2：已生效，3：失效*/
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getInvalidType() {
		return invalidType;
	}
	public void setInvalidType(Integer invalidType) {
		this.invalidType = invalidType;
	}
	public Integer getInvalidDay() {
		return invalidDay;
	}
	public void setInvalidDay(Integer invalidDay) {
		this.invalidDay = invalidDay;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
