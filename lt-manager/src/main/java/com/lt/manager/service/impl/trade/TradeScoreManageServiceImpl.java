package com.lt.manager.service.impl.trade;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.trade.IOrderScoreApiService;
import com.lt.api.user.IUserApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.*;
import com.lt.manager.bean.trade.DigitalCoinPosition;
import com.lt.manager.bean.trade.DigitalProductBrandPosition;
import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.manager.dao.trade.*;
import com.lt.manager.service.trade.ITradeScoreManageService;
import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.trade.OrderScoreSuccessInfo;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.LoggerTools;
import com.lt.util.TradeUtil;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.vo.fund.FundOrderVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;
import com.lt.vo.trade.OrderVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 积分订单交易实现类
 *
 * @author jingwb
 */
@Service
public class TradeScoreManageServiceImpl implements ITradeScoreManageService {
    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private OrderScoreInfoDao scoreOrdersInfoDao;
    @Autowired
    private IOrderScoreApiService orderApiService;
    @Autowired
    private OrderScoreEntrustInfoDao orderScoreEntrustInfoDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IFundTradeApiService fundTradeApiServiceImpl;
    @Autowired
    private IProductApiService productApiService;
    @Autowired
    private OrderScoreSuccessInfoDao orderScoreSuccessInfoDao;
    @Autowired
    private DigitalScoreCoinPositionCountManageDao positionCountManageDao;
    @Autowired
    private DigitalScoreProductBrandPositionManageDao brandPositionManageDao;

    @Autowired
    private IUserApiService userApiService;
    @Override
    public Page<OrderParamVO> queryTradeOrderPage(OrderParamVO param)
            throws Exception {
        Page<OrderParamVO> page = new Page<OrderParamVO>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());


        List<OrderParamVO> list = scoreOrdersInfoDao.selectScoreTradeOrderPage(param);

        //计算浮动盈亏
        for (OrderParamVO order : list) {
            order.setFloatLossProfit(0.0);
            if (order.getStatus() != null
                    && order.getStatus() == 3) {//持仓中
                logger.info("======order={}", JSONObject.toJSONString(order));
                Double floatLossProfit = TradeUtil.dealFloatLossProfit(order.getProductCode(), order.getTradeDirection(), order.getBuyAvgPrice(),
                        order.getJumpPrice(), order.getJumpValue(), order.getHoldCount(), order.getRate(), redisTemplate);
                order.setFloatLossProfit(floatLossProfit);

                if (order.getTrailStopLoss() == 1) {//已开启移动止损
                    TradeUtil.dealMoveStopLoss(order, orderApiService);
                }
            }
        }
        page.addAll(list);
        page.setTotal(scoreOrdersInfoDao.selectScoreTradeOrderCount(param));
        return page;
    }

    @Override
    public Page<OrderParamVO> queryEntrustTradeOrderPage(OrderParamVO param)
            throws Exception {
        Page<OrderParamVO> page = new Page<OrderParamVO>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.setTotal(orderScoreEntrustInfoDao.selectScoreEntrustTradeOrderCount(param));
        page.addAll(orderScoreEntrustInfoDao.selectScoreEntrustTradeOrderPage(param));
        return page;
    }

    @Override
    public Page<OrderParamVO> querySuccessTradeOrderPage(OrderParamVO param)
            throws Exception {
        Page<OrderParamVO> page = new Page<OrderParamVO>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.setTotal(orderScoreSuccessInfoDao.selectScoreSuccessTradeOrderCount(param));
        page.addAll(orderScoreSuccessInfoDao.selectScoreSuccessTradeOrderPage(param));
        return page;
    }

    @Override
    public Map<String, String> queryTradeOrderDateMap(OrderParamVO param)
            throws Exception {
        return scoreOrdersInfoDao.selectScoreTradeOrderDateMap(param);
    }

    @Override
    public OrderParamVO getScoreOrderInfo(String id) throws Exception {
        OrderParamVO order = scoreOrdersInfoDao.selectScoreOrderInfo(id);
        order.setPerCounterFee(DoubleUtils.mul(order.getPerCounterFee(), order.getBuyEntrustCount()));
        if (order.getActualDeferFund() > 0) {
            order.setPerDeferFund(DoubleUtils.mul(order.getPerDeferFund(), order.getBuyEntrustCount()));
        } else {
            order.setPerDeferFund(0.0);
        }
        if (order.getDeferInterest() > 0) {
            order.setPerDeferInterest(DoubleUtils.mul(order.getPerDeferInterest(), order.getBuyEntrustCount()));
        } else {
            order.setPerDeferInterest(0.0);
        }

        if (order.getStatus() == 3) {//持仓中
            //计算浮动盈亏
            logger.info("======order={}", JSONObject.toJSONString(order));
            Double floatLossProfit = TradeUtil.dealFloatLossProfit(order.getProductCode(), order.getTradeDirection(), order.getBuyAvgPrice(),
                    order.getJumpPrice(), order.getJumpValue(), order.getHoldCount(), order.getRate(), redisTemplate);
            order.setFloatLossProfit(floatLossProfit);
            floatLossProfit = TradeUtil.dealFloatLossProfitRate(order.getProductCode(), order.getTradeDirection(), order.getBuyAvgPrice(),
                    order.getJumpPrice(), order.getJumpValue(), order.getHoldCount(), order.getRate(), redisTemplate);
            order.setFloatLossProfitRate(floatLossProfit);
            if (order.getTrailStopLoss() == 1) {//已开启移动止损
                TradeUtil.dealMoveStopLoss(order, orderApiService);
            }
        }
        order.setPerStopLoss(DoubleUtils.mul(order.getPerStopLoss(), order.getBuyEntrustCount()));
        order.setPerStopProfit(DoubleUtils.mul(order.getPerStopProfit(), order.getBuyEntrustCount()));
        order.setPerSurcharge(DoubleUtils.add(DoubleUtils.mul(order.getPerSurcharge(), order.getBuyEntrustCount()), order.getPerStopLoss()));

        if (order.getStatus() != 4) {
            order.setLossProfit(null);
        }
        return order;
    }

    @Override
    public List<Map<String, Object>> getOrderBuyOrSaleList(String id) throws Exception {
        List<Map<String, Object>> list = scoreOrdersInfoDao.selectScoreOrderBuyOrSaleList(id);
        int length = list.size();
        for (int i = 1; i <= length; i++) {
            for (int j = 0; j < list.size(); j++) {
                Map<String, Object> map = list.get(j);
                if (map.get("date") == null) {
                    list.remove(j);
                }
            }
        }

        return list;
    }

    @Override
    public OrderParamVO getScoreEntrustOrderInfo(Integer id) throws Exception {
        return orderScoreEntrustInfoDao.selectScoreEntrustOrderInfo(id);
    }

    @Override
    public OrderParamVO getScoreSuccOrderInfo(Integer id) throws Exception {
        return orderScoreSuccessInfoDao.selectScoreSuccOrderInfo(id);
    }

    @Override
    public void forceSell(OrderParamVO param) throws Exception {
        //开始时间
        long startTime = System.currentTimeMillis();

        String displayId = param.getOrderId();
        Integer fundType = FundTypeEnum.SCORE.getValue();
        Double userSalePrice = 0.0;
        logger.info("=====人工强制平仓开始 displayId={}========", displayId);

        //订单不存在
        logger.info("校验订单displayId:{}", displayId);
        if (displayId == null) {
            throw new LTException(LTResponseCode.TD00004);
        }

        // 订单卖出参数
        OrderVo orderVo = new OrderVo(displayId, fundType, SellTriggerTypeEnum.ADMINISTRATOR.getValue(),
                null, userSalePrice, new Date(startTime));

        // 调用订单服务接口卖出
        logger.info("调用订单服务接口平仓");
        OrderScoreInfo orderScoreInfo = queryOrderInfo(param);
        //不是持仓中
        if (!(orderScoreInfo.getHoldCount() > 0 && (orderScoreInfo.getSellEntrustCount() == null || orderScoreInfo.getSellEntrustCount() == 0))) {
            throw new LTException(LTResponseCode.TDJ0003);
        }
        orderApiService.sell(orderVo);
        //用于等待平仓完成
        Thread.currentThread().sleep(1000);
        logger.info("平仓用时:{}ms", (System.currentTimeMillis() - startTime));
    }


    /**
     * String orderId = param.getOrderId();//订单id
     * Integer entrustCount = param.getEntrustCount();//委托数量
     * String entrustTime = param.getEntrustDate();//委托时间
     * Integer triggerType = param.getTriggerType();//触发方式
     * Integer successCount = param.getSuccessCount();//成交数量
     * Double successProce = param.getSuccessPrice();//成交价
     * String successDate = param.getSuccessDate();//成交时间
     * Integer tradeType = param.getTradeType();//交易类型
     */
    @Override
    @Transactional
    public void forceSuccess(OrderParamVO param) throws Exception {
        logger.info("==========强制成功参数，param={}=========", JSONObject.toJSONString(param));
        //查询订单信息
        OrderScoreInfo orderScoreInfo = scoreOrdersInfoDao.selectOrderScoreInfoOne(param);

        if (orderScoreInfo == null) {
            throw new LTException(LTResponseCode.TD00004);
        }
        //查询商品信息
        ProductVo productVo = productApiService.getProductInfo(orderScoreInfo.getProductCode());
        if (TradeTypeEnum.BUY.getValue() == param.getTradeType()) {//开仓
            Integer count = orderScoreInfo.getBuyEntrustCount() - orderScoreInfo.getBuySuccessCount() - orderScoreInfo.getBuyFailCount();
            if (count <= 0) {//不满足
                throw new LTException(LTResponseCode.TDJ0001);
            }
            this.forceSuccessForBuy(orderScoreInfo, param, productVo);
        } else if (TradeTypeEnum.SELL.getValue() == param.getTradeType()) {//平仓
            if (orderScoreInfo.getHoldCount() <= 0) {//持仓数小于等于0，不满足条件
                throw new LTException(LTResponseCode.TDJ0001);
            }
            this.forceSuccessForSell(orderScoreInfo, param, productVo);
        }
    }

    /**
     * 强制成功--平仓
     *
     * @param orderScoreInfo
     * @param param
     * @throws Exception
     */
    public void forceSuccessForSell(OrderScoreInfo orderScoreInfo, OrderParamVO param, ProductVo product) throws Exception {
        //将单子移除风控
        orderApiService.removeRiskControlOrder(orderScoreInfo);
        OrderScoreInfo order = new OrderScoreInfo();
        order.setSellEntrustCount(0);//卖委托数量

        UserBaseInfo info = userApiService.findUserByUserId(orderScoreInfo.getUserId());
        orderScoreInfo.setBrandId(info.getBrandId());

        //计算买入均价、买入成功数量
        AvgPriceVo avgPriceVo = new AvgPriceVo(orderScoreInfo.getSellSuccessCount(), orderScoreInfo.getSellAvgPrice(), param.getSuccessCount(),
                param.getSuccessPrice(), orderScoreInfo.getTradeDirection(), product.getDecimalDigits());
        logger.info("=========avgPriceVo={}==========", JSONObject.toJSONString(avgPriceVo));
        order.setSellSuccessCount(avgPriceVo.getCount());//平仓成交数量
        order.setHoldCount(orderScoreInfo.getHoldCount() - avgPriceVo.getCount());//持仓数量
        order.setSellAvgPrice(avgPriceVo.getAvgPrice());//平仓均价

        this.dealLossProfit(orderScoreInfo, avgPriceVo);
        logger.info("=======SysLossProfit={},LossProfit={}===========", orderScoreInfo.getSysLossProfit(), orderScoreInfo.getLossProfit());
        //设置结算盈亏
        order.setSysLossProfit(orderScoreInfo.getSysLossProfit());
        order.setLossProfit(orderScoreInfo.getLossProfit());

        orderScoreInfo.setThisLossProfit(orderScoreInfo.getLossProfit());
        orderScoreInfo.setThisSysLossProfit(orderScoreInfo.getSysLossProfit());

        //修改订单信息
        order.setOrderId(orderScoreInfo.getOrderId());
        order.setModifyDate(new Date());
        order.setLastSellDate(DateTools.toDefaultDateTime(param.getSuccessDate()));
        order.setSellTriggerType(param.getTriggerType());
        order.setUserCommitSellDate(DateTools.toDefaultDateTime(param.getEntrustDate()));
        order.setEntrustSellDate(DateTools.toDefaultDateTime(param.getEntrustDate()));
        order.setUserCommitSellPrice(param.getSuccessPrice());
        //委托价
        Double entrustPrice = orderScoreInfo.getBuyEntrustType() == 1 ? 0 : param.getSuccessPrice();
        order.setEntrustSellPrice(entrustPrice);
        order.setSellEntrustType(orderScoreInfo.getBuyEntrustType());
        order.setModifyUserId(param.getModifyUserId());
        logger.info("==========强制成功--平仓order={}=============", JSONObject.toJSONString(order));
        scoreOrdersInfoDao.updateScore(order);

        //如果是差价合约，更新差价合约持仓数
        if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue().equals(orderScoreInfo.getPlate())) {
            DigitalCoinPosition coinPosition = new DigitalCoinPosition();
            coinPosition.setUserId(orderScoreInfo.getUserId());
            coinPosition.setProductCode(orderScoreInfo.getProductCode());
            coinPosition.setInvestorId(orderScoreInfo.getInvestorId());
            logger.info("cashOrdersInfo.getInvestorId()---------{}", orderScoreInfo.getInvestorId());

            //锁住单户持仓记录
            coinPosition = positionCountManageDao.selectCoinPositionForUpdate(coinPosition);
            logger.info("coinPosition---------{}", coinPosition);

            if (coinPosition != null) {
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == orderScoreInfo.getTradeDirection()) {
                    //平多
                    coinPosition.setBuyCount(coinPosition.getBuyCount() - order.getSellSuccessCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == orderScoreInfo.getTradeDirection()) {
                    //平空
                    coinPosition.setSellCount(coinPosition.getSellCount() - order.getSellSuccessCount());
                }
                positionCountManageDao.updateCoinPosition(coinPosition);
                logger.info("积分强制成功--平仓，修改单户持仓记录, coinPosition:{}", coinPosition);
            }

             /*
            锁住品牌持仓记录
             */
            DigitalProductBrandPosition  brandPosition = new DigitalProductBrandPosition();
            brandPosition.setBrandId(orderScoreInfo.getBrandId());
            brandPosition.setProductCode(orderScoreInfo.getProductCode());

            brandPosition = brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);
            logger.info("brandPosition---------{}", brandPosition);

            if (brandPosition != null) {
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == orderScoreInfo.getTradeDirection()) {
                    //平多
                    brandPosition.setBuyCount(brandPosition.getBuyCount() - order.getSellSuccessCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == orderScoreInfo.getTradeDirection()) {
                    //平空
                    brandPosition.setSellCount(brandPosition.getSellCount() - order.getSellSuccessCount());
                }
                brandPositionManageDao.updateCoinPosition(brandPosition);
                logger.info("积分强制成功--平仓，修改品牌持仓记录, coinPosition:{}", brandPosition);
            }
        }

        //添加或修改委托记录
        this.addOrUpdateOrderScoreEntrustInfo(orderScoreInfo, param);

        //增加成交记录
        this.addOrderScoreSuccessInfo(orderScoreInfo, param);

        //删除内存
        orderApiService.addOrRemoveOrderInfo(orderScoreInfo, "r");

        //结算
        orderApiService.fundBalance(orderScoreInfo, param.getSuccessCount());
        logger.info("积分强制成功--平仓--资金结算完成,orderId={}", orderScoreInfo.getOrderId());
    }


    public void addOrderScoreSuccessInfo(OrderScoreInfo order, OrderParamVO param) throws Exception {
        long now = System.currentTimeMillis();
        OrderScoreSuccessInfo info = new OrderScoreSuccessInfo("SU" + now, order.getOrderId(), "EN" + now,
                order.getProductId(), order.getInvestorId(),
                order.getAccountId(), order.getProductCode(), order.getProductName(), order.getExchangeCode(),
                order.getPlate(), order.getSecurityCode(),
                param.getTradeType(), order.getTradeDirection(), 0, 0, param.getEntrustCount(),
                param.getSuccessCount(), param.getSuccessCount(), "0000", "0000", param.getSuccessPrice(),
                DateTools.toDefaultDateTime(param.getSuccessDate()), new Date(now));
        info.setSuccessStatus(2);//完全成交
        orderScoreSuccessInfoDao.addScore(info);
    }


    /**
     * 增加或修改委托记录
     *
     * @param order
     * @param param
     * @throws Exception
     */
    public void addOrUpdateOrderScoreEntrustInfo(OrderScoreInfo order, OrderParamVO param) throws Exception {
        //获取委托信息
        OrderScoreEntrustInfo entrustInfo = orderScoreEntrustInfoDao.selectScoreEntrustInfoOne(param);
        if (entrustInfo == null) {//添加
            long now = System.currentTimeMillis();
            //委托价
            Double entrustPrice = order.getBuyEntrustType() == 1 ? 0 : param.getSuccessPrice();
            //构建委托实体
            entrustInfo = new OrderScoreEntrustInfo(order.getOrderId(), order.getOrderId().replace("OR", ""), order.getProductId(), order.getProductCode(),
                    order.getProductName(), order.getExchangeCode(), order.getPlate(), order.getSecurityCode(),
                    order.getInvestorId(), order.getAccountId(), param.getTradeType(), order.getTradeDirection(),
                    0, 0, param.getEntrustCount(), entrustPrice,
                    order.getBuyEntrustType(), 0.0, new Date(now), new Date(now));
            entrustInfo.setTriggerType(param.getTriggerType());
            entrustInfo.setEntrustDate(DateTools.toDefaultDateTime(param.getEntrustDate()));
            entrustInfo.setEntrustStatus(1);//成功

            orderScoreEntrustInfoDao.add(entrustInfo);
        } else {//修改
            OrderScoreEntrustInfo entrustInfo1 = new OrderScoreEntrustInfo();
            entrustInfo1.setOrderId(order.getOrderId());
            entrustInfo1.setTradeType(param.getTradeType());
            entrustInfo1.setEntrustCount(param.getEntrustCount());
            entrustInfo1.setTriggerType(param.getTriggerType());
            entrustInfo1.setEntrustDate(DateTools.toDefaultDateTime(param.getEntrustDate()));
            entrustInfo1.setModifyDate(new Date());
            entrustInfo1.setEntrustStatus(1);//成功
            //委托价
            Double entrustPrice = order.getBuyEntrustType() == 1 ? 0 : param.getSuccessPrice();
            entrustInfo1.setEntrustPrice(entrustPrice);
            entrustInfo1.setEntrustType(order.getBuyEntrustType());
            orderScoreEntrustInfoDao.update(entrustInfo1);
        }
    }

    /**
     * 处理结算盈亏
     *
     * @param orderScoreInfo
     * @param avgPriceVo
     */
    public void dealLossProfit(OrderScoreInfo orderScoreInfo, AvgPriceVo avgPriceVo) {
        //每手收益:按用户提交平仓价计算
        double perBenefit = 0.00;
        //每手收益:按系统平均平仓价计算
        double perSysbenefit = 0.00;
        double rmbMultiple = avgPriceVo.getCount() * orderScoreInfo.getRate();
        //每手止损金额(原币种)
        Double perStopLoss = orderScoreInfo.getPerStopLoss();
        //每手递延保证金(原币种)
        Double perDeferFund = orderScoreInfo.getPerDeferFund();
        //每手止盈金额(原币种)
        Double perStopProfit = orderScoreInfo.getPerStopProfit();

        //查询商品信息
        ProductVo productVo = productApiService.getProductInfo(orderScoreInfo.getProductCode());
        //最小波动点
        Double jumpValue = productVo.getJumpValue();
        //最小变动价格
        Double jumpPrice = productVo.getJumpPrice();
        //点位
        double points = jumpPrice / jumpValue;
        //计算收益
        if (TradeDirectionEnum.DIRECTION_UP.getValue() == orderScoreInfo.getTradeDirection()) {//多
            perSysbenefit = (avgPriceVo.getAvgPrice() - orderScoreInfo.getBuyAvgPrice()) * points;
        } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == orderScoreInfo.getTradeDirection()) {
            perSysbenefit = (orderScoreInfo.getBuyAvgPrice() - avgPriceVo.getAvgPrice()) * points;
        }
        //用户盈亏
        perBenefit = perSysbenefit;

        //每手保证金参数
        Double perSurcharge = orderScoreInfo.getPerSurcharge();
        //每手保证金
        Double perHoldFund = DoubleTools.add(perStopLoss, perSurcharge);
        //递延单处理
        if (DeferStatusEnum.DEFER.getValue() == orderScoreInfo.getDeferStatus()) {
            //穿破止损保证金 + 递延保证金
            perBenefit = (DoubleUtils.add(DoubleUtils.add(perSysbenefit, perHoldFund), perDeferFund) < 0) ? -DoubleUtils.add(perHoldFund, perDeferFund) : perSysbenefit;
        } else {//非递延单处理
            perBenefit = (DoubleUtils.add(perSysbenefit, perHoldFund) < 0) ? -perHoldFund : perSysbenefit;
        }
        //判断是否盈利
        perBenefit = perBenefit > perStopProfit ? perStopProfit : perBenefit;

        //用户总盈亏
        double userBenefit = DoubleTools.mulDec2(perBenefit, rmbMultiple);
        //系统总盈亏
        double sysBenefit = DoubleTools.mulDec2(perSysbenefit, rmbMultiple);
        orderScoreInfo.setLossProfit(userBenefit);
        orderScoreInfo.setSysLossProfit(sysBenefit);
    }


    /**
     * 强制成功--开仓
     *
     * @param orderScoreInfo
     * @param param
     * @throws Exception
     */
    public void forceSuccessForBuy(OrderScoreInfo orderScoreInfo, OrderParamVO param, ProductVo product) throws Exception {
        OrderScoreInfo order = new OrderScoreInfo();
        order.setBuyEntrustCount(param.getEntrustCount());//买委托数量

        UserBaseInfo info = userApiService.findUserByUserId(orderScoreInfo.getUserId());
        orderScoreInfo.setBrandId(info.getBrandId());

        //计算买入均价、买入成功数量
        AvgPriceVo avgPriceVo = new AvgPriceVo(orderScoreInfo.getBuySuccessCount(), orderScoreInfo.getBuyAvgPrice(), param.getSuccessCount(),
                param.getSuccessPrice(), orderScoreInfo.getTradeDirection(), product.getDecimalDigits());
        order.setBuySuccessCount(avgPriceVo.getCount());//开仓成交数量
        order.setHoldCount(avgPriceVo.getCount());//持仓数量
        order.setBuyAvgPrice(avgPriceVo.getAvgPrice());//开仓均价

        //修改订单信息
        order.setOrderId(orderScoreInfo.getOrderId());
        order.setModifyDate(new Date());
        order.setLastBuyDate(DateTools.toDefaultDateTime(param.getSuccessDate()));
        //处理实收保证金，实收手续费
        this.dealActualValue(orderScoreInfo, avgPriceVo.getCount(), order);
        order.setBuyTriggerType(param.getTriggerType());
        order.setUserCommitBuyDate(DateTools.toDefaultDateTime(param.getEntrustDate()));
        order.setUserCommitBuyPrice(param.getSuccessPrice());
        order.setEntrustBuyDate(DateTools.toDefaultDateTime(param.getEntrustDate()));
        order.setEntrustBuyPrice(param.getSuccessPrice());
        order.setModifyUserId(param.getModifyUserId());
        scoreOrdersInfoDao.updateScore(order);

        //如果是差价合约，更新差价合约持仓数
        if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue().equals(orderScoreInfo.getPlate())) {
            DigitalCoinPosition coinPosition = new DigitalCoinPosition();
            coinPosition.setUserId(orderScoreInfo.getUserId());
            coinPosition.setProductCode(orderScoreInfo.getProductCode());
            coinPosition.setInvestorId(orderScoreInfo.getInvestorId());

            //锁住持仓记录
            coinPosition = positionCountManageDao.selectCoinPositionForUpdate(coinPosition);

            if (coinPosition != null) {
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看多
                    coinPosition.setBuyCount(coinPosition.getBuyCount() + order.getBuySuccessCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看空
                    coinPosition.setSellCount(coinPosition.getSellCount() + order.getBuySuccessCount());
                }
                positionCountManageDao.updateCoinPosition(coinPosition);
                logger.info("积分强制成功--开仓，修改持仓记录, coinPosition:{}", coinPosition);

            } else {
                coinPosition = new DigitalCoinPosition();
                coinPosition.setUserId(orderScoreInfo.getUserId());
                coinPosition.setProductCode(orderScoreInfo.getProductCode());
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看多
                    coinPosition.setBuyCount(orderScoreInfo.getBuySuccessCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看空
                    coinPosition.setSellCount(orderScoreInfo.getBuySuccessCount());
                }
                coinPosition.setInvestorId(orderScoreInfo.getInvestorId());
                positionCountManageDao.insertCoinPosition(coinPosition);
                logger.info("积分强制成功--开仓，新增持仓记录, coinPosition:{}", coinPosition);

                //锁住持仓记录
                positionCountManageDao.selectCoinPositionForUpdate(coinPosition);
            }

            //锁住品牌持仓记录
            DigitalProductBrandPosition brandPosition = new DigitalProductBrandPosition();
            brandPosition.setBrandId(orderScoreInfo.getBrandId());
            brandPosition.setProductCode(orderScoreInfo.getProductCode());

            brandPosition = brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);

            if (brandPosition != null) {
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看多
                    brandPosition.setBuyCount(brandPosition.getBuyCount() + order.getBuySuccessCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看空
                    brandPosition.setSellCount(brandPosition.getSellCount() + order.getBuySuccessCount());
                }
                brandPositionManageDao.updateCoinPosition(brandPosition);
                logger.info("积分强制成功--开仓，修改品牌持仓记录, brandPosition:{}", brandPosition);

            } else {
                brandPosition = new DigitalProductBrandPosition();
                brandPosition.setBrandId(orderScoreInfo.getBrandId());
                brandPosition.setProductCode(orderScoreInfo.getProductCode());
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看多
                    brandPosition.setBuyCount(orderScoreInfo.getBuySuccessCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == orderScoreInfo.getTradeDirection()) {
                    //看空
                    brandPosition.setSellCount(orderScoreInfo.getBuySuccessCount());
                }
                brandPositionManageDao.insertCoinPosition(brandPosition);
                logger.info("积分强制成功--开仓，新增品牌持仓记录, brandPosition:{}", brandPosition);

                //锁住持仓记录
                brandPositionManageDao.selectCoinPositionForUpdate(brandPosition);
            }
        }

        //添加或修改委托记录
        this.addOrUpdateOrderScoreEntrustInfo(orderScoreInfo, param);

        //增加成交记录
        this.addOrderScoreSuccessInfo(orderScoreInfo, param);

        //查询单子信息
        OrderScoreInfo newOrderScoreInfo = scoreOrdersInfoDao.selectOrderScoreInfoOne(param);

        //添加内存
        orderApiService.addOrRemoveOrderInfo(newOrderScoreInfo, "a");

        //通知风控
        orderApiService.fillRiskControlQueue(newOrderScoreInfo);
    }


    /**
     * 处理实扣字段
     *
     * @param orderScoreInfo
     * @param matchTotal
     */
    public void dealActualValue(OrderScoreInfo orderScoreInfo, Integer matchTotal, OrderScoreInfo order) {
        //汇率
        double rate = orderScoreInfo.getRate();

        //每手止损金额(原币种)
        double perStopLoss = orderScoreInfo.getPerStopLoss();
        //单手保证金附加费
        double perSurcharge = orderScoreInfo.getPerSurcharge();
        //每手手续费
        double perCounterFee = orderScoreInfo.getPerCounterFee();
        //止损金额
        double stopLoss = perStopLoss * matchTotal * rate;
        //保证金参数
        double surcharge = perSurcharge * matchTotal * rate;
        //本次实扣保证金
        double actualHoldFund = stopLoss + surcharge;
        //实扣手续费
        double actualCounterFee = perCounterFee * matchTotal * rate;
        order.setActualHoldFund(actualHoldFund);
        order.setActualCounterFee(actualCounterFee);
    }

    /**
     * 强制失败
     * Integer tradeType = param.getTradeType();//开仓 平仓
     * String orderId = param.getOrderId();//订单id
     */
    @Override
    @Transactional
    public void forceFail(OrderParamVO param) throws Exception {
        //查询订单信息
        OrderScoreInfo orderScoreInfo = scoreOrdersInfoDao.selectOrderScoreInfoOne(param);
        if (orderScoreInfo == null) {
            throw new LTException(LTResponseCode.TD00004);
        }

        if (TradeTypeEnum.BUY.getValue() == param.getTradeType()) {//开仓
            Integer count = orderScoreInfo.getBuyEntrustCount() - orderScoreInfo.getBuySuccessCount() - orderScoreInfo.getBuyFailCount();
            if (count <= 0) {//不满足
                throw new LTException(LTResponseCode.TDJ0001);
            }
            //强制失败--开仓
            orderScoreInfo.setModifyUserId(param.getModifyUserId());
            this.forceFailForBuy(orderScoreInfo);
        } else if (TradeTypeEnum.SELL.getValue() == param.getTradeType()) {//平仓
            Integer count = orderScoreInfo.getSellEntrustCount();
            if (count == null || count <= 0) {//不满足
                throw new LTException(LTResponseCode.TDJ0001);
            }
            //强制失败--平仓
            orderScoreInfo.setModifyUserId(param.getModifyUserId());
            this.forceFailForSell(orderScoreInfo);
        }

    }

    @Override
    public void forceTypeMulti(OrderParamVO param, String type) throws Exception {
        if (StringTools.isEmpty(param.getIds())) {
            throw new LTException(LTResponseCode.TRJ0000);
        }
        StringBuilder stringBuilder = new StringBuilder();
        String[] idArray = param.getIds().split("/");
        for (String id : idArray) {
            try {
                param.setOrderId(id);
                //强制成功
                if (StringUtils.equals(type, "Success")) {
                    this.forceSuccess(param);
                }
                //强制失败
                else if (StringUtils.equals(type, "Fail")) {
                    this.forceFail(param);
                }
                //强制平仓
                else if (StringUtils.equals(type, "Sell")) {
                    this.forceSell(param);
                }

            } catch (Exception e) {
                stringBuilder.append("订单").append(id).append("错误:").append(LTResponseCode.getCode(e.getMessage()).getMsg()).append("\n");
            }
        }
        //错误消息统一处理
        if (stringBuilder.length() > 0) {
            throw new LTException(stringBuilder.toString());
        }
    }


    /**
     * 强制失败--平仓
     *
     * @param orderScoreInfo
     */
    public void forceFailForSell(OrderScoreInfo orderScoreInfo) {
        //删除内存
        orderApiService.addOrRemoveOrderInfo(orderScoreInfo, "r");
        OrderScoreInfo order = new OrderScoreInfo();
        order.setOrderId(orderScoreInfo.getOrderId());
        order.setSellEntrustCount(0);//平仓委托数清零
        order.setModifyDate(new Date());
        scoreOrdersInfoDao.updateScore(order);

        scoreOrdersInfoDao.updateScoreSellInfoForNull(orderScoreInfo.getOrderId());

        //修改委托单信息为失败
        OrderScoreEntrustInfo entrustInfo1 = new OrderScoreEntrustInfo();
        entrustInfo1.setOrderId(order.getOrderId());
        entrustInfo1.setEntrustStatus(0);//委托失败
        entrustInfo1.setModifyDate(new Date());
        entrustInfo1.setTradeType(TradeTypeEnum.SELL.getValue());
        orderScoreEntrustInfoDao.update(entrustInfo1);
    }

    /**
     * 强制失败--开仓
     *
     * @param orderScoreInfo
     */
    public void forceFailForBuy(OrderScoreInfo orderScoreInfo) {
        //删除内存
        orderApiService.addOrRemoveOrderInfo(orderScoreInfo, "r");
        //修改订单的失败数
        OrderScoreInfo order = new OrderScoreInfo();
        order.setOrderId(orderScoreInfo.getOrderId());
        order.setBuyFailCount(orderScoreInfo.getBuyEntrustCount() - orderScoreInfo.getBuySuccessCount());//开仓失败数
        order.setModifyDate(new Date());
        scoreOrdersInfoDao.updateScore(order);

        //修改委托单信息为失败
        OrderScoreEntrustInfo entrustInfo1 = new OrderScoreEntrustInfo();
        entrustInfo1.setOrderId(order.getOrderId());
        if (orderScoreInfo.getBuyEntrustCount() == order.getBuyFailCount()) {
            entrustInfo1.setEntrustStatus(0);//委托失败
        }
        entrustInfo1.setModifyDate(new Date());
        entrustInfo1.setTradeType(TradeTypeEnum.BUY.getValue());
        orderScoreEntrustInfoDao.update(entrustInfo1);

        //退款
        this.reFund(orderScoreInfo);
        logger.info("===========强制失败退款完成，orderId={}=========", orderScoreInfo.getOrderId());

    }

    public void reFund(OrderScoreInfo orderScoreInfo) {
        //失败手数
        Integer failCount = orderScoreInfo.getBuyEntrustCount() - orderScoreInfo.getBuySuccessCount();
        //汇率
        Double rate = orderScoreInfo.getRate();

        //退回递延费用
        double deferFund = 0.00;
        //递延状态
        if (DeferStatusEnum.DEFER.getValue() == orderScoreInfo.getDeferStatus()) {
            deferFund = DoubleTools.mul(DoubleTools.mul(orderScoreInfo.getPerDeferFund(), rate), failCount);
        }
        //每手止损保证金 = 每手止损金额 + 每手止损保证金参数
        double perHoldFund = DoubleTools.add(orderScoreInfo.getPerStopLoss(), orderScoreInfo.getPerSurcharge());
        // 退回保证金
        double holdFund = DoubleTools.mul(DoubleTools.mul(perHoldFund, rate), failCount);
        // 退回手续费
        double actualCounterFee = DoubleTools.mul(DoubleTools.mul(orderScoreInfo.getPerCounterFee(), rate), failCount);

        // 初始化订单资金对象
        FundOrderVo fundOrderVo = new FundOrderVo(
                FundTypeEnum.SCORE, orderScoreInfo.getProductName(),
                orderScoreInfo.getOrderId(), orderScoreInfo.getUserId(),
                holdFund, deferFund, actualCounterFee);
        //退款
        fundTradeApiServiceImpl.doRefund(fundOrderVo);
    }

    @Override
    public OrderScoreEntrustInfo queryEntrustInfo(OrderParamVO param)
            throws Exception {
        OrderScoreEntrustInfo info = orderScoreEntrustInfoDao.selectScoreEntrustInfoOne(param);
        if (info == null) {
            OrderScoreInfo orderScoreInfo = this.queryOrderInfo(param);
            if (orderScoreInfo != null) {
                info = new OrderScoreEntrustInfo();
                info.setProductCode(orderScoreInfo.getProductCode());
                info.setTradeType(param.getTradeType());
                info.setEntrustDate(new Date());
                info.setTradeDirection(orderScoreInfo.getTradeDirection());
            } else {
                return null;
            }
        }
        Double quotaPrice = TradeUtil.getQuotaPrice(info.getProductCode(), info.getTradeDirection(), redisTemplate);
        info.setQuotaPrice(quotaPrice);

        return info;
    }

    @Override
    public OrderScoreInfo queryOrderInfo(OrderParamVO param) throws Exception {
        //查询订单信息
        return scoreOrdersInfoDao.selectOrderScoreInfoOne(param);
    }
}
