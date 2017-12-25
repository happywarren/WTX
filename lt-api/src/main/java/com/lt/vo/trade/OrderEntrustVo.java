package com.lt.vo.trade;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托订单列表返回实体
 * @author guodw
 *
 */
public class OrderEntrustVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8088556019320246630L;
	/** 订单显示ID */
	private String orderId;
	/** 商品编码: 如CL1701 */
	private String productCode;
	/** 商品名称: 如美原油 */
	private String productName;
	/** 交易类型: 1 买多, 2 买空, 3 卖多, 4 卖空  */
	private Integer tradeType;
	private Integer tradeDirection;
	/** 委托手数 */
	private Integer entrustCount;
	/** 委托价 */
	private Double entrustPrice;
	/** 价格类型: 1:市价; 2:限价(报价) */
	private Integer entrustType;
	/** 创建时间 */
	private Date createDate;
	/** 状态: 1成功; 0失败 */
	private Integer status;
	/** 开仓成功手数 */
	private Integer buySuccessCount;
	/** 平仓成功手数 */
	private Integer sellSuccessCount;
	/** 开仓成交均价 */
	private Double buyAvgPrice;
	/** 平仓成交均价 */
	private Double sellAvgPrice;
	
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
	
	public Integer getEntrustCount() {
		return entrustCount;
	}
	public void setEntrustCount(Integer entrustCount) {
		this.entrustCount = entrustCount;
	}
	public Double getEntrustPrice() {
		return entrustPrice;
	}
	public void setEntrustPrice(Double entrustPrice) {
		this.entrustPrice = entrustPrice;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public Integer getEntrustType() {
		return entrustType;
	}
	public void setEntrustType(Integer entrustType) {
		this.entrustType = entrustType;
	}
	public Integer getBuySuccessCount() {
		return buySuccessCount;
	}
	public void setBuySuccessCount(Integer buySuccessCount) {
		this.buySuccessCount = buySuccessCount;
	}
	public Integer getSellSuccessCount() {
		return sellSuccessCount;
	}
	public void setSellSuccessCount(Integer sellSuccessCount) {
		this.sellSuccessCount = sellSuccessCount;
	}
	public Double getBuyAvgPrice() {
		return buyAvgPrice;
	}
	public void setBuyAvgPrice(Double buyAvgPrice) {
		this.buyAvgPrice = buyAvgPrice;
	}
	public Double getSellAvgPrice() {
		return sellAvgPrice;
	}
	public void setSellAvgPrice(Double sellAvgPrice) {
		this.sellAvgPrice = sellAvgPrice;
	}
	
}
