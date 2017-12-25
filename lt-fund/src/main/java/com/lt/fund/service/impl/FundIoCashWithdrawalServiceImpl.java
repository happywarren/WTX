package com.lt.fund.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundIoWithdrawalEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.fund.dao.FundFlowCashDao;
import com.lt.fund.dao.FundIoCashWithdrawalDao;
import com.lt.fund.dao.FundMainCashDao;
import com.lt.fund.dao.FundOptCodeDao;
import com.lt.fund.service.IFundIoCashWithdrawalService;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.UserContant;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.redis.RedisInfoOperate;

@Service
public class FundIoCashWithdrawalServiceImpl implements
		IFundIoCashWithdrawalService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FundMainCashDao fundMainCashDao;

	@Autowired
	private FundIoCashWithdrawalDao fundIoCashWithdrawalDao;

	@Autowired
	private FundOptCodeDao fundOptCodeDao;

	@Autowired
	private FundFlowCashDao fundFlowCashDao;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 申请提现
	 * 
	 * @param userId
	 *            用户id
	 * @param outAmount
	 *            提现金额
	 * @param flag
	 *            是否需要扣手续费
	 */
	@Override
	@Transactional
	public FundIoCashWithdrawal doRequestWithdraw(String userId, Double outAmount,Double rate,
											String withdrawCode,String faxCode, boolean flag, boolean backGroundFlag) throws LTException{
		FundOptCode fundOptCode = getFundOptCode(null,null,withdrawCode);
		if (fundOptCode == null) {
			throw new LTException(LTResponseCode.FUJ0001);
		}
		FundOptCode faxOptcode = getFundOptCode(null,null,faxCode);//手续费配置
		if (faxOptcode == null) {
			throw new LTException(LTResponseCode.FUJ0001);
		}
		// 查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao
				.queryFundMainCashForUpdate(userId);
		logger.debug("查询userId:{}现金主账户信息", userId);
		if (userFundMainCash == null) {
			throw new LTException("用户现金主账户未初始化!");
		}
		FundMainCash cash = new FundMainCash();
		cash.setUserId(userId);
		double amt  = userFundMainCash.getBalance() == null ? 0.0:userFundMainCash.getBalance();
		double residue = DoubleTools.sub(amt , outAmount);
		if (residue < 0) {
			throw new LTException("用户现金主账户余额不足!");
		}
		cash.setBalance(-outAmount);
		cash.setFreezeAmount(outAmount);
		// 更新用户主账户
		fundMainCashDao.updateFundMainCash(cash);
		// TODO
		// 手续费规则收取
		double tax = DoubleTools.mul(outAmount , 0.02) < 2 ? 2 : DoubleTools.mul(outAmount , 0.02);
		double rmb_tax = DoubleUtils.scaleFormatEnd(DoubleUtils.div(tax , rate),2);
		double factTax = 0;
		double factRmbTax = 0 ;
		if (flag) {
			factTax = tax;
			factRmbTax = rmb_tax ;
		}
		// 提现申请记录
		Double rmbAmt = DoubleUtils.scaleFormatEnd(DoubleUtils.div(outAmount , rate),2);
		logger.info("rate={},outAmount={},rmbAmt={},减法：{}",rate,outAmount,rmbAmt,DoubleTools.sub(factTax, outAmount));
		FundIoCashWithdrawal fundIoCashWithdrawal = new FundIoCashWithdrawal(
				userId, fundOptCode, outAmount, residue, tax, factTax,rmb_tax,factRmbTax,null,fundOptCode.getRemark(),rmbAmt,rate);
		fundIoCashWithdrawal.setCreateDate(new Date());
		if(backGroundFlag){
			fundIoCashWithdrawal.setBackgroundFlag(1);
		}
		fundIoCashWithdrawalDao.addFundIoCashWithdrawal(fundIoCashWithdrawal);
		
		//生成提现流水
		FundFlow fundFlow = new FundFlow(userId, fundOptCode.getFlowType(), fundOptCode.getFirstOptCode(), 
				fundOptCode.getSecondOptCode(), fundOptCode.getThirdOptCode(), DoubleTools.sub(factTax, outAmount)  ,
				DoubleTools.add(residue , factTax), StringTools.formatStr(fundIoCashWithdrawal.getId(), "-1"), fundOptCode.getRemark(), new Date(), new Date());
		fundFlowCashDao.addFundFlowCash(fundFlow);
		
		if(factTax != 0){
			//生成提现手续费流水
			FundFlow feeFlow = new FundFlow(userId, faxOptcode.getFlowType(), faxOptcode.getFirstOptCode(), 
					faxOptcode.getSecondOptCode(), faxOptcode.getThirdOptCode(), -factTax,
					residue , StringTools.formatStr(fundIoCashWithdrawal.getId(), "-1"), faxOptcode.getRemark(), new Date(), new Date());
			fundFlowCashDao.addFundFlowCash(feeFlow);
		}
		
		
		//生成提现手续费流水
		return fundIoCashWithdrawal;
	}

	/**
	 * 获取业务码
	 * @param firstOptCode
	 * @param secondOptCode
	 * @param thirdOptCode
	 * @return
	 */
	private FundOptCode getFundOptCode(String firstOptCode,String secondOptCode,String thirdOptCode) throws LTException{
		FundOptCode foc = new FundOptCode();
		foc.setFirstOptCode(firstOptCode);
		foc.setSecondOptCode(secondOptCode);
		foc.setThirdOptCode(thirdOptCode);
		List<FundOptCode> focs = fundOptCodeDao.selectFundOptCodes(foc);
		if(focs == null || focs.size() == 0){
			throw new LTException(LTResponseCode.FUJ0001);
		}
		return focs.get(0);
	}

	/**
	 * 提现审核 ( 0 待审核, 1 待转账, 2 提现拒绝, 3 转账中, 4 转账失败, 5 转账成功, 6 提现撤销',)
	 * 
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 */
	@Override
	public void doAudit(Long id, String modifyUserId, Integer status,
			IFundOptCode thirdOptCode,Double fact_fax, String remark) {

		FundIoCashWithdrawal info = fundIoCashWithdrawalDao.queryFundIoCashWithdrawalById(id);
		if(null == info ){
			throw new LTException("没有提现记录");
		}
		Date date = null;
		
		FundMainCash userFundMainCash = fundMainCashDao.queryFundMainCashForUpdate(info.getUserId());
		// 提现拒绝  提现撤销
		if (status == FundIoWithdrawalEnum.REPULSE.getValue() || status == FundIoWithdrawalEnum.REVOCATION.getValue()){
			
			if(info.getStatus() != FundIoWithdrawalEnum.AUDIT.getValue()){
				throw new LTException("订单已被处理");
			}
			
			// 查询用户现金账户信息
			FundMainCash cash = new FundMainCash();
			cash.setUserId(userFundMainCash.getUserId());
			
			String secondOptCode = status == FundIoWithdrawalEnum.REPULSE.getValue() ? FundThirdOptCodeEnum.WITHDRAW_REJECT.getSecondCode()
					: FundThirdOptCodeEnum.WITHDRAW_CANCLE.getSecondCode();
			
			String thirdCode = status == FundIoWithdrawalEnum.REPULSE.getValue() ? FundThirdOptCodeEnum.WITHDRAW_REJECT.getThirdLevelCode()
					: FundThirdOptCodeEnum.WITHDRAW_CANCLE.getThirdLevelCode();
			
			remark = status == FundIoWithdrawalEnum.REPULSE.getValue() ? remark
					: FundThirdOptCodeEnum.WITHDRAW_CANCLE.getThirdLevelname();
			//生成提现退回流水
			FundFlow feeFlow = new FundFlow(info.getUserId(), FundFlowTypeEnum.INCOME.getValue(), FundCashOptCodeEnum.CANCLE_WITHDRAW.getFirstLevelCode(),
					secondOptCode, thirdCode, info.getAmount(), userFundMainCash.getBalance()+info.getAmount(), info.getId().toString(), remark, new Date(), new Date());
			logger.debug("插入现金流");
			fundFlowCashDao.addFundFlowCash(feeFlow);
			//退回余额    冻结提款金额减去本次提现金额
			cash.setBalance(info.getAmount());
			cash.setFreezeAmount(-info.getAmount());
			// 更新用户主账户
			logger.debug("修改资金余额");
			fundMainCashDao.updateFundMainCash(cash);
		}
		
		// 转账拒绝
		if(status == FundIoWithdrawalEnum.TREPULSE.getValue()){
			
			if(info.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()){
				throw new LTException("订单已被处理");
			}
				
			FundOptCode fundOptCode = getFundOptCode(null,null,FundThirdOptCodeEnum.WITHDRAW_AMT.getThirdLevelCode());
			logger.info("原始充值对象为：{}",JSONObject.toJSONString(info));
			FundIoCashWithdrawal fundIoCashWithdrawal = new FundIoCashWithdrawal(
					info.getUserId(), fundOptCode, info.getAmount(), userFundMainCash.getBalance(), info.getTax(), info.getFactTax(), info.getRmbTax(), info.getRmbFactTax(),null,fundOptCode.getRemark(),info.getRmbAmt(),info.getRate());
			fundIoCashWithdrawal.setCreateDate(info.getCreateDate());
			fundIoCashWithdrawal.setModifyUserId(modifyUserId);
			logger.info("拼接后充值对象为：{}",JSONObject.toJSONString(fundIoCashWithdrawal));
			fundIoCashWithdrawalDao.addFundIoCashWithdrawal(fundIoCashWithdrawal);
		}
		
		// 转账成功
		if (status == FundIoWithdrawalEnum.SUCCEED.getValue()) {
			
			if(info.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()){
				throw new LTException("订单已被处理");
			}
			
			date = new Date();
			// 查询用户现金账户信息
			FundMainCash cash = new FundMainCash();
			cash.setUserId(userFundMainCash.getUserId());
			//冻结提款金额减去本次提现金额
			cash.setFreezeAmount(-info.getAmount());
			// 更新用户主账户
			fundMainCashDao.updateFundMainCash(cash);
		}
		
		/** 处理提现记录表 beg*/
		FundIoCashWithdrawal cashWithdrawal = new FundIoCashWithdrawal();
		cashWithdrawal.setId(id);
		cashWithdrawal.setStatus(status);
		if(remark != null){
			cashWithdrawal.setRemark(remark);
		}
		
		if(status == FundIoWithdrawalEnum.WAIT.getValue()){
			
			if(info.getStatus() != FundIoWithdrawalEnum.AUDIT.getValue()){
				throw new LTException("订单已被处理");
			}
			
			cashWithdrawal.setAuditDate(new Date());
			cashWithdrawal.setFactTax(fact_fax);
			if(fact_fax != null ){
				cashWithdrawal.setRmbFactTax(DoubleUtils.scaleFormatEnd(DoubleUtils.div(fact_fax , info.getRate()),2));
			}else{
				cashWithdrawal.setRmbFactTax(null);
			}
		}
		
		cashWithdrawal.setModifyUserId(modifyUserId+"");
		/** 处理提现记录表 end*/
		cashWithdrawal.setDoneDate(date);
		
		fundIoCashWithdrawalDao
				.updateFundIoCashWithdrawal(cashWithdrawal);
	}

	@Override
	public Map<String, String> doWithdrawApply(String userId, Double outAmount,Double rate,
							String withdrawCode,String faxCode,boolean flag,String sign)throws LTException {
		if(!RedisInfoOperate.setSuccess(redisTemplate, UserContant.USER_WITHDRAW_LOCK+":"+userId+sign, userId)){
			throw new LTException(LTResponseCode.US01112);
		}
		/**查询出金限制次数**/
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		String maxWithdrawCount = sysCfgRedis.get("max_withdraw_count");
		if(StringTools.isEmpty(maxWithdrawCount)){
			maxWithdrawCount = "2";
		}
		//是否后台出金标记，财务后台出金不做任何限制
		boolean backgroundFlag = StringTools.isNotEmpty(sign) && sign.contains("BACKGROUND");
		//提现金额不能小于20
		logger.info("outAmount:{}",outAmount);
		
		//if((outAmount == null || outAmount.compareTo(new Double(10)) < 0) && !backgroundFlag){
		//	throw new LTException(LTResponseCode.FUJ0002);
		//}
		//判断当日提现次数是否已超2次
		Integer count = fundIoCashWithdrawalDao.getTotayWithdrawCount(userId);
		if(count >= Integer.valueOf(maxWithdrawCount) && !backgroundFlag){
			throw new LTException(LTResponseCode.FUJ0006);
		}
		FundIoCashWithdrawal fio = doRequestWithdraw(userId, outAmount,rate,withdrawCode,faxCode, flag, backgroundFlag);
		
		logger.info("组装返回信息");
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", fio.getId() + "");
		map.put("inOutAmt", fio.getAmount() + "");
		map.put("factTax", fio.getFactTax() + "");
		map.put("factAmt", DoubleTools.sub(fio.getAmount(), fio.getFactTax()) + "");
		
		return map;
	}

	
	@Override
	public void withdrawSuccess(FundIoCashWithdrawal fio,String thirdOptCode) throws Exception {
		logger.info("=====用户提现成功，====更新冻结金额=======");
		// 查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao
				.queryFundMainCashForUpdate(fio.getUserId());
		FundMainCash cash = new FundMainCash();
		cash.setUserId(userFundMainCash.getUserId());
		cash.setFreezeAmount(-fio.getAmount());
		cash.setTotalDrawAmount(fio.getAmount());
		// 更新用户主账户
		fundMainCashDao.updateFundMainCash(cash);
		
		/**---------------------转账成功之后操作用户出金现金流显示 -----------------------beg**/
		List<FundFlow> flowList = fundFlowCashDao.qryFundCashFlowByExternId(fio.getId().toString());
		if(CollectionUtils.isNotEmpty(flowList) && thirdOptCode != null ){
			for(FundFlow fundFlow : flowList){
				if(fundFlow == null){
					continue ;
				}else{
					if(fundFlow.getThirdOptCode().equals(FundThirdOptCodeEnum.WITHDRAW_FEE.getThirdLevelCode())){
						continue ;
					}else{
						fundFlow.setThirdOptCode(thirdOptCode);
						FundOptCode  fundOptCode = getFundOptCode(null,null,thirdOptCode);
						fundFlow.setRemark(fundOptCode.getThirdOptName());
						fundFlowCashDao.updateFundCashFlow(fundFlow);
					}
				}
			}
		}
		/**---------------------转账成功之后操作用户出金现金流显示 -----------------------end**/
		
		logger.info("======用户提现成功更新冻结金额===结束=======");
	}

	@Override
	public void withdrawFail(FundIoCashWithdrawal fio) throws Exception {
		logger.info("=====用户提现失败，====更新账户余额和冻结金额=======");
		// 查询用户现金账户信息
		FundMainCash userFundMainCash = fundMainCashDao
				.queryFundMainCashForUpdate(fio.getUserId());
		FundMainCash cash = new FundMainCash();
		cash.setUserId(userFundMainCash.getUserId());
		FundFlow feeFlow = new FundFlow(fio.getUserId(), FundFlowTypeEnum.INCOME.getValue(), FundCashOptCodeEnum.REJECT_WITHDRAW.getFirstLevelCode(), FundCashOptCodeEnum.REJECT_WITHDRAW.getCode(),
				FundThirdOptCodeEnum.WITHDRAW_REJECT.getThirdLevelCode(), fio.getAmount(), DoubleTools.add(userFundMainCash.getBalance(),fio.getAmount()), fio.getId().toString(), fio.getRemark(), new Date(), new Date());
		fundFlowCashDao.addFundFlowCash(feeFlow);
		
		cash.setFreezeAmount(-fio.getAmount());
		cash.setBalance(fio.getAmount());
		// 更新用户主账户
		fundMainCashDao.updateFundMainCash(cash);
		//生成提现流水（退回流水）
		logger.info("======用户提现失败更新账户余额和冻结金额===结束=======");
	}

	@Override
	public void WithdrawCancel(String userId, Long ioId) throws LTException {
		//获取出金流水
		FundIoCashWithdrawal io = fundIoCashWithdrawalDao.queryFundIoCashWithdrawalById(ioId);
		if(io == null){
			throw new LTException(LTResponseCode.FUJ0004);
		}
		//出金状态不为待审核，也不为待转账 则不允许撤销
		if(io.getStatus() != FundIoWithdrawalEnum.AUDIT.getValue() && io.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()){
			throw new LTException(LTResponseCode.FUJ0005);
		}
		
		this.doAudit(ioId, userId, FundIoWithdrawalEnum.REVOCATION.getValue(),null,null, "出金撤销");
	}

}
