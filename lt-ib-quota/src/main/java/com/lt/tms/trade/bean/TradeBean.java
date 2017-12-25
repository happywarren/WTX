package com.lt.tms.trade.bean;

/**
 * 开仓 指的是建仓，想要进行一笔交易，首先必须建立仓位
 *
 * 开仓 交易者新买入或新卖出一定数量的标准合约
 * 平仓 交易者通过笔数相等、方向相反的交易来对冲原来持有的合约的过程
 *
 * 【买进开仓】：是指投资者对未来价格趋势看涨而采取的交易手段，买进持有看涨合约，意味着帐户资金买进合约而冻结
 * 【卖出平仓】：是指投资者对未来价格趋势不看好而采取的交易手段，而将原来买进的看涨合约卖出，投资者资金帐户解冻
 *
 * 【卖出开仓】：是指投资者对未来价格趋势看跌而采取的交易手段，卖出看跌合约。卖出开仓，帐户资金冻结
 * 【买进平仓】：是指投资者将持有的卖出合约对未来行情不再看跌而补回以前卖出合约，与原来的卖出合约对冲抵消退出市场，帐户资金解冻
 *
 * 买入开仓 做多、买涨、买多的操作 对应 卖出平仓
 * 卖出开仓 做空、买跌、买空的操作 应该 买入平仓
 *
 *
 */
public class TradeBean implements java.io.Serializable{

    /** 商品 */
    private String productCode;
    /** 订单id */
    private String tradeId;
    /** 数量 */
    private int number;
    /** 报单价 */
    private double orderPrice;
    /** 行情价 */
    private double quotePrice;
    /** 方向 (看多 看空) */
    private int direct;
    /** 开/平仓 */
    private int offset;
    /** 类型(市价/限价) */
    private int orderType;
    /** 时间戳(毫秒) */
    private long timeStamp;

    public TradeBean() {

    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public double getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(double quotePrice) {
        this.quotePrice = quotePrice;
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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
