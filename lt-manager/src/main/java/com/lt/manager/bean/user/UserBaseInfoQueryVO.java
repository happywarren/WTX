package com.lt.manager.bean.user;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**   
* 项目名称：lt-manager   
* 类名称：UserBaseInfoQueryVO   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月6日 下午3:27:51      
*/
public class UserBaseInfoQueryVO extends BaseBean {
	
	private String userId;
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
	 * 身份证
	 */
	private String idCardNum;
	/** 
	 * 个性签名
	 */
	private String personalSign;
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
	private Integer setGesture;
	/**
	 * 身份证号码
	 */
	private String cardNum;
	/**
	 * 上级推广员ID
	 */
	
	private String  promoteId;
	/**
	 * 手机号 
	 */
	private String tele;
	
	/**
	 * 开户状态
	 */
	private Integer openAccountStatus;
	/**
	 * 手机绑定状态
	 */
	private Integer teleStatus ;
	
	/**
	 *运营商ID
	 */
	private Integer  reg_operator;
	/**
	 * 注册渠道
	 */
	private Integer regSource;
	
	/**
	 * 注册模式 PC 手机 微信
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
	/**
	 * 设备类型 iPhone 三星
	 */
	private String deviceModel;

	/**
	 * 终端设备版本
	 */
	private String deviceVersion;
	/**
	 * 注册终端IMEI
	 */
	private String deviceImei;
	/**
	 * 软件版本
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
	 * 离开天数最大值
	 */
	private Integer leftDaysMax ;
	/**
	 * 离开天数最小值
	 */
	private Integer leftDaysMin ;
	
	/**
	 * 上次登录的IMEI
	 */
	private String lastLoginImei;
	/**
	 * 注册运营商
	 */
	private Integer regOperator ; 
	/**
	 * 注册日期 查询值
	 */
	private String startTime; 
	
	private String endTime; 
	
	/**
	 * 操作日期 查询值
	 */
	private String operStartTime; 
	
	private String operEndTime; 
	
	/**
	 * 修改日期
	 */
	private Date modify_date;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 用户可用余额
	 */
	private Double usedAmount;
	
	/**
	 * 总手续费
	 */
	private Double totalCounterFee;
	
	/**
	 * 资金类型（0 现金   1积分）
	 */
	private Integer fundType ;
	
	/**
	 * 资金信息
	 * 参考CashColumnEnum.java的code
	 */
	private String fundInfo ;
	
	/**
	 * 用户归属
	 */
	private String  promoteNick;
	
	/**
	 * 资金范围
	 */
	private Double fundScope ;

	/**
	 * 最大值
	 */
	private Double minMoney;
	/**
	 * 最小值
	 */
	private Double maxMoney;
	
	/** 
	 * 操作系统名
	 */
	private String systemName;
	/**
	 * 选择现金还是积分表
	 */
	private String cashOrScoreTable;
	/** 
	 * 申请实名时间
	 */
	private String applyTime;
	/** 
	 * 实名通过时间
	 */
	private Integer isSetGesturePwd;
	
	/** 
	 * 实名通过时间
	 */
	private Integer realNameStatus;
	
	/** 
	 * 用户创建时间
	 */
	 private String createDate;
	 
	 private String finishTime;
	 
	 private Integer creditValue;
	 private String  modifyDate;
	 /**
	  * 用户是否黑名单
	  */
	 private Integer isBlack;
	 
	 /**服务code*/
	 private String serviceCode;
	 
	 private Integer tagId;
	 
	private String col;//列明
	private String dir;//排序规则
	

	/** 身份证反面链接*/
	private String idPicReverse;
	/** 身份证正面链接*/
	private String idPicPositive;
	/**人脸识别照片路径* */
	private String facePicPath;
	/** 用户签名图片路径* */
	private String signPicPath;
	/**银行卡图片**/
	private String bankCardPic;
	/**风险等级**/
	private String riskLevel;

	/**
	 * add 梅传颂
	 * 券商id
	 */
	private String investorAccountId;

    //修改人 yanzhenyu
    //品牌 id
    private String brandId;

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
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}
	/**
	 * @param passwd the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
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
	 * @return the sex
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
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
	 * @return the setGesture
	 */
	public Integer getSetGesture() {
		return setGesture;
	}
	/**
	 * @param setGesture the setGesture to set
	 */
	public void setSetGesture(Integer setGesture) {
		this.setGesture = setGesture;
	}
	/**
	 * @return the cardNum
	 */
	public String getCardNum() {
		return cardNum;
	}
	/**
	 * @param cardNum the cardNum to set
	 */
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	/**
	 * @return the promoteId
	 */
	public String getPromoteId() {
		return promoteId;
	}
	/**
	 * @param promoteId the promoteId to set
	 */
	public void setPromoteId(String promoteId) {
		this.promoteId = promoteId;
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
	 * @return the reg_operator
	 */
	public Integer getReg_operator() {
		return reg_operator;
	}
	/**
	 * @param reg_operator the reg_operator to set
	 */
	public void setReg_operator(Integer reg_operator) {
		this.reg_operator = reg_operator;
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
	 * @return the userGrade
	 */
	public String getUserGrade() {
		return userGrade;
	}
	/**
	 * @param userGrade the userGrade to set
	 */
	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
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
	 * @return the leftDaysMax
	 */
	public Integer getLeftDaysMax() {
		return leftDaysMax;
	}
	/**
	 * @param leftDaysMax the leftDaysMax to set
	 */
	public void setLeftDaysMax(Integer leftDaysMax) {
		this.leftDaysMax = leftDaysMax;
	}
	/**
	 * @return the leftDaysMin
	 */
	public Integer getLeftDaysMin() {
		return leftDaysMin;
	}
	/**
	 * @param leftDaysMin the leftDaysMin to set
	 */
	public void setLeftDaysMin(Integer leftDaysMin) {
		this.leftDaysMin = leftDaysMin;
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
	 * @return the regOperator
	 */
	public Integer getRegOperator() {
		return regOperator;
	}
	/**
	 * @param regOperator the regOperator to set
	 */
	public void setRegOperator(Integer regOperator) {
		this.regOperator = regOperator;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the operStartTime
	 */
	public String getOperStartTime() {
		return operStartTime;
	}
	/**
	 * @param operStartTime the operStartTime to set
	 */
	public void setOperStartTime(String operStartTime) {
		this.operStartTime = operStartTime;
	}
	/**
	 * @return the operEndTime
	 */
	public String getOperEndTime() {
		return operEndTime;
	}
	/**
	 * @param operEndTime the operEndTime to set
	 */
	public void setOperEndTime(String operEndTime) {
		this.operEndTime = operEndTime;
	}
	/**
	 * @return the modify_date
	 */
	public Date getModify_date() {
		return modify_date;
	}
	/**
	 * @param modify_date the modify_date to set
	 */
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
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
	 * @return the usedAmount
	 */
	public Double getUsedAmount() {
		return usedAmount;
	}
	/**
	 * @param usedAmount the usedAmount to set
	 */
	public void setUsedAmount(Double usedAmount) {
		this.usedAmount = usedAmount;
	}
	/**
	 * @return the totalCounterFee
	 */
	public Double getTotalCounterFee() {
		return totalCounterFee;
	}
	/**
	 * @param totalCounterFee the totalCounterFee to set
	 */
	public void setTotalCounterFee(Double totalCounterFee) {
		this.totalCounterFee = totalCounterFee;
	}
	/**
	 * @return the fundType
	 */
	public Integer getFundType() {
		return fundType;
	}
	/**
	 * @param fundType the fundType to set
	 */
	public void setFundType(Integer fundType) {
		this.fundType = fundType;
	}
	/**
	 * @return the fundInfo
	 */
	public String getFundInfo() {
		return fundInfo;
	}
	/**
	 * @param fundInfo the fundInfo to set
	 */
	public void setFundInfo(String fundInfo) {
		this.fundInfo = fundInfo;
	}
	/**
	 * @return the promoteNick
	 */
	public String getPromoteNick() {
		return promoteNick;
	}
	/**
	 * @param promoteNick the promoteNick to set
	 */
	public void setPromoteNick(String promoteNick) {
		this.promoteNick = promoteNick;
	}
	/**
	 * @return the fundScope
	 */
	public Double getFundScope() {
		return fundScope;
	}
	/**
	 * @param fundScope the fundScope to set
	 */
	public void setFundScope(Double fundScope) {
		this.fundScope = fundScope;
	}
	/**
	 * @return the minMoney
	 */
	public Double getMinMoney() {
		return minMoney;
	}
	/**
	 * @param minMoney the minMoney to set
	 */
	public void setMinMoney(Double minMoney) {
		this.minMoney = minMoney;
	}
	/**
	 * @return the maxMoney
	 */
	public Double getMaxMoney() {
		return maxMoney;
	}
	/**
	 * @param maxMoney the maxMoney to set
	 */
	public void setMaxMoney(Double maxMoney) {
		this.maxMoney = maxMoney;
	}
	/**
	 * @return the systemName
	 */
	public String getSystemName() {
		return systemName;
	}
	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	/**
	 * @return the cashOrScoreTable
	 */
	public String getCashOrScoreTable() {
		return cashOrScoreTable;
	}
	/**
	 * @param cashOrScoreTable the cashOrScoreTable to set
	 */
	public void setCashOrScoreTable(String cashOrScoreTable) {
		this.cashOrScoreTable = cashOrScoreTable;
	}
	/**
	 * @return the applyTime
	 */
	public String getApplyTime() {
		return applyTime;
	}
	/**
	 * @param applyTime the applyTime to set
	 */
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
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
	 * @return the realNameStatus
	 */
	public Integer getRealNameStatus() {
		return realNameStatus;
	}
	/**
	 * @param realNameStatus the realNameStatus to set
	 */
	public void setRealNameStatus(Integer realNameStatus) {
		this.realNameStatus = realNameStatus;
	}
	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the finishTime
	 */
	public String getFinishTime() {
		return finishTime;
	}
	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	/**
	 * @return the creditValue
	 */
	public Integer getCreditValue() {
		return creditValue;
	}
	/**
	 * @param creditValue the creditValue to set
	 */
	public void setCreditValue(Integer creditValue) {
		this.creditValue = creditValue;
	}
	/**
	 * @return the modifyDate
	 */
	public String getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
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
	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}
	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	/**
	 * @return the tagId
	 */
	public Integer getTagId() {
		return tagId;
	}
	/**
	 * @param tagId the tagId to set
	 */
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	/**
	 * @return the col
	 */
	public String getCol() {
		return col;
	}
	/**
	 * @param col the col to set
	 */
	public void setCol(String col) {
		this.col = col;
	}
	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
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
	 * @return the facePicPath
	 */
	public String getFacePicPath() {
		return facePicPath;
	}
	/**
	 * @param facePicPath the facePicPath to set
	 */
	public void setFacePicPath(String facePicPath) {
		this.facePicPath = facePicPath;
	}
	/**
	 * @return the signPicPath
	 */
	public String getSignPicPath() {
		return signPicPath;
	}
	/**
	 * @param signPicPath the signPicPath to set
	 */
	public void setSignPicPath(String signPicPath) {
		this.signPicPath = signPicPath;
	}
	/**
	 * @return the bankCardPic
	 */
	public String getBankCardPic() {
		return bankCardPic;
	}
	/**
	 * @param bankCardPic the bankCardPic to set
	 */
	public void setBankCardPic(String bankCardPic) {
		this.bankCardPic = bankCardPic;
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

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
