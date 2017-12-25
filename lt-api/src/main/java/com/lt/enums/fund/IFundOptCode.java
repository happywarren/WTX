/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: IFundOptCode.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.enums.fund;

/**
 * TODO 资金业务码接口
 * @author XieZhibing
 * @date 2016年12月7日 下午9:27:41
 * @version <b>1.0.0</b>
 */
public interface IFundOptCode {

	/** 
	 * 获取 业务编码 
	 * @return code 
	 */
	public String getCode();
	/** 
	 * 设置 业务编码 
	 * @param code 业务编码 
	 */
	public void setCode(String code);
	/** 
	 * 获取 业务编码描述 
	 * @return name 
	 */
	public String getName();
	/** 
	 * 设置 业务编码描述 
	 * @param name 业务编码描述 
	 */
	public void setName(String name);
	/** 
	 * 获取 一级业务编码 
	 * @return firstLevelCode 
	 */
	public String getFirstLevelCode();
	/** 
	 * 设置 一级业务编码 
	 * @param firstLevelCode 一级业务编码 
	 */
	public void setFirstLevelCode(String firstLevelCode);
	/** 
	 * 获取 一级业务编码描述 
	 * @return firstLevelname 
	 */
	public String getFirstLevelname();
	/** 
	 * 设置 一级业务编码描述 
	 * @param firstLevelname 一级业务编码描述 
	 */
	public void setFirstLevelname(String firstLevelname);
	/** 
	 * 获取 资金进出方向: -1 支出;  1 收入 
	 * @return inout 
	 */
	public Integer getInout();
	/** 
	 * 设置 资金进出方向: -1 支出;  1 收入 
	 * @param inout 资金进出方向: -1 支出;  1 收入 
	 */
	public void setInout(Integer inout);
}
