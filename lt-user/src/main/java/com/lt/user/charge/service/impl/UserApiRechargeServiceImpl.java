package com.lt.user.charge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.charge.IUserApiRechargeService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.RechargeGroupEnum;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.BankInfo;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.charge.FundAggpayRecharge;
import com.lt.model.user.charge.FundAlipayRecharge;
import com.lt.model.user.charge.FundIapppayRecharge;
import com.lt.model.user.charge.XDPayRecharge;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.user.charge.service.UserRecharegeService;
import com.lt.user.core.dao.sqldb.IUserBussinessDao;
import com.lt.user.core.service.IUserBankInfoService;
import com.lt.user.core.service.impl.UserApiAutoRechargeServiceImpl;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.MapUtils;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.MD5Util;
import com.lt.util.utils.iapppay.sign.SignHelper;
import com.pay.merchant.MerchantClient;
import com.pay.vo.CertPayConfirmRequest;
import com.pay.vo.CertPayConfirmResponse;
import com.pay.vo.DeductInfo;
import com.pay.vo.PaymentNotifyResponse;

/**
 * 项目名称：lt-user 类名称：UserApiRechargeServiceImpl 类描述：用户充值处理接口 创建人：yuanxin
 * 创建时间：2017年6月29日 下午3:10:58
 */
@Component
public class UserApiRechargeServiceImpl implements IUserApiRechargeService {

	@Autowired
	private IFundAccountApiService fundAccountServiceImpl;

	@Autowired
	private IUserBankInfoService userBankInfoServiceImpl;

	@Autowired
	private UserRecharegeService userRechargeServiceImpl;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private IUserBussinessDao userBussinessDao;

	private static final Logger logger = LoggerFactory.getLogger(UserApiAutoRechargeServiceImpl.class);

	@Override
	public Map<String, Object> userChargeFund(Map<String, Object> paraMap) throws LTException {
		/** 三级业务码传参 */
		String thirdOptCode = StringTools.formatStr(paraMap.get("thirdOptCode"), "");
		/** 银行卡id */
		String bankCardId = StringTools.formatStr(paraMap.get("bankCardId"), "");
		/** 用户id */
		String userId = StringTools.formatStr(paraMap.get("userId"), "");

		UserBankInfo userBankInfo = userBankInfoServiceImpl.getUserBankInfoByUserIdBankCode(userId, bankCardId);
		if (StringTools.isNotEmpty(userBankInfo)) {
			paraMap.put("bankCardNum", userBankInfo.getBankCardNum());
			paraMap.put("bankCode", userBankInfo.getBankCode());
			paraMap.put("idCardNum", userBankInfo.getIdCardNum());
			paraMap.put("tele", userBankInfo.getTele());
			paraMap.put("userName", userBankInfo.getUserName());
		}

		/** daddyPay充值需要获取特殊的银行编码 */
		if (thirdOptCode.equals(FundThirdOptCodeEnum.DADDYPAY.getThirdLevelCode())) {
			BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
			JSONObject sysCfgRedis = JSONObject.parseObject(rechargeConfig.get(thirdOptCode));
			// 默认bank_id,比如银联、支付宝等,如果没有配置则走银行卡
			String defBankId = sysCfgRedis.getString("daddypay_default_bank_id");
			if (StringTools.isNotEmpty(defBankId)) {
				paraMap.put("bankCode", defBankId);
			} else {
				BankInfo info = userBussinessDao.getBankInfoByCode(userBankInfo.getBankCode());
				if (info != null && StringTools.isNotEmpty(info.getDaddypayBankCode())) {
					paraMap.put("bankCode", info.getDaddypayBankCode());
				} else {
					throw new LTException(LTResponseCode.USY0001);
				}
			}
		}
		String groupId = this.userRechargeServiceImpl.queryGroupIdByChannelId(thirdOptCode);
		paraMap.put("groupId", groupId);
		if (RechargeGroupEnum.ALIPAY.getGroupId().equals(groupId) 
				|| RechargeGroupEnum.IAPPPAY.getGroupId().equals(groupId) 
				|| RechargeGroupEnum.QIANTONPAY.getGroupId().equals(groupId)
				|| RechargeGroupEnum.SWIFTPASS.getGroupId().equals(groupId)
				|| RechargeGroupEnum.DADDYPAY.getGroupId().equals(groupId) 
				|| RechargeGroupEnum.AGGPAY.getGroupId().equals(groupId)
				|| RechargeGroupEnum.ALIPAYTRANSFER.getGroupId().equals(groupId)
				|| RechargeGroupEnum.ALIPAYH5.getGroupId().equals(groupId)) {
			logger.info("获取类名为：{}", "userRechargeBy" + groupId);
			UserChargeFunc userChargefunc = (UserChargeFunc) SpringUtils.getBean("userRechargeBy" + groupId);
			logger.info("userRechargeBy 是否为null ：{}", userChargefunc == null);
			return userChargefunc.excute(paraMap, fundAccountServiceImpl);
		}

		logger.info("获取类名为：{}", "userCharge" + thirdOptCode + "Func");
		UserChargeFunc userChargefunc = (UserChargeFunc) SpringUtils.getBean("userCharge" + thirdOptCode + "Func");
		logger.info("userchargeFunc 是否为null ：{}", userChargefunc == null);
		return userChargefunc.excute(paraMap, fundAccountServiceImpl);
	}

	@Override
	public void userReviceZfbFResponse(Map<String, Object> map) throws LTException {

		// 商户订单号
		String order_id = map.get("merchantOutOrderNo").toString();
		// 订单详细信息
		String pay_msg = map.get("pay_msg").toString();
		// 支付宝平台订单号
		String order_no = map.get("orderNo").toString();
		// 支付结果
		String payResult = map.get("payResult").toString();
		// 实际到账金额
		String factAmount = map.get("payMoney").toString();
		// 签名
		String sign = map.get("sign").toString();
		// 签名要素
		String signParam = map.get("signParam").toString();

		logger.info("支付宝收到结果result:{}", payResult);

		if ("1".equals(payResult)) {
			/** 校验 beg */
			FundAlipayRecharge alipayRecharge = userRechargeServiceImpl.getAliChargeByOrderId(order_id);
			if (alipayRecharge == null) {
				logger.info("未成功查询到历史充值记录");
				throw new LTException(LTResponseCode.FUY00006);
			}

			if (alipayRecharge.getPayResult() != 2) {
				logger.info("订单已被处理");
				throw new LTException(LTResponseCode.FUY00011);
			}
			/** 校验 end */

			/** 校验签名start **/
			/** 查询配置信息 **/
			BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
			String config = rechargeConfig.get(alipayRecharge.getBizCode());
			logger.info("支付宝渠道配置:{}", config);
			JSONObject jsonObject = JSONObject.parseObject(config);

			String key = jsonObject.getString("secretKey");
			String signValidate = jsonObject.getString("signValidate");

			// BoundHashOperations<String, String, String> sysCfgRedis =
			// redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			// String signValidate = sysCfgRedis.get("xl_alipay_sign_validate");
			if ("1".equals(signValidate)) {
				signParam = signParam + "key=" + key;
				if (!sign.equals(MD5Util.md5(signParam).toLowerCase())) {
					logger.info("校验签名失败!sign:{},paramSign:{}", sign, signParam);
					return;
				}
			}
			/** 校验签名end **/

			String bizCode = alipayRecharge.getBizCode();
			/** 存入记录 beg */
			// 不清楚具体的报文格式，暂时协定为1.0
			alipayRecharge = new FundAlipayRecharge(order_id, Integer.parseInt(payResult), order_no, Double.valueOf(factAmount), pay_msg, null);
			Integer num = userRechargeServiceImpl.updateAlipayBackInfo(alipayRecharge);
			if (num != 1) {
				logger.info("更新支付宝失败或已被操作处理");
				return;
			}
			/** 存入记录 end */

			/** 修改资金数据 beg */
			FundOptCode code = this.userRechargeServiceImpl.getFundOptCodeByThirdOptCode(bizCode);
			fundAccountServiceImpl.doUserRecharge(order_id, order_no, code, Double.valueOf(factAmount));
			/** 修改资金数据 end */
		}
	}

	@Override
	@Transactional
	public void userReviceXDPayResponse(Map<String, Object> map) throws LTException {
		try {
			/** 接收参数 **/
			String notifyType = (String) map.get("notify");
			String orderType = (String) map.get("oerder_type");
			String orderNo = (String) map.get("order_no");
			String hostOrderNo = (String) map.get("host_order_no");
			String amount = (String) map.get("amount");
			String orgAmount = (String) map.get("org_amount");
			String sign = (String) map.get("sign");
			String status = (String) map.get("status");
			String errCode = (String) map.get("err_code");
			String errMsg = (String) map.get("err_msg");
			String orderTime = (String) map.get("order_time");
			String reserver = (String) map.get("reserver");

			logger.info("【熙大支付宝】收到金额" + amount + "支付结果：" + status);
			/** 接收参数 **/
			// String signParam = (String)map.get("signParam");

			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

			/** 是否校验签名start **/
			String checkSign = sysCfgRedis.get("xdpay_check_sign");
			String key = sysCfgRedis.get("xdpay_private_key");
			if ("1".equals(checkSign)) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("amount=" + amount);
				if (StringTools.isNotEmpty(errCode)) {
					buffer.append("&err_code=" + errCode);
				}
				if (StringTools.isEmpty(errMsg)) {
					buffer.append("&err_msg=" + errMsg);
				}
				buffer.append("&host_order_no=" + hostOrderNo);
				buffer.append("&notify_type=" + notifyType);
				buffer.append("&order_no=" + orderNo);
				buffer.append("&order_type=" + orderType);
				buffer.append("&org_amount=" + orgAmount);
				if (StringTools.isNotEmpty(orderTime)) {
					buffer.append("&order_time=" + orderTime);
				}
				if (StringTools.isNotEmpty(reserver)) {
					buffer.append("&reserver=" + reserver);
				}
				buffer.append("&status=" + status);
				buffer.append(key);
				String currentSign = MD5Util.md5(buffer.toString()).toLowerCase();
				if (!sign.equals(currentSign)) {
					logger.info("【熙大支付宝处理支付结果】检查签名不一致！");
					return;
				}
			}
			/** 是否校验签名end **/

			/** 订单处理 **/
			XDPayRecharge recharge = this.userRechargeServiceImpl.queryXDPayRechargeByOrderId(orderNo);
			if (recharge == null) {
				logger.info("当前订单不存在,订单号:{}" + orderNo);
				return;
			}
			if ("SUCCESS".equals(recharge.getStatus())) {
				logger.info("当前订单已处理成功!订单号:{}" + orderNo);
				return;
			}
			if ("SUCCESS".equals(status)) {
				recharge.setStatus(status);
				recharge.setHostOrderNo(hostOrderNo);
				recharge.setTransAmount(Double.valueOf(orgAmount));
				recharge.setNotifyType(notifyType);
				if (StringTools.isNotEmpty(orderTime)) {
					recharge.setOrderTime(DateTools.parseDate(orderTime, "yyyyMMddHHmmss"));
				}
				this.userRechargeServiceImpl.updateXDPayRecharge(recharge);

				/** 修改资金数据 beg */
				FundOptCode code = new FundOptCode();
				code.setFirstOptCode(FundCashOptCodeEnum.RECHARGE.getFirstLevelCode());
				code.setSecondOptCode(FundThirdOptCodeEnum.XDPAY.getSecondCode());
				code.setThirdOptCode(FundThirdOptCodeEnum.XDPAY.getThirdLevelCode());
				fundAccountServiceImpl.doUserRecharge(orderNo, hostOrderNo, code, Double.valueOf(StringTools.getAmountToYuan(orgAmount)));

				/** 修改资金数据 end */
			} else if ("FAIL".equals(status)) {
				recharge.setStatus(status);
				if (StringTools.isNotEmpty(errCode)) {
					recharge.setErrCode(errCode);
				}
				if (StringTools.isNotEmpty(errMsg)) {
					recharge.setErrCode(errMsg);
				}
				this.userRechargeServiceImpl.updateXDPayRecharge(recharge);
			} else {
				logger.info("【熙大支付宝处理结果失败】status:" + status);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("处理熙大支付宝回调结果异常");
			throw new LTException(LTResponseCode.ER400);
		}
	}

	@Override
	public void userReviceQTPayResponse(Map<String, Object> map) throws LTException {
		String paramstr = (String) map.get("paramstr");
		try {
			// 业务码
			FundOptCode code = new FundOptCode();
			code.setFirstOptCode(FundCashOptCodeEnum.RECHARGE.getFirstLevelCode());
			code.setSecondOptCode(FundThirdOptCodeEnum.QIANTONGPAY.getSecondCode());
			code.setThirdOptCode(FundThirdOptCodeEnum.QIANTONGPAY.getThirdLevelCode());

			PaymentNotifyResponse pnResponse = new MerchantClient().parsePaymentNotify(paramstr.toString());
			logger.info("========钱通充值={}==============", JSONObject.toJSONString(pnResponse));
			List<DeductInfo> list = pnResponse.getDeductList();
			// 理论上list只会有一条数据
			for (DeductInfo deductInfo : list) {
				String payOrderId = deductInfo.getPayOrderId();// 外部支付id
				String payAmt = StringTools.getAmountToYuan(deductInfo.getPayAmt());// 到账金额
				String payStatus = deductInfo.getPayStatus();// 到账状态
				logger.info("=======钱通充值返回结果，payAmt={},payStatus={}=======", payAmt, payStatus);
				if ("01".equals(payStatus)) {// 成功
					/** 修改资金数据 beg */
					fundAccountServiceImpl.doUserRecharge(pnResponse.getMerchantOrderId(), payOrderId, code, Double.valueOf(payAmt));
					/** 修改资金数据 end */
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void userReviceIapppayResponse(Map<String, Object> map) throws LTException {
		try {
			logger.info("爱贝接收参数:{}", map.toString());
			String transdata = (String) map.get("transdata");
			JSONObject object = JSONObject.parseObject(transdata);
			String cporderid = object.getString("cporderid");
			String transid = object.getString("transid");
			FundIapppayRecharge iapppayRecharge = this.userRechargeServiceImpl.getFundIapppayRechargeByPayId(cporderid);
			if (iapppayRecharge == null) {
				logger.info("平台订单号不存在:{}", cporderid);
				return;
			}
			if ("0".equals(iapppayRecharge.getResultId())) {
				logger.info("已处理,m平台订单号存在:{}", cporderid);
				return;
			}
			BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
			JSONObject rechargeInfo = JSONObject.parseObject(rechargeConfig.get(iapppayRecharge.getBizCode()));
			String publicKey = rechargeInfo.getString("publicKey");
			/** 校验签名 **/
			String sign = (String) map.get("sign");
			if (!SignHelper.verify(transdata, sign, publicKey)) {
				logger.info("[校验签名失败]");
				return;
			}

			/**
			 * result 交易结果： 0–交易成功 1–交易失败
			 **/
			String result = object.getString("result");
			if ("1".equals(result)) {
				logger.info("交易失败,订单号{}", cporderid);
				return;
			}
			if ("0".equals(result)) {
				logger.info("交易成功,订单号{}", cporderid);
				Double money = object.getDouble("money");
				FundOptCode optCode = this.userRechargeServiceImpl.getFundOptCodeByThirdOptCode(iapppayRecharge.getBizCode());
				this.fundAccountServiceImpl.doUserRecharge(cporderid, transid, optCode, money);
				iapppayRecharge.setResultId(result);
				iapppayRecharge.setUpdateStamp(new Date());
				this.userRechargeServiceImpl.updateFundIapppayRechargeByPayId(iapppayRecharge);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("处理爱贝回调异常");
		}
	}

	@Override
	public Map<String, Object> qianTongPayConfirm(String checkCode, String payOrderId) throws LTException {
		logger.info("==========钱通接口支付请求参数checkCode:{},payOrderId:{}==========", checkCode, payOrderId);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("code", LTResponseCode.SUCCESS);
		resultMap.put("msg", "充值提交成功");
		/** 获取参数 **/
		BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		// 钱通业务码
		JSONObject sysCfgRedis = JSONObject.parseObject(rechargeConfig.get(FundThirdOptCodeEnum.QIANTONGPAY.getThirdLevelCode()));
		// 商户号
		String merchantId = sysCfgRedis.getString("mchId");
		// 支付地址
		String qiantong_pay_url = sysCfgRedis.getString("reqUrl");
		// 版本
		String qiantong_version = sysCfgRedis.getString("qiantong_version");
		// 支付平台H5
		// String qiantong_application =
		// sysCfgRedis.getString("qiantong_application");
		CertPayConfirmRequest confirmRequest = new CertPayConfirmRequest();
		// 封装bean
		confirmRequest.setApplication("CertPayConfirm");
		confirmRequest.setVersion(qiantong_version);
		confirmRequest.setMerchantId(merchantId);
		confirmRequest.setMerchantOrderId(payOrderId);
		confirmRequest.setCheckCode(checkCode);
		MerchantClient merchantClient = new MerchantClient();
		merchantClient.setPayURL(qiantong_pay_url);
		try {
			CertPayConfirmResponse confirmResponse = merchantClient.sendCertPayConfirmRequest(confirmRequest);
			logger.info("=========钱通接口支付第二次返回数据:{}===========", JSONObject.toJSONString(confirmResponse));
			if (!"000".equals(confirmResponse.getRespCode())) {
				resultMap.put("code", LTResponseCode.ER400);
				resultMap.put("msg", confirmResponse.getRespDesc());
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", LTResponseCode.ER400);
			resultMap.put("msg", "请求异常");
		}
		return resultMap;
	}

	@Override
	@Transactional
	public void userReviceAggpayResponse(Map<String, Object> map) throws LTException {
		try {
			logger.info("聚合支付结果接收参数:{}", map.toString());

			String dealStatus = (String) map.get("DealStatus");
			String orderId = (String) map.get("OrderID");
			FundAggpayRecharge aggpayRecharge = this.userRechargeServiceImpl.getFundAggpayRechargeByPayIdForUpdate(orderId);
			if (aggpayRecharge == null) {
				logger.info("平台订单号不存在:{}", orderId);
				throw new LTException("平台订单号不存在");
			}
			if ("0".equals(aggpayRecharge.getDealStatus())) {
				logger.info("已支付,平台订单号存在:{}", orderId);
				throw new LTException("订单已处理");
			}
			if ("1".equals(dealStatus)) {
				logger.info("未支付,平台订单号:{}", orderId);
				throw new LTException("订单未支付");
			}
			BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
			JSONObject rechargeInfo = JSONObject.parseObject(rechargeConfig.get(aggpayRecharge.getBizCode()));
			String secretKey = rechargeInfo.getString("secretKey");

			/**
			 * dealStatus 0已支付，1未支付，其他失败
			 **/
			if (!"1".equals(dealStatus) && !"0".equals(dealStatus)) {
				logger.info("交易失败,订单号{}", orderId);
				throw new LTException("支付失败");
			}
			if ("0".equals(dealStatus)) {
				/** 校验签名 **/
				String sign = (String) map.get("Sign");
				map.remove("Sign");
				/**交易附言可能为空**/
				if(StringTools.isEmpty((String)map.get("Summary"))){
					map.remove("Summary");
				}
				String signData = MapUtils.convertUrlParam(map).concat("&Key=" + secretKey);
				String mySign = MD5Util.md5(signData).toUpperCase();
				logger.info("平台签名:{}",mySign);
				if (!sign.equals(mySign)) {
					logger.info("校验签名失败");
					throw new LTException("校验签名失败");
				}

				/** 更新订单 **/
				logger.info("支付成功,订单号{}", orderId);
				Double money = Double.valueOf(StringTools.getAmountToYuan((String) map.get("Amt")));
				FundOptCode optCode = this.userRechargeServiceImpl.getFundOptCodeByThirdOptCode(aggpayRecharge.getBizCode());
				this.fundAccountServiceImpl.doUserRecharge(orderId, null, optCode, money);
				aggpayRecharge.setModtifyStamp(new Date());
				aggpayRecharge.setDealStatus(dealStatus);
				this.userRechargeServiceImpl.updateFundAggpayRechargeByPayId(aggpayRecharge);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("处理聚合回调异常");
		}
	}

}
