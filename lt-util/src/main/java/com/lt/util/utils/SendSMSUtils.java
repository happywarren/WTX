package com.lt.util.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class SendSMSUtils {
	
	public static String http(String url, NameValuePair[] param) {
		try {
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			method.setRequestBody(param);
			int statusCode = client.executeMethod(method);
			System.out.println(statusCode);
			StringBuilder sb = new StringBuilder();
			InputStream ins = null;
			if (statusCode == HttpStatus.SC_OK) {
				ins = method.getResponseBodyAsStream();
				byte[] b = new byte[1024];
				int r_len = 0;
				while ((r_len = ins.read(b)) > 0) {
					sb.append(new String(b, 0, r_len, method.getResponseCharSet()));
				}
			}
//			System.out.println(sb.toString());
			String strs = sb.toString();
			String str = null;
			if (strs != null && strs.contains("</string>")) {
				str = strs.split(">")[2].split("<")[0];
//				System.out.println(str);
//				String s = strs.substring(strs.length() - 28, strs.length() - 9);
//				System.out.println(s);
			}
			method.releaseConnection();
			return str;
		} catch (HttpException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param uri 应用地址，类似于http://ip:port/msg/
	 * @param account 账号
	 * @param pswd 密码
	 * @param mobiles 手机号码，多个号码使用","分割
	 * @param content 短信内容
	 * @param needstatus 是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 */
	public static String send(String uri, String account, String pswd, String mobiles, String content,
			boolean needstatus, String product, String extno) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(uri, false);
			method.setURI(new URI(base, "HttpSendSM", false));
			method.setQueryString(new NameValuePair[] { 
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd), 
					new NameValuePair("mobile", mobiles),
					new NameValuePair("needstatus", String.valueOf(needstatus)), 
					new NameValuePair("msg", content),
					new NameValuePair("product", product), 
					new NameValuePair("extno", extno), 
				});
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}

	}

	/**
	 * 创蓝发送短信 返回发送状态
	 * @param uri 应用地址，类似于http://ip:port/msg/
	 * @param account 账号
	 * @param pswd 密码
	 * @param mobiles 手机号码，多个号码使用","分割
	 * @param content 短信内容
	 * @param needstatus 是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 */
	public static String batchSend(String uri, String account, String pswd, String mobiles, String content,
			boolean needstatus, String product, String extno) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		try {
			System.out.println("url: "+ uri);
			URI base = new URI(uri, false);
			method.setURI(new URI(base, "HttpBatchSendSM", false));
			method.setQueryString(new NameValuePair[] { 
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd), 
					new NameValuePair("mobile", mobiles),
					new NameValuePair("needstatus", String.valueOf(needstatus)), 
					new NameValuePair("msg", content),
					new NameValuePair("product", product),
					new NameValuePair("extno", extno), 
				});
			System.out.println("url: "+ method.getURI());
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	/**
	 * 
	 *	
	 * 描述:查询余额
	 *
	 * @author  郭达望
	 * @created 2015年12月28日 下午12:52:51
	 * @since   v1.0.0 
	 * @param uri
	 * @param account
	 * @param pswd
	 * @param mobiles
	 * @param content
	 * @param needstatus
	 * @param product
	 * @param extno
	 * @return
	 * @throws Exception
	 * @return  String
	 */
	public static String QueryBalance(String uri, String account, String pswd) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(uri, false);
			method.setURI(new URI(base, "QueryBalance", false));
			method.setQueryString(new NameValuePair[] { 
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd), 
				});
			System.out.println("url: "+ method.getURI());
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	public static void main(String[] args) {
		String url = "http://222.73.117.169/msg/";// 应用地址
		String account = "VIP_laikeying";// 账号
		String pswd = "Tch123456";// 密码
		String mobile = "18072865329";// 手机号码，多个号码使用","分割,13575727399,13606526192
		String msg = "推荐一款比彩票还好玩的APP，盈利不止125%。免费送你150元现金红包，退订回复TD。http://t.cn/R4qzVNl";// 短信内容
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String product = null;// 产品ID
		String extno = null;// 扩展码

		try {
			String returnString = SendSMSUtils.QueryBalance(url, account, pswd);
			System.out.println(returnString);
			if(returnString.contains("\n")){
				System.out.println(returnString.split("\n")[1].trim());
				String str = returnString.split("\n")[1].trim();
				if(str !=null&&str.contains(",")){
					str = str.split(",")[1].trim();
					System.out.println(str);
				}
			}
			// TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {
			// TODO 处理异常
			e.printStackTrace();
		}
	}
}
