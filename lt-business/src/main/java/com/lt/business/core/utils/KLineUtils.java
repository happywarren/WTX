/*package com.lt.business.core.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.lt.business.core.server.QuotaInitializeServer;
import com.lt.business.core.vo.KLineBean;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;

public class KLineUtils {
	static Logger logger = LoggerFactory.getLogger(TimeSharingplanUtils.class);

	*//**
	 * 本地内存 Map<商品,Map<时间,K线图对象>>
	 *//*
	public static Map<String, Map<String, KLineBean>> kLineMap = new ConcurrentHashMap<String, Map<String, KLineBean>>();

	*//**
	 * 所有行情 Map<品种,Map<时间(yyyy-MM-dd HH:mm:00),行情>>
	 *//*
	public static Map<String, Map<String, List<RedisQuotaObject>>> allMap = new ConcurrentHashMap<String, Map<String, List<RedisQuotaObject>>>();

	
	public static Map<String,Set<String>> setMap = new ConcurrentHashMap<String, Set<String>>();
	
	public static Map<String,Map<Integer,String>> minuteMap = new ConcurrentHashMap<String, Map<Integer,String>>();
	
	
	*//**
	 * 创建K线图
	 * 
	 * @param product
	 * @param obj
	 * @param data
	 * @throws Exception
	 *//*
	public static void createKLine(final String product, RedisQuotaObject obj) {
		try {
			// -----------------------------k线图生成逻辑start-----------------
			// 时间戳格式化
			String time = QuotaTimeConfigUtils.Dateformat(obj.getTimeStamp());
			Date date = DateTools.toDefaultDateTime(time);
			String setDate = DateTools.parseToDefaultDateTimeString(date);
			long i = date.getTime() % (60 * 1000);
			// 00秒时
			if (i == 0) {
				createKLine(date, product, obj);
			}

			// 5分钟K线
			i = date.getTime() % (5 * 60 * 1000);
			if (i == 0) {
				if(isFlag(setDate, product,5)){
					changeBoolean(setDate, product,5);
					createKLineBean(product, 5, date);
				}
				
			}

			// 15分钟K线
			i = date.getTime() % (15 * 60 * 1000);
			if (i == 0) {
				if(isFlag(setDate, product,15)){
					changeBoolean(setDate, product,15);
					createKLineBean(product, 15, date);
				}
			}
			// 30分钟K线
			i = date.getTime() % (30 * 60 * 1000);
			if (i == 0) {
				if(isFlag(setDate, product,30)){
					changeBoolean(setDate, product,30);
					createKLineBean(product, 30, date);
				}
			}
			// 60分钟K线
			i = date.getTime() % (60 * 60 * 1000);
			if (i == 0) {
				if(isFlag(setDate, product,60)){
					changeBoolean(setDate, product,60);
					createKLineBean(product, 60, date);
				}
			}
			// 1天的K线
			KLineByDay(product, date);
			// ---------------------------- -K线图图生成逻辑end-----------------
		} catch (Exception e) {
			logger.error("创建K线图异常", e);
		}

	}
	*//**
	 * 是否可以写K线
	 * @param timeStamp
	 * @param product
	 * @param minute
	 * @return
	 *//*
	public static boolean isFlag(String timeStamp,String product,Integer minute){
		Map<Integer,String> map = minuteMap.get(product);
		if(map != null){
			String time = map.get(minute);
			if (time != null && time.equals(timeStamp)) {
				return false;
			}else{
				return true;
			}
		}else{
			map = new ConcurrentHashMap<Integer, String>();
			minuteMap.put(product, map);
			return true;
		}
	}
	*//**
	 * 设置写K线的判断标识
	 * @param timeStamp
	 * @param product
	 * @param minute
	 *//*
	public static void changeBoolean(String timeStamp,String product,Integer minute){
		Map<Integer,String> map = minuteMap.get(product);
		map.put(minute,timeStamp);
		minuteMap.put(product, map);
	}
	

	*//**
	 * 算法
	 * 
	 * @param date
	 * @param product
	 * @param obj
	 *//*
	private static void createKLine(Date date, final String product,
			RedisQuotaObject obj) {
		String t = DateTools.parseToDefaultDateTimeString(date);
		String lastTime = DateTools.findLastDate(date, 1);
		Map<String, KLineBean> tmap = kLineMap.get(product);
		KLineBean bean = null;
		if (tmap != null) {
			bean = tmap.get(t);
		} else {
			tmap = new ConcurrentHashMap<String, KLineBean>();
		}
		// 缓存中不存在这一分钟的数据 生成一条K线图
		if (null == bean) {
			Map<String, List<RedisQuotaObject>> map = allMap.get(product);
			List<RedisQuotaObject> list = map.get(lastTime);
			bean = createKLineBean(list);
			if (bean != null) {
				bean.setProductName(product);
				bean.setTimeStamp(t);
				// 文件
				final String data = JSONObject.toJSONString(bean);
				QuotaUtils.executor.execute(new Runnable() {

					@Override
					public void run() {
						try {
							QuotaUtils.wirteFileK(data, "1", product);
						} catch (Exception e) {
							logger.error(".k日志文件写入异常", e);
							e.printStackTrace();
						}

					}
				});
				bean.setVolume(obj.getTotalQty());
				// 内存
				tmap.put(t, bean);
				kLineMap.put(product, tmap);
				// K线图已处理完成，把处理完的分钟内的所有数据清除
				map.remove(lastTime);
				allMap.put(product, map);

			}
		}
	}

	*//**
	 * 1分钟K生成逻辑
	 * 
	 * @param obj
	 * @param timeStamp
	 * @return
	 *//*
	private static KLineBean createKLineBean(List<RedisQuotaObject> list) {
		try {
			KLineBean info = new KLineBean();
			if (list != null && list.size() > 0) {
				info.setOpenPrice(list.get(0).getLastPrice());
				info.setClosePrice(list.get(list.size() - 1).getLastPrice());
				Integer startVolume = list.get(0).getTotalQty();
				Integer endVolume = list.get(list.size() - 1).getTotalQty();
				Integer volume = endVolume - startVolume;
				info.setVolume(volume);
				double maxPrice = 0d, minPrice = 100000d;
				for (RedisQuotaObject redisQuotaObject : list) {
					if (redisQuotaObject != null) {
						double highPrice = Double.parseDouble(redisQuotaObject
								.getLastPrice());
						double lowPrice = Double.parseDouble(redisQuotaObject
								.getLastPrice());
						if (highPrice > maxPrice) {
							maxPrice = highPrice;
						}
						if (lowPrice < minPrice) {
							minPrice = lowPrice;
						}
					}
				}
				info.setHighPrice(DoubleTools.ceil(maxPrice, 2).toString());
				info.setLowPrice(DoubleTools.ceil(minPrice, 2).toString());
				return info;
			}
		} catch (Exception e) {
			logger.error("1分钟K生成逻辑异常", e);
			return null;
		}
		return null;
	}

	*//**
	 * 保存每一条行情数据
	 * 
	 * @param product
	 * @param obj
	 *//*
	public static void saveAllQuotaData(String product, RedisQuotaObject obj) {
		try {
			Map<String, List<RedisQuotaObject>> everyHashOps = allMap
					.get(product);
			String time = QuotaTimeConfigUtils.DateformatForTimeSharingplan(obj
					.getTimeStamp());
			if (null == everyHashOps) {
				everyHashOps = new ConcurrentHashMap<String, List<RedisQuotaObject>>();
				allMap.put(product, everyHashOps);
			}
			List<RedisQuotaObject> list = everyHashOps.get(time);
			if (null == list) {
				list = new ArrayList<RedisQuotaObject>();
			}
			list.add(obj);
			everyHashOps.put(time, list);
			allMap.put(product, everyHashOps);
		} catch (Exception e) {
			logger.error("保存每一条行情数据异常", e);
		}
	}

	*//**
	 * 创建 n 分钟K线图
	 * 
	 * @param product
	 * @param minute
	 *//*
	public static void createKLineBean(final String product, final int minute,
			Date date) {
		if (minute <= 1) {
			return;
		}
		try {
			Map<String, KLineBean> kmap = kLineMap.get(product);
			if (kmap == null) {
				return;
			}
			List<KLineBean> list = new ArrayList<KLineBean>();
			for (int i = 1; i <= minute; i++) {
				String lastTime = DateTools.findLastDate(date, i);
				KLineBean kl = kmap.get(lastTime);
				if (null != kl) {
					list.add(kl);
				}
			}
			if (list.size() <= 0) {
				return;
			}
			// 文件
			String data = JSONObject.toJSONString(createKLineCalculate(
					list, date));
			try {
				String param = minute <= 60 ? (minute + "") : "day";
				QuotaUtils.wirteFileK(data, param, product);
			} catch (Exception e) {
				logger.error(".k日志文件写入异常", e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			logger.error("创建 " + minute + "分钟K线图异常", e);
		}

	}

	*//**
	 * K线图算法
	 * 
	 * @param list
	 * @param timeStamp
	 * @return
	 *//*
	private static KLineBean createKLineCalculate(List<KLineBean> list,
			Date date) {
		if (list == null || list.size() <= 0) {
			return null;
		}
		// 计算list中最高价格 最低价 开盘价 收盘价
		KLineBean info = new KLineBean();
		info.setOpenPrice(list.get(0).getOpenPrice());
		info.setClosePrice(list.get(list.size() - 1).getClosePrice());
		Integer um = list.get(list.size() - 1).getVolume()
				- list.get(0).getVolume();
		info.setVolume(Math.abs(um));
		info.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
		double maxPrice = 0d, minPrice = 100000d;
		for (KLineBean kLineByMinute : list) {
			double highPrice = Double.parseDouble(kLineByMinute.getHighPrice());
			double lowPrice = Double.parseDouble(kLineByMinute.getLowPrice());
			if (highPrice > maxPrice) {
				maxPrice = highPrice;
			}
			if (lowPrice < minPrice) {
				minPrice = lowPrice;
			}
		}
		info.setHighPrice(DoubleTools.ceil(maxPrice, 2).toString());
		info.setLowPrice(DoubleTools.ceil(minPrice, 2).toString());
		return info;

	}

	*//**
	 * 日K方法
	 * 
	 * @param product
	 * @param date
	 *//*
	private static void KLineByDay(String product, Date date) {

		// 如果当前时间是商品的结束时间
		Map<String, String> timeMap = QuotaInitializeServer.map.get(product);
		if (null == timeMap) {
			return;
		}
		for (Entry<String, String> entry : timeMap.entrySet()) {
			try {
				String startDate = entry.getKey(); // 字符串类型的开市时间
				String endDate = entry.getValue(); // 字符串类型的闭市时间
				Date startTime = DateTools.toDefaultTime(startDate);
				Date endTime = DateTools.toDefaultTime(endDate);
				if ((startTime.getTime() - endTime.getTime()) < 0) {
					endTime = DateTools.add(endTime, 1);
				}
				if (date.equals(endTime)) {
					logger.info(date.toString());
					logger.info(endTime.toString());
					int minute = Math.abs(DateTools.minutesBetween(startTime,
							endTime));
					createKLineBean(product, minute, date);
				}
			} catch (Exception e) {
				logger.error("日K生成方法异常", e);
				continue;
			}
		}

		// 查询该商品的开始时间
		// 计算二者之间相差的分钟数 i
		// 创建一个新的List对象 用来存放分钟K
		// 当前分钟往前推，直到满足当天的分钟数
		// 根据往前推的时间查询K线数据是否存在
		// 存在加入list对象
		// 全部加入后获取第一条数据最后一条数据，取出开盘价格，收盘价格 买卖总量=第一条数据的总量
		// 计算当前list中最高的价格 最低的价格
		// 保存日K
	}
}
*/