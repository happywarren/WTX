package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 描述:新闻资讯
 * @author XieZhibing
 * @date 2017年2月3日 下午3:30:30
 * @version <b>1.0.0</b>
 */
public class NewsArticle implements Serializable{
	private Integer id;
	// 新闻标题
	private String title;
	// 新闻副标题
	private String subTitle;
	// 所属栏目
	private Integer section;
	// 关键词，用英文逗号隔开
	private String keyword;
	// 新闻简介
	private String summary;
	// 外部来源名称
	private String outSourceName;
	// 外部来源连接
	private String outSourceUrl;
	// 资源类型0本地文章内容1外部链接内容
	private Integer sourceType;
	// banner图地址
	private String bannerUrl;
	// 是否允许评论 0不允许1允许
	private Integer permitComment;
	// 状态：0待审核，1已审核未发布，2已审核发布中，3已撤销发布
	private Integer status;
	// 权重默认0，数值越大排序越靠前
	private Integer orderWeight;
	// 新闻静态文件地址
	private String filePath;
	// 发布日期
	private Date publishDate;
	// 新闻内容
	private String content;
	// 本条新闻内部系统创建日期
	private Date createDate;
	// 本条新闻内部最后一次修改日期
	private Date modifyDate;
	// 内部创建人ID
	private Integer createStaffId;
	// 内部创建人名字
	private String createStaffName;
	// 内部修改人ID
	private Integer modifyStaffId;
	// 内部修改人名字
	private String modifyStaffName;

	private Integer top; // 是否置顶  0

	private Integer hot; // 是否热门

	private Date hotEndTime;// 热门有效期，是热门时有效

	private Integer picFlag; // 图片标示，列表图片 0无图，1小图 ， 2大图

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public Integer getSection() {
		return section;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getSummary() {
		return summary;
	}

	public String getOutSourceName() {
		return outSourceName;
	}

	public String getOutSourceUrl() {
		return outSourceUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public Integer getPermitComment() {
		return permitComment;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getOrderWeight() {
		return orderWeight;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public Integer getCreateStaffId() {
		return createStaffId;
	}

	public String getCreateStaffName() {
		return createStaffName;
	}

	public Integer getModifyStaffId() {
		return modifyStaffId;
	}

	public String getModifyStaffName() {
		return modifyStaffName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setOutSourceName(String outSourceName) {
		this.outSourceName = outSourceName;
	}

	public void setOutSourceUrl(String outSourceUrl) {
		this.outSourceUrl = outSourceUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public void setPermitComment(Integer permitComment) {
		this.permitComment = permitComment;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setOrderWeight(Integer orderWeight) {
		this.orderWeight = orderWeight;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public void setCreateStaffId(Integer createStaffId) {
		this.createStaffId = createStaffId;
	}

	public void setCreateStaffName(String createStaffName) {
		this.createStaffName = createStaffName;
	}

	public void setModifyStaffId(Integer modifyStaffId) {
		this.modifyStaffId = modifyStaffId;
	}

	public void setModifyStaffName(String modifyStaffName) {
		this.modifyStaffName = modifyStaffName;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
