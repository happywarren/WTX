package com.lt.user.charge.bean;

/**   
* 项目名称：lt-user   
* 类名称：RechargeByKQ   
* 类描述：快钱支付订单   
* 创建人：yuanxin   
* 创建时间：2017年7月12日 下午2:25:49      
*/
public class RechargeByKQ {
	
	/** 校验码*/
	private String validCode ;
	/** 电话号码*/
	private String tele ;
	
	public RechargeByKQ(){
		
	}
	
	public RechargeByKQ(String validCode,String tele){
		this.validCode = validCode ;
		this.tele = tele ;
	}

	/** 
	* 获取validCode  
	*/
	public String getValidCode() {
		return validCode;
	}

	/** 
	* 获取validCode  
	*/
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	/** 
	* 获取tele  
	*/
	public String getTele() {
		return tele;
	}

	/** 
	* 获取tele  
	*/
	public void setTele(String tele) {
		this.tele = tele;
	}
	
	
}
