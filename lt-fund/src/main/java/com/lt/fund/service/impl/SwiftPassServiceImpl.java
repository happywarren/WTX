package com.lt.fund.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundIoRechargeEnum;
import com.lt.fund.dao.FundIoCashRechargeDao;
import com.lt.fund.dao.FundOptCodeDao;
import com.lt.fund.dao.SwiftPassDao;
import com.lt.fund.service.IFundCashRechargeService;
import com.lt.fund.service.ISwiftPassService;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.charge.FundSwiftPassResultModel;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.util.utils.swiftpass.util.SignUtils;
import com.lt.util.utils.swiftpass.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by guodawang on 2017/9/4.
 */
@Service
public class SwiftPassServiceImpl implements ISwiftPassService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FundOptCodeDao fundOptCodeDao;

    @Autowired
    private SwiftPassDao swiftPassDao;

    @Autowired
    private FundIoCashRechargeDao fundIoCashRechargeDao;

    @Autowired
    private IFundCashRechargeService fundCashRechargeService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public String swiftPassResult(Map<String, String> map)throws LTException{
//        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);



        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
        String out_trade_no = map.get("out_trade_no");
        FundIoCashRecharge bean = fundIoCashRechargeDao.selectFundIoRechargeForUpdate(out_trade_no);
        logger.info("查询该笔交易是否已处理 bean = {}", JSON.toJSON(bean));
        if(!StringTools.isNotEmpty(bean)){
            logger.error("在数据库中无法查到对应的支付订单 订单号:{}",out_trade_no);
            return "fail";
        }
        //String json = sysCfgRedis.get("1010118");
        String json = sysCfgRedis.get(bean.getThirdOptcode());
        JSONObject obj = JSONObject.parseObject(json);
        String key = obj.getString("secretKey");

        String respString = null;
        String res = XmlUtils.toXml(map);
        FundSwiftPassResultModel model = paraObject(map);
        swiftPassDao.insert(model);
        logger.info("通知内容:{}" , res);
        if(map.containsKey("sign")){
            if(map.containsKey("cmd")){
                map.remove("cmd");

            }
            if(map.containsKey("func")){
                map.remove("func");
            }
            if(map.containsKey("code"))
            if(!SignUtils.checkParam(map, key)){
                res = "验证签名不通过";
                respString = "fail";
                logger.info("---------------------------签名不通过------------------------");
            }else{
                logger.info("---------------------------签名通过------------------------");
                //此处可以在添加相关处理业务，校验通知参数中的商户订单号out_trade_no和金额total_fee是否和商户业务系统的单号和金额是否一致，一致后方可更新数据库表中的记录。
                String status = map.get("status");
                if(status != null && "0".equals(status)){
                    String result_code = map.get("result_code");
                    if(result_code != null && "0".equals(result_code)){
                        //根据订单号查询用户资金信息 是否该笔入金已处理

                        if(bean.getStatus() != 1){
                            //Object obj =
                            //若处理了 直接return 若没处理执行入金操作
//                           String trade_state = map.get("trade_state");
                            //SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭 REVERSE—已冲正 REVOK—已撤销
                            //实际成功的情况下没收到trade_state参数
//                            if(trade_state.equals("SUCCESS")){
                                //TODO 执行入金操作
                                logger.info("执行入金操作");
                                callback(bean,bean.getThirdOptcode(),model.getTotalFee());
//                            }
                        }else{
                            logger.error("=======================入金订单已处理,无需再处理。入金订单号：{}",out_trade_no);
                        }
                        respString = "success";
                    }
                }

            }
        }
        return  respString;
    }

    @Transactional
    private void callback(FundIoCashRecharge bean,String thirdOptCode,String actAmount){
        String orderId = bean.getPayId();
        // 查询充值订单信息
        FundIoCashRecharge fio = fundIoCashRechargeDao.selectFundIoRechargeOne(orderId);
        try{
            if (fio == null) {
                logger.info("威富通返回的订单不存在，orderid={}", orderId);
            }
            if (FundIoRechargeEnum.AUDIT.getValue() != fio.getStatus()) {
                logger.info("此订单状态已被改变，orderid={}", orderId);
            }
            logger.info("执行reCharge");
            // 获取充值配置信息

            FundOptCode fundOptCode = fundOptCodeDao.queryFundOptCodeByCode(null, null, thirdOptCode);
            if (fundOptCode == null) {
                throw new LTException("没有对应的资金流转方式!");
            }

            String payId = fio.getPayId();
            String externalId = "";
//            Double realAmount = fio.getAmount();
            Double realRmbAmount = DoubleTools.div(Double.valueOf(actAmount),100);

            fundCashRechargeService.doSuccessRecharge(payId, externalId, realRmbAmount, fundOptCode);
        }
        catch (LTException e){
            Response response = LTResponseCode.getCode(e.getMessage());
            if(fio != null){
                fio.setStatus(FundIoRechargeEnum.FAIL.getValue());// 失败
                fio.setRemark("威富通付充值:回调失败【"+response.getMsg()+"】");
                fundIoCashRechargeDao.updateFundIoRecharge(fio);
            }
        }

    }

    private FundSwiftPassResultModel paraObject(Map<String,String> resultMap){
        FundSwiftPassResultModel model = new FundSwiftPassResultModel();
        model.setCharset(resultMap.get("charset"));
        model.setCodeImgUrl(resultMap.get("code_img_url"));
        model.setCodeUrl(resultMap.get("code_url"));
        model.setMchId(resultMap.get("mch_id"));
        model.setNonceStr(resultMap.get("nonce_str"));
        model.setResultCode(resultMap.get("result_code"));
        model.setSign(resultMap.get("sign"));
        model.setSignType(resultMap.get("sign_type"));
        model.setVersion(resultMap.get("version"));
        model.setStatus(resultMap.get("status"));
        model.setContent(JSONObject.toJSONString(resultMap));
        model.setBuyerLogonId(resultMap.get("buyer_logon_id"));
        model.setBankType(resultMap.get("bank_type"));
        model.setUserId(resultMap.get("user_id"));
        model.setTransactionId(resultMap.get("transaction_id"));
        model.setBuyerUserId(resultMap.get("buyer_user_id"));
        model.setErrCode(resultMap.get("err_code"));
        model.setErrMsg(resultMap.get("err_msg"));
        model.setFeeType(resultMap.get("fee_type"));
        model.setFundBillList(resultMap.get("fund_bill_list"));
        model.setGmtCreate(resultMap.get("gmt_create"));
        model.setMessage(resultMap.get("message"));
        model.setOpenid(resultMap.get("openid"));
        model.setOutTradeNo(resultMap.get("out_trade_no"));
        model.setOutTransactionId(resultMap.get("out_transaction_id"));
        model.setPayResult(resultMap.get("pay_result"));
        model.setTimeEnd(resultMap.get("time_end"));
        model.setTotalFee(resultMap.get("total_fee"));
        model.setTradeType(resultMap.get("trade_type"));
        return model;

    }
}
