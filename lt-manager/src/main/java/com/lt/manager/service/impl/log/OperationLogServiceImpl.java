/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.manager.service.impl.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.log.OperationLog;
import com.lt.manager.bean.log.OperationLogQuery;
import com.lt.manager.mogodb.user.OperationLogDao;
import com.lt.manager.mogodb.user.UserOperateLogDao;
import com.lt.manager.service.log.OperationLogService;
import com.lt.model.logs.LogInfo;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserUpdateInfoLog;

@Service
public class OperationLogServiceImpl implements OperationLogService {
	
	@Autowired
	private OperationLogDao operationLogDao;
	
	@Autowired
	private UserOperateLogDao UserOperateLogDao;

	@Override
	public void saveOperationLog(OperationLog operationLog) {
		operationLogDao.saveOperationLog(operationLog);
	}

	@Override
	public Page<OperationLog> findStockList(OperationLogQuery logQuery, Integer page, Integer rows) {
		return operationLogDao.queryStockList(logQuery, page, rows);
	}

	@Override
	public Page<LogInfo>  findList(LogInfo logInfo, int pageNo, int pageSize,
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		return operationLogDao.findList(logInfo, pageNo, pageSize, startDate, endDate);
	}

	@Override
	public Page<UserOperateLog> findUserOperateLogList(
			UserOperateLog userOperateLog, Integer page, Integer rows,
			String startDate, String endDate) {
		return UserOperateLogDao.findList(userOperateLog, page, rows, startDate, endDate);
	}

	@Override
	public Page<UserUpdateInfoLog> findUserUpdateInfoLogList(String userId,Integer updateType,
			Integer page, Integer rows, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return UserOperateLogDao.getUserUpdateInfoLog(userId, updateType, page, rows, startDate, endDate);
	}
	
	

}
