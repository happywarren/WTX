package com.lt.manager.service.log;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.log.LtManagerLog;

public interface LtManagerLogService {

	public void log(LtManagerLog log);
	
	public Long findLogCount(Integer staffId, Integer type, Boolean isSuccessed, Date startTime);
	
	public List<LtManagerLog> findLogList(Integer staffId, Integer type, Boolean isSuccessed, Date startTime);
	
	Page<LtManagerLog> selectAllByParm(Boolean isSuccessed, String name, String lastLoginName, Integer id, Integer lastLoginId, String ip, String startDate, String endDate, int rows, int page);

	LtManagerLog findLtManagerLog(Integer staffId, Integer type,
			Boolean isSuccessed);

	List<LtManagerLog> findLtManagerLogForList(Integer staffId, Integer type,
			Boolean isSuccessed, Integer pageSize);

}
