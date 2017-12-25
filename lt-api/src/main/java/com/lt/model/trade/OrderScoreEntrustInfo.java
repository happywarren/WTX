package com.lt.model.trade;

import java.io.Serializable;
import java.util.Date;
/**
 * 订单现金委托实体
 * @author jingwb
 *
 */
public class OrderScoreEntrustInfo implements Serializable{
	
	private static final long serialVersionUID = -6154187795296910777L;
	/**由数据库自增生成，不参与实际业务*/
	private Integer id;
	/**委托单id，由java系统生成（参与实际业务）*/
	private String entrustId;
	/**订单的order_id*/
	private String orderId;
	/**交易所返回的委托单号*/
	private String exEntrustId;
	/**商品id*/
	private Integer productId;
	/**商品代码*/
	private String productCode;
	/**商品名称*/
	private String productName;
	/**交易所编码*/
	private String exchangeCode;
	/**内外盘标识：0 内盘, 1 外盘*/
	private Integer plate;
	/**券商id*/
	private String investorId;
	/**证券账户id*/
	private Integer accountId;
	/**证券账户*/
	private String securityCode;
	/**触发方式100人工、200策略*/
	private Integer triggerType;
	/**委托方式1市价，2限价*/
	private Integer entrustType;
	/**报单委托价(市价 0；挂单为委托价)*/
	private Double entrustPrice;
	/**交易类型1开仓 2平仓*/
	private Integer tradeType;
	/**交易方向1看多 2看空*/
	private Integer tradeDirection;
	/**实际交易类型（转换后） 平仓: 67(C) ; 开仓:79(O)*/
	private Integer actTradeType;
	/**实际交易方向(转换后): 买:66(B); 卖:83(S)*/
	private Integer actTradeDirection;
	/**委托数量*/
	private Integer entrustCount;
	/**委托时间（通知c++时间）*/
	private Date entrustDate;
	/**委托状态1成功 0失败*/
	private Integer entrustStatus;
	/**错误编码*/
	private String errorCode;
	/**错误信息*/
	private String errorMsg;	
	/**创建时间*/
	private Date createDate;
	/**修改时间*/
	private Date modifyDate;
	/**限价加的点位*/
	private Double limitedPriceValue;
	
	private Double quotaPrice;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEntrustId() {
		return entrustId;
	}
	public void setEntrustId(String entrustId) {
		this.entrustId = entrustId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getExEntrustId() {
		return exEntrustId;
	}
	public void setExEntrustId(String exEntrustId) {
		this.exEntrustId = exEntrustId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
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
	public String getExchangeCode() {
		return exchangeCode;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public Integer getPlate() {
		return plate;
	}
	public void setPlate(Integer plate) {
		this.plate = plate;
	}

	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	
	public Integer getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public Integer getEntrustType() {
		return entrustType;
	}
	public void setEntrustType(Integer entrustType) {
		this.entrustType = entrustType;
	}
	public Double getEntrustPrice() {
		return entrustPrice;
	}
	public void setEntrustPrice(Double entrustPrice) {
		this.entrustPrice = entrustPrice;
	}
	public Integer getTradeType() {
		return tradeType;
	}
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
	public Integer getTradeDirection() {
		return tradeDirection;
	}
	public void setTradeDirection(Integer tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	public Integer getActTradeType() {
		return actTradeType;
	}
	public void setActTradeType(Integer actTradeType) {
		this.actTradeType = actTradeType;
	}
	public Integer getActTradeDirection() {
		return actTradeDirection;
	}
	public void setActTradeDirection(Integer actTradeDirection) {
		this.actTradeDirection = actTradeDirection;
	}
	
	public Integer getEntrustCount() {
		return entrustCount;
	}
	public void setEntrustCount(Integer entrustCount) {
		this.entrustCount = entrustCount;
	}
	public Date getEntrustDate() {
		return entrustDate;
	}
	public void setEntrustDate(Date entrustDate) {
		this.entrustDate = entrustDate;
	}
	public Integer getEntrustStatus() {
		return entrustStatus;
	}
	public void setEntrustStatus(Integer entrustStatus) {
		this.entrustStatus = entrustStatus;
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
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Double getLimitedPriceValue() {
		return limitedPriceValue;
	}
	public void setLimitedPriceValue(Double limitedPriceValue) {
		this.limitedPriceValue = limitedPriceValue;
	}
	
	public Double getQuotaPrice() {
		return quotaPrice;
	}
	public void setQuotaPrice(Double quotaPrice) {
		this.quotaPrice = quotaPrice;
	}
	public OrderScoreEntrustInfo(){
		
	}
	
	public OrderScoreEntrustInfo(String orderId,
			String entrustId, Integer productId, String productCode,
			String productName, String exchangeCode, Integer plate,
			String securityCode,String investorId,Integer accountId, Integer tradeType, Integer tradeDirection,Integer actTradeType,
			Integer actTradeDirection, Integer entrustCount, Double entrustPrice,
			Integer entrustType, Double limitedPriceValue, Date createDate, Date modifyDate){
		this.orderId = orderId;
		this.entrustId = entrustId;
		this.productId = productId;
		this.productCode = productCode;
		this.productName = productName;
		this.exchangeCode = exchangeCode;
		this.plate = plate;
		this.securityCode = securityCode;
		this.investorId = investorId;
		this.accountId = accountId;
		this.tradeType = tradeType;
		this.tradeDirection = tradeDirection;
		this.actTradeType = actTradeType;
		this.actTradeDirection = actTradeDirection;
		this.entrustCount = entrustCount;
		this.entrustPrice = entrustPrice;
		this.entrustType = entrustType;
		this.limitedPriceValue = limitedPriceValue;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
	}
}
