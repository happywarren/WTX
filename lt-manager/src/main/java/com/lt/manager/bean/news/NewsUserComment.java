package com.lt.manager.bean.news;

import java.io.Serializable;

/**   
* 项目名称：lt-manager   
* 类名称：NewsUserComment   
* 类描述：用户评论类   
* 创建人：yuanxin   
* 创建时间：2017年2月13日 下午7:05:37      
*/
public class NewsUserComment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户id*/
	private String userId;
	/** 新闻id*/
	private Integer newsId;
	/** 评论内容*/
	private String content;
	/** 用户昵称*/
	private String nickName;
	/** 用户头像*/
	private String headPic;
	/** 评论状态*/
	private Integer status;
	/** 创建时间*/
	private String createDate;
	/** 回复数量*/
	private Integer replyCount;
	/** 用户等级*/
	private Integer userGrade;
	/** 评论id*/
	private Integer commentId;
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
	 * @return the headPic
	 */
	public String getHeadPic() {
		return headPic;
	}
	/**
	 * @param headPic the headPic to set
	 */
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
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
	 * @return the replyCount
	 */
	public Integer getReplyCount() {
		return replyCount;
	}
	/**
	 * @param replyCount the replyCount to set
	 */
	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}
	/**
	 * @return the userGrade
	 */
	public Integer getUserGrade() {
		return userGrade;
	}
	/**
	 * @param userGrade the userGrade to set
	 */
	public void setUserGrade(Integer userGrade) {
		this.userGrade = userGrade;
	}
	/**
	 * @return the commentId
	 */
	public Integer getCommentId() {
		return commentId;
	}
	/**
	 * @param commentId the commentId to set
	 */
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	
}
