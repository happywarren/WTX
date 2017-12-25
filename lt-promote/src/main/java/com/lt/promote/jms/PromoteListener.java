package com.lt.promote.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.util.utils.jms.Listener;
import com.lt.vo.promote.BrancherVo;
import com.lt.promote.service.IPromoteService;

/**
 * 推广员监听器
 * @author jingwb
 *
 */
@Service
public class PromoteListener implements Listener{

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IPromoteService promoteServiceImpl;
	
	@Override
	public boolean onMessage(Message message) {
		TextMessage text = (TextMessage)message;
		String data = null;
		try {
			data = text.getText();
			logger.info("PromoteListener data={}",data);
			BrancherVo push = JSONObject.parseObject(data, BrancherVo.class);	
			promoteServiceImpl.dealData(push);
			return true;
		} catch (JMSException e) {
			logger.info("push接收消息失败，e={}",e);
		}
		return false;
	}

}
