package com.lt.business.core.task;

import java.io.FileWriter;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.lt.business.core.vo.TimeSharingplanBean;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.prop.CustomerPropertiesConfig;

public class TimeSharingplanTask implements InitializingBean {
	
	Logger logger = LoggerFactory.getLogger(TimeSharingplanTask.class);
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		int time = DateTools.getSecond(new Date()) % 60;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				fstServiceByMinutes("1");
			}
		}, (60 - time) * 1000, 60 * 1000);
		int seconds = 5 - DateTools.getMinute(new Date()) % 5;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				fstServiceByMinutes("5");
			}
		}, (seconds * 60 - time) * 1000, 5 * 60 * 1000);
		seconds = 15 - DateTools.getMinute(new Date()) % 15;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				fstServiceByMinutes("15");
			}
		}, (seconds * 60 - time) * 1000, 15 * 60 * 1000);
		seconds = 30 - DateTools.getMinute(new Date()) % 30;

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				fstServiceByMinutes("30");
			}
		}, (seconds * 60 - time) * 1000, 30 * 60 * 1000);
		seconds = 60 - DateTools.getMinute(new Date()) % 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				fstServiceByMinutes("60");
			}
		}, (seconds * 60 - time) * 1000, 60 * 60 * 1000);
	}

	public void fstServiceByMinutes(String minute) {
		/*	// 画分时图
		String time = DateTools.parseToTimeStamp(new Date());
		// 是否是周末
		try {
			// 获取行情最新数据
			for (Map.Entry entry : NettyChannelMap.mapCache.entrySet()) {
				String futuresType = (String) entry.getKey();
				FuturesQuotaDataInfo info = NettyChannelMap.mapCache
						.get(futuresType);
				// 如果是不是模拟盘，且是周末跳过
				if (!futuresType.contains("MN")) {
					if (DateTools.getWeekNumber(new Date()) == 0
							|| (new Date().getTime() > MarketUtils.getDates(6,
									0, 0) && DateTools
									.getWeekNumber(new Date()) == 6)) {
						// 是周末跳过 .周六6点半前是买卖时间
						continue;
					}
				}
				// 获取当前品种是否在开市时间
				boolean bool = MarketUtils.getStateOfMarket(futuresType);
				if (bool) {
					// 查询是否有相同数据

						if (info.getLastPrice() > 1) {
							// 写入文件
							String wirteQuota = info.getInstrumentID() + ","
									+ info.getLastPrice() + ","
									+ time.substring(0, time.length() - 5)
									+ "00" + "|";
							String path = propCfg.getProperty("path")
									+ info.getInstrumentID()
											.replaceAll("\\d+", "")
											.toUpperCase()
									+ DateTools.parseToYMDTimeStamp(new Date())
									+ "_" + minute + ".fst";
							if (StringTools.isNumeric(info.getInstrumentID())) {
								path = propCfg.getProperty("path")
										+ info.getInstrumentID()
										+ DateTools
												.parseToYMDTimeStamp(new Date())
										+ "_" + minute + ".fst";
								;
							}
							if (info.getInstrumentID().endsWith("MNL")) {
								path = propCfg.getProperty("path")
										+ info.getInstrumentID()
												.substring(
														0,
														info.getInstrumentID()
																.length() - 1)
										+ DateTools
												.parseToYMDTimeStamp(new Date())
										+ "_" + minute + ".fst";
							}
							FileWriter fw = new FileWriter(path, true);
							fw.write(wirteQuota);
							fw.close();
						}
				}
			}
		} catch (Exception e) {
			logger.error("分时图定时器异常", e);
		}*/
	}

}
