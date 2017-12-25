package com.lt.manager.service.impl.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.manager.bean.fund.FundIoCashRechargeVO;
import com.lt.manager.bean.user.BankCard;
import com.lt.manager.bean.user.BankChannelVo;
import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserBaseInfoQueryVO;
import com.lt.manager.dao.user.BankCardManageDao;
import com.lt.manager.dao.user.UserManageDao;
import com.lt.manager.dao.user.UserServiceManageDao;
import com.lt.manager.dao.user.UserServiceMapperDao;
import com.lt.manager.mogodb.user.UserLoggerDao;
import com.lt.manager.service.user.IUserManageService;
import com.lt.model.user.ServiceContant;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.log.UserOperateLog;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;

import javolution.util.FastMap;

/**
 * 查询用户
 *
 * @author licy
 */
@Service
public class UserManageServiceImpl implements IUserManageService {

    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private UserManageDao userManageDao;
    @Autowired
    private UserLoggerDao userLoggerDao;
    @Autowired
    private UserServiceMapperDao userServiceMapperDao;
    @Autowired
    private UserServiceManageDao userServiceManageDao;
    @Autowired
    private BankCardManageDao bankCardManageDao;
    @Autowired
    private IFundAccountApiService fundAccountApiServiceImpl;

    @Override
    public Page<UserBase> queryUserInfoPage(UserBaseInfoQueryVO param)
            throws Exception {

        Page<UserBase> page = new Page<UserBase>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.addAll(userManageDao.selectUserInfoPage(param));
        page.setTotal(userManageDao.selectUserInfoCount(param));

        return page;

    }

    @Override
    public Page<UserBase> queryUserBlackListPage(UserBaseInfoQueryVO param)
            throws Exception {

        Page<UserBase> page = new Page<UserBase>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.addAll(userManageDao.selectUserBlackListPage(param));
        page.setTotal(userManageDao.selectUserBlackListCount(param));

        return page;

    }

    @Override
    public Page<UserBase> queryUserRealNameListPage(UserBaseInfoQueryVO param)
            throws Exception {

        Page<UserBase> page = new Page<UserBase>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.addAll(userManageDao.selectRealNameList(param));
        page.setTotal(userManageDao.selectRealNameListCount(param));

        return page;

    }

    @Override
    public Integer queryUserInfoCount(UserBaseInfoQueryVO param) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateUserBase(UserBaseInfoQueryVO param) throws Exception {
        // TODO Auto-generated method stub
        logger.info("=====进入到修改开户状态方法中 ：{}", JSONObject.toJSONString(param));
        if (param.getOpenAccountStatus() != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            UserBase user = userManageDao.selectUserBase(param.getId());
            if (!user.getOpenAccountStatus().equals(param.getOpenAccountStatus())) {//有做开户状态的修改
                if (param.getOpenAccountStatus() == 1) {
                    map.put("userId", param.getId());
                    map.put("code", ServiceContant.CASH_SERVICE_CODE);
                    map.put("all", ServiceContant.CASH_SERVICE_CODE);
                    // 先删除垃圾数据
                    userServiceMapperDao.deleteUserService(map);
                    //插入正常数据
                    userServiceMapperDao.insertServiceMappper(map);

                    fundAccountApiServiceImpl.doInitFundCashAccount(param.getId());
                }
            }

        }
        userManageDao.updateUserBase(param);
    }

    @Override
    public void updateUserInvestor(String userId, String investorAccountId) {
        String[] userIdArray = userId.split(",");
        List<String> userIds = new ArrayList<>();
        for(int i = 0; i < userIdArray.length; i ++){
            userIds.add(userIdArray[i]);
        }
        userManageDao.updateUserInvestor(userIds, investorAccountId);
    }

    @Override
    public void removeUserBase(String id) throws Exception {
        // TODO Auto-generated method stub
        userManageDao.deleteUserBase(id);
    }

    @Override
    public UserBase getUserBase(String id) throws Exception {
        // TODO Auto-generated method stub
        UserBase userBase = userManageDao.selectUserBase(id);
        return userBase;
    }

    @Override
    public UserBase getUserBaseFuzzy(String id) throws Exception {
        // TODO Auto-generated method stub
        UserBase userBase = userManageDao.selectUserBase(id);
        userBase.setTele(StringTools.fuzzy(userBase.getTele(), 3, 4));
        userBase.setIdCardNum(StringTools.fuzzy(userBase.getIdCardNum(), 3, 3));
        return userBase;
    }

    @Override
    public UserBase getUserPic(String id) throws Exception {
        // TODO Auto-generated method stub
        return userManageDao.selectUserPic(id);
    }

    @Override
    public UserOperateLog getUserLastLoginLog(UserOperateLog log) throws Exception {
        try {
            return userLoggerDao.queryUserOPerateLog(log);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.debug("e:{}", e);
            throw new LTException(LTResponseCode.US00002);
        }
    }

    @Override
    public Map<String, Double> getUserDailyAmt(String userId, Double rate) throws LTException {
        Double topAmt = userServiceManageDao.getUserDailyTopAmt(userId);
        topAmt = topAmt == null ? 0.0 : topAmt;
        rate = rate == null ? 0.0 : rate;
        FundIoCashRechargeVO fundioCashRecharege = userServiceManageDao.getUserRechargeAmt(userId);

        Double amount = fundioCashRecharege.getActualAmount() == null ? 0.0 : fundioCashRecharege.getActualAmount();
        Double rmbAmount = fundioCashRecharege.getActualRmbAmount() == null ? 0.0 : fundioCashRecharege.getActualRmbAmount();

        Double restAmt = DoubleUtils.scaleFormatEnd((topAmt * rate) - amount, 2);
        restAmt = restAmt < 0 ? 0 : restAmt;


        Map<String, Double> amtMap = new HashMap<String, Double>();
        amtMap.put("amount", amount);
        amtMap.put("rmbAmount", rmbAmount);
        amtMap.put("restAmt", restAmt);
        amtMap.put("restRmbAmt", topAmt - rmbAmount < 0 ? 0 : topAmt - rmbAmount);

        return amtMap;
    }

    @Override
    public List<Map<String, Object>> getBankChannelVoList(String userId) throws LTException {
        List<String> channels = userServiceManageDao.getUserChannel(userId);

        List<Map<String, Object>> list = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(channels)) {
            List<BankCard> bankList = bankCardManageDao.selectBankCardByUserId(userId);
            for (BankCard bankCard : bankList) {
                Map<String,Object> map = new HashMap<>();
                map.put("bankName", bankCard.getBankName());
                map.put("bankPic", bankCard.getBankPic());
                String bankCode = bankCard.getBankCode();
                //BankChannelVo bankChannelVo = new BankChannelVo(bankCard.getBankName(), bankCard.getBankPic());
                for (String channel : channels) {
                    BankChargeMapper bankChargeMapper = new BankChargeMapper();
                    bankChargeMapper.setChannelId(channel);
                    bankChargeMapper.setBankCode(bankCode);
                    bankChargeMapper = userServiceManageDao.getUserBankChannelAmt(bankChargeMapper);

                    if (bankChargeMapper == null) {
                        continue;
                    }

                    Double daily = bankChargeMapper.getDailyLimit() == null ? 0 : bankChargeMapper.getDailyLimit();
                    Double single = bankChargeMapper.getSingleLimit() == null ? 0 : bankChargeMapper.getSingleLimit();

                    map.put("dailyLimit_" + channel, daily);
                    map.put("singleLimit_" + channel, single);
                }

                list.add(map);

            }

            return list;
        }

        return new ArrayList<>();
    }

    @Override
    public int getUserOpenAccount(String userId) throws LTException {
        UserBussinessInfo userInfo = this.userManageDao.selectUserInfo(userId);
        int step = 0;
        if (userInfo != null) {
            if (StringTools.isAllNotEmpty(userInfo.getFacePicPath(), userInfo.getIdCardNum()) && StringTools.isAllEmpty(userInfo.getBankCardNum(), userInfo.getRiskRet(), userInfo.getSignPicPath())) {
                step = 1;
            } else if (StringTools.isAllNotEmpty(userInfo.getIdCardNum(), userInfo.getBankCardNum()) && StringTools.isAllEmpty(userInfo.getRiskRet(), userInfo.getSignPicPath())) {
                step = 2;
            } else if (StringTools.isAllNotEmpty(userInfo.getIdCardNum(), userInfo.getBankCardNum(), userInfo.getRiskRet()) && StringTools.isEmpty(userInfo.getSignPicPath())) {
                step = 3;
            } else if (StringTools.isNotEmpty(userInfo.getSignPicPath())) {
                step = 4;
            }
            if (userInfo.getOpenAccountStatus() == 1) {
                return 5;
            }
        }
        return step;
    }

    @Override
    public Map<String, Object> getUserInfo(String userId) throws LTException {
        Map<String, Object> resultMap = FastMap.newInstance();
        try {
            UserBase userBase = userManageDao.selectUserBase(userId);
            List<BankCard> bankCards = userManageDao.selectUserBankInfoByUserId(userId);
            resultMap.put("user", userBase);
            resultMap.put("bank", bankCards);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("【查询用户信息出错】");
        }
        return resultMap;
    }

	@Override
	public Page<UserBase> queryInvestorList(UserBaseInfoQueryVO param) throws Exception {
        Page<UserBase> page = new Page<UserBase>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.addAll(userManageDao.selectInvestorList(param));
        page.setTotal(userManageDao.selectInvestorCount(param));
        return page;
	}
}
