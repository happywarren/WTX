package com.lt.user.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;
import com.lt.api.user.IUserApiLogService;
import com.lt.model.user.log.NotificationLog;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.model.user.log.UserOperateLog;
import com.lt.user.core.dao.mogodb.notification.NotificationLoggerDao;
import com.lt.user.core.dao.mogodb.user.LoggerDao;
import org.springframework.data.redis.core.RedisTemplate;

/**   
* 项目名称：lt-user   
* 类名称：UserLogServiceImpl   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年5月8日 下午1:48:53      
*/
public class UserLogServiceImpl implements IUserApiLogService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoggerDao  loggerDao;

	@Autowired
	private NotificationLoggerDao NotificationLoggerDaoImpl;

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public void insertUserOrderLossProfitDeferLog(OrderLossProfitDefLog defLog) {
		try{
			logger.info("--------调用用户插入操作日志-------");
			loggerDao.insertUserOrderLossProfitDefLog(defLog);
			logger.info("--------用户插入操作日志成功-------");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.info("插入用户止盈止损日志异常");
		}
	}

	@Override
	public Page<OrderLossProfitDefLog> qryUserOrderLossProfitDeferLog(String orderId,int pages,int rows) {
		Page<OrderLossProfitDefLog> page = new Page<OrderLossProfitDefLog>();
		page.setPageNum(pages);
		page.setPageSize(rows);
		page.addAll(loggerDao.qryUserOrderLossProfitDefLogByOrderId(orderId, pages, rows));
		page.setTotal(loggerDao.qryUserOrderLossProfitDefLogSizeByOrderId(orderId));
		return page;
	}

	@Override
	public void insertUserClientLog(UserOperateLog operateLog) {
		synchronized (stringBufferLock(operateLog.getUserId())) {
			Long time = (Long) redisTemplate.opsForHash().get("USER_LOGIN_LOG_REDIS",operateLog.getUserId());
			if (null == time || operateLog.getCreateTime().getTime()/1000 > time/1000){
				redisTemplate.opsForHash().put("USER_LOGIN_LOG_REDIS",operateLog.getUserId(),operateLog.getCreateTime().getTime());
				loggerDao.saveUserOperateLog(operateLog);
			}
		}
	}

	@Override
	public void insertUserClientOffLine(UserOperateLog userLog) {
		synchronized (stringBufferLock(userLog.getUserId())) {
			Long time = (Long) redisTemplate.opsForHash().get("USER_LOGIN_LOG_REDIS", userLog.getUserId());
			if (null != time) {
				userLog.setTime((userLog.getCreateTime().getTime() - time) + "");
				loggerDao.saveUserOperateLog(userLog);
				redisTemplate.opsForHash().delete("USER_LOGIN_LOG_REDIS", userLog.getUserId());
			}

		}
	}

	@Override
	public List<NotificationLog> getNotificationLogPage(String userId,
			int page, int rows) {
		return NotificationLoggerDaoImpl.getNotificationLogPage(userId, page, rows);
	}

	private String stringBufferLock(String string){
		StringBuilder sb = new StringBuilder(string);
		String lock = sb.toString().intern();
		return lock;
	}
	

}
