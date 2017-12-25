package com.lt.manager.controller.user;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lt.manager.bean.user.BankCard;
import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserBaseInfoQueryVO;
import com.lt.manager.service.user.IBankCardManageService;
import com.lt.manager.service.user.IUserManageService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;




/**
 * 商品类型控制器
 * @author licy
 *
 */
@Controller
@RequestMapping(value="user")
public class BankCardManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IBankCardManageService iBankCardManageService;
	
	@Autowired
	private IUserManageService iUserManageService;
	
	/**
	 * 添加商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/addBankCard")
	@ResponseBody
	public String addBankCard(HttpServletRequest request,BankCard param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getUserId())){
				response = LTResponseCode.getCode(LTResponseCode.PR00001);
				return response.toJsonString();
			}
			logger.info("========param={}==========",JSONObject.toJSONString(param));
		    iBankCardManageService.addBankCard(param);
		    return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("添加银行卡信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑商品类型
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/user/editBankCard")
	@ResponseBody
	public String editBankCard(HttpServletRequest request){
		Response response = null;
		UserBaseInfoQueryVO param = new UserBaseInfoQueryVO();
		param.setId(request.getParameter("userId"));
		//param.setTele(request.getParameter("tele"));
		//param.setIdCardNum(request.getParameter("idCardNum"));
		//param.setOpenAccountStatus(Integer.valueOf(request.getParameter("openAccountStatus")));
		//param.setUserName(request.getParameter("userName"));
		String data=request.getParameter("bankCardInfo");
		logger.info(data);
		if(param.getId() != null && data != null){
			try {
					iUserManageService.updateUserBase(param);
					List<BankCard> bankCardList = JSONObject.parseArray(data, BankCard.class);
					BankCard bcVO = new BankCard();
					for (Iterator iter = bankCardList.iterator(); iter.hasNext();){
						bcVO = (BankCard)iter.next();
						iBankCardManageService.editBankCard(bcVO);
					}
						 response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
						 
				} catch (Exception e) {
					response = LTResponseCode.getCode(e.getMessage());
					logger.info("编辑银行卡信息异常，e={}",e);
				}
		   }
		return response.toJsonString();
	}
	
	/**
	 * 删除商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/removeBankCard")
	@ResponseBody
	public String removeBankCard(HttpServletRequest request,BankCard param){
		Response response = null;
		try {
			if(String.valueOf(param.getId()) == null){
				return response.toJsonString();
			}
			 iBankCardManageService.removeBankCard(param);
		} catch (Exception e) {
			logger.info("删除银行卡信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findBankCardByUserId")
	@ResponseBody
	public String findBankCardByUserId(HttpServletRequest request){
		Response response = null;
		List<BankCard> bankcardlist = null;
		String userId = null;
		try {
			if(request.getParameter("userId") == null){
				response = LTResponseCode.getCode(LTResponseCode.US20000);
				return response.toJsonString();
			}else{
				userId =  request.getParameter("userId");
			}
			bankcardlist = iBankCardManageService.findBankCardByUserId(userId);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, bankcardlist);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return  response.toJsonString();
	}
	
	/**
	 * 查询银行信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findBankInfo")
	@ResponseBody
	public String findBankInfo(HttpServletRequest request){
		Response response = null;
		List<BankCard> banklist = null;
		try {
			banklist = iBankCardManageService.findBankInfo();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, banklist);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return  response.toJsonString();
	}
	
	/**
	 * 查询省份信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findProv")
	@ResponseBody
	public String findProv(HttpServletRequest request){
		Response response = null;
		List<BankCard> provlist = null;
		try {
			provlist = iBankCardManageService.findProInfo();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, provlist);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return  response.toJsonString();
	}
	
	/**
	 * 根据省份查城市信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findCity")
	@ResponseBody
	public String findCity(HttpServletRequest request){
		Response response = null;
		List<BankCard> citylist = null;
		String provinceId = request.getParameter("provinceId");
		try {
			citylist = iBankCardManageService.findCityInfo(provinceId);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, citylist);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return  response.toJsonString();
	}
	
	/**
	 * 根据省份查城市信息
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/findBranchBank")
	@ResponseBody
	public String findBranchBank(HttpServletRequest request){
		Response response = null;
		List<BankCard> branchbanklist = null;
		String cityId = request.getParameter("cityId");
		String bankCode = request.getParameter("bankCode");
		try {
			branchbanklist = iBankCardManageService.findBranchBank(cityId,bankCode);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, branchbanklist);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
		}
		return  response.toJsonString();
	}
	
	
}
