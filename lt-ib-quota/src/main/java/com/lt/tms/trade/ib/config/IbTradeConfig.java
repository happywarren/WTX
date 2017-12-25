package com.lt.tms.trade.ib.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ib/trade.properties")
public class IbTradeConfig {

    @Value("${sys.ib.trade.host}")
    private String host;
    @Value("${sys.ib.trade.port}")
    private int port;
    @Value("${sys.ib.trade.client.id}")
    private int clientId;
    @Value("${sys.ib.trade.exchange.code}")
    private String exchangeCode;
    @Value("${sys.ib.trade.startup}")
    private boolean startup;

    public IbTradeConfig() {

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

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public boolean isStartup() {
        return startup;
    }

    public void setStartup(boolean startup) {
        this.startup = startup;
    }
}
