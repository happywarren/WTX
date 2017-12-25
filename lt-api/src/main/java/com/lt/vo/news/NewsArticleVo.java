package com.lt.vo.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 描述:新闻列表使用的封装类
 * @author XieZhibing
 * @date 2017年2月3日 下午3:40:55
 * @version <b>1.0.0</b>
 */
public class NewsArticleVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;// 新闻ID

	private String title;// 新闻标题

	private String subTitle;// 新闻子标题

	private Integer section;// 栏目标识

	private String sectionName;// 新闻所属栏目名称

	private String keyword;// 新闻关键词

	private String summary;// 新闻简介

	private String outSourceUrl;// 外部资源连接

	private String outSourceName;// 外部来源名称

	private Integer sourceType;// 资源类型0本地文章内容1外部文章链接

	private String bannerUrl;// 新闻banner地址

	private Integer permitComment;// 是否允许评论0不允许1允许

	private String content;// 新闻内容

	private String plateName;// 标签名称

	private Integer readCount;// 阅读量

	private Integer cmtCount;// 评论数量

	private String createDate;// 发布时间

	private String modifyDate;// 修改日期

	private Integer top; // 是否置顶 0

	private Integer hot; // 是否热门

	private Date hotEndTime;// 热门有效期，是热门时有效

	private Integer picFlag; // 图片标示，列表图片 0无图，1小图 ， 2大图

	/**
	 * 创建人
	 */
	private String creater;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCmtCount() {
		return cmtCount;
	}

	public void setCmtCount(Integer cmtCount) {
		this.cmtCount = cmtCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getOutSourceUrl() {
		return outSourceUrl;
	}

	public void setOutSourceUrl(String outSourceUrl) {
		this.outSourceUrl = outSourceUrl;
	}

	public String getOutSourceName() {
		return outSourceName;
	}

	public void setOutSourceName(String outSourceName) {
		this.outSourceName = outSourceName;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public Integer getPermitComment() {
		return permitComment;
	}

	public void setPermitComment(Integer permitComment) {
		this.permitComment = permitComment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPlateName() {
		return plateName;
	}

	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public Date getHotEndTime() {
		return hotEndTime;
	}

	public void setHotEndTime(Date hotEndTime) {
		this.hotEndTime = hotEndTime;
	}

	public Integer getPicFlag() {
		return picFlag;
	}

	public void setPicFlag(Integer picFlag) {
		this.picFlag = picFlag;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
}
