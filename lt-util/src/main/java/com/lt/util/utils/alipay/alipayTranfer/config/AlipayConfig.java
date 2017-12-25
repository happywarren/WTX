package com.lt.util.utils.alipay.alipayTranfer.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {

	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	//来客赢商户
//	public static String partner = "2088811252264971";
	//上海宏融信息科技有限公司
	public static String partner = "2088221712264382";
	// 来客赢商户的私钥-和上传支付宝的公钥是一一对应的，通过openssl加密
	/*public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKP+nxYC46/mI2/b" 
			+ "3iPOhnQIOEL0B8WwLX06F+Uzw7MyNiL6/h4kyjdA1J4ppM4TBtHExEMclARFENaZ" 
			+ "u2tcvFifPRY5h9ZGZdkf0qbe/3dP5YyfWCbf81r87tJ8wvBjr2nQrIFS2EZEBDU4" 
			+ "okn2T5wLBPNqe4m1ETfvhZqEH3PPAgMBAAECgYB1rd0SungQXSDIwAmjwCuTT+Kv" 
			+ "iNWa8lVyKyHCg2bHTBy09U8s6lGBXLA13sQ4bEbUeFItpnBAsB7pH9/xptYvy+GE" 
			+ "6v23QJCC9hddDCEBg5U2h9Y2Xenx/ZLFmRGsWkhXvwj3vSqOA4iF+O9Pm6k44ZQe" 
			+ "5u/5DRBxps1ywaX1AQJBAM69jXq+P+Ox5oy7qS9MnAK5NAi/0BS3/h/5iQgu1RPl" 
			+ "j61VS/wHvseB/Uuzujtd7HdttuArBAbVs1N+ZHnbia8CQQDLEbjiqDeRaPWnACeK" 
			+ "Ci8sNsOwrTVkN1XEb6mtPTg6ZwH3cJqT7CS6jS86yibkjNimOV1yQpYznQ1KZr+B" 
			+ "hN/hAkAn784Iy/+jiJY00XuZO39Xt1cp6YPUvMZGfvnNrt5SfaIHpdaL1SMxfxbj" 
			+ "LNDo7vfjCjqtiyG4AEgn9N6L+7DZAkAl6FcFKLXG1TsWQ7cd06zeqhJ5xruyy4md" 
			+ "M+MOzeDTNittz31ro6dvk69/YAFGpOxEi2zgSYHUfLrgarHRMcbBAkEAr6TyaMj/" 
			+ "o+zQr7+poyYTT752JV7iYi4a+qCP6RGbrIp+m/X3k4EK7xOpjf+XAocNEwgysc1x" 
			+ "k+Zljn0WxshHxw==";*/
	//上海宏融信息科技有限公司私钥-和上传支付宝的公钥是一一对应的，通过openssl加密
	public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPXNj8toHFqlGdHO" 
			+ "ymD2n8wa9PimP+x+RZv7KiamgYtvuc4AbNOXEhp10vr/kCa9K5ndwVwVFSA9lazo" 
			+ "xpY2NunXmzf+/Y22BzYmDzQDulW3+A3XAPjeA3SP/ou+vQcyjXShUMKCBjBTWKuU" 
			+ "Dpg/WNT+9Z/MJuUiwXBGia1agnAvAgMBAAECgYEAmhX5jT4/gAc9b0udjDkuJSnE" 
			+ "5VZ6fgsOIeDKJJ5+WOV56YxUrDNostOnpZatS8uMWJEt1EaXDrXsbQ8i5ikoNCZi" 
			+ "j7Z8CAHwP5Ec9eD0nNWL3hf06JFT2gMMVKoCqhdapAx3YrIshXRZq26tHaYqDwDW" 
			+ "5tCVH9YcSuGuNp2YaGkCQQD9WFEOInekonBuFascjNI/9pz6rF60XYr/xZMTDl58" 
			+ "NH9bXpxbKY32figojcNroXTCN789OEHm1q7KBnNh6udLAkEA+GEC1olUIbsBBQJF" 
			+ "Zme4qHZEbSunNAumArbOy1Q0fHd7kCOeQztHABgGFOdT6ZI+HYk3/iRxM5qKXVSl" 
			+ "TmJYLQJAchR901aT94+p/vhE6ANFfcYIJBzQPHnO6KPIOZGD3HwrSTKOR8KriW5/" 
			+ "mNtVPzciA37k36Vgj5jmdlR3SoVjhwJBAOdY3N7dLQr59vomYmP/CkWHWzbgaS4W" 
			+ "B3wRsE49UWH5nJLmSjxPE1PoDIod6c/uEoNKU2gZo4MwV96UwCBhg8UCQArwXSju" 
			+ "1t9Lu3A6yZotUubHGmN2ouuwJKrzjCCqo4vrlShI6UM4Jq0FD22vFglW9825XhVi" 
			+ "OGptFvz0QkCd4Go=";
	//支付宝的公钥，无需修改该值
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y" 
			+ "2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8" 
			+ "fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "/data/logs/";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 签名方式 不需修改
	public static String sign_type = "RSA";
}
