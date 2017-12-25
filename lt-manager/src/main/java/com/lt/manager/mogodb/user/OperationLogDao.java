package com.lt.manager.mogodb.user;

import java.util.Date;

import com.github.pagehelper.Page;
import com.lt.manager.bean.log.OperationLog;
import com.lt.manager.bean.log.OperationLogQuery;
import com.lt.model.logs.LogInfo;

public interface OperationLogDao {

	void saveOperationLog(OperationLog operationLog);

	Page<OperationLog> queryStockList(OperationLogQuery logQuery, Integer page,
			Integer rows);
	
	Page<LogInfo> findList(LogInfo logInfo, int pageNo, int pageSize, String startDate,
			String endDate); 
}
