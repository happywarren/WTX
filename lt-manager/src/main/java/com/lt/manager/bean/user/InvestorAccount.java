package com.lt.manager.bean.user;


import com.lt.manager.bean.BaseBean;

/**
 *
 * 描述: 用户列表页面返回对象
 *
 * @author  李长有
 * @created 2016年11月28日 下午21:30
 * @since   v1.0.0
 */
public class InvestorAccount extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  券商账户ID
	 */
	private String userId;
	/**
	 *  账户名称
	 */
	private String accountName;	
	
	/**昵称*/
	private String nickName;
	
	/**
	 *  账户密码     
	 */
	private String passwd;	
	
	
	/**
	 *  强制状态    
	 */
	private Integer forceStatus;	
	
	
	/**
	 *  备注
	 */
	private String remark;
	
	/**
	 *  证券账户编码
	 */
	private String securityCode;

	/**
	 *  在线状态
	 */
	private Integer onlineStatus;

	/**
	 *  是否开启
	 */
	private Integer isOpen;
	/**
	 *  账户权值
	 */
	private Integer weight;
	/**
	 *  账户类型 内盘 外盘
	 */
	private Integer plateType;
	
	/**
	 *  服务器IP地址
	 */
	private String serverIp;
	/**
	 *  服务器端口号
	 */
	private String serverPort;
	
	/**
	 *  关闭阈值
	 */
	private Double closeValue;
	
	/**
	 *  开启阈值
	 */
	private Double openValue;
	
	/**
	 *  启动资金
	 */
	private Double startFund;
	/**
	 *  账户余额
	 */
	private Double amount;
	/**
	 *  券商总收入
	 */
	private Double totalProfit;
	/**
	 *  券商总亏损
	 */
	private Double totalLess;
	/**
	 *  券商充值总额
	 */
	private Double totalIn;
	/**
	 *  券商提现总额
	 */
	private Double totalOut;
	/**
	 *  券商提现总额
	 */
	private String createDate;
	/***
	 * 最小权重
	 */
	private Integer minWeight;
	/**
	 * 最大权重
	 */
	private Integer maxWeight;
	
	private String updateDate;
	
	/**券商id**/
	private String investorId;
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Integer getForceStatus() {
		return forceStatus;
	}
	public void setForceStatus(Integer forceStatus) {
		this.forceStatus = forceStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public Integer getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	public Integer getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getPlateType() {
		return plateType;
	}
	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public Double getCloseValue() {
		return closeValue;
	}
	public void setCloseValue(Double closeValue) {
		this.closeValue = closeValue;
	}
	public Double getOpenValue() {
		return openValue;
	}
	public void setOpenValue(Double openValue) {
		this.openValue = openValue;
	}
	public Double getStartFund() {
		return startFund;
	}
	public void setStartFund(Double startFund) {
		this.startFund = startFund;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}
	public Double getTotalLess() {
		return totalLess;
	}
	public void setTotalLess(Double totalLess) {
		this.totalLess = totalLess;
	}
	public Double getTotalIn() {
		return totalIn;
	}
	public void setTotalIn(Double totalIn) {
		this.totalIn = totalIn;
	}
	public Double getTotalOut() {
		return totalOut;
	}
	public void setTotalOut(Double totalOut) {
		this.totalOut = totalOut;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Integer getMinWeight() {
		return minWeight;
	}
	public void setMinWeight(Integer minWeight) {
		this.minWeight = minWeight;
	}
	public Integer getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(Integer maxWeight) {
		this.maxWeight = maxWeight;
	}
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	
 }


