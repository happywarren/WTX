package com.lt.model.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户自选商品关联关系表
 * 
 * @author guodw
 *
 */
public class UserProductSelect implements Serializable{

	private Integer id;

	private String userId;

	private Integer productId;

	private String productShortCode;

	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductShortCode() {
		return productShortCode;
	}

	public void setProductShortCode(String productShortCode) {
		this.productShortCode = productShortCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public UserProductSelect(String userId, Integer productId) {
		super();
		this.userId = userId;
		this.productId = productId;
	}

	public UserProductSelect() {
		super();
	}

}
