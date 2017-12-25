package com.lt.util.utils.iapppay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import com.lt.util.utils.iapppay.sign.SignHelper;

import net.sf.json.JSONObject;

public class QueryTransdataByAppidAndCporderid {
	

	

    /*
     * 此demo 代码 使用于 cp 通过主动查询方式 获取同步数据。
     * 请求地址见文档
     * 请求方式:post
     * 流程：cp服务端组装请求参数并对参数签名，以post方式提交请求并获得响应数据，处理得到的响应数据，调用验签函数对数据验签。 
     * 请求参数及请求参数格式：transdata={"appid":"123456","cporderid":"3213213"}&sign=xxxxxx&signtype=RSA
     * 注意：只有在客户端支付成功的订单，主动查询才会有交易数据。
     * 以下实现 各项请求参数 处理代码：
     * 
     * */
    
    /**
     * 组装请求参数
     *
     * @param appid 
     *          应用编号
     * @param cporderid 
     *          商户订单号
     * @return 返回组装好的用于post的请求数据 .................
     */
    public static String ReqData(String appid, String cporderid) {
        String reqData = "";
        //组装成 json格式
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", IAppPaySDKConfig.APP_ID);
        jsonObject.put("cporderid", cporderid);
        reqData = jsonObject.toString();
        //调用签名函数对数据签名
        String sign = "";
        sign = SignHelper.sign(reqData, IAppPaySDKConfig.APPV_KEY);//此处的 私钥请 查看 SDK示例代码中 文档（需要对私钥进行处理）
        //组装请求参数：
        String data = "transdata=" + reqData + "&sign=" + sign + "&signtype=RSA";
        return data;
    }

    // 数据验签
    public static void CheckSign(String appid, String cporderid) {
        String reqData = ReqData(IAppPaySDKConfig.APP_ID, cporderid);
        String respData = HttpUtils.sentPost("http://ipay.iapppay.com:9999/payapi/queryresult", reqData, "UTF-8"); // 请求验证服务端

        /*---------------------------------------------如果得到成功响应的结果-----------------------------------------------------------*/
        // 解析结果 得到的 数据为一个以&分割的字符串，需要分成三个部分transdata，sign，signtype。
        // 成功示例：respData == "transdata={"appid":"500000185","cporderid":"1421220957285","cpprivate":"cpprivateinfo123456","currency":"RMB","feetype":6,"money":1.0,"paytype":5,"result":0,"transid":"32011501141535580250","transtime":"2015-01-14 15:36:23","waresid":4}&sign=cbKD9RDjYpjgPMaaWJ0K4q7f6ldXOj8QA+IWkPdCfjmOlj2Gb9pRQg88CAONSB49wsNvHkiM+4OAOpqcohN/kdgDc92fmqzGcPb5GyUSdDacKHWFqFkyxxeboFiH0P5SrDIsklHBAfBiWP5DXTnL1Y6RiG52JyjX2sRWnGm1dAI=&signtype=RSA"
        Map<String, String> reslutMap = SignHandUtils.getParmters(respData);
        String transdata = null;
        String signtype = reslutMap.get("signtype"); // "RSA";
			if(signtype==null)
			{
					try {
						System.out.println(""+ URLDecoder.decode(respData, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}else{
				
					/*
					 * 调用验签接口
					 * 
					 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
					 */
					
					try {
						transdata = URLDecoder.decode(reslutMap.get("transdata"), "UTF-8");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // "{\"loginname\":\"18701637882\",\"userid\":\"14382295\"}";
						String sign = null;
					try {
						sign = URLDecoder.decode(reslutMap.get("sign"),  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} 
					
							if (SignHelper.verify(transdata, sign,IAppPaySDKConfig.PLATP_KEY)) {
								System.out.println("verify ok");
							} else {
								System.out.println("verify fail");
							}
			}
		}
    
    
    		public static void main(String[] args) {
    			CheckSign("", "158023_1_1450428382464_0_2");
    			
    			
    			
			}
    
}
