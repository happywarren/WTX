package com.lt.model.user;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * 描述: 券商商品配置
 *
 * @author  梅传颂
 * @created 2017年07月18日 下午13:13
 * @since   v1.0.0
 */
public class InvestorProductConfig implements Serializable {
	

	/**
	 *  ID 
	 */
	private Long id;

	/**
	 * 券商id
	 */
	private String investorAccountId;

	/**
	 * 商品id
	 */
	private String productCode;

	/**
	 * 手续费
	 */
	private String counterFee;

	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 *  创建人
	 */
	private String creater;


	public InvestorProductConfig() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvestorAccountId() {
		return investorAccountId;
	}

	public void setInvestorAccountId(String investorAccountId) {
		this.investorAccountId = investorAccountId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(String counterFee) {
		this.counterFee = counterFee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
}


