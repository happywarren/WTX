package com.lt.trade.defer.bean;

import java.io.Serializable;

/**   
* 项目名称：lt-trade   
* 类名称：ProNextTradePeriod   
* 类描述： 存储递延跨天时间的信息  
* 创建人：yuanxin   
* 创建时间：2017年2月9日 上午10:14:45      
*/
public class ProNextTradePeriod implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 短类型 如CU*/
	public Integer productId;
	/** 系统卖出时间*/
	public String sysSaleEndTime;
	
	
	/** 隔天的卖出时间*/
	public String nextDayTime;
	
	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return the sysSaleEndTime
	 */
	public String getSysSaleEndTime() {
		return sysSaleEndTime;
	}
	/**
	 * @param sysSaleEndTime the sysSaleEndTime to set
	 */
	public void setSysSaleEndTime(String sysSaleEndTime) {
		this.sysSaleEndTime = sysSaleEndTime;
	}
	/**
	 * @return the nextDayTime
	 */
	public String getNextDayTime() {
		return nextDayTime;
	}
	/**
	 * @param nextDayTime the nextDayTime to set
	 */
	public void setNextDayTime(String nextDayTime) {
		this.nextDayTime = nextDayTime;
	}
	
}
