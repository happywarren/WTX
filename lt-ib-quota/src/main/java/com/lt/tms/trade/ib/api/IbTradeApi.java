package com.lt.tms.trade.ib.api;

import com.lt.tms.comm.json.FastJsonUtils;
import com.lt.tms.library.ib.client.*;
import com.lt.tms.tcp.server.TcpClient;
import com.lt.tms.trade.bean.TradeBean;
import com.lt.tms.trade.bean.TradeResBean;
import com.lt.tms.trade.constant.TradeConstants;
import com.lt.tms.trade.ib.api.bean.IbTradeBean;
import com.lt.tms.trade.ib.config.IbTradeConfig;
import com.lt.tms.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class IbTradeApi {

    private Logger logger = LoggerFactory.getLogger(IbTradeApi.class);

    private EClientSocket client;
    private EReaderSignal signal;

    //线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Map<Integer, TradeBean> orderSessionMap = new ConcurrentHashMap<Integer, TradeBean>();

    @Autowired
    private IbTradeConfig ibTradeConfig;
    @Autowired
    private IbTradeWrapper ibTradeWrapper;
    @Autowired
    private TcpClient tcpClient;

    public IbTradeApi() {

    }

    @PostConstruct
    public void init() {
        if (ibTradeConfig.isStartup()) {
            client = ibTradeWrapper.getClientSocket();
            signal = ibTradeWrapper.getReaderSignal();
            client.eConnect(ibTradeConfig.getHost(), ibTradeConfig.getPort(), ibTradeConfig.getClientId());
            startupCallbackThread();
            startupTradeDataThread();
        }
    }

    // 交易数据发送线程
    private void startupTradeDataThread() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        IbTradeBean ibTradeBean = ibTradeWrapper.getOrderDataQueue().take();
                        TradeBean tradeBean = orderSessionMap.get(ibTradeBean.getOrderId());
                        //处理订单状态
                        TradeResBean tradeResBean = new TradeResBean();
                        tradeResBean.setTradeId(tradeBean.getTradeId());
                        tradeResBean.setProductCode(tradeBean.getProductCode());
                        tradeResBean.setDirect(tradeBean.getDirect());
                        tradeResBean.setOffset(tradeBean.getOffset());
                        tradeResBean.setSysOrderId(ibTradeBean.getSysOrderId());
                        tradeResBean.setNumber(tradeBean.getNumber());
                        tradeResBean.setSuccessNumber(ibTradeBean.getSuccessNumber());
                        tradeResBean.setOverNumber(ibTradeBean.getOverNumber());
                        tradeResBean.setAvgPrice(ibTradeBean.getAvgPrice());
                        tradeResBean.setLastPrice(ibTradeBean.getLastPrice());
                        String status = ibTradeBean.getStatus();
                        //TODO 状态转换
                        tradeResBean.setStatus(1);

                        tcpClient.sendTradeInfo(tradeResBean);
                    } catch (Exception e) {
                        logger.error("交易数据发送出错, 异常信息: " + e.getMessage());
                    }
                }
            }
        });
    }

    // 启动回调线程
    private void startupCallbackThread() {
        final EReader reader = new EReader(client, signal);
        reader.start();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (client.isConnected()) {
                    signal.waitForSignal();
                    try {
                        reader.processMsgs();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Exception: " + e.getMessage());
                    }
                }
            }
        });
    }

    private Contract contract(String productCode) {
        Contract contract = new Contract();

        return contract;
    }

    // 释放资源
    @PreDestroy
    public void release() {
        executorService.shutdown();
    }

    public boolean isConnected() {
        if (Utils.isNotEmpty(client)) {
            return client.isConnected();
        }
        return false;
    }

    public void trade(TradeBean tradeBean) {
        logger.info("ib报单: {} ", FastJsonUtils.toJson(tradeBean));
        int orderId = ibTradeWrapper.getNextOrderId();
        if (orderSessionMap.containsKey(orderId)) {
            logger.error("报单请求, orderId异常...");
        } else {
            orderSessionMap.put(orderId, tradeBean);
            Contract contract = contract(tradeBean.getProductCode());
            // 订单
            Order order = new Order();

            int direct = tradeBean.getDirect();
            int offset = tradeBean.getOffset();

            //TRADE_DIRECT_BUY = 66;// 买: 'B'(看多)
            //TRADE_DIRECT_SELL = 83;// 卖: 'S'(看空)

            //TRADE_OFFSET_OPEN = 79;// 开仓: 'O' 79
            //TRADE_OFFSET_CLOSE = 67;// 平仓: 'C' 67

            if (offset == TradeConstants.TRADE_OFFSET_CLOSE && direct == TradeConstants.TRADE_DIRECT_SELL) {
                order.action("SELL");
            }
            if (offset == TradeConstants.TRADE_OFFSET_CLOSE && direct == TradeConstants.TRADE_DIRECT_BUY) {
                order.action("BUY");
            }
            if (offset == TradeConstants.TRADE_OFFSET_OPEN && direct == TradeConstants.TRADE_DIRECT_SELL) {
                order.action("SELL");
            }
            if (offset == TradeConstants.TRADE_OFFSET_OPEN && direct == TradeConstants.TRADE_DIRECT_BUY) {
                order.action("BUY");
            }
            if (tradeBean.getOrderType() == TradeConstants.TRADE_ORDER_TYPE_LIMIT) {
                order.orderType("LMT");
                order.lmtPrice(tradeBean.getOrderPrice());
                order.auxPrice(0);
            } else if (tradeBean.getOrderType() == TradeConstants.TRADE_ORDER_TYPE_MARKET) {
                order.orderType("MKT");
                order.lmtPrice(0);
                order.auxPrice(0);
            }
            order.triggerMethod(0);
            order.totalQuantity(tradeBean.getNumber());
            order.transmit(true);
            // 报单
            client.placeOrder(orderId, contract, order);
        }
    }
}
