package com.lt.quota.core.model;

/**
 * 分时图
 */
public class TimeSharingBean implements java.io.Serializable {

    private String timeStamp;

    private String lastPrice;

    private Integer volume;

    public TimeSharingBean() {
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
