package com.lt.manager.mogodb.user;

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
import com.lt.model.user.log.UserOperateLog;
import com.lt.util.utils.StringTools;

/**   
* 项目名称：lt-manager   
* 类名称：UserLoggerDaoImpl   
* 类描述：   用户日志操作类（实现类）  
* 创建人：yuanxin   
* 创建时间：2017年1月13日 下午2:12:18      
*/
@Repository
public class UserLoggerDaoImpl implements UserLoggerDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public UserOperateLog queryUserOPerateLog(UserOperateLog operateLog) {
		Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(operateLog.getUserId()));
        if (operateLog.getOperateType() != null ) {
            query.addCriteria(Criteria.where("operateType").is(operateLog.getOperateType().intValue()));
        }
        
        if(operateLog.getIsSuccessed() != null){
        	query.addCriteria(Criteria.where("isSuccessed").is(operateLog.getIsSuccessed()));
        }
        
        if (operateLog.getCreateTime() !=null) {
            query.addCriteria(Criteria.where("createTime").gte(operateLog.getCreateTime()));
        }
        
        query.with(new Sort(Direction.DESC, "createTime"));
        
        logger.info("mogodb的查询结果为：{}", JSONObject.toJSONString(query));
        return mongoTemplate.findOne(query, UserOperateLog.class);
	}

	
}
