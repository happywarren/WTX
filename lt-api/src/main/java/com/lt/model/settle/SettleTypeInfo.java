package com.lt.model.settle;

import java.util.Date;

/**   
* 项目名称：lt-trade   
* 类名称：SettleTypeInfo   
* 类描述：结算配置表   
* 创建人：yuanxin   
* 创建时间：2017年3月16日 下午3:29:52      
*/
public class SettleTypeInfo {
	
	/** id*/
	private Integer id;
	/** 结算功能 值*/
	private String settleValue;
	/** 结算功能 名称*/
	private String settleName;
	/** 结算功能 序列*/
	private String settleList;
	/** 备注*/
	private String remark;
	/** 创建时间*/
	private Date createDate;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the settleValue
	 */
	public String getSettleValue() {
		return settleValue;
	}
	/**
	 * @param settleValue the settleValue to set
	 */
	public void setSettleValue(String settleValue) {
		this.settleValue = settleValue;
	}
	/**
	 * @return the settleName
	 */
	public String getSettleName() {
		return settleName;
	}
	/**
	 * @param settleName the settleName to set
	 */
	public void setSettleName(String settleName) {
		this.settleName = settleName;
	}
	/**
	 * @return the settleList
	 */
	public String getSettleList() {
		return settleList;
	}
	/**
	 * @param settleList the settleList to set
	 */
	public void setSettleList(String settleList) {
		this.settleList = settleList;
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
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
