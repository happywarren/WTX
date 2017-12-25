package com.lt.trade.riskcontrol.bean;

import com.lt.trade.tradeserver.bean.FutureOrderBean;

/**
 * 封装订单风控信息
 *
 * Created by sunch on 2016/11/11.
 */
public class RiskControlBean {

    private String productName;//商品名
    private String uniqueOrderId;//订单号
    private int direct;//方向
    private double matchPrice;//买入均价
    private double stopGainPrice;//止盈
    private double stopLossPrice;//止损
    private Boolean isMutableStopLoss;//是否是移动止损单
    private long deferredOrderTimeStamp;//递延订单时间
    private FutureOrderBean orderBean;

    public RiskControlBean() {
    }

    public RiskControlBean(String productName, String uniqueOrderId, int direct, Boolean isMutableStopLoss, long deferredOrderTimeStamp,
                           double matchPrice, double stopGainPrice, double stopLossPrice, FutureOrderBean orderBean) {
        this.productName = productName;
        this.uniqueOrderId = uniqueOrderId;
        this.direct = direct;
        this.isMutableStopLoss = isMutableStopLoss;
        this.deferredOrderTimeStamp = deferredOrderTimeStamp;
        this.matchPrice = matchPrice;
        this.stopGainPrice = stopGainPrice;
        this.stopLossPrice = stopLossPrice;
        this.orderBean = orderBean;
    }

    public String getUniqueOrderId() {
        return uniqueOrderId;
    }

    public void setUniqueOrderId(String uniqueOrderId) {
        this.uniqueOrderId = uniqueOrderId;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public Boolean isMutableStopLoss() {
        return isMutableStopLoss;
    }

    public void setMutableStopLoss(Boolean mutableStopLoss) {
        isMutableStopLoss = mutableStopLoss;
    }

    public long getDeferredOrderTimeStamp() {
        return deferredOrderTimeStamp;
    }

    public void setDeferredOrderTimeStamp(long deferredOrderTimeStamp) {
        this.deferredOrderTimeStamp = deferredOrderTimeStamp;
    }

    public double getMatchPrice() {
        return matchPrice;
    }

    public void setMatchPrice(double matchPrice) {
        this.matchPrice = matchPrice;
    }

    public double getStopGainPrice() {
        return stopGainPrice;
    }

    public void setStopGainPrice(double stopGainPrice) {
        this.stopGainPrice = stopGainPrice;
    }

    public double getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(double stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public FutureOrderBean getOrderBean() {
        return orderBean;
    }

    public void setOrderBean(FutureOrderBean orderBean) {
        this.orderBean = orderBean;
    }
}
