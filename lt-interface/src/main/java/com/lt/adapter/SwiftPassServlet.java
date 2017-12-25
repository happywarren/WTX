package com.lt.adapter;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lt.model.user.charge.FundSwiftPassResultModel;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.swiftpass.config.SwiftpassConfig;
import com.lt.util.utils.swiftpass.util.SwiftPassMD5;
import com.lt.util.utils.swiftpass.util.SignUtils;
import com.lt.util.utils.swiftpass.util.XmlUtils;


/**
 * 威富通支付宝扫码支付处理
 * <功能详细描述>支付处理
 *
 */
public class SwiftPassServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        SortedMap<String,String> map = XmlUtils.getParameterMap(req);


        map.put("mch_id", SwiftpassConfig.mch_id);
        map.put("notify_url", SwiftpassConfig.notify_url);
        map.put("nonce_str", String.valueOf(new Date().getTime()));

        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = SwiftPassMD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
        map.put("sign", sign);

        String reqUrl = SwiftpassConfig.req_url;
        System.out.println("reqUrl：" + reqUrl);

        System.out.println("reqParams:" + XmlUtils.parseXML(map));

        Map<String,String> resultMap = HttpTools.doPostXML(reqUrl, map);

        String res = "success";
        if(StringTools.isNotEmpty(resultMap)){
            res = XmlUtils.toXml(resultMap);
            System.out.println("请求结果：" + res);
            if(resultMap.containsKey("sign")){
                if(!SignUtils.checkParam(resultMap, SwiftpassConfig.key)){
                    res = "验证签名不通过";
                    //TODO 加入错误日志
                }else{

                    if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
                        //TODO 查询数据库 是否成交
                        //TODO 若已处理 抛出数据不处理
                            //重复提交的时候直接查询本地的状态
                        res = "查询结果".equals(resultMap.get(map.get("out_trade_no"))) ? "未支付" : "已支付";
                        //TODO 若还没有处理 则插入入金纪录
                             //先查询out_trade_no对应的 user_id 相关资金纪录
                             //userId 入金：fund_follow_cash流水插入  fund_main_cash 更新账户余额 累计充值金额

                    }
                }
            }
        }
        if(res.startsWith("<")){
            resp.setHeader("Content-type", "text/xml;charset=UTF-8");
        }else{
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
        }
        resp.getWriter().write(res);
    }

    public static void main22(String[] args) {
        String mch_id="399590032501";
        String key="53116be6a82c3e3422e4f50f0e06476f";
        String req_url="https://pay.swiftpass.cn/pay/gateway";
        String notify_url="https://test2.meiguwang.cn/lt-interface/swiftPassResultSerlet";
        SortedMap map = new TreeMap();
        //必要参数
        map.put("out_trade_no", "170901171111312333");
        map.put("mch_create_ip", "127.0.0.1");
        map.put("total_fee", "1");
//        map.put("service", "pay.alipay.native");//支付宝
        map.put("service", "pay.weixin.native");//微信
        map.put("body", "测试交易");
        map.put("mch_id",mch_id);
        map.put("notify_url", notify_url);
        map.put("nonce_str", String.valueOf(new Date().getTime()));

        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = SwiftPassMD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);

        String reqUrl = req_url;
        System.out.println("reqUrl：" + reqUrl);

        System.out.println("reqParams:" + XmlUtils.parseXML(map));

        Map<String,String> resultMap = HttpTools.doPostXML(reqUrl, map);

        String res = "success";
        if(StringTools.isNotEmpty(resultMap)){
            res = XmlUtils.toXml(resultMap);
            System.out.println("请求结果：" + res);
            if(resultMap.containsKey("sign")){
                if(!SignUtils.checkParam(resultMap, key)){
                    res = "验证签名不通过";
                    //TODO 加入错误日志
                }else{

                    if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
                        //TODO 查询数据库 是否成交
                        //TODO 若已处理 抛出数据不处理
                        //重复提交的时候直接查询本地的状态
                        res = "查询结果".equals(resultMap.get(map.get("out_trade_no"))) ? "未支付" : "已支付";
                        //TODO 若还没有处理 则插入入金纪录
                        //先查询out_trade_no对应的 user_id 相关资金纪录
                        //userId 入金：fund_follow_cash流水插入  fund_main_cash 更新账户余额 累计充值金额

                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("id","1");
        map.put("codeImgUrl","123212132131");
        map.put("codeUrl","1");
        map.put("errCode","1");
        map.put("errMsg","1");
        map.put("userId","1");
        map.put("message","1");

        SignUtils.reflectFilter(map, FundSwiftPassResultModel.class);
    }
}
