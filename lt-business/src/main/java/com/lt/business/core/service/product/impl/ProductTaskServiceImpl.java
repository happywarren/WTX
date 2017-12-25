package com.lt.business.core.service.product.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.lt.enums.trade.PlateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.api.dispatcher.IDispatcherApiService;
import com.lt.business.core.dao.product.IProductDao;
import com.lt.business.core.dao.product.IProductTimeConfigDao;
import com.lt.business.core.service.product.IProductTaskService;
import com.lt.business.core.service.sys.ISysThreadLockService;
import com.lt.business.core.utils.PriceLimitUtils;
import com.lt.enums.product.ProductMarketEnum;
import com.lt.enums.sys.SysThreadLockEnum;
import com.lt.model.dispatcher.enums.DispatherEnum;
import com.lt.model.product.Product;
import com.lt.model.product.ProductTimeConfig;
import com.lt.model.trade.JmsTopicOrderMsg;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.constant.redis.RedisUtil;
import com.lt.vo.product.ProductVo;
import com.lt.vo.product.TimeConfigVO;
import com.lt.vo.product.TimeConfigVO.SysSaleTime;
import com.lt.vo.product.TimeConfigVO.TradeAndQuotaTime;

@Service
public class ProductTaskServiceImpl implements IProductTaskService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private IProductDao productDao;
	@Autowired
	private IProductTimeConfigDao productTimeConfigDao;
	@Autowired
	private IDispatcherApiService dispatcherApiService;
	@Autowired
	private ISysThreadLockService sysThreadLockService;
	@Resource(name = "sendOrderMsgProducer")
	private MessageQueueProducer sendOrderMsgProducer;

	//标签 ： 是否发送刷新信息
	boolean flag = false;
	/**
	 * 商品 ：市场状态
	 */
	Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();

	@Override
	public void changeProductStatus(String product, String changeRate) {
		try {
			//当商品状态已被系统维护更改，涨跌幅维护则不可更改商品市场状态
			if (PriceLimitUtils.isOperateMap.get(product) != 1) {
				//logger.debug("没有权限处理根据涨停百分比刷新市场状态");
				return;
			}
			Product provo= productDao.selectProductInfo(product);
			if(null == provo){
				return;
			}
			int i = provo.getMarketStatus() == null ? 0 : provo.getMarketStatus();
			double rate = DoubleUtils.ceil(Double.valueOf(changeRate), 2);
			// 系统涨跌幅
			Double productChangeRate = PriceLimitUtils.changeRateMap.get(product);
			double productRate = DoubleUtils.ceil(productChangeRate, 2);
			// 绝对值 大于 涨跌幅比例 更新状态 可卖不可买
			if (Math.abs(rate) > productRate) {
				//开市期间，读取行情价格，当行情价格达到开仓限制价格则触发定时任务更改商品市场状态为不可买卖；
				if (i != ProductMarketEnum.ONLY_LIQUIDATION.getValue()) {
					logger.debug("updateProductStatus   ONLY_LIQUIDATION");
					productDao.updateProductStatus(
							ProductMarketEnum.ONLY_LIQUIDATION.getValue(), product);
					provo.setMarketStatus(ProductMarketEnum.ONLY_LIQUIDATION.getValue());
					//刷新redis
					redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO).put(product, provo);
				}
			}else{
				//开市期间，读取行情价格，当行情价格未达到涨跌幅开仓限制价格则触发定时任务更改商品市场状态为可买可卖；
				if (i == ProductMarketEnum.ONLY_LIQUIDATION.getValue()) {
					logger.debug("updateProductStatus   START_TRADING");
					provo.setMarketStatus(ProductMarketEnum.START_TRADING.getValue());
					productDao.updateProductStatus(
							ProductMarketEnum.START_TRADING.getValue(), product);
					//刷新redis
					redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO).put(product, provo);
				}
			}
		} catch (Exception e) {
			logger.error("涨跌幅开仓限制",e);
			return;
		}
		
	}

	
	@Override
	public void loadProTimeCfgToRedis() throws Exception{
		//当前时间
		Date date = new Date();
		logger.info("============定时任务执行--将商品时间配置加载到缓存中============");
		redisTemplate.delete(RedisUtil.PRODUCT_TIME_CONFIG);
		//建立今日商品时间配置信息
		BoundHashOperations<String, String, TimeConfigVO> proTimeCfgRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
		
		//获取缓存中所有商品信息
		BoundHashOperations<String, String, ProductVo> proRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
		Set<String> proCodes = proRedis.keys();//获取所有商品code
		logger.info("========商品信息={}=========",JSONObject.toJSONString(proCodes));
		String time = DateTools.parseToDefaultDateTimeString(date);
		//获取当前时间处于假日的商品code
		List<String> codesForHoliday = productDao.selectCodeByNow(time);
		Map<String,String> bm = new HashMap<String,String>();
		for(String code:codesForHoliday){
				bm.put(code, "1");
		}

		//获取现在是什么时区（冬令时/夏令时）
		Boolean isWinter=false;//冬令时
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		String cfg_value = sysCfgRedis.get("winter_summer_change");
		if(StringTools.isEmpty(cfg_value) || "1".equals(cfg_value)){//夏令时
			isWinter=false;
		}else{
			isWinter=true;
		}
		
		//遍历获取商品交易时间配置信息
		for(String proCode:proCodes){
			ProductVo pro = proRedis.get(proCode);
			Integer status = pro.getStatus();//上下架状态
			Integer plate = pro.getPlate();//0：内盘 1：外盘 2:差价合约
			if(1 != status && 2 != status){//非上架和预售状态的商品不加载商品的时间信息
				continue;
			}
			
			logger.info("--------执行商品={}--------",proCode);
			List<ProductTimeConfig> timeCfgs = null;
			if(isWinter){//获取冬令时配置
				timeCfgs = productTimeConfigDao.findProductTimeConfigWinterByProductId(proRedis.get(proCode).getId());
			}else{//取夏令时配置
				timeCfgs = productTimeConfigDao.findProductTimeConfigByProductId(proRedis.get(proCode).getId());
			}			

			//将系统清仓时间加载到缓存
			TimeConfigVO timeConfigVO = new TimeConfigVO();	
			logger.info("timeCfgs.size()={}",timeCfgs.size());
			for(int i = 0; i < timeCfgs.size(); i ++){
				ProductTimeConfig ptc = timeCfgs.get(i);
				Date d1 = DateTools.toDefaultTime(ptc.getSysSaleBeginTime());
				if(i != timeCfgs.size()-1){
					logger.info("i+1={}",i+1);
					Date d2 = DateTools.toDefaultTime(timeCfgs.get(i+1).getSysSaleBeginTime());
					if(DateTools.isEqual(d1, d2)){
						continue;
					}
				}
				timeConfigVO.addSysSaleTimeList(ptc.getSysSaleBeginTime(), 
												ptc.getSysSaleEndTime(), 
												ptc.getSysSaleTime());				
			}
			
			//判断该商品是否处于节假日（不包含双休日）
			if(bm.get(proCode) != null){//是
				//将每个商品的时间配置改为00:00:00格式，放入缓存中	
				timeConfigVO.addTradeTimeList("00:00:00", "00:00:00","00:00:00");
				timeConfigVO.addQuotaTimeList("00:00:00", "00:00:00",null);
			}else{//否
				//判断当前时间是否为双休日(排除差价合约，差价合约周末也交易)
				if(plate != PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue() && DateTools.isWeekend(date)){//是
					//将每个商品的时间配置改为00:00:00格式，放入缓存中	
					timeConfigVO.addTradeTimeList("00:00:00", "00:00:00","00:00:00");
					timeConfigVO.addQuotaTimeList("00:00:00", "00:00:00",null);
				}else{//否
					//处理商品时间配置信息到缓存	
					dealTimeCfgForHoliday(proCode, timeCfgs, timeConfigVO);
				}
			}
			proTimeCfgRedis.put(proCode, timeConfigVO);
			logger.info("proCode={},timeConfigVO={}",proCode,JSONObject.toJSONString(timeConfigVO));
		}
		
		logger.info("============定时任务执行--将商品时间配置加载到缓存中=====结束=======");
	}


	/**
	 * 处理商品时间配置信息到缓存
	 * @param proCode
	 * @param timeCfgs
	 * @throws ParseException
	 */
	private void dealTimeCfgForHoliday(String proCode,List<ProductTimeConfig> timeCfgs, 
												TimeConfigVO timeConfigVO)throws ParseException {
		
		for(ProductTimeConfig timeCfg:timeCfgs){						
			
			//如果开始时间大于结束时间（07:00:00>06:00:00 跨天）
			if(DateTools.compareTo(DateTools.toDefaultTime(timeCfg.getQuotaBeginTime()), 
												DateTools.toDefaultTime(timeCfg.getQuotaEndTime()))){

				timeConfigVO.addTradeTimeList(timeCfg.getTradeBeginTime(), "23:59:59",timeCfg.getSysSaleBeginTime());
				timeConfigVO.addTradeTimeList("00:00:00", timeCfg.getTradeEndTime(),timeCfg.getSysSaleBeginTime());
				timeConfigVO.addQuotaTimeList(timeCfg.getQuotaBeginTime(), "23:59:59",null);
				timeConfigVO.addQuotaTimeList("00:00:00", timeCfg.getQuotaEndTime(),null);
				logger.info("proCode={},timeConfigVO222222222={}",proCode,JSONObject.toJSONString(timeConfigVO));

			}else{			
				timeConfigVO.addTradeTimeList(timeCfg.getTradeBeginTime(), timeCfg.getTradeEndTime(),timeCfg.getSysSaleBeginTime());
				timeConfigVO.addQuotaTimeList(timeCfg.getQuotaBeginTime(), timeCfg.getQuotaEndTime(),null);
			}
		}
	}

	@Override
	public void refreshProMarketStatusForRedis() throws Exception {		
		try{
			
			Date date = new Date();		
			String now = DateTools.parseToDefaultTimeString(date);//当前时间12:00:00
			logger.info("========每一分钟刷新当前市场状态======执行，当前时间={}",now);
			
			//锁校验
			if(!sysThreadLockService.lock(SysThreadLockEnum.REFRESH_MARKET_STATUS.getCode())){
				return;
			}
			
			//从redis中读取商品信息
			BoundHashOperations<String, String, ProductVo> proRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
			//从redis中读取商品交易时间配置信息
			BoundHashOperations<String, String,TimeConfigVO> tradeTimeCfgRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
			Set<String> proCodes = proRedis.keys();//获取所有商品code
			for(String proCode:proCodes){
				//获取商品
				ProductVo product = proRedis.get(proCode);
				if("运营强制闭市".equals(product.getShutReason())){
					logger.info("========商品code={},运营强制闭市，无法触发开始===============",product.getProductCode());
					continue;
				}
				Integer oldMarketStatus = product.getMarketStatus();//记录原开市状态
				//获取交易时间配置
				TimeConfigVO timeConfigVO = tradeTimeCfgRedis.get(proCode);	
				if(timeConfigVO == null){
					continue;
				}
				List<TradeAndQuotaTime> tradeTimes = timeConfigVO.getTradeTimeList();//交易时间配置
				List<SysSaleTime> saleTimes = timeConfigVO.getSysSaleTimeList();//清仓时间配置

				boolean isOpenStatus = false;//是否是开市状态 true：是  false 否
				boolean isCloseStatus = false;//是否是闭市状态true：是  false 否
				boolean isOnlySale = false;//是否是仅开仓状态true：是  false 否
				//1.判断当前是否为开市状态还是闭市状态
				for(TradeAndQuotaTime tradeTime : tradeTimes){
					Date d1 = DateTools.toDefaultTime(tradeTime.getBeginTime());
					Date d2 = DateTools.toDefaultTime(tradeTime.getEndTime());
					if(DateTools.isEqual(d1, d2)){//如果，开始时间与结束时间相等，则有两种情况，1：运营时间配置错误；2：现在是周六周日或双休日
						product.setMarketStatus(ProductMarketEnum.STOP_TRADING.getValue());
						isCloseStatus = true;
						break;
					}else{
						boolean b = DateTools.betweenDate1AndDate2(d1, d2,DateTools.toDefaultTime(now));
						if(b){//当前时间在交易时间段内，将市场状态改为开市
							product.setMarketStatus(ProductMarketEnum.START_TRADING.getValue());
							isOpenStatus = true;
							break;
						}
					}	
				}
							
				//2.判断当前是否为仅平仓状态
				if(!isOpenStatus && !isCloseStatus){
					for(SysSaleTime saleTime:saleTimes){
						boolean b1 = DateTools.betweenDate1AndDate2(DateTools.toDefaultTime(saleTime.getBeginTime()), 
									DateTools.toDefaultTime(saleTime.getEndTime()),DateTools.toDefaultTime(now));
						if(b1){//当前时间仅平仓
							product.setMarketStatus(ProductMarketEnum.ONLY_LIQUIDATION.getValue());
							isOnlySale = true;
							break;
						}
					}
				}
						
				//3.判断当前是否为休市状态还是闭市状态
				if(!isOpenStatus && !isOnlySale){
					for(TradeAndQuotaTime tradeTime : tradeTimes){
						Date d1 = DateTools.toDefaultTime(tradeTime.getBeginTime());
						boolean b2 = DateTools.betweenDate1AndDate2(d1,DateTools.toDefaultTime(tradeTime.getTime()),
								   DateTools.toDefaultTime(now));
						if(b2){
							product.setMarketStatus(ProductMarketEnum.REST.getValue());
							break;
						}
						product.setMarketStatus(ProductMarketEnum.STOP_TRADING.getValue());
					}
				}	
				
				if(oldMarketStatus != product.getMarketStatus()){//市场状态有变化
					logger.info("==商品={},市场状态==有变化==(原市场状态)={},(新市场状态)={}=="
										,product.getProductCode(),oldMarketStatus,product.getMarketStatus());
					
					flag = true;
					if(product.getMarketStatus() == ProductMarketEnum.STOP_TRADING.getValue()){//闭市
						product.setShutReason("定时任务触发闭市");
						logger.info("---------------定时任务触发闭市-----------");
					}else if(product.getMarketStatus() == ProductMarketEnum.START_TRADING.getValue()){
						product.setShutReason("定时任务触发开市");
						logger.info("---------------定时任务触发开市-----------");
					}else if(product.getMarketStatus() == ProductMarketEnum.ONLY_LIQUIDATION.getValue()){
						product.setShutReason("定时任务触发仅平仓");
						logger.info("---------------定时任务触发仅平仓-----------");
					}else if(product.getMarketStatus() == ProductMarketEnum.REST.getValue()){
						product.setShutReason("定时任务触发休市");
						logger.info("---------------定时任务触发休市-----------");
					}	
					productDao.updateProduct(product);
					proRedis.put(proCode, product);
				}else{
					logger.info("==商品={},市场状态==无变化==(原市场状态)={},(新市场状态)={}=="
							,product.getProductCode(),oldMarketStatus,product.getMarketStatus());
				}
			}
			logger.info("========每一分钟刷新当前市场状态======执行结束=========");
		}finally{
			//解锁
			sysThreadLockService.unlock(SysThreadLockEnum.REFRESH_MARKET_STATUS.getCode());
		}
				
		if(flag){
			String msg = "{\"CMDID\":\"QU003\",\"DATA\":\"市场状态变化刷新\"}";
//			dispatcherApiService.sendMsg(msg, DispatherEnum.ALL);
			JmsTopicOrderMsg topicMsg = new JmsTopicOrderMsg(DispatherEnum.ALL,msg);
	    	String json = JSON.toJSONString(topicMsg);
	    	logger.info("向MQ推送交易结果:{}", json);
	    	sendOrderMsgProducer.sendMessage(json);
			flag = false;
			logger.info("向APP客户端发送刷新命令:{}", msg);
		}
	}


	@Override
	public void loadProTimeCfgToRedisSchedule() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);//当前时间
		
		//判断当前时区，如果是冬令时执行6点定时任务，如果是夏令时执行5点定时任务
		BoundHashOperations<String, String, String>  sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		String winter_summer_change = sysCfgRedis.get("winter_summer_change");//1：夏令时，2：冬令时
		if("1".equals(winter_summer_change) && hour==5){
			this.loadProTimeCfgToRedis();
		}else if("2".equals(winter_summer_change) && hour==6){
			this.loadProTimeCfgToRedis();
		}
		
	}

}
