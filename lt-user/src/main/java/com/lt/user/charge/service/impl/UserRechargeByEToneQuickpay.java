package com.lt.user.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.etonepay.b2c.utils.MD5;
import com.itrus.util.DateUtils;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.utils.DoubleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserRechargeByEToneQuickpay extends UserRechargeSuper implements UserChargeFunc {

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
        requestParams.put("version","1.0.0");
        requestParams.put("transCode","8888");
        requestParams.put("merchantId",jsonObject.getString("mchId"));
        requestParams.put("merOrderNum",baseCharge.getPayOrderId());
        requestParams.put("bussId",jsonObject.getString("publicKey"));
        requestParams.put("tranAmt",String.valueOf((int)(DoubleUtils.mul(baseCharge.getRmbAmt(),100))));
        requestParams.put("sysTraceNum",baseCharge.getPayOrderId());
        requestParams.put("tranDateTime",DateUtils.getCustomDateString(new Date(),"yyyyMMddHHmmss"));
        requestParams.put("currencyType","156");
        requestParams.put("merURL",jsonObject.getString("notifyUrl"));
        requestParams.put("backURL",jsonObject.getString("returnUrl"));
        requestParams.put("orderInfo","");
        requestParams.put("userId",baseCharge.getTele());
        requestParams.put("userPhoneHF",baseCharge.getTele());
        requestParams.put("userAcctNo",baseCharge.getBankCardNum());
        try {
            requestParams.put("userNameHF", URLEncoder.encode(baseCharge.getUserName(),"utf-8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestParams.put("userIp","");
        requestParams.put("bankId","888880170122900");
        requestParams.put("stlmId","");
        requestParams.put("entryType","");
        requestParams.put("attach","");
        requestParams.put("reserver1","");
        requestParams.put("reserver2","");
        requestParams.put("reserver3","");
        requestParams.put("reserver4","7");
        String payUrl = jsonObject.getString("reqUrl");

        logger.info("username={}",baseCharge.getUserName());
        String txnString=
                requestParams.get("version")+"|"+requestParams.get("transCode")+"|"+requestParams.get("merchantId")+"|"+requestParams.get("merOrderNum")+"|"+requestParams.get("bussId")
                        +"|"+requestParams.get("tranAmt")+"|"+requestParams.get("sysTraceNum")+"|"+requestParams.get("tranDateTime")+"|"+requestParams.get("currencyType")+"|"+
                        requestParams.get("merURL")+"|"+requestParams.get("backURL")+"|"+requestParams.get("orderInfo")+"|"+requestParams.get("userId");


        String secretKey = jsonObject.getString("secretKey");
        String signValue = MD5.getInstance().getMD5ofStr(txnString+secretKey);
        requestParams.put("signValue",signValue);
        StringBuilder urlParamStr = new StringBuilder();
        for (Map.Entry<String,String> entry : requestParams.entrySet()){
            urlParamStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        String urlParams = urlParamStr.substring(1);
        String url = payUrl+"?"+urlParams;
        map.put("code", "200");
        map.put("msg", "处理成功");
        map.put("returnUrl",url);
        return map;
    }

    @Override
    public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {

        return null;
    }

    @Override
    public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
        /*
        super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(),
                baseCharge.getRmbAmt(), baseCharge.getBankCardNum(),baseCharge.getBankCode(),
                FundThirdOptCodeEnum.ETONEQUICKPAY.getThirdLevelCode(), baseCharge.getRate());*/
        logger.info("baseCharge:{}", JSONObject.toJSONString(baseCharge));
        super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(), baseCharge.getRmbAmt(), baseCharge.getBankCardNum(), baseCharge.getBankCode(), baseCharge.getChannelId(),
                baseCharge.getRate());
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
