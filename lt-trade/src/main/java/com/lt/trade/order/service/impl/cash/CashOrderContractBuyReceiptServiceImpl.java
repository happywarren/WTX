/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderBuyServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.service.impl.cash;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.trade.OrderCashSuccessInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.dao.*;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import com.lt.trade.tradeserver.bean.DigitalProductBrandPosition;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 差价合约订单买入委托数据、回执数据处理
 */
@Service
public class CashOrderContractBuyReceiptServiceImpl extends AbstractCashOrderBuyReceiptService {

    private static final long serialVersionUID = 1050229096357327075L;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(CashOrderContractBuyReceiptServiceImpl.class);

    /**
     * 订单基本信息数据接口
     */
    @Autowired
    private OrderCashInfoDao orderCashInfoDao;
    @Autowired
    private OrderCashEntrustInfoDao orderCashEntrustInfoDao;
    /**
     * 成交结果数据接口
     */
    @Autowired
    private OrderCashSuccessInfoDao orderCashSuccessInfoDao;
    /**
     * 资金分布式接口: 资金扣款、结算、退款业务接口
     */
    @Autowired
    private IFundTradeApiService fundTradeApiService;
    /**
     * 交易服务
     */
    @Autowired
    private TradeServer tradeServer;
    @Autowired
    private IProductInfoService productInfoServiceImpl;
    @Autowired
    private IInvestorFeeCfgApiService investorFeeCfgApiService;

    @Autowired
    private DigitalCoinPositionCountManageDao positionCountManageDao;

    @Autowired
    private DigitalProductBrandPositionManageDao brandPositionManageDao;

    @Resource(name = "sendOrderMsgProducer")
    private MessageQueueProducer sendOrderMsgProducer;

    /**
     * 差价合约处理数据
     */
    @Override
    public void doPersist2003(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {

    }

    @Override
    public void doPersist2002(OrderCashEntrustInfo entrustRecord, int errorId) {
        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[2002]:差价合约开仓订单委托失败, orderId:{}, entrustId:{}", orderId, entrustRecord.getEntrustId());

        //订单基本信息
        OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
        if (cashOrdersInfo == null) {
            logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的差价合约现金订单数据!", orderId);
            return;
        }
        OrderCashInfo cashCache = cashOrdersInfo;

        try {
            OrderCashInfoCache.remove(orderId);
            logger.info("[2002]:已删除缓存中的差价合约订单主数据");
            //删除缓存中的委托单
            OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());
            logger.info("[2002]:删除缓存中的差价合约委托单");
            //移除超时处理
            TradeTimeOut.remove(orderId);
            //移除超时预警
            TradeTimeOut.earlyWarnEntrustMapRemove(orderId);

            //委托单修改时间
            Date date = new Date();
            entrustRecord.setModifyDate(date);

            //订单修改时间
            cashOrdersInfo.setModifyDate(date);

            //订单失败数量
            cashOrdersInfo.setBuyFailCount(entrustRecord.getEntrustCount());
            entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());

            //新增订单到数据库
            getOrderCashInfoDao().add(cashOrdersInfo);

            //新增委托信息到数据库
            getOrderCashEntrustInfoDao().add(entrustRecord);

            //退款
            this.refund(cashOrdersInfo, entrustRecord.getEntrustCount());
            logger.info("[2002]:开仓失败退款完成");

            //向APP客户端推送交易结果
            TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(), sendOrderMsgProducer);
        } catch (RuntimeException e) {
            e.printStackTrace();
            OrderCashInfoCache.put(cashCache);
            OrderCashEntrustInfoCache.put(entrustRecord);
            logger.error("事物回滚，内存回复   cashCache:{}", JSONObject.toJSON(cashCache));
        } finally {
        }

    }

    /**
     * 更新持仓数量、买入成功数量、买入均价、买入时间、添加成交记录<br/>
     * 买入失败时，记录买入失败数量、并退款
     */
    @Override
    public void doPersist2004(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {

        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[------------]:[差价合约]收到成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
        //移除超时处理
        TradeTimeOut.remove(orderId);
        //移除预警处理
        TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
        //订单基本信息
        OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
        if (cashOrdersInfo == null) {
            logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的差价合约现金订单数据!", orderId);
            return;
        }
        Date now = new Date();

        //从redis中获取产品
        ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());

        Double jumpPrice = productVo.getJumpValue();


        //更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
        cashOrdersInfo = this.fillBuyCashOrderCount(now, matchBean.getMatchPrice(),
                matchBean.getOrderTotal(), matchBean.getMatchVol(), matchBean.getMatchTotal(), cashOrdersInfo, jumpPrice, entrustRecord.getTradeType());

        //重新计算止盈价、止损价
        this.calculateStopLossProfitPrice(cashOrdersInfo, productVo.getDecimalDigits());

        cashOrdersInfo.setModifyDate(now);

        //成交记录实体组装
        OrderCashSuccessInfo orderCashSuccessInfo = new OrderCashSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
                entrustRecord.getProductId(), entrustRecord.getInvestorId(), entrustRecord.getAccountId(),
                entrustRecord.getProductCode(), entrustRecord.getProductName(),
                entrustRecord.getExchangeCode(), entrustRecord.getPlate(),
                entrustRecord.getSecurityCode(), entrustRecord.getTradeType(),
                entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(),
                entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(),
                matchBean.getMatchVol(), cashOrdersInfo.getBuySuccessCount(),
                matchBean.getSysMatchId(), matchBean.getSysOrderId(),
                matchBean.getMatchPrice(), cashOrdersInfo.getLastBuyDate(), now);
        //设置委托时间
        cashOrdersInfo.setEntrustBuyDate(now);

        //设置成交状态为完全成交
        orderCashSuccessInfo.setSuccessStatus(2);

        //委托实体入库
        addEntrustInfo(entrustRecord);

        //更新订单信息
        orderCashInfoDao.add(cashOrdersInfo);
        logger.info("[2004]:[差价合约]更新订单信息, orderId:{}", orderId);

        //新增成交记录
        orderCashSuccessInfoDao.add(orderCashSuccessInfo);
        logger.info("[2004]:[差价合约]新增成交记录, orderId:{}", orderId);

        //删除缓存中的买入委托单
        OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());


        //锁住单户持仓记录
        DigitalCoinPosition coinPosition = new DigitalCoinPosition();
        coinPosition.setUserId(cashOrdersInfo.getUserId());
        coinPosition.setProductCode(cashOrdersInfo.getProductCode());
        coinPosition.setInvestorId(cashOrdersInfo.getInvestorId());
        logger.info("cashOrdersInfo.getInvestorId()---------{}", cashOrdersInfo.getInvestorId());

        coinPosition = positionCountManageDao.selectCoinPositionForUpdate(coinPosition);

        if (coinPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看多
                coinPosition.setBuyCount(coinPosition.getBuyCount() + cashOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看空
                coinPosition.setSellCount(coinPosition.getSellCount() + cashOrdersInfo.getBuySuccessCount());
            }
            positionCountManageDao.updateCoinPosition(coinPosition);
            logger.info("[2004]:[差价合约]开仓，修改单户持仓记录, coinPosition:{}", coinPosition);

        } else {
            coinPosition = new DigitalCoinPosition();
            coinPosition.setUserId(cashOrdersInfo.getUserId());
            coinPosition.setProductCode(cashOrdersInfo.getProductCode());
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看多
                coinPosition.setBuyCount(cashOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看空
                coinPosition.setSellCount(cashOrdersInfo.getBuySuccessCount());
            }
            coinPosition.setInvestorId(cashOrdersInfo.getInvestorId());
            positionCountManageDao.insertCoinPosition(coinPosition);
            logger.info("[2004]:[差价合约]开仓，新增单户持仓记录, coinPosition:{}", coinPosition);

            //锁住持仓记录
            positionCountManageDao.selectCoinPositionForUpdate(coinPosition);
        }

        //锁住品牌持仓记录
        DigitalProductBrandPosition brandPosition = new DigitalProductBrandPosition();
        brandPosition.setBrandId(cashOrdersInfo.getBrandId());
        brandPosition.setProductCode(cashOrdersInfo.getProductCode());
        logger.info("brandPosition.getBrandId()---------{},brandPosition.getProductCode()---------{}",
                brandPosition.getBrandId(),brandPosition.getProductCode());

        brandPosition = brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);

        if (brandPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看多
                brandPosition.setBuyCount(brandPosition.getBuyCount() + cashOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看空
                brandPosition.setSellCount(brandPosition.getSellCount() + cashOrdersInfo.getBuySuccessCount());
            }
            brandPositionManageDao.updateCoinPosition(brandPosition);
            logger.info("[2004]:[差价合约]开仓，修改品牌持仓记录, brandPosition:{}", brandPosition);

        } else {
            brandPosition = new DigitalProductBrandPosition();
            brandPosition.setBrandId(cashOrdersInfo.getBrandId());
            brandPosition.setProductCode(cashOrdersInfo.getProductCode());
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看多
                brandPosition.setBuyCount(cashOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == cashOrdersInfo.getTradeDirection()) {
                //看空
                brandPosition.setSellCount(cashOrdersInfo.getBuySuccessCount());
            }
            brandPositionManageDao.insertCoinPosition(brandPosition);
            logger.info("[2004]:[差价合约]开仓，新增品牌持仓记录, brandPosition:{}", brandPosition);

            //锁住持仓记录
            brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);
        }

        //订单加入到风控监控队列
        this.fillRiskControlQueue(cashOrdersInfo);
        logger.info("[2004]:[差价合约]订单加入到风控监控队列, displayId:{}", orderId);

        //向APP客户端推送交易结果
        TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(), sendOrderMsgProducer);
    }

    @Override
    public void doPersist(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {

    }


    /**
     * 更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
     */
    public OrderCashInfo fillBuyCashOrderCount(Date lastBuyDate, Double matchPrice,
                                               Integer orderTotal, Integer matchVol, Integer matchTotal, OrderCashInfo orderCashInfo, Double jumpPrice, Integer tradeType) {
        //已买入成功手数、买入均价
        Integer buySuccCount = orderCashInfo.getBuySuccessCount();

        //计算买入均价、买入成功数量
        AvgPriceVo avgPriceVo = new AvgPriceVo(orderCashInfo.getBuyPriceTotal(), orderCashInfo.getBuyMinPrice(), buySuccCount, matchVol, matchPrice,
                orderCashInfo.getTradeDirection(), tradeType, jumpPrice);

        logger.info("=============买入均价，买入成功数量==avgPriceVo={}===========", JSONObject.toJSONString(avgPriceVo));

        orderCashInfo.setBuyMinPrice(avgPriceVo.getMinPrice());//最小价
        orderCashInfo.setBuyPriceTotal(avgPriceVo.getPriceTotal());//总价
        //更新买入时间
        orderCashInfo.setLastBuyDate(lastBuyDate);

        //更新已买入成功手数
        orderCashInfo.setBuySuccessCount(avgPriceVo.getCount());
        //更新买入均价
        orderCashInfo.setBuyAvgPrice(avgPriceVo.getAvgPrice());
        //更新持仓数量
        orderCashInfo.setHoldCount(orderCashInfo.getBuySuccessCount());

        //处理实扣字段
        this.dealActualParams(orderCashInfo, matchVol);

        logger.info("[差价合约]更新订单持仓, sysBuyAvgPrice:{}, buySuccCount:{}", avgPriceVo.getAvgPrice(), orderCashInfo.getBuySuccessCount());
        return orderCashInfo;
    }

    /**
     * 处理实扣订单字段值
     */
    public void dealActualParams(OrderCashInfo orderCashInfo, Integer matchVol) {
        //汇率
        double rate = orderCashInfo.getRate();

        //每手止损金额(原币种)
        double perStopLoss = DoubleTools.scaleFormat(orderCashInfo.getPerStopLoss());
        //单手保证金附加费
        double perSurcharge = DoubleTools.scaleFormat(orderCashInfo.getPerSurcharge());

        //每手止损保证金
        double perHoldFund = DoubleTools.scaleFormat(DoubleTools.add(perSurcharge, perStopLoss));
        //本次实扣保证金
        double actualHoldFund = DoubleTools.mul(DoubleTools.mul(perHoldFund, rate), matchVol);

        //实扣递延保证金
        double actualDeferFund = 0.0;
        if (orderCashInfo.getDeferStatus() == DeferStatusEnum.DEFER.getValue()) {//如果开启递延，更新实扣递延保证金
            //每手递延保证金
            double perDeferFund = DoubleTools.scaleFormat(orderCashInfo.getPerDeferFund());

            actualDeferFund = DoubleTools.mul(DoubleTools.mul(perDeferFund, rate), matchVol);

        }

        //每手手续费
        double perCounterFee = DoubleTools.scaleFormat(orderCashInfo.getPerCounterFee());
        //实扣手续费
        double actualCounterFee = DoubleTools.mul(DoubleTools.mul(perCounterFee, rate), matchVol);

        orderCashInfo.setActualHoldFund(orderCashInfo.getActualHoldFund() + actualHoldFund);
        orderCashInfo.setActualDeferFund(orderCashInfo.getActualDeferFund() + actualDeferFund);
        orderCashInfo.setActualCounterFee(orderCashInfo.getActualCounterFee() + actualCounterFee);
    }


    /**
     * 获取 资金分布式接口: 资金扣款、结算、退款业务接口
     *
     * @return fundTradeApiService
     */
    @Override
    public IFundTradeApiService getFundTradeApiService() {
        return fundTradeApiService;
    }

    /**
     * 设置 资金分布式接口: 资金扣款、结算、退款业务接口
     *
     * @param fundTradeApiService 资金分布式接口: 资金扣款、结算、退款业务接口
     */
    public void setFundTradeApiService(IFundTradeApiService fundTradeApiService) {
        this.fundTradeApiService = fundTradeApiService;
    }

    /**
     * 获取 交易服务
     *
     * @return tradeServer
     */
    @Override
    public TradeServer getTradeServer() {
        return tradeServer;
    }


    @Override
    public OrderCashInfoDao getOrderCashInfoDao() {
        return this.orderCashInfoDao;
    }

    @Override
    public OrderCashEntrustInfoDao getOrderCashEntrustInfoDao() {
        return this.orderCashEntrustInfoDao;
    }


}
