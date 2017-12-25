package com.lt.manager.bean.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 说明：员工数据
 * 
 *
 */
public class Staff implements Serializable{

	/* ID */
	private Integer id;
	/* 姓名 */
	private String name;
	/* 登录名,唯一确定  */
	private String loginName;
	/* 后台密码: 默认123456*/
	private String pwd = "123456";
	/* 性别 0：未知 1：男 2：女 */
	private Integer sex = 0;
	/* 邮箱 */
	private String email;
	/* 手机 */
	private String tele;
	/* 座机 */
	private String mobile = "";
	/* 状态 0：可用 -1：不可用 */
	private Integer status = 0;
	/* 所属部门名称 */
	private String deptName = "";
	/* 创建日期 */
	private Date createDate ;
	/* 修改日期 */
	private Date modifyDate ;
	/* 创建人 */
	private Integer createStaffId;
	/* 修改人 */
	private Integer modifyStaffId;
	/* 员工照片 */
	private String headImg = "";
	/* 员工身份证号 */
	private String idCard = "";
	/* 是否启用Ip限制 */
	private Integer isStartUseIpAstrict ;
	/* 是否启用设备限制 */
	private Integer isStartDeviceAstrict;
	/**
	 * 是否发送验证码
	 */
	private Integer isStartCode;
	
	public Integer getIsStartCode() {
		return isStartCode;
	}

	public void setIsStartCode(Integer isStartCode) {
		this.isStartCode = isStartCode;
	}

	public Integer getIsStartDeviceAstrict() {
		return isStartDeviceAstrict;
	}

	public void setIsStartDeviceAstrict(Integer isStartDeviceAstrict) {
		this.isStartDeviceAstrict = isStartDeviceAstrict;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTele() {
		return tele;
	}

	public void setTele(String tele) {
		this.tele = tele;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getCreateStaffId() {
		return createStaffId;
	}

	public void setCreateStaffId(Integer createStaffId) {
		this.createStaffId = createStaffId;
	}

	public Integer getModifyStaffId() {
		return modifyStaffId;
	}

	public void setModifyStaffId(Integer modifyStaffId) {
		this.modifyStaffId = modifyStaffId;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getIsStartUseIpAstrict() {
		return isStartUseIpAstrict;
	}

	public void setIsStartUseIpAstrict(Integer isStartUseIpAstrict) {
		this.isStartUseIpAstrict = isStartUseIpAstrict;
	}

	public Staff( String name, String loginName, String pwd,
			Integer sex, String email, String tele, String mobile,
			Integer status, String deptName, 
			Integer createStaffId, Integer modifyStaffId, String headImg,
			String idCard, Integer isStartUseIpAstrict,
			Integer isStartDeviceAstrict) {
		super();
		this.name = name;
		this.loginName = loginName;
		this.pwd = pwd;
		this.sex = sex;
		this.email = email;
		this.tele = tele;
		this.mobile = mobile;
		this.status = status;
		this.deptName = deptName;
		this.createDate = new Date();
		this.createStaffId = createStaffId;
		this.modifyStaffId = modifyStaffId;
		this.headImg = headImg;
		this.idCard = idCard;
		this.isStartUseIpAstrict = isStartUseIpAstrict;
		this.isStartDeviceAstrict = isStartDeviceAstrict;
	}

	public Staff() {
		super();
	}
	
}
