package com.lt.user.log.dao.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.lt.model.logs.LogInfo;
import com.lt.model.user.log.UserCheckRegcode;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserToken;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.utils.StringTools;

@Repository
public class UserLoggerDaoImpl implements UserLoggerDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void save(LogInfo logInfo) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "id"));
		LogInfo newInfoLog =  mongoTemplate.findOne(query, LogInfo.class);
		if(newInfoLog == null ){
			logInfo.setId(1);
		}else{
			logInfo.setId(newInfoLog.getId()+1);
		}
		mongoTemplate.save(logInfo);
	}
	
	@Override
	public String findList(LogInfo logInfo,int pageNo,int pageSize,Date startDate,Date endDate){
		
		Query query = new Query();
		if(StringTools.isNotEmpty(logInfo) && StringTools.isNotEmpty(logInfo.getCmd())){
			query.addCriteria(Criteria.where("cmd").is(logInfo.getCmd()));
			
		}
		if(StringTools.isNotEmpty(logInfo) && StringTools.isNotEmpty(logInfo.getFunc())){
			query.addCriteria(Criteria.where("func").is(logInfo.getFunc()));
			
		}
		if(StringTools.isNotEmpty(logInfo) && StringTools.isNotEmpty(logInfo.getIp())){
			query.addCriteria(Criteria.where("ip").regex(".*?\\" +logInfo.getIp()+ ".*"));
		}
		if(StringTools.isNotEmpty(logInfo) && StringTools.isNotEmpty(logInfo.getMsg())){
			query.addCriteria(Criteria.where("msg").regex(".*?\\" +logInfo.getMsg()+ ".*"));  
			
		}
		if(StringTools.isNotEmpty(logInfo) && StringTools.isNotEmpty(logInfo.getDeviceNum())){
			query.addCriteria(Criteria.where("deviceNum").is(logInfo.getDeviceNum()));
			
		}
		if(StringTools.isNotEmpty(logInfo) && StringTools.isNotEmpty(logInfo.getDeviceType())){
			query.addCriteria(Criteria.where("deviceType").is(logInfo.getDeviceType()));
			
		}
	    query.addCriteria(Criteria.where("userId").regex(".*?\\" +logInfo.getUserId()+ ".*"));  
        query.addCriteria(Criteria.where("createDate").gte(startDate).lte(endDate));
        query.skip((pageNo - 1) * pageSize);  
        query.limit(pageSize);  
        query.with(new Sort(Direction.DESC, "createDate"));
        logger.debug("mogodb的查询结果为：{}", JSONObject.toJSONString(query));
		List<LogInfo> list = mongoTemplate.find(query, LogInfo.class);
		long count = mongoTemplate.count(query, LogInfo.class);
		Map<String, Object> result = new HashMap();  
		result.put("data", list);  
	    result.put("total", count);  
    	return JSONObject.toJSONString(result);
	}


}
