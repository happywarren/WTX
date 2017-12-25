/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderSellServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.service.impl.cash;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.trade.EntrustStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.trade.OrderCashSuccessInfo;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
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
 * TODO 卖出订单数据持久化处理
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年12月12日 下午8:05:48
 */
@Service
public class CashOrderContractSellReceiptServiceImpl extends AbstractCashOrderSellReceiptService {

    /**
     * TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = -7672675751441906028L;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(CashOrderContractSellReceiptServiceImpl.class);

    /**
     * 订单基本信息数据接口
     */
    @Autowired
    private OrderCashInfoDao orderCashInfoDao;
    /**
     * 委托单数据接口
     */
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
     * 券商费用配置接口
     */
    @Autowired
    private IInvestorFeeCfgApiService investorFeeCfgApiService;
    @Autowired
    private IProductInfoService productInfoServiceImpl;
    @Autowired
    private OrderCashEntrustChildInfoDao orderCashEntrustChildInfoDao;
    @Autowired
    private MessageQueueProducer promoteProducer;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource(name = "sendOrderMsgProducer")
    private MessageQueueProducer sendOrderMsgProducer;
    @Autowired
    private DigitalCoinPositionCountManageDao positionCountManageDao;

    @Autowired
    private DigitalProductBrandPositionManageDao brandPositionManageDao;

    @Override
    public void doPersist2003(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {
    }

    @Override
    public void doPersist2002(OrderCashEntrustInfo entrustRecord, int errorId) {
        OrderCashEntrustInfo eR = entrustRecord;

        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[2002]:差价合约平仓订单委托失败, orderId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());

        //现金订单基本数据实例
        OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
        if (cashOrdersInfo == null) {
            logger.error("[2002]:没有在缓存中查找到订单显示ID为:{}的差价合约现金订单基本数据!", orderId);
            return;
        }
        OrderCashInfo cashCache = cashOrdersInfo;
        try {
            //委托单修改时间
            Date modifyDate = new Date();
            //设置修改时间
            entrustRecord.setModifyDate(modifyDate);
            entrustRecord.setEntrustStatus(EntrustStatusEnum.FAIL.getValue());

            //不成功平仓委托记录不入库
            getOrderCashEntrustChildInfoDao().add(entrustRecord);

            //删除内存的订单
            OrderCashInfoCache.remove(orderId);
            //删除缓存中的委托单
            OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());
            //移除超时处理
            TradeTimeOut.remove(orderId);
            //移除预警处理
            TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
            //预警发送 委托失败
        } catch (RuntimeException e) {
            e.printStackTrace();
            OrderCashEntrustInfoCache.put(eR);
            cashCache.setSellSuccessCount(0);
            OrderCashInfoCache.put(cashCache);
            logger.error("[2002]:差价合约平仓订单委托失败");
        } finally {
            TradeUtils.delKeyLockSell(redisTemplate, orderId);
            //向APP客户端推送交易结果
            TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(), sendOrderMsgProducer);
            //重新加入风控
        }
    }

    @Override
    public void doPersist2004(FutureMatchBean matchBean, OrderCashEntrustInfo entrustRecord) {
        //订单显示ID
        String orderId = entrustRecord.getOrderId();
        logger.info("[2004]差价合约收到成交回执, displayId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());
        //移除超时处理
        TradeTimeOut.remove(orderId);
        //移除预警处理
        TradeTimeOut.earlyWarnEntrustMapRemove(orderId);
        //现金订单基本数据实例
        OrderCashInfo cashOrdersInfo = OrderCashInfoCache.get(orderId);
        if (cashOrdersInfo == null) {
            logger.error("没有在缓存中查找到订单显示ID为:{}的差价合约现金订单基本数据!", orderId);
            return;
        }

        //修改时间
        Date now = new Date();
        entrustRecord.setModifyDate(now);

        //产品
        ProductVo productVo = productInfoServiceImpl.queryProduct(entrustRecord.getProductCode());

        Double jumpPrice = productVo.getJumpValue();

        logger.info("----------MatchPrice：{}------", matchBean.getMatchPrice());
        //更新订单持仓数量对象的卖出成功数量、卖出均价、持仓数量等信息
        this.fillSellCashOrderCount(now, matchBean.getMatchPrice(), matchBean.getOrderTotal(),
                matchBean.getMatchVol(), cashOrdersInfo, jumpPrice, entrustRecord.getTradeType());

        //成交记录
        OrderCashSuccessInfo orderCashSuccessInfo = new OrderCashSuccessInfo(TradeUtils.makeSuccessCode(), orderId, entrustRecord.getEntrustId(),
                entrustRecord.getProductId(), entrustRecord.getInvestorId(), entrustRecord.getAccountId(),
                entrustRecord.getProductCode(), entrustRecord.getProductName(),
                entrustRecord.getExchangeCode(), entrustRecord.getPlate(),
                entrustRecord.getSecurityCode(), entrustRecord.getTradeType(),
                entrustRecord.getTradeDirection(), entrustRecord.getActTradeDirection(),
                entrustRecord.getActTradeType(), entrustRecord.getEntrustCount(),
                matchBean.getMatchVol(), cashOrdersInfo.getSellSuccessCount(),
                matchBean.getSysMatchId(), matchBean.getSysOrderId(), matchBean.getMatchPrice(), cashOrdersInfo.getLastSellDate(), now);

        //买入均价
        Double sysBuyAvgPrice = cashOrdersInfo.getBuyAvgPrice();
        logger.info("----------买入均价：{}------", sysBuyAvgPrice);
        //卖出均价
        Double sysSellAvgPrice = cashOrdersInfo.getSellAvgPrice();
        logger.info("----------卖出均价：{}------", sysSellAvgPrice);

        //计算盈亏
        this.dealLossProfit(cashOrdersInfo, matchBean.getMatchTotal(), matchBean.getMatchVol(), sysBuyAvgPrice, sysSellAvgPrice);

        orderCashSuccessInfo.setSuccessStatus(2);

        cashOrdersInfo.setModifyDate(now);

        //委托实体入库
        getOrderCashEntrustInfoDao().add(entrustRecord);

        //更新结算日期、结算盈亏、系统结算盈亏
        orderCashInfoDao.update(cashOrdersInfo);

        //新增成交记录
        orderCashSuccessInfoDao.add(orderCashSuccessInfo);
        logger.info("[差价合约]新增成交记录, displayId:{}", orderId);

        //资金结算
        this.fundBalance(cashOrdersInfo, matchBean.getMatchVol());
        //平仓结束
        //删除缓存中的委托单
        OrderCashEntrustInfoCache.remove(entrustRecord.getEntrustId());
        //删除缓存中的订单基本信息数据
        OrderCashInfoCache.remove(orderId);
        //设置卖出完成标记
        TradeUtils.setSuccess(redisTemplate, cashOrdersInfo.getUserId(), orderId);

        //用于统计推广数据
        JSONObject json = new JSONObject();
        json.put("userId", cashOrdersInfo.getUserId());
        json.put("orderId", cashOrdersInfo.getOrderId());
        json.put("tradeHandCount", cashOrdersInfo.getBuySuccessCount());
        json.put("tradeAmount", cashOrdersInfo.getActualCounterFee());
        json.put("dateTime", cashOrdersInfo.getEntrustSellDate());
        logger.info("===========通知推广模块统计===========");
        promoteProducer.sendMessage(json);


        //锁住单户持仓记录
        DigitalCoinPosition coinPosition = new DigitalCoinPosition();
        coinPosition.setUserId(cashOrdersInfo.getUserId());
        coinPosition.setProductCode(cashOrdersInfo.getProductCode());
        coinPosition.setInvestorId(cashOrdersInfo.getInvestorId());
        logger.info("cashOrdersInfo.getInvestorId()---------{}", cashOrdersInfo.getInvestorId());

        coinPosition = positionCountManageDao.selectCoinPositionForUpdate(coinPosition);
        logger.info("coinPosition---------{}", coinPosition);

        if (coinPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == cashOrdersInfo.getTradeDirection()) {
                //平多
                coinPosition.setBuyCount(coinPosition.getBuyCount() - cashOrdersInfo.getSellSuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == cashOrdersInfo.getTradeDirection()) {
                //平空
                coinPosition.setSellCount(coinPosition.getSellCount() - cashOrdersInfo.getSellSuccessCount());
            }
            positionCountManageDao.updateCoinPosition(coinPosition);
            logger.info("[2004]:[差价合约]平仓，修改单户持仓记录, coinPosition:{}", coinPosition);
        }

        //锁住品牌持仓记录
        DigitalProductBrandPosition brandPosition = new DigitalProductBrandPosition();
        brandPosition.setBrandId(cashOrdersInfo.getBrandId());
        brandPosition.setProductCode(cashOrdersInfo.getProductCode());

        brandPosition = brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);
        logger.info("brandPosition---------{}", brandPosition);

        if (brandPosition != null) {
            if (TradeDirectionEnum.DIRECTION_UP.getValue() == cashOrdersInfo.getTradeDirection()) {
                //平多
                brandPosition.setBuyCount(brandPosition.getBuyCount() - cashOrdersInfo.getSellSuccessCount());
            } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == cashOrdersInfo.getTradeDirection()) {
                //平空
                brandPosition.setSellCount(brandPosition.getSellCount() - cashOrdersInfo.getSellSuccessCount());
            }
            brandPositionManageDao.updateCoinPosition(brandPosition);
            logger.info("[2004]:[差价合约]平仓，修改品牌持仓记录, brandPosition:{}", brandPosition);
        }

        //向APP客户端推送交易结果
        TradeUtils.notifyAPPClient(cashOrdersInfo.getUserId(), sendOrderMsgProducer);
    }

    /**
     * 更新订单持仓数量对象的卖出成功数量、卖出均价、卖出时间、持仓数量等信息
     */
    private OrderCashInfo fillSellCashOrderCount(Date lastBuyDate, Double matchPrice,
                                                 Integer orderTotal, Integer matchVol, OrderCashInfo orderCashInfo, Double jumpPrice, Integer tradeType) {
        //已卖出成功手数、卖出均价
        Integer sellSuccCount = orderCashInfo.getSellSuccessCount();

        //计算卖出均价、卖出成功数量
        AvgPriceVo avgPriceVo = new AvgPriceVo(orderCashInfo.getSellPriceTotal(), orderCashInfo.getSellMinPrice(), sellSuccCount, matchVol, matchPrice,
                orderCashInfo.getTradeDirection(), tradeType, jumpPrice);

        orderCashInfo.setSellMinPrice(avgPriceVo.getMinPrice());//最小价
        orderCashInfo.setSellPriceTotal(avgPriceVo.getPriceTotal());//总价

        //持仓数量
        orderCashInfo.setHoldCount(orderCashInfo.getHoldCount() - matchVol);
        //更新卖出时间

        orderCashInfo.setLastSellDate(lastBuyDate);
        //更新已卖出成功手数
        orderCashInfo.setSellSuccessCount(avgPriceVo.getCount());
        //更新委托卖数量
        orderCashInfo.setSellEntrustCount(orderCashInfo.getSellEntrustCount() - matchVol);

        //更新卖出均价
        orderCashInfo.setSellAvgPrice(avgPriceVo.getAvgPrice());
        return orderCashInfo;
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
    public OrderCashInfoDao getOrderCashInfoDao() {
        return this.orderCashInfoDao;
    }

    @Override
    public OrderCashEntrustInfoDao getOrderCashEntrustInfoDao() {
        return this.orderCashEntrustInfoDao;
    }


    @Override
    public OrderCashEntrustChildInfoDao getOrderCashEntrustChildInfoDao() {
        return this.orderCashEntrustChildInfoDao;
    }


    @Override
    public void doPersist(FutureMatchBean matchBean,
                          OrderCashEntrustInfo entrustRecord) {

    }

}
