package com.lt.model.user.charge;

import java.io.Serializable;

/**
 * 威富通返回的消息实体(包括异步返回 以及直接返回 放到同一张表)
 * * Created by guodawang on 2017/9/4.
 */
public class FundSwiftPassResultModel implements Serializable {


    public FundSwiftPassResultModel() {
    }

    public Integer id;

    /**
     * 二维码图片  直接用此链接请求二维码图片
     */

    public String codeImgUrl;
    /**
     * 二维码链接 此参数可直接生成二维码展示出来进行扫码 支付
     */
    public String codeUrl;

    /**
     * 错误码
     */
    public String errCode;

    /* 错误消息*/
    public String errMsg;

    /**
     * 系统交易用户
     */
    public String userId;

    /**
     * 收到的完整内容
     */
    public String content;

    /**
     * 返回信息，如非空，为错误原因签名失败参数格式校验错误
     */
    public String message;

    /**
     * 银行类型
     */
    public String bankType;
//    <public><![CDATA[ALIPAYACCOUNT]]></bank_type>
    /**
     * 支付账户
     */
    public String buyerLogonId;
//<buyer_logon_id><![CDATA[402***@qq.com]]></buyer_logon_id>
    /**
     * 用户标识 用户支付宝的账户名  ?跟openid  感觉没区别  帮助文档没有说明
     */
    public String buyerUserId;
//<buyer_user_id><![CDATA[2088302209863674]]></buyer_user_id>

    /**
     * 字符集：可选值 UTF-8 ，默认为 UTF-8。
     */
    public String charset;
//<charset><![CDATA[UTF-8]]></charset>

    /**
     * 币种
     */
    public String feeType;
//<fee_type><![CDATA[CNY]]></fee_type>

    /**
     * 资金账单列表  [{"amount":"0.01","fundChannel":"ALIPAYACCOUNT"}]
     */
    public String fundBillList;
//<fund_bill_list><![CDATA[[{"amount":"0.01","fundChannel":"ALIPAYACCOUNT"}]]]></fund_bill_list>

    /**
     *
     */
    public String gmtCreate;
//<gmt_create><![CDATA[20170901174435]]></gmt_create>

    /**
     * 商家ID
     */
    public String mchId;
//<mch_id><![CDATA[399590032501]]></mch_id>

    /**
     * 随机字符串 随机字符串，不长于 32 位
     */
    public String nonceStr;
//<nonce_str><![CDATA[1504259081190]]></nonce_str>

    /**
     * 用户标识 用户支付宝的账户名
     */
    public String openid;
//<openid><![CDATA[2088302209863674]]></openid>

    /**
     * 商户订单：商户系统内部的定单号，32 个字符内、可包 含字母
     */
    public String outTradeNo;
//<out_trade_no><![CDATA[1709011711131233]]></out_trade_no>

    /**
     * 第三方商户号:对应支付宝交易(微信)记录账单详情中的交易号
     */
    public String outTransactionId;
//<out_transaction_id><![CDATA[2017090121001004670241452167]]></out_transaction_id>

    /**
     * 支付返回结果
     */
    public String payResult;
//<pay_result><![CDATA[0]]></pay_result>

    /**
     * 业务结果 0 表示成功非 0 表示失败
     */
    public String resultCode;
//<result_code><![CDATA[0]]></result_code>

    /**
     * SwiftPassMD5 签名结果
     */
    public String sign;
//<sign><![CDATA[9124AF6D7F4E4879EE9224F1F00C8CC7]]></sign>

    /**
     * 签名类型
     */
    public String signType;
//<sign_type><![CDATA[SwiftPassMD5]]></sign_type>

    /**
     * 0 表示成功非 0 表示失败 此字段是通信标识，非交易 标识，交易是否成功需要查看 trade_state 来判断
     */
    public String status;
//<status><![CDATA[0]]></status>

    /**
     * 支付完成时间 YYYYMMDDhhmmss
     */
    public String timeEnd;
//<time_end><![CDATA[20170901174440]]></time_end>

    /**
     * 支付金额   整数类型 单位分
     */
    public String totalFee;
//<total_fee><![CDATA[1]]></total_fee>

    /**
     * 交易类型  pay.alipay.native pay.weixin.native
     */
    public String tradeType;
//<trade_type><![CDATA[pay.alipay.native]]></trade_type>

    /**
     * 对应支付宝交易记录账单详情中的商户订单 号
     * 威富通交易号 , out_trade_no 和 transaction_id 至少一个必填，同时存在时 transaction_id 优先。
     */
    public String transactionId;
//<transaction_id><![CDATA[399590032501201709017204785699]]></transaction_id>

    /**
     * 版本号，version 默认值是 2.0
     */
    public String version;
//<version><![CDATA[2.0]]></version>


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFundBillList() {
        return fundBillList;
    }

    public void setFundBillList(String fundBillList) {
        this.fundBillList = fundBillList;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutTransactionId() {
        return outTransactionId;
    }

    public void setOutTransactionId(String outTransactionId) {
        this.outTransactionId = outTransactionId;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getTradeType() {
        return tradeType;

    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCodeImgUrl() {
        return codeImgUrl;
    }

    public void setCodeImgUrl(String codeImgUrl) {
        this.codeImgUrl = codeImgUrl;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}


//    请求失败结果：<xml><charset><![CDATA[UTF-8]]></charset>
//<err_code><![CDATA[Order paid]]></err_code>
//<err_msg><![CDATA[订单已支付]]></err_msg>
//<mch_id><![CDATA[399590032501]]></mch_id>
//<nonce_str><![CDATA[1504493540828]]></nonce_str>
//<result_code><![CDATA[1]]></result_code>
//<sign><![CDATA[90BBB9D4B86AE7C234C7A1AE643BABF9]]></sign>
//<sign_type><![CDATA[SwiftPassMD5]]></sign_type>
//<status><![CDATA[0]]></status>
//<version><![CDATA[2.0]]></version>
//</xml>

//    请求成功结果：<xml><appid><![CDATA[wxe00fda5293a2fa25]]></appid>
//<charset><![CDATA[UTF-8]]></charset>
//<code_img_url><![CDATA[https://pay.swiftpass.cn/pay/qrcode?uuid=weixin%3A%2F%2Fwxpay%2Fbizpayurl%3Fpr%3Dn5Uh8qy]]></code_img_url>
//<code_url><![CDATA[weixin://wxpay/bizpayurl?pr=n5Uh8qy]]></code_url>
//<mch_id><![CDATA[399590032501]]></mch_id>
//<nonce_str><![CDATA[1504493598101]]></nonce_str>
//<result_code><![CDATA[0]]></result_code>
//<sign><![CDATA[9A1F31A3D67EA5E5A165F716B6F5E7AE]]></sign>
//<sign_type><![CDATA[SwiftPassMD5]]></sign_type>
//<status><![CDATA[0]]></status>
//<uuid><![CDATA[1ba7deb9f39da9e9575eef64e20abdff5]]></uuid>
//<version><![CDATA[2.0]]></version>
//</xml>
//    appid=wxe00fda5293a2fa25&charset=UTF-8&code_img_url=https://pay.swiftpass.cn/pay/qrcode?uuid=weixin%3A%2F%2Fwxpay%2Fbizpayurl%3Fpr%3Dn5Uh8qy&code_url=weixin://wxpay/bizpayurl?pr=n5Uh8qy&mch_id=399590032501&nonce_str=1504493598101&result_code=0&sign_type=MD5&status=0&uuid=1ba7deb9f39da9e9575eef64e20abdff5&version=2.0&key=53116be6a82c3e3422e4f50f0e06476f

//通知内容：
//<xml><bank_type><![CDATA[ALIPAYACCOUNT]]></bank_type>
//<buyer_logon_id><![CDATA[402***@qq.com]]></buyer_logon_id>
//<buyer_user_id><![CDATA[2088302209863674]]></buyer_user_id>
//<charset><![CDATA[UTF-8]]></charset>
//<fee_type><![CDATA[CNY]]></fee_type>
//<fund_bill_list><![CDATA[[{"amount":"0.01","fundChannel":"ALIPAYACCOUNT"}]]]></fund_bill_list>
//<gmt_create><![CDATA[20170901174435]]></gmt_create>
//<mch_id><![CDATA[399590032501]]></mch_id>
//<nonce_str><![CDATA[1504259081190]]></nonce_str>
//<openid><![CDATA[2088302209863674]]></openid>
//<out_trade_no><![CDATA[1709011711131233]]></out_trade_no>
//<out_transaction_id><![CDATA[2017090121001004670241452167]]></out_transaction_id>
//<pay_result><![CDATA[0]]></pay_result>
//<result_code><![CDATA[0]]></result_code>
//<sign><![CDATA[9124AF6D7F4E4879EE9224F1F00C8CC7]]></sign>
//<sign_type><![CDATA[SwiftPassMD5]]></sign_type>
//<status><![CDATA[0]]></status>
//<time_end><![CDATA[20170901174440]]></time_end>
//<total_fee><![CDATA[1]]></total_fee>
//<trade_type><![CDATA[pay.alipay.native]]></trade_type>
//<transaction_id><![CDATA[399590032501201709017204785699]]></transaction_id>
//<version><![CDATA[2.0]]></version>
//</xml>
