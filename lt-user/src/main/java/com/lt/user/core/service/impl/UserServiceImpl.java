package com.lt.user.core.service.impl;

import com.itrus.raapi.info.UserInfo;
import com.lt.model.sys.QuotaHostBean;
import com.lt.model.user.*;
import com.lt.model.user.log.UserCheckRegcode;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserToken;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.user.core.dao.mogodb.user.LoggerDao;
import com.lt.user.core.dao.sqldb.IUserBussinessDao;
import com.lt.user.core.dao.sqldb.IUserDao;
import com.lt.user.core.service.IUserService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.EmojiFilter;
import com.lt.util.utils.SensitivewordFilter;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.TokenTools;
import com.lt.util.utils.model.Token;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements IUserService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserDao userDao;

    @Autowired
    private LoggerDao loggerDao;

    @Autowired
    private IUserBussinessDao userBussinessDao;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String USER_NICK_NAME_MODIFY = "user:nick:name:modify";

    private final String USER_PASSWORD_ERROR_COUNT = "user:password:error:count:";


    /**
     * 根据ID查询用户基本信息
     *
     * @param id
     * @return
     * @author XieZhibing
     * @date 2017年2月6日 上午11:17:21
     */
    @Override
    public UserBaseInfo queryUserBuyId(String id) {
        // TODO Auto-generated method stub
        return userDao.queryUserBuyId(id);
    }

    @Override
    public boolean chkRegiterMaxCount(String tele, String smsType, String brandId) {
        // 今天时间
        String sCreateDate = CalendarTools.formatDateTime(new Date(), CalendarTools.DATE);
        String eCreateDate = CalendarTools.getNowDateTime();
        logger.debug("时间区间为：{}-{}", sCreateDate, eCreateDate);
        int count = userDao.getTeleMsgCount(tele, sCreateDate, eCreateDate, smsType, brandId);
        return count >= 5;
    }

    @Override
    public boolean chkRegisterCode(String tele, String brandId, String smsType, String clientCode) throws LTException {
        String edate = CalendarTools.getNowDateTime();
        String sdate = CalendarTools.formatDateTime(CalendarTools.addMinutes(new Date(), -30), "yyyy-MM-dd HH:mm:ss");
        String serverCode = "";
        try {
            serverCode = userDao.getAuthCodeByBrand(tele, brandId, sdate, edate, smsType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }
        /* 默认查询30分钟内最新的有效的验证码 */
        if (!clientCode.equals(serverCode)) {
            UserCheckRegcode checkRegcode = new UserCheckRegcode(tele, clientCode, false);
            try {
                loggerDao.saveUserCheckRegCode(checkRegcode);
            } catch (Exception e) {
                e.printStackTrace();
                throw new LTException(LTResponseCode.US00002);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean chkRegisterCode(String tele, String smsType, String clientCode) throws LTException {
        String edate = CalendarTools.getNowDateTime();
        String sdate = CalendarTools.formatDateTime(CalendarTools.addMinutes(new Date(), -30), "yyyy-MM-dd HH:mm:ss");
        String serverCode = "";
        try {
            serverCode = userDao.getAuthCode(tele, sdate, edate, smsType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }
        /* 默认查询30分钟内最新的有效的验证码 */
        if (!clientCode.equals(serverCode)) {
            UserCheckRegcode checkRegcode = new UserCheckRegcode(tele, clientCode, false);
            try {
                loggerDao.saveUserCheckRegCode(checkRegcode);
            } catch (Exception e) {
                e.printStackTrace();
                throw new LTException(LTResponseCode.US00002);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean checkTeleIsUsed(String tele) throws LTException {
        UserBaseInfo baseInfo = new UserBaseInfo();
        baseInfo.setTele(tele);
        baseInfo.setTeleStatus(UserContant.USER_TELE_BIND);
        baseInfo = getBaseUserInfoNoCancel(baseInfo);
        return baseInfo != null ? true : false;
    }

    @Override
    public boolean checkTeleIsUsed(String tele, String brandId) throws LTException {
        UserBaseInfo baseInfo = new UserBaseInfo();
        baseInfo.setTele(tele);
        baseInfo.setBrandId(brandId);
        baseInfo.setTeleStatus(UserContant.USER_TELE_BIND);
        baseInfo = getBaseUserInfoNoCancel(baseInfo);
        return baseInfo != null ? true : false;
    }

    @Override
    public Token initToken(UserBaseInfo baseUser) {
        Token token = new Token();
        String userToken = TokenTools.createToken(baseUser.getUserId() + "");
        token.setIsdeline(false);
        token.setToken(userToken);
        token.setUserSecret(null);
        token.setUserId(null);

        return token;
    }

    @Override
    public void updateToken(String id, String token) throws LTException {
        try {
            UserToken info = loggerDao.selectByIdAndToken(id);
            if (info != null) {
                loggerDao.delToken(id);
            }
            loggerDao.saveToken(new UserToken(id, token, new Date()));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00002);
        }

    }


    @Override
    public Integer userLogin(UserBaseInfo baseInfo) throws LTException {
        UserBaseInfo info = new UserBaseInfo();
        info.setTele(baseInfo.getTele());
        info.setBrandId(baseInfo.getBrandId());
        info.setTeleStatus(UserContant.USER_TELE_BIND);
        info = getBaseUserInfoNoCancel(info);

        if (info == null) {
            throw new LTException(LTResponseCode.US01105);
        }

        if (info.getStatus() != null && info.getStatus() == -20) {
            throw new LTException(LTResponseCode.US02101);
        }

        if (info.getIsBlack().intValue() == UserContant.IS_BLACK_USER) {
            throw new LTException(LTResponseCode.US01116);
        }
        logger.debug("校验密码错误次数");
        int count = pwdErrorCount(info);
        if (count != 5) {
            UserOperateLog operateLog = null;
            if (!baseInfo.getPasswd().equalsIgnoreCase(info.getPasswd())) {
                operateLog = new UserOperateLog(info.getUserId(), UserContant.OPERATE_USER_LOGIN_LOG, "用户登陆",
                        false, "用户登陆失败，密码错误", "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                        baseInfo.getDeviceImei(), baseInfo.getDeviceModel(),
                        baseInfo.getRecordVersion(),
                        baseInfo.getRecordIP(),
                        baseInfo.getRecordLoginMode(),
                        baseInfo.getRecordImei(),
                        baseInfo.getRecordDevice(),
                        baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());
                insertUserOperateLog(operateLog);
                redisTemplate.opsForValue().set(USER_PASSWORD_ERROR_COUNT + info.getUserId(), String.valueOf((count + 1)), 60, TimeUnit.MINUTES);
                if (5 - count - 1 == 0) {
                    throw new LTException(LTResponseCode.US02103);
                }
                return 5 - count - 1;
            } else {
                operateLog = new UserOperateLog(info.getUserId(), UserContant.OPERATE_USER_LOGIN_LOG, "用户登陆",
                        true, "用户登陆成功", "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                        baseInfo.getDeviceImei(), baseInfo.getDeviceModel(),
                        baseInfo.getRecordVersion(),
                        baseInfo.getRecordIP(),
                        baseInfo.getRecordLoginMode(),
                        baseInfo.getRecordImei(),
                        baseInfo.getRecordDevice(),
                        baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());

                insertUserOperateLog(operateLog);

                baseInfo.setUserId(info.getUserId());
                baseInfo.setHeadPic(info.getHeadPic() == null ? "" : info.getHeadPic());
                baseInfo.setNickName(info.getNickName());
                baseInfo.setPersonalSign(info.getPersonalSign() == null ? "" : info.getPersonalSign());
                baseInfo.setIsSetGesturePwd(info.getIsSetGesturePwd().intValue());
                baseInfo.setIsStartGesture(info.getIsStartGesture().intValue());
                baseInfo.setNickStatus(checkNickName(info.getUserId()));

                info.setLastLoginDate(new Date());
                info.setLastLoginImei(baseInfo.getDeviceImei());

                // 返回用户服务
                logger.debug("更新上一次登陆日期和设备IEMI");
                updateUserBaseInfo(info);
                redisTemplate.delete(USER_PASSWORD_ERROR_COUNT + info.getUserId());
                return null;
            }
        } else {
            throw new LTException(LTResponseCode.US02103);
        }
    }

    private int checkNickName(String userId) {
        Integer nickNameModify = (Integer) redisTemplate.opsForHash().get(USER_NICK_NAME_MODIFY, userId);
        if (null != nickNameModify) {
            return nickNameModify.intValue();
        }
        try {
            UserUpdateInfoLog updateLog = new UserUpdateInfoLog();
            updateLog.setUserId(userId);
            updateLog.setUpdate_type(UserContant.OPERATE_USER_UPTNICKNAME_LOG);
            List<UserUpdateInfoLog> updateList = loggerDao.getUserUpdateInfoLog(updateLog);
            if (!CollectionUtils.isEmpty(updateList) && updateList.size() >= 1) {
                redisTemplate.opsForHash().put(USER_NICK_NAME_MODIFY, userId, UserContant.USER_NICK_STATUS_UPD);
                return 1;
            }
            redisTemplate.opsForHash().put(USER_NICK_NAME_MODIFY, userId, UserContant.USER_NICK_STATUS_NOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int pwdErrorCount(UserBaseInfo baseInfo) {
        try {
            String errorCount = redisTemplate.opsForValue().get(USER_PASSWORD_ERROR_COUNT + baseInfo.getUserId());
            return StringTools.isEmpty(errorCount) ? 0 : Integer.valueOf(errorCount);
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public UserBaseInfo updateUserNickName(UserBaseInfo baseInfo) throws LTException {
        String nickName = baseInfo.getNickName();
        String user_id = baseInfo.getUserId();

        if (StringTools.isNotEmpty(nickName)) {
            SensitivewordFilter filter = new SensitivewordFilter();
            Set<String> set = filter.getSensitiveWord(nickName, 1);
            logger.info("昵称包含敏感词的个数为：" + set.size() + "。包含：" + set);
            if (set.size() > 0) {
                throw new LTException(LTResponseCode.US03102);
            }
            int length = StringTools.getChineseLength(nickName);
            if (length > 16 || length < 4) {
                throw new LTException(LTResponseCode.US03103);
            }
            String s = StringTools.illegalChar(nickName);
            if (StringTools.isNotEmpty(s)) {
                throw new LTException(LTResponseCode.US03104);
            }

            UserBaseInfo baseInfo1 = new UserBaseInfo();
            baseInfo1.setNickName(nickName);
            try {
                baseInfo1 = getBaseUserInfoNoCancel(baseInfo1);
            } catch (Exception e) {
                e.printStackTrace();
                throw new LTException(LTResponseCode.US00001);
            }

            if (baseInfo1 != null) {
                throw new LTException(LTResponseCode.US03105);
            }

            logger.debug("查询用户的修改日志");
            Integer nickNameModify = (Integer) redisTemplate.opsForHash().get(USER_NICK_NAME_MODIFY, user_id);

            if (null != nickNameModify && nickNameModify.intValue() == 1) {
                throw new LTException(LTResponseCode.US03106);
            } else {
                logger.debug("开始存入用户昵称修改日志");
                UserOperateLog operateLog = new UserOperateLog(user_id, UserContant.USER_UPDATE_NICKNAME, "用户修改信息",
                        true, "用户修改昵称", "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                        baseInfo.getDeviceImei(), baseInfo.getDeviceModel(),
                        baseInfo.getRecordVersion(),
                        baseInfo.getRecordIP(),
                        baseInfo.getRecordLoginMode(),
                        baseInfo.getRecordImei(),
                        baseInfo.getRecordDevice(),
                        baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());
                insertUserOperateLog(operateLog);

                baseInfo1 = new UserBaseInfo();
                baseInfo1.setUserId(user_id);
                baseInfo1 = getBaseUserInfoNoCancel(baseInfo1);
                UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(baseInfo1.getUserId().toString(), UserContant.USER_UPDATE_NICKNAME,
                        baseInfo1.getUserId().toString(), baseInfo.getDeviceModel(), baseInfo.getIp(), baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());
                userUpdateInfoLog.setContent("用户修改昵称，将签名从" + baseInfo1.getPersonalSign() == null ? "" : baseInfo1.getPersonalSign() + "改为" +
                        nickName);
                insertUserUpdateInfoLog(userUpdateInfoLog);


                baseInfo.setNickName(nickName);
                int count = updateUserBaseInfo(baseInfo);
                boolean flag = count > 0 ? true : false;

                if (flag) {
                    redisTemplate.opsForHash().put(USER_NICK_NAME_MODIFY, user_id, 1);
                    baseInfo.setNickStatus(UserContant.USER_NICK_STATUS_UPD);
                    return baseInfo;
                } else {
                    throw new LTException(LTResponseCode.US00004);
                }
            }
        } else {
            throw new LTException(LTResponseCode.US03101);
        }
    }

    @Override
    public UserBaseInfo updateUserPersonSign(UserBaseInfo baseInfo) throws LTException {
        String personSign = baseInfo.getPersonalSign();
        logger.debug("修改的个性签名为：{}", personSign);
        if (personSign.length() > 100) {
            throw new LTException(LTResponseCode.US03103);
        }
        SensitivewordFilter filter = new SensitivewordFilter();
        Set<String> set = filter.getSensitiveWord(personSign, 1);
        logger.info("个性签名中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        if (set.size() > 0) {
            throw new LTException(LTResponseCode.US03102);
        }
        if(EmojiFilter.containsEmoji(personSign)){
        	logger.info("个性签名包含emjoy表情");
        	throw new LTException(LTResponseCode.US03104);
        }
        UserBaseInfo oldInfo = new UserBaseInfo();
        oldInfo.setUserId(baseInfo.getUserId());
        oldInfo = getBaseUserInfoNoCancel(oldInfo);

        boolean isSuccessed = updateUserBaseInfo(baseInfo) > 0 ? true : false;
        if (isSuccessed) {
            //更新昵称状态缓存
            UserOperateLog operateLog = new UserOperateLog(baseInfo.getUserId(), UserContant.USER_UPDATE_PERSONSIGN, "用户修改信息",
                    true, "用户修改个性签名", "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                    baseInfo.getDeviceImei(), baseInfo.getDeviceModel(),
                    baseInfo.getRecordVersion(),
                    baseInfo.getRecordIP(),
                    baseInfo.getRecordLoginMode(),
                    baseInfo.getRecordImei(),
                    baseInfo.getRecordDevice(),
                    baseInfo.getRecordCarrierOperator(),
                    baseInfo.getRecordAccessMode());
            insertUserOperateLog(operateLog);

            UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(oldInfo.getUserId().toString(), UserContant.USER_UPDATE_PERSONSIGN,
                    oldInfo.getUserId().toString(), baseInfo.getDeviceModel(), baseInfo.getIp(), baseInfo.getRecordCarrierOperator(),
                    baseInfo.getRecordAccessMode());

            userUpdateInfoLog.setContent("用户修改个性签名，将签名从" + oldInfo.getPersonalSign() == null ? "" : oldInfo.getPersonalSign() + "改为" +
                    personSign);
            insertUserUpdateInfoLog(userUpdateInfoLog);

            oldInfo.setPersonalSign(personSign);
            return oldInfo;
        } else {
            throw new LTException(LTResponseCode.US00004);
        }
    }

    @Override
    public void updateUserPwd(UserBaseInfo baseInfo, String oldPwd) throws LTException {
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUserId(baseInfo.getUserId());
        userBaseInfo = getBaseUserInfoNoCancel(userBaseInfo);

        if (oldPwd.equals(userBaseInfo.getPasswd())) {
            userBaseInfo.setPasswd(baseInfo.getPasswd());
            int count = updateUserBaseInfo(userBaseInfo);
            if (count == 0) {
                throw new LTException(LTResponseCode.US00004);
            } else {
                //更新昵称状态缓存
                UserOperateLog operateLog = new UserOperateLog(baseInfo.getUserId(), UserContant.OPERATE_USER_UPTPWD, "用户修改密码",
                        true, "用户修改密码为" + baseInfo.getPasswd(), "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                        baseInfo.getDeviceImei(), baseInfo.getDeviceModel(),
                        baseInfo.getRecordVersion(),
                        baseInfo.getRecordIP(),
                        baseInfo.getRecordLoginMode(),
                        baseInfo.getRecordImei(),
                        baseInfo.getRecordDevice(),
                        baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());
                insertUserOperateLog(operateLog);

                UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(baseInfo.getUserId().toString(), UserContant.OPERATE_USER_UPTPWD,
                        baseInfo.getUserId().toString(), baseInfo.getDeviceModel(), baseInfo.getIp(), baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());
                userUpdateInfoLog.setContent("用户修改密码，将密码从" + oldPwd + "改为" +
                        baseInfo.getPasswd());
                insertUserUpdateInfoLog(userUpdateInfoLog);
            }
        } else {
            throw new LTException(LTResponseCode.US02102);
        }
    }

    @Override
    public UserBaseInfo updateUserHeadPic(UserBaseInfo baseInfo) throws LTException {
        UserOperateLog operateLog = new UserOperateLog(baseInfo.getUserId(), UserContant.USER_UPDATE_HEAD, "用户修改信息",
                true, "用户修改头像为：" + baseInfo.getHeadPic(), "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                baseInfo.getDeviceImei(), baseInfo.getDeviceModel(), baseInfo.getRecordVersion(),
                baseInfo.getRecordIP(),
                baseInfo.getRecordLoginMode(),
                baseInfo.getRecordImei(),
                baseInfo.getRecordDevice(),
                baseInfo.getRecordCarrierOperator(),
                baseInfo.getRecordAccessMode());
        insertUserOperateLog(operateLog);

        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUserId(baseInfo.getUserId());
        userBaseInfo = getBaseUserInfoNoCancel(userBaseInfo);

        UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(baseInfo.getUserId().toString(), UserContant.USER_UPDATE_HEAD,
                baseInfo.getUserId().toString(), baseInfo.getDeviceModel(), baseInfo.getIp(), baseInfo.getRecordCarrierOperator(),
                baseInfo.getRecordAccessMode());
        userUpdateInfoLog.setContent("用户将头像从" + userBaseInfo.getHeadPic() == null ? "" : userBaseInfo.getHeadPic() + "改为" +
                baseInfo.getHeadPic());
        insertUserUpdateInfoLog(userUpdateInfoLog);

        int count = updateUserBaseInfo(baseInfo);
        boolean flag = count > 0 ? true : false;
        if (flag) {
            userBaseInfo.setHeadPic(baseInfo.getHeadPic());
            return userBaseInfo;
        } else {
            throw new LTException(LTResponseCode.US00004);
        }
    }

    @Override
    public UserBaseInfo findUserPwd(UserBaseInfo baseInfo) throws LTException {
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setBrandId(baseInfo.getBrandId());
        userBaseInfo.setTele(baseInfo.getTele());
        userBaseInfo.setTeleStatus(UserContant.USER_TELE_BIND);
        userBaseInfo = getBaseUserInfoNoCancel(userBaseInfo);

        if (userBaseInfo == null) {
            throw new LTException(LTResponseCode.US01105);
        } else {
            String oldPwd = userBaseInfo.getPasswd();
            userBaseInfo.setPasswd(baseInfo.getPasswd());
            int count = updateUserBaseInfo(userBaseInfo);

            if (count > 0) {
                UserOperateLog operateLog = new UserOperateLog(userBaseInfo.getUserId(), UserContant.OPERATE_USER_FINDPWD, "用户找回密码",
                        true, "用户将密码修改为：" + userBaseInfo.getPasswd(), "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                        baseInfo.getDeviceImei(), baseInfo.getDeviceModel(), baseInfo.getRecordVersion(),
                        baseInfo.getRecordIP(),
                        baseInfo.getRecordLoginMode(),
                        baseInfo.getRecordImei(),
                        baseInfo.getRecordDevice(),
                        baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());
                insertUserOperateLog(operateLog);

                UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(userBaseInfo.getUserId().toString(), UserContant.OPERATE_USER_UPTPWD,
                        userBaseInfo.getUserId().toString(), baseInfo.getDeviceModel(), baseInfo.getIp(),
                        baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());
                userUpdateInfoLog.setContent("用户将密码从" + oldPwd + "改为" +
                        baseInfo.getPasswd());
                insertUserUpdateInfoLog(userUpdateInfoLog);

                operateLog = new UserOperateLog(userBaseInfo.getUserId(), UserContant.OPERATE_USER_LOGIN_LOG, "用户登陆",
                        true, "用户登陆成功", "", userBaseInfo.getTele(), userBaseInfo.getVersion(), baseInfo.getIp(), userBaseInfo.getDeviceVersion(),
                        baseInfo.getDeviceImei(), baseInfo.getDeviceModel(), baseInfo.getRecordVersion(),
                        baseInfo.getRecordIP(),
                        baseInfo.getRecordLoginMode(),
                        baseInfo.getRecordImei(),
                        baseInfo.getRecordDevice(),
                        baseInfo.getRecordCarrierOperator(),
                        baseInfo.getRecordAccessMode());

                insertUserOperateLog(operateLog);

                UserUpdateInfoLog updateLog = new UserUpdateInfoLog();
                updateLog.setUserId(userBaseInfo.getUserId().toString());
                updateLog.setUpdate_type(UserContant.OPERATE_USER_UPTNICKNAME_LOG);
                List<UserUpdateInfoLog> updateList = loggerDao.getUserUpdateInfoLog(updateLog);
                if (!CollectionUtils.isEmpty(updateList) && updateList.size() >= 1) {
                    userBaseInfo.setNickStatus(UserContant.USER_NICK_STATUS_UPD);
                } else {
                    userBaseInfo.setNickStatus(UserContant.USER_NICK_STATUS_NOR);
                }
                return userBaseInfo;
            } else {
                throw new LTException(LTResponseCode.US00003);
            }
        }
    }

    @Override
    public boolean checkRegCodeCount(String tele) {
        List<UserCheckRegcode> checkList = loggerDao.getUserCheckRegCode(tele, new Date());
        if (!CollectionUtils.isEmpty(checkList)) {
            return checkList.size() > 100 ? false : true;
        }
        return true;
    }

    @Override
    public List<UserService> UserAutoActiveService(UserBaseInfo baseInfo) throws LTException {
        try {
            UserService userService = new UserService();
            userService.setDefaultStatus(ServiceContant.USERSERVICE_DEFAULT_OPEN);
            List<UserService> serviceList = userBussinessDao.getUserService(userService);

            if (CollectionUtils.isNotEmpty(serviceList)) {
                UserServiceMapper mapper = null;
                for (UserService service : serviceList) {
                    mapper = new UserServiceMapper();
                    if (service.getServiceCode() != null && !service.getServiceCode().equals("")) {
                        mapper.setServiceCode(service.getServiceCode());
                    } else {
                        logger.debug("用户服务表配置错误----------------");
                        serviceList.remove(service);
                        continue;
                    }
                    mapper.setStatus(ServiceContant.USERSERVICE_PROCESSING);
                    mapper.setUserId(baseInfo.getUserId());
                    userBussinessDao.insertUserServiceMapper(mapper);

                }
            }

            return serviceList;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.debug("程序异常", e);
            throw new LTException(LTResponseCode.US00003);
        }
    }

    @Override
    public int insertUserBaseInfo(UserBaseInfo baseInfo) throws LTException {
        try {
            return userDao.insertUserBaseInfo(baseInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }
    }

    @Override
    public UserBaseInfo getBaseUserInfoNoCancel(UserBaseInfo baseInfo) throws LTException {
        try {
            return userDao.getBaseUserInfoNoCancel(baseInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }
    }

    @Override
    public void insertUserOperateLog(UserOperateLog operateLog) throws LTException {
        try {
            loggerDao.saveUserOperateLog(operateLog);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }

    }

    @Override
    public void insertUserUpdateInfoLog(UserUpdateInfoLog operateLog) throws LTException {
        try {
            loggerDao.saveUserUpdateInfoLog(operateLog);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }

    }

    @Override
    public int updateUserBaseInfo(UserBaseInfo baseInfo) throws LTException {
        try {
            int count = userDao.updateUserBaseInfo(baseInfo);
            return count;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00001);
        }
    }

    @Override
    public Integer getCertificationStatus(String userId) throws Exception {
        return userDao.selectCertificationStatus(userId);
    }

    @Override
    public boolean checkUserPasswd(String tokenStr, String passwd)
            throws LTException {
        Token token = TokenTools.parseToken(tokenStr);
        String userId = token.getUserId();
        BoundHashOperations<String, String, Map<String, Integer>> redis = redisTemplate.boundHashOps("CHECK_PWD_COUNT");
        logger.debug("redis 是否为null：{}", redis == null);
        Map<String, Integer> map = redis.get(userId);//存储token和验证次数的map
        Integer count = 0;
        if (map != null) {
            count = map.get(token.getToken()) == null ? 0 : map.get(token.getToken());//已校验密码次数
            if (count > 2) {
                throw new LTException(LTResponseCode.USJ2001);
            }
        }

        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUserId(userId);
        userBaseInfo = userDao.getBaseUserInfo(userBaseInfo).get(0);
        if (passwd.equals(userBaseInfo.getPasswd())) {
            redis.delete(userId);//删除该用户redis
            return true;
        } else {
            if (map == null) {
                map = new HashMap<String, Integer>();
            }
            map.put(token.getToken(), ++count);//验证次数加一
            redis.put(userId, map);//存入redis
            return false;
        }


    }

    @Override
    public int checkUserPasswdCount(String tokenStr, String passwd)
            throws LTException {
        Token token = TokenTools.parseToken(tokenStr);
        String userId = token.getUserId();
        BoundHashOperations<String, String, Map<String, Integer>> redis = redisTemplate.boundHashOps("CHECK_PWD_COUNT");
        logger.debug("redis 是否为null：{}", redis == null);
        Map<String, Integer> map = redis.get(userId);//存储token和验证次数的map
        Integer count = 0;
        if (map != null) {
            count = map.get(token.getToken()) == null ? 0 : map.get(token.getToken());//已校验密码次数
            if (count > 2) {
                throw new LTException(LTResponseCode.USJ2001);
            }
        }

        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUserId(userId);
        userBaseInfo = userDao.getBaseUserInfo(userBaseInfo).get(0);
        if (passwd.equals(userBaseInfo.getPasswd())) {
            redis.delete(userId);//删除该用户redis
            return 0;
        } else {
            if (map == null) {
                map = new HashMap<String, Integer>();
            }
            count++;
            if(count>2){
            	throw new LTException(LTResponseCode.USJ2001);
            }
            map.put(token.getToken(), count);//验证次数加一
            redis.put(userId, map);//存入redis
            return count;
        }


    }

    @Override
    public String queryUserId(String id) {
        return userDao.selectUserId(id);
    }

    @Override
    public void checkToken(String token) {
        UserToken tokens = loggerDao.selectToken(token);
        if (!StringTools.isNotEmpty(tokens)) {
            throw new LTException(LTResponseCode.US01114);
        }
    }

    @Override
    public Integer getUserBaseCountByNickName(String nickName) throws LTException {
        return this.userDao.selectUserBaseCountByNickName(nickName);
    }

    @Override
    public List<QuotaHostBean> findClientQuotaHost() {
        List<QuotaHostBean> list = new ArrayList<QuotaHostBean>();
        BoundHashOperations<String, String, QuotaHostBean> redis = redisTemplate.boundHashOps("REDIS_QUOTA_HOST");
        Map<String, QuotaHostBean> map = redis.entries();
        Iterator<Map.Entry<String, QuotaHostBean>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, QuotaHostBean> entry = entries.next();
            list.add(entry.getValue());
        }
        return list;
    }

    @Override
    public UserBaseInfo findUserInfo(String userid) {

        UserBaseInfo userInfo = new UserBaseInfo();
        userInfo.setUserId(userid);

        List<UserBaseInfo> userBaseInfoList =  userDao.getBaseUserInfo(userInfo);
        if(userBaseInfoList != null && userBaseInfoList.size() > 0){
            return userBaseInfoList.get(0);
        }
        return null;
    }

    @Override
    public void saveCrashLog(Map<String, Object> params) {
        userDao.saveCrashLog(params);
    }
}
