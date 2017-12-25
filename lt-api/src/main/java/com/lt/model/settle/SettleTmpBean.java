package com.lt.model.settle;

import java.util.Date;

/**   
* 项目名称：lt-trade   
* 类名称：TmpBean   
* 类描述：临时表bean
* 创建人：yuanxin   
* 创建时间：2017年3月16日 下午1:56:55      
*/
public class SettleTmpBean {
	
	
	private Integer id;
	/** 用户id*/
	private String userId;
	/** 创建时间*/
	private Date createTime;
	/** 金额*/
	private Double amt;
	/** 备注*/
	private String remark;
	/** 关联id*/
	private String externId;
	/** 状态*/
	private Integer status;
	/** 结算类型*/
	private Integer balanceType;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	 * @return the status
	 */
	public Integer getStaus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	/**
	 * @return the balanceType
	 */
	public Integer getBalanceType() {
		return balanceType;
	}
	/**
	 * @param balanceType the balanceType to set
	 */
	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}
	
	
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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	public  SettleTmpBean(){
		
	}
	
	
	public SettleTmpBean(String userId,Double amt,String remark,String externId,Integer status,Integer balanceType){
		this.userId = userId;
		this.amt = amt;
		this.remark = remark ; 
		this.externId = externId;
		this.status = status ;
		this.balanceType = balanceType ;
	}
	
}
