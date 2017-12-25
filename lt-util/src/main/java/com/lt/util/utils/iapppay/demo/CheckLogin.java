package com.lt.util.utils.iapppay.demo;
	/**
	 * '以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己的需要，按照技术文档编写,并非一定要使用该代码。
	 * '该代码仅供学习和研究爱贝云计费接口使用，只是提供一个参考。
	 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import com.lt.util.utils.iapppay.sign.SignHelper;

import net.sf.json.JSONObject;

public class CheckLogin {
	/*
	 * 在客户端调用登陆接口，得到返回 logintoken 客户端把 logintoken 传给 服务端
	 * 服务端组装验证令牌的请求参数：transdata={"appid":"123","logintoken":"3213213"}&sign=xxxxxx&signtype=RSA
	 * 请求地址：以文档给出的为准
	 */
    	/**
	 * 组装请求参数
	 * 
	 * @param appid
	 *            应用编号
	 * @param logintoken
	 *          从服务端获取的token
	 * @return 返回组装好的用于post的请求数据
	 * .................
	 */
	public static String ReqData(String appid, String logintoken) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", IAppPaySDKConfig.APP_ID);
		jsonObject.put("logintoken", logintoken);
		String content = jsonObject.toString();// 组装成 json格式数据
		String sign = SignHelper.sign(content, IAppPaySDKConfig.APPV_KEY);// 调用签名函数
		String data = "transdata=" + URLEncoder.encode(content) + "&sign=" + URLEncoder.encode(sign)
				+ "&signtype=RSA";// 组装请求参数
		System.out.println("请求数据："+data);
		return data;
	}
	// 令牌验证
	public static void CheckToken(String appid, String logintoken) {
		String reqData = ReqData(IAppPaySDKConfig.APP_ID, logintoken);
		String respData = HttpUtils.sentPost("http://ipay.iapppay.com:9999/openid/openidcheck",reqData,"UTF-8"); // 请求验证服务端
		System.out.println("响应数据："+respData);
		
		/*---------------------------------------------如果得到成功响应的结果-----------------------------------------------------------*/
		// 解析结果 得到的 数据为一个以&分割的字符串，需要分成三个部分transdata，sign，signtype。
		// 成功示例：respData == "transdata={"loginname":"18701637882","userid":"14382295"}&sign=HU6L6dZNR0PJEgsINI5Dlt2L2WfCsN8WDAUP+i/mLNIIwMVCHBBB6GKSrLvz10B5w5LGnX0PQf74oJx8O7JBOMJyQ7oQWoIs4NcpRi73BSxqdnt8XUTIBjfg33sfuGCCQO6GEW6gFHnocsXzNq8MIWk9mvCOFRL3pp/GmKdbbhQ=&signtype=RSA"
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
	
	public static void main(String[] args)  {
		String appid=IAppPaySDKConfig.APP_ID;
		String logintoken="d597003d15fd9a918e2ffbf422447aa4";
		CheckToken(appid,logintoken);
	}

}
