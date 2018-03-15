package com.lt.trade.tradeserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.enums.trade.PlateEnum;
import com.lt.trade.tradeserver.bean.*;
import com.lt.trade.utils.LTConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
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
                LOGGER.info("外盘合约-虚拟报单成功 返回信息 result : {}", result);
                JSONObject jsonData = JSON.parseObject(result);
                String message = jsonData.getString("DATA");

                FutureOrderBean orderBean = JSON.parseObject(message, FutureOrderBean.class);

                FutureMatchBean matchBean = new FutureMatchBean();
                BeanUtils.copyProperties(orderBean, matchBean);
                matchBean.setMatchDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

                //取当前行情价
                ProductPriceBean productPriceBean = getQuotePrice(orderBean.getProductName());
                double quotePrice = productPriceBean.getLastPrice();
                int direct = orderBean.getDirect();

                if (LTConstants.TRADE_OFFSET_OPEN == orderBean.getOffset()) {
                    //开空
                    if (direct == LTConstants.TRADE_DIRECT_SELL) {
                        quotePrice = productPriceBean.getBidPrice();
                        LOGGER.info("----------开空quotePrice：{}------", quotePrice);
                        //开多
                    } else if (direct == LTConstants.TRADE_DIRECT_BUY) {
                        quotePrice = productPriceBean.getAskPrice();
                        LOGGER.info("----------开多quotePrice：{}------", quotePrice);
                    }
                } else if (LTConstants.TRADE_OFFSET_CLOSE  == orderBean.getOffset()) {
                    //平空
                    if (direct == LTConstants.TRADE_DIRECT_SELL) {
                        quotePrice = productPriceBean.getBidPrice();
                        LOGGER.info("----------平空quotePrice：{}------", quotePrice);
                        //平多
                    } else if (direct == LTConstants.TRADE_DIRECT_BUY) {
                        quotePrice = productPriceBean.getAskPrice();
                        LOGGER.info("----------平多quotePrice：{}------", quotePrice);
                    }
                }


                // String quotaTime =  productPriceBean.getQuotaTime();
                LOGGER.info("......外盘开始报单最后依次行情时间:{}.....",productPriceBean.getQuotaTime());
                //15S 无行情就认为没有行情 行情时间超过10S就认为没有行情
                if(quotePrice <= 0.00000001 || (System.currentTimeMillis()-Long.parseLong(productPriceBean.getQuotaTime())) > 25000  ){
                    LOGGER.info("报单失败！！！,行情获取失败.");
                    //获取不到行情价格 保单失败
                    FutureErrorBean errorBean = new FutureErrorBean();
                    errorBean.setErrorId(5889);
                    errorBean.setErrorMsg("行情获取失败");
                    errorBean.setFundType(orderBean.getFundType());
                    errorBean.setPlatformId(orderBean.getOrderInsertId());
                    FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
                    matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ERROR);
                    matchWrapper.setBaseMatchBean(errorBean);
                    persistExecutor.put(matchWrapper);
                    return;
                }
                matchBean.setMatchPrice(quotePrice);
                matchBean.setMatchSelf(0);
                matchBean.setMatchTotal(orderBean.getAmount());
                matchBean.setMatchVol(orderBean.getAmount());
                matchBean.setOrderTotal(orderBean.getAmount());
                matchBean.setPlatformId(orderBean.getOrderInsertId());
                matchBean.setSysMatchId(orderBean.getUniqueOrderId());
                matchBean.setSysOrderId(String.valueOf(orderBean.getOrderInsertId()));

                FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
                matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ORDER_MATCH);
                matchWrapper.setBaseMatchBean(matchBean);

                //存入外盘合约委托成功消息
                FutureErrorBean futureErrorBean = new FutureErrorBean();
                futureErrorBean.setErrorId(0);
                futureErrorBean.setErrorMsg("报单成功");
                futureErrorBean.setFundType(orderBean.getFundType());
                futureErrorBean.setPlatformId(orderBean.getOrderInsertId());

                FutureMatchWrapper matchWrapperStatus = new FutureMatchWrapper();
                matchWrapperStatus.setBaseMatchBean(futureErrorBean);
                matchWrapperStatus.setMessageId(LTConstants.FRAME_TYPE_ORDER_INSERT_RSP);

                //将报单成功的消息存入外盘委托队列
                persistExecutor.put(matchWrapperStatus);
                //存入外盘合约委托回执队列
                persistExecutor.put(matchWrapper);

                LOGGER.info("外盘合约-虚拟报单完全成交>> 账号: {}, 订单Id: {}, 商品: {}, 方向: {}, 开平: {}, " +
                                "成交量: {}, 已成交量: {}, 委托数量: {}, 成交价: {}, 成交类型: {}, 委托Id: {},  成交Id: {}, 成交时间: {}",
                        matchBean.getInvestorId(), matchBean.getPlatformId(), matchBean.getProductName(), matchBean.getDirect(), matchBean.getOffset(),
                        matchBean.getMatchVol(), matchBean.getMatchTotal(), matchBean.getOrderTotal(), matchBean.getMatchPrice(), matchBean.getMatchSelf(),
                        matchBean.getSysOrderId(), matchBean.getSysMatchId(), matchBean.getMatchDateTime());
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
