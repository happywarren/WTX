package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：FundAlipayRecharge   
* 类描述：支付宝充值（自动）   
* 创建人：yuanxin   
* 创建时间：2017年7月17日 下午3:01:57      
*/
public class FundAlipayRecharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户id*/
	private String userId ;
	/** 支付宝充值订单id*/
	private String alipayId ;
	/** 请求支付金额*/
	private Double amount ;
	/** 商户号*/
	private String merid ;
	/** 请求报文内容*/
	private String sendMsg ;
	/** 请求发送时间*/
	private Date sendDate;
	/** 支付返回时间 */
	private Date payBackTime ;
	/** 支付结果*/
	private Integer payResult ; 
	/** 支付宝平台订单号*/
	private String orderNo;
	/** 实际入账金额*/
	private Double factAmount ;
	/** 回调返回信息*/
	private String payMsg ;
	
	/**业务码**/
	private String bizCode;
	public FundAlipayRecharge(){
		
	}
	
	/**
	 * 请求支付宝参数
	 * @param orderId 订单id
	 * @param userId 用户id
	 * @param amount 支付金额
	 * @param merid 商户号
	 * @param sendMsg 发送报文
	 */
	public FundAlipayRecharge(String orderId ,String userId,Double amount,String merid,String sendMsg){
		this.alipayId = orderId ;
		this.userId = userId ;
		this.amount = amount ;
		this.merid = merid ;
		this.sendMsg = sendMsg ;
		this.sendDate = new Date();
	}
	
	/**
	 *  支付宝接口回传参数
	 * @param alipayId 订单id
	 * @param payResult 
	 * @param orderNo
	 * @param factAmount
	 * @param payMsg 返回订单报文
	 */
	public FundAlipayRecharge(String alipayId,Integer payResult,String orderNo,Double factAmount,String payMsg,Date payBackTime){
		this.alipayId = alipayId ;
		this.payResult = payResult ;
		this.orderNo = orderNo ;
		this.factAmount = factAmount ;
		this.payMsg = payMsg ;
		this.payBackTime = payBackTime == null ? new Date() : payBackTime;
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
	* 获取alipayId  
	*/
	public String getAlipayId() {
		return alipayId;
	}
	/** 
	* 获取alipayId  
	*/
	public void setAlipayId(String alipayId) {
		this.alipayId = alipayId;
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
	* 获取merid  
	*/
	public String getMerid() {
		return merid;
	}
	/** 
	* 获取merid  
	*/
	public void setMerid(String merid) {
		this.merid = merid;
	}
	/** 
	* 获取sendMsg  
	*/
	public String getSendMsg() {
		return sendMsg;
	}
	/** 
	* 获取sendMsg  
	*/
	public void setSendMsg(String sendMsg) {
		this.sendMsg = sendMsg;
	}
	/** 
	* 获取payResult  
	*/
	public Integer getPayResult() {
		return payResult;
	}
	/** 
	* 获取payResult  
	*/
	public void setPayResult(Integer payResult) {
		this.payResult = payResult;
	}
	/** 
	* 获取orderNo  
	*/
	public String getOrderNo() {
		return orderNo;
	}
	/** 
	* 获取orderNo  
	*/
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/** 
	* 获取factAmount  
	*/
	public Double getFactAmount() {
		return factAmount;
	}
	/** 
	* 获取factAmount  
	*/
	public void setFactAmount(Double factAmount) {
		this.factAmount = factAmount;
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
	* 获取sendDate  
	*/
	public Date getSendDate() {
		return sendDate;
	}

	/** 
	* 获取sendDate  
	*/
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/** 
	* 获取payBackTime  
	*/
	public Date getPayBackTime() {
		return payBackTime;
	}

	/** 
	* 获取payBackTime  
	*/
	public void setPayBackTime(Date payBackTime) {
		this.payBackTime = payBackTime;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	
}
