package com.lt.user.log.dao.mongodb;

import java.util.Date;
import java.util.List;

import com.lt.model.logs.LogInfo;
import com.lt.model.user.log.UserCheckRegcode;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserToken;
import com.lt.model.user.log.UserUpdateInfoLog;

/**   
 * 用户操作日志
*/
public interface UserLoggerDao {
	
	

	/**
	 * 保存
	 * @param logInfo
	 */
	void save(LogInfo logInfo);

	/**
	 * 分页查询  返回JSON
	 * @param logInfo
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findList(LogInfo logInfo, int pageNo, int pageSize, Date startDate,
			Date endDate);
}
