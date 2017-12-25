package com.lt.manager.service.impl.user;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.manager.bean.user.BankVo;
import com.lt.manager.bean.user.ChargeChannelVo;
import com.lt.manager.bean.user.RechargeGroupVO;
import com.lt.manager.bean.user.RechargeInfo;
import com.lt.manager.dao.user.ChargeChannelManageDao;
import com.lt.manager.service.user.IBankManageService;
import com.lt.manager.service.user.IChargeChannelManageService;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.util.error.LTException;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * 银行卡管理实现类
 * 
 * @author licy
 *
 */
@Service
public class ChargeChannelManageServiceImpl implements IChargeChannelManageService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ChargeChannelManageDao chargeChannelManageDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IBankManageService bankService;
	@Override
	public List<ChargeChannelVo> queryChargeChannelList() {
		try {
			return this.chargeChannelManageDao.selectChargeChannelList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【查询渠道信息异常】");
			throw new LTException("【查询渠道信息异常】");
		}

	}

	@Override
	@Transactional
	public void modifyChargeChannelPriority(ChargeChannelVo chargeChannelVo) {
		try {
			ChargeChannelVo oldChargeChannel = this.chargeChannelManageDao.selectChargeChannel(chargeChannelVo.getChannelId());
			int newPriority = chargeChannelVo.getPriority();
			int oldPriority = oldChargeChannel.getPriority();
			Map<String, Object> map = FastMap.newInstance();
			map.put("newPriority", newPriority);
			map.put("oldPriority", oldPriority);
			if (newPriority > oldPriority) {
				this.chargeChannelManageDao.updateChargeChannelPriorityDesc(map);
			} else if (newPriority < oldPriority) {
				this.chargeChannelManageDao.updateChargeChannelPriorityAsc(map);
			} else {
				throw new LTException("【更新渠道信息异常】");
			}
			this.chargeChannelManageDao.updateChargeChannelPriority(chargeChannelVo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【更新渠道信息异常】");
			throw new LTException("【更新渠道信息异常】");
		}

	}

	@Override
	public void modifyChargeChannel(ChargeChannelVo chargeChannelVo) {
		try {
			this.chargeChannelManageDao.updateChargeChannel(chargeChannelVo);
	        //BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
	        //sysCfgRedis.put(chargeChannelVo.getChannelId()+"_mch_id", chargeChannelVo.getMchId());
	        //sysCfgRedis.put(chargeChannelVo.getChannelId()+"_secret_key", chargeChannelVo.getSecretKey());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【更新渠道信息异常】");
			throw new LTException("【更新渠道信息异常】");
		}

	}

	@Override
	public List<ChargeChannelVo> queryChargeChannelPriorityList() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Object> getUserChargeChannelInfo(String userId) {
		try {
			List<ChargeChannelVo> supportChargeChannel = this.chargeChannelManageDao.selectUserChargeChannelList(userId);
			UserChannelTrans userChannelTrans = null;
			for (ChargeChannelVo channelVo : supportChargeChannel) {
				if(channelVo.getDailyLimitCount()==0){
					channelVo.setDailyLimitCount(-1);
				}else{
					userChannelTrans = new UserChannelTrans();
					userChannelTrans.setChannelId(channelVo.getChannelId());
					userChannelTrans.setUserId(userId);
					userChannelTrans.setCreateDate(DateTools.getDefaultDate());
					int dailyLimitCount = 0;
					dailyLimitCount = this.chargeChannelManageDao.selectUserChannelTransCount(userChannelTrans);
					channelVo.setDailyLimitCount(channelVo.getDailyLimitCount()-dailyLimitCount);
				}
			}
			List<ChargeChannelVo> unsupportChargeChannel = this.chargeChannelManageDao.selectUserUnsupportChargeChannelList(userId);
			Map<String, Object> resultMap = FastMap.newInstance();
			resultMap.put("supportChargeChannel", supportChargeChannel);
			resultMap.put("unsupportChargeChannel", unsupportChargeChannel);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【更新渠道信息异常】");
			throw new LTException("【更新渠道信息异常】");
		}
	}

	@Override
	@Transactional
	public Map<String, Object> modifyUserChargeChannelInfo(String userId, String supportChannels, String unsupportChannels) {
		try {

			if (StringTools.isNotEmpty(supportChannels)) {
				String[] channels = supportChannels.split(",");
				for (String channelId : channels) {
					Map<String, Object> map = FastMap.newInstance();
					map.put("userId", userId);
					map.put("channelId", channelId);
					ChargeChannelVo chargeChannelVo = this.chargeChannelManageDao.selectUserChargeChannel(map);
					if (chargeChannelVo == null) {
						this.chargeChannelManageDao.insertUserChargeMapper(map);
					}
				}
			}
			if (StringTools.isNotEmpty(unsupportChannels)) {
				String[] channels = unsupportChannels.split(",");
				for (String channelId : channels) {
					Map<String, Object> map = FastMap.newInstance();
					map.put("userId", userId);
					map.put("channelId", channelId);
					ChargeChannelVo chargeChannelVo = this.chargeChannelManageDao.selectUserChargeChannel(map);
					if (chargeChannelVo != null) {
						this.chargeChannelManageDao.deleteUserChargeMapper(map);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【修改用户渠道信息异常】");
		}
		return null;
	}

	@Override
	public List<RechargeGroupVO> queryRechargeGroupList() throws LTException{
		try{
			return this.chargeChannelManageDao.selectRechargeGroupList();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("【查询充值渠道组异常】");
			throw new LTException("【查询充值渠道组异常】");
		}
	}

	@Override
	@Transactional
	public void addChargeChannel(ChargeChannelVo chargeChannelVo) throws LTException {
		try{
			String maxChannelId = this.chargeChannelManageDao.selectMaxChannelId();
			String channelId = null;
			if(StringTools.isNotEmpty(maxChannelId)){
				channelId = Integer.valueOf(maxChannelId)+1+"";
			}else{
				channelId = "20000";
			}
			FundOptCode fundOptCode = new FundOptCode();
			fundOptCode.setFirstOptCode(FundCashOptCodeEnum.RECHARGE.getFirstLevelCode());
			fundOptCode.setFirstOptName(FundCashOptCodeEnum.RECHARGE.getFirstLevelname());
			fundOptCode.setSecondOptCode(FundCashOptCodeEnum.RECHARGE.getCode());
			fundOptCode.setSecondOptName(FundCashOptCodeEnum.RECHARGE.getName());
			fundOptCode.setThirdOptCode(channelId);
			fundOptCode.setThirdOptName(chargeChannelVo.getChannelName());
			fundOptCode.setFlowType(1);
			fundOptCode.setFundType(0);
			fundOptCode.setRemark(chargeChannelVo.getRemark());
			chargeChannelVo.setChannelId(channelId);
			this.chargeChannelManageDao.insertFundOptcode(fundOptCode);
			this.chargeChannelManageDao.insertChargeChannel(chargeChannelVo);
			List<BankVo> bankVos = this.bankService.queryBankInfoList();
			List<BankChargeMapper> bankChargeMappers = FastList.newInstance();
			for(BankVo bankVo:bankVos){
				bankChargeMappers.add(new BankChargeMapper(bankVo.getBankCode(), channelId, 0.0, 0.0));
			}
			this.chargeChannelManageDao.insertBankChargeMapperList(bankChargeMappers);
	        //系统配置
	        //BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
	        //sysCfgRedis.put(channelId+"_mch_id", chargeChannelVo.getMchId());
	        //sysCfgRedis.put(channelId+"_secret_key", chargeChannelVo.getSecretKey());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("【增加充值渠道异常】");
			throw new LTException("【增加充值渠道异常】");
		}
	}

	@Override
	@Transactional
	public void deleteChargeChannelInfo(String channelId) throws LTException{
		try {
			this.chargeChannelManageDao.delteBankChargeMapperByChannelId(channelId);
			this.chargeChannelManageDao.delteFundOptCodeByThirdOptCode(channelId);
			this.chargeChannelManageDao.deleteChargeChannelInfoByChannelId(channelId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[删除渠道信息异常]"+e.getMessage());
		}
	}

	@Override
	public void refreshConfig() throws LTException {
		try{
			List<RechargeInfo> rechargeInfos = this.chargeChannelManageDao.selectRechargeInfoList();
			BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
			for(RechargeInfo rechargeInfo : rechargeInfos){
				Map<String, Object> map = FastMap.newInstance();
				map.put("mchId", rechargeInfo.getMchId());
				map.put("secretKey", rechargeInfo.getSecretKey());
				map.put("publicKey", rechargeInfo.getPublicKey());
				map.put("reqUrl", rechargeInfo.getReqUrl());
				map.put("notifyUrl", rechargeInfo.getNotifyUrl());
				JSONObject object = null;
				if(StringTools.isNotEmpty(rechargeInfo.getReserve())){
					 object = JSONObject.parseObject(rechargeInfo.getReserve());
				}else{
					object = new JSONObject();
				}
				object.putAll(map);
				rechargeConfig.put(rechargeInfo.getChannelId(), object.toJSONString());
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("刷新支付渠道配置异常！"+e.getMessage());
			throw new LTException("刷新支付渠道配置异常！");
		}
		
	}

}
