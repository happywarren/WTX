package com.lt.user.charge.service.impl;

import java.util.Date;
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
import com.lt.model.user.charge.FundAlipayRecharge;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.dao.sqldb.UserCharge1010101FDao;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.MD5Util;

/**   
* 项目名称：lt-user   
* 类名称：UserCharge1010101FFunc   
* 类描述：  支付宝充值（聚合支付） 
* 创建人：yuanxin   
* 创建时间：2017年7月17日 下午3:30:11      
*/
@Service
public class UserCharge1010101FFunc extends UserRechargeSuper implements UserChargeFunc {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate; 
	
	@Autowired
	private UserCharge1010101FDao userchargeDao ;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet, BaseChargeBean baseCharge)
			throws LTException {
		StringBuilder signStr = new StringBuilder();
		Map<String,Object> map = new HashMap<String,Object>();
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		
		String orderId = "ali"+String.valueOf(CalendarTools.getMillis(new Date()));
		signStr.append("merchantOutOrderNo=").append(orderId).append("&");
		map.put("merchantOutOrderNo", orderId);
		
		String merid = sysCfgRedis.get("jh_alipay_merid");
		signStr.append("merid="+merid).append("&");
		map.put("merid", merid);
		
		String noceStr = StringTools.getUUID().replaceAll("-", "");
		signStr.append("noncestr="+noceStr.substring(0, 16)).append("&");
		map.put("noncestr", noceStr.substring(0, 16));
		
		String notifyUrl = sysCfgRedis.get("jh_alipay_notifyUrl");
		signStr.append("notifyUrl="+notifyUrl).append("&");
		map.put("notifyUrl", notifyUrl);
		
		
		String money = baseCharge.getRmbAmt() == null ? "0.0" : baseCharge.getRmbAmt().toString();
		signStr.append("orderMoney="+money).append("&");
		map.put("orderMoney", money);
		
		String orderTime = DateTools.formatDate(new  Date(), DateTools.TIME_STAMP_2);
		signStr.append("orderTime="+orderTime).append("&");
		map.put("orderTime", orderTime);
		
		String key = sysCfgRedis.get("jh_alipay_key");
		signStr.append("key=").append(key);
		
		String sign = MD5Util.md5(signStr.toString());
		map.put("sign", sign.toLowerCase());
		signStr.append("&").append("sign=").append(sign.toLowerCase());
		
		// 下一步的判断数据 及调用的链接数据
		map.put("action", sysCfgRedis.get("jh_alipay_action"));
		map.put("code", "200");
		map.put("msg", "处理成功");
		baseCharge.setPayOrderId(orderId);
		
		FundAlipayRecharge alipayRecharge = new FundAlipayRecharge(orderId, baseCharge.getUserId(),
				baseCharge.getRmbAmt(), merid, signStr.toString());
		alipayRecharge.setBizCode(FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode());
		logger.info("alipayRecharge:{}",JSONObject.toJSONString(alipayRecharge));
		userchargeDao.insertAlipayApply(alipayRecharge);
		return map;
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
				FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode(), baseCharge.getRate());
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
