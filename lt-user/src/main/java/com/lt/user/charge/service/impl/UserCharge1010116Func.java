package com.lt.user.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.bean.RechargeByDaddyPay;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**   
* 项目名称：lt-user   
* 类名称：UserCharge1010113Func
* 类描述：  爸爸付充值
* 创建人：lvx
* 创建时间：2017年8月28日
*/
@Deprecated
@Service
public class UserCharge1010116Func extends UserRechargeSuper implements UserChargeFunc {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet, BaseChargeBean baseCharge)
			throws LTException {
		RechargeByDaddyPay daddyPay = (RechargeByDaddyPay)baseCharge.getObject();
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("code", LTResponseCode.SUCCESS);
		resultMap.put("msg", "daddyPay充值提交成功");

/*		*//**获取参数**//*
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		// 商户id
		String company_id = sysCfgRedis.get("daddypay_company_id");
		// 提现接口地址
		String charge_url = sysCfgRedis.get("daddypay_charge_url");
		// 私钥
		String private_key = sysCfgRedis.get("daddypay_key");
		// 默认充值渠道
		String defDepMode = sysCfgRedis.get("daddypay_default_deposit_mode");*/

		/**获取参数**/
		BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		JSONObject sysCfgRedis = JSONObject.parseObject(rechargeConfig.get(baseCharge.getChannelId()));
		// 商户号
		String company_id = sysCfgRedis.getString("mchId");
		// 充值地址
		String charge_url = sysCfgRedis.getString("reqUrl");
		// 私钥
		String private_key = sysCfgRedis.getString("secretKey");
		// 默认充值渠道
		String defDepMode = sysCfgRedis.getString("daddypay_default_deposit_mode");

		if (StringTools.isEmpty(company_id) || StringTools.isEmpty(charge_url)
				|| StringTools.isEmpty(private_key) || StringTools.isEmpty(defDepMode)) {
			throw new LTException(LTResponseCode.FU00003);
		}

		String depositMode = StringTools.formatStr(daddyPay.getPayWay(),defDepMode);
		String amount = DoubleTools.decimalFormat(baseCharge.getRmbAmt(), "#.00");
		// 生成签名
		String signSrc = MD5Util.md5Low(private_key) + company_id + baseCharge.getBankCode() + amount
				+ baseCharge.getPayOrderId() + "leagueTrade" + baseCharge.getBankCode() + depositMode + "0" + "https://test2-admin.meiguwang.cn" +"2";
		String key = MD5Util.md5Low(signSrc);

		Map<String, String> param = new HashMap<>();
		// 参数组装
		param.put("company_id", company_id);
		param.put("bank_id", baseCharge.getBankCode());
		param.put("company_order_num", baseCharge.getPayOrderId());
		param.put("amount", amount);
		param.put("company_user", "leagueTrade");
		param.put("estimated_payment_bank",baseCharge.getBankCode());
		param.put("deposit_mode", depositMode);
		param.put("group_id", "0");
		param.put("note_model", "2");
		param.put("terminal", "2");
		param.put("web_url", "https://test2-admin.meiguwang.cn");
		param.put("key", key);

		logger.info("daddypay充值提交入参：{}", param);
		JSONObject json = HttpTools.doPost(charge_url, param);
		if(json == null){
			throw new LTException(LTResponseCode.FUY00015);
		}
		String strResult = json.toString();
		logger.info("daddypay充值提交返回json：{}", strResult);
		// 解析返回结果
		Map rmap = JSONObject.parseObject(strResult, Map.class);
		param.clear();
		if ((Integer)rmap.get("status") == 1) {// 充值请求成功
			resultMap.put("returnUrl", rmap.get("break_url"));
			resultMap.put("mownecum_order_num", rmap.get("mownecum_order_num"));
			resultMap.put("company_order_num", rmap.get("company_order_num"));
			resultMap.put("amount", rmap.get("amount"));
		}
		else{
			resultMap.put("code", "400");
			resultMap.put("msg", rmap.get("error_msg"));
		}
		return resultMap;
	}


	@Override
	public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		logger.info("baseCharge:{}",JSONObject.toJSONString(baseCharge));
		super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(),
				baseCharge.getRmbAmt(), baseCharge.getBankCardNum(),baseCharge.getBankCode(),
				FundThirdOptCodeEnum.DADDYPAY.getThirdLevelCode(), baseCharge.getRate());
		return true;
	}

	@Override
	public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl)
			throws LTException {
		logger.info("map:{}",JSONObject.toJSONString(map));
		super.setChargeFunc(this);
		super.setFundAccountServiceImpl(fundAccountServiceImpl);
		Map<String, Object> returnMap =  super.excute(map);
		return returnMap ;
	}

}
