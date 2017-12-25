package com.lt.controller.promote.bean;

import java.util.Date;

/**
 * 推广猿汇总记录实体
 * @author jingwb
 *
 */
public class StatisticPromoterSummaryLogOld{

	private String user_id;
	/**一层下线注册用户数*/
	private Integer first_register_count;
	/**二层下线注册用户数*/
	private Integer second_register_count;
	/**一层下线交易用户数*/
	private Integer first_trader_count;
	/**二层下线交易用户数*/
	private Integer second_trader_count;
	/**一层下线交易手数*/
	private Integer first_hand_count;
	/**二层下线交易手数*/
	private Integer second_hand_count;
	/**二层充值用户数*/
	private Integer second_recharger_count;
	/**一层充值用户数*/
	private Integer first_recharger_count;
	/**一层下线佣金*/
	private Double first_commision;
	/**二层下线佣金*/
	private Double second_commision;
	/**一层下线充值金额*/
	private Double first_recharge_amount;
	/**二层下线充值金额*/
	private Double second_recharge_amount;
	/**一层下线交易总金额*/
	private Double first_trade_amount;
	/**二层下线交易总金额*/
	private Double second_trade_amount;
	/**已结算佣金*/
	private Double balance_commision;
	/***/
	private Date create_time;
	private Date modify_time;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Integer getFirst_register_count() {
		return first_register_count;
	}
	public void setFirst_register_count(Integer first_register_count) {
		this.first_register_count = first_register_count;
	}
	public Integer getSecond_register_count() {
		return second_register_count;
	}
	public void setSecond_register_count(Integer second_register_count) {
		this.second_register_count = second_register_count;
	}
	public Integer getFirst_trader_count() {
		return first_trader_count;
	}
	public void setFirst_trader_count(Integer first_trader_count) {
		this.first_trader_count = first_trader_count;
	}
	public Integer getSecond_trader_count() {
		return second_trader_count;
	}
	public void setSecond_trader_count(Integer second_trader_count) {
		this.second_trader_count = second_trader_count;
	}
	public Integer getFirst_hand_count() {
		return first_hand_count;
	}
	public void setFirst_hand_count(Integer first_hand_count) {
		this.first_hand_count = first_hand_count;
	}
	public Integer getSecond_hand_count() {
		return second_hand_count;
	}
	public void setSecond_hand_count(Integer second_hand_count) {
		this.second_hand_count = second_hand_count;
	}
	public Integer getSecond_recharger_count() {
		return second_recharger_count;
	}
	public void setSecond_recharger_count(Integer second_recharger_count) {
		this.second_recharger_count = second_recharger_count;
	}
	public Integer getFirst_recharger_count() {
		return first_recharger_count;
	}
	public void setFirst_recharger_count(Integer first_recharger_count) {
		this.first_recharger_count = first_recharger_count;
	}
	public Double getFirst_commision() {
		return first_commision;
	}
	public void setFirst_commision(Double first_commision) {
		this.first_commision = first_commision;
	}
	public Double getSecond_commision() {
		return second_commision;
	}
	public void setSecond_commision(Double second_commision) {
		this.second_commision = second_commision;
	}
	public Double getFirst_recharge_amount() {
		return first_recharge_amount;
	}
	public void setFirst_recharge_amount(Double first_recharge_amount) {
		this.first_recharge_amount = first_recharge_amount;
	}
	public Double getSecond_recharge_amount() {
		return second_recharge_amount;
	}
	public void setSecond_recharge_amount(Double second_recharge_amount) {
		this.second_recharge_amount = second_recharge_amount;
	}
	public Double getFirst_trade_amount() {
		return first_trade_amount;
	}
	public void setFirst_trade_amount(Double first_trade_amount) {
		this.first_trade_amount = first_trade_amount;
	}
	public Double getSecond_trade_amount() {
		return second_trade_amount;
	}
	public void setSecond_trade_amount(Double second_trade_amount) {
		this.second_trade_amount = second_trade_amount;
	}
	public Double getBalance_commision() {
		return balance_commision;
	}
	public void setBalance_commision(Double balance_commision) {
		this.balance_commision = balance_commision;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getModify_time() {
		return modify_time;
	}
	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}
	
	
}
