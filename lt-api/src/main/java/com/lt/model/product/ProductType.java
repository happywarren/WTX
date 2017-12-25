package com.lt.model.product;

import java.io.Serializable;
import java.util.Date;

/**
 *商品类型实体
 * @author jingwb
 *
 */
public class ProductType implements Serializable{
	
	private Integer id;
	private String name;
	private Integer sortNum;
	private Date createDate;
	private String remark;
	private String code;
	
	/**
	 * 获取主键
	 * @return Integer
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置主键
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取类型名
	 * @return String
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置类型名
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取排序
	 * @return Integer
	 */

	public Integer getSortNum() {
		return sortNum;
	}
	/**
	 * 设置排序
	 * @param sort
	 */
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	/**
	 * 获取创建时间
	 * @return Date
	 */
	public Date getCreateDate() {
		return createDate;
	}
	
	/**
	 * 设置创建时间
	 * @param createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取备注
	 * @return String
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置备注
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
