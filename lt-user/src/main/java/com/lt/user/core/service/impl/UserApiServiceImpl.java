package com.lt.user.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.promote.IPromoteApiService;
import com.lt.api.sms.ISmsApiService;
import com.lt.api.user.IUserApiService;
import com.lt.constant.LTConstant;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.product.ProductTypeEnum;
import com.lt.model.brand.BrandInfo;
import com.lt.model.sms.SystemMessage;
import com.lt.model.sms.SystemMessageContant;
import com.lt.model.sys.QuotaHostBean;
import com.lt.model.user.*;
import com.lt.model.user.log.UserOperateLog;
import com.lt.user.core.dao.sqldb.IUserAccessBtcDao;
import com.lt.user.core.service.*;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.CodeCreate;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Token;
import com.lt.vo.product.ProductVo;
import com.lt.vo.user.UserProductSelectListVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserApiServiceImpl implements IUserApiService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userServiceImp;

    @Autowired
    private ISmsApiService smsService;

    @Autowired
    private IFundAccountApiService fundAccountService;

    @Autowired
    private IPromoteApiService promoteApiService;

    @Autowired
    private IUserProductSelectService userProductSelectService;

    @Autowired
    private IProductApiService productApiService;

    @Autowired
    private IUserAutoRechageService userAutoRechargeServiceImpl;

    @Autowired
    private IChannelService channelService;

    @Autowired
    private IBrandInfoService brandInfoService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private IUserAccessBtcDao accessBtcDao;


    @Override
    public UserBaseInfo findUserByUserId(String userId) {
        return userServiceImp.queryUserBuyId(userId);
    }

    @Override
    public void sendRegisterMessage(String tele, String ip, String sms_type, String brandCode) throws LTException {
        logger.debug("接收到的手机号：{}", tele);
        logger.debug("品牌：{}", brandCode);
        String cause = "";

        BrandInfo brandInfo = brandInfoService.getBrandInfoByCode(brandCode);
        String brandId;
        if (!StringTools.isNotEmpty(brandInfo)) {
            //TODO 默认品牌id
            brandId = redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND);
            logger.debug("brandId：{}", brandId);
        } else {
            brandId = brandInfo.getBrandId();
        }

        if (sms_type.equals(UserContant.REGISTER_MSG_TYPE)) {
            logger.debug("进入到校验发送次数判断");

            if (userServiceImp.checkTeleIsUsed(tele, brandId)) {
                throw new LTException(LTResponseCode.US01101);
            }

            if (userServiceImp.chkRegiterMaxCount(tele, UserContant.REGISTER_MSG_TYPE, brandId)) {
                throw new LTException(LTResponseCode.US01104);
            }
            cause = UserContant.REGISTER_MSG_MARK;
        } else if (sms_type.equals(UserContant.FINDPWD_MSG_TYPE)) {
            logger.debug("进入到校验是否存在手机号判断");
            if (!userServiceImp.checkTeleIsUsed(tele, brandId)) {
                throw new LTException(LTResponseCode.US01105);
            }

            if (userServiceImp.chkRegiterMaxCount(tele, UserContant.FINDPWD_MSG_TYPE, brandId)) {
                logger.info("进来了么？");
                throw new LTException(LTResponseCode.US01104);
            }
            cause = UserContant.FINDPWD_MSG_MARK;
        }

        String authCode = StringTools.getRandom(4);
        /* 对于用户不存在的情况下，用户ID设置为-999 */
        logger.info("调用发送短信方法");
        SystemMessage ann = new SystemMessage();
        ann.setUserId("-999"); // 注册时不存在用户信息，赋默认值
        ann.setDestination(tele);
        ann.setContent(authCode);
        ann.setCause(cause);
        ann.setType(UserContant.SMS_SHORT_TYPE);
        ann.setSmsType(Integer.parseInt(sms_type));
        ann.setPriority(0);
        ann.setStatus(SystemMessageContant.SEND_STATUS_SUCCESS); // 默认为发送成功
        ann.setUserType(0);
        ann.setIp(ip);
        ann.setCreateDate(new Date());
        ann.setBrandId(brandId);
        logger.debug("处理systemMessage对象");

        boolean flag;
        try {
            flag = smsService.sendImmediateMsg(ann);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new LTException(LTResponseCode.US01006);
        }

        if (!flag) {
            throw new LTException(LTResponseCode.US01106);
        }
    }

    @Override
    public void checkRegCode(String tele, String brandCode, String smsType, String validCode) throws LTException {
        //验证品牌
        BrandInfo brandInfo = brandInfoService.getBrandInfoByCode(brandCode);
        String brandId;
        if (!StringTools.isNotEmpty(brandInfo)) {
            //TODO 默认品牌id
            brandId = redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND);

        } else {
            brandId = brandInfo.getBrandId();
        }
        boolean flag = userServiceImp.chkRegisterCode(tele, brandId, smsType, validCode);
        if (!flag) {
            throw new LTException(LTResponseCode.US01109);
        }
    }

    @Override
    public void checkRegCode(String tele, String smsType, String validCode) throws LTException {
        boolean flag = userServiceImp.chkRegisterCode(tele, smsType, validCode);
        if (!flag) {
            throw new LTException(LTResponseCode.US01109);
        }
    }

    @Override
    @Transactional
    public HashMap<String, Object> userRegister(UserBaseInfo baseInfo, String reg_code) throws LTException {
        //验证品牌
        BrandInfo brandInfo = brandInfoService.getBrandInfoByCode(baseInfo.getBrandCode());
        if (!StringTools.isNotEmpty(brandInfo)) {
            //TODO 默认品牌id
            baseInfo.setBrandId(redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND));
            baseInfo.setInvestorAccountId(LTConstant.DEFAULT_INVESTOR_ID.toString());
        } else {
            baseInfo.setBrandId(brandInfo.getBrandId());
            baseInfo.setInvestorAccountId(brandInfo.getInvestorId());
        }

        boolean flag = userServiceImp.checkTeleIsUsed(baseInfo.getTele(), baseInfo.getBrandId());
        if (flag) {
            throw new LTException(LTResponseCode.US01101);
        }

        flag = userServiceImp.chkRegisterCode(baseInfo.getTele(), baseInfo.getBrandId(), UserContant.REGISTER_MSG_TYPE, reg_code);

        if (!flag) {
            throw new LTException(LTResponseCode.US01109);
        }

        baseInfo.setTeleStatus(UserContant.USER_TELE_BIND);
        baseInfo.setUserId(StringTools.createUserCode());
        int number = userServiceImp.insertUserBaseInfo(baseInfo);
        if (number < 1) {
            throw new LTException(LTResponseCode.US00003);
        } else {
            baseInfo = userServiceImp.getBaseUserInfoNoCancel(baseInfo);
            if (baseInfo == null) {
                throw new LTException(LTResponseCode.US00004);
            }

            //生成昵称
            String nickName = CodeCreate.createNickName(Integer.valueOf(baseInfo.getId()), 2);
            if (userServiceImp.getUserBaseCountByNickName(nickName) > 0) {
                nickName = "新" + nickName;
            }
            baseInfo.setNickName(nickName);
            userServiceImp.updateUserBaseInfo(baseInfo);
            //注册成功 初始化用户的自选列表
            doInitProductOptional(baseInfo.getUserId());

            UserOperateLog operateLog = new UserOperateLog(
                    baseInfo.getUserId(),
                    UserContant.OPERATE_USER__REGISTER_LOG,
                    "用户注册",
                    true,
                    "用户注册成功",
                    "",
                    baseInfo.getTele(),
                    baseInfo.getVersion(),
                    baseInfo.getIp(),
                    baseInfo.getDeviceVersion(),
                    baseInfo.getDeviceImei(),
                    baseInfo.getDeviceModel(),
                    baseInfo.getRecordVersion(),
                    baseInfo.getRecordIP(),
                    baseInfo.getRecordLoginMode(),
                    baseInfo.getRecordImei(),
                    baseInfo.getRecordDevice(),
                    baseInfo.getRecordCarrierOperator(),
                    baseInfo.getRecordAccessMode());
            userServiceImp.insertUserOperateLog(operateLog);
            operateLog = new UserOperateLog(baseInfo.getUserId(), UserContant.OPERATE_USER_LOGIN_LOG, "用户登陆",
                    true, "用户登陆成功", "", baseInfo.getTele(), baseInfo.getVersion(), baseInfo.getIp(), baseInfo.getDeviceVersion(),
                    baseInfo.getDeviceImei(), baseInfo.getDeviceModel(), baseInfo.getRecordVersion(),
                    baseInfo.getRecordIP(),
                    baseInfo.getRecordLoginMode(),
                    baseInfo.getRecordImei(),
                    baseInfo.getRecordDevice(),
                    baseInfo.getRecordCarrierOperator(),
                    baseInfo.getRecordAccessMode());
            userServiceImp.insertUserOperateLog(operateLog);
        }

        List<UserService> serviceList = userServiceImp.UserAutoActiveService(baseInfo);
        logger.info("用户服务列表为：{}", serviceList.size());
        if (CollectionUtils.isNotEmpty(serviceList)) {
            for (UserService service : serviceList) {
                logger.debug("进入到循环中");
                if (service.getServiceCode().equals(ServiceContant.SCORE_SERVICE_CODE)) {
                    logger.debug("进入到初始化资金账户功能中");
                    flag = fundAccountService.doInitFundScoreAccount(baseInfo.getUserId());
                    if (!flag) {
                        throw new LTException(LTResponseCode.US01113);
                    }
                }
            }
        }
        logger.info("用户服务列表为：{}", serviceList.size());
        userAutoRechargeServiceImpl.initUserChargeMapper(baseInfo.getUserId(), baseInfo.getRegSource() + "");

        logger.info("用户注册关联充值渠道完成");
        Token token = userServiceImp.initToken(baseInfo);
        logger.info("实例化token   token:{}", token);

        final String userId = baseInfo.getUserId();
        final String ip = baseInfo.getIp();
        final String tele = baseInfo.getTele();
        new Thread(new Runnable() {
            public void run() {
                /* 构建推广关系信息 */
                promoteApiService.addPromoteUserMapper(userId, ip, tele);
            }
        }).start();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("tokenInfo", token);
        resultMap.put("userInfo", baseInfo);
        resultMap.put("nickStatus", 0);
        //更新id_token库
        userServiceImp.updateToken(baseInfo.getUserId(), token.getToken());
        logger.info("更新id_token库完成");
        return resultMap;
    }

    //	@Override
    public HashMap<String, Object> userLogin(UserBaseInfo baseInfo) throws LTException {
        logger.debug("----------进入到登陆流程--------");
        //验证品牌
        BrandInfo brandInfo = brandInfoService.getBrandInfoByCode(baseInfo.getBrandCode());
        if (!StringTools.isNotEmpty(brandInfo)) {
            //TODO 默认品牌id
            baseInfo.setBrandId(redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND));
        } else {
            baseInfo.setBrandId(brandInfo.getBrandId());
        }

        Integer count = userServiceImp.userLogin(baseInfo);

        if (count != null) {
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("count", count);
            return resultMap;
        }

        logger.debug("------更新token并返回前台数据------");
        Token token = userServiceImp.initToken(baseInfo);

        logger.info("baseInfo:{},token:{}", JSONObject.toJSONString(baseInfo), JSONObject.toJSONString(token));
        /* 构建推广信息 */
        //promote(promoteCode, baseUser.getId().toString(), ip, tele, baseUser);

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("tokenInfo", token);
        resultMap.put("userInfo", baseInfo);
        //更新id_token库
        userServiceImp.updateToken(baseInfo.getUserId(), token.getToken());

        return resultMap;
    }

    @Override
    public UserBaseInfo updateUserNickAndSign(UserBaseInfo baseInfo, int type) throws LTException {
        switch (type) {
            case UserContant.USER_UPDATE_NICKNAME:
                logger.debug("进入到用户昵称=======");
                return userServiceImp.updateUserNickName(baseInfo);
            case UserContant.USER_UPDATE_PERSONSIGN:
                logger.debug("进入到用户修改个性签名=======");
                return userServiceImp.updateUserPersonSign(baseInfo);
            case UserContant.USER_UPDATE_HEAD:
                logger.debug("进入到用户头像=======");
                return userServiceImp.updateUserHeadPic(baseInfo);
        }

        throw new LTException(LTResponseCode.US03101);
    }

    @Override
    public void updateUserPwd(UserBaseInfo baseInfo, String oldPwd) throws LTException {
        userServiceImp.updateUserPwd(baseInfo, oldPwd);
    }

    @Override
    public HashMap<String, Object> findUserPassword(UserBaseInfo baseInfo, String authCode) throws LTException {
        //验证品牌
        BrandInfo brandInfo = brandInfoService.getBrandInfoByCode(baseInfo.getBrandCode());
        if (!StringTools.isNotEmpty(brandInfo)) {
            //TODO 默认品牌id
            baseInfo.setBrandId(redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND));
        } else {
            baseInfo.setBrandId(brandInfo.getBrandId());
        }

        boolean flag = userServiceImp.chkRegisterCode(baseInfo.getTele(), baseInfo.getBrandId(), UserContant.FINDPWD_MSG_TYPE, authCode);
        if (flag) {
            baseInfo = userServiceImp.findUserPwd(baseInfo);
            Token token = userServiceImp.initToken(baseInfo);

            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("tokenInfo", token);
            resultMap.put("userInfo", baseInfo);
            //更新id_token库
            userServiceImp.updateToken(baseInfo.getUserId(), token.getToken());
            redisTemplate.delete("user:password:error:count:" + baseInfo.getUserId());
            return resultMap;

        } else {
            throw new LTException(LTResponseCode.US01109);
        }
    }

    @Override
    public Integer getCertificationStatus(String userId) throws Exception {
        return userServiceImp.getCertificationStatus(userId);
    }

    @Override
    public boolean checkUserPasswd(String token, String passwd)
            throws LTException {
        return userServiceImp.checkUserPasswd(token, passwd);
    }

    @Override
    public int checkUserPasswdCount(String token, String passwd)
            throws LTException {
        return userServiceImp.checkUserPasswdCount(token, passwd);
    }

    /**
     * 给用户初始化自选商品
     *
     * @param userId
     */
    private void doInitProductOptional(String userId) {
        List<UserProductSelectListVo> list = productApiService.findAttentionList(null, -1);
        if (StringTools.isNotEmpty(list)) {
            for (UserProductSelectListVo vo : list) {
                userProductSelectService.addProductOptional(userId, vo.getId());
            }
        }
    }


    @Override
    public void addProductOptional(String userId, String productIds) {
        userProductSelectService.addProductOptional(userId, productIds);
    }

    @Override
    public void updateProductOptional(String userId, String productIds) {
        userProductSelectService.updateProductOptional(userId, productIds);
    }

    @Override
    public List<UserProductSelectListVo> selectProductOptional(String userId) {
        //查询用户是否存在所属券商
        UserBaseInfo info = userServiceImp.queryUserBuyId(userId);
        logger.info("UserBaseInfo :{}", JSONObject.toJSON(info));
        if (StringTools.isNotEmpty(info) && StringTools.isNotEmpty(info.getInvestorAccountId())) {
            logger.info("userId :{} accountId{}", userId, info.getInvestorAccountId());
            return userProductSelectService.selectProductForInvestorGroup(userId, info.getInvestorAccountId());
        }
        return userProductSelectService.selectProductOptional(userId);
    }

    @Override
    public List<UserProductSelectListVo> selectProductOptional(String userId, String clientVersion) throws LTException {
        //如果版本号clientVersion小于2.3.0，排除差价合约商品类型
        String excludeProductTypeCode = "";
        if (StringTools.isEmpty(clientVersion)) {
            excludeProductTypeCode = ProductTypeEnum.CONTRACT.getCode();
        } else {
            if (clientVersion.compareTo(LTConstant.CFD_MIN_VERSION) < 0) {
                excludeProductTypeCode = ProductTypeEnum.CONTRACT.getCode();
            }
        }

        //查询用户是否存在所属券商
        UserBaseInfo info = userServiceImp.queryUserBuyId(userId);
        logger.info("UserBaseInfo :{}", JSONObject.toJSON(info));
        if (StringTools.isNotEmpty(info) && StringTools.isNotEmpty(info.getInvestorAccountId())) {
            logger.info("userId :{} accountId{}", userId, info.getInvestorAccountId());
            return userProductSelectService.selectProductForInvestorGroup(userId, info.getInvestorAccountId(), excludeProductTypeCode);
        }
        return userProductSelectService.selectProductOptionalByCondition(userId, excludeProductTypeCode);
    }

    @Override
    public void deleteProductOptional(String userId, String productCode) {
        userProductSelectService.deleteProductOptional(userId, productCode);
    }

    @Override
    public void addProductOptionalByCode(String userId, String productCode) {
        ProductVo info = productApiService.getProductInfo(productCode);
        if (!StringTools.isNotEmpty(info)) {
            throw new LTException(LTResponseCode.MA00017);
        }
//		UserProductSelect ups = userProductSelectService.selectProductOptional(userId,info.getId());
        int id = info.getId();
        UserProductSelect ups = userProductSelectService.selectProductOptional(userId, info.getShortCode());
        if (StringTools.isNotEmpty(ups)) {
            throw new LTException(LTResponseCode.US04102);
        }
        userProductSelectService.addProductOptional(userId, id);

    }

    @Override
    public void checkToken(String token) throws LTException {
        userServiceImp.checkToken(token);
    }


    @Override
    public List<QuotaHostBean> findClientQuotaHost() {
        return userServiceImp.findClientQuotaHost();
    }

    @Override
    public String getChannelCode(String code, String deviceType) {
        return channelService.getChannelCode(code, deviceType);
    }

    @Override
    public void userAccessBtc(String userId) {

        UserAccessBtc userAccessBtc = new UserAccessBtc();
        userAccessBtc.setUserId(userId);
        userAccessBtc.setIsAccess(true);
        if (accessBtcDao.findUserAccessBtc(userId) != null) {
            throw new LTException(LTResponseCode.USJ2002);
        }
        accessBtcDao.userAccessBtc(userAccessBtc);
    }

    @Override
    public boolean findUserAccessBtc(String userId) {
        UserAccessBtc userAccessBtc = accessBtcDao.findUserAccessBtc(userId);

        if (userAccessBtc.getIsAccess() == null) {
            userAccessBtc.setIsAccess(false);
        }

        return userAccessBtc.getIsAccess();
    }

}
