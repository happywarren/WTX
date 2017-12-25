package com.lt.model.news;

import java.io.Serializable;

/**
 * 
 * TODO 新闻评论回复参数
 * @author XieZhibing
 * @date 2017年2月3日 下午3:31:19
 * @version <b>1.0.0</b>
 */
public class NewsCmtReplyParam implements Serializable{
	/** 文章ID */
	private Integer newsId; 
	/** 评论内容 */
	private String cmtContent;
	/** 评论用户ID */
	private String userId;
	/** 评论用户昵称 */
	private String userNick;
	/** 回复内容 */
	private String replyContent; 
	/** 回复用户ID */
	private String replyUserId;
	/** 父级评论 */
	private Integer cmtId; 
	/** 父级回复 */
	private Integer replyId; 
	/** 状态 */
	private Integer status;

	public Integer getNewsId() {
		return newsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public Integer getCmtId() {
		return cmtId;
	}

	public Integer getReplyId() {
		return replyId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getCmtContent() {
		return cmtContent;
	}

	public void setCmtContent(String cmtContent) {
		this.cmtContent = cmtContent;
	}
	
	/** 
	 * 获取 评论用户昵称 
	 * @return userNick 
	 */
	public String getUserNick() {
		return userNick;
	}

	/** 
	 * 设置 评论用户昵称 
	 * @param userNick 评论用户昵称 
	 */
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}

	public void setCmtId(Integer cmtId) {
		this.cmtId = cmtId;
	}

	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
