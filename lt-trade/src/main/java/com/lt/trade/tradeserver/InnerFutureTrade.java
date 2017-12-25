package com.lt.trade.tradeserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.enums.trade.PlateEnum;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchWrapper;
import com.lt.trade.tradeserver.bean.FutureOrderBean;
import com.lt.trade.utils.LTConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * 内盘期货交易
 * <p>
 * Created by sunch on 2016/11/10.
 */
public class InnerFutureTrade extends BaseTrade {

    private static Logger LOGGER = LoggerFactory.getLogger(InnerFutureTrade.class);
    /**
     * 平仓类型
     */
    final String SHFE = "SHFE";


    public InnerFutureTrade() {
        super(PlateEnum.INNER_PLATE.getName());
        addMessageHandler(LTConstants.FRAME_TYPE_ERROR, errorMessageHandler);
        addMessageHandler(LTConstants.FRAME_TYPE_ORDER_STATUS, orderStatusMessageHandler);
        addMessageHandler(LTConstants.FRAME_TYPE_ORDER_MATCH, orderMatchMessageHandler);
        addMessageHandler(LTConstants.FRAME_TYPE_ORDER_INSERT_RSP, orderInsertMessageHandler);
    }


    /**
     * 加载运行时数据(数据库数据及配置信息)
     */
    @Override
    public void loadRuntimeData() throws Exception {
        initRiskControlQueue();

        // 加载订单数据, 并填充至风控队列
        if (!loadAllCashOrdersFromDB(PlateEnum.INNER_PLATE.getValue())) {
            throw new Exception("加载持仓订单失败...");
        }

        //启动每分钟加载订单到风控中定时器
        startPerRiskSchedule();

    }

    @Override
    public void orderInsertSuccess(final String result) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                LOGGER.info(result);
            }
        });
    }

    @Override
    public void orderInsertFailure(final String result) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("内盘报单失败 返回信息 result : {}", result);
                JSONObject jsonData = JSON.parseObject(result);
                String message = jsonData.getString("DATA");

                FutureOrderBean orderBean = JSON.parseObject(message, FutureOrderBean.class);
                FutureErrorBean errorBean = new FutureErrorBean();
                errorBean.setErrorId(5889);
                errorBean.setErrorMsg("报单失败, 通讯异常或非交易时间段");
                errorBean.setPlatformId(orderBean.getOrderInsertId());
                errorBean.setFundType(orderBean.getFundType());
                FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
                matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ERROR);
                matchWrapper.setBaseMatchBean(errorBean);

                // 存入内盘委托回执队列
                persistExecutor.put(matchWrapper);
            }
        });
    }

    // 填充报单参数
    @Override
    public void fillOrderInsertParams(FutureOrderBean orderBean) {
        double quotePrice = getQuotePrice(orderBean.getProductName()).getLastPrice();
        double orderPrice = quotePrice;
        double offsetPrice = orderBean.getOrderPrice();

        if (offsetPrice > 0.0) {
            // 限价
            if (orderBean.getDirect() == LTConstants.TRADE_DIRECT_BUY) {
                orderPrice += offsetPrice;
            } else {
                orderPrice -= offsetPrice;
            }
            orderBean.setOrderType(LTConstants.TRADE_ORDER_TYPE_LIMIT);
        } else {
            // 市价
            orderBean.setOrderType(LTConstants.TRADE_ORDER_TYPE_MARKET);
        }

        orderBean.setQuotePrice(quotePrice);
        orderBean.setOrderPrice(orderPrice);

        // 平仓类型
        if (orderBean.getExchangeNo().equals(SHFE) && orderBean.getOffset() == LTConstants.TRADE_OFFSET_CLOSE) {
            Calendar calendar = Calendar.getInstance();
            int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
            long matchTimeStamp = orderBean.getTimeStamp();
            long currentTimeStamp = System.currentTimeMillis();
            long offset = 0;
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                offset = 48 * 60 * 60 * 1000;
            }

            long dayTimeThreshold0 = 15 * 60 * 60 * 1000 + offset;
            long dayTimeThreshold1 = 20 * 60 * 60 * 1000 + offset;
            long nightThreshold = 6 * 60 * 60 * 1000;

            // 默认报平仓, 满足下面条件报平今
            if (nowHour >= 9 && nowHour <= 11) {
                // 白盘09:00~11:30平仓操作
                if (currentTimeStamp - matchTimeStamp < dayTimeThreshold0) {
                    orderBean.setOffset(LTConstants.TRADE_OFFSET_CLOSE_TODAY);
                }
            } else if (nowHour >= 13 && nowHour <= 15) {
                // 白盘13:30~15:00平仓操作
                if (currentTimeStamp - matchTimeStamp < dayTimeThreshold1) {
                    orderBean.setOffset(LTConstants.TRADE_OFFSET_CLOSE_TODAY);
                }
            } else if (nowHour >= 21 || nowHour <= 2) {
                // 夜盘21:00~02:30平仓操作
                if (currentTimeStamp - matchTimeStamp < nightThreshold) {
                    orderBean.setOffset(LTConstants.TRADE_OFFSET_CLOSE_TODAY);
                }
            }
        }// if

        // 报单时间戳
        orderBean.setTimeStamp(System.currentTimeMillis());
    }

}
