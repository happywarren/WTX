package com.lt.trade.riskcontrol.bean;

/**
 * 封装移动止损信息
 *
 * Created by sunch on 2016/12/12.
 */
public class MutableStopLossBean {

    private int direct;//方向
    private double stopLossPrice;//止损价
    private double sentinelPrice;//曾经到达的最高价

    public MutableStopLossBean() {
    }

    public MutableStopLossBean(int direct, double stopLossPrice, double sentinelPrice) {
        this.direct = direct;
        this.stopLossPrice = stopLossPrice;
        this.sentinelPrice = sentinelPrice;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public double getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(double stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public double getSentinelPrice() {
        return sentinelPrice;
    }

    public void setSentinelPrice(double sentinelPrice) {
        this.sentinelPrice = sentinelPrice;
    }

}
