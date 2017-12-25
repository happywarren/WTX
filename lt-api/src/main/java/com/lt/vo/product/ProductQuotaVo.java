package com.lt.vo.product;

import java.io.Serializable;


public class ProductQuotaVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8521654800437517334L;
	/**
	 * 商品
	 */
	private String code;
	/**
	 * 买一价
	 */
	private String bidPrice;
	/**
	 * 卖价
	 */
	private String askPrice;
	/**
	 * 当前价
	 */
	private String lastPrice;
	/**
	 * 涨跌幅
	 */
	private String percentage;
	
	public ProductQuotaVo(){
		
	}
	
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}
	public String getAskPrice() {
		return askPrice;
	}
	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}
	public String getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}
	public ProductQuotaVo(String code, String bidPrice, String askPrice,
			String lastPrice, String percentage) {
		this.code = code;
		this.bidPrice = bidPrice;
		this.askPrice = askPrice;
		this.lastPrice = lastPrice;
		this.percentage = percentage;
	}
	
	
}
