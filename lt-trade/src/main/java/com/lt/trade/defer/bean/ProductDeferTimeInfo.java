package com.lt.trade.defer.bean;

import java.io.Serializable;

/**   
* 项目名称：lt-trade   
* 类名称：ProductDeferTimeInfo   
* 类描述：递延清仓配置类   
* 创建人：yuanxin   
* 创建时间：2017年4月24日 下午2:31:31      
*/
public class ProductDeferTimeInfo implements Serializable{
	
	/** 产品id*/
	private String productId;
	/** 清仓时间 格式 05:00*/
	private String deferBalanceTime;
	/**
	 * @return the deferBalanceTime
	 */
	public String getDeferBalanceTime() {
		return deferBalanceTime;
	}
	/**
	 * @param deferBalanceTime the deferBalanceTime to set
	 */
	public void setDeferBalanceTime(String deferBalanceTime) {
		this.deferBalanceTime = deferBalanceTime;
	}
	
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
}
