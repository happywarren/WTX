package com.lt.model.sys;

import java.io.Serializable;

/**
 * Created by guodawang on 2017/8/4.
 */
public class QuotaHostBean implements Serializable{

    private int id;

    private String host;

    private int port;

    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
