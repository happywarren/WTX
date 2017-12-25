package com.lt.user.charge.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.model.user.charge.FundAggpayRecharge;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.dao.sqldb.UserRechargeAggpayDao;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.MapUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.MD5Util;

import javolution.util.FastMap;

/**
 * 项目名称：lt-user 类名称：UserRechargeByAggpay 类描述： 聚合支付 创建人：yubei 创建时间：2017年7月17日
 * 下午3:30:11
 */
@Service
public class UserRechargeByAggpay extends UserRechargeSuper implements UserChargeFunc {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private UserRechargeAggpayDao aggpayDao;

	private static final Logger logger = LoggerFactory.getLogger(UserRechargeByAggpay.class);

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet, BaseChargeBean baseCharge) throws LTException {

		Map<String, Object> param = FastMap.newInstance();

		// 组装数据
		BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		String config = rechargeConfig.get(baseCharge.getChannelId());
		logger.info("渠道配置:{}", config);
		JSONObject jsonObject = JSONObject.parseObject(config);

		FundAggpayRecharge aggpayRecharge = new FundAggpayRecharge();
		aggpayRecharge.setUserId(baseCharge.getUserId());
		aggpayRecharge.setBizCode(baseCharge.getChannelId());
		aggpayRecharge.setAmt(StringTools.getAmountToCent(baseCharge.getRmbAmt()));
		aggpayRecharge.setCreateStamp(new Date());
		aggpayRecharge.setTradeType("bis.pay.submit");
		aggpayRecharge.setMerchantId(jsonObject.getString("mchId"));
		aggpayRecharge.setPayType((String) baseCharge.getObject());
		aggpayRecharge.setOrderId(baseCharge.getPayOrderId());
		try {
			aggpayRecharge.setMachineIp(IpUtils.getLocalIP());
		} catch (Exception e) {
			aggpayRecharge.setMachineIp("127.0.0.1");
		}
		aggpayRecharge.setNotifyUrl(jsonObject.getString("notifyUrl"));
		aggpayRecharge.setReturnUrl(jsonObject.getString("returnUrl"));
		aggpayRecharge.setSubmitTime(DateTools.parseToTimeStamp2(new Date()));
		aggpayRecharge.setAmt(StringTools.getAmountToCent(baseCharge.getRmbAmt()));
		aggpayRecharge.setSubject("入金");
		aggpayRecharge.setCreditLimit("1");

		/** 封裝签名 **/
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("Amt", aggpayRecharge.getAmt());
		map.put("TradeType", aggpayRecharge.getTradeType());
		map.put("PayType", aggpayRecharge.getPayType());
		map.put("MerchantID", aggpayRecharge.getMerchantId());
		map.put("OrderID", aggpayRecharge.getOrderId());
		map.put("Subject", aggpayRecharge.getSubject());
		map.put("MachineIP", aggpayRecharge.getMachineIp());
		map.put("NotifyUrl", aggpayRecharge.getNotifyUrl());
		map.put("ReturnUrl", aggpayRecharge.getReturnUrl());
		map.put("SubmitTime", aggpayRecharge.getSubmitTime());
		map.put("CreditLimit", aggpayRecharge.getCreditLimit());
		String signData = MapUtils.convertUrlParam(map).concat("&Key=" + jsonObject.getString("secretKey"));
		String sign = MD5Util.md5(signData).toUpperCase();
		map.put("Sign", sign);

		// 下单
		try {
			String result = HttpTools.sendPost(jsonObject.getString("reqUrl"), map);
			if (StringTools.isEmpty(result)) {
				logger.info("聚合支付下单失败");
				throw new LTException(LTResponseCode.FU00000);
			}
			logger.info(URLEncoder.encode(result, "utf-8"));

			Map<String, Object> resultMap = FastMap.newInstance();
			String data[] = result.split("&");
			for (String s : data) {
				resultMap.put(s.split("=")[0], s.split("=")[1]);
			}
			
			String retStatus = (String) resultMap.get("RetStatus");
			String retCode = (String)resultMap.get("RetCode");
			if(StringTools.isAllEmpty(retStatus,retCode)){
				throw new LTException(LTResponseCode.FU00000);
			}

			if ("0".equals(retStatus) && "9999".equals(retCode)) {
				String codeUrl = (String) resultMap.get("CodeUrl");
				String codeImgUrl = (String) resultMap.get("CodeImgUrl");
				String payCode = (String) resultMap.get("PayCode");
				if (StringTools.isNotEmpty(codeUrl)) {
					param.put("codeUrl", codeUrl);
					aggpayRecharge.setCodeUrl(codeUrl);
				}
				if (StringTools.isNotEmpty(codeImgUrl)) {
					param.put("codeImgUrl", codeImgUrl);
					aggpayRecharge.setCodeImgUrl(codeImgUrl);
				}

				if (StringTools.isNotEmpty(payCode)) {
					param.put("PayCode", payCode);
					aggpayRecharge.setCodeUrl(payCode);
				}
				
				aggpayRecharge.setSign(sign);
				logger.info("保存聚合支付:{}", JSONObject.toJSONString(aggpayRecharge));
				aggpayDao.insertAggpayRecharge(aggpayRecharge);
				param.put("payType", aggpayRecharge.getPayType());
				param.put("code", "200");
				param.put("msg", "处理成功");
				return param;

			}
		} catch (Exception e) {
			throw new LTException(LTResponseCode.FU00000);
		}
		map.put("code", "400");
		map.put("msg", "处理失败");
		return param;
	}

	@Override
	public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		logger.info("登记聚合入金信息:{}", JSONObject.toJSONString(baseCharge));
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
