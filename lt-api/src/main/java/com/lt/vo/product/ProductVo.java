package com.lt.vo.product;

import com.lt.model.product.Product;

import java.io.Serializable;

/**
 * 扩展实体
 * @author guodw
 *
 */
public class ProductVo extends Product implements Serializable{

	/**
	 * 行情浮动限制
	 */
	private Double floatLimit;
	/**
	 * 行情图基线
	 */
	private Integer baseline;	
	/**
	 * 闪动图波动区间
	 */
	private Float intervalFloat;
	/**
	 * 是否双线0:关闭 1：开启
	 */
	private Integer isDouble;
	/**
	 * 商品类型名
	 */
	private String typeName;
	/**
	 * 货币名
	 */
	private String currencyName;
	/**
	 * 币种编码(CNY、USD)'
	 */
	private String currency;
	/**
	 * 货币符号
	 */
	private String currencySign;
	/**
	 * 货币单位
	 */
	private String currencyUnit;
	/**
	 * 交易所代码
	 */
	private String exchangeCode;
	/**
	 * 交易所名字
	 */
	private String exchangeName;
	/**
	 * 汇率
	 */
	private Double rate;
	
	/**
	 * 最小波动点，如 0.01
	 */
	private Double jumpValue;
	
	/**
	 * 最小变动价格，如10
	 */
	private Double jumpPrice;

	/**
	 * 是否支持市价，0：否 1：是
	 */
	private Integer isMarketPrice;
	
	/**
	 * 限价加减点位
	 */
	private Double limitedPriceValue;
	
	/**
	 * 一天内交易分钟数，分时图一天多少个点
	 */
	private Integer dayPoints;


	public Integer getDayPoints() {
		return dayPoints;
	}

	public void setDayPoints(Integer dayPoints) {
		this.dayPoints = dayPoints;
	}

	/** 
	 * 获取 行情浮动限制 
	 * @return floatLimit 
	 */
	public Double getFloatLimit() {
		return floatLimit;
	}

	/** 
	 * 设置 行情浮动限制 
	 * @param floatLimit 行情浮动限制 
	 */
	public void setFloatLimit(Double floatLimit) {
		this.floatLimit = floatLimit;
	}

	/** 
	 * 获取 行情图基线 
	 * @return baseline 
	 */
	public Integer getBaseline() {
		return baseline;
	}

	/** 
	 * 设置 行情图基线 
	 * @param baseline 行情图基线 
	 */
	public void setBaseline(Integer baseline) {
		this.baseline = baseline;
	}

	/** 
	 * 获取 闪动图波动区间 
	 * @return intervalFloat 
	 */
	public Float getIntervalFloat() {
		return intervalFloat;
	}

	/** 
	 * 设置 闪动图波动区间 
	 * @param intervalFloat 闪动图波动区间 
	 */
	public void setIntervalFloat(Float intervalFloat) {
		this.intervalFloat = intervalFloat;
	}

	/** 
	 * 获取 是否双线0:关闭 1：开启 
	 * @return isDouble 
	 */
	public Integer getIsDouble() {
		return isDouble;
	}

	/** 
	 * 设置 是否双线0:关闭 1：开启 
	 * @param isDouble 是否双线0:关闭 1：开启 
	 */
	public void setIsDouble(Integer isDouble) {
		this.isDouble = isDouble;
	}

	/** 
	 * 获取 商品类型名 
	 * @return typeName 
	 */
	public String getTypeName() {
		return typeName;
	}

	/** 
	 * 设置 商品类型名 
	 * @param typeName 商品类型名 
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** 
	 * 获取 货币名 
	 * @return currencyName 
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/** 
	 * 设置 货币名 
	 * @param currencyName 货币名 
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	/** 
	 * 获取 币种编码(CNY、USD)' 
	 * @return currency 
	 */
	public String getCurrency() {
		return currency;
	}

	/** 
	 * 设置 币种编码(CNY、USD)' 
	 * @param currency 币种编码(CNY、USD)' 
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/** 
	 * 获取 货币符号 
	 * @return currencySign 
	 */
	public String getCurrencySign() {
		return currencySign;
	}

	/** 
	 * 设置 货币符号 
	 * @param currencySign 货币符号 
	 */
	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	/** 
	 * 获取 货币单位 
	 * @return currencyUnit 
	 */
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	/** 
	 * 设置 货币单位 
	 * @param currencyUnit 货币单位 
	 */
	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	/** 
	 * 获取 交易所代码 
	 * @return exchangeCode 
	 */
	public String getExchangeCode() {
		return exchangeCode;
	}

	/** 
	 * 设置 交易所代码 
	 * @param exchangeCode 交易所代码 
	 */
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	/** 
	 * 获取 交易所名字 
	 * @return exchangeName 
	 */
	public String getExchangeName() {
		return exchangeName;
	}

	/** 
	 * 设置 交易所名字 
	 * @param exchangeName 交易所名字 
	 */
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	/** 
	 * 获取 汇率 
	 * @return rate 
	 */
	public Double getRate() {
		return rate;
	}

	/** 
	 * 设置 汇率 
	 * @param rate 汇率 
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}

	/** 
	 * 获取 最小波动点，如 0.01 
	 * @return jumpValue 
	 */
	public Double getJumpValue() {
		return jumpValue;
	}

	/** 
	 * 设置 最小波动点，如 0.01 
	 * @param jumpValue 最小波动点，如 0.01 
	 */
	public void setJumpValue(Double jumpValue) {
		this.jumpValue = jumpValue;
	}

	/** 
	 * 获取 最小变动价格，如10 
	 * @return jumpPrice 
	 */
	public Double getJumpPrice() {
		return jumpPrice;
	}

	/** 
	 * 设置 最小变动价格，如10 
	 * @param jumpPrice 最小变动价格，如10 
	 */
	public void setJumpPrice(Double jumpPrice) {
		this.jumpPrice = jumpPrice;
	}

	/** 
	 * 获取 是否支持市价，0：否 1：是 
	 * @return isMarketPrice 
	 */
	public Integer getIsMarketPrice() {
		return isMarketPrice;
	}

	/** 
	 * 设置 是否支持市价，0：否 1：是 
	 * @param isMarketPrice 是否支持市价，0：否 1：是 
	 */
	public void setIsMarketPrice(Integer isMarketPrice) {
		this.isMarketPrice = isMarketPrice;
	}

	/** 
	 * 获取 限价加减点位 
	 * @return limitedPriceValue 
	 */
	public Double getLimitedPriceValue() {
		return limitedPriceValue;
	}

	/** 
	 * 设置 限价加减点位 
	 * @param limitedPriceValue 限价加减点位 
	 */
	public void setLimitedPriceValue(Double limitedPriceValue) {
		this.limitedPriceValue = limitedPriceValue;
	}

}
