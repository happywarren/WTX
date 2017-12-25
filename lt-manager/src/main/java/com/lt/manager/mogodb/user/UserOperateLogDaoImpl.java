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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.LoggerTools;
import com.lt.util.utils.CalendarTools;

@Repository
public class UserOperateLogDaoImpl implements UserOperateLogDao {
	
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private MongoTemplate mongoTemplate;

	
	@Override
	public Page<UserOperateLog> findList(UserOperateLog userOperateLog,
			Integer pageNo, Integer pageSize, String startDate, String endDate) {
		Page<UserOperateLog> pageView = new Page<UserOperateLog>();
		List<UserOperateLog> list = new ArrayList<UserOperateLog>();
		Query query = new Query();
		if (userOperateLog.getUserId() != null) {
			query.addCriteria(Criteria.where("userId").is(userOperateLog.getUserId()));
		}
		if (StringUtils.isNotEmpty(userOperateLog.getBehaviorType())) {
			query.addCriteria(Criteria.where("behaviorType").regex( ".*?" + userOperateLog.getBehaviorType() + ".*" ));
		}
		if (StringUtils.isNotEmpty(userOperateLog.getOperateName())) {
			query.addCriteria(Criteria.where("operateName").regex( ".*?" + userOperateLog.getOperateName() + ".*" ));
		}
		String saTime = startDate;
		String eaTime = endDate;
		Date sTime = null;
		Date eTime = null;
		try {
			sTime = CalendarTools.parseDateTime(saTime, CalendarTools.DATETIME);
			eTime = CalendarTools.parseDateTime(eaTime, CalendarTools.DATETIME);
		} catch (ParseException e) {
			logger.error("日期转换失败：", e);
		}
		if (StringUtils.isNoneBlank(saTime, eaTime)) {
			query.addCriteria(Criteria.where("createTime").gte(sTime).lte(eTime));
		} else if (StringUtils.isNotBlank(saTime)) {
			query.addCriteria(Criteria.where("createTime").gte(sTime));
		} else if (StringUtils.isNotBlank(eaTime)) {
			query.addCriteria(Criteria.where("createTime").lt(eTime));
		}
		
		long count = mongoTemplate.count(query, UserOperateLog.class);
		pageView.setTotal(count);
		if (count > 0) {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 20;
			}
			int skip = (pageNo == 1 ? 0 : pageSize * (pageNo-1));
			query.skip(skip);// skip相当于从那条记录开始  
			query.limit(pageSize);// 从skip开始,取多少条记录  
			query.with(new Sort(Direction.DESC, "_id"));
			list = mongoTemplate.find(query, UserOperateLog.class);
		}
		pageView.addAll(list);
		return pageView;
	}
	
	@Override
	public Page<UserUpdateInfoLog> getUserUpdateInfoLog(String userId,Integer updateType,
			Integer pageNo, Integer pageSize,String startDate ,String endDate) {
		
		Page<UserUpdateInfoLog> pageView = new Page<UserUpdateInfoLog>();
		List<UserUpdateInfoLog> list = new ArrayList<UserUpdateInfoLog>();
		Query query = new Query();
		if (userId != null) {
			query.addCriteria(Criteria.where("userId").is(userId.toString()));
		}
		 if(updateType != null){
        	query.addCriteria(Criteria.where("update_type").is(updateType));
        }
		String saTime = startDate;
		String eaTime = endDate;
		Date sTime = null;
		Date eTime = null;
		try {
			sTime = CalendarTools.parseDateTime(saTime, CalendarTools.DATETIME);
			eTime = CalendarTools.parseDateTime(eaTime, CalendarTools.DATETIME);
		} catch (ParseException e) {
			logger.error("日期转换失败：", e);
		}
		if (StringUtils.isNoneBlank(saTime, eaTime)) {
			query.addCriteria(Criteria.where("update_time").gte(sTime).lte(eTime));
		} else if (StringUtils.isNotBlank(saTime)) {
			query.addCriteria(Criteria.where("update_time").gte(sTime));
		} else if (StringUtils.isNotBlank(eaTime)) {
			query.addCriteria(Criteria.where("update_time").lt(eTime));
		}
		
		long count = mongoTemplate.count(query, UserUpdateInfoLog.class);
		pageView.setTotal(count);
		if (count > 0) {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 20;
			}
			int skip = (pageNo == 1 ? 0 : pageSize * (pageNo-1));
			query.skip(skip);// skip相当于从那条记录开始  
			query.limit(pageSize);// 从skip开始,取多少条记录  
			query.with(new Sort(Direction.DESC, "_id"));
			list = mongoTemplate.find(query, UserUpdateInfoLog.class);
		}
		pageView.addAll(list);
		return pageView;
	}
}
