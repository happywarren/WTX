package com.lt.manager.bean.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lt.manager.bean.BaseBean;
import com.lt.model.user.charge.BankChargeMapper;

/**   
* 项目名称：lt-manager 
* 类名称：BankVo   
* 类描述：银行信息
* 创建人：yubei   
* 创建时间：2017年6月13日 下午3:19:19      
*/
public class BankVo  extends BaseBean implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 银行编号*/
	private String bankCode ;
	/** 渠道id*/
	private String channelId;
	/***渠道名称*/
	private String channelName;
	/** 单词入金限额*/
	private Double singleLimit ;
	/** 单日入金限额*/
	private Double dailyLimit;
	/** 创建时间*/
	private Date createDate;
	/**修改时间**/
	private Date updateDate;
	/** 银行名称*/
	private String bankName;
	/** 银行图案地址*/
	private String bankPic;
	/*** 银行卡背景色*/
	private String cardBackground;	
	
	/** 优先级*/	
	private Integer priority;
	/** 是否默认开通 0 否 1 是*/
	private Integer isDefault;
	/** 是否开启该渠道 0 否 1是*/
	private Integer isStart;
	/** 渠道总充值金额（汇总数据）*/
	private Double totalAmount;
	/**	保留 **/
	private Map<String, Object> reserve;
	private Map<String, Object> alipay;
	private Map<String, Object> dinpay;
	private Map<String, Object> unspay;
	private Map<String, Object> daddyPay;
	private Map<String, Object> kqpay;
	private Map<String, Object> xdpay;/**熙大支付宝**/
	private Map<String, Object> handalipay;
	/**兴联支付宝**/
	private Map<String, Object> xlpay1;
	private Map<String, Object> xlpay2;
	private Map<String, Object> xlpay3;
	private Map<String, Object> xlpay4;
	private Map<String, Object> xlpay5;
	private Map<String, Object> xlpay6;
	private Map<String, Object> xlpay7;
	private String resver1;
	private List<BankChargeMapper> limitInfo;
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
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the singleLimit
	 */
	public Double getSingleLimit() {
		return singleLimit;
	}
	/**
	 * @param singleLimit the singleLimit to set
	 */
	public void setSingleLimit(Double singleLimit) {
		this.singleLimit = singleLimit;
	}
	/**
	 * @return the dailyLimit
	 */
	public Double getDailyLimit() {
		return dailyLimit;
	}
	/**
	 * @param dailyLimit the dailyLimit to set
	 */
	public void setDailyLimit(Double dailyLimit) {
		this.dailyLimit = dailyLimit;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankPic() {
		return bankPic;
	}
	public void setBankPic(String bankPic) {
		this.bankPic = bankPic;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCardBackground() {
		return cardBackground;
	}
	public void setCardBackground(String cardBackground) {
		this.cardBackground = cardBackground;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public Integer getIsStart() {
		return isStart;
	}
	public void setIsStart(Integer isStart) {
		this.isStart = isStart;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Map<String, Object> getReserve() {
		return reserve;
	}
	public void setReserve(Map<String, Object> reserve) {
		this.reserve = reserve;
	}
	public Map<String, Object> getAlipay() {
		return alipay;
	}
	public void setAlipay(Map<String, Object> alipay) {
		this.alipay = alipay;
	}
	public Map<String, Object> getDinpay() {
		return dinpay;
	}
	public void setDinpay(Map<String, Object> dinpay) {
		this.dinpay = dinpay;
	}
	public Map<String, Object> getUnspay() {
		return unspay;
	}
	public void setUnspay(Map<String, Object> unspay) {
		this.unspay = unspay;
	}
	public Map<String, Object> getKqpay() {
		return kqpay;
	}
	public void setKqpay(Map<String, Object> kqpay) {
		this.kqpay = kqpay;
	}
	public String getResver1() {
		return resver1;
	}
	public void setResver1(String resver1) {
		this.resver1 = resver1;
	}
	public Map<String, Object> getXdpay() {
		return xdpay;
	}
	public void setXdpay(Map<String, Object> xdpay) {
		this.xdpay = xdpay;
	}
	public Map<String, Object> getHandalipay() {
		return handalipay;
	}
	public void setHandalipay(Map<String, Object> handalipay) {
		this.handalipay = handalipay;
	}
	public Map<String, Object> getXlpay1() {
		return xlpay1;
	}
	public void setXlpay1(Map<String, Object> xlpay1) {
		this.xlpay1 = xlpay1;
	}
	public Map<String, Object> getXlpay2() {
		return xlpay2;
	}
	public void setXlpay2(Map<String, Object> xlpay2) {
		this.xlpay2 = xlpay2;
	}
	public Map<String, Object> getXlpay3() {
		return xlpay3;
	}
	public void setXlpay3(Map<String, Object> xlpay3) {
		this.xlpay3 = xlpay3;
	}
	public Map<String, Object> getXlpay4() {
		return xlpay4;
	}
	public void setXlpay4(Map<String, Object> xlpay4) {
		this.xlpay4 = xlpay4;
	}
	public Map<String, Object> getXlpay5() {
		return xlpay5;
	}
	public void setXlpay5(Map<String, Object> xlpay5) {
		this.xlpay5 = xlpay5;
	}
	public Map<String, Object> getXlpay6() {
		return xlpay6;
	}
	public void setXlpay6(Map<String, Object> xlpay6) {
		this.xlpay6 = xlpay6;
	}
	public Map<String, Object> getXlpay7() {
		return xlpay7;
	}
	public void setXlpay7(Map<String, Object> xlpay7) {
		this.xlpay7 = xlpay7;
	}

	public Map<String, Object> getDaddyPay() {
		return daddyPay;
	}

	public void setDaddyPay(Map<String, Object> daddyPay) {
		this.daddyPay = daddyPay;
	}
	public List<BankChargeMapper> getLimitInfo() {
		return limitInfo;
	}
	public void setLimitInfo(List<BankChargeMapper> limitInfo) {
		this.limitInfo = limitInfo;
	}
	
}
