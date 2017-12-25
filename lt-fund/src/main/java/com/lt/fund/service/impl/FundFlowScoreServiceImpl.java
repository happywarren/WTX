package com.lt.fund.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.fund.FundScoreOptCodeEnum;
import com.lt.fund.dao.FundFlowScoreDao;
import com.lt.fund.dao.FundMainScoreDao;
import com.lt.fund.service.IFundFlowScoreService;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundMainScore;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.vo.fund.FundFlowVo;

@Service
public class FundFlowScoreServiceImpl implements IFundFlowScoreService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FundFlowScoreDao fundFlowScoreDao;
	@Autowired
	private FundMainScoreDao fundMainScoreDao;
	
	@Override
	public boolean doDeferFundDeductions(FundMainScore userFundMainScore,
			double deferFund, String orderId, String userId, String productName) {
		//查询用户积分账户信息
		if(!StringTools.isNotEmpty(userFundMainScore) || userFundMainScore.getBalance() <= 0) {
			logger.error("用户积分主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		// TODO 递延冻结扣款
		FundMainScore score = new FundMainScore();
		score.setUserId(userFundMainScore.getUserId());
		//当前时间
		Date currentTime = new Date();
		double userBalance = userFundMainScore.getBalance();
		//=============增加解冻用户递延保证金明细=============
		//用户余额 = 用户余额  - 递延保证金
		userBalance = DoubleTools.sub(userBalance , deferFund);
		//增加解冻用户递延保证金明细
		FundFlow deferFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.FREEZE_DEFERFUND, 
				userId, deferFund, userBalance, orderId, currentTime, currentTime);
		//用户冻结递延保证金    
		score.setDeferFund(deferFund); 
		//余额减少
		score.setBalance(-deferFund);
		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScore(deferFundFlow);
		//更新用户主账户
		fundMainScoreDao.updateFundMainScore(score);
		return true;
	}
	@Override
	public boolean doDeferFundRefund(FundMainScore userFundMainScore,
			double deferFund, String orderId, String userId, String productName) {
		// TODO 递延解冻退款
		//查询用户积分账户信息
		logger.debug("查询userId:{}积分主账户信息", userId);
		//查询用户积分账户信息
		if(!StringTools.isNotEmpty(userFundMainScore)) {
			logger.error("用户积分主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		FundMainScore score = new FundMainScore();
		score.setUserId(userFundMainScore.getUserId());
		//当前时间
		Date currentTime = new Date();
		//用户积分账户余额
		double userBalance = userFundMainScore.getBalance();
		//============= 增加解冻递延保证金流水=============
		//冻结递延保证金
		//用户余额 = 用户余额  + 解冻递延保证金
		userBalance = DoubleTools.add(userBalance , deferFund);
		//增加用户解冻递延保证金明细
		FundFlow deferFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.UNFREEZE_DEFERFUND, 
				userId, deferFund, userBalance, orderId, currentTime, currentTime);
		//退回用户冻结递延保证金
		score.setDeferFund(-deferFund);
		//余额增加
		score.setBalance(deferFund);
		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScore(deferFundFlow);
		
		//更新用户主账户
		fundMainScoreDao.updateFundMainScore(score);
		return true;
	}


	@Override
	public Double findFreezeFundFlowScoreSum(String orderId,
			String userId,List<String> list) {
		return fundFlowScoreDao.queryFundFlowScoreSum(orderId, userId,list);
	}

	/**
	 * 冻结积分用户止盈止损
	 * @param userId
	 * @return
	 */
	@Override
	public boolean doFreezeProfitLoss(FundMainScore userFundMainScore,
			String investorId, String userId, double holdFund,
			double stopProfit, String orderId, String productName) {
		// TODO Auto-generated method stub
		//查询券商积分账户信息
		logger.debug("查询userId:{}积分主账户信息", userId);
		if(!StringTools.isNotEmpty(userFundMainScore) || userFundMainScore.getBalance() <= 0) {
			logger.error("用户积分主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		//当前时间
		Date currentTime = new Date();
		//资金流水
		List<FundFlow> flowList = new ArrayList<FundFlow>();
		//====================用户扣保证金====================
		if(holdFund > 0 ){
			FundMainScore score = new FundMainScore();
			score.setUserId(userFundMainScore.getUserId());
			//用户积分账户余额
			double userBalance = userFundMainScore.getBalance();
			//用户余额 = 用户余额  - 止损保证金
			userBalance = DoubleTools.sub(userBalance , holdFund);
			//增加用户止损保证金明细
			FundFlow holdFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.FREEZE_HOLDFUND, 
					userId, holdFund, userBalance, orderId, currentTime, currentTime);
			flowList.add(holdFundFlow);
			//增加用户主账户止损保证金
			score.setHoldFund(holdFund);
			score.setBalance(-holdFund);
			//更新用户主账户
			fundMainScoreDao.updateFundMainScore(score);
		}
		//添加积分流水明细
		fundFlowScoreDao.addFundFlowScoreList(flowList);
		
		return true;
	}
	
	
	/**
	 * 解冻积分用户止盈止损
	 * @param userId
	 * @return
	 */
	@Override
	public boolean doUnfreezeProfitLoss(FundMainScore userFundMainScore,
			String investorId, String userId, double holdFund,
			double stopProfit, String orderId, String productName) {
		// TODO Auto-generated method stub
		//查询券商积分账户信息
		logger.debug("查询userId:{}积分主账户信息", userId);
		if(!StringTools.isNotEmpty(userFundMainScore)) {
			logger.error("用户积分主账户未初始化或账户余额是0!");
			throw new LTException(LTResponseCode.FU00007);
		}
		//当前时间
		Date currentTime = new Date();
		//资金流水
		List<FundFlow> flowList = new ArrayList<FundFlow>();
		//====================用户退保证金====================
		if(holdFund > 0 ){
			FundMainScore score = new FundMainScore();
			score.setUserId(userFundMainScore.getUserId());
			//用户积分账户余额
			double userBalance = userFundMainScore.getBalance();
			//用户余额 = 用户余额  + 止损保证金
			userBalance = DoubleTools.add(userBalance , holdFund);
			//增加用户解冻止损保证金明细
			FundFlow holdFundFlow = new FundFlow(productName, FundScoreOptCodeEnum.UNFREEZE_HOLDFUND, 
					userId, holdFund, userBalance, orderId, currentTime, currentTime);
			flowList.add(holdFundFlow);
			//减少用户主账户止损保证金
			score.setHoldFund(-holdFund);
			score.setBalance(holdFund);
			//更新用户主账户
			fundMainScoreDao.updateFundMainScore(score);
		}
		//添加积分流水明细
		logger.debug("flowList:{}",JSONObject.toJSONString(flowList));
		fundFlowScoreDao.addFundFlowScoreList(flowList);
		return true;
	}

	@Override
	public List<FundFlowVo> findFundFollowByUserId(Map<String, Object> map) {
		return fundFlowScoreDao.findFundFollowByUserId(map);
	}


	@Override
	public List<FundFlowVo> findFundFollowByOrder(Map<String, Object> map) {
		return fundFlowScoreDao.findFundFlowListByOrder(map);
	}
	

	
	
	
}
