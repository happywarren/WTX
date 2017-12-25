package com.lt.tms.tcp.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    @Value("${sys.netty.port}")
    private int port;
    @Value("${sys.netty.so.timeout}")
    private int soTimeout;
    @Value("${sys.netty.server.startup}")
    private boolean startup;

    public NettyConfig() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public boolean isStartup() {
        return startup;
    }

    public void setStartup(boolean startup) {
        this.startup = startup;
    }
}
