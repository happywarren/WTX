package com.lt.trade.defer.bean;

/**
 * @author mcsong
 * @create 2017-10-26 16:34
 */
public class CoinPositionCountBO {

    private Integer buyCount;

    private Integer sellCount;

    public CoinPositionCountBO() {
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }
}
