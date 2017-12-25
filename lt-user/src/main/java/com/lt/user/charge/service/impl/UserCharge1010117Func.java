package com.lt.user.charge.service.impl;

import java.util.HashMap;
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
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.pay.merchant.MerchantClient;
import com.pay.vo.CertPayH5Request;

@Service
public class UserCharge1010117Func extends UserRechargeSuper implements UserChargeFunc{

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge)
			throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet,
			BaseChargeBean baseCharge) throws LTException {
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("code", LTResponseCode.SUCCESS);
		resultMap.put("msg", "充值提交成功");

		/**获取参数**/
		BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		JSONObject sysCfgRedis = JSONObject.parseObject(rechargeConfig.get(baseCharge.getChannelId()));
		// 商户号
		String merchantId = sysCfgRedis.getString("mchId");
		// 支付地址
		String qiantong_pay_url = sysCfgRedis.getString("reqUrl");
		// 版本
		String qiantong_version = sysCfgRedis.getString("qiantong_version");
		// 支付平台H5
		String qiantong_application = sysCfgRedis.getString("qiantong_application");	
		//后台回调地址
		String qiantong_merchantPayNotifyUrl = sysCfgRedis.getString("notifyUrl");	
		//前端回调地址
		String qiantong_merchantFrontEndUrl = sysCfgRedis.getString("qiantong_merchantFrontEndUrl");	
		//产品描述
		String qiantong_merchantOrderDesc = sysCfgRedis.getString("qiantong_merchantOrderDesc");	
		//支付金额
		String amount = StringTools.getAmountToCent(baseCharge.getRmbAmt().toString());
		//支付id
		String payOrderId = baseCharge.getPayOrderId();
		//用户ID
		String userId = baseCharge.getUserId();
		
		CertPayH5Request certPayRequest = new CertPayH5Request();
		//封装bean
		certPayRequest.setApplication(qiantong_application);
		certPayRequest.setVersion(qiantong_version);
		certPayRequest.setMerchantId(merchantId);
		certPayRequest.setMerchantOrderId(payOrderId);		
		certPayRequest.setMerchantOrderAmt(amount); 
		certPayRequest.setMerchantOrderDesc(qiantong_merchantOrderDesc);
		certPayRequest.setPayerId(userId);
		certPayRequest.setMerchantPayNotifyUrl(qiantong_merchantPayNotifyUrl);
		certPayRequest.setMerchantFrontEndUrl(qiantong_merchantFrontEndUrl);
		certPayRequest.setMerchantName("");
		certPayRequest.setPayerId("");
		certPayRequest.setGuaranteeAmt("0");
		try {
			String msg = new MerchantClient().sendCertPayH5Request(certPayRequest);
			logger.info("=======钱通支付参数：certPayRequest={},msg={}===",JSONObject.toJSONString(certPayRequest),msg);
			resultMap.put("qiantong_merchantOrderDesc", qiantong_merchantOrderDesc);
			resultMap.put("qiantong_pay_url", qiantong_pay_url);
			resultMap.put("amount", amount);
			resultMap.put("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}

	@Override
	public String requestUrl(String packet, BaseChargeBean baseCharge)
			throws LTException {
		return null;
	}

	@Override
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		logger.info("baseCharge:{}",JSONObject.toJSONString(baseCharge));
		super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(),
				baseCharge.getRmbAmt(), baseCharge.getBankCardNum(),baseCharge.getBankCode(),
				FundThirdOptCodeEnum.QIANTONGPAY.getThirdLevelCode(), baseCharge.getRate());
		return true;
	}

	@Override
	public Map<String, Object> excute(Map<String, Object> map,
			IFundAccountApiService fundAccountServiceImpl) throws LTException {
		logger.info("map:{}",JSONObject.toJSONString(map));
		super.setChargeFunc(this);
		super.setFundAccountServiceImpl(fundAccountServiceImpl);
		Map<String, Object> returnMap =  super.excute(map);
		return returnMap ;
	}

}
