package com.lt.vo.user;

import java.io.Serializable;


/**
 * 全部商品实体
 * @author guodw
 *
 */
public class UserProductSelectVo_old implements Serializable{

	private Integer id;
	private String productCode;//商品代码如CL1601
	private String shortCode;//品种代码如CL
	private String productName;//商品名
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
