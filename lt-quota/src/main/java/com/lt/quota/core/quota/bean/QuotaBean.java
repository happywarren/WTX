package com.lt.quota.core.quota.bean;


public class QuotaBean implements java.io.Serializable {

    /**
     * 卖一价
     */
    private String askPrice1;
    /**
     * 卖量1
     */
    private String askQty1;
    /**
     * 均价
     */
    private String averagePrice;
    /**
     * 买1价
     */
    private String bidPrice1;
    /**
     * 买量1
     */
    private String bidQty1;
    /**
     * 涨幅
     */
    private String changeRate;
    /**
     * 涨跌值
     */
    private String changeValue;
    /**
     * 最高价
     */
    private String highPrice;
    /**
     * 最新价
     */
    private String lastPrice;
    /**
     * 跌停价
     */
    private String limitDownPrice;
    /**
     * 涨停价
     */
    private String limitUpPrice;
    /**
     * 最低价
     */
    private String lowPrice;
    /**
     * 开盘价
     */
    private String openPrice;
    /**
     * 持仓量
     */
    private String positionQty;
    /**
     * 昨收盘价
     */
    private String preClosePrice;
    /**
     * 昨结算价
     */
    private String preSettlePrice;
    /**
     * 商品
     */
    private String productName;
    /**
     * 结算价
     */
    private String settlePrice;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 当日总成交量
     */
    private Integer totalQty;

    private String source;

    private Integer plate;

    private String low13Week;     //13周最低价
    private String high13Week;     //13周最高价
    private String low26Week;     //26周最低价
    private String high26Week;     //26周最高价
    private String low52Week;     //52周最低价
    private String high52Week;     //52周最高价

    private String marketCap; //市值


    public QuotaBean() {

    }

    public QuotaBean(String productName) {
        this.productName = productName;
    }


    public String getAskPrice1() {
        return askPrice1;
    }

    public void setAskPrice1(String askPrice1) {
        this.askPrice1 = askPrice1;
    }

    public String getAskQty1() {
        return askQty1;
    }

    public void setAskQty1(String askQty1) {
        this.askQty1 = askQty1;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getBidPrice1() {
        return bidPrice1;
    }

    public void setBidPrice1(String bidPrice1) {
        this.bidPrice1 = bidPrice1;
    }

    public String getBidQty1() {
        return bidQty1;
    }

    public void setBidQty1(String bidQty1) {
        this.bidQty1 = bidQty1;
    }

    public String getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(String changeRate) {
        this.changeRate = changeRate;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLimitDownPrice() {
        return limitDownPrice;
    }

    public void setLimitDownPrice(String limitDownPrice) {
        this.limitDownPrice = limitDownPrice;
    }

    public String getLimitUpPrice() {
        return limitUpPrice;
    }

    public void setLimitUpPrice(String limitUpPrice) {
        this.limitUpPrice = limitUpPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getPositionQty() {
        return positionQty;
    }

    public void setPositionQty(String positionQty) {
        this.positionQty = positionQty;
    }

    public String getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(String preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public String getPreSettlePrice() {
        return preSettlePrice;
    }

    public void setPreSettlePrice(String preSettlePrice) {
        this.preSettlePrice = preSettlePrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSettlePrice() {
        return settlePrice;
    }

    public void setSettlePrice(String settlePrice) {
        this.settlePrice = settlePrice;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getPlate() {
        return plate;
    }

    public void setPlate(Integer plate) {
        this.plate = plate;
    }

    public String getLow13Week() {
        return low13Week;
    }

    public void setLow13Week(String low13Week) {
        this.low13Week = low13Week;
    }

    public String getHigh13Week() {
        return high13Week;
    }

    public void setHigh13Week(String high13Week) {
        this.high13Week = high13Week;
    }

    public String getLow26Week() {
        return low26Week;
    }

    public void setLow26Week(String low26Week) {
        this.low26Week = low26Week;
    }

    public String getHigh26Week() {
        return high26Week;
    }

    public void setHigh26Week(String high26Week) {
        this.high26Week = high26Week;
    }

    public String getLow52Week() {
        return low52Week;
    }

    public void setLow52Week(String low52Week) {
        this.low52Week = low52Week;
    }

    public String getHigh52Week() {
        return high52Week;
    }

    public void setHigh52Week(String high52Week) {
        this.high52Week = high52Week;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    @Override
    public String toString() {
        return "QuotaBean{" +
                "askPrice1='" + askPrice1 + '\'' +
                ", askQty1='" + askQty1 + '\'' +
                ", averagePrice='" + averagePrice + '\'' +
                ", bidPrice1='" + bidPrice1 + '\'' +
                ", bidQty1='" + bidQty1 + '\'' +
                ", changeRate='" + changeRate + '\'' +
                ", changeValue='" + changeValue + '\'' +
                ", highPrice='" + highPrice + '\'' +
                ", lastPrice='" + lastPrice + '\'' +
                ", limitDownPrice='" + limitDownPrice + '\'' +
                ", limitUpPrice='" + limitUpPrice + '\'' +
                ", lowPrice='" + lowPrice + '\'' +
                ", openPrice='" + openPrice + '\'' +
                ", positionQty='" + positionQty + '\'' +
                ", preClosePrice='" + preClosePrice + '\'' +
                ", preSettlePrice='" + preSettlePrice + '\'' +
                ", productName='" + productName + '\'' +
                ", settlePrice='" + settlePrice + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", totalQty=" + totalQty +
                ", source='" + source + '\'' +
                ", low13Week='" + low13Week + '\'' +
                ", high13Week='" + high13Week + '\'' +
                ", low26Week='" + low26Week + '\'' +
                ", high26Week='" + high26Week + '\'' +
                ", low52Week='" + low52Week + '\'' +
                ", high52Week='" + high52Week + '\'' +
                ", marketCap='" + marketCap + '\'' +
                '}';
    }
}
