package com.lt.promote.mongo.promote.impl;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;




import com.lt.promote.mongo.promote.IPromoteMongoDao;
import com.lt.promote.vo.PromoteLibrary;
import com.lt.promote.vo.UserCount;
import com.lt.util.utils.DateTools;

@Repository
public class PromoteMongoDaoImpl implements IPromoteMongoDao{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void visitCount(Integer count, String userId) {
		Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        UserCount uc =  mongoTemplate.findOne(query, UserCount.class);
        if(uc!=null){
        	mongoTemplate.remove(query, UserCount.class);
        }
		uc = new UserCount();
		uc.setCount(count);
		uc.setUserId(userId);
		mongoTemplate.save(uc);
	}

	@Override
	public void addPromoteLibrary(String userId, String ip, String tele) {
		PromoteLibrary library = new PromoteLibrary();
		library.setCreateDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		library.setUserId(userId);
		library.setIp(ip);
		library.setTele(tele);
		mongoTemplate.save(library);
	}

	@Override
	public int getVisitCount(String userId) {
		Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        UserCount uc =  mongoTemplate.findOne(query, UserCount.class);
        if(uc!=null){
        	return uc.getCount();
        }else{
        	return 0;
        }
	}

	@Override
	public PromoteLibrary getPromoteLibraryByTele(String tele) {
		String startTime = DateTools.get12HoursSith();
		Query query = new Query();
        query.addCriteria(Criteria.where("tele").is(tele));
        query.addCriteria(Criteria.where("createDate").gt(startTime));
        query.with(new Sort(Direction.DESC, "createDate"));
		return mongoTemplate.findOne(query, PromoteLibrary.class);
	}

	@Override
	public PromoteLibrary getPromoteLibraryByIP(String ip) {
		String startTime = DateTools.get12HoursSith();
		Query query = new Query();
        query.addCriteria(Criteria.where("ip").is(ip));
        query.addCriteria(Criteria.where("createDate").gt(startTime));
        query.with(new Sort(Direction.DESC, "createDate"));
		return mongoTemplate.findOne(query, PromoteLibrary.class);
	}

}
