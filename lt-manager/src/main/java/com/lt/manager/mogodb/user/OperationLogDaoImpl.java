/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.manager.mogodb.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.log.OperationLog;
import com.lt.manager.bean.log.OperationLogQuery;
import com.lt.model.logs.LogInfo;
import com.lt.util.LoggerTools;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.StringTools;

@Repository("operationLogDao")
public class OperationLogDaoImpl implements OperationLogDao {
	
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void saveOperationLog(OperationLog operationLog) {
		mongoTemplate.save(operationLog);
	}

	@Override
	public Page<OperationLog> queryStockList(OperationLogQuery logQuery, Integer page, Integer rows) {
		Page<OperationLog> pageView = new Page<OperationLog>();
		List<OperationLog> list = new ArrayList<OperationLog>();
		Query query = new Query();
		if (logQuery.getStaffId() != null) {
			query.addCriteria(Criteria.where("staffId").is(logQuery.getStaffId()));
		}
		if (StringUtils.isNotEmpty(logQuery.getUrl())) {
			query.addCriteria(Criteria.where("url").regex( ".*?" + logQuery.getUrl() + ".*" ));
		}
		if (StringUtils.isNotEmpty(logQuery.getParameters())) {
			query.addCriteria(Criteria.where("parameters").regex( ".*?" + logQuery.getParameters() + ".*" ));
		}
		String saTime = logQuery.getStartAccessTime();
		String eaTime = logQuery.getEndAccessTime();
		Date sTime = null;
		Date eTime = null;
		try {
			sTime = CalendarTools.parseDateTime(saTime, CalendarTools.DATETIME);
			eTime = CalendarTools.parseDateTime(eaTime, CalendarTools.DATETIME);
		} catch (ParseException e) {
			logger.error("日期转换失败：", e);
		}
		if (StringUtils.isNoneBlank(saTime, eaTime)) {
			query.addCriteria(Criteria.where("accessTime").gte(sTime).lte(eTime));
		} else if (StringUtils.isNotBlank(saTime)) {
			query.addCriteria(Criteria.where("accessTime").gte(sTime));
		} else if (StringUtils.isNotBlank(eaTime)) {
			query.addCriteria(Criteria.where("accessTime").lt(eTime));
		}
		
		long count = mongoTemplate.count(query, OperationLog.class);
		pageView.setTotal(count);
		if (count > 0) {
			if (page == null) {
				page = 1;
			}
			if (rows == null) {
				rows = 20;
			}
			int skip = (page == 1 ? 0 : rows * (page-1));
			query.skip(skip);// skip相当于从那条记录开始  
			query.limit(rows);// 从skip开始,取多少条记录  
			query.with(new Sort(Direction.DESC, "_id"));
			list = mongoTemplate.find(query, OperationLog.class);
		}
		pageView.addAll(list);
		return pageView;
	}
	@Override
	public Page<LogInfo> findList(LogInfo logInfo,int pageNo,int pageSize,String startDate,String endDate){
		
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
		
		if(StringTools.isNotEmpty(logInfo) && StringTools.isNotEmpty(logInfo.getUserId())){
			query.addCriteria(Criteria.where("userId").is(logInfo.getUserId()));
		}
	    if(StringTools.isNotEmpty(startDate)){
	    	query.addCriteria(Criteria.where("createDate").gte(startDate));
	    }
	    if(StringTools.isNotEmpty(endDate)){
	    	query.addCriteria(Criteria.where("createDate").lte(endDate));
	    }
        query.skip((pageNo - 1) * pageSize);  
        query.limit(pageSize);  
        query.with(new Sort(Direction.DESC, "createDate"));
        logger.debug("mogodb的查询结果为：{}", JSONObject.toJSONString(query));
		List<LogInfo> list = mongoTemplate.find(query, LogInfo.class);
		long count = mongoTemplate.count(query, LogInfo.class);
		Page<LogInfo> pageView = new Page<LogInfo>(pageNo, pageSize);
		pageView.setTotal(count);
		pageView.addAll(list);
    	return pageView;
	}

}
