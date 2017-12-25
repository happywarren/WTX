package com.lt.manager.bean.fund;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**   
* 项目名称：lt-manager   
* 类名称：FundMainCashScore   
* 类描述： 用户内部存取界面内容 
* 创建人：yuanxin   
* 创建时间：2017年1月10日 下午5:35:13      
*/
public class FundMainCashScore extends BaseBean{
	
	/** 用户ID*/
	private String userId;
	/** 用户姓名*/
	private String userName;
	/** 用户昵称*/
	private String nickName;
	/** 电话号码*/
	private String tele;
	/** 资金总数*/
	private Double cashAmount;
	/**
	 * 人民币资金总数
	 */
	private Double cashAmountRmb;
	/** 积分总数*/
	private Double scoreAmount;
	/** 创建时间*/
	private Date createDate;
	private String brandId;

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
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
	 * @return the cashAmount
	 */
	public Double getCashAmount() {
		return cashAmount;
	}
	/**
	 * @param cashAmount the cashAmount to set
	 */
	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}
	/**
	 * @return the scoreAmount
	 */
	public Double getScoreAmount() {
		return scoreAmount;
	}
	/**
	 * @param scoreAmount the scoreAmount to set
	 */
	public void setScoreAmount(Double scoreAmount) {
		this.scoreAmount = scoreAmount;
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
	public Double getCashAmountRmb() {
		return cashAmountRmb;
	}
	public void setCashAmountRmb(Double cashAmountRmb) {
		this.cashAmountRmb = cashAmountRmb;
	}
	
	
}
