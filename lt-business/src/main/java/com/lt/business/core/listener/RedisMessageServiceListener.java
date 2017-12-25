package com.lt.business.core.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.business.core.service.product.IProductTaskService;
import com.lt.business.core.utils.TimeSharingplanUtils;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.StringTools;
//import com.lt.business.core.utils.KLineUtils;

/**
 * redis监听消息处理
 * @author guodw
 *
 */
@Service
public class RedisMessageServiceListener {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IProductTaskService productTaskService;
	
	@Autowired
	private IPersistExecutor persistExecutor;
	
	
	/**
	 * redis消息接收
	 * @param message
	 */
	public void handleMessage(String message) {
		logger.debug(message);
		if(StringTools.isEmpty(message)){
			return ;
		}
		try {
			RedisQuotaObject obj = TimeSharingplanUtils.toJsonObject(message);
			if(null == obj){
				return;
			}
			persistExecutor.put(obj);
			
		} catch (Exception e) {
			logger.error("RedisMessageServiceListener ----->>handleMessage--->>error-->>",e);
		}finally{
			
		}
		
	}
	
	
	public static void main(String[] args) {
		try {
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		     Date date = format.parse("2016-11-30 16:23:00.01");
		     System.out.println(date.getTime());
			long i = date.getTime()%60000;
			System.out.println(i);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}