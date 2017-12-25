package com.lt.trade.tradeserver.bean;

/**
 * 封装期货成交信息
 *
 * Created by sunch on 2016/12/8.
 */
public class FutureMatchBean extends BaseMatchBean {

	/** 交易平台订单id */
    private int platformId;
    /** 交易所 */
    private String exchangeNo;
    /** 商品 */
    private String productName;
    /** 方向 */
    private Integer direct;
    /** 开平 */
    private Integer offset;
    /** 期货公司委托id */
    private String sysOrderId;
    /** 委托数量 */
    private Integer orderTotal;
    /** 期货公司成交id */
    private String sysMatchId;
    /** 本次成交数量 */
    private Integer matchVol;
    /** 已成交数量 */
    private Integer matchTotal;
    /** 成交价 */
    private Double matchPrice;
    /** 0 自成交, 1 交易所成交 */
    private Integer matchSelf;
    /** 投资者帐号 */
    private String investorId;
    /** 成交时间 */
    private String matchDateTime;
    /**  资金类型  0、现金；1、积分 */
    private int fundType;
    public FutureMatchBean() {
    }

	/** 
	 * 获取 交易平台订单id 
	 * @return platformId 
	 */
	public int getPlatformId() {
		return platformId;
	}

	/** 
	 * 设置 交易平台订单id 
	 * @param platformId 交易平台订单id 
	 */
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
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

	/** 
	 * 获取 方向 
	 * @return direct 
	 */
	public Integer getDirect() {
		return direct;
	}

	/** 
	 * 设置 方向 
	 * @param direct 方向 
	 */
	public void setDirect(Integer direct) {
		this.direct = direct;
	}

	/** 
	 * 获取 开平 
	 * @return offset 
	 */
	public Integer getOffset() {
		return offset;
	}

	/** 
	 * 设置 开平 
	 * @param offset 开平 
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/** 
	 * 获取 期货公司委托id 
	 * @return sysOrderId 
	 */
	public String getSysOrderId() {
		return sysOrderId;
	}

	/** 
	 * 设置 期货公司委托id 
	 * @param sysOrderId 期货公司委托id 
	 */
	public void setSysOrderId(String sysOrderId) {
		this.sysOrderId = sysOrderId;
	}

	/** 
	 * 获取 委托数量 
	 * @return orderTotal 
	 */
	public Integer getOrderTotal() {
		return orderTotal;
	}

	/** 
	 * 设置 委托数量 
	 * @param orderTotal 委托数量 
	 */
	public void setOrderTotal(Integer orderTotal) {
		this.orderTotal = orderTotal;
	}

	/** 
	 * 获取 期货公司成交id 
	 * @return sysMatchId 
	 */
	public String getSysMatchId() {
		return sysMatchId;
	}

	/** 
	 * 设置 期货公司成交id 
	 * @param sysMatchId 期货公司成交id 
	 */
	public void setSysMatchId(String sysMatchId) {
		this.sysMatchId = sysMatchId;
	}

	/** 
	 * 获取 本次成交数量 
	 * @return matchVol 
	 */
	public Integer getMatchVol() {
		return matchVol;
	}

	/** 
	 * 设置 本次成交数量 
	 * @param matchVol 本次成交数量 
	 */
	public void setMatchVol(Integer matchVol) {
		this.matchVol = matchVol;
	}

	/** 
	 * 获取 已成交数量 
	 * @return matchTotal 
	 */
	public Integer getMatchTotal() {
		return matchTotal;
	}

	/** 
	 * 设置 已成交数量 
	 * @param matchTotal 已成交数量 
	 */
	public void setMatchTotal(Integer matchTotal) {
		this.matchTotal = matchTotal;
	}

	/** 
	 * 获取 成交价 
	 * @return matchPrice 
	 */
	public Double getMatchPrice() {
		return matchPrice;
	}

	/** 
	 * 设置 成交价 
	 * @param matchPrice 成交价 
	 */
	public void setMatchPrice(Double matchPrice) {
		this.matchPrice = matchPrice;
	}

	/** 
	 * 获取 0 自成交 1 交易所成交 
	 * @return matchSelf 
	 */
	public Integer getMatchSelf() {
		return matchSelf;
	}

	/** 
	 * 设置 0 自成交 1 交易所成交 
	 * @param matchSelf 0 自成交 1 交易所成交 
	 */
	public void setMatchSelf(Integer matchSelf) {
		this.matchSelf = matchSelf;
	}

	/** 
	 * 获取 投资者帐号 
	 * @return investorId 
	 */
	public String getInvestorId() {
		return investorId;
	}

	/** 
	 * 设置 投资者帐号 
	 * @param investorId 投资者帐号 
	 */
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	/** 
	 * 获取 成交时间 
	 * @return matchDateTime 
	 */
	public String getMatchDateTime() {
		return matchDateTime;
	}

	/** 
	 * 设置 成交时间 
	 * @param matchDateTime 成交时间 
	 */
	public void setMatchDateTime(String matchDateTime) {
		this.matchDateTime = matchDateTime;
	}

	public int getFundType() {
		return fundType;
	}

	public void setFundType(int fundType) {
		this.fundType = fundType;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月29日 下午8:43:48
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FutureMatchBean [platformId=").append(platformId)
				.append(", exchangeNo=").append(exchangeNo)
				.append(", productName=").append(productName)
				.append(", direct=").append(direct).append(", offset=")
				.append(offset).append(", sysOrderId=").append(sysOrderId)
				.append(", orderTotal=").append(orderTotal)
				.append(", sysMatchId=").append(sysMatchId)
				.append(", matchVol=").append(matchVol).append(", matchTotal=")
				.append(matchTotal).append(", matchPrice=").append(matchPrice)
				.append(", matchSelf=").append(matchSelf)
				.append(", investorId=").append(investorId)
				.append(", fundType=").append(fundType)
				.append(", matchDateTime=").append(matchDateTime).append("]");
		return builder.toString();
	}

}
