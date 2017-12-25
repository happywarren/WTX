package com.lt.tms.trade.ib.api.bean;


public class ContractBean {

    private String productCode;
    private Integer tickerId;//合约id
    private String symbol;//品种
    private String localSymbol;//本地品种
    private String secType;//证券类型
    private String currency;//币种
    private String exchange;//交易所
    private String contractMonth;//合约
    private String optRight;
    private Double optStrike;

    public ContractBean() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getTickerId() {
        return tickerId;
    }

    public void setTickerId(Integer tickerId) {
        this.tickerId = tickerId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLocalSymbol() {
        return localSymbol;
    }

    public void setLocalSymbol(String localSymbol) {
        this.localSymbol = localSymbol;
    }

    public String getSecType() {
        return secType;
    }

    public void setSecType(String secType) {
        this.secType = secType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getContractMonth() {
        return contractMonth;
    }

    public void setContractMonth(String contractMonth) {
        this.contractMonth = contractMonth;
    }

    public String getOptRight() {
        return optRight;
    }

    public void setOptRight(String optRight) {
        this.optRight = optRight;
    }

    public Double getOptStrike() {
        return optStrike;
    }

    public void setOptStrike(Double optStrike) {
        this.optStrike = optStrike;
    }
}
