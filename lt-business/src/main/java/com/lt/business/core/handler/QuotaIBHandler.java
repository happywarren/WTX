package com.lt.business.core.handler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lt.business.core.server.ProductTimeCache;
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
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 行情服务
 * <p>
 */
public class QuotaIBHandler implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(QuotaHandler.class);

	private BaseClient quoteClient_;
	private Thread quoteClientThread_;
	private ProductTimeCache productTimeCache;

	public QuotaIBHandler(String quoteHost, int quotePort,
			OnStartupListener startupListener,ProductTimeCache productTimeCache) {
		this.productTimeCache = productTimeCache;
		quoteClient_ = new BaseClient(quoteHost, quotePort,
				quoteMessageListener_, startupListener);
		quoteClientThread_ = new Thread(quoteClient_);
		quoteClientThread_.start();
	}

	// 接收行情数据
	private OnMessageListener quoteMessageListener_ = new OnMessageListener() {
		@Override
		public void onMessage(final String msg) {
			try {
				JSONObject jsonData = JSON.parseObject(msg);
				String DATA = jsonData.getString("data");
				
				RedisQuotaObject obj = IbQuotaDataChange.getInstance().change(DATA);
				
				double askPrice = Double.valueOf(obj.getAskPrice1());
				double bidPrice = Double.valueOf(obj.getBidPrice1());
				double lastPrice = Double.valueOf(obj.getLastPrice());
				if (askPrice <= 0 || bidPrice <= 0 || lastPrice <= 0) {
					LOGGER.info(" 收到行情数据时 卖一价 买一价 当前价 有一个为null 或者为 0  msg = {} ", msg);
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
			} catch (Exception e) {
				LOGGER.error("数据解析异常msg={}",msg);
				LOGGER.error("数据解析异常",e);
			}
			
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
	private void createKLine(RedisQuotaObject obj){
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
