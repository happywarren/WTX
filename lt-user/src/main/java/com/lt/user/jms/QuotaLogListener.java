package com.lt.user.jms;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.user.IUserApiLogService;
import com.lt.model.user.UserContant;
import com.lt.model.user.log.UserOperateLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Date;


@Component
public class QuotaLogListener implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserApiLogService IUserApiLogService;
	
	@Override
	public void onMessage(Message message) {
		try {

			TextMessage textMessage = null;
			if (message instanceof TextMessage) {
				textMessage = (TextMessage) message;
			}
			if (textMessage == null) {
				return;
			}
			long startTime = new Date().getTime();
			logger.info("===========接收到日志消息结果=============："+textMessage.getText());
			UserOperateLog log = JSONObject.parseObject(textMessage.getText(), UserOperateLog.class);
			if (log == null) {
				return;
			}
			if(log.getOperateType() == UserContant.OPERATE_USER_GO_LIVE_LOG){
				IUserApiLogService.insertUserClientLog(log);
			}else{
				IUserApiLogService.insertUserClientOffLine(log);
			}
			long endTime = new Date().getTime();
			logger.info("===========处理消息=============："+(endTime - startTime));
		} catch (Exception e) {
			logger.error("处理日志消息异常  e:{}",e);
		}
		
	}
}
