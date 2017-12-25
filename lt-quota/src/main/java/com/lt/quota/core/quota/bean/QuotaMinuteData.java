package com.lt.quota.core.quota.bean;

/**
 * 行情分钟数据
 */
public class QuotaMinuteData implements java.io.Serializable {

    /**
     * 产品编码
     */
    private String productName;

    /**
     * 时间字符窜 yyyyMMddHHmm
     * 精确到秒
     */
    private String timestamp;

    /**
     * 开盘价
     */
    private String openPrice;

    /**
     * 收盘价
     */
    private String closePrice;

    /**
     * 最高价
     */
    private String highPrice;

    /**
     * 最低价
     */
    private String lowPrice;

    /**
     * 持仓量
     */
    private String positionVolume;

    /**
     * 成交量
     */
    private String tradeVolume;

    /**
     * 成交额
     */
    private String tradeAmount;


    public QuotaMinuteData() {
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getPositionVolume() {
        return positionVolume;
    }

    public void setPositionVolume(String positionVolume) {
        this.positionVolume = positionVolume;
    }

    public String getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(String tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }
}
