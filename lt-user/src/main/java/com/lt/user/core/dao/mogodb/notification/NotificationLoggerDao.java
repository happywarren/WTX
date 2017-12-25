package com.lt.user.core.dao.mogodb.notification;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.lt.model.user.log.NotificationLog;

/**
 * 消息中心
 * @author jingwb
 *
 */
public interface NotificationLoggerDao {

	/**
	 * 分页查询消息记录
	 * @param userId
	 * @param page第几页
	 * @param rows几条数据
	 * @return
	 */
	public List<NotificationLog> getNotificationLogPage(String userId,@RequestParam(defaultValue="1") int page,
																			@RequestParam(defaultValue="10")int rows);
	
	/**
	 * 保存消息中心日志
	 * @param log
	 */
	public void saveNotificationLog(NotificationLog log);
	
	
}
