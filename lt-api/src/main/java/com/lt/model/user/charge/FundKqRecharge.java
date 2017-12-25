package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.CalendarTools;

/**
 * 项目名称：lt-api 类名称：FundKqRecharge 类描述：快钱充值记录 创建人：yuanxin 创建时间：2017年7月11日
 * 上午10:54:20
 */
public class FundKqRecharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Integer id;
	/** 快钱支付id*/
	private String kqOrderId;
	/** 用户id*/
	private String userId;
	/** 商户号*/
	private String merchantId;
	/** 用户姓名*/
	private String userName;
	/** 身份证号*/
	private String idCardNum;
	/** 银行卡号*/
	private String bankCardNum;
	/** 金额（人民币）*/
	private Double amount;
	/** 验证码发送时间*/
	private Date validTime;
	/** 验证码报文*/
	private String validPacket;
	
	/**验证码返回码*/
	private String validResponseCode;
	/** 验证码反馈时间*/
	private Date validResponseTime;
	/** 验证码反馈消息*/
	private String validResponseMsg;
	/** 验证码反馈token*/
	private String validResponseToken;
	/** 验证码获取返回报文*/
	private String validResponsePacket;
	
	
	/** 发送支付请求时间*/
	private Date reqPayTime;
	/** 请求回调地址*/
	private String reqCallbackUrl;
	/** 验证码*/
	private String reqValidCode;
	/** 支付请求报文*/
	private String reqPacket;
	
	/** 支付回调时间*/
	private Date payDoneTime;
	/** 实际支付金额 （人民币）*/
	private Double payAmount ;
	/** 支付返回码*/
	private String payCode;
	/** 支付返回信息*/
	private String payMsg;
	/** 支付返回报文*/
	private String payPacket ;
	/** 检索参考号*/
	private String refNumber;
	
	public FundKqRecharge(){
		
	}
	
	/**
	 * 发送验证码步骤记录
	 * @param userId 用户id
	 * @param merchantId 商户id
	 * @param userName 用户姓名
	 * @param idCardNum 用户证件号
	 * @param bankCardNum 银行卡号
	 * @param amount 金额（人民币）
	 * @param validPacket 验证码请求报文
	 */
	public FundKqRecharge(String userId,String merchantId,String userName,String idCardNum,String bankCardNum,Double amount,String validPacket){
		this.kqOrderId =  "kq"+String.valueOf(CalendarTools.getMillis(new Date()));
		this.userId = userId;
		this.merchantId = merchantId ;
		this.userName = userName ;
		this.idCardNum = idCardNum ;
		this.bankCardNum = bankCardNum ;
		this.amount = amount ;
		this.validTime = new Date();
		this.validPacket = validPacket ;
	}
	
	/**
	 * 接收快钱验证码返回报文
	 * @param kqOrderId 快钱订单id
	 * @param validResponseCode 验证码返回码
	 * @param validResponseMsg 验证码返回信息
	 * @param validResponsePacket 验证码返回报文
	 * @param validResponseToken 验证码返回toekn
	 */
	public FundKqRecharge(String kqOrderId,String validResponseCode,String validResponseMsg,String validResponsePacket,String validResponseToken){
		this.kqOrderId = kqOrderId ;
		this.validResponseCode = validResponseCode;
		this.validResponseTime = new Date() ;
		this.validResponseMsg = validResponseMsg ;
		this.validResponsePacket = validResponsePacket ;
		this.validResponseToken = validResponseToken ;
	}
	
	/**
	 * 快钱支付请求报文内容
	 * @param kqOrderId快钱订单
	 * @param reqPayTime 支付报文请求时间
	 * @param reqCallbackUrl 支付回调地址
	 * @param reqValidCode 验证码
	 * @param reqPacket 支付请求报文
	 */
	public FundKqRecharge(String kqOrderId ,String reqCallbackUrl,String reqValidCode,String reqPacket){
		this.kqOrderId = kqOrderId ; 
		this.reqPayTime = new Date();
		this.reqCallbackUrl = reqCallbackUrl ;
		this.reqValidCode = reqValidCode ;
		this.reqPacket = reqPacket ;
	}
	
	/**
	 * 快钱支付返回信息
	 * @param kqOrderId快钱订单id
	 * @param payAmount 实际支付金额（人民币）
	 * @param payCode 回调返回码
	 * @param payMsg 回调返回信息
	 * @param payPacket 回调报文
	 * @param refNumber 检索参考号
	 */
	public FundKqRecharge(String kqOrderId,Double payAmount,String payCode,String payMsg,String payPacket,String refNumber ){
		this.kqOrderId = kqOrderId ;
		this.payDoneTime = new Date() ;
		this.payCode = payCode ;
		this.payAmount = payAmount ;
		this.payMsg = payMsg ;
		this.payPacket = payPacket ;
		this.refNumber = refNumber ;
	}
	
	
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
	* 获取kqOrderId  
	*/
	public String getKqOrderId() {
		return kqOrderId;
	}
	/** 
	* 获取kqOrderId  
	*/
	public void setKqOrderId(String kqOrderId) {
		this.kqOrderId = kqOrderId;
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
	* 获取merchantId  
	*/
	public String getMerchantId() {
		return merchantId;
	}
	/** 
	* 获取merchantId  
	*/
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
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
	* 获取validTime  
	*/
	public Date getValidTime() {
		return validTime;
	}
	/** 
	* 获取validTime  
	*/
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}
	/** 
	* 获取validResponseTime  
	*/
	public Date getValidResponseTime() {
		return validResponseTime;
	}
	/** 
	* 获取validResponseTime  
	*/
	public void setValidResponseTime(Date validResponseTime) {
		this.validResponseTime = validResponseTime;
	}
	/** 
	* 获取validResponseMsg  
	*/
	public String getValidResponseMsg() {
		return validResponseMsg;
	}
	/** 
	* 获取validResponseMsg  
	*/
	public void setValidResponseMsg(String validResponseMsg) {
		this.validResponseMsg = validResponseMsg;
	}
	/** 
	* 获取reqPayTime  
	*/
	public Date getReqPayTime() {
		return reqPayTime;
	}
	/** 
	* 获取reqPayTime  
	*/
	public void setReqPayTime(Date reqPayTime) {
		this.reqPayTime = reqPayTime;
	}
	/** 
	* 获取reqCallbackUrl  
	*/
	public String getReqCallbackUrl() {
		return reqCallbackUrl;
	}
	/** 
	* 获取reqCallbackUrl  
	*/
	public void setReqCallbackUrl(String reqCallbackUrl) {
		this.reqCallbackUrl = reqCallbackUrl;
	}
	/** 
	* 获取reqValidCode  
	*/
	public String getReqValidCode() {
		return reqValidCode;
	}
	/** 
	* 获取reqValidCode  
	*/
	public void setReqValidCode(String reqValidCode) {
		this.reqValidCode = reqValidCode;
	}
	/** 
	* 获取payDoneTime  
	*/
	public Date getPayDoneTime() {
		return payDoneTime;
	}
	/** 
	* 获取payDoneTime  
	*/
	public void setPayDoneTime(Date payDoneTime) {
		this.payDoneTime = payDoneTime;
	}
	/** 
	* 获取payCode  
	*/
	public String getPayCode() {
		return payCode;
	}
	/** 
	* 获取payCode  
	*/
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	/** 
	* 获取payMsg  
	*/
	public String getPayMsg() {
		return payMsg;
	}
	/** 
	* 获取payMsg  
	*/
	public void setPayMsg(String payMsg) {
		this.payMsg = payMsg;
	}
	/** 
	* 获取refNumber  
	*/
	public String getRefNumber() {
		return refNumber;
	}
	/** 
	* 获取refNumber  
	*/
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	/** 
	* 获取payAmount  
	*/
	public Double getPayAmount() {
		return payAmount;
	}
	/** 
	* 获取payAmount  
	*/
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	/** 
	* 获取validPacket  
	*/
	public String getValidPacket() {
		return validPacket;
	}

	/** 
	* 获取validPacket  
	*/
	public void setValidPacket(String validPacket) {
		this.validPacket = validPacket;
	}

	/** 
	* 获取validResponseCode  
	*/
	public String getValidResponseCode() {
		return validResponseCode;
	}

	/** 
	* 获取validResponseCode  
	*/
	public void setValidResponseCode(String validResponseCode) {
		this.validResponseCode = validResponseCode;
	}

	/** 
	* 获取validResponsePacket  
	*/
	public String getValidResponsePacket() {
		return validResponsePacket;
	}

	/** 
	* 获取validResponsePacket  
	*/
	public void setValidResponsePacket(String validResponsePacket) {
		this.validResponsePacket = validResponsePacket;
	}

	/** 
	* 获取reqPacket  
	*/
	public String getReqPacket() {
		return reqPacket;
	}

	/** 
	* 获取reqPacket  
	*/
	public void setReqPacket(String reqPacket) {
		this.reqPacket = reqPacket;
	}

	/** 
	* 获取payPacket  
	*/
	public String getPayPacket() {
		return payPacket;
	}

	/** 
	* 获取payPacket  
	*/
	public void setPayPacket(String payPacket) {
		this.payPacket = payPacket;
	}

	/** 
	* 获取validResponseToken  
	*/
	public String getValidResponseToken() {
		return validResponseToken;
	}

	/** 
	* 获取validResponseToken  
	*/
	public void setValidResponseToken(String validResponseToken) {
		this.validResponseToken = validResponseToken;
	}
	
}
