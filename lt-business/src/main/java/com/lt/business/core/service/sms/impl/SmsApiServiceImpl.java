package com.lt.business.core.service.sms.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.iapppay.demo.HttpUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lt.api.sms.ISmsApiService;
import com.lt.business.core.dao.sms.SmsDao;
import com.lt.constant.redis.RedisUtil;
import com.lt.model.sms.SystemMessage;
import com.lt.model.sms.SystemMessageContant;
import com.lt.model.user.UserContant;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.SendSMSUtils;

/**   
* 项目名称：lt-business   
* 类名称：SmsApiServiceImpl   
* 类描述：处理消息类   
* 创建人：yuanxin   
* 创建时间：2016年11月29日 下午4:00:31      
*/
@Service
public class SmsApiServiceImpl implements ISmsApiService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SmsDao smsDao;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	BoundHashOperations<String, String, String> sysCfgRedis;

	@Override
	public boolean sendImmediateMsg(SystemMessage announce) throws Exception{
		logger.info("进入到消息模块");
		boolean flag = true ;
		String destination = announce.getDestination();
		
		try{
			sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String sms_content =  sysCfgRedis.get(UserContant.SYSCONFIG_SMS_IDENTIFY_CONFIG);
			String content =  sms_content.replace("(code)", announce.getContent());
			logger.info("目的地为：{}，内容为：{}",destination,content);
			String str = sendYunXin(destination,  content,UserContant.SYSCONFIG_SMS_TYPE_RIGHT);
			logger.info("+++++++++++++++++++++++++++++++++短信商返回信息"+str);
			if(str!=null&&str.length()>5){
				announce.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS);
			}else{
				announce.setStatus(SystemMessageContant.STATUS_FAIL);
				flag = false ;
				logger.error("sendSmsApi error:"+str);
			}
			announce.setCause("短信验证码");
			announce.setTryCount(0);
			announce.setSendDate(new Date());
			smsDao.saveSms(announce);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		
		return flag;
	}
	
	/**
	 * 调用云信留客短信配置 发送短信
	 * @param destination 目的地
	 * @param context 内容 （含尾缀）
	 * @param channel 渠道
	 * @return
	 * @throws LTException    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月12日 下午1:43:58
	 */
	private String sendYunXin(String destination,String context,String channel) throws LTException {
		String  str = "";
		try{
			sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String user_code = sysCfgRedis.get(UserContant.SYSCONFIG_SMS_USER_CODE) ;//"zjrccf";
			String user_pass = sysCfgRedis.get(UserContant.SYSCONFIG_SMS_USER_PASS) ;//"CAsfgy125";
			String url = sysCfgRedis.get(UserContant.SYSCONFIG_SMS_SERVICE_URL);
			NameValuePair[] params = {
					//登录密码
					new NameValuePair("apikey", user_pass),
					//手机号码
					new NameValuePair("mobile", destination),
					//短信内容
					new NameValuePair("content", context) };
			final NameValuePair[] param =  params;
			logger.info("进入SendSMSUtils方法中");
			/*"http://h.1069106.com:1210/services/msgsend.asmx/SendMsg"/*url*/
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("apikey",user_pass);
			paramMap.put("mobile",destination);
			paramMap.put("content",context);
			str =  HttpTools.sendPost(url,paramMap);
			//str = SendSMSUtils.http(url, param);
			logger.info("执行完成，返回字符串为Str：{}",str);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00004);
		}

		if(str != null){
			JSONObject jsonObject =  (JSONObject) JSONObject.parse(str);
			if(jsonObject.getInteger("code") == 1){
				return "消息短信发送成功！";
			}
		}
		return null;
	}

	@Override
	public boolean sendExpirationMsg(SystemMessage announce) throws Exception {
		logger.info("进入到消息模块");
		boolean flag = true ;
		String destination = announce.getDestination();
		
		try{
			sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String sms_content =  sysCfgRedis.get(UserContant.SYSCONFIG_SMS_EXPIRATION_CONFIG);
			String content =  sms_content.replace("(content)", announce.getContent());
			logger.info("目的地为：{}，内容为：{}",destination,content);
			String str = sendYunXin(destination,  content,UserContant.SYSCONFIG_SMS_TYPE_TRIGGER);
			logger.debug("+++++++++++++++++++++++++++++++++短信商返回信息"+str);
			if(str!=null&&str.length()>5){
				announce.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS);
			}else{
				announce.setStatus(SystemMessageContant.STATUS_FAIL);
				flag = false ;
				logger.error("sendSmsApi error:"+str);
			}
			announce.setTryCount(0);
			announce.setSendDate(new Date());
			smsDao.saveSms(announce);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		
		return flag;
	}

	@Override
	public SystemMessage findLastCodeByStaffId(Integer staffId) {
		return smsDao.findLastCodeByStaffId(staffId);
	}

	@Override
	public boolean sendUserFundMsg(SystemMessage announce) throws Exception {
		logger.debug("进入到消息模块");
		boolean flag = true ;
		String destination = announce.getDestination();
		
		try{
			logger.debug("目的地为：{}，内容为：{}",destination,announce.getContent());
			String str = sendYunXin(destination,  announce.getContent(),UserContant.SYSCONFIG_SMS_TYPE_TRIGGER);
			logger.debug("+++++++++++++++++++++++++++++++++短信商返回信息"+str);
			if(str!=null&&str.length()>5){
				announce.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS);
			}else{
				announce.setStatus(SystemMessageContant.STATUS_FAIL);
				flag = false ;
				logger.error("sendSmsApi error:"+str);
			}
			announce.setTryCount(0);
			announce.setSendDate(new Date());
			smsDao.saveSms(announce);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		
		return flag;
	}

}
