package com.lt.manager.controller.user;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.manager.bean.enums.product.CashColumnEnum;
import com.lt.manager.bean.user.BankChannelVo;
import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserBaseInfoQueryVO;
import com.lt.manager.service.user.IUserManageService;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundMainScore;
import com.lt.model.user.ServiceContant;
import com.lt.model.user.UserContant;
import com.lt.model.user.log.UserOperateLog;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.Md5Encrypter;
import com.lt.util.utils.model.Response;

/**
 * 用户管理控制器
 *
 * @author licy
 */
@Controller
@RequestMapping(value = "/user")
public class UserManageController {

    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private IUserManageService iUserManageService;

    @Autowired
    private IFundAccountApiService fundAccountServiceImpl;

    @Autowired
    private IProductApiService productApiServiceImpl;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户列表查询
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/findUserInfo")
    @ResponseBody
    public String queryUserInfo(HttpServletRequest request, UserBaseInfoQueryVO param) {

        Response response = null;
        logger.info("查询用户信息param={}", param);
        try {
            Page<UserBase> page = iUserManageService.queryUserInfoPage(param);
            for (int i = 0; i < page.size(); i++) {
                String userId = page.get(i).getId();
                logger.info(userId);
                int online = 0;
                boolean isOnLine = redisTemplate.opsForSet().isMember(UserContant.REDIS_USER_ONLINE_LIST, userId);
                if (isOnLine) {
                    online = 1;
                }
                page.get(i).setOnline(online);
                int step = this.iUserManageService.getUserOpenAccount(userId);
                page.get(i).setOpenStep(step);
            }
            return JqueryEasyUIData.init(page);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("查询用户信息异常，e={}", e);

        }
        return response.toJsonString();
    }
    /**
     * 用户列表查询
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/viewInvestorList")
    @ResponseBody
    public String viewInvestorInfo(HttpServletRequest request, UserBaseInfoQueryVO param) {

        Response response = null;
        logger.info("查询用户信息param={}", param);
        try {
        	param.setServiceCode(ServiceContant.INVESTOR_SERVICE_CODE);
            Page<UserBase> page = iUserManageService.queryInvestorList(param);
            for (int i = 0; i < page.size(); i++) {
                String userId = page.get(i).getId();
                logger.info(userId);
            }
            return JqueryEasyUIData.init(page);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("查询用户信息异常，e={}", e);

        }
        return response.toJsonString();
    }
    
    /**
     * 用户列表查询
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/findUserBlackList")
    @ResponseBody
    public String findUserBlackList(HttpServletRequest request, UserBaseInfoQueryVO param) {

        Response response = null;
        logger.info("查询黑名单用户信息param={}", param);
        try {
            Page<UserBase> page = iUserManageService.queryUserBlackListPage(param);
            return JqueryEasyUIData.init(page);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("查询黑名单用户信息异常，e={}", e);

        }
        return response.toJsonString();
    }

    /**
     * 用户列表查询
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/findUserRealNameList")
    @ResponseBody
    public String findUserRealNameList(HttpServletRequest request, UserBaseInfoQueryVO param) {

        Response response = null;
        logger.info("查询用户实名制信息param={}", param);
        try {
            Page<UserBase> page = iUserManageService.queryUserRealNameListPage(param);
            return JqueryEasyUIData.init(page);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("查询用户实名制信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 用户信息修改
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/editUserInfo")
    @ResponseBody
    public String editUserInfo(HttpServletRequest request, UserBaseInfoQueryVO param) {

        Response response = null;
        logger.info("修改用户信息param={}", param);
        try {
            //修改密码
            if (!StringTools.isEmpty(param.getPasswd())) {
                param.setPasswd(Md5Encrypter.MD5(param.getPasswd()));
            }

            param.setId(param.getUserId());
            iUserManageService.updateUserBase(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("修改用户信息异常，e={}", e);
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
    @RequestMapping(value = "/user/removeUserInfo")
    @ResponseBody
    public String removeUserInfo(HttpServletRequest request, String param) {

        Response response = null;
        logger.info("删除用户信息param={}", param);

        String[] userIDs = param.split(",");
        try {
            for (String id : userIDs) {
                iUserManageService.removeUserBase(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除用户信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 更改用户角色类型 1,内部员工 2,运营人员  3,普通用户
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/setUserType")
    @ResponseBody
    public String setUserType(HttpServletRequest request) {

        Response response = null;
        String userId = request.getParameter("userId");
        String isStaff = request.getParameter("isStaff");
        logger.info("设置用户类型param={}", userId);
        UserBaseInfoQueryVO uvo = new UserBaseInfoQueryVO();
        try {
            uvo.setId(userId);
            uvo.setIsStaff(Integer.valueOf(isStaff));
            iUserManageService.updateUserBase(uvo);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("设置用户类型异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 更改用户状态 0,普通用户 -10,注销用户  -20,黑名单用户
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/setUserStatus")
    @ResponseBody
    public String setUserStatus(HttpServletRequest request) {

        Response response = null;
        String userIdStr = request.getParameter("userId");
        String statusStr = request.getParameter("status");
        Integer status = null;
        if ("".equals(statusStr) || statusStr == null) {
            throw new LTException(LTResponseCode.US20001);
        } else {
            status = Integer.valueOf(statusStr);
        }
        logger.info("设置用户状态param={}", userIdStr);
        UserBaseInfoQueryVO uvo = new UserBaseInfoQueryVO();
        try {
            uvo.setId(userIdStr);
            uvo.setStatus(Integer.valueOf(status));
            iUserManageService.updateUserBase(uvo);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("设置用户状态异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 更改用户状态 0,普通用户 -10,注销用户  -20,黑名单用户
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/user/setUserBlack")
    @ResponseBody
    public String setUserBlack(HttpServletRequest request) {

        Response response = null;
        String userIdStr = request.getParameter("userId");
        String isSetStr = request.getParameter("isSet");
        Integer isSet = null;
        if (StringTools.isEmpty(isSetStr)) {
            throw new LTException(LTResponseCode.US20001);
        } else {
            isSet = Integer.valueOf(isSetStr);
        }
        logger.info("设置用户黑名单param={}", isSet);
        UserBaseInfoQueryVO uvo = new UserBaseInfoQueryVO();
        try {
            uvo.setId(userIdStr);
            uvo.setIsBlack(isSet);
            iUserManageService.updateUserBase(uvo);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("设置用户黑名单异常，e={}", e);
        }
        return response.toJsonString();
    }

    /**
     * 更改用户实名制状态 0,未实名 1，申请实名，2，申请成功 3，申请失败
     *
     * @param request
     * @param param
     * @return
     * @throws Exception
     * @throws NumberFormatException
     */
    @RequestMapping(value = "/user/setUserRealNameStatus")
    @ResponseBody
    public String setUserRealNameStatus(HttpServletRequest request, UserBaseInfoQueryVO param) throws NumberFormatException, Exception {

        Response response = null;
        String ids = request.getParameter("ids");
        String isCheckAll = request.getParameter("isCheckAll");
        String finalStatusStr = request.getParameter("finalStatus");
        Integer finalStatus = null;
        //没有传任何ID给后台
        if (isCheckAll == null && ids == null) {
            throw new LTException(LTResponseCode.US20001);
        }
        //没有传状态值过来
        if (finalStatusStr == null) {
            throw new LTException(LTResponseCode.US20001);
        } else {
            finalStatus = Integer.valueOf(finalStatusStr);
        }
        //只传一个值过来
        if (isCheckAll == null && ids != null && !ids.contains(",")) {
            return updateRealstatus(ids, finalStatus);
        }
        //传多个值但是没有传checkAll的
        if (isCheckAll == null && ids != null) {
            return updateRealstatus(ids, finalStatus, param);
        }
        //传checkAll的
        if (isCheckAll != null) {
            return updateRealstatus(finalStatus, param);
        }
        response = LTResponseCode.getCode(LTResponseCode.USL2003);
        return response.toJsonString();
    }

    //用户只选择一个用户时候调用此方法
    public String updateRealstatus(String id, Integer finalStatus) {
        Response response = null;
        UserBaseInfoQueryVO param = new UserBaseInfoQueryVO();
        try {
            UserBase baseuser = iUserManageService.getUserBase(id);
            //成功状态的不可以在操作
            if (baseuser.getRealNameStatus() == 2) {
                response = LTResponseCode.getCode(LTResponseCode.USL2004);
                return response.toJsonString();
            }
            //失败状态的不可以在后台做成功操作
            if (baseuser.getRealNameStatus() == 3) {
                response = LTResponseCode.getCode(LTResponseCode.USL2005);
                return response.toJsonString();
            }
            param.setId(String.valueOf(id));
            param.setRealNameStatus(finalStatus);
            if (finalStatus == 2) {
                //设置审核通过时间
                java.util.Date today = new java.util.Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                param.setFinishTime(df.format(today));
            }
            iUserManageService.updateUserBase(param);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
            e.printStackTrace();
        }
        return response.toJsonString();
    }

    //用户选择了多个用户且没有选择全选时候调用此方法
    public String updateRealstatus(String ids, Integer finalStatus, UserBaseInfoQueryVO param) throws NumberFormatException, Exception {
        Response response = null;
        String[] userIDs = ids.split(",");
        //逐个判断所选用户的原始实名制状态
        Set set = new HashSet();
        for (String id : userIDs) {
            UserBase baseuser = iUserManageService.getUserBase(id);
            set.add(baseuser.getRealNameStatus());
            //成功状态的不可以在操作
            if (baseuser.getRealNameStatus() == 2) {
                response = LTResponseCode.getCode(LTResponseCode.USL2004);
                return response.toJsonString();
            }
            //失败状态的不可以在后台做成功操作
            if (baseuser.getRealNameStatus() == 3) {
                response = LTResponseCode.getCode(LTResponseCode.USL2005);
                return response.toJsonString();
            }
        }
        //判断set元素值是否大于1，如果大于1说明用户所选的值状态不一致
        if (set.size() > 1) {
            response = LTResponseCode.getCode(LTResponseCode.USL2002);
            return response.toJsonString();
        } else {

            for (String id : userIDs) {
                param.setId(id);
                param.setRealNameStatus(finalStatus);
                if (finalStatus == 2) {
                    //设置审核通过时间
                    java.util.Date today = new java.util.Date();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    param.setFinishTime(df.format(today));
                }
                iUserManageService.updateUserBase(param);
            }
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        }

        return response.toJsonString();
    }

    public String updateRealstatus(Integer finalStatus, UserBaseInfoQueryVO param) throws Exception {
        Response response = null;
        //选择了全选按钮，根据查询条件检索所有的数据
        List<UserBase> userList = iUserManageService.queryUserInfoPage(param);
        UserBase baseuser = new UserBase();
        Set set = new HashSet();
        for (Iterator iter = userList.iterator(); iter.hasNext(); ) {
            baseuser = (UserBase) iter.next();
            set.add(baseuser.getRealNameStatus());
            //成功状态的不可以在操作
            if (baseuser.getRealNameStatus() == 2) {
                response = LTResponseCode.getCode(LTResponseCode.USL2004);
                return response.toJsonString();
            }
            //失败状态的不可以在后台做成功操作
            if (baseuser.getRealNameStatus() == 3) {
                response = LTResponseCode.getCode(LTResponseCode.USL2005);
                return response.toJsonString();
            }
        }
        if (set.size() > 1) {
            response = LTResponseCode.getCode(LTResponseCode.USL2002);
            return response.toJsonString();
        }
        baseuser = new UserBase();
        for (Iterator iter = userList.iterator(); iter.hasNext(); ) {
            baseuser = (UserBase) iter.next();
            param.setId(baseuser.getId());
            param.setRealNameStatus(finalStatus);
            if (finalStatus == 2) {
                //设置审核通过时间
                java.util.Date today = new java.util.Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                param.setFinishTime(df.format(today));
            }
            iUserManageService.updateUserBase(param);
        }
        response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        return response.toJsonString();
    }


    /**
     * 给用户发短信
     *
     * @param request
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/sendShortMsg")
    @ResponseBody
    public String sendShortMsg(HttpServletRequest request, UserBaseInfoQueryVO param) throws Exception {

        Response response = null;

        String ids = request.getParameter("ids");
        String isCheckAll = request.getParameter("isCheckAll");
        logger.info("给用用户发信息param={}", ids);
        UserBase uvo = new UserBase();
        if (isCheckAll.equals("1")) {
            //选择了全选按钮，根据查询条件检索所有的数据
            List<UserBase> userList = iUserManageService.queryUserInfoPage(param);
            for (Iterator iter = userList.iterator(); iter.hasNext(); ) {
                uvo = (UserBase) iter.next();
                //TODO  调用发短信方法
                //sendShortMsg(uvo.getTele());
            }
        } else {
            try {
                if (ids.contains(",")) {
                    String[] userIDs = ids.split(",");
                    for (String id : userIDs) {
                        uvo = iUserManageService.getUserBase(id);
                        //TODO  调用发短信方法
                        //sendShortMsg(uvo.getTele());
                    }
                } else {
                    uvo = iUserManageService.getUserBase(ids);
                    //TODO  调用发短信方法
                    //sendShortMsg(uvo.getTele());
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = LTResponseCode.getCode(e.getMessage());
                logger.info("给用户发送短信异常，e={}", e);
            }
        }

        return response.toJsonString();
    }

    /**
     * 获取用户对象
     *
     * @param request
     * @param param
     * @return
     */

    @RequestMapping(value = "/user/getUserObj")
    @ResponseBody
    public String getUserObj(HttpServletRequest request) {

        Response response = null;
        if (request.getParameter("userId") == null) {
            logger.info("查询用户Id为空");
            throw new LTException(LTResponseCode.US20001);
        }
        try {
            UserBase uvo = iUserManageService.getUserBaseFuzzy(request.getParameter("userId"));
            int step = this.iUserManageService.getUserOpenAccount(request.getParameter("userId"));
            uvo.setOpenStep(step);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, uvo);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 查询用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/getUserInfo")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request) {

        Response response = null;
        if (request.getParameter("userId") == null) {
            logger.info("查询用户Id为空");
            throw new LTException(LTResponseCode.US20001);
        }
        try {
            Map<String, Object> resultMap = iUserManageService.getUserInfo(request.getParameter("userId"));
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 获取用户图片
     *
     * @param request
     * @param param
     * @return
     */

    @RequestMapping(value = "/user/showPic")
    @ResponseBody
    public String showPic(HttpServletRequest request) {

        Response response = null;
        UserBase uvo = new UserBase();
        String userId = null;
        if (request.getParameter("userId") == null) {
            logger.info("查询用户Id为空");
            throw new LTException(LTResponseCode.US20001);
        } else {
            userId = request.getParameter("userId");
            System.out.println(userId);
        }
        try {
            uvo = iUserManageService.getUserPic(userId);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, uvo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toJsonString();
    }
   
    /**
     * 获取用户在线状态
     *
     * @param request
     * @param param
     * @return
     */

    @RequestMapping(value = "/user/online")
    @ResponseBody
    public String online(HttpServletRequest request) {
        Response response = null;
        boolean isOnLine = false;
        String userId = null;
        if (request.getParameter("userId") == null) {
            logger.info("查询用户Id为空");
            throw new LTException(LTResponseCode.US20001);
        } else {
            userId = request.getParameter("userId");
        }
        try {
            isOnLine = redisTemplate.opsForSet().isMember(UserContant.REDIS_USER_ONLINE_LIST, userId);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, isOnLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toJsonString();
    }

    /**
     * 获取资金查询条件返回给页面
     *
     * @param request
     * @param param
     * @return
     */

    @RequestMapping(value = "/user/cashColunmList")
    @ResponseBody
    public String cashColunmList(HttpServletRequest request) {
        Response response = null;
        Map cashColumnMap = new HashMap();
        for (CashColumnEnum e : CashColumnEnum.values()) {
            cashColumnMap.put(e.getCode(), e.getName());
        }
        response = LTResponseCode.getCode(LTResponseCode.SUCCESS, cashColumnMap);
        return response.toJsonString();
    }

    /**
     * @param userId
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2017年1月13日 下午2:24:02
     */
    @RequestMapping(value = "/user/getUserLastLoginLog")
    @ResponseBody
    public String getUserLastLoginLog(String userId) {
        Response response = null;
        try {
            if (!StringTools.isNotEmpty(userId)) {
                throw new LTException(LTResponseCode.US03101);
            }
            UserOperateLog log = new UserOperateLog();
            log.setUserId(userId);
            log.setOperateType(UserContant.OPERATE_USER_LOGIN_LOG);
            log.setIsSuccessed(true);
            log = iUserManageService.getUserLastLoginLog(log);
            response = new Response(LTResponseCode.SUCCESS, "查询成功", log);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    /**
     * 查询用户的账户信息
     *
     * @param userId 用户id
     * @return
     * @throws
     * @return: String
     * @author yuanxin
     * @Date 2017年1月13日 下午2:50:36
     */
    @RequestMapping(value = "/user/getUserMainAccount")
    @ResponseBody
    public String getUserMainAccount(String userId) {
        Response response = null;
        try {
            if (!StringTools.isNotEmpty(userId)) {
                throw new LTException(LTResponseCode.US03101);
            }
            FundMainCash mainCash = fundAccountServiceImpl.queryFundMainCash(userId);
            FundMainScore mainScore = fundAccountServiceImpl.queryFundMainScore(userId);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cash", mainCash);
            map.put("score", mainScore);

            response = new Response(LTResponseCode.SUCCESS, "查询成功", map);
        } catch (Exception e) {
            // TODO: handle exception
            response = LTResponseCode.getCode(e.getMessage());
        }

        return response.toJsonString();
    }

    @RequestMapping(value = "/user/getUserDailyAmt")
    @ResponseBody
    public String getUserDailyAmt(String userId) {
        Response response = null;

        try {
            Map<String, Double> amtMap = new HashMap<String, Double>();
            Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
            amtMap = iUserManageService.getUserDailyAmt(userId, rate);
            response = new Response(LTResponseCode.SUCCESS, "查询成功", amtMap);
        } catch (Exception e) {
            // TODO: handle exception
            response = LTResponseCode.getCode(e.getMessage());
        }

        return response.toJsonString();
    }

    @RequestMapping(value = "/user/getUserChannelbankList")
    @ResponseBody
    public String getUserChannelbankList(String userId) {
        Response response = null;

        try {
            List<Map<String, Object>> list = iUserManageService.getBankChannelVoList(userId);

            response = new Response(LTResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            // TODO: handle exception
            response = LTResponseCode.getCode(e.getMessage());
        }

        return response.toJsonString();
    }

    /**
     * 查询在线用户数
     *
     * @return
     */
    @RequestMapping(value = "/user/getUserOnlineNumber")
    @ResponseBody
    public String getUserOnlineNumber() {
        Response response = null;

        try {
            Long count = redisTemplate.opsForSet().size(UserContant.REDIS_USER_ONLINE_LIST);
            response = new Response(LTResponseCode.SUCCESS, "查询成功", count);
        } catch (Exception e) {
            // TODO: handle exception
            response = LTResponseCode.getCode(e.getMessage());
        }

        return response.toJsonString();

    }

    /**
     * 设置用户所属券商
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/setInvestor")
    @ResponseBody
    public String setInvestor(HttpServletRequest request) {

        Response response = null;
        String userId = request.getParameter("userId");
        String investorAccountId = request.getParameter("investorAccountId");
        if (StringTools.isEmpty(userId)) {
            throw new LTException(LTResponseCode.US20001);
        }
        logger.info("设置用户所属券商param={}", investorAccountId);
        try {
            iUserManageService.updateUserInvestor(userId, investorAccountId);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("设置用户所属券商异常，e={}", e);
        }
        return response.toJsonString();
    }
    
    /**
     * 添加限制条件
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/addLimitCondition")
    @ResponseBody
    public String addLimitCondition(HttpServletRequest request) {
    	 Response response = null;
    	 try{
    		 String limitCondition = request.getParameter("limitCondition");
        	 if(StringTools.isEmpty(limitCondition.replaceAll("\\$",""))){
        		 throw new LTException(LTResponseCode.PR00001);
        	 }
        	 BoundHashOperations<String, String, String> limitRedis = redisTemplate.boundHashOps(RedisUtil.LIMIT_CONDITION);
        	 String limit = limitRedis.get(RedisUtil.LIMIT_CONDITION);
        	 if(StringTools.isEmpty(limit)){
        		 limit="";
        	 }
        	 limitRedis.put(RedisUtil.LIMIT_CONDITION, limit+"$"+limitCondition+"$");
        	 response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
    	 }catch(Exception e){
    		 e.printStackTrace();
             response = LTResponseCode.getCode(e.getMessage());
             logger.info("添加限制条件异常，e={}", e);
    	 }  	 
    	 return response.toJsonString();
    }
    
    
    /**
     * 获取限制条件
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/findLimitCondition")
    @ResponseBody
    public String findLimitCondition(HttpServletRequest request) {  	
    	 Response response = null;
    	 try{
        	 BoundHashOperations<String, String, String> limitRedis = redisTemplate.boundHashOps(RedisUtil.LIMIT_CONDITION);
        	 String limit = limitRedis.get(RedisUtil.LIMIT_CONDITION);
        	 response = LTResponseCode.getCode(LTResponseCode.SUCCESS,limit);
    	 }catch(Exception e){
    		 e.printStackTrace();
             response = LTResponseCode.getCode(e.getMessage());
             logger.info("添加限制条件异常，e={}", e);
    	 }  	 
    	 return response.toJsonString();
    }
    
    /**
     * 删除限制
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/removeLimitCondition")
    @ResponseBody
    public String removeLimitCondition(HttpServletRequest request) {  	
    	 Response response = null;
    	 try{
    		 String limitCondition = request.getParameter("limitCondition");
    		 if(StringTools.isEmpty(limitCondition)){
    			 throw new LTException(LTResponseCode.PR00001);
    		 }
    		 logger.info("=======接收参数limitCondition={}=======",limitCondition);
        	 BoundHashOperations<String, String, String> limitRedis = redisTemplate.boundHashOps(RedisUtil.LIMIT_CONDITION);
        	 String limit = limitRedis.get(RedisUtil.LIMIT_CONDITION);
        	 limit = limit.replaceAll("\\$"+limitCondition+"\\$", "");
        	 limitRedis.put(RedisUtil.LIMIT_CONDITION, limit);
        	 response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
    	 }catch(Exception e){
    		 e.printStackTrace();
             response = LTResponseCode.getCode(e.getMessage());
             logger.info("删除限制异常，e={}", e);
    	 }  	 
    	 return response.toJsonString();
    }
}
