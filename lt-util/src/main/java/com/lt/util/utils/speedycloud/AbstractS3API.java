package com.lt.util.utils.speedycloud;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.utils.StringTools;


public class AbstractS3API {
    private String host;
    private String access_key;
    private String secret_key;
    private SortedMap<String, String> metadata;
	private final static Logger logger = LoggerFactory.getLogger(AbstractS3API.class);

    public AbstractS3API(String host,String access_key, String secret_key) {
        this.host = host;
        this.access_key = access_key;
        this.secret_key = secret_key;
        this.metadata = new TreeMap<String, String>();
    }

    private String createSignString(String... args) {
        if (args.length == 5) {
            String sign = args[0];
            for (int i = 1; i < 4; i++) {
                sign += "\n" + args[i];
            }
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                sign += "\n" + entry.getKey().toLowerCase() + ":" + entry.getValue();
            }
            sign += "\n" + args[4];
            return sign;
        } else {
            String sign = args[0];
            for (int i = 1; i < 5; i++) {
                sign += "\n" + args[i];
            }
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                sign += "\n" + entry.getKey().toLowerCase() + ":" + entry.getValue();
            }
            sign += "\n" + args[5];
            return sign;
        }
    }

    private String createSign(String... args) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] key = this.secret_key.getBytes();
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        String signString = createSignString(args);
        byte[] data = signString.getBytes();
        
        //String value = Base64.getEncoder().encodeToString(mac.doFinal(data));
        String value = com.lt.util.utils.alipay.alipayTranfer.sign.Base64.encode(mac.doFinal(data));
        logger.info("签名："+value);
        return value;
    }

    private String putData(String method, String url, String data, String type) {
        try {
            URL localURL = new URL(this.host + url);
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = new Date();
            String requestDate = dateFormat.format(date);
            httpURLConnection.setRequestProperty("Date", requestDate);
            try {
                httpURLConnection.setRequestProperty("Authorization", "AWS " + this.access_key + ":" + createSign(method, "", "image/jpeg", requestDate, url));
            } catch (InvalidKeyException e) {
                return e.getMessage();
            } catch (NoSuchAlgorithmException e) {
                return e.getMessage();
            } finally {

            }
            httpURLConnection.setConnectTimeout(10000);
            long contentLength = 0;

            if (type.equals("file")) {
                File file = new File(data);
                if (file.length() > 1024 * 1024 * 1024) {
                    return "File is bigger than 1G!";
                }
                contentLength = file.length();
                httpURLConnection.setRequestProperty("Content-Length", Long.toString(contentLength));
                httpURLConnection.setRequestProperty("Content-Type", "image/jpeg");
                FileInputStream fileInputStream = new FileInputStream(data);
                byte[] buffer = new byte[1024];
                int length = -1;
              
                httpURLConnection.setRequestProperty("Connection", "Close");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                while ((length = fileInputStream.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, length);
                }
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();
            } else {
                byte[] requestStringBytes = data.getBytes();
                contentLength = requestStringBytes.length;
                httpURLConnection.setRequestProperty("Content-Length", Long.toString(contentLength));

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(requestStringBytes);
                outputStream.close();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            reader.close();
            httpURLConnection.disconnect();
            return content;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     * 上传图片
     * @param method
     * @param url
     * @param data
     * @param type
     * @param contentType
     * @return
     */
    private String putImgFile(String method, String url, String data) {
        try {
        	String contentType = null;
        	if(StringTools.isEmpty(data)){
        		return null;
        	}
        	String imgExt = data.substring(data.lastIndexOf("."),data.length()).toLowerCase();
        	if(".jpeg".equals(imgExt) || ".jpg".equals(imgExt)){
        		contentType = "image/jpeg";
        	}else if (".png".equals(imgExt)) {
				contentType = "image/png";
			}else if(".gif".equals(imgExt)){
				contentType = "image/gif";
			}else{
				return null;
			}
            URL localURL = new URL(this.host + url);
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = new Date();
            String requestDate = dateFormat.format(date);
            httpURLConnection.setRequestProperty("Date", requestDate);
            try {
                httpURLConnection.setRequestProperty("Authorization", "AWS " + this.access_key + ":" + createSign(method, "", contentType, requestDate, url));
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return e.getMessage();
            } finally {

            }
            httpURLConnection.setConnectTimeout(10000);
            long contentLength = 0;

            File file = new File(data);
            if (file.length() > 1024 * 1024 * 1024) {
                return "File is bigger than 1G!";
            }
            contentLength = file.length();
            httpURLConnection.setRequestProperty("Content-Length", Long.toString(contentLength));
            httpURLConnection.setRequestProperty("Content-Type", contentType);
            FileInputStream fileInputStream = new FileInputStream(data);
            byte[] buffer = new byte[1024];
            int length = -1;
          
            httpURLConnection.setRequestProperty("Connection", "Close");

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            while ((length = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, length);
            }
            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            logger.info("上传结果:"+content);
            logger.info("header:"+httpURLConnection.getHeaderFields().toString());
            String resultHead = httpURLConnection.getHeaderFields().get(null).toString();
            reader.close();
            httpURLConnection.disconnect();
            if(StringTools.isNotEmpty(resultHead)){
                logger.info("resultHead:{}",resultHead);
            	return resultHead;
            }

            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
 

    /** 上传图片数据**/
    private String putImgData(String method, String url, InputStream inputStream) {
        logger.info("............putImgData.........");
        try {
        	if(inputStream == null){
        	    logger.info("inputStream is null!");
        		return null;
        	}
        	String contentType = null;
        	String imgExt = url.substring(url.lastIndexOf("."),url.length()).toLowerCase();
        	if(".jpeg".equals(imgExt) || ".jpg".equals(imgExt)){
        		contentType = "image/jpeg";
        	}else if (".png".equals(imgExt)) {
				contentType = "image/png";
			}else if(".gif".equals(imgExt)){
				contentType = "image/gif";
			}else{
        	    logger.info("图片格式不对！");
				return null;
			}
            URL localURL = new URL(this.host + url);
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = new Date();
            String requestDate = dateFormat.format(date);
            httpURLConnection.setRequestProperty("Date", requestDate);
            try {
                httpURLConnection.setRequestProperty("Authorization", "AWS " + this.access_key + ":" + createSign(method, "", contentType, requestDate, url));
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return e.getMessage();
            } finally {

            }
            httpURLConnection.setConnectTimeout(10000);
            long contentLength = 0;

            //File file = new File(data);
            if (inputStream.available() > 1024 * 1024 * 10) {
                logger.info("文件内容太大！");
                return "File is bigger than 10M!";
            }
            contentLength = inputStream.available();
            httpURLConnection.setRequestProperty("Content-Length", Long.toString(contentLength));
            httpURLConnection.setRequestProperty("Content-Type", contentType);
            //FileInputStream fileInputStream = new FileInputStream(data);
            byte[] buffer = new byte[1024];
            int length = -1;
          
            httpURLConnection.setRequestProperty("Connection", "Close");

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            while ((length = inputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, length);
            }
            inputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            logger.info("上传结果:"+content);
            logger.info("header:"+httpURLConnection.getHeaderFields().toString());
            String resultHead = httpURLConnection.getHeaderFields().get(null).toString();
            reader.close();
            httpURLConnection.disconnect();
            if(StringTools.isNotEmpty(resultHead)){
                logger.info(resultHead);
            	return resultHead;
            }
            logger.info(content);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    public String request(String method, String url) {
        try {
            URL localURL = new URL(this.host + url);
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = new Date();
            String requestDate = dateFormat.format(date);
            System.out.println(requestDate);
            httpURLConnection.setRequestProperty("Date", requestDate);
            try {
                httpURLConnection.setRequestProperty("Authorization", "AWS " + this.access_key + ":" + createSign(method, "", "", requestDate, url));
            } 
            catch (InvalidKeyException e) {
                return e.getMessage();
            } catch (NoSuchAlgorithmException e) {
                return e.getMessage();
            } finally {
//                httpURLConnection.disconnect();
            }
            httpURLConnection.setConnectTimeout(10000);
            System.out.println("响应："+httpURLConnection.getResponseCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            System.out.println("request:"+line);
            reader.close();
            httpURLConnection.disconnect();
            return content;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public String requestUpdate(String method, String url, String acl) {
        try {
            URL localURL = new URL(this.host + url);
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setRequestProperty("X-Amz-Acl", acl);
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            httpURLConnection.setRequestMethod(method);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = new Date();
            String requestDate = dateFormat.format(date);
            httpURLConnection.setRequestProperty("Date", requestDate);
            try {
                //httpURLConnection.setRequestProperty("Authorization", "AWS " + this.access_key + ":" + createSign(method, "", "", acl, requestDate, url));
            	httpURLConnection.setRequestProperty("Authorization", "AWS " + this.access_key + ":" + createSign(method, "", "", requestDate,"x-amz-acl:"+acl, url));
            } catch (InvalidKeyException e) {
                return e.getMessage();
            } catch (NoSuchAlgorithmException e) {
                return e.getMessage();
            } finally {
                httpURLConnection.disconnect();
            }
            httpURLConnection.setConnectTimeout(10000);
            System.out.println(httpURLConnection.getResponseCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            System.out.println("requestUpdate:"+line);
            reader.close();
            httpURLConnection.disconnect();
            logger.info("header:"+httpURLConnection.getHeaderFields().toString());
            String resultHead = httpURLConnection.getHeaderFields().get(null).toString();
            if(StringTools.isNotEmpty(resultHead)){
            	return resultHead;
            }
            return content;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public String putKeyFromFile(String method, String url, String path) {
        return putData(method, url, path, "file");
    }
    
    public String putKeyFromImgFile(String method, String url, String path) {
    	return putImgFile(method, url, path);
    }
    
    public String putKeyFromImgData(String method, String url, InputStream inStream) {
    	return putImgData(method, url, inStream);
    }

    public String putKeyFromString(String method, String url, String requestString) {
        return putData(method, url, requestString, "string");
    }

    public String putString(String method, String url, String requestString) {
        return putData(method, url, requestString, "string");
    }

    public void setMeta(String key, String value) {
        String first = key.substring(0, 1).toUpperCase();
        String rest = key.substring(1, key.length());
        this.metadata.put("X-Amz-Meta-" + first + rest, value);
    }
}
