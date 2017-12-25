package com.lt.user.core.dao.mogodb.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lt.model.user.log.NotificationLog;

/**
 * 消息中心实现类
 * @author jingwb
 *
 */
@Repository
public class NotificationLoggerDaoImpl implements NotificationLoggerDao{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<NotificationLog> getNotificationLogPage(String userId,
			int page, int rows) {
		Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        query.skip(page*rows);
        query.limit(rows);
        query.with(new Sort(Direction.DESC, "createDate"));
        return mongoTemplate.find(query,NotificationLog.class);
	}
	
	@Override
	public void saveNotificationLog(NotificationLog log) {
		mongoTemplate.save(log);
	}

}
