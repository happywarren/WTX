package com.lt.manager.controller.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Logger;

import com.lt.api.business.product.IProductApiService;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.sys.ISysConfigManageService;
import com.lt.model.sys.SysConfig;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * 系统配置controller
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="sys")
public class SysConfigManageController {
	@Autowired
	private ISysConfigManageService sysConfigManageServiceImpl;
	@Autowired
	private IProductApiService productApiService;

	
	@RequestMapping("/loadSysCfgToRedis")
	@ResponseBody
	public String loadSysCfgToRedis(){
		Response response = null;
		try {
			sysConfigManageServiceImpl.loadSysCfgToRedis();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	
	
	/**
	 * 查询开户年龄配置信息
	 * @param request
	 * @return
	 * @author yubei
	 */
	@RequestMapping("/viewOpeningAgeInfo")
	@ResponseBody	
	public String viewOpeningAgeInfo(HttpServletRequest request) {
		Response response = null;
		Map<String, Object> resultMap = FastMap.newInstance();
		try{
			SysConfig sysConfigUpperLimit = this.sysConfigManageServiceImpl.querySysConfigByCode("opening_age_upper_limit");
			SysConfig sysConfigLowLimit = this.sysConfigManageServiceImpl.querySysConfigByCode("opening_age_low_limit");
			resultMap.put("openingAgeUpperLimit", sysConfigUpperLimit);
			resultMap.put("openingAgeLowLimit", sysConfigLowLimit);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,resultMap);
		}catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 查询开户年龄配置信息
	 * @param request
	 * @return
	 * @author yubei
	 */	
	@RequestMapping("/editOpeningAgeInfo")
	@ResponseBody		
	public String editOpeningAgeInfo(HttpServletRequest request) {
		Response response = null;
		SysConfig sysConfigUpperLimit = null;
		SysConfig sysConfigLowLimit = null;
		String openingAgeUpperValue = request.getParameter("openingAgeUpperCode");
		String openingAgeLowValue = request.getParameter("openingAgeLowCode");
		String openingAgeUpperDesc = request.getParameter("openingAgeUpperDesc");
		String openingAgeLowDesc = request.getParameter("openingAgeLowDesc");
		
		try{
			sysConfigUpperLimit = new SysConfig();
			sysConfigUpperLimit.setCfgCode("opening_age_upper_limit");
			sysConfigUpperLimit.setReleaseName(openingAgeUpperDesc);
			sysConfigUpperLimit.setCfgValue(openingAgeUpperValue);
			sysConfigLowLimit = new SysConfig();
			sysConfigLowLimit.setCfgCode("opening_age_low_limit");
			sysConfigLowLimit.setCfgValue(openingAgeLowValue);
			sysConfigLowLimit.setReleaseName(openingAgeLowDesc);			
			this.sysConfigManageServiceImpl.updateSysConfig(sysConfigUpperLimit);
			this.sysConfigManageServiceImpl.updateSysConfig(sysConfigLowLimit);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
		}
		return response.toJsonString();	
	}

	
	/***
	 * 查询冬令时，夏令时
	 * @param request
	 * @return
	 */
	@RequestMapping("/findWinterSummerChange")
	@ResponseBody		
	public String findWinterSummerChange(HttpServletRequest request) {
		SysConfig config = sysConfigManageServiceImpl.querySysConfigByCode("winter_summer_change");
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,config).toJsonString();
	}
	
	/**
	 * 冬夏令时切换
	 * @param request
	 * @return
	 */
	@RequestMapping("/modifyWinterSummerChange")
	@ResponseBody		
	public String modifyWinterSummerChange(HttpServletRequest request) {
		try{
			String value = request.getParameter("cfgValue");
			if(StringTools.isEmpty(value)){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			sysConfigManageServiceImpl.modifyWinterSummerChange(value);
			//加载时间配置信息到redis
	        productApiService.loadProTimeCfgToRedis();
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		}catch(Exception e){
			return LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
		}
		
	}
	
	@RequestMapping("/viewFundInOutInfo")
	@ResponseBody	
	public String viewFundInOutInfo(HttpServletRequest request) {
		Response response = null;
		Map<String, Object> resultMap = FastMap.newInstance();
		try{
			List<String> cfgCodes = FastList.newInstance();
			cfgCodes.add("min_recharege_amount");
			cfgCodes.add("min_withdraw_amount");
			cfgCodes.add("max_withdraw_amount");
			cfgCodes.add("max_withdraw_count");
			List<SysConfig> sysConfigs = this.sysConfigManageServiceImpl.fuzzyQuerySysConfigByCfgCode(cfgCodes);
			for(SysConfig sysConfig:sysConfigs){
				resultMap.put(sysConfig.getCfgCode(), sysConfig.getCfgValue());
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,resultMap);
		}catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
		}
		
		return response.toJsonString();
	}
	
	/**
	 * 查询开户年龄配置信息
	 * @param request
	 * @return
	 * @author yubei
	 */	
	@RequestMapping("/editFundInOutInfo")
	@ResponseBody		
	public String editFundInOutInfo(HttpServletRequest request) {
		Response response = null;
		SysConfig sysConfig = null;
		List<SysConfig> sysConfigs = null;
/*		String minRecharegeAmount = request.getParameter("min_recharege_amount");
		String minWithdrawAmount = request.getParameter("min_withdraw_amount");
		String maxWithdrawAmount = request.getParameter("max_withdraw_amount");
		String maxWithdrawCount = request.getParameter("max_withdraw_count");*/
		String[] cfgCodes = new String[]{"min_recharege_amount","min_withdraw_amount","max_withdraw_amount","max_withdraw_count"};
		
		try{
			sysConfigs = FastList.newInstance();
			for(String cfgCode:cfgCodes){
				sysConfig = new SysConfig(cfgCode, request.getParameter(cfgCode));
				sysConfigs.add(sysConfig);
			}
			this.sysConfigManageServiceImpl.updateSysConfigList(sysConfigs);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
		}
		return response.toJsonString();	
	}

}
