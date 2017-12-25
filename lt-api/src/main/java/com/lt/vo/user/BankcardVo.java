package com.lt.vo.user;

import java.io.Serializable;

/**   
* 项目名称：lt-api   
* 类名称：BankcardVo   
* 类描述：  用户绑定的银行卡信息返回实体 
* 创建时间：2017年1月5日 上午9:18:23      
*/
public class BankcardVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id*/
	private Integer id ;
	/** 银行卡号码*/
	private String num;
	/** 银行名称*/
	private String bankName;
	/** 银行图案地址*/
	private String pic;
	/** 是否是默认银行卡*/
	private Integer isDefault;
	/**
	 * 银行卡背景色
	 */
	private String cardBackground;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the pic
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * @param pic the pic to set
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getCardBackground() {
		return cardBackground;
	}
	public void setCardBackground(String cardBackground) {
		this.cardBackground = cardBackground;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
	
}
