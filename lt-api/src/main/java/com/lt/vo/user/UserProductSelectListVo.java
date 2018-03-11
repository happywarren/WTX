package com.lt.vo.user;

import java.io.Serializable;

public class UserProductSelectListVo implements Serializable,Comparable<UserProductSelectListVo>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int productTypeId;
	private int marketStatus;
	private String beginTime;
	private int id;
	private String slogan;
	private String productCode;
    private String typeName;
    private String shutReason;
    private String iconUrl;
    private int status;
    private String productName;
    private int decimalDigits;//小数位数
	private int plate;   //内外盘标识
	/**
	 * exchangeId：交易所ID
	 */
	private int exchangeId;
	private int sort;
	//当前价
	//涨跌幅 （今日开盘价-昨日收盘价）/昨日收盘价*100%，行情色彩为红涨绿跌。
	public int getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}
	public int getMarketStatus() {
		return marketStatus;
	}
	public void setMarketStatus(int marketStatus) {
		this.marketStatus = marketStatus;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getShutReason() {
		return shutReason;
	}
	public void setShutReason(String shutReason) {
		this.shutReason = shutReason;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}
	public int getDecimalDigits() {
		return decimalDigits;
	}
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}
	public int getPlate() {return plate;}
	public void setPlate(int plate) {this.plate = plate;}
	public int getSort() { return sort;}
    public void setSort(int sort) {this.sort = sort;}

	@Override
	public int compareTo(UserProductSelectListVo o) {
		if(this.sort >o.getSort())
			return 1;
		return -1;
	}
}
