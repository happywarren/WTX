package com.lt.feedback.dto;


import java.util.Date;
import java.util.List;

public class FeedbackDTO implements java.io.Serializable{

    private String parentId;

    private String subjectId;

    private String userId;

    private String content;

    private String imageUrl;

    private Integer reply;

    private Integer seeReply;

    private Date createTime;

    public FeedbackDTO() {
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }

    public Integer getSeeReply() {
        return seeReply;
    }

    public void setSeeReply(Integer seeReply) {
        this.seeReply = seeReply;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}