package com.lt.trade.order.executor;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.sms.ISmsApiService;
import com.lt.trade.order.executor.bean.TradeTimeOutVo;
import com.lt.trade.order.service.score.IScoreOrderPersistService;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.utils.LTConstants;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单超时处理 用户下单时加入待超时队列 
 * 单子成功或者失败时移除待超时队列 
 * 当单子2秒内没有响应执行强制失败处理 
 * 若强制失败后C++返回了订单成功信息
 * 发送短信给财务通知人工卖出订单
 * 
 * @author guodw
 *
 */
@Component
public class TradeTimeOut implements InitializingBean{

	static Logger logger = LoggerFactory.getLogger(TradeTimeOut.class);

	
	@Autowired
	private IScoreOrderPersistService scoreOrderOuterPlatePersistServiceImpl;
	
	@Autowired
	private ISmsApiService smsApiService;
	/**
	 * 订单:委托号对应关系
	 */
	private static Map<String,TradeTimeOutVo> entrustMap = new ConcurrentHashMap<String, TradeTimeOutVo>();
	/**
	 * 待超时队列
	 */
	private static Map<String, Long> timeOutMap = new ConcurrentHashMap<String, Long>();
	/**
	 * 预警队列
	 */
	private static Map<String, Long> earlyWarnMap = new ConcurrentHashMap<String, Long>();
	/**
	 * 预警订单:委托号对应关系
	 */
	private static Map<String,String> earlyWarnEntrustMap = new ConcurrentHashMap<String, String>();



	/**
	 * time:存放执行超时打回逻辑的时间判断条件   单位 long
	 */
	public  static Map<String,Long> map = new ConcurrentHashMap<>();

	/**
	 * 加入超时、预警队列
	 * @param orderId
	 * @param createTime
	 */
	public static void put(String orderId, long createTime) {
		timeOutMap.put(orderId, createTime);
		earlyWarnMap.put(orderId, createTime);
	}

	/**
	 * 移除超时队列
	 * @param orderId
	 */
	public static void remove(String orderId) {
		timeOutMap.remove(orderId);
		entrustMap.remove(orderId);
	}
	
	/**
	 * 加入超时委托关系
	 * @param orderId
	 * @param entrustId
	 */
	public static void entrustMapPut(String orderId, String entrustId,Integer type) {
		TradeTimeOutVo vo = new TradeTimeOutVo();
		vo.setPlatformId(Integer.valueOf(entrustId));
		vo.setType(type);
		entrustMap.put(orderId, vo);
	}
	/**
	 * 加入预警委托关系
	 * @param orderId
	 * @param entrustId
	 */
	public static void earlyWarnEntrustMapPut(String orderId, String entrustId) {
		earlyWarnEntrustMap.put(orderId, entrustId);
	}
	
	/**
	 * 移除预警队列
	 * @param orderId
	 */
	public static void earlyWarnEntrustMapRemove(String orderId) {
		earlyWarnEntrustMap.remove(orderId);
		earlyWarnMap.remove(orderId);
	}
	/**
	 * 预警详细处理
	 */
	private void warnHanding() {
		Iterator<Entry<String, Long>> entries = earlyWarnMap.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Entry<String, Long> entry = entries.next();
			String orderId = entry.getKey();
			Long createTime = entry.getValue();
			Long nowTime = System.currentTimeMillis();
			long time = nowTime - createTime;
			if (time >= 60000) {
				if(!orderId.contains("SOR")){
					// 执行强制失败处理
					logger.error("--------------执行预警短信发送{}",orderId);
					logger.error("--------------createTime{}",createTime);
					logger.error("--------------执行预警短信发送 时间:{}",DateTools.parseToTimeStamp2(new Date()));
					logger.info("--------------执行超时订单earlyWarnMap:{}",JSONObject.toJSON(earlyWarnMap));
					try {
						TradeUtils.sendOrderIdErrorSMS(smsApiService, orderId, null,"2005",null);
					} catch (Exception e) {
						logger.error(e.getMessage());
						e.printStackTrace();			
					}finally{
						earlyWarnEntrustMapRemove(orderId);
					}
					logger.error("--------------执行预警短信发送{}完成",orderId);
				}
			}
		}
	}
	private void handing() {
		Iterator<Entry<String, Long>> entries = timeOutMap.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Entry<String, Long> entry = entries.next();
			String orderId = entry.getKey();
			Long createTime = entry.getValue();
			Long nowTime = System.currentTimeMillis();
			long time = nowTime - createTime;
			long datetime =10*1000L;
			if (StringTools.isNotEmpty(map)&&StringTools.isNotEmpty(map.get("time"))){
				datetime = map.get("time");

			}
			if (time >= datetime) {
				// 执行强制失败处理
				if(!orderId.contains("SOR")){
					logger.error("--------------开始处理超时订单{}",orderId);
					logger.error("--------------datetime{}",datetime);
					logger.error("--------------执行强制失败处理 时间:{}",DateTools.parseToTimeStamp2(new Date()));
					persist(orderId);
					logger.error("--------------结束处理超时订单{}完成",orderId);
				}
			}

		}
	}
	/**
	 * 现金组装
	 * @param orderId
	 * @return {"errorId":0,"errorMsg":"订单超时，报单失败","fundType":0,"platformId":396583697}
	 */
	private FutureErrorBean setErrorBean(String orderId){
		FutureErrorBean errorBean = new FutureErrorBean();
		errorBean.setErrorId(5890);
		errorBean.setErrorMsg("订单超时，报单失败");
		if(orderId.contains("SOR")){
			errorBean.setFundType(LTConstants.FUND_TYPE_SCORE);
		}
		//注意：这里委托订单号C++是int类型的 Java是String类型。以后这个实体errorBean需要改变
		errorBean.setPlatformId(entrustMap.get(orderId).getPlatformId());
		return errorBean;
	}
	/**
	 * 持久化数据
	 */
	private void persist(String orderId){
		try {
			FutureErrorBean errorBean = setErrorBean(orderId);
			if(!StringTools.isNotEmpty(errorBean)){
				logger.info("------------处理超时回执信息errorBean={}----------------",JSONObject.toJSONString(errorBean));
				return;
			}
			scoreOrderOuterPlatePersistServiceImpl.doPersist2002(errorBean);

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();			
		}finally{
			remove(orderId);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						handing();
						warnHanding();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		}).start();
	}
	public static void main(String[] args) {
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		map.remove("string");
	}

}
