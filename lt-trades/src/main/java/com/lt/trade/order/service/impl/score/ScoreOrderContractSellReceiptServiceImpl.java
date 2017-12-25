/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: ScoreOrderSellServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.service.impl.score;

import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.trade.OrderScoreSuccessInfo;
import com.lt.trade.order.cache.score.OrderScoreEntrustInfoCache;
import com.lt.trade.order.cache.score.OrderScoreInfoCache;
import com.lt.trade.order.dao.*;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import com.lt.trade.tradeserver.bean.DigitalProductBrandPosition;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 差价合约积分卖出订单数据持久化处理
 *
 * @author yanzhenyu
 */
@Service
public class ScoreOrderContractSellReceiptServiceImpl extends AbstractScoreOrderSellReceiptService {

    /**
     * TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = -7672675751441906028L;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ScoreOrderContractSellReceiptServiceImpl.class);

    /**
     * 订单基本信息数据接口
     */
    @Autowired
    private OrderScoreInfoDao orderScoreInfoDao;
    /**
     * 委托单数据接口
     */
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
     * 券商费用配置接口
     */
    @Autowired
    private IInvestorFeeCfgApiService investorFeeCfgApiService;
    @Autowired
    private IProductInfoService productInfoServiceImpl;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
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
        OrderScoreEntrustInfo eR = entrustRecord;
        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[2002]:差价合约积分平仓订单委托失败, orderId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());

        //现金订单基本数据实例
        OrderScoreInfo ScoreOrdersInfo = OrderScoreInfoCache.get(orderId);
        if (ScoreOrdersInfo == null) {
            logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的差价合约积分订单基本数据!", orderId);
            return;
        }
        OrderScoreInfo cache = ScoreOrdersInfo;
        try {
            //委托单修改时间
            Date modifyDate = new Date();
            //设置修改时间
            entrustRecord.setModifyDate(modifyDate);
            entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());
            //保存失败卖出委托单
            getOrderScoreEntrustInfoDao().add(entrustRecord);

            //删除内存的订单
            OrderScoreInfoCache.remove(orderId);
            //删除缓存中的委托单
            OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
            //向APP客户端推送交易结果
            TradeUtils.notifyAPPClient(ScoreOrdersInfo.getUserId(), sendOrderMsgProducer);

        } catch (RuntimeException e) {
            e.printStackTrace();
            cache.setSellSuccessCount(0);
            OrderScoreEntrustInfoCache.put(eR);
            OrderScoreInfoCache.put(cache);
            logger.error("[2012]差价合约积分委托失败，更新失败原因异常");
        }
    }

    @Override
    public void doPersist2004(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord) {
        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[2004]:[差价合约积分]收到成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
        //移除超时处理
        TradeTimeOut.remove(orderId);
        //移除预警处理
        TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
        //现金订单基本数据实例
        OrderScoreInfo scoreOrdersInfo = OrderScoreInfoCache.get(orderId);
        if (scoreOrdersInfo == null) {
            logger.error("没有在缓存中查找到订单显示ID为:{}的差价合约积分订单基本数据!", orderId);
            return;
        }

        //修改时间
        Date now = new Date();
        entrustRecord.setModifyDate(now);

        //产品
        ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());
        Double jumpPrice = productVo.getJumpValue();

        //更新订单持仓数量对象的卖出成功数量、卖出均价、持仓数量等信息
        this.fillSellScoreOrderCount(now, matchBean.getMatchPrice(), matchBean.getOrderTotal(),
                matchBean.getMatchVol(), scoreOrdersInfo, jumpPrice, entrustRecord.getTradeType());

        //成交记录
        OrderScoreSuccessInfo orderScoreSuccessInfo = new OrderScoreSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
                entrustRecord.getProductId(), entrustRecord.getInvestorId(), entrustRecord.getAccountId(),
                entrustRecord.getProductCode(), entrustRecord.getProductName(),
                entrustRecord.getExchangeCode(), entrustRecord.getPlate(),
                entrustRecord.getSecurityCode(), entrustRecord.getTradeType(),
                entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(),
                entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(),
                matchBean.getMatchVol(), scoreOrdersInfo.getSellSuccessCount(),
                matchBean.getSysMatchId(), matchBean.getSysOrderId(), matchBean.getMatchPrice(), scoreOrdersInfo.getLastSellDate(), now);

        //买入均价
        Double sysBuyAvgPrice = scoreOrdersInfo.getBuyAvgPrice();
        //卖出均价
        Double sysSellAvgPrice = scoreOrdersInfo.getSellAvgPrice();

        //计算盈亏
        this.dealLossProfit(scoreOrdersInfo, matchBean.getMatchTotal(), matchBean.getMatchVol(), sysBuyAvgPrice, sysSellAvgPrice);
        //设置成交状态为完全成交
        orderScoreSuccessInfo.setSuccessStatus(2);

        scoreOrdersInfo.setModifyDate(now);

        getOrderScoreEntrustInfoDao().add(entrustRecord);

        //更新结算日期、结算盈亏、系统结算盈亏
        orderScoreInfoDao.update(scoreOrdersInfo);

        //新增成交记录
        orderScoreSuccessInfoDao.add(orderScoreSuccessInfo);
        logger.info("[差价合约积分]新增成交记录, displayId:{}", orderId);

        //资金结算
        this.fundBalance(scoreOrdersInfo, matchBean.getMatchVol());
        //平仓结束
        //删除缓存中的委托单
        OrderScoreEntrustInfoCache.remove(entrustRecord.getEntrustId());
        //删除缓存中的订单基本信息数据
        OrderScoreInfoCache.remove(orderId);
        //设置卖出完成标记
        TradeUtils.setSuccess(redisTemplate, scoreOrdersInfo.getUserId(), orderId);


        //锁住单户持仓记录
        DigitalCoinPosition coinPosition = new DigitalCoinPosition();
        coinPosition.setUserId(scoreOrdersInfo.getUserId());
        coinPosition.setProductCode(scoreOrdersInfo.getProductCode());
        coinPosition.setInvestorId(scoreOrdersInfo.getInvestorId());

        coinPosition = positionCountManageDao.selectCoinPositionForUpdate(coinPosition);

        if (coinPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //平多
                coinPosition.setBuyCount(coinPosition.getBuyCount() - scoreOrdersInfo.getSellSuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //平空
                coinPosition.setSellCount(coinPosition.getSellCount() - scoreOrdersInfo.getSellSuccessCount());
            }
            positionCountManageDao.updateCoinPosition(coinPosition);
            logger.info("[2004]:[差价合约积分]平仓，修改单户持仓记录, coinPosition:{}", coinPosition);
        }

        //锁住品牌持仓记录
        DigitalProductBrandPosition brandPosition = new DigitalProductBrandPosition();
        brandPosition.setProductCode(scoreOrdersInfo.getProductCode());
        brandPosition.setBrandId(scoreOrdersInfo.getBrandId());

        brandPosition = brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);

        if (brandPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //平多
                brandPosition.setBuyCount(brandPosition.getBuyCount() - scoreOrdersInfo.getSellSuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == scoreOrdersInfo.getTradeDirection()) {
                //平空
                brandPosition.setSellCount(brandPosition.getSellCount() - scoreOrdersInfo.getSellSuccessCount());
            }
            brandPositionManageDao.updateCoinPosition(brandPosition);
            logger.info("[2004]:[差价合约积分]平仓，修改品牌持仓记录, coinPosition:{}", brandPosition);
        }

        //向APP客户端推送交易结果
        TradeUtils.notifyAPPClient(scoreOrdersInfo.getUserId(), sendOrderMsgProducer);
    }

    /**
     * 更新订单持仓数量对象的卖出成功数量、卖出均价、卖出时间、持仓数量等信息
     */
    private OrderScoreInfo fillSellScoreOrderCount(Date lastBuyDate, Double matchPrice,
                                                   Integer orderTotal, Integer matchVol, OrderScoreInfo orderScoreInfo, Double jumpPrice, Integer tradeType) {
        //已卖出成功手数、卖出均价
        Integer sellSuccCount = orderScoreInfo.getSellSuccessCount();

        //计算卖出均价、卖出成功数量
        AvgPriceVo avgPriceVo = new AvgPriceVo(orderScoreInfo.getSellPriceTotal(), orderScoreInfo.getSellMinPrice(), sellSuccCount, matchVol, matchPrice,
                orderScoreInfo.getTradeDirection(), tradeType, jumpPrice);

        orderScoreInfo.setSellMinPrice(avgPriceVo.getMinPrice());//最小价
        orderScoreInfo.setSellPriceTotal(avgPriceVo.getPriceTotal());//总价

        //持仓数量
        orderScoreInfo.setHoldCount(orderScoreInfo.getHoldCount() - matchVol);
        //更新卖出时间

        orderScoreInfo.setLastSellDate(lastBuyDate);
        //更新已卖出成功手数
        orderScoreInfo.setSellSuccessCount(avgPriceVo.getCount());
        //更新委托卖数量
        orderScoreInfo.setSellEntrustCount(orderScoreInfo.getSellEntrustCount() - matchVol);

        //更新卖出均价
        orderScoreInfo.setSellAvgPrice(avgPriceVo.getAvgPrice());
        return orderScoreInfo;
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
     * 获取 券商费用配置接口
     *
     * @return investorFeeCfgApiService
     */
    @Override
    public IInvestorFeeCfgApiService getInvestorFeeCfgApiService() {
        return investorFeeCfgApiService;
    }

    /**
     * 设置 券商费用配置接口
     *
     * @param investorFeeCfgApiService 券商费用配置接口
     */
    public void setInvestorFeeCfgApiService(IInvestorFeeCfgApiService investorFeeCfgApiService) {
        this.investorFeeCfgApiService = investorFeeCfgApiService;
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
