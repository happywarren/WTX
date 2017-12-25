package com.lt.quota.core.quota.coin.bitfinex.bo;

/**
 * @author mcsong
 * @create 2017-10-27 7:48
 */
public class BitfinexResponseBO {

    private String event;

    private String channel;

    private String chanId;

    private String pair;

    public BitfinexResponseBO() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChanId() {
        return chanId;
    }

    public void setChanId(String chanId) {
        this.chanId = chanId;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }
}
