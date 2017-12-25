/*
 * PROJECT NAME: lt-fund
 * PACKAGE NAME: com.lt.fund.service
 * FILE    NAME: FundAccountApiServiceImpl.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.fund.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.fund.service.IFundCashService;
import com.lt.fund.service.IFundFlowCashService;
import com.lt.fund.service.IFundFlowScoreService;
import com.lt.fund.service.IFundScoreService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.vo.fund.FundOrderVo;

/**
 * TODO 资金扣款、结算、退款业务实现
 * @author XieZhibing
 * @date 2016年11月30日 下午4:43:58
 * @version <b>1.0.0</b>
 */
@Service
public class FundTradeApiServiceImpl implements IFundTradeApiService {

	/** 序列化标识 */
	private static final long serialVersionUID = -1372141418781594234L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(FundTradeApiServiceImpl.class);
	
	@Autowired
	private IFundCashService fundCashServiceImpl;
	@Autowired
	private IFundScoreService fundScoreServiceImpl;
	@Autowired
	private IFundFlowCashService fundFlowCashServiceImpl;
	@Autowired
	private IFundFlowScoreService fundFlowScoreServiceImpl;

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午8:05:01
	 * @see com.lt.api.fund.rmi.IFundApiService#doBuy(com.lt.vo.fund.FundOrderVo)
	 * @param fundOrderVo
	 * @return
	 * @throws LTException
	 */
	@Override
	public boolean doBuy(FundOrderVo fundOrderVo) throws LTException {
		long startTime1 = System.currentTimeMillis();
		logger.info("=======fundOrderVo={}=======",JSONObject.toJSONString(fundOrderVo));
		//返回结果
		boolean result = false;
		
		//资金类型
		FundTypeEnum fundType = fundOrderVo.getFundType();
		if(FundTypeEnum.CASH != fundType && FundTypeEnum.SCORE != fundType) {
			throw new LTException(LTResponseCode.TD00003);	
		}
		//产品名称
		String productName = fundOrderVo.getProductName();
		if(StringTools.isEmpty(productName)) {
			throw new LTException(LTResponseCode.PR00014);
		}
		//订单ID
		String orderId = fundOrderVo.getOrderId(); 
		if(StringTools.isEmpty(orderId)) {
			throw new LTException(LTResponseCode.TD00004);
		}
		//用户ID
		String userId = fundOrderVo.getUserId(); 
		if(userId == null ) {
			throw new LTException(LTResponseCode.US01105);
		}
		//止损保证金
		double holdFund = fundOrderVo.getHoldFund();
		if(holdFund < 0){
			throw new LTException(LTResponseCode.TD00014);
		}		
		//递延保证金
		double deferFund = fundOrderVo.getDeferFund();
		//手续费
		double actualCounterFee = fundOrderVo.getActualCounterFee();
		
		if(fundType.getValue() == FundTypeEnum.CASH.getValue()){//现金
			//现金开仓扣款
			result = fundCashServiceImpl.doBuy(productName, orderId, userId, 
					holdFund, deferFund, actualCounterFee);
		}else if(fundType.getValue() == FundTypeEnum.SCORE.getValue()){//积分
			//积分开仓扣款
			result = fundScoreServiceImpl.doBuy(productName, orderId, userId, 
					holdFund, deferFund, actualCounterFee);
		}

		logger.info("开仓扣款总计用时:{}ms", (System.currentTimeMillis() - startTime1));
		return result;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午8:05:01
	 * @see com.lt.api.fund.rmi.IFundApiService#doBalance(com.lt.vo.fund.FundOrderVo)
	 * @param fundOrderVo
	 * @return
	 * @throws LTException
	 */
	@Override
	public void doBalance(FundOrderVo fundOrderVo) throws LTException {
		// TODO Auto-generated method stub
		//资金类型
		FundTypeEnum fundType = fundOrderVo.getFundType();
		if(FundTypeEnum.CASH != fundType && FundTypeEnum.SCORE != fundType) {
			throw new LTException(LTResponseCode.TD00003);	
		}
		//产品名称
		String productName = fundOrderVo.getProductName();
		if(StringTools.isEmpty(productName)) {
			throw new LTException(LTResponseCode.PR00014);
		}
		//订单ID
		String orderId = fundOrderVo.getOrderId(); 
		if(StringTools.isEmpty(orderId)) {
			throw new LTException(LTResponseCode.TD00004);
		}
		//用户ID
		String userId = fundOrderVo.getUserId(); 
		if(userId == null ) {
			throw new LTException(LTResponseCode.US01105);
		}
		//止损保证金
		double holdFund = fundOrderVo.getHoldFund();
		if(holdFund < 0){
			throw new LTException(LTResponseCode.TD00014);
		}
		//递延保证金
		double deferFund = fundOrderVo.getDeferFund();
		//用户净利润
		double userBenefit = fundOrderVo.getUserBenefit();		
		logger.info("==============fundType={}=========",fundType.getValue());
		if(fundType.getValue() == FundTypeEnum.CASH.getValue()){//现金
			//现金结算
			fundCashServiceImpl.doBalance(productName, orderId, userId, 
					holdFund, deferFund, userBenefit);
		}else if(fundType.getValue() == FundTypeEnum.SCORE.getValue()){//积分
			//积分结算
			fundScoreServiceImpl.doBalance(productName, orderId, userId, 
					holdFund, deferFund, userBenefit);
		}
	}


	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午8:05:01
	 * @see com.lt.api.fund.rmi.IFundApiService#doRefund(com.lt.vo.fund.FundOrderVo)
	 * @param fundOrderVo
	 * @return
	 * @throws LTException
	 */
	@Override
	public void doRefund(FundOrderVo fundOrderVo) throws LTException {
		// TODO Auto-generated method stub
		//资金类型
		FundTypeEnum fundType = fundOrderVo.getFundType();
		if(FundTypeEnum.CASH != fundType && FundTypeEnum.SCORE != fundType) {
			throw new LTException(LTResponseCode.TD00003);	
		}
		//产品名称
		String productName = fundOrderVo.getProductName();
		if(StringTools.isEmpty(productName)) {
			throw new LTException(LTResponseCode.PR00014);
		}
		//订单ID
		String orderId = fundOrderVo.getOrderId(); 
		if(StringTools.isEmpty(orderId)) {
			throw new LTException(LTResponseCode.TD00004);
		}
		//用户ID
		String userId = fundOrderVo.getUserId(); 
		if(userId == null) {
			throw new LTException(LTResponseCode.US01105);
		}
		//止损保证金
		double holdFund = DoubleTools.scaleFormat(fundOrderVo.getHoldFund());
		if(holdFund < 0){
			throw new LTException(LTResponseCode.TD00014);
		}
		
		//递延保证金
		double deferFund = DoubleTools.scaleFormat(fundOrderVo.getDeferFund());
		//手续费
		double actualCounterFee = DoubleTools.scaleFormat(fundOrderVo.getActualCounterFee());
		
		if(fundType.getValue() == FundTypeEnum.CASH.getValue()){//现金
			//现金退款
			fundCashServiceImpl.doRefund(productName, orderId, userId, 
					holdFund, deferFund, actualCounterFee);	
		}else if(fundType.getValue() == FundTypeEnum.SCORE.getValue()){//积分
			//积分退款
			fundScoreServiceImpl.doRefund(productName, orderId, userId, 
					holdFund, deferFund, actualCounterFee);
		}
	}

	@Override
	public Double getHoldFundByOrderId(String orderId,Integer fundType) {
		if(fundType == FundTypeEnum.CASH.getValue()){//现金
			return fundFlowCashServiceImpl.getHoldFundByOrderId(orderId);
		}
		return null;
	}

}
