/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderSellTradeServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.service.impl.cash;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IUserApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.EntrustPriceTypeEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.SellTriggerTypeEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.UserBaseInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.dao.OrderCashEntrustInfoDao;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.service.IOrderSellTradeService;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.OrderVo;
import com.lt.vo.trade.TradeDirectVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * TODO 现金订单平仓业务实现
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年12月12日 下午8:44:03
 */
@Service
public class CashOrderSellTradeServiceImpl extends AbstractCashOrderTradeSerivce implements IOrderSellTradeService {

    /**
     * TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = -1111246308799240761L;
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(CashOrderSellTradeServiceImpl.class);

    /**
     * 资金分布式接口: 资金扣款、结算、退款业务接口
     */
    @Autowired
    private IFundTradeApiService fundTradeApiService;
    /**
     * 订单基本信息接口
     */
    @Autowired
    private OrderCashInfoDao orderCashInfoDao;

    /**
     * 产品信息接口
     */
    @Autowired
    private IProductInfoService productInfoServiceImpl;
    /**
     * 交易服务
     */
    @Autowired
    private TradeServer tradeServer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IProductApiService productApiService;

    @Autowired
    private IUserApiService userApiService;
    /**
     * 平仓
     *
     * @param orderVo
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月12日 下午8:44:03
     * @see com.lt.trade.order.service.IOrderSellTradeService#doSell(com.lt.vo.trade.OrderVo)
     */
    @Override
    public void doSell(OrderVo orderVo) throws LTException {
        //开始时间
        long startTime = System.currentTimeMillis();
        //订单显示ID
        String orderId = orderVo.getOrderId();
        logger.info("======平仓开始orderId:{}======", orderId);
        //现金订单基本信息
        OrderCashInfo cashOrder = this.queryCashOrdersInfo(orderVo);

        //是否卖完
        if (TradeUtils.isSuccess(redisTemplate, "", orderId)) {
            OrderCashInfoCache.remove(orderId);
            removeRiskControlOrder(cashOrder);
            logger.error("订单已卖出  orderVo:{} ", JSONObject.toJSON(orderVo));
            return;
        }

        //订单正在重复卖出
        if (TradeUtils.isDuplicateSell(redisTemplate, "", orderId)) {

            logger.error("订单正在卖出中  orderVo:{} ", JSONObject.toJSON(orderVo));
            throw new LTException(LTResponseCode.TDJ0002);
        }


        //处理订单的移动止损金额、移动止损价
        this.dealMoveStopLoss(cashOrder, orderVo);

        //初始化卖出委托单
        OrderCashEntrustInfo entrustRecord = this.initSellEntrustRecord(cashOrder);
        logger.info("已初始化平仓委托单, orderId:{}, entrustCode:{}", orderId, entrustRecord.getEntrustId());

        //设置委托类型，委托价
        cashOrder.setSellEntrustType(entrustRecord.getEntrustType());
        cashOrder.setEntrustSellPrice(entrustRecord.getEntrustPrice());

        //获取用户信息
        UserBaseInfo info = userApiService.findUserByUserId(cashOrder.getUserId());
        cashOrder.setBrandId(info.getBrandId());

        //订单主数据保存到缓存
        OrderCashInfoCache.put(cashOrder);
        //委托单保存到缓存
        OrderCashEntrustInfoCache.put(entrustRecord);

        //发送委托消息到C++
        this.sendSellEntrustMessage(cashOrder, entrustRecord);
        logger.info("======订单orderId:{}平仓用时time:{}ms======", orderId, (System.currentTimeMillis() - startTime));
    }

    public void removeRiskControlOrder(OrderCashInfo cashOrdersInfo) {
        RiskControlBean riskBean = new RiskControlBean(
                cashOrdersInfo.getProductCode(), cashOrdersInfo.getOrderId(),
                -999, null, cashOrdersInfo.getSysSetSellDate().getTime(),
                cashOrdersInfo.getBuyAvgPrice(), cashOrdersInfo.getStopProfitPrice(),
                cashOrdersInfo.getStopLossPrice(), null);
        if (PlateEnum.INNER_PLATE.getValue() == cashOrdersInfo.getPlate()) {
            //删除监控队列
            getTradeServer().getInnerFutureTrade().removeRiskControlOrder(riskBean);
            logger.info("[内盘]将订单移除监控队列, displayId:{}", cashOrdersInfo.getOrderId());
        } else if (PlateEnum.OUTER_PLATE.getValue() == cashOrdersInfo.getPlate()) {
            //删除监控队列
            getTradeServer().getOuterFutureTrade().removeRiskControlOrder(riskBean);
            logger.info("[外盘]将订单移除监控队列, displayId:{}", cashOrdersInfo.getOrderId());
        } else if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue() == cashOrdersInfo.getPlate()) {
            //删除监控队列
            getTradeServer().getContractTrade().removeRiskControlOrder(riskBean);
            logger.info("[差价合约]将订单移除监控队列, displayId:{}", cashOrdersInfo.getOrderId());
        }

    }

    /**
     * TODO 批量平仓
     *
     * @param userId
     * @param productCode
     * @throws LTException
     * @author XieZhibing
     * @date 2017年1月4日 上午9:56:14
     */
    @Override
    public void doSell(String userId, String productCode) throws LTException {
        //执行时间
        long startTime = System.currentTimeMillis();
        logger.info("======批量平仓开始userId:{}, productCode:{}======", userId, productCode);

        //查询持仓中的现金订单信息
        List<OrderCashInfo> orderCashInfoList = orderCashInfoDao.queryUserVendibleCashOrderList(userId, productCode);
        //判断现金订单是否存在
        if (orderCashInfoList == null || orderCashInfoList.isEmpty()) {
            logger.info("未查询到用户:{}, 产品:{}的订单", userId, productCode);
            return;
        }

        for (OrderCashInfo orderCashInfo : orderCashInfoList) {
            //是否卖完
            if (TradeUtils.isSuccess(redisTemplate, "", orderCashInfo.getOrderId())) {
                OrderCashInfoCache.remove(orderCashInfo.getOrderId());
                removeRiskControlOrder(orderCashInfo);
                logger.error("订单已卖出  orderVo:{} ", JSONObject.toJSON(orderCashInfo));
                return;
            }
            //判断是否重复下单
            if (TradeUtils.isDuplicateSell(redisTemplate, orderCashInfo.getUserId().toString(), orderCashInfo.getOrderId())) {
//				throw new LTException(LTResponseCode.TDJ0002);
                //订单正在卖出中
                logger.error("订单正在卖出中  orderCashInfo:{} ", JSONObject.toJSON(orderCashInfo));
                continue;
            }
            OrderVo orderVo = new OrderVo(orderCashInfo.getOrderId(), FundTypeEnum.CASH.getValue(), SellTriggerTypeEnum.CUSTOMER.getValue(),
                    userId, 0, new Date(startTime));

            //现金订单基本信息
            OrderCashInfo cashOrder = this.queryCashOrdersInfo(orderVo);
            //处理订单的移动止损金额、移动止损价
            this.dealMoveStopLoss(cashOrder, orderVo);

            //初始化卖出委托单
            OrderCashEntrustInfo entrustRecord = this.initSellEntrustRecord(cashOrder);
            logger.info("已初始化平仓委托单, orderId:{}, entrustCode:{}", orderCashInfo.getOrderId(), entrustRecord.getEntrustId());

            //设置委托类型，委托价
            cashOrder.setSellEntrustType(entrustRecord.getEntrustType());
            cashOrder.setEntrustSellPrice(entrustRecord.getEntrustPrice());

            //获取用户信息
            UserBaseInfo info = userApiService.findUserByUserId(cashOrder.getUserId());
            cashOrder.setBrandId(info.getBrandId());

            //订单主数据保存到缓存
            OrderCashInfoCache.put(cashOrder);
            //委托单保存到缓存
            OrderCashEntrustInfoCache.put(entrustRecord);

            //发送委托消息到C++
            this.sendSellEntrustMessage(cashOrder, entrustRecord);

        }
        logger.info("======批量平仓完成 userId:{}, productCode:{}, 执行时间time:{}ms======", userId, productCode, (System.currentTimeMillis() - startTime));
    }


    /**
     * TODO 初始化平仓委托单
     *
     * @param cashOrder
     * @return
     * @author XieZhibing
     * @date 2017年1月3日 下午8:48:34
     */
    private OrderCashEntrustInfo initSellEntrustRecord(OrderCashInfo cashOrder) {

        //订单买入时的交易方向
        Integer tradeDirection = cashOrder.getTradeDirection();

        //计算委托单的委托方向
        TradeDirectVo tradeDirectVo = new TradeDirectVo(tradeDirection, TradeTypeEnum.SELL.getValue());

        //委托价
        double entrustPrice = cashOrder.getUserCommitSellPrice();
        //限价浮动值: 限价加减点位
        double limitedPriceValue = -999;
        //委托价类型
        Integer entrustType = EntrustPriceTypeEnum.LIMIT_PRICE.getValue();
        //获取产品波动点位、波动价格
        ProductVo productVo = productInfoServiceImpl.queryProduct(cashOrder.getProductCode());
        if (1 == productVo.getIsMarketPrice()) {
            entrustType = EntrustPriceTypeEnum.MARKET_PRICE.getValue();
            entrustPrice = 0;
        } else {
            //限价加减点位
            limitedPriceValue = productVo.getLimitedPriceValue();
        }

        //时间
        Date now = new Date();
        //生成报给交易所的委托单号
        int entrustCode = TradeUtils.makeEntrustCode();
        String entrustId = String.valueOf(entrustCode);

        //初始化卖出委托单
        OrderCashEntrustInfo orderCashEntrustInfo = new OrderCashEntrustInfo(cashOrder.getOrderId(), entrustId, cashOrder.getProductId(),
                cashOrder.getProductCode(), cashOrder.getProductName(), cashOrder.getExchangeCode(),
                cashOrder.getPlate(), cashOrder.getSecurityCode(), cashOrder.getInvestorId(),
                cashOrder.getAccountId(), TradeTypeEnum.SELL.getValue(), tradeDirection,
                tradeDirectVo.getTradeOffset(), tradeDirectVo.getTradeDirect(), cashOrder.getSellEntrustCount(),
                entrustPrice, entrustType, limitedPriceValue, now, now);
        //触发方式
        orderCashEntrustInfo.setTriggerType(cashOrder.getSellTriggerType());

        return orderCashEntrustInfo;
    }


    /**
     * TODO 查询订单主数据信息
     *
     * @param orderVo
     * @return
     * @author XieZhibing
     * @date 2016年12月13日 上午9:27:50
     */
    private OrderCashInfo queryCashOrdersInfo(OrderVo orderVo) throws LTException {

        //订单显示ID
        String orderId = orderVo.getOrderId();
        //查询缓存数据
        OrderCashInfo cashOrder = OrderCashInfoCache.get(orderId);
        if (cashOrder == null || cashOrder.getBuySuccessCount() == 0 || cashOrder.getHoldCount() == 0) {//如果买成功数为0，查下数据库
            //查询数据库中订单基本数据
            cashOrder = orderCashInfoDao.queryByOrderId(orderId);
        }

        if (cashOrder == null) {
            throw new LTException("查不到订单, orderId:{" + orderId + "}");
        }

        logger.info("=====执行queryCashOrdersInfo=cashOrder={}====", JSONObject.toJSONString(cashOrder));

        //不是持仓中订单，打回
        if (!(cashOrder.getHoldCount() > 0 && (cashOrder.getSellEntrustCount() == null || cashOrder.getSellEntrustCount() == 0))) {
            removeRiskControlOrder(cashOrder);
            throw new LTException(LTResponseCode.TDJ0003);
        }

        //产品编码
        String productCode = cashOrder.getProductCode();
        //产品
        ProductVo productVo = productInfoServiceImpl.queryCheckProduct(productCode);
        orderVo.setProductVo(productVo);
        //产品价格小数位数
        Integer scale = productVo.getDecimalDigits();

        cashOrder.setUserCommitSellDate(orderVo.getUserSaleDate());


        //价格
        double quotePrice = orderVo.getUserSalePrice();
        //系统提交卖出价
        if (Objects.equals(cashOrder.getPlate(), PlateEnum.INNER_PLATE.getValue())) {
            quotePrice = tradeServer.getInnerFutureTrade().getQuotePrice(cashOrder.getProductCode()).getLastPrice();
        } else if (Objects.equals(cashOrder.getPlate(), PlateEnum.OUTER_PLATE.getValue())) {
            quotePrice = tradeServer.getOuterFutureTrade().getQuotePrice(cashOrder.getProductCode()).getLastPrice();
        } else if (Objects.equals(cashOrder.getPlate(), PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue())) {
            quotePrice = tradeServer.getContractTrade().getQuotePrice(cashOrder.getProductCode()).getLastPrice();
        }
        quotePrice = DoubleTools.scaleFormat(quotePrice, scale);

        //用户提交卖出价格
        cashOrder.setUserCommitSellPrice(quotePrice);
        cashOrder.setUserCommitSellDate(orderVo.getUserSaleDate());
        //卖出触发方式
        cashOrder.setSellTriggerType(orderVo.getSellTriggerType());
        //设置卖出委托数量
        cashOrder.setSellEntrustCount(cashOrder.getHoldCount());
        //平仓成功数量
        cashOrder.setSellSuccessCount(0);
        cashOrder.setModifyUserId(orderVo.getModifyUserId());
        return cashOrder;
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

    /**
     * 设置 交易服务
     *
     * @param tradeServer 交易服务
     */
    public void setTradeServer(TradeServer tradeServer) {
        this.tradeServer = tradeServer;
    }

    @Override
    public OrderCashInfoDao getOrderCashInfoDao() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderCashEntrustInfoDao getOrderCashEntrustInfoDao() {
        // TODO Auto-generated method stub
        return null;
    }

}
