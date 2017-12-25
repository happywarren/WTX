package com.lt.user.core.dao.sqldb;

import com.lt.model.user.UserAccessBtc;

/**
 * 用户-btc
 * @author yanzhenyu
 */
public interface IUserAccessBtcDao {
    /**
     * 用户激活使用 btc
     * @param userAccessBtc 参数对象
     */
    void userAccessBtc(UserAccessBtc userAccessBtc);

    /**
     * 查询用户是否可以使用 btc
     * @param userId 用户 id
     * @return UserAccessBtc对象
     */
    UserAccessBtc findUserAccessBtc(String userId);
}
