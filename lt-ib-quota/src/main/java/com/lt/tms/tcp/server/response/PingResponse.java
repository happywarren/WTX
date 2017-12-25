package com.lt.tms.tcp.server.response;


public class PingResponse {

    private Long timeStamp;

    public PingResponse() {
        this.timeStamp = System.currentTimeMillis();
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
