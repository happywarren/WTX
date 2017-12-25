package com.lt.manager.controller.sys;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.SysNoticeParam;
import com.lt.manager.service.sys.ISysNoticeService;
import com.lt.model.sys.SysNotice;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 系统公告
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="sys")
public class SysNoticeManageController {

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private ISysNoticeService sysNoticeServiceImpl;
	
	/**
	 * 查询系统公告--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/querySysNoticePage")
	@ResponseBody
	public String querySysNoticePage(HttpServletRequest request,SysNoticeParam param){
		Response response = null;
		try {
			Page<SysNotice> page = sysNoticeServiceImpl.querySysNoticePage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询系统公告异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 新增系统公告
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/addSysNotice")
	@ResponseBody
	public String addSysNoticePage(HttpServletRequest request,SysNoticeParam param){
		Response response = null;
		try {
			sysNoticeServiceImpl.addSysNotice(param);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("新增系统公告异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除公告
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/removeSysNotice")
	@ResponseBody
	public String removeSysNoticePage(HttpServletRequest request,SysNoticeParam param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getIds())){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			sysNoticeServiceImpl.removeSysNotice(param);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("新增系统公告异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑公告
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/editSysNotice")
	@ResponseBody
	public String editSysNoticePage(HttpServletRequest request,SysNoticeParam param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			sysNoticeServiceImpl.editSysNotice(param);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("编辑公告异常,e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 查看
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/viewSysNoticePage")
	@ResponseBody
	public String viewSysNoticePage(HttpServletRequest request,SysNoticeParam param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getId())){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			SysNotice data = sysNoticeServiceImpl.viewSysNotice(param);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,data).toJsonString();
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查看公告异常,e={}",e);
		}
		return response.toJsonString();
	}
	
}
