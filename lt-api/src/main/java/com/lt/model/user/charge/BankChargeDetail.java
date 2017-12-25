package com.lt.model.user.charge;

import java.io.Serializable;
import java.util.Date;

import com.lt.util.utils.DateTools;

/**   
* 项目名称：lt-api   
* 类名称：BankChargeDetail   
* 类描述：银行卡充值明细 
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午2:55:38      
*/
public class BankChargeDetail implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键id*/
	private Integer id ;
	/** 用户id*/
	private String userId ;
	/** 渠道id*/
	private String channelId;
	/** 银行编号*/
	private String bankCode ;
	/** 银行卡号id*/
	private String bankCardNum;
	/** 金额（累计）*/
	private Double amount ;
	/** 创建时间*/
	private String createTime ;
	/** 修改时间*/
	private Date updateTime;
	
	public BankChargeDetail(){
		
	}
	
	public BankChargeDetail(String userId,String channelId,String bankCode,String bankCardNum,Double amount){
		this.userId = userId ;
		this.channelId = channelId ;
		this.bankCode = bankCode ;
		this.bankCardNum = bankCardNum ;
		this.amount = amount ;
		this.createTime = DateTools.formatDate(new Date(), DateTools.YMD_TIME_STAMP);
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
