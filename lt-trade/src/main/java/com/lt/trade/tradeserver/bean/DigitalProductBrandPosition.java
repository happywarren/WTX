package com.lt.trade.tradeserver.bean;


/**
 * @author yanzhenyu
 */
public class DigitalProductBrandPosition {

  private String productCode;
  private String brandId;
  private int buyCount;
  private int sellCount;

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }


  public String getBrandId() {
    return brandId;
  }

  public void setBrandId(String brandId) {
    this.brandId = brandId;
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
    return "DigitalProductBrandPosition{" +
            "productCode='" + productCode + '\'' +
            ", brandId='" + brandId + '\'' +
            ", buyCount=" + buyCount +
            ", sellCount=" + sellCount +
            '}';
  }
}
