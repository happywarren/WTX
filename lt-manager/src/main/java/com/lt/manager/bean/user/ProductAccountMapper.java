package com.lt.manager.bean.user;

import com.lt.manager.bean.BaseBean;
import java.util.Date;
/**
 * 
 * <br>
 * <b>功能：</b>ProductAccountMapperEntity<br>
 */
public class ProductAccountMapper extends BaseBean {
		private java.lang.Integer productId;//   产品ID	private java.lang.String securityCode;//   证券账号	private String userId;//   用户ID
	private java.lang.Integer plateType;//   内外盘类型
	private java.lang.String productName;//   产品代码
	private java.lang.Integer direct;//   内外盘类型
	private java.lang.Integer isUse;//是否选中该产品
	private Integer productType;// 商品类型
		public java.lang.Integer getProductId() {	    return this.productId;	}	public void setProductId(java.lang.Integer productId) {	    this.productId=productId;	}	public java.lang.String getSecurityCode() {	    return this.securityCode;	}	public void setSecurityCode(java.lang.String securityCode) {	    this.securityCode=securityCode;	}	public String getUserId() {	    return this.userId;	}	public void setUserId(String userId) {	    this.userId=userId;	}
	public java.lang.Integer getPlateType() {
		return plateType;
	}
	public void setPlateType(java.lang.Integer plateType) {
		this.plateType = plateType;
	}
	public java.lang.Integer getDirect() {
		return direct;
	}
	public void setDirect(java.lang.Integer direct) {
		this.direct = direct;
	}
	public java.lang.String getProductName() {
		return productName;
	}
	public void setProductName(java.lang.String productName) {
		this.productName = productName;
	}
	public java.lang.Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(java.lang.Integer isUse) {
		this.isUse = isUse;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	
}

