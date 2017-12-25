package com.lt.util.utils.iapppay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import com.lt.util.utils.iapppay.sign.SignHelper;

import net.sf.json.JSONObject;

public class QueryContract {	
	/*
	 * 此demo 代码 使用于 cp 通过主动查询方式 获取商品契约。
	 * 请求地址见文档
	 * 请求方式:post
	 * 流程：cp服务端组装请求参数并对参数签名，以post方式提交请求并获得响应数据，处理得到的响应数据，调用验签函数对数据验签。 
	 * 请求参数及请求参数格式：transdata={"appid":"500000185","appuserid":"A100003A832D40"}&sign=VvT9gHqGjwuhj07/lbcErBo6b23tX1Z5f/aiBItCw5YlFZb6MQpg/NLc9SCA6qc+S6Pw+Jqe87QiiWpXhPf1fEIclLdu5vWmbFMvA4VMW+Il+6oTJFuJItjfIfhGhljEIrgqXO5ZrNs8mrbKBkJHjUtHv1jRFzFtCQZeMgwZr3U=&signtype=RSA
	 * 以下实现 各项请求参数 处理代码：
	 * 
	 * */
	
    	/**
	 * 组装请求参数
	 * 
	 * @param appid
	 *          应用编号
	 * @param appuserid
	 *          用户的应用账号
	 * @return 返回组装好的用于post的请求数据
	 * .................
	 */
	public static String ReqData(String appid,String appuserid,int waresid){
		String reqData ="";
		//组装成 json格式
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("appid", IAppPaySDKConfig.APP_ID);
		jsonObject.put("appuserid",appuserid);
		if(waresid!=0){
			jsonObject.put("waresid", IAppPaySDKConfig.WARES_ID_1);
		}
		reqData=jsonObject.toString();
		//调用签名函数对数据签名
		String sign="";
		sign=SignHelper.sign(reqData, IAppPaySDKConfig.APPV_KEY);//此处的 私钥请 查看 SDK示例代码中 文档（需要对私钥进行处理）
		//组装请求参数：
		String data="transdata="+reqData+"&sign="+sign+"&signtype=RSA";
		return data;
	}
	
	// 数据验签
	public static void CheckSign(String appid,String appuserid,int waresid) {
		String reqData = ReqData( IAppPaySDKConfig.APP_ID, appuserid, IAppPaySDKConfig.WARES_ID_1);
		String respData = HttpUtils.sentPost("http://ipay.iapppay.com:9999/payapi/subsquery", reqData,"UTF-8"); // 请求验证服务端
		
		/*---------------------------------------------如果得到成功响应的结果-----------------------------------------------------------*/
		// 解析结果 得到的 数据为一个以&分割的字符串，需要分成三个部分transdata，sign，signtype。
		// 成功示例：respData == "transdata={"subslist":[{"feetype":5,"subsstatus":0,"waresid":3},{"feetype":6,"leftcount":4,"subsstatus":0,"waresid":4}],"subsnum":2}&sign=L3mx5+lvDtS6YULaQLc3p23MAntwMwZmB7o/+D3cjqbtW9MjZOXzx/K/s6xod9EiMoFPuN0gDhCXd3DJbzvebWBKYR0BGq3myJx3hVk9sCHoLGogSLiryDy9I50VPPG3KR6Nm5KPF8a70hlIDBiOKxBhHB/kr+FiXJx0GVohGss=&signtype=RSA"
		
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
	
}
