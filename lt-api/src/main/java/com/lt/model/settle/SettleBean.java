package com.lt.model.settle;

import java.util.List;

/**   
* 项目名称：lt-trade   
* 类名称：SettleBean   
* 类描述： 清算功能里的传入对象  
* 创建人：yuanxin   
* 创建时间：2017年3月16日 上午10:07:24      
*/
public class SettleBean {
	
	/** 调用的功能列表*/
	private String func;
	/** 金额*/
	private Double amt;
	/** 内置bean 对象 包含比例，用户，关联订单号*/
	private List<SettleInnerBean> settleInnerBean;
	
	public  SettleBean(){
		
	}
	
	public SettleBean(String func,Double amt,List<SettleInnerBean> settleInnerBean){
		this.func = func;
		this.amt = amt;
		this.settleInnerBean = settleInnerBean;
	}

	/**
	 * @return the func
	 */
	public String getFunc() {
		return func;
	}

	/**
	 * @param func the func to set
	 */
	public void setFunc(String func) {
		this.func = func;
	}

	/**
	 * @return the amt
	 */
	public Double getAmt() {
		return amt;
	}

	/**
	 * @param amt the amt to set
	 */
	public void setAmt(Double amt) {
		this.amt = amt;
	}

	/**
	 * @return the settleInnerBean
	 */
	public List<SettleInnerBean> getSettleInnerBean() {
		return settleInnerBean;
	}

	/**
	 * @param settleInnerBean the settleInnerBean to set
	 */
	public void setSettleInnerBean(List<SettleInnerBean> settleInnerBean) {
		this.settleInnerBean = settleInnerBean;
	}
	
}
