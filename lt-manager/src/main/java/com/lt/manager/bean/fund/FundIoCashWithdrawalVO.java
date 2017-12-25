package com.lt.manager.bean.fund;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 * 用户资金提现明细实体
 * @author guodw
 *
 */
public class FundIoCashWithdrawalVO extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** yuanxin 返回数据beg */
	/**	提现金额(用户提交，作参考) */
	private Double amount = 0.00;
	/** 余额 */
	private Double balance = 0.00;
	/** 手续费 */
	private Double tax = 2.00;
	
	private Double factTax = 0.0;
	
	/** 手续费 */
	private Double rmbTax = 2.00;
	
	private Double rmbFactTax = 0.0;
	/** 状态: 0 待审核, 1 待转账, 2 提现拒绝, 3 转账中, 4 转账失败, 5 转账成功, 6 提现撤销 */
	private Integer status ;
	/** 创建时间 */
	private Date createDate;
	/** 后台支付id（回调通知）*/
	private String payId;
	/** 银行卡号*/
	private String bankCardNum;
	/** 备注*/
	private String remark;
	/** 到账时间*/
	private Date doneDate;
	/** yuanxin 返回数据end */
	
	/** 流水ID*/
	private Integer flowId;
	/** 用户昵称*/
	private String nickName;
	/** 用户id*/
	private String userId;
	/** 开户银行*/
	private String bankName;
	/** 操作员id*/
	private String modifyUserId;
	/**操作员姓名**/
	private String modifyUserName;
	/** yuanxin 查询条件beg */
	/** 用户手机号*/
	private String tele;
	/** 用户姓名*/
	private String userName;
	/** 申请开始时间*/
	private String startDate;
	/** 申请结束时间*/
	private String endDate;
	/** 到账日期开始时间 */
	private String doneStartDate;
	/** 到账日期结束时间*/
	private String doneEndDate;
	/** 银行编号*/
	private String bankCode;
	/** 查询多个状态订单*/
	private String mutilStatus;
	/** yuanxin 查询条件end */
	
	/*------------------------详细内容---------------------------*/
	/** 总的提现金额 */
	private Double drawTotalAmt;
	/** 总的提现手续费 */
	private Double taxTotal;
	/** 用户身份证号 */
	private String idCardNum;
	/** 利率*/
	private Double rate;
	/** 人民币值*/
	private Double rmbAmt;
	//品牌 id
	private String brandId;
	
	public FundIoCashWithdrawalVO(){
		
	}

	/**
	 * @param amount
	 * @param balance
	 * @param tax
	 * @param factTax
	 * @param status
	 * @param createDate
	 * @param payId
	 * @param bankCardNum
	 * @param remark
	 * @param doneDate
	 * @param flowId
	 * @param nickName
	 * @param userId
	 * @param bankName
	 * @param modifyUserId
	 * @param tele
	 * @param userName
	 * @param startDate
	 * @param endDate
	 * @param doneStartDate
	 * @param doneEndDate
	 * @param bankCode
	 * @param mutilStatus
	 * @param drawTotalAmt
	 * @param taxTotal
	 * @param idCardNum
	 * @param rate
	 * @param rmbAmt
	 * @param brandId
	 */
	public FundIoCashWithdrawalVO(Double amount, Double balance, Double tax, Double factTax, Integer status,
			Date createDate, String payId, String bankCardNum, String remark, Date doneDate, Integer flowId,
			String nickName, String userId, String bankName, String modifyUserId, String tele, String userName,
			String startDate, String endDate, String doneStartDate, String doneEndDate, String bankCode,
			String mutilStatus, Double drawTotalAmt, Double taxTotal, String idCardNum, Double rate, Double rmbAmt,String brandId) {
		super();
		this.amount = amount;
		this.balance = balance;
		this.tax = tax;
		this.factTax = factTax;
		this.status = status;
		this.createDate = createDate;
		this.payId = payId;
		this.bankCardNum = bankCardNum;
		this.remark = remark;
		this.doneDate = doneDate;
		this.flowId = flowId;
		this.nickName = nickName;
		this.userId = userId;
		this.bankName = bankName;
		this.modifyUserId = modifyUserId;
		this.tele = tele;
		this.userName = userName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.doneStartDate = doneStartDate;
		this.doneEndDate = doneEndDate;
		this.bankCode = bankCode;
		this.mutilStatus = mutilStatus;
		this.drawTotalAmt = drawTotalAmt;
		this.taxTotal = taxTotal;
		this.idCardNum = idCardNum;
		this.rate = rate;
		this.rmbAmt = rmbAmt;
		this.brandId = brandId;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	/**
	 * @return the tax
	 */
	public Double getTax() {
		return tax;
	}
	/**
	 * @param tax the tax to set
	 */
	public void setTax(Double tax) {
		this.tax = tax;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the flowId
	 */
	public Integer getFlowId() {
		return flowId;
	}
	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the tele
	 */
	public String getTele() {
		return tele;
	}
	/**
	 * @param tele the tele to set
	 */
	public void setTele(String tele) {
		this.tele = tele;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the modifyUserId
	 */
	public String getModifyUserId() {
		return modifyUserId;
	}
	/**
	 * @param modifyUserId the modifyUserId to set
	 */
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	/**
	 * @return the doneStartDate
	 */
	public String getDoneStartDate() {
		return doneStartDate;
	}
	/**
	 * @param doneStartDate the doneStartDate to set
	 */
	public void setDoneStartDate(String doneStartDate) {
		this.doneStartDate = doneStartDate;
	}
	/**
	 * @return the doneEndDate
	 */
	public String getDoneEndDate() {
		return doneEndDate;
	}
	/**
	 * @param doneEndDate the doneEndDate to set
	 */
	public void setDoneEndDate(String doneEndDate) {
		this.doneEndDate = doneEndDate;
	}
	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * @return the mutilStatus
	 */
	public String getMutilStatus() {
		return mutilStatus;
	}
	/**
	 * @param mutilStatus the mutilStatus to set
	 */
	public void setMutilStatus(String mutilStatus) {
		this.mutilStatus = mutilStatus;
	}
	/**
	 * @return the payId
	 */
	public String getPayId() {
		return payId;
	}
	/**
	 * @param payId the payId to set
	 */
	public void setPayId(String payId) {
		this.payId = payId;
	}
	/**
	 * @return the bankCardNum
	 */
	public String getBankCardNum() {
		return bankCardNum;
	}
	/**
	 * @param bankCardNum the bankCardNum to set
	 */
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the drawTotalAmt
	 */
	public Double getDrawTotalAmt() {
		return drawTotalAmt;
	}
	/**
	 * @param drawTotalAmt the drawTotalAmt to set
	 */
	public void setDrawTotalAmt(Double drawTotalAmt) {
		this.drawTotalAmt = drawTotalAmt;
	}
	/**
	 * @return the taxTotal
	 */
	public Double getTaxTotal() {
		return taxTotal;
	}
	/**
	 * @param taxTotal the taxTotal to set
	 */
	public void setTaxTotal(Double taxTotal) {
		this.taxTotal = taxTotal;
	}
	/**
	 * @return the idCardNum
	 */
	public String getIdCardNum() {
		return idCardNum;
	}
	/**
	 * @param idCardNum the idCardNum to set
	 */
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	/**
	 * @return the doneDate
	 */
	public Date getDoneDate() {
		return doneDate;
	}
	/**
	 * @param doneDate the doneDate to set
	 */
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}
	/**
	 * @return the factTax
	 */
	public Double getFactTax() {
		return factTax;
	}
	/**
	 * @param factTax the factTax to set
	 */
	public void setFactTax(Double factTax) {
		this.factTax = factTax;
	}
	/**
	 * @return the rate
	 */
	public Double getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}
	/**
	 * @return the rmbAmt
	 */
	public Double getRmbAmt() {
		return rmbAmt;
	}
	/**
	 * @param rmbAmt the rmbAmt to set
	 */
	public void setRmbAmt(Double rmbAmt) {
		this.rmbAmt = rmbAmt;
	}

	/**
	 * @return the rmbTax
	 */
	public Double getRmbTax() {
		return rmbTax;
	}

	/**
	 * @param rmbTax the rmbTax to set
	 */
	public void setRmbTax(Double rmbTax) {
		this.rmbTax = rmbTax;
	}

	/**
	 * @return the rmbFactTax
	 */
	public Double getRmbFactTax() {
		return rmbFactTax;
	}

	/**
	 * @param rmbFactTax the rmbFactTax to set
	 */
	public void setRmbFactTax(Double rmbFactTax) {
		this.rmbFactTax = rmbFactTax;
	}

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}


	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
