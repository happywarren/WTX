package com.lt.model.statistic;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DoubleUtils;

/**
 * 下线统计日信息实体
 * @author jingwb
 *
 */
public class StatisticBrancherDayLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6470215398007205789L;
	/***/
	private Integer id;
	/***/
	private String userId;
	private String promoterUserId;//上线userId
	/**充值金额*/
	private Double rechargeAmount;
	/**交易手数*/
	private Integer handCount;
	/**一层下线交易手数*/
	private Integer firstHandCount;
	/**交易金额*/
	private Double tradeAmount;
	/**一层下线交易金额*/
	private Double firstTradeAmount;
	/***/
	private Date createTime;
	private Date modifyTime;
	/**统计数据日期，格式如：2017-02-07*/
	private String statisticTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = DoubleUtils.scaleFormatEnd(rechargeAmount,2);
	}
	public Integer getHandCount() {
		return handCount;
	}
	public void setHandCount(Integer handCount) {
		this.handCount = handCount;
	}
	public Double getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = DoubleUtils.scaleFormatEnd(tradeAmount,2);
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatisticTime() {
		return statisticTime;
	}
	public void setStatisticTime(String statisticTime) {
		this.statisticTime = statisticTime;
	}
	
	public String getPromoterUserId() {
		return promoterUserId;
	}
	public void setPromoterUserId(String promoterUserId) {
		this.promoterUserId = promoterUserId;
	}
	public Integer getFirstHandCount() {
		return firstHandCount;
	}
	public void setFirstHandCount(Integer firstHandCount) {
		this.firstHandCount = firstHandCount;
	}
	public Double getFirstTradeAmount() {
		return firstTradeAmount;
	}
	public void setFirstTradeAmount(Double firstTradeAmount) {
		this.firstTradeAmount = DoubleUtils.scaleFormatEnd(firstTradeAmount,2);
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
