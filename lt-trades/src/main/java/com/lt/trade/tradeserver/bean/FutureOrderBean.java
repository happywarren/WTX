package com.lt.trade.tradeserver.bean;

/**
 * 封装期货报单信息
 *
 * Created by sunch on 2016/11/10.
 */
public class FutureOrderBean {

	/** 期货账户 */
    private String investorId;
    /** 交易所 */
    private String exchangeNo;    
    /** 商品 */
    private String productName;
    /** 报单id */
    private int orderInsertId;
	/** 订单id */
	private String uniqueOrderId;
    /** 数量 */
    private int amount; 
    /** 报单价 */
    private double orderPrice;
    /** 行情价 */
    private double quotePrice;
    /** 方向(买/卖) */
    private int direct;
    /** 开/平仓 */
    private int offset; 
    /** 类型(市价/限价) */
    private int orderType;
	/** 资金类型: 0 现金; 1 积分 */
	private int fundType;
    /** 时间戳(毫秒) */
    private long timeStamp;
    /**平仓触发类型*/
    private Integer sellType;
    
    /** 曾经到达的移动止损最高价 ; */
	private Double sentinelPrice;
	/**
	 * 用户ID
	 */
	private String userId;

    public FutureOrderBean() {
    }

    public FutureOrderBean(String userId,String investorId, String exchangeNo, String productName,
                           String uniqueOrderId, int orderInsertId, int amount, double price,
						   int direct, int offset, int orderType, int fundType, long timeStamp) {
    	this.userId = userId;
    	this.investorId = investorId;
        this.exchangeNo = exchangeNo;
        this.productName = productName;

		this.uniqueOrderId = uniqueOrderId;
        this.orderInsertId = orderInsertId;
        this.amount = amount;
        this.orderPrice = price;
        this.quotePrice = price;

        this.direct = direct;
        this.offset = offset;
        this.orderType = orderType;
		this.fundType = fundType;

        this.timeStamp = timeStamp;
    }

	/** 
	 * 获取 期货账户 
	 * @return investorId 
	 */
	public String getInvestorId() {
		return investorId;
	}

	/** 
	 * 设置 期货账户 
	 * @param investorId 期货账户 
	 */
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	/** 
	 * 获取 交易所 
	 * @return exchangeNo 
	 */
	public String getExchangeNo() {
		return exchangeNo;
	}

	/** 
	 * 设置 交易所 
	 * @param exchangeNo 交易所 
	 */
	public void setExchangeNo(String exchangeNo) {
		this.exchangeNo = exchangeNo;
	}

	/** 
	 * 获取 商品 
	 * @return productName 
	 */
	public String getProductName() {
		return productName;
	}

	/** 
	 * 设置 商品 
	 * @param productName 商品 
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getOrderInsertId() {
		return orderInsertId;
	}

	public void setOrderInsertId(int orderInsertId) {
		this.orderInsertId = orderInsertId;
	}

	public String getUniqueOrderId() {
		return uniqueOrderId;
	}

	public void setUniqueOrderId(String uniqueOrderId) {
		this.uniqueOrderId = uniqueOrderId;
	}

	/**
	 * 获取 数量 
	 * @return amount 
	 */
	public int getAmount() {
		return amount;
	}

	/** 
	 * 设置 数量 
	 * @param amount 数量 
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/** 
	 * 获取 报单价 
	 * @return orderPrice 
	 */
	public double getOrderPrice() {
		return orderPrice;
	}

	/** 
	 * 设置 报单价 
	 * @param orderPrice 报单价 
	 */
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	/** 
	 * 获取 行情价 
	 * @return quotePrice 
	 */
	public double getQuotePrice() {
		return quotePrice;
	}

	/** 
	 * 设置 行情价 
	 * @param quotePrice 行情价 
	 */
	public void setQuotePrice(double quotePrice) {
		this.quotePrice = quotePrice;
	}

	/** 
	 * 获取 方向(买卖) 
	 * @return direct 
	 */
	public int getDirect() {
		return direct;
	}

	/** 
	 * 设置 方向(买卖) 
	 * @param direct 方向(买卖) 
	 */
	public void setDirect(int direct) {
		this.direct = direct;
	}

	/** 
	 * 获取 开平仓 
	 * @return offset 
	 */
	public int getOffset() {
		return offset;
	}

	/** 
	 * 设置 开平仓 
	 * @param offset 开平仓 
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/** 
	 * 获取 类型(市价限价) 
	 * @return orderType 
	 */
	public int getOrderType() {
		return orderType;
	}

	/** 
	 * 设置 类型(市价限价) 
	 * @param orderType 类型(市价限价) 
	 */
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getFundType() {
		return fundType;
	}

	public void setFundType(int fundType) {
		this.fundType = fundType;
	}

	/**
	 * 获取 时间戳(毫秒) 
	 * @return timeStamp 
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/** 
	 * 设置 时间戳(毫秒) 
	 * @param timeStamp 时间戳(毫秒) 
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getSellType() {
		return sellType;
	}

	public void setSellType(Integer sellType) {
		this.sellType = sellType;
	}

	public Double getSentinelPrice() {
		return sentinelPrice;
	}

	public void setSentinelPrice(Double sentinelPrice) {
		this.sentinelPrice = sentinelPrice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
