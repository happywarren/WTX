package com.lt.manager.bean.news;

import java.io.Serializable;

/**   
* 项目名称：lt-manager   
* 类名称：NewsArticleCmtLikeRlyCount   
* 类描述：统计点赞，评论，回复的数量   
* 创建人：yuanxin   
* 创建时间：2017年2月13日 下午3:27:11      
*/
public class NewsArticleCmtLikeRlyCount implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 统计时间*/
	private String  timeFrame;
	/** 点赞数量*/
	private Integer likeCount;
	/** 评论数量*/
	private Integer commentCount;
	/** 阅读数量*/
	private Integer readCount;
	/**
	 * @return the timeFrame
	 */
	public String getTimeFrame() {
		return timeFrame;
	}
	/**
	 * @param timeFrame the timeFrame to set
	 */
	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}
	/**
	 * @return the likeCount
	 */
	public Integer getLikeCount() {
		return likeCount;
	}
	/**
	 * @param likeCount the likeCount to set
	 */
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	/**
	 * @return the commentCount
	 */
	public Integer getCommentCount() {
		return commentCount;
	}
	/**
	 * @param commentCount the commentCount to set
	 */
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	/**
	 * @return the readCount
	 */
	public Integer getReadCount() {
		return readCount;
	}
	/**
	 * @param readCount the readCount to set
	 */
	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}
	
}
