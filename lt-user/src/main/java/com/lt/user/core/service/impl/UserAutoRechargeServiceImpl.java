package com.lt.user.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.remoting.ExecutionException;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.model.user.charge.BankChargeDetail;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.ChargeChannelInfo;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.model.user.charge.UserChargeMapper;
import com.lt.user.core.dao.sqldb.IUserBussinessDao;
import com.lt.user.core.dao.sqldb.IUserRechargeDao;
import com.lt.user.core.service.IUserAutoRechageService;
import com.lt.util.error.LTException;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;


/**   
* 项目名称：lt-user   
* 类名称：UserAutoRechargeServiceImpl   
* 类描述：系统自动选择充值渠道    
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午4:09:06      
*/
@Service
public class UserAutoRechargeServiceImpl implements IUserAutoRechageService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IUserRechargeDao userchargeDao ;
	
	@Autowired
	private IUserBussinessDao userBussinessDao;

	@Override
	public List<ChargeChannelInfo> qryUserChargeChannel(String userId)  throws LTException {
		List<ChargeChannelInfo> channelList = userchargeDao.getUserChargeMapperChannel(userId);
		if(CollectionUtils.isEmpty(channelList))
			return new ArrayList<ChargeChannelInfo>();
		else{
			return channelList ;
		}
	}

	@Override
	public List<BankChargeMapper> qryBankChargeInfo(String bankCode, List<String> channelIds)  throws LTException{
		List<BankChargeMapper> bankChargeList = userchargeDao.getUserBankChargeChannel(bankCode, channelIds);
		if(CollectionUtils.isEmpty(bankChargeList))
			return new ArrayList<BankChargeMapper>();
		else{
			return bankChargeList ;
		}
	}

	@Override
	public Map<String, Double> qryBankIdChannelDailyAmt(String bankCardId,String userId, List<String> channelIds)  throws LTException{
		String today = DateTools.formatDate(new Date(), DateTools.YMD_TIME_STAMP);
		List<BankChargeDetail> bankChargeDetailList = userchargeDao.getBankChargeDetail(userId, bankCardId, today, channelIds);
		if(CollectionUtils.isNotEmpty(bankChargeDetailList)){
			Map<String,Double> amtMap = new HashMap<String,Double>();
			for(BankChargeDetail bankChargeDetail : bankChargeDetailList){
				if(bankChargeDetail.getChannelId() != null && bankChargeDetail.getAmount() != null ){
					amtMap.put(bankChargeDetail.getChannelId(), bankChargeDetail.getAmount());
				}
			}
			
			return amtMap ;
		}else{
			return new HashMap<String,Double>();
		}
	}

	@Override
	public UserchargeBankDetailInfo qryLastUserChargeBankInfo(String userId) throws LTException {
		return userchargeDao.getUserChargeBankInfo(userId);
	}

	@Override
	public List<UserchargeBankDetailInfo> qryUserChargeBankList(String userId) throws LTException {
		return userchargeDao.getUserChargeBankList(userId);
	}

	@Override
	public void initUserChargeMapper(String userId,String regSource) throws LTException {
		try{
			if(StringTools.isEmpty(regSource)){
				List<ChargeChannelInfo> chargeChannelList = userchargeDao.getDefaultChargeChannelList(1);
				if(CollectionUtils.isNotEmpty(chargeChannelList)){
					List<UserChargeMapper> chargeList = new ArrayList<UserChargeMapper>();
					
					for(ChargeChannelInfo chargeChannelInfo : chargeChannelList){
						UserChargeMapper chargeMapper = new UserChargeMapper(userId, chargeChannelInfo.getChannelId());
						chargeList.add(chargeMapper);
					}
					
					userchargeDao.insertUserChargeMapperMutil(chargeList);
				}			
			}else{
				List<String> channelIds = this.userchargeDao.selectUserChannelIds(regSource);
				logger.info("---渠道列表---"+channelIds.toString());
				if(CollectionUtils.isNotEmpty(channelIds)){
					List<UserChargeMapper> chargeList = new ArrayList<UserChargeMapper>();
					
					for(String channelId : channelIds){
						UserChargeMapper chargeMapper = new UserChargeMapper(userId, channelId);
						chargeList.add(chargeMapper);
					}
					
					userchargeDao.insertUserChargeMapperMutil(chargeList);
				}			
			}
			
		}catch(Exception e){
			logger.error("用户注册初始化支付渠道异常");
			e.printStackTrace();
		}
	}

	@Override
	public boolean isExitBankCard(String userId, String bankCode, String bankCardNum) throws LTException {
		UserBankInfo userBankInfo = new UserBankInfo();
		userBankInfo.setBankCardNum(bankCardNum);
		userBankInfo.setBankCode(bankCode);
		userBankInfo.setUserId(userId);
		List<UserBankInfo> list = userBussinessDao.getUserBankInfo(userBankInfo);
		if(CollectionUtils.isEmpty(list)){
			return false ;
		}else{
			return true ;
		}
	}

	@Override
	public Integer getUserChannelTransCount(UserChannelTrans userChannelTrans) throws LTException {
		try{
			return this.userchargeDao.selectUserChannelTransCount(userChannelTrans);
		}catch(Exception exception){
			exception.printStackTrace();
			logger.error("【查询当日用户渠道次数异常】");
			//return 0;
			throw new LTException("查询当日用户渠道次数异常");
		}
	}

}
