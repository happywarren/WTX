package com.lt.controller.promote.bean;

import java.util.Date;

/**
 * 推广员实体
 * @author jingwb
 *
 */
public class PromoterOld{

	/**用户id*/
	private String user_id;
	/**级别id*/
	private Integer level_id;
	/**创建时间*/
	private Date create_time;
	/**修改时间*/
	private Date modify_time;

	private Integer flag;
	private Integer modify_user_id;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getModify_user_id() {
		return modify_user_id;
	}
	public void setModify_user_id(Integer modify_user_id) {
		this.modify_user_id = modify_user_id;
	}
	
	
}
