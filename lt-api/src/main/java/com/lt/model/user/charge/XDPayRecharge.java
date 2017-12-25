package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

/**
 * 熙大支付宝充值
 * 
 * @date 2017年8月1日
 * @author yubei
 *
 */
public class XDPayRecharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键**/
	private Integer id;
	/**用户id**/
	private String userId;
	/**商户号**/
	private String merchantNo;
	/**商户订单号**/
	private String orderNo;
	/**平台订单号**/
	private String hostOrderNo;
	/**实际交易金额**/
	private Double transAmount;
	/**支付业务类型**/
	private String orderType;
	/**商品名称**/
	private String goods;
	/**商户保留信息**/
	private String reserver;
	/**异步通知地址**/
	private String bgUrl;
	/**签名**/
	private String sign;
	/**订单金额**/
	private Double amount;
	/**额外信息**/
	private String extraData;
	/**通知类型**/
	private String notifyType;
	/**交易状态
	SUCCESS 交易成功
	FAIL 交易失败**/
	private String status;
	/**错误码**/
	private String errCode;
	/**错误信息**/
	private String errMsg;
	/**创建时间戳**/
	private Date createStamp;
	/**更新时间戳**/
	private Date modifyStamp;
	
	/**订单时间**/
	private Date orderTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Double getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(Double transAmount) {
		this.transAmount = transAmount;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getReserver() {
		return reserver;
	}
	public void setReserver(String reserver) {
		this.reserver = reserver;
	}
	public String getBgUrl() {
		return bgUrl;
	}
	public void setBgUrl(String bgUrl) {
		this.bgUrl = bgUrl;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getExtraData() {
		return extraData;
	}
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public Date getCreateStamp() {
		return createStamp;
	}
	public void setCreateStamp(Date createStamp) {
		this.createStamp = createStamp;
	}
	public Date getModifyStamp() {
		return modifyStamp;
	}
	public void setModifyStamp(Date modifyStamp) {
		this.modifyStamp = modifyStamp;
	}
	public String getHostOrderNo() {
		return hostOrderNo;
	}
	public void setHostOrderNo(String hostOrderNo) {
		this.hostOrderNo = hostOrderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public XDPayRecharge(){
		
	}
	
	public XDPayRecharge(String userId, String merchantNo, String orderNo, String orderType, String bgUrl, String sign, Double amount) {
		super();
		this.userId = userId;
		this.merchantNo = merchantNo;
		this.orderNo = orderNo;
		this.orderType = orderType;
		this.bgUrl = bgUrl;
		this.sign = sign;
		this.amount = amount;
	}
	
}
