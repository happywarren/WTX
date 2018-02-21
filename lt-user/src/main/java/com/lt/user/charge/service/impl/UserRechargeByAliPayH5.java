package com.lt.user.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserRechargeByAliPayH5 extends UserRechargeSuper implements UserChargeFunc {

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
        logger.info("baseCharge="+baseCharge);
        logger.info("根据通道id查询通道的配置文件channelId={}",baseCharge.getChannelId());
        String config = rechargeConfig.get(baseCharge.getChannelId());
        JSONObject jsonObject = JSONObject.parseObject(config);
        logger.info("渠道配置:{}",config);
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,String> requestParams = new HashMap<String,String>();
        String appid = jsonObject.getString("mchId");
        String payUrl = jsonObject.getString("reqUrl");
        String secretKey = jsonObject.getString("secretKey");
        String publicKey = jsonObject.getString("publicKey");
        String notifyUrl = jsonObject.getString("notifyUrl");

        AlipayClient client = new DefaultAlipayClient(payUrl,appid, secretKey, "json", "UTF-8", publicKey,"RSA2");
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(baseCharge.getPayOrderId());
        model.setSubject("支付费用");
        model.setTotalAmount(baseCharge.getRmbAmt()+"");
        model.setBody("");
        model.setTimeoutExpress("2m");
        model.setProductCode("QUICK_WAP_WAY");
        alipay_request.setNotifyUrl(notifyUrl);


        try {
            String body =  client.pageExecute(alipay_request).getBody();
            logger.info("body:{}",body);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        String signValue = "";
        requestParams.put("signValue",signValue);


        map.put("code", "200");
        map.put("msg", "处理成功");
      //  map.put("returnUrl",url);

        return map;
    }

    @Override
    public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
        return null;
    }

    @Override
    public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {

        super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(),
                baseCharge.getRmbAmt(), baseCharge.getBankCardNum(),baseCharge.getBankCode(),
               baseCharge.getChannelId(), baseCharge.getRate());
        return false;
    }

    @Override
    public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl) throws LTException {
        logger.info("map:{}", JSONObject.toJSONString(map));
        super.setChargeFunc(this);
        super.setFundAccountServiceImpl(fundAccountServiceImpl);
        Map<String, Object> returnMap =  super.excute(map);
        return returnMap ;
    }

}
