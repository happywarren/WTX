package com.lt.manager.controller.sys;

import java.util.ArrayList;
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
import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.AuthInfoVo;
import com.lt.manager.bean.sys.AuthVO;
import com.lt.manager.bean.sys.MenusAuth;
import com.lt.manager.bean.sys.Role;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.sys.IAuthService;
import com.lt.manager.service.sys.IMenusService;
import com.lt.manager.service.sys.IRoleService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 权限管理
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    Logger logger = LoggerTools.getInstance(this.getClass());

    @Autowired
    private IAuthService authService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenusService menusService;

//    @RequestMapping(value = "/load")
//    @ResponseBody
//    public void load(){
//    	String strPath = "C:\\work\\eclipse\\workspace_lt_241\\lt-parent\\lt-manager\\src\\main\\java\\com\\lt\\manager\\controller";
//		List<File> filelist = Test.getFileList(strPath);
//		if(filelist !=null){
//			for (File file : filelist) {
//				Test.readFileByLines(file);
//			}
//		}
//		List<String> list = Test.urlList;
//		for (String str : list) {
//			Auth auth = new Auth();
//			auth.setUrl(str.replaceAll(" ", ""));
//			auth.setType(1);
//			authService.saveAuth(auth);
//		}
//    }
    
    /**
     * 新增auth
     *
     * @param name
     * @param url
     * @param desc
     * @param menusId
     * @param operaType
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveAuth")
    @ResponseBody
    public String saveAuth(String name, String url, String desc, String menusId, String operaType, HttpServletRequest request) {
        Response response = null;
        try {
            if (!StringTools.isNotEmpty(name)) {
                logger.error("name 为空");
                throw new LTException(LTResponseCode.MA00004);
            }
            if (!StringTools.isNotEmpty(url)) {
                logger.error("url 为空");
                throw new LTException(LTResponseCode.MA00004);
            }
            if (!StringTools.isNumeric(menusId)) {
                logger.error("menusId 为空,或者不是数字");
                throw new LTException(LTResponseCode.MA00004);
            }
            if (!StringTools.isNumeric(operaType)) {
                logger.error("operaType 为空,或者不是数字");
                throw new LTException(LTResponseCode.MA00004);
            }
            Auth auth = new Auth();
            auth.setName(name.trim());
            auth.setUrl(url.trim());
            auth.setDesc(desc);
            auth.setMenusId(Integer.parseInt(menusId));
            auth.setOperaType(Integer.valueOf(operaType));
            authService.saveAuth(auth);
            menusService.insertMenusAuth(Integer.parseInt(menusId), auth.getId());
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            logger.info("新增权限信息异常，e={}", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:根据id删除权限 不提供批量删除
     *
     * @param id
     * @return String
     * @author 郭达望
     * @created 2015年6月2日 下午3:08:41
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/delAuth")
    public String delAuth(String id, HttpServletRequest request) {
        Response response = null;
        try {
        	Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
            logger.info("删除权限  进入参数 id={},userSession={}", id, staff.getId());
            if (!StringTools.isNumeric(id)) {
                logger.error("id 为空或者不是数字");
                throw new LTException(LTResponseCode.MA00004);
            }
            menusService.removeByAuthId(Integer.valueOf(id));
            logger.info("员工ID = " + staff.getId() + " ， 删除操作权限成功 id= " + id);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
        	e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:auth修改
     *
     * @param id
     * @param name
     * @param url
     * @param desc
     * @return String
     * @author 郭达望
     * @created 2015年6月2日 下午3:08:28
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/editAuth")
    public String editAuth(String id, String name, String url, String desc, String menusId, String operaType, HttpServletRequest request) {
        Response response = null;
        Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
        try {
            logger.info("修改权限  进入参数 id={},name={},url={},desc={},userSession={}", id, name, url, desc, staff.getId());
            if (!StringTools.isNumeric(id)) {
                logger.error("id 为空或者不是数字");
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
            if (!StringTools.isNumeric(menusId)) {
                logger.error("menusId 为空,或者不是数字");
                throw new LTException(LTResponseCode.MA00004);
            }
            if (!StringTools.isNumeric(operaType)) {
                logger.error("operaType 为空,或者不是数字");
                throw new LTException(LTResponseCode.MA00004);
            }
            Auth auth = new Auth();
            auth.setId(Integer.valueOf(id));
            auth.setName(name.trim());
            auth.setUrl(url.trim());
            auth.setDesc(desc);
            auth.setMenusId(Integer.parseInt(menusId));
            auth.setOperaType(Integer.valueOf(operaType));
            authService.updateAuth(auth);
            MenusAuth menusAuth = menusService.selectByMIdAndAId(Integer.parseInt(menusId), auth.getId());
            if (menusAuth != null) {
                menusService.removeMenusAuthById(menusAuth.getId());
            }
            menusService.insertMenusAuth(Integer.parseInt(menusId), auth.getId());
            logger.info("员工ID = " + staff.getId() + " ， 编辑操作权限成功  id= " + id);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            logger.error("员工ID = " + staff.getId() + " ， 编辑操作权限异常 id= " + id, e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:auth列表
     *
     * @param name
     * @param rows
     * @param page
     * @return String
     * @author 郭达望
     * @created 2015年6月2日 下午3:08:11
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/selectAuth")
    public String selectAuth(String name, Integer rows, Integer page, String menusName, String operaType,String url, HttpServletRequest request) {
        Response response = null;
        Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
        try {
            logger.info("auth列表  进入参数 name={},userSession={}", name, staff.getId());
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (null == rows || rows == 0) {
                rows = 10;
            }
            if (null == page || page == 0) {
                page = 1;
            }
            if (StringTools.isNotEmpty(menusName)) {
                paramMap.put("menusName", menusName);
            }
            if (StringTools.isNumeric(operaType)) {
                paramMap.put("operaType", operaType);
            }
            if (!StringTools.isEmpty(url)) {
                paramMap.put("url", url);
            }
            paramMap.put("name", name);
            Page<AuthInfoVo> auth = authService.selectAuth(paramMap, rows, page);
            logger.info("员工ID = " + staff.getId() + " ， 查询操作权限列表成功    总共有" + auth.getTotal() + "条");
            return JqueryEasyUIData.init(auth);
        } catch (Exception e) {
            logger.error("员工ID = " + staff.getId() + " ， 查询操作权限异常 ", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:auth 详情
     *
     * @param id
     * @return String
     * @author 郭达望
     * @created 2015年6月2日 下午3:07:59
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/selectAuthById")
    public String selectAuthById(String id, HttpServletRequest request) {
        Response response = null;
        Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
        try {
            logger.info("权限详情  进入参数 id={},userSession={}", id, staff.getId());
            if (!StringTools.isNumeric(id)) {
                logger.error("id 为空或者不是数字");
                throw new LTException(LTResponseCode.MA00004);
            }
            Auth auth = authService.selectAuthById(Integer.valueOf(id));
            logger.info("员工ID = " + staff.getId() + " ， 查询操作权限成功    id= " + id);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, auth);
        } catch (Exception e) {
            logger.error("员工ID = " + staff.getId() + " ， 查询操作权限异常 ", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:查询所有权限
     *
     * @return String
     * @author 郭达望
     * @created 2015年6月2日 下午3:44:49
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/selectAuthList")
    public String selectAuthList(HttpServletRequest request) {
        Response response = null;
        Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
        try {
            String menuId = request.getParameter("menuId");
            if (NumberUtils.isNumber(menuId)) {
                List<AuthVO> auth = authService.selectAuthListByMenuId(Integer.parseInt(menuId));
                logger.info("员工ID = " + staff.getId() + " ， 查询所有操作权限成功   ");
                response = LTResponseCode.getCode(LTResponseCode.SUCCESS, auth);
            } else {
                List<Auth> auth = authService.selectAuthList();
                logger.info("员工ID = " + staff.getId() + " ， 查询所有操作权限成功   ");
                response = LTResponseCode.getCode(LTResponseCode.SUCCESS, auth);
            }
        } catch (Exception e) {
            logger.error("员工ID = " + staff.getId() + " ， 查询操作权限异常 ", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:分配角色
     *
     * @param staffId
     * @param roleIds
     * @return String
     * @author 郭达望
     * @created 2015年6月3日 上午11:16:47
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/roleOfDist")
    public String roleOfDist(String staffId, String roleIds, HttpServletRequest request) {
        Response response = null;
        Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
        logger.info("分配角色 进入参数 staffId={},roleIds={},userSession={}", staffId, roleIds, staff.getId());
        try {
            if (!StringTools.isNumeric(staffId)) {
                logger.error("员工id为空或者不是数字");
                throw new LTException(LTResponseCode.MA00004);
            }
            if (!StringTools.isNotEmpty(roleIds)) {
                logger.error("roleIds 参数为空");
                throw new LTException(LTResponseCode.MA00004);
            }
            roleService.roleOfDist(staffId, roleIds);
            logger.info("员工ID = " + staff.getId() + " ，分配角色成功   ");
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            logger.error("员工ID = " + staff.getId() + " ， 分配角色异常  ", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:获取用户角色
     *
     * @return String
     * @author 郭达望
     * @created 2015年6月4日 下午4:52:09
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/selectRoleByStaffId")
    public String selectRoleByStaffId(String staffId, HttpServletRequest request) {
        Response response = null;
        Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
        try {
            if (!StringTools.isNumeric(staffId)) {
                staffId = staff.getId() + "";
            }
            List<Role> list = authService.selectRoleByStaffId(Integer.valueOf(staffId));
            logger.info("员工ID = " + staff.getId() + " ，获取当前用户角色成功  ");
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
        } catch (Exception e) {
            logger.error("员工ID = " + staff.getId() + " ， 获取当前用户角色异常  ", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:权限页，将一个权限分配给菜单
     *
     * @param authId   权限ID
     * @param menusIds 菜单id，多个用逗号（,）隔开
     * @return String
     * @author jiupeng
     * @created 2015年6月25日 下午6:52:47
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/authorityToMenu")
    public String authorityToMenu(Integer authId, String menusIds) {
        Response response = null;
        try {
            if (authId == null || StringTools.isBlank(menusIds)) {
                throw new LTException(LTResponseCode.MA00004);
            }
            List<Integer> mids = new ArrayList<Integer>();
            String[] idStr = menusIds.split(",");
            for (String string : idStr) {
                if (StringTools.isNotBlank(string)) {
                    mids.add(Integer.valueOf(string));
                }
            }
            String res = authService.distAuthToMenus(authId, mids);
            if (res != null) {
                throw new LTException("分配权限到菜单异常");
            }
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            logger.error("权限页，将一个权限分配给菜单异常", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 描述:权限页，将一个权限分配给部门（角色）
     *
     * @param authId
     * @param roleIds
     * @return String
     * @author 郭达望
     * @created 2015年8月10日 下午4:22:32
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/authorityToRole")
    public String authorityToRole(Integer authId, String roleIds) {
        Response response = null;
        try {
            if (authId == null || StringTools.isBlank(roleIds)) {
                throw new LTException(LTResponseCode.MA00004);
            }
            List<Integer> rids = new ArrayList<Integer>();
            String[] idStr = roleIds.split(",");
            for (String string : idStr) {
                if (StringTools.isNotBlank(string)) {
                    rids.add(Integer.valueOf(string));
                }
            }
            authService.distAuthToRole(authId, rids);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            logger.error("权限分配异常", e);
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();


    }
    /**
     * 查询三级内最底层节点
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectMenusBottom")
    public String selectMenusBottom() {
    	Response response = null;
    	try {
    		;
    		response = LTResponseCode.getCode(LTResponseCode.SUCCESS,authService.selectMenusBottom());
    	} catch (Exception e) {
    		logger.error("查询三级内最底层节点异常", e);
    		response = LTResponseCode.getCode(e.getMessage());
    	}
    	return response.toJsonString();
    	
    	
    }
}
