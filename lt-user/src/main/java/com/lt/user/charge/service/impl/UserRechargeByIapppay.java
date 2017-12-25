package com.lt.user.charge.service.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.model.user.charge.FundIapppayRecharge;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.dao.sqldb.UserRechargeIapppayDao;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.iapppay.IapppayUtil;

import javolution.util.FastMap;

/**
 * 项目名称：lt-user 类名称：UserRehargeByIapppay 类描述： 爱贝充值 创建人：yubei
 */
@Service
public class UserRechargeByIapppay extends UserRechargeSuper implements UserChargeFunc {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private UserRechargeIapppayDao iapppayDao;
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet, BaseChargeBean baseCharge) throws LTException {
		try {
			BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
			String config = rechargeConfig.get(baseCharge.getChannelId());
			logger.info("渠道配置:{}",config);

			JSONObject jsonObject = JSONObject.parseObject(config);
			String mchId = jsonObject.getString("mchId");
			String secretKey = jsonObject.getString("secretKey");
			String publicKey = jsonObject.getString("publicKey");
			String waresid = jsonObject.getString("waresid");
			String waresname = jsonObject.getString("waresname");
			String cporderid = baseCharge.getPayOrderId();
			String notifyurl = jsonObject.getString("notifyUrl");
			String appuserid = jsonObject.getString("appuserid");
			String url_r = jsonObject.getString("url_r");
			String reqUrl = jsonObject.getString("reqUrl");
			String orderUrl = jsonObject.getString("orderUrl");

			String cpprivateinfo = jsonObject.getString("cpprivateinfo");

			IapppayUtil iapppayUtil = IapppayUtil.getInstance();
			logger.info("实例化爱贝对象工具");
			if(iapppayUtil==null){
				iapppayUtil = new IapppayUtil();
			}
			iapppayUtil.setAppid(mchId);
			iapppayUtil.setWaresid(Integer.valueOf(waresid));
			iapppayUtil.setWaresname(waresname);
			iapppayUtil.setCporderid(cporderid);
			iapppayUtil.setPrice(baseCharge.getRmbAmt());
			iapppayUtil.setAppuserid(appuserid);
			iapppayUtil.setCpprivateinfo(cpprivateinfo);
			iapppayUtil.setNotifyurl(notifyurl);
			iapppayUtil.setSecretKey(secretKey);
			iapppayUtil.setPublicKey(publicKey);
			
			//iapppayUtil.init(mchId, Integer.valueOf(waresid), waresname, cporderid, baseCharge.getRmbAmt(), appuserid, cpprivateinfo, notifyurl, secretKey, publicKey);
			iapppayUtil.setUrl_r(url_r);
			iapppayUtil.setReqUrl(reqUrl);
			iapppayUtil.setorderUrl(orderUrl);
			
			logger.info("开始执行爱贝服务");
			Map<String, Object> result = iapppayUtil.execute();
			String resultId = (String) result.get("resultId");
			if (LTResponseCode.SUCCESS.equals(resultId)) {
				Map<String, Object> map = FastMap.newInstance();
				map.put("code", "200");
				map.put("msg", "处理成功");
				map.put("reqUrl", result.get("reqUrl"));
				FundIapppayRecharge iapppayRecharge = new FundIapppayRecharge();
				iapppayRecharge.setAppid(mchId);
				iapppayRecharge.setNotifyurl(notifyurl);
				iapppayRecharge.setBizCode(baseCharge.getChannelId());
				iapppayRecharge.setCporderid(cporderid);
				iapppayRecharge.setUserId(baseCharge.getUserId());
				iapppayRecharge.setCreateStamp(new Date());
				iapppayRecharge.setPrice(baseCharge.getRmbAmt());
				iapppayDao.insertFundIapppayRecharge(iapppayRecharge);
				return map;
			} else {
				Map<String, Object> map = FastMap.newInstance();
				map.put("code", "400");
				map.put("msg", "处理失败");
				return map;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("入金异常");
		}

	}

	@Override
	public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		logger.info("baseCharge:{}", JSONObject.toJSONString(baseCharge));
		super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(), baseCharge.getRmbAmt(), baseCharge.getBankCardNum(), baseCharge.getBankCode(), baseCharge.getChannelId(),
				baseCharge.getRate());
		return true;
	}

	@Override
	public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl) throws LTException {
		logger.info("map:{}", JSONObject.toJSONString(map));
		super.setChargeFunc(this);
		super.setFundAccountServiceImpl(fundAccountServiceImpl);
		Map<String, Object> returnMap = super.excute(map);
		return returnMap;
	}

}
