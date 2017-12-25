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

/**
 * 差价合约交易
 * <p>
 * Created by sunch on 2016/11/10.
 */
public class ContractTrade extends BaseTrade {

    private static Logger LOGGER = LoggerFactory.getLogger(ContractTrade.class);

    public ContractTrade() {
        super(PlateEnum.CONTRACT_FOR_DIFFERENCE.getName());
        addMessageHandler(LTConstants.FRAME_TYPE_ERROR, errorMessageHandler);
        addMessageHandler(LTConstants.FRAME_TYPE_ORDER_STATUS, orderStatusMessageHandler);
        addMessageHandler(LTConstants.FRAME_TYPE_ORDER_MATCH, orderMatchMessageHandler);
        addMessageHandler(LTConstants.FRAME_TYPE_ORDER_INSERT_RSP, orderInsertMessageHandler);
    }

    // 加载运行时数据(数据库数据及配置信息)
    @Override
    public void loadRuntimeData() throws Exception {
        initRiskControlQueue();

        // 加载订单数据, 并填充至风控队列
        if (!loadAllOrdersFromDB(PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue())) {
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
                LOGGER.info("差价合约-虚拟报单成功 返回信息 result : {}", result);
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

                //开仓
                if (LTConstants.TRADE_OFFSET_OPEN == orderBean.getOffset()) {
                    //看空
                    if (direct == LTConstants.TRADE_DIRECT_SELL) {
                        quotePrice = productPriceBean.getBidPrice();
                        //看多
                    } else if (direct == LTConstants.TRADE_DIRECT_BUY) {
                        quotePrice = productPriceBean.getAskPrice();
                    }
                    //平仓
                } else if (LTConstants.TRADE_OFFSET_CLOSE  == orderBean.getOffset()) {
                    //看空
                    if (direct == LTConstants.TRADE_DIRECT_SELL) {
                        quotePrice = productPriceBean.getAskPrice();
                        //看多
                    } else if (direct == LTConstants.TRADE_DIRECT_BUY) {
                        quotePrice = productPriceBean.getBidPrice();
                    }
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

                //存入差价合约委托回执队列
                persistExecutor.put(matchWrapper);

                LOGGER.info("差价合约-虚拟报单完全成交>> 账号: {}, 订单Id: {}, 商品: {}, 方向: {}, 开平: {}, " +
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
                LOGGER.info("差价合约-虚拟报单失败 返回信息 result : {}", result);
                JSONObject jsonData = JSON.parseObject(result);
                String message = jsonData.getString("DATA");

                FutureOrderBean orderBean = JSON.parseObject(message, FutureOrderBean.class);
                FutureErrorBean errorBean = new FutureErrorBean();
                errorBean.setErrorMsg("差价合约-虚拟报单失败, 通讯异常或非交易时间段");
                errorBean.setPlatformId(orderBean.getOrderInsertId());
                errorBean.setFundType(orderBean.getFundType());
                FutureMatchWrapper matchWrapper = new FutureMatchWrapper();
                matchWrapper.setMessageId(LTConstants.FRAME_TYPE_ERROR);
                matchWrapper.setBaseMatchBean(errorBean);

                // 存入差价合约委托回执队列
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
            LOGGER.info("===========填充报单参数,限价=offsetPrice={}===================", offsetPrice);
            // 限价
            if (orderBean.getDirect() == LTConstants.TRADE_DIRECT_BUY) {
                orderPrice += offsetPrice;
            } else {
                orderPrice -= offsetPrice;
            }
            orderBean.setOrderType(LTConstants.TRADE_ORDER_TYPE_LIMIT);
        } else {
            LOGGER.info("===========填充报单参数,市价=offsetPrice={}===================", offsetPrice);
            // 市价
            orderBean.setOrderType(LTConstants.TRADE_ORDER_TYPE_MARKET);
        }
        LOGGER.info("===========填充报单参数,quotePrice={},orderPrice={}===================", quotePrice, orderPrice);
        orderBean.setQuotePrice(quotePrice);
        orderBean.setOrderPrice(orderPrice);
        orderBean.setTimeStamp(System.currentTimeMillis());
    }

    @Override
    public void produceNormalFutureOrders(FutureOrderBean futureOrder) {
        try {
            String productName = futureOrder.getProductName();
            Boolean isExchangeTradingTime = productTimeCache.getIsExchangeTradingTime(productName);
            if (isExchangeTradingTime) {
                FutureOrderWrapper orderWrapper = new FutureOrderWrapper();
                orderWrapper.CMDID = LTConstants.FRAME_TYPE_ORDER_MATCH;
                orderWrapper.DATA = futureOrder;
                orderInsertSuccess(JSON.toJSONString(orderWrapper));
                LOGGER.error("报单成功: {}", productName);
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


    /**
     * 风控/清仓报单
     */
    @Override
    public void produceSpecialFutureOrders(FutureOrderBean futureOrder) {
        try {
            FutureOrderWrapper orderWrapper = new FutureOrderWrapper();
            orderWrapper.CMDID = LTConstants.FRAME_TYPE_ORDER_MATCH;
            orderWrapper.DATA = futureOrder;
            orderInsertSuccess(JSON.toJSONString(orderWrapper));
            LOGGER.error("风控/清仓报单成功: {}", futureOrder.getProductName());
        } catch (Exception e) {
            LOGGER.error("添加风控/清仓订单出错, 异常信息: " + e.getMessage());
        }
    }
}
