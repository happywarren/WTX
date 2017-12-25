package com.lt.util.utils.iapppay.demo;

import java.net.URLEncoder;
import java.util.Map;

import com.lt.util.utils.iapppay.sign.BareBonesBrowserLaunch;
import com.lt.util.utils.iapppay.sign.SignHelper;

import net.sf.json.JSONObject;
public class OrderTest {
	/**
	 * 类名：demo 功能 服务器端签名与验签Demo 版本：1.0 日期：2014-06-26 '说明：
	 * '以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己的需要，按照技术文档编写,并非一定要使用该代码。
	 * '该代码仅供学习和研究爱贝云计费接口使用，只是提供一个参考。
	 * 
	 */
	/*
	 * CP服务端组装请求参数,请求下单url，得到 返回的  transid。把 transid 传到客户端进行支付。
	 * 服务端组装下单请求参数：transdata={"appid":"500000185","waresid":2,"cporderid":"1421206966472","price":1,"currency":"RMB","appuserid":"A100003A832D40","cpprivateinfo":"cpprivateinfo123456","notifyurl":"http://192.168.0.140:8094/monizhuang/api?type=100"}&sign=PNkLyWO5dxzZJrGNRJhSQGJ1oRMpvNDOHmQJntCt7OP3faT6oyL3Jc4Ne6r4IyJMxm3CAk1rxiQBoSuuAf06zsoEWbT4pNIkgqyafP4ai7zKfkJxeX7gsiG6wycT3PqRlwtmF0L7W4RDicrnAGrOQ3ynUxsrGW4oJ+7dKdHM4ZA=&signtype=RSA
	 * 请求地址：以文档给出的为准
	 * 再此请格外注意  每个参数值的 数据类型
	 * 可选参数 ：waresname   price  cpprivateinfo  notifyurl
	 */
	    	/**
	 * 组装请求参数
	 * 
	 * @param appid
	 *          应用编号
	 * @param waresid
	 *          商品编号
         * @param price
	 *          商品价格
         * @param waresname
	 *          商品名称
	 * @param cporderid
	 *          商户订单号
         * @param appuserid
	 *          用户编号
	 * @param cpprivateinfo
	 *          商户私有信息
         * @param notifyurl
	 *          支付结果通知地址
	 * @return 返回组装好的用于post的请求数据
	 * .................
	 */
	static String transid;
	public static String ReqData(String appid, int waresid,String waresname,String cporderid ,float price,String appuserid,String cpprivateinfo,String notifyurl) {
		
		String json;
		json = "appid:";
		json += IAppPaySDKConfig.APP_ID;
		json += " userid:";
		json += appuserid;
		json += " waresid:";
		json += waresid;
		json += "cporderid:";
		json += cporderid;
		System.out.println("json="+json);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", IAppPaySDKConfig.APP_ID);
		jsonObject.put("waresid", IAppPaySDKConfig.WARES_ID_1);
		jsonObject.put("cporderid", cporderid);
		jsonObject.put("currency", "RMB");
		jsonObject.put("appuserid", appuserid);
		//以下是参数列表中的可选参数
		if(!waresname.isEmpty()){
			jsonObject.put("waresname", waresname);
		}
			/*
			 * 当使用的是 开放价格策略的时候 price的值是 程序自己 设定的价格，使用其他的计费策略的时候
			 * price 不用传值
			 * */ 
			jsonObject.put("price", price);
		if(!cpprivateinfo.isEmpty()){
			jsonObject.put("cpprivateinfo", cpprivateinfo);
		}
		if(!notifyurl.isEmpty()){
			/*
			 * 如果此处不传同步地址，则是以后台传的为准。
			 * */
			jsonObject.put("notifyurl", notifyurl);
		}
		String content = jsonObject.toString();// 组装成 json格式数据
		// 调用签名函数      重点注意： 请一定要阅读  sdk 包中的爱贝AndroidSDK3.4.4\03-接入必看-服务端接口说明及范例\爱贝服务端接入指南及示例0311\IApppayCpSyncForJava \接入必看.txt 
		//content="{\"tid\":\"32221706081600006810\",\"app\":\"301160131\",\"url_r\":\"https://staging.51zcd.com\",\"url_h\":\"https://staging.51zcd.com\"}";
		
		String sign = SignHelper.sign(content, IAppPaySDKConfig.APPV_KEY);
		String data = "transdata=" + content + "&sign=" + sign+ "&signtype=RSA";// 组装请求参数
		System.out.println("请求数据:"+data);
		return data;
	}
	// 数据验签
	public static void CheckSign(String appid, int waresid,String waresname,String cporderid,float price,String appuserid,String cpprivateinfo,String notifyurl) {
		//String reqData = ReqData( IAppPaySDKConfig.APP_ID,  IAppPaySDKConfig.WARES_ID_1, waresname,cporderid, price, appuserid, cpprivateinfo, notifyurl);
		String reqData = ReqData( "3015203700",  1, waresname,cporderid, price, appuserid, cpprivateinfo, notifyurl);
		//String respData = HttpUtils.sentPost("http://ipay.iapppay.com:9999/payapi/order", reqData,"UTF-8"); // 请求验证服务端
		String respData = HttpUtils.sentPost("http://ipay.iapppay.com:9999/payapi/order", reqData,"UTF-8"); // 请求验证服务端
		System.out.println("响应数据："+respData);
		
		/*---------------------------------------------如果得到成功响应的结果-----------------------------------------------------------*/
		// 解析结果 得到的 数据为一个以&分割的字符串，需要分成三个部分transdata，sign，signtype。
		// 成功示例：respData == "transdata={"transid":"32011501141440430237"}&sign=NJ1qphncrBZX8nLjonKk2tDIKRKc7vHNej3e/jZaXV7Gn/m1IfJv4lNDmDzy88Vd5Ui1PGMGvfXzbv8zpuc1m1i7lMvelWLGsaGghoXi0Rk7eqCe6tpZmciqj1dCojZoi0/PnuL2Cpcb/aMmgpt8LVIuebYcaFVEmvngLIQXwvE=&signtype=RSA"
		
		 Map<String, String> reslutMap = SignHandUtils.getParmters(respData);
         String transdata = null;
         String signtype = reslutMap.get("signtype"); // "RSA";
	if(signtype==null)
	{
		
	}else{
		/*
		 * 调用验签接口
		 * 
		 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
		 */
		if (SignHelper.verify(reslutMap.get("transdata"), reslutMap.get("sign"),IAppPaySDKConfig.PLATP_KEY)) {
			System.out.println(reslutMap.get("transdata"));
			System.out.println( reslutMap.get("sign"));
			JSONObject json=new JSONObject().fromObject(reslutMap.get("transdata"));
			transid=json.getString("transid");
			System.out.println("verify ok");
		} else {
			System.out.println("verify fail");
		}
	}
}
	//当客户端上使用H5 的时候下面的示例代码可以有所帮助。
	
	public static void H5orPCpay(String transid){
		String pcurl="https://web.iapppay.com/h5/gateway?";
		String h5url="https://web.iapppay.com/pc/gateway?";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("tid",transid);
		jsonObject.put("app",IAppPaySDKConfig.APP_ID);
		jsonObject.put("url_r", "http://58.250.160.241:8888/IapppayCpSyncForPHPDemo/Test.php");
		jsonObject.put("url_h", "http://58.250.160.241:8888/IapppayCpSyncForPHPDemo/Test.php");
		String content=jsonObject.toString();
		String sign = SignHelper.sign(content, IAppPaySDKConfig.APPV_KEY);
		String data = "data=" + URLEncoder.encode(content) + "&sign=" + URLEncoder.encode(sign)+ "&signtype=RSA";
		System.out.println("可以直接在浏览器中访问该链接:"+h5url+data);//我们的常连接版本 有PC 版本 和移动版本。 根据使用的环境不同请更换相应的URL:h5url,pcurl.
		String url=pcurl+data;  //String url=pcurl+data; 可以直接更换 url=pcurl+data中的pcurl 为h5url，即可在手机浏览器中调出移动版本的收银台。
		System.out.println(url);
		BareBonesBrowserLaunch.openURL(url);
	}
//	可以右键运行查看效果
	public static void main(String[] argv) {
//CheckSign(             String appid,          int waresid,     String waresname,   String cporderid, float prce,     String appuserid,        String cpprivateinfo,       String notifyurl)
		//CheckSign( IAppPaySDKConfig.APP_ID,IAppPaySDKConfig.WARES_ID_1,"aaa", "12312223",0.1f, "108412312312310", "1231231231", "http://58.250.160.241:8888/IapppayCpSyncForPHPDemo/TradingResultsNotice.php");
		//H5orPCpay(transid);
		CheckSign( "3015203700",
				1,
				"ProLtrader", 
				System.currentTimeMillis()+"",
				1.00F, 
				"lt201708@163.com",
				"test", 
				"https://test3.meiguwang.cn/lt-interface/iapppayServlet");
		H5orPCpay(transid);
	}


}
