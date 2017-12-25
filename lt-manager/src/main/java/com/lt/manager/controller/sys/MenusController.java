package com.lt.manager.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.MenusVO;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.dao.sys.MenusDao;
import com.lt.manager.service.sys.IMenusService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 
 *
 * 描述:菜单管理 增删改查
 *
 * @author 郭达望
 * @created 2015年6月2日 下午3:24:49	
 * @since v1.0.0
 */
@Controller
@RequestMapping(value = "/menus")
public class MenusController {
	Logger logger = LoggerTools.getInstance(this.getClass());

	@Autowired
	private MenusDao menusDao;

	@Autowired
	private IMenusService menusService;

	/**
	 * 
	 *
	 * 描述:新增Menus
	 *
	 * @Menusor 郭达望
	 * @created 2015年6月2日 下午3:08:59
	 * @since v1.0.0
	 * @param name
	 * @param url
	 * @param desc
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/saveMenus")
	public String saveMenus(String name, String desc, String pid,
			String msort, String model, HttpServletRequest request, String url) {
		Response response = null;
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		logger.info("新增菜单  进入参数 name={},desc={},userSession={}", name, desc,
				staff.getId());
		try {
			if (!StringTools.isNotEmpty(name)) {
				logger.error("name 为空");
				throw new LTException(LTResponseCode.MA00004);
			}
			if (!StringTools.isNotEmpty(url)) {
				logger.error("url 为空");
				throw new LTException(LTResponseCode.MA00004);
			}
			if (!NumberUtils.isNumber(pid)) {
				logger.error("pid 为空");
				throw new LTException(LTResponseCode.MA00004);
			}
			Menus menus = new Menus();
			menus.setUrl(url.trim());
			menus.setName(name.trim());
			menus.setDesc(desc);
			menus.setModel(Integer.valueOf(model));
			menus.setMsort(0);
			menus.setCreateUserId(staff.getId());
			menus.setPid(Integer.valueOf(pid));
			menusService.saveMenus(menus);
			logger.info("员工ID = " + staff.getId() + " ， 保存操作菜单成功  ");
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 保存操作菜单异常  ", e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:根据id删除菜单 不提供批量删除
	 *
	 * @Menusor 郭达望
	 * @created 2015年6月2日 下午3:08:41
	 * @since v1.0.0
	 * @param id
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/delMenus")
	public String delMenus(String id, HttpServletRequest request) {
		Response response = null;
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		logger.info("删除菜单  进入参数 id={},userSession={}", id, staff.getId());
		if (!StringTools.isNumeric(id)) {
			throw new LTException(LTResponseCode.MA00004);
		}
		try {
			menusService.removeById(Integer.valueOf(id));
			logger.info("员工ID = " + staff.getId() + " ， 删除操作菜单成功 id= " + id);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 删除操作菜单异常 id= " + id, e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:Menus修改
	 *
	 * @Menusor 郭达望
	 * @created 2015年6月2日 下午3:08:28
	 * @since v1.0.0
	 * @param id
	 * @param name
	 * @param url
	 * @param desc
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/editMenus")
	public String editMenus(String id, String name, String pid, String msort,
			String model, String desc, HttpServletRequest request, String url) {
		Response response = null;
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		logger.info("修改菜单  进入参数 id={},name={},desc={},userSession={}", id,
				name, desc, staff.getId());
		try {
			if (!StringTools.isNumeric(id)) {
				throw new LTException(LTResponseCode.MA00004);
			}
			if (!NumberUtils.isNumber(pid)) {
				logger.error("pid 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			if (!StringTools.isNotEmpty(name)) {
				logger.error("name 为空");
				throw new LTException(LTResponseCode.MA00004);
			}
	
			if (!StringTools.isNotEmpty(url)) {
				logger.error("url 为空");
				throw new LTException(LTResponseCode.MA00004);
			}
			Menus menus = new Menus();
			menus.setId(Integer.valueOf(id));
			menus.setName(name.trim());
			menus.setPid(Integer.valueOf(pid));
			menus.setDesc(desc);
			menus.setUrl(url);
			menus.setModel(Integer.valueOf(model));
			menus.setMsort(0);
			menus.setModifyUserId(staff.getId());
			menusService.updateMenus(menus);
			logger.info("员工ID = " + staff.getId() + " ， 编辑操作菜单成功  id= " + id);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 编辑操作菜单异常  id= " + id,
					e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:Menus列表
	 *
	 * @Menusor 郭达望
	 * @created 2015年6月2日 下午3:08:11
	 * @since v1.0.0
	 * @param name
	 * @param rows
	 * @param page
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/selectMenus")
	public String selectMenus(String pid, String menusName, Integer model,Integer rows,
			Integer page, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		logger.info("Menus列表  进入参数 name={},userSession={}", menusName, staff.getId());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (null == rows || rows == 0) {
			rows = 10;
		}
		if (null == page || page == 0) {
			page = 1;
		}
		paramMap.put("name", menusName);
		paramMap.put("model", model);
		paramMap.put("pid", pid);
		Response response = null;
		try {
			Page<Menus> menus = menusService.selectMenus(paramMap, rows,page);
			logger.info("员工ID = " + staff.getId() + " ， 查询操作菜单列表成功    总共有"
					+ menus.getTotal() + "条");
			return JqueryEasyUIData.init(menus);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 查询操作菜单列表异常   ", e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:获取父级菜单
	 *
	 * @author 郭达望
	 * @created 2015年6月4日 上午11:45:37
	 * @since v1.0.0
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/selectMenusParent")
	public String selectMenusParent(String pid, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		logger.info("获取父级菜单Menus列表  ");
		Response response = null ; 
		try {
			if (!StringTools.isNumeric(pid)) {
				pid = "-1";
			}
			List<Menus> Menus = menusService.selectMenusParent(Integer
					.valueOf(pid));
			logger.info("员工ID = " + staff.getId() + " ， 获取父级菜单成功    总共有"
					+ Menus.size() + "条");
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,Menus);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 获取父级菜单异常   ", e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:Menus 详情
	 *
	 * @Menusor 郭达望
	 * @created 2015年6月2日 下午3:07:59
	 * @since v1.0.0
	 * @param id
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/selectMenusById")
	public String selectMenusById(String id, HttpServletRequest request) {
		Response response = null;
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		logger.info("菜单详情  进入参数 id={},userSession={}", id, staff.getId());
		if (!StringTools.isNumeric(id)) {
			logger.error("id 为空或者不是数字");
			throw new LTException(LTResponseCode.MA00004);
		}
		try {
			Menus menus = menusService.selectMenusById(Integer.valueOf(id));
			logger.info("员工ID = " + staff.getId() + " ， 查询操作菜单成功    id= " + id);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,menus);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 查询操作菜单异常   id= " + id,
					e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:菜单分配权限
	 *
	 * @author 郭达望
	 * @created 2015年6月2日 下午3:43:37
	 * @since v1.0.0
	 * @param id
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/authOfDist")
	public String authorityOfDistribution(String id, String ids,
			HttpServletRequest request) {
		Response response = null;
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		logger.info("菜单分配权限  进入参数 id={},userSession={}", id, staff.getId());
		try {
			if (!StringTools.isNumeric(id)) {
				logger.error("菜单id为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			if (!StringTools.isNotEmpty(ids)) {
				logger.error("权限ids为空");
				throw new LTException(LTResponseCode.MA00004);
			}
			menusService.authOfDist(id, ids);
			logger.info("员工ID = " + staff.getId() + " ， 操作菜单分配权限成功   ");
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 操作菜单分配权限异常   ", e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:获得所有菜单权限
	 *
	 * @author 郭达望
	 * @created 2015年6月2日 下午4:09:26
	 * @since v1.0.0
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/menusAuthList")
	public String getMenusAuthList(String staffId, String roleId,
			HttpServletRequest request) {
		Response response = null;
		try {
			String returnJson = "";
			if (StringTools.isNumeric(roleId)) {
				returnJson = menusService.getMenusAuthListByRoleId(Integer
						.valueOf(roleId));
			} else {
				returnJson = menusService.getMenusAuthList();
			}
			String str =  returnJson.replaceAll("\"name\"", "\"text\"");
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,str);
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:获得权限相关的菜单
	 *
	 * @author 郭达望
	 * @created 2015年6月2日 下午4:09:26
	 * @since v1.0.0
	 * @param userSession
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/authMenus")
	public String getAuthMenus(Integer authId, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession()
				.getAttribute("staff"), Staff.class);
		Response response = null;
		try {
			if (authId == null) {
				throw new LTException("获得权限所属菜单异常");
			}
			// 获取所有菜单，已选部分标记
			List<MenusVO> res = menusService.selectMenusOfAuth(authId);
			logger.info("员工ID = " + staff.getId() + " ， 获得authId：" + authId
					+ "的菜单成功   ");
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,res);
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ，获得权限所属菜单异常   ", e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}

	/**
	 * 
	 *
	 * 描述:获得所有二级菜单
	 *
	 * @author 郭达望
	 * @created 2015年8月13日 下午3:41:28
	 * @since v1.0.0
	 * @return
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllSecondaryMenus")
	public String getAllSecondaryMenus() {
		List<Menus> Menus = menusService.selectAllSecondaryMenus();
		return JSON.toJSONString(Menus);
	}
}
