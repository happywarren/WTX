package com.lt.model.sys;

import java.io.Serializable;
import java.util.Date;

public class SysThreadLock implements Serializable{

	private static final long serialVersionUID = -4352317176778849421L;
	
	private Integer id;
	//任务名称
	private String name;
	//任务编码
	private String code;
	//修改时间
	private Date modifyDate;
	//执行状态，1 执行中 0未执行
	private Integer status;
	
	public SysThreadLock(String name,String code){
		this.name = name; 
		this.code = code;
	}
	
	
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
