package com.lt.manager.bean.news;

import java.io.Serializable;

/**   
* 项目名称：lt-manager   
* 类名称：NewsArticleDetail   
* 类描述：文章详情列表   
* 创建人：yuanxin   
* 创建时间：2017年2月13日 上午9:23:36      
*/
public class NewsArticleDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 栏目名称*/
	private String sectionName;
	/** 文章标题*/
	private String title;
	/** 来源名称*/
	private String outSourceName;
	/** 创建人名称*/
	private String createStaffName;
	private Integer createStaffId;
	private String modifyStaffName;
	private Integer modifyStaffId;
	/** 发布时间*/
	private String publishDate;
	/** 图片地址*/
	private String bannerUrl;
	/** 文章内容*/
	private String content;
	/** 标签名称，多个标签以分号;分隔*/
	private String plateName;
	/** 标签id，多个标签以分号;分隔*/
	private String plate;
	/** 文章状态*/
	private Integer status;
	/** 初始化阅读量*/
	private Integer initReadCount;
	/** 初始化点赞数*/
	private Integer initLikeCount;
	/** 实际点赞数*/
	private Integer realLikeCount;
	/** 实际阅读数*/
	private Integer realReadCount;
	/** 实际评论数*/
	private Integer realCommentCount;
	/** 是否允许评论 0 不允许， 1 允许*/
	private Integer permitComment;
	/** 封面图标展示 0 无图 1 小图 2 大图*/
	private Integer picFlag;
	
	
	/**----------------新增文章（开始）---------------------**/
	/** 所属栏目*/
	private Integer sectionId;
	/** 简介*/
	private String summary;
	/** 来源类型*/
	private Integer sourceType;
	/**----------------新增文章（开始）---------------------**/
	
	/**----------------修改文章（开始）---------------------**/
	/** 文章id*/
	private Integer newsArticleId;
	/**
	 * 品牌   按，分割
	 */
	private String brandId;
	/**
	 * 前端显示文章创建人
	 */
	private String creater;
	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}
	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
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
	 * @return the createStaffName
	 */
	public String getCreateStaffName() {
		return createStaffName;
	}
	/**
	 * @param createStaffName the createStaffName to set
	 */
	public void setCreateStaffName(String createStaffName) {
		this.createStaffName = createStaffName;
	}
	
	/**
	 * @return the publishDate
	 */
	public String getPublishDate() {
		return publishDate;
	}
	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
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
	 * @return the plateName
	 */
	public String getPlateName() {
		return plateName;
	}
	/**
	 * @param plateName the plateName to set
	 */
	public void setPlateName(String plateName) {
		this.plateName = plateName;
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
	/**
	 * @return the realLikeCount
	 */
	public Integer getRealLikeCount() {
		return realLikeCount;
	}
	/**
	 * @param realLikeCount the realLikeCount to set
	 */
	public void setRealLikeCount(Integer realLikeCount) {
		this.realLikeCount = realLikeCount;
	}
	/**
	 * @return the realReadCount
	 */
	public Integer getRealReadCount() {
		return realReadCount;
	}
	/**
	 * @param realReadCount the realReadCount to set
	 */
	public void setRealReadCount(Integer realReadCount) {
		this.realReadCount = realReadCount;
	}
	/**
	 * @return the realCommentCount
	 */
	public Integer getRealCommentCount() {
		return realCommentCount;
	}
	/**
	 * @param realCommentCount the realCommentCount to set
	 */
	public void setRealCommentCount(Integer realCommentCount) {
		this.realCommentCount = realCommentCount;
	}
	/**
	 * @return the permitComment
	 */
	public Integer getPermitComment() {
		return permitComment;
	}
	/**
	 * @param permitComment the permitComment to set
	 */
	public void setPermitComment(Integer permitComment) {
		this.permitComment = permitComment;
	}
	/**
	 * @return the picFlag
	 */
	public Integer getPicFlag() {
		return picFlag;
	}
	/**
	 * @param picFlag the picFlag to set
	 */
	public void setPicFlag(Integer picFlag) {
		this.picFlag = picFlag;
	}
	/**
	 * @return the outSourceName
	 */
	public String getOutSourceName() {
		return outSourceName;
	}
	/**
	 * @param outSourceName the outSourceName to set
	 */
	public void setOutSourceName(String outSourceName) {
		this.outSourceName = outSourceName;
	}
	
	/**
	 * @return the initReadCount
	 */
	public Integer getInitReadCount() {
		return initReadCount;
	}
	/**
	 * @param initReadCount the initReadCount to set
	 */
	public void setInitReadCount(Integer initReadCount) {
		this.initReadCount = initReadCount;
	}
	/**
	 * @return the initLikeCount
	 */
	public Integer getInitLikeCount() {
		return initLikeCount;
	}
	/**
	 * @param initLikeCount the initLikeCount to set
	 */
	public void setInitLikeCount(Integer initLikeCount) {
		this.initLikeCount = initLikeCount;
	}
	/**
	 * @return the newsArticleId
	 */
	public Integer getNewsArticleId() {
		return newsArticleId;
	}
	/**
	 * @param newsArticleId the newsArticleId to set
	 */
	public void setNewsArticleId(Integer newsArticleId) {
		this.newsArticleId = newsArticleId;
	}
	/**
	 * @return the sectionId
	 */
	public Integer getSectionId() {
		return sectionId;
	}
	/**
	 * @param sectionId the sectionId to set
	 */
	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the sourceType
	 */
	public Integer getSourceType() {
		return sourceType;
	}
	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
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
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}
	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Integer getCreateStaffId() {
		return createStaffId;
	}

	public void setCreateStaffId(Integer createStaffId) {
		this.createStaffId = createStaffId;
	}

	public String getModifyStaffName() {
		return modifyStaffName;
	}

	public void setModifyStaffName(String modifyStaffName) {
		this.modifyStaffName = modifyStaffName;
	}

	public Integer getModifyStaffId() {
		return modifyStaffId;
	}

	public void setModifyStaffId(Integer modifyStaffId) {
		this.modifyStaffId = modifyStaffId;
	}
}
