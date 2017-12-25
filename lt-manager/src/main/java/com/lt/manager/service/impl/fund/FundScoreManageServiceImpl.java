package com.lt.manager.service.impl.fund;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundScoreOptCodeEnum;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.bean.fund.FundIoCashInnerVO;
import com.lt.manager.bean.fund.FundIoCashWithdrawalVO;
import com.lt.manager.dao.fund.FundScoreManageDao;
import com.lt.manager.service.fund.IFundScoreManageService;
import com.lt.model.fund.FundIoCashInner;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

@Service
public class FundScoreManageServiceImpl implements IFundScoreManageService {

	@Autowired
	private FundScoreManageDao fundScoreManageDao;
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Override
	public Page<FundFlowVO> selectFundFlowPageList(FundFlowVO fundFlow) {
		Page<FundFlowVO> page = new Page<FundFlowVO>();
		page.setPageNum(fundFlow.getPage());
		page.setPageSize(fundFlow.getRows());
		page.addAll(fundScoreManageDao.selectFundFlowList(fundFlow));
		page.setTotal(fundScoreManageDao.selectFundFlowListCount(fundFlow));
		return page;
	}

	@Override
	public Map<String,Double> selectFundFlowTotalAmt(FundFlowVO fundFlow) {
		List<FundFlowVO> list = fundScoreManageDao.selectFundFlowTotalAmt(fundFlow);
		Map<String,Double> map = new HashMap<String,Double>();
		Double inTotalAmt = 0.0 ;
		Double outTotalAmt = 0.0 ;
		for(FundFlowVO flowVO : list){
			if(FundFlowTypeEnum.INCOME.getValue().toString().equals(flowVO.getFlowType())){
				inTotalAmt += flowVO.getAmount();
			}else if(FundFlowTypeEnum.OUTLAY.getValue().toString().equals(flowVO.getFlowType())){
				outTotalAmt -= flowVO.getAmount();
			}
		}
		
		map.put("totalIn", inTotalAmt);
		map.put("totalOut", outTotalAmt);
		return map;
	}

	@Override
	public Page<FundIoCashInnerVO> qryFundInnerInOutPageDetail(FundIoCashInnerVO fundIoCashInnerVO) {
		Page<FundIoCashInnerVO> page = new Page<FundIoCashInnerVO>();
		page.setPageNum(fundIoCashInnerVO.getPage());
		page.setPageSize(fundIoCashInnerVO.getRows());
		page.addAll(fundScoreManageDao.qryFundInnerInOutPageDetail(fundIoCashInnerVO));
		page.setTotal(fundScoreManageDao.qryFundInnerInOutDetailCount(fundIoCashInnerVO));
		return page;
	}

	@Override
	public Map<String, Double> qryFundInnerInOutDetailAmt(FundIoCashInnerVO fundIoCashInnerVO) {
		List<FundIoCashInnerVO> list = fundScoreManageDao.qryFundInnerInOutDetailList(fundIoCashInnerVO);
		Map<String,Double> map = new HashMap<String,Double>();
		Double totalInAmt = 0.0;
		Double totalOutAmt = 0.0 ;
		for(FundIoCashInnerVO cashInnerVO : list){
			if(cashInnerVO.getFlowType().intValue() == FundFlowTypeEnum.INCOME.getValue().intValue()){
				totalInAmt += cashInnerVO.getAmount();
			}else if(cashInnerVO.getFlowType().intValue() == FundFlowTypeEnum.OUTLAY.getValue().intValue()){
				totalOutAmt += cashInnerVO.getAmount();
			}
		}
		
		map.put("totalIn", totalInAmt);
		map.put("totalOut", totalOutAmt);
		return map;
	}

	@Override
	public void addFundIoInnerOut(FundIoCashInner cashInner, String remark) throws LTException {
		try{
			String thirdOptCode = cashInner.getSecondOptCode()+"01";
			IFundOptCode fundOptCode = null ;
			if(cashInner.getSecondOptCode().equals(FundScoreOptCodeEnum.MANUALIN.getCode())){
				fundOptCode = FundScoreOptCodeEnum.MANUALIN;
			}else{
				fundOptCode = FundScoreOptCodeEnum.MANUALOUT;
			}
			
			fundAccountApiService.manualOutOrIn(cashInner.getOrderId(),cashInner.getUserId(), cashInner.getAmount(), fundOptCode, FundTypeEnum.SCORE, cashInner.getThirdOptCode(),remark,null,cashInner.getModifyUserId());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.FUY00002);
		}
	}

}
