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
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.pay.merchant.MerchantClient;
import com.pay.vo.CertPayRequest;
import com.pay.vo.CertPayResponse;

/**
 *  钱通接口支付
 * @author jingwb
 *
 */
@Service
public class UserRechargeByQianTongPay extends UserRechargeSuper implements UserChargeFunc {

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
		//String qiantong_application = sysCfgRedis.getString("qiantong_application");	
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
		//手机号
		String tele = baseCharge.getTele();
		//银行卡号
		String bankCardNum = baseCharge.getBankCardNum();
		//用户名
		String userName = baseCharge.getUserName();
		//身份证号
		String idCardNum = baseCharge.getIdCardNum();
		String credentialType = "01";//证件类型
		
		CertPayRequest certPayRequest = new CertPayRequest();
		//封装bean
		certPayRequest.setApplication("CertPayOrder");
		certPayRequest.setVersion(qiantong_version);
		certPayRequest.setMerchantId(merchantId);
		certPayRequest.setMerchantOrderId(payOrderId);
		certPayRequest.setMerchantOrderAmt(amount);
		certPayRequest.setMerchantOrderDesc(qiantong_merchantOrderDesc);
		certPayRequest.setUserMobileNo(tele);
		certPayRequest.setPayerId(userId);
		certPayRequest.setUserName(userName);
		certPayRequest.setSalerId("");
		certPayRequest.setGuaranteeAmt("0");
		certPayRequest.setCredentialType(credentialType);
		certPayRequest.setCredentialNo(idCardNum);
		certPayRequest.setMerchantPayNotifyUrl(qiantong_merchantPayNotifyUrl);
		certPayRequest.setMerchantFrontEndUrl(qiantong_merchantFrontEndUrl);
		certPayRequest.setCardNo(bankCardNum);
		certPayRequest.setCvv2("");
		certPayRequest.setValidPeriod("");
		logger.info("=======钱通接口支付请求参数certPayRequest:{}==================",JSONObject.toJSONString(certPayRequest));
		try {
			MerchantClient merchantClient = new MerchantClient();
			merchantClient.setPayURL(qiantong_pay_url);//支付地址
			CertPayResponse certPayResponse = merchantClient.sendCertPayRequest(certPayRequest);
			logger.info("=========钱通接口支付第一次返回数据:{}===========",JSONObject.toJSONString(certPayResponse));
			if(!"000".equals(certPayResponse.getRespCode())){
			
				resultMap.put("code", LTResponseCode.ER400);
				resultMap.put("msg", certPayResponse.getRespDesc());
			}else{//成功
				resultMap.put("payOrderId", payOrderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", LTResponseCode.ER400);
			resultMap.put("msg", "请求异常");
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
				baseCharge.getChannelId(), baseCharge.getRate());
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
