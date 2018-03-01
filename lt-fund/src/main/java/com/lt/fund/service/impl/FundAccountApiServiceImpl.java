/*
 * PROJECT NAME: lt-fund
 * PACKAGE NAME: com.lt.fund.service
 * FILE    NAME: FundAccountApiServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.fund.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lt.fund.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundIoRechargeEnum;
import com.lt.enums.fund.FundScoreOptCodeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.fund.dao.FundIoCashRechargeDao;
import com.lt.fund.dao.FundIoCashWithdrawalDao;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundMainScore;
import com.lt.model.fund.FundOptCode;
import com.lt.model.fund.FundPayAuthFlow;
import com.lt.model.fund.FundVo;
import com.lt.model.promote.CommisionIo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.vo.fund.FundFlowVo;
import com.lt.vo.fund.FundOrderVo;

/**
 * TODO 资金账户业务实现
 * 
 * @author XieZhibing
 * @date 2016年11月30日 下午4:43:58
 * @version <b>1.0.0</b>
 */
@Service
public class FundAccountApiServiceImpl implements IFundAccountApiService {

	/** 序列化标识 */
	private static final long serialVersionUID = -1372141418781594234L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(FundAccountApiServiceImpl.class);

	@Autowired
	private IFundCashService fundCashServiceImpl;
	@Autowired
	private IFundScoreService fundScoreServiceImpl;
	@Autowired
	private IFundFlowCashService fundFlowCashServiceImpl;
	@Autowired
	private IFundFlowScoreService fundFlowScoreServiceImpl;
	@Autowired
	private IFundIoCashWithdrawalService fundIoCashWithdrawalService;
	@Autowired
	private IFundIoCashInnerService fundIoCashInnerService;
	@Autowired
	private FundIoCashWithdrawalDao fundIoCashWithdrawalDao;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;	
	@Autowired
	private FundIoCashRechargeDao fundIoCashRechargeDao;

	@Autowired
	private IFundCashRechargeService fundCashRechargeService;

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:53:45
	 * @see com.lt.api.fund.rmi.IFundAccountApiService#queryFundMainCash(java.lang.Integer)
	 * @param userId
	 * @return
	 */
	@Override
	public FundMainCash queryFundMainCash(String userId) {
		// TODO Auto-generated method stub
		return fundCashServiceImpl.queryFundMainCash(userId);
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:53:45
	 * @see com.lt.api.fund.rmi.IFundAccountApiService#queryFundMainScore(java.lang.Integer)
	 * @param userId
	 * @return
	 */
	@Override
	public FundMainScore queryFundMainScore(String userId) {
		// TODO Auto-generated method stub
		return fundScoreServiceImpl.queryFundMainScore(userId);
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月13日 上午11:19:11
	 * @see com.lt.api.fund.IFundAccountApiService#doInitFundCashAccount(java.lang.Integer)
	 * @param userId
	 * @return
	 * @throws LTException
	 */
	@Override
	public boolean doInitFundCashAccount(String userId) throws LTException {
		// TODO Auto-generated method stub

		// 初始化现金主账户
		fundCashServiceImpl.doInitFundCashAccount(userId);
		logger.info("已初始化现金主账户 userId:{}", userId);

		return true;
	}

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月13日 上午11:19:19
	 * @see com.lt.api.fund.IFundAccountApiService#doInitFundScoreAccount(java.lang.Integer)
	 * @param userId
	 * @return
	 * @throws LTException
	 */
	public boolean doInitFundScoreAccount(String userId) throws LTException {
		// TODO Auto-generated method stub

		// 初始化积分主账户
		fundScoreServiceImpl.doInitFundScoreAccount(userId);
		logger.info("已初始化积分主账户 userId:{}", userId);

		return true;
	}

	@Override
	public void updateDefer(boolean flag, FundOrderVo fundOrderVo) throws LTException {
		logger.info("开启/关闭递延==============================开始");
		// TODO 开启/关闭递延操作
		// 资金类型
		FundTypeEnum fundType = fundOrderVo.getFundType();
		if (fundType == null) {
			throw new LTException("资金类型为空!");
		}
		// 产品名称
		String productName = fundOrderVo.getProductName();
		if (StringTools.isEmpty(productName)) {
			throw new LTException("产品名称不能为空!");
		}
		// 订单ID
		String orderId = fundOrderVo.getOrderId();
		if (StringTools.isEmpty(orderId)) {
			throw new LTException("订单ID不能为空!");
		}
		// 用户ID
		String userId = fundOrderVo.getUserId();
		if (userId == null) {
			throw new LTException("用户ID不能为空!");
		}
		// 递延保证金
		double deferFund = fundOrderVo.getDeferFund();
		if (deferFund < 0) {
			throw new LTException("递延保证金不能小于0!");
		}

		switch (fundType) {

		case CASH:
			// 查询用户现金账户信息
			logger.info("进入到修改递延资金方法");
			updateDeferCash(flag, orderId, userId, productName, deferFund);
			break;
		case SCORE:
			FundMainScore userFundMainScore = fundScoreServiceImpl.queryFundMainScoreForUpdate(userId);
			updateDeferScore(userFundMainScore, flag, orderId, userId, productName, deferFund);
			break;
		default:
			break;
		}
		logger.info("开启/关闭递延==============================结束");

	}

	/**
	 * 开启/关闭递延 现金
	 * 
	 * @param flag
	 *            true/false 开启/关闭
	 * @param orderId
	 * @param userId
	 * @param productName
	 * @param deferFund
	 */
	private void updateDeferCash(boolean flag, String orderId, String userId, String productName, double deferFund) {
		// 查询该订单冻结流水信息 return size();
		List<String> list = new ArrayList<String>();
		list.add(FundCashOptCodeEnum.FREEZE_DEFERFUND.getCode());
		list.add(FundCashOptCodeEnum.UNFREEZE_DEFERFUND.getCode());
		Double fundFlowCashSum = fundFlowCashServiceImpl.findFreezeFundFlowCashSum(orderId, userId, list);

		if (fundFlowCashSum == null) {
			fundFlowCashSum = new Double(0.0);
		}
		//如果 orderId的所有冻结流水金额(负数) + orderId的所有退款流水金额 < 0   关闭   执行关闭退款操作 
		logger.info("flag============================"+flag);
		if(fundFlowCashSum < 0 && !flag){
			if(Math.abs(deferFund) == Math.abs(fundFlowCashSum)){
				fundFlowCashServiceImpl.doDeferFundRefund(deferFund, orderId, userId, productName);
				logger.info("递延退款操作执行完毕");
				return;
			} else {
				throw new LTException("用户" + userId + "的订单" + orderId + "流水异常 。退款递延保证金:" + fundFlowCashSum + "，传入保证金:"
						+ deferFund + " 。请数据管理员核实流水数据");
			}
		}
		// 如果 orderId的所有冻结流水金额 (负数 )+ orderId的所有退款流水金额 == 0 开启 执行递延扣款
		if (flag && fundFlowCashSum == 0) {
			fundFlowCashServiceImpl.doDeferFundDeductions(deferFund, orderId, userId, productName);
			logger.info("递延扣款操作执行完毕");
			return;
		}
		// 大于0 是异常 操作 抛出异常
		logger.error(" flag={}, orderId={}, userId={}, productName={}, deferFund={}", flag, orderId, userId,
				productName, deferFund);
		throw new LTException("资金没有执行任何操作");
	}

	/**
	 * 开启/关闭递延 积分
	 * 
	 * @param userFundMainScore
	 * @param flag
	 * @param orderId
	 * @param userId
	 * @param productName
	 * @param deferFund
	 */
	private void updateDeferScore(FundMainScore userFundMainScore, boolean flag, String orderId, String userId,
			String productName, double deferFund) {
		List<String> list = new ArrayList<String>();
		list.add(FundScoreOptCodeEnum.FREEZE_DEFERFUND.getCode());
		list.add(FundScoreOptCodeEnum.UNFREEZE_DEFERFUND.getCode());
		Double fundFlowScoreSum = fundFlowScoreServiceImpl.findFreezeFundFlowScoreSum(orderId, userId, list);

		if (fundFlowScoreSum == null) {
			fundFlowScoreSum = new Double(0.0);
		}

		// 如果 orderId的所有冻结流水金额(负数) + orderId的所有退款流水金额 < 0 关闭 执行关闭退款操作
		if (fundFlowScoreSum < 0 && !flag) {
			if (Math.abs(deferFund) == Math.abs(fundFlowScoreSum)) {
				fundFlowScoreServiceImpl.doDeferFundRefund(userFundMainScore, deferFund, orderId, userId, productName);
				logger.info("递延退款操作执行完毕");
				return;
			} else {
				throw new LTException("用户" + userId + "的订单" + orderId + "流水异常 。退款递延保证金:" + fundFlowScoreSum + "，传入保证金:"
						+ deferFund + " 。请数据管理员核实流水数据");
			}
		}
		// 如果 orderId的所有冻结流水金额（负数） + orderId的所有退款流水金额 == 0 开启 执行递延扣款
		if (flag && fundFlowScoreSum == 0) {
			fundFlowScoreServiceImpl.doDeferFundDeductions(userFundMainScore, deferFund, orderId, userId, productName);
			logger.info("递延扣款操作执行完毕");
			return;
		}
		// 大于0 是异常 操作 抛出异常
		logger.error(" flag={}, orderId={}, userId={}, productName={}, deferFund={}", flag, orderId, userId,
				productName, deferFund);
		throw new LTException("资金没有执行任何操作");
	}

	@Override
	public void updateProfitLoss(FundOrderVo fundOrderVo) throws LTException {
		// TODO 更新止盈止损操作
		// 资金类型
		FundTypeEnum fundType = fundOrderVo.getFundType();
		if (fundType == null) {
			throw new LTException("资金类型为空!");
		}
		// 投资人
		String investorId = fundOrderVo.getInvestorId();
		if (investorId == null) {
			throw new LTException("用户ID不能为空!");
		}
		// 产品名称
		String productName = fundOrderVo.getProductName();
		if (StringTools.isEmpty(productName)) {
			throw new LTException("产品名称不能为空!");
		}
		// 订单ID
		String orderId = fundOrderVo.getOrderId();
		if (StringTools.isEmpty(orderId)) {
			throw new LTException("订单ID不能为空!");
		}
		// 用户ID
		String userId = fundOrderVo.getUserId();
		if (userId == null) {
			throw new LTException("用户ID不能为空!");
		}
		// 止损保证金
		double holdFund = fundOrderVo.getHoldFund();
		if (holdFund < 0) {
			throw new LTException("止损保证金不能小于0!");
		}
		if (holdFund == 0) {
			// 止损保证金没有变化不对资金做任何处理
			return;
		}
		// 止盈保证金
		double stopProfit = fundOrderVo.getStopProfit();
		if (stopProfit < 0) {
			throw new LTException("止盈保证金不能小于0!");
		}
		switch (fundType) {
		case CASH:
			FundMainCash userFundMainCash = fundCashServiceImpl.queryFundMainCash(userId);
			updateProfitLossCash(userFundMainCash, investorId, userId, holdFund, orderId, productName);
			break;
		case SCORE:
			FundMainScore userFundMainScore = fundScoreServiceImpl.queryFundMainScore(userId);
			updateProfitLossScore(userFundMainScore, investorId, userId, holdFund, stopProfit, orderId, productName);
			break;
		default:
			break;
		}
	}

	/**
	 * 修改现金止盈止损操作
	 * 
	 * @param userFundMainCash
	 * @param investorId
	 * @param userId
	 * @param holdFund
	 *            止损保证金
	 * @param stopProfit
	 *            止盈保证金
	 * @param orderId
	 * @param productName
	 */
	private void updateProfitLossCash(FundMainCash userFundMainCash, String investorId, String userId,
			double holdFund, String orderId, String productName) {
		// TODO 修改止盈止损 先退回冻结保证金 然后再扣
		List<String> list = new ArrayList<String>();
		list.add(FundCashOptCodeEnum.FREEZE_HOLDFUND.getCode());
		list.add(FundCashOptCodeEnum.UNFREEZE_HOLDFUND.getCode());
		Double fundFlowCashSum = fundFlowCashServiceImpl.findFreezeFundFlowCashSum(orderId, userId, list);
		// 如果 orderId的所有冻结流水金额(负数) + orderId的所有退款流水金额 > 0 关闭 执行关闭退款操作
		if (fundFlowCashSum < 0) {
			// 判断是否允许修改资金
			double sum = Math.abs(fundFlowCashSum);
			// 解冻金+余额 > 新的止损保证金 可以修改止盈止损
			if ((sum + userFundMainCash.getBalance()) >= holdFund) {
				fundFlowCashServiceImpl.doUnfreezeProfitLoss(userId, sum, orderId, productName);
				logger.info("止盈止损解冻操作执行完毕");
				fundFlowCashServiceImpl.doFreezeProfitLoss(userId, holdFund, orderId, productName);
				logger.info("止盈止损冻结操作执行完毕");
				return;
			} else {
				throw new LTException("修改现金止盈止损 参数有误   数据库止损保证金与入参止损保证金不相符");
			}
		}
		// 大于0 是数据库中同一组解冻资金比冻结资金还多异常 操作抛出异常
		throw new LTException("资金没有执行任何操作");
	}

	/**
	 * 修改积分止盈止损操作
	 * 
	 * @param userFundMainScore
	 * @param investorId
	 *            证券账户
	 * @param userId
	 * @param holdFund
	 *            止损保证金
	 * @param stopProfit
	 *            止盈保证金
	 * @param orderId
	 * @param productName
	 */
	private void updateProfitLossScore(FundMainScore userFundMainScore, String investorId, String userId,
			double holdFund, double stopProfit, String orderId, String productName) {
		// TODO 修改止盈止损 先退回冻结保证金 然后再扣
		List<String> list = new ArrayList<String>();
		list.add(FundScoreOptCodeEnum.FREEZE_HOLDFUND.getCode());
		list.add(FundScoreOptCodeEnum.UNFREEZE_HOLDFUND.getCode());
		Double fundFlowScoreSum = fundFlowScoreServiceImpl.findFreezeFundFlowScoreSum(orderId, userId, list);
		// 如果 orderId的所有冻结流水金额(负数) + orderId的所有退款流水金额 > 0 关闭 执行关闭退款操作
		if (fundFlowScoreSum < 0) {
			// 判断是否允许修改资金
			double sum = Math.abs(fundFlowScoreSum);
			// 解冻金+余额 > 新的止损保证金 可以修改止盈止损
			if (DoubleTools.add(sum , userFundMainScore.getBalance()) >= holdFund) {
				fundFlowScoreServiceImpl.doUnfreezeProfitLoss(userFundMainScore, investorId, userId, sum,
						stopProfit, orderId, productName);
				logger.info("止盈止损解冻操作执行完毕");
				fundFlowScoreServiceImpl.doFreezeProfitLoss(userFundMainScore, investorId, userId, holdFund, stopProfit,
						orderId, productName);
				logger.info("止盈止损冻结操作执行完毕");
				return;
			} else {
				throw new LTException("修改积分止盈止损 参数有误   数据库止损保证金与入参止损保证金不相符");
			}
		}
		// 大于0 是异常 操作 抛出异常
		throw new LTException("资金没有执行任何操作");
	}

	@Override
	public void manualOutOrIn(String orderId,String userId, Double freezeAmount, IFundOptCode fundCashOptCode, FundTypeEnum fundType,
			String thirdOptCode,String remark,Double rmbAmount,Integer modifyUserId) {
		if (null == userId) {
			throw new LTException("userId 参数为空!!!");
		}
		if (null == freezeAmount || freezeAmount == 0D) {
			throw new LTException("freezeAmount 参数为空!!!");
		}
		// 人工取出类型
		if (fundCashOptCode == null) {
			throw new LTException("请输入 参数为空!!!");
		}
		// 资金类型
		if (fundType == null) {
			throw new LTException("资金类型为空!");
		}
		switch (fundType) {
		case CASH:
			if (fundCashOptCode.getCode() == FundCashOptCodeEnum.MANUALOUT.getCode()) {
				fundIoCashInnerService.doManualOutCash(orderId,userId, freezeAmount, thirdOptCode,remark,rmbAmount,modifyUserId);
			}
			if (fundCashOptCode.getCode() == FundCashOptCodeEnum.MANUALIN.getCode()) {
				fundIoCashInnerService.doManualInByCash(orderId,userId, freezeAmount, thirdOptCode,remark,rmbAmount,modifyUserId);
			}
			if (fundCashOptCode.getCode() == FundCashOptCodeEnum.RECHARGENOTE.getCode()) {
				fundIoCashInnerService.doManualInByCash(orderId,userId, freezeAmount, thirdOptCode,remark,rmbAmount,modifyUserId);
			}
			break;
		case SCORE:
			if (fundCashOptCode.getCode() == FundScoreOptCodeEnum.MANUALOUT.getCode()) {
				fundIoCashInnerService.doManualOutScore(orderId,userId, freezeAmount, thirdOptCode,remark,modifyUserId);
			}
			if (fundCashOptCode.getCode() == FundScoreOptCodeEnum.MANUALIN.getCode()) {
				fundIoCashInnerService.doManualInByScore(orderId,userId, freezeAmount, thirdOptCode,remark,modifyUserId);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public Map<String, String> rechargeForUnspay(UserBussinessInfo info, String amount,String rmbAmt, String responseUrl,String bankCode,
			String thirdOptcode,Double rate) throws LTException {
		return fundCashServiceImpl.rechargeForUnspay(info, amount,rmbAmt, responseUrl,bankCode, thirdOptcode,rate);
	}

	@Override
	public void updateDeferAndProfitLoss(boolean flag, FundOrderVo fundOrderVo) throws LTException {
		// TODO Auto-generated method stub
		// 资金类型
		FundTypeEnum fundType = fundOrderVo.getFundType();
		if (fundType == null) {
			throw new LTException("资金类型为空!");
		}
		// 投资人
		String investorId = fundOrderVo.getInvestorId();
		if (investorId == null) {
			throw new LTException("用户ID不能为空!");
		}
		// 产品名称
		String productName = fundOrderVo.getProductName();
		if (StringTools.isEmpty(productName)) {
			throw new LTException("产品名称不能为空!");
		}
		// 订单ID
		String orderId = fundOrderVo.getOrderId();
		if (StringTools.isEmpty(orderId)) {
			throw new LTException("订单ID不能为空!");
		}
		// 用户ID
		String userId = fundOrderVo.getUserId();
		if (userId == null) {
			throw new LTException("用户ID不能为空!");
		}
		// 止损保证金
		double holdFund = fundOrderVo.getHoldFund();
		if (holdFund < 0) {
			throw new LTException("止损保证金不能小于0!");
		}
		// 止盈保证金
		double stopProfit = fundOrderVo.getStopProfit();
		if (stopProfit < 0) {
			throw new LTException("止盈保证金不能小于0!");
		}
		// 递延保证金
		double deferFund = fundOrderVo.getDeferFund();
		if (deferFund < 0) {
			throw new LTException("递延保证金不能小于0!");
		}
		switch (fundType) {
		case CASH:

			updateDeferCash(flag, orderId, userId, productName, deferFund);
			if (holdFund == 0) {
				return;
			} else {
				FundMainCash userFundMainCash = fundCashServiceImpl.queryFundMainCash(userId);
				updateProfitLossCash(userFundMainCash, investorId, userId, holdFund, orderId, productName);

			}
			break;
		case SCORE:
			// 查询用户现金账户信息
			FundMainScore userFundMainScore = fundScoreServiceImpl.queryFundMainScore(userId);
			updateDeferScore(userFundMainScore, flag, orderId, userId, productName, deferFund);
			if (holdFund == 0) {
				return;
			} else {
				updateProfitLossScore(userFundMainScore, investorId, userId, holdFund, stopProfit, orderId,
						productName);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void callbackForUnspay(Map<String, Object> map) throws Exception {
		fundCashServiceImpl.callbackForUnspay(map);
	}

	@Override
	public Map<String,String> callbackForDaddyPay(Map<String, String> map) {
		return fundCashServiceImpl.callbackForDaddyPay(map);
	}

	/**
	 * 获取用户积分现金可以余额，持仓保证金
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public FundVo findUserFund(String userId) {
		FundVo fundVo = new FundVo();
		FundMainCash fundMainCash = fundCashServiceImpl.queryFundMainCash(userId);
		if (fundMainCash != null) {
			fundVo.setCashAmt(fundMainCash.getBalance());
			// 持仓保证金包含 递延保证金
			fundVo.setHoldCashFund(DoubleTools.add(fundMainCash.getHoldFund() , fundMainCash.getDeferFund()));
		}
		FundMainScore fundMainScore = fundScoreServiceImpl.queryFundMainScore(userId);
		if (fundMainScore == null) {
			throw new LTException("积分账户为空异常");
		}
		fundVo.setScoreAmt(fundMainScore.getBalance());
		fundVo.setHoldScoreFund(DoubleTools.add(fundMainScore.getHoldFund() , fundMainScore.getDeferFund()));
		return fundVo;
	}

	@Override
	public Map<String, String> doWithdrawApply(String userId, Double outAmount,Double rate, String withdrawCode, String faxCode,
			boolean flag,String sign) throws LTException {
		return fundIoCashWithdrawalService.doWithdrawApply(userId, outAmount, rate,withdrawCode, faxCode, flag,sign);
	}

	@Override
	public void withdrawSuccess(FundIoCashWithdrawal fio,String thirdOptCode) throws Exception {
		fundIoCashWithdrawalService.withdrawSuccess(fio,thirdOptCode);
	}

	@Override
	public void withdrawFail(FundIoCashWithdrawal fio) throws Exception {
		fundIoCashWithdrawalService.withdrawFail(fio);
	}

	@Override
	@Transactional
	public void fundDoAudit(Long id, Integer modifyUserId, Integer status, IFundOptCode thirdOptCode, String remark)
			throws LTException {
		FundIoCashWithdrawal info = fundIoCashWithdrawalDao.queryFundIoCashWithdrawalById(id);
		fundIoCashWithdrawalService.doAudit(id, modifyUserId+"", status, thirdOptCode,info.getFactTax(), remark);
	}

	@Override
	public void fundDoAuditSingle(Long id, Integer modifyUserId, Integer status, IFundOptCode thirdOptCode,
			Double amount, String remark) throws LTException {
		fundIoCashWithdrawalService.doAudit(id, modifyUserId+"", status, thirdOptCode,amount, remark);
	}

	@Override
	public void fundInnerDoAudit(Long id, Integer status, String remark,Integer modifyUserId) throws LTException {
		fundIoCashInnerService.doCashAudit(id, status, remark,modifyUserId);
	}
	
	@Override
	public void scoreInnerDoAudit(Long id, Integer status, String remark,Integer modifyUserId) throws LTException {
		fundIoCashInnerService.doScoreAudit(id, status, remark,modifyUserId);
	}

	@Override
	public List<FundFlowVo> findFundFollowByUserId(Map<String, Object> map, Integer fundType) {
		if (fundType == FundTypeEnum.CASH.getValue()) {
			return fundFlowCashServiceImpl.findFundFollowByUserId(map);
		} else {
			return fundFlowScoreServiceImpl.findFundFollowByUserId(map);
		}
	}

	@Override
	public List<FundFlowVo> findFundFollowByOrder(Map<String, Object> map, Integer fundType) {
		if (fundType == FundTypeEnum.CASH.getValue()) {
			return fundFlowCashServiceImpl.findFundFollowByOrder(map);
		} else {
			return fundFlowScoreServiceImpl.findFundFollowByOrder(map);
		}
	}

	@Override
	public void repairFinancyIo(Double actAmount,Double rmbAmt,FundIoCashRecharge fio, FundOptCode foc) {
		try {
			fundCashServiceImpl.doReCharge(actAmount,rmbAmt,fio, foc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LTException(LTResponseCode.FUY00003);
		}
	}

	@Override
	public void commisionWithdraw(CommisionIo io) throws Exception {
		fundCashServiceImpl.doCommisionWithdraw(io);
	}

	@Override
	public void updateFundByPeriodFee(String userId, String productName, String orderId, Double fee)
			throws LTException {
		fundCashServiceImpl.deductInterest(userId, productName, orderId, fee);
	}

	@Override
	public void updateScoreByPeriodFee(String userId, String productName, String orderId, Double fee)
			throws LTException {
		// TODO Auto-generated method stub
		fundScoreServiceImpl.deductInterest(userId, productName, orderId, fee);
	}

	@Override
	public Map<String, Object> getWithdrawDoneDate(String userId, Long ioId) throws LTException {
		return fundCashServiceImpl.getWithdrawDoneDate(userId, ioId);
	}

	@Override
	public List<Map<String, Object>> getWithdrawHistory(Map<String, Object> param) throws LTException {
		return fundCashServiceImpl.getWithdrawHistory(param);
	}

	@Override
	public Map<String, Object> getWithdrawHistoryDetail(String userId, Long ioId) throws LTException {
		return fundCashServiceImpl.getWithdrawHistoryDetail(userId, ioId);
	}

	@Override
	public void WithdrawCancel(String userId, Long ioId) throws LTException {
		fundIoCashWithdrawalService.WithdrawCancel(userId, ioId);
	}

	@Override
	public Map<String, String> createAlipayUrl(String userId, Double amt, String url,Double rate) throws LTException {

		logger.info("校验金额是否传递到后台：{}", amt);
		if (amt == null ) {
			logger.info("参数传递失败");
			throw new LTException(LTResponseCode.FU00003);
		}

		// 系统内部订单号
		return fundCashServiceImpl.RequestchargeByZfb(userId, amt, url,rate);
	}

	@Override
	public void reviceZfbResponse(Map<String, Object> paraMap) throws LTException {
		fundCashServiceImpl.aplipayCallBack(paraMap);
	}
	
	@Override
	public Boolean isExistPayAuthFlow(FundPayAuthFlow financyPayAuthFlow){
		List<FundPayAuthFlow> flowList = fundCashServiceImpl.getFundPayAuthFlow(financyPayAuthFlow); 
		if(CollectionUtils.isNotEmpty(flowList)){
			return true;
		}
		return false;
	}
	
	@Override
	public void insertRechargeIo(String userId,String orderId,Double amt,Double rmbAmt,String transferNum,String bankCode,String thirdLevelCode,Double rate) throws LTException{
		fundCashServiceImpl.insertRechargeIo(userId, orderId, amt,rmbAmt,rate, transferNum,bankCode, thirdLevelCode);
	}
	
	@Override
	public void insertAlipayRechargeIo(String userId,String orderId,Double amt,Double rmbAmt,String transferNum,String alipayNum,String bankCode,String thirdLevelCode,Double rate) throws LTException{
		fundCashServiceImpl.insertAlipayRechargeIo(userId, orderId,amt, rmbAmt, rate, transferNum, alipayNum, bankCode, thirdLevelCode);
	}

	@Override
	public void zfbTransfer(Map<String, Object> paraMap) throws LTException {
		String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
		String orderId = String.valueOf(CalendarTools.getMillis(new Date()));
		String amtStr = StringTools.formatStr(paraMap.get("amt"), "0");
		String rmbAmtStr = StringTools.formatStr(paraMap.get("rmbAmt"), "0");
		String transferNum = StringTools.formatStr(paraMap.get("transferNum"), "");
		String rateStr = StringTools.formatStr(paraMap.get("rate"), "");
		String alipay_num = StringTools.formatStr(paraMap.get("alipay_num"), "");
		String bankCode = StringTools.formatStr(paraMap.get("bankCode"), "");
		
		if(!StringTools.isNumberic(rateStr,false,true,true) || !StringTools.isNumberic(rateStr,false,true,true)){
			throw new LTException(LTResponseCode.FU00003);
		}
		
		Double amt = Double.valueOf(amtStr);
		Double rate = Double.valueOf(rateStr);
		Double rmbAmt = Double.valueOf(rmbAmtStr);
		insertAlipayRechargeIo(userId, orderId, amt,rmbAmt,transferNum,alipay_num,bankCode,FundThirdOptCodeEnum.HANDAIPAY.getThirdLevelCode(),rate);
	}

	@Override
	public Integer updateFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException {
		return fundCashServiceImpl.updateFundPayAuthFlow(financyPayAuthFlow);
	}

	@Override
	public void addFinancyPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException {
		fundCashServiceImpl.addFundPayAuthFlow(financyPayAuthFlow);
	}

	@Override
	public void doUserKQrecharge(String payId, Double rmbAmt) throws LTException {
		FundIoCashRecharge cashRecharge = fundCashServiceImpl.qryFundIoCashRechargeByPayId(payId);
		
		logger.info("原充值金额为：{},比较金额为：{}",cashRecharge.getRmbAmt(),rmbAmt);
		if(cashRecharge == null || cashRecharge.getRmbAmt()!= rmbAmt.doubleValue()){
			
			throw new LTException(LTResponseCode.FU00006);
		}
		
		FundOptCode code = new FundOptCode();
		code.setFirstOptCode(FundCashOptCodeEnum.RECHARGE.getFirstLevelCode());
		code.setSecondOptCode(FundThirdOptCodeEnum.KQCZ.getSecondCode());
		code.setThirdOptCode(FundThirdOptCodeEnum.KQCZ.getThirdLevelCode());
		
		try {
			fundCashServiceImpl.doRecharge(cashRecharge, code);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
	}
	
	@Override
	public void doUserRecharge(String payId,String externalNo, FundOptCode code, Double rmbAmt) throws LTException {

		fundCashRechargeService.doSuccessRecharge(payId,externalNo,rmbAmt,code);

//		FundIoCashRecharge cashRecharge = fundCashServiceImpl.qryFundIoCashRechargeByPayId(payId);
//
//		logger.info("原充值金额为：{},比较金额为：{}",cashRecharge.getRmbAmt(),rmbAmt);
//		if(cashRecharge == null || cashRecharge.getRmbAmt()!= rmbAmt.doubleValue()){
//
//			throw new LTException(LTResponseCode.FUY00014);
//		}
//
//		try {
//			fundCashServiceImpl.doRecharge(cashRecharge, code);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new LTException(e.getMessage());
//		}
	}

	@Override
	public void setfailRechargeIo(String payId, String failReason) throws LTException {
		FundIoCashRecharge cashRecharge = new FundIoCashRecharge();
		cashRecharge.setPayId(payId);
		cashRecharge.setFailReason(failReason);
		fundCashServiceImpl.setFundRechargeIoFail(cashRecharge);
	}

	@Override
	public Double getDailyUserRechargeTotalAmt(String userId) throws LTException {
		return fundFlowCashServiceImpl.getUserDailyRestReChargeAmt(userId);
	}

	@Override
	public Double getPingAnRechargeAmtInYear(String userId) throws LTException {
		return fundFlowCashServiceImpl.getPingAnRechargeAmtInYear(userId);
	}
	
	@Override
	public FundIoCashRecharge qryFundIoCashRechargeByPayId(String payId) throws LTException {
		return fundCashServiceImpl.qryFundIoCashRechargeByPayId(payId);
	}

	@Override
	public void pingAnTransfer(Map<String, Object> paraMap) throws LTException {
		String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
		String orderId = String.valueOf(CalendarTools.getMillis(new Date()));
		String amtStr = StringTools.formatStr(paraMap.get("amt"), "0");
		String rmbAmtStr = StringTools.formatStr(paraMap.get("rmbAmt"), "0");
		String transferNum = StringTools.formatStr(paraMap.get("transferNum"), "");
		String rateStr = StringTools.formatStr(paraMap.get("rate"), "");
		String bankCode = StringTools.formatStr(paraMap.get("bankCode"), "");
		
		if(!StringTools.isNumberic(rateStr,false,true,true) || !StringTools.isNumberic(rateStr,false,true,true)){
			throw new LTException(LTResponseCode.FU00003);
		}
		
		Double amt = Double.valueOf(amtStr);
		Double rate = Double.valueOf(rateStr);
		Double rmbAmt = Double.valueOf(rmbAmtStr);
		insertRechargeIo(userId, orderId, amt,rmbAmt,transferNum,bankCode, FundThirdOptCodeEnum.YHHK.getThirdLevelCode(),rate);
	}

	@Override
	public void dinPayAccept(Map<String, Object> map) throws LTException {
		this.fundCashServiceImpl.dinpayAccept(map);
	}

	@Override
	public Map<String, Object> dinPayCreate(Map<String, Object> paramMap) throws LTException {
		return this.fundCashServiceImpl.dinpayCreate(paramMap);
	}

	@Override
	public List<FundPayAuthFlow> queryFundPayAuthFlowList(FundPayAuthFlow financyPayAuthFlow) throws LTException {
		return fundCashServiceImpl.getFundPayAuthFlow(financyPayAuthFlow); 
	}

	@Override
	public List<FundIoCashRecharge> queryFundIoCashRechargeList(FundIoCashRecharge fundIoCashRecharge) throws LTException {
		return this.fundIoCashRechargeDao.selectFundIoRecharge(fundIoCashRecharge);
	}

	@Override
	public void swiftPassCallback(Map<String,String> map)throws LTException{
		fundCashServiceImpl.swiftPassCallback(map);
	}

	@Override
	public void callbackForYiBao(Map<String, String> map) throws LTException {

	}
}
