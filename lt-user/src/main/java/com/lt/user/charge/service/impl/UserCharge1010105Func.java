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
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.model.user.charge.DinpayExtraInfo;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.bean.RechargeByDinpay;
import com.lt.user.charge.dao.sqldb.DinpayExtraInfoDao;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;

import javolution.util.FastMap;

/**   
* 项目名称：lt-user   
* 类名称：UserCharge1010101Func   
* 类描述：支付宝充值   
* 创建人：yubei   
* 创建时间：2017年6月29日 下午5:43:37      
*/
@Service
public class UserCharge1010105Func extends UserRechargeSuper implements UserChargeFunc {
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(UserCharge1010105Func.class);	
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private DinpayExtraInfoDao dinpayExtraInfoDao;
	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return "";
	}

	@Override
	public Map<String, Object> returnParam(String urlCode,String packet,BaseChargeBean baseCharge) throws LTException {
		try{
			RechargeByDinpay dinpay = (RechargeByDinpay) baseCharge.getObject();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", LTResponseCode.SUCCESS);
			map.put("msg", "智付处理参数成功");
			
			/**获取参数**/
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String dinpayServiceType = sysCfgRedis.get("dinpay_service_type");
			String dinpayInterfaceVersion = sysCfgRedis.get("dinpay_interface_version");
			String dinpaySignType = sysCfgRedis.get("dinpay_sign_type");
			String dinpayPrivateKey = sysCfgRedis.get("dinpay_private_key");
			String dinpayPfxKey = sysCfgRedis.get("dinpay_pfx_key");
			String dinpayPfxPath = sysCfgRedis.get("dinpayPfxPath");
			String dinpayMerchantCode = sysCfgRedis.get("dinpay_merchant_code");
			String dinpay_callback_url = sysCfgRedis.get("dinpay_callback_url");
			String dinpay_res_url = sysCfgRedis.get("dinpay_res_url");
			String dinpay_req_url = sysCfgRedis.get("dinpay_req_url");

			String orderNo = baseCharge.getPayOrderId();
			logger.info("【智付入金】生成平台订单号:"+orderNo);
			StringBuffer signSrc = new StringBuffer();
			String orderTime = DateTools.parseToDefaultDateTimeString(new Date());
			String orderAmount = baseCharge.getRmbAmt().toString();
			String product_name = "智付入金";
			String return_url = dinpay_res_url;
			String redo_flag = "";
			String product_code = "";
			String product_num = "";
			String product_desc = "";
			String pay_type = "";
			String show_url = "";
			String customer_name = "";
			String customer_idNumber = "";
			String clientType = dinpay.getClientType()+"";
			
			/***处理签名**/
			//移动端
			if ("1".equals(clientType) || "2".equals(clientType)) {
				signSrc.append("interface_version=").append(dinpayInterfaceVersion);
				signSrc.append("&merchant_code=").append(dinpayMerchantCode);
				signSrc.append("&notify_url=").append(dinpay_callback_url);
				signSrc.append("&order_amount=").append(orderAmount);
				signSrc.append("&order_no=").append(orderNo);
				signSrc.append("&order_time=").append(orderTime);
				signSrc.append("&product_name=").append(product_name);
			}else if ("3".equals(clientType)) {
				//web端
				signSrc.append("input_charset=").append("UTF-8");
				signSrc.append("&interface_version=").append(dinpayInterfaceVersion);
				signSrc.append("&merchant_code=").append(dinpayMerchantCode);
				signSrc.append("&notify_url=").append(dinpay_callback_url);
				signSrc.append("&order_amount=").append(orderAmount);
				signSrc.append("&order_no=").append(orderNo);
				signSrc.append("&order_time=").append(orderTime);
				if (StringTools.isNotEmpty(pay_type)) {
					signSrc.append("&pay_type=").append(pay_type);
				}
				if (StringTools.isNotEmpty(product_code)) {
					signSrc.append("&product_code=").append(product_code);
				}
				if (StringTools.isNotEmpty(product_desc)) {
					signSrc.append("&product_desc=").append(product_desc);
				}
				signSrc.append("&product_name=").append(product_name);
				if (StringTools.isNotEmpty(product_num)) {
					signSrc.append("&product_num=").append(product_num);
				}
				if (StringTools.isNotEmpty(redo_flag)) {
					signSrc.append("&redo_flag=").append(redo_flag);
				}
				if (StringTools.isNotEmpty(return_url)) {
					signSrc.append("&return_url=").append(return_url);
				}
				signSrc.append("&service_type=").append("direct_pay");
				if (StringTools.isNotEmpty(show_url)) {
					signSrc.append("&show_url=").append(show_url);
				}			
			}

			String signInfo = signSrc.toString();
			String sign = "";

			if ("RSA-S".equals(dinpaySignType)) {
				try {
					sign = RSAWithSoftware.signByPrivateKey(signInfo, dinpayPrivateKey);
				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.info("RSA-S商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
				logger.info("RSA-S商家发送的签名：" + sign.length() + " -->" + sign + "\n");
			}
			if ("RSA".equals(dinpaySignType)) {
				String pfxPass = dinpayPfxKey;
				RSAWithHardware mh = new RSAWithHardware();
				try {
					mh.initSigner(dinpayPfxPath, pfxPass);
				} catch (Exception e) {
					e.printStackTrace();
					throw new LTException("智付签名异常");
				}
				sign = mh.signByPriKey(signInfo);
				logger.info("RSA商家发送的签名字符串：" + signInfo.length() + " -->" + signInfo);
				logger.info("RSA商家发送的签名：" + sign.length() + " -->" + sign + "\n");
			}
			if (StringTools.isEmpty(dinpaySignType)) {
				logger.error("【智付支付登记异常!未查询到签名类型配置信息】");
				throw new LTException();
			}
			if (StringTools.isEmpty(sign)) {
				logger.error("【智付支付登记异常!参数加签失败】");
				throw new LTException();
			}

			Map<String, Object> resultMap = FastMap.newInstance();
			resultMap.put("merchant_code", dinpayMerchantCode);
			if("3".equals(clientType)){
				resultMap.put("input_charset", "UTF-8");
				resultMap.put("pay_type", pay_type);
				resultMap.put("return_url", dinpay_res_url);			
				resultMap.put("service_type", dinpayServiceType);
				resultMap.put("bank_code", "");
				resultMap.put("show_url", show_url);
				resultMap.put("dinpay_req_url", dinpay_req_url);
				resultMap.put("client_ip", "");
				resultMap.put("extend_param", "");			
			}
			resultMap.put("interface_version", dinpayInterfaceVersion);
			resultMap.put("notify_url",dinpay_callback_url);
			resultMap.put("sign_type", dinpaySignType);
			resultMap.put("order_no", orderNo);
			resultMap.put("order_time", orderTime);
			resultMap.put("order_amount",orderAmount);
			resultMap.put("product_name", product_name);
			resultMap.put("signInfo", signInfo);
			if("1".equals(clientType)){//安卓手机需要处理
				resultMap.put("sign", sign.replaceAll("\\+", "%2B"));
			}else{
				resultMap.put("sign", sign);
			}
			resultMap.put("redo_flag", "");
			resultMap.put("product_code", "");
			resultMap.put("product_num", "");
			resultMap.put("product_desc", "");
			resultMap.put("extra_return_param", "");
			
			resultMap.put("customer_name", customer_name);
			resultMap.put("customer_idNumber", customer_idNumber);
			return resultMap;					
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("【智付入金处理签名异常】");
			throw new LTException(e.getMessage());
		}

	}

	@Override
	public String requestUrl(String packet,BaseChargeBean baseCharge) throws LTException {
		return "";
	}

	@Override
	@Transactional
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		try{
			super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(),
					baseCharge.getRmbAmt(), baseCharge.getBankCardNum(), baseCharge.getBankCode(),
					FundThirdOptCodeEnum.DINPAY_MOB.getThirdLevelCode(), baseCharge.getRate());
			
			return true ;
		}catch(LTException e){
			e.printStackTrace();
			logger.error("【智付入金保存数据异常】");
			throw new LTException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl)
			throws LTException {
		try{
			super.setChargeFunc(this);
			super.setFundAccountServiceImpl(fundAccountServiceImpl);
			BaseChargeBean baseChargeBean = super.paramToObject(map);
			Map<String, Object> resultMap = this.returnParam(null, null, baseChargeBean );
			this.insertDataBase(baseChargeBean);
			DinpayExtraInfo dinpayExtraInfo = new DinpayExtraInfo(baseChargeBean.getPayOrderId(),new Date(),new Date(),baseChargeBean.getRmbAmt(),(String) JSONObject.toJSONString(resultMap),"登记成功");
			this.dinpayExtraInfoDao.insert(dinpayExtraInfo);
			resultMap.put("code", LTResponseCode.SUCCESS);
			resultMap.put("msg", "成功");
			return resultMap;
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("【智付入金异常】");
			throw new LTException(e.getMessage());
		}
	}
	
}
