package com.lt.manager.bean.news;

import java.io.Serializable;

/**   
* 项目名称：lt-manager   
* 类名称：NewsUserCommentReply   
* 类描述： 用户评论回复  
* 创建人：yuanxin   
* 创建时间：2017年2月13日 下午5:43:38      
*/
public class NewsUserCommentReply implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 回复id*/
	private Integer id;
	/** 用户id*/
	private String userId;
	/** 新闻id*/
	private Integer newsId;
	/** 用户昵称*/
	private String nickName;
	/** 回复内容*/
	private String replyContent;
	/** 回复的评论id*/
	private Integer cmtId;
	/** 回复的回复id*/
	private Integer replyId;
	/** 回复另一用户的用户id*/
	private String replyUserId;
	/** 回复另一用户的用户名字*/
	private String replyName;
	/** 回复状态*/
	private Integer status;
	/** 创建时间*/
	private String createDate;
	/** 被回复用户ID*/
	private String replySourceUserId;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the newsId
	 */
	public Integer getNewsId() {
		return newsId;
	}
	/**
	 * @param newsId the newsId to set
	 */
	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the replyContent
	 */
	public String getReplyContent() {
		return replyContent;
	}
	/**
	 * @param replyContent the replyContent to set
	 */
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	/**
	 * @return the cmtId
	 */
	public Integer getCmtId() {
		return cmtId;
	}
	/**
	 * @param cmtId the cmtId to set
	 */
	public void setCmtId(Integer cmtId) {
		this.cmtId = cmtId;
	}
	/**
	 * @return the replyId
	 */
	public Integer getReplyId() {
		return replyId;
	}
	/**
	 * @param replyId the replyId to set
	 */
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}
	/**
	 * @return the replyUserId
	 */
	public String getReplyUserId() {
		return replyUserId;
	}
	/**
	 * @param replyUserId the replyUserId to set
	 */
	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}
	/**
	 * @return the replyName
	 */
	public String getReplyName() {
		return replyName;
	}
	/**
	 * @param replyName the replyName to set
	 */
	public void setReplyName(String replyName) {
		this.replyName = replyName;
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
	 * @return the replySourceUserId
	 */
	public String getReplySourceUserId() {
		return replySourceUserId;
	}
	/**
	 * @param replySourceUserId the replySourceUserId to set
	 */
	public void setReplySourceUserId(String replySourceUserId) {
		this.replySourceUserId = replySourceUserId;
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
	
}
