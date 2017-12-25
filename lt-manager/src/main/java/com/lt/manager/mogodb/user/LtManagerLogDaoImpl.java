package com.lt.manager.mogodb.user;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lt.manager.bean.log.LtManagerLog;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.StringTools;

/**
 * 
 *
 * 描述:日志保存，暂时没有日志读取需求，如有需求
 *
 */
@Repository
public class LtManagerLogDaoImpl implements LtManagerLogDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void saveLog(LtManagerLog log) {
		mongoTemplate.save(log);
	}
	
	@Override
	public Long queryLogCount(Integer staffId, Integer type, Boolean isSuccessed, Date startTime) {
		Query query = new Query();
        query.addCriteria(Criteria.where("staffId").is(staffId));
        if (null != type) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (null != isSuccessed) {
            query.addCriteria(Criteria.where("isSuccessed").is(isSuccessed));
        }
        if (null != startTime) {
            query.addCriteria(Criteria.where("createTime").gte(startTime));
        }
        query.with(new Sort(Direction.DESC, "createTime"));
        return mongoTemplate.count(query, LtManagerLog.class);
	}
	
	public List<LtManagerLog> queryLogList(Integer staffId, Integer type, Boolean isSuccessed, Date startTime) {
		Query query = new Query();
        query.addCriteria(Criteria.where("staffId").is(staffId));
        if (null != type) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (null != isSuccessed) {
            query.addCriteria(Criteria.where("isSuccessed").is(isSuccessed));
        }
        if (null != startTime) {
            query.addCriteria(Criteria.where("createTime").gte(startTime));
        }
        List<LtManagerLog> logList = mongoTemplate.find(query, LtManagerLog.class);
        if (null == logList) {
        	logList = new ArrayList<LtManagerLog>();
        }
        return logList;
	}
	
	@Override
	public LtManagerLog queryLtManagerLog(Integer staffId, Integer type, Boolean isSuccessed) {
		Query query = new Query();
        query.addCriteria(Criteria.where("staffId").is(staffId));
        if (null != type) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (null != isSuccessed) {
            query.addCriteria(Criteria.where("isSuccessed").is(isSuccessed));
        }
        return mongoTemplate.findOne(query, LtManagerLog.class);
	}

	@Override
	public List<LtManagerLog> findLtManagerLogForList(Integer staffId, Integer type, Boolean isSuccessed,Integer pageSize) {
		Query query = new Query();
        query.addCriteria(Criteria.where("staffId").is(staffId));
        if (null != type) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (null != isSuccessed) {
            query.addCriteria(Criteria.where("isSuccessed").is(isSuccessed));
        }
        query.limit(pageSize);
        query.with(new Sort(Direction.DESC, "createTime"));
        return mongoTemplate.find(query, LtManagerLog.class);
	}
	@Override
	public List<LtManagerLog> selectAllByParm(Boolean isSuccessed,String name, String lastLoginName, Integer id, Integer lastLoginId, String ip, String startDate,String endDate,int index, int size) throws ParseException {
		Query query = new Query();
		if(StringTools.isNotEmpty(name)){
			query.addCriteria(Criteria.where("userName").is(name));
		}
		if(StringTools.isNotEmpty(lastLoginName)){
			query.addCriteria(Criteria.where("lastLoginName").is(lastLoginName));
		}
		if (null != isSuccessed) {
            query.addCriteria(Criteria.where("isSuccessed").is(isSuccessed));
        }
		if(id!=null&&id>0){
			query.addCriteria(Criteria.where("staffId").is(id));
		}
		if(lastLoginId!=null&&lastLoginId>0){
			query.addCriteria(Criteria.where("lastLoginId").is(lastLoginId));
		}
		if(StringTools.isNotEmpty(ip)){
			query.addCriteria(Criteria.where("ip").is(ip));
		}
		if (StringTools.isNotEmpty(startDate)&&StringTools.isNotEmpty(endDate)) {
			query.addCriteria(Criteria.where("createTime").gte(CalendarTools.parseDateTime(startDate))
					.lt(CalendarTools.parseDateTime(endDate)));
		} else if (StringTools.isNotBlank(startDate)) {
			query.addCriteria(Criteria.where("createTime").gte(CalendarTools.parseDateTime(startDate)));
		} else if (StringTools.isNotBlank(endDate)) {
			query.addCriteria(Criteria.where("createTime").lt(CalendarTools.parseDateTime(endDate)));
		}
		query.skip(index);// skip相当于从那条记录开始  
		query.limit(size);// 从skip开始,取多少条记录  
		query.with(new Sort(Direction.DESC, "createTime"));
		return mongoTemplate.find(query, LtManagerLog.class);
	}
	@Override
	public long selectAllByParmCount(Boolean isSuccessed,String name, String lastLoginName, Integer id, Integer lastLoginId, String ip, String startDate,String endDate,int index, int size) throws ParseException {
		Query query = new Query();
		if(StringTools.isNotEmpty(name)){
			query.addCriteria(Criteria.where("userName").is(name));
		}
		if(StringTools.isNotEmpty(lastLoginName)){
			query.addCriteria(Criteria.where("lastLoginName").is(lastLoginName));
		}
		if (null != isSuccessed) {
            query.addCriteria(Criteria.where("isSuccessed").is(isSuccessed));
        }
		if(id!=null&&id>0){
			query.addCriteria(Criteria.where("staffId").is(id));
		}
		if(lastLoginId!=null&&lastLoginId>0){
			query.addCriteria(Criteria.where("lastLoginId").is(lastLoginId));
		}
		if(StringTools.isNotEmpty(ip)){
			query.addCriteria(Criteria.where("ip").is(ip));
		}
		if (StringTools.isNotEmpty(startDate)&&StringTools.isNotEmpty(endDate)) {
			query.addCriteria(Criteria.where("createTime").gte(CalendarTools.parseDateTime(startDate))
					.lt(CalendarTools.parseDateTime(endDate)));
		} else if (StringTools.isNotBlank(startDate)) {
			query.addCriteria(Criteria.where("createTime").gte(CalendarTools.parseDateTime(startDate)));
		} else if (StringTools.isNotBlank(endDate)) {
			query.addCriteria(Criteria.where("createTime").lt(CalendarTools.parseDateTime(endDate)));
		}
		query.with(new Sort(Direction.DESC, "createTime"));
		return mongoTemplate.count(query, LtManagerLog.class);
	}

}
