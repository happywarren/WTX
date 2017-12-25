package com.lt.user.charge.bean;

/**   
* 项目名称：lt-user   
* 类名称：RechargeByKQ   
* 类描述： 快钱
* 创建人：yuanxin   
* 创建时间：2017年6月30日 下午6:12:30      
*/
public class RechargeByKQDynamic {
	
	/**
	 * 电话号码
	 */
	private String tele ;
	
	
	public RechargeByKQDynamic(){
		
	}
	
	public RechargeByKQDynamic(String tele){
		this.tele = tele ;
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
