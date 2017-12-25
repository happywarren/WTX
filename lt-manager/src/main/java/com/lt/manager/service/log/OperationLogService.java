/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.manager.service.log;

import java.util.Date;

import com.github.pagehelper.Page;
import com.lt.manager.bean.log.OperationLog;
import com.lt.manager.bean.log.OperationLogQuery;
import com.lt.model.logs.LogInfo;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserUpdateInfoLog;

/**
 *
 * 描述:
 *
 */
public interface OperationLogService {
	
	public void saveOperationLog(OperationLog operationLog);
	
	/**
	 * 后台访问日志
	 * @param logQuery
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<OperationLog> findStockList(OperationLogQuery logQuery, Integer page, Integer rows);
	
	/**
	 * 前端所有访问日志
	 * @param logInfo
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Page<LogInfo>  findList(LogInfo logInfo, int pageNo, int pageSize, String startDate,
			String endDate);

	/**
	 * 用户登录日志
	 * @param userOperateLog
	 * @param page
	 * @param rows
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Page<UserOperateLog> findUserOperateLogList(UserOperateLog userOperateLog,
			Integer page, Integer rows, String startDate, String endDate);

	/**
	 * 用户修改日志
	 * @param userId
	 * @param page
	 * @param rows
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Page<UserUpdateInfoLog> findUserUpdateInfoLogList(String userId,
			Integer updateType, Integer page, Integer rows, String startDate,
			String endDate);
}
