package com.lt.controller.user.bean;

import java.util.Date;

import com.lt.model.user.UserContant;

/**   
* 项目名称：lt-transfer   
* 类名称：UserBase   
* 类描述：用户基本信息   
* 创建人：yuanxin   
* 创建时间：2017年5月16日 下午2:34:09      
*/
public class UserBase {
	/** 用户id*/
	public String user_id;
	/** 用户姓名*/
	public String user_name;
	/** 用户密码*/
	public String passwd;
	/** 用户昵称*/
	public String nick_name;
	/** 用户身份证 正面*/
	public String id_pic_positive;
	/** 用户身份证 反面*/
	public String id_pic_reverse;
	/** 用户银行卡图片*/
	public String bankcard_pic;
	/** 用户身份证*/
	public String id_card_num;
	/** 用户开户状态*/
	public Integer open_account_status;
	/** 用户实名绑定状态*/
	public Integer real_name_status;
	/** 用户手机号*/
	public String tele;
	
	public Integer tele_status;
	/** 用户注册渠道*/
	public Integer reg_source;
	/** 用户注册方式*/
	public Integer reg_mode;
	/** 用户 IP*/
	public String ip ;
	/** 用户头像*/
	public String head_pic ;
	/**注册设备版本*/
	public String device_version;
	/** 用户imei*/
	public String device_imei;
	/**注册设备*/
	public String device_model;
	/** 软件版本*/
	public String version;
	/** 用户注册时间*/
	public Date create_date;
	/** 用户状态*/
	public Integer status = 0 ;

	public UserBase(){
		
	}
	
	public UserBase(UserOldBaseInfo baseInfo){
		
		this.user_id = baseInfo.getUserId();
		this.user_name = baseInfo.getUser_name();
		this.passwd = baseInfo.getPassword();
		this.nick_name = baseInfo.getNick_name();
		this.id_pic_positive = baseInfo.getImage_1();
		this.id_pic_reverse = baseInfo.getImage_2();
		this.bankcard_pic = baseInfo.getImage_3();
		this.id_card_num = baseInfo.getId_card_num();
		// 老系统 0 为 未审核 1 为已审核 新系统 2 为已审核 0 未审核
		this.real_name_status = baseInfo.getReal_status() == 1 ? 2 : 0;
		this.tele = baseInfo.getTele();	
		this.tele_status = baseInfo.getTele_status();
		this.reg_source = baseInfo.getReg_source();
		this.reg_mode = 1; // 默认为手机注册
		this.ip = baseInfo.getIp();
		this.head_pic = baseInfo.getHead_pic();
		this.device_imei = baseInfo.getDevice_imei();
		this.device_model = baseInfo.getDevice_model();
		this.device_version = baseInfo.getDevice_version();
		this.version = baseInfo.getVersion();
		this.create_date = baseInfo.getCreate_date();
		
		if(baseInfo.getBank_num() != null && !baseInfo.getBank_num().equals("") && 
				baseInfo.getTele()!=null && !baseInfo.getTele().equals("")
				&& baseInfo.getId_card_num()!=null && !baseInfo.getId_card_num().equals("")){
			this.open_account_status = 1 ;
		}else{
			this.open_account_status = 0 ;
		}
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
	 * @return the id_pic_positive
	 */
	public String getId_pic_positive() {
		return id_pic_positive;
	}

	/**
	 * @param id_pic_positive the id_pic_positive to set
	 */
	public void setId_pic_positive(String id_pic_positive) {
		this.id_pic_positive = id_pic_positive;
	}

	/**
	 * @return the id_pic_reverse
	 */
	public String getId_pic_reverse() {
		return id_pic_reverse;
	}

	/**
	 * @param id_pic_reverse the id_pic_reverse to set
	 */
	public void setId_pic_reverse(String id_pic_reverse) {
		this.id_pic_reverse = id_pic_reverse;
	}

	/**
	 * @return the bankcard_pic
	 */
	public String getBankcard_pic() {
		return bankcard_pic;
	}

	/**
	 * @param bankcard_pic the bankcard_pic to set
	 */
	public void setBankcard_pic(String bankcard_pic) {
		this.bankcard_pic = bankcard_pic;
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
	 * @return the open_account_status
	 */
	public Integer getOpen_account_status() {
		return open_account_status;
	}

	/**
	 * @param open_account_status the open_account_status to set
	 */
	public void setOpen_account_status(Integer open_account_status) {
		this.open_account_status = open_account_status;
	}

	/**
	 * @return the real_name_status
	 */
	public Integer getReal_name_status() {
		return real_name_status;
	}

	/**
	 * @param real_name_status the real_name_status to set
	 */
	public void setReal_name_status(Integer real_name_status) {
		this.real_name_status = real_name_status;
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
	 * @return the reg_mode
	 */
	public Integer getReg_mode() {
		return reg_mode;
	}

	/**
	 * @param reg_mode the reg_mode to set
	 */
	public void setReg_mode(Integer reg_mode) {
		this.reg_mode = reg_mode;
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
	
}
