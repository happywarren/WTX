package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 新闻回复日志
 * @author XieZhibing
 * @date 2017年2月3日 下午3:30:54
 * @version <b>1.0.0</b>
 */
public class NewsCmtReply implements Serializable{
	private Integer id;
	private Integer newsId;
	private String newsName;
	private String replyContent;
	private String replyUserId;
	private String replyUserHead = "";
	private String replyUserNick;
	private Integer cmtId;
	private Integer replyId;

	private String upUserNick;
	// 0:待审核，1审核通过，2审核不通过
	private Integer status;
	private Integer verifyStaffId;
	private String verifyStaffName;
	private String createDate;
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

	public String getReplyContent() {
		return replyContent;
	}

	public String getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}

	public String getReplyUserHead() {
		return replyUserHead;
	}

	public String getReplyUserNick() {
		return replyUserNick;
	}

	public Integer getCmtId() {
		return cmtId;
	}

	public Integer getReplyId() {
		return replyId;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getVerifyStaffId() {
		return verifyStaffId;
	}

	public String getVerifyStaffName() {
		return verifyStaffName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUpUserNick() {
		return upUserNick;
	}

	public void setUpUserNick(String upUserNick) {
		this.upUserNick = upUserNick;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public void setNewsName(String newsName) {
		this.newsName = newsName;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public void setReplyUserHead(String replyUserHead) {
		this.replyUserHead = replyUserHead;
	}

	public void setReplyUserNick(String replyUserNick) {
		this.replyUserNick = replyUserNick;
	}

	public void setCmtId(Integer cmtId) {
		this.cmtId = cmtId;
	}

	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setVerifyStaffId(Integer verifyStaffId) {
		this.verifyStaffId = verifyStaffId;
	}

	public void setVerifyStaffName(String verifyStaffName) {
		this.verifyStaffName = verifyStaffName;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

}
