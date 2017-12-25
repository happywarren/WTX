package com.lt.manager.service.impl.fund;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.bean.fund.FundIoCashInnerVO;
import com.lt.manager.bean.fund.FundIoCashRechargeVO;
import com.lt.manager.bean.fund.FundIoCashWithdrawalVO;
import com.lt.manager.bean.fund.FundMainCashScore;
import com.lt.manager.bean.fund.FundTransferDetailById;
import com.lt.manager.bean.fund.FundTransferDetailVO;
import com.lt.manager.dao.fund.FundCashManageDao;
import com.lt.manager.dao.fund.FundWithdrawDao;
import com.lt.manager.service.fund.IFundCashManageService;
import com.lt.model.fund.FundIoCashInner;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.BankInfo;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

@Service
public class FundCashManageServiceImpl implements IFundCashManageService {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private FundCashManageDao fundCashManageDao;
	
	@Autowired
	private FundWithdrawDao fundWithdrawDao;
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Autowired
	private IProductApiService productApiServiceImpl;


	@Override
	public Page<FundFlowVO> selectFundFlowPageList(FundFlowVO fundFlow) {
		Page<FundFlowVO> page = new Page<FundFlowVO>();
		page.setPageNum(fundFlow.getPage());
		page.setPageSize(fundFlow.getRows());
		page.addAll(fundCashManageDao.selectFundFlowPageList(fundFlow));
		page.setTotal(fundCashManageDao.selectFundFlowListCount(fundFlow));
		return page;
	}
	
	@Override
	public Page<FundIoCashRechargeVO> selectFundIoCashRechargePageList(FundIoCashRechargeVO fundIoCashRecharge) {
		// TODO Auto-generated method stub
		Page<FundIoCashRechargeVO> page = new Page<FundIoCashRechargeVO>();
		page.setPageNum(fundIoCashRecharge.getPage());
		page.setPageSize(fundIoCashRecharge.getRows());
		page.addAll(fundCashManageDao.selectFundChargeFlowPage(fundIoCashRecharge));
		page.setTotal(fundCashManageDao.selectFundChargeFlowCount(fundIoCashRecharge));
		return page;
	}

	@Override
	public Page<FundIoCashWithdrawalVO> selectFundIoCashWithdrawaPagelList(FundIoCashWithdrawalVO fundIoCashWithdrawal) {
		Page<FundIoCashWithdrawalVO> page = new Page<FundIoCashWithdrawalVO>();
		page.setPageNum(fundIoCashWithdrawal.getPage());
		page.setPageSize(fundIoCashWithdrawal.getRows());
		page.addAll(fundCashManageDao.selectFundWithdrawalPage(fundIoCashWithdrawal));
		page.setTotal(fundCashManageDao.selectFundWithdrawalCount(fundIoCashWithdrawal));
		return page;
	}
	
	@Override
	public Map<String,Double> selectFundIoCashWithdrawalList(FundIoCashWithdrawalVO fundIoCashWithdrawal) {
		Map<String,Double> map = new HashMap<String,Double>();
		List<FundIoCashWithdrawalVO> list = fundCashManageDao.selectFundWithdrawal(fundIoCashWithdrawal);
		Double totalAmt = 0.0 ;
		Double totalRmbAmt = 0.0 ;
		Double totalTex = 0.0 ;
		Double totalRmbTex = 0.0 ;
		Double totalFactTex = 0.0;
		Double totalRmbFactTex = 0.0 ;
		
		for(FundIoCashWithdrawalVO cashWithdrawalVO : list){
			totalAmt += cashWithdrawalVO.getAmount();
			totalRmbAmt += cashWithdrawalVO.getRmbAmt();
			totalTex += cashWithdrawalVO.getTax();
			totalRmbTex += cashWithdrawalVO.getRmbTax();
			totalFactTex += cashWithdrawalVO.getFactTax();
			totalRmbFactTex += cashWithdrawalVO.getRmbFactTax();
		}

		map.put("totalAmt", totalAmt);
		map.put("totalRmbAmt", totalRmbAmt);
		map.put("totalTex", totalTex);
		map.put("totalRmbTex", totalRmbTex);
		map.put("totalFactTex", totalFactTex);
		map.put("totalRmbFactTex", totalRmbFactTex);
		return map;
	}

	@Override
	public Page<FundTransferDetailVO> selectFundTransferPageDetail(FundTransferDetailVO detailVO) {
		Page<FundTransferDetailVO> page = new Page<FundTransferDetailVO>();
		page.setPageNum(detailVO.getPage());
		page.setPageSize(detailVO.getRows());
		page.addAll(fundCashManageDao.selectFundTransferDetailList(detailVO));
		page.setTotal(fundCashManageDao.selectFundTransferCount(detailVO));
		return page;
	}

	@Override
	public FundTransferDetailById qryFundTransferDetail(String id) throws LTException {
		try{
			FundTransferDetailById detailVO = fundCashManageDao.getFundTransferDetailById(Integer.parseInt(id));
			return detailVO;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.FUY00002);
		}
	}
	
	@Override
	public FundIoCashRechargeVO qryRechargeRepair(String id,Integer status) throws LTException {
		FundIoCashRechargeVO rechargeVO = new FundIoCashRechargeVO();
		rechargeVO.setIoId(Long.parseLong(id));
		if(status !=null ){
			rechargeVO.setStatus(status);
		}
		FundIoCashRechargeVO cashRechargeVO =  fundWithdrawDao.selectFundRepair(rechargeVO);
		
		if(cashRechargeVO == null){
			throw new LTException(LTResponseCode.FUY00011);
		}
		return cashRechargeVO;
	}

	@Override
	public Page<FundMainCashScore> qryUserMainCashScore(FundMainCashScore fundMainCashScore) {
		Page<FundMainCashScore> page = new Page<FundMainCashScore>();
		page.setPageNum(fundMainCashScore.getPage());
		page.setPageSize(fundMainCashScore.getRows());
		page.addAll(fundCashManageDao.getUserMainCashScoreList(fundMainCashScore));
		page.setTotal(fundCashManageDao.getUserMainCashScoreCount(fundMainCashScore));
		return page;
	}

	@Override
	public Page<FundIoCashInnerVO> qryFundInnerInOutPageDetail(FundIoCashInnerVO fundIoCashInnerVO) {
		Page<FundIoCashInnerVO> page = new Page<FundIoCashInnerVO>();
		page.setPageNum(fundIoCashInnerVO.getPage());
		page.setPageSize(fundIoCashInnerVO.getRows());
		page.addAll(fundCashManageDao.qryFundInnerInOutPageDetail(fundIoCashInnerVO));
		page.setTotal(fundCashManageDao.qryFundInnerInOutDetailCount(fundIoCashInnerVO));
		return page;
	}

	@Override
	public void addFundIoInnerOut(FundIoCashInner cashInner,String remark) throws LTException {
		try{
//			String thirdOptCode = cashInner.getSecondOptCode()+"01";
			IFundOptCode fundOptCode = null ;
			if(cashInner.getSecondOptCode().equals(FundCashOptCodeEnum.MANUALIN.getCode())){
				fundOptCode = FundCashOptCodeEnum.MANUALIN;
			}else if(cashInner.getSecondOptCode().equals(FundCashOptCodeEnum.MANUALOUT.getCode())){
				fundOptCode = FundCashOptCodeEnum.MANUALOUT;
			}else if(cashInner.getSecondOptCode().equals(FundCashOptCodeEnum.RECHARGENOTE.getCode())){
				fundOptCode = FundCashOptCodeEnum.RECHARGENOTE;
			}else{
				throw new LTException(LTResponseCode.FUJ0001);
			}
			
			fundAccountApiService.manualOutOrIn(cashInner.getOrderId(),cashInner.getUserId(), cashInner.getAmount(), fundOptCode, FundTypeEnum.CASH, cashInner.getThirdOptCode(),remark,cashInner.getRmbAmt(),cashInner.getModifyUserId());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.debug("新增内部存取单异常：e：{}",e);
			throw new LTException(LTResponseCode.FUY00002);
		}
	}

	@Override
	public FundIoCashWithdrawalVO selectFundIoCashWithdrawalDetail(String ioId) {
		try{
			FundIoCashWithdrawalVO detailVO = fundCashManageDao.getFundDrawDetailById(ioId);
			Double amount = fundCashManageDao.getUserFundTransferTotalAmount(detailVO.getUserId().toString(), detailVO.getCreateDate());
			Double tax = fundCashManageDao.getUserFundTransferTotalTax(detailVO.getUserId().toString(), detailVO.getCreateDate());
			detailVO.setDrawTotalAmt(amount == null ? 0.0 :amount);
			detailVO.setTaxTotal(tax == null ? 0.0 :tax);
			return detailVO;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.FUY00002);
		}
	}

	@Override
	public Map<String, Double> selectFundCashFlowList(FundFlowVO fundFlow) {
		Page<FundFlowVO> page = new Page<FundFlowVO>();
		page.addAll(fundCashManageDao.selectFundFlowList(fundFlow));
		Map<String,Double> map = new HashMap<String,Double>();
		List<FundFlowVO> list = page.getResult();
		Double inTotalAmt = 0.0 ;
		Double outTotalAmt = 0.0 ;
		for(FundFlowVO flowVO : list){
			if(FundFlowTypeEnum.INCOME.getValue().toString().equals(flowVO.getFlowType())){
				inTotalAmt += flowVO.getAmount();
			}else if(FundFlowTypeEnum.OUTLAY.getValue().toString().equals(flowVO.getFlowType())){
				outTotalAmt += flowVO.getAmount();
			}
		}
		
		map.put("totalInAmt", inTotalAmt);
		map.put("totalOutAmt", outTotalAmt);
		return map;
	}

	@Override
	public Map<String, Double> selectFundIoCashRechargeList(FundIoCashRechargeVO cashRecharge) {
		List<FundIoCashRechargeVO> list = fundCashManageDao.selectFundChargeFlow(cashRecharge);
		logger.info("list:{}",JSONObject.toJSONString(list));
		Map<String,Double> map = new HashMap<String,Double>();
		Double totalAmount = 0.0 ;
		Double totalRmbAmount = 0.0 ;
		Double totalDoneAmount = 0.0 ;
		Double totalDoneRmbAmount = 0.0 ;
		if(!CollectionUtils.isEmpty(list)){
			for(FundIoCashRechargeVO fundIoCashRecharge : list){
				Double amount = fundIoCashRecharge.getAmount() == null ?  0.0:  fundIoCashRecharge.getAmount();
				totalAmount += amount;
				
				Double rmbAmt = fundIoCashRecharge.getRmbAmt() == null ?  0.0:  fundIoCashRecharge.getRmbAmt();
				totalRmbAmount += rmbAmt ;
				
				Double actualAmount = fundIoCashRecharge.getActualAmount() == null ?  0.0:  fundIoCashRecharge.getActualAmount();
				totalDoneAmount += actualAmount;
				
				Double actualRmbAmount = fundIoCashRecharge.getActualRmbAmount() == null ?  0.0:  fundIoCashRecharge.getActualRmbAmount();
				totalDoneRmbAmount += actualRmbAmount;
			}
		}
		
		map.put("totalAmt", totalAmount);
		map.put("totalRmbAmt", totalRmbAmount);
		map.put("totalDoneAmt", totalDoneAmount);
		map.put("totalRmbDoneAmt", totalDoneRmbAmount);
		return map;
	}

	@Override
	public Map<String,Double> selectFundTransferDetailList(FundTransferDetailVO detailVO) {
		List<FundTransferDetailVO> list = fundCashManageDao.selectFundTransferDetailListTotalAmt(detailVO);
		Map<String,Double> map = new HashMap<String,Double>();
		double amount = 0.0 ;
		double rmbAmt = 0.0 ;
		for(FundTransferDetailVO fundTransVo : list){
			amount += fundTransVo.getAmount();
			rmbAmt += fundTransVo.getRmbAmt();
		}
		map.put("totalAmt", amount);
		map.put("totalRmbAmt", rmbAmt);
		return map;
	}

	@Override
	public Map<String, Double> qryFundInnerInOutDetailAmt(FundIoCashInnerVO fundIoCashInnerVO) {
		List<FundIoCashInnerVO> list = fundCashManageDao.qryFundInnerInOutDetailList(fundIoCashInnerVO);
		Map<String,Double> map = new HashMap<String,Double>();
		Double totalInAmt = 0.0;
		Double totalInRmbAmt = 0.0 ;
		Double totalOutAmt = 0.0 ;
		Double totalOutRmbAmt = 0.0 ;
		for(FundIoCashInnerVO cashInnerVO : list){
			if(cashInnerVO.getFlowType().intValue() == FundFlowTypeEnum.INCOME.getValue().intValue()){
				totalInAmt += cashInnerVO.getAmount();
				totalInRmbAmt += cashInnerVO.getRmbAmt() == null ? 0.0 : cashInnerVO.getRmbAmt() ;
			}else if(cashInnerVO.getFlowType().intValue() == FundFlowTypeEnum.OUTLAY.getValue().intValue()){
				totalOutAmt += cashInnerVO.getAmount();
				totalOutRmbAmt += cashInnerVO.getRmbAmt() == null ? 0.0 : cashInnerVO.getRmbAmt() ;
			}
		}
		
		map.put("totalIn", totalInAmt);
		map.put("totalOut", totalOutAmt);
		map.put("totalInRmbAmt", totalInRmbAmt);
		map.put("totalOutRmbAmt", totalOutRmbAmt);
		return map;
	}

	@Override
	public List<BankInfo> findBankInfo() throws LTException {
		return fundCashManageDao.fundBankInfo();
	}

	@Override
	public List<FundOptCode> getFundOptCode() throws LTException {
		return fundCashManageDao.getFundOptCode();
	}

	@Override
	public List<FundOptCode> getFirstOptCode(Integer fundType) throws LTException {
		return fundCashManageDao.getFirstFundOptCode(fundType);
	}

	@Override
	public List<FundOptCode> getSecondOptCode(Integer fundType, Integer firstOptCode) throws LTException {
		return fundCashManageDao.getSecondFundOptCode(fundType, firstOptCode);
	}

	@Override
	public List<FundOptCode> getOptCodeByFirst(List<String> firstOptCode) throws LTException {
		return fundCashManageDao.getFundOptCodeByFirst(firstOptCode);
	}

	@Override
	public Map<String, Double> selectFundCashFlowMap(FundFlowVO fundFlow) {
		Map<String, Double> dmap = new HashMap<String, Double>();
		Map<String, Object> map = fundCashManageDao.selectfundFlowMap(fundFlow);
		if(map == null){
			dmap.put("totalInAmt", 0.0);
			dmap.put("totalOutAmt", 0.0);
		}else{
			dmap.put("totalInAmt", Double.valueOf(map.get("inTotalAmt")==null?"0":String.valueOf(map.get("inTotalAmt"))));
			dmap.put("totalOutAmt",Double.valueOf(map.get("outTotalAmt")==null?"0":String.valueOf(map.get("outTotalAmt"))));
		}
		return dmap;
	}
	
}
