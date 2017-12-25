package com.lt.quota.core.model;

import java.io.Serializable;

public class Token implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userSecret;
    private String token;
    private boolean isdeline = true;
    private String userId;

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIsdeline() {
        return isdeline;
    }

    public void setIsdeline(boolean isdeline) {
        this.isdeline = isdeline;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
