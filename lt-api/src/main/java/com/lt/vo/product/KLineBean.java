package com.lt.vo.product;

import java.io.Serializable;

/**
 * K线实体 
 * @author guodw
 *
 */
public class KLineBean implements Serializable{

	/**
	 * 商品名称
	 */
	private String productName ;
	
	/**
	 * 时间
	 */
	private String timeStamp;
	
	/**
	 * 开盘价
	 */
	private String openPrice;

	/**
	 * 收盘价
	 */
	private String closePrice;
	
	
	/**
	 * 最高价
	 */
	private String highPrice;
	
	/**
	 * 最低价
	 */
	private String lowPrice;
	
	/**
	 * 与上一条数据相减的买卖量 
	 */
	private Integer volume;
	/**
	 * 创建时的买卖量
	 */
	private Integer totalQty;
	
	/**
	 * 类型 1分钟K 5分钟K 15分钟K 30分钟K 60分钟K
	 */
	private Integer type;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}

	public String getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}

	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public KLineBean(String productName, String timeStamp, String openPrice,
			String closePrice, String highPrice, String lowPrice,
			Integer volume, Integer type) {
		this.productName = productName;
		this.timeStamp = timeStamp;
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.volume = volume;
		this.type = type;
	}

	public KLineBean() {
	}
	
}
