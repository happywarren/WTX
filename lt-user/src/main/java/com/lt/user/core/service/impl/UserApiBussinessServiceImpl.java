package com.lt.user.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.constant.redis.RedisUtil;
import com.lt.model.brand.BrandInfo;
import com.lt.user.core.service.*;
import com.lt.util.utils.StringTools;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.model.fund.FundVo;
import com.lt.model.user.BankInfo;
import com.lt.model.user.ServiceContant;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.UserChargeBankInfo;
import com.lt.model.user.UserService;
import com.lt.model.user.UserServiceMapper;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.vo.user.BankcardVo;
import com.lt.vo.user.UserAccountInfoVo;

/**
 * 项目名称：lt-user
 * 类名称：UserApiBussinessServiceImpl
 * 类描述：
 * 创建人：yuanxin
 * 创建时间：2016年12月7日 下午7:17:40
 */
@Service
public class UserApiBussinessServiceImpl implements IUserApiBussinessService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUServiceBussiness userviceBussiness;
    @Autowired
    private IFundAccountApiService fundAccountService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserAutoRechageService userAutoRechargeServiceImpl;
    @Autowired
    private IUserBankInfoService userBankInfoService;

    @Autowired
    private IBrandInfoService brandInfoService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public void activeUserService(UserServiceMapper mapper) throws LTException {
        logger.info("------------激活服务mapper={}---------", JSONObject.toJSONString(mapper));
        UserService service = new UserService();
        service.setServiceCode(mapper.getServiceCode());
        List<UserService> list = userviceBussiness.queryUserService(service);
        if (CollectionUtils.isEmpty(list)) {
            throw new LTException(LTResponseCode.US04101);
        }
        boolean flag = isUserServiceActived(mapper.getUserId(), mapper.getServiceCode());
        if (!flag) {
            mapper.setStatus(ServiceContant.USERSERVICE_PROCESSING);
            flag = userviceBussiness.activeUserService(mapper);
            if (!flag) {
                throw new LTException(LTResponseCode.US00004);
            }
        } else {
            throw new LTException(LTResponseCode.US04102);
        }
    }

    @Override
    public void userActiveAccount(UserUpdateInfoLog updateInfoLog, String value1, String value2, String value3, String value4, String value5, String value6, String step) throws LTException {
        int orderStep = Integer.parseInt(step);
        switch (orderStep) {
            case 1:
                userviceBussiness.userActivityAccountStep1(updateInfoLog, value1, value2, value3, value4, value5);
                break;
            case 2:
                userviceBussiness.userActivityAccountStep2(updateInfoLog, value1, value2, 1);
                break;
            default:
                throw new LTException(LTResponseCode.US00004);
        }
    }

    @Override
    public ArrayList<UserService> getUserAccountService() {
        UserService service = new UserService();
        ArrayList<UserService> list = (ArrayList<UserService>) userviceBussiness.queryUserService(service);
        return list;
    }

    @Override
    public void activeUserAccountStatus(String userId) {
        userviceBussiness.activeUserAccount(userId);
    }

    @Override
    public UserBussinessInfo getUserBankList(UserBankInfo bankInfo) {
        return userviceBussiness.getUserDefaultBankInfo(bankInfo);
    }

    @Override
    public UserBussinessInfo getUserDefaultBankInfoForDinpay(UserBankInfo bankInfo) {
        return userviceBussiness.getUserDefaultBankInfoForDinpay(bankInfo);
    }

    @Override
    public HashMap<String, Object> userActiveAccount0(String user_id) throws LTException {
        return userviceBussiness.userActivityAccountStep0(user_id);
    }


    @Override
    public List<UserService> getUserHomePageInfo(String userId) throws LTException {
        try {
            List<UserService> list = userviceBussiness.getUserService(userId);
            if (CollectionUtils.isNotEmpty(list)) {
                FundVo fund = fundAccountService.findUserFund(userId);
                for (UserService userService : list) {
                    userService.setUserId(null);
                    if (userService.getServiceCode().equals(ServiceContant.CASH_SERVICE_CODE)) {
                        userService.setAmt(fund.getCashAmt());
                        userService.setFloatAmt(fund.getFloatCashAmt());
                        userService.setHoldAmt(fund.getHoldCashFund());
                    } else if (userService.getServiceCode().equals(ServiceContant.SCORE_SERVICE_CODE)) {
                        userService.setAmt(fund.getScoreAmt());
                        userService.setFloatAmt(fund.getFloatScoreAmt());
                        userService.setHoldAmt(fund.getHoldScoreFund());
                    }
                }
                return list;
            } else {
                throw new LTException(LTResponseCode.FU00002);
            }
        } catch (LTException e) {
            throw new LTException(e.getMessage());
        }
    }

    @Override
    public List<BankInfo> getBankInfo() throws LTException {
        return userviceBussiness.getBankInfo();
    }

    @Override
    public FundVo getUserAmt(String userId) throws LTException {
        return fundAccountService.findUserFund(userId);
    }

    @Override
    public UserBussinessInfo getUserBussiness(UserBaseInfo baseInfo) throws LTException {
        return userviceBussiness.getUserDetailInfo(baseInfo);
    }

    @Override
    public boolean isUserServiceActived(String userId, String serviceCode) throws LTException {
        List<UserServiceMapper> list = userviceBussiness.getUserActivedService(userId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (UserServiceMapper mapper : list) {
                if (serviceCode.equals(mapper.getServiceCode())) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void UpdateUserIdPicAndBankPic(UserBussinessInfo bussinessInfo) throws LTException {
        // TODO Auto-generated method stub
        UserUpdateInfoLog log = new UserUpdateInfoLog();
        log.setUserId(bussinessInfo.getUserId().toString());
        log.setDevice_model(bussinessInfo.getDeviceModel());
        log.setIp(bussinessInfo.getIp());
        userviceBussiness.updateUserIdPicAndBankPic(bussinessInfo, log);
    }

    @Override
    public Map<String, Object> getWithdrawBankInfo(String userId)
            throws LTException {
        return userviceBussiness.getWithdrawBankInfo(userId);
    }

    @Override
    public UserchargeBankDetailInfo getUserChargeBankInfo(String useId) throws LTException {
        UserchargeBankDetailInfo bankDetailInfo = userAutoRechargeServiceImpl.qryLastUserChargeBankInfo(useId);
        if (bankDetailInfo == null) {
            return userviceBussiness.getUserChargeBankDetailInfo(useId);
        } else {
            if (userAutoRechargeServiceImpl.isExitBankCard(useId, bankDetailInfo.getBankCode(), bankDetailInfo.getBankCardNum())) {
                return bankDetailInfo;
            } else {
                return userviceBussiness.getUserChargeBankDetailInfo(useId);
            }
        }
    }

    @Override
    public List<UserChargeBankInfo> getUserChargeBankInfoList(String useId) throws LTException {
        return userviceBussiness.getUserChargeBankInfoList(useId);
    }

    @Override
    public UserAccountInfoVo getUserAccountInfo(String userId) {
        return userviceBussiness.getUserAccountInfo(userId);
    }

    @Override
    public List<BankcardVo> getBankcardVo(String userId) {
        return userviceBussiness.getBankcardVo(userId);
    }

    @Override
    public void deleteBankcard(UserUpdateInfoLog updateInfoLog, int id, String userId) {
        userviceBussiness.deleteBankCard(updateInfoLog, id, userId);
    }

    @Override
    public UserBaseInfo queryUserBuyId(String userId) {
        return userService.queryUserBuyId(userId);
    }

    @Override
    public String queryUserId(String id) {
        return userService.queryUserId(id);
    }

    @Override
    public int getActivityAccountStep(String userId) throws LTException {
        return userviceBussiness.getActivityAccountStep(userId);
    }

    @Override
    public int getUserOpeningCount(String userId, int step) throws LTException {
        return userviceBussiness.getUserOpeningCount(userId, step);
    }

    @Override
    public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum) throws LTException {
        return userviceBussiness.getUserBaseInfoCountByIdCardNum(idCardNum);
    }

    @Override
    public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum, String brandCode) throws LTException {
        //验证品牌
        BrandInfo brandInfo = brandInfoService.getBrandInfoByCode(brandCode);
        String brandId;
        if (!StringTools.isNotEmpty(brandInfo)) {
            //TODO 默认品牌id
            brandId = redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND);
        } else {
            brandId = brandInfo.getBrandId();
        }
        return userviceBussiness.getUserBaseInfoCountByIdCardNum(idCardNum, brandId);
    }

    @Override
    public void userActiveAccount1(UserUpdateInfoLog updateInfoLog, String userName, String idCard,
                                   String urlPositive, String urlReverse, String urlFacePath) throws LTException {
        userviceBussiness.userActivityAccountStep1(updateInfoLog, userName, idCard, urlPositive, urlReverse, urlFacePath);

    }

    @Override
    public Map<String, Object> userActiveAccount2(UserUpdateInfoLog updateInfoLog, String bankName, String bankNum, int isDefalut)
            throws LTException {
        return userviceBussiness.userActivityAccountStep2(updateInfoLog, bankName, bankNum, isDefalut);

    }

    @Override
    public void saveOpenLog(UserUpdateInfoLog userUpdateInfoLog, String step) {
        userviceBussiness.userActivityAccountLog(userUpdateInfoLog, step);

    }

    @Override
    public void userActiveAccount3(UserUpdateInfoLog updateInfoLog, String riskRet, String riskLevel) throws LTException {
        userviceBussiness.userActivityAccountStep3(updateInfoLog, riskRet, riskLevel);

    }

    @Override
    public void userActiveAccount4(UserUpdateInfoLog updateInfoLog, String signPic) throws LTException {
        userviceBussiness.userActivityAccountStep4(updateInfoLog, signPic);

    }

    @Override
    public UserBankInfo getUserBankInfo(String userId, String bankId) {
        return userBankInfoService.getUserBankInfoByUserIdBankCode(userId, bankId);
    }

    @Override
    public Map<String, Object> getUserFundInOutLimitInfo() throws LTException {
        return userviceBussiness.getUserFundInOutLimitInfo();
    }
}
