package com.lt.user.charge.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.IUserApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.KQAPIResponseMap;
import com.lt.model.fund.FundPayAuthFlow;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.charge.FundKqRecharge;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.bean.RechargeByKQDynamic;
import com.lt.user.charge.dao.sqldb.UserCharge1010102Dao;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.kqRecharge.util.DJXXml;
import com.lt.util.utils.kqRecharge.util.Post;

/**   
* 项目名称：lt-user   
* 类名称：UserCharge1010102Func   
* 类描述：快钱充值   
* 创建人：yuanxin   
* 创建时间：2017年6月30日 下午5:09:07      
*/
@Component
public class UserCharge1010102DFunc extends UserRechargeSuper implements UserChargeFunc {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private IUserApiService userApiServiceImpl;
	
	@Autowired
	private UserCharge1010102Dao charge1010102Dao ;                                                                                                                                                              
	
	Logger logger = LoggerFactory.getLogger(UserCharge1010102DFunc.class);

	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		
		RechargeByKQDynamic rechargeByKqDy = (RechargeByKQDynamic)baseCharge.getObject() ;

		Double amt = baseCharge.getAmt();
		String telephone = rechargeByKqDy.getTele();
		String userId = baseCharge.getUserId();
		Double rate = baseCharge.getRate();
		Double rmbAmt = baseCharge.getRmbAmt();
		String bankCardNum = baseCharge.getBankCardNum();

		logger.info("rmbAmt:{},rate:{},amt:{}",rmbAmt,rate,amt);
		if (DoubleUtils.scaleFormatEnd(DoubleUtils.mul(rmbAmt, rate), 2) != amt) {
			throw new LTException(LTResponseCode.FUY00005);
		}

		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		// 获取用户默认银行卡信息
		UserBaseInfo baseInfo = userApiServiceImpl.findUserByUserId(userId);
		// [快钱API文档]MAS CNP Merchant API Specification 1.21.doc
		// 4. 消息规范：信息字段说明
		String version = "1.0";// 接口版本号
		String storablePan = bankCardNum.substring(0, 6)
				+ bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length());// 缩略卡号(银行卡号前6位+后4位).在快捷再次支付时，若查询到客户号已绑定2张（含2张）以上的卡号时，短卡号必填。
		String cardHolderName = baseInfo.getUserName();// 客户姓名
		String merchantId = sysCfgRedis.get("kuaiqian_api_merchant_id");// 商户编号
		String idType = "0";// 证件类型
		String cardHolderId = baseInfo.getIdCardNum().toUpperCase();// 客户身份证号

		String phoneNO = telephone;
		if (StringUtils.isEmpty(phoneNO)) {
			phoneNO = baseInfo.getTele();// 手机号码
		}
		// 商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
		FundKqRecharge fundKqRecharge = new FundKqRecharge(userId, merchantId, baseInfo.getUserName(), 
				baseInfo.getIdCardNum().toUpperCase(), bankCardNum, rmbAmt, null) ;
		String orderId = fundKqRecharge.getKqOrderId() ;
		baseCharge.setPayOrderId(orderId);
		//=========[判断是否鉴权绑卡]=================================================
        FundPayAuthFlow authFlow = new FundPayAuthFlow();
        authFlow.setCustomerId(userId);
        authFlow.setStorablePan(storablePan);

        Boolean isExist = super.getFundAccountServiceImpl().isExistPayAuthFlow(authFlow);
        //=========[判断是否鉴权绑卡]=================================================

        String str1Xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\">"
                + "<version>" + version + "</version>"
                + "<GetDynNumContent>"
                + "<merchantId>" + merchantId + "</merchantId>"
                + "<customerId>" + userId + "</customerId>"
                + "<externalRefNumber>" + orderId + "</externalRefNumber>";

        if (!isExist) {
            str1Xml += "<cardHolderName>" + cardHolderName + "</cardHolderName>"
                    + "<idType>" + idType + "</idType>"
                    + "<cardHolderId>" + cardHolderId.toUpperCase() + "</cardHolderId>"
                    + "<pan>" + bankCardNum + "</pan>"
                    + "<phoneNO>" + phoneNO + "</phoneNO>";
        } else {
            if (!"".equals(storablePan)) {
                str1Xml += "<storablePan>" + storablePan + "</storablePan>";
            }
        }
        str1Xml += "<amount>" + rmbAmt + "</amount>"
                + "</GetDynNumContent>"
                + "</MasMessage>";
        
        fundKqRecharge.setValidPacket(str1Xml);
        // 添加验证码报文记录
        charge1010102Dao.insertKQrecordValid(fundKqRecharge);
        logger.info("[快钱API充值渠道]是否首次支付：{}，获取手机验证码报文》str1Xml={}", !isExist, str1Xml);
		return str1Xml;
	}

	@Override
	public Map<String, Object> returnParam(String urlCode,String packet, BaseChargeBean baseCharge) throws LTException {
		String responseCode = urlCode.substring(urlCode.indexOf("<responseCode>") + 14,urlCode.indexOf("</responseCode>"));
		// 返回返回提示信息
		String responseTextMessage = "";// 错误的时候有提示信息
		if (urlCode.contains("responseTextMessage")) {
			responseTextMessage = urlCode.substring(urlCode.indexOf("<responseTextMessage>") + 21,urlCode.indexOf("</responseTextMessage>"));
		}

		logger.info("===========responseCode={}=================", responseCode);
		Map<String, Object> respMap = null ;
		String token = "";
		// 如果TR2获取的应答码responseCode的值为00时，成功
		if ("00".equals(responseCode)) {
			logger.info("[快钱API充值渠道] 获取动态码成功");

			String respXmlCut1 = urlCode.substring(urlCode.indexOf("<GetDynNumContent>"),urlCode.indexOf("</GetDynNumContent>"));
			String respXmlCut2 = "</GetDynNumContent>";
			String respXmlcut = respXmlCut1 + respXmlCut2;

			DJXXml dxml = new DJXXml();
			NodeList tr2listxml = null;
			tr2listxml = dxml.Jxml(respXmlcut);

			token = tr2listxml.item(3).getFirstChild().getNodeValue() ;

			// 因订单号是在获取验证码的时候获取的，又通过缓存传给下单支付报文。不能在下单支付时生成系统内部订单，否则会因为多次点击支付，生成多个订单
			respMap = new HashMap<String, Object>();
			respMap.put("msg", baseCharge.getPayOrderId());
			respMap.put("code", LTResponseCode.SUCCESS);
			
		}else{
			
			String resMsg = responseTextMessage;
			if (StringUtils.isEmpty(responseTextMessage)) {
				resMsg = KQAPIResponseMap.getResponseMsg(responseCode);
			}

			String resp = "{\"code\": " + responseCode + ",\"msg\": \"" + resMsg
					+ "\", \"msgType\": 0,\"errparam\": \"\",\"data\": \"\"}";
			logger.warn("resp={}", resp);

			respMap = new HashMap<String, Object>();
			respMap.put("code", responseCode);
			respMap.put("msg", resMsg);
		}
		
		// 添加返回数据记录
		FundKqRecharge fundKqRecharge = new FundKqRecharge(baseCharge.getPayOrderId(), responseCode, 
				responseTextMessage, urlCode,token);
		charge1010102Dao.addKQrecordValid(fundKqRecharge);
		
		return respMap;
	}

	@Override
	public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
		//快捷支付手机动态鉴权URL
		
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		
        String getdynnumUrl = sysCfgRedis.get("kuaiqian_api_getdynnum_url");
        
        String respXml = "";

        try {
            //TR2接收的数据
            respXml = Post.sendPost(getdynnumUrl, packet);
            logger.info("[快钱API充值渠道]TR2接收的数据,respXml={}", JSON.toJSONString(respXml));

            if (StringUtils.isEmpty(respXml)) {
                logger.warn("[快钱API充值渠道]获取手机验证码 TR2数据失败.");
                throw new LTException(LTResponseCode.US01106);
            }
        }catch (Exception e) {
			throw new LTException(e.getMessage());
		}
		return respXml;
	}

	@Override
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		logger.info("[快钱API充值渠道]============生成系统内部订单 START============");
		
		try {
			super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(), baseCharge.getRmbAmt(), baseCharge.getBankCardNum(),
					baseCharge.getBankCode(), FundThirdOptCodeEnum.KQCZ.getThirdLevelCode(), baseCharge.getRate());
			
		} catch (RuntimeException e) {
			logger.error("[快钱API充值渠道] 错误堆栈:e={}", e);
			e.printStackTrace();
			throw new LTException(LTResponseCode.FUY00002);
		}
		
		logger.info("[快钱API充值渠道]=================生成系统内部订单 END==============");
		
		return false;
	}

	@Override
	public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl)
			throws LTException {
		super.setChargeFunc(this);
		super.setFundAccountServiceImpl(fundAccountServiceImpl);
		return super.excute(map);
	}

}
