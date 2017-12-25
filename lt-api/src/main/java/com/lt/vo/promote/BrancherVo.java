package com.lt.vo.promote;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DateTools;

/**
 * 下线信息，用于消息传递
 * @author jingwb
 *
 */
public class BrancherVo implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 下线id
	 */
	private String userId;
	/**注册数*/
	private boolean register;
	/**交易手数*/
	private Integer tradeHandCount;
	/**交易金额*/
	private Double tradeAmount;
	/**充值金额*/
	private Double rechargeAmount;
	/**时间戳*/
	private String dateTime;
	/**订单id*/
	private String orderId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isRegister() {
		return register;
	}
	public void setRegister(boolean register) {
		this.register = register;
	}
	public Integer getTradeHandCount() {
		return tradeHandCount;
	}
	public void setTradeHandCount(Integer tradeHandCount) {
		this.tradeHandCount = tradeHandCount;
	}
	public Double getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		Date dd = new Date(Long.valueOf(dateTime));
		this.dateTime = DateTools.parseToTradeTimeStamp1(dd);
	}
	public Double getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
