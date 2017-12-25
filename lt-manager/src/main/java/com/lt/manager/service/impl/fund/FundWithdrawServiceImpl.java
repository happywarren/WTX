package com.lt.manager.service.impl.fund;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.lt.util.utils.*;
import com.lt.util.utils.jiupai.DateUtils;
import com.lt.util.utils.jiupai.HiDesUtils;
import com.lt.util.utils.jiupai.MerchantUtil;
import com.lt.util.utils.jiupai.RSASignUtil;
import com.pay.merchant.MerchantClient;
import com.pay.vo.ReceivePayQueryRequest;
import com.pay.vo.ReceivePayQueryResponse;
import com.pay.vo.ReceivePayRequest;
import com.pay.vo.ReceivePayResponse;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundIoWithdrawalEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.fund.FundTransferEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.enums.sys.SysThreadLockEnum;
import com.lt.manager.bean.fund.FundIoCashWithdrawalVO;
import com.lt.manager.dao.fund.FundIoCashWithdrawalDao;
import com.lt.manager.dao.fund.FundWithdrawDao;
import com.lt.manager.service.fund.IFundWithdrawService;
import com.lt.manager.service.sys.ISysThreadLockService;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundOptCode;
import com.lt.model.fund.FundTransferDetail;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.UserContant;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.alipay.alipayTranfer.httpClient.HttpProtocolHandler;
import com.lt.util.utils.alipay.alipayTranfer.httpClient.HttpRequest;
import com.lt.util.utils.alipay.alipayTranfer.httpClient.HttpResponse;
import com.lt.util.utils.alipay.alipayTranfer.httpClient.HttpResultType;
import com.lt.util.utils.alipay.alipayTranfer.sign.RSA;
import com.lt.util.utils.crypt.MD5Util;
import com.lt.util.utils.dinpay.DinpayHttpTools;
import com.lt.util.utils.redis.RedisInfoOperate;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现接口类
 *
 * @author jingwb
 */
@Service
public class FundWithdrawServiceImpl implements IFundWithdrawService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FundWithdrawDao fundwithDrawDao;
    @Autowired
    private FundIoCashWithdrawalDao fundIoCashWithdrawalDao;
    @Autowired
    private IFundAccountApiService fundAccountService;
    @Autowired
    private IUserApiBussinessService userApiBussinessService;
    @Autowired
    private IFundAccountApiService fundAccountApiService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ISysThreadLockService sysThreadLockService;

    BoundHashOperations<String, String, String> sysCfgRedis;

    @Override
    public void auditWithdraw(String ioIds, int operate, String modifyId, String remark, IFundOptCode codeEnum)
            throws LTException {
        // TODO Auto-generated method stub
        logger.debug("执行提现申请审核操作，申请的流水ID为：{}，操作为：{}，修改用户为：{}", ioIds, operate, modifyId);
//		FundCashOptCodeEnum codeEnum = getDrawalEnumByThirdCode(thirdOptCode);
        try {

            if (codeEnum == null) {
                throw new LTException(LTResponseCode.FUJ0001);
            }

            if (ioIds.contains(",")) {
                for (String id : ioIds.split(",")) {
                    fundAccountService.fundDoAudit(Long.parseLong(id), Integer.parseInt(modifyId), operate, codeEnum,
                            remark);
                }
            } else {
                fundAccountService.fundDoAudit(Long.parseLong(ioIds), Integer.parseInt(modifyId), operate, codeEnum,
                        remark);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(e.getMessage());
        }
    }

    @Override
    public void alipayToBank(String ioIds, String modifyId) throws LTException {
        Map<String, String> paramMap = new HashMap<String, String>();

        sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String notify_url = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_NOTIFY_URL);
        String sign_txt = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_SIGN);
        String company_name = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_COMPANY);
        String remark = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_REMARK);
        String privateKey = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_PRIVATEKEY);
        String publicKey = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_PUBLICKEY);
        String gateWay = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_GATEWAY);
        String partner = sysCfgRedis.get(UserContant.ALIPAY_TRAFER_PARTNER);
        String input_charset = sysCfgRedis.get(UserContant.ALIPAY_INPUT_CHARSET);

        String company_name_encoded = "";
        String remark_encoded = "";
        try {
            company_name_encoded = URLEncoder.encode(company_name, "UTF-8");
            remark_encoded = URLEncoder.encode(remark, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            logger.debug("支付宝接口转码操作异常：{}", e);
            e.printStackTrace();
            throw new LTException(LTResponseCode.FU00000);
        }

        // 存储成功的转账ID
        StringBuilder successId = new StringBuilder("");
        // 存储失败的转账id
        StringBuilder failId = new StringBuilder("");

        List<FundTransferDetail> detailList = new ArrayList<FundTransferDetail>();

        StringBuilder ids = new StringBuilder("");
        if (ioIds.contains(",")) {
            for (String id : ioIds.split(",")) {
                if (id.equals("")) {
                    continue;
                } else {
                    ids.append(",'" + id + "'");
                }
            }

            ids.replace(0, 1, "");
        } else {
            ids = new StringBuilder(ioIds);
        }
        logger.debug("将要处理的支付宝转账单号为：{}", ids.toString());
        paramMap.put("ioId", ids.toString());
        paramMap.put("status", FundIoWithdrawalEnum.WAIT.getValue().toString());

        List<FundIoCashWithdrawalVO> drawList = fundwithDrawDao.alipayTranferToBank(paramMap);
        logger.debug("实际操作转账数量为：{}", drawList.size());

        String sign = "";
        String url = "";
        FundTransferDetail fundTransferDetail = null;
        for (FundIoCashWithdrawalVO cashWithdrawalVO : drawList) {
            String out_biz_no = cashWithdrawalVO.getPayId();
            String amount = DoubleUtils.sub(cashWithdrawalVO.getAmount(), cashWithdrawalVO.getTax()) + "";// 转账金额

            sign = sign_txt.replace("${money}", amount + "").replace("${notify_url}", notify_url)
                    .replace("${innerOrderId}", out_biz_no).replace("${bank_card}", cashWithdrawalVO.getBankCardNum())
                    .replace("${bank_name}", cashWithdrawalVO.getBankName())
                    .replace("${user_name}", cashWithdrawalVO.getUserName()).replace("${company_name}", company_name)
                    .replace("${remark}", remark).replace("${payer_account}", partner).replace("${partner}", partner); // 原配置写死
            try {
                sign = RSA.sign(sign, privateKey, "UTF-8");
                url = gateWay.replace("${sign}", URLEncoder.encode(sign, "UTF-8")).replace("${money}", amount + "")
                        .replace("${notify_url}", notify_url).replace("${innerOrderId}", out_biz_no)
                        .replace("${bank_card}", cashWithdrawalVO.getBankCardNum())
                        .replace("${bank_name}", URLEncoder.encode(cashWithdrawalVO.getBankName(), "UTF-8"))
                        .replace("${user_name}", URLEncoder.encode(cashWithdrawalVO.getUserName(), "UTF-8"))
                        .replace("${company_name_encoded}", company_name_encoded)
                        .replace("${remark_encoded}", remark_encoded).replace("${payer_account}", partner)
                        .replace("${partner}", partner); // 原配置写死

                HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
                HttpRequest httpRequest = new HttpRequest(HttpResultType.BYTES);
                // 设置编码集
                httpRequest.setCharset(input_charset);
                httpRequest.setUrl(url);

                HttpResponse httpResponse = httpProtocolHandler.execute(httpRequest, "", "");
                logger.info("======================[]", httpResponse);
                logger.info("======================[]", JSONUtils.toJSONString(httpResponse));
                String[] result = parseResponse(httpResponse.getStringResult(), publicKey);

                if (result[0].equals("T")) {
                    // 转账成功处理逻辑,状态修改为转账中，
                    logger.info("===========支付宝转账接口调用成功============");
                    cashWithdrawalVO.setPayId(result[1]);
                    cashWithdrawalVO.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());
                    cashWithdrawalVO.setModifyUserId(modifyId);
                    fundTransferDetail = new FundTransferDetail(cashWithdrawalVO.getPayId(),
                            cashWithdrawalVO.getUserId(), Long.valueOf(cashWithdrawalVO.getFlowId().toString()),
                            cashWithdrawalVO.getUserName(), cashWithdrawalVO.getBankCardNum(),
                            cashWithdrawalVO.getBankName(), cashWithdrawalVO.getAmount(), Integer.parseInt(modifyId),
                            cashWithdrawalVO.getRemark(), FundIoWithdrawalEnum.PROCESS.getValue());

                    if (successId.toString().contains(",")) {
                        successId.append(",'" + cashWithdrawalVO.getFlowId() + "'");
                    } else {
                        successId.append("'" + cashWithdrawalVO.getFlowId() + "'");
                    }
                } else if (result[0].equals("F")) {
                    // 转账失败处理
                    logger.info("============支付宝转账接口调用失败============");
                    cashWithdrawalVO.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                    cashWithdrawalVO.setModifyUserId(modifyId);
                    fundTransferDetail = new FundTransferDetail(null, cashWithdrawalVO.getUserId(),
                            Long.valueOf(cashWithdrawalVO.getFlowId().toString()), cashWithdrawalVO.getUserName(),
                            cashWithdrawalVO.getBankCardNum(), cashWithdrawalVO.getBankName(),
                            cashWithdrawalVO.getAmount(), Integer.parseInt(modifyId), result[1],
                            FundIoWithdrawalEnum.FAILURE.getValue());

                    if (failId.toString().contains(",")) {
                        failId.append(",'" + cashWithdrawalVO.getFlowId() + "'");
                    } else {
                        failId.append("'" + cashWithdrawalVO.getFlowId() + "'");
                    }

                    FundIoCashWithdrawal fio = new FundIoCashWithdrawal();
                    fio.setUserId(cashWithdrawalVO.getUserId());
                    fio.setAmount(Double.valueOf(amount));
                    fundAccountService.withdrawFail(fio);
                }
                detailList.add(fundTransferDetail);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.debug("订单发送请求发生异常，支付订单编号为payId：{}", out_biz_no);
                e.printStackTrace();
            }
        }

        try {
            logger.debug("批量执行处理sql");
            paramMap = new HashMap<String, String>();
            paramMap.put("modifyId", modifyId);
            paramMap.put("status", FundIoWithdrawalEnum.FAILURE.getValue().toString());
            paramMap.put("ids", failId.toString());
            logger.debug("执行修改转账失败数据：{}", JSONUtils.toJSONString(paramMap));
            fundwithDrawDao.auditWithdraw(paramMap);
            paramMap.put("status", FundIoWithdrawalEnum.PROCESS.getValue().toString());
            paramMap.put("ids", successId.toString());
            logger.debug("执行修改转账成功数据：{}", JSONUtils.toJSONString(paramMap));
            fundwithDrawDao.auditWithdraw(paramMap);
            logger.debug("执行插入转账详情：{}", JSONUtils.toJSONString(detailList));
            fundIoCashWithdrawalDao.insertFundTransferDetails(detailList);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.debug("支付宝转账修改转账资金及转账记录异常e：{}", e);
            throw new LTException(LTResponseCode.FU00000);
        }
    }

    /**
     * @param response
     * @param publicKey
     * @return
     * @throws @author yuanxin
     * @return: String[] [0]:状态 T 成功 F:失败 [1]:请求成功返回支付id，请求失败返回原因
     * @Date 2017年1月9日 下午2:13:34
     */
    public String[] parseResponse(String response, String publicKey) {
        logger.info("============PARSE RESPONSE============");
        logger.info(response);
        String[] result = new String[2];
        try {
            Document root = DocumentHelper.parseText(response);
            String req_success = root.selectSingleNode("//alipay/is_success").getText();
            if (!"T".equals(req_success)) {
                result[0] = "F";
                result[1] = root.selectSingleNode("//alipay/error").getText();
                return result;
            }
            String exec_success = root.selectSingleNode("//alipay/response/alipay/result_code").getText();
            if ("FAIL".equals(exec_success)) {
                result[0] = "F";
                result[1] = root.selectSingleNode("//alipay/response/alipay/error_msg").getText();// 请求成功，
                // 但转账失败
                return result;
            }
            String sign = root.selectSingleNode("//alipay/sign").getText();
            @SuppressWarnings("unchecked")
            List<Node> list = root.selectNodes("//alipay/response/alipay/*");
            String to_sign = "";
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                Node node = (Node) iterator.next();
                if (!to_sign.isEmpty()) {
                    to_sign += "&";
                }
                to_sign += node.getName() + "=" + node.getText();
            }
            logger.info(" alipay trans result is {}", RSA.verify(to_sign, sign, publicKey, "UTF-8"));
            result[0] = "T";
            result[1] = root.selectSingleNode("//alipay/response/alipay/order_id").getText();// 转账成功，返回订单号
            return result;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FundIoCashWithdrawal getFundCashDrawInfo(FundIoCashWithdrawal fundIoCashWithdrawal) throws LTException {
        try {
            return fundwithDrawDao.qryFundCashWithdsById(fundIoCashWithdrawal);
        } catch (Exception e) {
            // TODO: handle exception
            logger.debug("查询提现单详情方法报错e:{}", e);
            throw new LTException(LTResponseCode.FUY00002);
        }
    }

    @Override
    public void alipayTransferResiveResult(FundIoCashWithdrawal fundIoCashWithdrawal, boolean flag) throws LTException {
        Map<String, String> paramMap = new HashMap<String, String>();
        try {
            logger.debug("修改提现表数据");
            paramMap.put("modifyId", fundIoCashWithdrawal.getModifyUserId().toString());
            paramMap.put("doneDate", "1");
            paramMap.put("ids", fundIoCashWithdrawal.getId().toString());

            if (flag) {
                paramMap.put("status", FundIoWithdrawalEnum.SUCCEED.getValue().toString());
                fundwithDrawDao.auditWithdraw(paramMap);

                paramMap.put("status", FundTransferEnum.SUCCEED.getValue().toString());
                fundwithDrawDao.updateTransferDetail(paramMap);

                logger.debug("支付宝成功通知，通知扣除冻结金额");
                fundAccountService.withdrawSuccess(fundIoCashWithdrawal, FundThirdOptCodeEnum.ZFBTX.getThirdLevelCode());
            } else {
                paramMap.put("status", FundIoWithdrawalEnum.FAILURE.getValue().toString());
                fundwithDrawDao.auditWithdraw(paramMap);

                paramMap.put("status", FundTransferEnum.SUCCEED.getValue().toString());
                fundwithDrawDao.updateTransferDetail(paramMap);

                logger.debug("支付宝失败通知，通知解冻金额，返回用户资金账户");
                fundAccountService.withdrawFail(fundIoCashWithdrawal);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.debug("接收支付宝结果处理提现记录时报错e:{}", e);
            throw new LTException(LTResponseCode.FUY00002);
        }

    }

    /**
     * 根据三级现金流编码查找对应的枚举类
     *
     * @param thirdCode
     * @return
     * @throws @author yuanxin
     * @return: FundIoWithdrawalEnum
     * @Date 2017年1月10日 上午11:46:12
     */
    public FundCashOptCodeEnum getDrawalEnumByThirdCode(String thirdCode) {

        for (FundCashOptCodeEnum c : FundCashOptCodeEnum.values()) {
            if (c.getCode().equals(thirdCode)) {
                return c;
            }
        }

        return null;
    }

    @Override
    @Transactional
    public void withdrawForUnspay(String[] ioArr, Integer transferUserId) throws Exception {
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

        // 银生宝key
        String unspay_key = sysCfgRedis.get("unspay_key");
        // 银生宝商户号
        String unspay_merchantNo = sysCfgRedis.get("unspay_merchantNo");
        // 银生宝接口请求地址
        String unspay_url = sysCfgRedis.get("unspay_withdraw_url");
        // 银生宝回掉地址
        String unspay_callback_url = sysCfgRedis.get("unspay_withdraw_callback_url");

        // 银生宝转账配置参数非空检查
        if (StringTools.isEmpty(unspay_key) || StringTools.isEmpty(unspay_url)
                || StringTools.isEmpty(unspay_callback_url) || StringTools.isEmpty(unspay_merchantNo)) {
            logger.error("银生宝转账信息配置错误：unspay_key:" + unspay_key + ", unspay_url=" + unspay_url
                    + ", unspay_callback_url=" + unspay_callback_url);
            throw new LTException("银生宝转账信息配置错误");
        }
        List<FundIoCashWithdrawal> fioList = new ArrayList<>();// 提现流水集合
        List<FundTransferDetail> ftdList = new ArrayList<>();// 提现明细集合
        // 遍历提现流水
        for (String id : ioArr) {
            FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(Long.valueOf(id));
            FundTransferDetail ftd = null;
            if (fio == null || fio.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()) {
                logger.info("记录不存在或者对应记录的为非转账状态ioId:{}", id);
                if (fio != null) {
                    logger.info("提现记录状态为{}", fio.getStatus());
                }
                continue;
            }

            // 查询用户信息
            UserBankInfo bankInfo = new UserBankInfo();
            bankInfo.setUserId(fio.getUserId());
            UserBussinessInfo ui = userApiBussinessService.getUserBankList(bankInfo);

            // 转账金额
            Double amount = DoubleUtils.sub(fio.getRmbAmt(), fio.getRmbFactTax());
            // 传给银生宝参数信息
            Map<String, String> param = new HashMap<String, String>();
            // 参数组装
            param.put("accountId", unspay_merchantNo);
            param.put("name", ui.getUserName());
            param.put("cardNo", ui.getBankCardNum());
            String code = CalendarTools.formatDateTime(new Date(), CalendarTools.DATETIMEFORMAT) + "_" + id;// 订单唯一标识
            param.put("orderId", code);
            param.put("purpose", "withdraw");
            param.put("amount", amount.toString());
            param.put("responseUrl", unspay_callback_url);

            // 生成签名
            StringBuffer macStr = new StringBuffer("accountId=" + unspay_merchantNo);
            macStr.append("&").append("name=" + ui.getUserName());
            macStr.append("&").append("cardNo=" + ui.getBankCardNum());
            macStr.append("&").append("orderId=" + code);
            macStr.append("&").append("purpose=withdraw");
            macStr.append("&").append("amount=" + amount.toString());
            macStr.append("&").append("responseUrl=" + unspay_callback_url);
            macStr.append("&").append("key=" + unspay_key);
            logger.info("signStr={}", macStr);
            String mac = MD5Util.md5(macStr.toString());
            logger.info("mac={}", mac);
            param.put("mac", mac);

            logger.info("执行银生宝提现请求: " + unspay_url);
            JSONObject json = HttpTools.doPost(unspay_url, param);
            param.clear();
            if (json == null) {
                logger.error("银生宝转账失败，没有响应");
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 beg*/
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(code);
                fio.setRemark("银生宝通讯失败，请等待后台自动处理");
                ftd = new FundTransferDetail(code, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "银生宝转账", FundTransferEnum.UNTREATED.getValue(), 2);
                //银生宝通讯失败有已处理改为待处理
                ftd.setRmbAmt(amount);
                ftd.setTransferUserId(transferUserId);
                fioList.add(fio);
                ftdList.add(ftd);
                continue;
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 end*/
//				
//				throw new RuntimeException("银生宝转账失败，没有响应");
            }
            String strResult = json.toString();
            // 解析返回结果
            Map<String, String> rmap = JSONObject.parseObject(strResult, Map.class);
            logger.info("resCode:" + rmap.get("result_code") + ", resMessage:" + rmap.get("result_msg"));

            if ("0000".equals(rmap.get("result_code"))) {// 请求银生宝成功（不代表支付成功，要看回掉接口）
                logger.info("============转账处理中============");
                fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());
                fio.setModifyDate(new Date());
//				fio.setModifyUserId(0);
                fio.setPayId(code);
                fio.setRemark("用户提现已提交到银生宝，等待处理中");
                ftd = new FundTransferDetail(code, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "银生宝转账", FundTransferEnum.UNTREATED.getValue(), 0);
            } else {
                logger.info("============转账失败============");
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(code);
//				fio.setModifyUserId(0);

                fio.setRemark("用户提现，银生宝返回失败");
                ftd = new FundTransferDetail(code, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "银生宝转账", FundTransferEnum.UNTREATED.getValue(), 2);
            }
            fio.setThirdOptCode(FundThirdOptCodeEnum.YSBTX.getThirdLevelCode());
            ftd.setRmbAmt(amount);
            ftd.setTransferUserId(transferUserId);
            fioList.add(fio);
            ftdList.add(ftd);
        }

        if(StringTools.isNotEmpty(fioList)){
            // 批量修改提现流水
            fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
        }
        if(StringTools.isNotEmpty(ftdList)){
            // 批量添加提现明细
            fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
        }


    }

    @Override
    @Transactional
    /**
     * 浙江银生宝转账
     */
    public void withdrawForUnspayZJ(String[] ioArr, Integer transferUserId) throws Exception {
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

        // 银生宝key
        String unspay_key = sysCfgRedis.get("unspay_key");
        // 银生宝商户号
        String unspay_merchantNo = sysCfgRedis.get("unspay_merchantNoZJ");
        // 银生宝接口请求地址
        String unspay_url = sysCfgRedis.get("unspay_withdraw_url");
        // 银生宝回掉地址
        String unspay_callback_url = sysCfgRedis.get("unspay_withdraw_callback_url");

        // 银生宝转账配置参数非空检查
        if (StringTools.isEmpty(unspay_key) || StringTools.isEmpty(unspay_url)
                || StringTools.isEmpty(unspay_callback_url) || StringTools.isEmpty(unspay_merchantNo)) {
            logger.error("银生宝转账信息配置错误：unspay_key:" + unspay_key + ", unspay_url=" + unspay_url
                    + ", unspay_callback_url=" + unspay_callback_url);
            throw new LTException("银生宝转账信息配置错误");
        }
        List<FundIoCashWithdrawal> fioList = new ArrayList<>();// 提现流水集合
        List<FundTransferDetail> ftdList = new ArrayList<>();// 提现明细集合
        // 遍历提现流水
        for (String id : ioArr) {
            FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(Long.valueOf(id));
            FundTransferDetail ftd = null;
            if (fio == null || fio.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()) {
                logger.info("记录不存在或者对应记录的为非转账状态ioId:{}", id);
                if (fio != null) {
                    logger.info("提现记录状态为{}", fio.getStatus());
                }
                continue;
            }

            // 查询用户信息
            UserBankInfo bankInfo = new UserBankInfo();
            bankInfo.setUserId(fio.getUserId());
            UserBussinessInfo ui = userApiBussinessService.getUserBankList(bankInfo);

            // 转账金额
            Double amount = DoubleUtils.sub(fio.getRmbAmt(), fio.getRmbFactTax());
            // 传给银生宝参数信息
            Map<String, String> param = new HashMap<String, String>();
            // 参数组装
            param.put("accountId", unspay_merchantNo);
            param.put("name", ui.getUserName());
            param.put("cardNo", ui.getBankCardNum());
            String code = CalendarTools.formatDateTime(new Date(), CalendarTools.DATETIMEFORMAT) + "_" + id;// 订单唯一标识
            param.put("orderId", code);
            param.put("purpose", "withdraw");
            param.put("amount", amount.toString());
            param.put("responseUrl", unspay_callback_url);

            // 生成签名
            StringBuffer macStr = new StringBuffer("accountId=" + unspay_merchantNo);
            macStr.append("&").append("name=" + ui.getUserName());
            macStr.append("&").append("cardNo=" + ui.getBankCardNum());
            macStr.append("&").append("orderId=" + code);
            macStr.append("&").append("purpose=withdraw");
            macStr.append("&").append("amount=" + amount.toString());
            macStr.append("&").append("responseUrl=" + unspay_callback_url);
            macStr.append("&").append("key=" + unspay_key);
            logger.info("signStr={}", macStr);
            String mac = MD5Util.md5(macStr.toString());
            logger.info("mac={}", mac);
            param.put("mac", mac);

            logger.info("执行银生宝提现请求: " + unspay_url);
            JSONObject json = HttpTools.doPost(unspay_url, param);
            param.clear();

            fio.setUnspayAccountType("ZJ");
            if (json == null) {
                logger.error("银生宝转账失败，没有响应");
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 beg*/
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(code);
                fio.setRemark("银生宝通讯失败，请等待后台自动处理");
                ftd = new FundTransferDetail(code, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "浙江银生宝转账", FundTransferEnum.UNTREATED.getValue(), 2);

                ftd.setRmbAmt(amount);
                ftd.setTransferUserId(transferUserId);
                fioList.add(fio);
                ftdList.add(ftd);
                continue;
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 end*/
//				
//				throw new RuntimeException("银生宝转账失败，没有响应");
            }
            String strResult = json.toString();
            // 解析返回结果
            Map<String, String> rmap = JSONObject.parseObject(strResult, Map.class);
            logger.info("resCode:" + rmap.get("result_code") + ", resMessage:" + rmap.get("result_msg"));

            if ("0000".equals(rmap.get("result_code"))) {// 请求银生宝成功（不代表支付成功，要看回掉接口）
                logger.info("============转账处理中============");
                fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());
                fio.setModifyDate(new Date());
//				fio.setModifyUserId(0);
                fio.setPayId(code);
                fio.setRemark("用户提现已提交到银生宝，等待处理中");
                ftd = new FundTransferDetail(code, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "浙江银生宝转账", FundTransferEnum.UNTREATED.getValue(), 0);
            } else {
                logger.info("============转账失败============");
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(code);
//				fio.setModifyUserId(0);

                fio.setRemark("用户提现，银生宝返回失败");
                ftd = new FundTransferDetail(code, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "浙江银生宝转账", FundTransferEnum.UNTREATED.getValue(), 2);
            }
            fio.setThirdOptCode(FundThirdOptCodeEnum.YSBTX.getThirdLevelCode());
            ftd.setRmbAmt(amount);
            ftd.setTransferUserId(transferUserId);
            fioList.add(fio);
            ftdList.add(ftd);
        }
        if(StringTools.isNotEmpty(fioList)){
            // 批量修改提现流水
            fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
        }
        if(StringTools.isNotEmpty(ftdList)){
            // 批量添加提现明细
            fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
        }
    }


    @Override
    public void callbackForUnspay(Map<String, Object> map) throws Exception {
        String orderId = (String) map.get("orderId");
        String result_code = (String) map.get("result_code");
        String result_msg = (String) map.get("result_msg");
        
        if(StringTools.isEmpty(result_code)){
        	logger.info("银生宝出金结果未知");
        	return;
        }   

        FundIoCashWithdrawal withdrawal = this.fundIoCashWithdrawalDao.selectFundIoCashWithdByPayId(orderId);
        if(withdrawal == null ){
        	logger.info("未查到银生宝返回的订单信息");
        	return;
        }
        if (withdrawal.getStatus() != FundIoWithdrawalEnum.PROCESS.getValue()) {
        	logger.info("当前订单已处理");
        	return;
        }
        
        if ("0000".equals(result_code)) {// 提现成功
        	withdrawal.setThirdOptCode(FundThirdOptCodeEnum.YSBTX.getThirdLevelCode());
            withdrawSuccess(withdrawal, FundThirdOptCodeEnum.YSBTX.getThirdLevelCode());
        } else {// 失败
        	withdrawal.setRemark(result_msg);
            withdrawFail(withdrawal);
        }

    }

    @Override
    public void callbackForQtong(String notifyResultStr) throws Exception {
        MerchantClient merchant = new MerchantClient();
        ReceivePayResponse pnResponse = merchant
                .parseReceivePayNotify(notifyResultStr);
        if(pnResponse == null || StringTools.isEmpty(pnResponse.getTranId())){
            logger.info("钱通提现返回参数异常");
            return;
        }
        logger.info("钱通提现返回参数:{}", JSONTools.toJSON(pnResponse));
        FundIoCashWithdrawal withdrawal = this.fundIoCashWithdrawalDao.selectFundIoCashWithdByPayId(pnResponse.getTranId());
        if(withdrawal == null ){
            logger.info("未查到钱通提现返回的订单信息");
            return;
        }
        if (withdrawal.getStatus() != FundIoWithdrawalEnum.PROCESS.getValue()) {
            logger.info("当前订单已处理");
            return;
        }

        if ("000".equals(pnResponse.getTranList().get(0).getRespCode())) {// 提现成功
            withdrawal.setThirdOptCode(FundThirdOptCodeEnum.QTTX.getThirdLevelCode());
            withdrawSuccess(withdrawal, FundThirdOptCodeEnum.QTTX.getThirdLevelCode());
        } else {// 失败
            withdrawal.setRemark(pnResponse.getTranList().get(0).getRespDesc());
            withdrawFail(withdrawal);
        }
    }

    @Override
    @Transactional
    public void withdrawSuccess(FundIoCashWithdrawal fio, String thirdOptCode) throws Exception {
        logger.info("========提现成功，处理开始=====");
        FundIoCashWithdrawal withdrawal = this.fundIoCashWithdrawalDao.selectFundIoCashWithdByPayIdForUpdate(fio.getPayId());
        
        if(FundIoWithdrawalEnum.SUCCEED.getValue() == withdrawal.getStatus()){
        	logger.info("当前提现已处理!订单号:{}",fio.getPayId());
        	return;
        }
        
        withdrawal.setStatus(FundIoWithdrawalEnum.SUCCEED.getValue());
        withdrawal.setThirdOptCode(thirdOptCode);
        withdrawal.setRemark("提现成功");
        this.fundIoCashWithdrawalDao.updateFundIoCashWithd(withdrawal);
        
        FundTransferDetail transferDetail = fundIoCashWithdrawalDao.selectTransferDetailByIoid(withdrawal.getId());
        transferDetail.setStatus(FundTransferEnum.SUCCEED.getValue());
        transferDetail.setRemark("转账成功");
        transferDetail.setOperateStatu(FundTransferEnum.TREATED.getValue());
        fundIoCashWithdrawalDao.updateTransferDetail(transferDetail);
        
        fundAccountApiService.withdrawSuccess(withdrawal, thirdOptCode);        
        
/*        fio.setStatus(FundIoWithdrawalEnum.SUCCEED.getValue());// 成功
        fio.setThirdOptCode(thirdOptCode);
        fio.setRemark("提现成功");
        fundIoCashWithdrawalDao.updateFundIoCashWithd(fio);
        FundTransferDetail ftd = fundIoCashWithdrawalDao.selectTransferDetailByIoid(fio.getId());
        ftd.setStatus(1);
        ftd.setRemark("转账成功");
        ftd.setOperateStatu(FundTransferEnum.TREATED.getValue());
        fundIoCashWithdrawalDao.updateTransferDetail(ftd);
        fundAccountApiService.withdrawSuccess(fio, thirdOptCode);*/
    }

    @Override
    @Transactional
    public void withdrawFail(FundIoCashWithdrawal fio) throws Exception {
        logger.info("========提现失败，处理开始=====");
        
        FundIoCashWithdrawal withdrawal = this.fundIoCashWithdrawalDao.selectFundIoCashWithdByPayIdForUpdate(fio.getPayId());
        if(FundIoWithdrawalEnum.FAILURE.getValue() == withdrawal.getStatus()){
        	logger.info("当前提现已处理!订单号:{}",fio.getPayId());
        	return;
        }
        
        withdrawal.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
        withdrawal.setRemark(fio.getRemark());
        this.fundIoCashWithdrawalDao.updateFundIoCashWithd(withdrawal);
        
        //转账失败落地人工处理
        FundTransferDetail transferDetail = fundIoCashWithdrawalDao.selectTransferDetailByIoid(withdrawal.getId());
        transferDetail.setStatus(FundTransferEnum.FAILED.getValue());
        transferDetail.setRemark("转账失败");
        transferDetail.setOperateStatu(FundTransferEnum.UNTREATED.getValue());
        fundIoCashWithdrawalDao.updateTransferDetail(transferDetail);        
        
        
/*        fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());// 失败
        fundIoCashWithdrawalDao.updateFundIoCashWithd(fio);
        FundTransferDetail ftd = fundIoCashWithdrawalDao.selectTransferDetailByIoid(fio.getId());
        ftd.setStatus(FundTransferEnum.FAILED.getValue());
        ftd.setRemark("转账失败");
        //转账失败落地人工处理
        ftd.setOperateStatu(FundTransferEnum.UNTREATED.getValue());
        fundIoCashWithdrawalDao.updateTransferDetail(ftd);*/
        //fundAccountApiService.withdrawSuccess(fio, null);
    }

    @Override
    public void queryWithdrawResultForUnspay() {
        try {

            if (!sysThreadLockService.lock(SysThreadLockEnum.UNSPAY_DRAW_QUERY_TASK.getCode())) {
                logger.info("查询银生宝提现执行中...");
                return;
            }

            logger.info("定时查询银生宝提现结果");
            FundIoCashWithdrawal fio = new FundIoCashWithdrawal();
            fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());// 转账中
            fio.setThirdOptCode(FundThirdOptCodeEnum.YSBTX.getThirdLevelCode());// 银生宝提现标志
            List<FundIoCashWithdrawal> fios = fundIoCashWithdrawalDao.selectFundIoCashWithds(fio);
            logger.info("查询未处理的提现订单，length={},financyios={}", fios.size(), JSONObject.toJSONString(fios));
            if (fios == null || fios.size() == 0) {
                return;
            }
            BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
            // 银生宝key
            String unspay_key = sysCfgRedis.get("unspay_key");
            // 银生宝商户号
            String unspay_merchantNo = sysCfgRedis.get("unspay_merchantNo");
            String unspay_merchantNoZJ = sysCfgRedis.get("unspay_merchantNoZJ");
            // 银生宝接口请求地址
            String unspay_url = sysCfgRedis.get("unspay_query_result_url");

            for (FundIoCashWithdrawal fioo : fios) {
                if ("ZJ".equals(fioo.getUnspayAccountType())) {
                    unspay_merchantNo = unspay_merchantNoZJ;
                }
                Map<String, String> param = new HashMap<String, String>();
                param.put("accountId", unspay_merchantNo);
                param.put("orderId", fioo.getPayId());
                // 生成签名
                StringBuffer macStr = new StringBuffer("accountId=" + unspay_merchantNo);
                macStr.append("&").append("orderId=" + fioo.getPayId());
                macStr.append("&").append("key=" + unspay_key);
                logger.info("signStr={}", macStr);
                String mac = MD5Util.md5(macStr.toString());
                logger.info("mac={}", mac);
                param.put("mac", mac);

                logger.info("请求银生宝查询结果接口，unspay_query_result_url={}", unspay_url);
                JSONObject json = HttpTools.doPost(unspay_url, param);
                param.clear();
                if (json == null) {
                    logger.error("银生宝查询失败，此接口没有响应");
                    return;
                }
                String strResult = json.toString();
                // 解析返回结果
                @SuppressWarnings("unchecked")
				Map<String, String> rmap = JSONObject.parseObject(strResult, Map.class);
                logger.info("resCode:" + rmap.get("result_code") + ", resMessage:" + rmap.get("result_msg"));
                // 结果分析处理
                if ("0000".equals(rmap.get("result_code"))) {// 查询成功
                    if ("00".equals(rmap.get("status"))) {// 提现成功
                        withdrawSuccess(fioo, FundThirdOptCodeEnum.YSBTX.getThirdLevelCode());
                    } else if ("20".equals(rmap.get("status"))) {// 失败
                    	String result_msg = rmap.get("result_msg");
                    	fioo.setRemark("银生宝查询结果:"+result_msg);
                        withdrawFail(fioo);
                    }
                } else {// 查询失败
                    logger.info("银生宝查询失败，orderid={},原因：result_msg={}", fioo.getPayId(), rmap.get("result_msg"));
                    continue;
                }
            }
            logger.info("定时查询银生宝提现结果--结束");
        } catch (Exception e) {
            logger.info("定时查询银生宝提现结果,异常e={}", e);
        } finally {
            sysThreadLockService.unlock(SysThreadLockEnum.UNSPAY_DRAW_QUERY_TASK.getCode());
        }
    }

    @Override
    public void queryWithdrawResultForDinpay() {
        try {
            logger.info("查询智付提现结果定时任务开始...");
            if (!RedisInfoOperate.addLock(redisTemplate, RedisUtil.DINPAY_QUERY_TRANSFER_LOCK, 5*60, TimeUnit.SECONDS)) {
                logger.info("查询智付提现结果执行中...");
                return;
            }

            FundIoCashWithdrawal fio = new FundIoCashWithdrawal();
            fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());// 转账中
            fio.setThirdOptCode(FundThirdOptCodeEnum.ZFTX.getThirdLevelCode());// 智付提现标志
            List<FundIoCashWithdrawal> fios = fundIoCashWithdrawalDao.selectFundIoCashWithds(fio);
            if (fios == null || fios.size() == 0) {
                return;
            }
            BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
            // 智付转账接口版本号
            String interface_version = sysCfgRedis.get("dinpay_transfer_interface_version");
            // 智付商户号
            String merchant_code = sysCfgRedis.get("dinpay_merchant_code");
            // 智付转账结果查询接口
            String transfer_url = sysCfgRedis.get("dinpay_transfer_url");
            // 智付签名方式
            String sign_type = sysCfgRedis.get("dinpay_sign_type");
            // 智付商家私钥
            String private_key = sysCfgRedis.get("dinpay_private_key");

            for (FundIoCashWithdrawal fioo : fios) {
                // 生成签名
                StringBuffer signSrc= new StringBuffer();
                signSrc.append("interface_version=").append(interface_version).append("&");
                signSrc.append("mer_transfer_no=").append(fioo.getPayId()).append("&");
                signSrc.append("merchant_no=").append(merchant_code).append("&");
                signSrc.append("tran_code=").append("DMTQ");
                String signInfo = signSrc.toString();
                String sign_info = "" ;

                if("RSA-S".equals(sign_type)) {//密钥对加密方式sign_type = "RSA-S"
                    sign_info = RSAWithSoftware.signByPrivateKey(signInfo,private_key) ;
                }

                if("RSA".equals(sign_type)){//数字证书加密方式sign_type = "RSA"
                    String path= sysCfgRedis.get("dinpay_pfx_path"); //请在商家后台证书下载处申请和下载pfx数字证书，一般要1~3个工作日才能获取到
                    String pfxPass = sysCfgRedis.get("dinpay_pfx_key"); //证书密钥，初始密码是商户号
                    RSAWithHardware mh = new RSAWithHardware();
                    mh.initSigner(path, pfxPass);
                    sign_info = mh.signByPriKey(signInfo);
                }

                Map<String, String> param = new HashMap<String, String>();
                param.put("interface_version", interface_version);
                param.put("mer_transfer_no", fioo.getPayId());
                param.put("merchant_no", merchant_code);
                param.put("tran_code", "DMTQ");
                param.put("sign_info", sign_info);
                param.put("sign_type", sign_type);

                String result = DinpayHttpTools.doPostSSL(transfer_url, param);
                param.clear();
                if (StringTools.isEmpty(result)){
                    logger.error("智付转账查询失败，此接口没有响应");
                    return;
                }

                Document document = DocumentHelper.parseText(result);
                String result_code = document.selectSingleNode("//dinpay/result_code").getText();
                String recv_code = document.selectSingleNode("//dinpay/recv_code").getText();
                String recv_info = document.selectSingleNode("//dinpay/recv_info").getText();

                // 结果分析处理
                if ("0".equals(result_code)) {// 查询成功
                    if ("0000".equals(recv_code)) {// 提现成功
                        withdrawSuccess(fioo, FundThirdOptCodeEnum.ZFTX.getThirdLevelCode());
                    } else if(!"0001".equals(recv_code)){// 提现失败
                        fioo.setRemark("智付转账失败，查询结果返回："+recv_info);
                        withdrawFail(fioo);
                    }
                    else{
                        continue;
                    }
                } else {// 查询失败
                    logger.info("智付转账查询失败，transfer_no={},原因：recv_info={}", fioo.getPayId(), recv_info);
                    continue;
                }
            }
        } catch (Exception e) {
            logger.info("定时查询智付提现结果,异常e={}", e);
        } finally {
            RedisInfoOperate.delKeyLock(redisTemplate, RedisUtil.DINPAY_QUERY_TRANSFER_LOCK);}
    }

    @Override
    public void queryWithdrawResultForQtong(){
        try{
            logger.info("查询钱通提现结果定时任务开始...");
            if (!RedisInfoOperate.addLock(redisTemplate, RedisUtil.QTPAY_QUERY_TRANSFER_LOCK, 5*60, TimeUnit.SECONDS)) {
                logger.info("查询钱通提现结果执行中...");
                return;
            }

            FundIoCashWithdrawal fio = new FundIoCashWithdrawal();
            fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());// 转账中
            fio.setThirdOptCode(FundThirdOptCodeEnum.QTTX.getThirdLevelCode());// 钱通提现标志
            List<FundIoCashWithdrawal> fios = fundIoCashWithdrawalDao.selectFundIoCashWithds(fio);
            if (fios == null || fios.size() == 0) {
                return;
            }
            BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
            // 商户号
            String merchantId = sysCfgRedis.get("qiantong_merchantId");
            // 版本
            String version = sysCfgRedis.get("qiantong_version");
            // 请求地址
            String pay_rul = sysCfgRedis.get("qiantong_pay_url");

            for (FundIoCashWithdrawal fioo : fios) {
                ReceivePayQueryRequest queryRequest = new ReceivePayQueryRequest();
                queryRequest.setApplication("ReceivePayQuery");
                queryRequest.setVersion(version);
                queryRequest.setMerchantId(merchantId);
                //queryRequest.setTimestamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                queryRequest.setQueryTranId(fioo.getPayId());
                MerchantClient merchantClient = new MerchantClient();
                merchantClient.setPayURL(pay_rul);
                ReceivePayQueryResponse queryResponse = merchantClient.receivePayQuery(queryRequest);
                logger.info("钱通提现查询返回json：{}", JSONTools.toJSON(queryResponse));
                if (queryResponse == null){
                    logger.error("钱通转账查询失败，此接口异常");
                    return;
                }

                if("000".equals(queryResponse.getRespCode())){
                    withdrawSuccess(fioo, FundThirdOptCodeEnum.QTTX.getThirdLevelCode());
                }
                else{
                    fioo.setRemark("钱通提现失败，查询结果返回："+queryResponse.getRespDesc());
                    withdrawFail(fioo);
                }
            }
        }
        catch (Exception e){
            logger.error("钱通提现结果查询异常：{}",e);
        }
        finally {
            RedisInfoOperate.delKeyLock(redisTemplate, RedisUtil.QTPAY_QUERY_TRANSFER_LOCK);}
    }

    @Override
    @Transactional(timeout=10*60)
    public void withdrawForDinpay(String[] ioArr, Integer transferUserId, Integer tranType) throws Exception {
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

        // 智付转账接口版本号
        String interface_version = sysCfgRedis.get("dinpay_transfer_interface_version");
        // 智付商户号
        String merchant_code = sysCfgRedis.get("dinpay_merchant_code");
        // 智付转账结果查询接口
        String transfer_url = sysCfgRedis.get("dinpay_transfer_url");
        // 智付签名方式
        String sign_type = sysCfgRedis.get("dinpay_sign_type");
        // 智付商家私钥
        String private_key = sysCfgRedis.get("dinpay_private_key");

        if (StringTools.isEmpty(interface_version) || StringTools.isEmpty(merchant_code)
                || StringTools.isEmpty(transfer_url) || StringTools.isEmpty(sign_type) || StringTools.isEmpty(private_key)) {
            throw new LTException("智付转账接口信息配置错误");
        }
        List<FundIoCashWithdrawal> fioList = new ArrayList<>();// 提现流水集合
        List<FundTransferDetail> ftdList = new ArrayList<>();// 提现明细集合
        // 遍历提现流水
        for (String id : ioArr) {
            FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(Long.valueOf(id));
            FundTransferDetail ftd = null;
            if (fio == null || fio.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()) {
                logger.info("记录不存在或者对应记录的为非转账状态ioId:{}", id);
                if (fio != null) {
                    logger.info("提现记录状态为{}", fio.getStatus());
                }
                continue;
            }

            // 查询用户信息
            UserBankInfo bankInfo = new UserBankInfo();
            bankInfo.setUserId(fio.getUserId());
            UserBussinessInfo ui = userApiBussinessService.getUserDefaultBankInfoForDinpay(bankInfo);
/*
            if(ui == null){
                throw new LTException(LTResponseCode.FU00001);
            }
            else if(StringTools.isEmpty(ui.getDinpayBankCode())){
                throw new LTException("用户绑定的银行卡未配置智付银行编码");
            }
*/
            //String recv_name = URLEncoder.encode("王鹏飞", "UTF-8");
            Double amount = DoubleUtils.sub(fio.getRmbAmt(), fio.getRmbFactTax());// 转账金额
            String transNo = CalendarTools.formatDateTime(new Date(), CalendarTools.DATETIMEFORMAT) + id;// 交易流水
            // 生成签名
            StringBuffer signSrc= new StringBuffer();
            signSrc.append("interface_version=").append(interface_version).append("&");
            signSrc.append("mer_transfer_no=").append(transNo).append("&");
            signSrc.append("merchant_no=").append(merchant_code).append("&");
            signSrc.append("recv_accno=").append(ui.getBankCardNum()).append("&");
            signSrc.append("recv_bank_code=").append(ui.getDinpayBankCode()).append("&");
            signSrc.append("recv_city=").append("1000").append("&");
            signSrc.append("recv_name=").append(ui.getUserName()).append("&");
            signSrc.append("recv_province=").append("11").append("&");
            signSrc.append("tran_amount=").append(amount.toString()).append("&");
            signSrc.append("tran_code=").append("DMTI").append("&");
            signSrc.append("tran_fee_type=").append("1").append("&");
            signSrc.append("tran_type=").append(tranType.toString());
            String signInfo = signSrc.toString();
            String sign_info = "" ;

            if("RSA-S".equals(sign_type)) {//密钥对加密方式sign_type = "RSA-S"
                sign_info = RSAWithSoftware.signByPrivateKey(signInfo,private_key) ;
            }

            if("RSA".equals(sign_type)){//数字证书加密方式sign_type = "RSA"
                String path= sysCfgRedis.get("dinpay_cert_path"); //请在商家后台证书下载处申请和下载pfx数字证书，一般要1~3个工作日才能获取到
                String pfxPass = sysCfgRedis.get("dinpay_cert_psd"); //证书密钥，初始密码是商户号
                if(StringTools.isEmpty(path) || StringTools.isEmpty(pfxPass)){
                    throw new LTException("签名方式为RSA时，智付商家后台证书地址和证书密码不能为空");
                }
                RSAWithHardware mh = new RSAWithHardware();
                mh.initSigner(path, pfxPass);
                sign_info = mh.signByPriKey(signInfo);
            }

            Map<String, String> param = new HashMap<String, String>();
            // 参数组装
            param.put("interface_version", interface_version);
            param.put("mer_transfer_no", transNo);
            param.put("merchant_no", merchant_code);
            param.put("tran_code", "DMTI");

            param.put("recv_bank_code", ui.getDinpayBankCode());
            param.put("recv_accno", ui.getBankCardNum());
            param.put("recv_name", ui.getUserName());
            param.put("recv_province", "11");
            param.put("recv_city", "1000");
            param.put("tran_amount", amount.toString());
            param.put("tran_fee_type", "1");
            param.put("tran_type", tranType.toString());
            param.put("sign_info",sign_info);
            param.put("sign_type",sign_type);

            String result = DinpayHttpTools.doPostSSL(transfer_url, param);
            param.clear();

            if (StringTools.isEmpty(result)) {
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 beg*/
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("智付通讯失败，请等待后台自动处理");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "智付转账", FundTransferEnum.UNTREATED.getValue(), 2);

                ftd.setRmbAmt(amount);
                ftd.setTransferUserId(transferUserId);
                fioList.add(fio);
                ftdList.add(ftd);
                continue;
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 end*/
            }

            Document document = DocumentHelper.parseText(result);
            String recv_code = document.selectSingleNode("//dinpay/recv_code").getText();
            String recv_info = document.selectSingleNode("//dinpay/recv_info").getText();

            if ("0001".equals(recv_code)) {// 提交转账请求成功
                fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现已提交到智付，等待处理中");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "智付转账", FundTransferEnum.UNTREATED.getValue(), 0);
            } else {
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现，智付返回失败："+recv_info);
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "智付转账", FundTransferEnum.UNTREATED.getValue(), 2);
            }
            fio.setThirdOptCode(FundThirdOptCodeEnum.ZFTX.getThirdLevelCode());
            ftd.setRmbAmt(amount);
            ftd.setTransferUserId(transferUserId);
            fioList.add(fio);
            ftdList.add(ftd);
        }
        if(StringTools.isNotEmpty(fioList)){
            // 批量修改提现流水
            fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
        }
        if(StringTools.isNotEmpty(ftdList)){
            // 批量添加提现明细
            fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
        }

    }

    @Override
    public void withdrawForDaddypay(String[] ioArr, Integer transferUserId) throws Exception {
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

        // 商户id
        String company_id = sysCfgRedis.get("daddypay_company_id");
        // 提现接口地址
        String withdraw_url = sysCfgRedis.get("daddypay_withdraw_url");
        // 私钥
        String private_key = sysCfgRedis.get("daddypay_key");

        if (StringTools.isEmpty(company_id) || StringTools.isEmpty(withdraw_url)
                || StringTools.isEmpty(private_key)) {
            throw new LTException("爸爸付提现接口信息配置错误");
        }
        // 遍历提现流水
        for (String id : ioArr) {
            FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(Long.valueOf(id));
            FundTransferDetail ftd = null;
            if (fio == null || fio.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()) {
                logger.info("记录不存在或者对应记录的为非转账状态ioId:{}", id);
                if (fio != null) {
                    logger.info("提现记录状态为{}", fio.getStatus());
                }
                continue;
            }

            List<FundIoCashWithdrawal> fioList = new ArrayList<>();// 提现流水集合
            List<FundTransferDetail> ftdList = new ArrayList<>();// 提现明细集合

            // 查询用户信息
            UserBankInfo bankInfo = new UserBankInfo();
            bankInfo.setUserId(fio.getUserId());
            UserBussinessInfo ui = userApiBussinessService.getUserDefaultBankInfoForDinpay(bankInfo);
            if(ui == null || StringTools.isEmpty(ui.getDaddypayBankCode())){
                throw new LTException("用户账户银行卡为空或该银行未配置爸爸付银行编码");
            }

            Double amountRmb = DoubleUtils.sub(fio.getRmbAmt(), fio.getRmbFactTax());// 转账金额
            String amount = DoubleTools.decimalFormat(amountRmb, "#.00");
            String transNo = "DP" + CalendarTools.formatDateTime(new Date(), CalendarTools.DATETIMEFORMAT) + id;// 交易流水
            // 生成签名
            String signSrc = MD5Util.md5Low(private_key) + company_id + ui.getDaddypayBankCode() + transNo +
                    amount + ui.getBankCardNum() + ui.getUserName() + "leagueTrade";
            String key = MD5Util.md5Low(signSrc);

            Map<String, String> param = new HashMap<String, String>();
            // 参数组装
            param.put("company_id", company_id);
            param.put("bank_id", ui.getDaddypayBankCode());
            param.put("company_order_num", transNo);
            param.put("amount", amount);
            param.put("card_num", ui.getBankCardNum());
            param.put("card_name", ui.getUserName());
            param.put("company_user", "leagueTrade");
            param.put("key", key);

            JSONObject json = HttpTools.doPost(withdraw_url, param);
            param.clear();
            if (json == null) {
                logger.error("爸爸付查询失败，此接口没有响应");
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 beg*/
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("爸爸付通讯失败，请等待后台自动处理");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "爸爸付转账", FundTransferEnum.UNTREATED.getValue(), 2);

                ftd.setRmbAmt(amountRmb);
                ftd.setTransferUserId(transferUserId);
                fioList.add(fio);
                ftdList.add(ftd);
                if(StringTools.isNotEmpty(fioList)){
                    fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
                }
                if(StringTools.isNotEmpty(ftdList)){
                    fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
                }
                continue;
                /** 通讯失败，请求会再次发送，可能发生发送成功情况， 将数据定义成失败，已处理（后台无需操作），异常情况 end*/
            }
            String strResult = json.toString();
            logger.info("daddypay提现返回json：{}", strResult);
            // 解析返回结果
            Map rmap = JSONObject.parseObject(strResult, Map.class);
            param.clear();
            if ((Integer)rmap.get("status") == 1) {// 提交提现请求成功
                fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现已提交到爸爸付，等待处理中");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "爸爸付转账", FundTransferEnum.UNTREATED.getValue(), 0);
            } else {
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现，爸爸付返回失败："+rmap.get("error_msg"));
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "爸爸付转账", FundTransferEnum.UNTREATED.getValue(), 2);
            }
            fio.setThirdOptCode(FundThirdOptCodeEnum.DPTX.getThirdLevelCode());
            ftd.setRmbAmt(amountRmb);
            ftd.setTransferUserId(transferUserId);
            fioList.add(fio);
            ftdList.add(ftd);

            if(StringTools.isNotEmpty(fioList)){
                // 单条修改，daddypay回调比较快，批量会造成状态未更新回调失败
                fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
            }
            if(StringTools.isNotEmpty(ftdList)){
                // 单条修改
                fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
            }
        }
    }

    @Override
    public void withdrawForQtongPay(String[] ioArr, Integer transferUserId) throws Exception {
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);

        // 商户号
        String merchantId = sysCfgRedis.get("qiantong_merchantId");
        // 版本
        String version = sysCfgRedis.get("qiantong_version");
        // 请求地址
        String pay_rul = sysCfgRedis.get("qiantong_pay_url");
        // 通知地址
        String notifyUrl = sysCfgRedis.get("qiantong_receivePayNotifyUrl");

        if (StringTools.isEmpty(merchantId) || StringTools.isEmpty(version)
                || StringTools.isEmpty(notifyUrl) || StringTools.isEmpty(pay_rul)) {
            throw new LTException("钱通提现接口信息配置错误");
        }
        List<FundIoCashWithdrawal> fioList = new ArrayList<>();// 提现流水集合
        List<FundTransferDetail> ftdList = new ArrayList<>();// 提现明细集合
        // 遍历提现流水
        for (String id : ioArr) {
            FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(Long.valueOf(id));
            FundTransferDetail ftd = null;
            if (fio == null || fio.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()) {
                logger.info("记录不存在或者对应记录的为非转账状态ioId:{}", id);
                if (fio != null) {
                    logger.info("提现记录状态为{}", fio.getStatus());
                }
                continue;
            }

            // 查询用户信息
            UserBankInfo bankInfo = new UserBankInfo();
            bankInfo.setUserId(fio.getUserId());
            UserBussinessInfo ui = userApiBussinessService.getUserDefaultBankInfoForDinpay(bankInfo);
            if(ui == null){
                throw new LTException("用户账户银行卡为空");
            }

            Double amount = 100 * DoubleUtils.sub(fio.getRmbAmt(), fio.getRmbFactTax());// 转账金额（分）
            Long amountTrn = amount.longValue();
            String transNo = "QT" + CalendarTools.formatDateTime(new Date(), CalendarTools.DATETIMEFORMAT) + id;// 交易流水
            ReceivePayRequest receivePayRequest = new ReceivePayRequest();
            receivePayRequest.setApplication("ReceivePay");
            receivePayRequest.setVersion(version);
            receivePayRequest.setMerchantId(merchantId);
            receivePayRequest.setTranId((transNo));
            receivePayRequest.setReceivePayNotifyUrl(notifyUrl);
            receivePayRequest.setReceivePayType("1");
            receivePayRequest.setAccountProp("0");
            receivePayRequest.setBankGeneralName(ui.getBankName());
            receivePayRequest.setAccNo(ui.getBankCardNum());
            receivePayRequest.setAccName(ui.getUserName());
            receivePayRequest.setAmount(amountTrn.toString());
            receivePayRequest.setCredentialType("01");
            receivePayRequest.setCredentialNo(ui.getIdCardNum());
            receivePayRequest.setTel(ui.getTele());
            receivePayRequest.setSummary("提现");

            MerchantClient merchantClient = new MerchantClient();
            merchantClient.setPayURL(pay_rul);
            ReceivePayResponse receivePayResponse = merchantClient.receivePayRequest(receivePayRequest);
            logger.info("钱通提现返回json：{}", JSONTools.toJSON(receivePayResponse));
            if (receivePayResponse == null) {
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("钱通提现失败，接口异常");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "钱通转账", FundTransferEnum.UNTREATED.getValue(), 2);

                ftd.setRmbAmt(amount/100);
                ftd.setTransferUserId(transferUserId);
                fioList.add(fio);
                ftdList.add(ftd);
                continue;
            }
            // 解析返回结果
            if ("000".equals(receivePayResponse.getRespCode())) {// 提交提现请求成功
                fio.setStatus(FundIoWithdrawalEnum.PROCESS.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现已提交到钱通，等待处理中");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "钱通转账", FundTransferEnum.UNTREATED.getValue(), 0);
            } else {
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现，钱通返回失败："+receivePayResponse.getRespDesc());
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "钱通转账", FundTransferEnum.UNTREATED.getValue(), 2);
            }
            fio.setThirdOptCode(FundThirdOptCodeEnum.QTTX.getThirdLevelCode());
            ftd.setRmbAmt(amount/100);
            ftd.setTransferUserId(transferUserId);
            fioList.add(fio);
            ftdList.add(ftd);
        }
        if(StringTools.isNotEmpty(fioList)){
            // 批量修改提现流水
            fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
        }
        if(StringTools.isNotEmpty(ftdList)){
            // 批量添加提现明细
            fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
        }
    }

    @Override
    public void auditWithdrawSingle(String ioIds, int operate, String modifyId, Double amount, String remark,
                                    IFundOptCode codeEnum) throws LTException {
        // TODO Auto-generated method stub
        logger.debug("执行提现申请审核操作，申请的流水ID为：{}，操作为：{}，修改用户为：{}", ioIds, operate, modifyId);
//		FundCashOptCodeEnum codeEnum = getDrawalEnumByThirdCode(thirdOptCode);
        try {

            if (codeEnum == null) {
                throw new LTException(LTResponseCode.FUJ0001);
            }

            fundAccountService.fundDoAuditSingle(Long.parseLong(ioIds), Integer.parseInt(modifyId), operate, codeEnum,
                    amount, remark);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new LTException(e.getMessage());
        }

    }

    @Override
    public void repairFinancyIo(String id, String amt, String rmbAmt, FundOptCode fundOptCode, Integer modifyUserId) throws LTException {
        FundIoCashRecharge cashRecharge = fundwithDrawDao.selectRechargeIo(id);
        cashRecharge.setModifyUserId(modifyUserId);
        if (fundOptCode == null) {
            fundOptCode = new FundOptCode();
        }

        if (cashRecharge.getStatus() != 0) {
            throw new LTException(LTResponseCode.FUY00011);
        }

        Double restAmt = fundAccountService.getDailyUserRechargeTotalAmt(cashRecharge.getUserId());

        logger.info("这里查询出了一些问题：restAmt：{},cashRecharge:{}", JSONObject.toJSONString(restAmt), JSONObject.toJSONString(cashRecharge));

        if (restAmt < Double.valueOf(rmbAmt)) {
            throw new LTException(LTResponseCode.FUY00009);
        }

        logger.info("这里有执行么？");
        fundOptCode.setFirstOptCode(cashRecharge.getFirstOptcode());
        fundOptCode.setSecondOptCode(cashRecharge.getSecondOptcode());
        fundOptCode.setThirdOptCode(cashRecharge.getThirdOptcode());
        fundAccountService.repairFinancyIo(Double.valueOf(amt), Double.valueOf(rmbAmt), cashRecharge, fundOptCode);
    }

    @Override
    public void financyIoReject(String ids) throws LTException {
        List<String> list = new ArrayList<String>();
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                list.add(id);
            }
        } else {
            list.add(ids);
        }

        try {
            fundwithDrawDao.updateFinancyIoMutil(list);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.debug("e{}", e);
            throw new LTException(LTResponseCode.FUY00003);
        }
    }

    @Override
    @Transactional
    public void manualTransfer(String ids,Integer modifyUserId) throws LTException {
        String[] idList = ids.split(",");
        FundTransferDetail fundtransFerDetail = null;
        String code = "";
        for (String id : idList) {
            try {
                FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(Long.valueOf(id));

                if (fio == null) {
                    logger.info("未查询到提现id为：{}，提现信息", id);
                    continue;
                }

                if (fio.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()) {
                    logger.info("提现记录：{} ，状态不为待转账，操作失败，进行下一个转账申请", JSONObject.toJSONString(fio));
                    continue;
                }

                UserBankInfo bankInfo = new UserBankInfo();
                bankInfo.setUserId(fio.getUserId());
                UserBussinessInfo ui = userApiBussinessService.getUserBankList(bankInfo);
                if(ui==null){
                	logger.info("未查到银行卡信息");
                	continue;
                }
                // 转账金额
                Double amount = DoubleUtils.sub(fio.getAmount(), fio.getFactTax());

                code = CalendarTools.formatDateTime(new Date(), CalendarTools.DATETIMEFORMAT) + "_" + id;
                fundtransFerDetail = new FundTransferDetail(code, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), amount,
                        new Date(), 0, "人工转账", FundTransferEnum.TREATED.getValue(), FundTransferEnum.SUCCEED.getValue());
                fundtransFerDetail.setDoneDate(new Date());
                fundtransFerDetail.setCreateDate(fio.getCreateDate());
                fundtransFerDetail.setRmbAmt(DoubleUtils.sub(fio.getRmbAmt(), fio.getRmbFactTax()));
                fundtransFerDetail.setTransferUserId(modifyUserId);
                logger.info("更新提现流水io状态");
                fio.setStatus(FundIoWithdrawalEnum.SUCCEED.getValue());// 成功
                fio.setThirdOptCode(FundThirdOptCodeEnum.YHTX.getThirdLevelCode());
                fio.setModifyUserId(modifyUserId+"");
                fundIoCashWithdrawalDao.updateFundIoCashWithd(fio);

                // 批量添加提现明细
                fundIoCashWithdrawalDao.insertFundTransferDetail(fundtransFerDetail);
                logger.info("调用外部接口修改资金");

                fundAccountApiService.withdrawSuccess(fio, FundThirdOptCodeEnum.YHTX.getThirdLevelCode());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new LTException(e.getMessage());
            }
            logger.info("========处理结束=====");
        }

    }

    @Override
    @Transactional
    public void transferForce(String id, Integer statu, String remark) throws LTException {
        try {
            FundTransferDetail ftd = fundIoCashWithdrawalDao.selectTransferDetailByIoid(Long.parseLong(id));

            if (ftd.getStatus() == FundTransferEnum.SUCCEED.getValue()) {
                throw new LTException(LTResponseCode.FUY00010);
            }

            if (ftd.getOperateStatu() == FundTransferEnum.TREATED.getValue()) {
                throw new LTException(LTResponseCode.FUY00011);
            }

            FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(ftd.getIoId());

            if (FundTransferEnum.SUCCEED.getValue().intValue() == statu.intValue()) {
                withdrawSuccess(fio, FundThirdOptCodeEnum.YHTX.getThirdLevelCode());
            } else {
//				withdrawFail(fio, false);
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());// 失败
                fundIoCashWithdrawalDao.updateFundIoCashWithd(fio);
                logger.info("更新转账明细");
                ftd.setStatus(2);
                ftd.setRemark(StringTools.isEmpty(remark) ? "转账失败" : remark);
                ftd.setOperateStatu(FundTransferEnum.TREATED.getValue());
                logger.info("转账的操作对象为：{}", JSONObject.toJSONString(ftd));
                fundIoCashWithdrawalDao.updateTransferDetail(ftd);
                logger.info("调用外部接口修改资金");
                fundAccountApiService.withdrawFail(fio);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new LTException(e.getMessage());
        }
    }

    @Override
    public Map<String,String> requestWithdrawForDaddypay(Map<String, String> map) {
        Map<String,String> rmap = new HashMap<>();
        try{
            String orderId = map.get("company_order_num");
            rmap.put("status","4");
            rmap.put("error_msg","");
            boolean errorFlag = true;
            FundIoCashWithdrawal query = new FundIoCashWithdrawal();
            query.setPayId(orderId);
            query.setThirdOptCode(FundThirdOptCodeEnum.DPTX.getThirdLevelCode());
            List<FundIoCashWithdrawal> list = fundIoCashWithdrawalDao.selectFundIoCashWithds(query);
            if(StringTools.isNotEmpty(list)){
                FundIoCashWithdrawal  cashWithdrawal = list.get(0);
                if(cashWithdrawal.getStatus() == FundIoWithdrawalEnum.PROCESS.getValue()){
                    errorFlag = false;
                }
            }
            if(errorFlag){
                rmap.put("status","5");
                rmap.put("error_msg","未查到daddypay提现订单记录或订单状态不正确");
            }
        }
        catch (Exception e){
            logger.info("daddypay提现信息确认回调失败:"+e.getMessage());
            rmap.put("status","0");
            rmap.put("error_msg",e.getMessage());
        }

        return rmap;
    }

    @Override
    public Map<String,String> withdrawalResultForDaddypay(Map<String, String> map) {
        Map<String,String> rmap = new HashMap<>();
        try{
            String orderId = map.get("company_order_num");
            String result_code = map.get("status");
            String result_msg = map.get("detail");

            rmap.put("status", "1");
            rmap.put("error_msg", "成功");
            FundIoCashWithdrawal ficw = new FundIoCashWithdrawal();
            ficw.setPayId(orderId);
            ficw.setThirdOptCode(FundThirdOptCodeEnum.DPTX.getThirdLevelCode());
            List<FundIoCashWithdrawal> list = fundIoCashWithdrawalDao.selectFundIoCashWithds(ficw);
            if (StringTools.isEmpty(list)) {
                logger.info("未查到daddypay提现订单记录");
                rmap.put("status", "0");
                rmap.put("error_msg", "未查到daddypay提现订单记录");
                return rmap;
            }
            FundIoCashWithdrawal fio = list.get(0);
            if (fio.getStatus() == FundIoWithdrawalEnum.PROCESS.getValue()) {// 转账中
                fio.setThirdOptCode(FundThirdOptCodeEnum.DPTX.getThirdLevelCode());
                if ("1".equals(result_code)) {// 提现成功
                    withdrawSuccess(fio, FundThirdOptCodeEnum.DPTX.getThirdLevelCode());
                } else {// 失败
                    fio.setRemark(result_msg);
                    withdrawFail(fio);
                }
            } else {
                FundTransferDetail detail = fundIoCashWithdrawalDao.selectTransferDetailByPayid(orderId);
                if (detail.getStatus() == 2 && detail.getOperateStatu() == FundTransferEnum.TREATED.getValue()) {
                    if ("1".equals(result_code)) {
                        withdrawSuccess(fio, FundThirdOptCodeEnum.DPTX.getThirdLevelCode());
                    }
                }

            }
        }
        catch (Exception e){
            logger.info("daddypay提现结果确认回调失败:"+e.getMessage());
            rmap.put("status", "0");
            rmap.put("error_msg", e.getMessage());
        }

        return rmap;

    }

    @Override
    public void withdrawForJiuPaiPay(String[] ioArr, Integer transferUserId, HttpServletRequest request) throws Exception {
        //提现到银行卡 九派
        BoundHashOperations<String,String,String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String jiupai_withdarw_url =  sysCfgRedis.get("jiupai_withdarw_url");
        String jiupai_merchantId = sysCfgRedis.get("jiupai_merchantId");       //
        String jiupai_merchantPass = sysCfgRedis.get("jiupai_merchantPass");   //私钥证书密码
        String jiupai_callback_url = sysCfgRedis.get("jiupai_callback_url");


        String charset = "02";
        String version = "1.0";
        String MCR = "/WEB-INF/jiupai_cert/";   //商户证书相对路径，用来添加签名
        String realRootCertPath = "/WEB-INF/cert/YH.cer";   //根证书
        String merchantCertPath = request.getSession().getServletContext().getRealPath("/") + MCR + jiupai_merchantId + ".p12";
        merchantCertPath = merchantCertPath.replace("\\","/");
        String rootCertPath = request.getSession().getServletContext().getRealPath("/") + realRootCertPath;
        String service = "capSingleTransfer";
        String signType = "RSA256";
        String encoding = "UTF-8";


        if(StringTools.isEmpty(jiupai_withdarw_url) || StringTools.isEmpty(jiupai_merchantId) ||
                StringTools.isEmpty(jiupai_merchantPass)){
            throw new LTException("九派提现信息配置错误！");
        }

        //遍历提现流水
        for(String id : ioArr){
            FundIoCashWithdrawal fio = fundIoCashWithdrawalDao.selectFundIoCashWithdrawalById(Long.valueOf(id));
            FundTransferDetail ftd = null;
            if(fio == null || fio.getStatus() != FundIoWithdrawalEnum.WAIT.getValue()){
                logger.info("记录不存在或者对应记录为非转账状态ioId:{}",id);
                if(fio != null){
                    logger.info("提现记录状态为{}",+fio.getStatus());
                }
                continue;
            }
            List<FundIoCashWithdrawal> fioList = new ArrayList<>(); //提现流水集合
            List<FundTransferDetail> ftdList = new ArrayList<>();  //提现明细集合

            //查询用户信息
            UserBankInfo bankInfo = new UserBankInfo();
            bankInfo.setUserId(fio.getUserId());
            UserBussinessInfo ui = userApiBussinessService.getUserDefaultBankInfoForDinpay(bankInfo);
            if(ui == null || StringTools.isEmpty(ui.getJiupaiBankCode())){
                throw new LTException("用户账户银行卡为空或该银行未配置九派银行编码");
            }

            Double amountRmb = DoubleUtils.sub(fio.getRmbAmt(),fio.getRmbFactTax());
            String amount = DoubleTools.decimalFormat(amountRmb,"#0.00");
            String transNo = "JP" + CalendarTools.formatDateTime(new Date(), CalendarTools.DATETIMEFORMAT) + id;// 交易流水
            //生成签名
            Map<String,Object> dataMap =new LinkedHashMap<String,Object>();
            dataMap.put("charset",charset);
            dataMap.put("version",version);
            dataMap.put("service",service);
            dataMap.put("signType",signType);
            dataMap.put("merchantId",jiupai_merchantId);
            String requestId = String.valueOf(System.currentTimeMillis());
            dataMap.put("requestId",requestId);
            dataMap.put("requestTime",requestId);

            dataMap.put("callBackUrl",jiupai_callback_url);
            dataMap.put("mcSequenceNo",transNo);
            dataMap.put("mcTransDateTime", DateUtils.formatYYYYMMDDHHMMSS());
            dataMap.put("orderNo",transNo);
            dataMap.put("amount",amount);
            dataMap.put("cardNo", HiDesUtils.desEnCode(ui.getBankCardNum()));   //银行卡号
            dataMap.put("accName",ui.getUserName());

            dataMap.put("accType","0");
            dataMap.put("lBnkNo","");
            dataMap.put("lBnkNam","");
            dataMap.put("validPeriod","");
            dataMap.put("cvv2","");
            dataMap.put("cellPhone","");
            dataMap.put("remark","");
            dataMap.put("bnkRsv","");
            dataMap.put("capUse","00");
            dataMap.put("crdType","00");
            dataMap.put("remark1","");
            dataMap.put("remark2","");
            dataMap.put("remark3","");

            Map requestMap = new HashMap();
            requestMap.putAll(dataMap);
            Set set = dataMap.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String)iterator.next();
                if ((dataMap.get(key) == null)||dataMap.get(key).toString().trim().length()==0) {
                    requestMap.remove(key);
                }
            }
            RSASignUtil signUtil = new RSASignUtil(merchantCertPath,jiupai_merchantPass);

            String reqData =  signUtil.coverMap2String(requestMap);
            signUtil.setService(service);

            String merchantSign = signUtil.sign(reqData,encoding);
            String merchantCert = signUtil.getCertInfo();

            String buf = reqData + "&merchantSign=" + merchantSign + "&merchantCert=" + merchantCert;
            String res =  MerchantUtil.sendAndRecv(jiupai_withdarw_url,buf,"UTF-8");
            if(StringTools.isEmpty(res)){
                logger.error("九派提现返回失败，此接口没有响应");
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("九派通讯失败，请等待后台自动处理");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "九派转账", FundTransferEnum.UNTREATED.getValue(), 2);
                ftd.setRmbAmt(amountRmb);
                ftd.setTransferUserId(transferUserId);
                fioList.add(fio);
                ftdList.add(ftd);
                if(StringTools.isNotEmpty(fioList)){
                    fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
                }
                if(StringTools.isNotEmpty(ftdList)){
                    fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
                }
                continue;
            }


            //对返回的结果进行解析
            Map<String,String> retMap = new LinkedHashMap<String,String>();
            retMap.put("charset",(String)signUtil.getValue(res,"charset"));
            retMap.put("version",(String)signUtil.getValue(res,"version"));
            retMap.put("service",(String)signUtil.getValue(res,"service"));
            retMap.put("requestId",(String)signUtil.getValue(res,"requestId"));
            retMap.put("responseId",(String)signUtil.getValue(res,"responseId"));
            retMap.put("responseTime",(String)signUtil.getValue(res,"responseTime"));
            retMap.put("signType",(String)signUtil.getValue(res,"signType"));
            retMap.put("merchantId",(String)signUtil.getValue(res,"merchantId"));
            retMap.put("rspCode",(String)signUtil.getValue(res,"rspCode"));
            retMap.put("rspMessage",(String)signUtil.getValue(res,"rspMessage"));
            retMap.put("mcTransDateTime",(String)signUtil.getValue(res,"mcTransDateTime"));
            retMap.put("orderNo",(String)signUtil.getValue(res,"orderNo"));
            retMap.put("bfbSequenceNo",(String)signUtil.getValue(res,"bfbSequenceNo"));

            retMap.put("mcSequenceNo",(String)signUtil.getValue(res,"mcSequenceNo"));
            retMap.put("mcTransDateTime",(String)signUtil.getValue(res,"mcTransDateTime"));
            retMap.put("cardNo",(String)signUtil.getValue(res,"cardNo"));
            retMap.put("amount",(String)signUtil.getValue(res,"amount"));
            retMap.put("remark1",(String)signUtil.getValue(res,"remark1"));
            retMap.put("remark2",(String)signUtil.getValue(res,"remark2"));
            retMap.put("remark3",(String)signUtil.getValue(res,"remark3"));
            retMap.put("transDate",(String)signUtil.getValue(res,"transDate"));
            retMap.put("transTime",(String)signUtil.getValue(res,"transTime"));
            retMap.put("respMsg",(String)signUtil.getValue(res,"respMsg"));
            retMap.put("orderSts",(String)signUtil.getValue(res,"orderSts"));
            Map responseMap = new HashMap();
            responseMap.putAll(retMap);
            Set set1 = retMap.keySet();
            Iterator iterator1 = set1.iterator();
            while (iterator1.hasNext()) {
                String key0 = (String)iterator1.next();
                String tmp = retMap.get(key0);
                if (StringUtils.equals(tmp, "null")||StringUtils.isBlank(tmp)) {
                    responseMap.remove(key0);
                }
            }

            boolean flag = false;
            RSASignUtil rsautil = new RSASignUtil(rootCertPath);
            String sf=	rsautil.coverMap2String(responseMap);
            flag = rsautil.verify(sf,(String) signUtil.getValue(res,"serverSign"),(String) signUtil.getValue(res,"serverCert"),encoding);
            if(flag){
                logger.error("九派提现接口失败！");
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现已经提交到九派支付，等待处理中");
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "九派转账", FundTransferEnum.UNTREATED.getValue(), 0);
            }else{
                fio.setStatus(FundIoWithdrawalEnum.FAILURE.getValue());
                fio.setModifyDate(new Date());
                fio.setPayId(transNo);
                fio.setRemark("用户提现，九派返回失败："+retMap.get("rspMessage"));
                ftd = new FundTransferDetail(transNo, fio.getUserId(), fio.getId(), ui.getUserName(), ui.getBankCardNum(),
                        ui.getProvinceCode(), ui.getCityCode(), String.valueOf(ui.getBranchId()), ui.getBankName(), DoubleUtils.sub(fio.getAmount(), fio.getFactTax()),
                        new Date(), 0, "九派转账", FundTransferEnum.UNTREATED.getValue(), 2);
            }
            fio.setThirdOptCode(FundThirdOptCodeEnum.DPTX.getThirdLevelCode());
            ftd.setRmbAmt(amountRmb);
            ftd.setTransferUserId(transferUserId);
            fioList.add(fio);
            ftdList.add(ftd);

            if(StringTools.isNotEmpty(fioList)){
                // 单条修改，九派回调比较快，批量会造成状态未更新回调失败
                fundIoCashWithdrawalDao.updateFundIoCashWithds(fioList);
            }
            if(StringTools.isNotEmpty(ftdList)){
                // 单条修改
                fundIoCashWithdrawalDao.insertFundTransferDetails(ftdList);
            }
        }

    }

    @Override
    public Map<String, String> withdrawalResultForJiuPai(Map<String, String> map,HttpServletRequest request) {
        String encoding = "UTF-8";
        BoundHashOperations<String,String,String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String jiupai_withdarw_url =  sysCfgRedis.get("jiupai_withdarw_url");
        String jiupai_merchantId = sysCfgRedis.get("jiupai_merchantId");       //
        String jiupai_merchantPass = sysCfgRedis.get("jiupai_merchantPass");   //私钥证书密码
        String jiupai_callback_url = sysCfgRedis.get("jiupai_callback_url");

        String charset = "02";
        String version = "1.0";
        String MCR = "/WEB-INF/jiupai_cert/";   //商户证书相对路径，用来添加签名
        String realRootCertPath = "/WEB-INF/cert/YH.cer";   //根证书
        String merchantCertPath = request.getSession().getServletContext().getRealPath("/") + MCR + jiupai_merchantId + ".p12";
        merchantCertPath = merchantCertPath.replace("\\","/");
        String rootCertPath = request.getSession().getServletContext().getRealPath("/") + realRootCertPath;
        String signType = "RSA256";


        try{
            map.remove("serverCert");
            map.remove("serverSign");
            Map requestMap  = new HashMap();
            requestMap.putAll(map);
            Set set = requestMap.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                String tmp = map.get(key);
                if (StringUtils.equals(tmp, "null")||StringUtils.isBlank(tmp)) {
                    requestMap.remove(key);
                }
            }
            //数据签名，hmac为签名后的消息摘要
            RSASignUtil rsaUtil = new RSASignUtil(rootCertPath);
            String returnData = rsaUtil.coverMap2String(requestMap);
            boolean flag = rsaUtil.verify(returnData, request.getParameter("serverSign"), request.getParameter("serverCert"), encoding);
            if(flag){
                //验签成功，商户业务逻辑处理
            }else{

            }

        }catch(Exception e){

        }
        return null;
    }
}
