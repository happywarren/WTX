package com.lt.user.charge.dao.sqldb;

import com.lt.model.user.charge.FundSwiftPassModel;
import com.lt.model.user.charge.FundSwiftPassResultModel;

/**
 * 威富通支付dao
 * Created by guodawang on 2017/9/4.
 */
public interface SwiftPassDao {
    /*插入用户提交的支付申请信息*/
    void insert(FundSwiftPassModel model);

    /*插入威富通支付返回信息*/
    void insertResult(FundSwiftPassResultModel model);

}
