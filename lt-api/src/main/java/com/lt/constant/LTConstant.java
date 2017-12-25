/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.constant
 * FILE    NAME: LTConstant.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.constant;

import java.io.Serializable;

/**
 * 
 * TODO 常量类
 * @author XieZhibing
 * @date 2017年2月7日 下午1:50:49
 * @version <b>1.0.0</b>
 */
public class LTConstant implements Serializable {

	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -249371961364688926L;
	
	/** 默认券商ID */
	public static final Integer	DEFAULT_INVESTOR_ID = 9999;
	
	/**
	 * 差价合约最低版本号
	 */
	public static final String CFD_MIN_VERSION = "2.3.0";
	
	/**
	 * 默认风险等级
	 */
	public static final String DEFAULT_RISK_LEVEL = "4";
	
}
