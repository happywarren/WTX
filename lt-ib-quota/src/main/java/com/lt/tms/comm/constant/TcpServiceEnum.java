package com.lt.tms.comm.constant;

/**
 * Tcp长连接服务
 */
public enum TcpServiceEnum {

    SERVICE_HEART_BEAT(IConstants.REQ_PING, "心跳"),

    SERVICE_MARKET_DATA(IConstants.REQ_QUOTA_DATA, "订阅行情"),

    SERVICE_CANCEL_MARKET_DATA(IConstants.REQ_CANCEL_MKT_DATA, "取消订阅行情"),

    SERVICE_TRADE(IConstants.REQ_TRADE, "交易");

    /**
     * 值
     */
    private String value;
    /**
     * 名称
     */
    private String name;

    TcpServiceEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
