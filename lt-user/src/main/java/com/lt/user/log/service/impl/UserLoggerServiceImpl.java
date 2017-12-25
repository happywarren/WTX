package com.lt.user.log.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.log.IUserLogApiService;
import com.lt.model.logs.LogInfo;
import com.lt.user.log.dao.mongodb.UserLoggerDao;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

@Service
public class UserLoggerServiceImpl implements IUserLogApiService {

	@Autowired
	private UserLoggerDao userLoggerDao;
	
	@Override
	public void save(LogInfo logInfo) {
		try {
			userLoggerDao.save(logInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00002);
		}
	}

	@Override
	public String findList(LogInfo logInfo, int rows, int page,Date startDate ,Date endDate) {
		try {
			return userLoggerDao.findList(logInfo, page, rows, startDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00002);
		}
	}

}
