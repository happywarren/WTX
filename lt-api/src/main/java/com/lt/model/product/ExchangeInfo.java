package com.lt.model.product;

import java.util.Date;

/**
 * 交易所实体
 * @author jingwb
 *
 */
public class ExchangeInfo {
	/**
	 * id 主键
	 */
	private Integer id;
	/**
	 * 代码
	 */
	private String code;
	/**
	 * 市场名称
	 */
	private String name;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 0：内盘 1:外盘
	 */
	private Integer plate;
	/**
	 * 获取主键id
	 * @return Integer
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置主键id
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取市场编码
	 * @return String
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置市场编码
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取市场名称
	 * @return String 
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置市场名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取创建时间
	 * @return java.util.Date
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置创建时间
	 * @param createDate
	 */
	public void setCreate_date(Date createDate) {
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
	public Integer getPlate() {
		return plate;
	}
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
