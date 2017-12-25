/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.enums.fund
 * FILE    NAME: FundTypeEnum.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.enums.fund;

/**
 * 充值组
 * 
 * @author yubei
 * @date 2017年9月7日
 */
public enum RechargeGroupEnum {
	ALIPAY("Alipay", "兴联支付宝"),
	IAPPPAY("Iapppay", "爱贝"), 
	QIANTONPAY("QianTongPay", "钱通"),
	SWIFTPASS("SwiftPass", "威富通"),
	DADDYPAY("DaddyPay","爸爸付"),
	AGGPAY("Aggpay","聚合支付"),
	ALIPAYTRANSFER("AlipayTransfer","支付宝转账");

	/** 值 */
	private String groupId;
	/** 描述 */
	private String description;

	RechargeGroupEnum(String groupId, String description) {
		this.groupId = groupId;
		this.description = description;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
