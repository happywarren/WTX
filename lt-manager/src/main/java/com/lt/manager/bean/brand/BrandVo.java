package com.lt.manager.bean.brand;

import com.lt.manager.bean.BaseBean;

/**
 * Description: 请求参数
 * Created by yanzhenyu on 2017/8/31.
 */
public class BrandVo extends BaseBean {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String brandId;//   品牌id
    private String brandName;//   品牌名称
    private String brandCode;//   品牌编码
    private String payChannels;//   支付渠道 id 列表
    private String investorId;	//品牌下的券商id
    private String nickName;
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

    public String getPayChannels() {
        return payChannels;
    }

    public void setPayChannels(String payChannels) {
        this.payChannels = payChannels;
    }


	public String getInvestorId() {
		return investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
    public String toString() {
        return "BrandVo{" +
                "brandId='" + brandId + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brandCode='" + brandCode + '\'' +
                ", payChannels='" + payChannels + '\'' +
                '}';
    }
}