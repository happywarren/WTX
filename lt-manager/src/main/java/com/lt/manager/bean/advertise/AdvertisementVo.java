package com.lt.manager.bean.advertise;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**   
* 项目名称：lt-manager   
* 类名称：AdvertisementVo   
* 类描述：  活动广告查询类 
* 创建人：yuanxin   
* 创建时间：2017年7月4日 上午10:23:42      
*/
public class AdvertisementVo extends BaseBean{
	
	/** 广告图id*/
	private String adverId ;
	/** 标题*/
	private String title ;
	/** 创建开始时间*/
	private String createBeginDate ;
	/** 创建结束时间*/
	private String createEndDate ;
	/** 创建用户*/
	private String createUserId ;
	/** 类型*/
	private Integer adverType ;
	/** 是否展示*/
	private String isShow;
	/** 权重*/
	private Integer weight ;
	/** 发布人名称*/
	private String createUserName;
	
	public AdvertisementVo(){
		
	}
	
	public AdvertisementVo(String adverId){
		this.adverId = adverId ;
	}
	
	public AdvertisementVo(Integer weight ,Integer adverType){
		this.weight = weight ;
		this.adverType = adverType ;
	}
	/**
	 * @return the adverId
	 */
	public String getAdverId() {
		return adverId;
	}
	/**
	 * @param adverId the adverId to set
	 */
	public void setAdverId(String adverId) {
		this.adverId = adverId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the createBeginDate
	 */
	public String getCreateBeginDate() {
		return createBeginDate;
	}

	/**
	 * @param createBeginDate the createBeginDate to set
	 */
	public void setCreateBeginDate(String createBeginDate) {
		this.createBeginDate = createBeginDate;
	}

	/**
	 * @return the createEndDate
	 */
	public String getCreateEndDate() {
		return createEndDate;
	}

	/**
	 * @param createEndDate the createEndDate to set
	 */
	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}

	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the adverType
	 */
	public Integer getAdverType() {
		return adverType;
	}
	/**
	 * @param adverType the adverType to set
	 */
	public void setAdverType(Integer adverType) {
		this.adverType = adverType;
	}
	/**
	 * @return the isShow
	 */
	public String getIsShow() {
		return isShow;
	}
	/**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
