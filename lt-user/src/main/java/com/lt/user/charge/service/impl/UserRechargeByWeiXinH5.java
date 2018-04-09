package com.lt.user.charge.service.impl;

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
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.crypt.MD5Util;
import com.lt.util.utils.iapppay.IapppayUtil;
import com.lt.util.utils.wxpay.WXPay;
import com.lt.util.utils.wxpay.WXPayConfigImpl;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 项目名称：lt-user 类名称：UserRehargeByIapppay 类描述： 爱贝充值 创建人：yubei
 */
@Service
public class UserRechargeByWeiXinH5 extends UserRechargeSuper implements UserChargeFunc {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;


	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet, BaseChargeBean baseCharge) throws LTException {
		BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		String config = rechargeConfig.get(baseCharge.getChannelId());
		JSONObject jsonObject = JSONObject.parseObject(config);
		String mchId = jsonObject.getString("mchId");
		String secretKey = jsonObject.getString("secretKey");
		String publicKey = jsonObject.getString("publicKey");
		String waresid = jsonObject.getString("waresid");
		String waresname = jsonObject.getString("waresname");
		String cporderid = baseCharge.getPayOrderId();
		String notifyurl = jsonObject.getString("notifyUrl");
		String appuserid = jsonObject.getString("");
		String url_r = jsonObject.getString("url_r");
		String reqUrl = jsonObject.getString("reqUrl");
		String orderUrl = jsonObject.getString("orderUrl");

		logger.info("notifyurl={}",notifyurl);

		WXPayConfigImpl wxConfig = null;
		WXPay wxpay = null;

		try {
			wxConfig = WXPayConfigImpl.getInstance();
			wxpay  = new WXPay(wxConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}


		Map<String,String> param = new HashMap<String,String>();
		param.put("body","dxjc");
		param.put("out_trade_no",baseCharge.getPayOrderId());
		param.put("fee_type","CNY");
		param.put("total_fee", ((int)DoubleUtils.mul(baseCharge.getRmbAmt(),100))+"");// 消费金额
		param.put("spbill_create_ip",baseCharge.getIp());
		param.put("notify_url",notifyurl);
		param.put("trade_type","MWEB");
		param.put("notify_url",notifyurl);
		param.put("nonce_str",baseCharge.getPayOrderId());
		String sceneinfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_name\": \"dxjc\",\"wap_url\": \"www.jingzh.top\"}}";
		param.put("scene_info",sceneinfo);
		try {

			Map<String, String> r = wxpay.unifiedOrder(param);
			logger.info("返回微信支付:"+r.toString());
			if(r != null  && "SUCCESS".equals(r.get("result_code"))){
				Map<String, Object> map = FastMap.newInstance();
				map.put("code", "200");
				map.put("msg", "处理成功");
				map.put("reqUrl", r.get("mweb_url"));
				return map;
			}else{
				Map<String, Object> map = FastMap.newInstance();
				map.put("code", "400");
				map.put("msg", "处理失败");
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = FastMap.newInstance();
		map.put("code", "400");
		map.put("msg", "处理失败");
		return map;

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

	// 随机字符串生成
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	//创建md5 数字签证
	public static String createSign(SortedMap<Object, Object> parame,String apiKey){
		StringBuffer buffer = new StringBuffer();
		Set set = parame.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String)entry.getKey();
			Object value = (String)entry.getValue();
			if(null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)){
				buffer.append(key+"="+value+"&");
			}
		}
		buffer.append("key="+apiKey);
		String sign = MD5Util.md5(buffer.toString()).toUpperCase();
		System.out.println("签名参数："+sign);
		return sign;
	}

	//拼接xml 请求路径
	public static String getRequestXML(SortedMap<Object, Object> parame){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<xml>");
		Set set = parame.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			//过滤相关字段sign
			if("sign".equalsIgnoreCase(key)){
				buffer.append("<"+key+">"+"<![CDATA["+value+"]]>"+"</"+key+">");
			}else{
				buffer.append("<"+key+">"+value+"</"+key+">");
			}
		}
		buffer.append("</xml>");
		return buffer.toString();
	}

}
