package com.lt.user.charge.bean;

/**   
* 项目名称：lt-user   
* 类名称：RechargeByZFBM   
* 类描述：熙大支付宝充值	
* 创建人：yubei   
* 创建时间：2018年8月7日      
*/
public class RechargeByXDPay {
	
	public RechargeByXDPay(){
		
	}
	
	public RechargeByXDPay(String zfbNum){
		this.zfbNum = zfbNum ;
	}

	/** 支付宝账号*/
	private String zfbNum;

	/**
	 * @return the zfbNum
	 */
	public String getZfbNum() {
		return zfbNum;
	}

	/**
	 * @param zfbNum the zfbNum to set
	 */
	public void setZfbNum(String zfbNum) {
		this.zfbNum = zfbNum;
	}
	
}
