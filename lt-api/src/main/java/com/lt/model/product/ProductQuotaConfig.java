package com.lt.model.product;

/**
 * 商品行情配置
 * @author jingwb
 *
 */
public class ProductQuotaConfig{

	private Integer id;
	private String ids;
	private Integer baseline;//行情图基线
	private Integer isDouble;//是否双线0:关闭 1：开启
	private Float intervalFloat;//闪动图波动区间
	private Integer productId;//商品id
	private Integer num;//行情总条数
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Integer getBaseline() {
		return baseline;
	}
	public void setBaseline(Integer baseline) {
		this.baseline = baseline;
	}

	public Integer getIsDouble() {
		return isDouble;
	}
	public void setIsDouble(Integer isDouble) {
		this.isDouble = isDouble;
	}
	public Float getIntervalFloat() {
		return intervalFloat;
	}
	public void setIntervalFloat(Float intervalFloat) {
		this.intervalFloat = intervalFloat;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
}
