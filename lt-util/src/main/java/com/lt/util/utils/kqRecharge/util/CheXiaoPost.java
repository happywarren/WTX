package com.lt.util.utils.kqRecharge.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.lt.util.utils.kqRecharge.kqutil.Base64Binrary;
import com.lt.util.utils.kqRecharge.kqutil.MyX509TrustManager;



public class CheXiaoPost
{
	  /**=============================测试数据=============================================**/
  public static final String PFX_KEYSTORE_FILE = "/81205154511001190.pfx";//pfx文件位置
  public static final String KEYSTORE_PASSWORD = "123456";//导出为pfx文件的设的密码 
  public static final String JKS_KEYSTORE_FILE = "/81205154511001190.jks"; //jks文件位置
  public static final String AUTH_FILE_PREFIX = "812051545110011";//设置通用的请求属性 ：商户名
  /**=============================测试数据=============================================**/
  /**=============================正式数据=============================================**/
//	  public static final String PFX_KEYSTORE_FILE = "/81231006011004690.pfx";//pfx文件位置
//	  public static final String KEYSTORE_PASSWORD = "vpos123";//导出为pfx文件的设的密码 
//	  public static final String JKS_KEYSTORE_FILE = "/81231006011004690.jks"; //jks文件位置
//	  public static final String AUTH_FILE_PREFIX = "812310060110046";//设置通用的请求属性 ：商户名
/**=============================正式数据=============================================**/
	  
	  
	public static String sendPost(String url,String tr1XML) throws Exception
	{
		OutputStream out = null;
		
		String respXml = "";
		String respXmlCut = "";
		String respXmlCut2 = "";
		
		//作为源码本地运行，读取资源文件没有问题；但如果改写打包成jar，则下面方法需要改写，否则读取不到资源文件
//		String certPath = Post.class.getResource("/81205154511001190.jks").toURI().getPath();
//		File certFile = new File(certPath);
		KeyStore ks = KeyStore.getInstance("JKS");
//		ks.load(new FileInputStream(certFile), "123456".toCharArray());
		
		InputStream inputStream=Post.class.getResourceAsStream(JKS_KEYSTORE_FILE);
		ks.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
		
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());
		TrustManager[] tm = { new MyX509TrustManager() }; 
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(kmf.getKeyManagers(),tm, null);
		SSLSocketFactory factory = sslContext.getSocketFactory();
		
		try
	    {
		    URL realUrl = new URL(url);
		    HttpsURLConnection conn = (HttpsURLConnection)realUrl.openConnection();
			conn.setSSLSocketFactory(factory);
		    conn.setDoOutput(true);
		    conn.setDoInput(true);
		    conn.setReadTimeout(100000);
		    
			String authString = AUTH_FILE_PREFIX + ":" + KEYSTORE_PASSWORD;
			String auth = "Basic " + Base64Binrary.encodeBase64Binrary(authString.getBytes());
		    conn.setRequestProperty("Authorization", auth);
		    
		    // 获取URLConnection对象对应的输出流
		    out = conn.getOutputStream();
		    //发送请求参数
		    out.write(tr1XML.getBytes());
		    out.flush();
	        
		    //得到服务端返回
		    InputStream is = conn.getInputStream();
		    String reqData = "";
		    if(is!=null && !"".equals(is))
		    {
		    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] receiveBuffer = new byte[2048];
				int readBytesSize = is.read(receiveBuffer);
				System.out.println("readBytesSize："+readBytesSize);
					
				while(readBytesSize != -1){
					bos.write(receiveBuffer, 0, readBytesSize);
					readBytesSize = is.read(receiveBuffer);
					
				}
				reqData = new String(bos.toByteArray(), "UTF-8");//编码后的tr2报文
		    }
		    //System.out.println("tr2报文："+reqData);
			//respXml= ParseUtil.parseXML(reqData);//给解析XML的函数传递快钱返回的TR2的XML数据流
		    respXml = reqData.replaceAll("\"","\'");
		    System.out.println("tr2报文转换之后："+respXml);
		    respXmlCut = respXml.replace("<?xml version='1.0' encoding='UTF-8' standalone='yes'?><MasMessage xmlns='http://www.99bill.com/mas_cnp_merchant_interface'><version>1.0</version>","");
		    respXmlCut2 = respXmlCut.replace("</MasMessage>","");
		    //System.out.println("获取应答码："+respXml.substring(respXml.indexOf("<responseCode>")+14,respXml.indexOf("</responseCode>")));
		   // System.out.println("tr2报文剪切之后："+respXmlCut2);
	    }
	    catch(Exception e)
	    {
		    System.out.println("发送POST请求出现异常！" + e);
		    e.printStackTrace();
	    }
	    //使用finally块来关闭输出流、输入流
	    finally
	    {
	    	if (out != null){out.close();}
			//if (in != null){in.close();}
	    }
	    return respXml;
	}
	public static void main(String[] args) throws Exception {
    	
       
    }
}