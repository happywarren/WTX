package com.lt.manager.bean.promote;

import com.lt.manager.bean.BaseBean;

/**
 * 佣金实体
 * @author jingwb
 *
 */
public class CommisionParamVo extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2895578221677877139L;
	private String userId;
	private String nickName;
	private String tele;
	private String userName;
	private String createDate;
	private String beginTime;
	private String endTime;
	private Double amount;//提现金额
	private Double balance;//可用余额
	private Integer status;//状态: 0 待审核， 1已通过， 2 已拒绝
	private String firstOptname;
	private String secondOptname;
	private String remark;
	private String firstOptcode;
	private String secondOptcode;
	private Integer flowType;
	//品牌 id
	private String brandId;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getFirstOptname() {
		return firstOptname;
	}
	public void setFirstOptname(String firstOptname) {
		this.firstOptname = firstOptname;
	}
	public String getSecondOptname() {
		return secondOptname;
	}
	public void setSecondOptname(String secondOptname) {
		this.secondOptname = secondOptname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFirstOptcode() {
		return firstOptcode;
	}
	public void setFirstOptcode(String firstOptcode) {
		this.firstOptcode = firstOptcode;
	}
	public String getSecondOptcode() {
		return secondOptcode;
	}
	public void setSecondOptcode(String secondOptcode) {
		this.secondOptcode = secondOptcode;
	}
	public Integer getFlowType() {
		return flowType;
	}
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
