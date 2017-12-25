package com.lt.quota.core.appServer.bean.request;

/**
 * 行情订阅
 */
public class MktSubscribeRequest extends BaseRequest {

    private String productCode;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

}
