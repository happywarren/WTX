package com.lt.fund.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lt.fund.dao.FundIoCashRechargeDao;
import com.lt.model.fund.FundIoCashRecharge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.fund.dao.FundFlowCashDao;
import com.lt.fund.dao.FundMainCashDao;
import com.lt.fund.service.IFundFlowCashService;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundMainCash;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.vo.fund.FundFlowVo;

@Service
public class FundFlowCashServiceImpl implements IFundFlowCashService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FundFlowCashDao fundFlowCashDao;
	@Autowired
	private FundMainCashDao fundMainCashDao;
	@Autowired
	private FundIoCashRechargeDao fundIoCashRechargeDao;
	
	@Override
	public boolean doDeferFundDeductions(double deferFund, String orderId,
			String userId, String productName) {
		//查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
		if(userFundMainCash==null || userFundMainCash.getBalance() <= 0) {
//		if(!StringTools.isNotEmpty(userFundMainCash) || userFundMainCash.getBalance() <= 0) {
			logger.error("用户现金主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		// TODO 递延冻结扣款
		FundMainCash cash = new FundMainCash();
		cash.setUserId(userFundMainCash.getUserId());
		//当前时间
		Date currentTime = new Date();
		double userBalance = userFundMainCash.getBalance();
		//=============增加冻结用户递延保证金明细=============
		//用户余额 = 用户余额  - 递延保证金
		userBalance = DoubleTools.sub(userBalance , deferFund);
		//增加冻结用户递延保证金明细
		FundFlow deferFundFlow = new FundFlow(productName, FundCashOptCodeEnum.FREEZE_DEFERFUND, 
				userId, deferFund, userBalance, orderId, currentTime, currentTime);
		//用户冻结递延保证金    
		cash.setDeferFund(deferFund); 
		//余额减少
		cash.setBalance(-deferFund);
		//添加现金流水明细
		fundFlowCashDao.addFundFlowCash(deferFundFlow);
		//更新用户主账户
		fundMainCashDao.updateFundMainCash(cash);
		return true;
	}
	
	@Override
	public boolean doDeferFundRefund(double deferFund, String orderId, String userId,String productName) throws LTException{
		// TODO 递延解冻退款
		//查询用户现金账户信息
		logger.debug("查询userId:{}现金主账户信息", userId);
		//查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
		if(userFundMainCash==null) {
			logger.error("用户现金主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		FundMainCash cash = new FundMainCash();
		cash.setUserId(userFundMainCash.getUserId());
		//当前时间
		Date currentTime = new Date();
		//用户现金账户余额
		double userBalance = userFundMainCash.getBalance();
		//============= 增加解冻递延保证金流水=============
		//冻结递延保证金
		//用户余额 = 用户余额  + 解冻递延保证金
		userBalance = DoubleTools.add(userBalance , deferFund);
		//增加用户解冻递延保证金明细
		FundFlow deferFundFlow = new FundFlow(productName, FundCashOptCodeEnum.UNFREEZE_DEFERFUND, 
				userId, deferFund, userBalance, orderId, currentTime, currentTime);
		//退回用户冻结递延保证金
		cash.setDeferFund(-deferFund);
		//余额增加
		cash.setBalance(deferFund);
		//添加现金流水明细
		fundFlowCashDao.addFundFlowCash(deferFundFlow);
		
		//更新用户主账户
		fundMainCashDao.updateFundMainCash(cash);
		return true;
	}

	@Override
	public Double findFreezeFundFlowCashSum(String orderId,
			String userId,List<String> list) {
		return fundFlowCashDao.queryFundFlowCashSum(orderId, userId,list);
	}

	/**
	 * 冻结现金用户止盈止损
	 * @param userId
	 * @return
	 */
	@Override
	public boolean doFreezeProfitLoss(String userId,double holdFund,String orderId,String productName)throws LTException{
		//查询券商现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
		logger.debug("查询userId:{}现金主账户信息", userId);
		if(userFundMainCash==null  || userFundMainCash.getBalance() <= 0) {
			logger.error("用户现金主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		//当前时间
		Date currentTime = new Date();
		//资金流水
		List<FundFlow> flowList = new ArrayList<FundFlow>();
		//====================用户扣保证金====================
		if(holdFund > 0 ){
			FundMainCash cash = new FundMainCash();
			cash.setUserId(userFundMainCash.getUserId());
			//用户现金账户余额
			double userBalance = userFundMainCash.getBalance();
			//用户余额 = 用户余额  - 止损保证金
			userBalance = DoubleTools.sub(userBalance , holdFund);
			//增加用户止损保证金明细
			FundFlow holdFundFlow = new FundFlow(productName, FundCashOptCodeEnum.FREEZE_HOLDFUND, 
					userId, holdFund, userBalance, orderId, currentTime, currentTime);
			flowList.add(holdFundFlow);
			//增加用户主账户止损保证金
			cash.setHoldFund(holdFund);
			cash.setBalance(-holdFund);
			//更新用户主账户
			fundMainCashDao.updateFundMainCash(cash);
		}
		//添加现金流水明细
		fundFlowCashDao.addFundFlowCashList(flowList);
		
		return true;
	}
	
	
	/**
	 * 解冻现金用户止盈止损
	 * @param userId
	 * @return
	 */
	@Override
	public boolean doUnfreezeProfitLoss(String userId,double holdFund,String orderId,String productName)throws LTException{
		//查询券商现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
		logger.debug("查询userId:{}现金主账户信息", userId);
		if(userFundMainCash==null) {
			logger.error("用户现金主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		//当前时间
		Date currentTime = new Date();
		//资金流水
		List<FundFlow> flowList = new ArrayList<FundFlow>();
		//====================用户退保证金====================
		if(holdFund > 0 ){
			FundMainCash cash = new FundMainCash();
			cash.setUserId(userFundMainCash.getUserId());
			//用户现金账户余额
			double userBalance = userFundMainCash.getBalance();
			//用户余额 = 用户余额  + 止损保证金
			userBalance = DoubleTools.add(userBalance , holdFund);
			//增加用户解冻止损保证金明细
			FundFlow holdFundFlow = new FundFlow(productName, FundCashOptCodeEnum.UNFREEZE_HOLDFUND, 
					userId, holdFund, userBalance, orderId, currentTime, currentTime);
			flowList.add(holdFundFlow);
			//减少用户主账户止损保证金
			cash.setHoldFund(-holdFund);
			cash.setBalance(holdFund);
			//更新用户主账户
			fundMainCashDao.updateFundMainCash(cash);
		}
		//添加现金流水明细
		fundFlowCashDao.addFundFlowCashList(flowList);
		
		
		return true;
	}

	@Override
	public List<FundFlowVo> findFundFollowByUserId(Map<String, Object> map) {
		return fundFlowCashDao.findFundFollowByUserId(map);
	}

	@Override
	public List<FundFlowVo> findFundFollowByOrder(Map<String, Object> map) {
		return fundFlowCashDao.findFundFlowListByOrder(map);
	}

	@Override
	public Double getPingAnRechargeAmtInYear(String userId) {
		Double yearTotalAmt = fundFlowCashDao.getPingAnRechargeAmt(userId);
		return yearTotalAmt == null ? 0.0 : yearTotalAmt ;
	}

	@Override
	public Double getUserDailyRestReChargeAmt(String userId) {
		Double amt = fundFlowCashDao.getUserDailyRechargeTopAmt(userId);
		return amt == null ? 0.0: amt;
	}

	@Override
	public Double getHoldFundByOrderId(String orderId) {
		String amount = fundFlowCashDao.selectHoldFundByOrderId(orderId);
		return DoubleTools.scaleFormat(Double.valueOf(amount));
	}

	@Override
	public List<FundIoCashRecharge> findRechargeByUserId(Map<String, Object> params) {
		return fundIoCashRechargeDao.findRechargeByUserId(params);
	}
}
