package com.lt.manager.bean.promote;

import com.lt.manager.bean.BaseBean;

/**
 * 推广vo
 * @author jingwb
 *
 */
public class PromoteParamVo extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3727404914508916157L;
	
	private String userId;
	private String nickName;
	private String userName;
	private String tele;
	private Integer levelId;
	private String level;
	/**注册用户数*/
	private Integer firstRegisterCount;
	/**交易用户数*/
	private Integer firstTraderCount;
	/**一层手数*/
	private Integer firstHandCount;
	/**一层佣金*/
	private Double firstCommision;
	/**二层手数*/
	private Integer secondHandCount;
	/**二层佣金*/
	private Double secondCommision;
	/**已结算佣金*/
	private Double balanceCommision;
	/**已转现佣金*/
	private Double withdrawCommision;
	/**佣金余额*/
	private Double commisionBalance;
	/**一层充值用户数*/
	private Integer firstRechargerCount;
	/**一层充值金额*/
	private Double firstRechargeAmount;
	/**一层交易金额*/
	private Double firstTradeAmount;
	private String brancherNickName;//下线昵称
	private Integer brancherUserId;//下线用户id
	private Double rechargeAmount;
	private Integer handCount;
	private Double tradeAmount;
	private String createDate;
	private String beginTime;
	private String endTime;
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getFirstRegisterCount() {
		return firstRegisterCount;
	}
	public void setFirstRegisterCount(Integer firstRegisterCount) {
		this.firstRegisterCount = firstRegisterCount;
	}
	public Integer getFirstTraderCount() {
		return firstTraderCount;
	}
	public void setFirstTraderCount(Integer firstTraderCount) {
		this.firstTraderCount = firstTraderCount;
	}
	public Integer getFirstHandCount() {
		return firstHandCount;
	}
	public void setFirstHandCount(Integer firstHandCount) {
		this.firstHandCount = firstHandCount;
	}
	public Double getFirstCommision() {
		return firstCommision;
	}
	public void setFirstCommision(Double firstCommision) {
		this.firstCommision = firstCommision;
	}
	public Integer getSecondHandCount() {
		return secondHandCount;
	}
	public void setSecondHandCount(Integer secondHandCount) {
		this.secondHandCount = secondHandCount;
	}
	public Double getSecondCommision() {
		return secondCommision;
	}
	public void setSecondCommision(Double secondCommision) {
		this.secondCommision = secondCommision;
	}
	public Double getBalanceCommision() {
		return balanceCommision;
	}
	public void setBalanceCommision(Double balanceCommision) {
		this.balanceCommision = balanceCommision;
	}
	public Double getWithdrawCommision() {
		return withdrawCommision;
	}
	public void setWithdrawCommision(Double withdrawCommision) {
		this.withdrawCommision = withdrawCommision;
	}
	public Double getCommisionBalance() {
		return commisionBalance;
	}
	public void setCommisionBalance(Double commisionBalance) {
		this.commisionBalance = commisionBalance;
	}
	public Integer getFirstRechargerCount() {
		return firstRechargerCount;
	}
	public void setFirstRechargerCount(Integer firstRechargerCount) {
		this.firstRechargerCount = firstRechargerCount;
	}
	public Double getFirstRechargeAmount() {
		return firstRechargeAmount;
	}
	public void setFirstRechargeAmount(Double firstRechargeAmount) {
		this.firstRechargeAmount = firstRechargeAmount;
	}
	public Double getFirstTradeAmount() {
		return firstTradeAmount;
	}
	public void setFirstTradeAmount(Double firstTradeAmount) {
		this.firstTradeAmount = firstTradeAmount;
	}
	public String getBrancherNickName() {
		return brancherNickName;
	}
	public void setBrancherNickName(String brancherNickName) {
		this.brancherNickName = brancherNickName;
	}
	public Integer getBrancherUserId() {
		return brancherUserId;
	}
	public void setBrancherUserId(Integer brancherUserId) {
		this.brancherUserId = brancherUserId;
	}
	public Double getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public Integer getHandCount() {
		return handCount;
	}
	public void setHandCount(Integer handCount) {
		this.handCount = handCount;
	}
	public Double getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
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
	
}
