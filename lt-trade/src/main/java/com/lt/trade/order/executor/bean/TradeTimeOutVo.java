package com.lt.trade.order.executor.bean;

/**
 * Created by guodawang on 2017/10/25.
 */
public class TradeTimeOutVo {

    /**
     * 委托号
     */
    private Integer platformId;

    /**
     * 委托类型
     */
    private Integer type;

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
