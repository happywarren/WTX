/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.api.fund
 * FILE    NAME: IFundCashService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.fund.service;

import com.lt.model.fund.FundOptCode;

/**
 * 充值入账处理
 */
public interface IFundCashRechargeService {

    /**
     * 充值入账
     *
     * @param payId         充值订单号
     * @param externalId    第三方订单号
     * @param realRmbAmount 时间到账RMB金额
     * @param fundOptCode   资金业务对象
     * @return
     */
    boolean doSuccessRecharge(String payId, String externalId, Double realRmbAmount, FundOptCode fundOptCode);

}
