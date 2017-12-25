package com.lt.vo.defer;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-trade   
* 类名称：PeroidOrderHoliday   
* 类描述：递延的节假日时间对象   
* 创建人：yuanxin   
* 创建时间：2017年2月8日 下午7:24:13      
*/
public class PeroidOrderHolidayVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 短类型*/
	private Integer productId;
	/** 假日开始时间*/
	private Date beginTime;
	/** 假日结束时间*/
	private Date endTime;
	
	
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
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
