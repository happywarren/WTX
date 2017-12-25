package com.lt.model.user;

/**   
* 项目名称：lt-api   
* 类名称：UserchargeBankDetailInfo   
* 类描述： 用户充值银行卡信息拓展字段  
* 创建人：yuanxin   
* 创建时间：2017年3月28日 上午9:34:14      
*/
public class UserchargeBankDetailInfo extends UserChargeBankInfo {
	
	/** 汇率*/
	private Double rate;
	/** 联系方式*/
	private String tele;
	/**
	 * @return the rate
	 */
	public Double getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(Double rate) {
		this.rate = rate;
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
	
}
