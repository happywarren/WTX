/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;

/**
 *
 * @author Administrator
 */
public class FuturesSnapshot extends Snapshot {

    public double settle;//结算价
    public double pSettle;//昨结算
    public double oi;//持仓
    public double pOi;//昨持仓
    public double upLimit;//涨停价
    public double downLimit;//跌停价
    public double bid1Price;
    public int bid1Volume;
    public double ask1Price;
    public int ask1Volume;

    @Override
    public void updateSnapshot(Snapshot snapshot) {
        FuturesSnapshot fSnapshot = (FuturesSnapshot) snapshot;
        this.date = fSnapshot.date;
        this.time = fSnapshot.time;
        this.open = fSnapshot.open;
        this.high = fSnapshot.high;
        this.low = fSnapshot.low;
        this.close = fSnapshot.close;
        this.pClose = fSnapshot.pClose;

        this.change = fSnapshot.close - fSnapshot.pClose;
        this.changeRate = change / this.pClose * 100;
        this.volume =(int)(fSnapshot.tVolume - this.tVolume);
        this.value =(int)(fSnapshot.tValue - this.tValue);
        this.tVolume = fSnapshot.tVolume;
        this.tValue = fSnapshot.tValue;
        this.settle = fSnapshot.settle;
        this.pSettle = fSnapshot.pSettle;
        this.close = fSnapshot.close;
        this.oi = fSnapshot.oi;
        this.pOi = fSnapshot.pOi;
        this.upLimit = fSnapshot.upLimit;
        this.downLimit = fSnapshot.downLimit;
        this.bid1Price = fSnapshot.bid1Price;
        this.bid1Volume = fSnapshot.bid1Volume;
        this.ask1Price = fSnapshot.ask1Price;
        this.ask1Volume = fSnapshot.ask1Volume;
    }
}
