package com.lt.manager.bean.product;

import com.lt.manager.bean.BaseBean;

public class ExchangeParamVO extends BaseBean{
	private static final long serialVersionUID = 1L;

	/**
	 * 代码
	 */
	private String code;
	/**
	 * 市场名称
	 */
	private String name;
	/**
	 * 备注
	 */
	private String remark;
	
	private String sign;//符号
	
	private Double rate;//汇率
	
	private String unit;//单位
	
	private String currency;//币种编码(CNY、USD)
	
	private Integer createUser;//创建人
	
	private String beginTime;
	
	private String endTime;
	private Integer exchangeId;//交易所id
	//0：内盘 1：外盘  2:差价合约
	private Integer plate;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(Integer exchangeId) {
		this.exchangeId = exchangeId;
	}
	public Integer getPlate() {
		return plate;
	}
	public void setPlate(Integer plate) {
		this.plate = plate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
}
