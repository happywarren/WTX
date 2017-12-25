package com.lt.model.user.charge;

import java.util.Date;

/**
 * 智付额外信息
 * 
 * @author yubei
 *
 */
public class DinpayExtraInfo {
	/**id主键**/
    private Integer id;
    /**商家订单号**/
    private String orderNo;
    /**商家订单时间**/
    private Date orderTime;
    /**商家订单金额**/
    private Double orderAmount;
    /**智付订单号**/
    private String tradeNo;
    /**智付订单时间**/
    private Date tradeTime;
    /**交易状态[SUCCESS:交易成功;UNPAY:未支付]**/
    private String tradeStatus;
    /**额外数据**/
    private String extraData;
    /**错误码**/
    private String errorCode;
    /**错误描述**/
    private String errorDesc;
    /**请求信息**/
    private String reqMessage;
    /**响应信息**/
    private String respMessage;
    /**保留字段**/
    private String reserve;
    /**备注**/
    private String remark;
    /**创建时间戳**/
    private Date createStatmp;
    /**最后一次更新时间戳**/
    private Date lastUpdaeStamp;
    
    
    public DinpayExtraInfo() {
		super();
	}

	public DinpayExtraInfo(String orderNo, Date createStatmp, Date lastUpdaeStamp,Double orderAmount, String extraData,String remark) {
		super();
		this.orderNo = orderNo;
		this.createStatmp = createStatmp;
		this.lastUpdaeStamp = lastUpdaeStamp;
		this.orderAmount = orderAmount;
		this.extraData = extraData;
		this.remark = remark;
	}

	  public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public Date getCreateStatmp() {
	        return createStatmp;
	    }

	    public void setCreateStatmp(Date createStatmp) {
	        this.createStatmp = createStatmp;
	    }

	    public Date getLastUpdaeStamp() {
	        return lastUpdaeStamp;
	    }

	    public void setLastUpdaeStamp(Date lastUpdaeStamp) {
	        this.lastUpdaeStamp = lastUpdaeStamp;
	    }

	    public String getOrderNo() {
	        return orderNo;
	    }

	    public void setOrderNo(String orderNo) {
	        this.orderNo = orderNo == null ? null : orderNo.trim();
	    }

	    public Date getOrderTime() {
	        return orderTime;
	    }

	    public void setOrderTime(Date orderTime) {
	        this.orderTime = orderTime;
	    }

	    public Double getOrderAmount() {
	        return orderAmount;
	    }

	    public void setOrderAmount(Double orderAmount) {
	        this.orderAmount = orderAmount;
	    }

	    public String getTradeNo() {
	        return tradeNo;
	    }

	    public void setTradeNo(String tradeNo) {
	        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
	    }

	    public Date getTradeTime() {
	        return tradeTime;
	    }

	    public void setTradeTime(Date tradeTime) {
	        this.tradeTime = tradeTime;
	    }

	    public String getTradeStatus() {
	        return tradeStatus;
	    }

	    public void setTradeStatus(String tradeStatus) {
	        this.tradeStatus = tradeStatus == null ? null : tradeStatus.trim();
	    }

	    public String getExtraData() {
	        return extraData;
	    }

	    public void setExtraData(String extraData) {
	        this.extraData = extraData == null ? null : extraData.trim();
	    }

	    public String getErrorCode() {
	        return errorCode;
	    }

	    public void setErrorCode(String errorCode) {
	        this.errorCode = errorCode == null ? null : errorCode.trim();
	    }

	    public String getErrorDesc() {
	        return errorDesc;
	    }

	    public void setErrorDesc(String errorDesc) {
	        this.errorDesc = errorDesc == null ? null : errorDesc.trim();
	    }

	    public String getReqMessage() {
	        return reqMessage;
	    }

	    public void setReqMessage(String reqMessage) {
	        this.reqMessage = reqMessage == null ? null : reqMessage.trim();
	    }

	    public String getRespMessage() {
	        return respMessage;
	    }

	    public void setRespMessage(String respMessage) {
	        this.respMessage = respMessage == null ? null : respMessage.trim();
	    }

	    public String getReserve() {
	        return reserve;
	    }

	    public void setReserve(String reserve) {
	        this.reserve = reserve == null ? null : reserve.trim();
	    }

	    public String getRemark() {
	        return remark;
	    }

	    public void setRemark(String remark) {
	        this.remark = remark == null ? null : remark.trim();
	    }
    
}