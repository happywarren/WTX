package com.lt.manager.bean.user;

import com.lt.manager.bean.BaseBean;

import java.util.Date;


/**
 *
 * 描述: 券商商品配置
 *
 * @author  梅传颂
 * @created 2017年07月18日 下午13:13
 * @since   v1.0.0
 */
public class InvestorProductConfig  extends BaseBean {


	/**
     * 券商id
	 */
	private String investorAccountId;

	/**
     * 品种
	 */
	private String productCode;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品类型
	 */
	private Integer productType;

	/**
     * 手续费
	 */
	private String counterFee;

	/**
	 * 最小手续费
	 */
	private double minCounterFee;

	/**
	 * 最大手续费
	 */
	private double maxCounterFee;

	/**
	 * 货币符号
	 */
	private String currency;

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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public String getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(String counterFee) {
		this.counterFee = counterFee;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getMinCounterFee() {
		return minCounterFee;
	}

	public void setMinCounterFee(double minCounterFee) {
		this.minCounterFee = minCounterFee;
	}

	public double getMaxCounterFee() {
		return maxCounterFee;
	}

	public void setMaxCounterFee(double maxCounterFee) {
		this.maxCounterFee = maxCounterFee;
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


