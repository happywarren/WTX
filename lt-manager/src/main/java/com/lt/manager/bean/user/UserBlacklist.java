package com.lt.manager.bean.user;

import com.lt.manager.bean.BaseBean;
import java.util.Date;
/**
 * 
 * <br>
 * <b>功能：</b>UserBlacklistEntity<br>
 */
public class UserBlacklist extends BaseBean {
	
		private java.lang.String regIp;//   注册IP	private java.lang.String loginIp;//   登录IP	private java.lang.String regImei;//   注册IMEI	private java.lang.String loginImei;//   登录IMEI
	private java.lang.String createDate;//   登录IMEI	public java.lang.String getRegIp() {	    return this.regIp;	}	public void setRegIp(java.lang.String regIp) {	    this.regIp=regIp;	}	public java.lang.String getLoginIp() {	    return this.loginIp;	}	public void setLoginIp(java.lang.String loginIp) {	    this.loginIp=loginIp;	}	public java.lang.String getRegImei() {	    return this.regImei;	}	public void setRegImei(java.lang.String regImei) {	    this.regImei=regImei;	}	public java.lang.String getLoginImei() {	    return this.loginImei;	}	public void setLoginImei(java.lang.String loginImei) {	    this.loginImei=loginImei;	}
	public java.lang.String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.lang.String createDate) {
		this.createDate = createDate;
	}
	
}

