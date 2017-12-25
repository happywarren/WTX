/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.rmi
 * FILE    NAME: OrderApiServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.business.product.IProductRiskConfigService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.api.user.IUserApiLogService;
import com.lt.api.user.IUserApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.product.ProductMarketEnum;
import com.lt.enums.product.ProductTypeEnum;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.fund.FundMainCash;
import com.lt.model.product.ProductRiskConfig;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.InvestorAccount;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserContant;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.trade.ProductTimeCache;
import com.lt.trade.TradeServer;
import com.lt.trade.defer.DeferInterestScheduTask;
import com.lt.trade.defer.service.DeferService;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.dao.DigitalCoinPositionCountManageDao;
import com.lt.trade.order.dao.DigitalProductBrandPositionManageDao;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.service.IOrderBusinessService;
import com.lt.trade.order.service.IOrderBuyTradeService;
import com.lt.trade.order.service.IOrderFunctionService;
import com.lt.trade.order.service.IOrderSellTradeService;
import com.lt.trade.order.service.impl.cash.AbstractCashOrderBuyReceiptService;
import com.lt.trade.order.service.impl.cash.AbstractCashOrderSellReceiptService;
import com.lt.trade.riskcontrol.RiskControlQueue;
import com.lt.trade.riskcontrol.bean.MutableStopLossBean;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.trade.tradeserver.BaseTrade;
import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import com.lt.trade.tradeserver.bean.DigitalProductBrandPosition;
import com.lt.trade.tradeserver.bean.FutureOrderBean;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.trade.utils.LTConstants;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.util.utils.redis.RedisInfoOperate;
import com.lt.vo.fund.FundOrderVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * 现金订单下发
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年12月8日 下午8:54:17
 */
@Service
public class OrderApiServiceImpl implements IOrderApiService {

    private static final long serialVersionUID = 3102459504571133638L;

    private static Logger logger = LoggerFactory.getLogger(OrderApiServiceImpl.class);

    @Autowired
    private IOrderBuyTradeService cashOrderBuyTradeServiceImpl;
    @Autowired
    private IOrderSellTradeService cashOrderSellTradeServiceImpl;
    @Autowired
    private IOrderFunctionService orderFunctionService;
    @Autowired
    private IInvestorFeeCfgApiService investorFeeCfgApiService;
    @Autowired
    private IFundAccountApiService fundAccountService;
    @Autowired
    private IProductApiService productApiService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IOrderBusinessService orderBusinessService;
    @Autowired
    private TradeServer tradeService;
    @Autowired
    private AbstractCashOrderBuyReceiptService cashOrderOuterBuyReceiptServiceImpl;
    @Autowired
    private AbstractCashOrderSellReceiptService cashOrderOuterSellReceiptServiceImpl;
    @Autowired
    private DeferInterestScheduTask deferInterestScheduTask;
    @Autowired
    private IUserApiLogService userLogServiceImpl;
    @Autowired
    private OrderCashInfoDao orderCashInfoDao;
    @Autowired
    private IUserApiLogService iUserApiLogService;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IProductRiskConfigService productRiskConfigService;

    @Autowired
    private DigitalCoinPositionCountManageDao positionCountManageDao;
    @Autowired
    private ProductTimeCache productTimeCache;

    @Autowired
    private DigitalProductBrandPositionManageDao brandPositionManageDao;


    @Autowired
    private DeferService deferservice;
    @Resource(name = "subscriptionProducer")
    private MessageQueueProducer subscriptionProducer;

    /**
     * @param orderVo 订单信息
     * @author XieZhibing
     * @date 2016年12月12日 下午8:37:41
     */
    @Override
    public void buy(OrderVo orderVo) throws LTException {
        //资金类型: 0 现金; 1 积分
        Integer fundType = orderVo.getFundType();
        //产品参数
        ProductVo productVo = orderVo.getProductVo();
        String productTypeCode = productVo.getProductTypeCode();
        Integer plate = productVo.getPlate();
        //产品编码
        String productCode = orderVo.getProductCode();

        //如果是差价合约，就判断是否会超过单户最大净持仓量
        adjustMaxPositionPer(orderVo, fundType, productTypeCode);

        //如果是差价合约，就判断是否会超过品牌最大净持仓量
        adjustMaxBrandPosition(orderVo, fundType, productTypeCode);

        //判断是否重复下单
        if (isDuplicateBuy(orderVo.getUserId(), orderVo.getExternalId())) {
            throw new LTException(LTResponseCode.TDJ0002);
        }

        //内盘判断涨跌停 不可开仓
        if (null != plate && plate.intValue() == PlateEnum.INNER_PLATE.getValue()) {
            ProductPriceBean productPriceBean = tradeService.getInnerFutureTrade().getQuotePrice(productCode);
            if (null != productPriceBean) {
                double changeRate = Math.abs(productPriceBean.getChangeRate());
                Double limit = productVo.getFloatLimit();
                if (null == limit) {
                    limit = 0.06D;
                }
                double floatLimit = DoubleTools.mul(limit, 100D);
                logger.info("商品: {} 涨跌幅: {} 行情浮动限制: {} ", productCode, productPriceBean.getChangeRate(), limit);
                if (DoubleTools.sub(changeRate, floatLimit) >= 0D) {
                    throw new LTException(LTResponseCode.PR00017);
                }
            }
        }


        //递延状态: 0 非递延; 1 递延
        int deferStatus = orderVo.getDeferStatus();
        logger.info("递延状态: 0 非递延; 1 递延, deferStatus:{}", deferStatus);

        //查询系统清仓时间
        Date sysSetSaleDate = TradeUtils.querySysSetSaleDate(productCode, deferStatus, redisTemplate);
        logger.info("查询系统清仓时间 sysSetSaleDate:{}", sysSetSaleDate);
        if (sysSetSaleDate == null) {
            throw new LTException(LTResponseCode.PR00010);
        }
        //递延单判断合约到期时间
       /* if (deferStatus == DeferStatusEnum.DEFER.getValue()) {
            //合约到期时间
            String expirationTime = productVo.getExpirationTime();
            //转化合约到期时间
            Date expirationDate = DateTools.parseDate(expirationTime, DateTools.DEFAULT_DATE_TIME);
            //清仓时间不能大于 (合约到期时间-3天)
            if (DateTools.compareTo(new Date(), DateTools.subtract(expirationDate, 3))) {
                //即将到期商品不能开启递延
                throw new LTException(LTResponseCode.PR00016);
            }

            //是否有大于3天的长假期:
            boolean haveHoliday = true;
            ;
            try {
                haveHoliday = productApiService.isContinueThreeDayHoliday(sysSetSaleDate, orderVo.getProductVo().getExchangeCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("是否有大于3天的长假期 haveHoliday:{}", haveHoliday);

            if (haveHoliday) {
                throw new LTException(LTResponseCode.TDY0001);
            }
        }
*/

        //设置本交易时间段的清仓时间
        orderVo.setSysSetSaleDate(sysSetSaleDate);

        //现金订单 和 积分订单分发
        logger.info("订单分发, 资金类型: 0 现金, 1 积分, fundType:{}", fundType);
        if (FundTypeEnum.CASH.getValue() == fundType) {
            cashOrderBuyTradeServiceImpl.doBuy(orderVo);
        } else {
            throw new LTException(LTResponseCode.TD00003);
        }

        //插入用户操作日志,记录用户真实下单时间和行情价
        if (StringTools.isNotEmpty(orderVo.getOrderId())) {
            OrderLossProfitDefLog lossProfitDefLog = new OrderLossProfitDefLog();
            lossProfitDefLog.setUserId(orderVo.getUserId());
            lossProfitDefLog.setOrderId(orderVo.getOrderId());
            lossProfitDefLog.setCreateTime(new Date());
            String operateLog = orderVo.getOrderTime() + UserContant.ORDER_SUBMIT_BUY_LOG + orderVo.getUserBuyPrice();
            lossProfitDefLog.setContent(operateLog);
            iUserApiLogService.insertUserOrderLossProfitDeferLog(lossProfitDefLog);
        }
    }

    /**
     * 如果是差价合约，就判断是否会超过品牌最大净持仓
     *
     * @param orderVo         订单信息
     * @param fundType        订单类型 现金、积分
     * @param productTypeCode 商品类型编码
     */
    private void adjustMaxBrandPosition(OrderVo orderVo, Integer fundType, String productTypeCode) {
        //品牌 id
        String brandId = orderVo.getBrandId();
        String productCode = orderVo.getProductCode();
        logger.info("--------------brandId--{},productCode--{}---------", brandId, productCode);
        if (productTypeCode != null) {
            if (ProductTypeEnum.CONTRACT.getCode().equals(productTypeCode)) {
                DigitalProductBrandPosition coinPosition = new DigitalProductBrandPosition();
                coinPosition.setProductCode(productCode);
                coinPosition.setBrandId(brandId);

                //品牌最大净持仓量
                Integer maxBrandPosition = orderVo.getProductVo().getBrandPosition();
                logger.info("--------------品牌最大净持仓量maxBrandPosition--{}---------", maxBrandPosition);

                if (FundTypeEnum.CASH.getValue() == fundType) {
                    coinPosition = brandPositionManageDao.selectCoinPosition(coinPosition);
                } else {
                    throw new LTException(LTResponseCode.TD00003);
                }
                logger.info("--------------coinPosition--{}---------", coinPosition);
                Integer tradeDirection = orderVo.getTradeDirection();

                String msg;

                if (coinPosition != null) {
                    //改动之前的品牌净头寸
                    Integer oldPosition = Math.abs(coinPosition.getBuyCount() - coinPosition.getSellCount());
                    logger.info("-------oldPosition:  {}-------", oldPosition);
                    //改动之后的品牌净头寸
                    Integer netPosition;

                    if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
                        netPosition = Math.abs(coinPosition.getBuyCount() + orderVo.getCount() - coinPosition.getSellCount());
                        msg = "下单失败，该品种系统性风险过大，限制开[多仓]，但可以继续开[空仓] 和平仓操作。";
                    } else if (tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue()) {
                        netPosition = Math.abs(coinPosition.getBuyCount() - coinPosition.getSellCount() - orderVo.getCount());
                        msg = "下单失败，该品种系统性风险过大，限制开[空仓]，但可以继续开[多仓] 和平仓操作。";
                    } else {
                        throw new LTException(LTResponseCode.TD00002);
                    }
                    if (maxBrandPosition != null) {
                        logger.info("-------netPosition:  {}-------", netPosition);
                        if (oldPosition <= netPosition) {
                            if (netPosition > maxBrandPosition) {
//                                throw new LTException(LTResponseCode.TDJ0005);
                                throw new LTException(msg);
                            }
                        }
                    }
                } else {
                    if (maxBrandPosition != null) {
                        if (orderVo.getCount() > maxBrandPosition) {
//                            throw new LTException(LTResponseCode.TDJ0005);
                            if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
                                msg = "下单失败，该品种系统性风险过大，限制开[多仓]，但可以继续开[空仓] 和平仓操作。";
                            } else if (tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue()) {
                                msg = "下单失败，该品种系统性风险过大，限制开[空仓]，但可以继续开[多仓] 和平仓操作。";
                            } else {
                                throw new LTException(LTResponseCode.TD00002);
                            }
                            throw new LTException(msg);
                        }
                    }
                }
            }
        }
    }

    /**
     * 如果是差价合约，就判断是否会超过单户最大净持仓
     *
     * @param orderVo         订单信息
     * @param fundType        订单类型 现金、积分
     * @param productTypeCode 商品类型编码
     */
    private void adjustMaxPositionPer(OrderVo orderVo, Integer fundType, String productTypeCode) {
        if (productTypeCode != null) {
            if (ProductTypeEnum.CONTRACT.getCode().equals(productTypeCode)) {
                DigitalCoinPosition coinPosition = new DigitalCoinPosition();
                coinPosition.setUserId(orderVo.getUserId());
                coinPosition.setProductCode(orderVo.getProductCode());
                coinPosition.setInvestorId(orderVo.getInvestorId());
                Integer maxPositionPer = orderVo.getProductVo().getMaxPositionPerAccount();//单户最大净持仓量

                if (FundTypeEnum.CASH.getValue() == fundType) {
                    coinPosition = positionCountManageDao.selectCoinPosition(coinPosition);
                } else {
                    throw new LTException(LTResponseCode.TD00003);
                }

                Integer tradeDirection = orderVo.getTradeDirection();

                if (coinPosition != null) {
                    //TODO 产品确认一下，净头寸
                    //改动之前的净头寸
                    Integer oldPosition = Math.abs(coinPosition.getBuyCount() - coinPosition.getSellCount());
                    //改动之后的净头寸
                    Integer netPosition;
                    if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
                        netPosition = Math.abs(coinPosition.getBuyCount() + orderVo.getCount() - coinPosition.getSellCount());
                    } else if (tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue()) {
                        netPosition = Math.abs(coinPosition.getBuyCount() - coinPosition.getSellCount() - orderVo.getCount());
                    } else {
                        throw new LTException(LTResponseCode.TD00002);
                    }

                    if (oldPosition < netPosition) {
                        if (netPosition > maxPositionPer) {
                            if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
                                throw new LTException("下单失败，您在该品种的净持仓量为" + oldPosition + "手，允许最多净持仓量为"
                                        + maxPositionPer + "手。为了控制风险，不得超额开[多仓]，但可以继续开[空仓]和平仓操作。");
                            } else if (tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue()) {
                                throw new LTException("下单失败，您在该品种的净持仓量为" + oldPosition + "手，允许最多净持仓量为"
                                        + maxPositionPer + "手。为了控制风险，不得超额开[空仓]，但可以继续开[多仓]和平仓操作。");
                            } else {
                                throw new LTException(LTResponseCode.TD00002);
                            }
//                                throw new LTException(LTResponseCode.TDJ0004);
                        }
                    }

                } else {
                    if (orderVo.getCount() > maxPositionPer) {
//                        throw new LTException(LTResponseCode.TDJ0004);
                        if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
                            throw new LTException("下单失败，您在该品种的净持仓量为" + 0 + "手，允许最多净持仓量为"
                                    + maxPositionPer + "手。为了控制风险，不得超额开[多仓]，但可以继续开[空仓]和平仓操作。");
                        } else if (tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue()) {
                            throw new LTException("下单失败，您在该品种的净持仓量为" + 0 + "手，允许最多净持仓量为"
                                    + maxPositionPer + "手。为了控制风险，不得超额开[空仓]，但可以继续开[多仓]和平仓操作。");
                        } else {
                            throw new LTException(LTResponseCode.TD00002);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param orderVo
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月12日 下午8:37:52
     * @see com.lt.api.trade.IOrderApiService#sell(com.lt.vo.trade.OrderVo)
     */
    @Override
    public void sell(OrderVo orderVo) throws LTException {
        //资金类型: 0 现金; 1 积分
        Integer fundType = orderVo.getFundType();

        //现金订单 和 积分订单分发
        if (FundTypeEnum.CASH.getValue() == fundType) {
            cashOrderSellTradeServiceImpl.doSell(orderVo);
        } else {
            throw new LTException(LTResponseCode.TD00003);
        }

        //插入用户操作日志,记录用户真实下单时间和行情价
        if (StringTools.isNotEmpty(orderVo.getOrderId())) {
            OrderLossProfitDefLog lossProfitDefLog = new OrderLossProfitDefLog();
            lossProfitDefLog.setUserId(orderVo.getUserId());
            lossProfitDefLog.setOrderId(orderVo.getOrderId());
            lossProfitDefLog.setCreateTime(new Date());
            ProductVo productVo = orderVo.getProductVo();
            double quotePrice = orderVo.getUserSalePrice();
            if (productVo != null) {
                quotePrice = DoubleTools.scaleFormat(quotePrice, productVo.getDecimalDigits());
            }
            String operateLog = orderVo.getOrderTime() + UserContant.ORDER_SUBMIT_SELL_LOG + quotePrice;
            lossProfitDefLog.setContent(operateLog);
            iUserApiLogService.insertUserOrderLossProfitDeferLog(lossProfitDefLog);
        }
    }

    /**
     * TODO 批量平仓
     *
     * @param fundType
     * @param userId
     * @param productCode
     * @throws LTException
     * @author XieZhibing
     * @date 2017年1月4日 上午10:19:16
     */
    @Override
    public void sellAll(Integer fundType, String userId, String productCode) throws LTException {

        //现金订单 和 积分订单分发
        if (FundTypeEnum.CASH.getValue() == fundType) {
            cashOrderSellTradeServiceImpl.doSell(userId, productCode);
        } else {
            throw new LTException(LTResponseCode.TD00003);
        }
    }

    /**
     * 用户修改止盈止损，开启关闭递延
     *
     * @param log
     * @param orderIdStr
     * @param deferStatu
     * @param stopLoss
     * @param stopfit
     * @throws LTException
     * @author yuanxin
     * @date 2017年1月3日 下午6:59:19
     */
    @Override
    public void doUpdateCashDefAndLossProfit(OrderLossProfitDefLog log, String orderIdStr, String deferStatu, String stopLoss, String stopfit, String trailStopLoss) throws LTException {
        String userIdStr = log.getUserId();

        logger.info("查询用户订单信息，订单编号：{}", orderIdStr);

        try {
            OrderCashInfo cashOrdersInfo = orderFunctionService.getCashOrderInfoById(orderIdStr);
            logger.info("订单信息为：{}", JSONObject.toJSONString(cashOrdersInfo));
            if (cashOrdersInfo == null) {
                throw new LTException(LTResponseCode.US00004);
            }
            //不是持仓中订单，打回
            if (!(cashOrdersInfo.getHoldCount() > 0 && (cashOrdersInfo.getSellEntrustCount() == null || cashOrdersInfo.getSellEntrustCount() == 0))) {
                throw new LTException(LTResponseCode.TDJ0003);
            }

            log.setOrderId(cashOrdersInfo.getOrderId());

            //缓存中查询产品信息
            ProductVo productVo = productApiService.getProductInfo(cashOrdersInfo.getProductCode());
            //汇率
            Double rate = cashOrdersInfo.getRate();
            if (productVo.getJumpPrice() == null || productVo.getJumpValue() == 0) {
                logger.info("最小波动点:{}, 最小变动价格:{}", productVo.getJumpValue(), productVo.getJumpPrice());
                throw new LTException(LTResponseCode.PR00011);
            }

            if (productVo.getMarketStatus() == ProductMarketEnum.STOP_TRADING.getValue() || productVo.getMarketStatus() == ProductMarketEnum.ONLY_LIQUIDATION.getValue()
                    || productVo.getMarketStatus() == ProductMarketEnum.REST.getValue()) {
                logger.debug("市场状态为：{}", productVo.getMarketStatus());
                throw new LTException(LTResponseCode.TRY0000);
            }

            logger.debug("获取费用配置信息");
            InvestorFeeCfg investorFeeCfg = investorFeeCfgApiService.getInvestorFeeCfgSecurityCode(cashOrdersInfo.getInvestorId(), cashOrdersInfo.getProductId(), cashOrdersInfo.getSecurityCode(), cashOrdersInfo.getTradeDirection());
            //获取用户信息
            UserBaseInfo info = userApiService.findUserByUserId(cashOrdersInfo.getUserId());
            info.setRiskLevel(info.getRiskLevel() == null ? "4" : info.getRiskLevel());//默认高风险
            //获取商品区间配置
            List<ProductRiskConfig> productRiskConfigList = productRiskConfigService.queryProductRiskConfig(productVo.getId(), Integer.valueOf(info.getRiskLevel()));
            if (productRiskConfigList != null && productRiskConfigList.size() > 0) {
                ProductRiskConfig productRiskConfig = productRiskConfigList.get(0);
                investorFeeCfg.setStopLossRange(productRiskConfig.getStopLossRange() == null ? investorFeeCfg.getStopLossRange() : productRiskConfig.getStopLossRange());
                investorFeeCfg.setStopProfitRange(productRiskConfig.getStopProfitRange() == null ? investorFeeCfg.getStopProfitRange() : productRiskConfig.getStopProfitRange());
                investorFeeCfg.setSurcharge(productRiskConfig.getSurcharge() == null ? investorFeeCfg.getSurcharge() : productRiskConfig.getSurcharge());
                investorFeeCfg.setDeferFee(productRiskConfig.getDeferFee() == null ? investorFeeCfg.getDeferFee() : productRiskConfig.getDeferFee());
                investorFeeCfg.setDeferFund(productRiskConfig.getDeferFund() == null ? investorFeeCfg.getDeferFund() : productRiskConfig.getDeferFund());
            }

            logger.debug("费用配置信息为：{}，获取本次执行的操作", JSONObject.toJSONString(investorFeeCfg));
            int operate = orderFunctionService.checkIfDeforLossAndProfit(investorFeeCfg, cashOrdersInfo, deferStatu, stopLoss, stopfit, trailStopLoss, FundTypeEnum.CASH.getValue(), log);
            logger.info("本次执行的操作为：{},获取用户的资金账户信息", operate);
            FundMainCash userMainCash = fundAccountService.queryFundMainCash(userIdStr);
            logger.debug("查询的用户资金账户信息为：{}", userMainCash);
            double mainCash = userMainCash.getBalance();

            if (operate == 0) {
                throw new LTException(LTResponseCode.PR00003);
            }

            // 风控通知对象
            RiskControlBean riskBean = new RiskControlBean();
            riskBean.setUniqueOrderId(cashOrdersInfo.getOrderId());
            riskBean.setProductName(cashOrdersInfo.getProductCode());
            // 设置修改的订单对象
            OrderCashInfo orderInfo = new OrderCashInfo();
            orderInfo.setId(cashOrdersInfo.getId());
            orderInfo.setDisplay(cashOrdersInfo.getDisplay());
            orderInfo.setOrderId(cashOrdersInfo.getOrderId());
            //封装资金对象
            FundOrderVo fundOrder = new FundOrderVo(FundTypeEnum.CASH, cashOrdersInfo.getProductName(), cashOrdersInfo.getOrderId(), cashOrdersInfo.getUserId(),
                    0.0, 0.0, 0.0);

            logger.debug("进入修改止盈止损流程：{}", operate);
            String opera = String.valueOf(operate);

            //操作包含止盈止损 1代表止盈 2代表止损 3代表 递延 4，移动止损
            if (opera.contains("1") || opera.contains("2")) {
                setRiskControlBeanCash(cashOrdersInfo.getOrderId(), riskBean, cashOrdersInfo);
                logger.info("riskBean:{}", JSONObject.toJSONString(riskBean));
                logger.info("orderBean:{}", JSONObject.toJSONString(riskBean.getOrderBean()));
                if (opera.contains("1")) {
                    orderFunctionService.setCashOrderStopProfit(Double.valueOf(stopfit), orderInfo, cashOrdersInfo, investorFeeCfg, productVo);
                    fundOrder.setHoldFund(0);
                    riskBean.setStopLossPrice(cashOrdersInfo.getStopLossPrice());
                    riskBean.setStopGainPrice(orderInfo.getStopProfitPrice());
                }

                if (opera.contains("2")) {
                    Integer defstatus = opera.contains("3") ? Integer.parseInt(deferStatu) : null;
                    Double cashAmt = orderBusinessService.findFloatCashAmtByUserId(orderInfo);
                    cashAmt = cashAmt == null || cashAmt > 0 ? 0.0 : cashAmt;
                    orderFunctionService.setCashOrderStopLoss(Double.valueOf(stopLoss), orderInfo, cashOrdersInfo, investorFeeCfg, productVo, defstatus, mainCash, cashAmt);

                    riskBean.setStopLossPrice(orderInfo.getStopLossPrice());
                    riskBean.setStopGainPrice(orderInfo.getStopProfitPrice() == null ? cashOrdersInfo.getStopProfitPrice() : orderInfo.getStopProfitPrice());
                    fundOrder.setHoldFund(orderInfo.getActualHoldFund() == null ? 0.0 : orderInfo.getActualHoldFund());
                }

                //通知资金扣款
                fundOrder.setInvestorId(cashOrdersInfo.getInvestorId());
                fundOrder.setStopProfit(orderInfo.getStopProfit() == null ? 0.0 : orderInfo.getStopProfit());
            }

            if (opera.contains("3")) {
                orderFunctionService.setCashOrderDef(Integer.parseInt(deferStatu), orderInfo, cashOrdersInfo, investorFeeCfg, productVo, mainCash);
                riskBean.setDeferredOrderTimeStamp(orderInfo.getSysSetSellDate().getTime());
                fundOrder.setDeferFund(DoubleTools.mul(DoubleTools.mul(investorFeeCfg.getDeferFund(), rate), cashOrdersInfo.getHoldCount()));
            }

            if (opera.contains("4")) {
                orderInfo.setTrailStopLoss(Integer.parseInt(trailStopLoss));
                logger.info("=====操作移动止损状态：trailStopLoss={}====", trailStopLoss);
                orderFunctionService.setCashRiskBeanTrailLoss(riskBean, cashOrdersInfo, opera, Integer.parseInt(trailStopLoss));
            }
            logger.info("风控传参为：{}", JSONObject.toJSONString(riskBean));

            int i = orderFunctionService.updateCashOrderInfo(orderInfo);
            if (i == 0) {
                logger.info("订单更新异常，事务回滚");
                throw new Exception();
            }

            //判断风控是否成功
            boolean riskFlag = true;
            if (cashOrdersInfo.getPlate().equals(PlateEnum.INNER_PLATE.getValue())) {
                if (opera.contains("1") || opera.contains("2")) {

                    riskFlag = tradeService.getInnerFutureTrade().updateRiskControlOrder(riskBean);
                }
                if (opera.contains("3")) {

                    riskFlag = tradeService.getInnerFutureTrade().updateDeferredOrder(riskBean);
                }
                if (opera.contains("4")) {

                    tradeService.getInnerFutureTrade().switchMutableStopLossOrder(riskBean);
                }
            } else if (cashOrdersInfo.getPlate().equals(PlateEnum.OUTER_PLATE.getValue())) {
                if (opera.contains("1") || opera.contains("2")) {

                    riskFlag = tradeService.getOuterFutureTrade().updateRiskControlOrder(riskBean);
                }
                if (opera.contains("3")) {

                    riskFlag = tradeService.getOuterFutureTrade().updateDeferredOrder(riskBean);
                }
                if (opera.contains("4")) {

                    tradeService.getOuterFutureTrade().switchMutableStopLossOrder(riskBean);
                }
            } else if (cashOrdersInfo.getPlate().equals(PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue())) {
                if (opera.contains("1") || opera.contains("2")) {
                    riskFlag = tradeService.getContractTrade().updateRiskControlOrder(riskBean);
                }
                if (opera.contains("3")) {
                    riskFlag = tradeService.getContractTrade().updateDeferredOrder(riskBean);
                }
                if (opera.contains("4")) {
                    tradeService.getContractTrade().switchMutableStopLossOrder(riskBean);
                }
            }

            if (riskFlag) {
                if (opera.contains("1") || opera.contains("2")) {
                    logger.debug("进入到止盈止损资金修改");
                    fundAccountService.updateProfitLoss(fundOrder);
                }

                if (opera.contains("3")) {
                    logger.debug("进入到递延资金修改:{}", JSONObject.toJSONString(fundOrder));
                    fundOrder.setDeferFund(fundOrder.getDeferFund());
                    fundAccountService.updateDefer(deferStatu.equals("1"), fundOrder);
                }

                //插入用户操作日志
                log.setCreateTime(new Date());
                userLogServiceImpl.insertUserOrderLossProfitDeferLog(log);
            } else {
                throw new LTException(LTResponseCode.TD00006);
            }
            //刷新订单缓存
            updateCacheByCash(orderInfo);
        } catch (Exception e) {
            logger.info("修改止盈止损异常,e={}", e);
            e.printStackTrace();
            throw new LTException(e.getMessage());
        }
    }


    /**
     * 更新内存实体信息 现金
     *
     * @param orderInfo
     */
    private void updateCacheByCash(OrderCashInfo orderInfo) {
        OrderCashInfo info = OrderCashInfoCache.get(orderInfo.getOrderId());
        if (!StringTools.isNotEmpty(info)) {
            return;
        }
        //递延相关
        if (StringTools.isNotEmpty(orderInfo.getPerDeferFund())) {
            info.setPerDeferFund(orderInfo.getPerDeferFund());
        }
        if (StringTools.isNotEmpty(orderInfo.getDeferStatus())) {
            info.setDeferStatus(orderInfo.getDeferStatus());
        }
        if (StringTools.isNotEmpty(orderInfo.getActualDeferFund())) {
            info.setActualDeferFund(orderInfo.getActualDeferFund());
        }
        //保证金
        //每手止损保证金参数(原币种)
        if (StringTools.isNotEmpty(orderInfo.getPerSurcharge())) {
            info.setPerSurcharge(orderInfo.getPerSurcharge());
        }
        //止盈
        if (StringTools.isNotEmpty(orderInfo.getStopProfit())) {
            info.setStopProfit(orderInfo.getStopProfit());
        }
        if (StringTools.isNotEmpty(orderInfo.getStopProfitPrice())) {
            info.setStopProfitPrice(orderInfo.getStopProfitPrice());
        }
        //每手止盈金额(原币种)
        if (StringTools.isNotEmpty(orderInfo.getPerStopProfit())) {
            info.setPerStopProfit(orderInfo.getPerStopProfit());
        }
        //止损
        if (StringTools.isNotEmpty(orderInfo.getStopLoss())) {
            info.setStopLoss(orderInfo.getStopLoss());
        }

        if (StringTools.isNotEmpty(orderInfo.getStopLossPrice())) {
            info.setStopLossPrice(orderInfo.getStopLossPrice());
        }
        //每手止损金额(原币种)
        if (StringTools.isNotEmpty(orderInfo.getPerStopLoss())) {
            info.setPerStopLoss(orderInfo.getPerStopLoss());
        }
        //有更改追踪止损状态
        if (!StringTools.isEmpty(orderInfo.getTrailStopLoss())) {
            info.setTrailStopLoss(orderInfo.getTrailStopLoss());
        }
        //实扣保证金
        if (StringTools.isNotEmpty(orderInfo.getActualHoldFund())) {
            info.setActualHoldFund(orderInfo.getActualHoldFund());
        }
        //应扣保证金
        if (StringTools.isNotEmpty(orderInfo.getShouldHoldFund())) {
            info.setShouldHoldFund(orderInfo.getShouldHoldFund());
        }
        OrderCashInfoCache.put(info);
    }

    /**
     * 设置通知风控的订单修改止盈止损递延的对象
     *
     * @param riskBean
     * @param cashOrdersInfo
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2016年12月17日 下午1:54:17
     */
    public void setRiskControlBeanCash(String displayId, RiskControlBean riskBean, OrderCashInfo cashOrdersInfo) {
        TradeDirectVo tradeDirect = new TradeDirectVo(cashOrdersInfo.getTradeDirection(), TradeTypeEnum.BUY.getValue());
        riskBean.setProductName(cashOrdersInfo.getProductCode());
        riskBean.setUniqueOrderId(displayId);
        riskBean.setDirect(tradeDirect.getTradeDirect());
        riskBean.setDeferredOrderTimeStamp(cashOrdersInfo.getSysSetSellDate().getTime());
        riskBean.setMatchPrice(cashOrdersInfo.getBuyAvgPrice());
        //1开 0关
        riskBean.setMutableStopLoss(cashOrdersInfo.getTrailStopLoss() == 1);
        riskBean.setStopGainPrice(0.0);
        riskBean.setStopLossPrice(0.0);
        FutureOrderBean orderBean = new FutureOrderBean(cashOrdersInfo.getUserId(), cashOrdersInfo.getSecurityCode(), cashOrdersInfo.getExchangeCode(), cashOrdersInfo.getProductCode(),
                cashOrdersInfo.getOrderId(), 888888, cashOrdersInfo.getHoldCount(), cashOrdersInfo.getBuyAvgPrice(),
                tradeDirect.getTradeDirect(), LTConstants.TRADE_OFFSET_CLOSE, LTConstants.TRADE_ORDER_TYPE_MARKET, LTConstants.FUND_TYPE_CASH,
                cashOrdersInfo.getCreateDate().getTime());
        riskBean.setOrderBean(orderBean);
        logger.info("setRiskControlBeanCash riskBean: {}", JSONObject.toJSONString(riskBean));
        logger.info("setRiskControlBeanCash orderBean: {}", JSONObject.toJSONString(orderBean));
    }


    @Override
    public Map<String, Double> findFloatAmtByUserId(String userId) {
        Map<String, Double> map = new HashMap<String, Double>();
        OrderCashInfo cashInfo = new OrderCashInfo();
        cashInfo.setUserId(userId);
        Double cashAmt = orderBusinessService.findFloatCashAmtByUserId(cashInfo);
        map.put("floatCashAmt", cashAmt);
        return map;
    }


    @Override
    public List<PositionOrderVo> findPositionOrderByUserId(String userId, Integer fundType) {
        List<PositionOrderVo> list = orderBusinessService.findPositionOrderByUserId(userId, fundType);
        if (null == list || list.size() == 0) {
            return null;
        }
        Set<String> set = new HashSet<String>();
        List<String> displayIdList = new ArrayList<String>();

        for (PositionOrderVo positionOrderVo : list) {
            if (null == positionOrderVo) {
                continue;
            }
            try {
                positionOrderVo.setPerStopLoss(DoubleTools.mulDec2(positionOrderVo.getPerStopLoss(), Double.valueOf(positionOrderVo.getHoldCount())));
                positionOrderVo.setPerStopProfit(DoubleTools.mulDec2(positionOrderVo.getPerStopProfit(), Double.valueOf(positionOrderVo.getHoldCount())));
                set.add(positionOrderVo.getProductCode());

                ProductPriceBean bean = null;
                MutableStopLossBean mbean = null;
                if (Objects.equals(PlateEnum.INNER_PLATE.getValue(), positionOrderVo.getPlate())) {
                    bean = tradeService.getInnerFutureTrade().getQuotePrice(positionOrderVo.getProductCode());
                    mbean = tradeService.getInnerFutureTrade().getMutableStopLossOrder(positionOrderVo.getProductCode(), positionOrderVo.getOrderId());

                } else if (Objects.equals(PlateEnum.OUTER_PLATE.getValue(), positionOrderVo.getPlate())) {
                    bean = tradeService.getOuterFutureTrade().getQuotePrice(positionOrderVo.getProductCode());
                    mbean = tradeService.getOuterFutureTrade().getMutableStopLossOrder(positionOrderVo.getProductCode(), positionOrderVo.getOrderId());
                } else if (Objects.equals(PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue(), positionOrderVo.getPlate())) {
                    bean = tradeService.getContractTrade().getQuotePrice(positionOrderVo.getProductCode());
                    mbean = tradeService.getContractTrade().getMutableStopLossOrder(positionOrderVo.getProductCode(), positionOrderVo.getOrderId());
                } else {
                    continue;
                }

                //通过订单号查询移动止损值
                logger.info("======================mbean={}===============", JSONObject.toJSONString(mbean));
                if (mbean != null) {
                    positionOrderVo.setMoveStopPrice(mbean.getSentinelPrice() + "");
                }
                //买一价、卖一价
                logger.info("==================bean={}========================", JSONObject.toJSONString(bean));
                if (null != bean) {
                    positionOrderVo.setBidPrice(bean.getBidPrice() + "");
                    positionOrderVo.setAskPrice(bean.getAskPrice() + "");
                }

                //调试, 记录displayId
                displayIdList.add(positionOrderVo.getOrderId());
            } catch (Exception e) {
                continue;
            }
        }
        logger.info("=========获取全品持仓数据: {}", JSONObject.toJSONString(displayIdList));
        List<String> strlist = new ArrayList<String>(set);
        logger.info("=========订阅数据: {}", JSONObject.toJSONString(strlist));

        //TODO 订阅品种 推送行情
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("list", strlist);
        subscriptionProducer.sendMessage(JSONObject.toJSONString(map));
        return list;
    }


    @Override
    public List<PositionOrderVo> findPositionOrderByUserAndProduct(String userId, String productCode, Integer fundType) {
        List<PositionOrderVo> list = orderBusinessService.findPositionOrderByUserAndProduct(userId, productCode, fundType);
        if (null == list || list.size() == 0) {
            return null;
        }
        Set<String> set = new HashSet<String>();
        List<String> displayIdList = new ArrayList<String>();

        for (PositionOrderVo positionOrderVo : list) {
            if (null == positionOrderVo) {
                continue;
            }
            try {
                positionOrderVo.setPerStopLoss(DoubleTools.mulDec2(positionOrderVo.getPerStopLoss(), Double.valueOf(positionOrderVo.getHoldCount())));
                positionOrderVo.setPerStopProfit(DoubleTools.mulDec2(positionOrderVo.getPerStopProfit(), Double.valueOf(positionOrderVo.getHoldCount())));
                set.add(positionOrderVo.getProductCode());

                ProductPriceBean bean = null;
                MutableStopLossBean mbean = null;
                if (Objects.equals(PlateEnum.INNER_PLATE.getValue(), positionOrderVo.getPlate())) {
                    bean = tradeService.getInnerFutureTrade().getQuotePrice(positionOrderVo.getProductCode());
                    mbean = tradeService.getInnerFutureTrade().getMutableStopLossOrder(positionOrderVo.getProductCode(), positionOrderVo.getOrderId());

                } else if (Objects.equals(PlateEnum.OUTER_PLATE.getValue(), positionOrderVo.getPlate())) {
                    bean = tradeService.getOuterFutureTrade().getQuotePrice(positionOrderVo.getProductCode());
                    mbean = tradeService.getOuterFutureTrade().getMutableStopLossOrder(positionOrderVo.getProductCode(), positionOrderVo.getOrderId());
                } else if (Objects.equals(PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue(), positionOrderVo.getPlate())) {
                    bean = tradeService.getContractTrade().getQuotePrice(positionOrderVo.getProductCode());
                    mbean = tradeService.getContractTrade().getMutableStopLossOrder(positionOrderVo.getProductCode(), positionOrderVo.getOrderId());
                } else {
                    continue;
                }

                //通过订单号查询移动止损值
                logger.info("======================mbean={}===============", JSONObject.toJSONString(mbean));
                if (mbean != null) {
                    positionOrderVo.setMoveStopPrice(mbean.getSentinelPrice() + "");
                }
                //买一价、卖一价
                logger.info("==================bean={}========================", JSONObject.toJSONString(bean));
                if (null != bean) {
                    positionOrderVo.setBidPrice(bean.getBidPrice() + "");
                    positionOrderVo.setAskPrice(bean.getAskPrice() + "");
                }

                //调试, 记录displayId
                displayIdList.add(positionOrderVo.getOrderId());
            } catch (Exception e) {
                continue;
            }
        }
        logger.info("=========获取全品持仓数据: {}", JSONObject.toJSONString(displayIdList));
        List<String> strlist = new ArrayList<String>(set);
        logger.info("=========订阅数据: {}", JSONObject.toJSONString(strlist));

        //TODO 订阅品种 推送行情
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("list", strlist);
        subscriptionProducer.sendMessage(JSONObject.toJSONString(map));
        return list;
    }

    @Override
    public List<SinglePositionOrderVo> findSinglePositionList(String userId, String productCode, Integer fundType) throws LTException {
        List<SinglePositionOrderVo> list = orderBusinessService.findSinglePositionList(userId, productCode, fundType);
        return list;
    }

    /**
     * TODO 查询用户委托中订单个数
     *
     * @param userId
     * @return
     */
    @Override
    public int findEntrustTheOrdersCount(String userId) {
        return orderBusinessService.findEntrustTheOrdersCount(userId);

    }

    /**
     * TODO 查询用户委托中订单
     *
     * @param userId
     * @return
     */
    @Override
    public List<EntrustVo> findEntrustTheOrdersList(String userId) {

        return orderBusinessService.findEntrustTheOrdersList(userId);
    }

    @Override
    public Integer queryOrdersCounts(String userId) {
        return orderBusinessService.queryOrdersCounts(userId);
    }

    @Override
    public Map<String, Map<String, Object>> findPosiOrderCount(String userId,
                                                               Integer fundType) {
        return orderBusinessService.findPosiOrderCount(userId, fundType);
    }

    // 管理商品信息(当进行新增商品/更换合约等操作时，供管理后台调用)
    // 新增商品时: newProductName为商品名称, 如CL1706或au1706, oldProductName为null
    // 删除商品时: newProductName为null, oldProductName为商品名称, 如CL1706或au1706
    // 更换合约时: newProductName为新合约名称, 如CL1706或au1706, oldProductName为原合约名称, 如CL1702或au1702
    // 如果newProductName为null, oldProductName为null, 则仅从数据库重新加载数据, 不影响风控队列
    @Override
    public boolean manageOuterProducts(String newProductName, String oldProductName) {
        return tradeService.getOuterFutureTrade().manageProducts(newProductName, oldProductName);
    }

    @Override
    public boolean manageInnerProducts(String newProductName, String oldProductName) {
        return tradeService.getInnerFutureTrade().manageProducts(newProductName, oldProductName);
    }

    @Override
    public boolean manageProducts(Integer plate, String newProductName, String oldProductName) {
        try {
            if (null == plate) {
                tradeService.getOuterFutureTrade().manageProducts(newProductName, oldProductName);
                tradeService.getInnerFutureTrade().manageProducts(newProductName, oldProductName);
                tradeService.getContractTrade().manageProducts(newProductName, oldProductName);
                return true;
            }
            if (plate.intValue() == PlateEnum.OUTER_PLATE.getValue()) {
                return tradeService.getOuterFutureTrade().manageProducts(newProductName, oldProductName);
            } else if (plate.intValue() == PlateEnum.INNER_PLATE.getValue()) {
                return tradeService.getInnerFutureTrade().manageProducts(newProductName, oldProductName);
            } else if (plate.intValue() == PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue()) {
                return tradeService.getContractTrade().manageProducts(newProductName, oldProductName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<OrderBalanceVo> findBalanceRecord(Map<String, Object> map) {
        return orderBusinessService.findBalanceRecord(map);
    }

    @Override
    public List<OrderEntrustVo> findEntrustRecord(Map<String, Object> map) {
        return orderBusinessService.findEntrustRecord(map);
    }

    @Override
    public List<Map<String, Object>> findEntrustAndSuccRecord(String orderId) {
        return orderBusinessService.findEntrustAndSuccRecord(orderId);
    }

    @Override
    public void fillRiskControlQueue(OrderCashInfo cashOrdersInfo) {
        cashOrderOuterBuyReceiptServiceImpl.fillRiskControlQueue(cashOrdersInfo);
    }

    @Override
    public void removeRiskControlOrder(OrderCashInfo cashOrdersInfo) {
        cashOrderOuterBuyReceiptServiceImpl.removeRiskControlOrder(cashOrdersInfo);
    }


    @Override
    public void fundBalance(OrderCashInfo cashOrdersInfo, Integer sellCount) {
        cashOrderOuterSellReceiptServiceImpl.fundBalance(cashOrdersInfo, sellCount);
    }


    /**
     * 判断是否重复下单
     *
     * @param userId
     * @param externalId
     * @return
     */
    private boolean isDuplicateBuy(String userId, String externalId) {
        return !RedisInfoOperate.setSuccess(redisTemplate, RedisUtil.ORDER_EXTERNAL_ID + ":" + externalId, userId);

    }


    @Override
    public Double getMoveStopLoss(String orderId, String proCode, Integer palte) {
        MutableStopLossBean mbean = null;
        if (Objects.equals(PlateEnum.INNER_PLATE.getValue(), palte)) {
            mbean = tradeService.getInnerFutureTrade().getMutableStopLossOrder(proCode, orderId);
        } else if (Objects.equals(PlateEnum.OUTER_PLATE.getValue(), palte)) {
            mbean = tradeService.getOuterFutureTrade().getMutableStopLossOrder(proCode, orderId);
        } else if (Objects.equals(PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue(), palte)) {
            mbean = tradeService.getContractTrade().getMutableStopLossOrder(proCode, orderId);
        }

        //通过订单号查询移动止损值
        logger.info("======================mbean={}===============", JSONObject.toJSONString(mbean));
        if (mbean == null) {
            return null;
        }
        return mbean.getSentinelPrice();
    }

    @Override
    public void updateDeferTimeConfig() {
        logger.info("-------------------------开始更新递延配置-------------------------");
        deferInterestScheduTask.updateDeferTime();
        logger.info("-------------------------结束更新递延配置-------------------------");
    }

    /**
     * 添加或删除内存--现金
     *
     * @param orderCashInfo
     */
    @Override
    public void addOrRemoveOrderInfo(OrderCashInfo orderCashInfo, String flag) {
        OrderCashInfo order = OrderCashInfoCache.get(orderCashInfo.getOrderId());
        if ("a".equals(flag)) {//添加
            if (order == null) {
                order = orderCashInfoDao.queryByOrderId(orderCashInfo.getOrderId());
            } else {
                order.setHoldCount(orderCashInfo.getHoldCount());
            }
            OrderCashInfoCache.put(order);
        } else if ("r".equals(flag)) {//删除
            if (order != null) {
                OrderCashInfoCache.remove(order.getOrderId());
            }
        }
        logger.info("======执行 addOrRemoveOrderInfo==orderCashInfo={}=====", JSONObject.toJSONString(orderCashInfo));

    }


    @Override
    public String updateRiskOrderClearTime(String... productCodes) {
        StringBuilder errorStr = new StringBuilder();
        BoundHashOperations<String, String, ProductVo> productInfo = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
        for (String productCode : productCodes) {
            ProductVo productVo = productInfo.get(productCode);
            if (productVo != null) {
                BaseTrade baseTrade = null;
                if (PlateEnum.OUTER_PLATE.getValue().equals(productVo.getPlate())) {
                    baseTrade = tradeService.getOuterFutureTrade();
                } else if (PlateEnum.INNER_PLATE.getValue().equals(productVo.getPlate())) {
                    baseTrade = tradeService.getInnerFutureTrade();
                } else if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue().equals(productVo.getPlate())) {
                    baseTrade = tradeService.getContractTrade();
                }

                if (baseTrade != null) {
                    RiskControlQueue riskControlQueue = baseTrade.getRiskControl().getRiskControlQueue(productCode);
                    Date sysSetSaleDate = null;
                    Date sysSetSaleDateDefer = null;
                    if (riskControlQueue != null) {
                        try {
                            sysSetSaleDate = TradeUtils.querySysSetSaleDate(productCode, 0, redisTemplate);
                            sysSetSaleDateDefer = TradeUtils.querySysSetSaleDate(productCode, 1, redisTemplate);
                        } catch (Exception e) {
                            //logger.info(e.getMessage());
                        }
                        if (sysSetSaleDate == null || sysSetSaleDateDefer == null) {
                            errorStr.append(productCode).append("商品清仓时间错误;");
                            continue;
                        }
                        for (Map.Entry<String, RiskControlBean> entry : riskControlQueue.getRiskOrdersMap().entrySet()) {
                            String orderId = entry.getKey();
                            RiskControlBean riskBean = entry.getValue();
                            int updateCount = 0;
                            if (riskBean.getOrderBean().getFundType() == LTConstants.FUND_TYPE_CASH) {
                                //查询缓存数据
                                OrderCashInfo cashOrder = OrderCashInfoCache.get(orderId);
                                if (cashOrder == null || cashOrder.getBuySuccessCount() == 0 || cashOrder.getHoldCount() == 0) {//如果买成功数为0，查下数据库
                                    //查询数据库中订单基本数据
                                    cashOrder = orderCashInfoDao.queryByOrderId(orderId);
                                }
                                if (cashOrder != null) {
                                    OrderCashInfo cashInfo = new OrderCashInfo();
                                    cashInfo.setOrderId(orderId);
                                    if (DeferStatusEnum.NOT_DEFER.getValue() == cashOrder.getDeferStatus()) {
                                        cashInfo.setSysSetSellDate(sysSetSaleDate);
                                        cashOrder.setSysSetSellDate(sysSetSaleDate);
                                        //更新风控订单内存清仓时间
                                        riskBean.setDeferredOrderTimeStamp(sysSetSaleDate.getTime());
                                    } else {
                                        cashInfo.setSysSetSellDate(sysSetSaleDateDefer);
                                        cashOrder.setSysSetSellDate(sysSetSaleDateDefer);
                                    }
                                    orderCashInfoDao.update(cashInfo);
                                }

                            }
                        }
                    }
                }
            }
        }

        this.callOrderClearTime();

        return errorStr.toString();
    }

    @Override
    public void callOrderClearTime(){
        productTimeCache.setExchangeTradingTime(true);
        tradeService.getInnerFutureTrade().startClearTimeSchedule();
        tradeService.getOuterFutureTrade().startClearTimeSchedule();
        tradeService.getContractTrade().startClearTimeSchedule();
    }

    @Override
    public void updateInvestorAccountServer(InvestorAccount investorAccount) {
        logger.info("开始更新券商账户【{}】的c++交易服务器，ip:{}, port:{}", investorAccount.getSecurityCode(), investorAccount.getServerIp(), investorAccount.getServerPort());
        tradeService.startupSingle(investorAccount);
    }

    @Override
    public double getOrderDeferredFee(Integer plate, Integer tradeDirection, String investorId, double deferInterest, String productCode) {
        return deferservice.getOrderDeferredFee(plate, tradeDirection, investorId, deferInterest, productCode);
    }

}
