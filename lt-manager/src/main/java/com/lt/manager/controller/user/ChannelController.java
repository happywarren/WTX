package com.lt.manager.controller.user;

import javax.servlet.http.HttpServletRequest;

import com.lt.util.error.LTException;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.bean.user.Channel;
import com.lt.manager.service.user.IChannelService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.model.Response;

/**
 * 
 * <br>
 * <b></b>ChannelController<br>
 * <br>
 */
@Controller
@RequestMapping(value = "channel")
public class ChannelController {
	private Logger logger = LoggerTools.getInstance(getClass());

	@Autowired
	private IChannelService iChannelService;

	/**
	 * 
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/channel/list")
	@ResponseBody
	public String listChannel(HttpServletRequest request, Channel param)
			throws Exception {
		Response response = null;
		logger.info("查询信息渠道信息,{}", param);
		try {
			Page<Channel> page = iChannelService.queryChannelPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询渠道信息异常，e={}", e);
		}
		return response.toJsonString();
	}

	/**
	 *
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/channel/listAll")
	@ResponseBody
	public String listAllChannel(HttpServletRequest request, Channel param)
			throws Exception {
		Response response = null;
		logger.info("查询信息渠道信息,{}", param);
		try {
			return LTResponseCode.getCode(
					LTResponseCode.SUCCESS, iChannelService.queryChannel(param)
			).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询渠道信息异常，e={}", e);
		}
		return response.toJsonString();
	}

	/**
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/channel/add")
	@ResponseBody
	public String addChannel(HttpServletRequest request, Channel param) {

		Response response = null;
		logger.info("添加渠道信息param={}", param);
		try {
			if(StringTools.isEmpty(param.getBrandId())){
				throw new LTException(LTResponseCode.MA00030);
			}
			Staff staff = JSON.parseObject((String) request.getSession()
					.getAttribute("staff"), Staff.class);
			param.setCreateStaffId(staff.getId());
			param.setModifyStaffId(staff.getId());
			iChannelService.addChannel(param);
			String channelId = param.getCode();
			String payChannels = request.getParameter("payChannels");
			this.iChannelService.addPayChannelRelation(channelId, payChannels);
			
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("添加渠道信息异常，e={}", e);
		}
		return response.toJsonString();
	}

	/**
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/channel/edit")
	@ResponseBody
	public String editChannel(HttpServletRequest request, Channel param) {

		Response response = null;
		logger.info("修改渠道信息param={}", param);
		try {
			if(StringTools.isEmpty(param.getBrandId())){
				throw new LTException(LTResponseCode.MA00030);
			}
			Staff staff = JSON.parseObject((String) request.getSession()
					.getAttribute("staff"), Staff.class);
			param.setModifyStaffId(staff.getId());
			iChannelService.editChannel(param);
			String payChannels = request.getParameter("payChannels");
			this.iChannelService.editPayChannelRelation(param.getCode(), payChannels);
			
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改渠道信息异常，e={}", e);
		}
		return response.toJsonString();
	}

	/**
	 * 用户删除
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/channel/remove")
	@ResponseBody
	public String removeChannel(HttpServletRequest request, String ids) {

		Response response = null;
		logger.info("删除渠道信息param={}", ids);

		String[] channelIDs = ids.split(",");
		try {
			for (String id : channelIDs) {
				iChannelService.removeChannel(Integer.valueOf(id));
			}
			String channelId = request.getParameter("code");
			this.iChannelService.removePayChannelRelation(channelId);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("删除渠道信息异常，e={}", e);
		}
		return response.toJsonString();
	}

	/**
	 * 用户删除
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/channel/getChannel")
	@ResponseBody
	public String getChannel(Integer id) {

		Response response = null;
		logger.info("查询param={}", id);

		try {
			Channel channel = iChannelService.getChannel(id);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, channel);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("获取渠道信息异常，e={}", e);
		}

		return response.toJsonString();
	}

	
	
	/**
	 * （未使用）
	 * 新增支付渠道关联关系
	 * @author yubei
	 */
	@RequestMapping(value = "/channel/addPayChannelRelation")
	@ResponseBody
	public String addPayChannelRelation(Integer id) {

		Response response = null;
		logger.info("查询param={}", id);

		try {
			Channel channel = iChannelService.getChannel(id);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, channel);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("获取渠道信息异常，e={}", e);
		}

		return response.toJsonString();
	}
	
	
	/**
	 * （未使用）
	 * 编辑支付渠道关联关系
	 * @author yubei
	 */
	@RequestMapping(value = "/channel/editPayChannelRelation")
	@ResponseBody	
	public String editPayChannelRelation(HttpServletRequest request) {
		
		Response response = null;
		
		try {
			String channelId = request.getParameter("code");
			String payChannels = request.getParameter("payChannels");
			this.iChannelService.editPayChannelRelation(channelId, payChannels);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("获取渠道信息异常，e={}", e);
		}
		
		return response.toJsonString();
	}
}
