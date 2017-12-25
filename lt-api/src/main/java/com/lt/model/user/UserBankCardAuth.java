package com.lt.model.user;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api <br>  
* 类名称：UserBankAuth <br>  
* 类描述：  用户认证信息实体 <br>
* 创建人：yubei   <br>
* 创建时间：2017年6月5日 下午17:23:05<br>      
*/
public class UserBankCardAuth implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id ;
	/**用户id**/
	private String userId;
	/**用户姓名**/
	private String userName;
	/** 银行卡号*/
	private String bankCardNo;
	/**结果码**/
	private String resultId;
	/**结果信息**/
	private String resultMsg;
	/**请求流水**/
	private String reqId;
	/**响应流水**/
	private String resId;
	/**创建时间**/
	private Date createDate;
	/**最后一次更新时间**/
	private Date lastModifyDate;
	
	public UserBankCardAuth() {
		super();
	}
	
	public UserBankCardAuth(Integer id, String userId, String userName, String bankCardNo, String resultId,
			String resultMsg, String reqId, String resId, Date createDate, Date lastModifyDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.bankCardNo = bankCardNo;
		this.resultId = resultId;
		this.resultMsg = resultMsg;
		this.reqId = reqId;
		this.resId = resId;
		this.createDate = createDate;
		this.lastModifyDate = lastModifyDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
}
