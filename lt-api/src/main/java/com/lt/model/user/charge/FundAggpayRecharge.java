package com.lt.model.user.charge;

import java.util.Date;

public class FundAggpayRecharge {
	private Integer id;

	/** 用户id **/
	private String userId;
	/** 交易类型 **/
	private String tradeType;

	/**
	 * 支付类型:0203-微信扫码支付 0204-支付宝扫码支付 0205-微信app支付 0207-微信公众号支付
	 **/
	private String payType;
	/** 商户编号 **/
	private String merchantId;
	/** 公众号appid **/
	private String appId;
	/** 微信openid **/
	private String openId;
	/** 订单号 **/
	private String orderId;
	/** 商品名称 **/
	private String subject;
	/** 终端IP **/
	private String machineIp;
	/** 通知地址 **/
	private String notifyUrl;
	/** 返回地址 **/
	private String returnUrl;
	/** 订单提交时间 **/
	private String submitTime;
	/** 信用卡限制使用标识 **/
	private String creditLimit;
	/** 交易金额 **/
	private String amt;
	/** 交易附言 **/
	private String summary;
	/** 数据签名 **/
	private String sign;

	private Date createStamp;

	private Date modtifyStamp;

	private String platSerial;

	private String retStatus;

	private String retCode;

	private String retMsg;

	private String retDesc;

	private String codeUrl;

	private String codeImgUrl;

	private String payUrl;

	private String payCode;
	/**业务代码**/
	private String bizCode;

	/**0已支付，1未支付，其他失败**/
	private String dealStatus;

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
		this.userId = userId == null ? null : userId.trim();
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType == null ? null : tradeType.trim();
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType == null ? null : payType.trim();
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId == null ? null : merchantId.trim();
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId == null ? null : appId.trim();
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId == null ? null : openId.trim();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject == null ? null : subject.trim();
	}

	public String getMachineIp() {
		return machineIp;
	}

	public void setMachineIp(String machineIp) {
		this.machineIp = machineIp == null ? null : machineIp.trim();
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl == null ? null : returnUrl.trim();
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime == null ? null : submitTime.trim();
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit == null ? null : creditLimit.trim();
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt == null ? null : amt.trim();
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary == null ? null : summary.trim();
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign == null ? null : sign.trim();
	}

	public Date getCreateStamp() {
		return createStamp;
	}

	public void setCreateStamp(Date createStamp) {
		this.createStamp = createStamp;
	}

	public Date getModtifyStamp() {
		return modtifyStamp;
	}

	public void setModtifyStamp(Date modtifyStamp) {
		this.modtifyStamp = modtifyStamp;
	}

	public String getPlatSerial() {
		return platSerial;
	}

	public void setPlatSerial(String platSerial) {
		this.platSerial = platSerial == null ? null : platSerial.trim();
	}

	public String getRetStatus() {
		return retStatus;
	}

	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus == null ? null : retStatus.trim();
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode == null ? null : retCode.trim();
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg == null ? null : retMsg.trim();
	}

	public String getRetDesc() {
		return retDesc;
	}

	public void setRetDesc(String retDesc) {
		this.retDesc = retDesc == null ? null : retDesc.trim();
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl == null ? null : codeUrl.trim();
	}

	public String getCodeImgUrl() {
		return codeImgUrl;
	}

	public void setCodeImgUrl(String codeImgUrl) {
		this.codeImgUrl = codeImgUrl == null ? null : codeImgUrl.trim();
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl == null ? null : payUrl.trim();
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode == null ? null : payCode.trim();
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode == null ? null : bizCode.trim();
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus == null ? null : dealStatus.trim();
	}
}