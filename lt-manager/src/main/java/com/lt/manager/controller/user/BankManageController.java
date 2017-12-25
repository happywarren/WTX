package com.lt.manager.controller.user;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.BankVo;
import com.lt.manager.service.user.IBankManageService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * 银行类型控制器
 * 
 * @author yubei
 *
 */
@Controller
// @RequestMapping(value = "user")
public class BankManageController {
	private Logger logger = LoggerTools.getInstance(getClass());

	@Autowired
	private IBankManageService iBankManageService;

	/**
	 * 添加银行信息
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/user/addBank")
	@ResponseBody
	public String addBank(HttpServletRequest request) {
		Response response = null;

		try {
			@SuppressWarnings("rawtypes")
			Enumeration en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param) + ",");
			}
			logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}

		try {
			BankVo bankVo = null;
			String[] channelArr = request.getParameter("channels").split(",");
			String bankName = request.getParameter("bankName");
			String bankPic = request.getParameter("bankPic");
			BankVo bankInfo = new BankVo();
			bankInfo.setBankName(bankName);
			bankInfo.setBankPic(bankPic);
			String bankCode = this.iBankManageService.addBankInfo(bankInfo);
			for (String channelId : channelArr) {
				double singleLimit = 0;
				if(StringTools.isNotEmpty(request.getParameter("singleLimit_" + channelId))){
					singleLimit =	DoubleTools.parseDoulbe(request.getParameter("singleLimit_" + channelId));
				}
				double dailyLimit = 0; 
				if(StringTools.isNotEmpty(request.getParameter("dailyLimit_" + channelId))){
					dailyLimit = DoubleTools.parseDoulbe(request.getParameter("dailyLimit_" + channelId));
				}
				bankVo = new BankVo();
				bankVo.setBankCode(bankCode);
				bankVo.setChannelId(channelId);
				bankVo.setBankName(bankName);
				bankVo.setBankPic(bankPic);
				bankVo.setSingleLimit(singleLimit);
				bankVo.setDailyLimit(dailyLimit);
				this.iBankManageService.addBankVo(bankVo);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, "处理成功!");

		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【添加银行异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 修改银行信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/modifyBank")
	@ResponseBody
	public String modifyBank(HttpServletRequest request) {
		Response response = null;

		try {
			@SuppressWarnings("rawtypes")
			Enumeration en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param) + ",");
			}
			logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}

		try {
			BankVo bankInfo = new BankVo();
			String bankCode = request.getParameter("bankCode");
			String bankName = request.getParameter("bankName");
			String bankPic = request.getParameter("bankPic");
			bankInfo.setBankCode(bankCode);
			bankInfo.setBankName(bankName);
			bankInfo.setBankPic(bankPic);
			//this.iBankManageService.modifyBankInfo(bankInfo);
			BankVo bankVo = null;
			String[] channelArr = request.getParameter("channels").split(",");
			for (String channelId : channelArr) {
				double singleLimit = 0;
				if(StringTools.isNotEmpty(request.getParameter("singleLimit_" + channelId))){
					singleLimit =	DoubleTools.parseDoulbe(request.getParameter("singleLimit_" + channelId));
				}
				double dailyLimit = 0; 
				if(StringTools.isNotEmpty(request.getParameter("dailyLimit_" + channelId))){
					dailyLimit = DoubleTools.parseDoulbe(request.getParameter("dailyLimit_" + channelId));
				}
				
				//double singleLimit = DoubleTools.parseDoulbe(request.getParameter("singleLimit_" + channelId));
				//double dailyLimit = DoubleTools.parseDoulbe(request.getParameter("dailyLimit_" + channelId));
				bankVo = new BankVo();
				bankVo.setChannelId(channelId);
				bankVo.setBankCode(bankCode);
				bankVo.setSingleLimit(singleLimit);
				bankVo.setDailyLimit(dailyLimit);
				this.iBankManageService.modifyBankVo(bankVo);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,"操作成功");

		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【添加银行异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 修改银行信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/removeBank")
	@ResponseBody
	public String removeBank(HttpServletRequest request) {
		Response response = null;
		try {
			BankVo bankVo = new BankVo();
			this.iBankManageService.addBankVo(bankVo);
			response = LTResponseCode.getCode(LTResponseCode.US20000);

		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【修改银行异常】", e);
		}
		return response.toJsonString();
	}

	@RequestMapping(value = "/user/viewBankInfo")
	@ResponseBody
	public String viewBankInfo(HttpServletRequest request) {
		Response response = null;
		try {
			@SuppressWarnings("rawtypes")
			Enumeration en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param));
			}
			logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}

		try {
			String bankCode = request.getParameter("bankCode");
			List<BankVo> bankVos = this.iBankManageService.queryBankVoByBankCode(bankCode);
			if (bankVos != null && bankVos.size() == 1) {

				response = new Response(LTResponseCode.SUCCESS, "查询成功", bankVos);
			} else {
				response = LTResponseCode.getCode(LTResponseCode.GE00001);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【查询银行列表异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 查询银行信息列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/viewBankList")
	@ResponseBody
	public String viewBankList(HttpServletRequest request) {
		Response response = null;
		try {
			@SuppressWarnings("rawtypes")
			Enumeration en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param));
			}
			logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}

		try {
			int page = 1;
			int rows = 10;
			if (StringTools.isNotEmpty(request.getParameter("page"))) {
				page = Integer.valueOf(request.getParameter("page"));
			}
			if (StringTools.isNotEmpty(request.getParameter("rows"))) {
				rows = Integer.valueOf(request.getParameter("rows"));
			}
			BankVo bankVo = new BankVo();
			bankVo.setPage(page);
			bankVo.setRows(rows);
			//List<BankVo> bankVos = this.iBankManageService.queryBankInfoList(bankVo);
			List<Map<String, Object>> bankVos =this.iBankManageService.queryBankInfoList(bankVo); 
			response = new Response(LTResponseCode.SUCCESS, "查询成功", bankVos);

			Page<Map<String, Object>> bankPage = new Page<Map<String, Object>>();
			bankPage.setPageNum(page);
			bankPage.setPageSize(rows);
			bankPage.addAll(bankVos);
			bankPage.setTotal(this.iBankManageService.queryBankVoCount());
			
			logger.info("【服务输出】" + JqueryEasyUIData.init(bankPage));
			return JqueryEasyUIData.init(bankPage);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【查询银行列表异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 查询单个银行信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/viewBank")
	@ResponseBody
	public String viewBank(HttpServletRequest request) {
		Response response = null;
		try {
			String bankCode = request.getParameter("bankCode");
			Map<String, Object> resultMap = FastMap.newInstance();
			BankVo bankVo = this.iBankManageService.queryBankVo(bankCode);
			List<BankVo> bankVos = this.iBankManageService.queryChannelByBankCode(bankCode);
			resultMap.put("bankCode", bankVo.getBankCode());
			resultMap.put("bankName", bankVo.getBankName());
			resultMap.put("bankPic", bankVo.getBankPic());
			List<Map<String, Object>> list = FastList.newInstance();
			StringBuffer stringBuffer = new StringBuffer();
			for (BankVo bankVo2 : bankVos) {
				Map<String, Object> map = FastMap.newInstance();
				String channelId = bankVo2.getChannelId();
				map.put("channelId", channelId);
				map.put("channelName", bankVo2.getChannelName());
				map.put("singleLimit", bankVo2.getSingleLimit());
				map.put("dailyLimit", bankVo2.getDailyLimit());
				list.add(map);

				if (stringBuffer.toString().length() > 0) {
					stringBuffer.append(",");
				}
				stringBuffer.append(bankVo2.getChannelId());
			}
			resultMap.put("channels", stringBuffer.toString());
			resultMap.put("resultList", list);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【查询银行列表异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 查询通道信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/viewChannelList")
	@ResponseBody
	public String viewChannelList(HttpServletRequest request) {

		Response response = null;
		try {
			@SuppressWarnings("rawtypes")
			Enumeration en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param));
			}
			logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}

		try {
			List<BankVo> channelList = this.iBankManageService.queryBankChannelList();
			if (channelList != null && channelList.size() > 0) {
				Map<String, Object> resultMap = FastMap.newInstance();
				StringBuffer stringBuffer = new StringBuffer();
				for (int i = 0; i < channelList.size(); i++) {
					stringBuffer.append(channelList.get(i).getChannelId());
					if (i < channelList.size() - 1) {
						stringBuffer.append(",");
					}
				}
				resultMap.put("channels", stringBuffer.toString());
				resultMap.put("resultList", channelList);
				response = new Response(LTResponseCode.SUCCESS, "查询成功", resultMap);
			} else {
				response = LTResponseCode.getCode(LTResponseCode.GE00001);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【 查询系统渠道列表异常】", e);
		}
		
		logger.info("【服务输出】" + response.toJsonString());
		
		return response.toJsonString();
	}
	
	
	/**
	 * 查询通道信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/viewChannelPriorityList")
	@ResponseBody
	public String viewChannelPriorityList(HttpServletRequest request) {
		
		Response response = null;
		try {
			@SuppressWarnings("rawtypes")
			Enumeration en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param));
			}
			logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}
		
		try {
			BankVo channelList = this.iBankManageService.queryBankChannelPriorityList();
			if (channelList != null ) {
				Map<String, Object> resultMap = FastMap.newInstance();
				resultMap.put("resultList", channelList);
				response = new Response(LTResponseCode.SUCCESS, "查询成功", channelList);
			} else {
				response = LTResponseCode.getCode(LTResponseCode.GE00001);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【 查询系统渠道列表异常】", e);
		}
		
		logger.info("【服务输出】" + response.toJsonString());
		
		return response.toJsonString();
	}
	
	@RequestMapping(value = "/user/initUserChargeMapper")
	@ResponseBody
	public String initUserChargeMapper(String channelCode) {
		Response response = new Response(LTResponseCode.SUCCESS, "初始化成功");
		iBankManageService.initUserChargeMapper(channelCode);
		return response.toJsonString();
	}
}
