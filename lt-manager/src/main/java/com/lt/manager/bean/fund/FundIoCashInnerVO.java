package com.lt.manager.bean.fund;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 * 内部存入取出 管理平台接口入参实体
 * 
 * @author guodw
 *
 */
public class FundIoCashInnerVO extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8404549283573241481L;

	/*-----------------------查询结果--------------------------------*/
	private Long ioId;
	/** 昵称*/
	private String nickName;
	/** 入账日期*/
	private Date auditDate;
	/** 申请时间*/
	private Date createDate;
	/** 类型，积分 或现金*/
	private Integer type;
	/** 方向：存 取*/
	private Integer flowType;
	/** 存取金额*/
	private Double amount;
	/** 状态: 0 待审核， 1已通过， 2 已拒绝 */
	private Integer status;
	/** 备注 */
	private String remark;
	/** 操作员id*/
	private Integer modifyUserId;
	/** 操作员姓名*/
	private String modifyUserName;
	/** 详情*/
	private String detail;
	/** 人民币金额*/
	private Double rmbAmt = 0.0 ;
	/*-----------------------查询条件--------------------------------*/
	
	/** 用户ID */
	private String userId;
	/** 昵称 */
	private String userName;
	/** 手机 */
	private String tele;
	/** 到账开始时间 */
	private String startDate;
	/** 到账结束时间 */
	private String endDate;
	
	/** 一级业务码*/
	private String level0;
	/** 二级业务码*/
	private String level1;
	/** 三级业务码*/
	private String level2;
	
	/**关联订单id**/
	private String orderId;
	//品牌 id
	private String brandId;
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the auditDate
	 */
	public Date getAuditDate() {
		return auditDate;
	}
	/**
	 * @param auditDate the auditDate to set
	 */
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the flowType
	 */
	public Integer getFlowType() {
		return flowType;
	}
	/**
	 * @param flowType the flowType to set
	 */
	public void setFlowType(Integer flowType) {
		this.flowType = flowType;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
	/**
	 * @return the modifyUserId
	 */
	public Integer getModifyUserId() {
		return modifyUserId;
	}
	/**
	 * @param modifyUserId the modifyUserId to set
	 */
	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
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
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the ioId
	 */
	public Long getIoId() {
		return ioId;
	}
	/**
	 * @param ioId the ioId to set
	 */
	public void setIoId(Long ioId) {
		this.ioId = ioId;
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/**
	 * @return the level0
	 */
	public String getLevel0() {
		return level0;
	}
	/**
	 * @param level0 the level0 to set
	 */
	public void setLevel0(String level0) {
		this.level0 = level0;
	}
	/**
	 * @return the level1
	 */
	public String getLevel1() {
		return level1;
	}
	/**
	 * @param level1 the level1 to set
	 */
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	/**
	 * @return the level2
	 */
	public String getLevel2() {
		return level2;
	}
	/**
	 * @param level2 the level2 to set
	 */
	public void setLevel2(String level2) {
		this.level2 = level2;
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
	public String getModifyUserName() {
		return modifyUserName;
	}
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
