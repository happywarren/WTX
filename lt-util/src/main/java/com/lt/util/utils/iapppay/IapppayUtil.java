package com.lt.util.utils.iapppay;

import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.iapppay.demo.HttpUtils;
import com.lt.util.utils.iapppay.demo.IAppPaySDKConfig;
import com.lt.util.utils.iapppay.demo.SignHandUtils;
import com.lt.util.utils.iapppay.sign.BareBonesBrowserLaunch;
import com.lt.util.utils.iapppay.sign.SignHelper;

import javolution.util.FastMap;
import net.sf.json.JSONObject;

public class IapppayUtil {
	private String appid;
	private int waresid;
	private String waresname;
	private String cporderid;
	private Double price;
	private String appuserid;
	private String cpprivateinfo;
	private String notifyurl;
	private String secretKey;
	private String publicKey;
	private String reqUrl;
	private String orderUrl;
	private String url_r;
	private static IapppayUtil IapppayUtil;
	
	public static final Logger logger = LoggerFactory.getLogger(IapppayUtil.class);

	public static IapppayUtil getInstance() {
		if (IapppayUtil == null) {
			synchronized (IapppayUtil.class) {
				if (IapppayUtil == null) {
					IapppayUtil = new IapppayUtil();
				}
			}
		}
		return IapppayUtil;
	}

	public IapppayUtil() {

	}

	public String getorderUrl() {
		return orderUrl;
	}

	public void setorderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void init(String appid, int waresid, String waresname, String cporderid, Double price, String appuserid, String cpprivateinfo, String notifyurl) {
		this.appid = appid;
		this.waresid = waresid;
		this.waresname = waresname;
		this.cporderid = cporderid;
		this.price = price;
		this.appuserid = appuserid;
		this.cpprivateinfo = cpprivateinfo;
		this.notifyurl = notifyurl;
	}

	public void init(String appid, int waresid, String waresname, String cporderid, Double price, String appuserid, String cpprivateinfo, String notifyurl, String secretKey, String publicKey) {
		this.appid = appid;
		this.waresid = waresid;
		this.waresname = waresname;
		this.cporderid = cporderid;
		this.price = price;
		this.appuserid = appuserid;
		this.cpprivateinfo = cpprivateinfo;
		this.notifyurl = notifyurl;
		this.secretKey = secretKey;
		this.publicKey = publicKey;
	}

	public String getUrl_r() {
		return url_r;
	}

	public void setUrl_r(String url_r) {
		this.url_r = url_r;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public int getWaresid() {
		return waresid;
	}

	public void setWaresid(int waresid) {
		this.waresid = waresid;
	}

	public String getWaresname() {
		return waresname;
	}

	public void setWaresname(String waresname) {
		this.waresname = waresname;
	}

	public String getCporderid() {
		return cporderid;
	}

	public void setCporderid(String cporderid) {
		this.cporderid = cporderid;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getAppuserid() {
		return appuserid;
	}

	public void setAppuserid(String appuserid) {
		this.appuserid = appuserid;
	}

	public String getCpprivateinfo() {
		return cpprivateinfo;
	}

	public void setCpprivateinfo(String cpprivateinfo) {
		this.cpprivateinfo = cpprivateinfo;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public static String getTransid() {
		return transid;
	}

	public static void setTransid(String transid) {
		IapppayUtil.transid = transid;
	}

	/**
	 * 类名：demo 功能 服务器端签名与验签Demo 版本：1.0 日期：2014-06-26 '说明：
	 * '以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己的需要，按照技术文档编写,并非一定要使用该代码。
	 * '该代码仅供学习和研究爱贝云计费接口使用，只是提供一个参考。
	 * 
	 */
	/*
	 * CP服务端组装请求参数,请求下单url，得到 返回的 transid。把 transid 传到客户端进行支付。
	 * 服务端组装下单请求参数：transdata={"appid":"500000185","waresid":2,"cporderid":
	 * "1421206966472","price":1,"currency":"RMB","appuserid":"A100003A832D40",
	 * "cpprivateinfo":"cpprivateinfo123456","notifyurl":
	 * "http://192.168.0.140:8094/monizhuang/api?type=100"}&sign=
	 * PNkLyWO5dxzZJrGNRJhSQGJ1oRMpvNDOHmQJntCt7OP3faT6oyL3Jc4Ne6r4IyJMxm3CAk1rxiQBoSuuAf06zsoEWbT4pNIkgqyafP4ai7zKfkJxeX7gsiG6wycT3PqRlwtmF0L7W4RDicrnAGrOQ3ynUxsrGW4oJ
	 * +7dKdHM4ZA=&signtype=RSA 请求地址：以文档给出的为准 再此请格外注意 每个参数值的 数据类型 可选参数
	 * ：waresname price cpprivateinfo notifyurl
	 */
	/**
	 * 组装请求参数
	 * 
	 * @param appid
	 *            应用编号
	 * @param waresid
	 *            商品编号
	 * @param price
	 *            商品价格
	 * @param waresname
	 *            商品名称
	 * @param cporderid
	 *            商户订单号
	 * @param appuserid
	 *            用户编号
	 * @param cpprivateinfo
	 *            商户私有信息
	 * @param notifyurl
	 *            支付结果通知地址
	 * @return 返回组装好的用于post的请求数据 .................
	 */
	static String transid;

	public String ReqData() {

		String json;
		json = "appid:";
		json += appid;
		json += " userid:";
		json += appuserid;
		json += " waresid:";
		json += waresid;
		json += "cporderid:";
		json += cporderid;
		System.out.println("json=" + json);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appid", appid);
		jsonObject.put("waresid", waresid);
		jsonObject.put("cporderid", cporderid);
		jsonObject.put("currency", "RMB");
		jsonObject.put("appuserid", appuserid);
		// 以下是参数列表中的可选参数
		if (!waresname.isEmpty()) {
			jsonObject.put("waresname", waresname);
		}
		/*
		 * 当使用的是 开放价格策略的时候 price的值是 程序自己 设定的价格，使用其他的计费策略的时候 price 不用传值
		 */
		jsonObject.put("price", price);
		if (!cpprivateinfo.isEmpty()) {
			jsonObject.put("cpprivateinfo", cpprivateinfo);
		}
		if (!notifyurl.isEmpty()) {
			/*
			 * 如果此处不传同步地址，则是以后台传的为准。
			 */
			jsonObject.put("notifyurl", notifyurl);
		}
		String content = jsonObject.toString();// 组装成 json格式数据
		// 调用签名函数 重点注意： 请一定要阅读 sdk
		// 包中的爱贝AndroidSDK3.4.4\03-接入必看-服务端接口说明及范例\爱贝服务端接入指南及示例0311\IApppayCpSyncForJava
		// \接入必看.txt
		// content="{\"tid\":\"32221706081600006810\",\"app\":\"301160131\",\"url_r\":\"https://staging.51zcd.com\",\"url_h\":\"https://staging.51zcd.com\"}";

		String sign = SignHelper.sign(content, IAppPaySDKConfig.APPV_KEY);
		String data = "transdata=" + content + "&sign=" + sign + "&signtype=RSA";// 组装请求参数
		System.out.println("请求数据:" + data);
		return data;
	}

	// 数据验签
	public void CheckSign() {
		String reqData = ReqData();
		String respData = HttpUtils.sentPost("http://ipay.iapppay.com:9999/payapi/order", reqData, "UTF-8"); // 请求验证服务端
		System.out.println("响应数据：" + respData);

		/*---------------------------------------------如果得到成功响应的结果-----------------------------------------------------------*/
		// 解析结果 得到的 数据为一个以&分割的字符串，需要分成三个部分transdata，sign，signtype。
		// 成功示例：respData ==
		// "transdata={"transid":"32011501141440430237"}&sign=NJ1qphncrBZX8nLjonKk2tDIKRKc7vHNej3e/jZaXV7Gn/m1IfJv4lNDmDzy88Vd5Ui1PGMGvfXzbv8zpuc1m1i7lMvelWLGsaGghoXi0Rk7eqCe6tpZmciqj1dCojZoi0/PnuL2Cpcb/aMmgpt8LVIuebYcaFVEmvngLIQXwvE=&signtype=RSA"

		Map<String, String> reslutMap = SignHandUtils.getParmters(respData);
		String transdata = null;
		String signtype = reslutMap.get("signtype"); // "RSA";
		if (signtype == null) {

		} else {
			/*
			 * 调用验签接口
			 * 
			 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
			 */
			if (SignHelper.verify(reslutMap.get("transdata"), reslutMap.get("sign"), IAppPaySDKConfig.PLATP_KEY)) {
				System.out.println(reslutMap.get("transdata"));
				System.out.println(reslutMap.get("sign"));
				JSONObject json = new JSONObject().fromObject(reslutMap.get("transdata"));
				transid = json.getString("transid");
				System.out.println("verify ok");
			} else {
				System.out.println("verify fail");
			}
		}
	}
	// 当客户端上使用H5 的时候下面的示例代码可以有所帮助。

	public static void H5orPCpay(String transid) {
		String pcurl = "https://web.iapppay.com/h5/gateway?";
		String h5url = "https://web.iapppay.com/pc/gateway?";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tid", transid);
		jsonObject.put("app", IAppPaySDKConfig.APP_ID);
		jsonObject.put("url_r", "http://58.250.160.241:8888/IapppayCpSyncForPHPDemo/Test.php");
		jsonObject.put("url_h", "http://58.250.160.241:8888/IapppayCpSyncForPHPDemo/Test.php");
		String content = jsonObject.toString();
		String sign = SignHelper.sign(content, IAppPaySDKConfig.APPV_KEY);
		String data = "data=" + URLEncoder.encode(content) + "&sign=" + URLEncoder.encode(sign) + "&signtype=RSA";
		System.out.println("可以直接在浏览器中访问该链接:" + h5url + data);// 我们的常连接版本 有PC 版本
																// 和移动版本。
																// 根据使用的环境不同请更换相应的URL:h5url,pcurl.
		String url = pcurl + data; // String url=pcurl+data; 可以直接更换
									// url=pcurl+data中的pcurl
									// 为h5url，即可在手机浏览器中调出移动版本的收银台。
		BareBonesBrowserLaunch.openURL(url);
	}

	public synchronized Map<String, Object> execute() throws LTException {
		Map<String, Object> result = FastMap.newInstance();
		try {
			String json;
			json = "appid:";
			json += appid;
			json += " userid:";
			json += appuserid;
			json += " waresid:";
			json += waresid;
			json += "cporderid:";
			json += cporderid;
			logger.info("json=" + json);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("appid", appid);
			jsonObject.put("waresid", waresid);
			jsonObject.put("cporderid", cporderid);
			jsonObject.put("currency", "RMB");
			jsonObject.put("appuserid", appuserid);
			// 以下是参数列表中的可选参数
			if (!waresname.isEmpty()) {
				jsonObject.put("waresname", waresname);
			}
			/*
			 * 当使用的是 开放价格策略的时候 price的值是 程序自己 设定的价格，使用其他的计费策略的时候 price 不用传值
			 */
			jsonObject.put("price", price);
			if (!cpprivateinfo.isEmpty()) {
				jsonObject.put("cpprivateinfo", cpprivateinfo);
			}
			if (!notifyurl.isEmpty()) {
				/*
				 * 如果此处不传同步地址，则是以后台传的为准。
				 */
				jsonObject.put("notifyurl", notifyurl);
			}
			String content = jsonObject.toString();// 组装成 json格式数据

			String sign = SignHelper.sign(content, secretKey);
			String reqData = "transdata=" + content + "&sign=" + sign + "&signtype=RSA";// 组装请求参数
			logger.info("请求数据:" + reqData);
			String respData = HttpUtils.sentPost(reqUrl, reqData, "UTF-8"); //
			Map<String, String> reslutMap = SignHandUtils.getParmters(respData);
			String signtype = reslutMap.get("signtype");
			@SuppressWarnings("static-access")
			JSONObject transdata = new JSONObject().fromObject(reslutMap.get("transdata"));
			/**下单失败**/
			if (signtype == null || StringTools.isEmpty(transdata.getString("transid"))) {
				result.put("resultId", LTResponseCode.ER400);
				if(StringTools.isNotEmpty(transdata.getString("errmsg"))){
					reslutMap.put("resultDesc", transdata.getString("errmsg"));
				}
				return result;
			} else {
				/**下单成功**/
				if (SignHelper.verify(reslutMap.get("transdata"), reslutMap.get("sign"), publicKey)) {
					logger.info(reslutMap.get("transdata"));
					logger.info(reslutMap.get("sign"));
			
					transid = transdata.getString("transid");
					result.put("transid", transid);
					result.put("resultId", LTResponseCode.SUCCESS);
				} else {
					logger.info("验证签名失败！");
					result.put("resultId", LTResponseCode.ER400);
					result.put("resultDesc","验证签名失败");
					return result;
				}
			}

			String h5url = "https://web.iapppay.com/h5/gateway?";
			//String pcurl = "https://web.iapppay.com/pc/gateway?";
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("tid", transid);
			jsonObject2.put("app", appid);
			jsonObject2.put("url_r", url_r);
			jsonObject2.put("url_h", url_r);
			String content1 = jsonObject2.toString();
			String sign1 = SignHelper.sign(content1, secretKey);
			String data1 = "data=" + URLEncoder.encode(content1) + "&sign=" + URLEncoder.encode(sign1) + "&signtype=RSA";
			System.out.println("可以直接在浏览器中访问该链接:" + h5url + data1);// 我们的常连接版本
																	// 有PC 版本
																	// 和移动版本。
																	// 根据使用的环境不同请更换相应的URL:h5url,pcurl.
			String url = h5url + data1;
			result.put("reqUrl", url);
			// BareBonesBrowserLaunch.openURL(url);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("resultId", LTResponseCode.ER400);
			return result;
		}

		return result;
	}

	// 可以右键运行查看效果
	public static void main(String[] argv) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					IapppayUtil iapppay = IapppayUtil.getInstance();
					iapppay.init("3015203700", 1, "ProLtrader", System.currentTimeMillis() + "", 1.00D, "lt201708@163.com", "test", "https://test3.meiguwang.cn/lt-interface/iapppayServlet");
					iapppay.setSecretKey(
							"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIvyByPmy/xOKDpoGEO9rdX1myFtxBjdT20udEP5WD71/xFYr9HLvQuWurEX+jCZlzl7HH+F66eD5yHmJ/Li8Mmaw4tPeKYej3on9WU++kpJxvmmwS/RJLJToPmm3v6qlvHh5lccwwjW62PzoigXCcq+npStuz+0srELCZn3lF2vAgMBAAECgYAlVJiHASfk08xzWBUw7MYUTwHktu0aXN61FzE4eKkLkn9J10h/REPXdYuzddvtXusyEB8X/VdRRiQ/rK93YQYtuuXBLOLMbdpTHehvYMWGaL3ZR4v4hZaCBqJE1YIE9b5tinPzwz+5+0kW1veBBFdqt5rSebvF0o4KLv/eNciToQJBAN6RAvBwSRclDKj+maG4XxUQXuAbKlak63fAIz9mx+8YGAErCbv/YC8MkRikWdqPCZbU+XN+q0/jW7vaVfxBu5kCQQCg98CI6fGm/bL9wVPIS+rwPGN2D5nasVYlEBXueKzwdLLps3M6lMf6cd+JN2J2HEmeft00HKm9Ses+sfT4Q/CHAkEAlPXrSK9uS95RLd5RRurWQIvXZBjqalkw+9IOBUYuNHkkv8tlVX0ji/nWNu7w8JXhbiEW39T8pZhe8ki8WfxIqQJAHNFeKeTgO4pOAjobWs+kpw/YqqlOXSXEi2we0QvPwljSMx7KcWFzj/XVFEbTHNwhnfuOdKbxCI90S0wm1E+tfwJBANfIQJaBKjOByMxHEsfL615lEzlCjG/BrMPthweE2L9qZ4AWXYKQJJ1MXO1k91DeGsYYGCvySmSkYFDL7PKy6Rk=");
					iapppay.setPublicKey(
							"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEH5d5wPMXawH8FTgV/fjB8YXa8+sKEQFkRISf3gZgnkVkj5SVxadQykJesmmfzxx3pVcCgoPhyOK5B4eYDqYYIHSdILfSWT45B1FF4Io2YIGfDdugKLckn1aqyLyn5qTgGfgtrvEzFeCNf0BRGItRfcaj/hGX/pWf3vrA4kFNAwIDAQAB");
					iapppay.execute();
				}
			}).start();

		} catch (Exception e) {
		}

	}

}
