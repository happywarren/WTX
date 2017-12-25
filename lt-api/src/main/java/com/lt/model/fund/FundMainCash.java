package com.lt.model.fund;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DoubleUtils;

/**
 * 
 * TODO 用户现金主账户
 * @author XieZhibing
 * @date 2016年11月28日 下午7:10:45
 * @version <b>1.0.0</b>
 */
public class FundMainCash implements Serializable {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 1793937961026006690L;
	/** 主键ID */
	private Integer id;
	/** 用户ID */
	private String userId;
	
	/** 现金可用余额 */
	private Double balance = 0.00;
	/** 冻结资金, 提现待审核资金 */
	private Double freezeAmount = 0.00;
	/** 当前持仓保证金  */
	private Double holdFund = 0.00;
	/** 递延保证金 */
	private Double deferFund = 0.00;
		
	/** 累计交易手续费, 不包含提现手续费 */
	private Double totalCounterFee = 0.00;
	/** 累计充值(现金) */
	private Double totalRechargeAmount = 0.00;
	/** 累计赠送金额 */
	private Double totalPresentAmount = 0.00;
	/** 累计盈亏, 不包含累计交易手续费 */
	private Double totalBenefitAmount = 0.00;
	/** 累计提现金额, 包含累计提现手续费 */
	private Double totalDrawAmount = 0.00;
	/** 累积回收金额(现金) */	
	private Double totalRecycleAmount = 0.00;
	/** 累计递延保证金利息 */
	private Double totalInterestAmount = 0.00;
	
	/** 创建时间 */
	private Date createDate;
	/** 修改时间 */
	private Date modifyDate;
	
	/** 总的人工存入金额 */
	private Double totalManualinAmount;
	/***/
	private Double totalManualoutAmount;
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年11月30日 上午9:57:53
	 */
	public FundMainCash() {
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
	 * 获取 现金可用余额 
	 * @return balance 
	 */
	public Double getBalance() {
		return balance;
	}

	/** 
	 * 设置 现金可用余额 
	 * @param balance 现金可用余额 
	 */
	public void setBalance(Double balance) {
		this.balance = DoubleUtils.scaleFormat(balance, 8);
	}

	/** 
	 * 获取 冻结资金 提现待审核资金 
	 * @return freezeAmount 
	 */
	public Double getFreezeAmount() {
		return freezeAmount;
	}

	/** 
	 * 设置 冻结资金 提现待审核资金 
	 * @param freezeAmount 冻结资金 提现待审核资金 
	 */
	public void setFreezeAmount(Double freezeAmount) {
		this.freezeAmount = DoubleUtils.scaleFormat(freezeAmount, 2);
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
		this.holdFund = holdFund;
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
		this.totalCounterFee =  DoubleUtils.scaleFormat(totalCounterFee, 2);
	}

	/** 
	 * 获取 累计充值(现金) 
	 * @return totalRechargeAmount 
	 */
	public Double getTotalRechargeAmount() {
		return totalRechargeAmount;
	}

	/** 
	 * 设置 累计充值(现金) 
	 * @param totalRechargeAmount 累计充值(现金) 
	 */
	public void setTotalRechargeAmount(Double totalRechargeAmount) {
		this.totalRechargeAmount = DoubleUtils.scaleFormatEnd(totalRechargeAmount, 2);
	}

	public Double getTotalPresentAmount() {
		return totalPresentAmount;
	}

	public void setTotalPresentAmount(Double totalPresentAmount) {
		this.totalPresentAmount =  DoubleUtils.scaleFormat(totalPresentAmount, 2);
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
	 * 获取 累计提现金额 包含累计提现手续费 
	 * @return totalDrawAmount 
	 */
	public Double getTotalDrawAmount() {
		return totalDrawAmount;
	}

	/** 
	 * 设置 累计提现金额 包含累计提现手续费 
	 * @param totalDrawAmount 累计提现金额 包含累计提现手续费 
	 */
	public void setTotalDrawAmount(Double totalDrawAmount) {
		this.totalDrawAmount = DoubleUtils.scaleFormat(totalDrawAmount, 2);
	}

	/** 
	 * 获取 累积回收金额(现金) 
	 * @return totalRecycleAmount 
	 */
	public Double getTotalRecycleAmount() {
		return totalRecycleAmount;
	}

	/** 
	 * 设置 累积回收金额(现金) 
	 * @param totalRecycleAmount 累积回收金额(现金) 
	 */
	public void setTotalRecycleAmount(Double totalRecycleAmount) {
		this.totalRecycleAmount = DoubleUtils.scaleFormat(totalRecycleAmount, 2);
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
	 * @date 2016年12月1日 下午8:28:56
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundMainCash [id=").append(id).append(", userId=")
				.append(userId).append(", balance=").append(balance)
				.append(", freezeAmount=").append(freezeAmount)
				.append(", holdFund=").append(holdFund).append(", deferFund=")
				.append(deferFund).append(", totalCounterFee=")
				.append(totalCounterFee).append(", totalRechargeAmount=")
				.append(totalRechargeAmount).append(", totalPresentAmount=")
				.append(totalPresentAmount).append(", totalManualinAmount=")
				.append(totalManualinAmount).append(", totalManualoutAmount=")
				.append(totalManualoutAmount).append(", totalBenefitAmount=")
				.append(totalBenefitAmount).append(", totalDrawAmount=")
				.append(totalDrawAmount).append(", totalRecycleAmount=")
				.append(totalRecycleAmount).append(", totalInterestAmount=")
				.append(totalInterestAmount).append(", createDate=")
				.append(createDate).append(", modifyDate=").append(modifyDate)
				.append("]");
		return builder.toString();
	}
	
	
}
