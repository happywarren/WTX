package com.lt.model.promote;

import java.io.Serializable;

/**
 * 佣金操作业务码（流转方式）实体
 * @author jingwb
 *
 */
public class CommisionOptcode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6787417214334534541L;
	/***/
	private Integer id;
	/**三级佣金业务码*/
	private String thirdOptcode;
	/**三级佣金业务码名称*/
	private String thirdOptname;
	/**二级佣金业务码*/
	private String secondOptcode;
	/**二级佣金业务码名称*/
	private String secondOptname;
	
	/**一级佣金业务码*/
	private String firstOptcode;
	/**一级佣金业务码名称*/
	private String firstOptname;
	/**佣金流转类型: -1 支出;  1 收入*/
	private Integer flowType;
	/**备注*/
	private String remark;
	
	public CommisionOptcode(){
		
	}
	
	public CommisionOptcode(String firstOptcode,String secondOptcode,String thirdOptcode){
		this.firstOptcode = firstOptcode;
		this.secondOptcode = secondOptcode;
		this.thirdOptcode = thirdOptcode;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getThirdOptcode() {
		return thirdOptcode;
	}
	public void setThirdOptcode(String thirdOptcode) {
		this.thirdOptcode = thirdOptcode;
	}
	public String getThirdOptname() {
		return thirdOptname;
	}
	public void setThirdOptname(String thirdOptname) {
		this.thirdOptname = thirdOptname;
	}
	public String getSecondOptcode() {
		return secondOptcode;
	}
	public void setSecondOptcode(String secondOptcode) {
		this.secondOptcode = secondOptcode;
	}
	public String getSecondOptname() {
		return secondOptname;
	}
	public void setSecondOptname(String secondOptname) {
		this.secondOptname = secondOptname;
	}
	public String getFirstOptcode() {
		return firstOptcode;
	}
	public void setFirstOptcode(String firstOptcode) {
		this.firstOptcode = firstOptcode;
	}
	public String getFirstOptname() {
		return firstOptname;
	}
	public void setFirstOptname(String firstOptname) {
		this.firstOptname = firstOptname;
	}
	public Integer getFlowType() {
		return flowType;
	}
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
