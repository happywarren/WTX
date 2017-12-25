package com.lt.util.utils.youdun;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class BankCardTools {
	private static String fformatStr = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/%s/product_code/%s/out_order_id/%s/signature/%s";

	public static String apiCall(String url, String pubkey, String secretkey, String serviceCode, String outOrderId,
			Map<String, String> parameter) throws Exception {
		if (parameter == null || parameter.isEmpty())
			throw new Exception("error ! the parameter Map can't be null.");

		StringBuffer bodySb = new StringBuffer("{");
		for (Map.Entry<String, String> entry : parameter.entrySet()) {
			bodySb.append("'").append(entry.getKey()).append("':'").append(entry.getValue()).append("',");
		}
		String bodyStr = bodySb.substring(0, bodySb.length() - 1) + "}";
		String signature = md5(bodyStr + "|" + secretkey);
		url = String.format(fformatStr, pubkey, serviceCode, outOrderId, signature);
		System.out.println("requestUrl=>" + url);
		System.out.println("request parameter body=>" + bodyStr);
		HttpResponse r = makePostRequest(url, bodyStr);
		return EntityUtils.toString(r.getEntity());
	}

	private static final CloseableHttpClient client = HttpClientBuilder.create().build();

	private static HttpResponse makePostRequest(String uri, String jsonData)
			throws ClientProtocolException, IOException {

		HttpPost httpPost = new HttpPost(URIUtil.encodeQuery(uri, "utf-8"));
		httpPost.setEntity(new StringEntity(jsonData, "UTF-8"));
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json; charset=utf-8");
		return client.execute(httpPost);

	}

	private static String md5(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data.toString().getBytes());
		return bytesToHex(md.digest());
	}

	private static String bytesToHex(byte[] ch) {
		StringBuffer ret = new StringBuffer("");
		for (int i = 0; i < ch.length; i++)
			ret.append(byteToHex(ch[i]));
		return ret.toString();
	}

	/**
	 * 字节转换为16进制字符串
	 */
	private static String byteToHex(byte ch) {
		String str[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		return str[ch >> 4 & 0xF] + str[ch & 0xF];
	}
}
