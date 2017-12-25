package com.lt.manager.bean.brand;

import com.lt.manager.bean.BaseBean;

/**
 * Description: 品牌分页数据
 * Created by yanzhenyu on 2017/8/25.
 */
public class BrandPage extends BaseBean {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String brandId;//   品牌id
    private String brandName;//   品牌名称
    private String brandCode;//   品牌编码
    private String payChannelIds;//   支付渠道 id字符串，以,分隔
    private String payChannelNames;//   支付渠道名称字符串，以,分隔
    private String investorId;			//券商id
    private String nickName;			//券商名称
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


    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }


    public String getPayChannelIds() {
        return payChannelIds;
    }

    public void setPayChannelIds(String payChannelIds) {
        this.payChannelIds = payChannelIds;
    }

    public String getPayChannelNames() {
        return payChannelNames;
    }

    public void setPayChannelNames(String payChannelNames) {
        this.payChannelNames = payChannelNames;
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

}