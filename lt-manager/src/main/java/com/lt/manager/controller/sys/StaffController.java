package com.lt.manager.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.log.LtManagerLog;
import com.lt.manager.bean.log.LtManagerLoggerType;
import com.lt.manager.bean.sys.Role;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.bean.sys.StaffIp;
import com.lt.manager.bean.sys.StaffVo;
import com.lt.manager.service.log.LtManagerLogService;
import com.lt.manager.service.sys.IAuthService;
import com.lt.manager.service.sys.IRoleService;
import com.lt.manager.service.sys.IStaffService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.Md5Encrypter;
import com.lt.util.utils.model.Response;

/**
 * 
 * 说明：
 * 
 * @author zheng_zhi_rui@163com
 * @date 2015年4月7日
 *
 */
@Controller
@RequestMapping(value = "/staff")
public class StaffController extends MultiActionController {

	//	final String passwd = "@luckin1775"; 
	final String passwd = "123456";

	private Logger logger = LoggerTools.getInstance(getClass());

	@Autowired
    private LtManagerLogService loggerService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IAuthService authService;
	
	@Autowired
	private IStaffService staffService;
	

//	@Autowired
//	private AdminStockLogService loggerService;

	/**
	 * 
	 *	
	 * 描述:员工列表
	 *
	 * @author  郭达望
	 * @created 2015年5月27日 下午5:55:17
	 * @since   v1.0.0 
	 * @param request
	 * @return
	 * @return  String
	 */
	@RequestMapping(value = "/pagestaff")
	@ResponseBody
	public String pageSelectStaff(HttpServletRequest request) {
		String rows = request.getParameter("rows");
		String page = request.getParameter("page");
		//用户名
		String userName = request.getParameter("userName");
		//手机
		String tele = request.getParameter("tele");
		//状态
		String status = request.getParameter("status");
		//部门
		String roleId = request.getParameter("roleId");
		//姓名
		String name = request.getParameter("name");
		

		logger.info("pageNo = " + rows + ",  pageSize = " + page + ", userName = " + userName + ",  tele = " + tele);

		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (!StringTools.isNumeric(rows)) {
			rows = "10";
		}

		if (!StringTools.isNumeric(page)) {
			page = "1";
		}
		paramMap.put("userName", userName);
		paramMap.put("tele", tele);
		paramMap.put("status", status);
		paramMap.put("roleId", roleId);
		paramMap.put("name", name);
		Page<Staff> pageResult = staffService.pageSelectStaff(paramMap, Integer.parseInt(page),Integer.parseInt(rows));
		Page<StaffVo> pr = new Page<StaffVo>();
		pr.setPageNum(pageResult.getPageNum());
		pr.setPageSize(pageResult.getPageSize());
		pr.setTotal(pageResult.getTotal());
		List<Staff> list = pageResult.getResult();
		List<StaffVo> listVo = new ArrayList<StaffVo>();
		for (int i = 0; i < list.size(); i++) {
			Staff staff = list.get(i);
			StaffVo vo = new StaffVo();
			BeanUtils.copyProperties(staff, vo);
			List<Role> roleList = roleService.selectRoleByStaffId(staff.getId());
			StringBuilder roleIds = new StringBuilder();
			StringBuilder roleNames = new StringBuilder();
			for (int j = 0; j < roleList.size(); j++) {
				if (j != 0) {
					roleIds.append(",");
					roleNames.append(",");
				}
				roleIds.append(roleList.get(j).getId());
				roleNames.append(roleList.get(j).getName());
			}
			vo.setRoleId(roleIds.toString());
			vo.setRoleName(roleNames.toString());
			listVo.add(vo);
		}
		pr.addAll(listVo);
		String jsonResut = JqueryEasyUIData.init(pr);
		return jsonResut;
	}

	/**
	 * 修改用户权限
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updstaffauth")
	@ResponseBody
	public String updStaffAuth(HttpServletRequest request) {

		String staffId = request.getParameter("staffId");
		/* 新的员工权限ID集合 */
		//TODO 待议 传入权限会被更改
		String staffAuthIds = request.getParameter("staffAuthIds");

		try{
			if (!StringTools.isNumeric(staffId)) {
				return LTResponseCode.getCode(LTResponseCode.MA00004).toJsonString();
			}
	
			if (!StringTools.isNotEmpty(staffAuthIds)) {
				return LTResponseCode.getCode(LTResponseCode.MA00004).toJsonString();
			}
	
			String[] authIdArray = staffAuthIds.split(",");
			if (authIdArray == null || authIdArray.length <= 0) {
				return LTResponseCode.getCode(LTResponseCode.MA00004).toJsonString();
			}
	
			for (String s : authIdArray) {
				if (!StringTools.isNumeric(s)) {
					return LTResponseCode.getCode(LTResponseCode.MA00004).toJsonString();
				}
			}
			staffService.updateStaffAuth(staffId,authIdArray);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();

	}

	@RequestMapping(value = "/preAddStaff")
	public String preAddStaff() {
		return "/staff/preAddStaff";
	}

	/**
	 * 添加员工信息
	 */
	@RequestMapping(value = "/addStaff")
	@ResponseBody
	public String addStaff(String idCard, String loginName, String userName, String email, String tele, String roleId,Integer clickNum,Integer totalNumber, Integer successfulNum, HttpServletRequest request) {
		try {
			Staff user = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
			Staff staff = new Staff();
			staff.setLoginName(loginName);
			staff.setName(userName);
			staff.setEmail(email);
			staff.setTele(tele);
			staff.setIdCard(idCard);
			if(successfulNum == null || "".equals(String.valueOf(successfulNum))){
				successfulNum = 0;
			}
			if(clickNum == null || "".equals(String.valueOf(clickNum))){
				clickNum = 0;
			}
			if(totalNumber == null || "".equals(String.valueOf(totalNumber))){
				totalNumber = 0;
			}
			/* 必填项验证 */
			if (!StringTools.isNotEmpty(loginName)) {
				throw new LTException(LTResponseCode.MA00004);
			}
	
			if (!StringTools.isNotEmpty(userName)) {
				throw new LTException(LTResponseCode.MA00004);
			}
	
			/* 有效性验证：姓名、身份证号、邮箱、手机 */
			String lName = StringTools.illegalChar(loginName);
			if (StringTools.isNotEmpty(lName)) {
				throw new LTException(LTResponseCode.MA00005);
			}
			String s = StringTools.illegalChar(userName);
			if (StringTools.isNotEmpty(s)) {
				throw new LTException(LTResponseCode.MA00005);
			}
			staff.setCreateStaffId(user.getId());
			/*唯一性验证：登录名、邮箱、手机号*/
			Integer num0 = staffService.queryStaffByLoginName(loginName, null);
			if (num0 != 0) {
				throw new LTException(LTResponseCode.MA00014);
			}
		
			/* 保存添加员工信息 */
			staff.setPwd(Md5Encrypter.MD5(passwd));
			staffService.saveStaffInfo(staff);
			/* 给用户分配角色（部门）*/
			roleService.roleOfDist(staff.getId() + "", roleId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	/**
	 * 
		根据员工id，查询出员工的基本详细信息
	 */
	@RequestMapping(value = "/preUpdateStaff")
	@ResponseBody
	public String preUpdateStaff(HttpServletRequest request) {
		try{
			String staffId = request.getParameter("staffId");
			/* 入参基本判断 */
			if (!StringTools.isNotEmpty(staffId)) {
				throw new LTException(LTResponseCode.MA00004);
			}
			Integer id = Integer.parseInt(staffId);
			Staff staff = staffService.queryStaffInfoById(id);
			if (staff == null) {
				throw new LTException(LTResponseCode.MA00007);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	@RequestMapping(value = "/updateStaffInfo")
	@ResponseBody
	public String updateStaffInfo(String idCard, String staffId, String loginName, String userName, String email,Integer clickNum,Integer totalNumber,String roleId, String tele, String startDate, String endDate, HttpServletRequest request) {
		try {
		Staff user = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		/* 检证当前登录用户权限 */
		if (null == user) {
			logger.error("未登录");
			throw new LTException(LTResponseCode.MA00003);
		}
		if (!StringTools.isNumeric(staffId)) {
			logger.error("员工id为空或者不是数字");
			throw new LTException(LTResponseCode.MA00004);
		}
		Staff staff = new Staff();
		staff.setLoginName(loginName);
		staff.setName(userName);
		staff.setEmail(email);
		staff.setTele(tele);
		try {
			if (StringTools.isNotEmpty(startDate)) {
				/* 时间转换错误 */
				staff.setCreateDate(DateTools.toDefaultDateTime(startDate));
			}
		} catch (Exception e) {
			logger.info("保存添加员工信息 时间转换错误:" + e.getMessage());
			throw new LTException(LTResponseCode.MA00016);
		}
		staff.setId(Integer.valueOf(staffId));
		staff.setIdCard(idCard);
		/* 必填项验证 */
		if (staffId == null || StringTools.isEmpty(staffId)) {
			throw new LTException(LTResponseCode.MA00004);
		}

		if (!StringTools.isNotEmpty(userName)) {
			throw new LTException(LTResponseCode.MA00004);
		}

		/* 有效性验证：姓名、身份证号、邮箱、手机 */
		String s = StringTools.illegalChar(userName);
		if (StringTools.isNotEmpty(s)) {
			throw new LTException(LTResponseCode.MA00005);
		}
		staff.setModifyStaffId(user.getId());
		/*唯一性验证：登录名、邮箱、手机号*/
		Integer num0 = staffService.queryStaffByLoginName(loginName, staff.getId());
		if (num0 != 0) {
			throw new LTException(LTResponseCode.MA00014);
		}
			/* 更新员工信息 */
			staffService.updateStaffInfo(staff);
			/* 给用户分配角色（部门）*/
			roleService.roleOfDist(staff.getId() + "", roleId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	/**
	 * 
	 *	
	 * 描述:修改密码
	 *
	 * @author  郭达望
	 * @created 2015年5月27日 下午6:06:08
	 * @since   v1.0.0 
	 * @param op
	 * @param np1
	 * @param np2
	 * @param session
	 * @return
	 * @return  String
	 */
	@RequestMapping(value = "/updatePassword")
	@ResponseBody
	public String updatePassword(String op, String np1, String np2, HttpServletRequest request) {
		Staff user = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		try {
			if (null == user) {
				logger.error("未登录");
				throw new LTException(LTResponseCode.MA00003);
			}
			int userId = user.getId();
			if (StringTools.isEmpty(op)) {
				throw new LTException(LTResponseCode.MA00004);
			}
	
			int opLength = op.length();
			if (opLength < 6 || opLength > 20) {
				throw new LTException(LTResponseCode.MA00012);
			}
	
			if (StringTools.isEmpty(np1)) {
				throw new LTException(LTResponseCode.MA00004);
			}
	
			int np1Length = np1.length();
			if (np1Length < 6 || np1Length > 20) {
				throw new LTException(LTResponseCode.MA00012);
			}
	
			if (StringTools.isEmpty(np2)) {
				throw new LTException(LTResponseCode.MA00004);
			}
	
			int np2Length = np2.length();
			if (np2Length < 6 || np2Length > 20) {
				throw new LTException(LTResponseCode.MA00012);
			}
	
			if (!np1.equals(np2)) {
				throw new LTException(LTResponseCode.MA00013);
			}
	
			String oldPwd = user.getPwd();
			op = Md5Encrypter.MD5(op);
			if (!oldPwd.equals(op)) {
				throw new LTException(LTResponseCode.MA00003);
			}
		/*更新密码*/
		
			np1 = Md5Encrypter.MD5(np1);
			user.setPwd(np1);
			staffService.updateStaffPassword(userId, np1);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	@RequestMapping(value = "/updatePasswordByStaffId")
	@ResponseBody
	public String updatePasswordByStaffId(String password, String staffId, HttpServletRequest request) {
		Staff user = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		if (null == user) {
			logger.error("未登录");
			throw new LTException(LTResponseCode.MA00003);
		}
		if (!StringTools.isNumeric(staffId)) {
			throw new LTException(LTResponseCode.MA00004);
		}
		if (StringTools.isEmpty(password)) {
			throw new LTException(LTResponseCode.MA00004);
		}

		int opLength = password.length();
		if (opLength < 6 || opLength > 20) {
			throw new LTException(LTResponseCode.MA00012);
		}

		/*更新密码*/
		try {
			staffService.updateStaffPassword(Integer.valueOf(staffId), Md5Encrypter.MD5(password));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}

		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	@RequestMapping(value = "/resetPassword")
	@ResponseBody
	public String resetPassword(String staffId, HttpServletRequest request) {
		try {
			/*入参检证*/
			if (StringTools.isEmpty(staffId)) {
				throw new LTException(LTResponseCode.MA00004);
			}
			int sId = Integer.parseInt(staffId);
			String pwd = Md5Encrypter.MD5(passwd);
			/*更新密码*/
			staffService.updateStaffPassword(sId, pwd);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	/**
	 * 修改禁用启用状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateStaffStatus")
	@ResponseBody
	public String updateStaffStatus(HttpServletRequest request) {
		/*权限检证*/
		Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
		String ip = IpUtils.getUserIP(request);
		//TODO
		String staffId = request.getParameter("staffId");
		String status = request.getParameter("status");
		try {
			/*入参检证*/
			if (StringTools.isEmpty(staffId)||StringTools.isEmpty(status)) {
				throw new LTException(LTResponseCode.MA00004);
			}
	
			int sId = Integer.parseInt(staffId);
		
			staffService.updateStaffStatus(sId, status);
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		LtManagerLog logInfo = new LtManagerLog(StringTools.formatInt(staffId, null), null, LtManagerLoggerType.LOGIN.getCode(), LtManagerLoggerType.LOGIN.getName(), true, "登录次数更新成功", ip, null, null);
		loggerService.log(logInfo);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	@RequestMapping(value = "/updateIPStatus")
	@ResponseBody
	public String updateIPStatus(HttpServletRequest request) {
		String staffId = request.getParameter("staffId");
		String status = request.getParameter("status");
		try {
			/*入参检证*/
			if (StringTools.isEmpty(staffId)||StringTools.isEmpty(status)) {
				throw new LTException(LTResponseCode.MA00004);
			}
	
			int sId = Integer.parseInt(staffId);
			int sts = Integer.parseInt(status);
			staffService.updateIPStatus(sId, sts);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return LTResponseCode.getCode(e.getMessage()).toJsonString();

		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}

	
	/**
	 * 
	 *	
	 * 描述:获取后台登录用户名与id 只有admin有权限获取所有其他只能获取自己的
	 *
	 * @author  郭达望
	 * @created 2015年11月11日 上午10:21:01
	 * @since   v1.0.0 
	 * @return
	 * @return  String
	 */
	@RequestMapping(value = "/getStaffAll")
	@ResponseBody
	public String getStaffAll(HttpSession session) {
		try {
			Staff staff = JSON.parseObject((String) session.getAttribute("staff"), Staff.class);
			if (null == staff) {
				throw new LTException(LTResponseCode.MA00003);
			}
			if (staff.getId() != 999) {
				return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
			}
			List<Staff> list = staffService.getStaffAll();
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,list).toJsonString();
		} catch (Exception e) {
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}
	
	/**
	 * 
	 *	
	 * 描述:删除
	 *
	 * @author  郭达望
	 * @created 2016年3月31日 下午3:56:31
	 * @since   v1.0.0 
	 * @param id
	 * @return
	 * @return  String
	 */
	@RequestMapping(value = "/deleteStaff")
	@ResponseBody
	public String delete(Integer id){
		//删除存在的员工与角色之间的关联关系 
		roleService.deleteForRoleMap("staff_id", id);
		staffService.deleteById(id);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}
	
	
	
	/**
	 * ip白名单 删
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteStaffIpByIds")
	@ResponseBody
	public String deleteStaffIpByIds(String ids){
		try {
			staffService.deleteStaffIpByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}
	/**
	 * ip白名单 增
	 * @param ip
	 * @param desc
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/saveSatffIp")
	@ResponseBody
	public String saveSatffIp(String ip,String desc,HttpSession session){
		Staff staff = JSON.parseObject((String) session.getAttribute("staff"), Staff.class);
		if (null == staff) {
			throw new LTException(LTResponseCode.MA00003);
		}
		try {
			staffService.saveSatffIp(ip, staff.getId(), desc);
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}
	/**
	 * ip白名单 改
	 * @param id
	 * @param ip
	 * @param desc
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/updateSatffIp")
	@ResponseBody
	public String updateSatffIp(Integer id,String ip,String desc,HttpSession session){
		Staff staff = JSON.parseObject((String) session.getAttribute("staff"), Staff.class);
		if (null == staff) {
			throw new LTException(LTResponseCode.MA00003);
		}
		try {
			staffService.updateSatffIp(ip, staff.getId(), id, desc);
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
	}
	/**
	 * ip白名单 查
	 * @param ip
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/findStaff")
	@ResponseBody
	public String findStaff(String ip,Integer rows,Integer page){
		if(null == rows || rows == 0){
			rows = 10;
		}
		if(null == page || page == 0){
			page = 1;
		}
		try {
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ip", ip);
			Page<StaffIp> pages = staffService.findStaff(paramMap, rows, page);
			logger.info("pages = {}",JSONObject.toJSONString(pages));
			String jsonResut = JqueryEasyUIData.init(pages);
			return jsonResut;
//			return LTResponseCode.getCode(LTResponseCode.SUCCESS,pages).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}
}
