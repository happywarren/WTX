package com.lt.vo.user;

import java.io.Serializable;

/**
 * 获取账户信息返回实体
 * @author guodw
 *
 */
public class UserAccountInfoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 可用余额
	 */
	private double balance;
	
	/**
	 * 手机号
	 */
	private String tel;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 默认身份证
	 */
	private String idCard;
	
	private String nickName;
	private String headPic;
	
	private String riskLevel;
	private String riskRet;
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getRiskRet() {
		return riskRet;
	}

	public void setRiskRet(String riskRet) {
		this.riskRet = riskRet;
	}
	
}
