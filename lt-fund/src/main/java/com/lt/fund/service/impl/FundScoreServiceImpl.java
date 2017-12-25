/*
 * PROJECT NAME: lt-fund
 * PACKAGE NAME: com.lt.fund.service
 * FILE    NAME: FundScoreServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.fund.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundScoreOptCodeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.fund.dao.FundFlowScoreDao;
import com.lt.fund.dao.FundMainScoreDao;
import com.lt.fund.service.IFundScoreService;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundMainScore;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;

/**
 * TODO 积分账户业务实现
 * @author XieZhibing
 * @date 2016年11月30日 下午4:43:10
 * @version <b>1.0.0</b>
 */
@Service
public class FundScoreServiceImpl implements IFundScoreService {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -2948732512872375582L;

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(FundScoreServiceImpl.class);
	
	@Autowired
	private FundMainScoreDao fundMainScoreDao;
	@Autowired
	private FundFlowScoreDao fundFlowScoreDao;
	
	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月9日 下午3:20:22
	 * @see com.lt.fund.service.IFundScoreService#isScoreBalanceEnough(java.lang.Integer, double)
	 * @param userId
	 * @param amount
	 * @return
	 */
	@Override
	public boolean isScoreBalanceEnough(String userId, double amount) {
		// TODO Auto-generated method stub
		return fundMainScoreDao.queryFundMainScoreBalance(userId, amount) > 0;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:54:28
	 * @see com.lt.api.fund.IFundScoreService#queryFundMainScore(java.lang.Integer)
	 * @param userId
	 * @return
	 */
	@Override
	public FundMainScore queryFundMainScore(String userId) {
		// TODO Auto-generated method stub
		return fundMainScoreDao.queryFundMainScore(userId);
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月6日 下午1:45:31
	 * @see com.lt.api.fund.IFundScoreService#checkFundMainScoreBalance(java.lang.Integer, double)
	 * @param userId
	 * @param amount
	 * @return
	 */
	@Override
	public boolean doCheckFundMainBalance(String userId, double amount) {
		// TODO Auto-generated method stub
		FundMainScore fundMainScore = fundMainScoreDao.queryFundMainScore(userId);
		logger.debug("查询userId:{}积分主账户信息", userId);
		if(fundMainScore == null || fundMainScore.getBalance() < 1) {
			return false;
		}
		
		if(fundMainScore.getBalance() > amount) {
			logger.info("userId:{}积分主账户可用余额充足", userId);
			return true;
		}
		
		logger.info("userId:{}积分主账户可用余额不足", userId);
		return false;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:15:48
	 * @see com.lt.api.fund.IFundScoreService#initFundScoreAccount(java.lang.Integer)
	 * @param userId
	 * @return
	 */
	@Override
	public boolean doInitFundScoreAccount(String userId) {
		// TODO Auto-generated method stub
		
		logger.info("先删除用户积分主账户, 在重新添加");
		
		fundMainScoreDao.deleteFundMainScore(userId);		
		fundMainScoreDao.initFundMainScore(userId);
		
		FundFlow scoreFlow = new FundFlow(userId, FundFlowTypeEnum.INCOME.getValue(), FundScoreOptCodeEnum.PRESENT.getFirstLevelCode(),
				FundScoreOptCodeEnum.PRESENT.getCode(), FundThirdOptCodeEnum.ZSTYJ.getThirdLevelCode(), 10000, 10000, 
				null, "赠送体验金", new Date(), new Date());
		
		fundFlowScoreDao.addFundFlowScore(scoreFlow);
		return true;
	}

	/**
	 * 开仓扣款
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:33:52
	 * @see com.lt.api.fund.IFundScoreService#doBuy(java.lang.String, java.lang.String, java.lang.Integer, double, double, double)
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param actualCounterFee	手续费
	 * @return
	 * @throws LTException
	 */
	@Override
	public boolean doBuy(String productName, String orderId, String userId,
			double holdFund, double deferFund, double actualCounterFee) throws LTException {
		// TODO Auto-generated method stub
		
		long startTime1 = System.currentTimeMillis();
		
		//查询用户积分账户信息
		FundMainScore userFundMainScore = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		logger.info("查询userId:{}积分主账户信息", userId);
		if(userFundMainScore == null) {
			logger.error("用户:{}积分主账户未初始化", userId);
			throw new LTException(LTResponseCode.FU00011);
		}
		
		//总费用
		double userTotalAmount = DoubleTools.add(DoubleTools.add(holdFund , deferFund),  actualCounterFee);
		//用户积分账户余额不足
		if(userFundMainScore.getBalance() <= userTotalAmount) {
			throw new LTException(LTResponseCode.FU00012);
		}
		logger.info("校验userId:{}用户积分账户余额");
		
		//当前时间
		Date currentTime = new Date();
		//积分流水
		List<FundFlow> flowList = new ArrayList<FundFlow>();
		
		//=============1 冻结用户止损保证金流水=============
		//用户积分账户余额
		double userBalance = userFundMainScore.getBalance();
		//用户余额 = 用户余额  - 止损保证金
		userBalance = DoubleTools.sub(userBalance , holdFund);
		//增加用户止损保证金明细
		FundFlow holdFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.FREEZE_HOLDFUND, 
				userId, holdFund, userBalance, orderId, currentTime, currentTime);		
		flowList.add(holdFundFlow);
		logger.info("增加userId:{}止损保证金明细");
		
		//=============2 递延保证金流水=============
		//冻结递延保证金
		if(deferFund > 0){
			//用户余额 = 用户余额  - 递延保证金
			userBalance = DoubleTools.sub(userBalance , deferFund);
			//增加用户冻结递延保证金明细
			FundFlow deferFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.FREEZE_DEFERFUND, 
					userId, deferFund, userBalance, orderId, currentTime, currentTime);
			flowList.add(deferFundFlow);
			logger.info("增加userId:{}冻结递延保证金明细");
		}
		
		//=============3 扣除手续费流水=============
		//用户余额 = 用户余额 - 总手续费
		userBalance = DoubleTools.sub(userBalance , actualCounterFee);
		//增加总手续费明细
		FundFlow platformFeeFlow = new FundFlow(productName, FundScoreOptCodeEnum.DEDUCT_PLATFORM_FEE, 
				userId, actualCounterFee, userBalance, orderId, currentTime, currentTime);
		flowList.add(platformFeeFlow);
		logger.info("增加userId:{}手续费明细");
		
		//=============4 更新用户主账户、新增流水=============
		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScoreList(flowList);
		
		//扣除用户止损保证金、递延保证金、手续费
		this.doDeductFund(userId, holdFund, deferFund, actualCounterFee);
		logger.info("扣除userId:{}用户止损保证金、递延保证金、手续费完成");
		
		logger.info("开仓扣款用时time:{}ms", (System.currentTimeMillis() - startTime1));
		return true;
	}

	/**
	 * 平仓结算更新积分账户
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:36:08
	 * @see com.lt.api.fund.IFundScoreService#doBalance(java.lang.String, java.lang.String, java.lang.Integer, double, double, double)
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param userBenefit	用户净利润
	 * @return
	 * @throws LTException
	 */
	@Override
	public void doBalance(String productName, String orderId, String userId,
			 double holdFund, double deferFund, double userBenefit) throws LTException {
		// TODO Auto-generated method stub		
		
		logger.info("用户:{}, 订单:{}, 开始结算", userId, orderId);
		
		//查询用户积分账户信息
		FundMainScore userFundMainScore = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		logger.debug("查询userId:{}积分主账户信息", userId);
		if(userFundMainScore == null) {
			logger.error("用户:{}积分主账户未初始化", userId);
			throw new LTException(LTResponseCode.FU00011);
		}
		
		//当前时间
		Date currentTime = new Date();		
		//用户可用余额
		Double userBalance = userFundMainScore.getBalance();		

		int i = 0;
		//1 退回用户止损保证金
		i = this.doReFundHoldFund(orderId, userId, holdFund, userBalance, productName, currentTime);
		if(i == 0){
			logger.info("=========orderId={},退回用户止损保证金异常========",orderId);
			throw new LTException(LTResponseCode.ER400);
		}
		userBalance = DoubleTools.add(userBalance , holdFund);//退回保证金后的余额
		
		//2 退回递延保证金
		if(deferFund > 0){
			i = this.doReFundDeferFund(orderId, userId, deferFund, userBalance, productName, currentTime);
			if(i == 0){
				logger.info("=========orderId={},退回递延保证金异常========",orderId);
				throw new LTException(LTResponseCode.ER400);
			}
			userBalance = DoubleTools.add(userBalance , deferFund);//退回递延保证金后的余额
		}
		
		
		//3 增加用户利润
		i = this.addUserBenefit(orderId, userId, userBenefit, userBalance, productName, currentTime);
		if(i == 0){
			logger.info("=========orderId={},增加用户利润异常========",orderId);
			throw new LTException(LTResponseCode.ER400);
		}
	}

	/**
	 * 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:37:07
	 * @see com.lt.api.fund.IFundScoreService#doRefund(java.lang.String, java.lang.String, java.lang.Integer, double, double, double)
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param actualCounterFee	总手续费
	 * @return
	 * @throws LTException
	 */
	@Override
	public void doRefund(String productName, String orderId, String userId, double holdFund, 
			double deferFund, double actualCounterFee) throws LTException {
		// TODO Auto-generated method stub
		
		logger.info("用户:{}, 订单:{}, 开始退款", userId, orderId);
		//查询用户积分账户信息
		FundMainScore userFundMainScore = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		logger.debug("查询userId:{}积分主账户信息", userId);
		if(userFundMainScore == null) {
			logger.error("用户:{}积分主账户未初始化", userId);
			throw new LTException(LTResponseCode.FU00011);
		}		
				
		//当前时间
		Date currentTime = new Date();
		//用户积分账户余额
		Double userBalance = userFundMainScore.getBalance();
		
		int i = 0;
		//1 退回用户止损保证金=============
		i = this.doReFundHoldFund(orderId, userId, holdFund, userBalance, productName, currentTime);
		if(i == 0){
			logger.info("=========orderId={},退回用户止损保证金异常========",orderId);
			throw new LTException(LTResponseCode.ER400);
		}
		userBalance = DoubleTools.add(userBalance , holdFund);//退回保证金后的余额
		
		//2 退回递延保证金=============
		if(deferFund > 0){
			i = this.doReFundDeferFund(orderId, userId, deferFund, userBalance, productName, currentTime);
			if(i == 0){
				logger.info("=========orderId={},退回递延保证金异常========",orderId);
				throw new LTException(LTResponseCode.ER400);
			}
			userBalance = DoubleTools.add(userBalance , deferFund);//退回递延保证金后的余额
		}
		
		
		//3 退回手续费=============
		i = this.doReFundActualCounterFee(orderId, userId, actualCounterFee, userBalance, productName, currentTime);
		if(i == 0){
			logger.info("=========orderId={},退回手续费异常========",orderId);
			throw new LTException(LTResponseCode.ER400);
		}
	}

	/**
	 * 查询用户积分信息 for update
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午10:37:07
	 * @see com.lt.fund.service.IFundScoreService#queryFundMainScoreForUpdate(java.lang.Integer)
	 * @param userId
	 * @return
	 */
	@Override
	public FundMainScore queryFundMainScoreForUpdate(String userId) {
		FundMainScore userFundMainScore = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		logger.debug("查询userId:{}积分主账户信息", userId);
		if(userFundMainScore == null) {
			throw new LTException("用户积分主账户未初始化!");
		}		
		return userFundMainScore;
	}
	
	/**
	 * TODO 查询止损保证金流水汇总
	 * 
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:23:30
	 * @see com.lt.fund.service.IFundScoreService#queryHoldFundFlowSum(java.lang.String, java.lang.Integer)
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryHoldFundFlowSum(String orderId, String userId){
		
		List<String> secondOptcodeList = new ArrayList<String>();
		//40401 止损保证金冻结 
		secondOptcodeList.add(FundScoreOptCodeEnum.FREEZE_HOLDFUND.getCode());
		//30501 止损保证金解冻 
		secondOptcodeList.add(FundScoreOptCodeEnum.UNFREEZE_HOLDFUND.getCode());
		
		return fundFlowScoreDao.queryFundFlowScoreSum(orderId, userId, secondOptcodeList);
	}
	
	/**
	 * TODO 查询递延保证金流水汇总
	 * 
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:23:54
	 * @see com.lt.fund.service.IFundScoreService#queryDeferFundFlowSum(java.lang.String, java.lang.Integer)
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryDeferFundFlowSum(String orderId, String userId){
		
		List<String> secondOptcodeList = new ArrayList<String>();
		//40403 递延金冻结 
		secondOptcodeList.add(FundScoreOptCodeEnum.FREEZE_DEFERFUND.getCode());
		//30501 递延金解冻 
		secondOptcodeList.add(FundScoreOptCodeEnum.UNFREEZE_DEFERFUND.getCode());
		
		return fundFlowScoreDao.queryFundFlowScoreSum(orderId, userId, secondOptcodeList);
	}
	
	/**
	 * TODO 查询手续费流水汇总
	 * 
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:24:00
	 * @see com.lt.fund.service.IFundScoreService#queryActualCounterFeeFlowSum(java.lang.String, java.lang.Integer)
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryActualCounterFeeFlowSum(String orderId, String userId){
		
		List<String> secondOptcodeList = new ArrayList<String>();		
		//40501 手续费扣除
		secondOptcodeList.add(FundScoreOptCodeEnum.DEDUCT_PLATFORM_FEE.getCode());
		//30601 手续费退款
		secondOptcodeList.add(FundScoreOptCodeEnum.REFUND_PLATFORM_FEE.getCode());
				
		return fundFlowScoreDao.queryFundFlowScoreSum(orderId, userId, secondOptcodeList);
	}
	/**
	 * TODO 扣除用户止损保证金、递延保证金、手续费
	 * @author XieZhibing
	 * @date 2017年2月9日 下午6:05:32
	 * @param userId
	 * @param holdFund
	 * @param deferFund
	 * @param actualCounterFee
	 */
	private void doDeductFund(String userId, double holdFund, double deferFund, double actualCounterFee) {
		//=============4 用户主账户计算=============
		FundMainScore userFundMainScore = new FundMainScore();
		//设置用户ID
		userFundMainScore.setUserId(userId);
		//增加用户主账户止损保证金
		userFundMainScore.setHoldFund(holdFund);
		//增加用户冻结递延保证金
		userFundMainScore.setDeferFund(deferFund);		
		//增加用户累计手续费
		userFundMainScore.setTotalCounterFee(actualCounterFee);		
		//用户可用余额减少
		userFundMainScore.setBalance(-DoubleTools.add(DoubleTools.add(holdFund , deferFund) , actualCounterFee));
		
		//更新用户主账户
		fundMainScoreDao.updateFundMainScore(userFundMainScore);
	}

	/**
	 * 
	 * TODO 退止损保证金
	 * @author XieZhibing
	 * @date 2017年2月9日 下午5:29:13
	 * @param orderId
	 * @param userId
	 * @param holdFund
	 * @param userBalance
	 * @param productName
	 * @param currentTime
	 * @return
	 */
	private int doReFundHoldFund(String orderId, String userId,
			double holdFund, double userBalance, String productName, Date currentTime) {
		
		//剩余保证金
		Double holdFundFlowSum = this.queryHoldFundFlowSum(orderId, userId);
		logger.info("开始退止损保证金, userId:{}, orderId:{}, userBalance:{}, holdFund:{}, holdFundFlowSum:{}", userId, orderId, userBalance, holdFund, holdFundFlowSum);
		
		if(holdFundFlowSum == null || DoubleTools.add(holdFundFlowSum , holdFund) < 0){
			throw new LTException("用户"+userId+"的订单"+orderId+"流水异常, 剩余保证金:"+holdFundFlowSum+", 传入保证金:"+holdFund+", 请数据管理员核实流水数据");
		}
		
		
		//增加用户解冻止损保证金明细
		FundFlow holdFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.UNFREEZE_HOLDFUND, 
				userId, holdFund, DoubleTools.add(userBalance , holdFund), orderId, currentTime, currentTime);		

		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScore(holdFundFlow);
		logger.info("添加积分流水, userId:{}, orderId:{}, userBalance:{}, holdFundFlow:{}", userId, orderId, userBalance + holdFund, holdFundFlow);
		
		//积分账户
		FundMainScore fundMainScore = new FundMainScore();
		//设置用户ID
		fundMainScore.setUserId(userId);
		//退回用户主账户止损保证金
		fundMainScore.setHoldFund(-holdFund);
		//用户可用余额增加
		fundMainScore.setBalance(holdFund);
		//更新用户主账户
		int i = fundMainScoreDao.updateFundMainScore(fundMainScore);
		logger.info("退止损保证金完成, userId:{}, orderId:{}, holdFund:{}", userId, orderId, holdFund);
		
		return i;
	}

	/**
	 * 
	 * TODO 退递延保证金
	 * @author XieZhibing
	 * @date 2017年2月9日 下午5:29:21
	 * @param orderId
	 * @param userId
	 * @param deferFund
	 * @param userBalance
	 * @param productName
	 * @param currentTime
	 * @return
	 */
	private int doReFundDeferFund(String orderId, String userId,
			double deferFund, double userBalance, String productName, Date currentTime) {
		
		//无递延可退
		if(deferFund <= 0){
			return 0;
		}
		
		//剩余递延保证金
		Double deferFundFlowSum = this.queryDeferFundFlowSum(orderId, userId);
		logger.info("开始退递延保证金, userId:{}, orderId:{}, userBalance:{}, deferFund:{}, deferFundFlowSum:{}", userId, orderId, userBalance, deferFund, deferFundFlowSum);
		
		if(deferFundFlowSum == null || DoubleTools.add(deferFundFlowSum , deferFund) < 0){
			throw new LTException("用户"+userId+"的订单"+orderId+"流水异常, 剩余递延保证金:"+deferFundFlowSum+", 传入递延保证金:"+deferFund+", 请数据管理员核实流水数据");
		}
		
		
		//增加用户解冻递延保证金明细
		FundFlow deferFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.UNFREEZE_DEFERFUND, 
				userId, deferFund, DoubleTools.add(userBalance , deferFund), orderId, currentTime, currentTime);
		
		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScore(deferFundFlow);
		logger.info("添加积分流水, userId:{}, orderId:{}, userBalance:{}, deferFundFlow:{}", userId, orderId, userBalance + deferFund, deferFundFlow);
		
		//积分账户
		FundMainScore fundMainScore = new FundMainScore();
		//设置用户ID
		fundMainScore.setUserId(userId);
		//退回用户冻结递延保证金
		fundMainScore.setDeferFund(-deferFund);
		//用户可用余额增加
		fundMainScore.setBalance(deferFund);
		//更新用户主账户
		int i = fundMainScoreDao.updateFundMainScore(fundMainScore);
		logger.info("退递延保证金完成, userId:{}, orderId:{}, deferFund:{}", userId, orderId, deferFund);

		return i;
	}
	
	/**
	 * 
	 * TODO 退手续费
	 * @author XieZhibing
	 * @date 2017年2月9日 下午5:29:30
	 * @param orderId
	 * @param userId
	 * @param actualCounterFee
	 * @param userBalance
	 * @param productName
	 * @param currentTime
	 * @return
	 */
	private int doReFundActualCounterFee(String orderId, String userId, 
			double actualCounterFee, double userBalance, String productName, Date currentTime) {
		actualCounterFee = DoubleTools.scaleFormat(actualCounterFee);
		//剩余手续费
		Double actualCounterFeeFlowSum = this.queryActualCounterFeeFlowSum(orderId, userId);
		logger.info("开始退手续费, userId:{}, orderId:{}, userBalance:{}, actualCounterFee:{}, actualCounterFeeFlowSum:{}", userId, orderId, userBalance, actualCounterFee, actualCounterFeeFlowSum);
				
		if(actualCounterFeeFlowSum == null || DoubleTools.add(actualCounterFeeFlowSum , actualCounterFee) < 0){
			throw new LTException("用户"+userId+"的订单"+orderId+"流水异常, 剩余手续费:"+actualCounterFeeFlowSum+", 传入手续费:"+actualCounterFee+", 请数据管理员核实流水数据");
		}
		
		
		//增加退回总手续费明细
		FundFlow platformFeeFlow = new FundFlow(productName, FundScoreOptCodeEnum.REFUND_PLATFORM_FEE, 
				userId, actualCounterFee, DoubleTools.add(userBalance , actualCounterFee), orderId, currentTime, currentTime);		
		
		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScore(platformFeeFlow);
		logger.info("添加积分流水, userId:{}, orderId:{}, userBalance:{}, platformFeeFlow:{}", userId, orderId, userBalance + actualCounterFee, platformFeeFlow);
		
		//积分账户
		FundMainScore fundMainScore = new FundMainScore();
		//设置用户ID
		fundMainScore.setUserId(userId);
		//退回用户累计手续费
		fundMainScore.setTotalCounterFee(-actualCounterFee);
		//用户可用余额增加
		fundMainScore.setBalance(actualCounterFee);
		//更新用户主账户
		int i = fundMainScoreDao.updateFundMainScore(fundMainScore);
		logger.info("退手续费完成, userId:{}, orderId:{}, actualCounterFee:{}", userId, orderId, actualCounterFee);

		return i;
	}

	/**
	 * TODO 增加用户利润
	 * @author XieZhibing
	 * @date 2017年2月9日 下午5:07:04
	 * @param orderId
	 * @param userId
	 * @param userBenefit
	 * @param userBalance
	 * @param productName
	 * @param currentTime
	 */
	private int addUserBenefit(String orderId, String userId,
			double userBenefit, double userBalance, String productName, Date currentTime) {
		
		logger.info("开始增加用户利润, userId:{}, orderId:{}, userBalance:{}, userBenefit:{}", userId, orderId, userBalance, userBenefit);
		
		
		//用户利润绝对值
		double benefit = userBenefit < 0 ? -userBenefit : userBenefit;
		
		//利润流水明细
		FundFlow userProfitFlow = null;
		if(userBenefit >= 0) {
			//用户盈利明细
			userProfitFlow = new FundFlow(productName, FundScoreOptCodeEnum.USER_PROFIT, 
					userId, benefit, DoubleTools.add(userBalance , userBenefit), orderId, currentTime, currentTime);			
		} else {
			//用户亏损明细
			userProfitFlow = new FundFlow(productName, FundScoreOptCodeEnum.USER_LOSS, 
					userId, benefit, DoubleTools.add(userBalance , userBenefit), orderId, currentTime, currentTime);			
		}
		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScore(userProfitFlow);
		logger.info("添加积分流水, userId:{}, orderId:{}, userBalance:{}, userProfitFlow:{}", userId, orderId, userBalance + userBenefit, userProfitFlow);
		
		//=============4 计算用户主账户=============
		//积分账户
		FundMainScore fundMainScore = new FundMainScore();
		//设置用户ID
		fundMainScore.setUserId(userId);
		//增加用户累计盈利
		fundMainScore.setTotalBenefitAmount(userBenefit);
		//用户可用余额增加
		fundMainScore.setBalance(userBenefit);
		
		//更新用户主账户
		int i = fundMainScoreDao.updateFundMainScore(fundMainScore);
		logger.info("增加用户利润完成, userId:{}, orderId:{}, userBenefit:{}", userId, orderId, userBenefit);
		 //用户余额 = 用户余额 + 用户利润
		return i;
	}

	@Override
	public void deductInterest(String userId, String productName, String orderId, double interest) {
		Date currentTime = new Date();
		//查询用户现金账户信息
		FundMainScore userFundMainScore = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		logger.info("查询userId:{}积分主账户信息", userId);
		if(userFundMainScore == null) {
			throw new LTException("用户积分主账户未初始化!");
		}
		FundMainScore score = new FundMainScore();
		score.setUserId(userId);
		score.setBalance(-interest);
		score.setTotalInterestAmount(interest);
		
		//递延结息流水明细
		FundFlow fundFlow = new FundFlow(productName,  FundScoreOptCodeEnum.DEDUCT_INTEREST, 
				userId, interest, DoubleTools.sub(userFundMainScore.getBalance() , interest), orderId, currentTime, currentTime);
		fundMainScoreDao.updateFundMainScore(score);
		fundFlowScoreDao.addFundFlowScore(fundFlow);
	
	}

}
