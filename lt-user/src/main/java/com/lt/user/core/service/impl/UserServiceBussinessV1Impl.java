package com.lt.user.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.constant.redis.RedisUtil;
import com.lt.model.brand.BrandInfo;
import com.lt.user.core.service.IBrandInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.model.user.BankInfo;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.UserChargeBankInfo;
import com.lt.model.user.UserContant;
import com.lt.model.user.UserService;
import com.lt.model.user.UserServiceMapper;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.model.user.log.UserUpdateInfoLog;
import com.lt.user.core.dao.mogodb.user.LoggerDao;
import com.lt.user.core.dao.sqldb.IUserBussinessDao;
import com.lt.user.core.service.IUServiceBussinessV1;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.vo.user.BankcardVo;
import com.lt.vo.user.UserAccountInfoVo;

/**   
* 项目名称：lt-user   
* 类名称：UserServiceBussinessImpl   
* 类描述： 用户对内接口方法提供  
* 创建人：yuanxin   
* 创建时间：2016年12月7日 下午7:19:48      
*/
@Service
public class UserServiceBussinessV1Impl implements IUServiceBussinessV1 {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserBussinessDao userBussinessDao;

	@Autowired
	private IBrandInfoService brandInfoService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private LoggerDao  loggerDao;
	
	@Override
	public HashMap<String,Object> userActivityAccountStep0(String userId) throws LTException{
		UserBussinessInfo bussinessInfo = new UserBussinessInfo();
		bussinessInfo.setUserId(userId);
		try{
			logger.info("bussinessInfo:{}", JSONObject.toJSONString(bussinessInfo));
			bussinessInfo = userBussinessDao.getUserActivityAccountInfo(bussinessInfo);
			HashMap<String,Object> resultMap = new HashMap<String,Object>();
			
			int step = 0 ;
			if(StringTools.isEmpty(bussinessInfo.getUserName()) 
					&& StringTools.isEmpty(bussinessInfo.getIdCardNum())){
				step = 1 ;
			}else{
				if(StringTools.isEmpty(bussinessInfo.getBankName()) 
						&& StringTools.isEmpty(bussinessInfo.getBankCardNum())){
					if(StringTools.isEmpty(bussinessInfo.getIdPicPositive()) 
							&& StringTools.isEmpty(bussinessInfo.getIdPicReverse())){
						step = 2;
					}else{
						step = 3;
					}
				}else{
					step = 4;
				}
			}
			
			resultMap.put("step", step);
			resultMap.put("bussinessInfo", bussinessInfo);
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
	}
	
	@Override
	public void userActivityAccountStep1(UserUpdateInfoLog updateInfoLog, String user_name, String idCard,String brandCode)  throws LTException{
		String userId = updateInfoLog.getUserId();
		
		UserBussinessInfo bussinessInfo = new UserBussinessInfo();
		bussinessInfo.setIdCardNum(idCard);

		BrandInfo brandInfo = brandInfoService.getBrandInfoByCode(brandCode);
		String brandId;
		if (!StringTools.isNotEmpty(brandInfo)) {
			//TODO 默认品牌id
			brandId = redisTemplate.opsForValue().get(RedisUtil.DEFAULT_BRAND);
			logger.debug("brandId：{}", brandId);
		} else {
			brandId = brandInfo.getBrandId();
		}

		bussinessInfo.setBrandId(brandId);
		bussinessInfo.setUserId(userId);
		try{
			Integer num = userBussinessDao.getUserUserIdCard(bussinessInfo);
			if(num.intValue() > 0){
				throw new LTException(LTResponseCode.US04102);
			}else{
				bussinessInfo.setIdCardNum(null);
			}
			UserBussinessInfo oldInfo  = userBussinessDao.getUserActivityAccountInfo(bussinessInfo);
			bussinessInfo.setUserName(user_name);
			bussinessInfo.setIdCardNum(idCard);
			int result = userBussinessDao.updateUserBussinessInfo(bussinessInfo);
			
			if(result > 0){
				UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_IDINFO, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),null,null);
				userUpdateInfoLog.setContent("用户真实姓名"+oldInfo.getUserName()==null?"":oldInfo.getUserName()+"修改为"+user_name
						+";"+"用户身份证号"+oldInfo.getIdCardNum()==null?"":oldInfo.getIdCardNum()+"修改为"+idCard);
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}else{
				throw new LTException(LTResponseCode.US00004);
			}
		}catch (Exception e) {
			// TODO: handle exception
			throw new LTException(e.getMessage());
		}
	}

	@Override
	public void userActivityAccountStep2(UserUpdateInfoLog updateInfoLog, String url_positive,String url_reverse) throws LTException{
		try{
			String userId = updateInfoLog.getUserId();
			
			UserBussinessInfo bussinessInfo = new UserBussinessInfo();
			bussinessInfo.setUserId(userId);
			UserBussinessInfo oldInfo  = userBussinessDao.getUserActivityAccountInfo(bussinessInfo);
			bussinessInfo.setIdPicPositive(url_positive);
			bussinessInfo.setIdPicReverse(url_reverse);
			
			int result = userBussinessDao.updateUserBussinessInfo(bussinessInfo);
			if(result > 0){
				UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_IDPIC, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),null,null);
				userUpdateInfoLog.setContent("用户身份证正面"+oldInfo.getIdPicPositive()==null?"":oldInfo.getIdPicPositive()+"修改为"+url_positive+";"
						+ "用户身份证反面"+oldInfo.getIdPicReverse()==null?"":oldInfo.getIdPicReverse()+"修改为"+url_reverse);
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}else{
				throw new LTException(LTResponseCode.US00004);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		
		
		
	}

	@Override
	public void userActivityAccountStep3(UserUpdateInfoLog updateInfoLog, String bankCode, String bankNum,int isDefault) throws LTException {
		try{
			String userId = updateInfoLog.getUserId();
			BankInfo info = userBussinessDao.getBankInfoByCode(bankCode);
			UserBankInfo bankInfo = new UserBankInfo();
			bankInfo.setUserId(userId);
			List<UserBankInfo> listBank = userBussinessDao.getUserBankInfo(bankInfo);
			if(CollectionUtils.isNotEmpty(listBank)&&listBank.size() >= 3){
				throw new LTException(LTResponseCode.US05001);
			}
			for(UserBankInfo userBankInfo:listBank){
				if(bankNum.trim().equals(userBankInfo.getBankCardNum())){
					throw new LTException(LTResponseCode.US04102);
				}
			}
			bankInfo.setIsDefault(isDefault);
			bankInfo.setBankCardNum(bankNum);
			bankInfo.setBankName(info.getBankName());
			listBank = userBussinessDao.getUserBankInfo(bankInfo);
			if(CollectionUtils.isNotEmpty(listBank)){
				throw new LTException(LTResponseCode.US04102);
			}
			bankInfo.setBankCode(bankCode);
			int i = userBussinessDao.insertUserBankInfo(bankInfo);
			
			if(i < 1 ){
				throw new LTException(LTResponseCode.US00003);
			}else{
				UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_BANKINFO, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),null,null);
				userUpdateInfoLog.setContent("用户新增银行："+bankCode+"；卡号为"+bankNum);
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
	}



	@Override
	public List<UserService> queryUserService(UserService service) throws LTException {
		try{
			return userBussinessDao.getUserService(service);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
	}

	@Override
	public boolean activeUserService(UserServiceMapper mapper)  throws LTException{
		int i = 0;
		try{
			i = userBussinessDao.insertUserServiceMapper(mapper);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
		if(i < 1){
			return false ;
		}else{
			return true ;
		}
	}

	@Override
	public void activeUserAccount(String userId) {
		userBussinessDao.activeUserAccount(userId);
	}

	@Override
	public UserBussinessInfo getUserDefaultBankInfo(UserBankInfo bankInfo) {
		return userBussinessDao.getUserDefaultBankInfo(bankInfo);
	}

	@Override
	public List<UserService> getUserService(String userId) throws LTException {
		try{
			return userBussinessDao.getUserAccountServiceByUserId(userId);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
	}

	@Override
	public List<BankInfo> getBankInfo() throws LTException {
		try{
			return userBussinessDao.getBankInfo();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
	}

	@Override
	public UserBussinessInfo getUserDetailInfo(UserBaseInfo bankInfo) throws LTException {
		// TODO Auto-generated method stub
		return userBussinessDao.getUserDetailInfo(bankInfo);
	}

	@Override
	public List<UserServiceMapper> getUserActivedService(String userId) throws LTException {
		try{
			return userBussinessDao.getUserActivedService(userId.toString());
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
	}

	@Override
	public void updateUserIdPicAndBankPic(UserBussinessInfo bussinessInfo,UserUpdateInfoLog updateInfoLog) throws LTException {
		bussinessInfo.setRealNameStatus(UserContant.USER_REALNAME_APPLY);
		bussinessInfo.setApplyTime(new Date());
		int result = userBussinessDao.updateUserBussinessInfo(bussinessInfo);
		if(result > 0){
			UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_IDBANKPIC, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),null,null);
			userUpdateInfoLog.setContent("用户身份证正面修改为"+bussinessInfo.getIdPicPositive()+";"
					+ "用户身份证反面修改为"+bussinessInfo.getIdPicPositive()+";"+"用户银行卡修改为"+bussinessInfo.getBankcardPic());
			loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
		}else{
			throw new LTException(LTResponseCode.US01105);
		}
	}

	@Override
	public Map<String, Object> getWithdrawBankInfo(String userId)
			throws LTException {
		return userBussinessDao.selectWithdrawBankInfo(userId);
	}

	@Override
	public UserchargeBankDetailInfo getUserChargeBankDetailInfo(String userId) throws LTException {
		Map<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("userId", userId.toString());
		paraMap.put("currency", CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		return userBussinessDao.getUserChargebankDetail(paraMap);
	}

	
	@Override
	public List<UserChargeBankInfo> getUserChargeBankInfoList(String userId) throws LTException {
		return  userBussinessDao.getUserChargeBankInfoList(userId);
	}

	@Override
	public UserAccountInfoVo getUserAccountInfo(String userId) {
		return userBussinessDao.getUserAccountInfo(userId);
	}

	@Override
	public List<BankcardVo> getBankcardVo(String userId) {
		return userBussinessDao.getBankcardVo(userId);
	}
	
	@Override
	public void deleteBankCard(UserUpdateInfoLog updateInfoLog, int id,
			String userId) {
		try{
			int i = userBussinessDao.deleteBankCard(id, userId);
			
			if(i < 1 ){
				throw new LTException(LTResponseCode.US00003);
			}else{
				UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_BANKINFO, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),null,null);
				userUpdateInfoLog.setContent("用户 userId:"+userId+",删除银行卡 银行卡ID:"+id+"；");
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00004);
		}
	}
}
