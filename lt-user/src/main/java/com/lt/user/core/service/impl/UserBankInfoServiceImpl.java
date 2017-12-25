package com.lt.user.core.service.impl;

import com.lt.model.user.UserBankInfo;
import com.lt.user.core.dao.sqldb.IUserBankInfoDao;
import com.lt.user.core.service.IUserBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/21 上午10:58
 * email:cndym@163.com
 */
@Service
public class UserBankInfoServiceImpl implements IUserBankInfoService {

    @Autowired
    private IUserBankInfoDao userBankInfoDao;

    @Override
    public UserBankInfo getUserBankInfoByUserIdBankCode(String userId, String bankId) {
        return userBankInfoDao.getUserBankInfoByUserIdBankCode(userId, bankId);
    }
}
