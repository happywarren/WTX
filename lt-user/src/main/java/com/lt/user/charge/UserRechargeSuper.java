package com.lt.user.charge;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.RechargeGroupEnum;
import com.lt.model.user.charge.FundSwiftPassModel;
import com.lt.model.user.charge.XDPayRecharge;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.bean.RechargeByDaddyPay;
import com.lt.user.charge.bean.RechargeByDinpay;
import com.lt.user.charge.bean.RechargeByKQ;
import com.lt.user.charge.bean.RechargeByKQDynamic;
import com.lt.user.charge.bean.RechargeByZFBM;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.StringTools;

/**
 * 项目名称：lt-user 类名称：UserRechargeSuper 类描述： 用户充值处理类父类 创建人：yuanxin 创建时间：2017年6月29日
 * 下午3:16:10
 */
public class UserRechargeSuper {

	private static final Logger logger = LoggerFactory.getLogger(UserChargeFunc.class);

	private IFundAccountApiService fundAccountServiceImpl;

	private UserChargeFunc chargeFunc;

	public UserRechargeSuper(IFundAccountApiService fundAccountServiceImpl, UserChargeFunc chargeFunc) {
		this.fundAccountServiceImpl = fundAccountServiceImpl;
		this.chargeFunc = chargeFunc;
	}

	public UserRechargeSuper() {

	}

	/**
	 * 默认处理流程
	 * 
	 * @param paraMap
	 * @return
	 * @return: Map<String,Object>
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年6月30日 下午6:11:24
	 */
	public Map<String, Object> excute(Map<String, Object> paraMap) {

		BaseChargeBean chargeBean = paramToObject(paraMap);

		String enclosePacket = chargeFunc.encapsulatePacket(chargeBean);

		String urlCode = chargeFunc.requestUrl(enclosePacket, chargeBean);

		try {
			Map<String, Object> map = chargeFunc.returnParam(urlCode, enclosePacket, chargeBean);

			String code = (String) map.get("code") == null ? "400" : (String) map.get("code");

			// 每个封装参数返回码需为200 才执行
			if (code.equals("200")) {
				logger.info("=====chargeBean={}=============", JSONObject.toJSONString(chargeBean));
				chargeFunc.insertDataBase(chargeBean);
			}
			//map.put("groupId", paraMap.get("groupId"));
			return map;
		} catch (Exception e) {
			throw new LTException(e.getMessage());
		}

	}

	/***
	 * 入参转换方法
	 * 
	 * @param paraMap
	 * @return
	 * @return: Object
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年6月30日 下午6:10:47
	 */
	public BaseChargeBean paramToObject(Map<String, Object> paraMap) {
		String thirdOptCode = StringTools.formatStr(paraMap.get("thirdOptCode"), null);
		String userId = StringTools.formatStr(paraMap.get("userId"), null);
		String bankCardId = StringTools.formatStr(paraMap.get("bankCardId"), null);
		String bankCode = StringTools.formatStr(paraMap.get("bankCode"), null);
		String bankCardNum = StringTools.formatStr(paraMap.get("bankCardNum"), null);
		Double amt = Double.valueOf(StringTools.formatStr(paraMap.get("amt"), "0.00"));
		Double rmbAmt = Double.valueOf(StringTools.formatStr(paraMap.get("rmbAmt"), "0.00"));
		Double rate = Double.valueOf(StringTools.formatStr(paraMap.get("rate"), "0.00"));
		String groupId = StringTools.formatStr((String) paraMap.get("groupId"), "");
		String tele = StringTools.formatStr((String)paraMap.get("tele"), "");
		String userTele = StringTools.formatStr((String)paraMap.get("userTele"), "");//用户自己填写的手机号
		String idCardNum = StringTools.formatStr((String)paraMap.get("idCardNum"), "");
		String userName = StringTools.formatStr((String)paraMap.get("userName"), "");
		BaseChargeBean chargeBean = null;

		/***
		 * 旧的支付代码
		 */

		/** 支付宝充值 （手工） */
		if (thirdOptCode.equals(FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode() + "M")) {
			String zfbNum = paraMap.get("zfbNum").toString();
			RechargeByZFBM byZFBM = new RechargeByZFBM(zfbNum);
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, byZFBM);
		}

		/** 支付宝充值(恒通宝) */
		if (thirdOptCode.equals(FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode() + "F")) {
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, null);
		}

		if (thirdOptCode.equals(FundThirdOptCodeEnum.YSBCZ.getThirdLevelCode())) {

		}

		/** 快钱充值 （获取验证码） */
		if (thirdOptCode.equals(FundThirdOptCodeEnum.KQCZ.getThirdLevelCode() + "D")) {
			if (!StringTools.isTele(tele)) {
				throw new LTException(LTResponseCode.US01102);
			}
			RechargeByKQDynamic byKQDynamic = new RechargeByKQDynamic(tele);
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, byKQDynamic);
		}

		/** 快钱充值（提交订单） */
		if (thirdOptCode.equals(FundThirdOptCodeEnum.KQCZ.getThirdLevelCode())) {
			String validCode = paraMap.get("validCode").toString();
			String code = paraMap.get("code").toString();
			if (StringUtils.isEmpty(validCode)) {
				throw new LTException(LTResponseCode.US01106);
			}

			if (!StringTools.isTele(tele)) {
				throw new LTException(LTResponseCode.US01102);
			}

			if (StringUtils.isEmpty(code)) {
				throw new LTException(LTResponseCode.FU00003);
			}

			RechargeByKQ byKQ = new RechargeByKQ(validCode, tele);
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, byKQ);
			chargeBean.setPayOrderId(code);
		}

		if (thirdOptCode.equals(FundThirdOptCodeEnum.DINPAY_MOB.getThirdLevelCode())) {
			RechargeByDinpay dinpay = new RechargeByDinpay();

			if (StringTools.isNotEmpty(paraMap.get("clientType"))) {
				int clientType = (int) paraMap.get("clientType");
				dinpay.setClientType(clientType);
			}
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, dinpay);
		}
		/**
		 * 熙大支付宝
		 */
		if (thirdOptCode.equals(FundThirdOptCodeEnum.XDPAY.getThirdLevelCode())) {
			XDPayRecharge recharge = new XDPayRecharge();
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, recharge);
		}

		if (thirdOptCode.equals(FundThirdOptCodeEnum.DINPAY_WEB.getThirdLevelCode())) {
			RechargeByDinpay dinpay = new RechargeByDinpay();
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, dinpay);
		}

		/**
		 * daddyPay充值
		 */
/*		if (thirdOptCode.equals(FundThirdOptCodeEnum.DADDYPAY.getThirdLevelCode())) {
			RechargeByDaddyPay daddyPay = new RechargeByDaddyPay();
			daddyPay.setPayWay((String) paraMap.get("payWay"));
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, daddyPay);
		}*/

		/**
		 * 钱通充值
		 */
/*		if (thirdOptCode.equals(FundThirdOptCodeEnum.QIANTONGPAY.getThirdLevelCode())) {
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, null);
			chargeBean.setChannelId(thirdOptCode);
			chargeBean.setTele(userTele);
			chargeBean.setUserName(userName);
			chargeBean.setIdCardNum(idCardNum);
		}*/

		/**
		 * 威富通充值 1010118
		 */
/*		if (thirdOptCode.equals("1010118")) {
//		if (groupId.equals("SwiftPass")) {
			FundSwiftPassModel model = new FundSwiftPassModel();
			model.setBody("用户入金");
			String ip = StringTools.formatStr(paraMap.get("ip"), "0");
			model.setMchCreateIp(ip);
			model.setUserId(userId);
			double totalFee = StringTools.formatDouble(paraMap.get("rmbAmt").toString(), 0.0d);
			model.setTotalFee((int) (totalFee * 100) + "");
			Integer type = StringTools.formatInt(paraMap.get("type"), 0);

			if (type == 1) {
				model.setService("pay.alipay.native");
			} else {
				model.setService("pay.weixin.native");
			}
			// TODO 组装model
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, model,thirdOptCode);
		}*/

		/***
		 * 新的支付代码
		 */

		/**
		 * 爱贝充值
		 */
		if ("Iapppay".equals(groupId)) {
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, null, thirdOptCode);
			chargeBean.setPayOrderId("iapppay" + String.valueOf(CalendarTools.getMillis(new Date())));
		}
		/**
		 * 兴联支付宝支付宝
		 */
		if (RechargeGroupEnum.ALIPAY.getGroupId().equals(groupId)) {
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, null, thirdOptCode);
			chargeBean.setPayOrderId("ali" + String.valueOf(CalendarTools.getMillis(new Date())));
		}

		/**
		 * 钱通
		 */
		if (RechargeGroupEnum.QIANTONPAY.getGroupId().equals(groupId)) {
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, null,thirdOptCode);
			chargeBean.setTele(userTele);
			chargeBean.setUserName(userName);
			chargeBean.setIdCardNum(idCardNum);			
			chargeBean.setPayOrderId("qiantong" + String.valueOf(CalendarTools.getMillis(new Date())));
		}

		/**
		 * 威富通
		 */
		if (RechargeGroupEnum.SWIFTPASS.getGroupId().equals(groupId)) {
			FundSwiftPassModel model = new FundSwiftPassModel();
			model.setBody("用户入金");
			String ip = StringTools.formatStr(paraMap.get("ip"), "0");
			model.setMchCreateIp(ip);
			model.setUserId(userId);
			double totalFee = StringTools.formatDouble(paraMap.get("rmbAmt").toString(), 0.0d);
			model.setTotalFee((int) (totalFee * 100) + "");
			Integer type = StringTools.formatInt(paraMap.get("type"), 0);
			if (type == 1) {
				model.setService("pay.alipay.native");
			} else {
				model.setService("pay.weixin.native");
			}
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, model,thirdOptCode);
			chargeBean.setPayOrderId("swiftpass" + String.valueOf(CalendarTools.getMillis(new Date())));
		}
		/**
		 * daddyPay充值
		 */
		if (RechargeGroupEnum.DADDYPAY.getGroupId().equals(groupId) ){
			RechargeByDaddyPay daddyPay = new RechargeByDaddyPay();
			daddyPay.setPayWay((String) paraMap.get("payWay"));
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, daddyPay,thirdOptCode);
			chargeBean.setPayOrderId("daddypay" + String.valueOf(CalendarTools.getMillis(new Date())));
		}
		/**
		 * 聚合支付
		 */
		if(RechargeGroupEnum.AGGPAY.getGroupId().equals(groupId)){
			String payType = (String) paraMap.get("payType");
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, null, thirdOptCode);
			chargeBean.setPayOrderId("aggpay" + String.valueOf(CalendarTools.getMillis(new Date())));
			if(StringTools.isNotEmpty(payType)){
				chargeBean.setObject(payType);
			}
		}
		/**
		 * 支付宝转账
		 */
		if(RechargeGroupEnum.ALIPAYTRANSFER.getGroupId().equals(groupId)){
			String alipayNum = (String) paraMap.getOrDefault("alipay_num","");
			String transferNum = (String)paraMap.getOrDefault("transferNum", "");
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, transferNum, bankCode, alipayNum, thirdOptCode);
			chargeBean.setPayOrderId("alipay" + String.valueOf(CalendarTools.getMillis(new Date())));
		}

		if(RechargeGroupEnum.ALIPAYH5.getGroupId().equals(groupId)){
			chargeBean = new BaseChargeBean(userId, bankCardId, amt, rmbAmt, rate, bankCardNum, bankCode, null, thirdOptCode);
			chargeBean.setPayOrderId("alipayh5" + String.valueOf(CalendarTools.getMillis(new Date())));
		}
		logger.info("groupId:{}", groupId);
		return chargeBean;

	}

	public IFundAccountApiService getFundAccountServiceImpl() {
		return fundAccountServiceImpl;
	}

	public void setFundAccountServiceImpl(IFundAccountApiService fundAccountServiceImpl) {
		this.fundAccountServiceImpl = fundAccountServiceImpl;
	}

	/**
	 * @return the chargeFunc
	 */
	public UserChargeFunc getChargeFunc() {
		return chargeFunc;
	}

	/**
	 * @param chargeFunc
	 *            the chargeFunc to set
	 */
	public void setChargeFunc(UserChargeFunc chargeFunc) {
		this.chargeFunc = chargeFunc;
	}

}
