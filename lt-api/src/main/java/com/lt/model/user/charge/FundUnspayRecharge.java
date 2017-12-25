package com.lt.model.user.charge;

import java.io.Serializable;

/**
 * 项目名称：lt-api 类名称：FundUnspayRecharge 类描述：银生宝充值bean 创建人：yuanxin 创建时间：2017年7月11日
 * 下午6:24:16
 */
public class FundUnspayRecharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id;
	/** 银生宝支付id*/
	private String unspayId;
	/** 银生宝回调链接 */
	private String callbackUrl;
	/** 支付链接*/
	private String payUrl;
	/** 支付金额（人民币）*/
	private Double amount;
	/** 用户id*/
	private String userId;
	/** 用户姓名*/
	private String userName;
	/** 用户证件号*/
	private String idCardNum;
	/** 银行卡号*/
	private String bankCardNum;
	/** 银生宝签名*/
	private String identification;
	/** 返回码*/
	private String resultCode;
	/** 返回信息*/
	private String resultMsg;
	/** 实际支付金额（人民币）*/
	private Double resultAmount;
	/** 原始返回串*/
	private String resultStr;
	/** 
	* 获取id  
	*/
	public Integer getId() {
		return id;
	}
	/** 
	* 获取id  
	*/
	public void setId(Integer id) {
		this.id = id;
	}
	/** 
	* 获取unspayId  
	*/
	public String getUnspayId() {
		return unspayId;
	}
	/** 
	* 获取unspayId  
	*/
	public void setUnspayId(String unspayId) {
		this.unspayId = unspayId;
	}
	/** 
	* 获取callbackUrl  
	*/
	public String getCallbackUrl() {
		return callbackUrl;
	}
	/** 
	* 获取callbackUrl  
	*/
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	/** 
	* 获取payUrl  
	*/
	public String getPayUrl() {
		return payUrl;
	}
	/** 
	* 获取payUrl  
	*/
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	/** 
	* 获取amount  
	*/
	public Double getAmount() {
		return amount;
	}
	/** 
	* 获取amount  
	*/
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/** 
	* 获取userId  
	*/
	public String getUserId() {
		return userId;
	}
	/** 
	* 获取userId  
	*/
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/** 
	* 获取userName  
	*/
	public String getUserName() {
		return userName;
	}
	/** 
	* 获取userName  
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/** 
	* 获取idCardNum  
	*/
	public String getIdCardNum() {
		return idCardNum;
	}
	/** 
	* 获取idCardNum  
	*/
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	/** 
	* 获取bankCardNum  
	*/
	public String getBankCardNum() {
		return bankCardNum;
	}
	/** 
	* 获取bankCardNum  
	*/
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}
	/** 
	* 获取identification  
	*/
	public String getIdentification() {
		return identification;
	}
	/** 
	* 获取identification  
	*/
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	/** 
	* 获取resultCode  
	*/
	public String getResultCode() {
		return resultCode;
	}
	/** 
	* 获取resultCode  
	*/
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/** 
	* 获取resultMsg  
	*/
	public String getResultMsg() {
		return resultMsg;
	}
	/** 
	* 获取resultMsg  
	*/
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	/** 
	* 获取resultAmount  
	*/
	public Double getResultAmount() {
		return resultAmount;
	}
	/** 
	* 获取resultAmount  
	*/
	public void setResultAmount(Double resultAmount) {
		this.resultAmount = resultAmount;
	}
	/** 
	* 获取resultStr  
	*/
	public String getResultStr() {
		return resultStr;
	}
	/** 
	* 获取resultStr  
	*/
	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}
	
}
