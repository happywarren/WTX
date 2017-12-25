package com.lt.quota.core.model;

/**
 * 群发接收实体
 *
 * @author guodw
 */
public class JmsTopicOrderMsg {

    private String userId;

    private String msg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JmsTopicOrderMsg() {

    }

    public JmsTopicOrderMsg(String userId, String msg) {
        this.userId = userId;
        this.msg = msg;
    }
}
