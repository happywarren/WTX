package com.lt.business.core.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.business.core.server.QuotaInitializeServer;
import com.lt.util.utils.DateTools;

/**
 * 当数据库中商品信息查不到的情况 使用该类下的参数 
 * @author guodw
 *
 */
public class QuotaTimeConfigUtils {
	
	static Logger logger = LoggerFactory.getLogger(QuotaTimeConfigUtils.class);

	public static String[] list = "SR701 pp1701 au1706 ag1706 CL1701 ru1705 cu1702 ni1705 GC1702 rb1705 HSI1612 MHI1612"
			.split(" ");
	static {

	}

	public static Map<String, String> init(String product) {
		product = product.toUpperCase();
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		//美黄金 美原油
		if (product.contains("CL") || product.contains("GC")) {
			map.put("06:59:55", "23:59:59");
			map.put("00:00:00", "06:00:05");
			return map;
		}else if(product.contains("HSI") || product.contains("MHI")){
			map.put("09:14:55", "12:00:05");
			map.put("12:59:55", "16:30:05");
			map.put("17:14:55", "23:45:05");
			return map;
		}else{
			map.put("08:59:55", "10:15:05");
			map.put("10:29:55", "11:30:05");
			map.put("13:29:55", "15:00:05");
		}
		//黄金白银
		if(product.contains("AU") || product.contains("AG")){
			map.put("20:59:55", "23:59:59");
			map.put("00:00:00", "02:30:05");
			return map;
		}
		//镍  铜
		if(product.contains("NI") || product.contains("CU")){
			map.put("20:59:55", "23:59:59");
			map.put("00:00:00", "01:00:05");
			return map;
		}
		//白糖
		if(product.contains("SR") ){
			map.put("20:59:55", "23:30:05");
			return map;
		}
		
		//天然橡胶   螺纹钢
		if(product.contains("RU")|| product.contains("RB")){
			map.put("20:59:55", "23:00:05");
			return map;
		}
		return map;
		
	}
	public static Map<String, String> initLocalMap(String product) {
		product = product.toUpperCase();
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		//美黄金 美原油
		if (product.contains("CL") || product.contains("GC")) {
			map.put("07:00:00", "06:00:00");
			return map;
		}else if(product.contains("HSI") || product.contains("MHI")){
			map.put("17:15:00", "16:30:00");
			return map;
		}else if(product.contains("PP") ){
			map.put("09:00:00", "15:00:00");
			return map;
		}else{
			map.put("21:00:00", "15:00:00");
			return map;
		}
		
	}
	

	/**
	 * 格式化内盘行情时间
	 * @param timeStamp
	 * @return
	 */
	public static String Dateformat(String timeStamp){
		String time = timeStamp;
		try {
			if (!time.contains("-")) {
				// 20161130 16:23:00.0 ==》》2016-11-30 16:23:00.0
				StringBuffer stringBuffer = new StringBuffer(time);
				stringBuffer.insert(4, "-").insert(7, "-");
				time = stringBuffer.toString();
			}
			return time;
		} catch (Exception e) {
			logger.info(timeStamp);
			logger.error("Dateformat",e);
			return timeStamp;
		}
	}
	
	/**
	 * 分时图行情格式化  
	 * @param timeStamp
	 * @return
	 */
	public static String DateformatForTimeSharingplan(String timeStamp){
		try {
			String time = timeStamp;
			if(time.contains(".")){
				time = time.split("\\.")[0];
				time = time.substring(0,time.length()-2)+"00";
			}
			if (!time.contains("-")) {
				// 20161130 16:23:00.0 ==》》2016-11-30 16:23:00.0
				StringBuffer stringBuffer = new StringBuffer(time);
				stringBuffer.insert(4, "-").insert(7, "-");
				time = stringBuffer.toString();
			}
			return time;
		} catch (Exception e) {
			logger.info(timeStamp);
			logger.error("DateformatForTimeSharingplan",e);
			return timeStamp;
		}
		
		
	}
	

	/**
	 * 获取当前品种是否在开市时间
	 * 数据库直接设置 开市时间提早5秒，闭市时间延后5秒，控制行情（避免非交易所时间段的脏数据）
	 * @return
	 */
	public static boolean startFlag(String product)  {

		Map<String, String> map = QuotaInitializeServer.productMap.get(product);
		if (null == map) {
			return false;
		}
		for (Entry<String, String> entry : map.entrySet()) {
			try {
				Date startDate = DateTools.toDefaultTime(entry.getKey());
				Date endDate = DateTools.toDefaultTime(entry.getValue());
				if(startDate.getTime()>endDate.getTime()){
					endDate = DateTools.add(endDate, 1);
				}
				if(DateTools.isBetween(DateTools.toDefaultTime(DateTools.parseToDefaultTimeString(new Date())),
						startDate,
						endDate)){
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("获取当前品种是否在开市时间异常",e);
				return false;
			}
		}
		return false;
	}
}
