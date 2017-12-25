/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.utils
 * FILE    NAME: StopLossProfitPriceUtil.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.utils;

import com.alibaba.fastjson.JSON;
import com.lt.api.sms.ISmsApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.sms.SystemMessage;
import com.lt.model.sms.SystemMessageContant;
import com.lt.model.trade.JmsTopicOrderMsg;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.UserContant;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.util.utils.redis.RedisInfoOperate;
import com.lt.vo.product.TimeConfigVO;
import com.lt.vo.product.TimeConfigVO.SysSaleTime;
import com.lt.vo.product.TimeConfigVO.TradeAndQuotaTime;
import com.lt.vo.trade.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO 交易工具类
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年12月15日 下午5:45:56
 */
public class TradeUtils implements Serializable {

    /**
     * TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = -5983642288631011838L;
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(TradeUtils.class);

    /**
     * 随机数
     */
    private static Random rand = new Random();
    /**
     * yyMMdd时间格式化
     */
    private static SimpleDateFormat yyMMdd_FORMAT = new SimpleDateFormat("yyMMdd");
    /**
     * MMdd时间格式化
     */
    private static SimpleDateFormat MMdd_FORMAT = new SimpleDateFormat("MMdd");
    /**
     * 委托单号缓存
     */
    private static ConcurrentHashMap<String, Integer> entrustCodeMap = new ConcurrentHashMap<String, Integer>();
    /**
     * 委托单号集合
     */
    private static Set<Integer> entrustCodeIdSet = new HashSet<Integer>();
    
    static Map<String, String> map = new ConcurrentHashMap<String, String>();
    
    /**
     * 多线程处理
     */
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
    /**
     * TODO 生产订单id
     *
     * @return
     * @author XieZhibing
     * @date 2016年12月9日 下午9:01:33
     */
    public synchronized static String makeOrderCode() {
        StringBuffer code = new StringBuffer(yyMMdd_FORMAT.format(new Date()));
        code.append(rand.nextInt(10));
        code.append(String.format("%09d", rand.nextInt(999999999)));
        return "OR" + code.toString();
    }

    /**
     * 积分订单
     *
     * @return
     */
    public synchronized static String makeScoreOrderCode() {
        StringBuffer code = new StringBuffer(yyMMdd_FORMAT.format(new Date()));
        code.append(rand.nextInt(10));
        code.append(String.format("%09d", rand.nextInt(999999999)));
        return "SOR" + code.toString();
    }

    /**
     * 生成成交单id
     *
     * @return
     */
    public synchronized static String makeSuccessCode() {
        StringBuffer code = new StringBuffer(yyMMdd_FORMAT.format(new Date()));
        code.append(rand.nextInt(10));
        code.append(String.format("%09d", rand.nextInt(999999999)));
        return "SU" + code.toString();
    }


    /***
     * 创建委托单id
     * @return
     */
    public synchronized static Integer makeEntrustCode() {
        return createData(7);
    }

    /**
     * TODO 根据产品买入时间选择清仓时间
     *
     * @param productCode
     * @param deferStatus   递延状态: 0 非递延; 1 递延
     * @param redisTemplate
     * @return
     * @author XieZhibing
     * @date 2016年12月15日 上午11:45:02
     */
    public static Date querySysSetSaleDate(String productCode, int deferStatus, RedisTemplate<String, String> redisTemplate) {

        long startTime = System.currentTimeMillis();

        //查询redis缓存中的交易时间配置
        BoundHashOperations<String, String, TimeConfigVO> redis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
        TimeConfigVO timeConfigVO = redis.get(productCode);
        if (timeConfigVO == null) {
            //未获取到商品时间配置信息
            throw new LTException(LTResponseCode.PRJ0001);
        }
        //交易时间段
        List<TradeAndQuotaTime> tradeTimeList = timeConfigVO.getTradeTimeList();
        //清仓时间段
        List<SysSaleTime> sysSaleTimeList = timeConfigVO.getSysSaleTimeList();
        if (tradeTimeList == null || tradeTimeList.isEmpty() || sysSaleTimeList == null || sysSaleTimeList.isEmpty()) {
            //未获取到商品时间配置信息
            throw new LTException(LTResponseCode.PRJ0001);
        }

        //返回结果
        Date result = null;
        //当前时间
        Date now = new Date();
        String sdate = DateTools.formatDate(now, DateTools.DEFAULT_DATE);
        //计算当前时间对应的清仓时间点
        for (TradeAndQuotaTime tradeAndQuotaTime : tradeTimeList) {
            //本交易时间段的开始时间
            Date dBeginTime = DateTools.parseDate(sdate + " " + tradeAndQuotaTime.getBeginTime(), DateTools.DEFAULT_DATE_TIME);
            //本交易时间段的结束时间
            Date dEndTime = DateTools.parseDate(sdate + " " + tradeAndQuotaTime.getEndTime(), DateTools.DEFAULT_DATE_TIME);
            //本交易时间段的清仓时间
            Date dSaleTime = DateTools.parseDate(sdate + " " + tradeAndQuotaTime.getTime(), DateTools.DEFAULT_DATE_TIME);

            //如: 09:00:00 -- 10:15:00 -- 14:48:00, 10:30:00 -- 11:30:00 -- 14:48:00,
            //13:30:00 -- 14:48:00-- 14:48:00, 00:00:00 -- 05:58:00 -- 05:58:00


            logger.info("当前时间信息：dBeginTime：{}，dEndTime：{}，dSaleTime：{}"
                    , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dBeginTime)
                    , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dEndTime)
                    , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dSaleTime));


            if (DateTools.date3betweenBothDate(dBeginTime, dSaleTime, dEndTime)) {
                if (DateTools.date3betweenBothDate(dBeginTime, dEndTime, now)) {
                    result = dSaleTime;
                    break;
                }
            }
            //如: 21:00:00 -- 02:28:00 -- 02:28:00
            if (DateTools.compareTo(dBeginTime, dEndTime) && DateTools.compareTo(dSaleTime, dEndTime)) {
                //日期延后一天
                dEndTime = DateTools.addDay(dEndTime, 1);
                //日期延后一天
                dSaleTime = DateTools.addDay(dSaleTime, 1);
                if (DateTools.date3betweenBothDate(dBeginTime, dEndTime, now)) {
                    result = dSaleTime;
                    break;
                }
            }
            //如: 07:00:00 -- 23:59:59 -- 05:58:00
            if (DateTools.compareTo(dEndTime, dBeginTime) && DateTools.compareTo(dBeginTime, dSaleTime)) {
                //日期延后一天
                dSaleTime = DateTools.addDay(dSaleTime, 1);
                if (DateTools.date3betweenBothDate(dBeginTime, dEndTime, now)) {
                    result = dSaleTime;
                    break;
                }
            }
        }

        //计算递延单的清仓时间
        if (result != null && deferStatus == DeferStatusEnum.DEFER.getValue()) {
            //重新计算清仓日期
            String saleDate = DateTools.formatDate(result, DateTools.DEFAULT_DATE);
            //重新计算清仓时间
            String saleTime = DateTools.formatDate(result, DateTools.DEFAULT_TIME);
            //清仓时间配置数量
            int size = sysSaleTimeList.size();
            //清仓时间计算
            for (int i = 0; i < size; i++) {
                //清仓时间配置
                SysSaleTime sysSaleTime = sysSaleTimeList.get(i);

                //选择清仓时间点
                if (saleTime.equals(sysSaleTime.getBeginTime())) {
                    if (i + 1 == size) {
                        sysSaleTime = sysSaleTimeList.get(0);
                    } else {
                        sysSaleTime = sysSaleTimeList.get(i + 1);
                    }
                    //清仓时间点
                    Date dSaleTime = DateTools.parseDate(saleDate + " " + sysSaleTime.getBeginTime(), DateTools.DEFAULT_DATE_TIME);
                    //如果result大于等于dSaleTime
                    if (DateTools.compareTo(result, dSaleTime)) {
                        dSaleTime = DateTools.addDay(dSaleTime, 1);
                    }
                    result = dSaleTime;
                    break;
                }
            }
        }

        long startTime2 = System.currentTimeMillis();
        logger.info("======查询闭市时间:{}用时:{}ms", DateTools.parseToTradeTimeStamp(new Date()), (startTime2 - startTime));

        //关闭连接
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnectionUtils.unbindConnection(connectionFactory);

        //清空List,便于GC回收内存
        tradeTimeList.clear();

        return result;
    }

    /**
     * TODO 计算每手的止损价
     *
     * @param buyPrice   买入价
     * @param jumpPrice  最小变动价格，如10
     * @param jumpValue  最小波动点，如 0.01
     * @return
     * @author XieZhibing
     * @date 2016年12月15日 下午6:01:45
     */
    public static double stopLossPrice(Integer tradeDirection, Double buyPrice,
                                       Double stopLoss, Double jumpPrice, Double jumpValue) {
        //止损价
        double stopLossPrice = 0.00;
        //多
        if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
            stopLossPrice = DoubleUtils.sub(buyPrice , DoubleUtils.mul(DoubleUtils.div(stopLoss, jumpPrice) , jumpValue));
            //空
        } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == tradeDirection) {
            stopLossPrice = DoubleUtils.add(buyPrice , DoubleUtils.mul(DoubleUtils.div(stopLoss, jumpPrice) , jumpValue));
        }

        logger.info("计算止损价: tradeDirection:{}, buyPrice:{}, stopLoss:{}, jumpPrice:{}, jumpValue:{}, stopLossPrice:{}",
                tradeDirection, buyPrice, stopLoss, jumpPrice, jumpValue, stopLossPrice);
        return stopLossPrice;
    }

    /**
     * TODO 计算每手的止盈价
     *
     * @param buyPrice   买入价
     * @param stopProfit 止盈值
     * @param jumpPrice  最小变动价格，如10
     * @param jumpValue  最小波动点，如 0.01
     * @return
     * @author XieZhibing
     * @date 2016年12月15日 下午6:01:33
     */
    public static double stopProfitPrice(Integer tradeDirection, Double buyPrice,
                                         Double stopProfit, Double jumpPrice, Double jumpValue) {
        //止盈价
        double stopProfitPrice = 0.00;
        //多
        if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
            stopProfitPrice =DoubleUtils.add(buyPrice, DoubleUtils.mul(DoubleUtils.div(stopProfit, jumpPrice) , jumpValue))   ;
            //空
        } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == tradeDirection) {
            stopProfitPrice =DoubleUtils.sub(buyPrice, DoubleUtils.mul(DoubleUtils.div(stopProfit, jumpPrice) , jumpValue)) ;
        }

        logger.info("计算止盈价: tradeType:{}, buyPrice:{}, stopProfit:{}, jumpPrice:{}, jumpValue:{}, stopProfitPrice:{}",
                tradeDirection, buyPrice, stopProfit, jumpPrice, jumpValue, stopProfitPrice);
        return stopProfitPrice;
    }

    /**
     * TODO 向APP客户端推送交易结果
     *
     * @param dispatcherApiService
     * @author XieZhibing
     * @date 2017年1月5日 下午9:40:18
     */
    public static void notifyAPPClient(String userId,MessageQueueProducer sendOrderMsgProducer) {
        //交易结果推送
//        String msg = "{\"CMDID\":\"QU003\"}";
        logger.info("==========通知App刷新界面,userId={}============", userId);

        if(sendOrderMsgProducer!=null){
        	notifyAPPClientForMQ(sendOrderMsgProducer, userId);
        }
//        logger.info("向APP客户端推送交易结果:{}", msg);
    }

    public static void notifyAPPClientForMQ(MessageQueueProducer sendOrderMsgProducer,String userId){
    	String msg = "{\"CMDID\":\"QU003\"}";
    	JmsTopicOrderMsg topicMsg = new JmsTopicOrderMsg(userId,msg);
    	String json = JSON.toJSONString(topicMsg);
    	logger.info("向MQ推送交易结果:{}", json);
    	sendOrderMsgProducer.sendMessage(json);
    	
    }
    /**
     * TODO 平仓结算异常, 通知管理员
     *
     * @author XieZhibing
     * @date 2017年2月14日 下午3:36:29
     */
    public static void sendBalanceErrorSMS(ISmsApiService smsApiService, Integer userId, String displayId) {
        String tele = "13857862430";
        String content = "用户（" + userId + "）订单（" + displayId + "）平仓成功，结算时发生异常，请联系管理员处理！";
        logger.info("发送平仓结算异常短信, tele:{}, content:{}", tele, content);
        TradeUtils.sendBalanceErrorSMS(smsApiService, tele, content);
    }

    /**
     * 平仓结算异常异常, 通知管理员
     * @param smsApiService
     * @param tele
     * @param content
     */
    public static void sendBalanceErrorSMS(ISmsApiService smsApiService, String tele, String content) {

        SystemMessage ann = new SystemMessage();
        ann.setUserId("-999"); // 注册时不存在用户信息，赋默认值
        ann.setDestination(tele);
        ann.setContent(content);
        ann.setCause(UserContant.SALE_ERROR_MSG_MARK);
        ann.setType(UserContant.SMS_SHORT_TYPE);
        ann.setSmsType(Integer.parseInt(UserContant.SALE_ERROR_MSG_TYPE));
        ann.setPriority(0);
        ann.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS); // 默认为发送成功
        ann.setUserType(0);
        ann.setIp("");
        ann.setCreateDate(new Date());

        try {
            smsApiService.sendExpirationMsg(ann);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("短信发送失败，内容为：{}", content);
        }
    }
    public static Set<String> teleSet = new HashSet<String>();
//    static {
//    	teleSet.add("13197938613");//徐
//    	teleSet.add("18896605394");//贾
//    	teleSet.add("18767169476");//董
//    	teleSet.add("15257103569");//宋
//    	teleSet.add("13857862430");//夏
//    	teleSet.add("18072865329");
//    	teleSet.add("13735853160");
//    }
    /**
	 * 发送短信给财务
	 * 类型                                     内容                                                                    是否需要人工干预
	 * 开仓报单延迟预警              {时间} {订单ID}开仓出现超时开仓失败                   否
	 * 开仓成交一分钟未回执      {时间} {订单ID}开仓未收到回执失败                       否
	 * 开仓报单-交易所成功，订单已失败    {时间} {订单ID}开仓购买成功但是订单开仓已失败，请及时处理    是
	 * 平仓报单延迟预警              {时间} {订单ID}平仓出现超时开仓失败
	 * 平仓成交一分钟没有回执   {时间} {订单ID}平仓未收到回执失败                       否
	 * 平仓 交易所成功订单失败   {时间} {订单ID}平仓购买成功但是订单开仓已失败，请及时处理    是   
	 * 未清仓预警
	 */
    /**
     * 发送短信给财务
     * @param smsApiService
     * @param displayId
     * @param entrustId
     * @param type
     * @param tradeType
     */
	public static void sendOrderIdErrorSMS(ISmsApiService smsApiService,
			String displayId, String entrustId,String type, Integer tradeType) {
		Date date =new Date() ;
		String content = "预警短信发送";
		switch (type) {
		case "2002":
//			开仓报单延迟预警              {时间} {订单ID}开仓出现超时开仓失败                   否
//			平仓报单延迟预警              {时间} {订单ID}平仓出现超时开仓失败
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				content = DateTools.parseToDefaultDateTimeString(date)+" 实际委托单号（" + entrustId + "）开仓出现超时开仓失败！";
				if(StringTools.isNotEmpty(displayId)){
					content = DateTools.parseToDefaultDateTimeString(date)+" 订单号（" + displayId + "）开仓出现超时开仓失败！";
				}
			}else{
				content = DateTools.parseToDefaultDateTimeString(date)+" 实际委托单号（" + entrustId + "）平仓出现超时平仓失败！";
				if(StringTools.isNotEmpty(displayId)){
					content = DateTools.parseToDefaultDateTimeString(date)+" 订单号（" + displayId + "）平仓出现超时平仓失败！";
				}
			}
			break;
		case "2003":
//		{时间} {订单ID}开仓购买成功但是订单开仓已失败，请及时处理    是
//			平仓 交易所成功订单失败   {时间} {订单ID}平仓购买成功但是订单开仓已失败，请及时处理    是   
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				content = DateTools.parseToDefaultDateTimeString(date)+" 实际委托单号（" + entrustId + "）开仓购买成功但是订单开仓已失败，请及时处理！";
				if(StringTools.isNotEmpty(displayId)){
					content = DateTools.parseToDefaultDateTimeString(date)+" 订单号（" + displayId + "）开仓购买成功但是订单开仓已失败，请及时处理！";
				}
			}
			else{
				content = DateTools.parseToDefaultDateTimeString(date)+" 实际委托单号（" + entrustId + "）平仓购买成功但是订单平仓已失败，请及时处理！";
				if(StringTools.isNotEmpty(displayId)){
					content = DateTools.parseToDefaultDateTimeString(date)+" 订单号（" + displayId + "）平仓购买成功但是订单平仓已失败，请及时处理！";
				}
			}
			
			break;
		case "2004":
//			{时间} {订单ID}开仓购买成功但是订单开仓已失败，请及时处理    是
//			平仓 交易所成功订单失败   {时间} {订单ID}平仓购买成功但是订单开仓已失败，请及时处理    是   
			if(tradeType == TradeTypeEnum.BUY.getValue()){
				content = DateTools.parseToDefaultDateTimeString(date)+" 实际委托单号（" + entrustId + "）开仓购买成功但是订单开仓已失败，请及时处理！";
				if(StringTools.isNotEmpty(displayId)){
					content = DateTools.parseToDefaultDateTimeString(date)+" 订单号（" + displayId + "）开仓购买成功但是订单开仓已失败，请及时处理！";
				}
			}else{
				content = DateTools.parseToDefaultDateTimeString(date)+" 实际委托单号（" + entrustId + "）平仓购买成功但是订单平仓已失败，请及时处理！";
				if(StringTools.isNotEmpty(displayId)){
					content = DateTools.parseToDefaultDateTimeString(date)+" 订单号（" + displayId + "）平仓购买成功但是订单平仓已失败，请及时处理！";
				}
			}
			
			break;
		case "2005":
			content = DateTools.parseToDefaultDateTimeString(date)+" 实际委托单号（" + entrustId + "）一分钟未收到回执失败，请及时处理！";
			if(StringTools.isNotEmpty(displayId)){
				content = DateTools.parseToDefaultDateTimeString(date)+" 订单号（" + displayId + "）一分钟未收到回执失败，请及时处理！";
			}
			break;
		default:
			content = "委托单号（" + entrustId + "）在系统平台已失败，请对比交易持仓与交易所持仓，强制卖出！";
			if(StringTools.isNotEmpty(displayId)){
				content = "订单号（" + displayId + "）在系统平台已失败，请对比交易持仓与交易所持仓，强制卖出！";
			}
			break;
		}
		for (String tele : teleSet) {  
		      logger.info("发送已失败订单，收到C++成功消息, tele:{}, content:{},displayId:{},entrustId:{}", tele, content,displayId,entrustId);
		      TradeUtils.sendBalanceErrorSMS(smsApiService, tele, content);
		}
	}

    /**
     * 比较行情买卖价格是否与上一条一致
     *
     * @param obj
     * @return true不一致  false 一致
     */
    public static boolean quotaEquals(ProductPriceBean obj) {
        String str = map.get(obj.getProductName());
        if (StringTools.isNotEmpty(obj)) {
            StringBuffer sf = new StringBuffer()
                    .append("askPrice:").append(obj.getAskPrice())
                    .append("bidPrice:").append(obj.getBidPrice());
            if (StringTools.isNotEmpty(str)) {
                if (str.equals(sf.toString())) {
//					logger.error("商品_{},上一条行情数据_{},本次一条行情数据_{}",obj.getProductName(),str,sf.toString());
                    return false;
                }
            } else {
                map.put(obj.getProductName(), sf.toString());
            }
        }
        return true;
    }

    //根据指定长度生成纯数字的随机数

    /**
     * length 《= 7
     *
     * @param length
     * @return
     */
    public static int createData(int length) {
        String key = DateTools.getSecond(new Date()) + "";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        String data = key + sb.toString();
        Integer len = Integer.valueOf(data);
        if (entrustCodeIdSet.contains(len)) {
            return createData(length);
        }
        entrustCodeIdSet.add(len);
        return len;
    }
    
    /**
	 * 检查止盈止损是否符合范围
	 * @param investorFeeCfg
	 * @param orderVo
	 */
	public static void checkStopLossAndStopProfit(InvestorFeeCfg investorFeeCfg,OrderVo orderVo){
			String loss =  StringTools.formatStr(investorFeeCfg.getStopLossRange(),"");
			String profit =  StringTools.formatStr(investorFeeCfg.getStopProfitRange(),"");
			double stopLoss = StringTools.formatDouble(orderVo.getStopLoss(),0.0) ;//每手止损金额
			double stopProfit = StringTools.formatDouble(orderVo.getStopProfit(),0.0);//每手止盈金额
			if(loss.contains(",")){
				loss = loss.split(",")[0];
			}
			if(profit.contains(",")){
				profit = profit.split(",")[0];
			}
			double defaultStopLoss = StringTools.formatDouble(loss,0.0);
			double defaultStopProfit = StringTools.formatDouble(profit,0.0);
			logger.info("stopLoss:{} stopProfit:{} defaultStopLoss:{} defaultStopProfit:{}",stopLoss, stopProfit, defaultStopLoss, defaultStopProfit);
			if(defaultStopLoss > stopLoss || defaultStopProfit > stopProfit) {
				throw new LTException(LTResponseCode.IV00004);
			}

            if(investorFeeCfg.getIsSupportDefer() == null || (orderVo.getDeferStatus() == 1
                    && investorFeeCfg.getIsSupportDefer() != 1)){
                throw new LTException(LTResponseCode.PRJ0007);
            }
		
	}
	
	/**
	 * 判断是否重复卖出
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public static boolean isDuplicateSell(RedisTemplate<String, String> redisTemplate,String userId,String orderId){	
		return !RedisInfoOperate.setSuccess(redisTemplate, RedisUtil.ORDER_EXTERNAL_ID_SELL+":"+orderId,userId);
		
	}
	/**
	 * 设置卖出完成
	 * @param redisTemplate
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public static boolean setSuccess(RedisTemplate<String, String> redisTemplate,String userId,String orderId){	
		return !RedisInfoOperate.setSuccess(redisTemplate, RedisUtil.ORDER_EXTERNAL_ID_SELL_SUCCESS+":"+orderId,userId);
		
	}
	/**
	 * 是否卖出完成
	 * @param redisTemplate
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public static boolean isSuccess(RedisTemplate<String, String> redisTemplate,String userId,String orderId){	
		return RedisInfoOperate.isExit(redisTemplate, RedisUtil.ORDER_EXTERNAL_ID_SELL_SUCCESS+":"+orderId);
		
	}
	/**
	 * 移除卖出监控
	 * @param redisTemplate
	 * @param orderId
	 */
	public static void delKeyLockSell(RedisTemplate<String, String> redisTemplate,String orderId){	
		RedisInfoOperate.delKeyLock(redisTemplate, RedisUtil.ORDER_EXTERNAL_ID_SELL+":"+orderId);
		
	}
	public static void main(String[] args) {
//		System.out.println(5&2);
		for (int i = 0; i < 360; i++) {
			System.out.println("20170717"+createDatas(20));;
		}
	}
	private static Set<String> set = new HashSet<String>();
	/**
	 * string类型
	 * @param length
	 * @return
	 */
	public static String createDatas(int length) {
        String key = "";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        String data = key + sb.toString();
        if (set.contains(data)) {
            return createDatas(length);
        }
        set.add(data);
        return data;
    }
}
