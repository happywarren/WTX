package com.lt.model.advertise;

import java.io.Serializable;
import java.util.Date;

/**   
* 项目名称：lt-api   
* 类名称：Advertisement   
* 类描述：活动广告对象   
* 创建人：yuanxin   
* 创建时间：2017年7月3日 下午8:09:29      
*/
public class Advertisement implements Serializable{

	/**
	 * Description:
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 广告id
	 */
	private String adverId;
	/**
	 * 活动标题
	 */
	private String title;

	/**
	 * 活动链接
	 */
	private String h5Url;

	/**
	 * 横幅
	 */
	private String bannerUrl;

	/**
	 * 内容展示类型【2:文本;1:h5url 0 不展示】
	 *
	 */
	private Integer contentType;
	
	/**
	 * 活动内容文本
	 */
	private String content;
	
	/**
	 * 权重
	 */
	private Integer weight;

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
	 * 修改人ID
	 */
	private String updateUserId;

	/**
	 * 修改日期
	 */
	private Date updateDate;

	/**
	 * 展示(0:不展示;1：展示)
	 */
	private Integer isShow;
	
	/**
	 * 内容是否展示（0 不展示 1展示）
	 */
	private Integer isShowContent;

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
	 * @return the h5Url
	 */
	public String getH5Url() {
		return h5Url;
	}

	/**
	 * @param h5Url the h5Url to set
	 */
	public void setH5Url(String h5Url) {
		this.h5Url = h5Url;
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
	 * @return the contentType
	 */
	public Integer getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the updateUserId
	 */
	public String getUpdateUserId() {
		return updateUserId;
	}

	/**
	 * @param updateUserId the updateUserId to set
	 */
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
	 * @return the isShowContent
	 */
	public Integer getIsShowContent() {
		return isShowContent;
	}

	/**
	 * @param isShowContent the isShowContent to set
	 */
	public void setIsShowContent(Integer isShowContent) {
		this.isShowContent = isShowContent;
	}
	
	
}
