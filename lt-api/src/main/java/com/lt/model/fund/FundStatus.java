package com.lt.model.fund;

import java.io.Serializable;

/**
 * 
 * TODO 充值与提现的处理状态
 * @author XieZhibing
 * @date 2016年11月28日 下午5:35:19
 * @version <b>1.0.0</b>
 */
public class FundStatus implements Serializable {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 6033118002023094338L;
	
	/** 操作方向: 充值、提现 */
	private String type;
	/** 操作项编码 */
	private Integer value;
	/** 操作描述 */
	private String name;
	/** 
	 * 获取 操作方向: 提现、充值 
	 * @return type 
	 */
	public String getType() {
		return type;
	}
	/** 
	 * 设置 操作方向: 提现、充值 
	 * @param type 操作方向: 提现、充值 
	 */
	public void setType(String type) {
		this.type = type;
	}
	/** 
	 * 获取 操作项编码 
	 * @return value 
	 */
	public Integer getValue() {
		return value;
	}
	/** 
	 * 设置 操作项编码 
	 * @param value 操作项编码 
	 */
	public void setValue(Integer value) {
		this.value = value;
	}
	/** 
	 * 获取 操作描述 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	/** 
	 * 设置 操作描述 
	 * @param name 操作描述 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年11月28日 下午5:35:01
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FundStatus [type=").append(type).append(", value=")
				.append(value).append(", name=").append(name).append("]");
		return builder.toString();
	}
	
	
	
}
