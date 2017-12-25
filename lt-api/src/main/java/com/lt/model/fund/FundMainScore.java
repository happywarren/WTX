package com.lt.model.fund;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DoubleUtils;

/**
 * 
 * TODO 用户积分主账户
 * @author XieZhibing
 * @date 2016年11月28日 下午7:10:45
 * @version <b>1.0.0</b>
 */
public class FundMainScore implements Serializable {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -2859705192970627100L;
	/** 主键ID */
	private Integer id;
	/** 用户ID */
	private String userId;
	/** 积分可用余额 */
	private Double balance = 0.00;
	/** 当前持仓保证金  */
	private Double holdFund = 0.00;
	/** 递延保证金 */
	private Double deferFund = 0.00;
	
	/** 累计交易手续费, 不包含提现手续费 */
	private Double totalCounterFee = 0.00;
	/** 累计兑换金额(积分), 现金兑换积分 */
	private Double totalExchangeAmount = 0.00;
	/** 累计消费金额(积分) */
	private Double totalConsumeAmount = 0.00;	
	/** 累计赠送金额 */
	private Double totalDonateAmount = 0.00;
	/** 累计盈亏, 不包含累计交易手续费 */
	private Double totalBenefitAmount = 0.00;
	/** 累计递延保证金利息 */
	private Double totalInterestAmount = 0.00;
	
	/** 创建时间 */
	private Date createDate;
	/** 修改时间 */
	private Date modifyDate;
	
	/** 累计人工存入金额*/
	private Double totalManualinAmount = 0.0;
	/** 累计人工取出金额*/
	private Double totalManualoutAmount = 0.0;
		
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月30日 下午1:47:45
	 */
	public FundMainScore() {
		super();
	}

	/** 
	 * 获取 主键ID 
	 * @return id 
	 */
	public Integer getId() {
		return id;
	}

	/** 
	 * 设置 主键ID 
	 * @param id 主键ID 
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** 
	 * 获取 积分可用余额 
	 * @return balance 
	 */
	public Double getBalance() {
		return balance;
	}

	/** 
	 * 设置 积分可用余额 
	 * @param balance 积分可用余额 
	 */
	public void setBalance(Double balance) {
		this.balance =  DoubleUtils.scaleFormat(balance, 8);
	}

	/** 
	 * 获取 当前持仓保证金 
	 * @return holdFund 
	 */
	public Double getHoldFund() {
		return holdFund;
	}

	/** 
	 * 设置 当前持仓保证金 
	 * @param holdFund 当前持仓保证金 
	 */
	public void setHoldFund(Double holdFund) {
		this.holdFund = DoubleUtils.scaleFormat(holdFund, 2);
	}

	/** 
	 * 获取 递延保证金 
	 * @return deferFund 
	 */
	public Double getDeferFund() {
		return deferFund;
	}

	/** 
	 * 设置 递延保证金 
	 * @param deferFund 递延保证金 
	 */
	public void setDeferFund(Double deferFund) {
		this.deferFund = DoubleUtils.scaleFormat(deferFund, 2);
	}

	/** 
	 * 获取 累计交易手续费 不包含提现手续费 
	 * @return totalCounterFee 
	 */
	public Double getTotalCounterFee() {
		return totalCounterFee;
	}

	/** 
	 * 设置 累计交易手续费 不包含提现手续费 
	 * @param totalCounterFee 累计交易手续费 不包含提现手续费 
	 */
	public void setTotalCounterFee(Double totalCounterFee) {
		this.totalCounterFee = DoubleUtils.scaleFormat(totalCounterFee, 2);
	}

	/** 
	 * 获取 累计兑换金额(积分) 现金兑换积分 
	 * @return totalExchangeAmount 
	 */
	public Double getTotalExchangeAmount() {
		return totalExchangeAmount;
	}

	/** 
	 * 设置 累计兑换金额(积分) 现金兑换积分 
	 * @param totalExchangeAmount 累计兑换金额(积分) 现金兑换积分 
	 */
	public void setTotalExchangeAmount(Double totalExchangeAmount) {
		this.totalExchangeAmount = DoubleUtils.scaleFormat(totalExchangeAmount, 2);
	}

	/** 
	 * 获取 累计消费金额(积分) 
	 * @return totalConsumeAmount 
	 */
	public Double getTotalConsumeAmount() {
		return totalConsumeAmount;
	}

	/** 
	 * 设置 累计消费金额(积分) 
	 * @param totalConsumeAmount 累计消费金额(积分) 
	 */
	public void setTotalConsumeAmount(Double totalConsumeAmount) {
		this.totalConsumeAmount = DoubleUtils.scaleFormat(totalConsumeAmount, 2);
	}

	/** 
	 * 获取 累计赠送金额 
	 * @return totalDonateAmount 
	 */
	public Double getTotalDonateAmount() {
		return totalDonateAmount;
	}

	/** 
	 * 设置 累计赠送金额 
	 * @param totalDonateAmount 累计赠送金额 
	 */
	public void setTotalDonateAmount(Double totalDonateAmount) {
		this.totalDonateAmount = DoubleUtils.scaleFormat(totalDonateAmount, 2);
	}

	/** 
	 * 获取 累计盈亏 不包含累计交易手续费 
	 * @return totalBenefitAmount 
	 */
	public Double getTotalBenefitAmount() {
		return totalBenefitAmount;
	}

	/** 
	 * 设置 累计盈亏 不包含累计交易手续费 
	 * @param totalBenefitAmount 累计盈亏 不包含累计交易手续费 
	 */
	public void setTotalBenefitAmount(Double totalBenefitAmount) {
		this.totalBenefitAmount = DoubleUtils.scaleFormat(totalBenefitAmount, 2);
	}

	/** 
	 * 获取 累计递延保证金利息 
	 * @return totalInterestAmount 
	 */
	public Double getTotalInterestAmount() {
		return totalInterestAmount;
	}

	/** 
	 * 设置 累计递延保证金利息 
	 * @param totalInterestAmount 累计递延保证金利息 
	 */
	public void setTotalInterestAmount(Double totalInterestAmount) {
		this.totalInterestAmount = DoubleUtils.scaleFormat(totalInterestAmount, 2);
	}

	/** 
	 * 获取 创建时间 
	 * @return createDate 
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/** 
	 * 设置 创建时间 
	 * @param createDate 创建时间 
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/** 
	 * 获取 修改时间 
	 * @return modifyDate 
	 */
	public Date getModifyDate() {
		return modifyDate;
	}

	/** 
	 * 设置 修改时间 
	 * @param modifyDate 修改时间 
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	/**
	 * @return the totalManualinAmount
	 */
	public Double getTotalManualinAmount() {
		return totalManualinAmount;
	}

	/**
	 * @param totalManualinAmount the totalManualinAmount to set
	 */
	public void setTotalManualinAmount(Double totalManualinAmount) {
		this.totalManualinAmount = DoubleUtils.scaleFormat(totalManualinAmount, 2);
	}

	/**
	 * @return the totalManualoutAmount
	 */
	public Double getTotalManualoutAmount() {
		return totalManualoutAmount;
	}

	/**
	 * @param totalManualoutAmount the totalManualoutAmount to set
	 */
	public void setTotalManualoutAmount(Double totalManualoutAmount) {
		this.totalManualoutAmount = DoubleUtils.scaleFormat(totalManualoutAmount, 2);
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2017年2月9日 下午4:46:39
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundMainScore [id=").append(id).append(", userId=")
				.append(userId).append(", balance=").append(balance)
				.append(", holdFund=").append(holdFund).append(", deferFund=")
				.append(deferFund).append(", totalCounterFee=")
				.append(totalCounterFee).append(", totalExchangeAmount=")
				.append(totalExchangeAmount).append(", totalConsumeAmount=")
				.append(totalConsumeAmount).append(", totalDonateAmount=")
				.append(totalDonateAmount).append(", totalManualinAmount=")
				.append(totalManualinAmount).append(", totalManualoutAmount=")
				.append(totalManualoutAmount).append(", totalBenefitAmount=")
				.append(totalBenefitAmount).append(", totalInterestAmount=")
				.append(totalInterestAmount).append(", createDate=")
				.append(createDate).append(", modifyDate=").append(modifyDate)
				.append("]");
		return builder.toString();
	}
	
}
