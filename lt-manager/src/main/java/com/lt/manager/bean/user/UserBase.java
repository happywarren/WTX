package com.lt.manager.bean.user;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**
 *
 * 描述: 用户列表页面返回对象
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class UserBase extends BaseBean{

	/**
	 *  真实姓名 
	 */
	private String userName;

	/**
	 *  密码 
	 */
	private String passwd;
	/** 
	 * 昵称
	 */
	private String nickName;

	/** 
	 * 个性签名
	 */           
	private String personalSign;
	
	/** 
	 * 操作系统名
	 */
	private String systemName;
	
	/** 
	 * 性别
	 */
	private Integer sex;
	/**
	 * 用户状态
	 */
	private Integer status;
	/**
	 * 是否开启手势密码
	 */
	private Integer isStartGesture;
	
	/**
	 * 设置手势密码
	 */
	private Integer isSetGesturePwd;
	/**
	 * 身份证号码
	 */
	private String idCardNum;
	
	/**
	 * 用户开户状态
	 */
	private Integer openAccountStatus;
	
	/**
	 * 上级推广员ID
	 */
	private String promoteId;

	/**
	 * 用户信用级别
	 */
	private Integer creditValue;
	/**
	 * 手机号 
	 */
	private String tele;
	/**
	 * 手机绑定状态
	 */
	private Integer teleStatus ;
	/**
	 * 注册运营商
	 */
	private Integer regOperator ;
	/**
	 * 注册渠道
	 */
	private Integer regSource;
	
	/**
	 * 注册渠道
	 */
	private String channelName;
	
	/**
	 * 注册方式  手机/PC/微信
	 */
	private Integer regMode;
	/**
	 * 是否为内部员工
	 */
	private Integer isStaff; 
	/**
	 * 注册IP地址
	 */
	private String ip;
	/**
	 * 用户头像
	 */
	private String headPic;
	
	private String idPicPositive;
	
	private String idPicReverse;
	
	private String bankCardPic;
	/**
	 * 注册终端类型 iPhone
	 */
	private String deviceModel;
	/**
	 * 设备版本
	 */
	private String deviceVersion;
	/**
	 * 注册终端IMEI
	 */
	private String deviceImei;
	/**
	 * 注册软件版本
	 */
	private String version;
	/**
	 * 用户级别
	 */
	private String userGrade;
	/** 
	 * 最后一次登录时间  
	 */
	private Date lastLoginDate;
	/**
	 * 上次登录的IMEI
	 */
	private String lastLoginImei;
	/** 
	 * 创建时间/注册时间 
	 */
	private Date createDate;
	/** 
	 * 修改时间
	 */
	private Date modifyDate;
	/** 
	 * 备注
	 */
	private String remark;
	
	/** 
	 * 现金余额
	 */
	private Double balanceCash;
	
	/** 
	 * 积分余额
	 */
	private Double balanceScore;
	
	/** 
	 * 总手续费
	 */
	private Double totalCounterFee;
	
	private Integer realNameStatus;
	
	/** 
	 * 备注
	 */
	private String bankName;
	
	/** 
	 * 备注
	 */
	private String bankCardNum;
	/** 
	 * 申请实名时间
	 */
	private String applyTime;
	/** 
	 * 实名通过时间
	 */
	 private String finishTime;
	/** 
	 * 备注
	 */
	private String operatorTime;
	
	/**
	 * 用户是否黑名单
	 */
	private String isBlack;
	/**
	 * 人脸识别照片路径
	 */
	private String facePicPath;
	
	/**
	 * 用户签名图片路径
	 */
	private String signPicPath;
	
	/**
	 * 风险测评结果
	 */
	private String riskRet;
	
	/**
	 * 是否在线
	 */
	private int online;
	
	/**
	 * 开户步骤
	 */
	private int openStep;
	/**风险等级**/
	private String riskLevel;

	/**
	 * 券商id
	 */
	private String investorAccountId;
	/**
	 * 品牌名称
	 * 修改人：yanzhenyu
	 */
	private String brandName;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPersonalSign() {
		return personalSign;
	}
	public void setPersonalSign(String personalSign) {
		this.personalSign = personalSign;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsStartGesture() {
		return isStartGesture;
	}
	public void setIsStartGesture(Integer isStartGesture) {
		this.isStartGesture = isStartGesture;
	}

	public Integer getIsSetGesturePwd() {
		return isSetGesturePwd;
	}
	public void setIsSetGesturePwd(Integer isSetGesturePwd) {
		this.isSetGesturePwd = isSetGesturePwd;
	}

	public String getIdCardNum() {
		return idCardNum;
	}
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}
	public String getPromoteId() {
		return promoteId;
	}
	public void setPromoteId(String promoteId) {
		this.promoteId = promoteId;
	}
	public Integer getOpenAccountStatus() {
		return openAccountStatus;
	}
	public void setOpenAccountStatus(Integer openAccountStatus) {
		this.openAccountStatus = openAccountStatus;
	}
	public Integer getCreditValue() {
		return creditValue;
	}
	public void setCreditValue(Integer creditValue) {
		this.creditValue = creditValue;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public Integer getTeleStatus() {
		return teleStatus;
	}
	public void setTeleStatus(Integer teleStatus) {
		this.teleStatus = teleStatus;
	}
	public Integer getRegOperator() {
		return regOperator;
	}
	public void setRegOperator(Integer regOperator) {
		this.regOperator = regOperator;
	}
	public Integer getRegSource() {
		return regSource;
	}
	public void setRegSource(Integer regSource) {
		this.regSource = regSource;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Integer getRegMode() {
		return regMode;
	}
	public void setRegMode(Integer regMode) {
		this.regMode = regMode;
	}
	public Integer getIsStaff() {
		return isStaff;
	}
	public void setIsStaff(Integer isStaff) {
		this.isStaff = isStaff;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHeadPic() {
		return headPic;
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	public String getIdPicPositive() {
		return idPicPositive;
	}
	public void setIdPicPositive(String idPicPositive) {
		this.idPicPositive = idPicPositive;
	}
	public String getIdPicReverse() {
		return idPicReverse;
	}
	public void setIdPicReverse(String idPicReverse) {
		this.idPicReverse = idPicReverse;
	}
	public String getBankCardPic() {
		return bankCardPic;
	}
	public void setBankCardPic(String bankCardPic) {
		this.bankCardPic = bankCardPic;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getDeviceVersion() {
		return deviceVersion;
	}
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}
	public String getDeviceImei() {
		return deviceImei;
	}
	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUserGrade() {
		return userGrade;
	}
	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getLastLoginImei() {
		return lastLoginImei;
	}
	public void setLastLoginImei(String lastLoginImei) {
		this.lastLoginImei = lastLoginImei;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getBalanceCash() {
		return balanceCash;
	}
	public void setBalanceCash(Double balanceCash) {
		this.balanceCash = balanceCash;
	}
	public Double getBalanceScore() {
		return balanceScore;
	}
	public void setBalanceScore(Double balanceScore) {
		this.balanceScore = balanceScore;
	}
	public Double getTotalCounterFee() {
		return totalCounterFee;
	}
	public void setTotalCounterFee(Double totalCounterFee) {
		this.totalCounterFee = totalCounterFee;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCardNum() {
		return bankCardNum;
	}
	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}
	public String getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public Integer getRealNameStatus() {
		return realNameStatus;
	}
	public void setRealNameStatus(Integer realNameStatus) {
		this.realNameStatus = realNameStatus;
	}
	/**
	 * @return the isBlack
	 */
	public String getIsBlack() {
		return isBlack;
	}
	/**
	 * @param isBlack the isBlack to set
	 */
	public void setIsBlack(String isBlack) {
		this.isBlack = isBlack;
	}
	public String getFacePicPath() {
		return facePicPath;
	}
	public void setFacePicPath(String facePicPath) {
		this.facePicPath = facePicPath;
	}
	public String getSignPicPath() {
		return signPicPath;
	}
	public void setSignPicPath(String signPicPath) {
		this.signPicPath = signPicPath;
	}
	public String getRiskRet() {
		return riskRet;
	}
	public void setRiskRet(String riskRet) {
		this.riskRet = riskRet;
	}
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	public int getOpenStep() {
		return openStep;
	}
	public void setOpenStep(int openStep) {
		this.openStep = openStep;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getInvestorAccountId() {
		return investorAccountId;
	}

	public void setInvestorAccountId(String investorAccountId) {
		this.investorAccountId = investorAccountId;
	}
}
