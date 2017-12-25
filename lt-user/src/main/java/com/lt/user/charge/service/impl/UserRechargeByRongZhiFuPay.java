package com.lt.user.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.rzf.HttpUtil;
import com.lt.util.utils.rzf.SignUtil;
import com.lt.util.utils.rzf.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserRechargeByRongZhiFuPay extends UserRechargeSuper implements UserChargeFunc {

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
        logger.info("渠道配置:{}",config);
        /**获取参数**/
        JSONObject sysCfgRedis = JSONObject.parseObject(rechargeConfig.get(baseCharge.getChannelId()));
        Map<String,String> map = new HashMap<String,String>();


        String rmbAmt = DoubleUtils.doubleFormat(baseCharge.getRmbAmt(),2);
        map.put("id",sysCfgRedis.getString("mchId"));
        map.put("appid",sysCfgRedis.getString("appid"));
        map.put("gid","75");
        map.put("orderidinf",baseCharge.getPayOrderId());
        map.put("totalPrice",rmbAmt);
        map.put("ordertitle","支付款项");
        map.put("goodsname","商品");
        map.put("goodsdetail","商品");
        map.put("bgRetUrl",sysCfgRedis.getString("notifyUrl"));
        map.put("returnUrl",sysCfgRedis.getString("returnUrl"));
        map.put("posttime",new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())); //请求时间
        String sign =  SignUtil.verifyToSign(map.get("id"), map.get("appid"), map.get("orderidinf"), map.get("totalPrice"),sysCfgRedis.getString("secretKey"));
        map.put("sign",sign);
        String result = HttpUtil.sendPost(sysCfgRedis.getString("reqUrl"),Utils.createLinkString(map));
        logger.info("返回的json={}",result);
        Map<String, Object> resultMap = (Map) JSON.parse(result);
        String code =  String.valueOf(resultMap.get("code"));
        String success =  String.valueOf(resultMap.get("success"));
        String msg = String.valueOf(resultMap.get("msg"));
        String respSign = String.valueOf(resultMap.get("sign"));
        String orderId = String.valueOf(resultMap.get("orderId"));
        String orderidinf = String.valueOf(resultMap.get("orderidinf"));
        String codeurl = String.valueOf(resultMap.get("codeurl"));
        //对响应的请求进行验签名
        String willSign = "payreturn"+sysCfgRedis.getString("mchId")+sysCfgRedis.getString("appid")+orderId+rmbAmt+sysCfgRedis.getString("secretKey");
        boolean retSign = SignUtil.verifySign(sysCfgRedis.getString("mchId"),sysCfgRedis.getString("appid"),orderidinf,map.get("totalPrice"),respSign,sysCfgRedis.getString("secretKey"));

        Map<String,Object> resultMapInfo = new HashMap<String,Object>();
        if(!retSign){
            resultMapInfo.put("code", "400");
            resultMapInfo.put("msg", "签名认证失败！");
        }else{
            if(code.equals("0x0000")){
                resultMapInfo.put("code", "200");
                resultMapInfo.put("msg", "处理成功！");
                resultMapInfo.put("reqUrl",codeurl);
            }else{
                resultMapInfo.put("code", "400");
                resultMapInfo.put("msg", "处理失败！");
            }
        }
        return resultMapInfo;
    }

    @Override
    public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
        return null;
    }

    @Override
    public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
        logger.info("baseCharge:{}", JSONObject.toJSONString(baseCharge));
        super.getFundAccountServiceImpl().insertRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(),
                baseCharge.getRmbAmt(), baseCharge.getBankCardNum(),baseCharge.getBankCode(),
                baseCharge.getChannelId(), baseCharge.getRate());
        return true;
    }

    @Override
    public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl) throws LTException {
        logger.info("map:{}",JSONObject.toJSONString(map));
        super.setChargeFunc(this);
        super.setFundAccountServiceImpl(fundAccountServiceImpl);
        Map<String, Object> returnMap =  super.excute(map);
        return returnMap ;
    }
}
