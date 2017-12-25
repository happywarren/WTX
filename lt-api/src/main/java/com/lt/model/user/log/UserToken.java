package com.lt.model.user.log;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：UserToken   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年12月1日 上午10:50:22      
*/
public class UserToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String token;
	
	private Date createDate;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public UserToken(String id, String token, Date createDate) {
		this.id = id;
		this.token = token;
		this.createDate = createDate;
	}
	
	public UserToken(){
		
	}
}
