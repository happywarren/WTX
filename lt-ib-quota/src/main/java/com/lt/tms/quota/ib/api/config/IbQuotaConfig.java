package com.lt.tms.quota.ib.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class IbQuotaConfig {

    @Value("${sys.ib.quota.host}")
    private String host;
    @Value("${sys.ib.quota.port}")
    private int port;
    @Value("${sys.ib.quota.client.id}")
    private int clientId;
    @Value("${sys.ib.quota.startup}")
    private boolean startup;
    @Value("${sys.ib.quota.file.dir}")
    private String fileDir;

    public IbQuotaConfig() {

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

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public boolean isStartup() {
        return startup;
    }

    public void setStartup(boolean startup) {
        this.startup = startup;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
}
