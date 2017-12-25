package com.lt.model.user.log;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserCheckRegcode   
* 类描述 ：校验验证码的日志   
* 创建人：yuanxin   
* 创建时间：2016年12月5日 下午5:53:50      
*/
public class UserCheckRegcode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 验证的号码*/
	private String tele;
	/** 验证码*/
	private String authCode;
	/** 创建时间*/
	private Date createTime;
	/** 是否校验成功*/
	private boolean isSuccessed;
	/**
	 * @return the tele
	 */
	public UserCheckRegcode(String tele,String authCode,boolean isSuccessed){
		this.tele  = tele;
		this.authCode = authCode;
		this.isSuccessed = isSuccessed;
		this.createTime = new Date();
	}
	
	public UserCheckRegcode(){
		
	}
	
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
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}
	/**
	 * @param authCode the authCode to set
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * @return the isSuccessed
	 */
	public boolean isSuccessed() {
		return isSuccessed;
	}
	/**
	 * @param isSuccessed the isSuccessed to set
	 */
	public void setSuccessed(boolean isSuccessed) {
		this.isSuccessed = isSuccessed;
	}
	
}
