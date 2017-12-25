package com.lt.manager.bean.trade;

/**
 * Description: 数字货币持仓量 现金，积分共用对象
 *
 * @author yanzhenyu
 * @date 2017/10/21
 */
public class DigitalCoinPosition {
    private String userId;
    private String productCode;
    private int buyCount;
    private int sellCount;
    private String investorId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getSellCount() {
        return sellCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    @Override
    public String toString() {
        return "DigitalCoinPosition{" +
                "userId='" + userId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", buyCount=" + buyCount +
                ", sellCount=" + sellCount +
                ", investorId='" + investorId + '\'' +
                '}';
    }

    public String getInvestorId() {
        return investorId;
    }

    public void setInvestorId(String investorId) {
        this.investorId = investorId;
    }
}