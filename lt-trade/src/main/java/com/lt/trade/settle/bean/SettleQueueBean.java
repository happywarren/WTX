package com.lt.trade.settle.bean;

import java.io.Serializable;

import com.lt.model.trade.OrderCashInfo;

/**   
* 项目名称：lt-trade   
* 类名称：SettleQueueBean   
* 类描述：结算队列bean   
* 创建人：yuanxin   
* 创建时间：2017年4月12日 上午11:31:18      
*/
public class SettleQueueBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 服务名*/
	private Integer service;
	/** 处理对象*/
	private Object object;
	
	
	/**
	 * @return the service
	 */
	public Integer getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(Integer service) {
		this.service = service;
	}
	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
	
	public SettleQueueBean(){
		
	}
	
	public SettleQueueBean(Integer service,OrderCashInfo cashInfo){
		this.service = service;
		this.object = cashInfo;
	}
}
