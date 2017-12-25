package com.lt.manager.bean.product;

/**
 * Description:差价合约-商品配置
 *
 * @author yanzhenyu
 * @date 2017/10/21
 */
public class DigitalCoinConfigVO {
    private Double spread;//点差
    private Double contractSize;//合约规格
    private Integer maxLever;//最大杠杆
    private Integer maxSingleOpenPosition;//单次最大开仓量
    private Integer maxPositionPerAccount;//单户最大净持仓量
    private String productCode;// 商品编码
    private Integer brandPosition;	//品牌净持仓量
    public Double getSpread() {
        return spread;
    }

    public void setSpread(Double spread) {
        this.spread = spread;
    }

    public Double getContractSize() {
        return contractSize;
    }

    public void setContractSize(Double contractSize) {
        this.contractSize = contractSize;
    }

    public Integer getMaxLever() {
        return maxLever;
    }

    public void setMaxLever(Integer maxLever) {
        this.maxLever = maxLever;
    }

    public Integer getMaxSingleOpenPosition() {
        return maxSingleOpenPosition;
    }

    public void setMaxSingleOpenPosition(Integer maxSingleOpenPosition) {
        this.maxSingleOpenPosition = maxSingleOpenPosition;
    }

    public Integer getMaxPositionPerAccount() {
        return maxPositionPerAccount;
    }

    public void setMaxPositionPerAccount(Integer maxPositionPerAccount) {
        this.maxPositionPerAccount = maxPositionPerAccount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getBrandPosition() {
		return brandPosition;
	}

	public void setBrandPosition(Integer brandPosition) {
		this.brandPosition = brandPosition;
	}

	@Override
	public String toString() {
		return "DigitalCoinConfigVO [" + (spread != null ? "spread=" + spread + ", " : "")
				+ (contractSize != null ? "contractSize=" + contractSize + ", " : "")
				+ (maxLever != null ? "maxLever=" + maxLever + ", " : "")
				+ (maxSingleOpenPosition != null ? "maxSingleOpenPosition=" + maxSingleOpenPosition + ", " : "")
				+ (maxPositionPerAccount != null ? "maxPositionPerAccount=" + maxPositionPerAccount + ", " : "")
				+ (productCode != null ? "productCode=" + productCode + ", " : "")
				+ (brandPosition != null ? "brandPosition=" + brandPosition : "") + "]";
	}
}