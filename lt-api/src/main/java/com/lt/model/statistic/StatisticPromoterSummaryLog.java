package com.lt.model.statistic;



import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DoubleUtils;

/**
 * 推广猿汇总记录实体
 * @author jingwb
 *
 */
public class StatisticPromoterSummaryLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7810361863385232959L;
	/***/
	private Integer id;
	/***/
	private String userId;
	/**一层下线注册用户数*/
	private Integer firstRegisterCount;
	/**二层下线注册用户数*/
	private Integer secondRegisterCount;
	/**一层下线交易用户数*/
	private Integer firstTraderCount;
	/**二层下线交易用户数*/
	private Integer secondTraderCount;
	/**一层下线交易手数*/
	private Integer firstHandCount;
	/**二层下线交易手数*/
	private Integer secondHandCount;
	/**二层充值用户数*/
	private Integer secondRechargerCount;
	/**一层充值用户数*/
	private Integer firstRechargerCount;
	/**一层下线佣金*/
	private Double firstCommision;
	/**二层下线佣金*/
	private Double secondCommision;
	/**一层下线充值金额*/
	private Double firstRechargeAmount;
	/**二层下线充值金额*/
	private Double secondRechargeAmount;
	/**一层下线交易总金额*/
	private Double firstTradeAmount;
	/**二层下线交易总金额*/
	private Double secondTradeAmount;
	/**已结算佣金*/
	private Double balanceCommision;
	/***/
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getFirstRegisterCount() {
		return firstRegisterCount;
	}
	public void setFirstRegisterCount(Integer firstRegisterCount) {
		this.firstRegisterCount = firstRegisterCount;
	}
	public Integer getSecondRegisterCount() {
		return secondRegisterCount;
	}
	public void setSecondRegisterCount(Integer secondRegisterCount) {
		this.secondRegisterCount = secondRegisterCount;
	}
	public Integer getFirstTraderCount() {
		return firstTraderCount;
	}
	public void setFirstTraderCount(Integer firstTraderCount) {
		this.firstTraderCount = firstTraderCount;
	}
	public Integer getSecondTraderCount() {
		return secondTraderCount;
	}
	public void setSecondTraderCount(Integer secondTraderCount) {
		this.secondTraderCount = secondTraderCount;
	}
	public Integer getFirstHandCount() {
		return firstHandCount;
	}
	public void setFirstHandCount(Integer firstHandCount) {
		this.firstHandCount = firstHandCount;
	}
	public Integer getSecondHandCount() {
		return secondHandCount;
	}
	public void setSecondHandCount(Integer secondHandCount) {
		this.secondHandCount = secondHandCount;
	}
	public Integer getSecondRechargerCount() {
		return secondRechargerCount;
	}
	public void setSecondRechargerCount(Integer secondRechargerCount) {
		this.secondRechargerCount = secondRechargerCount;
	}
	public Integer getFirstRechargerCount() {
		return firstRechargerCount;
	}
	public void setFirstRechargerCount(Integer firstRechargerCount) {
		this.firstRechargerCount = firstRechargerCount;
	}
	public Double getFirstCommision() {
		return firstCommision;
	}
	public void setFirstCommision(Double firstCommision) {
		this.firstCommision = DoubleUtils.scaleFormatEnd(firstCommision,2);
	}
	public Double getSecondCommision() {
		return secondCommision;
	}
	public void setSecondCommision(Double secondCommision) {
		this.secondCommision = DoubleUtils.scaleFormatEnd(secondCommision,2);
	}
	public Double getFirstRechargeAmount() {
		return firstRechargeAmount;
	}
	public void setFirstRechargeAmount(Double firstRechargeAmount) {
		this.firstRechargeAmount = DoubleUtils.scaleFormatEnd(firstRechargeAmount,2);
	}
	public Double getSecondRechargeAmount() {
		return secondRechargeAmount;
	}
	public void setSecondRechargeAmount(Double secondRechargeAmount) {
		this.secondRechargeAmount = DoubleUtils.scaleFormatEnd(secondRechargeAmount,2);
	}
	public Double getFirstTradeAmount() {
		return firstTradeAmount;
	}
	public void setFirstTradeAmount(Double firstTradeAmount) {
		this.firstTradeAmount = DoubleUtils.scaleFormatEnd(firstTradeAmount,2);
	}
	public Double getSecondTradeAmount() {
		return secondTradeAmount;
	}
	public void setSecondTradeAmount(Double secondTradeAmount) {
		this.secondTradeAmount = DoubleUtils.scaleFormatEnd(secondTradeAmount,2);
	}
	public Double getBalanceCommision() {
		return balanceCommision;
	}
	public void setBalanceCommision(Double balanceCommision) {
		this.balanceCommision = DoubleUtils.scaleFormatEnd(balanceCommision,2);
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
