package com.lt.controller.promote.bean;

import java.util.Date;

/**
 * 推广员与用户关系实体
 * @author jingwb
 *
 */
public class PromoterUserMapperOld{

	/**用户id*/
	private String user_id;
	/**推广员的用户id*/
	private String promoter_id;
	/**推广员与用户关系存在标识：0 破裂 1存在（该字段用于修改推广关系时使用）*/
	private Integer flag;	
	/***/
	private Date create_time;
	/***/
	private Date modify_time;
	private Integer modify_user_id;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPromoter_id() {
		return promoter_id;
	}
	public void setPromoter_id(String promoter_id) {
		this.promoter_id = promoter_id;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
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
	public Integer getModify_user_id() {
		return modify_user_id;
	}
	public void setModify_user_id(Integer modify_user_id) {
		this.modify_user_id = modify_user_id;
	}
	
	
}
