package com.lt.manager.controller.promote;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.promote.IPromoteApiService;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.manager.bean.promote.CommisionParamVo;
import com.lt.manager.service.promote.ICommisionManageService;
import com.lt.model.promote.CommisionOptcode;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 佣金管理控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="promote/commision")
public class CommisionManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IPromoteApiService promoteApiService;
	@Autowired
	private ICommisionManageService commisionManageService;
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	/**
	 * 佣金提现审核
	 * @param request
	 * @param ids
	 *  @param flag 1:通过   2:拒绝
	 * @return
	 */
	@RequestMapping(value="/commisionWithdrawCheck")
	@ResponseBody
	public String commisionWithdrawCheck(HttpServletRequest request,String ids,String flag){
		Response response = null;
		try{
			if(StringTools.isEmpty(ids) || StringTools.isEmpty(flag)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			String[] idArray = ids.split(",");
			if(flag.equals("1")){//通过
				for(String id:idArray){
					promoteApiService.commisionWidthdrawPass(id);
				}
			}else if(flag.equals("2")){//拒绝
				for(String id:idArray){
					promoteApiService.commisionWidthdrawNoPass(id);
				}
			}else{
				return LTResponseCode.getCode(LTResponseCode.PROMJ0008).toJsonString();
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("佣金提现审核异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询佣金存取明细--分页
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryCommisionIoPage")
	@ResponseBody
	public String queryCommisionIoPage(HttpServletRequest request,CommisionParamVo param){
		Response response = null;
		try{
			param.setStatus(0);//查询待审核数据
			Page<CommisionParamVo> page = commisionManageService.queryCommisionIoPage(param);
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询佣金存取明细异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询佣金流水--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryCommisionFlowPage")
	@ResponseBody
	public String queryCommisionFlowPage(HttpServletRequest request,CommisionParamVo param){
		Response response = null;
		try{
			Page<CommisionParamVo> page = commisionManageService.queryCommisionFlowPage(param);
			Map<String, Object> data = commisionManageService.getCommisionFlowData(param);
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			data.put("rate", rate);
			return JqueryEasyUIData.init(page, data);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询佣金流水异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询佣金流水--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryCommisionFlowByUserIdPage")
	@ResponseBody
	public String queryCommisionFlowByUserIdPage(HttpServletRequest request,CommisionParamVo param){
		Response response = null;
		try{
			Page<CommisionParamVo> page = commisionManageService.queryCommisionFlowPage(param);
			Map<String, Object> data = commisionManageService.getCommisionFlowData(param);
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			data.put("rate", rate);
			return JqueryEasyUIData.init(page, data);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询佣金流水异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 获取一级目录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getFirstOptcode")
	@ResponseBody
	public String getFirstOptcode(HttpServletRequest request){
		Response response = null;
		try{
			List<CommisionOptcode> list = commisionManageService.getFirstOptcode();
			response =  LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("获取佣金一级目录异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 获取二级目录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSecondOptcode")
	@ResponseBody
	public String getSecondOptcode(HttpServletRequest request,String firstOptcode){
		Response response = null;
		try{
			CommisionOptcode code = new CommisionOptcode();
			code.setFirstOptcode(firstOptcode);
			List<CommisionOptcode> list = commisionManageService.getSecondOptcode(code);
			response =  LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("获取佣金二级目录异常，e={}",e);
		}
		return response.toJsonString();
	}
	
}
