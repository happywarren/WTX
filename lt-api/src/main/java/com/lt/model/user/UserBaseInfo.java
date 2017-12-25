package com.lt.model.user;

import java.io.Serializable;
import java.util.Date;

import com.lt.model.user.log.LoggerInfo;

/**
* 项目名称：lt-api
* 类名称：UserBaseInfo
* 类描述：  用户基础信息（基础对象）
* 创建人：yuanxin
* 创建时间：2016年11月28日 下午7:52:04
*/
public class UserBaseInfo extends LoggerInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	/** 用户id*/
	private String userId;
	/** 昵称*/
	private String nickName;
	/** 个性签名*/
	private String personalSign;
	/** 用户状态*/
	private Integer status;
	/** 手机号码*/
	private String tele;
	/** 密码*/
	private String passwd;
	/** 用户头像*/
	private String headPic;

	/** 员工属性 */
	private Integer isStaff;
	/** 手机绑定状态*/
	private Integer teleStatus;
	/** 用户等级 默认0000*/
	private Integer creditGrade;
	/** 用户等级*/
	private Integer userGrade;
	/** 用户开户状态*/
	private Integer openAccountStatus;

	/** 用户真实名称*/
	private String userName;
	/** 身份证*/
	private String idCardNum;
	/** 身份证反面链接*/
	private String idPicReverse;
	/** 身份证正面链接*/
	private String idPicPositive;

	/**品牌 id**/
	private String brandId;

	/**品牌编码**/
	private String brandCode;

	/** 注册渠道*/
	private Integer regSource;
	/** 注册设备型号*/
	private String deviceModel;
	/** 注册设备名称*/
	private Integer systemName;
	/** 注册设备版本*/
	private String deviceVersion;
	/** 注册设备IMIE*/
	private String deviceImei;
	/** 软件版本*/
	private String version;
	/** 注册IP*/
	private String ip;
	/** 注册方式*/
	private Integer regMode;
	/** 注册的运营商*/
	private String regCarrieroperator;

	/** 上次登录时间*/
	private Date lastLoginDate;
	/** 上次登录设备IMEI*/
	private String lastLoginImei;


	/** 创建时间*/
	private Date createDate;
	/** 修改时间*/
	private Date modifyDate;
	/** 备注*/
	private String remark;
	/** 推广员id*/
	private Integer promoteId;
	/** 是否开启手势密码*/
	private Integer isStartGesture;
	/** 是否设置手势密码*/
	private Integer isSetGesturePwd;
	/** 用户是否加入黑名单*/
	private Integer isBlack;
	/**
	 * 人脸识别照片路径
	 */
	private String facePicPath;

	/**
	 * 用户签名图片路径
	 */
	private String signPicPath;

	/**风险结果**/
	private String riskRet;

	/**风险等级**/
	private String riskLevel;

	/*---------------------开始：用户附加属性---------------*/
	/** 用户昵称修改状态 0：未修改 1：已修改*/
	private int nickStatus ;

	/**
	 * 所属券商ID
	 */
	private String investorAccountId;



	public String getInvestorAccountId() {
		return investorAccountId;
	}
	public void setInvestorAccountId(String investorAccountId) {
		this.investorAccountId = investorAccountId;
	}
	/**
	 * @return the nickStatus
	 */
	public int getNickStatus() {
		return nickStatus;
	}
	/**
	 * @param nickStatus the nickStatus to set
	 */
	public void setNickStatus(int nickStatus) {
		this.nickStatus = nickStatus;
	}
	/*---------------------结束：用户附加属性---------------*/


	/**
	 * @return the id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the id to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
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
	 * @return the personalSign
	 */
	public String getPersonalSign() {
		return personalSign;
	}
	/**
	 * @param personalSign the personalSign to set
	 */
	public void setPersonalSign(String personalSign) {
		this.personalSign = personalSign;
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
	 * @return the userGrade
	 */
	public Integer getUserGrade() {
		return userGrade;
	}
	/**
	 * @param userGrade the userGrade to set
	 */
	public void setUserGrade(Integer userGrade) {
		this.userGrade = userGrade;
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
	 * @return the teleStatus
	 */
	public Integer getTeleStatus() {
		return teleStatus;
	}
	/**
	 * @param teleStatus the teleStatus to set
	 */
	public void setTeleStatus(Integer teleStatus) {
		this.teleStatus = teleStatus;
	}
	/**
	 * @return the password
	 */
	public String getPasswd() {
		return passwd;
	}
	/**
	 * @param password the password to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	/**
	 * @return the isStaff
	 */
	public Integer getIsStaff() {
		return isStaff;
	}
	/**
	 * @param isStaff the isStaff to set
	 */
	public void setIsStaff(Integer isStaff) {
		this.isStaff = isStaff;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the regSource
	 */
	public Integer getRegSource() {
		return regSource;
	}
	/**
	 * @param regSource the regSource to set
	 */
	public void setRegSource(Integer regSource) {
		this.regSource = regSource;
	}
	/**
	 * @return the deviceModel
	 */
	public String getDeviceModel() {
		return deviceModel;
	}
	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	/**
	 * @return the deviceVersion
	 */
	public String getDeviceVersion() {
		return deviceVersion;
	}
	/**
	 * @param deviceVersion the deviceVersion to set
	 */
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}
	/**
	 * @return the deviceImei
	 */
	public String getDeviceImei() {
		return deviceImei;
	}
	/**
	 * @param deviceImei the deviceImei to set
	 */
	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the regMode
	 */
	public Integer getRegMode() {
		return regMode;
	}
	/**
	 * @param regMode the regMode to set
	 */
	public void setRegMode(Integer regMode) {
		this.regMode = regMode;
	}
	/**
	 * @return the headPic
	 */
	public String getHeadPic() {
		return headPic;
	}
	/**
	 * @param headPic the headPic to set
	 */
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	/**
	 * @return the lastLoginDate
	 */
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	/**
	 * @param lastLoginDate the lastLoginDate to set
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	/**
	 * @return the lastLoginImei
	 */
	public String getLastLoginImei() {
		return lastLoginImei;
	}
	/**
	 * @param lastLoginImei the lastLoginImei to set
	 */
	public void setLastLoginImei(String lastLoginImei) {
		this.lastLoginImei = lastLoginImei;
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
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * @return the creditGrade
	 */
	public Integer getCreditGrade() {
		return creditGrade;
	}
	/**
	 * @param creditGrade the creditGrade to set
	 */
	public void setCreditGrade(Integer creditGrade) {
		this.creditGrade = creditGrade;
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
	 * @return the idCardNum
	 */
	public String getIdCardNum() {
		return idCardNum;
	}
	/**
	 * @param idCardNum the idCardNum to set
	 */
	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
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
	 * @return the isStartGesture
	 */
	public Integer getIsStartGesture() {
		return isStartGesture;
	}
	/**
	 * @param isStartGesture the isStartGesture to set
	 */
	public void setIsStartGesture(Integer isStartGesture) {
		this.isStartGesture = isStartGesture;
	}
	/**
	 * @return the isSetGesturePwd
	 */
	public Integer getIsSetGesturePwd() {
		return isSetGesturePwd;
	}
	/**
	 * @param isSetGesturePwd the isSetGesturePwd to set
	 */
	public void setIsSetGesturePwd(Integer isSetGesturePwd) {
		this.isSetGesturePwd = isSetGesturePwd;
	}
	/**
	 * @return the promoteId
	 */
	public Integer getPromoteId() {
		return promoteId;
	}
	/**
	 * @param promoteId the promoteId to set
	 */
	public void setPromoteId(Integer promoteId) {
		this.promoteId = promoteId;
	}
	/**
	 * @return the systemName
	 */
	public Integer getSystemName() {
		return systemName;
	}
	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(Integer systemName) {
		this.systemName = systemName;
	}
	/**
	 * @return the regCarrieroperator
	 */
	public String getRegCarrieroperator() {
		return regCarrieroperator;
	}
	/**
	 * @param regCarrieroperator the regCarrieroperator to set
	 */
	public void setRegCarrieroperator(String regCarrieroperator) {
		this.regCarrieroperator = regCarrieroperator;
	}
	/**
	 * @return the idPicReverse
	 */
	public String getIdPicReverse() {
		return idPicReverse;
	}
	/**
	 * @param idPicReverse the idPicReverse to set
	 */
	public void setIdPicReverse(String idPicReverse) {
		this.idPicReverse = idPicReverse;
	}
	/**
	 * @return the idPicPositive
	 */
	public String getIdPicPositive() {
		return idPicPositive;
	}
	/**
	 * @param idPicPositive the idPicPositive to set
	 */
	public void setIdPicPositive(String idPicPositive) {
		this.idPicPositive = idPicPositive;
	}
	/**
	 * @return the openAccountStatus
	 */
	public Integer getOpenAccountStatus() {
		return openAccountStatus;
	}
	/**
	 * @param openAccountStatus the openAccountStatus to set
	 */
	public void setOpenAccountStatus(Integer openAccountStatus) {
		this.openAccountStatus = openAccountStatus;
	}
	/**
	 * @return the isBlack
	 */
	public Integer getIsBlack() {
		return isBlack;
	}
	/**
	 * @param isBlack the isBlack to set
	 */
	public void setIsBlack(Integer isBlack) {
		this.isBlack = isBlack;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}


	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
}
