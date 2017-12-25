package com.lt.trade.tradeserver.bean;

/**
 * 封装商品价格
 * <p>
 * Created by sunch on 2016/11/10.
 */
public class ProductPriceBean {

    private double lastPrice;// 当前价
    private double bidPrice;// 买一价
    private double askPrice;// 卖一价
    private double changeValue;//涨跌值
    private double changeRate;//涨跌幅
    private String productName;// 产品名
    private String quotaTime;//行情时间
    private Integer plate;// 内外盘标记

    public ProductPriceBean() {
    }

    public ProductPriceBean(String productName, double lastPrice, double bidPrice, double askPrice, double changeValue, double changeRate, String quotaTime) {
        this.lastPrice = lastPrice;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.productName = productName;
        this.changeValue = changeValue;
        this.changeRate = changeRate;
        this.quotaTime = quotaTime;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(double changeValue) {
        this.changeValue = changeValue;
    }

    public double getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(double changeRate) {
        this.changeRate = changeRate;
    }

    public String getQuotaTime() {
        return quotaTime;
    }

    public void setQuotaTime(String quotaTime) {
        this.quotaTime = quotaTime;
    }

    public Integer getPlate() {
        return plate;
    }

    public void setPlate(Integer plate) {
        this.plate = plate;
    }

    @Override
    public String toString() {
        return "ProductPriceBean{" +
                "lastPrice=" + lastPrice +
                ", bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                ", productName='" + productName + '\'' +
                ", quotaTime='" + quotaTime + '\'' +
                ", plate=" + plate +
                '}';
    }

}
