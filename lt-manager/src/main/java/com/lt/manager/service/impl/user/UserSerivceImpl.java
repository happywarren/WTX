package com.lt.manager.service.impl.user;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.manager.bean.user.UserService;
import com.lt.manager.dao.user.UserServiceManageDao;
import com.lt.manager.service.user.IUserServiceManage;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 银行卡管理实现类
 * @author licy
 *
 */
@Service
public class UserSerivceImpl implements IUserServiceManage {

	@Autowired
	private UserServiceManageDao userServiceManageDao;

	@Autowired
	private IFundAccountApiService fundAccountApiService;

	@Autowired
	private IUserApiBussinessService userApiBussinessService;

	@Autowired
	private IOrderApiService orderApiService;

	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@Override
	public void addUserService(UserService param) throws Exception {
		userServiceManageDao.insertUserService(param);
	}

	@Override
	public void editUserService(UserService param) throws Exception {
		userServiceManageDao.updateUserService(param);
	}

	@Override
	public void removeUserService(Integer id) throws Exception {
		//判断该商品类型是否已被商品占用，如果被占用则不允许删除
		userServiceManageDao.deleteUserService(id);
	}

	@Override
	public Page<UserService> findUserServiceById(String id) {
		return userServiceManageDao.selectUserServiceById(id);
	}

	@Override
	public void updateUserDailyTopAmt(String userId, Double amt) throws LTException {
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("userId", userId);
		paraMap.put("amt", amt);
		userServiceManageDao.updateUserDailyTopAmt(paraMap);
	}

	@Override
	public Double getUserDailyTopAmt(String userId) throws LTException {
		return userServiceManageDao.getUserDailyTopAmt(userId);
	}

	@Override
	public Response withdrawApply(String userId, Double outAmount) {

		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		String sign = "BACKGROUND"+String.valueOf(System.currentTimeMillis());
		Response resp = null;
		try{
			//获取用户信息
			UserBankInfo bankInfo = new UserBankInfo();
			bankInfo.setUserId(userId);
			UserBussinessInfo ubi = userApiBussinessService.getUserBankList(bankInfo);
			if(ubi == null){
				return LTResponseCode.getCode(LTResponseCode.FU00001);
			}
			else if(StringTools.isEmpty(ubi.getIdCardNum()) || StringTools.isEmpty(ubi.getUserName())){
				return LTResponseCode.getCode(LTResponseCode.FU00014);
			}
			String withdrawCode = FundThirdOptCodeEnum.WITHDRAW_AMT.getThirdLevelCode();//提现业务码
			String faxCode = FundThirdOptCodeEnum.WITHDRAW_FEE.getThirdLevelCode();//提现手续费业务码
			if(outAmount == null || StringUtils.isEmpty(withdrawCode) || StringUtils.isEmpty(faxCode)){
				return LTResponseCode.getCode(LTResponseCode.FU00003);
			}
			//获取用户历史下单手数
			Integer count = orderApiService.queryOrdersCounts(userId);
			boolean flag = count>0?false:true;//true:收取手续费
			//提现申请
			Map<String, String> map = fundAccountApiService.doWithdrawApply(userId, outAmount,rate,withdrawCode,faxCode,flag,sign);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS, map);
		}catch(LTException e){
			resp = LTResponseCode.getCode(e.getMessage());
		}
		return resp;

	}

}
