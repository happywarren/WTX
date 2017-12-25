package com.lt.manager.bean.product;


import com.lt.manager.bean.BaseBean;
import com.lt.util.utils.StringTools;

import java.util.ArrayList;
import java.util.List;

public class ProductParamVO extends BaseBean{
	private static final long serialVersionUID = 1L;
	
	private String name;//名称	
	private Integer sortNum;//排序编号	
	private String remark;//备注
	private String code;//代码如CL1601
	private String shortCode;//品种代码如CL
	private Integer productTypeId;//商品类型id
	private String productTypeCode;//商品类型code
	private Integer exchangeId;//交易所id
	private String quotaExchangeCode; //行情交易所id
	private Integer status;//上架状态
	private String beginTime;//开始时间
	private String endTime;//结束时间
	private Integer operator;//操作人
	private Integer marketStatus;//市场状态
	private Integer decimalDigits;//小数位数
	private Integer currentRateId;//通用货币id
	private String expirationBeginTime;//合约开始时间
	private String expirationTime;//合约到期时间
	private Integer fundType;//资金类型：0现金，1虚拟 2all
	private String jsonTimeConfigList;//时间配置（夏令时）json格式
	private String jsonTimeConfigWinterList;//时间配置(冬令时)json格式
	private String jsonRiskConfigList;//风险杠杆配置json格式
	private String jsonTradeConfig;//商品配置json格式
	private String jsonQuotaConfig;//行情配置json格式
	private String slogan;//广告语
	private String iconUrl;//图片地址
	private Integer isOperate;//是否允许行情触发开市0:否 1：是
	private String tagIds;//标签id串1,2,3
	private Integer num;//行情总条数
	private String shutReason;//操作市场状态执行者
	private String jsonProductCodeList;//商品代码和合约到期json
	private String openTimeSummer;//开市时间夏令时
	private String closeTimeSummer;//闭市时间夏令时
	private String openTimeWinter;//开市时间冬令时
	private String closeTimeWinter;//闭市时间冬令时
	private Double spread;//点差
	private Double contractSize;//合约规格
	private Integer maxLever;//最大杠杆
	private Integer maxSingleOpenPosition;//单次最大开仓量
	private Integer maxPositionPerAccount;//单户最大净持仓量
	private List<String> spreadModProductList;//差价合约点差变化商品列表
    private Integer brandPosition;	//品牌净持仓量
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public Integer getMarketStatus() {
		return marketStatus;
	}

	public void setMarketStatus(Integer marketStatus) {
		this.marketStatus = marketStatus;
	}

	public Integer getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(Integer decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public Integer getCurrentRateId() {
		return currentRateId;
	}

	public void setCurrentRateId(Integer currentRateId) {
		this.currentRateId = currentRateId;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Integer getFundType() {
		return fundType;
	}

	public void setFundType(Integer fundType) {
		this.fundType = fundType;
	}

	public String getJsonTimeConfigList() {
		return jsonTimeConfigList;
	}

	public void setJsonTimeConfigList(String jsonTimeConfigList) {
		this.jsonTimeConfigList = jsonTimeConfigList;
	}

	public String getJsonRiskConfigList() {
		return jsonRiskConfigList;
	}

	public void setJsonRiskConfigList(String jsonRiskConfigList) {
		this.jsonRiskConfigList = jsonRiskConfigList;
	}

	public String getJsonTradeConfig() {
		return jsonTradeConfig;
	}

	public void setJsonTradeConfig(String jsonTradeConfig) {
		this.jsonTradeConfig = jsonTradeConfig;
	}

	public String getJsonQuotaConfig() {
		return jsonQuotaConfig;
	}

	public void setJsonQuotaConfig(String jsonQuotaConfig) {
		this.jsonQuotaConfig = jsonQuotaConfig;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Integer getIsOperate() {
		return isOperate;
	}

	public void setIsOperate(Integer isOperate) {
		this.isOperate = isOperate;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getShutReason() {
		return shutReason;
	}

	public void setShutReason(String shutReason) {
		this.shutReason = shutReason;
	}

	public String getJsonProductCodeList() {
		return jsonProductCodeList;
	}

	public void setJsonProductCodeList(String jsonProductCodeList) {
		this.jsonProductCodeList = jsonProductCodeList;
	}

	public String getExpirationBeginTime() {
		return expirationBeginTime;
	}

	public void setExpirationBeginTime(String expirationBeginTime) {
		this.expirationBeginTime = expirationBeginTime;
	}

	public String getJsonTimeConfigWinterList() {
		return jsonTimeConfigWinterList;
	}

	public void setJsonTimeConfigWinterList(String jsonTimeConfigWinterList) {
		this.jsonTimeConfigWinterList = jsonTimeConfigWinterList;
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

	public List<String> getSpreadModProductList() {
		return spreadModProductList;
	}

	public void setSpreadModProductList(List<String> spreadModProductList) {
		this.spreadModProductList = spreadModProductList;
	}

	public void addSpreadModProductList(String productCode){
		if(StringTools.isEmpty(spreadModProductList)){
			spreadModProductList = new ArrayList<>();
			spreadModProductList.add(productCode);
		}
		else{
			spreadModProductList.add(productCode);
		}
	}

	public Integer getBrandPosition() {
		return brandPosition;
	}

	public void setBrandPosition(Integer brandPosition) {
		this.brandPosition = brandPosition;
	}

	public static class ProductCode{
		
		private Integer id;//商品id
		/**商品代码*/
		private String productCode;
		/**合约开始时间*/
		private String expirationBeginTime;
		/**合约到期时间*/
		private String expirationEndTime;
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getExpirationBeginTime() {
			return expirationBeginTime;
		}
		public void setExpirationBeginTime(String expirationBeginTime) {
			this.expirationBeginTime = expirationBeginTime;
		}
		public String getExpirationEndTime() {
			return expirationEndTime;
		}
		public void setExpirationEndTime(String expirationEndTime) {
			this.expirationEndTime = expirationEndTime;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		
		
	}

	@Override
	public String toString() {
		return "ProductParamVO [" + (name != null ? "name=" + name + ", " : "")
				+ (sortNum != null ? "sortNum=" + sortNum + ", " : "")
				+ (remark != null ? "remark=" + remark + ", " : "") + (code != null ? "code=" + code + ", " : "")
				+ (shortCode != null ? "shortCode=" + shortCode + ", " : "")
				+ (productTypeId != null ? "productTypeId=" + productTypeId + ", " : "")
				+ (productTypeCode != null ? "productTypeCode=" + productTypeCode + ", " : "")
				+ (exchangeId != null ? "exchangeId=" + exchangeId + ", " : "")
				+ (quotaExchangeCode != null ? "quotaExchangeCode=" + quotaExchangeCode + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (beginTime != null ? "beginTime=" + beginTime + ", " : "")
				+ (endTime != null ? "endTime=" + endTime + ", " : "")
				+ (operator != null ? "operator=" + operator + ", " : "")
				+ (marketStatus != null ? "marketStatus=" + marketStatus + ", " : "")
				+ (decimalDigits != null ? "decimalDigits=" + decimalDigits + ", " : "")
				+ (currentRateId != null ? "currentRateId=" + currentRateId + ", " : "")
				+ (expirationBeginTime != null ? "expirationBeginTime=" + expirationBeginTime + ", " : "")
				+ (expirationTime != null ? "expirationTime=" + expirationTime + ", " : "")
				+ (fundType != null ? "fundType=" + fundType + ", " : "")
				+ (jsonTimeConfigList != null ? "jsonTimeConfigList=" + jsonTimeConfigList + ", " : "")
				+ (jsonTimeConfigWinterList != null ? "jsonTimeConfigWinterList=" + jsonTimeConfigWinterList + ", "
						: "")
				+ (jsonRiskConfigList != null ? "jsonRiskConfigList=" + jsonRiskConfigList + ", " : "")
				+ (jsonTradeConfig != null ? "jsonTradeConfig=" + jsonTradeConfig + ", " : "")
				+ (jsonQuotaConfig != null ? "jsonQuotaConfig=" + jsonQuotaConfig + ", " : "")
				+ (slogan != null ? "slogan=" + slogan + ", " : "")
				+ (iconUrl != null ? "iconUrl=" + iconUrl + ", " : "")
				+ (isOperate != null ? "isOperate=" + isOperate + ", " : "")
				+ (tagIds != null ? "tagIds=" + tagIds + ", " : "") + (num != null ? "num=" + num + ", " : "")
				+ (shutReason != null ? "shutReason=" + shutReason + ", " : "")
				+ (jsonProductCodeList != null ? "jsonProductCodeList=" + jsonProductCodeList + ", " : "")
				+ (openTimeSummer != null ? "openTimeSummer=" + openTimeSummer + ", " : "")
				+ (closeTimeSummer != null ? "closeTimeSummer=" + closeTimeSummer + ", " : "")
				+ (openTimeWinter != null ? "openTimeWinter=" + openTimeWinter + ", " : "")
				+ (closeTimeWinter != null ? "closeTimeWinter=" + closeTimeWinter + ", " : "")
				+ (spread != null ? "spread=" + spread + ", " : "")
				+ (contractSize != null ? "contractSize=" + contractSize + ", " : "")
				+ (maxLever != null ? "maxLever=" + maxLever + ", " : "")
				+ (maxSingleOpenPosition != null ? "maxSingleOpenPosition=" + maxSingleOpenPosition + ", " : "")
				+ (maxPositionPerAccount != null ? "maxPositionPerAccount=" + maxPositionPerAccount + ", " : "")
				+ (spreadModProductList != null ? "spreadModProductList=" + spreadModProductList + ", " : "")
				+ (brandPosition != null ? "brandPosition=" + brandPosition : "") + "]";
	}
}
