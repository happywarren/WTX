package com.lt.model.settle;

/**   
* 项目名称：lt-trade   
* 类名称：SettleInnerBean   
* 类描述：  清算bean中内置的处理对象(百分比或固定值)
* 创建人：yuanxin   
* 创建时间：2017年3月16日 上午10:08:32      
*/
public class SettleInnerBean {
	
	/** 用户id*/
	private String userId;
	/** 抽成比例/固定值*/
	private Double scale;
	/** 计算类型*/
	private Integer counterType;
	/** 关联id*/
	private String externId;
	/** 备注*/
	private String remark;
	
	public SettleInnerBean(){
		
	}
	
	public SettleInnerBean(String userId,Double scale,String externId,Integer counterType,String remark){
		this.userId = userId;
		this.scale = scale;
		this.externId = externId;
		this.counterType = counterType;
		this.remark = remark ;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the scale
	 */
	public Double getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(Double scale) {
		this.scale = scale;
	}

	/**
	 * @return the externId
	 */
	public String getExternId() {
		return externId;
	}

	/**
	 * @param externId the externId to set
	 */
	public void setExternId(String externId) {
		this.externId = externId;
	}

	/**
	 * @return the counterType
	 */
	public Integer getCounterType() {
		return counterType;
	}

	/**
	 * @param counterType the counterType to set
	 */
	public void setCounterType(Integer counterType) {
		this.counterType = counterType;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
