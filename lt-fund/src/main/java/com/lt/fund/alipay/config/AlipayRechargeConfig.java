package com.lt.fund.alipay.config;

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

public class AlipayRechargeConfig {

	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	//来客赢商户
//	public static String partner = "2088811252264971";
	//上海宏融信息科技有限公司
	public static String partner = "2088421699309786";
	
	public static String app_id = "2016091201894433";
	// 来客赢商户的私钥-和上传支付宝的公钥是一一对应的，通过openssl加密
	//上海宏融信息科技有限公司私钥-和上传支付宝的公钥是一一对应的，通过openssl加密
	public static String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANBWIQrUQ42Nve5AbQeXsZFNE07tepUTOMxR3zXa0Agl52f3Y7Yxr+YV4zfb2muadYM1R0MKvv24Pa6IL8E7/i1uv1RQuuFEGT8hWjiP0aGMC08MKja079p2KwlyHBB++X0v6iC6woQmNiXwErNotO9jl5jnPr/+atFG3+OfTRWxAgMBAAECgYAiiSSRFv7byFNj22o3h/33iD9bJiLb84PG2wuVceBqeV4S7f+phDMUgSxZZiifDcvNJFfb1Twz8wbGl+zSq49GzCpEyjSqBzee8rsOhxirIKiNc3IZ2Sa5g9q52FnOuQgr7NKZEvg4HS9v0SpkFJyltLlK+sQ/QkO+lYtDZUZRAQJBAO5HbM5O9qGOR30rG4Bw91R3v3bVdnJhRkN8jfvnKH1xwFjSh7KH4UnIrSVmDb1om0t+TJ4VbNWfx7ICdnPZE80CQQDf1KFHAX7nH16zyIh2IsfIRG4fVTEaM6ctTaQbonQxP/xwk4AEbaaBAnelfEOqhua5fTd0KRaukPSgUhfzDC11AkA+UFmJHqdrGp1hg4t+rIxR/7sHpc2DHiNU9Nbg4NP68mueDvjD3LszI5L8a9L+DMRIGvCIiwvaVh3QVMqLg2KlAkBcVH+6pJlOiZgtHTl9UdeLev7aGfBJWKlLfM2HQLohXr2pSo7yRLD8YqIsN1hKTZ97QHXRiul6VP0z7OlILuvJAkAodJ9s6vkROusKHmro5j6TMbtG0O2oVw5n0tRtwMGBDJcBnbA2e7gvP/cIlBTfZneuazXkiH8Zbwnrxut3U5o4";
	//用户公钥
	public static String user_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQViEK1EONjb3uQG0Hl7GRTRNO7XqVEzjMUd812tAIJedn92O2Ma/mFeM329prmnWDNUdDCr79uD2uiC/BO/4tbr9UULrhRBk/IVo4j9GhjAtPDCo2tO/adisJchwQfvl9L+ogusKEJjYl8BKzaLTvY5eY5z6//mrRRt/jn00VsQIDAQAB";
	//支付宝的公钥，无需修改该值
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "/data/logs/";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "UTF-8";

	// 签名方式 不需修改
	public static String sign_type = "RSA";
	
	public static String format = "json";
	
	public static String ali_pay_url ="https://openapi.alipay.com/gateway.do";

	
}
