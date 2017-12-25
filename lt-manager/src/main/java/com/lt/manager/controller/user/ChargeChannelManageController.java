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
import com.lt.manager.bean.user.ChargeChannelVo;
import com.lt.manager.bean.user.RechargeGroupVO;
import com.lt.manager.service.user.IChargeChannelManageService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

import javolution.util.FastMap;

/**
 * 银行类型控制器
 * 
 * @author licy
 *
 */
@Controller
public class ChargeChannelManageController {
	private Logger logger = LoggerTools.getInstance(getClass());

	@Autowired
	private IChargeChannelManageService iChargeChannelManageService;

	/**
	 * 添加银行信息
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/user/viewChargeChannelList1")
	@ResponseBody
	public String viewChargeChannelList1(HttpServletRequest request) {
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
			List<ChargeChannelVo> chargeChannelVos = this.iChargeChannelManageService.queryChargeChannelList();
			StringBuffer channels = new StringBuffer();
			Map<String, Object> resultMap = FastMap.newInstance();
			for (ChargeChannelVo chargeChannelVo : chargeChannelVos) {
				String channelId = chargeChannelVo.getChannelId();
				if (channels.toString().length() > 0) {
					channels.append(",");
				}
				resultMap.put("channelName_" + channelId, chargeChannelVo.getChannelName());
				resultMap.put("is_start_" + channelId, chargeChannelVo.getIsStart());
				resultMap.put("is_default_" + channelId, chargeChannelVo.getIsDefault());
				resultMap.put("priority_" + channelId, chargeChannelVo.getPriority());
				resultMap.put("createTime_" + channelId, DateTools.parseToDefaultDateTimeString(chargeChannelVo.getCreateTime()));
			}
			resultMap.put("channels", channels.toString());
			response = new Response(LTResponseCode.SUCCESS, "查询成功", resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【添加银行异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 查询渠道名称
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/user/viewChargeChannelList")
	@ResponseBody
	public String viewChargeChannelList(HttpServletRequest request) {
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
			List<ChargeChannelVo> chargeChannelVos = this.iChargeChannelManageService.queryChargeChannelList();
			response = new Response(LTResponseCode.SUCCESS, "查询成功", chargeChannelVos);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【添加银行异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}
	
	@RequestMapping(value = "/user/modifyChargeChannel")
	@ResponseBody
	public String modifyChargeChannel(HttpServletRequest request) {
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
			String channelId = request.getParameter("channelId");
			ChargeChannelVo chargeChannelVo = new ChargeChannelVo();
			chargeChannelVo.setChannelId(channelId);
			String isStart = request.getParameter("isStart");
			String isDefault = request.getParameter("isDefault");
			String dailyLimitCount = request.getParameter("dailyLimitCount");
			String weight = request.getParameter("weight");
			String mchId = request.getParameter("mchId");
			String secretKey = request.getParameter("secretKey");
			String channelName = request.getParameter("channelName");

			if (StringTools.isNotEmpty(isStart)) {
				chargeChannelVo.setIsStart(Integer.valueOf(isStart));
			}
			if (StringTools.isNotEmpty(isDefault)) {
				chargeChannelVo.setIsDefault(Integer.valueOf(isDefault));
			}
			if (StringTools.isNotEmpty(dailyLimitCount)) {
				chargeChannelVo.setDailyLimitCount(Integer.valueOf(dailyLimitCount));
			}
			if ("".equals(dailyLimitCount)) {
				chargeChannelVo.setDailyLimitCount(0);
			}
			if (StringTools.isNotEmpty(weight)) {
				chargeChannelVo.setWeight(Integer.valueOf(weight));
			}
			chargeChannelVo.setMchId(mchId);
			chargeChannelVo.setSecretKey(secretKey);
			chargeChannelVo.setChannelName(channelName);
			chargeChannelVo.setRemark(channelName);
			this.iChargeChannelManageService.modifyChargeChannel(chargeChannelVo);
			response = new Response(LTResponseCode.SUCCESS, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【修改充值渠道信息异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 修改优先级
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/modifyChargeChannelPriority")
	@ResponseBody
	public String modifyChargeChannelPriority(HttpServletRequest request) {
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
			String channelId = request.getParameter("channelId");
			String priority = request.getParameter("priority");
			ChargeChannelVo chargeChannelVo = new ChargeChannelVo();
			chargeChannelVo.setChannelId(channelId);
			chargeChannelVo.setPriority(Integer.valueOf(priority));
			this.iChargeChannelManageService.modifyChargeChannelPriority(chargeChannelVo);
			response = new Response(LTResponseCode.SUCCESS, "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【修改优先级异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 查询用户渠道信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/viewUserChargeChannel")
	@ResponseBody
	public String viewUserChargeChannel(HttpServletRequest request) {
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
			String userId = request.getParameter("userId");
			Map<String, Object> resultMap = this.iChargeChannelManageService.getUserChargeChannelInfo(userId);
			response = new Response(LTResponseCode.SUCCESS, "查询成功", resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【修改优先级异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 修改用户渠道信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/modifyUserChargeChannel")
	@ResponseBody
	public String modifyUserChargeChannel(HttpServletRequest request) {
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
			String userId = request.getParameter("userId");
			String supportChannels = request.getParameter("supportChannels");
			String unsupportChannels = request.getParameter("unsupportChannels");
			this.iChargeChannelManageService.modifyUserChargeChannelInfo(userId, supportChannels, unsupportChannels);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【修改用户充值渠道异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 新增充值渠道信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/addChargeChannel")
	@ResponseBody
	public String addChargeChannel(HttpServletRequest request) {
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
			String channelName = request.getParameter("channelName");
			String mchId = request.getParameter("mchId");
			String secretKey = request.getParameter("secretKey");
			String groupId = request.getParameter("groupId");
			int weight = 0;
			int dailyLimitCount = 0;
			if (StringTools.isNotEmpty(request.getParameter("dailyLimitCount"))) {
				dailyLimitCount = Integer.valueOf(request.getParameter("dailyLimitCount"));
			}
			if(StringTools.isNotEmpty(request.getParameter("weight"))){
				weight = Integer.valueOf(request.getParameter("weight"));
			}
			ChargeChannelVo chargeChannelVo = new ChargeChannelVo();
			chargeChannelVo.setChannelName(channelName);
			chargeChannelVo.setMchId(mchId);
			chargeChannelVo.setSecretKey(secretKey);
			chargeChannelVo.setGroupId(groupId);
			chargeChannelVo.setDailyLimitCount(dailyLimitCount);
			chargeChannelVo.setWeight(weight);
			chargeChannelVo.setRemark(channelName);
			this.iChargeChannelManageService.addChargeChannel(chargeChannelVo);
			response = new Response(LTResponseCode.SUCCESS, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【新增充值渠道异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	/**
	 * 查詢充值渠道組信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/viewRechargeGroupList")
	@ResponseBody
	public String viewRechargeGroupList(HttpServletRequest request) {
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
			List<RechargeGroupVO> rechargeGroupVOs = this.iChargeChannelManageService.queryRechargeGroupList();
			response = new Response(LTResponseCode.SUCCESS, "查询成功", rechargeGroupVOs);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			logger.info("【查询充值渠道组异常】", e);
		}
		logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}

	@RequestMapping({ "/user/viewChargeChannel" })
	@ResponseBody
	/**
	 * 查询渠道信息列表
	 * @param request
	 * @return
	 */
	public String viewChargeChannel(HttpServletRequest request) {
		Response response = null;
		try {
			Enumeration<?> en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param) + ",");
			}
			this.logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}
		try {
/*			int page = 1;
			int rows = 10;
			if (StringTools.isNotEmpty(request.getParameter("page"))) {
				page = Integer.valueOf(request.getParameter("page")).intValue();
			}
			if (StringTools.isNotEmpty(request.getParameter("rows"))) {
				rows = Integer.valueOf(request.getParameter("rows")).intValue();
			}*/
			List<ChargeChannelVo> chargeChannelVos = this.iChargeChannelManageService.queryChargeChannelList();
			Map<String, Object> resultMap = FastMap.newInstance();
			resultMap.put("page", Integer.valueOf(1));
			resultMap.put("rows", Integer.valueOf(10));
			Page<ChargeChannelVo> bankPage = new Page<ChargeChannelVo>();
			bankPage.setPageNum(10);
			bankPage.setPageSize(10);
			bankPage.addAll(chargeChannelVos);
			bankPage.setTotal(chargeChannelVos.size());
			this.logger.info("【服务输出】" + JqueryEasyUIData.init(bankPage));
			response = new Response(LTResponseCode.SUCCESS, "查询成功", resultMap);
			return JqueryEasyUIData.init(bankPage);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			this.logger.info("【查询渠道信息异常】", e);
		}
		this.logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}
	
	
	/**
	 * 删除渠道信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/user/removeChargeChannel" })
	@ResponseBody
	public String removeChargeChannel(HttpServletRequest request) {
		Response response = null;
		try {
			Enumeration<?> en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param) + ",");
			}
			this.logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}
		try {
			String channelId = request.getParameter("channelId");
			if(StringTools.isEmpty(channelId)){
				return new Response(LTResponseCode.ER400, "渠道id不能为空").toJsonString();
			}
			this.iChargeChannelManageService.deleteChargeChannelInfo(channelId);
			Map<String, Object> resultMap = FastMap.newInstance();
			response = new Response(LTResponseCode.SUCCESS, "删除成功", resultMap);
			return response.toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			response.setMsg("删除渠道信息异常");
			this.logger.info("【删除渠道信息异常】", e);
		}
		this.logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}	
	
	
	/**
	 * 刷新渠道信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/user/refreshChargeChannel" })
	@ResponseBody
	public String refreshChargeChannel(HttpServletRequest request) {
		Response response = null;
		try {
			Enumeration<?> en = request.getParameterNames();
			StringBuffer stringBuffer = new StringBuffer();
			while (en.hasMoreElements()) {
				String param = en.nextElement().toString();
				stringBuffer.append(param + ":" + request.getParameter(param) + ",");
			}
			this.logger.info("【服务输入】" + stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("【获取服务输入信息出错】");
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			return response.toJsonString();
		}
		try {
			this.iChargeChannelManageService.refreshConfig();
			response = new Response(LTResponseCode.SUCCESS, "刷新成功");
			return response.toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			response.setMsg("刷新渠道异常");
			this.logger.info("【刷新渠道信息异常】", e);
		}
		this.logger.info("【服务输出】" + response.toJsonString());
		return response.toJsonString();
	}		
}
