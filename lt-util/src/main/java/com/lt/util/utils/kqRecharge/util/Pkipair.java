package com.lt.util.utils.kqRecharge.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class Pkipair {
	
	/**=============================测试数据=============================================**/
	public static final String KEYSTORE_PASSWORD = "123456";//导出为pfx文件的设的密码 
	/**=============================测试数据=============================================**/
	
	/**=============================正式数据=============================================**/
//	public static final String KEYSTORE_PASSWORD = "vpos123";//导出为pfx文件的设的密码 
	/**=============================正式数据=============================================**/
	 
	 
	 
	 
	public String signMsg( String signMsg) {

		String base64 = "";
		try {
			// 密钥仓库
			KeyStore ks = KeyStore.getInstance("PKCS12");

			// 读取密钥仓库
//			FileInputStream ksfis = new FileInputStream("e:/tester-rsa.pfx");
			
			// 读取密钥仓库（相对路径）
			//作为源码本地运行，读取资源文件没有问题；但如果改写打包成jar，则下面方法需要改写，否则读取不到资源文件
//			String file = Pkipair.class.getResource("/tester-rsa.pfx").getPath().replaceAll("%20", " ");
//			FileInputStream ksfis = new FileInputStream(file);
//			BufferedInputStream ksbufin = new BufferedInputStream(ksfis);

			InputStream inputStream=Post.class.getResourceAsStream("/tester-rsa.pfx");
			
			char[] keyPwd = KEYSTORE_PASSWORD.toCharArray();
			//char[] keyPwd = "YaoJiaNiLOVE999Year".toCharArray();
			ks.load(inputStream, keyPwd);
			// 从密钥仓库得到私钥
			PrivateKey priK = (PrivateKey) ks.getKey("test-alias", keyPwd);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(priK);
			signature.update(signMsg.getBytes("utf-8"));
			sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			base64 = encoder.encode(signature.sign());
			
		} catch(FileNotFoundException e){
			System.out.println("文件找不到");
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("test = "+base64);
		return base64;
	}
	public boolean enCodeByCer( String val, String msg) {
		boolean flag = false;
		try {
			//获得文件(绝对路径)
			//InputStream inStream = new FileInputStream("e:/99bill[1].cert.rsa.20140803.cer");
			
			//获得文件(相对路径)
			//作为源码本地运行，读取资源文件没有问题；但如果改写打包成jar，则下面方法需要改写，否则读取不到资源文件
//			String file = Pkipair.class.getResource("/mgw.cer").toURI().getPath();
//			FileInputStream inStream = new FileInputStream(file);
			
			InputStream inputStream=Post.class.getResourceAsStream("/mgw.cer");
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
			//获得公钥
			PublicKey pk = cert.getPublicKey();
			//签名
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(pk);
			signature.update(val.getBytes());
			//解码
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			System.out.println(new String(decoder.decodeBuffer(msg)));
			flag = signature.verify(decoder.decodeBuffer(msg));
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("no");
		} 
		return flag;
	}
}
