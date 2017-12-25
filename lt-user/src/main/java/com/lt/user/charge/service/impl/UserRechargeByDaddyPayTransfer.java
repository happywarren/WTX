package com.lt.user.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.etonepay.b2c.utils.HttpUtil;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.crypt.MD5Util;
import com.lt.util.utils.iapppay.demo.HttpUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserRechargeByDaddyPayTransfer extends UserRechargeSuper implements UserChargeFunc {

    private  static Map<String,String> bankInfoMap =new HashMap<String,String>();

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    Logger logger = LoggerFactory.getLogger(getClass());


    static{
        bankInfoMap.put("1","工商银行");
        bankInfoMap.put("2","招商银行");
        bankInfoMap.put("3","建设银行");
        bankInfoMap.put("4","农业银行");
        bankInfoMap.put("5","中国银行");
        bankInfoMap.put("6","交通银行");
        bankInfoMap.put("7","民生银行");
        bankInfoMap.put("8","中信银行");
        bankInfoMap.put("9","上海浦东发展银行");
        bankInfoMap.put("10","邮政储汇");
        bankInfoMap.put("11","光大银行");
        bankInfoMap.put("12","平安银行");
        bankInfoMap.put("13","广发银行");
        bankInfoMap.put("14","华夏银行");
        bankInfoMap.put("15","兴业银行");
    }

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

        String private_key = jsonObject.getString("secretKey");
        String payUrl = jsonObject.getString("reqUrl");;

        String bankId = "";

        String bankCode = baseCharge.getBankCode();
        if(bankCode.equals("0001")){ //广发银行
            bankId= "13";
        }else if(bankCode.equals("0002")){   //浦发银行
            bankId = "9";
        }else if(bankCode.equals("0003")){   //兴业银行
            bankId = "15";
        }else if(bankCode.equals("0004")){   //招商银行
            bankId = "2";
        }else if(bankCode.equals("0005")){   //中国银行
            bankId = "5";
        }else if(bankCode.equals("0006")){   //中国农业银行
            bankId = "4";
        }else if(bankCode.equals("0007")){    //中国建设银行
            bankId = "3";
        }else if(bankCode.equals("0008")){     //中国光大银行
            bankId = "11";
        }else if(bankCode.equals("0009")){     //平安银行
            bankId = "12";
        }else if(bankCode.equals("0010")){     //中国民生银行
            bankId = "7";
        }else if(bankCode.equals("0011")){     //华夏银行
            bankId = "14";
        }else if(bankCode.equals("0012")){      //工商银行
            bankId = "1";
        }else if(bankCode.equals("0013")){       //中信银行
            bankId = "8";
        }else if(bankCode.equals("0014")){        //交通银行
            bankId = "6";
        }else if(bankCode.equals("0015")){      //邮政储蓄银行
            bankId = "10";
        }

        //参数组装
        Map<String,String> reqParamMap = new HashMap<String,String>();
        reqParamMap.put("company_id",jsonObject.getString("mchId"));
        reqParamMap.put("bank_id",bankId);
        Double rmbAmt =  baseCharge.getRmbAmt();
        reqParamMap.put("amount", DoubleUtils.doubleFormat(rmbAmt,2));
        reqParamMap.put("company_order_num",baseCharge.getPayOrderId());
        reqParamMap.put("company_user","LT_DADDY_TRANSFER");
        reqParamMap.put("estimated_payment_bank",bankId);
        reqParamMap.put("deposit_mode","1");
        reqParamMap.put("group_id","0");
        reqParamMap.put("web_url","https://wwww.lt168168.com");
        reqParamMap.put("memo","");
        reqParamMap.put("note","");
        reqParamMap.put("note_model","2");
        reqParamMap.put("terminal","1");


        //生成签名
         String keySign =  MD5Util.md5Low(MD5Util.md5Low(private_key)+reqParamMap.get("company_id")+reqParamMap.get("bank_id")
                +reqParamMap.get("amount")+reqParamMap.get("company_order_num")+reqParamMap.get("company_user")
                +reqParamMap.get("estimated_payment_bank")+reqParamMap.get("deposit_mode")
                +reqParamMap.get("group_id")+reqParamMap.get("web_url")+reqParamMap.get("memo")
                +reqParamMap.get("note")+reqParamMap.get("note_model"));

         reqParamMap.put("key",keySign);

         String url  = "";
         for(Map.Entry<String,String> entry : reqParamMap.entrySet()){
             url = url + "&" + entry.getKey()+"="+entry.getValue();
         }
        logger.info("请求的url="+url);
        //JSONObject  responJsonObj = HttpTools.doPost(payUrl,reqParamMap);
        String content =  HttpTools.urlGet(payUrl+url,null,"utf-8",false);
        logger.info("content={}",content);
        JSONObject  responJsonObj =(JSONObject) JSONObject.parse(content);
        //String content =  HttpUtils.sentPost(payUrl,"");
        logger.info("responseJsonObj={}",responJsonObj);
        String bank_card_num = responJsonObj.getString("bank_card_num")==null? "":responJsonObj.getString("bank_card_num");
        String bank_acc_name = responJsonObj.getString("bank_acc_name")==null? "":responJsonObj.getString("bank_acc_name");
        String amount = responJsonObj.getString("amount")==null? "":responJsonObj.getString("amount");
        String email = responJsonObj.getString("email")==null? "":responJsonObj.getString("email");
        String company_order_num = responJsonObj.getString("company_order_num")==null? "":responJsonObj.getString("company_order_num");
        String datetime = responJsonObj.getString("datetime")==null ? "":responJsonObj.getString("datetime");
        String key = responJsonObj.getString("key")==null? "":responJsonObj.getString("key");
        String note = responJsonObj.getString("note")==null? "":responJsonObj.getString("note");
        String mownecum_order_num = responJsonObj.getString("mownecum_order_num")==null? "":responJsonObj.getString("mownecum_order_num"); //dp系统订单好
        String status = responJsonObj.getString("status")==null? "":responJsonObj.getString("status");  //状态
        String error_msg = responJsonObj.getString("error_msg")==null? "":responJsonObj.getString("error_msg");  //错误信息
        String mode = responJsonObj.getString("mode")==null? "":responJsonObj.getString("mode");
        String issuing_bank_address = responJsonObj.getString("issuing_bank_address")==null? "":responJsonObj.getString("issuing_bank_address");
        String break_url = responJsonObj.getString("break_url")==null? "":responJsonObj.getString("break_url");
        String deposit_mode = responJsonObj.getString("deposit_mode")==null? "":responJsonObj.getString("deposit_mode");
        String collection_bank_id = responJsonObj.getString("collection_bank_id")==null?"":responJsonObj.getString("collection_bank_id");
        String sign =  MD5Util.md5Low(MD5Util.md5Low(private_key)+bank_card_num+bank_acc_name+amount+email+company_order_num+
                datetime+note+mownecum_order_num+status+error_msg+mode+issuing_bank_address+break_url+deposit_mode+collection_bank_id);

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("支付渠道","daddypay银行卡转账!");
        if(status.equals("1")){
            if(key.equals(sign)){
                resultMap.put("code", "200");
                resultMap.put("msg","获取成功！");
                resultMap.put("bank_acc_name",bank_acc_name);
                resultMap.put("issuing_bank_address",issuing_bank_address);
                resultMap.put("bank_acc_name",bank_acc_name);
                resultMap.put("bank_card_num",bank_card_num);
                resultMap.put("collection_bank_id",bankInfoMap.get(collection_bank_id));
                resultMap.put("amount",amount);
                resultMap.put("note",note);
            }else{
                resultMap.put("code","400");
                resultMap.put("msg","签名失败！");
            }
        }else{
            resultMap.put("code", "400");
            resultMap.put("msg", error_msg);
        }
       // baseCharge.setRechargeIdentification(JSONObject.toJSONString(resultMap));
        return resultMap;
    }

    @Override
    public String requestUrl(String packet, BaseChargeBean baseCharge) throws LTException {
        return null;
    }

    @Override
    public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
        logger.info("baseCharge:{}", JSONObject.toJSONString(baseCharge));

        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("userId",baseCharge.getUserId());
        paramsMap.put("orderId",baseCharge.getPayOrderId());
        paramsMap.put("amt",baseCharge.getAmt());
        paramsMap.put("rmbAmt",baseCharge.getRmbAmt());
        paramsMap.put("rate",baseCharge.getRate());
        paramsMap.put("transferNum",baseCharge.getBankCardNum());
        paramsMap.put("bankCode",baseCharge.getBankCode());
        paramsMap.put("thirdLevelCode",baseCharge.getChannelId());
       // paramsMap.put("rechargeIdentification",baseCharge.getRechargeIdentification());
      //  super.getFundAccountServiceImpl().insertRechargeIo(paramsMap);
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
