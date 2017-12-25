package com.lt.model.brand;

import java.io.Serializable;

/**
 * Description: 品牌-支付渠道 关系对象
 * Created by yanzhenyu on 2017/8/31.
 */
public class BrandPayInfo implements Serializable {
    private int id;//主键
    private String brandId;//品牌 id
    private String payId;//支付渠道 id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    @Override
    public String toString() {
        return "BrandPayInfo{" +
                "id='" + id + '\'' +
                ", brandId='" + brandId + '\'' +
                ", payId='" + payId + '\'' +
                '}';
    }
}