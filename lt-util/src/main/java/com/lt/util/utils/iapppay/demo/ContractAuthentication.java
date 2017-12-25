package com.lt.util.utils.iapppay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import com.lt.util.utils.iapppay.sign.SignHelper;

import net.sf.json.JSONObject;

public class ContractAuthentication {
	/*
	 * 此demo 代码 使用于 用户契约鉴权。
	 * 请求地址见文档
	 * 请求方式:post
	 * 流程：cp服务端组装请求参数并对参数签名，以post方式提交请求并获得响应数据，处理得到的响应数据，调用验签函数对数据验签。 
	 * 请求参数及请求参数格式：transdata={"appid":"500000185","appuserid":"A100003A832D40","waresid":4}&sign=N85bxusvUozqF3iwfAq3Ts3UeyZn8mKi5BVe+H+Vg1nrcE06AhHt7IrJLO3I5njZSF4g5CbLMLiTJiXCmNsH/t35gU3bmIKFPKiw7g3aq0hMofyhgsCLXSWEOrSIa7W6mLzPcEhUkjdX9XxsASbsILHTrJwZYYG7d9PTyhqSmoA=&signtype=RSA
	 * 以下实现 各项请求参数 处理代码：
	 * 
	 * */
	
	
    	/**
	 * 组装请求参数
	 * 
	 * @param appid
	 *            应用编号
	 * @param appuserid
	 *          用户的应用账号
         * @param waresid
	 *          商品编号
	 * @return 返回组装好的用于post的请求数据
	 * .................
	 */
	public static String ReqData(String appid,String appuserid,int waresid){
		String reqData ="";
		//组装成 json格式
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("appid", IAppPaySDKConfig.APP_ID);
		jsonObject.put("appuserid",appuserid);
		jsonObject.put("waresid",IAppPaySDKConfig.WARES_ID_1);
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
		String reqData = ReqData(  IAppPaySDKConfig.APP_ID, appuserid,IAppPaySDKConfig.WARES_ID_1);
		String respData = HttpUtils.sentPost("http://ipay.iapppay.com:9999/payapi/subsauth", reqData,"UTF-8"); // 请求验证服务端
		
		/*---------------------------------------------如果得到成功响应的结果-----------------------------------------------------------*/
		// 解析结果 得到的 数据为一个以&分割的字符串，需要分成三个部分transdata，sign，signtype。
		// 成功示例：respData == "transdata={"feetype":6,"leftcount":3,"subsstatus":0}&sign=UO1WBJM61May1XV0Eb+PmoPxmIoZRrzhxzUS5BFSXcBUVvqQUdhQcAGQMql1vacrRIUY/pi0IExWT8RVcaNyosF8PfQbTevHNT+pa8UFMVpPjJLSWLf8ZDNt0GBvga1ZBs0Xp7T1KtIEC9jTZsPB4+TwnVptYBj9VjaaA9gpYjY=&signtype=RSA"
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
