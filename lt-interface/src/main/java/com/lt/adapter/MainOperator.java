package com.lt.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.lt.util.utils.crypt.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.IAdapter;
import com.lt.adapter.adapterConfig.ServiceList;
import com.lt.adapter.interceptor.TokenInterceptor;
import com.lt.adapter.utils.LimitConditionUtil;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.IpUtils;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.TokenTools;
import com.lt.util.utils.crypt.Md5Encrypter;
import com.lt.util.utils.model.Response;
import com.lt.util.utils.model.Token;

/**
 * 作者: 邓玉明 时间: 2017/2/24 上午11:40 email:cndym@163.com
 */
public class MainOperator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public final String ADAPTER = "Adapter";

    public static final String KEY = "LT7";

    public String operator(String msg, String ip) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("ip", ip);
        return exec(msg, paraMap);
    }

    public String operator(HttpServletRequest request) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        String msg = request.getParameter("msg");

        msg = StringTools.formatStr(msg, null);
        String ip = IpUtils.getUserIP(request);
        paraMap.put("ip", ip);
        String method = request.getAttribute("callbackForUnspay") == null ? "" : request.getAttribute("callbackForUnspay").toString();
        if (method.contains("callbackForUnspay")) {
            return exec(request, paraMap);
        } else {
            return exec(msg, paraMap);
        }
    }

    public void aliPayoperator(HttpServletRequest request) {

        logger.info("======支付宝手机充值异步接受结果======alipay============");
        Map<String, String[]> map = request.getParameterMap();
        Set<Entry<String, String[]>> set = map.entrySet();
        Iterator<Entry<String, String[]>> it = set.iterator();
        while (it.hasNext()) {
            Entry<String, String[]> entry = it.next();
            logger.info("KEY:" + entry.getKey());
            for (String i : entry.getValue()) {
                logger.info("Value:" + i);
            }
        }

        // 通知类型:bptb_result_notify
        String[] notify_type_arr = map.get("notify_type");
        String notify_type = notify_type_arr[0];
        // 应用id
        String[] app_id_arr = map.get("app_id");
        String app_id = app_id_arr[0];
        // 充值金额
        String[] total_amount_arr = map.get("total_amount");
        String total_amount = total_amount_arr[0];
        // 原订单号
        String[] out_trade_no_arr = map.get("out_trade_no");
        String out_trade_no = out_trade_no_arr[0];
        // 通知时间
        String[] notify_time_arr = map.get("notify_time");
        String notify_time = notify_time_arr[0];
        // 签名方式:sign_type
        String[] sign_type_arr = map.get("sign_type");
        String sign_type = sign_type_arr[0];

        String[] buyer_pay_amount_arr = map.get("buyer_pay_amount");
        String buyer_pay_amount = buyer_pay_amount_arr[0];

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("notify_type", notify_type);
        paraMap.put("app_id", app_id);
        paraMap.put("total_amount", total_amount);
        paraMap.put("out_trade_no", out_trade_no);
        paraMap.put("notify_time", notify_time);
        paraMap.put("sign_type", sign_type);
        paraMap.put("buyer_pay_amount", buyer_pay_amount);
        paraMap.put("data", JSONObject.toJSONString(paraMap));
        paraMap.put("func", "reviceZfbResponse");

        logger.info("通知类型.notify_type={},通知时间.notify_time={},签名方式 sign_type={},app_id={},totalamount={},out_trade_no={},buyer_pay_amount_arr={}",
                notify_type, notify_time, sign_type, app_id, total_amount, out_trade_no, buyer_pay_amount);
        paraMap.put("cmd", "fund");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        adapter.execute(paraMap);
    }

    /**
     * 支付宝（聚合支付）处理operate
     *
     * @author yuanxin
     * @date 2017年7月17日 下午6:23:59
     */
    public void aliPayFoperator(Map<String, Object> params) {
        logger.info("params:{}", JSONObject.toJSONString(params));
        params.put("func", "reviceZfbFResponse");
        params.put("cmd", "user");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        adapter.execute(params);
    }

    public void kqoperator(Map<String, Object> paramMap) {
        logger.info("=====快钱手机充值异步接受结果======alipay============");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "reviceKQResponse");
        paramMap.put("cmd", "fund");
        adapter.execute(paramMap);
    }

    /**
     * 熙大支付宝处理
     *
     * @param paramMap
     */
    public void xdpayOperator(Map<String, Object> paramMap) throws LTException {
        logger.info("=====【接收熙大支付宝处理结果】============");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "xdPayAccept");
        paramMap.put("cmd", "fund");
        adapter.execute(paramMap);
    }

    /**
     * 威富通回调处理
     *
     * @param paramMap
     */
    public void swiftPassOperator(Map<String, Object> paramMap) throws LTException {
        logger.info("=====【威富通回调处理】============");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "swiftPassResult");
        paramMap.put("cmd", "fund");
        adapter.execute(paramMap);
    }

    /**
     * 充值回调
     *
     * @param paramMap
     */
    public void chargeCallBackOperator(Map<String, Object> paramMap) {
        logger.info("【接收充值回调结果】");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "chargeCallBack");
        paramMap.put("cmd", "user");
        adapter.execute(paramMap);
    }


    /**
     * 智付处理类
     *
     * @param paramMap
     */
    public void dinpayOperator(Map<String, Object> paramMap) {
        logger.info("=====【接收智付处理结果】============");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "dinpayAccept");
        paramMap.put("cmd", "fund");
        adapter.execute(paramMap);
    }

    public void qianTongPayOperator(String paramstr) {
        logger.info("=====【接收钱通处理结果】============");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("func", "qianTongPayCallback");
        paramMap.put("cmd", "user");
        paramMap.put("paramstr", paramstr);
        adapter.execute(paramMap);
    }

    /**
     * 爱贝充值回调处理
     *
     * @param paramMap
     */
    public void iapppayOperator(Map<String, Object> paramMap) throws LTException {
        logger.info("=====【接收爱贝处理理结果】============");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "iapppayAccept");
        paramMap.put("cmd", "fund");
        adapter.execute(paramMap);
    }

    /**
     * 微信h5充值回调处理
     * @param paramMap
     */
    public void weixinh5Operator(Map<String,Object> paramMap){
        logger.info("=============【接收微信处理结果】================");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "weixinh5Accept");
        paramMap.put("cmd", "fund");
        adapter.execute(paramMap);
    }

    /**
     * 聚合充值回调处理
     *
     * @param paramMap
     */
    public String aggpayOperator(Map<String, Object> paramMap) throws LTException {
        logger.info("=====【接收聚合处理理结果】============");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "aggpayAccept");
        paramMap.put("cmd", "fund");
        return adapter.execute(paramMap).getCode();
    }

    public String alipayH5Operator(Map<String,Object> paramMap) throws LTException{
        logger.info("===========【接收支付宝h5处理结果】==========");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("fund" + ADAPTER);
        paramMap.put("func", "alipayH5Accept");
        paramMap.put("cmd", "fund");
        return adapter.execute(paramMap).getCode();
    }

    /**
     * 操作处理类（Servlet已经携带参数）
     *
     * @param request
     * @param paraMap
     * @author yuanxin
     * @date 2017年3月29日 上午10:47:19
     */
    public String operator(HttpServletRequest request, Map<String, Object> paraMap) {
        String msg = request.getParameter("msg");
        msg = StringTools.formatStr(msg, null);
        String ip = IpUtils.getUserIP(request);
        paraMap.put("ip", ip);
        logger.info("ip: " + ip);
        return exec(msg, paraMap);
    }

    /**
     * 充值回调接口处理
     *
     * @param request
     * @param paraMap
     * @return
     */
    private String exec(HttpServletRequest request, Map<String, Object> paraMap) {
        Response response;
        String cmd = "fund";
        String func = "callbackForUnspay";
        logger.info("操作码: " + cmd);
        logger.info("功能参数: " + func);
        String result_code = request.getParameter("result_code");// 返回码
        String result_msg = request.getParameter("result_msg");// 返回信息
        String amount = request.getParameter("amount");// 实际充值金额
        String orderId = request.getParameter("orderId");// 订单号
        paraMap.put("result_code", result_code);
        paraMap.put("result_msg", result_msg);
        paraMap.put("amount", amount);
        paraMap.put("orderId", orderId);
        String data = JSONObject.toJSONString(paraMap);
        paraMap.put("func", func);
        paraMap.put("cmd", cmd);
        paraMap.put("data", data);
        logger.info("----银生宝充值回调接口返回参数-result_code={},result_msg={},amount={},orderId={}-----",
                result_code, result_msg, amount, orderId);
        if (StringTools.isEmpty(result_code) || StringTools.isEmpty(amount) || StringTools.isEmpty(orderId)) {
            logger.info("银生宝充值回调接口返回参数缺失");
            throw new LTException(LTResponseCode.FU00003);
        }
        IAdapter adapter = (IAdapter) SpringUtils.getBean(cmd + ADAPTER);
        response = adapter.execute(paraMap);
        return response.toJsonString();
    }


    /**
     * 普通接口处理
     *
     * @param msg app 传进来的原始数据
     * @param paraMap 参数集合
     * @return 处理结果
     */
    private String exec(String msg, Map<String, Object> paraMap) {
        Response response;
        JSONObject jsonObject;
        if (StringTools.isNotEmpty(msg)) {
            paraMap.put("msg", msg);
            logger.info("msg: " + msg);
            try {
                jsonObject = JSON.parseObject(msg);
            } catch (Exception e) {
                e.printStackTrace();
                throw new LTException(LTResponseCode.PR00001);
            }
            String cmd = jsonObject.getString("cmd");
            paraMap.put("cmd", cmd);
            String func = jsonObject.getString("func");
            paraMap.put("func", func);

            // 渠道
            String sid = jsonObject.getString("sid");
            paraMap.put("sid", sid);

            String md5 = jsonObject.getString("md5");
            paraMap.put("md5", md5);
            String token = jsonObject.getString("token");
            paraMap.put("token", token);

            // 实际数据
            String data = jsonObject.getString("data");
            paraMap.put("data", data);

            // 品牌
            String brand = jsonObject.getString("brand");
            paraMap.put("brand", brand);

            String timestamp = jsonObject.getString("timestamp");
            paraMap.put("timestamp", timestamp);
            String deviceType = jsonObject.getString("deviceType");
            paraMap.put("deviceType", deviceType);//iOS Android
            String recordVersion = jsonObject.getString("recordVersion");
            paraMap.put("recordVersion", recordVersion);
            String recordIP = jsonObject.getString("recordIP");
            paraMap.put("recordIP", recordIP);
            String recordLoginMode = jsonObject.getString("recordLoginMode");
            paraMap.put("recordLoginMode", recordLoginMode);
            String recordImei = jsonObject.getString("recordImei");
            paraMap.put("recordImei", recordImei);
            String recordDevice = jsonObject.getString("recordDevice");
            paraMap.put("recordDevice", recordDevice);
            String recordAccessMode = jsonObject.getString("recordAccessMode");
            paraMap.put("recordAccessMode", recordAccessMode);
            String recordCarrierOperator = jsonObject.getString("recordCarrierOperator");
            paraMap.put("recordCarrierOperator", recordCarrierOperator);

            //app版本
            String clientVersion = jsonObject.getString("clientVersion");
            paraMap.put("clientVersion", clientVersion);

            logger.info("md5: " + md5);
            logger.info("操作码: " + cmd);
            logger.info("功能参数: " + func);
            logger.info("渠道: " + sid);
            logger.info("token: " + token);
            logger.info("实际数据: " + data);
            logger.info("时间: " + timestamp);
            logger.info("app版本: " + clientVersion);

            try {
                jsonObject = JSON.parseObject(data);
                if (jsonObject != null) {
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        paraMap.put(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new LTException(LTResponseCode.PR00001);
            }

            //特殊标记用于自测，正式环境去掉
            //去掉签名验证，改用数据加密
            logger.info("data:{}",data+KEY);
            if (!"test".equals(md5)) {
                // 签名验证

               /* if (!md5.equalsIgnoreCase(Md5Encrypter.MD5(data + KEY))) {
                    logger.info("============后加密数据为：{}", data + KEY);
                    logger.info("预先加密数据：{} 和 后加密数据：{}", md5, Md5Encrypter.MD5(data + KEY));
                    throw new LTException(LTResponseCode.SIGN_FAILED);
                }*/
               /*
                if (!md5.equalsIgnoreCase(MD5Util.md5(data + KEY))) {
                    logger.info("============后加密数据为：{}", data + KEY);
                    logger.info("预先加密数据：{} 和 后加密数据：{}", md5, MD5Util.md5(data + KEY));
                    throw new LTException(LTResponseCode.SIGN_FAILED);
                }*/
            }

            Token tk = TokenTools.parseToken(token);
            paraMap.put("userId", tk.getUserId());

            if (LimitConditionUtil.isLimit(paraMap)) {
                logger.info("----------黑名单被限制，paraMap={}---------", JSONObject.toJSONString(paraMap));
                throw new LTException(LTResponseCode.USJ0000);
            }

            // 是否校验token
            boolean checkToken = ServiceList.token(cmd, func);
            if (checkToken) {
                if (StringTools.isEmpty(token)) {
                    throw new LTException(LTResponseCode.US01114);
                }

                //单点登录校验
                TokenInterceptor tokenInterceptor = (TokenInterceptor) SpringUtils.getBean("tokenInterceptor");
                if (!tokenInterceptor.checkToken(token)) {
                    throw new LTException(LTResponseCode.US01114);
                }
            }

            IAdapter adapter = (IAdapter) SpringUtils.getBean(cmd + ADAPTER);
            response = adapter.execute(paraMap);
        } else {
            throw new LTException(LTResponseCode.PR00001);
        }
        return response.toJsonString();
    }

    public Response advertiseOperator(String advertiseId) {
        logger.info("=====广告图内容显示======");
        IAdapter adapter = (IAdapter) SpringUtils.getBean("advertiseShow" + ADAPTER);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("adverId", advertiseId);
        paramMap.put("func", "getAdvertiseComment");
        return adapter.execute(paramMap);
    }

    public static void main(String[] args) {

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("a", "v");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmd", "order");
        map.put("func", "findEntrustRecord");
        map.put("sid", null);
        map.put("md5", Md5Encrypter.MD5("data"));
        map.put("token",
                "917450b68537cbfa43126995e6e292bc387da3519f9b7b359ed2efcebebc7170881e48be315b53e033ed04411433faa824ea14eecc3c026bcb25cf0fd8a6fb76");
        map.put("data", map1);
        map.put("timestamp", System.currentTimeMillis());

        System.out.println(JSONObject.toJSONString(map));
    }
}
