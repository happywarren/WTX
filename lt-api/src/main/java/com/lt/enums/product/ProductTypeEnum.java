package com.lt.enums.product;

/**
 * 描述:商品类型枚举类
 *
 * @author lvx
 * @created 2017/10/21
 */
public enum ProductTypeEnum {
    /**
     * 10001 期货
     */
    FUTURE("10001", "期货"),
    /**
     * 10002 港股
     */
    HKS("10002", "港股"),
    /**
     * 10003 美股
     */
    HAS("10003", "美股"),
    /**
     * 10004 差价合约
     */
    CONTRACT("10004", "差价合约")
    ;

    private String code;
    private String name;

    ProductTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
