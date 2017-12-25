package com.lt.controller.user.bean;

/**   
* 项目名称：lt-transfer   
* 类名称：UserBankInfo   
* 类描述：  用户银行卡信息 
* 创建人：yuanxin   
* 创建时间：2017年5月16日 下午4:27:12      
*/
public class UserOldBankInfo {
	
	/** 用户id*/
	public String user_id;
	/** 银行编码*/
	public String bank_code;
	/** 是否默认银行卡*/
	public Integer is_default;
	/** 银行信息绑定状态*/
	public Integer bank_status;
	/** 银行卡号*/
	public String bank_card_num ;
	/** 银行名称*/
	public String bank_name ;
	
	public UserOldBankInfo(){
		
	}
	
	/**
	 * 银行编码未初始化
	 * @param baseInfo
	 */
	public UserOldBankInfo(UserOldBaseInfo baseInfo){
		this.user_id = baseInfo.getUserId();
		this.is_default = baseInfo.getIs_default_bank();
		this.bank_status = baseInfo.getBank_status();
		this.bank_card_num = baseInfo.getBank_num();
		this.bank_name = baseInfo.getBank_name() ;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the bank_code
	 */
	public String getBank_code() {
		return bank_code;
	}
	/**
	 * @param bank_code the bank_code to set
	 */
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	/**
	 * @return the is_default
	 */
	public Integer getIs_default() {
		return is_default;
	}
	/**
	 * @param is_default the is_default to set
	 */
	public void setIs_default(Integer is_default) {
		this.is_default = is_default;
	}
	/**
	 * @return the bank_status
	 */
	public Integer getBank_status() {
		return bank_status;
	}
	/**
	 * @param bank_status the bank_status to set
	 */
	public void setBank_status(Integer bank_status) {
		this.bank_status = bank_status;
	}

	/**
	 * @return the bank_card_num
	 */
	public String getBank_card_num() {
		return bank_card_num;
	}

	/**
	 * @param bank_card_num the bank_card_num to set
	 */
	public void setBank_card_num(String bank_card_num) {
		this.bank_card_num = bank_card_num;
	}

	/**
	 * @return the bank_name
	 */
	public String getBank_name() {
		return bank_name;
	}

	/**
	 * @param bank_name the bank_name to set
	 */
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	
}
