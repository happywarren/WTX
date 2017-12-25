package com.lt.model.dispatcher.enums;

import java.io.Serializable;

public class RedisQuotaObject implements Serializable {

	/**
	 * 卖一价
	 */
	private String askPrice1;
	/**
	 * 卖量1
	 */
	private String askQty1;
	/**
	 * 均价
	 */
	private String averagePrice;
	/**
	 *  买1价
	 */
	private String bidPrice1;
	/**
	 * 买量1
	 */
	private String bidQty1;
	/**
	 *  涨幅
	 */
	private String changeRate;
	/**
	 * 涨跌值
	 */
	private String changeValue;
	/**
	 * 最高价
	 */
	private String highPrice;
	/**
	 * 最新价
	 */
	private String lastPrice;
    /**
     * 跌停价
     */
	private String limitDownPrice;
	/**
	 * 涨停价
	 */
	private String limitUpPrice;
	/**
	 * 最低价
	 */
	private String lowPrice;
	/**
	 * 开盘价
	 */
	private String openPrice;
	/**
	 * 持仓量
	 */
	private String positionQty;
	/**
	 * 昨收盘价
	 */
	private String preClosePrice;
	/**
	 * 昨结算价
	 */
	private String preSettlePrice;
	/**
	 * 商品
	 */
	private String productName;
	/**
	 * 结算价
	 */
	private String settlePrice;
	/**
	 * 时间戳
	 */
	private String timeStamp;
	/**
	 * 当日总成交量
	 */
	private Integer totalQty;
	public String getAskPrice1() {
		return askPrice1;
	}
	public void setAskPrice1(String askPrice1) {
		this.askPrice1 = askPrice1;
	}
	public String getAskQty1() {
		return askQty1;
	}
	public void setAskQty1(String askQty1) {
		this.askQty1 = askQty1;
	}
	public String getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
	}
	public String getBidPrice1() {
		return bidPrice1;
	}
	public void setBidPrice1(String bidPrice1) {
		this.bidPrice1 = bidPrice1;
	}
	public String getBidQty1() {
		return bidQty1;
	}
	public void setBidQty1(String bidQty1) {
		this.bidQty1 = bidQty1;
	}
	public String getChangeRate() {
		return changeRate;
	}
	public void setChangeRate(String changeRate) {
		this.changeRate = changeRate;
	}
	public String getChangeValue() {
		return changeValue;
	}
	public void setChangeValue(String changeValue) {
		this.changeValue = changeValue;
	}
	public String getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}
	public String getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}
	public String getLimitDownPrice() {
		return limitDownPrice;
	}
	public void setLimitDownPrice(String limitDownPrice) {
		this.limitDownPrice = limitDownPrice;
	}
	public String getLimitUpPrice() {
		return limitUpPrice;
	}
	public void setLimitUpPrice(String limitUpPrice) {
		this.limitUpPrice = limitUpPrice;
	}
	public String getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}
	public String getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}
	public String getPositionQty() {
		return positionQty;
	}
	public void setPositionQty(String positionQty) {
		this.positionQty = positionQty;
	}
	public String getPreClosePrice() {
		return preClosePrice;
	}
	public void setPreClosePrice(String preClosePrice) {
		this.preClosePrice = preClosePrice;
	}
	public String getPreSettlePrice() {
		return preSettlePrice;
	}
	public void setPreSettlePrice(String preSettlePrice) {
		this.preSettlePrice = preSettlePrice;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSettlePrice() {
		return settlePrice;
	}
	public void setSettlePrice(String settlePrice) {
		this.settlePrice = settlePrice;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
	
	
}
