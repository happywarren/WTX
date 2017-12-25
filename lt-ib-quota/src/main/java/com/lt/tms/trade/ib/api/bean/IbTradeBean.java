package com.lt.tms.trade.ib.api.bean;


public class IbTradeBean {

    private int orderId;

    /**
     * 第三方系统订单号
     */
    private String sysOrderId;

    /**
     * 成交数量
     */
    private double successNumber;

    /**
     * 剩余数量
     */
    private double overNumber;

    /**
     * 均价
     */
    private double avgPrice;

    /**
     * 最后成交价
     */
    private double lastPrice;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 成交时间
     */
    private long timestamp;

    public IbTradeBean() {

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getSysOrderId() {
        return sysOrderId;
    }

    public void setSysOrderId(String sysOrderId) {
        this.sysOrderId = sysOrderId;
    }

    public double getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(double successNumber) {
        this.successNumber = successNumber;
    }

    public double getOverNumber() {
        return overNumber;
    }

    public void setOverNumber(double overNumber) {
        this.overNumber = overNumber;
    }

    public void setOverNumber(int overNumber) {
        this.overNumber = overNumber;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
