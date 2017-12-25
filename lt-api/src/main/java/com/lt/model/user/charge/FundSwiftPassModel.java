package com.lt.model.user.charge;

import java.io.Serializable;

/**
 * 威富通交易请求参数实体
 * Created by guodawang on 2017/9/4.
 */
public class FundSwiftPassModel implements Serializable {
    //请求参数
//    body=测试交易&mch_create_ip=127.0.0.1&mch_id=399590032501&nonce_str=1504493598101&notify_url=https://test2.meiguwang.cn/lt-interface/swiftPassResultSerlet&out_trade_no=170901171111312333&service=pay.weixin.native&total_fee=1&key=53116be6a82c3e3422e4f50f0e06476f
//    reqUrl：https://pay.swiftpass.cn/pay/gateway
//    reqParams:<xml><body>测试交易</body>
//<mch_create_ip>127.0.0.1</mch_create_ip>
//<mch_id>399590032501</mch_id>
//<nonce_str>1504493598101</nonce_str>
//<notify_url>https://test2.meiguwang.cn/lt-interface/swiftPassResultSerlet</notify_url>
//<out_trade_no>170901171111312333</out_trade_no>
//<service>pay.weixin.native</service>
//<sign>8faa4dd3388da799c3eb236246ac3257</sign>
//<total_fee>1</total_fee>
//</xml>

    public String userId;

    /**
     * 交易标题
      */
    public String body;

    /**
     * 提交IP
     */
    public String mchCreateIp;

    /**
     * 商家号
     */
    public String mchId;

    /**
     * 随机数
     */
    public String nonceStr;

    /**
     * 返回通知地址
     */
    public String notifyUrl;

    /**
     * 服务端生成的订单号
     */
    public String outTradeNo;
    /* 支付类型 支付宝 微信*/
    public String service;
    /**
     * MD5加密字符串
     */
    public String sign;

    /**
     * 申请支付金额
     */
    public String totalFee;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMchCreateIp() {
        return mchCreateIp;
    }

    public void setMchCreateIp(String mchCreateIp) {
        this.mchCreateIp = mchCreateIp;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
