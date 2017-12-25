package com.lt.quota.core.comm.bean.request;

/**
 * 取消行情订阅
 */
public class MktUnSubscribeRequest extends BaseRequest {

    private String productCode;
    private String productType;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
