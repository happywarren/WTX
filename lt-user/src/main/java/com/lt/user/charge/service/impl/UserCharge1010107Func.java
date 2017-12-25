package com.lt.user.charge.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.net.URLCodec;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.container.page.Menu;
import com.alibaba.fastjson.JSONObject;
import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.model.user.charge.DinpayExtraInfo;
import com.lt.model.user.charge.XDPayRecharge;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.bean.RechargeByDinpay;
import com.lt.user.charge.dao.sqldb.DinpayExtraInfoDao;
import com.lt.user.charge.dao.sqldb.UserCharge1010107Dao;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.MD5Util;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import javolution.util.FastMap;

/**   
* 项目名称：lt-user   
* 类名称：UserCharge1010107Func   
* 类描述：支付宝充值2   
* 创建人：yubei   
* 创建时间：2017年8月1日      
*/
@Service
public class UserCharge1010107Func extends UserRechargeSuper implements UserChargeFunc {
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(UserCharge1010107Func.class);	
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	
	@Autowired
	private UserCharge1010107Dao rechargeDao;
	
	
	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return "";
	}

	@Override
	public Map<String, Object> returnParam(String urlCode,String packet,BaseChargeBean baseCharge) throws LTException {
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", LTResponseCode.SUCCESS);
			map.put("msg", "熙大支付宝处理参数成功");
			
			/**获取参数**/
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String merchantNo = sysCfgRedis.get("xdpay_merchant_no");
			String orderType = sysCfgRedis.get("xdpay_order_type");
			String bgUrl = sysCfgRedis.get("xdpay_notify_url");
			String key = sysCfgRedis.get("xdpay_private_key");
			String reqUrl = sysCfgRedis.get("xdpay_req_url");
			String pageUrl = sysCfgRedis.get("xdpay_page_url");
			//String queryUrl = sysCfgRedis.get("xdpay_query_url");
			String goods = URLEncoder.encode("入金", "utf-8");
			String orderNo = baseCharge.getPayOrderId();
			logger.info("【熙大支付宝充值】生成平台订单号:"+orderNo);
			String orderAmount = StringTools.getAmountToCent(baseCharge.getRmbAmt().toString());
			
			/**签名**/
			StringBuffer paramSign = new StringBuffer();
			paramSign.append("bg_url="+bgUrl);
			paramSign.append("&goods="+goods);
			paramSign.append("&merchant_no="+merchantNo);
			paramSign.append("&order_no="+orderNo);
			paramSign.append("&order_type="+orderType);
			paramSign.append("&page_url="+pageUrl);
			paramSign.append("&trans_amount="+orderAmount);
			paramSign.append(key);
			String sign = MD5Util.md5(paramSign.toString()).toLowerCase();
			
			map.put("bg_url", bgUrl);
			map.put("goods", goods);
			map.put("merchant_no", merchantNo);
			map.put("trans_amount",orderAmount);
			map.put("order_type", orderType);
			map.put("sign", sign);
			map.put("req_url", reqUrl);
			map.put("order_no", orderNo);
			map.put("page_url", pageUrl);
			
			XDPayRecharge recharge = new XDPayRecharge();
			recharge.setUserId(baseCharge.getUserId());
			recharge.setAmount(Double.valueOf(StringTools.getAmountToCent(baseCharge.getRmbAmt()+"")));
			recharge.setBgUrl(bgUrl);
			recharge.setGoods(goods);
			recharge.setSign(sign);
			recharge.setMerchantNo(merchantNo);
			recharge.setOrderType(orderType);
			recharge.setOrderNo(orderNo);
			recharge.setStatus("WAIT");
			map.put("recharge", recharge);
			return map;					
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("【熙大支付宝入金处理签名异常】");
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
					FundThirdOptCodeEnum.XDPAY.getThirdLevelCode(), baseCharge.getRate());
			
			return true ;
		}catch(LTException e){
			e.printStackTrace();
			logger.error("【熙大支付宝入金保存数据异常】");
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
			XDPayRecharge recharge = (XDPayRecharge)resultMap.get("recharge");
			this.rechargeDao.insertXDPayRecharge(recharge);
			resultMap.put("code", LTResponseCode.SUCCESS);
			resultMap.put("msg", "成功");
			resultMap.remove("recharge");
			logger.info("【熙大入金输出】"+resultMap.toString());
			return resultMap;
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("【熙大支付宝入金异常】");
			throw new LTException(e.getMessage());
		}
	}
	public static void main(String[] args) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("bg_url=https://test2.meiguwang.cn/lt-interface/xdpayCallBackServlet");
		buffer.append("&goods=%E7%94%A8%E6%88%B7%E5%85%A5%E9%87%91");
		buffer.append("&merchant_no=827000000781531");
		buffer.append("&order_no=1502187985918");
		buffer.append("&order_type=1002");
		buffer.append("&trans_amount=800");
		buffer.append("10727C2262C2CFE7FDF7C427E42CFB7B");
		System.out.println(MD5Util.md5(buffer.toString()).toLowerCase());
	}
}
