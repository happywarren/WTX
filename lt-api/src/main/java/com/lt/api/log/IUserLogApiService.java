package com.lt.api.log;

import java.util.Date;

import com.lt.model.logs.LogInfo;

public interface IUserLogApiService {
	/**
	 * 增
	 * @param logInfo
	 */
	void save(LogInfo logInfo);
	
	/**
	 * 查
	 * @param logInfo
	 * @param rows
	 * @param page
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String findList(LogInfo logInfo, int rows, int page, Date startDate,
			Date endDate);
}
