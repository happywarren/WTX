package com.lt.trade.riskcontrol;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.trade.SellTriggerTypeEnum;
import com.lt.trade.riskcontrol.bean.MutableStopLossBean;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.trade.tradeserver.bean.FutureOrderBean;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.trade.utils.LTConstants;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 风控队列(止盈/止损)
 *
 * Created by sunch on 2016/11/11.
 */
public class RiskControlQueue {
	
	private static Logger logger = LoggerFactory.getLogger(RiskControlQueue.class);

    private ReadWriteLock priceLock = new ReentrantReadWriteLock(true);
    private ReadWriteLock orderLock = new ReentrantReadWriteLock(true);

    /**
     *  多方向止盈 升序
     */
    private Map<Double, Set<String>> abovePriceQueue = new TreeMap<Double, Set<String>>();
    /**
     *  多方向止损 降序
     */
    private Map<Double, Set<String>> belowPriceQueue = new TreeMap<Double, Set<String>>(Collections.reverseOrder());
    /**
     *  空方向止盈 升序
     */
    private Map<Double, Set<String>> emptyabovePriceQueue = new TreeMap<Double, Set<String>>(Collections.reverseOrder());
    /**
     *  空方向止损  降序
     */
    private Map<Double, Set<String>> emptybelowPriceQueue = new TreeMap<Double, Set<String>>();
    /**
     *  订单(确保订单唯一)
     */
    private Map<String, RiskControlBean> riskOrdersMap = new ConcurrentHashMap<String, RiskControlBean>();
    /**
     *  移动止损
     */
    private Map<String, MutableStopLossBean> mutableStopLossOrdersMap = new ConcurrentHashMap<String, MutableStopLossBean>();
    
    public RiskControlQueue() {
    }

    public Map<String, RiskControlBean> getRiskOrdersMap(){
		return riskOrdersMap;
    }
    public String getMutableStopLossOrdersMap(){
    	String str = JSONObject.toJSONString(mutableStopLossOrdersMap);
    //	logger.info("mutableStopLossOrdersMap"+str);
		return str;
    }
    // 获取清仓订单
    List<FutureOrderBean> getClearOrders() {
    	logger.info("获取清仓订单------------------------");
    	//getRiskOrdersMap();
        List<FutureOrderBean> orderList = new ArrayList<FutureOrderBean>();
        long currentTimeStamp = System.currentTimeMillis();

        for (Entry<String, RiskControlBean> entry : riskOrdersMap.entrySet()) {
//            String orderId = entry.getKey();
            RiskControlBean riskBean = entry.getValue();
            if (riskBean != null) {
                long orderTimeStamp = riskBean.getDeferredOrderTimeStamp();
                //递延时间比当前时间小的全部清仓
                if(currentTimeStamp >= orderTimeStamp ){
                	orderList.add(riskBean.getOrderBean());
                	continue;
                }
                //卖出时间在清仓时间点上下5分钟内的才卖
                if (Math.abs(currentTimeStamp - orderTimeStamp) < 30 * 5  * 1000) {
                    orderList.add(riskBean.getOrderBean());
                }
            }
        }

        logger.info("orderList = {}",JSONObject.toJSONString(orderList));
        return orderList.isEmpty() ? null : orderList;
    }
    // 更新递延订单
    public boolean updateDeferredOrder(String orderId, long timeStamp) {
        RiskControlBean riskBean = riskOrdersMap.get(orderId);
        if (riskBean != null) {
            riskBean.setDeferredOrderTimeStamp(timeStamp);
            return true;
        }

        return false;
    }

    // 获取风控订单(移动止损)
    public MutableStopLossBean getMutableStopLossOrder(String orderId) {
        return mutableStopLossOrdersMap.get(orderId);
    }

    // 开启/关闭移动止损
    public void switchMutableStopLossOrder(RiskControlBean riskBean) {
        if (riskBean.isMutableStopLoss()) {
            mutableStopLossOrdersMap.put(
                    riskBean.getUniqueOrderId(),
                    new MutableStopLossBean(riskBean.getDirect(), riskBean.getStopLossPrice(), riskBean.getMatchPrice()));
            getMutableStopLossOrdersMap();//打印日志
        } else {
            if (mutableStopLossOrdersMap.get(riskBean.getUniqueOrderId()) != null) {
                mutableStopLossOrdersMap.remove(riskBean.getUniqueOrderId());
                getMutableStopLossOrdersMap();//打印移动风控日志
            }
        }
    }

    // 更新风控订单(移动止损)
    //已开启的移动止损的情况下，修改了止盈止损，这时需要计算新的移动止损价格
    private void updateMutableStopLossOrder(RiskControlBean riskBean) {
        MutableStopLossBean stopLossBean = mutableStopLossOrdersMap.get(riskBean.getUniqueOrderId());
        if (stopLossBean != null) {
            if (riskBean.getDirect() == LTConstants.TRADE_DIRECT_BUY) {
                //差额 = 买入均价  -   新的订单止损价
                double point = DoubleUtils.sub(riskBean.getMatchPrice() , riskBean.getStopLossPrice());
                //新移动风控止损价 = 移动止损价格 - 差额
            	stopLossBean.setStopLossPrice(DoubleUtils.sub(stopLossBean.getSentinelPrice() , point));
            } else {
                double point = DoubleUtils.sub(riskBean.getStopLossPrice() , riskBean.getMatchPrice());
            	stopLossBean.setStopLossPrice(DoubleUtils.add(stopLossBean.getSentinelPrice() , point));
            }
            logger.info("修改移动风控止盈止损  "+JSONObject.toJSONString(stopLossBean));
        }
    }

    // 更新风控订单(会存在修改止盈止损过程中刚好触发风控, 返回true, 但订单已经被平掉情况)
    public boolean updateRiskControlOrder(RiskControlBean riskBean) {
        // 移动止损
    	logger.info("进入到风控模块，入参为：{}",JSONObject.toJSONString(riskBean));
        if (riskBean.isMutableStopLoss()) {
        	logger.info("进入到修改移动风控");
            updateMutableStopLossOrder(riskBean);
        }

        // 普通订单
        orderLock.writeLock().lock();
        try {
            if (riskOrdersMap.remove(riskBean.getUniqueOrderId()) != null) {
            	logger.info("进入到填充序列 riskBean : {}",JSONObject.toJSON(riskBean));
                fillRiskControlQueue(riskBean);
                return true;
            }
            return false;
        } finally {
            orderLock.writeLock().unlock();
        }
    }

    // 移除风控订单
    public boolean removeRiskControlOrder(RiskControlBean riskBean) {
        String orderId = riskBean.getUniqueOrderId();

        if (mutableStopLossOrdersMap.get(orderId) != null) {
            mutableStopLossOrdersMap.remove(orderId);
        }

        orderLock.writeLock().lock();
        try {
            return riskOrdersMap.remove(orderId) != null;
        } finally {
            orderLock.writeLock().unlock();
        }
    }

    // 填充风控队列
    public void fillRiskControlQueue(RiskControlBean riskBean) {
        // 移动止损
        if (riskBean.isMutableStopLoss() && (mutableStopLossOrdersMap.get(riskBean.getUniqueOrderId()) == null)) {
            mutableStopLossOrdersMap.put(
                    riskBean.getUniqueOrderId(),
                    new MutableStopLossBean(riskBean.getDirect(), riskBean.getStopLossPrice(), riskBean.getMatchPrice()));
        }
        // 普通订单
        if (riskBean.getDirect() == LTConstants.TRADE_DIRECT_BUY) {
            // 开仓买入(看多)
            priceLock.writeLock().lock();
            try {
                // 添加
                riskOrdersMap.put(riskBean.getUniqueOrderId(), riskBean);

                // 止盈
                if (abovePriceQueue.containsKey(riskBean.getStopGainPrice())) {
                    Set<String> riskControlList = abovePriceQueue.get(riskBean.getStopGainPrice());
                    riskControlList.add(riskBean.getUniqueOrderId());
                } else {
                    Set<String> riskControlList = new HashSet<String>();
                    riskControlList.add(riskBean.getUniqueOrderId());
                    abovePriceQueue.put(riskBean.getStopGainPrice(), riskControlList);
                }
//                logger.info("abovePriceQueue止盈_开仓买入(看多) = {}",JSONObject.toJSONString(abovePriceQueue));

                // 止损
                if (belowPriceQueue.containsKey(riskBean.getStopLossPrice())) {
                    Set<String> riskControlList = belowPriceQueue.get(riskBean.getStopLossPrice());
                    riskControlList.add(riskBean.getUniqueOrderId());
                } else {
                    Set<String> riskControlList = new HashSet<String>();
                    riskControlList.add(riskBean.getUniqueOrderId());
                    belowPriceQueue.put(riskBean.getStopLossPrice(), riskControlList);
                }
//                logger.info("belowPriceQueue止损_开仓买入(看多) = {}",JSONObject.toJSONString(belowPriceQueue));
            } finally {
                priceLock.writeLock().unlock();
            }
        } else {
            // 开仓卖出(看空)
            priceLock.writeLock().lock();
            try {
                // 添加
                riskOrdersMap.put(riskBean.getUniqueOrderId(), riskBean);

                // 止盈
                if (emptyabovePriceQueue.containsKey(riskBean.getStopGainPrice())) {
                    Set<String> riskControlList = emptyabovePriceQueue.get(riskBean.getStopGainPrice());
                    riskControlList.add(riskBean.getUniqueOrderId());
                } else {
                    Set<String> riskControlList = new HashSet<String>();
                    riskControlList.add(riskBean.getUniqueOrderId());
                    emptyabovePriceQueue.put(riskBean.getStopGainPrice(), riskControlList);
                }
//                logger.info("emptyabovePriceQueue止盈_开仓卖出(看空) = {}",JSONObject.toJSONString(emptyabovePriceQueue));

                // 止损
                if (emptybelowPriceQueue.containsKey(riskBean.getStopLossPrice())) {
                    Set<String> riskControlList = emptybelowPriceQueue.get(riskBean.getStopLossPrice());
                    riskControlList.add(riskBean.getUniqueOrderId());
                } else {
                    Set<String> riskControlList = new HashSet<String>();
                    riskControlList.add(riskBean.getUniqueOrderId());
                    emptybelowPriceQueue.put(riskBean.getStopLossPrice(), riskControlList);
                }
//                logger.info("emptybelowPriceQueue止损_开仓卖出(看空) = {}",JSONObject.toJSONString(emptybelowPriceQueue));
            } finally {
                priceLock.writeLock().unlock();
            }
        }
    }
    //移动风控处理
    public List<FutureOrderBean> riskMutableStopLoss(ProductPriceBean productPrice) {
    	if(!TradeUtils.quotaEquals(productPrice)){
//    		logger.error("行情价格与上一条一致");
    		return null;
    	}
        double askPrice = productPrice.getAskPrice();// 卖一价
        double bidPrice = productPrice.getBidPrice();// 买一价
        List<FutureOrderBean> orderList = new ArrayList<FutureOrderBean>();
     // 移动止损
        List<String> candidateMutableList = new ArrayList<String>();
        for (Entry<String, MutableStopLossBean> entry : mutableStopLossOrdersMap.entrySet()) {
            String orderId = entry.getKey();
            MutableStopLossBean stopLossBean = entry.getValue();
            if (stopLossBean.getDirect() == LTConstants.TRADE_DIRECT_BUY) {
                // 更新止损
                if (bidPrice > stopLossBean.getSentinelPrice()) {
                    double diffPrice = DoubleUtils.sub(bidPrice , stopLossBean.getSentinelPrice());
                    stopLossBean.setSentinelPrice(bidPrice);
                    stopLossBean.setStopLossPrice(DoubleUtils.add(stopLossBean.getStopLossPrice() , diffPrice));
                }

                // 平仓止损
                if (bidPrice <= stopLossBean.getStopLossPrice()) {
                    candidateMutableList.add(orderId);
                }
            } else {
                // 更新止损
                if (askPrice < stopLossBean.getSentinelPrice()) {
                    double diffPrice = DoubleUtils.sub(stopLossBean.getSentinelPrice() , askPrice);
                    stopLossBean.setSentinelPrice(askPrice);
                    stopLossBean.setStopLossPrice(DoubleUtils.sub(stopLossBean.getStopLossPrice() , diffPrice));
                }

                // 平仓止损
                if (askPrice >= stopLossBean.getStopLossPrice()) {
                    candidateMutableList.add(orderId);
                }
            }
        }// for
        // 风控移动止损订单
        for (String orderId : candidateMutableList) {
            RiskControlBean riskBean = riskOrdersMap.get(orderId);
            if (riskBean != null) {
            	FutureOrderBean orderBean = riskBean.getOrderBean();
            	orderBean.setSellType(SellTriggerTypeEnum.MOVESTOPLOSS.getValue());//移动止损
            	orderBean.setSentinelPrice(mutableStopLossOrdersMap.get(orderId).getSentinelPrice());//移动止损最高价
        		orderList.add(orderBean);
            }
            mutableStopLossOrdersMap.remove(orderId);
        }
        return orderList.isEmpty() ? null : orderList;
    }

    // 风控 fixme: above_与below_因效率原因, 不做同步更新, 会有无效orderId
    public List<FutureOrderBean> risk(ProductPriceBean productPrice) {
//    	logger.info("ProductPriceBean : {}",JSONObject.toJSONString(productPrice));
    	if(!TradeUtils.quotaEquals(productPrice)){
//    		logger.error("行情价格与上一条一致");
    		return null;
    	}
        double askPrice = productPrice.getAskPrice();// 卖一价
        double bidPrice = productPrice.getBidPrice();// 买一价
        List<FutureOrderBean> orderList = new ArrayList<FutureOrderBean>();

        // 普通订单
        priceLock.writeLock().lock();
        try {
        	//多队列处理
        	queueProcess(bidPrice, orderList);
        	//空队列处理
        	emptyQueueProcess(askPrice, orderList);
        } finally {
            priceLock.writeLock().unlock();
        }
        return orderList.isEmpty() ? null : orderList;

    }

    /**
     * 多队列处理
     * @param bidPrice
     * @param orderList
     */
    public void queueProcess(Double bidPrice,List<FutureOrderBean> orderList){
    	if (abovePriceQueue.isEmpty() || belowPriceQueue.isEmpty()) {
            return ;
        }
        // 多方向 止盈队列
        Iterator<Entry<Double, Set<String>>> aboveEntries = abovePriceQueue.entrySet().iterator();
        while (aboveEntries.hasNext()) {
            Entry<Double, Set<String>> entry = aboveEntries.next();
            Double priceKey = entry.getKey();
            if (priceKey <= bidPrice) {
            	//收集触发平仓单
                Set<String> listValue = entry.getValue();
                for (String orderId : listValue) {
                	//获取订单详情
                    RiskControlBean riskBean = riskOrdersMap.get(orderId);
                    //订单不为空 且卖一价 比止盈价大 触发平仓
                    if (riskBean != null && StringTools.isNotEmpty(riskBean.getOrderBean())) {
                    	FutureOrderBean orderBean = riskBean.getOrderBean();
                    	//设置触发标签    多方向止盈
                    	if (priceKey == riskBean.getStopGainPrice()) {//看多
                    		orderBean.setSellType(SellTriggerTypeEnum.STOPPROFIT.getValue());//止盈
                    	}else{
                    		continue;
                    	}
                		orderList.add(orderBean);
                    }
                }
                //移除止盈止损监控队列
                aboveEntries.remove();
            } else {
                break;
            }
        }
        // 多方向 止损队列
        Iterator<Entry<Double, Set<String>>> belowEntries = belowPriceQueue.entrySet().iterator();
        while (belowEntries.hasNext()) {
            Entry<Double, Set<String>> entry = belowEntries.next();
            Double priceKey = entry.getKey();
            if (priceKey >= bidPrice) {
                Set<String> listValue = entry.getValue();
                for (String orderId : listValue) {
                    RiskControlBean riskBean = riskOrdersMap.get(orderId);
                    if (riskBean != null && StringTools.isNotEmpty(riskBean.getOrderBean())) {
                    	FutureOrderBean orderBean = riskBean.getOrderBean();
                    	if (priceKey == riskBean.getStopLossPrice()) {//看多
                    		orderBean.setSellType(SellTriggerTypeEnum.STOPLOSS.getValue());//止损
                    	}else{
                    		continue;
                    	}
                		orderList.add(orderBean);
                    }
                }
                belowEntries.remove();
            } else {
                break;
            }
        }
    }

    /**
     * 空队列处理
     * @param askPrice
     * @param orderList
     */
    public void emptyQueueProcess(Double askPrice,List<FutureOrderBean> orderList) {
    	if (emptyabovePriceQueue.isEmpty() || emptybelowPriceQueue.isEmpty()) {
            return ;
        }
    	// 空方向 止盈队列
        Iterator<Entry<Double, Set<String>>> emptyAboveEntries = emptyabovePriceQueue.entrySet().iterator();
        while (emptyAboveEntries.hasNext()) {
            Entry<Double, Set<String>> entry = emptyAboveEntries.next();
            Double priceKey = entry.getKey();
            //价格  小于 止盈价  触发风控
            if (askPrice <= priceKey ) {
            	//收集触发平仓单
                Set<String> listValue = entry.getValue();
                for (String orderId : listValue) {
                	//获取订单详情
                    RiskControlBean riskBean = riskOrdersMap.get(orderId);
                    //订单不为空
                    if (riskBean != null && StringTools.isNotEmpty(riskBean.getOrderBean())) {
                    	FutureOrderBean orderBean = riskBean.getOrderBean();
                    	//设置触发标签      空方向止盈
                    	if (priceKey == riskBean.getStopGainPrice()) {//看空
                    		orderBean.setSellType(SellTriggerTypeEnum.STOPPROFIT.getValue());//止盈
                    	}else{
                    		continue;
                    	}
                		orderList.add(orderBean);
                    }
                }
                //移除止盈止损监控队列
                emptyAboveEntries.remove();
            }else {
				break;
			}
        }

        // 空方向 止损队列
        Iterator<Entry<Double, Set<String>>> emptyBelowEntries = emptybelowPriceQueue.entrySet().iterator();
        while (emptyBelowEntries.hasNext()) {
            Entry<Double, Set<String>> entry = emptyBelowEntries.next();
            Double priceKey = entry.getKey();
            // 价格 大于 止损价  触发风控
            if (askPrice >= priceKey) {
                Set<String> listValue = entry.getValue();
                for (String orderId : listValue) {
                    RiskControlBean riskBean = riskOrdersMap.get(orderId);
                    if (riskBean != null && StringTools.isNotEmpty(riskBean.getOrderBean())) {
                    	FutureOrderBean orderBean = riskBean.getOrderBean();
                    	//设置触发标签      空方向止损
                    	if (priceKey == riskBean.getStopLossPrice()) {//看空
                    		orderBean.setSellType(SellTriggerTypeEnum.STOPLOSS.getValue());//止损
                    	}else{
                    		continue;
                    	}
                		orderList.add(orderBean);
                    }
                }
                emptyBelowEntries.remove();
            }else {
				break;
			}
        }
	}
    public static void main(String[] args) {
    	final Map<String ,String> map = new ConcurrentHashMap<String, String>();
    	for (int i = 0; i < 1000; i++) {
			map.put(i+"", i+"");
		}
    	System.out.println(JSONObject.toJSONString(map));
    	System.out.println(map.size());
    	new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 100;
				Iterator<Entry<String, String>> emptyAboveEntries = map.entrySet().iterator();
		        while (emptyAboveEntries.hasNext()) {
		            Entry<String, String> entry = emptyAboveEntries.next();
		            map.put(entry.getKey(), i++ +"");
		        }
		        System.out.println(JSONObject.toJSONString(map));
		        System.out.println(map.size());
			}
		}).start();
    	new Thread(new Runnable() {
			@Override
			public void run() {
				List<String> list = new ArrayList<String>();
				Iterator<Entry<String, String>> emptyAboveEntries = map.entrySet().iterator();
		        while (emptyAboveEntries.hasNext()) {
		            Entry<String, String> entry = emptyAboveEntries.next();
		            String value = entry.getValue();
		            int v = Integer.valueOf(value);
		            if(v < 100){
		            	list.add(value);
		            	emptyAboveEntries.remove();
		            }
		        }
		        System.out.println(JSONObject.toJSONString(map));
		        System.out.println(map.size());
		        System.out.println("list = "+JSONObject.toJSONString(list));
		        System.out.println("list.size = "+list.size());
			}
		}).start();
	}
}
