package com.lt.quota.core.comm.config;

/**
 * 作者: 邓玉明
 * 时间: 2017/6/6 下午5:32
 * email:cndym@163.com
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SysConfig {

    @Value("${sys.config.app.port}")
    private int serverPort;

    @Value("${sys.read.time.out}")
    private int readTimeout;

    @Value("${sys.write.time.out}")
    private int writeTimeout;

    @Value("${sys.all.time.out}")
    private int allTimeout;

    @Value("${sys.cserver.ip}")
    private String cserverIp;

    @Value("${sys.cserver.port}")
    private int cserverPort;

    public SysConfig() {
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getAllTimeout() {
        return allTimeout;
    }

    public void setAllTimeout(int allTimeout) {
        this.allTimeout = allTimeout;
    }

    public String getCserverIp() {
        return cserverIp;
    }

    public void setCserverIp(String cserverIp) {
        this.cserverIp = cserverIp;
    }

    public int getCserverPort() {
        return cserverPort;
    }

    public void setCserverPort(int cserverPort) {
        this.cserverPort = cserverPort;
    }
}
