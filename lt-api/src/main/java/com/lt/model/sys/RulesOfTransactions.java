package com.lt.model.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 交易规则
 * @author guodw
 *
 */
public class RulesOfTransactions implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5307600048780415954L;
	
	private Integer id ;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 品种代码（如：CL）
	 */
	private String shortCode ;
	/**
	 * 商品类型
	 */
	private Integer productTypeId ;
	/**
	 * json1
	 */
	private String basicInfo ;
	/**
	 * json2
	 */
	private String descInfo ;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改时间
	 */
	private Date modifyDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public Integer getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}
	public String getBasicInfo() {
		return basicInfo;
	}
	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}
	public String getDescInfo() {
		return descInfo;
	}
	public void setDescInfo(String descInfo) {
		this.descInfo = descInfo;
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
	
}
