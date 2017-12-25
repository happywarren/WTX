package com.lt.promote.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.promote.service.IStatisticTaskService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

/**
 * 推广
 * @author jingwb
 *
 */
@RequestMapping(value="/promote")
@Controller
public class PromoteController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IStatisticTaskService statisticTaskServiceImpl;
	
	/**
	 * 手动初始化推广日报和下线日报
	 * @param request
	 * @param day
	 * @return
	 */
	@RequestMapping(value="/initStatisticDayLog")
	@ResponseBody
	public String initStatisticDayLog(HttpServletRequest request,String day){
		try{
			if(StringTools.isEmpty(day)){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			statisticTaskServiceImpl.initStatisticDayLog(day);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=====手动执行初始化推广日报和下线日报异常,e={}===",e);
		}
		return LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
	}
	
	/**
	 * 手动佣金结算
	 * @param request
	 * @param day
	 * @return
	 */
	@RequestMapping(value="/balanceCommisionTask")
	@ResponseBody
	public String balanceCommisionTask(HttpServletRequest request,String day){
		try{
			if(StringTools.isEmpty(day)){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			statisticTaskServiceImpl.balanceCommisionTask(day);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=====手动佣金结算异常,e={}===",e);
		}
		return LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
	}
}
