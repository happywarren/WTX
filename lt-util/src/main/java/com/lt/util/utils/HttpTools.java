package com.lt.util.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.lt.util.utils.swiftpass.util.XmlUtils;

/**
 * Http请求管理 说明：
 * 
 * @author zheng_zhi_rui@163com
 * @date 2015年3月25日
 *
 */
public class HttpTools {
	private static final int TIMEOUT = 5000;
	private static Logger logger = LoggerFactory.getLogger(HttpTools.class);
	/**
	 * Get请求，兼容https
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	public static JSONObject doGet(String url, Map<String, String> params) {
		if(StringTools.isBlankOrEmpty(url)){
			return null;
		}
		if(url.startsWith("https")){
			return doSSLGet(url, params);
		}
		
		CloseableHttpClient httpClient = HttpClients.custom().build();
		JSONObject jsonData = null;
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT)
					.setConnectionRequestTimeout(TIMEOUT).build();
			URI uri = null;
			URIBuilder uriBuilder = new URIBuilder();
			if (params != null && params.size() > 0) {
				Set<String> keys = params.keySet();
				Iterator<String> it = keys.iterator();
				for (; it.hasNext();) {
					String key = it.next();
					String val = params.get(key);
					uriBuilder.setParameter(key, val);
				}
			}
			uri = uriBuilder.build();

			HttpGet httpGet = new HttpGet(url + uri);
			// System.out.println(url + uri);
			httpGet.setConfig(requestConfig);

			jsonData = httpClient.execute(httpGet, createResponseHandler());
		} catch (Exception e1) {
			e1.printStackTrace();
			return jsonData;
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonData;
	}

	/**
	 * POST请求，兼容https
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static JSONObject doPost(String url, Map<String, String> params) {
		if(StringTools.isBlankOrEmpty(url)){
			return null;
		}
		if(url.startsWith("https")){
			return doSSLPost(url, params);
		}
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT)
				.setConnectionRequestTimeout(TIMEOUT).build();

		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		logger.info("invoke other service,url ---> {}", url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		JSONObject jsonData = null;
		CloseableHttpClient httpClient = HttpClients.custom().build();
		try {
			jsonData = httpClient.execute(httpPost, createResponseHandler());
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonData;
		} finally {
			try {
				if(httpClient != null){
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jsonData;
	}

	/**
	 * POST请求，传输二进制数据
	 *
	 * @param url
	 * @param content
	 * @return
	 */
	public static String writeBinaryPost(String url, Map<String, String> params, String content, String charset,
			boolean encode) {
		try {
			/*
			 * 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
			 */
			String get_url = url;
			if (url != null && url.lastIndexOf("?") == -1)
				get_url += "?";
			if (params != null && params.size() > 0)
				for (String key : params.keySet()) {
					get_url += key + "=";
					if (encode) {
						get_url += URLEncoder.encode(params.get(key), charset) + "&";
					} else {
						get_url += params.get(key) + "&";
					}
				}
			if (params != null && params.size() > 0)
				get_url = get_url.substring(0, get_url.lastIndexOf('&'));
			URL postUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("contentType", charset);
			connection.setRequestProperty("Content-Type", "binary/octet-stream");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			/* out.writeBytes(); 在转换的时候会造成中文乱码 */
			out.write(content.getBytes(charset));
			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
			String line = "";
			String responseContent = "";
			for (; (line = reader.readLine()) != null;) {
				responseContent += line;
			}
			reader.close();
			connection.disconnect();
			return responseContent;

		} catch (Exception e) {
			return "";
		}

	}

	public static JSONObject doPost(String url, Map<String, String> params, Map<String, String> header) {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT)
				.setAuthenticationEnabled(true).setConnectionRequestTimeout(TIMEOUT).build();

		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}

		HttpPost httpPost = new HttpPost(url);
		if (header != null && header.size() > 0) {
			Set<String> keySet = header.keySet();
			for (String key : keySet) {
				httpPost.addHeader(key, header.get(key));
			}
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		httpPost.setConfig(requestConfig);

		JSONObject jsonData = null;
		CloseableHttpClient httpClient = HttpClients.custom().build();
		try {
			jsonData = httpClient.execute(httpPost, createResponseHandler());
		} catch (Exception e) {
			e.printStackTrace();
			return jsonData;
		} finally {
			try {
				if(httpClient != null){
					httpClient.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonData;
	}

	/**
	 * GET 请求 HTTPURLConnection实现
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String urlGet(String url, Map<String, String> params, String charset, boolean encode) {
		try {
			/*
			 * 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
			 */
			String get_url = url;
			String paramStr = "";
			if (params != null && params.size() > 0) {
				for (String key : params.keySet()) {
					paramStr += key + "=";
					if (encode) {
						paramStr += URLEncoder.encode(params.get(key), charset) + "&";
					} else {
						paramStr += params.get(key) + "&";
					}
				}
			}
			if (params != null && params.size() > 0) {
				paramStr = paramStr.substring(0, paramStr.lastIndexOf('&'));
				if (url != null && url.lastIndexOf("?") == -1) {
					get_url += "?" + paramStr;
				}
			}
			URL getUrl = new URL(get_url);
			/*
			 * 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
			 * 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
			 */
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			/*
			 * 进行连接，但是实际上get
			 * request要在下一句的connection.getInputStream()函数中才会真正发到服务器
			 */
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
			String line = "";
			String responseContent = "";
			for (; (line = reader.readLine()) != null;) {
				responseContent += line;
			}
			reader.close();
			connection.disconnect();
			return responseContent;
		} catch (Exception e) {
			// logger.error("msg:"+e);
			// e.printStackTrace();
			return "";
		}

	}

	/**
	 * POST 请求 HTTPURLConnection实现
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String urlPost(String url, Map<String, String> params, String charset, boolean lineFlag) {
		try {
			logger.info("invoke other service ,url-->{}", url);
			URL postUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			/*
			 * 设置是否向connection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true
			 */
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			/*
			 * Post 请求不能使用缓存
			 */
			connection.setUseCaches(false);
			/* URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。 */
			/* URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数 */
			connection.setInstanceFollowRedirects(true);
			/*
			 * 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
			 * 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
			 * 进行编码
			 */
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			/*
			 * 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成.
			 * 要注意的是connection.getOutputStream会隐含的进行connect。
			 */
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			/*
			 * 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
			 */
			String content = "";
			if (params != null && params.size() > 0)
				for (String key : params.keySet()) {
					content += key + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&";
				}
			if (params != null && params.size() > 0)
				content = content.substring(0, content.lastIndexOf('&'));
			/*
			 * DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
			 */
			out.writeBytes(content);
			out.flush();
			out.close();
			/*
			 * 设置编码,否则中文乱码
			 */
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
			String line = "";
			String responseContent = "";
			for (; (line = reader.readLine()) != null;) {
				if (lineFlag) {
					responseContent += "\r\n" + line;
				} else {
					responseContent += line;
				}
			}
			reader.close();
			connection.disconnect();
			return responseContent;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * SSL安全认证请求 测试方法
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static void SSLPost(String url, String params) {
		final String KEYSTORE_FILE = "";
		final String TRUSTSTORE_FILE = "";
		final String KEYSTORE_PASSWORD = "";

		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			File file = new File("");
			FileInputStream fis = new FileInputStream(file);

			keyStore.load(fis, "nopassword".toCharArray());
			fis.close();

			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
					.build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			try {
				HttpPost httpget = new HttpPost(url);
				CloseableHttpResponse response = httpclient.execute(httpget);
				try {
					HttpEntity entity = response.getEntity();
					EntityUtils.consume(entity);
				} finally {
					response.close();
				}
			} finally {
				httpclient.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String httpClientGet(String url) {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(url);
		String res = "";
		try {
			int st = client.executeMethod(get);
			if (st == 200) {
				InputStream in = get.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				res = URLDecoder.decode(baos.toString(), "UTF-8");
			} else {// 请求失败，重新请求一遍
				st = client.executeMethod(get);
				InputStream in = get.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				res = URLDecoder.decode(baos.toString(), "UTF-8");;
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	private static ResponseHandler<JSONObject> createResponseHandler() {
		ResponseHandler<JSONObject> jsonHandler = new ResponseHandler<JSONObject>() {

			public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				// StatusLine statusCode = response.getStatusLine();
				// if (statusCode.getStatusCode() >= 300) {
				// throw new HttpResponseException(statusCode.getStatusCode(),
				// statusCode.getReasonPhrase());
				// }
				HttpEntity httpEntity = response.getEntity();
				if (httpEntity == null) {
					throw new ClientProtocolException("Response contains no content");
				}
				ContentType contentType = ContentType.getOrDefault(httpEntity);
				Charset charset = contentType.getCharset();
				if (charset == null) {
					charset = Consts.UTF_8;
				}

				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
						"UTF-8"));
				StringBuffer buffer = new StringBuffer();
				String line = "";
				for (; (line = reader.readLine()) != null;) {
					buffer.append(line + "\n");
				}
				// System.out.println(buffer.toString());
				return JSONObject.parseObject(buffer.toString());
			}
		};
		return jsonHandler;
	}
	private static ResponseHandler<String> createResponseStringHandler() {
		ResponseHandler<String> stringHandler = new ResponseHandler<String>() {
			
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				// StatusLine statusCode = response.getStatusLine();
				// if (statusCode.getStatusCode() >= 300) {
				// throw new HttpResponseException(statusCode.getStatusCode(),
				// statusCode.getReasonPhrase());
				// }
				HttpEntity httpEntity = response.getEntity();
				if (httpEntity == null) {
					throw new ClientProtocolException("Response contains no content");
				}
				ContentType contentType = ContentType.getOrDefault(httpEntity);
				Charset charset = contentType.getCharset();
				if (charset == null) {
					charset = Consts.UTF_8;
				}
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
						"UTF-8"));
				StringBuffer buffer = new StringBuffer();
				String line = "";
				for (; (line = reader.readLine()) != null;) {
					buffer.append(line + "\n");
				}
				// System.out.println(buffer.toString());
				return buffer.toString();
			}
		};
		return stringHandler;
	}
	/**
	 * 发送https协议的Get请求
	 * @param url
	 * @return
	 */
	public static JSONObject doSSLGet(String url, Map<String, String> params) {
		CloseableHttpClient httpClient = createSSLClientDefault();
		JSONObject jsonData = null;
		try {
			//定义
			HttpGet httpGet = new HttpGet();
			
			//设置URL
			URIBuilder uriBuilder = new URIBuilder(url);
			if(params != null){
				for (String key : params.keySet()) {
					uriBuilder.setParameter(key, params.get(key));
				}
			}
			URI uri = uriBuilder.build();			
			httpGet.setURI(uri);	
			
			//设置连接超时、响应时间
			RequestConfig config = RequestConfig.custom()  
			        .setSocketTimeout(TIMEOUT)  
			        .setConnectTimeout(TIMEOUT)
			        .build();			
			httpGet.setConfig(config);
			
			//执行请求，获取响应
			logger.debug("executing request" + httpGet.getRequestLine());
			jsonData = httpClient.execute(httpGet, createResponseHandler());
			
			return jsonData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(httpClient != null){
					httpClient.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonData;
	}
	/**
	 * 发送https协议的Post请求
	 * @param url
	 * @return
	 */
	public static JSONObject doSSLPost(String url, Map<String, String> params) {
		CloseableHttpClient httpClient = createSSLClientDefault();
		JSONObject jsonData = null;
		try {
			//定义
			HttpPost httpPost = new HttpPost();
			
			//设置URL
			URIBuilder uriBuilder = new URIBuilder(url);
			if(params != null){
				for (String key : params.keySet()) {
					uriBuilder.setParameter(key, params.get(key));
				}
			}
			URI uri = uriBuilder.build();			
			httpPost.setURI(uri);	
			
			//设置连接超时、响应时间
			RequestConfig config = RequestConfig.custom()  
			        .setSocketTimeout(TIMEOUT)  
			        .setConnectTimeout(TIMEOUT)
			        .build();			
			httpPost.setConfig(config);
			
			//执行请求，获取响应
			logger.debug("executing request" + httpPost.getRequestLine());
			jsonData = httpClient.execute(httpPost, createResponseHandler());
			
			return jsonData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(httpClient != null){
					httpClient.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonData;
	}
	/**
	 * 信任所有证书
	 * @return
	 */
	private static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有
						@Override
						public boolean isTrusted(X509Certificate[] arg0,
								String arg1) throws CertificateException {
							// TODO Auto-generated method stub
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}


	public static Map<String,String> doPostXML(String reqUrl,SortedMap<String,String> map){
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		Map<String,String> resultMap = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if(response != null && response.getEntity() != null){
				resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
				return resultMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return resultMap;
		} finally {
			try {
				if(response != null){
					response.close();
				}
				if(client != null){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}

	/**
	 * POST请求，兼容https
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String sendPost(String url, Map<String, Object> params) {
		if(StringTools.isBlankOrEmpty(url)){
			return null;
		}
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT)
				.setConnectionRequestTimeout(TIMEOUT).build();

		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				paramList.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
			}
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		logger.info("invoke other service,url ---> {}", url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String resultData = null;
		CloseableHttpClient httpClient = HttpClients.custom().build();
		try {
			resultData = httpClient.execute(httpPost, createResponseStringHandler());
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultData;
		} finally {
			try {
				if(httpClient != null){
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultData;
	}	
}
