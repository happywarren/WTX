package com.lt.model.fund;

import java.io.Serializable;

import com.lt.util.utils.DoubleUtils;

/**
 * 账户中心使用。各个账户状态
 * 
 * @author guodw
 *
 */
public class FundVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8990267359385075907L;

	/**
	 * 现金余额
	 */
	private Double cashAmt; // 余额

	/**
	 * 积分余额
	 */
	private Double scoreAmt;
	/**
	 * 现金浮动盈亏
	 */
	private Double floatCashAmt; // 动态收益，暂时不添加,后续可以添加

	/**
	 * 积分浮动盈亏
	 */
	private Double floatScoreAmt;

	/**
	 * 积分持仓保证金
	 */
	private Double holdScoreFund;
	/**
	 * 现金持仓保证金
	 */
	private Double holdCashFund;

	public Double getCashAmt() {
		return cashAmt;
	}

	public void setCashAmt(Double cashAmt) {
		this.cashAmt = DoubleUtils.scaleFormat(cashAmt, 2);
	}

	public Double getScoreAmt() {
		return scoreAmt;
	}

	public void setScoreAmt(Double scoreAmt) {
		this.scoreAmt = DoubleUtils.scaleFormat(scoreAmt, 2);
	}

	public Double getFloatCashAmt() {
		return floatCashAmt;
	}

	public void setFloatCashAmt(Double floatCashAmt) {
		this.floatCashAmt =  DoubleUtils.scaleFormat(floatCashAmt, 2);
	}

	public Double getFloatScoreAmt() {
		return floatScoreAmt;
	}

	public void setFloatScoreAmt(Double floatScoreAmt) {
		this.floatScoreAmt = DoubleUtils.scaleFormat(floatScoreAmt, 2);
	}

	public Double getHoldScoreFund() {
		return holdScoreFund;
	}

	public void setHoldScoreFund(Double holdScoreFund) {
		this.holdScoreFund = DoubleUtils.scaleFormat(holdScoreFund, 2);
	}

	public Double getHoldCashFund() {
		return holdCashFund;
	}

	public void setHoldCashFund(Double holdCashFund) {
		this.holdCashFund = DoubleUtils.scaleFormat(holdCashFund, 2);
	}

}
