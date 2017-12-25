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

/**
 * 支付宝转账
 * 
 * @author yubei
 * @date 2017年10月13日 上午11:00
 *
 */
@Service
public class UserRechargeByAlipayTransfer extends UserRechargeSuper implements UserChargeFunc {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final Logger logger = LoggerFactory.getLogger(UserRechargeByAlipayTransfer.class);

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet, BaseChargeBean baseCharge)
			throws LTException {
		Map<String, Object> map = new HashMap<String, Object>();
		BoundHashOperations<String, String, String> rechargeConfig = redisTemplate
				.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		String config = rechargeConfig.get(baseCharge.getChannelId());
		logger.info("渠道配置:{}", config);
		JSONObject jsonObject = JSONObject.parseObject(config);

		map.put("reqUrl", jsonObject.getString("mchId"));
		map.put("code", LTResponseCode.SUCCESS);
		map.put("msg", "处理成功");

		return map;
	}

	@Override
	public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		logger.info("baseCharge:{}", JSONObject.toJSONString(baseCharge));
		super.getFundAccountServiceImpl().insertAlipayRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(),
				baseCharge.getAmt(), baseCharge.getRmbAmt(), baseCharge.getBankCardNum(), baseCharge.getObject() + "",
				baseCharge.getBankCode(), baseCharge.getChannelId(), baseCharge.getRate());
		return true;
	}

	@Override
	public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl)
			throws LTException {
		logger.info("map:{}", JSONObject.toJSONString(map));
		super.setChargeFunc(this);
		super.setFundAccountServiceImpl(fundAccountServiceImpl);
		Map<String, Object> returnMap = super.excute(map);
		return returnMap;
	}

}
