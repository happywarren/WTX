package com.lt.manager.controller.fund;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundFlowTypeEnum;
import com.lt.enums.fund.FundIoCashInnerEnum;
import com.lt.enums.fund.FundIoRechargeEnum;
import com.lt.enums.fund.FundIoWithdrawalEnum;
import com.lt.enums.fund.FundScoreOptCodeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.bean.fund.FundIoCashInnerVO;
import com.lt.manager.bean.fund.FundIoCashRechargeVO;
import com.lt.manager.bean.fund.FundIoCashWithdrawalVO;
import com.lt.manager.bean.fund.FundMainCashScore;
import com.lt.manager.bean.fund.FundTransferDetailById;
import com.lt.manager.bean.fund.FundTransferDetailVO;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.fund.IFundCashManageService;
import com.lt.manager.service.fund.IFundScoreManageService;
import com.lt.model.fund.FundIoCashInner;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.BankInfo;
import com.lt.util.LoggerTools;
import com.lt.util.StaffUtil;
import com.lt.util.annotation.LimitLess;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Controller
@RequestMapping("/fundManage")
public class FundManageController {
	
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IFundCashManageService fundCashManageServiceImpl;
	@Autowired
	private IFundScoreManageService fundScoreManageServiceImpl;
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	/** 查询所有的资金流水*/
	@RequestMapping(value="/qryCashFlow")
	@ResponseBody
	public String qryFundCashFlow(FundFlowVO fundFlowVO){
		Response response = null;
		try {
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			Page<FundFlowVO>  page = fundCashManageServiceImpl.selectFundFlowPageList(fundFlowVO);
			Map<String,Double> map = fundCashManageServiceImpl.selectFundCashFlowMap(fundFlowVO);
			map.put("rate", rate);
			return JqueryEasyUIData.init(page,map);
		} catch (Exception e) {
			logger.info("查询用户现金流异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	/** 查询单个用户所有的资金流水*/
	@RequestMapping(value="/qryUserCashFlow")
	@ResponseBody
	public String qryUserFundCashFlow(FundFlowVO fundFlowVO){
		Response response = null;
		try {
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			Page<FundFlowVO>  page = fundCashManageServiceImpl.selectFundFlowPageList(fundFlowVO);
			Map<String,Double> map = fundCashManageServiceImpl.selectFundCashFlowMap(fundFlowVO);
			map.put("rate", rate);
			return JqueryEasyUIData.init(page,map);
		} catch (Exception e) {
			logger.info("查询用户现金流异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/** 查询所有的积分流水*/
	@RequestMapping(value="/qryScoreFlow")
	@ResponseBody
	public String qryFundScoreFlow(FundFlowVO fundFlowVO){
		Response response = null;
		try {
			Page<FundFlowVO>  page = fundScoreManageServiceImpl.selectFundFlowPageList(fundFlowVO);
			Map<String,Double> map = fundScoreManageServiceImpl.selectFundFlowTotalAmt(fundFlowVO);
			
			return JqueryEasyUIData.init(page,map);
		} catch (Exception e) {
			logger.info("查询用户现金流异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询用户所有的积分流水*/
	@RequestMapping(value="/qryUserScoreFlow")
	@ResponseBody
	public String qryUserFundScoreFlow(FundFlowVO fundFlowVO){
		Response response = null;
		try {
			Page<FundFlowVO>  page = fundScoreManageServiceImpl.selectFundFlowPageList(fundFlowVO);
			Map<String,Double> map = fundScoreManageServiceImpl.selectFundFlowTotalAmt(fundFlowVO);
			
			return JqueryEasyUIData.init(page,map);
		} catch (Exception e) {
			logger.info("查询用户现金流异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询所有的补单的充值记录*/
	@RequestMapping(value="/getFinancyIoRepair")
	@ResponseBody
	public String getFinancyIoRepair(FundIoCashRechargeVO cashRecharge){
		Response response = null;
		try {
			cashRecharge.setStatus(FundIoRechargeEnum.AUDIT.getValue());
			Page<FundIoCashRechargeVO>  page = fundCashManageServiceImpl.selectFundIoCashRechargePageList(cashRecharge);
			Map<String,Double> map = fundCashManageServiceImpl.selectFundIoCashRechargeList(cashRecharge);
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			map.put("rate", rate);
			return JqueryEasyUIData.init(page,map);
		}catch(Exception e){
			logger.info("查询用户充值记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}


	/** 查询所有的现金充值记录*/
	@RequestMapping(value="/getFinancyIoDetail")
	@ResponseBody
	public String getFinancyIoDetail(FundIoCashRechargeVO cashRecharge){
		Response response = null;
		try {
//			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
//			rate = rate == null ? 0.0:rate;
			Page<FundIoCashRechargeVO>  page = fundCashManageServiceImpl.selectFundIoCashRechargePageList(cashRecharge);
			
//			if(page != null && !page.isEmpty()){
//				for(FundIoCashRechargeVO fundFlow : page){
//					fundFlow.setRmbAmt(DoubleTools.ceil(fundFlow.getAmount()/rate, 2));
//				}
//			}
			
			Map<String,Double> map = fundCashManageServiceImpl.selectFundIoCashRechargeList(cashRecharge);
//			map.put("rate", rate);
			return JqueryEasyUIData.init(page,map);
		}catch(Exception e){
			logger.info("查询用户充值记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/** 查询详细的补单的充值记录*/
	@RequestMapping(value="/getFinancyIoById")
	@ResponseBody
	public String getFinancyIoById(String ioId,Integer status){
		Response response = null;
		try{
			if(ioId == null  || ioId.trim().equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			FundIoCashRechargeVO fundIoCash = fundCashManageServiceImpl.qryRechargeRepair(ioId,status);
//			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
//			fundIoCash.setRate(rate);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", fundIoCash);
			
		}catch(Exception e){
			logger.info("查询用户充值记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
		
	}
	
	/** 查询所有的待审核提现记录*/
	@RequestMapping(value="/getFinancyWithdrawal")
	@ResponseBody
	public String getFinancyWithdrawal(FundIoCashWithdrawalVO fundIoCashWithVO){
		Response response = null;
		try{
			fundIoCashWithVO.setStatus(FundIoWithdrawalEnum.AUDIT.getValue());
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			Page<FundIoCashWithdrawalVO>  page = fundCashManageServiceImpl.selectFundIoCashWithdrawaPagelList(fundIoCashWithVO);
			
//			if(page != null && !page.isEmpty()){
//				for(FundIoCashWithdrawalVO fundFlow : page){
//					fundFlow.setRmbAmt(DoubleTools.ceil(fundFlow.getAmount()/rate, 2));
//				}
//			}
			
			Map<String,Double> map = fundCashManageServiceImpl.selectFundIoCashWithdrawalList(fundIoCashWithVO);
			map.put("rate", rate);
			return JqueryEasyUIData.init(page,map);
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询所有的提现记录*/
	@RequestMapping(value="/getFinancyWithdrawalDetail")
	@ResponseBody
	public String getFinancyWithdrawalDetail(FundIoCashWithdrawalVO fundIoCashWithVO){
		Response response = null;
		try{
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			Page<FundIoCashWithdrawalVO>  page = fundCashManageServiceImpl.selectFundIoCashWithdrawaPagelList(fundIoCashWithVO);
//			if(page != null && !page.isEmpty()){
//				for(FundIoCashWithdrawalVO fundFlow : page){
//					fundFlow.setRmbAmt(DoubleTools.ceil(fundFlow.getAmount()/rate, 2));
//				}
//			}
			Map<String,Double> map = fundCashManageServiceImpl.selectFundIoCashWithdrawalList(fundIoCashWithVO);
			map.put("rate", rate);
			return JqueryEasyUIData.init(page,map);
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 根据转账ID查询单条提现的详细记录*/
	@RequestMapping(value="/getFinancyWithdrawalById")
	@ResponseBody
	public String getFinancyWithdrawalById(String ioId){
		Response response = null;
		try{
			if(ioId == null  || ioId.trim().equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			FundIoCashWithdrawalVO  fundIoCashWithdrawalVO = fundCashManageServiceImpl.selectFundIoCashWithdrawalDetail(ioId);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", fundIoCashWithdrawalVO);
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询转账明细数据*/
	@RequestMapping(value="/getFinancyTransFering")
	@ResponseBody
	public String getFinancyTransFering(FundIoCashWithdrawalVO fundIoCashWithVO){
		Response response = null;
		try{
			fundIoCashWithVO.setStatus(FundIoWithdrawalEnum.WAIT.getValue());
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			Page<FundIoCashWithdrawalVO>  page = fundCashManageServiceImpl.selectFundIoCashWithdrawaPagelList(fundIoCashWithVO);
			
			Map<String,Double> map = fundCashManageServiceImpl.selectFundIoCashWithdrawalList(fundIoCashWithVO);
			
			return JqueryEasyUIData.init(page,map);
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询所有提现记录*/
	@RequestMapping(value="/getTransferDetailList")
	@ResponseBody
	public String getTransferDetailList(FundTransferDetailVO fundTransferDetailVO){
		Response response = null;
		try{
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			Page<FundTransferDetailVO>  page = fundCashManageServiceImpl.selectFundTransferPageDetail(fundTransferDetailVO);
			Map<String,Double> map = fundCashManageServiceImpl.selectFundTransferDetailList(fundTransferDetailVO);
			map.put("rate", rate);
			return JqueryEasyUIData.init(page,map);
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询详细提现记录*/
	@RequestMapping(value="/getTransferDetail")
	@ResponseBody
	public String getTransferDetail(String ioId){
		Response response = null;
		try{
			FundTransferDetailById detailVO = fundCashManageServiceImpl.qryFundTransferDetail(ioId);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", detailVO);
		}catch (Exception e) {
			// TODO: handle exception
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询详细提现记录*/
	@RequestMapping(value="/getUserMainCashScore")
	@ResponseBody
	public String getUserMainCashScore(FundMainCashScore fundMainCashScore){
		Response response = null;
		try{
			Page<FundMainCashScore> detailVO = fundCashManageServiceImpl.qryUserMainCashScore(fundMainCashScore);
			return JqueryEasyUIData.init(detailVO);
		}catch (Exception e) {
			// TODO: handle exception
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询详细提现记录*/
	@RequestMapping(value="/getUserMainCashScoreByUserId")
	@ResponseBody
	public String getUserMainCashScoreByUserId(FundMainCashScore fundMainCashScore){
		Response response = null;
		try{
			Page<FundMainCashScore> detailVO = fundCashManageServiceImpl.qryUserMainCashScore(fundMainCashScore);
			return JqueryEasyUIData.init(detailVO);
		}catch (Exception e) {
			// TODO: handle exception
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询所有的内部存取记录*/
	@RequestMapping(value="/getFinancyInnerInOutDetailList")
	@ResponseBody
	public String getFinancyInnerInOutDetailList(FundIoCashInnerVO fundIoCashInnerVO,Integer operate){
		Response response = null;
		try{
			
			if(operate != null){
				if(operate == 0){
					fundIoCashInnerVO.setFlowType(1);
				}else{
					fundIoCashInnerVO.setFlowType(-1);
				}
			}
			
			// 不传入查询条件，默认查询现金
			if(fundIoCashInnerVO.getType() == null || fundIoCashInnerVO.getType().equals("")){
				fundIoCashInnerVO.setType(FundTypeEnum.CASH.getValue());
			}
			
			Page<FundIoCashInnerVO>  page = null ;
			Map<String,Double> map = null ;
			if(fundIoCashInnerVO.getType().intValue() == FundTypeEnum.CASH.getValue()){
				page = fundCashManageServiceImpl.qryFundInnerInOutPageDetail(fundIoCashInnerVO);
				map = fundCashManageServiceImpl.qryFundInnerInOutDetailAmt(fundIoCashInnerVO);
			}else{
				page = fundScoreManageServiceImpl.qryFundInnerInOutPageDetail(fundIoCashInnerVO);
				map = fundScoreManageServiceImpl.qryFundInnerInOutDetailAmt(fundIoCashInnerVO);
			}
			
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			rate = rate == null ? 0.0:rate;
			map.put("rate", rate);
			
			return JqueryEasyUIData.init(page,map);
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 生成内部存取数据*/
	@RequestMapping(value="/addFundInnerInOut")
	@ResponseBody
	public String addFundInnerInOut(HttpServletRequest request,FundIoCashInner cashInner,String type,String userIds,String remark){
		Response response = null;
		try{
//			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
//			rate = rate == null ? 0.0:rate;
			logger.info("cashInner:{}",JSONObject.toJSONString(cashInner));
			Integer modifyId = 0;// staff == null ?"0":staff.getName();
			/**获取当前操作人**/
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				modifyId = staff.getId();
				cashInner.setModifyUserId(modifyId);
			}
			
			if(userIds == null || userIds.equals("")){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			
			if(userIds.contains(",")){
				for(String id:userIds.split(",")){
					cashInner.setUserId(id);
					if(type.equals(String.valueOf(FundTypeEnum.CASH.getValue()))){
						fundCashManageServiceImpl.addFundIoInnerOut(cashInner,remark);
					}else{
						fundScoreManageServiceImpl.addFundIoInnerOut(cashInner,remark);
					}
				}
			}else{
				cashInner.setUserId(userIds);
				if(type.equals(String.valueOf(FundTypeEnum.CASH.getValue()))){
					fundCashManageServiceImpl.addFundIoInnerOut(cashInner,remark);
				}else{
					fundScoreManageServiceImpl.addFundIoInnerOut(cashInner,remark);
				}
			}
			
			
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		}catch (Exception e) {
			logger.info("生成内部存取数据异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/** 查询详细内部存取记录*/
	@RequestMapping(value="/getFundInOutAuditList")
	@ResponseBody
	public String getFundInOutAuditList(FundIoCashInnerVO fundIoCashInnerVO){
		Response response = null;
		try{
			// 不传入查询条件，默认查询现金
			if(fundIoCashInnerVO.getType() == null || fundIoCashInnerVO.getType().equals("")){
				fundIoCashInnerVO.setType(FundTypeEnum.CASH.getValue());
			}
			
			fundIoCashInnerVO.setStatus(FundIoCashInnerEnum.AUDIT.getValue());
			
			Page<FundIoCashInnerVO>  page = null ;
			if(fundIoCashInnerVO.getType().intValue() == FundTypeEnum.CASH.getValue()){
				page = fundCashManageServiceImpl.qryFundInnerInOutPageDetail(fundIoCashInnerVO);
			}else{
				page = fundScoreManageServiceImpl.qryFundInnerInOutPageDetail(fundIoCashInnerVO);
			}
			
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	

	/** 审核内部存取记录*/
	@RequestMapping(value="/fundInnerInOutAudit")
	@ResponseBody
	public String fundInnerInOutAudit(HttpServletRequest request,String ioId,String status,String type){
		Response response = null;
		try{
			
			if(ioId == null || ioId.equals("") || status == null || status.equals("")
					|| type == null || type.equals("")){
				return LTResponseCode.getCode(LTResponseCode.FU00003).toJsonString();
			}
			Integer modifyId = 0;// staff == null ?"0":staff.getName();
			/**获取当前操作人**/
			Staff staff = StaffUtil.getStaff(request);
			if(staff!=null && StringTools.isNotEmpty(staff.getId()+"")){
				modifyId = staff.getId();
			}
			
			if(ioId.contains(",")){
				for(String id:ioId.split(",")){
					if(type.equals(String.valueOf(FundTypeEnum.CASH.getValue()))){
						fundAccountApiService.fundInnerDoAudit(Long.parseLong(id), Integer.parseInt(status),null,modifyId);
					}else{
						fundAccountApiService.scoreInnerDoAudit(Long.parseLong(id), Integer.parseInt(status),null,modifyId);
					}
				}
			}else{
				if(type.equals(String.valueOf(FundTypeEnum.CASH.getValue()))){
					fundAccountApiService.fundInnerDoAudit(Long.parseLong(ioId), Integer.parseInt(status),null,modifyId);
				}else{
					fundAccountApiService.scoreInnerDoAudit(Long.parseLong(ioId), Integer.parseInt(status),null,modifyId);
				}
			}
			
			response =  new Response(LTResponseCode.SUCCESS, "审核成功");
		}catch(Exception e){
			logger.info("查询提现记录常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/** 查询银行信息记录*/
	@RequestMapping(value="/findBankInfo")
	@ResponseBody
	public String findBankInfo(){
		Response response = null;
		try{
			List<BankInfo> list = fundCashManageServiceImpl.findBankInfo();
			response = new Response((LTResponseCode.SUCCESS),"查询成功",list);
		}catch(Exception e){
			logger.info("银行列表记录异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 获取现金或者积分全部的资金业务码
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/getFundOptCode")
	@ResponseBody
	public String getFundOptCode(String type){
		Response response = null;
		try{
			List<FundOptCode> list = fundCashManageServiceImpl.getFundOptCode();
			List<FundOptCode> returnList = new ArrayList<FundOptCode>();
			StringBuilder code = new StringBuilder();
			
			boolean flag = type!=null && type.equals("second")?true:false;
			
			for(FundOptCode fundCode : list){
				if(flag){
					if( code.indexOf(fundCode.getSecondOptCode()) < 0){
						code.append(fundCode.getSecondOptCode()+",");
						returnList.add(fundCode);
					}
				}else{
					if( code.indexOf(fundCode.getFirstOptCode()) < 0){
						code.append(fundCode.getFirstOptCode()+",");
						returnList.add(fundCode);
					}
				}
				
				
			}
			
			response = new Response((LTResponseCode.SUCCESS),"查询成功",returnList);
		}catch(Exception e){
			logger.info("现金配置列表异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		
		return response.toJsonString();
	}
	/**
	 * 获取人工存入取出现金流配置码
	 * @return
	 */
	@RequestMapping(value="/getInOutOptCode")
	@ResponseBody
	public String getInOutOptCode(){
		Response response = null;
		List<FundOptCode> returnList = new ArrayList<FundOptCode>();
		List<String> firstOptCode = new ArrayList<String>();
		firstOptCode.add(FundCashOptCodeEnum.MANUALIN.getFirstLevelCode());
		firstOptCode.add(FundCashOptCodeEnum.MANUALOUT.getFirstLevelCode());
		firstOptCode.add(FundScoreOptCodeEnum.MANUALIN.getFirstLevelCode());
		firstOptCode.add(FundScoreOptCodeEnum.MANUALOUT.getFirstLevelCode());
		firstOptCode.add(FundCashOptCodeEnum.RECHARGE.getFirstLevelCode());
		
		returnList = fundCashManageServiceImpl.getOptCodeByFirst(firstOptCode);
		response = new Response((LTResponseCode.SUCCESS),"查询成功",returnList);
		
		return response.toJsonString();
	}
	
	/**
	 * 获取一级资金业务码
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/getFirstOptCode")
	@ResponseBody
	public String getFirstOptCode(Integer type){
		Response response = null ;
		if(type == null){
			response = new Response(LTResponseCode.ER400, "缺失必填参数");
		}else{
			List<FundOptCode> list = fundCashManageServiceImpl.getFirstOptCode(type);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", list);
		}
		return response.toJsonString();
	}
	
	/**
	 * 获取二级资金业务码
	 * @param type
	 * @param firstOptCode
	 * @return
	 */
	@RequestMapping(value="/getSecondOptCode")
	@ResponseBody
	public String getSecondOptCode(Integer type,Integer firstOptCode){
		Response response = null ;
		if(type == null || firstOptCode == null){
			response = new Response(LTResponseCode.ER400, "缺失必填参数");
		}else{
			List<FundOptCode> list = fundCashManageServiceImpl.getSecondOptCode(type, firstOptCode);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", list);
		}
		return response.toJsonString();
	}
}
