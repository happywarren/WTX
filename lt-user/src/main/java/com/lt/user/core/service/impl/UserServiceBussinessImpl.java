package com.lt.user.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.model.user.BankInfo;
import com.lt.model.user.UserBankCardAuth;
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
import com.lt.user.core.service.IUServiceBussiness;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.youdun.BankCardTools;
import com.lt.vo.user.BankcardVo;
import com.lt.vo.user.UserAccountInfoVo;

import javolution.util.FastMap;

//import javolution.util.FastMap;

/**   
* 项目名称：lt-user   
* 类名称：UserServiceBussinessImpl   
* 类描述： 用户对内接口方法提供  
* 创建人：yuanxin   
* 创建时间：2016年12月7日 下午7:19:48      
*/
@Service
public class UserServiceBussinessImpl implements IUServiceBussiness {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserBussinessDao userBussinessDao;
	
	@Autowired
	private LoggerDao  loggerDao;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;	
	
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
			logger.error(e.getMessage());
			throw new LTException(LTResponseCode.US00001);
		}
	}
	
	@Override
	@Transactional
	public void userActivityAccountStep1(UserUpdateInfoLog updateInfoLog, String userName, String idCard,
			String urlPositive, String urlReverse, String urlFacePath) throws LTException {
		String userId = updateInfoLog.getUserId();
		UserBussinessInfo bussinessInfo = new UserBussinessInfo();
		bussinessInfo.setIdCardNum(idCard);
/*		Integer num = userBussinessDao.getUserUserIdCard(bussinessInfo);
		if(num.intValue() > 0){
			//throw new LTException(LTResponseCode.US04102);
		}*/
/*		SequenceUtil sequence = SequenceUtil.getSequence();
		sequence.setIdColName("seq_id");
		sequence.setNameColName("seq_name");
		sequence.setTableName("sequence");*/
/*		Map<String, Object> resultMap =  SpeedyCloudUtils.getSpeedyCloudUtils().uploadImageFromUrl("idcard"+System.currentTimeMillis()+".jpg", urlPositive);
		String resultId = (String)resultMap.get("resultId");
		if("SUCCESS".equals(resultId)){
			urlPositive = (String) resultMap.get("filePath");
		}else{
			throw new LTException(LTResponseCode.US01122);
		}
		resultMap =  SpeedyCloudUtils.getSpeedyCloudUtils().uploadImageFromUrl("idcard"+System.currentTimeMillis()+".jpg", urlReverse);
		resultId = (String)resultMap.get("resultId");
		if("SUCCESS".equals(resultId)){
			urlReverse = (String) resultMap.get("filePath");
		}else{
			throw new LTException(LTResponseCode.US01122);
		}
		resultMap = SpeedyCloudUtils.getSpeedyCloudUtils().uploadImageFromUrl("idcard"+System.currentTimeMillis()+".jpg",urlFacePath);
		resultId = (String)resultMap.get("resultId");
		if("SUCCESS".equals(resultId)){
			urlFacePath = (String) resultMap.get("filePath");
		}else{
			throw new LTException(LTResponseCode.US01122);
		}	*/	

		bussinessInfo.setUserId(userId);
		UserBussinessInfo oldInfo  = userBussinessDao.getUserActivityAccountInfo(bussinessInfo);
		bussinessInfo.setUserName(userName);
		bussinessInfo.setIdCardNum(idCard);
		bussinessInfo.setIdPicPositive(urlPositive);
		bussinessInfo.setIdPicReverse(urlReverse);
		bussinessInfo.setFacePicPath(urlFacePath);
		bussinessInfo.setRealNameStatus(2);//实名审核成功
		int result = userBussinessDao.updateUserBussinessInfo(bussinessInfo);
		logger.info("result:"+result);
		if(result > 0){
			UserUpdateInfoLog userUpdateInfoLog1 = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_IDINFO, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),updateInfoLog.getRecordCarrierOperator(), updateInfoLog.getRecordAccessMode());
			userUpdateInfoLog1.setContent("用户真实姓名:"+(StringTools.isEmpty(oldInfo.getUserName())?"":oldInfo.getUserName()+"修改为"+userName)+userName+";"
										+"用户身份证号:"+(StringTools.isEmpty(oldInfo.getIdCardNum())?"":oldInfo.getIdCardNum()+"修改为"+idCard)+idCard);
			loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog1);
			
			UserUpdateInfoLog userUpdateInfoLog2 = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_IDPIC, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),updateInfoLog.getRecordCarrierOperator(), updateInfoLog.getRecordAccessMode());
			userUpdateInfoLog2.setContent("用户身份证正面:"+(StringTools.isEmpty(oldInfo.getIdPicPositive())?"":oldInfo.getIdPicPositive()+"修改为"+urlPositive+";")
										+ "用户身份证反面:"+(StringTools.isEmpty(oldInfo.getIdPicReverse())?"":oldInfo.getIdPicReverse()+"修改为"+urlReverse+";")
										+ "用户人脸照片:"+(StringTools.isEmpty(oldInfo.getFacePicPath())?"":oldInfo.getFacePicPath()+"修改为"+urlFacePath));
			loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog2);
			
		}else{
			throw new LTException(LTResponseCode.US00004);
		}
	}


	@Override
	@Transactional
	public Map<String, Object> userActivityAccountStep2(UserUpdateInfoLog updateInfoLog, String bankCode, String bankNum,int isDefault) throws LTException {
		Map<String, Object> resultMap = FastMap.newInstance();
		String userId = updateInfoLog.getUserId();
		
		//绑定银行卡有每天10验证机会
		if(isDefault==0){
			int openingCnt = getUserOpeningCount(userId,6);
			if(openingCnt==0){
				logger.error("【绑定银行卡验证】当前银行卡验证剩余次数为0");
				resultMap.put("openingCnt", "0");
				return resultMap;
			}
		}
		
		//检查银行卡绑定信息
		BankInfo info = userBussinessDao.getBankInfoByCode(bankCode);
		UserBankInfo bankInfo = new UserBankInfo();
		bankInfo.setUserId(userId);
		List<UserBankInfo> listBank = userBussinessDao.getUserBankInfo(bankInfo);
		boolean defaultBankCard = false;
		
		if(CollectionUtils.isEmpty(listBank)){
			logger.info("进来了？");
			defaultBankCard = true;
		}else{
			logger.info("listBank:{},banknum:{}",JSONObject.toJSONString(listBank),bankNum);
			for(UserBankInfo userBankInfo : listBank){
				if(bankNum.trim().equals(userBankInfo.getBankCardNum().trim())){
					logger.error("========当前用户已绑定银行卡！");
					throw new LTException(LTResponseCode.US04102);
				}
			}
		}
		
		logger.info("listBank:{},banknum:{}",JSONObject.toJSONString(listBank),bankNum);
		
		if(CollectionUtils.isNotEmpty(listBank)&&listBank.size() >= 3){
			logger.error("==========绑定银行卡不能超过3张，当前已绑定数量："+listBank.size());
			throw new LTException(LTResponseCode.US05001);
		}

		bankInfo.setIsDefault(isDefault);
		bankInfo.setBankCardNum(bankNum);
		bankInfo.setBankName(info.getBankName());
		listBank = userBussinessDao.getUserBankInfo(bankInfo);
		if(CollectionUtils.isNotEmpty(listBank)){
			logger.error("========当前用户已绑定银行卡！");
			throw new LTException(LTResponseCode.US04102);
		}
		bankInfo.setBankCode(bankCode);
		UserBaseInfo userBaseInfo = userBussinessDao.getUserBaseInfo(userId);
		
		try{
			//查询有盾配置信息,初始化
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
/*			sysCfgRedis.put("youdun_bankcard_url", "https://api4.udcredit.com");
			sysCfgRedis.put("youdun_bankcard_servicecode", "O1001S0302");
			sysCfgRedis.put("youdun_bannkcard_pubkey", "7acf38b8-f850-4d7c-b5fd-84d226562cda");
			sysCfgRedis.put("youdun_bankcard_secretkey", "ef722df3-9a83-4988-9b09-7ddfd313112c");*/
			
			String url = sysCfgRedis.get("youdun_bankcard_url");
			//String url = "https://api4.udcredit.com";
			String pubkey = sysCfgRedis.get("youdun_bannkcard_pubkey");
			//String pubkey =  "7acf38b8-f850-4d7c-b5fd-84d226562cda";
			String secretkey = sysCfgRedis.get("youdun_bankcard_secretkey");
			//String secretkey =  "ef722df3-9a83-4988-9b09-7ddfd313112c";
			String serviceCode = sysCfgRedis.get("youdun_bankcard_servicecode");
			//String serviceCode =  "O1001S0302";
			String reqId = System.currentTimeMillis()+"";
			UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_BANKINFO, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),updateInfoLog.getRecordCarrierOperator(), updateInfoLog.getRecordAccessMode());
			userUpdateInfoLog.setContent("用户新增银行："+info.getBankName()+";卡号为"+bankNum);
			loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			//更新日志
			if(isDefault==0){
				userActivityAccountLog(userUpdateInfoLog, "6");
			}else{
				userActivityAccountLog(userUpdateInfoLog, "2");
			}
			
			
			Map<String, String> parameter = new HashMap<String, String>();
			//银行卡三要素验证 id_no:身份证号码;id_name:身份证姓名;bank_card_no:银行卡号
			parameter.put("id_no",userBaseInfo.getIdCardNum()); 
			parameter.put("id_name", userBaseInfo.getUserName()); 
			parameter.put("bank_card_no", bankNum);
			String result = BankCardTools.apiCall(url, pubkey, secretkey, serviceCode, reqId, parameter);
			JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
			@SuppressWarnings("unchecked")
			Map<String,Object> header = (Map<String, Object>) jsonObject.get("header");
			String retCode = (String) header.get("ret_code");
			/*retCode = "000000";//测试，正式需删除*/
			String retMsg = (String)header.get("ret_msg");
			/*retMsg  = "发送成功";//测试，正式需删除*/
			logger.info("【有盾银行卡验证结果:retCode=】"+retCode);
			
			//查询剩余次数
			int openingCount = getUserOpeningCount(userId, 2);
			resultMap.put("openingCount", openingCount);
			if("000000".equals(retCode)){
				@SuppressWarnings("unchecked")
				Map<String,Object> body= (Map<String, Object>) jsonObject.get("body");
				String resId = (String) body.get("ud_order_no");
				String status = (String) body.get("status");
				
				//认证结果状态码， 1-认证一致， 2-认证不一致， 3-无结果（在公安数据库中查询不到此条数据）
				logger.info("【有盾银行卡验证结果:status=】"+status);
				resultMap.put("resultId", status);
				resultMap.put("reultMsg", "1".equals(status)?"认证一致":("2".equals(status)?"认证不一致":"认证系统查询不到此银行卡信息"));
				
				

/*				String resId = "10001";
				String status = "1";
				resultMap.put("resultId", "1");
				resultMap.put("resultMsg", "success");*/
				if("1".equals(status)){
					//验证通过保存到数据库
					UserBankCardAuth userBankCardAuth = new UserBankCardAuth();
					userBankCardAuth.setUserId(userUpdateInfoLog.getUserId());
					userBankCardAuth.setUserName(userBaseInfo.getUserName());
					userBankCardAuth.setBankCardNo(bankNum);
					userBankCardAuth.setReqId(reqId);
					userBankCardAuth.setResId(resId);
					userBankCardAuth.setResultId(retCode);
					userBankCardAuth.setResultMsg(retMsg);
					userBussinessDao.insertUserBaseCardAuth(userBankCardAuth);
					if(defaultBankCard){
						isDefault = 1;
						bankInfo.setIsDefault(isDefault);
					}
					int i = userBussinessDao.insertUserBankInfo(bankInfo);
					if(i < 1 ){
						logger.error("==========保存银行卡信息失败=============");
						throw new LTException(LTResponseCode.US00003);
					}
				}
			}else {
				logger.info("==================银行卡验证失败!错误码："+retCode+";错误信息:"+retMsg);
				resultMap.put("resultId", "0");
				resultMap.put("resultMsg", "银行卡认证出现异常！");
				return resultMap;
			}
			 
		}catch(Exception e){
			e.printStackTrace();
			logger.error("================系统调用有盾银行卡验证异常："+e.getMessage());
			throw new LTException(LTResponseCode.US01121);
		}
		return resultMap;
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
	public UserBussinessInfo getUserDefaultBankInfoForDinpay(UserBankInfo bankInfo) {
		return userBussinessDao.getUserDefaultBankInfoForDinpay(bankInfo);
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
			List<BankInfo> list = userBussinessDao.getBankInfo();
			if(CollectionUtils.isNotEmpty(list)){
				for(BankInfo bankInfo : list){
					List<Double> amtList = userBussinessDao.getMaxChargeAmt(bankInfo.getBankCode());
					
					logger.info("amtList:{}",JSONObject.toJSONString(amtList));
					if(CollectionUtils.isNotEmpty(amtList)){
						bankInfo.setSingleAmt(amtList.get(0));
						bankInfo.setDailyAmt(amtList.get(1));
					}
				}
				
				logger.info("处理结果：{}",JSONObject.toJSONString(list));
			}
			return list;
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
			// TODO: handle exception
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
			UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_IDBANKPIC, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),updateInfoLog.getRecordCarrierOperator(), updateInfoLog.getRecordAccessMode());
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
				UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_BANKINFO, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),updateInfoLog.getRecordCarrierOperator(), updateInfoLog.getRecordAccessMode());
				userUpdateInfoLog.setContent("用户 userId:"+userId+",删除银行卡 银行卡ID:"+id+"；");
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00004);
		}
	}

	@Override
	public int getActivityAccountStep(String userId) throws LTException {
		UserBussinessInfo bussinessInfo = new UserBussinessInfo();
		//UserBankInfo userBankInfo = new UserBankInfo();
		bussinessInfo.setUserId(userId);
		//userBankInfo.setUserId(userId);
		try{
			logger.info("-------------开始查询此用户开户步骤-------------");
			logger.info("bussinessInfo:{}", JSONObject.toJSONString(bussinessInfo));
			
			bussinessInfo = userBussinessDao.getUserActivityAccountInfo(bussinessInfo);
			//List<UserBankInfo> userBankInfos = userBussinessDao.getUserBankInfo(userBankInfo);
			//if(userBankInfos!=null && userBankInfos.size()>0){
				
			//}
			if(null == bussinessInfo){
				logger.error("=====查询用户信息失败！====");
				throw new LTException(LTResponseCode.US01105);
			}
			int step = 1 ;
			if(StringTools.isAllEmpty(bussinessInfo.getBankCardNum(),bussinessInfo.getRiskRet(),bussinessInfo.getSignPicPath()) && StringTools.isAllNotEmpty(bussinessInfo.getFacePicPath(),bussinessInfo.getIdCardNum())){
				step=2;
			}else if (StringTools.isAllNotEmpty(bussinessInfo.getIdCardNum(),bussinessInfo.getBankCardNum()) && StringTools.isAllEmpty(bussinessInfo.getRiskRet(),bussinessInfo.getSignPicPath()) ) {
				step=3;
			}else if (StringTools.isAllNotEmpty(bussinessInfo.getIdCardNum(),bussinessInfo.getBankCardNum(),bussinessInfo.getRiskRet()) && StringTools.isEmpty(bussinessInfo.getSignPicPath())) {
				step=4;
			}else if(StringTools.isNotEmpty(bussinessInfo.getSignPicPath())){
				step=5;
			}
			logger.info("-------------开始查询此用户开户步骤为"+step+"-------------");
			return step;
		}catch (Exception e) {
			logger.error(e.getMessage());
			throw new LTException(LTResponseCode.US00001);
		}
	}

	@Override
	public int getUserOpeningCount(String userId,int step) throws LTException {
		try{
			int openingCount = 0;
			logger.info("-------------开始查询此用户当天开户次数-------------");
			UserUpdateInfoLog updateInfoLog = null;
			if(step==1){
				updateInfoLog = new UserUpdateInfoLog(userId, UserContant.OPEN_STEP_ONE, null, null,null,null,null);
			}else if(step==2){
				updateInfoLog = new UserUpdateInfoLog(userId, UserContant.OPEN_STEP_TWO, null, null,null,null,null);
			}else if(step==6){
				//添加银行卡次数
				updateInfoLog = new UserUpdateInfoLog(userId, UserContant.BINDING_BANK_CARD, null, null,null,null,null);
			}else{
				return 3;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND,0);
			Date updateTime = calendar.getTime();
			List<UserUpdateInfoLog> userUpdateInfoLogs = this.loggerDao.getUserUpdateInfoLogByTime(updateInfoLog, updateTime);
			if(userUpdateInfoLogs!=null && userUpdateInfoLogs.size()>0){
				openingCount = userUpdateInfoLogs.size();
			}
			logger.info("-------------查询此用户当天开户次数:"+openingCount+"-------------");
			if(step!=6){
				if(openingCount>=3){
					openingCount=0;
				}else{
					openingCount = 3-openingCount;
				}
			}else{
				//添加银行卡次数
				if(openingCount>=10){
					openingCount=0;
				}else{
					openingCount = 10-openingCount;
				}
			}
			logger.info("-------------查询此用户当天剩余开户次数:"+openingCount+"-------------");
			return openingCount;
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			throw new LTException(LTResponseCode.US00001);
		}
		
	}

	@Override
	public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum) throws LTException {
		Map<String, Object> resultMap = FastMap.newInstance();
		if(StringTools.isEmpty(idCardNum)){
			logger.debug("idCardNum:"+idCardNum);
			throw new LTException(LTResponseCode.US01119);
		}
		try{
			/**检查用户年龄是否满20周岁**/
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String openingAgeUpperLimitInfo = sysCfgRedis.get("opening_age_upper_limit");
			String openingAgeLowLimitInfo = sysCfgRedis.get("opening_age_low_limit");
			
			int upperlimit = Integer.parseInt(openingAgeUpperLimitInfo.split("-")[0]);
			int lowLimit = Integer.parseInt(openingAgeLowLimitInfo.split("-")[0]);
			String bornDate = idCardNum.substring(6,14);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = simpleDateFormat.parse(bornDate);
			
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			calendar1.add(Calendar.YEAR, upperlimit);
			
			/**当前日期**/
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(simpleDateFormat.parse(simpleDateFormat.format(new Date())));
			
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(date);
			calendar3.add(Calendar.YEAR, lowLimit);
			
			logger.info("【用户出生日期】"+bornDate);			
			if(calendar1.before(calendar2) || calendar1.equals(calendar2)){
				resultMap.put("sucCount", -1+"");
				resultMap.put("code",LTResponseCode.US01124);
				resultMap.put("msg", openingAgeUpperLimitInfo.split("-")[1]);
				return resultMap;
			}
			if(calendar3.after(calendar2) || calendar3.equals(calendar2)){
				resultMap.put("sucCount", -1+"");
				resultMap.put("code",LTResponseCode.US01124);
				resultMap.put("msg", openingAgeLowLimitInfo.split("-")[1]);
				return resultMap;				
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException("检查用户身份证异常");
		}
			
		try{
			logger.info("-------------开始查询此用户历史成功开户次数-------------");
			int openingCount = 0;
			openingCount = this.userBussinessDao.getUserBaseCountByIdCardNum(idCardNum);
			logger.info("-------------查询此用户历史成功开户次数:"+openingCount+"-------------");
			resultMap.put("sucCount", openingCount+"");
			return resultMap;
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new LTException(LTResponseCode.US00001);
		}
	}

	@Override
	public Map<String, Object> getUserBaseInfoCountByIdCardNum(String idCardNum,String brandId) throws LTException {
		Map<String, Object> resultMap = FastMap.newInstance();
		if(StringTools.isEmpty(idCardNum)){
			logger.debug("idCardNum:"+idCardNum);
			throw new LTException(LTResponseCode.US01119);
		}
		try{
			/**检查用户年龄是否满20周岁**/
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String openingAgeUpperLimitInfo = sysCfgRedis.get("opening_age_upper_limit");
			String openingAgeLowLimitInfo = sysCfgRedis.get("opening_age_low_limit");

			int upperlimit = Integer.parseInt(openingAgeUpperLimitInfo.split("-")[0]);
			int lowLimit = Integer.parseInt(openingAgeLowLimitInfo.split("-")[0]);
			String bornDate = idCardNum.substring(6,14);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = simpleDateFormat.parse(bornDate);

			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			calendar1.add(Calendar.YEAR, upperlimit);

			/**当前日期**/
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(simpleDateFormat.parse(simpleDateFormat.format(new Date())));

			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(date);
			calendar3.add(Calendar.YEAR, lowLimit);

			logger.info("【用户出生日期】"+bornDate);
			if(calendar1.before(calendar2) || calendar1.equals(calendar2)){
				resultMap.put("sucCount", -1+"");
				resultMap.put("code",LTResponseCode.US01124);
				resultMap.put("msg", openingAgeUpperLimitInfo.split("-")[1]);
				return resultMap;
			}
			if(calendar3.after(calendar2) || calendar3.equals(calendar2)){
				resultMap.put("sucCount", -1+"");
				resultMap.put("code",LTResponseCode.US01124);
				resultMap.put("msg", openingAgeLowLimitInfo.split("-")[1]);
				return resultMap;
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new LTException("检查用户身份证异常");
		}

		try{
			logger.info("-------------开始查询此用户历史成功开户次数-------------");
			int openingCount = 0;
			openingCount = this.userBussinessDao.getUserBaseCountByIdCardNumAndBrandId(idCardNum,brandId);
			logger.info("-------------查询此用户历史成功开户次数:"+openingCount+"-------------");
			resultMap.put("sucCount", openingCount+"");
			return resultMap;
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new LTException(LTResponseCode.US00001);
		}
	}

	@Override
	public void userActivityAccountLog(UserUpdateInfoLog userUpdateInfoLog, String step) {
		try{
			userUpdateInfoLog.setUpdate_time(new Date());
			if("1".equals(step)){
				userUpdateInfoLog.setUpdate_type(UserContant.OPEN_STEP_ONE);
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}else if ("2".equals(step)) {
				userUpdateInfoLog.setUpdate_type(UserContant.OPEN_STEP_TWO);
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}else if ("3".equals(step)) {
				userUpdateInfoLog.setUpdate_type(UserContant.OPEN_STEP_THREE);
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}else if ("6".equals(step)) {
				//添加绑定银行卡次数
				userUpdateInfoLog.setUpdate_type(UserContant.BINDING_BANK_CARD);
				loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
			}			
		}catch(Exception e){
			logger.error("保存开户日志异常:"+e.getMessage());
			throw new LTException(LTResponseCode.US00004);
		}
		
	}

	@Override
	public void userActivityAccountStep3(UserUpdateInfoLog updateInfoLog, String riskRet,String riskLevel) throws LTException {
		String userId = updateInfoLog.getUserId();
		
		UserBussinessInfo bussinessInfo = new UserBussinessInfo();
		bussinessInfo.setUserId(userId);
		UserBussinessInfo oldInfo  = userBussinessDao.getUserActivityAccountInfo(bussinessInfo);
		bussinessInfo.setRiskRet(riskRet);
		bussinessInfo.setRiskLevel(riskLevel);
		int result = userBussinessDao.updateUserBussinessInfo(bussinessInfo);
		if(result > 0){
			UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_RISK, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),updateInfoLog.getRecordCarrierOperator(), updateInfoLog.getRecordAccessMode());
			userUpdateInfoLog.setContent("用户测评风险结果"+(StringTools.isEmpty(oldInfo.getRiskRet())?"":(oldInfo.getRiskRet()+"修改为"))+riskRet+"用户测评等级："+riskLevel);
			loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
		}else{
			throw new LTException(LTResponseCode.US00004);
		}
		
	}

	@Override
	public void userActivityAccountStep4(UserUpdateInfoLog updateInfoLog, String signPicPath) throws LTException {
		String userId = updateInfoLog.getUserId();
		
		UserBussinessInfo bussinessInfo = new UserBussinessInfo();
		bussinessInfo.setUserId(userId);
		UserBussinessInfo oldInfo  = userBussinessDao.getUserActivityAccountInfo(bussinessInfo);
		bussinessInfo.setSignPicPath(signPicPath);
		
		int result = userBussinessDao.updateUserBussinessInfo(bussinessInfo);
		if(result > 0){
			UserUpdateInfoLog userUpdateInfoLog = new UserUpdateInfoLog(updateInfoLog.getUserId(), UserContant.USER_UPDATE_SIGN, updateInfoLog.getUserId(), updateInfoLog.getDevice_model(), updateInfoLog.getIp(),updateInfoLog.getRecordCarrierOperator(), updateInfoLog.getRecordAccessMode());
			userUpdateInfoLog.setContent("用户签名"+(StringTools.isEmpty(oldInfo.getSignPicPath())?"":(oldInfo.getSignPicPath()+"修改为"))+signPicPath);
			loggerDao.saveUserUpdateInfoLog(userUpdateInfoLog);
		}else{
			throw new LTException(LTResponseCode.US00004);
		}
		
	}

	@Override
	public Map<String, Object> getUserFundInOutLimitInfo() throws LTException {
		Map<String, Object> resultMap = FastMap.newInstance();
		try {
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			resultMap.put("minRecharegeAmount", sysCfgRedis.get("min_recharege_amount"));
			resultMap.put("minWithdrawAmount", sysCfgRedis.get("min_withdraw_amount"));
			resultMap.put("maxWithdrawAmount", sysCfgRedis.get("max_withdraw_amount"));
			resultMap.put("maxWithdrawCount", sysCfgRedis.get("max_withdraw_count"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.ER400);
		}
		return resultMap;
	}

}
