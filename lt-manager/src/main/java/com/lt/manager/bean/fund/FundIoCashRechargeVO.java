package com.lt.manager.bean.fund;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 * 用户资金充值明细入参实体
 * @author guodw
 *
 */
public class FundIoCashRechargeVO extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键ID */
	private Long ioId;
	/** 用户ID */
	private String userId;
	/** 昵称 */
	private String nickName;
	/** 姓名 */
	private String userName;
	/** 手机号 */
	private String tele;
	/**	充值金额(用户提交，作参考) */
	private Double amount = 0.00;
	/** 实际到账金额 */
	private Double actualAmount = 0.00;
	/** 实际到账人民币*/
	private Double actualRmbAmount = 0.00;
	/** 备注 */
	private String remark;
	/** 创建时间*/
	private Date createDate;
	/** 到账时间*/
	private Date doneDate;
	/** 外部订单id*/
	private String externalId = "";
	
	/**	转账账户，支付宝或者银行账号 */
	private String transferNumber; 
	/** 状态: 0 未处理, 1 成功, -1失败 */
	private Integer status;
	/** 商户id*/
	private String payId;
	/** 操作人*/
	private Integer modifyUserId;
	 
	/**操作人姓名**/
	private String modifyUserName;
	/** yuanxin 查询条件 beg */
	/** 创建时间(入账开始时间) */
	private String startDate;
	/** 创建时间(入账结束时间) */
	private String endDate;
	/** 一级业务码*/
	private String firstOptcode;
	/** 二级业务码*/
	private String secondOptcode;
	/** 三级业务码*/
	private String thirdOptcode;
	
	
	/*-----------------明细查询-------------------------*/
	/** 到账时间（开始时间）*/
	private String doneStartDate;
	/** 到账时间（截止时间）*/
	private String doneEndDate;
	/** yuanxin 查询条件 end */
	
	/** 开户省*/
	private String openBankCity;
	/** 开户市*/
	private String openBankProv;
	/** 充值方式*/
	private String thirdOptname;
	/** 银行卡号*/
	private String bankCardNum;
	/** 银行名称*/
	private String bankName;
	
	private Double rate ;
	
	/** 人民币金额*/
	private Double rmbAmt;
	/** 支付宝账号*/
	private String alipayNum;
	//品牌 id
	private String brandId;
	/**
	 * @return the ioId
	 */
	public Long getIoId() {
		return ioId;
	}
	/**
	 * @param ioId the ioId to set
	 */
	public void setIoId(Long ioId) {
		this.ioId = ioId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return the transferNumber
	 */
	public String getTransferNumber() {
		return transferNumber;
	}
	/**
	 * @param transferNumber the transferNumber to set
	 */
	public void setTransferNumber(String transferNumber) {
		this.transferNumber = transferNumber;
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
	 * @return the actualAmount
	 */
	public Double getActualAmount() {
		return actualAmount;
	}
	/**
	 * @param actualAmount the actualAmount to set
	 */
	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
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
	 * @return the externalId
	 */
	public String getExternalId() {
		return externalId;
	}
	/**
	 * @param externalId the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
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
	 * @return the modifyUserId
	 */
	public Integer getModifyUserId() {
		return modifyUserId;
	}
	/**
	 * @param modifyUserId the modifyUserId to set
	 */
	public void setModifyUserId(Integer modifyUserId) {
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
	 * @return the openBankCity
	 */
	public String getOpenBankCity() {
		return openBankCity;
	}
	/**
	 * @param openBankCity the openBankCity to set
	 */
	public void setOpenBankCity(String openBankCity) {
		this.openBankCity = openBankCity;
	}
	/**
	 * @return the openBankProv
	 */
	public String getOpenBankProv() {
		return openBankProv;
	}
	/**
	 * @param openBankProv the openBankProv to set
	 */
	public void setOpenBankProv(String openBankProv) {
		this.openBankProv = openBankProv;
	}
	/**
	 * @return the thirdOptname
	 */
	public String getThirdOptname() {
		return thirdOptname;
	}
	/**
	 * @param thirdOptname the thirdOptname to set
	 */
	public void setThirdOptname(String thirdOptname) {
		this.thirdOptname = thirdOptname;
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
	 * @return the firstOpcode
	 */
	public String getFirstOpcode() {
		return firstOptcode;
	}
	/**
	 * @param firstOpcode the firstOpcode to set
	 */
	public void setFirstOpcode(String firstOpcode) {
		this.firstOptcode = firstOpcode;
	}
	/**
	 * @return the secondOptcode
	 */
	public String getSecondOptcode() {
		return secondOptcode;
	}
	/**
	 * @param secondOptcode the secondOptcode to set
	 */
	public void setSecondOptcode(String secondOptcode) {
		this.secondOptcode = secondOptcode;
	}
	/**
	 * @return the thirdOptcode
	 */
	public String getThirdOptcode() {
		return thirdOptcode;
	}
	/**
	 * @param thirdOptcode the thirdOptcode to set
	 */
	public void setThirdOptcode(String thirdOptcode) {
		this.thirdOptcode = thirdOptcode;
	}
	/**
	 * @return the firstOptcode
	 */
	public String getFirstOptcode() {
		return firstOptcode;
	}
	/**
	 * @param firstOptcode the firstOptcode to set
	 */
	public void setFirstOptcode(String firstOptcode) {
		this.firstOptcode = firstOptcode;
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
	 * @return the actualRmbAmount
	 */
	public Double getActualRmbAmount() {
		return actualRmbAmount;
	}
	/**
	 * @param actualRmbAmount the actualRmbAmount to set
	 */
	public void setActualRmbAmount(Double actualRmbAmount) {
		this.actualRmbAmount = actualRmbAmount;
	}
	/**
	 * @return the alipayNum
	 */
	public String getAlipayNum() {
		return alipayNum;
	}
	/**
	 * @param alipayNum the alipayNum to set
	 */
	public void setAlipayNum(String alipayNum) {
		this.alipayNum = alipayNum;
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
