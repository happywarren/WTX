package com.lt.enums.user;

/**   
* 项目名称：lt-api   
* 类名称：ChargeEnum   
* 类描述：系统选择充值方式枚举类   
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午7:43:55      
*/
public enum ChargeEnum {
	
	/** 获取用户最大可充值金额 beg*/
	
	/** 可充值金额不足*/
	NOT_MATCH_CHANNEL(404,"可充值金额不足"),
	/** 系统未配置参数*/
	NOT_SYS_SET(500,"系统未配置参数"),
	/** 匹配成功*/
	SUCCESS(200,"匹配成功")
	
	/** 获取用户最大可充值金额 end*/
	
	/** 获取用户最大可充值金额 beg*/
	
	
	/** 获取用户最大可充值金额 end*/
	;
	
	
	/** 值*/
	private Integer value ;
	/** 备注*/
	private String remark ;
	
	/**
	 * 
	 */
	ChargeEnum(Integer value , String remark ) {
		this.value = value ;
		this.remark = remark ;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
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
	
}
