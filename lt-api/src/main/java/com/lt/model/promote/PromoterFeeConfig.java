package com.lt.model.promote;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广佣金费用配置实体
 * @author jingwb
 *
 */
public class PromoterFeeConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8486671496775578303L;
	/***/
	private Integer id;
	/**级别id*/
	private Integer levelId;
	/**品种*/
	private String shortCode;
	/**一层下线佣金费*/
	private Double firstCommissionFee;
	/**二层下线用金费*/
	private Double secondCommissionFee;
	/**创建时间*/
	private Date createTime;
	/**修改时间*/
	private Date modifyTime;
	
	private String productName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public Double getFirstCommissionFee() {
		return firstCommissionFee;
	}
	public void setFirstCommissionFee(Double firstCommissionFee) {
		this.firstCommissionFee = firstCommissionFee;
	}
	public Double getSecondCommissionFee() {
		return secondCommissionFee;
	}
	public void setSecondCommissionFee(Double secondCommissionFee) {
		this.secondCommissionFee = secondCommissionFee;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
