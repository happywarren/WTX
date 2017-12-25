package com.lt.tms.tcp.server.response;


public class BaseResponse<T> {

    private String cmd;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
