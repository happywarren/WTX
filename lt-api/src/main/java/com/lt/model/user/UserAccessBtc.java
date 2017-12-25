package com.lt.model.user;

/**
 * 用户是否可以使用 btc
 *
 * @author yanzhenyu
 */
public class UserAccessBtc {

    private long id;
    private String userId;
    private Boolean isAccess;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Boolean getIsAccess() {
        return isAccess;
    }

    public void setIsAccess(Boolean isAccess) {
        this.isAccess = isAccess;
    }

}
