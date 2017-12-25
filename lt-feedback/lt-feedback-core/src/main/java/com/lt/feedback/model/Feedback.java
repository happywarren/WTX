package com.lt.feedback.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 意见反馈
 */
@Entity
@Table(name = "fb_feedback")
public class Feedback implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 父主题id
     */
    @Column(name = "parent_id", length = 20)
    private String parentId;

    /**
     * 意见反馈编号
     */
    @NotNull
    @Column(name = "feedback_id", length = 20)
    private String feedbackId;

    /**
     * 用户编号
     */
    @NotNull
    @Column(name = "user_id", length = 30)
    private String userId;

    /**
     * 反馈内容
     */
    @NotNull
    @Column(name = "content", length = 2000)
    private String content;

    /**
     * 图片url
     */
    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    /**
     * 问题数量
     */
    @Column(name = "feedback_num", length = 10)
    private Integer feedbackNum;

    /**
     * 回复内容
     */
    @Column(name = "reply_content", length = 2000)
    private String replyContent;

    /**
     * 回复用户
     */
    @Column(name = "reply_user", length = 50)
    private String replyUser;

    /**
     * 状态 0 不显示 1 显示
     */
    @NotNull
    @Column(name = "status", length = 1)
    private Integer status;

    /**
     * 是否回复 0 未回复 1 已回复 2 已查看
     */
    @NotNull
    @Column(name = "reply_status", length = 1)
    private Integer replyStatus;

    /**
     * 创建时间
     */
    @NotNull
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "reply_time")
    private Date replyTime;


    public Feedback() {

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
