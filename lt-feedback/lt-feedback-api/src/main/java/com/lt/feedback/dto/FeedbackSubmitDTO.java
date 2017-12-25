package com.lt.feedback.dto;

/**
 * 提交建议反馈DTO
 */
public class FeedbackSubmitDTO implements java.io.Serializable{

    private String parentId;

    private String userId;

    private String content;

    private String imageUrl;

    public FeedbackSubmitDTO() {
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
}