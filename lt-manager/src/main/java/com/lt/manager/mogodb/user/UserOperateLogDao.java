package com.lt.manager.mogodb.user;

import com.github.pagehelper.Page;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserUpdateInfoLog;

public interface UserOperateLogDao {

	Page<UserOperateLog> findList(UserOperateLog userOperateLog,
			Integer pageNo, Integer pageSize, String startDate, String endDate);

	Page<UserUpdateInfoLog> getUserUpdateInfoLog(String userId,
			Integer updateType, Integer pageNo, Integer pageSize,
			String startDate, String endDate); 
}
