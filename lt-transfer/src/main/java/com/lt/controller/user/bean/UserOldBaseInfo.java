package com.lt.controller.user.bean;

import java.util.Date;

/**   
* 项目名称：lt-transfer   
* 类名称：UserBaseInfo   
* 类描述： 老系统用户汇总基本信息(包含基本信息，注册信息，银行卡信息)  
* 创建人：yuanxin   
* 创建时间：2017年5月16日 下午1:31:00      
*/
public class UserOldBaseInfo {
	
	/** 用户id*/
	public String userId;
	/** 用户头像*/
	public String head_pic ;
	/** 用户电话*/
	public String tele;
	/** 用户电话绑定状态*/
	public Integer tele_status;
	/** 用户昵称*/
	public String nick_name;
	/** 密码*/
	public String password;
	/** 用户姓名*/
	public String user_name;
	/** 用户证件号*/
	public String id_card_num;
	/** 实名状态*/
	public Integer real_status ;
	/** 卡号*/
	public String bank_num ;
	/** 开户行*/
	public String bank_name;
	/** 是否默认银行*/
	public Integer is_default_bank;
	/** 银行信息绑定状态*/
	public Integer bank_status ;
	/** 身份证正面*/
	public String image_1;
	/** 身份证反面*/
	public String image_2;
	/** 银行卡正面照片*/
	public String image_3;
	/** */
	public Date create_date;
	/** 注册方式*/
	public String reg_mode ;
	/** 注册渠道*/
	public Integer reg_source;
	/** 注册IMEI*/
	public String device_imei;
	/** 注册ip*/
	public String ip;
	/** 注册设备型号*/
	public String device_model;
	/** 注册版本*/
	public String device_version;
	/** 软件版本*/
	public String version;
	
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
	 * @return the head_pic
	 */
	public String getHead_pic() {
		return head_pic;
	}
	/**
	 * @param head_pic the head_pic to set
	 */
	public void setHead_pic(String head_pic) {
		this.head_pic = head_pic;
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
	 * @return the tele_status
	 */
	public Integer getTele_status() {
		return tele_status;
	}
	/**
	 * @param tele_status the tele_status to set
	 */
	public void setTele_status(Integer tele_status) {
		this.tele_status = tele_status;
	}
	/**
	 * @return the nick_name
	 */
	public String getNick_name() {
		return nick_name;
	}
	/**
	 * @param nick_name the nick_name to set
	 */
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}
	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	/**
	 * @return the id_card_num
	 */
	public String getId_card_num() {
		return id_card_num;
	}
	/**
	 * @param id_card_num the id_card_num to set
	 */
	public void setId_card_num(String id_card_num) {
		this.id_card_num = id_card_num;
	}
	/**
	 * @return the real_status
	 */
	public Integer getReal_status() {
		return real_status;
	}
	/**
	 * @param real_status the real_status to set
	 */
	public void setReal_status(Integer real_status) {
		this.real_status = real_status;
	}
	/**
	 * @return the bank_num
	 */
	public String getBank_num() {
		return bank_num;
	}
	/**
	 * @param bank_num the bank_num to set
	 */
	public void setBank_num(String bank_num) {
		this.bank_num = bank_num;
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
	/**
	 * @return the is_default_bank
	 */
	public Integer getIs_default_bank() {
		return is_default_bank;
	}
	/**
	 * @param is_default_bank the is_default_bank to set
	 */
	public void setIs_default_bank(Integer is_default_bank) {
		this.is_default_bank = is_default_bank;
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
	 * @return the image_1
	 */
	public String getImage_1() {
		return image_1;
	}
	/**
	 * @param image_1 the image_1 to set
	 */
	public void setImage_1(String image_1) {
		this.image_1 = image_1;
	}
	/**
	 * @return the image_2
	 */
	public String getImage_2() {
		return image_2;
	}
	/**
	 * @param image_2 the image_2 to set
	 */
	public void setImage_2(String image_2) {
		this.image_2 = image_2;
	}
	/**
	 * @return the image_3
	 */
	public String getImage_3() {
		return image_3;
	}
	/**
	 * @param image_3 the image_3 to set
	 */
	public void setImage_3(String image_3) {
		this.image_3 = image_3;
	}
	/**
	 * @return the create_date
	 */
	public Date getCreate_date() {
		return create_date;
	}
	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	/**
	 * @return the reg_mode
	 */
	public String getReg_mode() {
		return reg_mode;
	}
	/**
	 * @param reg_mode the reg_mode to set
	 */
	public void setReg_mode(String reg_mode) {
		this.reg_mode = reg_mode;
	}
	/**
	 * @return the reg_source
	 */
	public Integer getReg_source() {
		return reg_source;
	}
	/**
	 * @param reg_source the reg_source to set
	 */
	public void setReg_source(Integer reg_source) {
		this.reg_source = reg_source;
	}
	/**
	 * @return the device_imei
	 */
	public String getDevice_imei() {
		return device_imei;
	}
	/**
	 * @param device_imei the device_imei to set
	 */
	public void setDevice_imei(String device_imei) {
		this.device_imei = device_imei;
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
	 * @return the device_model
	 */
	public String getDevice_model() {
		return device_model;
	}
	/**
	 * @param device_model the device_model to set
	 */
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	/**
	 * @return the device_version
	 */
	public String getDevice_version() {
		return device_version;
	}
	/**
	 * @param device_version the device_version to set
	 */
	public void setDevice_version(String device_version) {
		this.device_version = device_version;
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
	
}
