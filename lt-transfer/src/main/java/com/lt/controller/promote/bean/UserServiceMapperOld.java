package com.lt.controller.promote.bean;

import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserServiceMapper   
* 类描述：   用户关联服务对象
* 创建人：yuanxin   
* 创建时间：2016年12月7日 下午4:50:51      
*/
public class UserServiceMapperOld {
	
	/** 用户id*/
	private String user_id;
	/** 服务编号*/
	private String service_code;
	/** 开启时间*/
	private Date open_time;
	/** 运行状态 0：正常运行中，1：关闭*/
	private String status;
	/** 创建时间*/
	private Date create_date;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getService_code() {
		return service_code;
	}
	public void setService_code(String service_code) {
		this.service_code = service_code;
	}
	public Date getOpen_time() {
		return open_time;
	}
	public void setOpen_time(Date open_time) {
		this.open_time = open_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	
}
