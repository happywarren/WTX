package com.lt.model.product;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品实体
 * @author jingwb
 *
 */
public class Product implements Serializable{
	private Integer id;
	private String productCode;//商品代码如CL1601
	private String shortCode;//品种代码如CL
	private String productName;//商品名
	private Integer productTypeId;//商品类型id
    private String productTypeCode;//商品类型编码
    private Integer exchangeId;//交易所id
	private String quotaExchangeCode; //行情交易所id
	/**
	 * 0：闭市、1：可买不可卖（仅开仓）、2：可卖不可买（仅平仓）、3：可买可卖 、4：休市
	 */
	private Integer marketStatus;//市场状态 可买可卖/不可买卖/可买不可卖/可卖不可买
	/**
	 * 0:下架  1:上架  2:预售  3:过期
	 */
	private Integer status;//上架状态
	private Integer sortNum;//排序编号
	private String iconUrl;//商品图标地址
	private String slogan;//广告语
	private Integer currentRateId;//通用货币id
	private Date createDate;
	private Date modifyDate;
	private Integer operator;
	private Integer decimalDigits;//小数位数
	private String expirationTime;//合约到期时间
	private String expirationBeginTime;//合约开始时间
	private String shutReason;//记录闭市原因
	private String beginTime;//商品交易开始时间格式：00:00:00
	private String endTime;//商品交易结束时间格式：00:00:00
	private Integer fundType;//资金类型：0现金，1虚拟 2all
	private Integer isOperate;//是否允许行情触发开市0:否 1：是(达望用)	
	private Integer plate;//0：内盘 1：外盘'
	private String openTimeSummer;//开市时间夏令时closeTimeWinter
	private String closeTimeSummer;//闭市时间夏令时
	private String openTimeWinter;//开市时间冬令时
	private String closeTimeWinter;//闭市时间冬令时

    /**
	 * 差价合约相关
	 */
	private Double spread;//点差
	private Double contractSize;//合约规格
	private Integer maxLever;//最大杠杆
	private Integer maxSingleOpenPosition;//单次最大开仓量
	private Integer maxPositionPerAccount;//单户最大净持仓量
	private Integer brandPosition;//单户最大净持仓量



	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}
	public Integer getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(Integer exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getQuotaExchangeCode() {
		return quotaExchangeCode;
	}

	public void setQuotaExchangeCode(String quotaExchangeCode) {
		this.quotaExchangeCode = quotaExchangeCode;
	}

	public Integer getMarketStatus() {
		return marketStatus;
	}
	public void setMarketStatus(Integer marketStatus) {
		this.marketStatus = marketStatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public Integer getCurrentRateId() {
		return currentRateId;
	}
	public void setCurrentRateId(Integer currentRateId) {
		this.currentRateId = currentRateId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Integer getOperator() {
		return operator;
	}
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	public Integer getDecimalDigits() {
		return decimalDigits;
	}
	public void setDecimalDigits(Integer decimalDigits) {
		this.decimalDigits = decimalDigits;
	}
	public String getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public String getShutReason() {
		return shutReason;
	}
	public void setShutReason(String shutReason) {
		this.shutReason = shutReason;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getFundType() {
		return fundType;
	}
	public void setFundType(Integer fundType) {
		this.fundType = fundType;
	}
	public Integer getIsOperate() {
		return isOperate;
	}
	public void setIsOperate(Integer isOperate) {
		this.isOperate = isOperate;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public Integer getPlate() {
		return plate;
	}
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	public String getExpirationBeginTime() {
		return expirationBeginTime;
	}
	public void setExpirationBeginTime(String expirationBeginTime) {
		this.expirationBeginTime = expirationBeginTime;
	}
	public String getOpenTimeSummer() {
		return openTimeSummer;
	}
	public void setOpenTimeSummer(String openTimeSummer) {
		this.openTimeSummer = openTimeSummer;
	}
	public String getCloseTimeSummer() {
		return closeTimeSummer;
	}
	public void setCloseTimeSummer(String closeTimeSummer) {
		this.closeTimeSummer = closeTimeSummer;
	}
	public String getOpenTimeWinter() {
		return openTimeWinter;
	}
	public void setOpenTimeWinter(String openTimeWinter) {
		this.openTimeWinter = openTimeWinter;
	}
	public String getCloseTimeWinter() {
		return closeTimeWinter;
	}
	public void setCloseTimeWinter(String closeTimeWinter) {
		this.closeTimeWinter = closeTimeWinter;
	}

	public Double getSpread() {
		return spread;
	}

	public void setSpread(Double spread) {
		this.spread = spread;
	}

	public Double getContractSize() {
		return contractSize;
	}

	public void setContractSize(Double contractSize) {
		this.contractSize = contractSize;
	}

	public Integer getMaxLever() {
		return maxLever;
	}

	public void setMaxLever(Integer maxLever) {
		this.maxLever = maxLever;
	}

	public Integer getMaxSingleOpenPosition() {
		return maxSingleOpenPosition;
	}

	public void setMaxSingleOpenPosition(Integer maxSingleOpenPosition) {
		this.maxSingleOpenPosition = maxSingleOpenPosition;
	}

	public Integer getMaxPositionPerAccount() {
		return maxPositionPerAccount;
	}

	public void setMaxPositionPerAccount(Integer maxPositionPerAccount) {
		this.maxPositionPerAccount = maxPositionPerAccount;
	}

	public String getProductTypeCode() {
		return productTypeCode;
	}

	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", productCode='" + productCode + '\'' +
				", shortCode='" + shortCode + '\'' +
				", productName='" + productName + '\'' +
				", productTypeId=" + productTypeId +
				", productTypeCode='" + productTypeCode + '\'' +
				", exchangeId=" + exchangeId +
				", quotaExchangeCode='" + quotaExchangeCode + '\'' +
				", marketStatus=" + marketStatus +
				", status=" + status +
				", sortNum=" + sortNum +
				", iconUrl='" + iconUrl + '\'' +
				", slogan='" + slogan + '\'' +
				", currentRateId=" + currentRateId +
				", createDate=" + createDate +
				", modifyDate=" + modifyDate +
				", operator=" + operator +
				", decimalDigits=" + decimalDigits +
				", expirationTime='" + expirationTime + '\'' +
				", expirationBeginTime='" + expirationBeginTime + '\'' +
				", shutReason='" + shutReason + '\'' +
				", beginTime='" + beginTime + '\'' +
				", endTime='" + endTime + '\'' +
				", fundType=" + fundType +
				", isOperate=" + isOperate +
				", plate=" + plate +
				", openTimeSummer='" + openTimeSummer + '\'' +
				", closeTimeSummer='" + closeTimeSummer + '\'' +
				", openTimeWinter='" + openTimeWinter + '\'' +
				", closeTimeWinter='" + closeTimeWinter + '\'' +
				", spread=" + spread +
				", contractSize=" + contractSize +
				", maxLever=" + maxLever +
				", maxSingleOpenPosition=" + maxSingleOpenPosition +
				", maxPositionPerAccount=" + maxPositionPerAccount +
				", brandPosition=" + brandPosition +
				'}';
	}

	public Integer getBrandPosition() {
		return brandPosition;
	}

	public void setBrandPosition(Integer brandPosition) {
		this.brandPosition = brandPosition;
	}
}
