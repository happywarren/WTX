package com.lt.manager.bean.brand;

import com.lt.manager.bean.BaseBean;

import java.io.Serializable;

/**
 * Description: 请求参数
 * Created by yanzhenyu on 2017/8/31.
 */
public class BrandBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String brandId;//   品牌id
    private String brandName;//   品牌名称
    private String brandCode;//   品牌编码

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
}