package com.lt.model.promote;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广级别实体
 * @author jingwb
 *
 */
public class PromoterLevel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -450780717764983107L;
	/***/
	private Integer id;
	/**等级*/
	private Integer level;
	/***/
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
