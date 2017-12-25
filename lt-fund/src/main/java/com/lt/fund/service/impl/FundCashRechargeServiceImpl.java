package com.lt.fund.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundIoRechargeEnum;
import com.lt.fund.dao.FundFlowCashDao;
import com.lt.fund.dao.FundIoCashRechargeDao;
import com.lt.fund.dao.FundMainCashDao;
import com.lt.fund.service.IFundCashRechargeService;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.charge.BankChargeDetail;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;

@Service
public class FundCashRechargeServiceImpl implements IFundCashRechargeService {

    private Logger logger = LoggerFactory.getLogger(FundCashServiceImpl.class);

    @Autowired
    private FundMainCashDao fundMainCashDao;
    @Autowired
    private FundFlowCashDao fundFlowCashDao;
    @Autowired
    private FundIoCashRechargeDao fundIoCashRechargeDao;
    @Autowired
	private MessageQueueProducer promoteProducer;

    @Override
    public boolean doSuccessRecharge(String payId, String externalId, Double realRmbAmount, FundOptCode fundOptCode) {

        logger.info("处理用户充值 {},{},{}", payId, externalId, realRmbAmount);

        if (StringTools.isEmpty(payId)) {
            logger.info("充值订单号为空");
            return false;
        }

        if (realRmbAmount.doubleValue() <= 0) {
            logger.info("充值订单 {} 实际RMB到账金额异常 {} ", payId, realRmbAmount);
            return false;
        }

        FundIoCashRecharge fundIoCashRecharge = fundIoCashRechargeDao.selectFundIoRechargeForUpdate(payId);

        if (!StringTools.isNotEmpty(fundIoCashRecharge)) {
            logger.info("充值记录 {} 不存在", payId);
            return false;
        }

        int status = fundIoCashRecharge.getStatus().intValue();
        if (status != FundIoRechargeEnum.AUDIT.getValue()) {
            logger.info("充值记录 {} 已处理，状态为 {} ", payId, status);
            throw new LTException(LTResponseCode.FUY00011);
        }

        double realAmount = fundIoCashRecharge.getAmount();
        if (realRmbAmount.doubleValue() != fundIoCashRecharge.getRmbAmt().doubleValue()) {
            logger.info("充值记录 {} 订单金额 {} 和返回金额 {} 不一致 ", payId, fundIoCashRecharge.getRmbAmt(), realRmbAmount);
            throw new LTException(LTResponseCode.FUY00014);
        }

        // 更改充值流水信息
        Date doneDate = new Date();
        fundIoCashRecharge.setStatus(FundIoRechargeEnum.SUCCESS.getValue());//成功
        fundIoCashRecharge.setActualAmount(realAmount);
        fundIoCashRecharge.setActualRmbAmount(realRmbAmount);
        fundIoCashRecharge.setDoneDate(doneDate);
        fundIoCashRechargeDao.updateFundIoRecharge(fundIoCashRecharge);

        FundMainCash fundMainCash = fundMainCashDao.queryFundMainCashForUpdate(fundIoCashRecharge.getUserId());

        String thirdOptCode = fundOptCode.getThirdOptCode();
        String fundIoId = fundIoCashRecharge.getId() + "";

        //判断资金流水
        FundFlow fundFlow = fundFlowCashDao.getFundFlow(fundIoId, thirdOptCode);
        if (StringTools.isNotEmpty(fundFlow)) {
            logger.info("充值记录 {} {} {} 已入账 ", payId, fundIoId, thirdOptCode);
            throw new LTException(LTResponseCode.FU00000);
        }

        //生成资金流水
        FundFlow FundFlow = new FundFlow(fundIoCashRecharge.getUserId(), FundFlowTypeEnum.INCOME.getValue(), fundOptCode.getFirstOptCode(),
                fundOptCode.getSecondOptCode(), fundOptCode.getThirdOptCode(), fundIoCashRecharge.getAmount(), fundMainCash.getBalance() + fundIoCashRecharge.getAmount(),
                fundIoCashRecharge.getId() + "", fundIoCashRecharge.getRemark(), new Date(), new Date());
        fundFlowCashDao.addFundFlowCash(FundFlow);

        BankChargeDetail bankChargeDetail = new BankChargeDetail(fundIoCashRecharge.getUserId(), fundIoCashRecharge.getThirdOptcode(), fundIoCashRecharge.getBankCode(), fundIoCashRecharge.getTransferNumber(), fundIoCashRecharge.getActualRmbAmount());

        BankChargeDetail queryBankChargeDetail = fundIoCashRechargeDao.qryBankChargeDetailByCondition(bankChargeDetail);
        if (queryBankChargeDetail == null) {
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
        mainCash.setUserId(fundIoCashRecharge.getUserId());
        mainCash.setBalance(fundIoCashRecharge.getAmount());
        mainCash.setTotalRechargeAmount(fundIoCashRecharge.getAmount());
        int i = fundMainCashDao.updateFundMainCash(mainCash);
        if (i == 0) {
            logger.error("修改用户资金账户失败,用户userid = {}", fundIoCashRecharge.getUserId());
            throw new LTException(LTResponseCode.FU00004);
        }

        /**保存用户渠道充值记录**/
        UserChannelTrans userChannelTrans = new UserChannelTrans();
        userChannelTrans.setChannelId(fundIoCashRecharge.getThirdOptcode());
        userChannelTrans.setUserId(fundIoCashRecharge.getUserId());
        userChannelTrans.setCreateTime(new Date());
        userChannelTrans.setCreateDate(DateTools.getDefaultDate());
        fundIoCashRechargeDao.insertUserChannelTrans(userChannelTrans);

        //用于统计推广数据
		JSONObject json = new JSONObject();
		json.put("userId", fundIoCashRecharge.getUserId());
		json.put("rechargeAmount",fundIoCashRecharge.getAmount());
		json.put("dateTime", doneDate);
		promoteProducer.sendMessage(json);
		
        return true;
    }

}
