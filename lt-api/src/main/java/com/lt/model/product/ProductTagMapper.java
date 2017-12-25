package com.lt.model.product;

/**
 * 商品与标签中间表
 * @author jingwb
 *
 */
public class ProductTagMapper {
	private Integer id;
	private Integer productId;//商品主键id
	private Integer tagId;//标签主键id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	
}
