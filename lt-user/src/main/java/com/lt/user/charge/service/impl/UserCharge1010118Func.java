package com.lt.user.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.model.user.charge.FundSwiftPassModel;
import com.lt.model.user.charge.FundSwiftPassResultModel;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.dao.sqldb.SwiftPassDao;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.swiftpass.config.SwiftpassConfig;
import com.lt.util.utils.swiftpass.util.SwiftPassMD5;
import com.lt.util.utils.swiftpass.util.SignUtils;
import com.lt.util.utils.swiftpass.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**   
* 项目名称：lt-user   
* 类名称：UserCharge1010108Func
* 类描述：  支付宝充值（聚合支付） 
* 创建人：yuanxin   
* 创建时间：2017年7月17日 下午3:30:11      
*/
@Service
public class UserCharge1010118Func extends UserRechargeSuper implements UserChargeFunc {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate; 
	
	@Autowired
	private SwiftPassDao swiftPassDao ;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return null;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode, String packet, BaseChargeBean baseCharge)
			throws LTException {
		SortedMap map = new TreeMap();
		FundSwiftPassModel model = (FundSwiftPassModel) baseCharge.getObject();
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		String json = sysCfgRedis.get("1010118");
		JSONObject obj = JSONObject.parseObject(json);
		String key = obj.getString("secretKey");
		String mchId = obj.getString("mchId");
		String notityUrl = obj.getString("notifyUrl");
		String reqUrl = obj.getString("reqUrl");
		String body = obj.getString("body");
//		String key = sysCfgRedis.get("1010118_secret_key");
//		String mchId = sysCfgRedis.get("1010118_mch_id");
//		String notityUrl = sysCfgRedis.get("1010118_notify_url");
//		String reqUrl = sysCfgRedis.get("1010118_req_url");

		String orderId = "swiftpass"+String.valueOf(CalendarTools.getMillis(new Date()));
		model.setOutTradeNo(orderId);
		baseCharge.setPayOrderId(orderId);
		model.setMchId(mchId);
		model.setNotifyUrl(notityUrl);
		//必要参数
		map.put("out_trade_no", model.getOutTradeNo());
		map.put("mch_create_ip", model.getMchCreateIp());
		map.put("total_fee", model.getTotalFee());
		map.put("service",model.getService());//支付宝 or 微信
		map.put("body", body);
		map.put("mch_id",model.getMchId());
		map.put("notify_url", model.getNotifyUrl());
		map.put("nonce_str", String.valueOf(new Date().getTime()));

		Map<String,String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SignUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString();
		String sign = SwiftPassMD5.sign(preStr, "&key=" + key, "utf-8");
		map.put("sign", sign);


		System.out.println("reqUrl：" + reqUrl);

		System.out.println("reqParams:" + XmlUtils.parseXML(map));

		Map<String,String> resultMap = HttpTools.doPostXML(reqUrl, map);


		Map<String,Object> rmap = new HashMap<>();
		String res = "success";
		if(StringTools.isNotEmpty(resultMap)){
			res = XmlUtils.toXml(resultMap);
			System.out.println("请求结果：" + res);
			if(resultMap.containsKey("sign")){
				if(!SignUtils.checkParam(resultMap, key)){
					res = "验证签名不通过";
					rmap.put("code", "500");
					rmap.put("msg", "处理失败：验证签名不通过");

				}else{

					if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
						//日志

						// 下一步的判断数据 及调用的链接数据
						rmap.put("codeImgUrl", resultMap.get("code_img_url"));
						rmap.put("codeUrl", resultMap.get("code_url"));
						rmap.put("code", "200");
						rmap.put("msg", "处理成功");
					}
				}
			}
		}
		//申请纪录保存
		swiftPassDao.insert(model);
		// 日志保存
		swiftPassDao.insertResult(paraObject(resultMap));


		return rmap;
	}


	private FundSwiftPassResultModel paraObject(Map<String,String> resultMap){
		FundSwiftPassResultModel model = new FundSwiftPassResultModel();
		model.setCharset(resultMap.get("charset"));
		model.setCodeImgUrl(resultMap.get("code_img_url"));
		model.setCodeUrl(resultMap.get("code_url"));
		model.setMchId(resultMap.get("mch_id"));
		model.setNonceStr(resultMap.get("nonce_str"));
		model.setResultCode(resultMap.get("result_code"));
		model.setSign(resultMap.get("sign"));
		model.setSignType(resultMap.get("sign_type"));
		model.setVersion(resultMap.get("version"));
		model.setStatus(resultMap.get("status"));
		model.setContent(JSONObject.toJSONString(resultMap));
		model.setBuyerLogonId(resultMap.get("buyer_logon_id"));
		model.setBankType(resultMap.get("bank_type"));
		model.setUserId(resultMap.get("user_id"));
		model.setTransactionId(resultMap.get("transaction_id"));
		model.setBuyerUserId(resultMap.get("buyer_user_id"));
		model.setErrCode(resultMap.get("err_code"));
		model.setErrMsg(resultMap.get("err_msg"));
		model.setFeeType(resultMap.get("fee_type"));
		model.setFundBillList(resultMap.get("fund_bill_list"));
		model.setGmtCreate(resultMap.get("gmt_create"));
		model.setMessage(resultMap.get("message"));
		model.setOpenid(resultMap.get("openid"));
		model.setOutTradeNo(resultMap.get("out_trade_no"));
		model.setOutTransactionId(resultMap.get("out_transaction_id"));
		model.setPayResult(resultMap.get("pay_result"));
		model.setTimeEnd(resultMap.get("time_end"));
		model.setTotalFee(resultMap.get("total_fee"));
		model.setTradeType(resultMap.get("trade_type"));
		return model;



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
				baseCharge.getChannelId(), baseCharge.getRate());
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
