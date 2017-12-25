package com.lt.controller.user.bean;

import java.io.Serializable;

/**   
* 项目名称：lt-transfer   
* 类名称：UserFundMsgBean   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年5月12日 上午10:07:43      
*/
public class UserFundMsgBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户联系方式*/
	private String tele ;
	/** 人民币金额*/
	private Double rmbAmt;
	/** 内容*/
	private String content ;
	/**
	 * @return the tele
	 */
	public String getTele() {
		return tele;
	}
	/**
	 * @param tele the tele to set
	 */
	public void setTele(String tele) {
		this.tele = tele;
	}
	/**
	 * @return the rmbAmt
	 */
	public Double getRmbAmt() {
		return rmbAmt;
	}
	/**
	 * @param rmbAmt the rmbAmt to set
	 */
	public void setRmbAmt(Double rmbAmt) {
		this.rmbAmt = rmbAmt;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
}
