package com.lt.tms.trade.bean;


public class TradeResBean {

    /**
     * 订单id
     */
    private String tradeId;

    /**
     * 商品code
     */
    private String productCode;
    /**
     * 方向
     */
    private int direct;
    /**
     * 开平
     */
    private int offset;

    /**
     * 第三方系统订单号
     */
    private String sysOrderId;

    /**
     * 委托数量
     */
    private int number;

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
    private int status;

    public TradeResBean() {
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getSysOrderId() {
        return sysOrderId;
    }

    public void setSysOrderId(String sysOrderId) {
        this.sysOrderId = sysOrderId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
