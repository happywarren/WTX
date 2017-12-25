package com.lt.model.news;

import java.io.Serializable;

/**
 * 
 * TODO 新闻评论
 * @author XieZhibing
 * @date 2017年2月3日 下午3:35:43
 * @version <b>1.0.0</b>
 */
public class NewsComment implements Serializable{
	private Integer id;
	private Integer newsId;
	private String newsName;
	private String content;
	private String userId;
	private String userNick;
	private String userHead;
	// 0:待审核，1审核通过，2审核不通过
	private Integer status;
	private String createDate;
	
	private Integer replyCount;
	private Integer verifyStaffId;
	private String verifyStaffName;
	private String modifyDate;

	public Integer getId() {
		return id;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public String getNewsName() {
		return newsName;
	}

	public String getContent() {
		return content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNick() {
		return userNick;
	}

	public String getUserHead() {
		return userHead;
	}

	public Integer getStatus() {
		return status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public Integer getVerifyStaffId() {
		return verifyStaffId;
	}

	public String getVerifyStaffName() {
		return verifyStaffName;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public void setNewsName(String newsName) {
		this.newsName = newsName;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setVerifyStaffId(Integer verifyStaffId) {
		this.verifyStaffId = verifyStaffId;
	}

	public void setVerifyStaffName(String verifyStaffName) {
		this.verifyStaffName = verifyStaffName;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

}
