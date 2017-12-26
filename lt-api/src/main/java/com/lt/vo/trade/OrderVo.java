/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.vo.trade
 * FILE    NAME: OrderVo.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.vo.trade;

import java.io.Serializable;
import java.util.Date;

import com.lt.model.user.UserBaseInfo;
import com.lt.vo.product.ProductVo;

/**
 * TODO 开仓平仓订单参数
 * @author XieZhibing
 * @date 2016年12月8日 下午5:34:54
 * @version <b>1.0.0</b>
 */
public class OrderVo implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 8319339966011777121L;
	/** 商品编码 */
	private String productCode;
	/** 商品名称 */
	private String productName;
	/** 商品类型ID */
	private Integer productTypeId;
	/** 用户ID */
	private String userId;
	/**用户信息实体*/
	private UserBaseInfo userBaseInfo;
	/** 券商ID */
	private String investorId;
	/** 订单ID */
	private String orderId;
	/** 交易手数 */
	private int count = 0;
	/** 交易方向: 0 空; 1 多 */
	private Integer tradeDirection = 0;
	/** 递延状态: 0 非递延; 1 递延 */
	private int deferStatus;
	/** 移动止损开关: 0.关; 1.开 */
	private Integer trailStopLoss;
	/** 资金类型: 0 现金; 1 积分 */
	private Integer fundType = 1;
	/** 止损值 */
	private double stopLoss = 0.00;
	/** 止盈值 */
	private double stopProfit = 0.00;
	/** 用户买入价 */
	private double userBuyPrice = 0.00;
	/** 用户买入时间 */
	private Date userBuyDate;
	/**买触发类型*/
	private Integer buyTriggerType;
	/** 平仓触发方式 */
	private Integer sellTriggerType;
	/** 平仓操作人ID */
	private String sellStaffId;
	/** 用户卖出价 */
	private double userSalePrice = 0.00;
	/** 用户卖出时间 */
	private Date userSaleDate;
	/** 手续费 */
	private double surcharge = 0.00;	
	/** 汇率(与人民币汇率) */
	private double rate=1.00;
	/** 订单总金额 */
	private double totalAmount=0.00;
	/** 商品参数 */
	private ProductVo productVo;
	/** 系统清仓时间 */
	private Date sysSetSaleDate;
	/** 曾经到达的移动止损最高价 ; */
	private Double sentinelPrice;
	
	private String externalId;
	/**记录下单方式：0:市价单 1:条件单 2:闪电单;*/
	private Integer purchaseOrderType;
	/**修改人*/
	private String modifyUserId;
	/**用户下单时间（app前端时间）*/
	private String orderTime;
	/**用户所属品牌 id*/
	private String brandId;
	/**迷你单的倍数*/
	private Double mini;

	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月8日 下午5:28:10
	 */
	public OrderVo() {
		super();
	}
	
	/**
	 * 构造 买入下单用
	 * @author XieZhibing
	 * @date 2016年12月16日 上午11:27:52
	 * @param productCode
	 * @param productName
	 * @param productTypeId
	 * @param userId
	 * @param investorId
	 * @param count
	 * @param tradeType
	 * @param deferStatus
	 * @param trailStopLoss
	 * @param fundType
	 * @param stopLoss
	 * @param stopProfit
	 * @param userBuyPrice
	 * @param userBuyDate
	 */
	public OrderVo(String productCode, String productName, Integer productTypeId, String userId,
			String investorId, int count, Integer tradeDirection, int deferStatus,
			Integer trailStopLoss, Integer fundType, double stopLoss,
			double stopProfit, double userBuyPrice, Date userBuyDate,Integer buyTriggerType,
			Integer purchaseOrderType) {
		super();
		this.productCode = productCode;
		this.productName = productName;
		this.productTypeId = productTypeId;
		this.userId = userId;
		this.investorId = investorId;
		this.count = count;
		this.tradeDirection = tradeDirection;
		this.deferStatus = deferStatus;
		this.trailStopLoss = trailStopLoss;
		this.fundType = fundType;
		this.stopLoss = stopLoss;
		this.stopProfit = stopProfit;
		this.userBuyPrice = userBuyPrice;
		this.userBuyDate = userBuyDate;
		this.buyTriggerType = buyTriggerType;
		this.purchaseOrderType = purchaseOrderType;
	}

	@Override
	public String toString() {
		return "OrderVo{" +
				"productCode='" + productCode + '\'' +
				", productName='" + productName + '\'' +
				", productTypeId=" + productTypeId +
				", userId='" + userId + '\'' +
				", userBaseInfo=" + userBaseInfo +
				", investorId='" + investorId + '\'' +
				", orderId='" + orderId + '\'' +
				", count=" + count +
				", tradeDirection=" + tradeDirection +
				", deferStatus=" + deferStatus +
				", trailStopLoss=" + trailStopLoss +
				", fundType=" + fundType +
				", stopLoss=" + stopLoss +
				", stopProfit=" + stopProfit +
				", userBuyPrice=" + userBuyPrice +
				", userBuyDate=" + userBuyDate +
				", buyTriggerType=" + buyTriggerType +
				", sellTriggerType=" + sellTriggerType +
				", sellStaffId='" + sellStaffId + '\'' +
				", userSalePrice=" + userSalePrice +
				", userSaleDate=" + userSaleDate +
				", surcharge=" + surcharge +
				", rate=" + rate +
				", totalAmount=" + totalAmount +
				", productVo=" + productVo +
				", sysSetSaleDate=" + sysSetSaleDate +
				", sentinelPrice=" + sentinelPrice +
				", externalId='" + externalId + '\'' +
				", purchaseOrderType=" + purchaseOrderType +
				", modifyUserId='" + modifyUserId + '\'' +
				", orderTime='" + orderTime + '\'' +
				", brandId='" + brandId + '\'' +
				'}';
	}

	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月16日 下午3:54:50
	 * @param displayId
	 * @param fundType
	 * @param sellType
	 * @param sellStaffId
	 * @param userSalePrice
	 * @param userSaleDate
	 */
	public OrderVo(String orderId, Integer fundType, Integer sellTriggerType,
			String sellStaffId, double userSalePrice, Date userSaleDate) {
		super();
		this.orderId = orderId;
		this.fundType = fundType;
		this.sellTriggerType = sellTriggerType;
		this.userId = sellStaffId;
		this.sellStaffId = sellStaffId;
		this.userSalePrice = userSalePrice;
		this.userSaleDate = userSaleDate;
	}

	/** 
	 * 获取 商品编码 
	 * @return productCode 
	 */
	public String getProductCode() {
		return productCode;
	}
	/** 
	 * 设置 商品编码 
	 * @param productCode 商品编码 
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/** 
	 * 获取 商品名称 
	 * @return productName 
	 */
	public String getProductName() {
		return productName;
	}
	/** 
	 * 设置 商品名称 
	 * @param productName 商品名称 
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/** 
	 * 获取 商品类型ID 
	 * @return productTypeId 
	 */
	public Integer getProductTypeId() {
		return productTypeId;
	}

	/** 
	 * 设置 商品类型ID 
	 * @param productTypeId 商品类型ID 
	 */
	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}


	public String getInvestorId() {
		return investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/** 
	 * 获取 交易手数 
	 * @return count 
	 */
	public int getCount() {
		return count;
	}
	/** 
	 * 设置 交易手数 
	 * @param count 交易手数 
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	public Integer getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(Integer tradeDirection) {
		this.tradeDirection = tradeDirection;
	}

	/** 
	 * 获取 递延状态: 0 非递延; 1 递延 
	 * @return deferStatus 
	 */
	public int getDeferStatus() {
		return deferStatus;
	}
	/** 
	 * 设置 递延状态: 0 非递延; 1 递延 
	 * @param deferStatus 递延状态: 0 非递延; 1 递延 
	 */
	public void setDeferStatus(int deferStatus) {
		this.deferStatus = deferStatus;
	}
	
	/** 
	 * 获取 移动止损开关: 0.关; 1.开 
	 * @return trailStopLoss 
	 */
	public Integer getTrailStopLoss() {
		return trailStopLoss;
	}

	/** 
	 * 设置 移动止损开关: 0.关; 1.开 
	 * @param trailStopLoss 移动止损开关: 0.关; 1.开 
	 */
	public void setTrailStopLoss(Integer trailStopLoss) {
		this.trailStopLoss = trailStopLoss;
	}

	/** 
	 * 获取 资金类型: 0 现金; 1 积分 
	 * @return fundType 
	 */
	public Integer getFundType() {
		return fundType;
	}
	/** 
	 * 设置 资金类型: 0 现金; 1 积分 
	 * @param fundType 资金类型: 0 现金; 1 积分 
	 */
	public void setFundType(Integer fundType) {
		this.fundType = fundType;
	}
	/** 
	 * 获取 止损值 
	 * @return stopLoss 
	 */
	public double getStopLoss() {
		return stopLoss;
	}
	/** 
	 * 设置 止损值 
	 * @param stopLoss 止损值 
	 */
	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}
	/** 
	 * 获取 止盈值 
	 * @return stopProfit 
	 */
	public double getStopProfit() {
		return stopProfit;
	}
	/** 
	 * 设置 止盈值 
	 * @param stopProfit 止盈值 
	 */
	public void setStopProfit(double stopProfit) {
		this.stopProfit = stopProfit;
	}
	/** 
	 * 获取 用户买入价 
	 * @return userBuyPrice 
	 */
	public double getUserBuyPrice() {
		return userBuyPrice;
	}
	/** 
	 * 设置 用户买入价 
	 * @param userBuyPrice 用户买入价 
	 */
	public void setUserBuyPrice(double userBuyPrice) {
		this.userBuyPrice = userBuyPrice;
	}
	/** 
	 * 获取 用户买入时间 
	 * @return userBuyDate 
	 */
	public Date getUserBuyDate() {
		return userBuyDate;
	}
	/** 
	 * 设置 用户买入时间 
	 * @param userBuyDate 用户买入时间 
	 */
	public void setUserBuyDate(Date userBuyDate) {
		this.userBuyDate = userBuyDate;
	}	

	

	public Integer getBuyTriggerType() {
		return buyTriggerType;
	}

	public void setBuyTriggerType(Integer buyTriggerType) {
		this.buyTriggerType = buyTriggerType;
	}

	public Integer getSellTriggerType() {
		return sellTriggerType;
	}

	public void setSellTriggerType(Integer sellTriggerType) {
		this.sellTriggerType = sellTriggerType;
	}


	/** 
	 * 获取 用户卖出价 
	 * @return userSalePrice 
	 */
	public double getUserSalePrice() {
		return userSalePrice;
	}
	/** 
	 * 设置 用户卖出价 
	 * @param userSalePrice 用户卖出价 
	 */
	public void setUserSalePrice(double userSalePrice) {
		this.userSalePrice = userSalePrice;
	}
	/** 
	 * 获取 用户卖出时间 
	 * @return userSaleDate 
	 */
	public Date getUserSaleDate() {
		return userSaleDate;
	}
	/** 
	 * 设置 用户卖出时间 
	 * @param userSaleDate 用户卖出时间 
	 */
	public void setUserSaleDate(Date userSaleDate) {
		this.userSaleDate = userSaleDate;
	}
	/** 
	 * 获取 手续费 
	 * @return surcharge 
	 */
	public double getSurcharge() {
		return surcharge;
	}
	/** 
	 * 设置 手续费 
	 * @param surcharge 手续费 
	 */
	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
	}
	/** 
	 * 获取 汇率(与人民币汇率) 
	 * @return rate 
	 */
	public double getRate() {
		return rate;
	}
	/** 
	 * 设置 汇率(与人民币汇率) 
	 * @param rate 汇率(与人民币汇率) 
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/** 
	 * 获取 订单总金额 
	 * @return totalAmount 
	 */
	public double getTotalAmount() {
		return totalAmount;
	}

	/** 
	 * 设置 订单总金额 
	 * @param totalAmount 订单总金额 
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/** 
	 * 获取 商品参数 
	 * @return productVo 
	 */
	public ProductVo getProductVo() {
		return productVo;
	}

	/** 
	 * 设置 商品参数 
	 * @param productVo 商品参数 
	 */
	public void setProductVo(ProductVo productVo) {
		this.productVo = productVo;
	}


	public Double getSentinelPrice() {
		return sentinelPrice;
	}

	public void setSentinelPrice(Double sentinelPrice) {
		this.sentinelPrice = sentinelPrice;
	}

	/** 
	 * 获取 系统清仓时间 
	 * @return sysSetSaleDate 
	 */
	public Date getSysSetSaleDate() {
		return sysSetSaleDate;
	}

	/** 
	 * 设置 系统清仓时间 
	 * @param sysSetSaleDate 系统清仓时间 
	 */
	public void setSysSetSaleDate(Date sysSetSaleDate) {
		this.sysSetSaleDate = sysSetSaleDate;
	}

	public UserBaseInfo getUserBaseInfo() {
		return userBaseInfo;
	}

	public void setUserBaseInfo(UserBaseInfo userBaseInfo) {
		this.userBaseInfo = userBaseInfo;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSellStaffId() {
		return sellStaffId;
	}

	public void setSellStaffId(String sellStaffId) {
		this.sellStaffId = sellStaffId;
	}

	public Integer getPurchaseOrderType() {
		return purchaseOrderType;
	}

	public void setPurchaseOrderType(Integer purchaseOrderType) {
		this.purchaseOrderType = purchaseOrderType;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public Double getMini() {
		return mini;
	}

	public void setMini(Double mini) {
		this.mini = mini;
	}
}
