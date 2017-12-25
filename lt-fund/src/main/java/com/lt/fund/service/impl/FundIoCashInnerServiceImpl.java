package com.lt.fund.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundIoCashInnerEnum;
import com.lt.enums.fund.FundScoreOptCodeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.fund.dao.FundFlowCashDao;
import com.lt.fund.dao.FundFlowScoreDao;
import com.lt.fund.dao.FundIoCashInnerlDao;
import com.lt.fund.dao.FundMainCashDao;
import com.lt.fund.dao.FundMainScoreDao;
import com.lt.fund.dao.FundOptCodeDao;
import com.lt.fund.service.IFundIoCashInnerService;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundIoCashInner;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundMainScore;
import com.lt.model.fund.FundOptCode;
import com.lt.util.error.LTException;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;

@Service
public class FundIoCashInnerServiceImpl implements IFundIoCashInnerService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FundMainCashDao fundMainCashDao;

	@Autowired
	private FundOptCodeDao fundOptCodeDao;

	@Autowired
	private FundFlowCashDao fundFlowCashDao;

	@Autowired
	private FundIoCashInnerlDao fundIoCashInnerlDao;

	@Autowired
	private FundMainScoreDao fundMainScoreDao;

	@Autowired
	private FundFlowScoreDao fundFlowScoreDao;

	@Override
	public void doManualInByCash(String orderId,String userId, Double freezeAmount, String thirdOptCode, 
			String remark,Double rmbAmount,Integer modifyUserId) {
		// Date currentTime = new Date();
		FundOptCode fundOptCode = fundOptCodeDao.queryFundOptCodeByCode(null, null, thirdOptCode);
		if (fundOptCode == null) {
			throw new LTException("没有对应的资金流转方式!");
		}
		// 查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
		logger.debug("查询userId:{}现金主账户信息", userId);
		if (userFundMainCash == null) {
			throw new LTException("用户现金主账户未初始化!");
		}
		// 内部存取申请记录
		logger.info("freezeAmount:{},rmbAmount:{}",freezeAmount,rmbAmount);
		FundIoCashInner fundIoCashInnerl = new FundIoCashInner(userId, fundOptCode, freezeAmount,
				userFundMainCash.getBalance(), remark);
		fundIoCashInnerl.setRmbAmt(rmbAmount);
		fundIoCashInnerl.setOrderId(orderId);
		fundIoCashInnerl.setModifyUserId(modifyUserId);
		logger.info("fundIoCashInnerl:{}",JSONObject.toJSONString(fundIoCashInnerl));
		fundIoCashInnerlDao.addFundIoCashInnerl(fundIoCashInnerl);
	}

	@Override
	public void doManualOutCash(String orderId,String userId, Double freezeAmount, String thirdOptCode, String remark,Double rmbAmount,Integer modifyUserId) {
		Date currentTime = new Date();
		FundOptCode fundOptCode = fundOptCodeDao.queryFundOptCodeByCode(null, null, thirdOptCode);
		if (fundOptCode == null) {
			throw new LTException("没有对应的资金流转方式!");
		}
		// 查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
		logger.debug("查询userId:{}现金主账户信息", userId);
		if (userFundMainCash == null) {
			throw new LTException("用户现金主账户未初始化!");
		}
		FundMainCash cash = new FundMainCash();
		cash.setUserId(userFundMainCash.getUserId());
		double residue = DoubleTools.sub(userFundMainCash.getBalance() , freezeAmount);
		if (residue < 0) {
			throw new LTException("用户现金主账户余额不足!");
		}
		cash.setBalance(-freezeAmount);
		cash.setFreezeAmount(freezeAmount);

		// 更新用户主账户
		fundMainCashDao.updateFundMainCash(cash);
		// 内部存取申请记录
		FundIoCashInner FundIoCashInnerl = new FundIoCashInner(userId, fundOptCode, freezeAmount,
				userFundMainCash.getBalance(), remark);
		FundIoCashInnerl.setRmbAmt(rmbAmount);
		FundIoCashInnerl.setOrderId(orderId);
		FundIoCashInnerl.setModifyUserId(modifyUserId);
		Integer id = fundIoCashInnerlDao.addFundIoCashInnerl(FundIoCashInnerl);

		// 内部取出记录
		FundFlow feeFlow = new FundFlow("", FundCashOptCodeEnum.MANUALOUT,thirdOptCode, userId, freezeAmount, residue, id.toString(),
				currentTime, currentTime);
		// 添加现金流水明细
		fundFlowCashDao.addFundFlowCash(feeFlow);
	}

	@Override
	public void doManualInByScore(String orderId,String userId, Double freezeAmount, String thirdOptCode, String remark,Integer modifyUserId) {
		FundOptCode fundOptCode = fundOptCodeDao.queryFundOptCodeByCode(null, null, thirdOptCode);
		if (fundOptCode == null) {
			throw new LTException("没有对应的资金流转方式!");
		}
		// 查询用户积分账户信息
		FundMainScore userFundMainScore = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		logger.debug("查询userId:{}积分主账户信息", userId);
		if (userFundMainScore == null) {
			throw new LTException("用户积分主账户未初始化!");
		}

		// 内部存取申请记录
		FundIoCashInner FundIoCashInnerl = new FundIoCashInner(userId, fundOptCode, freezeAmount,
				userFundMainScore.getBalance(), remark);
		FundIoCashInnerl.setOrderId(orderId);
		FundIoCashInnerl.setModifyUserId(modifyUserId);
		fundIoCashInnerlDao.addFundIoScoreInnerl(FundIoCashInnerl);

	}

	@Override
	public void doManualOutScore(String orderId,String userId, double freezeAmount, String thirdOptCode, String remark,Integer modifyUserId) {
		Date currentTime = new Date();
		FundOptCode fundOptCode = fundOptCodeDao.queryFundOptCodeByCode(null, null, thirdOptCode);
		if (fundOptCode == null) {
			throw new LTException("没有对应的资金流转方式!");
		}
		// 查询用户积分账户信息
		FundMainScore userFundMainScore = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		logger.debug("查询userId:{}积分主账户信息", userId);
		if (userFundMainScore == null) {
			throw new LTException("用户积分主账户未初始化!");
		}
		FundMainScore score = new FundMainScore();
		score.setUserId(userId);
		double residue = DoubleTools.sub(userFundMainScore.getBalance() , freezeAmount);
		if (residue < 0) {
			throw new LTException("用户积分主账户余额不足!");
		}
		score.setBalance(-freezeAmount);

		// 后台内部取出记录
		FundIoCashInner fundIoCashInnerl = new FundIoCashInner(userId, fundOptCode, freezeAmount, residue, remark);
		fundIoCashInnerl.setOrderId(orderId);
		fundIoCashInnerl.setModifyUserId(modifyUserId);
		// 更新用户主账户
		fundMainScoreDao.updateFundMainScore(score);
		// 新增记录
		Integer id = fundIoCashInnerlDao.addFundIoScoreInnerl(fundIoCashInnerl);

		// 用户内部取出流水
		FundFlow feeFlow = new FundFlow("", FundScoreOptCodeEnum.MANUALOUT,thirdOptCode, userId, freezeAmount, residue,
				id.toString(), currentTime, currentTime);
		// 添加现金流水明细
		fundFlowScoreDao.addFundFlowScore(feeFlow);

	}

	/**
	 * 内部存取审核 ( 0 待审核, 1 已审核, 2已拒绝)
	 * 
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 */
	@Override
	public void doCashAudit(Long id, Integer status, String remark,Integer modifyUserId) {
		FundIoCashInner info = fundIoCashInnerlDao.queryFundIoCashInnerlById(id);
		if (null == info) {
			throw new LTException("没有内部存取记录");
		}
		String userId = info.getUserId();

		// 查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(userId);
		FundMainCash fundCashUpt = new FundMainCash();
		// 内部存取成功
		if (status == FundIoCashInnerEnum.SUCCESS.getValue()) {
			fundCashUpt.setUserId(userFundMainCash.getUserId());

			if (info.getSecondOptCode().equals(FundCashOptCodeEnum.MANUALIN.getCode()) 
					|| info.getSecondOptCode().equals(FundCashOptCodeEnum.RECHARGENOTE.getCode())) {
				logger.debug("成功存入的话增加对应的余额");
				fundCashUpt.setBalance(info.getAmount());

				Date currentTime = new Date();
				FundOptCode fundOptCode = fundOptCodeDao.queryFundOptCodeByCode(null, null, info.getThirdOptCode());
				FundFlow feeFlow = null;
				if(info.getSecondOptCode().equals(FundCashOptCodeEnum.RECHARGENOTE.getCode())){
					feeFlow = new FundFlow("", FundCashOptCodeEnum.RECHARGENOTE,info.getThirdOptCode(), userId, info.getAmount(),
							DoubleTools.add(userFundMainCash.getBalance() , info.getAmount()), info.getId().toString(), currentTime, currentTime);
				}else{
					feeFlow = new FundFlow("", FundCashOptCodeEnum.MANUALIN,info.getThirdOptCode(), userId, info.getAmount(),
							DoubleTools.add(userFundMainCash.getBalance() , info.getAmount()), info.getId().toString(), currentTime, currentTime);
				}
				feeFlow.setRemark(fundOptCode.getThirdOptName());
				// 添加现金流水明细
				fundFlowCashDao.addFundFlowCash(feeFlow);
				
				fundCashUpt.setTotalManualinAmount(info.getAmount());
			} else {
				logger.debug("成功取出的话扣除相应的冻结金额");
				fundCashUpt.setFreezeAmount(-info.getAmount());
				fundCashUpt.setTotalManualoutAmount(info.getAmount());
			}
			// 更新用户主账户
			fundMainCashDao.updateFundMainCash(fundCashUpt);
		} else {
			// 生成内部存取退回流水
			FundFlow feeFlow = null;
			if (info.getSecondOptCode().equals(FundCashOptCodeEnum.MANUALOUT.getCode())) {
				feeFlow = new FundFlow("", FundCashOptCodeEnum.MANUALIN,FundThirdOptCodeEnum.MANUALOUT_REJECT.getThirdLevelCode(), userId, info.getAmount(),
						DoubleTools.add(userFundMainCash.getBalance() , info.getAmount()), info.getId().toString(), new Date(), new Date());
				fundCashUpt.setUserId(userFundMainCash.getUserId());
				fundCashUpt.setFreezeAmount(-info.getAmount());
				fundCashUpt.setBalance(info.getAmount());
				fundMainCashDao.updateFundMainCash(fundCashUpt);
				fundFlowCashDao.addFundFlowCash(feeFlow);
			}
		}
		// 内部存取记录 状态更改
		info.setRemark(remark);
		info.setStatus(status);
		info.setModifyUserId(modifyUserId);
		info.setDoneDate(new Date());
		info.setAuditDate(new Date());
		fundIoCashInnerlDao.updateFundIoCashInnerl(info);
	}

	/**
	 * 内部存取审核 ( 0 待审核, 1 已审核, 2已拒绝)
	 * 
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 */
	@Override
	public void doScoreAudit(Long id, Integer status, String remark,Integer modifyUserId) {
		FundIoCashInner info = fundIoCashInnerlDao.queryFundIoScoreInnerlById(id);
		if (null == info) {
			throw new LTException("没有内部存取记录");
		}
		String userId = info.getUserId();

		// 查询用户现金账户信息
		FundMainScore userFundMainCash = fundMainScoreDao.queryFundMainScoreForUpdate(userId);
		FundMainScore fundCashUpt = new FundMainScore();
		// 内部存取成功
		if (status == FundIoCashInnerEnum.SUCCESS.getValue()) {
			fundCashUpt.setUserId(userFundMainCash.getUserId());

			if (info.getSecondOptCode().equals(FundScoreOptCodeEnum.MANUALIN.getCode())) {
				logger.debug("成功存入的话增加对应的余额");
				fundCashUpt.setBalance(info.getAmount());
				FundOptCode fundOptCode = fundOptCodeDao.queryFundOptCodeByCode(null, null, info.getThirdOptCode());
				FundFlow feeFlow = new FundFlow("", FundScoreOptCodeEnum.MANUALIN,info.getThirdOptCode(), userId, info.getAmount(),
						DoubleTools.add(userFundMainCash.getBalance() , info.getAmount()), null, new Date(), new Date());
				feeFlow.setRemark(fundOptCode.getThirdOptName());
				fundFlowScoreDao.addFundFlowScore(feeFlow);
				
				fundCashUpt.setTotalManualinAmount(info.getAmount());
			}else{
				fundCashUpt.setTotalManualoutAmount(info.getAmount());
			}
			
			
			// 更新用户主账户
			fundMainScoreDao.updateFundMainScore(fundCashUpt);
		} else {
			// 生成内部存取退回流水
			FundFlow feeFlow = null;
			if (info.getSecondOptCode().equals(FundScoreOptCodeEnum.MANUALOUT.getCode())) {
				feeFlow = new FundFlow("", FundScoreOptCodeEnum.MANUALIN,FundThirdOptCodeEnum.MANUALOUT_REJECT.getThirdLevelCode(), userId, info.getAmount(),
						DoubleTools.add(userFundMainCash.getBalance() , info.getAmount()), null, new Date(), new Date());
				fundCashUpt.setUserId(userFundMainCash.getUserId());
				fundCashUpt.setBalance(info.getAmount());
				fundMainScoreDao.updateFundMainScore(fundCashUpt);
				fundFlowScoreDao.addFundFlowScore(feeFlow);
			}
		}
		// 内部存取记录 状态更改
		info.setRemark(remark);
		info.setStatus(status);
		info.setModifyUserId(modifyUserId);
		info.setDoneDate(new Date());
		info.setAuditDate(new Date());
		fundIoCashInnerlDao.updateFundIoScoreInnerl(info);
	}
}
