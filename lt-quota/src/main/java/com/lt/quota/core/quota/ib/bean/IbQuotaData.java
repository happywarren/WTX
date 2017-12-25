package com.lt.quota.core.quota.ib.bean;


public class IbQuotaData implements java.io.Serializable {

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 今开盘价
     */
    private String openPrice;

    /**
     * 最高价
     */
    private String highPrice;

    /**
     * 最低价
     */
    private String lowPrice;

    /**
     * 现价
     */
    private String lastPrice;

    /**
     * 申卖价1
     */
    private String askPrice1;

    /**
     * 申卖量1
     */
    private String askVolume1;

    /**
     * 申买价1
     */
    private String bidPrice1;

    /**
     * 申买量1
     */
    private String bidVolume1;

    /**
     * 均价
     */
    private String avgPrice;

    /**
     * 涨幅
     */
    private String changeRate;

    /**
     * 涨跌值
     */
    private String changePrice;

    /**
     * 涨停价
     */
    private String upLimitPrice;

    /**
     * 跌停价
     */
    private String downLimitPrice;

    /**
     * 持仓量
     */
    private String positionVolume;

    /**
     * 今结算价
     */
    private String settlePrice;

    /**
     * 成交量
     */
    private String tradeVolume;

    /**
     * 成交额
     */
    private String tradeAmount;

    /**
     * 昨收盘价
     */
    private String preClosePrice;

    /**
     * 昨持仓量
     */
    private String prePositionVolume;

    /**
     * 昨结算价
     */
    private String preSettlePrice;

    private String low13Week;     //13周最低价
    private String high13Week;     //13周最高价
    private String low26Week;     //26周最低价
    private String high26Week;     //26周最高价
    private String low52Week;     //52周最低价
    private String high52Week;     //52周最高价
    private String marketCap; //市值


    public IbQuotaData() {
    }

    public IbQuotaData(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
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

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getAskPrice1() {
        return askPrice1;
    }

    public void setAskPrice1(String askPrice1) {
        this.askPrice1 = askPrice1;
    }

    public String getAskVolume1() {
        return askVolume1;
    }

    public void setAskVolume1(String askVolume1) {
        this.askVolume1 = askVolume1;
    }

    public String getBidPrice1() {
        return bidPrice1;
    }

    public void setBidPrice1(String bidPrice1) {
        this.bidPrice1 = bidPrice1;
    }

    public String getBidVolume1() {
        return bidVolume1;
    }

    public void setBidVolume1(String bidVolume1) {
        this.bidVolume1 = bidVolume1;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(String changeRate) {
        this.changeRate = changeRate;
    }

    public String getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(String changePrice) {
        this.changePrice = changePrice;
    }

    public void setPositionVolume(String positionVolume) {
        this.positionVolume = positionVolume;
    }

    public void setPrePositionVolume(String prePositionVolume) {
        this.prePositionVolume = prePositionVolume;
    }

    public String getUpLimitPrice() {
        return upLimitPrice;
    }

    public void setUpLimitPrice(String upLimitPrice) {
        this.upLimitPrice = upLimitPrice;
    }

    public String getDownLimitPrice() {
        return downLimitPrice;
    }

    public void setDownLimitPrice(String downLimitPrice) {
        this.downLimitPrice = downLimitPrice;
    }

    public String getSettlePrice() {
        return settlePrice;
    }

    public void setSettlePrice(String settlePrice) {
        this.settlePrice = settlePrice;
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

    public String getPositionVolume() {
        return positionVolume;
    }

    public String getPrePositionVolume() {
        return prePositionVolume;
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
}
