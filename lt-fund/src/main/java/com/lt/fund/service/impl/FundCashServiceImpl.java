/*
 * PROJECT NAME: lt-fund
 * PACKAGE NAME: com.lt.fund.service
 * FILE    NAME: FundCashServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.fund.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import com.lt.api.sms.ISmsApiService;
import com.lt.api.sys.IThreadLockService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundIoRechargeEnum;
import com.lt.enums.fund.FundIoWithdrawalEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.enums.sys.SysThreadLockEnum;
import com.lt.fund.alipay.config.AlipayRechargeConfig;
import com.lt.fund.alipay.util.AlipayRechargeSubmit;
import com.lt.fund.dao.FundFlowCashDao;
import com.lt.fund.dao.FundIoCashRechargeDao;
import com.lt.fund.dao.FundIoCashWithdrawalDao;
import com.lt.fund.dao.FundMainCashDao;
import com.lt.fund.dao.FundOptCodeDao;
import com.lt.fund.dao.FundPayAuthFlowDao;
import com.lt.fund.service.IFundCashRechargeService;
import com.lt.fund.service.IFundCashService;
import com.lt.fund.service.ISwiftPassService;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundOptCode;
import com.lt.model.fund.FundPayAuthFlow;
import com.lt.model.promote.CommisionIo;
import com.lt.model.sms.SystemMessage;
import com.lt.model.sms.SystemMessageContant;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.UserContant;
import com.lt.model.user.charge.BankChargeDetail;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.Base64Utils;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.RSAUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.MD5Util;
import com.lt.util.utils.dinpay.DinpayHttpTools;
import com.lt.util.utils.model.Response;

import javolution.util.FastMap;

/**
 * TODO 现金账户业务实现
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年11月30日 下午4:43:10
 */
@Service
public class FundCashServiceImpl implements IFundCashService {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 828286934590797225L;
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(FundCashServiceImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FundMainCashDao fundMainCashDao;
    @Autowired
    private FundFlowCashDao fundFlowCashDao;
    @Autowired
    private FundIoCashRechargeDao fundIoCashRechargeDao;
    @Autowired
    private FundOptCodeDao fundOptCodeDao;
    @Autowired
    private FundIoCashWithdrawalDao fundIoCashWithdrawalDao;
    @Autowired
    private FundPayAuthFlowDao fundPayAuthFlowDao;
    @Autowired
    private IUserApiBussinessService userApiBussinessService;
    @Autowired
    private IThreadLockService threadLockService;
    @Autowired
    private ISmsApiService smsApiService;
    @Autowired
    private ISwiftPassService swiftPassService;


    @Autowired
    private IFundCashRechargeService fundCashRechargeService;

    /**
     * @param userId
     * @param amount
     * @return
     * @author XieZhibing
     * @date 2016年12月9日 下午3:20:37
     * @see com.lt.fund.service.IFundCashService#isCashBalanceEnough(java.lang.Integer,
     * double)
     */
    @Override
    public boolean isCashBalanceEnough(String userId, double amount) {
        // TODO Auto-generated method stub
        return fundMainCashDao.queryFundMainCashBalance(userId, amount) > 0;
    }

    /**
     * @param userId
     * @return
     * @author XieZhibing
     * @date 2016年12月7日 下午9:54:59
     * @see com.lt.api.fund.IFundCashService#queryFundMainCash(java.lang.Integer)
     */
    @Override
    public FundMainCash queryFundMainCash(String userId) {
        // TODO Auto-generated method stub
        return fundMainCashDao.queryFundMainCash(userId);
    }

    /**
     * @param userId
     * @param amount
     * @return
     * @author XieZhibing
     * @date 2016年12月6日 下午1:45:31
     * @see com.lt.api.fund.IFundCashService#checkFundMainCashBalance(java.lang.Integer,
     * double)
     */
    @Override
    public boolean doCheckFundMainBalance(String userId, double amount) {
        // TODO Auto-generated method stub
        FundMainCash fundMainCash = fundMainCashDao.queryFundMainCash(userId);
        logger.debug("查询userId:{}现金主账户信息", userId);
        if (fundMainCash == null || fundMainCash.getBalance() < 1) {
            return false;
        }

        if (fundMainCash.getBalance() > amount) {
            logger.info("userId:{}现金主账户可用余额充足", userId);
            return true;
        }

        logger.info("userId:{}现金主账户可用余额不足", userId);
        return false;
    }

    /**
     * @param userId
     * @return
     * @author XieZhibing
     * @date 2016年11月30日 下午8:15:48
     * @see com.lt.api.fund.IFundCashService#initFundCashAccount(java.lang.Integer)
     */
    @Override
    public boolean doInitFundCashAccount(String userId) {
        // TODO Auto-generated method stub

        logger.info("初始化现金主账户，先查用户是否存在资金账户.没有才创建资金账户");

        FundMainCash fmc = fundMainCashDao.queryFundMainCash(userId);
        if (StringTools.isNotEmpty(fmc)) {
            logger.info("用户资金账户已存在, 不允许再次初始化资金");
            throw new LTException(LTResponseCode.FU00013);
        }
        fundMainCashDao.initFundMainCash(userId);

        return true;
    }

    /**
     * @param productName      商品名称
     * @param orderId          订单ID
     * @param userId           用户ID
     * @param holdFund         止损保证金
     * @param deferFund        递延保证金
     * @param actualCounterFee 总手续费
     * @return
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月7日 下午2:24:35
     * @see com.lt.fund.service.IFundCashService#doBuy(java.lang.String,
     * java.lang.String, java.lang.Integer, double, double, double)
     */
    public boolean doBuy(String productName, String orderId, String userId, double holdFund, double deferFund,
                         double actualCounterFee) throws LTException {
        // TODO Auto-generated method stubo

        long startTime1 = System.currentTimeMillis();

        // 止损保证金
        if (holdFund <= 0) {
            throw new LTException(LTResponseCode.TD00014);
        }

        // 查询用户现金账户信息
        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        logger.info("查询userId:{}现金主账户信息", userId);

        if (userFundMainCash == null) {
            throw new LTException(LTResponseCode.FU00007);
        }

        // 总金额
//		double userTotalAmount = holdFund + deferFund + actualCounterFee;
        double userTotalAmount = DoubleTools.add(DoubleTools.add(holdFund, deferFund), actualCounterFee);
        // 用户现金账户余额不足
        if (userFundMainCash.getBalance() < userTotalAmount) {
            throw new LTException(LTResponseCode.FU00008);
        }
        logger.info("校验userId:{}用户现金账户余额");

        // 当前时间
        Date currentTime = new Date();
        // 资金流水
        List<FundFlow> flowList = new ArrayList<FundFlow>();

        // =============1 冻结用户止损保证金流水=============
        // 用户现金账户余额
        double userBalance = userFundMainCash.getBalance();
        // 用户余额 = 用户余额 - 止损保证金
        userBalance = DoubleTools.sub(userBalance, holdFund);
        logger.info("==========前holdFund={}===", holdFund);
        // 增加用户止损保证金明细
        FundFlow holdFundFlow = new FundFlow(productName, FundCashOptCodeEnum.FREEZE_HOLDFUND, userId, holdFund,
                userBalance, orderId, currentTime, currentTime);
        logger.info("==========后holdFund={}===", holdFundFlow.getAmount());
        flowList.add(holdFundFlow);
        logger.info("增加userId:{}止损保证金明细");

        // =============2 递延保证金流水=============
        // 冻结递延保证金
        if (deferFund > 0) {
            // 用户余额 = 用户余额 - 递延保证金
//			userBalance = userBalance - deferFund;
            userBalance = DoubleTools.sub(userBalance, deferFund);
            // 增加用户冻结递延保证金明细
            FundFlow deferFundFlow = new FundFlow(productName, FundCashOptCodeEnum.FREEZE_DEFERFUND, userId, deferFund,
                    userBalance, orderId, currentTime, currentTime);
            flowList.add(deferFundFlow);
            logger.info("增加userId:{}冻结递延保证金明细");
        }

        // =============3 扣除手续费流水=============
        // 扣除手续费
        // 用户余额 = 用户余额 - 手续费
//		userBalance = userBalance - actualCounterFee;
        userBalance = DoubleTools.sub(userBalance, actualCounterFee);
        // 增加手续费明细
        FundFlow platformFeeFlow = new FundFlow(productName, FundCashOptCodeEnum.DEDUCT_PLATFORM_FEE, userId,
                actualCounterFee, userBalance, orderId, currentTime, currentTime);
        flowList.add(platformFeeFlow);
        logger.info("增加userId:{}手续费明细");

        // =============4 更新用户主账户、新增流水=============
        // 添加现金流水明细
        fundFlowCashDao.addFundFlowCashList(flowList);

        // 扣除用户止损保证金、递延保证金、手续费
        this.doDeductFund(userId, holdFund, deferFund, actualCounterFee);
        logger.info("扣除userId:{}用户止损保证金、递延保证金、手续费完成");

        logger.info("开仓扣款用时time:{}ms", (System.currentTimeMillis() - startTime1));
        return true;
    }

    /**
     * @param productName 商品名称
     * @param orderId     订单ID
     * @param userId      用户ID
     * @param holdFund    止损保证金
     * @param deferFund   递延保证金
     * @param userBenefit 用户净利润
     * @return
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月7日 下午2:28:44
     * @see com.lt.api.fund.IFundCashService#doBalance(java.lang.String,
     * java.lang.String, java.lang.Integer, double, double)
     */
    @Override
    public void doBalance(String productName, String orderId, String userId, double holdFund, double deferFund,
                          double userBenefit) throws LTException {
        // TODO Auto-generated method stub

        logger.info("用户:{}, 订单:{}, 开始结算", userId, orderId);

        // 查询用户现金账户信息
        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        logger.info("查询userId:{}现金主账户信息", userId);
        if (userFundMainCash == null) {
            throw new LTException(LTResponseCode.FU00007);
        }

        // 当前时间
        Date currentTime = new Date();
        // 用户可用余额
        Double userBalance = userFundMainCash.getBalance();
        logger.info("========oldUserBalance={}====", userBalance);

        int i = 0;
        // 1 退回用户止损保证金
        i = this.doReFundHoldFund(orderId, userId, holdFund, userBalance, productName, currentTime);
        if (i == 0) {
            logger.info("=========orderId={},退回用户止损保证金异常========", orderId);
            throw new LTException(LTResponseCode.ER400);
        }
        userBalance = DoubleTools.add(userBalance, holdFund);//退回保证金后的余额

        // 2 退回递延保证金
        if (deferFund > 0) {
            i = this.doReFundDeferFund(orderId, userId, deferFund, userBalance, productName, currentTime);
            if (i == 0) {
                logger.info("=========orderId={},退回递延保证金异常========", orderId);
                throw new LTException(LTResponseCode.ER400);
            }
            userBalance = DoubleTools.add(userBalance, deferFund);//退回递延保证金后的余额
        }


        // 3 增加用户利润
        i = this.addUserBenefit(orderId, userId, userBenefit, userBalance, productName, currentTime);
        if (i == 0) {
            logger.info("=========orderId={},增加用户利润异常========", orderId);
            throw new LTException(LTResponseCode.ER400);
        }

    }

    /**
     * TODO 开仓失败退款
     *
     * @param productName      商品名称
     * @param orderId          订单ID
     * @param userId           用户ID
     * @param holdFund         止损保证金
     * @param deferFund        递延保证金
     * @param actualCounterFee 总手续费
     * @return
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月7日 下午7:02:23
     * @see com.lt.api.fund.IFundCashService#doRefund(java.lang.String,
     * java.lang.String, java.lang.Integer, double, double, double,
     * java.lang.Integer, double, double)
     */
    @Override
    public void doRefund(String productName, String orderId, String userId, double holdFund, double deferFund,
                         double actualCounterFee) throws LTException {

        // 查询用户现金账户信息
        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        logger.info("查询userId:{}现金主账户信息", userId);
        if (userFundMainCash == null) {
            throw new LTException(LTResponseCode.FU00007);
        }

        // 当前时间
        Date currentTime = new Date();
        // 用户积分账户余额
        Double userBalance = userFundMainCash.getBalance();

        int i = 0;
        // 1 退回用户止损保证金=============
        i = this.doReFundHoldFund(orderId, userId, holdFund, userBalance, productName, currentTime);
        if (i == 0) {
            logger.info("=========orderId={},退回用户止损保证金异常========", orderId);
            throw new LTException(LTResponseCode.ER400);
        }
        userBalance = DoubleTools.add(userBalance, holdFund);//退回保证金后的余额

        // 2 退回递延保证金=============
        if (deferFund > 0) {
            i = this.doReFundDeferFund(orderId, userId, deferFund, userBalance, productName, currentTime);
            if (i == 0) {
                logger.info("=========orderId={},退回递延保证金异常========", orderId);
                throw new LTException(LTResponseCode.ER400);
            }
            userBalance = DoubleTools.add(userBalance, deferFund);//退回递延保证金后的余额
        }


        // 3 退回手续费=============
        i = this.doReFundActualCounterFee(orderId, userId, actualCounterFee, userBalance, productName,
                currentTime);
        if (i == 0) {
            logger.info("=========orderId={},退回手续费金异常========", orderId);
            throw new LTException(LTResponseCode.ER400);
        }

    }

    @Override
    public FundMainCash queryFundMainCashForUpdate(String userId) {
        // 查询用户现金账户信息
        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        logger.info("查询userId:{}现金主账户信息", userId);
        if (userFundMainCash == null) {
            throw new LTException(LTResponseCode.FU00007);
        }
        return userFundMainCash;

    }

    /**
     * TODO 查询止损保证金流水汇总
     *
     * @param orderId
     * @param userId
     * @return
     * @author XieZhibing
     * @date 2017年2月9日 下午3:23:30
     * @see com.lt.fund.service.IFundCashService#queryHoldFundFlowSum(java.lang.String,
     * java.lang.Integer)
     */
    public Double queryHoldFundFlowSum(String orderId, String userId) {

        List<String> secondOptcodeList = new ArrayList<String>();
        // 40401 止损保证金冻结
        secondOptcodeList.add(FundCashOptCodeEnum.FREEZE_HOLDFUND.getCode());
        // 30501 止损保证金解冻
        secondOptcodeList.add(FundCashOptCodeEnum.UNFREEZE_HOLDFUND.getCode());

        return fundFlowCashDao.queryFundFlowCashSum(orderId, userId, secondOptcodeList);
    }

    /**
     * TODO 查询递延保证金流水汇总
     *
     * @param orderId
     * @param userId
     * @return
     * @author XieZhibing
     * @date 2017年2月9日 下午3:23:54
     * @see com.lt.fund.service.IFundCashService#queryDeferFundFlowSum(java.lang.String,
     * java.lang.Integer)
     */
    public Double queryDeferFundFlowSum(String orderId, String userId) {

        List<String> secondOptcodeList = new ArrayList<String>();
        // 40403 递延金冻结
        secondOptcodeList.add(FundCashOptCodeEnum.FREEZE_DEFERFUND.getCode());
        // 30501 递延金解冻
        secondOptcodeList.add(FundCashOptCodeEnum.UNFREEZE_DEFERFUND.getCode());

        return fundFlowCashDao.queryFundFlowCashSum(orderId, userId, secondOptcodeList);
    }

    /**
     * TODO 查询手续费流水汇总
     *
     * @param orderId
     * @param userId
     * @return
     * @author XieZhibing
     * @date 2017年2月9日 下午3:24:00
     * @see com.lt.fund.service.IFundCashService#queryActualCounterFeeFlowSum(java.lang.String,
     * java.lang.Integer)
     */
    public Double queryActualCounterFeeFlowSum(String orderId, String userId) {

        List<String> secondOptcodeList = new ArrayList<String>();
        // 40501 手续费扣除
        secondOptcodeList.add(FundCashOptCodeEnum.DEDUCT_PLATFORM_FEE.getCode());
        // 30601 手续费退款
        secondOptcodeList.add(FundCashOptCodeEnum.REFUND_PLATFORM_FEE.getCode());

        return fundFlowCashDao.queryFundFlowCashSum(orderId, userId, secondOptcodeList);
    }

    /**
     * TODO 扣除用户止损保证金、递延保证金、手续费
     *
     * @param userId
     * @param holdFund
     * @param deferFund
     * @param actualCounterFee
     * @param userTotalAmount
     * @author XieZhibing
     * @date 2017年2月14日 上午10:12:20
     */
    private void doDeductFund(String userId, double holdFund, double deferFund, double actualCounterFee) {
        // 总费用
//		double userTotalAmount = holdFund + deferFund + actualCounterFee;
        double userTotalAmount = DoubleTools.add(DoubleTools.add(holdFund, deferFund), actualCounterFee);
        FundMainCash userFundMainCash = new FundMainCash();
        userFundMainCash.setUserId(userId);
        // 增加用户主账户止损保证金
        userFundMainCash.setHoldFund(holdFund);
        // 增加用户冻结递延保证金
        userFundMainCash.setDeferFund(deferFund);
        // 增加用户累计手续费
        userFundMainCash.setTotalCounterFee(actualCounterFee);
        // 用户可用余额减少
        userFundMainCash.setBalance(-userTotalAmount);

        // 更新用户主账户
        fundMainCashDao.updateFundMainCash(userFundMainCash);
    }

    /**
     * TODO 退止损保证金
     *
     * @param orderId
     * @param userId
     * @param holdFund
     * @param userBalance
     * @param productName
     * @param currentTime
     * @return
     * @author XieZhibing
     * @date 2017年2月9日 下午5:29:13
     */
    private int doReFundHoldFund(String orderId, String userId, double holdFund, double userBalance,
                                 String productName, Date currentTime) {

        // 剩余保证金
        Double holdFundFlowSum = this.queryHoldFundFlowSum(orderId, userId);
        logger.info("开始退止损保证金, userId:{}, orderId:{}, userBalance:{}, holdFund:{}, holdFundFlowSum:{}", userId, orderId,
                userBalance, holdFund, holdFundFlowSum);

        if (holdFundFlowSum == null || DoubleTools.add(holdFundFlowSum, holdFund) > 0) {
            throw new LTException("用户" + userId + "的订单" + orderId + "流水异常, 剩余保证金:" + Math.abs(holdFundFlowSum)
                    + ", 传入保证金:" + holdFund + ", 请数据管理员核实流水数据");
        }


        // 增加用户解冻止损保证金明细
        FundFlow holdFundFlow = new FundFlow(productName, FundCashOptCodeEnum.UNFREEZE_HOLDFUND, userId, holdFund,
                DoubleTools.add(userBalance, holdFund), orderId, currentTime, currentTime);

        // 添加现金流水明细
        fundFlowCashDao.addFundFlowCash(holdFundFlow);
        logger.info("添加现金流水, userId:{}, orderId:{}, userBalance:{}, holdFundFlow:{}", userId, orderId, userBalance + holdFund,
                holdFundFlow);

        // 现金账户
        FundMainCash fundMainCash = new FundMainCash();
        // 设置用户ID
        fundMainCash.setUserId(userId);
        // 退回用户主账户止损保证金
        fundMainCash.setHoldFund(-holdFund);
        // 用户可用余额增加
        fundMainCash.setBalance(holdFund);
        // 更新用户主账户
        int i = fundMainCashDao.updateFundMainCash(fundMainCash);
        logger.info("退止损保证金完成, userId:{}, orderId:{}, holdFund:{}", userId, orderId, holdFund);

        return i;
    }

    /**
     * TODO 退递延保证金
     *
     * @param orderId
     * @param userId
     * @param deferFund
     * @param userBalance
     * @param productName
     * @param currentTime
     * @return
     * @author XieZhibing
     * @date 2017年2月9日 下午5:29:21
     */
    private int doReFundDeferFund(String orderId, String userId, double deferFund, double userBalance,
                                  String productName, Date currentTime) {

        // 无递延可退
        if (deferFund <= 0) {
            return 0;
        }

        // 剩余递延保证金
        Double deferFundFlowSum = this.queryDeferFundFlowSum(orderId, userId);
        logger.info("开始退递延保证金, userId:{}, orderId:{}, userBalance:{}, deferFund:{}, deferFundFlowSum:{}", userId,
                orderId, userBalance, deferFund, deferFundFlowSum);

        if (deferFundFlowSum == null || DoubleTools.add(deferFundFlowSum, deferFund) > 0) {
            throw new LTException("用户" + userId + "的订单" + orderId + "流水异常, 剩余递延保证金:" + Math.abs(deferFundFlowSum)
                    + ", 传入递延保证金:" + deferFund + ", 请数据管理员核实流水数据");
        }

        // 增加用户解冻递延保证金明细
        FundFlow deferFundFlow = new FundFlow(productName, FundCashOptCodeEnum.UNFREEZE_DEFERFUND, userId, deferFund,
                DoubleTools.add(userBalance, deferFund), orderId, currentTime, currentTime);

        // 添加现金流水明细
        fundFlowCashDao.addFundFlowCash(deferFundFlow);
        logger.info("添加现金流水, userId:{}, orderId:{}, userBalance:{}, deferFundFlow:{}", userId, orderId, DoubleTools.add(userBalance, deferFund),
                deferFundFlow);

        // 现金账户
        FundMainCash fundMainCash = new FundMainCash();
        // 设置用户ID
        fundMainCash.setUserId(userId);
        // 退回用户冻结递延保证金
        fundMainCash.setDeferFund(-deferFund);
        // 用户可用余额增加
        fundMainCash.setBalance(deferFund);
        // 更新用户主账户
        int i = fundMainCashDao.updateFundMainCash(fundMainCash);
        logger.info("退递延保证金完成, userId:{}, orderId:{}, deferFund:{}", userId, orderId, deferFund);

        return i;
    }

    /**
     * TODO 退手续费
     *
     * @param orderId
     * @param userId
     * @param actualCounterFee
     * @param userBalance
     * @param productName
     * @param currentTime
     * @return
     * @author XieZhibing
     * @date 2017年2月9日 下午5:29:30
     */
    private int doReFundActualCounterFee(String orderId, String userId, double actualCounterFee, double userBalance,
                                         String productName, Date currentTime) {
        actualCounterFee = DoubleTools.scaleFormat(actualCounterFee, 2);
        // 剩余手续费
        Double actualCounterFeeFlowSum = this.queryActualCounterFeeFlowSum(orderId, userId);
        logger.info("开始退手续费, userId:{}, orderId:{}, userBalance:{}, actualCounterFee:{}, actualCounterFeeFlowSum:{}",
                userId, orderId, userBalance, actualCounterFee, actualCounterFeeFlowSum);

        if (actualCounterFeeFlowSum == null || DoubleTools.add(actualCounterFeeFlowSum, actualCounterFee) > 0) {
            throw new LTException("用户" + userId + "的订单" + orderId + "流水异常, 剩余手续费:" + Math.abs(actualCounterFeeFlowSum)
                    + ", 传入手续费:" + actualCounterFee + ", 请数据管理员核实流水数据");
        }

        // 增加退回总手续费明细
        FundFlow platformFeeFlow = new FundFlow(productName, FundCashOptCodeEnum.REFUND_PLATFORM_FEE, userId,
                actualCounterFee, DoubleTools.add(userBalance, actualCounterFee), orderId, currentTime, currentTime);

        // 添加现金流水明细
        fundFlowCashDao.addFundFlowCash(platformFeeFlow);
        logger.info("添加现金流水, userId:{}, orderId:{}, userBalance:{}, platformFeeFlow:{}", userId, orderId, userBalance + actualCounterFee,
                platformFeeFlow);

        // 现金账户
        FundMainCash fundMainCash = new FundMainCash();
        // 设置用户ID
        fundMainCash.setUserId(userId);
        // 退回用户累计手续费
        fundMainCash.setTotalCounterFee(-actualCounterFee);
        // 用户可用余额增加
        fundMainCash.setBalance(actualCounterFee);
        // 更新用户主账户
        int i = fundMainCashDao.updateFundMainCash(fundMainCash);
        logger.info("退手续费完成, userId:{}, orderId:{}, actualCounterFee:{}", userId, orderId, actualCounterFee);

        return i;
    }

    /**
     * TODO 增加用户利润
     *
     * @param orderId
     * @param userId
     * @param userBenefit
     * @param userBalance
     * @param productName
     * @param currentTime
     * @author XieZhibing
     * @date 2017年2月9日 下午5:07:04
     */
    private int addUserBenefit(String orderId, String userId, double userBenefit, double userBalance,
                               String productName, Date currentTime) {

        logger.info("开始增加用户利润, userId:{}, orderId:{}, userBalance:{}, userBenefit:{}", userId, orderId, userBalance,
                userBenefit);


        // 用户利润绝对值
        double benefit = userBenefit < 0 ? -userBenefit : userBenefit;
        logger.info("========前benefit={}=========", benefit);
        // 利润流水明细
        FundFlow userProfitFlow = null;
        if (userBenefit >= 0) {
            // 用户盈利明细
            userProfitFlow = new FundFlow(productName, FundCashOptCodeEnum.USER_PROFIT, userId, benefit, DoubleTools.add(userBalance, userBenefit),
                    orderId, currentTime, currentTime);
        } else {
            // 用户亏损明细
            userProfitFlow = new FundFlow(productName, FundCashOptCodeEnum.USER_LOSS, userId, benefit, DoubleTools.add(userBalance, userBenefit),
                    orderId, currentTime, currentTime);
        }
        logger.info("========后benefit={}=========", userProfitFlow.getAmount());
        // 添加现金流水明细
        fundFlowCashDao.addFundFlowCash(userProfitFlow);
        logger.info("添加现金流水, userId:{}, orderId:{}, userBalance:{}, userProfitFlow:{}", userId, orderId, DoubleTools.add(userBalance, userBenefit),
                userProfitFlow);

        // =============4 计算用户主账户=============
        // 现金账户
        FundMainCash fundMainCash = new FundMainCash();
        // 设置用户ID
        fundMainCash.setUserId(userId);
        // 增加用户累计盈利
        fundMainCash.setTotalBenefitAmount(userBenefit);
        // 用户可用余额增加
        fundMainCash.setBalance(userBenefit);

        // 更新用户主账户
        int i = fundMainCashDao.updateFundMainCash(fundMainCash);
        logger.info("增加用户利润完成, userId:{}, orderId:{}, userBenefit:{}", userId, orderId, userBenefit);
        // 用户余额 = 用户余额 + 用户利润
        return i;
    }

    /**
     * 冻结or解冻券商止盈保证金流水
     *
     * @param flowList
     * @param investorBalance
     * @param stopProfit
     * @param productName
     * @param investorId
     * @param orderId
     * @param currentTime
     */
    @Override
    public void freezeInvestorStopProfit(List<FundFlow> flowList, double investorBalance, double stopProfit,
                                         String productName, String investorId, String orderId, Date currentTime) {

        if (stopProfit == 0) {
            logger.error("止盈保证金为0 不需要冻结或者解冻");
            return;
        }
        // 券商余额 = 券商余额 + 解冻止盈保证金
        investorBalance = DoubleTools.add(investorBalance, stopProfit);
        // 如果 保证金大于0 解冻保证金类型 否则 冻结保证金类型
        IFundOptCode optCodeEnum = stopProfit > 0 ? FundCashOptCodeEnum.UNFREEZE_STOPPROFIT
                : FundCashOptCodeEnum.FREEZE_STOPPROFIT;
        // 增加券商解冻止盈保证金明细
        FundFlow stopProfitFlow = new FundFlow(productName, optCodeEnum, investorId, stopProfit, investorBalance,
                orderId, currentTime, currentTime);
        flowList.add(stopProfitFlow);
    }

    /**
     * 退回or扣款券商抽成手续费
     *
     * @param flowList
     * @param investorBalance 券商账号余额
     * @param investorFee     券商抽成手续费
     * @param productName
     * @param investorId
     * @param orderId
     * @param currentTime
     */
    @Override
    public void refundProrata(List<FundFlow> flowList, double investorBalance, double investorFee, String productName,
                              String investorId, String orderId, Date currentTime) {

        // 券商余额 = 券商余额 + 券商抽成手续费
        investorBalance = DoubleTools.add(investorBalance, investorFee);
        // 如果 券商抽成手续费 大于0 券商抽成手续费 否则 券商抽成手续费退回
        IFundOptCode optCodeEnum = investorFee > 0 ? FundCashOptCodeEnum.INCOME_INVESTOR_FEE
                : FundCashOptCodeEnum.OUTLAY_INVESTOR_FEE;
        // 增加券商抽取手续费取出明细
        FundFlow incomeFeeflow = new FundFlow(productName, optCodeEnum, investorId, investorFee, investorBalance,
                orderId, currentTime, currentTime);
        flowList.add(incomeFeeflow);
    }

    /**
     * 券商主账户计算 扣款时 主账户 减少金额 所以 investorFee：正数 stopProfit ：负数 //券商余额变动 = （-保证金） +
     * 手续费抽成 退款时 主账户 增加金额 所以 stopProfit：正数 investorFee：负数 //券商余额变动 = 保证金 +
     * （-手续费抽成）
     *
     * @param investorFundMainCash
     * @param stopProfit           保证金
     * @param investorFee          手续费抽成
     */
    @Override
    public void addInvestorAccountDetails(FundMainCash investorFundMainCash, double stopProfit, double investorFee) {

        // 券商余额变动 = （-保证金） + 手续费抽成
        // 券商余额变动 = 解冻保证金 + （-手续费抽成）
        double invstDiffBalance = DoubleTools.add(stopProfit, investorFee);
        investorFundMainCash.setBalance(invstDiffBalance);
    }

    /**
     * 流水及用户、券商主账户更新
     *
     * @param flowList
     * @param userFundMainCash
     * @param investorFundMainCash
     */
    @Override
    public void saveOrUpdateAccount(List<FundFlow> flowList, FundMainCash userFundMainCash,
                                    FundMainCash investorFundMainCash) {
        if (flowList != null) {
            // 添加现金流水明细
            fundFlowCashDao.addFundFlowCashList(flowList);
        }
        if (userFundMainCash != null) {
            // 更新用户主账户
            fundMainCashDao.updateFundMainCash(userFundMainCash);
        }
        if (investorFundMainCash != null) {
            // 更新券商主账户
            fundMainCashDao.updateFundMainCash(investorFundMainCash);
        }
    }

    /**
     * 递延结息
     *
     * @param userId
     * @param productName
     * @param orderId
     * @param interest
     */
    @Override
    public void deductInterest(String userId, String productName, String orderId, double interest) {
        Date currentTime = new Date();
        // 查询用户现金账户信息
        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        logger.info("查询userId:{}现金主账户信息", userId);
        if (userFundMainCash == null) {
            throw new LTException("用户现金主账户未初始化!");
        }
        FundMainCash cash = new FundMainCash();
        cash.setUserId(userId);
        cash.setBalance(-interest);
        cash.setTotalInterestAmount(interest);

        // 递延结息流水明细
        FundFlow fundFlow = new FundFlow(productName, FundCashOptCodeEnum.DEDUCT_INTEREST, userId, interest,
                DoubleTools.sub(userFundMainCash.getBalance(), interest), orderId, currentTime, currentTime);
        fundMainCashDao.updateFundMainCash(cash);
        fundFlowCashDao.addFundFlowCash(fundFlow);
    }

    @Override
    @Transactional
    public Map<String, String> rechargeForUnspay(UserBussinessInfo info, String amount, String rmbAmt, String responseUrl, String bankCode,
                                                 String thirdOptcode, Double rate) throws LTException {
        logger.info("银生宝======充值");
        String userId = info.getUserId();// 用户id
        logger.info("银生宝充值用户信息,userid={}", userId);
        // 生成充值订单，状态为处理中
        String payId = String.valueOf(CalendarTools.getMillis(new Date()));
        // 获取充值配置信息
        FundOptCode foc = null;
        try {
            foc = getFundOptCode(null, null, thirdOptcode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.FU00000);
        }

        logger.info("foc={}", JSONObject.toJSONString(foc));
        if (foc == null) {
            throw new LTException(LTResponseCode.FUJ0001);
        }
        // 获取已用银生宝充值成功的io流水的充值标识码
        String rechargeIdentification = fundIoCashRechargeDao.selectRechargeIdentification(userId, thirdOptcode);
        if (StringTools.isEmpty(rechargeIdentification)) {
            rechargeIdentification = StringTools.getUUID();
        }

        logger.info("美元金额为：{},人民币金额为：{},人民币转美金为:{}", amount, rmbAmt, DoubleUtils.scaleFormatEnd(DoubleUtils.mul(Double.valueOf(rmbAmt), rate), 2));
        if (DoubleUtils.scaleFormatEnd(DoubleUtils.mul(Double.valueOf(rmbAmt), rate), 2) != Double.valueOf(amount)) {
            throw new LTException(LTResponseCode.FU00003);
        }

        int result = addFundIoForRecharge(payId, userId, Double.valueOf(amount), Double.valueOf(rmbAmt), info.getBankCardNum(), bankCode, foc,
                rechargeIdentification, rate);
        if (result == -1) {
            throw new LTException(LTResponseCode.FU00002);
        } else if (result == -2) {
            logger.info("创建充值流水失败");
            throw new LTException(LTResponseCode.FU00000);
        }

        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

        // 参数封装
        Map<String, String> params = null;
        try {
            params = dealParam(info, payId, Double.valueOf(rmbAmt), responseUrl, sysCfgRedis, rechargeIdentification);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(LTResponseCode.FUY00012);
        }

        logger.info("银生宝组装参数params={}", params);
        // 返回解析
        return params;
    }

    /**
     * 获取业务码
     *
     * @param firstOptCode
     * @param secondOptCode
     * @param thirdOptCode
     * @return
     */
    private FundOptCode getFundOptCode(String firstOptCode, String secondOptCode, String thirdOptCode)
            throws Exception {
        FundOptCode foc = new FundOptCode();
        foc.setFirstOptCode(firstOptCode);
        foc.setSecondOptCode(secondOptCode);
        foc.setThirdOptCode(thirdOptCode);
        List<FundOptCode> focs = fundOptCodeDao.selectFundOptCodes(foc);
        if (focs == null || focs.size() == 0) {
            throw new LTException(LTResponseCode.FUJ0001);
        }
        return focs.get(0);
    }

    @Override
    public int addFundIoForRecharge(String payId, String userId, Double amount, Double rmbAmt, String transferNumber, String bankCode, FundOptCode foc,
                                    String rechargeIdentification, Double rate) {
        // 查询用户现金账户信息
        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        if (userFundMainCash == null) {
            logger.error("用户资金账户不存在,用户userid = {}", userId);
            return -1;
        }

        FundIoCashRecharge ficr = new FundIoCashRecharge(userId, payId, foc.getFirstOptCode(), foc.getSecondOptCode(),
                foc.getThirdOptCode(), amount, null, userFundMainCash.getBalance(), null, transferNumber, bankCode,
                foc.getRemark(), FundIoRechargeEnum.AUDIT.getValue(), 0, rechargeIdentification, rmbAmt, rate);
        logger.info("充值数据ficr=" + JSONObject.toJSONString(ficr));
        int i = fundIoCashRechargeDao.insertFundIoRecharge(ficr);
        if (i != 1) {
            logger.error("创建用户充值记录失败，用户userid={},充值金额为{}", userId, amount);
            return -2;
        }
        return 1;
    }

    /**
     * @param info            用户基本信息，包括银行卡信息
     * @param merchantNo      商户号
     * @param orderId         订单id
     * @param amount          金额（人民币）
     * @param responseUrl     返回前台地址
     * @param backResponseUrl 返回后台地址
     * @return
     */
    Map<String, String> dealParam(UserBussinessInfo info, String payId, Double amount, String responseUrl,
                                  BoundHashOperations<String, String, String> sysCfgRedis, String rechargeIdentification) throws Exception {
        Map<String, String> rmap = new HashMap<String, String>();
        // 获取银生宝商户号
        String unspay_merchantNo = sysCfgRedis.get("unspay_merchantNo");
        // 银生宝充值回掉接口
        String unspay_callback_url = sysCfgRedis.get("unspay_callback_url");
        // 银生宝认证支付接口
        String unspay_url = sysCfgRedis.get("unspay_url");
        // 银生宝私钥
        String unspay_privateKey = sysCfgRedis.get("unspay_privateKey");

        rmap.put("accountId", unspay_merchantNo);// 商户号
        rmap.put("orderId", payId);// 订单号
        rmap.put("amount", amount.toString());// 金额
        rmap.put("backResponseUrl", unspay_callback_url);// 返回后台url
        rmap.put("responseUrl", responseUrl);// 页面url
        rmap.put("unspayUrl", unspay_url);
        // data拼接
        StringBuffer dataStr = new StringBuffer(rechargeIdentification);
        dataStr.append("|").append(info.getUserName());
        dataStr.append("|").append(info.getBankCardNum());
        dataStr.append("|").append(info.getIdCardNum());
        logger.info("dataStr={}", dataStr);
        // 将data用rsa加密

        byte[] encodedData = RSAUtils.encryptByPrivateKey(dataStr.toString().getBytes("UTF-8"), unspay_privateKey);
        logger.info("encodedData={}", encodedData);
        String data = Base64Utils.encode(encodedData);
        logger.info("data={}", data);
        rmap.put("data", data);// data

        StringBuffer signStr = new StringBuffer(unspay_merchantNo);
        signStr.append("&").append(payId);
        signStr.append("&").append(amount);
        signStr.append("&").append(responseUrl);
        signStr.append("&").append(unspay_callback_url);
        signStr.append("&").append(data);
        logger.info("signStr={}", signStr);
        // 数字签名
        String sign = RSAUtils.sign(signStr.toString().getBytes(), unspay_privateKey);

        rmap.put("sign", sign);

        return rmap;

    }

    @Override
    public void callbackForUnspay(Map<String, Object> map) throws Exception {
        logger.info("================收到银生宝通知返回==============");
        String result_code = (String) map.get("result_code");
        String result_msg = (String) map.get("result_msg");
        String orderId = (String) map.get("orderId");
        String actAmount = (String) map.get("amount");// 实际到账金额
        logger.info("银生宝返回结果orderId={}, result_code={}, result_msg={}", orderId, result_code, result_msg);

        // 查询充值订单信息
        FundIoCashRecharge fio = fundIoCashRechargeDao.selectFundIoRechargeOne(orderId);
        if (fio == null) {
            logger.info("银生宝返回的订单不存在，orderid={}", orderId);
            return;
        }
        if (FundIoRechargeEnum.AUDIT.getValue() != fio.getStatus()) {
            logger.info("此订单状态已被改变，orderid={}", orderId);
            return;
        }

        if ("0000".equals(result_code)) {// 操作成功
            logger.info("执行reCharge");
            // 获取充值配置信息
            FundOptCode foc = getFundOptCode(null, null, fio.getThirdOptcode());
            String payId = fio.getPayId();
            String externalId = "";
            Double realAmount = fio.getAmount();
            Double realRmbAmount = Double.valueOf(actAmount);

            fundCashRechargeService.doSuccessRecharge(payId, externalId, realRmbAmount, foc);

        } else {// 失败
            fio.setStatus(FundIoRechargeEnum.FAIL.getValue());// 失败
            fundIoCashRechargeDao.updateFundIoRecharge(fio);
        }
    }

    @Override
    public Map<String,String> callbackForDaddyPay(Map<String, String> map){
        logger.info("daddyPay充值回调开始，入参为{}", map);
        String orderId = map.get("company_order_num");
        String actAmount = map.get("amount");// 实际转账金额

        Map<String,String> rmap = new HashMap<>();
        rmap.put("status", "1");
        rmap.put("error_msg", "成功");

        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        // 私钥
        String private_key = sysCfgRedis.get("daddypay_key");
        String signSrc = MD5Util.md5Low(private_key) + StringTools.formatStr(map.get("pay_time"),"") + StringTools.formatStr(map.get("bank_id"),"") + StringTools.formatStr(map.get("amount"),"") +
                StringTools.formatStr(map.get("company_order_num"),"") + StringTools.formatStr(map.get("mownecum_order_num"),"") + StringTools.formatStr(map.get("pay_card_num"),"") + StringTools.formatStr(map.get("pay_card_name"),"") + StringTools.formatStr(map.get("channel"),"") + StringTools.formatStr(map.get("area"),"")
                + StringTools.formatStr(map.get("fee"),"") + StringTools.formatStr(map.get("transaction_charge"),"") + StringTools.formatStr(map.get("deposit_mode"),"");
        String key = MD5Util.md5Low(signSrc);
        //校验签名
        if(!key.equals(map.get("key"))){
            logger.info("daddyPay充值回调签名校验不匹配，key={},inputkey={}", key, map.get("key"));
            rmap.put("status", "0");
            rmap.put("error_msg", "签名不匹配");
            return rmap;
        }
        // 查询充值订单信息
        FundIoCashRecharge fio = fundIoCashRechargeDao.selectFundIoRechargeOne(orderId);
        try{
            if (fio == null) {
                logger.info("daddyPay返回的订单不存在，orderid={}", orderId);
                return rmap;
            }
            if (FundIoRechargeEnum.AUDIT.getValue() != fio.getStatus()) {
                logger.info("此订单状态已被改变，orderid={}", orderId);
                return rmap;
            }
            logger.info("执行reCharge");
            // 获取充值配置信息
            FundOptCode foc = getFundOptCode(null, null, fio.getThirdOptcode());

            String payId = fio.getPayId();
            String externalId = "";
            Double realAmount = fio.getAmount();
            Double realRmbAmount = Double.valueOf(actAmount);

            fundCashRechargeService.doSuccessRecharge(payId, externalId, realRmbAmount, foc);
        }
        catch (LTException e){
            Response response = LTResponseCode.getCode(e.getMessage());
            if(fio != null){
                fio.setStatus(FundIoRechargeEnum.FAIL.getValue());// 失败
                fio.setRemark("爸爸付充值:回调失败【"+response.getMsg()+"】");
                fundIoCashRechargeDao.updateFundIoRecharge(fio);
            }
            rmap.put("status", "0");
            rmap.put("error_msg", response.getMsg());
        }
        catch (Exception e){
            logger.info("daddyPay充值回调失败:{}", e.getMessage());
            rmap.put("status", "0");
            rmap.put("error_msg", e.getMessage());
        }
        return rmap;
    }


    @Override
    public int doReCharge(Double amt, Double rmbAmount, FundIoCashRecharge fio, FundOptCode foc) throws Exception {
        Integer modifyUserId = 0;
        if (fio != null) {
            modifyUserId = fio.getModifyUserId();
        }
        fio = fundIoCashRechargeDao.selectRechargeIoById(fio.getId().toString());
        if (fio == null || fio.getId() == null) {
            throw new LTException(LTResponseCode.FUY00007);
        }


        if (FundIoRechargeEnum.AUDIT.getValue() != fio.getStatus()) {
            logger.info("此订单状态已被改变，orderid={}", fio.getId());
            throw new LTException(LTResponseCode.FUY00011);
        }
        // 更改充值流水信息
        fio.setStatus(FundIoRechargeEnum.SUCCESS.getValue());// 成功
        fio.setActualAmount(amt);// 实际到账金额
        fio.setActualRmbAmount(rmbAmount); // 实际到账人民币
//		fio.setRmbAmt(rmbAmount);
        fio.setDoneDate(new Date());
        if (fio != null) {
            fio.setModifyUserId(modifyUserId);
        }
        fundIoCashRechargeDao.updateFundIoRecharge(fio);

        FundMainCash fundMainCash = fundMainCashDao.queryFundMainCash(fio.getUserId());

        // 生成资金流水
        FundFlow FundFlow = new FundFlow(fio.getUserId(), FundFlowTypeEnum.INCOME.getValue(), foc.getFirstOptCode(),
                foc.getSecondOptCode(), foc.getThirdOptCode(), amt, fundMainCash.getBalance() + amt,
                fio.getId() + "", fio.getRemark(), new Date(), new Date());
        fundFlowCashDao.addFundFlowCash(FundFlow);

        BankChargeDetail bankChargeDetail = new BankChargeDetail(fio.getUserId(), fio.getThirdOptcode(), fio.getBankCode(), fio.getTransferNumber(), fio.getActualRmbAmount());

        if (fundIoCashRechargeDao.qryBankChargeDetailByCondition(bankChargeDetail) == null) {
            try {
                fundIoCashRechargeDao.insertBankChargeDetail(bankChargeDetail);
            } catch (Exception e) {
                logger.info("用户银行卡充值信息统计异常，信息为bankChargeDetail：{}", bankChargeDetail);
            }
        } else {
            fundIoCashRechargeDao.updateBankChargeAmountByCondition(bankChargeDetail);
        }

        // 更改资金账户信息
        FundMainCash mainCash = new FundMainCash();
        mainCash.setUserId(fio.getUserId());
        mainCash.setBalance(amt);
        mainCash.setTotalRechargeAmount(amt);
        int i = fundMainCashDao.updateFundMainCash(mainCash);
        if (i == 0) {
            logger.error("修改用户资金账户失败,用户userid = {}", fio.getUserId());
            throw new LTException(LTResponseCode.FU00004);
        }
        /**保存用户渠道充值记录**/
        UserChannelTrans userChannelTrans = new UserChannelTrans();
        userChannelTrans.setChannelId(fio.getThirdOptcode());
        userChannelTrans.setUserId(fio.getUserId());
        userChannelTrans.setCreateTime(new Date());
        userChannelTrans.setCreateDate(DateTools.getDefaultDate());
        this.saveUserChannelTrans(userChannelTrans);
        return 1;
    }

    @Override
    public int doRecharge(FundIoCashRecharge fio, FundOptCode foc) throws Exception {

        // 更改充值流水信息
        fio.setStatus(FundIoRechargeEnum.SUCCESS.getValue());// 成功
        fio.setActualAmount(fio.getAmount());
        fio.setActualRmbAmount(fio.getRmbAmt()); // 实际到账人民币
        fio.setDoneDate(new Date());
        fundIoCashRechargeDao.updateFundIoRecharge(fio);

        FundMainCash fundMainCash = fundMainCashDao.queryFundMainCash(fio.getUserId());

        // 生成资金流水
        FundFlow FundFlow = new FundFlow(fio.getUserId(), FundFlowTypeEnum.INCOME.getValue(), foc.getFirstOptCode(),
                foc.getSecondOptCode(), foc.getThirdOptCode(), fio.getAmount(), fundMainCash.getBalance() + fio.getAmount(),
                fio.getId() + "", fio.getRemark(), new Date(), new Date());
        fundFlowCashDao.addFundFlowCash(FundFlow);

        BankChargeDetail bankChargeDetail = new BankChargeDetail(fio.getUserId(), fio.getThirdOptcode(), fio.getBankCode(), fio.getTransferNumber(), fio.getActualRmbAmount());

        if (fundIoCashRechargeDao.qryBankChargeDetailByCondition(bankChargeDetail) == null) {
            try {
                fundIoCashRechargeDao.insertBankChargeDetail(bankChargeDetail);
            } catch (Exception e) {
                logger.info("用户银行卡充值信息统计异常，信息为bankChargeDetail：{}", bankChargeDetail);
            }
        } else {
            fundIoCashRechargeDao.updateBankChargeAmountByCondition(bankChargeDetail);
        }


        // 更改资金账户信息
        FundMainCash mainCash = new FundMainCash();
        mainCash.setUserId(fio.getUserId());
        mainCash.setBalance(fio.getAmount());
        mainCash.setTotalRechargeAmount(fio.getAmount());
        int i = fundMainCashDao.updateFundMainCash(mainCash);
        if (i == 0) {
            logger.error("修改用户资金账户失败,用户userid = {}", fio.getUserId());
            throw new LTException(LTResponseCode.FU00004);
        }
        /**保存用户渠道充值记录**/
        UserChannelTrans userChannelTrans = new UserChannelTrans();
        userChannelTrans.setChannelId(fio.getThirdOptcode());
        userChannelTrans.setUserId(fio.getUserId());
        userChannelTrans.setCreateTime(new Date());
        userChannelTrans.setCreateDate(DateTools.getDefaultDate());
        this.saveUserChannelTrans(userChannelTrans);

        return 1;
    }

    @Override
    @Transactional
    public int doRecharge(String payId, FundOptCode foc) throws Exception {
        FundIoCashRecharge fio = fundIoCashRechargeDao.selectFundIoRechargeForUpdate(payId);
        // 更改充值流水信息
        fio.setStatus(FundIoRechargeEnum.SUCCESS.getValue());// 成功
        fio.setDoneDate(new Date());
        fundIoCashRechargeDao.updateFundIoRecharge(fio);

        FundMainCash fundMainCash = fundMainCashDao.queryFundMainCash(fio.getUserId());

        // 生成资金流水
        FundFlow FundFlow = new FundFlow(fio.getUserId(), FundFlowTypeEnum.INCOME.getValue(), foc.getFirstOptCode(),
                foc.getSecondOptCode(), foc.getThirdOptCode(), fio.getAmount(), fundMainCash.getBalance() + fio.getAmount(),
                fio.getId() + "", fio.getRemark(), new Date(), new Date());
        fundFlowCashDao.addFundFlowCash(FundFlow);

        BankChargeDetail bankChargeDetail = new BankChargeDetail(fio.getUserId(), fio.getThirdOptcode(), fio.getBankCode(), fio.getTransferNumber(), fio.getActualRmbAmount());

        if (fundIoCashRechargeDao.qryBankChargeDetailByCondition(bankChargeDetail) == null) {
            try {
                fundIoCashRechargeDao.insertBankChargeDetail(bankChargeDetail);
            } catch (Exception e) {
                logger.info("用户银行卡充值信息统计异常，信息为bankChargeDetail：{}", bankChargeDetail);
            }
        } else {
            fundIoCashRechargeDao.updateBankChargeAmountByCondition(bankChargeDetail);
        }


        // 更改资金账户信息
        FundMainCash mainCash = new FundMainCash();
        mainCash.setUserId(fio.getUserId());
        mainCash.setBalance(fio.getAmount());
        mainCash.setTotalRechargeAmount(fio.getAmount());
        int i = fundMainCashDao.updateFundMainCash(mainCash);
        if (i == 0) {
            logger.error("修改用户资金账户失败,用户userid = {}", fio.getUserId());
            throw new LTException(LTResponseCode.FU00004);
        }
        /**保存用户渠道充值记录**/
        UserChannelTrans userChannelTrans = new UserChannelTrans();
        userChannelTrans.setChannelId(fio.getThirdOptcode());
        userChannelTrans.setUserId(fio.getUserId());
        userChannelTrans.setCreateTime(new Date());
        userChannelTrans.setCreateDate(DateTools.getDefaultDate());
        this.saveUserChannelTrans(userChannelTrans);

        return 1;
    }

    @Override
    public void queryOrderStatusForUnspay() {
        logger.info("========定时任务查询银生宝充值订单状态====执行===");
        try {

            if (!threadLockService.lock(SysThreadLockEnum.UNSPAY_QUERY_TASK.getCode())) {
                logger.info("查询银生宝充值订单状态执行中，查询操作结束---------------------------");
                return;
            }

            FundIoCashRecharge fio = new FundIoCashRecharge();
            fio.setStatus(FundIoRechargeEnum.AUDIT.getValue());
            fio.setThirdOptcode(FundThirdOptCodeEnum.YSBCZ.getThirdLevelCode());// 银生宝充值三级业务码，目前写死
            List<FundIoCashRecharge> ioList = fundIoCashRechargeDao.selectFundIoRecharge(fio);
            logger.info("===========银生宝充值未处理的单子有，ioList={}", JSONObject.toJSONString(ioList));

            BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
            // 获取银生宝商户号
            String unspay_merchantNo = sysCfgRedis.get("unspay_merchantNo");
            // 银生宝接口请求地址
            String unspay_recharge_query_url = sysCfgRedis.get("unspay_recharge_query_url");
            // 查询密钥
            String unspay_key = sysCfgRedis.get("unspay_key");

            for (FundIoCashRecharge io : ioList) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountId", unspay_merchantNo);
                param.put("orderId", io.getPayId());
                // 生成签名
                StringBuffer macStr = new StringBuffer("accountId=" + unspay_merchantNo);
                macStr.append("&").append("orderId=" + io.getPayId());
                macStr.append("&").append("key=" + unspay_key);
                logger.info("signStr={}", macStr);
                String mac = MD5Util.md5(macStr.toString());
                logger.info("mac={}", mac);
                param.put("mac", mac);

                logger.info("请求银生宝充值查询结果接口，unspay_recharge_query_url={}", unspay_recharge_query_url);
                JSONObject json = HttpTools.doPost(unspay_recharge_query_url, param);
                param.clear();
                if (json == null) {
                    logger.error("===========银生宝充值查询失败，此接口没有响应====坑爹，他们又挂啦========");
                    return;
                }
                String strResult = json.toString();

                // 解析返回结果
                Map<String, String> rmap = JSONObject.parseObject(strResult, Map.class);
                logger.info("resCode:" + rmap.get("result_code") + ", resMessage:" + rmap.get("result_msg"));
                // 结果分析处理
                if ("0000".equals(rmap.get("result_code"))) {// 查询成功
                    logger.info("========查询接口0000只代表查询成功========");
                    if ("00".equals(rmap.get("status"))) {// 充值成功
                        logger.info("=========充值，银生宝悄悄说==成功啦！！！==========");
                        logger.info("执行reCharge");
                        // 获取充值配置信息
                        FundOptCode foc = getFundOptCode(null, null, io.getThirdOptcode());

                        String payId = io.getPayId();
                        String externalId = "";
                        Double realRmbAmount = io.getRmbAmt();

                        fundCashRechargeService.doSuccessRecharge(payId, externalId, realRmbAmount, foc);

                    } else if ("20".equals(rmap.get("status"))) {// 充值失败
                        logger.info("=========充值，银生宝说失败啦！！！==========");
                        io.setStatus(FundIoRechargeEnum.FAIL.getValue());// 失败
                        fundIoCashRechargeDao.updateFundIoRecharge(io);

                    } else {// 充值未处理
                        logger.info("=========充值，银生宝那边正在处理中，别着急！！！==========");
                    }
                } else {
                    logger.info("==========查询失败=============");
                }
            }

            logger.info("========定时任务查询银生宝充值订单状态===完成=========");
        } catch (Exception e) {
            logger.info("========定时任务查询银生宝充值订单状态,执行异常========");
        } finally {
            threadLockService.unlock(SysThreadLockEnum.UNSPAY_QUERY_TASK.getCode());
        }
    }

    @Override
    public void doCommisionWithdraw(CommisionIo io) throws Exception {
        // 获取佣金转现业务码
        FundOptCode foc = getFundOptCode(null, null, FundThirdOptCodeEnum.YJZX.getThirdLevelCode());
        if (foc == null) {
            throw new LTException(LTResponseCode.FUJ0001);
        }
        // 查询用户资金账户
        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(io.getUserId());
        if (userFundMainCash == null) {
            throw new LTException(LTResponseCode.FU00002);
        }
        // 修改用户资金余额
        FundMainCash mainCash = new FundMainCash();
        mainCash.setUserId(io.getUserId());
        mainCash.setBalance(io.getAmount());
        fundMainCashDao.updateFundMainCash(mainCash);
        // 生成佣金转现流水
        FundFlow fundFlow = new FundFlow(io.getUserId(), foc.getFlowType(), foc.getFirstOptCode(),
                foc.getSecondOptCode(), foc.getThirdOptCode(), io.getAmount(),
                DoubleTools.add(userFundMainCash.getBalance(), io.getAmount()), io.getId().toString(), foc.getRemark(), new Date(),
                new Date());
        fundFlowCashDao.addFundFlowCash(fundFlow);
    }

    @Override
    public Map<String, Object> getWithdrawDoneDate(String userId, Long ioId) throws LTException {
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取出金流水
        FundIoCashWithdrawal fundIoCashWithdrawal = fundIoCashWithdrawalDao.queryFundIoCashWithdrawalById(ioId);
        if (fundIoCashWithdrawal == null) {
            throw new LTException(LTResponseCode.FUJ0004);
        }
        // 提现时间
        Date widthdrawDate = fundIoCashWithdrawal.getCreateDate();

        Calendar cla = Calendar.getInstance();
        cla.setTime(widthdrawDate);
        int dayOfWeek = cla.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            widthdrawDate = CalendarTools.addDays(widthdrawDate, 1);
            map.put("mainTitle", "节假日提现");
            map.put("subTitle", "到账时间 " + CalendarTools.getMonth(widthdrawDate) + "月"
                    + CalendarTools.getDayOfMonth(widthdrawDate) + "号（星期一）");
        } else if (dayOfWeek == 7) {
            widthdrawDate = CalendarTools.addDays(widthdrawDate, 2);
            map.put("mainTitle", "节假日提现");
            map.put("subTitle", "到账时间 " + CalendarTools.getMonth(widthdrawDate) + "月"
                    + CalendarTools.getDayOfMonth(widthdrawDate) + "号（星期一）");
        } else {
            String endTime = fundIoCashWithdrawalDao
                    .selectHolidays(DateTools.parseToDefaultDateTimeString(widthdrawDate));
            if (StringTools.isNotEmpty(endTime)) {// 在节假日内提现
                Date endDate = null;
                try {
                    endDate = DateTools.toDefaultDateTime(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDate = CalendarTools.addDays(endDate, 1);
                map.put("mainTitle", "节假日提现");
                map.put("subTitle", "到账时间 " + CalendarTools.getMonth(endDate) + "月"
                        + CalendarTools.getDayOfMonth(endDate) + "号（" + this.getDayOfWeek(dayOfWeek) + "）");
            } else {
                if (CalendarTools.getHour(widthdrawDate) > 17) {
                    widthdrawDate = CalendarTools.addDays(widthdrawDate, 1);
                    map.put("mainTitle", "下班时间提现");
                    map.put("subTitle", "到账时间  明天上班时间到账");
                } else {
                    map.put("mainTitle", "工作日提现");
                    map.put("subTitle", "到账时间 2小时内到账");
                }
            }
        }
        return map;
    }

    private String getDayOfWeek(int weekday) {
        String result = "";
        switch (weekday) {
            case 2:
                result = "星期一";
                break;
            case 3:
                result = "星期二";
                break;
            case 4:
                result = "星期三";
                break;
            case 5:
                result = "星期四";
                break;
            case 6:
                result = "星期五";
                break;
            default:
                result = "工作日";
                break;
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getWithdrawHistory(Map<String, Object> param) throws LTException {
        return fundIoCashWithdrawalDao.selectWithdrawPage(param);
    }

    @Override
    public Map<String, Object> getWithdrawHistoryDetail(String userId, Long ioId) throws LTException {
        Map<String, Object> map = null;
        // 获取流水
        FundIoCashWithdrawal io = fundIoCashWithdrawalDao.queryFundIoCashWithdrawalById(ioId);
        if (io == null) {
            throw new LTException(LTResponseCode.FUJ0004);
        }

        if (io.getStatus() == FundIoWithdrawalEnum.SUCCEED.getValue()
                || io.getStatus() == FundIoWithdrawalEnum.FAILURE.getValue()
                || io.getStatus() == FundIoWithdrawalEnum.PROCESS.getValue()) {// 出金成功或失败或转账中

            map = fundIoCashWithdrawalDao.selectFioAndDetail(ioId);
        } else {
            map = fundIoCashWithdrawalDao.selectFioAndBank(ioId);
        }
        return map;
    }

    @Override
    public Map<String, String> RequestchargeByZfb(String userId, Double amt, String url, Double rate) throws LTException {
        String orderId = String.valueOf(Calendar.getInstance().getTimeInMillis());
        logger.info("============生成系统内部订单 START============");

        // 获取充值配置信息
        insertRechargeIo(userId, orderId, amt, null, rate, null, null, FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode());

        logger.info("开始生成支付宝处理内容");
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayRechargeConfig.ali_pay_url, AlipayRechargeConfig.app_id, AlipayRechargeConfig.private_key
                , AlipayRechargeConfig.format, AlipayRechargeConfig.input_charset, AlipayRechargeConfig.user_public_key);//获得初始化的AlipayClient
        String biz_content = "{" +
                "\"out_trade_no\":\"" + orderId + "\"," +
                "\"total_amount\":" + amt + "," +
                "\"body\":\"Lt用户充值\"," +
                "\"subject\":\"充值\"," +
                "\"enable_pay_channels\":\"debitCardExpress,balance,moneyFund\"," +
                "\"product_code\":\"QUICK_WAP_PAY\"" +
                "}";
        logger.info("biz_content:{}", biz_content);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String current_url = sysCfgRedis.get("current_url");
        String ali_notifyUrl = sysCfgRedis.get("ali_notifyUrl");
        logger.info("异步返回地址为：{}", current_url + ali_notifyUrl);
        logger.info("同返回地址为：{}", current_url + url);
        alipayRequest.setNotifyUrl(current_url + ali_notifyUrl);
        alipayRequest.setReturnUrl(current_url + "/" + url);
        alipayRequest.setBizContent(biz_content);//填充业务参数

        logger.info("拼接支付宝接口url");
        url = AlipayRechargeSubmit.postToZfb(alipayRequest, alipayClient);

        if (url.equals("")) {
            throw new LTException(LTResponseCode.FUY00004);
        } else {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("url", url);
            paramMap.put("biz_content", biz_content);
            return paramMap;
        }
    }

    @Override
    public void aplipayCallBack(Map<String, Object> paraMap) throws LTException {
        // 通知类型:bptb_result_notify
        String notify_type = paraMap.get("notify_type").toString();
        // 应用id
        String app_id = paraMap.get("app_id").toString();
        // 充值金额
        String total_amount = paraMap.get("total_amount").toString();
        // 原订单号
        String out_trade_no = paraMap.get("out_trade_no").toString();
        // 通知时间
        String notify_time = paraMap.get("notify_time").toString();
        // 签名方式:sign_type
        String sign_type = paraMap.get("sign_type").toString();

        String buyer_pay_amount = paraMap.get("buyer_pay_amount").toString();

        logger.info(
                "通知类型.notify_type={},通知时间.notify_time={},签名方式 sign_type={},app_id={},totalamount={},out_trade_no={},buyer_pay_amount_arr={}",
                notify_type, notify_time, sign_type, app_id, total_amount, out_trade_no, buyer_pay_amount);

        if (AlipayRechargeConfig.app_id.trim().equals(app_id.trim())) {
            FundIoCashRecharge fio = fundIoCashRechargeDao.selectFundIoRechargeOne(out_trade_no);
            if (fio != null) {
                try {
                    if (FundIoRechargeEnum.AUDIT.getValue() == fio.getStatus().intValue()) {
                        // 校验充值填入金额是否等于充值金额，买方支付金额是否等于充值金额
                        if (Double.valueOf(total_amount) == fio.getRmbAmt().doubleValue()
                                && fio.getRmbAmt().doubleValue() == Double.valueOf(buyer_pay_amount)) {
                            if (notify_type.trim().equals("trade_status_sync")) { // 校验通知方式是否为异步通知
                                FundOptCode foc = getFundOptCode(null, null, fio.getThirdOptcode());
                                doRecharge(fio, foc);
                            }
                        } else {
                            logger.info("充值订单：{}中的充值金额和支付宝返回结果不匹配，充值失败，提交值为：{}，实际到账值为：{}", out_trade_no,
                                    fio.getAmount().doubleValue(), Double.valueOf(total_amount));
                            fio.setStatus(FundIoRechargeEnum.FAIL.getValue());// 失败
                            fundIoCashRechargeDao.updateFundIoRecharge(fio);
                        }
                    } else {
                        logger.info("充值订单：{}订单已完成", out_trade_no);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                logger.info("未成功查询到历史充值记录");
            }
        } else {
            logger.info("未成功匹配到app_id");
        }
    }

    @Override
    public List<FundPayAuthFlow> getFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException {
        List<FundPayAuthFlow> flowList = fundPayAuthFlowDao.getFinancyPayAuthFlow(financyPayAuthFlow);
        return flowList;
    }

    @Override
    public void insertRechargeIo(String userId, String orderId, Double amt, Double rmbAmt, Double rate, String transferNum, String bankCode, String thirdLevelCode) throws LTException {
        // 获取充值配置信息
        FundOptCode foc = null;
        try {
            foc = getFundOptCode(null, null, thirdLevelCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("foc={}", JSONObject.toJSONString(foc));
        if (foc == null) {
            throw new LTException(LTResponseCode.FUJ0001);
        }

        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        if (userFundMainCash == null) {
            logger.error("用户资金账户不存在,用户userid = {}", userId);
            throw new LTException(LTResponseCode.US01105);
        }

        FundIoCashRecharge ficr = new FundIoCashRecharge(userId, orderId, foc.getFirstOptCode(), foc.getSecondOptCode(),
                foc.getThirdOptCode(), amt, null, userFundMainCash.getBalance(), null, transferNum, bankCode, foc.getRemark(),
                0, FundIoRechargeEnum.AUDIT.getValue(), null, rmbAmt, rate);
        logger.info("充值数据ficr=" + JSONObject.toJSONString(ficr));
        fundIoCashRechargeDao.insertFundIoRecharge(ficr);

    }

    @Override
    public void insertAlipayRechargeIo(String userId, String orderId, Double amt, Double rmbAmt, Double rate, String transferNum, String alipayNum, String bankCode, String thirdLevelCode) throws LTException {
        // 获取充值配置信息
        FundOptCode foc = null;
        try {
            foc = getFundOptCode(null, null, thirdLevelCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("foc={}", JSONObject.toJSONString(foc));
        if (foc == null) {
            throw new LTException(LTResponseCode.FUJ0001);
        }

        FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
        if (userFundMainCash == null) {
            logger.error("用户资金账户不存在,用户userid = {}", userId);
            throw new LTException(LTResponseCode.US01105);
        }

        if (DoubleUtils.scaleFormatEnd(DoubleUtils.mul(rmbAmt, rate), 2) != amt) {
            throw new LTException(LTResponseCode.FU00003);
        }
        FundIoCashRecharge ficr = new FundIoCashRecharge(userId, orderId, foc.getFirstOptCode(), foc.getSecondOptCode(),
                foc.getThirdOptCode(), amt, null, userFundMainCash.getBalance(), null, transferNum, bankCode, foc.getRemark(),
                0, FundIoRechargeEnum.AUDIT.getValue(), null, rmbAmt, rate);
        ficr.setAlipayNum(alipayNum);
        logger.info("充值数据ficr=" + JSONObject.toJSONString(ficr));
        fundIoCashRechargeDao.insertFundIoRecharge(ficr);

    }

    @Override
    public Integer updateFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException {
        return fundPayAuthFlowDao.updateFinancyPayAuthFlow(financyPayAuthFlow);
    }

    @Override
    public void addFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException {
        fundPayAuthFlowDao.addFinancyPayAuthFlow(financyPayAuthFlow);
    }

    @Override
    public FundIoCashRecharge qryFundIoCashRechargeByPayId(String payId) throws LTException {
        return fundIoCashRechargeDao.selectFundIoRechargeOne(payId);
    }

    @Override
    public void setFundRechargeIoFail(FundIoCashRecharge cashRecharge) throws LTException {
        fundIoCashRechargeDao.updateFundIoRechargeByPayId(cashRecharge);
    }

    @Override
    public void dinpayAccept(Map<String, Object> map) throws LTException {
        logger.info("================【智付支付结果】接收处理start==============");
        String resultCode = (String) map.get("tradeStatus");
        String orderId = (String) map.get("orderNo");
        String tradeNo = (String) map.get("tradeNo");
        String actAmount = (String) map.get("orderAmount");// 实际到账金额
        logger.info("智付支付結果:orderId={}, resultCode={},", orderId, resultCode);
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String dinpayCheckSign = sysCfgRedis.get("dinpay_check_sign");
        String sign = (String) map.get("sign");
        String signInfo = (String) map.get("signInfo");
        String signType = (String) map.get("signType");
        if ("1".equals(dinpayCheckSign)) {
            String dinpayPrivateKey = sysCfgRedis.get("dinpay_private_key");
            boolean signResult = false;
            if ("RSA-S".equals(signType)) {
                try {
                    signResult = RSAWithSoftware.validateSignByPublicKey(signInfo, dinpayPrivateKey, sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if ("RSA".equals(signType)) {
                String dinpayPfxKey = sysCfgRedis.get("dinpay_pfx_key");
                String dinpayPfxPath = sysCfgRedis.get("dinpay_pfx_path");
                String dinpayMerchantCode = sysCfgRedis.get("dinpay_merchant_code");
                RSAWithHardware mh = new RSAWithHardware();
                try {
                    mh.initSigner(dinpayPfxPath, dinpayPfxKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                signResult = mh.validateSignByPubKey(dinpayMerchantCode, signInfo, sign);
            } else {
                throw new LTException("==============【智付支付结果】查询签名类型失败!==========");
            }
            if (!signResult) {
                logger.error("===========【智付支付结果】验证签名失败！============");
            }
        }

        // 查询充值订单信息
        FundIoCashRecharge fio = fundIoCashRechargeDao.selectFundIoRechargeOne(orderId);
        if (fio == null) {
            logger.info("智付返回的订单不存在，订单号:orderId={}", orderId);
            return;
        }
        if (FundIoRechargeEnum.SUCCESS.getValue() == fio.getStatus()) {
            logger.info("此订单状态已被处理，当前系统，orderId={}", orderId + ";+智付返回结果:resultCode" + resultCode);
            return;
        }

        Double realAmount = Double.valueOf(actAmount);
        if ("SUCCESS".equals(resultCode)) {// 操作成功
            logger.info("执行reCharge");
            // 获取充值配置信息
            FundOptCode foc = null;
            try {
                foc = getFundOptCode(null, null, fio.getThirdOptcode());
            } catch (Exception e) {
                e.printStackTrace();
                throw new LTException("获取业务码异常");
            }

            fundCashRechargeService.doSuccessRecharge(orderId, tradeNo, realAmount, foc);

        } else {// 失败不处理
            logger.info("【智付支付结果】" + resultCode);
            fio.setStatus(FundIoRechargeEnum.FAIL.getValue());// 失败
            fundIoCashRechargeDao.updateFundIoRecharge(fio);
        }
        logger.info("================【智付支付结果】接收处理end==============");

    }

    @Override
    public void queryDinpayResult() {
        logger.info("========定时任务查询智付支付结果start===");
        try {
            /**检查当前是否已经存在查询任务**/
            if (!threadLockService.lock(SysThreadLockEnum.DINPAY_QUERY_TASK.getCode())) {
                return;
            }
            /**查询智付配置参数**/
            BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
            String dinpayServiceType = "single_trade_query";
            String dinpayInterfaceVersion = sysCfgRedis.get("dinpay_interface_version");
            String dinpaySignType = sysCfgRedis.get("dinpay_sign_type");
            String dinpayQueryUrl = sysCfgRedis.get("dinpay_query_url");
            String dinpayPrivateKey = sysCfgRedis.get("dinpay_private_key");
            String dinpayPfxKey = sysCfgRedis.get("dinpay_pfx_key");
            String dinpayPfxPath = sysCfgRedis.get("dinpay_pfx_path");
            String dinpayMerchantCode = sysCfgRedis.get("dinpay_merchant_code");
            String querySecond = sysCfgRedis.get("dinpay_query_second");
            String queryNum = sysCfgRedis.get("dinpay_query_num");
            String tels = sysCfgRedis.get("pay_warn_tel");
            String warnSecond = sysCfgRedis.get("pay_warn_second");


            //充值时间
            long curStamp = System.currentTimeMillis() - Long.valueOf(querySecond) * 1000;
            Map<String, Object> queryMap = FastMap.newInstance();
            queryMap.put("createDate", new Date(curStamp));
            queryMap.put("thirdOptcode", FundThirdOptCodeEnum.DINPAY_MOB.getThirdLevelCode());
            queryMap.put("status", FundIoRechargeEnum.AUDIT.getValue());
            queryMap.put("queryNum", Integer.valueOf(queryNum));
            List<FundIoCashRecharge> ioList = fundIoCashRechargeDao.selectFundIoRechargeList(queryMap);
            logger.info("===========智付支付未处理的单子有，ioList={}", JSONObject.toJSONString(ioList));
            for (FundIoCashRecharge io : ioList) {
                String payId = io.getPayId();
                StringBuffer signSrc = new StringBuffer();
                signSrc.append("interface_version=").append(dinpayInterfaceVersion).append("&");
                signSrc.append("merchant_code=").append(dinpayMerchantCode).append("&");
                signSrc.append("order_no=").append(payId).append("&");
                signSrc.append("service_type=").append(dinpayServiceType);

                String signInfo = signSrc.toString();
                String sign = "";
                if ("RSA-S".equals(dinpaySignType)) {
                    sign = RSAWithSoftware.signByPrivateKey(signInfo, dinpayPrivateKey);
                    logger.info("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
                    logger.info("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
                }

                if ("RSA".equals(dinpaySignType)) {//数字证书加密方式 sign_type = "RSA"
                    //String rootPath = this.getClass().getResource("/").toString();
                    //请在商家后台证书下载处申请和下载pfx数字证书，一般要1~3个工作日才能获取到,1111110166.pfx是测试商户号1111110166的数字证书
                    //String path= rootPath.substring(rootPath.indexOf("/")+1,rootPath.length()-8)+"certification/1111110166.pfx";
                    String pfxPass = dinpayPfxKey; //证书密钥，初始密码是商户号
                    RSAWithHardware mh = new RSAWithHardware();
                    mh.initSigner(dinpayPfxPath, pfxPass);

                    sign = mh.signByPriKey(signInfo);
                    logger.info("RSA商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
                    logger.info("RSA商家发送的签名：" + sign.length() + " -->" + sign + "\n");
                }
                if (StringTools.isEmpty(dinpaySignType)) {
                    logger.error("[智付支付结果查询异常!未查询到证书配置信息");
                    throw new LTException();
                }
                if (StringTools.isEmpty(sign)) {
                    logger.error("[智付支付结果查询异常!参数加签失败");
                    throw new LTException();
                }

                Map<String, String> param = FastMap.newInstance();
                param.put("sign", sign);
                param.put("merchant_code", dinpayMerchantCode);
                param.put("service_type", dinpayServiceType);
                param.put("interface_version", dinpayInterfaceVersion);
                param.put("sign_type", dinpaySignType);
                param.put("order_no", payId);
                //param.put("trade_no", externalId);

                logger.info("请求智付支付结果查询接口,接口地址：dinpayQueryUrl={}", dinpayQueryUrl);
                String result = DinpayHttpTools.doPost(dinpayQueryUrl, param);
                if (StringTools.isEmpty(result)) {
                    logger.info("[result:null]");
                    continue;
                }
                Document document = StringTools.stringConvertXML(result, "");
                param.clear();
                if (document == null) {
                    logger.error("==========智付支付结果查询接口失败，此接口没有响应====坑爹，他们又挂啦========");
                    return;
                }
                String is_success = document.getElementsByTagName("is_success").item(0).getTextContent();
                // 结果分析处理
                if ("T".equals(is_success)) {// 查询成功
                    String trade_status = document.getElementsByTagName("trade_status").item(0).getTextContent();
                    if ("SUCCESS".equals(trade_status)) {
                        logger.info("===========智付支付结果：订单号" + payId + "支付成功");
                        FundOptCode foc = getFundOptCode(null, null, FundThirdOptCodeEnum.DINPAY_MOB.getThirdLevelCode());
                        Double actualRmbAmount = Double.valueOf(document.getElementsByTagName("order_amount").item(0).getTextContent());
                        String trade_no = document.getElementsByTagName("trade_no").item(0).getTextContent();
                        logger.info("【智付到账RMB金额】:" + actualRmbAmount);

                        fundCashRechargeService.doSuccessRecharge(payId, trade_no, actualRmbAmount, foc);

                    } else if ("UNPAY".equals(trade_status)) {
                        logger.info("===========智付支付结果：订单号" + payId + "未支付");
                    } else {
                        logger.info("=========智付支付查询结果未名！！！==========");
                    }
                    // 获取充值配置信息
                } else if ("F".equals(is_success)) {
                    String error_code = document.getElementsByTagName("error_code").item(0).getTextContent();
                    logger.info("======智付支付查询结果查询失败错误码：" + error_code);
                    String failReason = "";
                    if ("CHECK_URL_NOT_MATCH".equals(error_code)) {
                        failReason = "商家域名校验不通过";
                    } else if ("TRANSACTION_REJECTED_001".equals(error_code)) {
                        failReason = "交易请求被拒绝";
                    } else if ("csp.orderService.order-is-already-exist".equals(error_code)) {
                        failReason = "提交的订单已经存在了";
                    } else if ("csp.orderService.prepay-create-order-failure".equals(error_code)) {
                        failReason = "生成订单失败";
                    } else if ("SYSTEM_ERROR".equals(error_code)) {
                        failReason = "系统异常错误";
                    } else if ("csp.orderService.order-not-exist".equals(error_code)) {
                        failReason = "订单不存在";
                    } else {
                        failReason = "订单异常";
                    }

                    //.setFailReason(error_code);
                    //if("csp.orderService.order-not-exist".equals(error_code)){
                    //	io.setStatus(FundIoRechargeEnum.FAIL.getValue());
                    //}
                    if (io.getModifyDate() == null) {
                        fundIoCashRechargeDao.updateFundIoRecharge(io);
                    } else {

                        logger.info("【当前时间】" + DateTools.parseToDefaultDateSecondString(new Date()) + "【订单生成时间】" + DateTools.parseToDefaultDateSecondString(io.getCreateDate()));

                        if ((System.currentTimeMillis() - io.getModifyDate().getTime()) > Integer.valueOf(warnSecond) * 1000) {
                            if (StringTools.isEmpty(io.getFailReason())) {
                                Map<String, Object> context = FastMap.newInstance();
                                context.put("payId", io.getPayId());
                                context.put("warnMsg", DateTools.parseToDefaultDateSecondString(io.getCreateDate()) + "智付" + "入金流水ID" + payId + failReason + ",请及时处理！【充值预警】");
                                context.put("tels", tels);
                                this.payWarning(context);
                                logger.info("【智付充值结果查询】20s没有结果，更新失败.");
                                io.setFailReason(failReason);
                                io.setStatus(FundIoRechargeEnum.FAIL.getValue());
                                fundIoCashRechargeDao.updateFundIoRecharge(io);
                            }
                        }
                    }
                } else {
                    logger.info("=========智付支付查询结果未明！！！==========");
                }
            }

            logger.info("========定时任务查询智付充值订单状态end=========");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("========定时任务查询智付充值订单状态,执行异常========");
        } finally {
            //解锁
            threadLockService.unlock(SysThreadLockEnum.DINPAY_QUERY_TASK.getCode());
        }
    }

    /**
     * 智付预警
     *
     * @param context
     * @author yubei
     */
    public void payWarning(Map<String, Object> context) {
        try {
            String warnMsg = (String) context.get("warnMsg");
            String tels = (String) context.get("tels");
            String[] telArr = tels.split("-");
            for (String tel : telArr) {
                SystemMessage ann = new SystemMessage();
                ann.setDestination(tel);
                ann.setUserId("-999"); // 注册时不存在用户信息，赋默认值
                ann.setCause(UserContant.APP_CHANGE_FUND_MARK);
                ann.setType(UserContant.SMS_SHORT_TYPE);
                ann.setSmsType(Integer.parseInt(UserContant.APP_CHANGE_FUND_INFO));
                ann.setPriority(0);
                ann.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS); // 默认为发送成功
                ann.setUserType(0);
                ann.setIp(null);
                ann.setCreateDate(new Date());
                ann.setContent(warnMsg);
                logger.info("【发送预警消息】" + warnMsg);
                logger.info("tel:" + tel);
                boolean result = smsApiService.sendUserFundMsg(ann);
                logger.info("****result***" + result);
                if (!result) {
                    throw new LTException("发送预警失败!");
                }
            }

        } catch (Exception e) {
            logger.error("【智付预警异常】");
            throw new LTException(e.getMessage());
        }
    }


    @Override
    @Transactional
    public Map<String, Object> dinpayCreate(Map<String, Object> reqMap) throws LTException {
        try {
            logger.info("【智付入金登记】");
            Map<String, Object> resultMap = dinpayPackParam(reqMap);
            String userId = (String) reqMap.get("userId");
            String bankId = (String) reqMap.get("bankId");
            Double usdAmt = DoubleTools.parseDoulbe(reqMap.get("usdAmt"));
            Double cnyAmt = DoubleTools.parseDoulbe(reqMap.get("cnyAmt"));
            Double rate = DoubleTools.parseDoulbe(reqMap.get("rate"));
            String orderId = (String) resultMap.get("order_no");
            String clientType = (String) reqMap.get("clientType");
            String fundThirdOptCode = "";
            String bankCode = null;
            String bankCardNum = null;
            if ("1".equals(clientType) || "2".equals(clientType)) {
                fundThirdOptCode = FundThirdOptCodeEnum.DINPAY_MOB.getThirdLevelCode();
                UserBankInfo userBankInfo = userApiBussinessService.getUserBankInfo(userId, bankId);
                bankCode = userBankInfo.getBankCode();
                bankCardNum = userBankInfo.getBankCardNum();
            } else if ("3".equals(clientType)) {
                fundThirdOptCode = FundThirdOptCodeEnum.DINPAY_WEB.getThirdLevelCode();
            } else {
                throw new LTException("获取客户端异常！");
            }


            insertRechargeIo(userId, orderId, usdAmt, cnyAmt, rate, bankCardNum, bankCode, fundThirdOptCode);
            return resultMap;
        } catch (Exception e) {
            logger.error("【查询查询智付配置信息异常！" + e.getMessage() + "】");
            throw new LTException("【智付配置信息异常】");
        }


    }


    /**
     * 包装智付入金参数
     *
     * @param reqMap
     * @return
     * @throws Exception
     * @author yubei
     */
    public Map<String, Object> dinpayPackParam(Map<String, Object> reqMap) throws Exception {
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String dinpayServiceType = sysCfgRedis.get("dinpay_service_type");
        String dinpayInterfaceVersion = sysCfgRedis.get("dinpay_interface_version");
        String dinpaySignType = sysCfgRedis.get("dinpay_sign_type");
        //String dinpayQueryUrl = sysCfgRedis.get("dinpay_query_url");
        String dinpayPrivateKey = sysCfgRedis.get("dinpay_private_key");
        String dinpayPfxKey = sysCfgRedis.get("dinpay_pfx_key");
        String dinpayPfxPath = sysCfgRedis.get("dinpayPfxPath");
        String dinpayMerchantCode = sysCfgRedis.get("dinpay_merchant_code");
        String dinpay_callback_url = sysCfgRedis.get("dinpay_callback_url");
        String dinpay_res_url = sysCfgRedis.get("dinpay_res_url");
        String dinpay_req_url = sysCfgRedis.get("dinpay_req_url");

        String orderNo = String.valueOf(CalendarTools.getMillis(new Date()));
        logger.info("========【智付入金】生成平台订单号:" + orderNo);
        StringBuffer signSrc = new StringBuffer();
        String orderTime = DateTools.parseToDefaultDateTimeString(new Date());
        String orderAmount = (String) reqMap.get("cnyAmt");
        String product_name = "智付入金";
        String return_url = dinpay_res_url;
        String redo_flag = "";
        String product_code = "";
        String product_num = "";
        String product_desc = "";
        String pay_type = "";
        String show_url = "";
        String customer_name = "";
        String customer_idNumber = "";
        String clientType = (String) reqMap.get("clientType");

        //移动端
        if ("1".equals(clientType) || "2".equals(clientType)) {
            signSrc.append("interface_version=").append(dinpayInterfaceVersion);
            signSrc.append("&merchant_code=").append(dinpayMerchantCode);
            signSrc.append("&notify_url=").append(dinpay_callback_url);
            signSrc.append("&order_amount=").append(orderAmount);
            signSrc.append("&order_no=").append(orderNo);
            signSrc.append("&order_time=").append(orderTime);
            signSrc.append("&product_name=").append(product_name);
        } else if ("3".equals(clientType)) {
            //web端
            signSrc.append("input_charset=").append("UTF-8");
            signSrc.append("&interface_version=").append(dinpayInterfaceVersion);
            signSrc.append("&merchant_code=").append(dinpayMerchantCode);
            signSrc.append("&notify_url=").append(dinpay_callback_url);
            signSrc.append("&order_amount=").append(orderAmount);
            signSrc.append("&order_no=").append(orderNo);
            signSrc.append("&order_time=").append(orderTime);
            if (StringTools.isNotEmpty(pay_type)) {
                signSrc.append("&pay_type=").append(pay_type);
            }
            if (StringTools.isNotEmpty(product_code)) {
                signSrc.append("&product_code=").append(product_code);
            }
            if (StringTools.isNotEmpty(product_desc)) {
                signSrc.append("&product_desc=").append(product_desc);
            }
            signSrc.append("&product_name=").append(product_name);
            if (StringTools.isNotEmpty(product_num)) {
                signSrc.append("&product_num=").append(product_num);
            }
            if (StringTools.isNotEmpty(redo_flag)) {
                signSrc.append("&redo_flag=").append(redo_flag);
            }
            if (StringTools.isNotEmpty(return_url)) {
                signSrc.append("&return_url=").append(return_url);
            }
            signSrc.append("&service_type=").append("direct_pay");
            if (StringTools.isNotEmpty(show_url)) {
                signSrc.append("&show_url=").append(show_url);
            }
        }


        String signInfo = signSrc.toString();
        String sign = "";

        if ("RSA-S".equals(dinpaySignType)) {
            sign = RSAWithSoftware.signByPrivateKey(signInfo, dinpayPrivateKey);
            logger.info("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
            logger.info("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
        }
        if ("RSA".equals(dinpaySignType)) {
            String pfxPass = dinpayPfxKey;
            RSAWithHardware mh = new RSAWithHardware();
            mh.initSigner(dinpayPfxPath, pfxPass);
            sign = mh.signByPriKey(signInfo);
            logger.info("RSA商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
            logger.info("RSA商家发送的签名：" + sign.length() + " -->" + sign + "\n");
        }
        if (StringTools.isEmpty(dinpaySignType)) {
            logger.error("【智付支付登记异常!未查询到签名类型配置信息】");
            throw new LTException();
        }
        if (StringTools.isEmpty(sign)) {
            logger.error("【智付支付登记异常!参数加签失败】");
            throw new LTException();
        }

        Map<String, Object> resultMap = FastMap.newInstance();
        resultMap.put("merchant_code", dinpayMerchantCode);
        if ("3".equals(clientType)) {
            resultMap.put("input_charset", "UTF-8");
            resultMap.put("pay_type", pay_type);
            resultMap.put("return_url", dinpay_res_url);
            resultMap.put("service_type", dinpayServiceType);
            resultMap.put("bank_code", "");
            resultMap.put("show_url", show_url);
            resultMap.put("dinpay_req_url", dinpay_req_url);
            resultMap.put("client_ip", "");
            resultMap.put("extend_param", "");
        }
        resultMap.put("interface_version", dinpayInterfaceVersion);
        resultMap.put("notify_url", dinpay_callback_url);
        resultMap.put("sign_type", dinpaySignType);
        resultMap.put("order_no", orderNo);
        resultMap.put("order_time", orderTime);
        resultMap.put("order_amount", orderAmount);
        resultMap.put("product_name", product_name);
        resultMap.put("signInfo", signInfo);
        if ("1".equals(clientType)) {
            resultMap.put("sign", sign.replaceAll("\\+", "%2B"));
        } else {
            resultMap.put("sign", sign);
        }
        resultMap.put("redo_flag", "");
        resultMap.put("product_code", "");
        resultMap.put("product_num", "");
        resultMap.put("product_desc", "");
        resultMap.put("extra_return_param", "");

        resultMap.put("customer_name", customer_name);
        resultMap.put("customer_idNumber", customer_idNumber);
        return resultMap;
    }

    @Override
    public int doDinpayRecharge(FundIoCashRecharge fio, FundOptCode foc) throws LTException {
        // 更改充值流水信息
        fio.setStatus(FundIoRechargeEnum.SUCCESS.getValue());// 成功
        fio.setActualAmount(fio.getAmount());
        fio.setActualRmbAmount(fio.getRmbAmt()); // 实际到账人民币
        fio.setDoneDate(new Date());
        fundIoCashRechargeDao.updateFundIoRecharge(fio);

        FundMainCash fundMainCash = fundMainCashDao.queryFundMainCashForUpdate(fio.getUserId());

        // 生成资金流水
        FundFlow FundFlow = new FundFlow(fio.getUserId(), FundFlowTypeEnum.INCOME.getValue(), foc.getFirstOptCode(),
                foc.getSecondOptCode(), foc.getThirdOptCode(), fio.getAmount(), fundMainCash.getBalance() + fio.getAmount(),
                fio.getId() + "", fio.getRemark(), new Date(), new Date());
        fundFlowCashDao.addFundFlowCash(FundFlow);

        BankChargeDetail bankChargeDetail = new BankChargeDetail(fio.getUserId(), fio.getThirdOptcode(), fio.getBankCode(), fio.getTransferNumber(), fio.getActualRmbAmount());

        if (fundIoCashRechargeDao.qryBankChargeDetailByCondition(bankChargeDetail) == null) {
            try {
                fundIoCashRechargeDao.insertBankChargeDetail(bankChargeDetail);
            } catch (Exception e) {
                logger.info("用户银行卡充值信息统计异常，信息为bankChargeDetail：{}", bankChargeDetail);
                throw new LTException("【智付充值修改银行卡充值异常】");
            }
        } else {
            fundIoCashRechargeDao.updateBankChargeAmountByCondition(bankChargeDetail);
        }

        // 更改资金账户信息
        FundMainCash mainCash = new FundMainCash();
        mainCash.setUserId(fio.getUserId());
        mainCash.setBalance(fio.getAmount());
        mainCash.setTotalRechargeAmount(fio.getAmount());
        int i = fundMainCashDao.updateFundMainCash(mainCash);
        if (i == 0) {
            logger.error("修改用户资金账户失败,用户userid = {}", fio.getUserId());
            throw new LTException(LTResponseCode.FU00004);
        }
        UserChannelTrans userChannelTrans = new UserChannelTrans();
        userChannelTrans.setChannelId(fio.getThirdOptcode());
        userChannelTrans.setUserId(fio.getUserId());
        userChannelTrans.setCreateTime(new Date());
        userChannelTrans.setCreateDate(DateTools.getDefaultDate());
        this.saveUserChannelTrans(userChannelTrans);
        return 1;
    }

    @Override
    public void saveUserChannelTrans(UserChannelTrans userChannelTrans) throws LTException {
        try {
            this.fundIoCashRechargeDao.insertUserChannelTrans(userChannelTrans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("【保存用户渠道充值记录异常】");
            //throw new LTException(e.getMessage());
        }
    }


    @Override
    public void swiftPassCallback(Map<String,String> map)throws  LTException{
        swiftPassService.swiftPassResult(map);
    }

}
