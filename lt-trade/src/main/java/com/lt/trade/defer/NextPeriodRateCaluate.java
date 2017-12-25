package com.lt.trade.defer;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.sms.ISmsApiService;
import com.lt.api.user.IUserApiLogService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.fund.FundMainCash;
import com.lt.model.sms.SystemMessage;
import com.lt.model.sms.SystemMessageContant;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.UserContant;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.trade.defer.bean.CoinPositionCountBO;
import com.lt.trade.defer.bean.NextPeroidOrderInfo;
import com.lt.trade.defer.service.DeferService;
import com.lt.trade.defer.service.ProductDeferRunRecordService;
import com.lt.trade.order.service.ICoinPositionSumService;
import com.lt.trade.order.service.IOrderFunctionService;
import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import com.lt.trade.utils.LoggerTools;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.vo.defer.PeroidOrderHolidayVo;
import com.lt.vo.defer.ProNextTradePeriodVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import java.util.*;

/**
 * 项目名称：lt-trade
 * 类名称：NextPeriodRateCaluate
 * 类描述： 递延费结算处理类
 * 创建人：yuanxin
 * 创建时间：2017年3月27日 下午8:19:31
 */
public class NextPeriodRateCaluate {

    private Logger logger = LoggerTools.getInstance(getClass());
    /**
     * 结算短类型
     */
    private String futureType;
    /**
     * 下一个交易时间段 格式为 ：品种1,品种2-时间;品种 - 时间 或 时间
     */
    private String nextOnePeriod;

    /**
     *
     */
    public NextPeriodRateCaluate(String futureType, String nextPeriod) {
        this.futureType = futureType;
        this.nextOnePeriod = nextPeriod;
    }


    /**
     * 递延手续费结算执行记录
     **/
    private ProductDeferRunRecordService productDeferRunRecordService;

    //	/** 递延利息处理类*/
    private DeferService deferServiceImpl;
    /**
     * 资金服务
     */
    private IFundAccountApiService fundAccountService;
    /**
     * 订单服务
     */
    private IOrderFunctionService orderService;
    /**
     * 短信接口
     */
    private ISmsApiService smsService;
    /**
     * 用户操作日志入口
     */
    private IUserApiLogService userLogServiceImpl;
    /**
     * 现金订单信息
     */
    private List<NextPeroidOrderInfo> cashOrderList;

    /**
     * 类型的节假日
     */
    private Map<Integer, Date[]> codeHoliday;
    /**
     * 用户资金余额 用于判断用户资金不充足的情况下通知用户
     */
    private Map<String, Double> userFund;
    /**
     * 用户电话
     */
    private Map<String, String> userTele;
    /**
     * 即将到达交割日，通知用户留意订单情况 String：userId ，Set<String> :合约
     */
    private Map<String, Set<String>> userPeriodFail;
    /**
     * 清仓时间段隔天的品种
     */
    private Map<Integer, ProNextTradePeriodVo> nextDayCode;
    /**
     * 存储品种(短类型如CU)的下一个清仓时间点
     */
    private Map<Integer, String> productNextOnePeriod;

    private ICoinPositionSumService coinPositionSumService;

    private Map<String, CoinPositionCountBO> cashPositionMap = new HashMap<>();


    public void run() {
        try {
            // futureType + nextOnePeriod + day
            // 已执行过return
            String day = DateTools.formatDate(new Date(), "yyyyMMdd");
            logger.info("递延费结算 {} {} {}任务", futureType, nextOnePeriod, day);
            boolean success = productDeferRunRecordService.doRun(futureType, nextOnePeriod, day);
            if (!success) {
                logger.info("递延费结算 {} {} {} 任务已执行 return", futureType, nextOnePeriod, day);
                return;
            }
            init();
            caluateCashOrder(cashOrderList);
            sendMessageToUser();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.debug("e", e);
        }
    }

    /**
     * 初始化所有用于计算的参数
     */
    public void init() {
        initParam();
        setPeriodMap(productNextOnePeriod, nextOnePeriod);

        List<String> futures = new ArrayList<String>();
        for (String future : futureType.split(",")) {
            futures.add(future);
        }
        logger.info("查询所有的订单数据，涉及的品种为:{}", futureType);
//		
        List<PeroidOrderHolidayVo> holidayList = deferServiceImpl.findCodeHoliday(futures);
        nextDayCode = deferServiceImpl.findCodeNextDayPeriod(futures);
        for (PeroidOrderHolidayVo holiday : holidayList) {
            if (holiday.getBeginTime() == null || holiday.getEndTime() == null) {
                continue;
            } else {
                codeHoliday.put(holiday.getProductId(), new Date[]{holiday.getBeginTime(), holiday.getEndTime()});
            }
        }

        cashOrderList = deferServiceImpl.findAllCashOrderByCode(futures);
        logger.info("查询所有的订单数据为:{}", JSONObject.toJSONString(cashOrderList));

        if (CollectionUtils.isNotEmpty(cashOrderList)) {
            for (NextPeroidOrderInfo o1 : cashOrderList) {
                userTele.put(o1.getUserId(), o1.getTele());
            }
        }

        //差价合约持仓
        try {
            List<DigitalCoinPosition> dataList = coinPositionSumService.getAllBuySellPosition();
            for (DigitalCoinPosition digitalCoinPosition : dataList) {
                String investorId = digitalCoinPosition.getInvestorId();
                String productCode = digitalCoinPosition.getProductCode();
                Integer buyCount = digitalCoinPosition.getBuyCount();
                Integer sellCount = digitalCoinPosition.getSellCount();

                CoinPositionCountBO coinPositionCountBO = new CoinPositionCountBO();
                coinPositionCountBO.setBuyCount(buyCount);
                coinPositionCountBO.setSellCount(sellCount);
                cashPositionMap.put(investorId + productCode, coinPositionCountBO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置所有的参数
     */
    public void initParam() {
        productNextOnePeriod = new HashMap<Integer, String>();
        cashOrderList = new ArrayList<NextPeroidOrderInfo>();
        userFund = new HashMap<String, Double>();
        codeHoliday = new HashMap<Integer, Date[]>();
        nextDayCode = new HashMap<Integer, ProNextTradePeriodVo>();
        userPeriodFail = new HashMap<String, Set<String>>();
        userTele = new HashMap<String, String>();
        cashPositionMap.clear();
    }

    /**
     * 设置递延区间值
     */
    public void setPeriodMap(Map<Integer, String> periodMap, String peroid) {
        Integer shortCode = null;
        String time = "";
        if (peroid.contains(";")) {
            for (String onePeriod : peroid.split(";")) {
                shortCode = Integer.parseInt(onePeriod.split("-")[0]);
                time = onePeriod.split("-")[1];
                periodMap.put(shortCode, time);
            }
        } else {
            shortCode = Integer.parseInt(peroid.split("-")[0]);
            time = peroid.split("-")[1];
            periodMap.put(shortCode, time);
        }
    }

    /**
     * 计算现金订单的递延费
     */
    public void caluateCashOrder(List<NextPeroidOrderInfo> orderList) {
        for (NextPeroidOrderInfo nextPeroidOrderInfo : orderList) {
            OrderCashInfo orderInfo = new OrderCashInfo();
            orderInfo.setOrderId(nextPeroidOrderInfo.getOrderId());
            orderInfo.setDeferInterest(nextPeroidOrderInfo.getDeferInterest() == null ? 0.0 : nextPeroidOrderInfo.getDeferInterest());
            orderInfo.setDeferTimes(nextPeroidOrderInfo.getDeferTimes() == null ? 0 : nextPeroidOrderInfo.getDeferTimes());

            OrderLossProfitDefLog defLog = new OrderLossProfitDefLog();
            //000 默认为系统
            defLog.setUserId("000");
            StringBuilder operateLog = new StringBuilder();

            double count = nextPeroidOrderInfo.getHoldCount();
            double rate = nextPeroidOrderInfo.getRate();
            int plate = nextPeroidOrderInfo.getPlate();

            String userId = nextPeroidOrderInfo.getUserId();
            double fee = DoubleTools.mul(DoubleTools.mul(nextPeroidOrderInfo.getPerDeferInterest(), rate), count);

            fee = DoubleTools.mul(fee, getMultiple(plate, nextPeroidOrderInfo.getTradeDirection(), nextPeroidOrderInfo.getInvestorId(), nextPeroidOrderInfo.getProductCode()));

            /** 用户的当余额*/
            Double balance = 0.0;

            if (userFund.containsKey(userId)) {
                balance = userFund.get(userId);
            } else {
                FundMainCash mainCash = fundAccountService.queryFundMainCash(userId);
                userFund.put(userId, mainCash.getBalance());
                balance = mainCash.getBalance();
            }

            try {
                fundAccountService.updateFundByPeriodFee(userId, nextPeroidOrderInfo.getProductName(), nextPeroidOrderInfo.getDisplayId(), fee);
                operateLog.append(UserContant.ORDER_DEFER_FEE_LOG.replace("value", String.valueOf(fee)));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                logger.info("订单执行递延扣款失败orderid：{}", nextPeroidOrderInfo.getOrderId());
                continue;
            }

            Date sysSetSaleDate = toNextPeroidMethod(nextPeroidOrderInfo, FundTypeEnum.CASH.getValue());
            orderInfo.setSysSetSellDate(sysSetSaleDate);
            operateLog.append(UserContant.ORDER_DEFER_TIME_LOG.replace("older", DateTools.formatDate(nextPeroidOrderInfo.getSysSetSellDate(), DateTools.FORMAT_LONG))
                    .replace("new", DateTools.formatDate(sysSetSaleDate, DateTools.FORMAT_LONG)));

            orderInfo.setDeferTimes(orderInfo.getDeferTimes() + 1);
            orderInfo.setDeferInterest(DoubleTools.add(fee, orderInfo.getDeferInterest()));
            userFund.put(userId, DoubleTools.sub(balance, fee));
            try {
                if (0 > balance) {
                    orderInfo.setDeferStatus(0);
                }
                orderService.updateCashOrderInfo(orderInfo);

                defLog.setContent(operateLog.toString());
                defLog.setCreateTime(new Date());
                defLog.setOrderId(nextPeroidOrderInfo.getOrderId());
                userLogServiceImpl.insertUserOrderLossProfitDeferLog(defLog);
            } catch (Exception e) {
                logger.info("现金订单执行递延失败orderid：{}", nextPeroidOrderInfo.getOrderId());
                continue;
            }
        }
    }

    private double getMultiple(Integer plate, Integer tradeDirection, String investorId, String productCode) {
        double multiple = 1D;
        try {
            CoinPositionCountBO coinPositionCountBO = cashPositionMap.get(investorId + productCode);
            logger.info("现金 券商: {} 多单: {} 空单: {} ", investorId, coinPositionCountBO.getBuyCount(), coinPositionCountBO.getSellCount());
            //差价合约
            if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue().intValue() == plate) {
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
                    //看多
                    multiple = getBuyMultiple(coinPositionCountBO.getBuyCount(), coinPositionCountBO.getSellCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == tradeDirection) {
                    //看空
                    multiple = getSellMultiple(coinPositionCountBO.getBuyCount(), coinPositionCountBO.getSellCount());
                }
            }
        } catch (Exception e) {
        }
        logger.info("现金 券商: {} plate: {} tradeDirection: {} multiple: {} ", investorId, plate, tradeDirection, multiple);
        return multiple;
    }


    private Double getBuyMultiple(Integer buyCount, Integer sellCount) {
        if (null == buyCount) {
            buyCount = 0;
        }
        if (null == sellCount) {
            sellCount = 0;
        }
        int sub = buyCount - sellCount;
        int sum = buyCount + sellCount;
        if (sum <= 0) {
            return 1D;
        }

        if (DoubleTools.div(sub, sum) > 0.9D) {
            return 3D;
        } else if (DoubleTools.div(sub, sum) > 0.7D) {
            return 2D;
        } else if (DoubleTools.div(sub, sum) > 0.5D) {
            return 1.5D;
        }

        return 1D;
    }

    private Double getSellMultiple(Integer buyCount, Integer sellCount) {
        if (null == buyCount) {
            buyCount = 0;
        }
        if (null == sellCount) {
            sellCount = 0;
        }

        int sub = sellCount - buyCount;
        int sum = buyCount + sellCount;

        if (sum <= 0) {
            return 1D;
        }
        if (DoubleTools.div(sub, sum) > 0.9D) {
            return 3D;
        } else if (DoubleTools.div(sub, sum) > 0.7D) {
            return 2D;
        } else if (DoubleTools.div(sub, sum) > 0.5D) {
            return 1.5D;
        }
        return 1D;
    }

    /**
     * 校验递延日期是否符合
     *
     * @param date           校验日期
     * @param settlementDate 交割时间
     * @param i              次数
     * @return
     * @throws
     * @return: boolean
     * @author yuanxin
     * @Date 2017年2月8日 下午8:15:34
     */
    @SuppressWarnings("deprecation")
    public boolean chkIfTradeDate(Date date, Date settlementDate, String futureType, int i) {
        boolean flag = true;
        logger.info("判断第一次递延的日期的后三天是否到达合约到期日");
        if (settlementDate != null && settlementDate.before(date)) {
            return false;
        } else {
            if (date.getDay() == 0) {
//				flag = chkIfTradeDate(DateTools.addDay(date, 1),null,futureType,i) ;// 周日不算入总的连续持仓次数
                return chkIfTradeDate(DateTools.addDay(date, 1), null, futureType, i);
            } else if (date.getDay() == 6) { // 周六
                if (nextDayCode.containsKey(futureType)) {
                    ProNextTradePeriodVo nextTradePeriod = nextDayCode.get(futureType);
                    if (Integer.parseInt(nextTradePeriod.getNextDayTime().split(":")[0]) < date.getHours()) {
//						 flag = chkIfTradeDate(DateTools.addDay(date, 1),null,futureType,i);
                        return chkIfTradeDate(DateTools.addDay(date, 1), null, futureType, i);
                    }
                }
            } else if (date.getDay() == 1) {
                if (nextDayCode.containsKey(futureType)) {
                    ProNextTradePeriodVo nextTradePeriod = nextDayCode.get(futureType);
                    if (Integer.parseInt(nextTradePeriod.getNextDayTime().split(":")[0]) >= date.getHours()) {
//						 flag = chkIfTradeDate(DateTools.addDay(date, 1),null,futureType,i);
                        return chkIfTradeDate(DateTools.addDay(date, 1), null, futureType, i);
                    }
                }
            }
        }

        if (flag) {
            if (codeHoliday.containsKey(futureType)) {
                Date[] dateT = codeHoliday.get(futureType);
                if (dateT[0].before(date) && dateT[1].after(date)) {
                    if (i > 1) {
                        return chkIfTradeDate(DateTools.addDay(date, 1), null, futureType, i--);
                    } else {
                        return false;
                    }
                }
            }
        } else {
            if (i > 1) {
                return chkIfTradeDate(DateTools.addDay(date, 1), null, futureType, i--);
            } else {
                return false;
            }
        }

        return flag;
    }

    /**
     * 判断清仓时间点是否在交割日，是的话返回null(返回正常分钟)
     *
     * @author yuanxin
     * @Date 2017年3月31日 上午9:52:36
     */
    @SuppressWarnings("deprecation")
    public Date toNextPeroidMethod(NextPeroidOrderInfo info, Integer fundType) {
        Date date = DateTools.addMinute(info.getSysSetSellDate(), 2);
        String nextOne = productNextOnePeriod.get(info.getProductId());

        Date sysSetSaleDate = (Date) date.clone();
        sysSetSaleDate.setHours(Integer.parseInt(nextOne.split(":")[0]));
        sysSetSaleDate.setMinutes(Integer.parseInt(nextOne.split(":")[1])); // 分钟此时为整数
        if (Integer.parseInt(nextOne.split(":")[0]) <= date.getHours()) {
            sysSetSaleDate = DateTools.addDay(sysSetSaleDate, 1);
        }

        if (info.getExpirationTime().compareTo(sysSetSaleDate) <= 0) {

            if (fundType == FundTypeEnum.CASH.getValue()) {
                if (userPeriodFail.containsKey(info.getUserId())) {
                    Set<String> set = userPeriodFail.get(info.getUserId());
                    set.add(info.getProductName() + info.getProductCode());
                } else {
                    Set<String> set = new HashSet<String>();
                    set.add(info.getProductName() + info.getProductCode());
                    userPeriodFail.put(info.getUserId(), set);
                }
            }

            return info.getSysSetSellDate();
        } else {
            return DateTools.addMinute(sysSetSaleDate, -2);
        }
    }

    public void sendMessageToUser() {
        logger.info("userTele:{}", JSONObject.toJSONString(userTele));
        if (!userPeriodFail.isEmpty()) {
            Set<String> userIdSet = userPeriodFail.keySet();
            for (String id : userIdSet) {
                Set<String> codes = userPeriodFail.get(id);
                StringBuilder codeStr = new StringBuilder();

                if (!codes.isEmpty()) {
                    for (String code : codes) {
                        if (codeStr.toString().getBytes().length > 46) {
                            break;
                        }
                        codeStr.append("," + code);
                    }
                }

                codeStr.append("等");
                if (codeStr.length() != 0) {
                    logger.info("id:{},userIdSet:{}", id, JSONObject.toJSONString(userIdSet));
                    sendMessage(userTele.get(id), codeStr.toString().replaceFirst(",", ""));
                }
            }
        }

    }

    public void sendMessage(String tele, String content) {
        SystemMessage ann = new SystemMessage();
        ann.setUserId("-999"); // 注册时不存在用户信息，赋默认值
        ann.setDestination(tele);
        ann.setContent(content);
        ann.setCause(UserContant.EXPIRATION_MSG_MARK);
        ann.setType(UserContant.SMS_SHORT_TYPE);
        ann.setSmsType(Integer.parseInt(UserContant.EXPIRATION_MSG_TYPE));
        ann.setPriority(0);
        ann.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS); // 默认为发送成功
        ann.setUserType(0);
        ann.setIp("");
        ann.setCreateDate(new Date());

        try {
            smsService.sendExpirationMsg(ann);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("短信发送失败，内容为：{}", content);
        }
    }

    public String getFutureType() {
        return futureType;
    }

    public void setFutureType(String futureType) {
        this.futureType = futureType;
    }

    public String getNextOnePeriod() {
        return nextOnePeriod;
    }

    public void setNextOnePeriod(String nextOnePeriod) {
        this.nextOnePeriod = nextOnePeriod;
    }

    /**
     * @return the fundAccountService
     */
    public IFundAccountApiService getFundAccountService() {
        return fundAccountService;
    }

    /**
     * @param fundAccountService the fundAccountService to set
     */
    public void setFundAccountService(IFundAccountApiService fundAccountService) {
        this.fundAccountService = fundAccountService;
    }

    /**
     * @return the orderService
     */
    public IOrderFunctionService getOrderService() {
        return orderService;
    }

    /**
     * @param orderService the orderService to set
     */
    public void setOrderService(IOrderFunctionService orderService) {
        this.orderService = orderService;
    }

    /**
     * @return the smsService
     */
    public ISmsApiService getSmsService() {
        return smsService;
    }

    /**
     * @param smsService the smsService to set
     */
    public void setSmsService(ISmsApiService smsService) {
        this.smsService = smsService;
    }

    /**
     * @return the deferServiceImpl
     */
    public DeferService getDeferServiceImpl() {
        return deferServiceImpl;
    }

    /**
     * @param deferServiceImpl the deferServiceImpl to set
     */
    public void setDeferServiceImpl(DeferService deferServiceImpl) {
        this.deferServiceImpl = deferServiceImpl;
    }

    /**
     * @return the userLogServiceImpl
     */
    public IUserApiLogService getUserLogServiceImpl() {
        return userLogServiceImpl;
    }

    /**
     * @param userLogServiceImpl the userLogServiceImpl to set
     */
    public void setUserLogServiceImpl(IUserApiLogService userLogServiceImpl) {
        this.userLogServiceImpl = userLogServiceImpl;
    }

    public ProductDeferRunRecordService getProductDeferRunRecordService() {
        return productDeferRunRecordService;
    }

    public void setProductDeferRunRecordService(ProductDeferRunRecordService productDeferRunRecordService) {
        this.productDeferRunRecordService = productDeferRunRecordService;
    }

    public ICoinPositionSumService getCoinPositionSumService() {
        return coinPositionSumService;
    }

    public void setCoinPositionSumService(ICoinPositionSumService coinPositionSumService) {
        this.coinPositionSumService = coinPositionSumService;
    }

}

