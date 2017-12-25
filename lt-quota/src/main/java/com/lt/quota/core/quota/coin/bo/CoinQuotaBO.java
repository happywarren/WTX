package com.lt.quota.core.quota.coin.bo;

import java.math.BigDecimal;

/**
 * @author mcsong
 * @create 2017-10-18 11:15
 */
public class CoinQuotaBO {

    private String productName;

    private String source;

    private BigDecimal lastPrice;

    private Long timestamp;

    public CoinQuotaBO() {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
