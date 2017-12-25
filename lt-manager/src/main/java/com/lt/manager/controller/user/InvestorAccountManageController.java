package com.lt.manager.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.InvestorAccount;
import com.lt.manager.service.user.IInvestorAccountManageService;
import com.lt.model.user.ServiceContant;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 券商控制器
 * @author licy
 *
 */
@Controller
@RequestMapping(value="user")
public class InvestorAccountManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IInvestorAccountManageService iInvestorManageService;
	
	/**
	 * 添加商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/addInvestorAccount")
	@ResponseBody
	public String addInvestorAccount(HttpServletRequest request,InvestorAccount param){
		Response response = null;
		try {
			if(StringTools.isEmpty(String.valueOf(param.getUserId()))){
				response = LTResponseCode.getCode(LTResponseCode.US20001);
				return response.toJsonString();
			}
			iInvestorManageService.addInvestorAccount(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().equals(LTResponseCode.US04102)){
				return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
			}
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("添加券商信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/editInvestorAccount")
	@ResponseBody
	public String editInvestorAccount(HttpServletRequest request,InvestorAccount param){
		Response response = null;
		try {
			if(String.valueOf(param.getId()) == null){
				throw new LTException(LTResponseCode.US20001);
			}
			
//			 InvestorAccount iavo = new InvestorAccount();
//			 iavo = iInvestorManageService.getInvestorAccountObj(param.getId());
			 iInvestorManageService.editInvestorAccount(param);
//			 if(!iavo.getSecurityCode().equals(param.getSecurityCode())){
//				 iProductAccountMapperService.editProductAccountMapperBySecurityCode(iavo.getSecurityCode(),param.getSecurityCode());
//			 }
			 response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("编辑券商信息异常，e={}",e);
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 删除商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/removeInvestorAccount")
	@ResponseBody
	public String removeInvestorAccount(HttpServletRequest request,InvestorAccount param){
		Response response = null;
		try {
			if(String.valueOf(param.getId()) == null){
				throw new LTException(LTResponseCode.US20001);
			}
			iInvestorManageService.removeInvestorAccount(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("删除券商信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	
	@RequestMapping(value="/user/findInvestorAccount")
	@ResponseBody
	public String findInvestorAccount(HttpServletRequest request,InvestorAccount param){
		
		Response response = null;
		logger.info("查询证券账号信息param={}",param);
		try {
			List<InvestorAccount> pagelist = iInvestorManageService.findInvestorAccount(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, pagelist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toJsonString();
	}
	
	@RequestMapping(value="/user/getInvestorAccountObj")
	@ResponseBody
	public String getInvestorAccountObj(HttpServletRequest request){
		
		Integer id = Integer.valueOf(request.getParameter("id"));
		Response response = null;
		logger.info("获取券商账号所信息param={}",id);
		
		try {
			InvestorAccount investorAccount = iInvestorManageService.getInvestorAccountObj(String.valueOf(id));
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, investorAccount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toJsonString();
	}
	
	
	@RequestMapping(value="/user/findInvestorAccountProduct")
	@ResponseBody
	public String findInvestorAccountProduct(HttpServletRequest request,InvestorAccount param){
		
		Response response = null;
		logger.info("获取券商账户信息param={}",param);
		try {
			List<InvestorAccount> pagelist = iInvestorManageService.findInvestorAccount(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, pagelist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询券商--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findInvestorAccountPage")
	@ResponseBody
	public String findInvestorAccountPage(HttpServletRequest request,InvestorAccount param){
		Response response = null;
		try {
			Page<InvestorAccount> page = iInvestorManageService.findInvestorAccountPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询券商--分页系统异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	
	/**
	 * 查询券商账户
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/user/findInvestorAccountList")
	@ResponseBody
	public String findInvestorAccountList(HttpServletRequest request){
		Response response = null;
		try{
			List<InvestorAccount> investorAccounts = iInvestorManageService.findInvestorAccountList(ServiceContant.INVESTOR_SERVICE_CODE);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, investorAccounts);
		}catch(Exception e){
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("异常，e={}",e);
		}
		return response.toJsonString();
	}
}
