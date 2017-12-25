package com.lt.trade.tradeserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.SellTriggerTypeEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderCashInfo;
import com.lt.trade.ProductTimeCache;
import com.lt.trade.netty.*;
import com.lt.trade.order.executor.IPersistExecutor;
import com.lt.trade.order.service.IOrderBusinessService;
import com.lt.trade.order.service.IOrderSellTradeService;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.riskcontrol.RiskControlServer;
import com.lt.trade.riskcontrol.bean.MutableStopLossBean;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.trade.tradeserver.bean.*;
import com.lt.trade.tradeserver.handler.MessageHandler;
import com.lt.trade.utils.ErrorCode;
import com.lt.trade.utils.LTConstants;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import com.lt.util.utils.StringTools;
import com.lt.vo.trade.OrderVo;
import com.lt.vo.trade.TradeDirectVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * 交易
 * <p>
 * Created by sunch on 2016/11/8.
 */
public abstract class BaseTrade implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseTrade.class);
    protected IPersistExecutor persistExecutor;

    /**
     * 内外盘标记：内盘/外盘/差价合约
     */
    private String plateName;
    private BaseClient tradeClient;
    private Map<String, BaseClient> clientMap = new ConcurrentHashMap<>();
    private Thread tradeClientThread;

    private RiskControlServer riskControl;
    private Thread riskControlThread;

    private RedisTemplate<String, String> redisTemplate;
    private IOrderBusinessService orderBusinessService;
    private IOrderSellTradeService cashOrderSellTradeService;

    private ScheduledFuture<?> clearFuture = null;
    private final ScheduledExecutorService clearScheduler = new ScheduledThreadPoolExecutor(1);
    private final ScheduledExecutorService clearTimeScheduler = new ScheduledThreadPoolExecutor(1);
    private IProductInfoService productInfoService;
    /**
     * 每分钟加载订单到风控
     */
    private final ScheduledExecutorService perRiskScheduler = new ScheduledThreadPoolExecutor(1);

    protected final ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
    /**
     * 多生产者/单消费者
     */
    private final BlockingQueue<FutureOrderBean> futureOrderQueue = new LinkedBlockingDeque<FutureOrderBean>();

    private Map<Integer, MessageHandler> messageHandlerMap = new HashMap<Integer, MessageHandler>();

    protected ProductTimeCache productTimeCache;

    /**
     * 接收交易数据
     */
    private OnMessageListener tradeMessageListener = new OnMessageListener() {
        @Override
        public void onMessage(String msg) {
            LOGGER.info("接受交易数据 msg = {}", msg);
            JSONObject jsonData = JSON.parseObject(msg);
            Integer frameType = jsonData.getInteger("CMDID");
            String message = jsonData.getString("DATA");
            MessageHandler handler = messageHandlerMap.get(frameType);
            handler.process(message);
        }
    };

    public BaseTrade() {
    }

    public BaseTrade(String plateName) {
        this.plateName = plateName;
    }

    public void setPersistExecutor(IPersistExecutor persistExecutor) {
        this.persistExecutor = persistExecutor;
    }

    public IPersistExecutor getPersistExecutor() {
        return this.persistExecutor;
    }

    public String getPlateName() {
        return plateName;
    }

    public Map<String, BaseClient> getClientMap() {
        return clientMap;
    }

    public void setClientMap(Map<String, BaseClient> clientMap) {
        this.clientMap = clientMap;
    }

    public Thread getTradeClientThread() {
        return tradeClientThread;
    }

    public void setTradeClientThread(Thread tradeClientThread) {
        this.tradeClientThread = tradeClientThread;
    }

    /**
     * 消息映射
     */
    public void addMessageHandler(Integer frameType, MessageHandler handler) {
        messageHandlerMap.put(frameType, handler);
    }

    /**
     * 交易，加同步锁，防止后台管理系统同时修改
     */
    public synchronized void startupFutureTrade(String tradeHost, int tradePort, OnClientStartupListener startupListener) {
        tradeClient = new BaseClient(tradeHost, tradePort, tradeMessageListener, startupListener);
        tradeClientThread = new Thread(tradeClient);
        tradeClientThread.start();
    }

    /**
     * 风控
     */
    public void startupRiskControl(String host, String port, OnStartupListener startupListener, Boolean quotaFlag) {
        riskControl = new RiskControlServer(this, productTimeCache, host, port, startupListener, quotaFlag);
        riskControlThread = new Thread(riskControl);
        riskControlThread.start();
    }

    // redis实例
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public ProductTimeCache getProductTimeCache() {
        return productTimeCache;
    }

    public void setProductTimeCache(ProductTimeCache productTimeCache) {
        this.productTimeCache = productTimeCache;
    }

    /**
     * 加载持仓订单
     */
    public void setOrderBusinessService(IOrderBusinessService orderBusinessService) {
        this.orderBusinessService = orderBusinessService;
    }

    /**
     * 数据库加载所有持仓订单
     *
     * @param plateType
     * @return
     */
    public boolean loadAllCashOrdersFromDB(int plateType) {
        try {
            // 加载现金订单
            List<OrderCashInfo> cashOrders = orderBusinessService.queryAllPositionCashOrders(plateType);
            if (cashOrders != null) {
                for (OrderCashInfo cashOrder : cashOrders) {
                    TradeDirectVo tradeDirect = new TradeDirectVo(cashOrder.getTradeDirection(), TradeTypeEnum.BUY.getValue());
                    RiskControlBean riskBean = new RiskControlBean();
                    riskBean.setProductName(cashOrder.getProductCode());
                    riskBean.setUniqueOrderId(cashOrder.getOrderId());
                    riskBean.setDirect(tradeDirect.getTradeDirect());
                    riskBean.setMatchPrice(cashOrder.getBuyAvgPrice() == null ? 0.0 : cashOrder.getBuyAvgPrice());
                    riskBean.setStopGainPrice(cashOrder.getStopProfitPrice());
                    riskBean.setStopLossPrice(cashOrder.getStopLossPrice());
                    riskBean.setMutableStopLoss(cashOrder.getTrailStopLoss() == LTConstants.MUTABLE_STOPLOSS_OPEN);
                    riskBean.setDeferredOrderTimeStamp(cashOrder.getSysSetSellDate().getTime());
                    FutureOrderBean orderBean = new FutureOrderBean(cashOrder.getUserId(), cashOrder.getSecurityCode(), cashOrder.getExchangeCode(), cashOrder.getProductCode(),
                            cashOrder.getOrderId(), 888888, cashOrder.getBuyEntrustCount(), cashOrder.getBuyAvgPrice() == null ? 0.0 : cashOrder.getBuyAvgPrice(),
                            tradeDirect.getTradeDirect(), LTConstants.TRADE_OFFSET_CLOSE, LTConstants.TRADE_ORDER_TYPE_MARKET, LTConstants.FUND_TYPE_CASH,
                            cashOrder.getCreateDate().getTime());
                    riskBean.setOrderBean(orderBean);
                    riskControl.fillRiskControlQueue(riskBean);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("{} 加载现金持仓订单失败..." + e.getMessage(), this.getClass().getSimpleName());
            return false;
        }

        return true;
    }

    /**
     * 加载商品数据
     */
    public void setProductInfoService(IProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    /**
     * 循环清仓定时任务
     */
    private class LoopClearTask implements Runnable {

        private int clearHour;
        private int clearMinute;
        private Integer plateValue = PlateEnum.getValueByName(plateName);

        private LoopClearTask(int clearHour, int clearMinute) {
            this.clearHour = clearHour;
            this.clearMinute = clearMinute;
        }

        @Override
        public void run() {
            // 清仓
            DecimalFormat df = new DecimalFormat("00");
            String timeKey = df.format(clearHour) + ":" + df.format(clearMinute);
            LOGGER.info("开始【" + plateName + "】清仓...本次清仓时间节点: " + timeKey);

            List<String> listValue = productTimeCache.getSummaryClearTime(timeKey + ":" + plateValue);
            List<String> list = new ArrayList<String>();
            Iterator<String> it = listValue.iterator();
            while (it.hasNext()) {
                String productName = it.next();

                Boolean isExchangeTradingTime = productTimeCache.getIsExchangeTradingTime(productName);
                if (isExchangeTradingTime != null && isExchangeTradingTime) {
                    list.add(productName);
                } else {
                    LOGGER.info("商品code:" + productName + ",交易所已休市...无法完成清仓操作: " + timeKey);
                }
            }
            //清仓
            if (list.size() != 0) {
                clear(list);
            }

            // 移除并追加至容器尾端
            productTimeCache.removeSummaryClearTime(timeKey + ":" + plateValue);
            productTimeCache.addSummaryClearTime(timeKey + ":" + plateValue, listValue);

            // 下一个清仓时间节点
            int nextClearHour = 0;
            int nextClearMinute = 0;
            Map<String, List<String>> summaryClearTimeMap = productTimeCache.getSummaryClearTimeMap();
            LOGGER.info("【" + plateName + "】summaryClearTimeMap={}" + JSONObject.toJSONString(summaryClearTimeMap));
            for (String key : summaryClearTimeMap.keySet()) {
                String[] result = key.split(":");
                Integer plate = Integer.parseInt(result[2]);
                if (plateValue.equals(plate)) {
                    nextClearHour = Integer.parseInt(result[0]);
                    nextClearMinute = Integer.parseInt(result[1]);
                    break;
                }
            }

            // 延迟指定时间后触发清仓(补充10秒)
            Calendar calendar = Calendar.getInstance();
            int nowSecond = calendar.get(Calendar.SECOND);
            int diffHour = nextClearHour - clearHour;
            int diffMinute = nextClearMinute - clearMinute;
            diffHour = (diffHour <= 0) ? diffHour + 24 : diffHour;
            int delayTime = diffHour * 3600 + diffMinute * 60 - nowSecond + 10;
            LOGGER.info("nextClearHour = {},nextClearMinute = {},delayTime = {}", nextClearHour, nextClearMinute, delayTime);
            clearFuture = clearScheduler.schedule(new LoopClearTask(nextClearHour, nextClearMinute), delayTime, TimeUnit.SECONDS);
        }

    }

    /**
     * 启动清仓调度器
     */
    public void startupClearScheduler() {
        Integer plateValue = PlateEnum.getValueByName(plateName);
        Map<String, List<String>> summaryClearTimeMap = productTimeCache.getSummaryClearTimeMap();
        if (summaryClearTimeMap.isEmpty()) {
            LOGGER.error("{} 清仓时间汇总异常, 无法触发清仓操作...", this.getClass().getSimpleName());
            return;
        } else {
            for (Map.Entry<String, List<String>> entry : summaryClearTimeMap.entrySet()) {
                String timeKey = entry.getKey();
                Integer plate = Integer.parseInt(timeKey.split(":")[2]);
                if (plateValue.equals(plate)) {
                    List<String> products = entry.getValue();
                    for (String productName : products) {
                        LOGGER.info("【" + plateName + "】" + "{} 清仓时间汇总: {} ~ {}", this.getClass().getSimpleName(), timeKey, productName);
                    }
                }
            }
        }

        // 查找第一个清仓时间节点
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        int nowSecond = calendar.get(Calendar.SECOND);

        int clearHour = 0, clearMinute = 0;
        List<String> rollTimeKeyList = new ArrayList<String>();
        Iterator<String> iterator = summaryClearTimeMap.keySet().iterator();
        Boolean isHasClearTime = false;//是否获取到清仓时间点
        while (iterator.hasNext()) {
            String timeKey = iterator.next();
            String[] result = timeKey.split(":");
            clearHour = Integer.parseInt(result[0]);
            clearMinute = Integer.parseInt(result[1]);
            Integer plate = Integer.parseInt(result[2]);
            if (plateValue.equals(plate)) {
                if ((clearHour * 60 + clearMinute) > (nowHour * 60 + nowMinute)) {
                    isHasClearTime = true;
                    LOGGER.info("获取第一个清仓时间节点: " + timeKey);
                    break;
                } else {
                    rollTimeKeyList.add(timeKey);
                }
            }
        }
        //获取到一个清仓时间点，将过滤掉的时间点放到队列尾
        if (isHasClearTime) {
            for (String rollTime : rollTimeKeyList) {
                List<String> productList = summaryClearTimeMap.get(rollTime);
                productTimeCache.removeSummaryClearTime(rollTime);
                productTimeCache.addSummaryClearTime(rollTime, productList);
            }
        } else {
            //未获取到清仓时间点，则取第一个
            for (String timeKey : summaryClearTimeMap.keySet()) {
                String[] result = timeKey.split(":");
                Integer plate = Integer.parseInt(result[2]);
                if (plateValue.equals(plate)) {
                    clearHour = Integer.parseInt(result[0]);
                    clearMinute = Integer.parseInt(result[1]);
                    break;
                }
            }

        }

        LOGGER.info("【" + plateName + "】排序后summaryClearTimeMap={}", JSONObject.toJSONString(summaryClearTimeMap));
        // 延迟指定时间后触发清仓(补充10秒)
        int diffHour = clearHour - nowHour;
        int diffMinute = clearMinute - nowMinute;
        diffHour = (diffHour < 0) ? diffHour + 24 : diffHour;
        int delayTime = diffHour * 3600 + diffMinute * 60 - nowSecond + 10;
        LOGGER.info("clearHour = {},clearMinute = {},delayTime = {}", clearHour, clearMinute, delayTime);
        clearFuture = clearScheduler.schedule(new LoopClearTask(clearHour, clearMinute), delayTime, TimeUnit.SECONDS);
        LOGGER.info(clearFuture.toString());
        LOGGER.info(clearScheduler.toString());
    }

    /**
     * 取消任务
     */
    public boolean cancelClearScheduler() {
        clearFuture.cancel(true);
        if (clearFuture.isCancelled()) {
            LOGGER.info("{} 取消清仓调度器待执行任务成功...", this.getClass().getSimpleName());
            return true;
        } else {
            LOGGER.info("{} 取消清仓调度器待执行任务失败...", this.getClass().getSimpleName());
            return false;
        }
    }

    /**
     * 获取清仓订单
     */
    public List<FutureOrderBean> getClearOrders(String productName) {
        return riskControl.getClearOrders(productName);
    }

    /**
     * 初始化风控队列
     */
    public void initRiskControlQueue() {
        riskControl.initRiskControlQueue();
    }

    /**
     * 新增风控商品(如: 更换合约)
     */
    public void addRiskControlProduct(String productName) {
        riskControl.addRiskControlProduct(productName);
    }

    /**
     * 移除风控商品
     */
    public void removeRiskControlProduct(String productName) {
        riskControl.removeRiskControlProduct(productName);
    }

    /**
     * 填充风控队列
     */
    public void fillRiskControlQueue(RiskControlBean riskControlBean) {
        riskControl.fillRiskControlQueue(riskControlBean);
    }

    /**
     * 移除风控订单
     */
    public boolean removeRiskControlOrder(RiskControlBean riskControlBean) {
        return riskControl.removeRiskControlOrder(riskControlBean);
    }

    /**
     * 更新风控订单(修改止盈止损)
     */
    public boolean updateRiskControlOrder(RiskControlBean riskBean) {
        return riskControl.updateRiskControlOrder(riskBean);
    }

    /**
     * 开启/关闭移动止损
     */
    public void switchMutableStopLossOrder(RiskControlBean riskBean) {
        riskControl.switchMutableStopLossOrder(riskBean);
    }

    /**
     * 更新递延订单
     */
    public boolean updateDeferredOrder(RiskControlBean riskBean) {
        return riskControl.updateDeferredOrder(riskBean);
    }

    /**
     * 获取风控订单(移动止损)
     */
    public MutableStopLossBean getMutableStopLossOrder(String productName, String uniqueOrderId) {
        return riskControl.getMutableStopLossOrder(productName, uniqueOrderId);
    }

    public void setCashOrderSellTradeService(IOrderSellTradeService orderSellTradeService) {
        this.cashOrderSellTradeService = orderSellTradeService;
    }

    public IOrderSellTradeService getCashOrderSellTradeService() {
        return cashOrderSellTradeService;
    }

    /**
     * 清仓操作
     *
     * @param orders
     */
    public void closeOrders(final List<FutureOrderBean> orders) {
        LOGGER.info("------------orders = {}", JSONObject.toJSONString(orders));
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (FutureOrderBean futureOrder : orders) {

                    OrderVo orderBean = new OrderVo();
                    orderBean.setOrderId(futureOrder.getUniqueOrderId());
                    orderBean.setSellTriggerType(futureOrder.getSellType());
                    orderBean.setSentinelPrice(futureOrder.getSentinelPrice());
                    if (futureOrder.getFundType() == LTConstants.FUND_TYPE_CASH) {
                        orderBean.setFundType(LTConstants.FUND_TYPE_CASH);
                        try {
                            cashOrderSellTradeService.doSell(orderBean);
                            LOGGER.info("风控平仓>> 账号: {}, 现金订单Id: {}, 商品: {}, 方向: {}, 数量: {},平仓类型：{}",
                                    futureOrder.getInvestorId(), futureOrder.getUniqueOrderId(),
                                    futureOrder.getProductName(), futureOrder.getDirect(), futureOrder.getAmount(), futureOrder.getSellType());
                        } catch (LTException e) {
                            LOGGER.error("---------LTException:{},futureOrder:{}--------", e, JSONObject.toJSONString(futureOrder));
                            continue;
                        } catch (Exception e) {
                            e.printStackTrace();
                            LOGGER.error("---------error:{},futureOrder:{}--------", e, JSONObject.toJSONString(futureOrder));
                            OrderCashInfo cashOrder = orderBusinessService.findOrderCashInfoByOrderId(futureOrder.getUniqueOrderId());
                            if (StringTools.isNotEmpty(cashOrder)) {
                                //风控触发失败，删除卖出标志
                                TradeUtils.delKeyLockSell(redisTemplate, cashOrder.getOrderId());
                                RiskControlBean riskBean = new RiskControlBean();
                                riskBean.setProductName(cashOrder.getProductCode());
                                riskBean.setUniqueOrderId(cashOrder.getOrderId());
                                riskBean.setDirect(futureOrder.getDirect());
                                riskBean.setMatchPrice(cashOrder.getBuyAvgPrice() == null ? 0.0 : cashOrder.getBuyAvgPrice());
                                riskBean.setStopGainPrice(cashOrder.getStopProfitPrice());
                                riskBean.setStopLossPrice(cashOrder.getStopLossPrice());
                                riskBean.setMutableStopLoss(cashOrder.getTrailStopLoss() == LTConstants.MUTABLE_STOPLOSS_OPEN);
                                riskBean.setDeferredOrderTimeStamp(cashOrder.getSysSetSellDate().getTime());
                                FutureOrderBean oBean = new FutureOrderBean(cashOrder.getUserId(), cashOrder.getSecurityCode(), cashOrder.getExchangeCode(), cashOrder.getProductCode(),
                                        cashOrder.getOrderId(), 888888, cashOrder.getBuyEntrustCount(), cashOrder.getBuyAvgPrice() == null ? 0.0 : cashOrder.getBuyAvgPrice(),
                                        futureOrder.getDirect(), LTConstants.TRADE_OFFSET_CLOSE, LTConstants.TRADE_ORDER_TYPE_MARKET, LTConstants.FUND_TYPE_CASH,
                                        cashOrder.getCreateDate().getTime());
                                riskBean.setOrderBean(oBean);
                                riskControl.fillRiskControlQueue(riskBean);
                            }

                        }
                    }


                }
            }
        });
    }

    /**
     * 普通用户报单
     */
    public void produceNormalFutureOrders(FutureOrderBean futureOrder) {
        try {
            String productName = futureOrder.getProductName();
            Boolean isExchangeTradingTime = productTimeCache.getIsExchangeTradingTime(productName);
            if (isExchangeTradingTime) {
                LOGGER.info("===========普通用户报单=============");
                futureOrderQueue.put(futureOrder);
            } else {
                FutureOrderWrapper orderWrapper = new FutureOrderWrapper();
                orderWrapper.CMDID = LTConstants.FRAME_TYPE_ORDER_INSERT_REQ;
                orderWrapper.DATA = futureOrder;
                orderInsertFailure(JSON.toJSONString(orderWrapper));
                LOGGER.error("报单失败: {}, 非交易时间段, 已经休市...", futureOrder.getProductName());
            }
        } catch (Exception e) {
            LOGGER.error("添加普通订单出错, 异常信息: " + e.getMessage());
        }
    }


    // 风控/清仓报单
    public void produceSpecialFutureOrders(FutureOrderBean futureOrder) {
        try {
            futureOrderQueue.put(futureOrder);
        } catch (Exception e) {
            LOGGER.error("添加风控/清仓订单出错, 异常信息: " + e.getMessage());
        }
    }

    /**
     * 获取行情数据
     */
    public ProductPriceBean getQuotePrice(String productName) {
        return riskControl.getQuotePrice(productName);
    }

    /**
     * 报单成功
     */
    public abstract void orderInsertSuccess(String result);

    /**
     * 报单失败
     */
    public abstract void orderInsertFailure(String result);

    /**
     * 计算报单价格
     */
    public abstract void fillOrderInsertParams(FutureOrderBean orderBean);

    /**
     * 清仓
     */
    public void clear(final List<String> productNames) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (String productName : productNames) {
                    LOGGER.info("开始清仓: " + productName);
                    List<FutureOrderBean> orders = null;
                    try {
                        orders = getClearOrders(productName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("商品:{}清仓失败,e={}", productName, e);
                        continue;
                    }

                    if (orders == null) {
                        LOGGER.info(productName + "无待清仓订单");
                        continue;
                    }
                    for (FutureOrderBean order : orders) {
                        try {

                            if (!StringTools.isNotEmpty(order.getUniqueOrderId())) {
                                continue;
                            }
                            if (!StringTools.isNotEmpty(order.getFundType())) {
                                continue;
                            }
                            OrderVo orderBean = new OrderVo();
                            orderBean.setOrderId(order.getUniqueOrderId());
                            orderBean.setSellTriggerType(SellTriggerTypeEnum.CLEARANCE.getValue());
                            if (order.getFundType() == LTConstants.FUND_TYPE_CASH) {
                                orderBean.setFundType(LTConstants.FUND_TYPE_CASH);
                                getCashOrderSellTradeService().doSell(orderBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LOGGER.error("清仓异常 e = {}", e);
                            continue;
                        }
                    }
                }


            }
        });
    }

    /**
     * 加载运行时数据
     *
     * @throws Exception
     */
    public abstract void loadRuntimeData() throws Exception;

    /**
     * 管理商品信息(新增商品/更换合约等)
     */
    public boolean manageProducts(String newProductName, String oldProductName) {
        // 取消清仓任务
        if (!cancelClearScheduler()) {
            return false;
        }

        if (oldProductName != null) {
            removeRiskControlProduct(oldProductName);
        }

        if (newProductName != null) {
            addRiskControlProduct(newProductName);
        }

        // 重启清仓调度器
        startupClearScheduler();
        LOGGER.info("newProductName = {},oldProductName = {} 重启清仓调度器 ", newProductName, oldProductName);
        return true;
    }

    @Override
    public void run() {
        // fixme: 循环显式退出?
        for (; ; ) {
            try {
                // 订单信息
                FutureOrderBean futureOrder = futureOrderQueue.take();
                String productName = futureOrder.getProductName();

                // 用户/风控/清仓平仓前需先移除风控订单

                if (futureOrder.getOffset() != LTConstants.TRADE_OFFSET_OPEN) {
                    RiskControlBean riskControlBean = new RiskControlBean();
                    riskControlBean.setProductName(productName);
                    riskControlBean.setUniqueOrderId(futureOrder.getUniqueOrderId());
                    // 多交易服务下 有可能出现风控没有被清除
                    removeRiskControlOrder(riskControlBean);
                }

                // 填充报单参数
                fillOrderInsertParams(futureOrder);
                LOGGER.info("开始报单, 账户: {}, 商品:{}, 订单id: {}, 数量: {}, 价格: {}, 方向: {}, 开平: {}, 类型: {}",
                        futureOrder.getInvestorId(), futureOrder.getProductName(), futureOrder.getOrderInsertId(),
                        futureOrder.getAmount(), futureOrder.getOrderPrice(), futureOrder.getDirect(), futureOrder.getOffset(), futureOrder.getOrderType());

                // 发送订单
                FutureOrderWrapper orderWrapper = new FutureOrderWrapper();
                orderWrapper.CMDID = LTConstants.FRAME_TYPE_ORDER_INSERT_REQ;
                orderWrapper.DATA = futureOrder;
                String msg = JSON.toJSONString(orderWrapper);
                BaseClient client = clientMap.get(futureOrder.getInvestorId());
                if (client != null) {
                    client.sendMessage(msg, new ResultListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            // 报单成功
                            orderInsertSuccess(result);
                        }

                        @Override
                        public void onFailure(String result) {
                            // 报单失败
                            orderInsertFailure(result);
                        }
                    });
                } else {
                    LOGGER.info("报单出错：订单{}未找到连接c++交易模块通道", futureOrder.getUniqueOrderId());
                }

            } catch (Exception e) {
                LOGGER.error("报单出错, 异常信息: " + e.getMessage());
            }
        }
    }

    private class LoopPerRiskTask implements Runnable {

        @Override
        public void run() {
            LOGGER.info("......【" + plateName + "】执行每分钟加载订单到风控中...." + new Date() + "");
            loadAllCashOrdersFromDB(PlateEnum.getValueByName(plateName));
        }

    }

    /**
     * 启动时初始化每分钟加载订单到风控中的定时器
     */
    public void startPerRiskSchedule() {
        LOGGER.info("......启动每分钟加载订单到风控中的定时器...." + new Date() + "");
        perRiskScheduler.scheduleAtFixedRate(new LoopPerRiskTask(), 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 启动时初始化每分钟加载清仓时间
     */
    public void startClearTimeSchedule() {
        try{
            LOGGER.info("【" + plateName + "】强制清仓启动");
            if (clearFuture != null && cancelClearScheduler()) {
                //productTimeCache.setExchangeTradingTime(true);
                startupClearScheduler();
                LOGGER.info("【" + plateName + "】强制清仓结束");
            }
        }
        catch (Exception e){
            LOGGER.info("【" + plateName + "】强制清仓错误,e={}", e);
        }

    }

    public RiskControlServer getRiskControl() {
        return riskControl;
    }

    /**
     * 释放资源
     */
    public void release() {
        clearScheduler.shutdown();
        executorService.shutdown();
        perRiskScheduler.shutdown();
    }

    /**
     * 报单错误消息
     */
    public MessageHandler errorMessageHandler = new MessageHandler() {
        @Override
        public void process(String message) {
            FutureErrorBean errorBean = JSON.parseObject(message, FutureErrorBean.class);
            FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
            matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ERROR);
            matchWrapper.setBaseMatchBean(errorBean);

            // 存入外盘委托回执队列
            persistExecutor.put(matchWrapper);

            LOGGER.info("订单异常: " + ErrorCode.getOuterErrcodeDesc(errorBean.getErrorId()) + " 详细: " + message);
        }
    };

    /**
     * 报单成功信息
     */
    public MessageHandler orderInsertMessageHandler = new MessageHandler() {
        @Override
        public void process(String message) {
            FutureErrorBean errorBean = JSON.parseObject(message, FutureErrorBean.class);
            FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
            matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ORDER_INSERT_RSP);
            matchWrapper.setBaseMatchBean(errorBean);

            // 存入外盘委托回执队列
            persistExecutor.put(matchWrapper);

            LOGGER.info("报单成功: " + message);
        }
    };

    /**
     * 部分成交信息
     */
    public MessageHandler orderStatusMessageHandler = new MessageHandler() {
        @Override
        public void process(String message) {
            final FutureMatchBean matchBean = JSON.parseObject(message, FutureMatchBean.class);
            FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
            matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ORDER_STATUS);
            matchWrapper.setBaseMatchBean(matchBean);

            //存入外盘委托回执队列
            persistExecutor.put(matchWrapper);

            LOGGER.info("部分成交>> 账号: {}, 订单Id: {}, 商品: {}, 方向: {}, 开平: {}, " +
                            "成交量: {}, 已成交量: {}, 委托数量: {}, 成交价: {}, 成交类型: {}, 委托Id: {},  成交Id: {}, 成交时间: {}",
                    matchBean.getInvestorId(), matchBean.getPlatformId(), matchBean.getProductName(), matchBean.getDirect(), matchBean.getOffset(),
                    matchBean.getMatchVol(), matchBean.getMatchTotal(), matchBean.getOrderTotal(), matchBean.getMatchPrice(), matchBean.getMatchSelf(),
                    matchBean.getSysOrderId(), matchBean.getSysMatchId(), matchBean.getMatchDateTime());
        }
    };


    /**
     * 完全成交信息
     */
    public MessageHandler orderMatchMessageHandler = new MessageHandler() {
        @Override
        public void process(String message) {
            final FutureMatchBean matchBean = JSON.parseObject(message, FutureMatchBean.class);
            FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
            matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ORDER_MATCH);
            matchWrapper.setBaseMatchBean(matchBean);

            //存入外盘委托回执队列
            persistExecutor.put(matchWrapper);

            LOGGER.info("完全成交>> 账号: {}, 订单Id: {}, 商品: {}, 方向: {}, 开平: {}, " +
                            "成交量: {}, 已成交量: {}, 委托数量: {}, 成交价: {}, 成交类型: {}, 委托Id: {},  成交Id: {}, 成交时间: {}",
                    matchBean.getInvestorId(), matchBean.getPlatformId(), matchBean.getProductName(), matchBean.getDirect(), matchBean.getOffset(),
                    matchBean.getMatchVol(), matchBean.getMatchTotal(), matchBean.getOrderTotal(), matchBean.getMatchPrice(), matchBean.getMatchSelf(),
                    matchBean.getSysOrderId(), matchBean.getSysMatchId(), matchBean.getMatchDateTime());
        }
    };

}
