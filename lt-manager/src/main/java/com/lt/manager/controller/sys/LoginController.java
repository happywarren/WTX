package com.lt.manager.controller.sys;

import com.alibaba.fastjson.JSON;
import com.lt.manager.bean.PrivilegesManager;
import com.lt.manager.bean.log.LtManagerLog;
import com.lt.manager.bean.log.LtManagerLoggerType;
import com.lt.manager.bean.sys.Auth;
import com.lt.manager.bean.sys.MenusVO;
import com.lt.manager.bean.sys.PurView;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.log.LtManagerLogService;
import com.lt.manager.service.sys.IAuthService;
import com.lt.manager.service.sys.IStaffService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户登录.
 */
@Controller
@RequestMapping()
public class LoginController {

    /**
     * 员工设备关联映射
     */
    public static Map<String, String> map = new HashMap<String, String>();

    /**
     * 唯一登录校验
     */
    public static Map<String, String> map_token = new HashMap<String, String>();

    @Autowired
    private IStaffService staffService;
    @Autowired
    private LtManagerLogService loggerService;
    @Autowired
    private IAuthService iAuthService;

    @ResponseBody
    @RequestMapping(value = "login")
    public String login(HttpServletRequest request) {
        Staff staff = null;
        String ip = IpUtils.getUserIP(request);
        try {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            String code = request.getParameter("code");
            String device = request.getParameter("deviceCode");
            /* 入参基本判断 */
            if (StringTools.isBlank(userName)) {
                throw new LTException(LTResponseCode.MA00004);
            }
            String s = StringTools.illegalChar(userName);
            if (StringTools.isNotEmpty(s)) {
                throw new LTException(LTResponseCode.MA00005);
            }
            if (!StringTools.isNotEmpty(password)) {
                throw new LTException(LTResponseCode.MA00006);
            }
            staff = staffService.selectLoginStaffByUserName(userName);
            if (staff == null) {
                throw new LTException(LTResponseCode.MA00007);
            }
            int status = staff.getStatus();
            if (0 != status) {
                throw new LTException(LTResponseCode.MA00024);
            }
            //限制IP登录限制
            if (!staffService.checkIp(staff, ip)) {
                throw new LTException(LTResponseCode.MA00019);
            }
            //是否需要验证码处理
            staffService.checkEmailDevice(staff, code, device);

//			错误登录次数校验
            staffService.checkErrorNum(staff);
            //密码验证
            if (!staff.getPwd().equalsIgnoreCase(password)) {
                throw new LTException(LTResponseCode.MA00010);
            }
//			/* 加载用户权限 */
            List<Auth> staffAuthList = iAuthService.selectAuthListByStaffId(staff.getId());
//			/* 加载用户菜单 */
            if (staffAuthList == null) {
                throw new LTException(LTResponseCode.MA00011);
            }

            PurView userPurView = PrivilegesManager.getInstance().userAuthRecompose(staffAuthList);
            LtManagerLog logInfo = new LtManagerLog(staff.getId(), staff.getName(), LtManagerLoggerType.LOGIN.getCode(), LtManagerLoggerType.LOGIN.getName(), true, "登录成功", ip, null, null);
            loggerService.log(logInfo);
            Map<String, Object> data = new HashMap<>();
            data.put("userSession", staff.getId());
            data.put("name", StringTools.isBlank(staff.getName()) ? staff.getLoginName() : staff.getName());
            data.put("loginName", staff.getLoginName());
            data.put("loginId", staff.getId());
            data.put("deviceCode", map.get(staff.getId().toString()));
//			data.put("user_id", StringTools.isEmpty(user_id) ? "" : user_id);
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(30 * 60);
            String uuid = UUID.randomUUID().toString();
            map_token.put(staff.getId().toString(), uuid);
            session.setAttribute("token", uuid);
            session.setAttribute("userSession", staff.getId());
            session.setAttribute("staff", JSON.toJSONString(staff));

            session.setAttribute("userPurView", JSON.toJSONString(userPurView));
            return LTResponseCode.getCode(LTResponseCode.SUCCESS, data).toJsonString();
        } catch (Exception e) {
            if (e.getMessage().equals(LTResponseCode.MA00010)) {
                LtManagerLog logInfo = new LtManagerLog(staff.getId(), staff.getName(), LtManagerLoggerType.LOGIN.getCode(), LtManagerLoggerType.LOGIN.getName(), false, "登录失败，用户密码错误 ", ip, null, null);
                loggerService.log(logInfo);
            }
            if (e.getMessage().equals(LTResponseCode.MA00019)) {
                LtManagerLog logInfo = new LtManagerLog(staff.getId(), staff.getName(), LtManagerLoggerType.LOGIN.getCode(), LtManagerLoggerType.LOGIN.getName(), false, "IP地址验证未通过", ip, null, null);
                loggerService.log(logInfo);
            }
            if (e.getMessage().equals(LTResponseCode.MA00020)) {
                LtManagerLog logInfo = new LtManagerLog(staff.getId(), staff.getName(), LtManagerLoggerType.LOGIN.getCode(), LtManagerLoggerType.LOGIN.getName(), false, "短信验证码错误，请重试", ip, null, null);
                loggerService.log(logInfo);
            }
            e.printStackTrace();
            return LTResponseCode.getCode(e.getMessage()).toJsonString();
        }

    }

    /**
     * 验证短信发送
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendSms")
    public String sendSms(HttpServletRequest request) {
        try {
            String userName = request.getParameter("userName");
            String ip = IpUtils.getUserIP(request);
            Staff staff = staffService.selectLoginStaffByUserName(userName);
            if (staff == null) {
                throw new LTException(LTResponseCode.MA00007);
            }
            String email = staff.getEmail();
            if (email == null) {
                throw new LTException(LTResponseCode.MA00004);
            }
            //短信
            staffService.sendRegisterEmailMsg(email, ip, staff.getId());
            return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
        } catch (Exception e) {
            e.printStackTrace();
            return LTResponseCode.getCode(e.getMessage()).toJsonString();
        }
    }

    @ResponseBody
    @RequestMapping(value = "getMenus")
    public String getMenus(HttpServletRequest request) {
        try {
            Staff staff = JSON.parseObject((String) request.getSession().getAttribute("staff"), Staff.class);
            if (staff == null) {
                throw new LTException(LTResponseCode.MA00003);
            }
            List<MenusVO> menusList = iAuthService.selectMenusListByStaffId(staff.getId());
            return LTResponseCode.getCode(LTResponseCode.SUCCESS, menusList).toJsonString();
        } catch (Exception e) {
            e.printStackTrace();
            return LTResponseCode.getCode(e.getMessage()).toJsonString();
        }
    }

    /**
     * 描述:注销登录
     *
     * @param userSession
     * @return String
     * @author 郭达望
     * @created 2015年6月5日 下午2:46:48
     * @since v1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "unlogin")
    public String unlogin(HttpServletRequest request) {
        request.getSession().invalidate();
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, "注销成功").toJsonString();
    }

}
