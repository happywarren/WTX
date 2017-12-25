package com.lt.user.charge.bean;

/**   
* 项目名称：lt-user   
* 类名称：RechargeByZFBM   
* 类描述：支付宝充值（手动）   
* 创建人：yuanxin   
* 创建时间：2017年6月29日 下午5:19:28      
*/
public class RechargeByZFBM {
	
	public RechargeByZFBM(){
		
	}
	
	public RechargeByZFBM(String zfbNum){
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
