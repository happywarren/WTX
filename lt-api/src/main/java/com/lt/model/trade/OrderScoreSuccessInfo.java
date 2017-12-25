package com.lt.model.trade;

import java.io.Serializable;
import java.util.Date;
/**
 * 订单现金成交信息实体
 * @author jingwb
 *
 */
public class OrderScoreSuccessInfo implements Serializable{

	
	private static final long serialVersionUID = -1010453467750515730L;

	/**id由数据库自增生成（不参与实际业务）*/
	private Integer id;
	/**成交单id，由java系统生成（参与实际业务）*/
	private String successId;
	/**订单的order_id*/
	private String orderId;
	/**委托单id*/
	private String entrustId;
	/**交易所返回的成交单号*/
	private String exSuccessId;
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
	/**'内外盘标识：0 内盘, 1 外盘*/
	private Integer plate;
	/**券商id*/
	private String investorId;
	/**证券账户id*/
	private Integer accountId;
	/**证券账户*/
	private String securityCode;
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
	/**成交数量*/
	private Integer successCount;
	/**已成交数量*/
	private Integer totalSuccessCount;
	/**成交价*/
	private Double successPrice;
	/**成交时间*/
	private Date successDate;
	/**创建时间*/
	private Date createDate;
	/**成交状态：1：部分成交  2：完全成交*/
	private Integer successStatus;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSuccessId() {
		return successId;
	}
	public void setSuccessId(String successId) {
		this.successId = successId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getEntrustId() {
		return entrustId;
	}
	public void setEntrustId(String entrustId) {
		this.entrustId = entrustId;
	}
	public String getExSuccessId() {
		return exSuccessId;
	}
	public void setExSuccessId(String exSuccessId) {
		this.exSuccessId = exSuccessId;
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
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public Integer getTotalSuccessCount() {
		return totalSuccessCount;
	}
	public void setTotalSuccessCount(Integer totalSuccessCount) {
		this.totalSuccessCount = totalSuccessCount;
	}
	public Double getSuccessPrice() {
		return successPrice;
	}
	public void setSuccessPrice(Double successPrice) {
		this.successPrice = successPrice;
	}
	public Date getSuccessDate() {
		return successDate;
	}
	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public Integer getSuccessStatus() {
		return successStatus;
	}
	public void setSuccessStatus(Integer successStatus) {
		this.successStatus = successStatus;
	}
	public String getExEntrustId() {
		return exEntrustId;
	}
	public void setExEntrustId(String exEntrustId) {
		this.exEntrustId = exEntrustId;
	}
	public OrderScoreSuccessInfo(){
		
	}
	
	public OrderScoreSuccessInfo(String successId,String orderId,
			String entrustId, Integer productId,String investorId,Integer accountId,
			String productCode, String productName, String exchangeCode,
			Integer plate, String securityCode, Integer tradeType,Integer tradeDirection,
			Integer actTradeDirection, Integer actTradeType,
			Integer entrustCount,Integer successCount, Integer totalSuccessCount,String exSuccessId,
			String exEntrustId,Double successPrice,Date successDate, Date createDate){
		this.successId = successId;
		this.orderId = orderId;
		this.entrustId = entrustId;
		this.productId = productId;
		this.investorId = investorId;
		this.accountId = accountId;
		this.productCode = productCode;
		this.productName = productName;
		this.exchangeCode = exchangeCode;
		this.plate = plate;
		this.securityCode = securityCode;
		this.tradeType = tradeType;
		this.tradeDirection = tradeDirection;
		this.actTradeDirection = actTradeDirection;
		this.actTradeType = actTradeType;
		this.entrustCount = entrustCount;
		this.successCount = successCount;
		this.totalSuccessCount = totalSuccessCount;
		this.exSuccessId = exSuccessId;
		this.exEntrustId = exEntrustId;
		this.successPrice = successPrice;
		this.successDate = successDate;
		this.createDate = createDate;
	}
}
