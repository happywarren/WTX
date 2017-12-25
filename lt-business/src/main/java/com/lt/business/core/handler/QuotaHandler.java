package com.lt.business.core.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lt.business.core.server.ProductTimeCache;
import com.lt.business.core.server.QuotaInitializeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.business.core.netty.BaseClient;
import com.lt.business.core.netty.OnMessageListener;
import com.lt.business.core.netty.OnStartupListener;
import com.lt.business.core.utils.KLine;
import com.lt.business.core.utils.QuotaTimeConfigUtils;
import com.lt.business.core.utils.QuotaUtils;
import com.lt.business.core.utils.TimeSharingplanUtils;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.StringTools;

/**
 * 行情服务
 * <p>
 */
public class QuotaHandler implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(QuotaHandler.class);

	private BaseClient quoteClient_;
	private Thread quoteClientThread_;
	private ProductTimeCache productTimeCache;
//	private Map<String, Boolean> isExchangeTradingTimeMap = new ConcurrentHashMap<String, Boolean>();// 存取每个品种交易状态，true
																										// 不可交易
	public static Map<String, String> quotePriceMap_ = new ConcurrentHashMap<String, String>();
//	private BlockingQueue<RedisQuotaObject> productPriceQueue_ = new LinkedBlockingDeque<RedisQuotaObject>();

	public QuotaHandler(String quoteHost, int quotePort,
			OnStartupListener startupListener,ProductTimeCache productTimeCache) {
		this.productTimeCache = productTimeCache;
		quoteClient_ = new BaseClient(quoteHost, quotePort,
				quoteMessageListener_, startupListener);
		quoteClientThread_ = new Thread(quoteClient_);
		quoteClientThread_.start();
	}

	// 设置交易时间
//	public void setExchangeTradingTime(Map<String, Boolean> tradingTime) {
//		isExchangeTradingTimeMap = tradingTime;
//	}

	// 获取行情数据
	public static String getQuotePrice(String productName) {
		return quotePriceMap_.get(productName);
	}
	// 添加数据
	public static void putQuotePrice(String productName,String msg) {
		quotePriceMap_.put(productName,msg);
	}
	// 接收行情数据
	private OnMessageListener quoteMessageListener_ = new OnMessageListener() {
		@Override
		public void onMessage(final String msg) {
			JSONObject jsonData = JSON.parseObject(msg);
			String DATA = jsonData.getString("DATA");
			jsonData = JSON.parseObject(DATA);
			RedisQuotaObject obj = JSONObject.toJavaObject(jsonData,
					RedisQuotaObject.class);
			double askPrice = Double.valueOf(obj.getAskPrice1());
			double bidPrice = Double.valueOf(obj.getBidPrice1());
			double lastPrice = Double.valueOf(obj.getLastPrice());
			if (askPrice <= 0 || bidPrice <= 0 || lastPrice <= 0) {
				LOGGER.info(" 收到行情数据时 卖一价 买一价 当前价 有一个为null 或者为 0  msg = {} ",
						msg);
				return;
			}
			final String product = obj.getProductName();
			//获取行情商品名称
			if(!StringTools.isNotEmpty(product) ){
				return;
			}
			//获取当前品种是否在开市时间
			if(!productTimeCache.getIsExchangeTradingTime(product)){
				LOGGER.error("商品{}不在交易时间段内 obj = {}",product,JSONObject.toJSONString(obj));
				return;
			}
			String data = JSONObject.toJSONString(obj);
			//内存保存最新的行情数据
			TimeSharingplanUtils.map.put(product, obj);
			//内存保存每一条数据
//			KLineUtils.saveAllQuotaData(product, obj);
			
			//写入.log行情日志
			try {
				QuotaUtils.wirteFileLog(data, obj.getProductName());
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("行情写入log日志异常  msg:{}",data);
			}
			
			if(StringTools.isNotEmpty(obj.getTimeStamp())){
				//保存最新的行情数据
				TimeSharingplanUtils.saveNewQuotaDataForRedis(product, obj);
				//生成分时图日志 及 保存到缓存中
				TimeSharingplanUtils.createTimeSharingplan(product, obj, data);
			
			}
			createKLine(obj);
		}
	};

	@Override
	public void run() {
		// fixme: 循环显式退出?
	}
	/**
	 * 处理K线数据
	 * @param obj
	 */
	public static void createKLine(RedisQuotaObject obj){
		//1分钟
//		KLine.addKLineBean(obj);
		KLine.addKLineBean(obj,1);
		KLine.addKLineBean(obj, 5);
		KLine.addKLineBean(obj, 15);
		KLine.addKLineBean(obj, 30);
		KLine.addKLineBean(obj, 60);
		KLine.addKLineBean(obj, 1440);
		KLine.addKLineBean(obj, 1440*5);
		KLine.addKLineBean(obj, 1440*22);
	}
	
}
