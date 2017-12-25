package com.lt.vo.trade;

import java.io.Serializable;

/**
 * 浮动盈亏使用的订单返回对象
 * 
 * @author guodw
 *
 */
public class FloatAmtOrderVo implements Serializable{

	private String userId;

	private String orderId;
	/**
	 * 买入均价
	 */
	private Double buyAvgPrice;

	/**
	 * '交易方向: 1 买多; 2 买空 ',
	 */
	private Integer tradeDirection;

	/**
	 * 商品code
	 */
	private String productCode;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	
	public Double getBuyAvgPrice() {
		return buyAvgPrice;
	}

	public void setBuyAvgPrice(Double buyAvgPrice) {
		this.buyAvgPrice = buyAvgPrice;
	}

	public Integer getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(Integer tradeDirection) {
		this.tradeDirection = tradeDirection;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
