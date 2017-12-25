package com.lt.manager.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.Role;
import com.lt.manager.bean.sys.RoleVo;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.dao.sys.RoleDao;
import com.lt.manager.service.sys.IMenusService;
import com.lt.manager.service.sys.IRoleService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;

/**
 * 
 *
 * 描述:角色管理
 *
 * @Roleor  郭达望
 * @created 2015年6月2日 下午3:18:10
 * @since   v1.0.0
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController {
	Logger logger = LoggerTools.getInstance(this.getClass());

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IMenusService menusService;

	/**
	 * 
	 *	
	 * 描述:新增Role
	 *
	 * @Roleor  郭达望
	 * @created 2015年6月2日 下午3:08:59
	 * @since   v1.0.0 
	 * @param name
	 * @param url
	 * @param desc
	 * @param userSession
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRole")
	public String saveRole(String name, String desc, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		logger.info("修改角色  进入参数 name={},desc={},userSession={}", name, desc, staff.getId());
		if (!StringTools.isNotEmpty(name)) {
			logger.error("name 为空");
			throw new LTException(LTResponseCode.MA00004);
		}
		try {
			Role Role = new Role(name,desc,staff.getId());
			roleService.saveRole(Role);
			logger.info("员工ID = " + staff.getId() + " ， 保存操作角色成功  ");
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 保存操作角色异常  id= ", e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}

	/**
	 * 
	 *	
	 * 描述:根据id删除角色 不提供批量删除
	 *
	 * @Roleor  郭达望
	 * @created 2015年6月2日 下午3:08:41
	 * @since   v1.0.0 
	 * @param id
	 * @param userSession
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/delRole")
	public String delRole(String id, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		logger.info("删除角色  进入参数 id={},userSession={}", id, staff.getId());
		try {
			if (!StringTools.isNumeric(id)) {
				logger.error("id 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			int i = roleService.removeById(Integer.valueOf(id));
			if (i == 0) {
				logger.info("员工ID = " + staff.getId() + " ， 删除操作角色失败 该角色被使用，存在用户角色关联关系不能删除");
//				return JSONTools.jsonResult(JSONTools.ERROR, "删除操作角色失败，该角色被使用，存在用户角色关联关系不能删除");
				throw new LTException(LTResponseCode.MA00018);
			}
			logger.info("员工ID = " + staff.getId() + " ， 删除操作角色成功 id= " + id);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 删除操作角色异常 id= " + id, e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}

	/**
	 * 
	 *	
	 * 描述:Role修改
	 *
	 * @Roleor  郭达望
	 * @created 2015年6月2日 下午3:08:28
	 * @since   v1.0.0 
	 * @param id
	 * @param name
	 * @param url
	 * @param desc
	 * @param userSession
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/editRole")
	public String editRole(String id, String name, String desc, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		logger.info("修改角色  进入参数 id={},name={},desc={},userSession={}", id, name, desc, staff.getId());
		try {
			if (!StringTools.isNumeric(id)) {
				logger.error("id 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			if (!StringTools.isNotEmpty(name)) {
				logger.error("name 为空");
				throw new LTException(LTResponseCode.MA00004);
			}
			Role Role = new Role(Integer.valueOf(id),name,desc);
			roleService.updateRole(Role);
			logger.info("员工ID = " + staff.getId() + " ， 编辑操作角色成功  id= " + id);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 编辑操作角色异常  id= " + id, e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}

	}

	/**
	 * 
	 *	
	 * 描述:Role列表
	 *
	 * @Roleor  郭达望
	 * @created 2015年6月2日 下午3:08:11
	 * @since   v1.0.0 
	 * @param name
	 * @param rows
	 * @param page
	 * @param userSession
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/selectRole")
	public String selectRole(String name, Integer rows, Integer page, HttpServletRequest request) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			if (null == rows || rows == 0 ) {
				rows = 10;
			}
			if (null == page || page == 0) {
				page = 1;
			}
			paramMap.put("name", name);
			Page<RoleVo> role = roleService.selectRole(paramMap, rows ,page);
			return JqueryEasyUIData.init(role);
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/selectRoleList")
	public String selectRoleList() {
		try {
			List<Role> role = roleService.selectAllRole();
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,role).toJsonString();
		} catch (Exception e) {
			logger.error("查询操作角色列表异常  ", e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}
	
	/**
	 * 
	 *	
	 * 描述:Role 详情
	 *
	 * @Roleor  郭达望
	 * @created 2015年6月2日 下午3:07:59
	 * @since   v1.0.0 
	 * @param id
	 * @param userSession
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/selectRoleById")
	public String selectRoleById(String id, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		logger.info("角色详情  进入参数 id={},userSession={}", id, staff.getId());
		try {
			if (!StringTools.isNumeric(id)) {
				logger.error("id 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			Role role = roleService.selectRoleById(Integer.valueOf(id));
			logger.info("员工ID = " + staff.getId() + " ， 查询操作角色成功    id= " + id);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,role).toJsonString();
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 查询操作角色异常   id= " + id, e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}

	/**
	 * 
	 *	
	 * 描述:分配权限 
	 *
	 * @author  郭达望
	 * @created 2015年6月2日 下午5:56:11
	 * @since   v1.0.0 
	 * @param id  roleId
	 * @param ids authIds
	 * @param menusIds
	 * @param staff.getId()
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/menusOfDist")
	public String authorityOfDistribution(String roleId, String authIds, String menusIds, HttpServletRequest request) {
		Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		logger.info("菜单分配权限  进入参数 roleId={},authIds={},menusIds={},staff.getId()={}", roleId, authIds, menusIds,
		    staff.getId());
		try {
//			if (!StringTools.isNumeric(roleId)) {
//				logger.error("菜单id为空或者不是数字");
//				throw new LTException(LTResponseCode.MA00004);
//			}
//			if (!StringTools.isNotEmpty(authIds)) {
//				logger.error("权限ids为空");
//				throw new LTException(LTResponseCode.MA00004);
//			}
			//分配菜单
			menusService.menusOfDist(roleId, menusIds);
			//分配权限
			roleService.authOfDist(roleId, authIds);
			logger.info("员工ID = " + staff.getId() + " ， 操作菜单分配权限成功   ");
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,"操作菜单分配权限成功").toJsonString();
		} catch (Exception e) {
			logger.error("员工ID = " + staff.getId() + " ， 操作菜单分配权限异常   ", e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}
	
	/**
	 * 
	 *	
	 * 描述:部门新增员工
	 *  增加部门员工关联关系
	 * 
	 * @author  郭达望
	 * @created 2015年8月10日 下午5:37:10
	 * @since   v1.0.0 
	 * @param roleId
	 * @param staffIds
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/saveStaffs")
	public String saveStaffs(String roleId, String staffIds){
		
		try {
//			if (staffIds == null || StringTools.isBlank(staffIds)) {
//				throw new LTException(LTResponseCode.MA00004);
//			}
			List<Integer> rids = new ArrayList<Integer>();
			//参数为“000”时，删除员工部门关系
			if(staffIds.equals("000")){
				roleService.distRoleToStaff(roleId, rids);
				return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
			}
			String[] idStr = staffIds.split(",");
			for (String string : idStr) {
				if (StringTools.isNotBlank(string)) {
					rids.add(Integer.valueOf(string));
				}
			}
			roleService.distRoleToStaff(roleId, rids);
			
		} catch (Exception e) {
			logger.error("权限分配异常",e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}
	/**
	 * 
	 *	
	 * 描述:根据部门（角色）ID查询旗下所有后台用户
	 * 查询部门下所有员工
	 * @author  郭达望
	 * @created 2015年8月10日 下午5:52:43
	 * @since   v1.0.0 
	 * @param roleId
	 * @return
	 * @return  String
	 */
	@ResponseBody
	@RequestMapping(value = "/staffListByRoleId")
	public String staffListByRoleId( String roleId){
		try {
			if (!StringTools.isNumeric(roleId)) {
				logger.error("roleId 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			List<Staff> role = roleService.selectStaffListById(Integer.valueOf(roleId));
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,role).toJsonString();
		} catch (Exception e) {
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/staffListByIsNotRoleId")
	public String staffListByIsNotRoleId( String roleId){
		try {
			if (!StringTools.isNumeric(roleId)) {
				logger.error("roleId 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			List<Staff> role = roleService.selectStaffListByIsNotId(Integer.valueOf(roleId));
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,role).toJsonString();
		} catch (Exception e) {
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}
	
	/**
	 * 查询权限被那几个部门使用
	 * @param authId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectRoleListByAuthId")
	public String selectRoleListByAuthId( String authId){
		try {
			if (!StringTools.isNumeric(authId)) {
				logger.error("authId 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			List<Integer> list = roleService.selectRoleListById(Integer.valueOf(authId));
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,list).toJsonString();
		} catch (Exception e) {
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}
	
	
}
