package com.lt.user.core.service.impl;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.charge.IUserApiAutoRechargeService;
import com.lt.enums.fund.FundIoRechargeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.RechargeGroupEnum;
import com.lt.enums.user.ChargeEnum;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundPayAuthFlow;
import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.ChargeChannelInfo;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.user.charge.service.UserRecharegeService;
import com.lt.user.core.service.IUserAutoRechageService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;

/**   
* 项目名称：lt-user   
* 类名称：UserAutoRechargeServiceImpl   
* 类描述：系统自动选择充值渠道     
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午3:41:45      
*/
@Service
public class UserApiAutoRechargeServiceImpl implements IUserApiAutoRechargeService {
	
	@Autowired
	private IUserAutoRechageService userAutoRechargeServiceImpl;
	
	@Autowired
	private UserRecharegeService recharegeService;
	
	@Autowired
	private IFundAccountApiService fundAccountServiceImpl;
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Map<String, Object> qryBankMaxRechargeAmount(String bankCardId, String userId, String bankCode) throws LTException {
		
		// 查询用户关联的通道号
		List<ChargeChannelInfo> chargeChannelList = userAutoRechargeServiceImpl.qryUserChargeChannel(userId);
		
		if(CollectionUtils.isEmpty(chargeChannelList)){
			throw new LTException(LTResponseCode.USY0004);
		}
		
		List<String> channelList = new ArrayList<String>();
		for(ChargeChannelInfo chargeChannelInfo : chargeChannelList){
			channelList.add(chargeChannelInfo.getChannelId());
		}
		
		//获取银行关联渠道下单日可入金额 和 单笔可入金额
		List<BankChargeMapper> bankChargeList = userAutoRechargeServiceImpl.qryBankChargeInfo(bankCode, channelList);
		// 获取银行卡对应渠道下总的充值金额
		Map<String,Double> channelTotalAmt = userAutoRechargeServiceImpl.qryBankIdChannelDailyAmt(bankCardId, userId, channelList);
		// 获取当日用户剩余可充值金额
		Double restAmt = fundAccountServiceImpl.getDailyUserRechargeTotalAmt(userId);
		restAmt = restAmt == null ? 0.0 : restAmt ;
		
		if(restAmt == 0){
			throw new LTException(LTResponseCode.USY0005);
		}
		
		TreeSet<Double> amtSet = new TreeSet<Double>();
		
		if(CollectionUtils.isEmpty(bankChargeList)){
			throw new LTException(LTResponseCode.USY0001);
		}
		

		for(BankChargeMapper bankChargeMapper : bankChargeList){
			double totalAmt = channelTotalAmt.containsKey(bankChargeMapper.getChannelId()) ? channelTotalAmt.get(bankChargeMapper.getChannelId()) : 0.0;
			
			//用户单日剩余可充值金额比较 银行卡的单日入金限额 - 已充值金额
			if(restAmt > bankChargeMapper.getDailyLimit() - totalAmt){
				// 银行卡的单日入金限额 - 已充值金额  比较 单笔入金限额
				if(bankChargeMapper.getDailyLimit() - totalAmt > bankChargeMapper.getSingleLimit()){
					amtSet.add(bankChargeMapper.getSingleLimit());
				}else{
					amtSet.add(bankChargeMapper.getDailyLimit() - totalAmt);
				}
			}else{
				//用户单日剩余可充值金额比较 单笔入金限额
				if(restAmt > bankChargeMapper.getSingleLimit()){
					amtSet.add(bankChargeMapper.getSingleLimit());
				}else{
					amtSet.add(restAmt);
				}
			}
		}
		
		if(amtSet.isEmpty()){
			// 查询通道为空，则返回空提示
			throw new LTException(LTResponseCode.USY0003);
		}
		
		Map<String,Object> returnType = new HashMap<String,Object>();
		returnType.put("type", ChargeEnum.SUCCESS.getValue());
		returnType.put("msg", ChargeEnum.SUCCESS.getRemark());
		returnType.put("amt", amtSet.last() < 0 ? 0.0 : amtSet.last());
			
		return returnType;
	}

	@Override
	public Map<String, Object> qryChargeChannel(String bankCard, String userId, String bankCode, Double amount) throws LTException {
		// TODO Auto-generated method stub
		// 查询用户关联的通道号
		List<ChargeChannelInfo> chargeChannelList = userAutoRechargeServiceImpl.qryUserChargeChannel(userId);
		Map<String,Integer> channelWeight = new HashMap<String,Integer>();

		if (CollectionUtils.isEmpty(chargeChannelList)) {
			// 查询通道为空，则返回空提示
			throw new LTException(LTResponseCode.USY0001);
		} 
		
		List<String> channelList = new ArrayList<String>();
		UserChannelTrans userChannelTrans = null;
		for (ChargeChannelInfo chargeChannelInfo : chargeChannelList) {
			if(chargeChannelInfo.getDailyLimitCount()!=0){
				userChannelTrans = new UserChannelTrans();
				userChannelTrans.setUserId(userId);
				userChannelTrans.setChannelId(chargeChannelInfo.getChannelId());
				userChannelTrans.setCreateDate(DateTools.getDefaultDate());
				//查询用户当日渠道使用次数
				if (chargeChannelInfo.getDailyLimitCount()-this.userAutoRechargeServiceImpl.getUserChannelTransCount(userChannelTrans)<1) {
					continue;
				}
			}
			channelList.add(chargeChannelInfo.getChannelId());
			channelWeight.put(chargeChannelInfo.getChannelId(), chargeChannelInfo.getWeight());
		}
		if(CollectionUtils.isEmpty(channelList)){
			throw new LTException(LTResponseCode.USY0006);
		}
		// 获取银行关联渠道下单日可入金额 和 单笔可入金额
		List<BankChargeMapper> bankChargeList = userAutoRechargeServiceImpl.qryBankChargeInfo(bankCode,channelList);
		// 获取银行卡对应渠道下总的充值金额
		Map<String, Double> channelTotalAmt = userAutoRechargeServiceImpl.qryBankIdChannelDailyAmt(bankCard,userId, channelList);
		// 获取当日用户剩余可充值金额
		Double restAmt = fundAccountServiceImpl.getDailyUserRechargeTotalAmt(userId);
		restAmt = restAmt == null ? 0.0 : restAmt;
		
		if(amount > restAmt){
			throw new LTException(LTResponseCode.FUY00009);
		}
		
		if (CollectionUtils.isEmpty(bankChargeList)) {
			// 查询通道为空，则返回空提示
			throw new LTException(LTResponseCode.USY0002);
		}
		
		Set<String> usedChannelList = new HashSet<String>();

		for (BankChargeMapper bankChargeMapper : bankChargeList) {
			double totalAmt = channelTotalAmt.containsKey(bankChargeMapper.getChannelId()) ? channelTotalAmt.get(bankChargeMapper.getChannelId()) : 0.0;
		    // 判断是否大于大笔充值金额
			if(amount > bankChargeMapper.getSingleLimit()){
				continue ;
			}

			//判断是否大于每日限额
			if(amount + totalAmt > bankChargeMapper.getDailyLimit()){
				continue ;
			}
			
			usedChannelList.add(bankChargeMapper.getChannelId());
		}

		if(CollectionUtils.isEmpty(usedChannelList)){
			throw new LTException(LTResponseCode.USY0001);
		}

		//计算权重总和
		int totalWeight = 0;
		for(String channel : usedChannelList){
			Integer weight = channelWeight.get(channel);
			if(weight != null){
				totalWeight += weight;
			}
		}

		//权重分配
		Random rand = new Random();
		int randWeight = rand.nextInt(totalWeight)+1;
		Map<String,Object> usedChannel = new HashMap<String,Object>();
		for(String channel : usedChannelList){
			int curWeight = channelWeight.get(channel)!=null?channelWeight.get(channel):0;
			randWeight = randWeight - curWeight;
			if(randWeight <= 0){
				usedChannel.put("channel", channel);
				//查询快钱的充值手机号码
				if(FundThirdOptCodeEnum.KQCZ.getThirdLevelCode().equals(channel)){
					if(StringTools.isNotEmpty(bankCard) && bankCard.trim().length()>=5){
						String storablePan = bankCard.substring(0, 6)+bankCard.substring(bankCard.length()-4, bankCard.length());
						FundPayAuthFlow fundPayAuthFlow = new FundPayAuthFlow();
						fundPayAuthFlow.setCustomerId(userId);
						fundPayAuthFlow.setStorablePan(storablePan);

						List<FundPayAuthFlow> fundPayAuthFlows = fundAccountServiceImpl.queryFundPayAuthFlowList(fundPayAuthFlow);
						if(fundPayAuthFlows!=null && fundPayAuthFlows.size()>0){
							String tel = fundPayAuthFlows.get(0).getTel();
							if(StringTools.isNotEmpty(tel)){
								usedChannel.put("tel", tel);
							}
						}
					}
				}

/*				//查询支付宝首次充值的账号
				if(FundThirdOptCodeEnum.HANDAIPAY.getThirdLevelCode().equals(channel) && StringTools.isNotEmpty(bankCard)){
					FundIoCashRecharge fundIoCashRecharge = new FundIoCashRecharge();
					fundIoCashRecharge.setThirdOptcode(FundThirdOptCodeEnum.HANDAIPAY.getThirdLevelCode());
					fundIoCashRecharge.setStatus(FundIoRechargeEnum.SUCCESS.getValue());
					fundIoCashRecharge.setTransferNumber(bankCard);
					fundIoCashRecharge.setUserId(userId);
					List<FundIoCashRecharge> fundIoCashRecharges = this.fundAccountServiceImpl.queryFundIoCashRechargeList(fundIoCashRecharge);
					if(fundIoCashRecharges!=null && fundIoCashRecharges.size()>0){
						String alipayAccount = fundIoCashRecharges.get(0).getAlipayNum();
						if(StringTools.isNotEmpty(alipayAccount)){
							usedChannel.put("alipayAccount", alipayAccount);
						}
					}
				}*/
				break;
			}
		}
		
		//查询支付宝首次充值的账号
		if(StringTools.isNotEmpty(usedChannel.get("channel"))){
			String groupId = this.recharegeService.queryGroupIdByChannelId(usedChannel.get("channel").toString());
			usedChannel.put("groupId", groupId);
			//查询支付宝首次充值的账号
			if(RechargeGroupEnum.ALIPAYTRANSFER.getGroupId().equals(groupId) && StringTools.isNotEmpty(bankCard)){
				FundIoCashRecharge fundIoCashRecharge = new FundIoCashRecharge();
				fundIoCashRecharge.setThirdOptcode(usedChannel.get("channel").toString());
				fundIoCashRecharge.setStatus(FundIoRechargeEnum.SUCCESS.getValue());
				fundIoCashRecharge.setTransferNumber(bankCard);
				fundIoCashRecharge.setUserId(userId);
				List<FundIoCashRecharge> fundIoCashRecharges = this.fundAccountServiceImpl.queryFundIoCashRechargeList(fundIoCashRecharge);
				if(fundIoCashRecharges!=null && fundIoCashRecharges.size()>0){
					String alipayAccount = fundIoCashRecharges.get(0).getAlipayNum();
					if(StringTools.isNotEmpty(alipayAccount)){
						usedChannel.put("alipayAccount", alipayAccount);
					}
				}
			}

		}
		usedChannel.put("bankChargeList",bankChargeList);
		return usedChannel;
	}

	@Override
	public List<UserchargeBankDetailInfo> qryBankChargeAmountList(String userId) throws LTException {
		List<UserchargeBankDetailInfo> chargeBankList = userAutoRechargeServiceImpl.qryUserChargeBankList(userId);
		
		if(CollectionUtils.isEmpty(chargeBankList)){
			throw new LTException(LTResponseCode.USY0003);
		}
		
		for(UserchargeBankDetailInfo userchargeBankDetailInfo : chargeBankList ){
			try{
				Map<String,Object> map = qryBankMaxRechargeAmount(userchargeBankDetailInfo.getBankCardNum()
						, userId, userchargeBankDetailInfo.getBankCode());
				
				if(map == null ){
					continue ;
				}else{
					Double amt = Double.valueOf(map.get("amt").toString());
					userchargeBankDetailInfo.setChargeAmt(amt);
				}
			}catch (LTException e) {
				// TODO: handle exception
				e.printStackTrace();
				logger.info("本次渠道处理异常：{}",JSONObject.toJSONString(userchargeBankDetailInfo));
				userchargeBankDetailInfo.setChargeAmt(0.0);
			}
		}
		
		return chargeBankList;
	}



}
