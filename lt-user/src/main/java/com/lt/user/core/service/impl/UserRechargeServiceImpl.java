package com.lt.user.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.model.sys.SysConfig;
import com.lt.model.user.UserBankInfo;
import com.lt.user.core.dao.sqldb.ISysConfigDao;
import com.lt.user.core.service.IUserBankInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.IUserApiService;
import com.lt.api.user.IUserRechargeService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundIoRechargeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.KQAPIResponseMap;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundPayAuthFlow;
import com.lt.model.user.UserBaseInfo;
import com.lt.user.core.thread.PayApproachThreadMap;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.CalendarTools;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.kqRecharge.util.DJXXml;
import com.lt.util.utils.kqRecharge.util.Post;

/**
 * 项目名称：lt-user
 * 类名称：UserRechargeServiceImpl
 * 类描述：用户充值接口实现类
 * 创建人：yuanxin
 * 创建时间：2017年4月5日 上午9:29:49
 */
@Service
public class UserRechargeServiceImpl implements IUserRechargeService {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(UserRechargeServiceImpl.class);

    @Autowired
    private IUserApiService userApiServiceImpl;

    @Autowired
    private IFundAccountApiService fundAccountService;

    @Autowired
    private IUserBankInfoService userBankInfoService;

    @Autowired
    private ISysConfigDao sysConfigDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, String> getKQDynamicNum(Map<String, Object> paraMap) throws LTException {
        String amtStr = StringTools.formatStr(paraMap.get("amt"), "0.0");
        String telephone = StringTools.formatStr(paraMap.get("telephone"), "");
        String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
        String bankId = StringTools.formatStr(paraMap.get("bankId"), "1");//这里取银行卡数据的ID
//		String bankCardNum =  StringTools.formatStr(paraMap.get("bankCardNum"), "1");//没有值的
        String rateStr = StringTools.formatStr(paraMap.get("rate"), "0");
        String rmbAmtStr = StringTools.formatStr(paraMap.get("rmbAmt"), "0.0");

        UserBankInfo userBankInfo = userBankInfoService.getUserBankInfoByUserIdBankCode(userId, bankId);
        if (null == userBankInfo) {
            throw new LTException(LTResponseCode.US20000);
        }
        String bankCardNum = userBankInfo.getBankCardNum();

        if (!StringTools.isTele(telephone) || !StringTools.isNumberic(amtStr, false, true, true)|| !StringTools.isNumberic(rmbAmtStr, false, true, true)
                || !StringTools.isNumeric(bankCardNum) || !StringTools.isNumberic(rateStr, false, true, true)) {
            throw new LTException(LTResponseCode.FU00003);
        }

        Double amt = Double.valueOf(amtStr);
        Double rate = Double.valueOf(rateStr);
        Double rmbAmt = Double.valueOf(rmbAmtStr);
        if (/*amt < 300D || */DoubleUtils.scaleFormatEnd(DoubleUtils.mul(rmbAmt, rate), 2) != amt) {
            throw new LTException(LTResponseCode.FUY00005);
        }

        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        // 获取用户默认银行卡信息
        UserBaseInfo baseInfo = userApiServiceImpl.findUserByUserId(userId);
        // 商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        String orderId = String.valueOf(CalendarTools.getMillis(new Date()));
        // [快钱API文档]MAS CNP Merchant API Specification 1.21.doc
        // 4. 消息规范：信息字段说明
        String version = "1.0";// 接口版本号
        String storablePan = bankCardNum.substring(0, 6) + bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length());// 缩略卡号(银行卡号前6位+后4位).在快捷再次支付时，若查询到客户号已绑定2张（含2张）以上的卡号时，短卡号必填。
        String cardHolderName = baseInfo.getUserName();// 客户姓名
        String merchantId = sysCfgRedis.get("kuaiqian_api_merchant_id");// 商户编号
        String idType = "0";// 证件类型
        String cardHolderId = baseInfo.getIdCardNum().toUpperCase();// 客户身份证号

        String phoneNO = telephone;
        if (StringUtils.isEmpty(phoneNO)) {
            phoneNO = baseInfo.getTele();//手机号码
        }

        //=========[判断是否鉴权绑卡]=================================================
        FundPayAuthFlow authFlow = new FundPayAuthFlow();
        authFlow.setCustomerId(userId);
        authFlow.setStorablePan(storablePan);

        Boolean isExist = fundAccountService.isExistPayAuthFlow(authFlow);
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

        logger.info("[快钱API充值渠道]是否首次支付：{}，获取手机验证码报文》str1Xml={}", !isExist, str1Xml);

        //快捷支付手机动态鉴权URL
        String getdynnumUrl = sysCfgRedis.get("kuaiqian_api_getdynnum_url");

        try {
            //TR2接收的数据
            String respXml = Post.sendPost(getdynnumUrl, str1Xml);
            logger.info("[快钱API充值渠道]TR2接收的数据,respXml={}", JSON.toJSONString(respXml));

            if (StringUtils.isEmpty(respXml)) {
                logger.warn("[快钱API充值渠道]获取手机验证码 TR2数据失败.");
                throw new LTException(LTResponseCode.US01106);
            }
            String responseCode = respXml.substring(respXml.indexOf("<responseCode>") + 14, respXml.indexOf("</responseCode>"));
            //返回返回提示信息
            String responseTextMessage = "";//错误的时候有提示信息
            if (respXml.contains("responseTextMessage")) {
                responseTextMessage = respXml.substring(respXml.indexOf("<responseTextMessage>") + 21, respXml.indexOf("</responseTextMessage>"));
            }

            logger.info("===========responseCode={}=================", responseCode);
            //如果TR2获取的应答码responseCode的值为00时，成功
            if ("00".equals(responseCode)) {
                logger.info("[快钱API充值渠道] 获取动态码成功");

                String respXmlCut1 = respXml.substring(respXml.indexOf("<GetDynNumContent>"), respXml.indexOf("</GetDynNumContent>"));
                String respXmlCut2 = "</GetDynNumContent>";
                String respXmlcut = respXmlCut1 + respXmlCut2;

                DJXXml dxml = new DJXXml();
                NodeList tr2listxml = null;
                tr2listxml = dxml.Jxml(respXmlcut);

                //将快钱的token令牌信息，缓存到redis中
                BoundHashOperations<String, String, String> kuaiqianPayInfo = redisTemplate.boundHashOps(RedisUtil.FINANCY_KUAIQIAN_PAY_INFO);
                JSONObject obj = new JSONObject();
                obj.put("token", tr2listxml.item(3).getFirstChild().getNodeValue());//快钱的令牌信息
                obj.put("externalRefNumber", orderId);//外部跟踪编号(最新充值订单号)
                obj.put("amount", amt); // 存储系统币种（美元）
                obj.put("rate", rate);
                obj.put("rmbAmt", rmbAmt);
                //以用户的userId + 订单号 作为主键 淡出userId 容易被覆盖
                kuaiqianPayInfo.put(userId + orderId, obj.toJSONString());

                //因订单号是在获取验证码的时候获取的，又通过缓存传给下单支付报文。不能在下单支付时生成系统内部订单，否则会因为多次点击支付，生成多个订单
                logger.info("[快钱API充值渠道]============生成系统内部订单 START============");
                Map<String, String> respMap = new HashMap<String, String>();
                //系统内部订单号
                try {
                    fundAccountService.insertRechargeIo(userId, orderId, amt,rmbAmt, bankCardNum,userBankInfo.getBankCode(), FundThirdOptCodeEnum.KQCZ.getThirdLevelCode(), rate);
                    respMap.put("msg", orderId);
                } catch (RuntimeException e) {
                    logger.error("[快钱API充值渠道] 错误堆栈:e={}", e);
                    e.printStackTrace();
                    throw new LTException(LTResponseCode.FUY00002);
                }
                logger.info("[快钱API充值渠道]=================生成系统内部订单 END==============");
                respMap.put("code", LTResponseCode.SUCCESS);
                return respMap;
            }

            String resMsg = responseTextMessage;
            if (StringUtils.isEmpty(responseTextMessage)) {
                resMsg = KQAPIResponseMap.getResponseMsg(responseCode);
            }

            String resp = "{\"code\": " + responseCode + ",\"msg\": \"" + resMsg + "\", \"msgType\": 0,\"errparam\": \"\",\"data\": \"\"}";
            logger.warn("resp={}", resp);

            Map<String, String> respMap = new HashMap<String, String>();
            respMap.put("code", responseCode);
            respMap.put("msg", resMsg);
            return respMap;

        } catch (Exception e) {
            logger.error("[快钱API充值渠道] 获取动态码，异常e={}", e);
            e.printStackTrace();
        }

        throw new LTException(LTResponseCode.ER400);
    }

    @Override
    public Map<String, String> userKQPayOrder(Map<String, Object> paraMap) throws LTException {
        String userIdStr = StringTools.formatStr(paraMap.get("userId"), "-1");
        String validCode = StringTools.formatStr(paraMap.get("validCode"), "");
        String tele = StringTools.formatStr(paraMap.get("telephone"), "");
        String cardNum = StringTools.formatStr(paraMap.get("bankCardNum"), "1");
        String code = StringTools.formatStr(paraMap.get("code"), "0");
        BoundHashOperations<String, String, String> kuaiqianCfg = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        BoundHashOperations<String, String, String> kuaiqianPayInfo = redisTemplate.boundHashOps(RedisUtil.FINANCY_KUAIQIAN_PAY_INFO);


        if (StringUtils.isEmpty(validCode)) {
            logger.warn("请输入手机号验证码.validCode={}", validCode);
            throw new LTException(LTResponseCode.US01106);
        }

        if (!StringTools.isTele(tele)) {
            logger.warn("手机号码格式输入错误.telephone={}", tele);
            throw new LTException(LTResponseCode.US01102);
        }


        //请求用户服务，获取用户基本信息
        UserBaseInfo baseInfo = userApiServiceImpl.findUserByUserId(userIdStr);
        logger.info("[快钱API充值渠道]》 info={}", JSON.toJSONString(baseInfo));

        String objJSON = kuaiqianPayInfo.get(baseInfo.getUserId() + code);
        logger.info("objectJson :{}",objJSON);
        JSONObject obj = JSON.parseObject(objJSON);
        //redis 快钱令牌信息  ...token
        String token = obj.getString("token");
        //redis 外部跟踪编号(最新充值订单号)
        final String externalRefNumber = obj.getString("externalRefNumber");
        //获取人民币金额
        final String rmbAmts = obj.getString("rmbAmt");

        //[快钱API文档]MAS CNP Merchant API Specification 1.21.doc
        //4. 消息规范：信息字段说明
        String version = "1.0";//接口版本号
        String txnType = "PUR";//交易类型编码
        final String customerId = userIdStr;//客户号

        String bankId = "";//银行代码当.输入bankId数值时，系统会判断卡号与bankId是否匹配。若匹配则交易正常进行，若不匹配，提示报错：B.MGW.0120  Element[bankId] not matched CardNo;当不上送bankId时，系统不会做任何判断。
        final String storablePan = cardNum.substring(0, 6) + cardNum.substring(cardNum.length() - 4, cardNum.length());//缩略卡号(银行卡号前6位+后4位).在快捷再次支付时，若查询到客户号已绑定2张（含2张）以上的卡号时，短卡号必填。
        String cardHolderName = baseInfo.getUserName();//客户姓名

        String interactiveStatus = "TR1";//消息状态
        String cardNo = cardNum;//卡号
        final String merchantId = kuaiqianCfg.get("kuaiqian_api_merchant_id");//商户编号
        final Double rmbAmt = Double.valueOf(rmbAmts);
        String terminalId = kuaiqianCfg.get("kuaiqian_api_terminal_id");//终端号
        String idType = "0";//证件类型
        String cardHolderId = baseInfo.getIdCardNum().toUpperCase();//客户身份证号
        String key = "phone";
        String tr3Url = kuaiqianCfg.get("kuaiqian_api_tr3_url");//tr3回调地址
        String entryTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//客户端交易时间

        String spFlag = "QuickPay";//消费鉴权
        String phone = tele;
        if (StringUtils.isEmpty(phone)) {
            phone = baseInfo.getTele();//持卡人手机号
        }
        final String tel = phone; 
        //手机验证码 ... validateCode
        String savePciFlag = "1";//是否保存鉴权信息  0 不保存 1 保存

        //=========[判断是否鉴权绑卡]=================================================
        FundPayAuthFlow authFlow = new FundPayAuthFlow();
        authFlow.setCustomerId(customerId);
        authFlow.setStorablePan(storablePan);

        Boolean isExist = fundAccountService.isExistPayAuthFlow(authFlow);
        //判断是否是首次支付(没有做过鉴权绑卡的就是首次)
        String payBatch = isExist ? "2" : "1";//(默认值设置为1)快捷支付批次:1：首次支付 /2：再次支付

        //========[Tr1报文拼接]====================================================================
        String str1Xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\">"
                + "<version>" + version + "</version>"
                + "<TxnMsgContent>"
                + "<interactiveStatus>" + interactiveStatus + "</interactiveStatus>"
                + "<spFlag>" + spFlag + "</spFlag>"
                + "<txnType>" + txnType + "</txnType>"
                + "<merchantId>" + merchantId + "</merchantId>"
                + "<terminalId>" + terminalId + "</terminalId>"
                + "<externalRefNumber>" + externalRefNumber + "</externalRefNumber>"
                + "<entryTime>" + entryTime + "</entryTime>"
                + "<amount>" + rmbAmt + "</amount>"
                + "<customerId>" + customerId + "</customerId>"
                + "<cardNo>" + cardNo + "</cardNo>"
                + "<storableCardNo>" + storablePan + "</storableCardNo>"
                + "<tr3Url>" + tr3Url + "</tr3Url>";
        if ("1".equals(payBatch)) {
            str1Xml += "<bankId>" + bankId + "</bankId>"
                    + "<cardHolderName>" + cardHolderName + "</cardHolderName>"
                    + "<idType>" + idType + "</idType>"
                    + "<cardHolderId>" + cardHolderId.toUpperCase() + "</cardHolderId>";
        }
        str1Xml += "<extMap>";

        str1Xml += "<extDate><key>" + key + "</key><value>" + phone + "</value></extDate>"
                + "<extDate><key>validCode</key><value>" + validCode + "</value></extDate>"
                + "<extDate><key>savePciFlag</key><value>" + savePciFlag + "</value></extDate>"
                + "<extDate><key>token</key><value>" + token + "</value></extDate>"
                + "<extDate><key>payBatch</key><value>" + payBatch + "</value></extDate>"
                + "</extMap>"
                + "</TxnMsgContent>"
                + "</MasMessage>";
        //========[Tr1报文拼接]====================================================================

        try {
            logger.info("[快钱API充值渠道]》订单支付，请求报文。payBatch={},str1Xml={}", payBatch, str1Xml);
            String payOrderPurchaseUrl = kuaiqianCfg.get("kuaiqian_api_pay_order_purchase_url");
            //TR2接收的数据
            String respXml = Post.sendPost(payOrderPurchaseUrl, str1Xml);
            
            logger.info("返回的数据为：respXml：{}",respXml);

            //返回错误码
            String responseCode = respXml.substring(respXml.indexOf("<responseCode>") + 14, respXml.indexOf("</responseCode>"));
            //返回返回提示信息
            String responseTextMessage = KQAPIResponseMap.getResponseMsg(responseCode);

            String respXmlCut = respXml.replace("<?xml version='1.0' encoding='UTF-8' standalone='yes'?><MasMessage xmlns='http://www.99bill.com/mas_cnp_merchant_interface'><version>1.0</version>", "");
            String respXmlCut2 = respXmlCut.replace("</MasMessage>", "");
            logger.info("支付返回信息：respXmlCut2 ={}", respXmlCut2);

            DJXXml dxml = new DJXXml();
            NodeList tr2listxml = null;
            tr2listxml = dxml.Jxml(respXmlCut2);

            //如果TR2获取的应答码responseCode的值为00时，成功.
            if ("00".equals(responseCode))//交易请求承兑或处理成功。
            {
                logger.info("[快钱API充值渠道]》支付成功");
                //检索参考号
                final String refNumber = tr2listxml.item(9).getFirstChild().getNodeValue();
                logger.info("********************** 检索参考号refNumber={}", refNumber);
                //同一个订单号放在一个单线程队列中，防止重复操作及通知(线程使用userId判断，不是订单号，防止循环查询确认订单时，同一个用户有多个掉单，反馈时，产生的并发情况)
                PayApproachThreadMap.getApproachTread(customerId).execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                //=====更新鉴权记录到本地系统，若更新失败，则插入鉴权记录(本系统采用消费鉴权)
                                try {

                                    FundPayAuthFlow financyPayAuthFlow = new FundPayAuthFlow();
                                    financyPayAuthFlow.setCustomerId(customerId);
                                    financyPayAuthFlow.setStorablePan(storablePan);
                                    financyPayAuthFlow.setMerchantId(merchantId);
                                    financyPayAuthFlow.setTel(tel);
                                    Integer rows = fundAccountService.updateFundPayAuthFlow(financyPayAuthFlow);
                                    if (rows == null || rows == 0) {
                                        fundAccountService.addFinancyPayAuthFlow(financyPayAuthFlow);
                                    }

                                } catch (Exception e) {
                                    logger.error("更新鉴权记录到本地系统.异常e={}", e);
                                    e.printStackTrace();
                                }

                                logger.info("===========[快钱API充值渠道]通知，修改本地资金流水记录START=====================================");
                                try {
                                    fundAccountService.doUserKQrecharge(externalRefNumber, rmbAmt);
                                } catch (Exception e) {
                                    logger.error("[快钱API充值渠道]：充值成功，内部订单处理失败,内部订单编号{}", externalRefNumber);
                                    logger.error("[快钱API充值渠道]：错误堆栈,{}", e);
                                }
                                logger.info("==========[快钱API充值渠道]结果通知，修改本地资金流水记录END=====================================");

                            }
                        }
                );
                //银行名称
//				String issuer = respXml.substring(respXml.indexOf("<issuer>")+8,respXml.indexOf("</issuer>"));
                //交易时间
                String transTime = respXml.substring(respXml.indexOf("<transTime>") + 11, respXml.indexOf("</transTime>"));
//				String tradeTime = formatPayTime(transTime);
//				
//				Map<String,String> payInfoMap = new HashMap<String,String>();
//				payInfoMap.put("time", tradeTime);
//				payInfoMap.put("bank", issuer);
//				payInfoMap.put("money", amount);

                return null;
            } else if ("C0".equals(responseCode) && "68".equals(responseCode)) {
                String resp = "{\"code\": " + responseCode + ",\"msg\": \"" + responseTextMessage + "\", \"msgType\": 0,\"errparam\": \"\",\"data\": \"\"}";
                logger.info("[快钱API充值渠道]》发卡银行正在处理交易请求，稍后MAS系统将发送交易处理结果通知消息给商户端(payNotifyResult)!responseCode={},resp={}", responseCode, resp);
                Map<String, String> respMap = new HashMap<String, String>();
                respMap.put("code", responseCode);
                respMap.put("msg", responseTextMessage);
                //发起查询
                paraMap.put("externalRefNumber", externalRefNumber);
                qryKQuserPayOrder(paraMap);
                return respMap;
            }

            //如果PUR/TR2应答码为“C0”或“68”，商户端不能按交易失败处理，需要主动到快钱查询该交易的最终结果
            //•如果应答码为“68”－表示快钱未收到银行端的结果、最终交易结果未知;
            //•如果应答码为“C0”－表示快钱内部处理中（未完成）、最终交易结果未知。
            //发起异步“查询交易流水”接口,查询10次

//			request.setAttribute("payId", externalRefNumber);
            paraMap.put("externalRefNumber", externalRefNumber);
            qryKQuserPayOrder(paraMap);

            Map<String, String> respMap = new HashMap<String, String>();
            respMap.put("code", responseCode);
            respMap.put("msg", responseTextMessage);

            return respMap;


        } catch (Exception e) {
            logger.error("[快钱API充值渠道]》支付请求异常.e={}", e);
            e.printStackTrace();
        }
        logger.error("系统繁忙,请稍后再试!");
        throw new LTException(LTResponseCode.ER400);
    }

    // 同步通知支付时间格式化 14位 20160225134849
    public String formatPayTime(String payTime) {

        String time = DateTools.parseToDefaultDateTimeString(new Date());

        try {
            if (StringUtils.isEmpty(payTime) || payTime.length() != 14) {

                return time;
            }

            String payTimeVo = payTime.substring(0, 4) + "-" + payTime.substring(4, 6) + "-" + payTime.substring(6, 8)
                    + " " + payTime.substring(8, 10) + ":" + payTime.substring(10, 12) + ":"
                    + payTime.substring(10, 12);

            return payTimeVo;
        } catch (Exception e) {
            logger.error("同步通知支付时间格式化 14位.异常e={}", e);
            e.printStackTrace();
        }

        return time;
    }

    @Override
    public Map<String, Object> qryKQuserPayOrder(Map<String, Object> paraMap) throws LTException {

        String payIdStr = StringTools.formatStr(paraMap.get("externalRefNumber"), "-1");//本地系统订单号
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        logger.info("后台发起快钱API充值流水查询.payId={}", payIdStr);
        if (StringUtils.isEmpty(payIdStr)) {
            throw new LTException(LTResponseCode.FU00003);
        }

        final String payId = payIdStr;
        //消息规范：信息字段说明
        String version = "1.0";//接口版本号
        final String externalRefNumber = payId;//外部跟踪编号
//				String refNumber = "";//系统参考号/外部检索参考号和检索参考号，必须填写一个。
        String txnType = "PUR";//交易类型编码
        final String merchantId = sysCfgRedis.get("kuaiqian_api_merchant_id");//商户编号
        if (StringUtils.isEmpty(merchantId)) {
            return null;
        }
        String settleMerchantId = "";//结算商户编号
        String terminalId = sysCfgRedis.get("kuaiqian_api_terminal_id");//终端号

        StringBuffer params = new StringBuffer();
        params.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        params.append("<MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\">");
        params.append("<version>" + version + "</version>");
        params.append("<QryTxnMsgContent>");
        params.append("<externalRefNumber>" + externalRefNumber + "</externalRefNumber>");
//				if(StringUtils.isNotEmpty(refNumber)){
//					params.append("<refNumber>"+refNumber+"</refNumber>");
//				}
        params.append("<txnType>" + txnType + "</txnType>");
        params.append("<merchantId>" + merchantId + "</merchantId>");
        if (StringUtils.isNotEmpty(settleMerchantId)) {
            params.append("<settleMerchantId>" + settleMerchantId + "</settleMerchantId>");
        }
        params.append("<terminalId>" + terminalId + "</terminalId>");
        params.append("</QryTxnMsgContent>");
        params.append("</MasMessage>");

        //发起订单查询请求
        String queryOrderTxnUrl = sysCfgRedis.get("kuaiqian_api_query_order_txn_url");
        logger.info("发起订单查询请求。params={},queryOrderTxnUrl={}.", params, queryOrderTxnUrl);

        try {
            //TR2接收的数据
            String respXml = Post.sendPost(queryOrderTxnUrl, params.toString());

            String respXmlCut = respXml.replace("<?xml version='1.0' encoding='UTF-8' standalone='yes'?><MasMessage xmlns='http://www.99bill.com/mas_cnp_merchant_interface'><version>1.0</version>", "");
            String respXmlCut2 = respXmlCut.replace("</MasMessage>", "");
            logger.info("[快钱API]发起订单查询请求,返回信息：respXmlCut2 ={}", respXmlCut2);

            //返回错误码
            String errorCode = "";
            if (respXml.contains("errorCode")) {
                errorCode = respXml.substring(respXml.indexOf("<errorCode>") + 11, respXml.indexOf("</errorCode>"));
            }

            //返回返回提示信息
            String errorMessage = "";
            if (respXml.contains("errorMessage")) {
                errorMessage = respXml.substring(respXml.indexOf("<errorMessage>") + 14, respXml.indexOf("</errorMessage>"));
            }
            logger.info("errorCode={},errorMessage={}", errorCode, errorMessage);
            if (StringUtils.isNotEmpty(errorCode)) {
                throw new LTException(LTResponseCode.FUY00007);
            }


            //如果条件对应的交易在MAS系统中不存在，则ErrorMsgContent节点存在，而TxnMsgContent节点不存在
            if (!respXml.contains("TxnMsgContent")) {
                //说明该[外部跟踪编号]在快钱平台不存在
                logger.warn("本系统充值流水订单号 externalRefNumber={},errorCode={},errorMessage={}",
                        externalRefNumber, errorCode, errorMessage);
                throw new LTException(LTResponseCode.FUY00007);
            }

            //交易状态:‘S’－交易成功  ‘F’－交易失败  ‘P’－交易挂起
            String txnStatus = "";
            if (respXml.contains("txnStatus")) {
                txnStatus = respXml.substring(respXml.indexOf("<txnStatus>") + 11, respXml.indexOf("</txnStatus>"));

            }
            //金额
            String amount = "0";
            if (respXml.contains("amount")) {
                amount = respXml.substring(respXml.indexOf("<amount>") + 8, respXml.indexOf("</amount>"));
            }

            final Double amt = StringTools.isDouble(amount) ? Double.valueOf(amount) : 0.0 ;

            //商户端交易时间
            String entryTime = "";
            if (respXml.contains("entryTime")) {
                entryTime = respXml.substring(respXml.indexOf("<entryTime>") + 11, respXml.indexOf("</entryTime>"));
            }

            //客户号
            String customerId = "";
            if (respXml.contains("customerId")) {
                customerId = respXml.substring(respXml.indexOf("<customerId>") + 12, respXml.indexOf("</customerId>"));
            }

            final String userId = customerId;
            //交易传输时间
            String transTime = "";
            if (respXml.contains("transTime")) {
                transTime = respXml.substring(respXml.indexOf("<transTime>") + 11, respXml.indexOf("</transTime>"));
            }

            //撤销标志
            String voidFlag = "";
            if (respXml.contains("voidFlag")) {
                voidFlag = respXml.substring(respXml.indexOf("<voidFlag>") + 10, respXml.indexOf("</voidFlag>"));
            }

            //系统参考号
            String reNumber = "";
            if (respXml.contains("refNumber")) {
                reNumber = respXml.substring(respXml.indexOf("<refNumber>") + 11, respXml.indexOf("</refNumber>"));
            }

            final String refNumber = reNumber;

            //应答码
            String responseCode = "";
            if (respXml.contains("responseCode")) {
                responseCode = respXml.substring(respXml.indexOf("<responseCode>") + 14, respXml.indexOf("</responseCode>"));
            }

            //应答码文本消息
            String responseTextMessage = "";
            if (respXml.contains("responseTextMessage")) {
                responseTextMessage = respXml.substring(respXml.indexOf("<responseTextMessage>") + 21, respXml.indexOf("</responseTextMessage>"));
            }


            //卡组织编号
            String cardOrg = "";
            if (respXml.contains("cardOrg")) {
                cardOrg = respXml.substring(respXml.indexOf("<cardOrg>") + 9, respXml.indexOf("</cardOrg>"));
            }

            //发卡银行名称
            String issuer = "";
            if (respXml.contains("issuer")) {
                issuer = respXml.substring(respXml.indexOf("<issuer>") + 8, respXml.indexOf("</issuer>"));
            }

            //缩略卡号
            String storableCardNo = "";
            if (respXml.contains("storableCardNo")) {
                storableCardNo = respXml.substring(respXml.indexOf("<storableCardNo>") + 16, respXml.indexOf("</storableCardNo>"));
            }

            final String storablePan = storableCardNo;
            //授权码
            String authorizationCode = "";
            if (respXml.contains("authorizationCode")) {
                authorizationCode = respXml.substring(respXml.indexOf("<authorizationCode>") + 19, respXml.indexOf("</authorizationCode>"));
            }

            logger.info("交易状态 txnStatus={},金额 amount={},商户端交易时间 entryTime={},客户号 customerId={},交易传输时间transTime={},"
                            + "撤销标志 voidFlag={},系统参考号refNumber={},应答码responseCode={},应答码文本消息 responseTextMessage={},卡组织编号 cardOrg={},发卡银行名称issuer={},"
                            + "缩略卡号 storableCardNo={},授权码authorizationCode={}", txnStatus, amount, entryTime, customerId, transTime, voidFlag, refNumber, responseCode, responseTextMessage,
                    cardOrg, issuer, storableCardNo, authorizationCode);

            //交易状态:‘S’－交易成功  ‘F’－交易失败  ‘P’－交易挂起
            if ("S".equals(txnStatus)) {
                //同一个订单号放在一个单线程队列中，防止重复操作及通知
                PayApproachThreadMap.getApproachTread(customerId).execute(

                        new Runnable() {

                            @Override
                            public void run() {
                                //=====更新鉴权记录到本地系统，若更新失败，则插入鉴权记录(本系统采用消费鉴权)
                                try {

                                    FundPayAuthFlow financyPayAuthFlow = new FundPayAuthFlow();
                                    financyPayAuthFlow.setCustomerId(userId);
                                    financyPayAuthFlow.setStorablePan(storablePan);
                                    financyPayAuthFlow.setMerchantId(merchantId);

                                    Integer rows = fundAccountService.updateFundPayAuthFlow(financyPayAuthFlow);
                                    logger.info("rows={}", rows);
                                    if (rows == null || rows == 0) {
                                        fundAccountService.addFinancyPayAuthFlow(financyPayAuthFlow);
                                    }

                                } catch (Exception e) {
                                    logger.error("更新鉴权记录到本地系统.异常e={}", e);
                                    e.printStackTrace();
                                }

                                logger.info("===========[快钱API充值渠道]通知，修改本地资金流水记录START=====================================");
                                try {
                                    //这一段必须要在这里做，放在外面，会因为并发通知问题导致资金更新有误
                                    //=====[start]=============================================================
                                    fundAccountService.doUserKQrecharge(payId, amt);

                                } catch (Exception e) {
                                    logger.error("[快钱API充值渠道]：充值成功，内部订单处理失败,内部订单编号{}", refNumber);
                                    logger.error("[快钱API充值渠道]：错误堆栈,{}", e);
                                }
                                logger.info("==========[快钱API充值渠道]结果通知，修改本地资金流水记录END=====================================");

                            }
                        }
                );

                return null;
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            //交易状态:‘S’－交易成功  ‘F’－交易失败  ‘P’－交易挂起
            if ("F".equals(txnStatus)) {

                String failReason = "流水号:" + externalRefNumber + ",txnStatus:" + txnStatus + ",交易失败!Reason:" + responseTextMessage;
                paramMap.put("payId", externalRefNumber);
                paramMap.put("failReason", failReason);

                fundAccountService.setfailRechargeIo(externalRefNumber, failReason);
                return null;
            }

            //交易状态:‘S’－交易成功  ‘F’－交易失败  ‘P’－交易挂起
            if ("P".equals(txnStatus)) {

                String failReason = "流水号:" + externalRefNumber + ",txnStatus:" + txnStatus + ",交易挂起(请人工处理,登陆快钱后台核实(https://www.99bill.com))!Reason:" + responseTextMessage;
                paramMap.put("payId", externalRefNumber);
                paramMap.put("failReason", failReason);

                fundAccountService.setfailRechargeIo(externalRefNumber, failReason);
                return null;
            }
            //R－交易冲正更新订单的支付结果为“交易失败”
            if ("R".equals(txnStatus)) {

                String failReason = "流水号:" + externalRefNumber + ",txnStatus:" + txnStatus + ",交易冲正(交易失败)!Reason:" + responseTextMessage;
                paramMap.put("payId", externalRefNumber);
                paramMap.put("failReason", failReason);
                return paramMap;
            }


        } catch (Exception e) {
            logger.error("发起订单查询请求,数据处理异常。params={},e={}.", params, e);
            e.printStackTrace();
        }

        throw new LTException(LTResponseCode.ER400);

    }

    @Override
    public void reviceKQResponse(Map<String, Object> paraMap) throws LTException {

        final String externalRefNumber = StringTools.formatStr(paraMap.get("externalRefNumber"), "");
        // 传回的金额为人民币
        String amount = StringTools.formatStr(paraMap.get("amount"), "-1");
        final String storableCardNo = StringTools.formatStr("storableCardNo", "");

        if (storableCardNo.equals("") || externalRefNumber.equals("") || Double.valueOf(amount) == -1 || !StringTools.isNumberic(amount, false, true, true)) {
            throw new LTException(LTResponseCode.FU00003);
        }

        
        final Double amt = Double.valueOf(amount) ;

        final FundIoCashRecharge cashRecharge = fundAccountService.qryFundIoCashRechargeByPayId(externalRefNumber);
        if (cashRecharge == null) {
            logger.info("[快钱API充值渠道]通知：FinancyIo记录不存在.externalRefNumber={}", externalRefNumber);
            // 如果没有创建本地资金流水，注意查看银联跳转的效果
            throw new LTException(LTResponseCode.FUY00006);
        }

        final String userId = String.valueOf(cashRecharge.getUserId());

        logger.info("[快钱API充值渠道]tr3验证通过,通知支付结果,更新本地快钱支付订单---------------------------------------------------------------------------");
        // 同一个订单号放在一个单线程队列中，防止重复操作及通知
        PayApproachThreadMap.getApproachTread(userId).execute(
                new Runnable() {
                    @Override
                    public void run() {
                        // =====更新鉴权记录到本地系统，若更新失败，则插入鉴权记录(本系统采用消费鉴权)
                        try {
                            FundPayAuthFlow financyPayAuthFlow = new FundPayAuthFlow();
                            financyPayAuthFlow.setCustomerId(userId);
                            financyPayAuthFlow.setStorablePan(storableCardNo);

                            Integer rows = fundAccountService.updateFundPayAuthFlow(financyPayAuthFlow);
                            if (rows == null || rows == 0) {
                                fundAccountService.addFinancyPayAuthFlow(financyPayAuthFlow);
                            }

                        } catch (Exception e) {
                            logger.error("更新鉴权记录到本地系统.异常e={}", e);
                            e.printStackTrace();
                        }

                        logger.info("===========[快钱API充值渠道]通知，修改本地资金流水记录START=====================================");

                        // 这一段必须要在这里做，放在外面，会因为并发问题导致资金更新有误
                        // =====[start]=============================================================

                        if (cashRecharge.getStatus() == FundIoRechargeEnum.SUCCESS.getValue().intValue()) {
                            logger.warn("[快钱API充值渠道]：该订单已经处理.检索参考号 refNumber:{}", externalRefNumber);
                            return;
                        }
                        // =====[end]=============================================================
                        try {
                            logger.info("[快钱API充值渠道]：更新用户账户资金金额");
                            // 同步、异步通知第三方订单号，没有相对应的字段
                            fundAccountService.doUserKQrecharge(externalRefNumber, amt);

                        } catch (Exception e) {
                            logger.error("[快钱API充值渠道]：充值成功，内部订单处理失败,内部订单编号{}", externalRefNumber);
                            logger.error("[快钱API充值渠道]：错误堆栈,{}", e);
                        }
                        logger.info("==========[快钱API充值渠道]结果通知，修改本地资金流水记录END=====================================");

                    }
                });
    }

    @Override
    public List<SysConfig> rechargeConfigList() {
        return sysConfigDao.selectSysConfigs();
    }
}
