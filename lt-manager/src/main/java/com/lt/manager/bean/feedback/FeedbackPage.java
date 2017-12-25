package com.lt.manager.bean.feedback;


import com.lt.manager.bean.BaseBean;

import java.util.Date;

/**
 * 意见反馈
 */
public class FeedbackPage extends BaseBean {

    /**
     * 意见反馈编号
     */
    private String feedbackId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 图片url
     */
    private String imageUrl;

    /**
     * 问题数量
     */
    private Integer feedbackNum;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复人
     */
    private String replyUser;

    /**
     * 是否已读 0 已读 1 未读
     */
    private Integer status;

    /**
     * 是否回复 0 未回复 1 已回复 2 已查看
     */
    private Integer replyStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date replyTime;


    public FeedbackPage() {

    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Integer getFeedbackNum() {
        return feedbackNum;
    }

    public void setFeedbackNum(Integer feedbackNum) {
        this.feedbackNum = feedbackNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(Integer replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
