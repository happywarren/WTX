package com.lt.user.core.service;

import com.lt.model.user.UserBankInfo;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/21 上午10:40
 * email:cndym@163.com
 */
public interface IUserBankInfoService {

    UserBankInfo getUserBankInfoByUserIdBankCode(String userId,String bankCode);
}
