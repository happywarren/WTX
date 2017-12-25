/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.model.fund
 * FILE    NAME: FundOptCode.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.model.fund;

import java.io.Serializable;

import com.lt.enums.fund.FundFlowTypeEnum;

/**
 * TODO（描述类的职责）
 * @author XieZhibing
 * @date 2016年11月30日 下午2:23:01
 * @version <b>1.0.0</b>
 */
public class FundOptCode implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -6844022683941665447L;
	/** 三级资金业务编码 */
	private String thirdOptCode;
	/**	三级资金业务名称 */
	private String thirdOptName;
	/** 二级资金业务编码 */
	private String secondOptCode;
	/** 二级资金业务名称 */
	private String secondOptName;
	/** 一级资金业务编码 */
	private String firstOptCode;
	/** 一级资金业务名称 */
	private String firstOptName;
	/** 资金流转类型, 参考FundFlowTypeEnum：1收入(默认)，-1支出  */
	private Integer flowType = FundFlowTypeEnum.INCOME.getValue();
	/** 资金类型: 0 现金; 1 积分;  2 通用 */
	private Integer fundType;
	/** 备注 */
	private String remark;
	/**主键**/
	private int id;
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月6日 下午2:33:45
	 */
	public FundOptCode() {
		super();
	}
	/** 
	 * 获取 三级资金业务编码 
	 * @return thirdOptCode 
	 */
	public String getThirdOptCode() {
		return thirdOptCode;
	}
	/** 
	 * 设置 三级资金业务编码 
	 * @param thirdOptCode 三级资金业务编码 
	 */
	public void setThirdOptCode(String thirdOptCode) {
		this.thirdOptCode = thirdOptCode;
	}
	/** 
	 * 获取 三级资金业务名称 
	 * @return thirdOptName 
	 */
	public String getThirdOptName() {
		return thirdOptName;
	}
	/** 
	 * 设置 三级资金业务名称 
	 * @param thirdOptName 三级资金业务名称 
	 */
	public void setThirdOptName(String thirdOptName) {
		this.thirdOptName = thirdOptName;
	}
	/** 
	 * 获取 二级资金业务编码 
	 * @return secondOptCode 
	 */
	public String getSecondOptCode() {
		return secondOptCode;
	}
	/** 
	 * 设置 二级资金业务编码 
	 * @param secondOptCode 二级资金业务编码 
	 */
	public void setSecondOptCode(String secondOptCode) {
		this.secondOptCode = secondOptCode;
	}
	/** 
	 * 获取 二级资金业务名称 
	 * @return secondOptName 
	 */
	public String getSecondOptName() {
		return secondOptName;
	}
	/** 
	 * 设置 二级资金业务名称 
	 * @param secondOptName 二级资金业务名称 
	 */
	public void setSecondOptName(String secondOptName) {
		this.secondOptName = secondOptName;
	}
	/** 
	 * 获取 一级资金业务编码 
	 * @return firstOptCode 
	 */
	public String getFirstOptCode() {
		return firstOptCode;
	}
	/** 
	 * 设置 一级资金业务编码 
	 * @param firstOptCode 一级资金业务编码 
	 */
	public void setFirstOptCode(String firstOptCode) {
		this.firstOptCode = firstOptCode;
	}
	/** 
	 * 获取 一级资金业务名称 
	 * @return firstOptName 
	 */
	public String getFirstOptName() {
		return firstOptName;
	}
	/** 
	 * 设置 一级资金业务名称 
	 * @param firstOptName 一级资金业务名称 
	 */
	public void setFirstOptName(String firstOptName) {
		this.firstOptName = firstOptName;
	}
	/** 
	 * 获取 资金流转类型 参考FundFlowTypeEnum：1收入(默认)，-1支出 
	 * @return flowType 
	 */
	public Integer getFlowType() {
		return flowType;
	}
	/** 
	 * 设置 资金流转类型 参考FundFlowTypeEnum：1收入(默认)，-1支出 
	 * @param flowType 资金流转类型 参考FundFlowTypeEnum：1收入(默认)，-1支出 
	 */
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}
	/** 
	 * 获取 资金类型: 0 现金; 1 积分;  2 通用 
	 * @return fundType 
	 */
	public Integer getFundType() {
		return fundType;
	}
	/** 
	 * 设置 资金类型: 0 现金; 1 积分;  2 通用 
	 * @param fundType 资金类型: 0 现金; 1 积分;  2 通用 
	 */
	public void setFundType(Integer fundType) {
		this.fundType = fundType;
	}
	/** 
	 * 获取 备注 
	 * @return remark 
	 */
	public String getRemark() {
		return remark;
	}
	/** 
	 * 设置 备注 
	 * @param remark 备注 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月6日 下午2:33:32
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundOptCode [thirdOptCode=").append(thirdOptCode)
				.append(", thirdOptName=").append(thirdOptName)
				.append(", secondOptCode=").append(secondOptCode)
				.append(", secondOptName=").append(secondOptName)
				.append(", firstOptCode=").append(firstOptCode)
				.append(", firstOptName=").append(firstOptName)
				.append(", flowType=").append(flowType).append(", fundType=")
				.append(fundType).append(", remark=").append(remark)
				.append("]");
		return builder.toString();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
