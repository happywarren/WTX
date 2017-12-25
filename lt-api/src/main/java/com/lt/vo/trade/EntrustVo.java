package com.lt.vo.trade;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户委托信息
 * 
 * @author guodw
 *
 */
public class EntrustVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5611346716102151948L;
	/**
	 * 委托价 （用户提交价格）
	 */
	private String userCommitBuyPrice;
	/**
	 * 委托时间
	 */
	private String entrustBuyPrice;
	/**
	 * 开仓委托手数
	 */
	private String buyEntrustCount;	
	/**
	 * 开仓成功手数
	 */
	private String buySuccessCount;
	/**
	 * 交易方向: 1 买多; 2 买空
	 */
	private String tradeDirection;
	/**
	 * 商品名称: 如美原油
	 */
	private String productName;
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	private String tradeType;

	public String getUserCommitBuyPrice() {
		return userCommitBuyPrice;
	}

	public void setUserCommitBuyPrice(String userCommitBuyPrice) {
		this.userCommitBuyPrice = userCommitBuyPrice;
	}

	public String getBuyEntrustCount() {
		return buyEntrustCount;
	}

	public void setBuyEntrustCount(String buyEntrustCount) {
		this.buyEntrustCount = buyEntrustCount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getEntrustBuyPrice() {
		return entrustBuyPrice;
	}

	public void setEntrustBuyPrice(String entrustBuyPrice) {
		this.entrustBuyPrice = entrustBuyPrice;
	}

	public String getBuySuccessCount() {
		return buySuccessCount;
	}

	public void setBuySuccessCount(String buySuccessCount) {
		this.buySuccessCount = buySuccessCount;
	}

	public String getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(String tradeDirection) {
		this.tradeDirection = tradeDirection;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

}
