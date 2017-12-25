package com.lt.user.core.dao.sqldb;

import com.lt.model.user.UserBankInfo;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/21 上午10:42
 * email:cndym@163.com
 */

public interface IUserBankInfoDao {
    public UserBankInfo getUserBankInfoByUserIdBankCode(String userId,String bankId);
}
