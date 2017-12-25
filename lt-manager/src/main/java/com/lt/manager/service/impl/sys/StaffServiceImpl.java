package com.lt.manager.service.impl.sys;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.sms.ISmsApiService;
import com.lt.manager.bean.log.LtManagerLog;
import com.lt.manager.bean.log.LtManagerLoggerType;
import com.lt.manager.bean.sys.*;
import com.lt.manager.controller.sys.LoginController;
import com.lt.manager.dao.sys.AuthDao;
import com.lt.manager.dao.sys.StaffDao;
import com.lt.manager.dao.sys.StaffDeviceDao;
import com.lt.manager.mail.MailEngine;
import com.lt.manager.service.log.LtManagerLogService;
import com.lt.manager.service.sys.IStaffService;
import com.lt.model.sms.SystemMessage;
import com.lt.model.sms.SystemMessageContant;
import com.lt.model.user.UserContant;
import com.lt.util.ManagerUtils;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaffServiceImpl implements IStaffService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LtManagerLogService loggerService;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private ISmsApiService smsService;

    @Autowired
    private StaffDeviceDao staffDeviceDao;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private MailEngine mailEngine;

    /*
     * 1、判断是否启用ip限制 2、IP是否可登录 是return true 否执行短信校验 3、校验短信 是 true ，否false
     * (non-Javadoc)
     *
     * @see
     * com.lt.manager.service.sys.LoginService#checkIp(com.lt.manager.bean.sys
     * .Staff, java.lang.String, java.lang.String)
     */
    @Override
    public boolean checkIp(Staff staff, String ip) {
        try {
            // 是否启用ip限制
            if (staff.getIsStartUseIpAstrict() != null
                    && staff.getIsStartUseIpAstrict() == 0) {
                // 是
                // 验证IP是否可登录
                if (checkIp(ip)) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.MA00021);
        }

    }

    /**
     * 设备验证
     *
     * @param staff
     * @param code
     * @param uuid
     * @return
     */
    @Override
    public boolean checkDevice(Staff staff, String code, String uuid) {
        try {
            if (staff.getIsStartCode() == 2) {
                return true;
            }
            //如果有验证码用验证码校验
            if (StringTools.isNotEmpty(code)) {
                return checkDevice(code, staff);
            }
            if (StringTools.isNotEmpty(uuid)) {
                if (staffDeviceDao.findByUUID(uuid, staff.getId()) < 1) {
                    return checkDevice(code, staff);
                } else {
                    // 设备号不为空且员工设备库中存在
                    return true;
                }
            }
            throw new LTException(LTResponseCode.MA00025);

        } catch (LTException e) {
            e.printStackTrace();
            throw new LTException(e.getMessage());
        }

    }

    /**
     * 设备验证
     *
     * @param code
     * @param staff
     * @return
     */
    private boolean checkDevice(String code, Staff staff) {
        if (StringTools.isNotEmpty(code)) {
            // 校验验证码
            String authCode = ManagerUtils.teleCodeMap.get(staff.getTele());
            logger.info(JSONObject.toJSONString(ManagerUtils.teleCodeMap));
            if (!StringTools.isNotEmpty(authCode)) {
                throw new LTException(LTResponseCode.MA00020);
            }
            String newCode = authCode.split("\\|")[0];
            String time = authCode.split("\\|")[1];
            long date = Long.valueOf(time);
            date = System.currentTimeMillis() - date;
            if (date > 5 * 60 * 1000) {
                throw new LTException(LTResponseCode.MA00020);
            }
            if (!newCode.equals(code)) {
                throw new LTException(LTResponseCode.MA00020);
            }
            if (checkCode(code, staff.getId())) {
                // 保存员工设备号
                saveDevice(staff.getId(), staff.getIsStartDeviceAstrict());
                return true;
            } else {
                // 短信验证失败 请重新登录
                throw new LTException(LTResponseCode.MA00020);
                // return false;
            }
        } else {
            // 验证码不能为空
            throw new LTException(LTResponseCode.MA00025);
        }
    }

    /**
     * 设备验证
     *
     * @param code
     * @param staff
     * @return
     */
    private boolean checkEmailDevice(String code, Staff staff) {
        if (StringTools.isNotEmpty(code)) {
            // 校验验证码
            String authCode = ManagerUtils.teleCodeMap.get(staff.getEmail());
            logger.info(JSONObject.toJSONString(ManagerUtils.teleCodeMap));
            if (!StringTools.isNotEmpty(authCode)) {
                throw new LTException(LTResponseCode.MA00020);
            }
            String newCode = authCode.split("\\|")[0];
            String time = authCode.split("\\|")[1];
            long date = Long.valueOf(time);
            date = System.currentTimeMillis() - date;
            if (date > 5 * 60 * 1000) {
                throw new LTException(LTResponseCode.MA00020);
            }
            if (!newCode.equals(code)) {
                throw new LTException(LTResponseCode.MA00020);
            }
            saveDevice(staff.getId(), staff.getIsStartDeviceAstrict());
            ManagerUtils.teleCodeMap.remove(staff.getEmail());
            return true;
        } else {
            // 验证码不能为空
            throw new LTException(LTResponseCode.MA00025);
        }
    }

    /**
     * 保存员工设备编码
     *
     * @param id
     * @param status
     */
    private void saveDevice(Integer id, Integer status) {
        try {
            staffDeviceDao.deleteBystaffId(id);
            StaffDevice userPermissions = new StaffDevice();
            userPermissions.setStaffId(id);
            String lastuuid = UUID.randomUUID().toString();
            userPermissions.setUuid(lastuuid);
            userPermissions.setStatus(status);
            staffDeviceDao.save(userPermissions);
            LoginController.map.put(id.toString(), lastuuid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.MA00021, e);
        }
    }

    /**
     * 验证IP是否属于白名单
     *
     * @param ip
     * @return
     */
    private boolean checkIp(String ip) {
        try {
            int i = staffDao.findCountByIp(ip);
            if (i > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.MA00021, e);
        }
    }

    /**
     * 验证登录短信
     *
     * @param code
     * @param staffId
     * @return
     */
    private boolean checkCode(String code, Integer staffId) {
        try {

            logger.info("staffId={},code={}", staffId, code);
            // 数据库表中查出这条数据
            // 比较2个值是否相等
            if (StringTools.isNotEmpty(code)) {
                SystemMessage sms = smsService.findLastCodeByStaffId(staffId);
                if (sms == null) {
                    return false;
                }
                if (sms.getContent().equals(code)) {
                    // if("123456".equals(code)){
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.MA00023);
        }
    }

    @Override
    public Staff selectLoginStaffByUserName(String userName) {
        return staffDao.selectLoginStaffByUserName(userName);
    }

    @Override
    public void updateStaffStatus(Integer id, String status) {
        staffDao.updateStaffStatus(id, status);
    }

    @Override
    public boolean checkErrorNum(Staff staff) {
        List<LtManagerLog> list = loggerService.findLtManagerLogForList(
                Integer.valueOf(staff.getId()),
                LtManagerLoggerType.LOGIN.getCode(), null, 5);
        int j = 0;
        for (LtManagerLog logInfo : list) {
            if (logInfo.getIsSuccessed() == true) {
                break;
            }
            j++;
        }
        if (j >= 5) {
            if (staff.getStatus() == 0) {
                this.updateStaffStatus(staff.getId(), "-1");
            }
            throw new LTException(LTResponseCode.MA00008);
        }
        return true;
    }


    /**
     * 发送短信方法
     *
     * @param tele
     * @param ip
     */
    @Override
    public void sendRegisterMsg(String tele, String ip, Integer staffId) {
        try {
            String authCode = StringTools.getRandom(4);
            /* 对于用户不存在的情况下，用户ID设置为-999 */
            logger.info("后台调用发送短信方法");
            SystemMessage ann = new SystemMessage();
            ann.setUserId(staffId.toString());
            ann.setDestination(tele);
            ann.setContent(authCode);
            ann.setCause(UserContant.MESSAGE_LOGIN_CHECK_MSG_MARK);
            ann.setType(UserContant.SMS_SHORT_TYPE);
            ann.setSmsType(Integer
                    .parseInt(UserContant.MESSAGE_LOGIN_CHECK_MSG_TYPE));
            ann.setPriority(0);
            ann.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS); // 默认为发送成功
            ann.setUserType(0);
            ann.setIp(ip);
            ann.setCreateDate(new Date());
            logger.debug("处理systemMessage对象");
            boolean flag = smsService.sendImmediateMsg(ann);
            if (!flag) {
                throw new LTException(LTResponseCode.US01106);
            }
            String code = authCode + "|" + System.currentTimeMillis();
            ManagerUtils.teleCodeMap.put(tele, code);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US01106);
        }

    }

    @Override
    public boolean checkEmailDevice(Staff staff, String code, String uuid) {
        try {
            if (staff.getIsStartCode() == 2) {
                return true;
            }
            //如果有验证码用验证码校验
            if (StringTools.isNotEmpty(code)) {
                return checkEmailDevice(code, staff);
            }
            if (StringTools.isNotEmpty(uuid)) {
                if (staffDeviceDao.findByUUID(uuid, staff.getId()) < 1) {
                    return checkEmailDevice(code, staff);
                } else {
                    // 设备号不为空且员工设备库中存在
                    return true;
                }
            }
            throw new LTException(LTResponseCode.MA00025);
        } catch (LTException e) {
            e.printStackTrace();
            throw new LTException(e.getMessage());
        }
    }

    /**
     * 发送短信方法
     *
     * @param email
     * @param ip
     */
    @Override
    public void sendRegisterEmailMsg(String email, String ip, Integer staffId) {
        try {
            String authCode = StringTools.getRandom(4);
            boolean flag = mailEngine.sendMail(email, authCode);
            if (!flag) {
                throw new LTException(LTResponseCode.US01106);
            }
            String code = authCode + "|" + System.currentTimeMillis();
            ManagerUtils.teleCodeMap.put(email, code);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US01106);
        }

    }

    @Override
    public Page<Staff> pageSelectStaff(Map<String, Object> paramMap, int page,
                                       int rows) {
        Page<Staff> pg = new Page<Staff>();
        pg.setPageNum(page);
        pg.setPageSize(rows);
        paramMap.put("limit1", (page - 1) * rows);
        paramMap.put("limit2", rows);
        List<Staff> list = staffDao.pageSelectStaff(paramMap);
        pg.addAll(list);
        pg.setTotal(staffDao.pageSelectStaffCount(paramMap));
        return pg;
    }

    @Override
    public List<Auth> queryStaffAuthByStaffId(int statffId) {
        // TODO Auto-generated method stub
        return staffDao.queryStaffAuthByStaffId(statffId);
    }

    @Override
    public Staff queryStaffInfoById(int staffId) {
        return staffDao.queryStaffInfoById(staffId);
    }

    @Override
    public void updateStaffAuth(String staffId, String[] authIdArray) {
        Staff staff = this.queryStaffInfoById(Integer.parseInt(staffId));
        if (staff == null) {
            throw new LTException(LTResponseCode.MA00007);
        }

		/* 员工已拥有的权限 */
        List<Auth> staffAuthList = staffDao.queryStaffAuthByStaffId(Integer.parseInt(staffId));
        /* 要删除的权限 */
        List<Integer> delAuthIdList = new ArrayList<Integer>();
		/* 要新增的权限 */
        List<AuthMap> addAuthMapList = new ArrayList<AuthMap>();
		/* 更新前共有的权限 */
        Set<String> shareAuthSet = new HashSet<String>();

        if (staffAuthList.size() <= 0) {
            for (String s : authIdArray) {
                AuthMap authMap = new AuthMap(Integer.parseInt(s), Integer.parseInt(staffId));
                addAuthMapList.add(authMap);
            }
            int count = authDao.addAuth(addAuthMapList);
            if (count > 0) {
                throw new LTException(LTResponseCode.SUCCESS);
            }
            throw new LTException(LTResponseCode.ER400);
        }

        for (Auth a : staffAuthList) {
            boolean f = true;
            for (String s : authIdArray) {
                if (a.getId() == Integer.parseInt(s)) {
                    shareAuthSet.add(a.getId() + "");
                    f = false;
                    break;
                }
            }
            if (f) {
                delAuthIdList.add(a.getId());
            }
        }

        for (String a : authIdArray) {
            boolean f = true;
            for (String s : shareAuthSet) {
                if (a.equals(s)) {
                    f = false;
                    break;
                }
            }
            if (f) {
                AuthMap authMap = new AuthMap(Integer.parseInt(a), Integer.parseInt(staffId));
                addAuthMapList.add(authMap);
            }
        }
        authDao.delAuth(delAuthIdList);
        authDao.addAuth(addAuthMapList);

    }

    @Override
    public Integer queryStaffByLoginName(String loginName, Integer staffId) {
        // TODO Auto-generated method stub
        return staffDao.queryStaffByLoginName(loginName, staffId);
    }

    @Override
    public void saveStaffInfo(Staff staff) {
        // TODO Auto-generated method stub
        staffDao.saveStaffInfo(staff);
    }

    @Override
    public void updateStaffInfo(Staff staff) {
        // TODO Auto-generated method stub
        staffDao.updateStaffInfo(staff);
    }

    @Override
    public void updateStaffPassword(int userId, String np1) {
        // TODO Auto-generated method stub
        staffDao.updateStaffPassword(userId, np1);

    }

    @Override
    public List<Staff> getStaffAll() {
        // TODO Auto-generated method stub
        return staffDao.getStaffAll();
    }

    @Override
    public void updateIPStatus(int sId, int status) {
        // TODO Auto-generated method stub
        staffDao.updateIPStatus(sId, status);
    }

    @Override
    public void updateSatffIp(String ip, Integer staffId, Integer id, String desc) {
        StaffIp staffIp = new StaffIp();
        staffIp.setCreator(staffId);
        staffIp.setIp(ip);
        staffIp.setId(id);
        staffIp.setDesc(desc);
        staffDao.updateStaffIp(staffIp);
    }

    @Override
    public void saveSatffIp(String ip, Integer staffId, String desc) {
        StaffIp staffIp = new StaffIp();
        staffIp.setCreator(staffId);
        staffIp.setIp(ip);
        staffIp.setDesc(desc);
        staffDao.saveStaffIp(staffIp);
    }

    @Override
    public void deleteStaffIpById(Integer id) {
        staffDao.deleteStaffIpById(id);
    }

    @Override
    public Page<StaffIp> findStaff(Map<String, Object> paramMap, int rows, int page) {
        Page<StaffIp> pg = new Page<StaffIp>();
        pg.setPageNum(page);
        pg.setPageSize(rows);
        paramMap.put("limit1", (page - 1) * rows);
        paramMap.put("limit2", rows);
        List<StaffIp> list = staffDao.findStaffIpById(paramMap);
        pg.addAll(list);
        pg.setTotal(staffDao.findStaffIpCount(paramMap));
        return pg;
    }

    @Override
    public void deleteStaffIpByIds(String ids) {
        // TODO Auto-generated method stub
        if (StringTools.isNotEmpty(ids)) {
            if (ids.contains(",")) {
                String[] array = ids.split(",");
                for (String id : array) {
                    staffDao.deleteStaffIpById(Integer.valueOf(id));
                }
            } else {
                staffDao.deleteStaffIpById(Integer.valueOf(ids));
            }
        }

    }

    @Override
    public void deleteById(Integer id) {
        staffDao.deleteById(id);
    }
}
