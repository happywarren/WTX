package com.lt.user.core.dao.mogodb.user;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.lt.model.user.UserContant;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.model.user.log.UserCheckRegcode;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserToken;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.utils.StringTools;

/**   
* 项目名称：lt-user   
* 类名称：LoggerDaoImp   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年11月29日 下午1:49:26      
*/
@Repository
public class LoggerDaoImpl implements LoggerDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void saveUserOperateLog(UserOperateLog operateLog) {
		Query query = new Query();
        query.with(new Sort(Direction.DESC, "_id"));
        UserOperateLog newInfoLog =  mongoTemplate.findOne(query, UserOperateLog.class);
        if(newInfoLog == null ){
        	operateLog.setId(1);
        }else{
			operateLog.setId(newInfoLog.getId()+1);
        }
		logger.info("插入mongo数据：{}",JSONObject.toJSONString(operateLog));
		mongoTemplate.save(operateLog);
	}

	@Override
	public UserToken selectByIdAndToken(String id) {
		UserToken userToken = new UserToken();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		userToken = mongoTemplate.findOne(query, UserToken.class);
    	return userToken;
	}

	@Override
	public void saveToken(UserToken idToken) {
		mongoTemplate.save(idToken);
	}

	@Override
	public void delToken(String id) {
		Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, UserToken.class);
	}

	@Override
	public List<UserOperateLog> getLastUserOperateLog(String userId, String type, Boolean isSuccessd, Date createTime,
			int pageSize) {
		Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId.toString()));
        if (type != null ) {
            query.addCriteria(Criteria.where("operateType").is(Integer.parseInt(type)));
        }
        logger.debug("加入是否成功条件");
        if(isSuccessd != null){
        	query.addCriteria(Criteria.where("isSuccessed").is(isSuccessd));
        }
        if (createTime !=null) {
            query.addCriteria(Criteria.where("createTime").gte(createTime));
        }
        query.limit(pageSize);
        query.with(new Sort(Direction.DESC, "createTime"));

        logger.info("mogodb的条件为：{}", JSONObject.toJSONString(query));
        return mongoTemplate.find(query, UserOperateLog.class);
	}

	@Override
	public List<UserCheckRegcode> getUserCheckRegCode(String tele, Date createTime) {
		Query query = new Query();
        query.addCriteria(Criteria.where("tele").is(tele));
        query.addCriteria(Criteria.where("isSuccessed").is(true));
        query.addCriteria(Criteria.where("createTime").gte(createTime));
        query.with(new Sort(Direction.DESC, "createTime"));
        return  mongoTemplate.find(query, UserCheckRegcode.class);
	}

	@Override
	public void saveUserCheckRegCode(UserCheckRegcode checkRegcode) {
		mongoTemplate.save(checkRegcode);
	}

	@Override
	public List<UserUpdateInfoLog> getUserUpdateInfoLog(UserUpdateInfoLog infoLog) {
		Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(infoLog.getUserId()));

        if(infoLog.getUpdate_type() != null){
        	 query.addCriteria(Criteria.where("update_type").is(infoLog.getUpdate_type()));
        }

        query.with(new Sort(Direction.DESC, "update_time"));
        return mongoTemplate.find(query, UserUpdateInfoLog.class);
	}

	@Override
	public void saveUserUpdateInfoLog(UserUpdateInfoLog infoLog) {
		Query query = new Query();
        query.with(new Sort(Direction.DESC, "_id"));
        UserUpdateInfoLog newInfoLog =  mongoTemplate.findOne(query, UserUpdateInfoLog.class);
    	if(newInfoLog == null){
    		infoLog.setId(1);
    	}else{
    		infoLog.setId(newInfoLog.getId()+1);
    	}
    	logger.info(infoLog.toString());
        mongoTemplate.save(infoLog);
	}

	@Override
	public void insertUserOrderLossProfitDefLog(OrderLossProfitDefLog defLog) {
		Query query = new Query();
        query.with(new Sort(Direction.DESC, "_id"));
        OrderLossProfitDefLog newInfoLog =  mongoTemplate.findOne(query, OrderLossProfitDefLog.class);
        if(newInfoLog == null ){
        	defLog.setId(1);
        }else{
        	defLog.setId(newInfoLog.getId()+1);
        }
        logger.info("插入mongo数据：{}",JSONObject.toJSONString(defLog));
		mongoTemplate.save(defLog);
	}

	@Override
	public List<OrderLossProfitDefLog> qryUserOrderLossProfitDefLogByOrderId(String orderId,int page,int rows) {
		Query query = new Query();
        query.addCriteria(Criteria.where("orderId").is(orderId));

        query.skip((page - 1) * rows).limit(rows); // 分页
        query.with(new Sort(Direction.DESC, "createTime"));

        logger.info("mogodb的条件为：{}", JSONObject.toJSONString(query));
        return mongoTemplate.find(query, OrderLossProfitDefLog.class);
	}

	@Override
	public Integer qryUserOrderLossProfitDefLogSizeByOrderId(String orderId) {
		// TODO Auto-generated method stub
		Query query = new Query();
        query.addCriteria(Criteria.where("orderId").is(orderId));

		List<OrderLossProfitDefLog> defLogs = mongoTemplate.find(query, OrderLossProfitDefLog.class);

		if(CollectionUtils.isEmpty(defLogs)){
			return 0 ;
		}else{
			return defLogs.size() ;
		}
	}

	@Override
	public List<UserUpdateInfoLog> getUserUpdateInfoLogByTime(UserUpdateInfoLog infoLog, Date updateTime) {
		Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(infoLog.getUserId()));
        query.addCriteria(Criteria.where("update_time").gte(updateTime));
        if(infoLog.getUpdate_type() != null){
        	query.addCriteria(Criteria.where("update_type").is(infoLog.getUpdate_type()));
        }
        query.with(new Sort(Direction.DESC, "update_time"));
        logger.info("************"+query.toString());
        return mongoTemplate.find(query, UserUpdateInfoLog.class);
	}

	@Override
	public UserToken selectToken(String token) {
		UserToken userToken = new UserToken();
		Query query = new Query();
		query.addCriteria(Criteria.where("token").is(token));
		userToken = mongoTemplate.findOne(query, UserToken.class);
    	return userToken;
	}

	@Override
	public void saveUserOperateLogForOffLine(UserOperateLog userLog) {
		if(!StringTools.isNotEmpty(userLog.getUserId())){
			logger.error("用户为空 你下线个鬼啊");
			return;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userLog.getUserId()));
		query.addCriteria(Criteria.where("operateType").is(UserContant.OPERATE_USER_OFFLINE_LOG));
        query.with(new Sort(Direction.DESC, "_id"));
        //查询最近下线时间
        UserOperateLog newInfoLog =  mongoTemplate.findOne(query, UserOperateLog.class);
        logger.info("newInfoLog：{}", JSONObject.toJSONString(newInfoLog));
        //查询最近下线时间后最近的上线时间
        query = new Query();
        query.addCriteria(Criteria.where("userId").is(userLog.getUserId()));
        query.addCriteria(Criteria.where("operateType").is(UserContant.OPERATE_USER_GO_LIVE_LOG));
        if(StringTools.isNotEmpty(newInfoLog)&&StringTools.isNotEmpty(newInfoLog.getCreateTime())){
        	query.addCriteria(Criteria.where("createTime").gt(newInfoLog.getCreateTime()));
		}
        query.with(new Sort(Direction.ASC, "_id"));
        UserOperateLog log =  mongoTemplate.findOne(query, UserOperateLog.class);
        if(StringTools.isNotEmpty(log)){
			long time = System.currentTimeMillis() - log.getCreateTime().getTime();
			userLog.setTime(time+"");
		}

        logger.info("插入mongo数据：{}",JSONObject.toJSONString(userLog));
        saveUserOperateLog(userLog);
	}
}
