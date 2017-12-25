package com.lt.manager.bean.feedback;


import java.util.Date;

/**
 * 意见反馈
 */
public class FeedbackVo implements java.io.Serializable {

    private Long id;

    /**
     * 父主题id
     */
    private String parentId;

    /**
     * 意见反馈编号
     */
    private String feedbackId;

    /**
     * 用户编号
     */
    private String userId;

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
     * 回复用户
     */
    private String replyUser;

    /**
     * 状态 0 不显示 1 显示
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


    public FeedbackVo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getFeedbackNum() {
        return feedbackNum;
    }

    public void setFeedbackNum(Integer feedbackNum) {
        this.feedbackNum = feedbackNum;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
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
