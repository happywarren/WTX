package com.lt.manager.bean.news;

/**   
* 项目名称：lt-manager   
* 类名称：NewsArticleCmtReplyVo   
* 类描述：文章中附带的阅读数和评论数   
* 创建人：yuanxin   
* 创建时间：2017年2月13日 下午2:51:56      
*/
public class NewsArticleCmtReplyVo extends NewsArticleVo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 文章简介*/
	private String summary;
	/** 初始化评论量*/
	private Integer realCommentCount;
	/** 初始化回复量*/
	private Integer realReplyCount;
	/** 初始化阅读量*/
	private Integer initReadCount;
	/** 实际阅读量*/
	private Integer realReadCount;
	/** 初始化点赞量*/
	private Integer initLikeCount;
	/** 实际点赞量*/
	private Integer realLikeCount;
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
	 * @return the realReplyCount
	 */
	public Integer getRealReplyCount() {
		return realReplyCount;
	}
	/**
	 * @param realReplyCount the realReplyCount to set
	 */
	public void setRealReplyCount(Integer realReplyCount) {
		this.realReplyCount = realReplyCount;
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
	
}
