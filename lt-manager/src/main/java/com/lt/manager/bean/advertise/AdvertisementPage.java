package com.lt.manager.bean.advertise;

import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**   
* 项目名称：lt-manager   
* 类名称：AdvertisementPage   
* 类描述：广告图分页数据   
* 创建人：yuanxin   
* 创建时间：2017年7月4日 下午2:27:18      
*/
public class AdvertisementPage extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 广告id
	 */
	private String adverId;
	/**
	 * 活动标题
	 */
	private String title;
	
	/**
	 * 发布人
	 */
	private String createUserName;
	
	/**
	 * 权重
	 */
	private Integer weight;

	/**
	 * 横幅
	 */
	private String bannerUrl;

	/**
	 * 广告类型[1:首页弹窗;2:活动广告]
	 */
	private Integer adverType;

	/**
	 * 添加人ID
	 */
	private String createrUserId;

	/**
	 * 添加时间
	 */
	private Date createDate;

	/**
	 * 展示(0:不展示;1：展示)
	 */
	private Integer isShow;

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
	 * @return the bannerUrl
	 */
	public String getBannerUrl() {
		return bannerUrl;
	}

	/**
	 * @param bannerUrl the bannerUrl to set
	 */
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
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
	 * @return the createrUserId
	 */
	public String getCreaterUserId() {
		return createrUserId;
	}

	/**
	 * @param createrUserId the createrUserId to set
	 */
	public void setCreaterUserId(String createrUserId) {
		this.createrUserId = createrUserId;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the isShow
	 */
	public Integer getIsShow() {
		return isShow;
	}

	/**
	 * @param isShow the isShow to set
	 */
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
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
	
}
