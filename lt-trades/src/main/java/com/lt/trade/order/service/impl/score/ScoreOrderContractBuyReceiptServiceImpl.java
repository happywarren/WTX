/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: ScoreOrderBuyServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.service.impl.score;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.trade.OrderScoreSuccessInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
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
 * 差价合约积分订单买入委托数据、回执数据处理
 *
 * @author yanzhenyu
 */
@Service
public class ScoreOrderContractBuyReceiptServiceImpl extends AbstractScoreOrderBuyReceiptService {

    private static final long serialVersionUID = 1050229096357327075L;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ScoreOrderContractBuyReceiptServiceImpl.class);

    /**
     * 订单基本信息数据接口
     */
    @Autowired
    private OrderScoreInfoDao orderScoreInfoDao;
    @Autowired
    private OrderScoreEntrustInfoDao orderScoreEntrustInfoDao;
    /**
     * 成交结果数据接口
     */
    @Autowired
    private OrderScoreSuccessInfoDao orderScoreSuccessInfoDao;
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

    @Resource(name = "sendOrderMsgProducer")
    private MessageQueueProducer sendOrderMsgProducer;

    @Autowired
    private DigitalScoreCoinPositionCountManageDao positionCountManageDao;

    @Autowired
    private DigitalScoreProductBrandPositionManageDao brandPositionManageDao;

    @Override
    public void doPersist2003(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord) {
    }

    @Override
    public void doPersist2002(OrderScoreEntrustInfo entrustRecord, int errorId) {
        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[2002]:差价合约积分开仓订单委托失败, orderId:{}, entrustId:{}", orderId, entrustRecord.getEntrustId());

        //订单基本信息
        OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);
        if (scoreOrdersInfo == null) {
            logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的差价合约积分订单数据!", orderId);
            return;
        }
        OrderScoreInfo cache = scoreOrdersInfo;
        try {
            //删除缓存中的订单主数据实例
            OrderScoreInfoCache.remove(orderId);
            logger.info("[2002]:已删除缓存中的差价合约积分订单主数据");
            //删除缓存中的委托单
            OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
            logger.info("[2002]:删除缓存中的差价合约积分委托单");
            //移除超时处理
            TradeTimeOut.remove(orderId);
            //移除预警处理
            TradeTimeOut.earlyWarnEntrustMapRemove(orderId);

            //委托单修改时间
            Date date = new Date();
            entrustRecord.setModifyDate(date);

            //订单修改时间
            scoreOrdersInfo.setModifyDate(date);

            //订单失败数量
            scoreOrdersInfo.setBuyFailCount(entrustRecord.getEntrustCount());
            entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());

            //新增订单到数据库
            getOrderScoreInfoDao().add(scoreOrdersInfo);

            //新增委托信息到数据库
            getOrderScoreEntrustInfoDao().add(entrustRecord);

            //退款
            this.refund(scoreOrdersInfo, entrustRecord.getEntrustCount());
            logger.info("[2002]:差价合约积分开仓失败退款完成");

            //向APP客户端推送交易结果
            TradeUtils.notifyAPPClient(scoreOrdersInfo.getUserId(), sendOrderMsgProducer);
        } catch (RuntimeException e) {
            e.printStackTrace();
            OrderScoreInfoCache.put(cache);
            logger.error("[2002] 事务回滚异常");
        }
    }

    /**
     * 更新持仓数量、买入成功数量、买入均价、买入时间、添加成交记录<br/>
     * 买入失败时，记录买入失败数量、并退款
     */
    @Override
    public void doPersist2004(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord) {
        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[2004]:[差价合约积分]收到成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
        //移除超时处理
        TradeTimeOut.remove(orderId);
        //移除预警处理
        TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
        //订单基本信息
        OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);
        if (scoreOrdersInfo == null) {
            logger.error("[2004]:没有在缓存中查找到订单显示ID为:{}的差价合约积分订单数据!", orderId);
            return;
        }

        //从redis中获取产品
        ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
        //产品价格小数位数
        //Integer scale = productVo.getDecimalDigits();
        Double jumpPrice = productVo.getJumpValue();

        //成交记录
        Date now = new Date();

        //更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
        this.fillBuyScoreOrderCount(now, matchBean.getMatchPrice(),
                matchBean.getOrderTotal(), matchBean.getMatchVol(), matchBean.getMatchTotal(), scoreOrdersInfo, jumpPrice, entrustRecord.getTradeType());


        //重新计算止盈价、止损价
        this.calculateStopLossProfitPrice(scoreOrdersInfo, productVo.getDecimalDigits());

        scoreOrdersInfo.setModifyDate(now);

        //成交记录实体组装
        OrderScoreSuccessInfo orderScoreSuccessInfo = new OrderScoreSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
                entrustRecord.getProductId(), entrustRecord.getInvestorId(), entrustRecord.getAccountId(),
                entrustRecord.getProductCode(), entrustRecord.getProductName(),
                entrustRecord.getExchangeCode(), entrustRecord.getPlate(),
                entrustRecord.getSecurityCode(), entrustRecord.getTradeType(),
                entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(),
                entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(),
                matchBean.getMatchVol(), scoreOrdersInfo.getBuySuccessCount(),
                matchBean.getSysMatchId(), matchBean.getSysOrderId(), matchBean.getMatchPrice(), scoreOrdersInfo.getLastBuyDate(), now);

        //设置委托时间
        scoreOrdersInfo.setEntrustBuyDate(now);

        orderScoreSuccessInfo.setSuccessStatus(2);

        addEntrustInfo(entrustRecord);

        //更新订单信息
        orderScoreInfoDao.add(scoreOrdersInfo);
        logger.info("[2004]:[差价合约积分]更新订单信息, orderId:{}", orderId);

        //新增成交记录
        orderScoreSuccessInfoDao.add(orderScoreSuccessInfo);
        logger.info("[2004]:[差价合约积分]新增成交记录, orderId:{}", orderId);

        //删除缓存中的买入委托单
        OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());



        //锁住持仓记录
        DigitalCoinPosition coinPosition = new DigitalCoinPosition();
        coinPosition.setUserId(scoreOrdersInfo.getUserId());
        coinPosition.setProductCode(scoreOrdersInfo.getProductCode());
        coinPosition.setInvestorId(scoreOrdersInfo.getInvestorId());

        coinPosition = positionCountManageDao.selectCoinPositionForUpdate(coinPosition);

        if (coinPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看多
                coinPosition.setBuyCount(coinPosition.getBuyCount() + scoreOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看空
                coinPosition.setSellCount(coinPosition.getSellCount() + scoreOrdersInfo.getBuySuccessCount());
            }
            positionCountManageDao.updateCoinPosition(coinPosition);
            logger.info("[2004]:[差价合约积分]开仓，修改持仓记录, coinPosition:{}", coinPosition);
        } else {
            coinPosition = new DigitalCoinPosition();
            coinPosition.setUserId(scoreOrdersInfo.getUserId());
            coinPosition.setProductCode(scoreOrdersInfo.getProductCode());
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看多
                coinPosition.setBuyCount(scoreOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看空
                coinPosition.setSellCount(scoreOrdersInfo.getBuySuccessCount());
            }
            coinPosition.setInvestorId(scoreOrdersInfo.getInvestorId());
            positionCountManageDao.insertCoinPosition(coinPosition);
            logger.info("[2004]:[差价合约积分]开仓，新增持仓记录, coinPosition:{}", coinPosition);

            //锁住持仓记录
            positionCountManageDao.selectCoinPositionForUpdate(coinPosition);
        }


        //锁住品牌持仓记录
        DigitalProductBrandPosition brandPosition = new DigitalProductBrandPosition();
        brandPosition.setBrandId(scoreOrdersInfo.getBrandId());
        brandPosition.setProductCode(scoreOrdersInfo.getProductCode());
        logger.info("brandPosition.getBrandId()---------{},brandPosition.getProductCode()---------{}",
                brandPosition.getBrandId(),brandPosition.getProductCode());

        brandPosition = brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);

        if (brandPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看多
                brandPosition.setBuyCount(brandPosition.getBuyCount() + scoreOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看空
                brandPosition.setSellCount(brandPosition.getSellCount() + scoreOrdersInfo.getBuySuccessCount());
            }
            brandPositionManageDao.updateCoinPosition(brandPosition);
            logger.info("[2004]:[差价合约积分]开仓，修改品牌持仓记录, brandPosition:{}", brandPosition);

        } else {
            brandPosition = new DigitalProductBrandPosition();
            brandPosition.setBrandId(scoreOrdersInfo.getBrandId());
            brandPosition.setProductCode(scoreOrdersInfo.getProductCode());
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看多
                brandPosition.setBuyCount(scoreOrdersInfo.getBuySuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //看空
                brandPosition.setSellCount(scoreOrdersInfo.getBuySuccessCount());
            }
            brandPositionManageDao.insertCoinPosition(brandPosition);
            logger.info("[2004]:[差价合约积分]开仓，新增品牌持仓记录, brandPosition:{}", brandPosition);

            //锁住持仓记录
            brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);
        }

        //订单加入到风控监控队列
        this.fillRiskControlQueue(scoreOrdersInfo);
        logger.info("[2004]:[差价合约积分]订单加入到风控监控队列, displayId:{}", orderId);

        //向APP客户端推送交易结果
        TradeUtils.notifyAPPClient(scoreOrdersInfo.getUserId(), sendOrderMsgProducer);
    }

    /**
     * 更新订单持仓数量对象的买入成功数量、买入失败数量、买入均价、持仓数量等信息
     */
    public OrderScoreInfo fillBuyScoreOrderCount(Date lastBuyDate, Double matchPrice,
                                                 Integer orderTotal, Integer matchVol, Integer matchTotal, OrderScoreInfo orderScoreInfo, Double jumpPrice, Integer tradeType) {
        //已买入成功手数、买入均价
        Integer buySuccCount = orderScoreInfo.getBuySuccessCount();

        //计算买入均价、买入成功数量
        AvgPriceVo avgPriceVo = new AvgPriceVo(orderScoreInfo.getBuyPriceTotal(), orderScoreInfo.getBuyMinPrice(), buySuccCount, matchVol, matchPrice,
                orderScoreInfo.getTradeDirection(), tradeType, jumpPrice);

        logger.info("=============买入均价，买入成功数量==avgPriceVo={}===========", JSONObject.toJSONString(avgPriceVo));

        orderScoreInfo.setBuyMinPrice(avgPriceVo.getMinPrice());//最小价
        orderScoreInfo.setBuyPriceTotal(avgPriceVo.getPriceTotal());//总价
        //更新买入时间
        orderScoreInfo.setLastBuyDate(lastBuyDate);

        //更新已买入成功手数
        orderScoreInfo.setBuySuccessCount(avgPriceVo.getCount());
        //更新买入均价
        orderScoreInfo.setBuyAvgPrice(avgPriceVo.getAvgPrice());
        //更新持仓数量
        orderScoreInfo.setHoldCount(orderScoreInfo.getBuySuccessCount());

        //处理实扣字段
        this.dealActualParams(orderScoreInfo, matchTotal);

        logger.info("[差价合约积分]更新订单持仓, sysBuyAvgPrice:{}, buySuccCount:{}", avgPriceVo.getAvgPrice(), orderScoreInfo.getBuySuccessCount());
        return orderScoreInfo;
    }

    /**
     * 处理实扣订单字段值
     */
    public void dealActualParams(OrderScoreInfo orderScoreInfo, Integer matchTotal) {
        //汇率
        double rate = orderScoreInfo.getRate();

        //每手止损金额(原币种)
        double perStopLoss = orderScoreInfo.getPerStopLoss();
        //单手保证金附加费
        double perSurcharge = orderScoreInfo.getPerSurcharge();

        //每手止损保证金
        double perHoldFund = DoubleTools.scaleFormat(DoubleTools.add(perSurcharge, perStopLoss));
        //本次实扣保证金
        double actualHoldFund = DoubleTools.mul(DoubleTools.mul(perHoldFund, rate), matchTotal);

        //实扣递延保证金
        double actualDeferFund = 0.0;
        if (orderScoreInfo.getDeferStatus() == DeferStatusEnum.DEFER.getValue()) {//如果开启递延，更新实扣递延保证金
            //每手递延保证金
            double perDeferFund = DoubleTools.scaleFormat(orderScoreInfo.getPerDeferFund());

            actualDeferFund = DoubleTools.mul(DoubleTools.mul(perDeferFund, rate), matchTotal);

        }

        //每手手续费
        double perCounterFee = DoubleTools.scaleFormat(orderScoreInfo.getPerCounterFee());
        //实扣手续费
        double actualCounterFee = DoubleTools.mul(DoubleTools.mul(perCounterFee, rate), matchTotal);

        orderScoreInfo.setActualHoldFund(actualHoldFund);
        orderScoreInfo.setActualDeferFund(actualDeferFund);
        orderScoreInfo.setActualCounterFee(actualCounterFee);
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
    public OrderScoreInfoDao getOrderScoreInfoDao() {
        return this.orderScoreInfoDao;
    }

    @Override
    public OrderScoreEntrustInfoDao getOrderScoreEntrustInfoDao() {
        return this.orderScoreEntrustInfoDao;
    }

    @Override
    public void doPersist(FutureMatchBean matchBean,
                          OrderScoreEntrustInfo entrustRecord) {

    }


}
