package com.lt.vo.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 自选列表
 * @author guodw
 *
 */
public class UserProductSelectVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * beginTIme:开市时间
	 */
	private String beginTime;
	/**
	 * id：商品id
	 */
	private int id;
	/**
	 * marketStatus:市场状态
	 */
	private int marketStatus;
	/**
	 * decimalDigits:小数位数
	 */
	private int decimalDigits;

	/**
	 * 清仓时间
	 */
	private Date expirationTime;

	/**
	 * currencyName: 币种名称 eg:人民币
	 */
	private String currencyName;
	/**
	 * currency:币种编码
	 */

	private String currency;
	/**
	 * exchangeCode：交易所编码
	 */

	private String exchangeCode;
	/**
	 * jumpPrice:最小变动价格
	 * 
	 */
	private double jumpPrice;
	/**
	 * shortCode:品种编码
	 */
	private String shortCode;

	/**
	 */
	private int sortNum;

	/**
	 * productTypeId:商品类型Id
	 */
	private int productTypeId;
	/**
	 * slogan:广告语
	 */
	private String slogan;

	private Integer plate;

	/**
	 * currencySign:币种符号 eg: ¥
	 */
	private String currencySign;

	/**
	 * 限价加减点位
	 */
	private Integer limitedPriceValue;
	/**
	 * productCode:商品代码
	 */
	private String productCode;
	/**
	 * endTime：闭市时间
	 */
	private String endTime;

	/**
	 * 是否支持市价
	 */
	private int isMarketPrice;
	/**
	 * baseline:基线
	 */
	private int baseline;
	/**
	 * typeName:分类名
	 */
	private String typeName;

	/**
	 * 记录闭市原因
	 */
	private String shutReason;
	/**
	 * status: 上架相关状态 0:下架，1:上架 2:预售 3:过期
	 */
	private int status;

	private Date createDate;

	/**
	 * 行情浮动限制
	 */
	private Double floatLimit;
	/**
	 * iconUrl:图片地址
	 */
	private String iconUrl;

	private Date modifyDate;
	/**
	 * fundType：产品类型 0:现金实盘 1:积分模拟
	 */
	private int fundType;
	/**
	 * exchangeName：交易所名称
	 */
	private String exchangeName;
	/**
	 * productName: 商品名
	 */
	private String productName;
	/**
	 * currencyUnit: 币种单位 eg:元
	 */
	private String currencyUnit;

	/**
	 * 是否允许行情触发开市0:否 1：是
	 */
	private int isOperate;

	/**
	 * 当日汇率ID
	 */
	private int currentRateId;
	/**
	 * jumpValue: 最小波动点
	 */
	private Double jumpValue;
	/**
	 * rate:汇率
	 */
	private Double rate;
	/**
	 * exchangeId：交易所ID
	 */
	private int exchangeId;

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

	public int getMarketStatus() {
		return marketStatus;
	}

	public void setMarketStatus(int marketStatus) {
		this.marketStatus = marketStatus;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public double getJumpPrice() {
		return jumpPrice;
	}

	public void setJumpPrice(double jumpPrice) {
		this.jumpPrice = jumpPrice;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public Integer getPlate() {
		return plate;
	}

	public void setPlate(Integer plate) {
		this.plate = plate;
	}

	public String getCurrencySign() {
		return currencySign;
	}

	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	public Integer getLimitedPriceValue() {
		return limitedPriceValue;
	}

	public void setLimitedPriceValue(Integer limitedPriceValue) {
		this.limitedPriceValue = limitedPriceValue;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getIsMarketPrice() {
		return isMarketPrice;
	}

	public void setIsMarketPrice(int isMarketPrice) {
		this.isMarketPrice = isMarketPrice;
	}

	public int getBaseline() {
		return baseline;
	}

	public void setBaseline(int baseline) {
		this.baseline = baseline;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Double getFloatLimit() {
		return floatLimit;
	}

	public void setFloatLimit(Double floatLimit) {
		this.floatLimit = floatLimit;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getFundType() {
		return fundType;
	}

	public void setFundType(int fundType) {
		this.fundType = fundType;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	public int getIsOperate() {
		return isOperate;
	}

	public void setIsOperate(int isOperate) {
		this.isOperate = isOperate;
	}

	public int getCurrentRateId() {
		return currentRateId;
	}

	public void setCurrentRateId(int currentRateId) {
		this.currentRateId = currentRateId;
	}

	public Double getJumpValue() {
		return jumpValue;
	}

	public void setJumpValue(Double jumpValue) {
		this.jumpValue = jumpValue;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}

}
