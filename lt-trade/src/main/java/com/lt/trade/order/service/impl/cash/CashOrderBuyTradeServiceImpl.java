/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: CashOrderBuyTradeServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.service.impl.cash;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.business.product.IProductRiskConfigService;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.api.user.IInvestorProductConfigApiService;
import com.lt.api.user.IUserApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.EntrustPriceTypeEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.TradeTypeEnum;
import com.lt.model.product.ProductRiskConfig;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.InvestorProductConfig;
import com.lt.model.user.UserBaseInfo;
import com.lt.trade.TradeServer;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.cache.OrderCashInfoCache;
import com.lt.trade.order.dao.OrderCashEntrustInfoDao;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.service.IOrderBuyTradeService;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.vo.fund.FundOrderVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.OrderVo;
import com.lt.vo.trade.TradeDirectVo;

/**
 * 现金订单开仓业务实现
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年12月9日 下午1:56:20
 */
@Service
public class CashOrderBuyTradeServiceImpl extends AbstractCashOrderTradeSerivce implements IOrderBuyTradeService {


    private static final long serialVersionUID = 8764014179497275939L;

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(CashOrderBuyTradeServiceImpl.class);

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
    /**
     * 交易服务
     */
    @Autowired
    private TradeServer tradeServer;
    /**
     * 订单基本信息数据接口
     */
    @Autowired
    private OrderCashInfoDao orderCashInfoDao;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IInvestorProductConfigApiService investorProductConfigApiService;
    @Autowired
    private IProductRiskConfigService productRiskConfigService;


    /**
     * 下单
     *
     * @param orderVo
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月9日 下午2:30:35
     * @see com.lt.trade.order.service.IOrderService#doBuy(com.lt.vo.trade.OrderVo)
     */
    @Override
    public void doBuy(OrderVo orderVo) throws LTException {
        //开始时间
        long startTime = System.currentTimeMillis();
        ProductVo product = orderVo.getProductVo();//商品信息

        //1.证券账号
        logger.info("查询证券账户 入参：orderVo.getInvestorId()={}, product.getId()={}, product.getPlate()={}, orderVo.getTradeDirection()={}", orderVo.getInvestorId(), product.getId(), product.getPlate(), orderVo.getTradeDirection());
        InvestorFeeCfg investorFeeCfg = investorFeeCfgApiService.getInvestorFeeCfg(orderVo.getInvestorId(), product.getId(), product.getPlate(), orderVo.getTradeDirection());
        logger.info("investorFeeCfg:{}", JSONObject.toJSON(investorFeeCfg));
        if (investorFeeCfg == null || StringTools.isEmpty(investorFeeCfg.getSecurityCode())) {
            throw new LTException(LTResponseCode.IV00002);
        }
        //获取用户信息
        UserBaseInfo info = userApiService.findUserByUserId(orderVo.getUserId());
        info.setRiskLevel(info.getRiskLevel() == null ? "4" : info.getRiskLevel());//默认高风险
        //获取商品手续费
        InvestorProductConfig config = investorProductConfigApiService.findInvestorProductConfig(orderVo.getInvestorId(), product.getProductCode());
        if (config != null && config.getCounterFee() != null) {//如果费用配不为空则取
            investorFeeCfg.setInvestorCounterfee(DoubleTools.parseDoulbe(config.getCounterFee()));
            //设置相应的手续费缩小响应的倍数
            //investorFeeCfg.setInvestorCounterfee(DoubleUtils.div(DoubleTools.parseDoulbe(config.getCounterFee()),orderVo.getMini()));
        }

        //获取商品区间配置
        List<ProductRiskConfig> productRiskConfigList = productRiskConfigService.queryProductRiskConfig(product.getId(), Integer.valueOf(info.getRiskLevel()));
        if (productRiskConfigList != null && productRiskConfigList.size() > 0) {
            ProductRiskConfig productRiskConfig = productRiskConfigList.get(0);
            investorFeeCfg.setStopLossRange(productRiskConfig.getStopLossRange() == null ? investorFeeCfg.getStopLossRange() : productRiskConfig.getStopLossRange());
            investorFeeCfg.setStopProfitRange(productRiskConfig.getStopProfitRange() == null ? investorFeeCfg.getStopProfitRange() : productRiskConfig.getStopProfitRange());
            investorFeeCfg.setSurcharge(productRiskConfig.getSurcharge() == null ? investorFeeCfg.getSurcharge() : productRiskConfig.getSurcharge());
            investorFeeCfg.setDeferFee(productRiskConfig.getDeferFee() == null ? investorFeeCfg.getDeferFee() : productRiskConfig.getDeferFee());
            investorFeeCfg.setDeferFund(productRiskConfig.getDeferFund() == null ? investorFeeCfg.getDeferFund() : productRiskConfig.getDeferFund());
            investorFeeCfg.setIsSupportDefer(productRiskConfig.getIsDefer());
        }
        //处理角模式
       // changeInvestorFeeCfg(investorFeeCfg,orderVo);

        logger.info("============费用配置，investorFeeCfg={}============", JSONObject.toJSONString(investorFeeCfg));

        //1.1检查止盈止损是否符合范围
        TradeUtils.checkStopLossAndStopProfit(investorFeeCfg, orderVo);

        //2. 初始化现金买入订单
        OrderCashInfo cashOrder = this.initBuyCashOrdersInfo(orderVo, investorFeeCfg);
        logger.info("已初始化订单, displayId:{}", cashOrder.getOrderId());

        //3. 证券账号
        cashOrder.setSecurityCode(investorFeeCfg.getSecurityCode());
        //证券账户id
        cashOrder.setAccountId(investorFeeCfg.getSecurityId());
        logger.info("已获取到证券账号, securityCode:{}", cashOrder.getSecurityCode());

        //4.设置当前价，止损价，止盈价，买入价格
        this.fillStopLossProfitPrice(cashOrder, orderVo);

        //5.实例化委托单对象
        OrderCashEntrustInfo entrustRecord = this.initBuyCashEntrustRecord(cashOrder, orderVo);

        //设置委托类型，委托价
        cashOrder.setBuyEntrustType(entrustRecord.getEntrustType());
        //行情委托价格
        cashOrder.setEntrustBuyPrice(entrustRecord.getEntrustPrice());
        //品牌 id
        cashOrder.setBrandId(info.getBrandId());
        //角模式
        cashOrder.setMini(orderVo.getMini());

        //6.订单存入缓存
        OrderCashInfoCache.put(cashOrder);
        logger.info("订单已存入缓存, displayId:{}", cashOrder.getOrderId());

        //7.委托单存入缓存
        OrderCashEntrustInfoCache.put(entrustRecord);
        logger.info("开仓委托单已存入缓存, entrustId:{}", entrustRecord.getEntrustId());

        //8. 扣款
        this.pay4Buy(orderVo, cashOrder);
        logger.info("订单已扣款, displayId:{}", cashOrder.getOrderId());

        //9.将委托单放到交易队列中（通知交易模块）,并设置委托时间，委托状态
        this.sendBuyEntrustMessage(cashOrder, entrustRecord);
        logger.info("商品:{}, 开仓用时time:{}ms", orderVo.getProductCode(), (System.currentTimeMillis() - startTime));
    }


    /**
     * 计算金额及扣款
     *
     * @param orderVo
     * @param cashOrder
     * @param investorFeeCfg
     * @author XieZhibing
     * @date 2016年12月13日 上午10:22:53
     */
    private void pay4Buy(OrderVo orderVo, OrderCashInfo cashOrder) {

        /*************** 调用资金模块付款接口 ***************/
        long startTime2 = System.currentTimeMillis();
        //现金订单付款数据参数
        FundOrderVo fundOrderVo = new FundOrderVo(FundTypeEnum.CASH,
                orderVo.getProductName(), orderVo.getOrderId(),
                orderVo.getUserId(), cashOrder.getShouldHoldFund(),
                cashOrder.getShouldDeferFund(), cashOrder.getShouldCounterFee());
        //调用资金模块付款接口
        boolean paid = fundTradeApiService.doBuy(fundOrderVo);
        if (!paid) {
            logger.info("下单支付失败, displayId:{}", orderVo.getOrderId());
            throw new LTException(LTResponseCode.FU00005);
        }
        logger.info("商品:{}, 开仓调用资金模块付款接口用时time:{}ms", orderVo.getProductCode(), (System.currentTimeMillis() - startTime2));
        /*************** 扣款成功  ***************/
    }
    /**
     *
     *设置订单的保证金、手续费、止盈价、止损价等
     * @author XieZhibing
     * @date 2017年2月13日 下午7:13:26
     * @param cashOrder
     * @param orderVo
     * @param investorFeeCfg
     */
    /*private void fillOrderFundFee(CashOrdersInfo cashOrder, OrderVo orderVo, InvestorFeeCfg investorFeeCfg) {
		//缓存中查询产品信息
		ProductVo productVo = orderVo.getProductVo();
		//产品价格小数点位数
		Integer scale = productVo.getDecimalDigits();
		
		//汇率
		Double rate = productVo.getRate();
		//最小变动价格，如10
		Double jumpPrice = productVo.getJumpPrice();
		//最小波动点，如 0.01
		Double jumpValue = productVo.getJumpValue();
		if (jumpValue == null || jumpValue == 0){
			logger.info("最小波动点:{}, 最小变动价格:{}", jumpValue, jumpPrice);
			throw new LTException(LTResponseCode.PR00011);
		}
		
		//下单手数
		int count = orderVo.getCount();
		//每手平台手续费(原币种)
		double perPlatformFee = investorFeeCfg.getPlatformCounterfee();
		//平台手续费
		double platformFee = DoubleTools.scaleFormat(perPlatformFee * count * rate);		
		//每手券商手续费(原币种)
		double perInvestorFee = investorFeeCfg.getInvestorCounterfee();
		//券商手续费
		double investorFee = DoubleTools.scaleFormat(perInvestorFee * count * rate);
		//每手总手续费(原币种)
		double perCounterFee = DoubleTools.add(perPlatformFee, perInvestorFee);
		//实际手续费
		double actualCounterFee = DoubleTools.add(platformFee, investorFee);
		//应扣手续费
		double shouldCounterFee = actualCounterFee;
				
		//每手防穿仓保证金参数
		double perSurcharge = investorFeeCfg.getSurcharge();
		//止损保证金参数
		double surcharge = DoubleTools.scaleFormat(perSurcharge * count * rate);		
		//每手止损值(原币种)
		double perStopLoss = orderVo.getStopLoss();
		//止损值
		double stopLoss = DoubleTools.scaleFormat(perStopLoss * count * rate);		
		//每手止盈值(原币种)
		double perStopProfit = orderVo.getStopProfit();
		//止盈值
		double stopProfit = DoubleTools.scaleFormat(perStopProfit * count * rate);
			
		//止损保证金
		double holdFund = DoubleTools.add(stopLoss, surcharge);	
		
		//每手递延利息(原币种)
		double perDeferFee = investorFeeCfg.getDeferFee();
		//每手递延保证金
		double perDeferFund = investorFeeCfg.getDeferFund();
		
		//递延保证金
		double deferFund = 0.00;
		//递延状态
		int deferStatus = orderVo.getDeferStatus();		
		//计算递延费
		if(DeferStatusEnum.DEFER.getValue() == deferStatus){			
			//计算总递延费
			deferFund = DoubleTools.scaleFormat(perDeferFund * count * rate);	
		}		
		
		//设置订单的保证金、手续费
		cashOrder = cashOrder.setOrderFundFee(holdFund, shouldCounterFee,
				actualCounterFee, platformFee, investorFee, stopLoss,
				stopProfit, deferStatus, deferFund, 0.00, perSurcharge,
				perStopLoss, perStopProfit, perCounterFee, perDeferFund, perDeferFee);
		
		// 设置止损价、止盈价
		this.fillStopLossProfitPrice(cashOrder, orderVo, jumpPrice, jumpValue, scale);		
	}*/

    /**
     * 设置止损价、止盈价
     *
     * @param cashOrder
     * @param orderVo
     * @param jumpPrice
     * @param jumpValue
     * @param scale     价格小数点位数
     * @author XieZhibing
     * @date 2017年1月5日 下午4:06:15
     */
    private void fillStopLossProfitPrice(OrderCashInfo cashOrder, OrderVo orderVo) {
        try {
            ProductVo product = orderVo.getProductVo();
            //最小变动价格，如10
            double jumpPrice = product.getJumpPrice();
            //最小波动点，如 0.01
            double jumpValue = product.getJumpValue();
            //产品价格小数点位数
            Integer scale = product.getDecimalDigits();
            //内外盘标识
            Integer plate = cashOrder.getPlate();
            //交易方向
            Integer tradeDirection = cashOrder.getTradeDirection();
            //产品编码
            String productCode = cashOrder.getProductCode();

            //获取当前价
            double currentPrice = 0.0;
            if (PlateEnum.INNER_PLATE.getValue() == plate) {
                currentPrice = tradeServer.getInnerFutureTrade().getQuotePrice(productCode).getLastPrice();
            } else if (PlateEnum.OUTER_PLATE.getValue() == plate) {
                currentPrice = tradeServer.getOuterFutureTrade().getQuotePrice(productCode).getLastPrice();
            } else if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue() == plate) {
                currentPrice = tradeServer.getContractTrade().getQuotePrice(productCode).getLastPrice();
            } else {
                throw new LTException(LTResponseCode.TD00006);
            }

            //止损价
            double stopLossPrice = TradeUtils.stopLossPrice(tradeDirection, currentPrice, orderVo.getStopLoss(), jumpPrice, jumpValue);
            stopLossPrice = DoubleUtils.scaleFormat(stopLossPrice, scale);
            //止盈价
            double stopProfitPrice = TradeUtils.stopProfitPrice(tradeDirection, currentPrice, orderVo.getStopProfit(), jumpPrice, jumpValue);
            stopProfitPrice = DoubleUtils.scaleFormat(stopProfitPrice, scale);

            //用户提交价格
            currentPrice = DoubleUtils.scaleFormat(currentPrice, scale);
            cashOrder.setUserCommitBuyPrice(currentPrice);
            //设置止损价
            cashOrder.setStopLossPrice(stopLossPrice);
            //设置止盈价
            cashOrder.setStopProfitPrice(stopProfitPrice);

            logger.info("设置止损价:{}, 止盈价:{}", stopLossPrice, stopProfitPrice);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.FU00000);
        }

    }

    /**
     * 初始化订单
     *
     * @param orderVo
     * @return
     * @author XieZhibing
     * @date 2016年12月13日 上午10:14:20
     */
    private OrderCashInfo initBuyCashOrdersInfo(OrderVo orderVo, InvestorFeeCfg investorFeeCfg) {
        //设置订单创建时间
        Date now = new Date();
        //设置订单显示ID
        String orderId = TradeUtils.makeOrderCode();
        logger.info("===========orderId={}===============", orderId);
        //记录订单显示ID
        orderVo.setOrderId(orderId);
        //产品参数
        ProductVo productVo = orderVo.getProductVo();
        //委托手数
        Integer count = orderVo.getCount();
        //汇率
        double rate = productVo.getRate();

        //每手平台手续费(原币种)
        double perPlatformFee = DoubleTools.scaleFormat(investorFeeCfg.getPlatformCounterfee());
        logger.info("perPlatformFee:" + perPlatformFee);
        //平台手续费
        double platformFee = DoubleTools.mul(DoubleTools.mul(perPlatformFee, rate), count);
        logger.info("platformFee:" + platformFee);
        //每手券商手续费(原币种)
        double perInvestorFee = DoubleTools.scaleFormat(investorFeeCfg.getInvestorCounterfee());
        logger.info("perInvestorFee:" + perInvestorFee);
        //券商手续费
        double investorFee = DoubleTools.mul(DoubleTools.mul(perInvestorFee, rate), count);
        logger.info("investorFee:" + investorFee);
        //每手总手续费(原币种)
        double perCounterFee = DoubleTools.add(perPlatformFee, perInvestorFee);
        logger.info("perCounterFee:" + perCounterFee);
        //实际手续费
        double actualCounterFee = 0.0;
        //应扣手续费
        double shouldCounterFee = DoubleTools.add(platformFee, investorFee);
        logger.info("shouldCounterFee:" + shouldCounterFee);
        //每手保证金参数
        double perSurcharge = DoubleTools.scaleFormat(investorFeeCfg.getSurcharge());
        logger.info("perSurcharge:" + perSurcharge);
        //保证金参数
        double surcharge = DoubleTools.mul(DoubleTools.mul(perSurcharge, rate), count);
        logger.info("surcharge:" + surcharge);
        //每手止损金额(原币种)
        double perStopLoss = DoubleTools.scaleFormat(orderVo.getStopLoss());
        //止损金额
        double stopLoss = DoubleTools.mul(DoubleTools.mul(perStopLoss, rate), count);
        //每手止盈金额(原币种)
        double perStopProfit = DoubleTools.scaleFormat(orderVo.getStopProfit());
        //止盈金额
        double stopProfit = DoubleTools.mul(DoubleTools.mul(perStopProfit, rate), count);

        //每手止损保证金
        double perHoldFund = DoubleTools.scaleFormat(DoubleTools.add(perSurcharge, perStopLoss));
        //应扣止损保证金
        double shouldHoldFund = DoubleTools.mul(DoubleTools.mul(perHoldFund, rate), count);

        double actualHoldFund = 0.0;
        //每手递延利息(原币种)
        double perDeferFee = DoubleTools.scaleFormat(investorFeeCfg.getDeferFee());
        //每手递延保证金
        double perDeferFund = DoubleTools.scaleFormat(investorFeeCfg.getDeferFund());
        //递延保证金
        double shouldDeferFund = 0.0;
        //递延状态
        int deferStatus = orderVo.getDeferStatus();
        //计算递延费
        if (DeferStatusEnum.DEFER.getValue() == deferStatus) {
            //计算总递延保证金
            shouldDeferFund = DoubleTools.mul(DoubleTools.mul(perDeferFund, rate), count);
        }
        //实扣递延保证金
        double actualDeferFund = 0.0;
        //显示、隐藏(默认显示)
        Integer display = 1;
        //开仓成功数
        Integer buySuccessCount = 0;
        //开仓失败数
        Integer buyFailCount = 0;
        //持仓数
        Integer holdCount = 0;
        //初始化订单对象
        OrderCashInfo cashOrder = new OrderCashInfo(orderVo.getUserId(), orderVo.getInvestorId(), orderId, orderVo.getTradeDirection(),
                productVo.getId(), productVo.getProductCode(), productVo.getProductName(), count,
                productVo.getExchangeCode(), productVo.getPlate(), productVo.getCurrencyUnit(), orderVo.getUserBuyPrice(),
                orderVo.getUserBuyDate(), orderVo.getSysSetSaleDate(), productVo.getCurrency(), rate,
                orderVo.getTrailStopLoss(), perSurcharge, perStopLoss, perStopProfit,
                perCounterFee, perDeferFund, perDeferFee, shouldHoldFund,
                actualHoldFund, shouldCounterFee, actualCounterFee, stopLoss,
                stopProfit, deferStatus, shouldDeferFund, actualDeferFund, now, now, display,
                buySuccessCount, buyFailCount, holdCount);
        cashOrder.setNickName(orderVo.getUserBaseInfo().getNickName());
        //应扣优惠券抵扣金额
        cashOrder.setShouldCouponFund(0.0);
        //实扣优惠券抵扣金额
        cashOrder.setActualCouponFund(0.0);

        //结算盈亏
        cashOrder.setLossProfit(0.0);
        //系统结算盈亏
        cashOrder.setSysLossProfit(0.0);
        //穿仓金额
        //cashOrder.setOverstepAmt(0.00);
        //递延次数
        cashOrder.setDeferTimes(0);
        cashOrder.setDeferInterest(0.0);
        //下单触发方式
        cashOrder.setBuyTriggerType(orderVo.getBuyTriggerType());
        cashOrder.setPurchaseOrderType(orderVo.getPurchaseOrderType());
        return cashOrder;
    }

    /**
     * 下单时初始化现金委托单
     *
     * @author XieZhibing
     * @date 2016年12月11日 上午10:26:49
     */
    private OrderCashEntrustInfo initBuyCashEntrustRecord(OrderCashInfo cashOrder, OrderVo orderVo) {
        //生成委托单号
        String entrustId = TradeUtils.makeEntrustCode().toString();
        logger.info("生成委托单号 entrustId: {}", entrustId);

        //用户交易方向交易方向1 看多; 2 看空
        Integer tradeDirection = cashOrder.getTradeDirection();
        //计算委托单的委托方向
        TradeDirectVo tradeDirectVo = new TradeDirectVo(tradeDirection, TradeTypeEnum.BUY.getValue());
        logger.info("=========tradeDirectVo={}=========", JSONObject.toJSONString(tradeDirectVo));
        //产品参数
        ProductVo productVo = orderVo.getProductVo();

        //限价加的值
        double limitedPriceValue = -999;
        //订单类型,默认限价
        Integer entrustType = EntrustPriceTypeEnum.LIMIT_PRICE.getValue();
        //委托价
        double entrustPrice = cashOrder.getUserCommitBuyPrice();
        if (1 == productVo.getIsMarketPrice()) {
            entrustType = EntrustPriceTypeEnum.MARKET_PRICE.getValue();
            entrustPrice = 0;
        } else {
            limitedPriceValue = productVo.getLimitedPriceValue();
        }
        //初始化卖出委托单
        Date now = new Date();
        OrderCashEntrustInfo orderCashEntrustInfo = new OrderCashEntrustInfo(cashOrder.getOrderId(), entrustId, cashOrder.getProductId(),
                cashOrder.getProductCode(), cashOrder.getProductName(), cashOrder.getExchangeCode(),
                cashOrder.getPlate(), cashOrder.getSecurityCode(), cashOrder.getInvestorId(),
                cashOrder.getAccountId(), TradeTypeEnum.BUY.getValue(), tradeDirection,
                tradeDirectVo.getTradeOffset(), tradeDirectVo.getTradeDirect(), cashOrder.getBuyEntrustCount(),
                entrustPrice, entrustType, limitedPriceValue, now, now);
        //下单触发方式
        orderCashEntrustInfo.setTriggerType(cashOrder.getBuyTriggerType());
        return orderCashEntrustInfo;
    }

    /**
     * 按照对应的倍数 进行相应的转换
     * @param investorFeeCfg
     * @param orderVo
     */
    private void changeInvestorFeeCfg(InvestorFeeCfg investorFeeCfg,OrderVo orderVo){

        logger.info("修改模式参数:investorFeeCfg={}",investorFeeCfg.toString());
        //修改止损
        String stopLossRanges =  StringTools.formatStr(investorFeeCfg.getStopLossRange());

        String [] stopLossRangeArray  =  stopLossRanges.split(",");
        stopLossRanges = "";
        for(int i=0;i<stopLossRangeArray.length;i++){
            Double stopLoss =  StringTools.formatDouble(stopLossRangeArray[i]);
            if(i == stopLossRangeArray.length -1){
                stopLossRanges = stopLossRanges+String.valueOf(DoubleTools.mul(stopLoss,orderVo.getMini()));
            }else{
                stopLossRanges = stopLossRanges+String.valueOf(DoubleTools.mul(stopLoss,orderVo.getMini()))+",";
            }

        }
        investorFeeCfg.setStopLossRange(stopLossRanges);

        //修改止赢
        String stopProfitRanges =  StringTools.formatStr(investorFeeCfg.getStopProfitRange());
        String [] stopProfitRangeArray = stopProfitRanges.split(",");
        stopProfitRanges = "";
        for(int i=0;i<stopProfitRangeArray.length; i++){
            Double stopProfit = StringTools.formatDouble(stopProfitRangeArray[i]);
            if(i == stopProfitRangeArray.length -1){
                stopProfitRanges = stopProfitRanges+String.valueOf(DoubleTools.mul(stopProfit,orderVo.getMini()));
            }else{
                stopProfitRanges = stopProfitRanges+String.valueOf(DoubleTools.mul(stopProfit,orderVo.getMini()))+",";
            }
        }
        investorFeeCfg.setStopProfitRange(stopProfitRanges);

        //修改保证金
        investorFeeCfg.setSurcharge(DoubleTools.mul(investorFeeCfg.getSurcharge(),orderVo.getMini()));
        //递延费
        investorFeeCfg.setDeferFee(DoubleTools.mul(investorFeeCfg.getDeferFee(),orderVo.getMini()));
        //递延保证金
        investorFeeCfg.setDeferFund(DoubleTools.mul(investorFeeCfg.getDeferFund(),orderVo.getMini()));
        //修改保证金
        investorFeeCfg.setInvestorCounterfee(DoubleTools.mul(investorFeeCfg.getInvestorCounterfee(),orderVo.getMini()));
        //默认止损
        investorFeeCfg.setDefaultStopLoss(DoubleTools.mul(investorFeeCfg.getDefaultStopLoss(),orderVo.getMini()));
        //默认止赢
        investorFeeCfg.setDefaultStopProfit(DoubleTools.mul(investorFeeCfg.getDefaultStopProfit(),orderVo.getMini()));
    }

    /**
     * 获取 资金分布式接口: 资金扣款、结算、退款业务接口
     *
     * @return fundTradeApiService
     */
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

    /**
     * 获取 订单基本信息数据接口
     *
     * @return cashOrderDao
     */
    public OrderCashInfoDao getCashOrderDao() {
        return orderCashInfoDao;
    }

    /**
     * 设置 订单基本信息数据接口
     *
     * @param cashOrderDao 订单基本信息数据接口
     */
    public void setCashOrderDao(OrderCashInfoDao orderCashInfoDao) {
        this.orderCashInfoDao = orderCashInfoDao;
    }

    @Override
    public OrderCashInfoDao getOrderCashInfoDao() {
        return orderCashInfoDao;
    }

    @Override
    public OrderCashEntrustInfoDao getOrderCashEntrustInfoDao() {
        return null;
    }


}
