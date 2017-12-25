package com.lt.enums.settle;

/**   
* 项目名称：lt-api   
* 类名称：CaluateTypeEnum   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月16日 下午5:42:34      
*/
public enum CaluateTypeEnum {
	
	ADD(1,"加法"),
	MULTI(2,"乘法");
	
	
	
	/** 类型*/
	private int type;
	/** 说明*/
	private String name;
	
	CaluateTypeEnum(){
		
	}
	
	CaluateTypeEnum(int type,String name){
		this.type = type ;
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
