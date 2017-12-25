package com.lt.manager.service.impl.log;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.log.LtManagerLog;
import com.lt.manager.mogodb.user.LtManagerLogDao;
import com.lt.manager.service.log.LtManagerLogService;

@Service
public class LtManagerLogServiceImpl implements LtManagerLogService {

	@Autowired
	private LtManagerLogDao loggerDao;

	public void log(LtManagerLog log) {
		loggerDao.saveLog(log);
	}

	@Override
	public Long findLogCount(Integer staffId, Integer type, Boolean isSuccessed, Date startTime) {
		return loggerDao.queryLogCount(staffId, type, isSuccessed, startTime);
	}

	@Override
	public LtManagerLog findLtManagerLog(Integer staffId, Integer type, Boolean isSuccessed) {
		return loggerDao.queryLtManagerLog(staffId, type, isSuccessed);
	}

	@Override
	public List<LtManagerLog> findLtManagerLogForList(Integer staffId, Integer type, Boolean isSuccessed,Integer pageSize) {
		return loggerDao.findLtManagerLogForList(staffId, type, isSuccessed,pageSize);
	}

	@Override
	public List<LtManagerLog> findLogList(Integer staffId, Integer type, Boolean isSuccessed, Date startTime) {
		return loggerDao.queryLogList(staffId, type, isSuccessed, startTime);
	}

	@Override
	public Page<LtManagerLog> selectAllByParm(Boolean isSuccessed,String name, String lastLoginName, Integer id, Integer lastLoginId, String ip, String startDate,String endDate,int rows, int page) {
		Page<LtManagerLog> pages =new Page<LtManagerLog>();
		int a = (page-1)*rows;
		try {
			List<LtManagerLog> list = loggerDao.selectAllByParm(isSuccessed,name, lastLoginName, id, lastLoginId, ip, startDate, endDate, a, rows);
			long count = loggerDao.selectAllByParmCount(isSuccessed,name, lastLoginName, id, lastLoginId, ip, startDate, endDate, a, rows);
			pages.setPageNum(a);
			pages.setPageSize(rows);
			pages.setTotal(count);
			pages.addAll(list);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pages;		
	}

}
