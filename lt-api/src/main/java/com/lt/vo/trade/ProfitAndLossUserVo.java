package com.lt.vo.trade;

/**
 * 描述:常盈常亏用户对象(用来做跟单)
 *
 * @author lvx
 * @created 2017/7/17
 */
public class ProfitAndLossUserVo {
    /**
     *  用户ID
     */
    private String userId;

    /**
     * 方向: 0 常盈; 1 常亏
     */
    private Integer direction = 0;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
}
