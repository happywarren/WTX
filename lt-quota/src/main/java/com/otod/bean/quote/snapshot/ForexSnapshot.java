/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;

/**
 *
 * @author Administrator
 */
public class ForexSnapshot extends Snapshot {

    public double bid1Price;
    public int bid1Volume;
    public double ask1Price;
    public int ask1Volume;

    @Override
    public ForexSnapshot clone() {
        ForexSnapshot snapshot = new ForexSnapshot();
        snapshot.symbol = symbol;
        snapshot.cnName = cnName;
        snapshot.date = date;
        snapshot.time = time;
        snapshot.open = open;
        snapshot.high = high;
        snapshot.low = low;
        snapshot.close = close;
        snapshot.change = change;
        snapshot.changeRate = changeRate;
        snapshot.pClose = pClose;               //昨收价
        snapshot.volume = volume;
        snapshot.value = value;
        snapshot.tVolume = tVolume;
        snapshot.tValue = tValue;
        snapshot.bid1Price = bid1Price;           //买一价
        snapshot.bid1Volume = bid1Volume;         //买一量
        snapshot.ask1Price = ask1Price;           //卖一价
        snapshot.ask1Volume = ask1Volume;         //卖一量

        return snapshot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ForexSnapshot other = (ForexSnapshot) obj;
        if ((this.symbol == null) ? (other.symbol != null) : !this.symbol.equals(other.symbol)) {
            return false;
        }
        if ((this.cnName == null) ? (other.cnName != null) : !this.cnName.equals(other.cnName)) {
            return false;
        }
        if (this.date != other.date) {
            return false;
        }
        if (this.time != other.time) {
            return false;
        }
        if (Double.doubleToLongBits(this.open) != Double.doubleToLongBits(other.open)) {
            return false;
        }
        if (Double.doubleToLongBits(this.high) != Double.doubleToLongBits(other.high)) {
            return false;
        }
        if (Double.doubleToLongBits(this.low) != Double.doubleToLongBits(other.low)) {
            return false;
        }
        if (Double.doubleToLongBits(this.close) != Double.doubleToLongBits(other.close)) {
            return false;
        }
        if (Double.doubleToLongBits(this.change) != Double.doubleToLongBits(other.change)) {
            return false;
        }
        if (Double.doubleToLongBits(this.changeRate) != Double.doubleToLongBits(other.changeRate)) {
            return false;
        }
        if (Double.doubleToLongBits(this.pClose) != Double.doubleToLongBits(other.pClose)) {
            return false;
        }
        if (Double.doubleToLongBits(this.volume) != Double.doubleToLongBits(other.volume)) {
            return false;
        }
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        if (Double.doubleToLongBits(this.tVolume) != Double.doubleToLongBits(other.tVolume)) {
            return false;
        }
        if (Double.doubleToLongBits(this.tValue) != Double.doubleToLongBits(other.tValue)) {
            return false;
        }
        if (Double.doubleToLongBits(this.bid1Price) != Double.doubleToLongBits(other.bid1Price)) {
            return false;
        }
        if (this.bid1Volume != other.bid1Volume) {
            return false;
        }
        if (Double.doubleToLongBits(this.ask1Price) != Double.doubleToLongBits(other.ask1Price)) {
            return false;
        }
        if (this.ask1Volume != other.ask1Volume) {
            return false;
        }
        return true;
    }

    public void updateFYSnapshot(ForexSnapshot fSnapshot) {

        this.date = fSnapshot.date;
        this.time = fSnapshot.time;

        if (fSnapshot.open != 0) {
            this.open = fSnapshot.open;
        }
        if (fSnapshot.high != 0) {
            this.high = fSnapshot.high;
        }
        if (fSnapshot.low != 0) {
            this.low = fSnapshot.low;
        }
        if (fSnapshot.close != 0) {
            this.close = fSnapshot.close;
        }
        if (fSnapshot.pClose != 0) {
            this.pClose = fSnapshot.pClose;
        }
        if (fSnapshot.tVolume != 0) {
            this.tVolume = fSnapshot.tVolume;
        }
        if (fSnapshot.tValue != 0) {
            this.tValue = fSnapshot.tValue;
        }
        if (fSnapshot.bid1Price != 0) {
            this.bid1Price = fSnapshot.bid1Price;
        }
        if (fSnapshot.bid1Volume != 0) {
            this.bid1Volume = fSnapshot.bid1Volume;
        }
        if (fSnapshot.ask1Price != 0) {
            this.ask1Price = fSnapshot.ask1Price;
        }
        if (fSnapshot.ask1Volume != 0) {
            this.ask1Volume = fSnapshot.ask1Volume;
        }

    }

    @Override
    public void updateSnapshot(Snapshot snapshot) {
        ForexSnapshot fSnapshot = (ForexSnapshot) snapshot;

        updateFYSnapshot(fSnapshot);

        if (this.pClose == 0) {
            this.pClose = this.close;
        }

        if (this.open == 0) {
            this.open = this.close;
        }
        if (this.high == 0) {
            this.high = this.close;
        }
        if (this.low == 0) {
            this.low = this.close;
        }
        this.high = Math.max(this.close, this.high);
        this.low = Math.min(this.close, this.low);

        this.change = this.close - this.pClose;
        this.changeRate = change / this.pClose * 100;

        if (Double.isNaN(changeRate) || Double.isInfinite(changeRate)) {
            changeRate = 0;
        }

        this.volume = (int) (fSnapshot.tVolume - this.tVolume);
        this.value = (int) (fSnapshot.tValue - this.tValue);

    }
}
