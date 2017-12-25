package com.lt.vo.trade;

import java.io.Serializable;

/**
 * 单品持仓订单返回实体
 * 
 * @author guodw
 *
 */
public class SinglePositionOrderVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3583195797145911968L;
	/** 最小变动价格 */
	private String jumpPrice;
	/** 最小波动点 */
	private String jumpValue;
	/** 汇率 */
	private String rate;
	/** 订单ID */
	private String orderId;
	/** 买卖方向 */
	private Integer tradeDirection;
	/** 持仓手数 */
	private Integer holdCount;
	/** 买入均价 */
	private String buyAvgPrice;
	
	private Integer tradeType;
	
	private String sysBuyAvgPrice;

	public String getJumpPrice() {
		return jumpPrice;
	}

	public void setJumpPrice(String jumpPrice) {
		this.jumpPrice = jumpPrice;
	}

	public String getJumpValue() {
		return jumpValue;
	}

	public void setJumpValue(String jumpValue) {
		this.jumpValue = jumpValue;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getHoldCount() {
		return holdCount;
	}

	public void setHoldCount(Integer holdCount) {
		this.holdCount = holdCount;
	}

	public Integer getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(Integer tradeDirection) {
		this.tradeDirection = tradeDirection;
	}

	public String getBuyAvgPrice() {
		return buyAvgPrice;
	}

	public void setBuyAvgPrice(String buyAvgPrice) {
		this.buyAvgPrice = buyAvgPrice;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public String getSysBuyAvgPrice() {
		return sysBuyAvgPrice;
	}

	public void setSysBuyAvgPrice(String sysBuyAvgPrice) {
		this.sysBuyAvgPrice = sysBuyAvgPrice;
	}


}
