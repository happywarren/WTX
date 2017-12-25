package com.lt.business.core.mongo.promote.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.lt.business.core.mongo.promote.ILogMongoDao;
import com.lt.model.logs.LogInfo;
import com.lt.util.utils.StringTools;

@Repository
public class LogMongoDaoImpl implements ILogMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addLog(LogInfo info) {
		mongoTemplate.save(info);

	}

	@Override
	public Page<LogInfo> findLogInfoByParam(int pageNum, int pageSize, String method,
			String ip, String deviceType) {
		Page<LogInfo> page = new Page<LogInfo>(pageNum, pageSize);
		Query query = new Query();
		if(StringTools.isNotEmpty(method)){
			query.addCriteria(Criteria.where("method").is(method));
		}
		if(StringTools.isNotEmpty(ip)){
			query.addCriteria(Criteria.where("ip").is(ip));
		}
		if(StringTools.isNotEmpty(deviceType)){
			query.addCriteria(Criteria.where("deviceType").is(deviceType));
		}
		List<LogInfo> list = mongoTemplate.find(
				query.limit(pageSize).skip(pageSize*(pageNum-1)), LogInfo.class);
		page.addAll(list);
		return page;
	}
}
