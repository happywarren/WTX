package com.lt.manager.bean.news;


import java.util.Date;

import com.lt.manager.bean.BaseBean;

/**   
* 项目名称：lt-manager   
* 类名称：NewsArticleVo   
* 类描述：文章查询类   
* 创建人：yuanxin   
* 创建时间：2017年2月7日 上午9:18:58      
*/
public class NewsArticleVo extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*--------------------查询结果 beg---------------------*/
	/** 文章id*/
	private Integer newsId;
	/** 栏目*/
	private String sectionName;
	/** 创建时间*/
	private String createDate;
	/** 发布时间*/
	private String publishDate;
	/** 创建用户*/
	private String createStaffName;
	/*--------------------查询结果 end---------------------*/
	
	/** 标签名称*/
	private String plateName;
	/** 标题*/
	private String title;
	/** 状态*/
	private Integer status;
	/** 置顶 0否 1是*/
	private Integer top;
	/**
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 品牌ID
	 */
	private String brandId;
	/**
	 * 发布人
	 */
	private String creater;
	
	/*--------------------查询条件 beg---------------------*/
	/** 栏目*/
	private Integer section;
	/** 发布开始时间*/
	private String publishBeginDate;
	/** 发布结束时间*/
	private String publishEndDate;
	/*--------------------查询条件 end---------------------*/
	/**
	 * @return the id
	 */
	public Integer getNewsId() {
		return newsId;
	}
	/**
	 * @param id the id to set
	 */
	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}
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
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the publicDate
	 */
	public String getPublishDate() {
		return publishDate;
	}
	/**
	 * @param publicDate the publicDate to set
	 */
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
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
	 * @return the top
	 */
	public Integer getTop() {
		return top;
	}
	/**
	 * @param top the top to set
	 */
	public void setTop(Integer top) {
		this.top = top;
	}
	/**
	 * @return the section
	 */
	public Integer getSection() {
		return section;
	}
	/**
	 * @param section the section to set
	 */
	public void setSection(Integer section) {
		this.section = section;
	}
	
	/**
	 * @return the publishBeginDate
	 */
	public String getPublishBeginDate() {
		return publishBeginDate;
	}
	/**
	 * @param publishBeginDate the publishBeginDate to set
	 */
	public void setPublishBeginDate(String publishBeginDate) {
		this.publishBeginDate = publishBeginDate;
	}
	/**
	 * @return the publishEndDate
	 */
	public String getPublishEndDate() {
		return publishEndDate;
	}
	/**
	 * @param publishEndDate the publishEndDate to set
	 */
	public void setPublishEndDate(String publishEndDate) {
		this.publishEndDate = publishEndDate;
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
}
