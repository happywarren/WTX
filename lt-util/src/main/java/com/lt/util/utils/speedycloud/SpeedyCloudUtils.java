package com.lt.util.utils.speedycloud;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.file.FileLoader;

import javolution.util.FastMap;

public class SpeedyCloudUtils {
	private final static String module = SpeedyCloudUtils.class.getName();
	private final static Logger logger = LoggerFactory.getLogger(SpeedyCloudUtils.class);
	public static SpeedyCloudS3 client = null;
	public static String host = "http://cos.speedycloud.org";
	public static String accessKey = "D862B7822119A3D797CE47F1604AAE63";
	public static String secretKey = "d450ec3b19d40a6b0e738378548e18c9316f8c2e0bd6e4af00af8d008dc14d04";
	public static String bucket = "cainiu-lt";

	private static SpeedyCloudUtils cloudUtils;

	public static String getSecretKey() {
		return secretKey;
	}

	public static void setSecretKey(String secretKey) {
		SpeedyCloudUtils.secretKey = secretKey;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		SpeedyCloudUtils.host = host;
	}

	public static String getAccessKey() {
		return accessKey;
	}

	public static void setAccessKey(String accessKey) {
		SpeedyCloudUtils.accessKey = accessKey;
	}

	public static String getBucket() {
		return bucket;
	}

	public static void setBucket(String bucket) {
		SpeedyCloudUtils.bucket = bucket;
	}

	public static SpeedyCloudUtils getSpeedyCloudUtils() {
		if (cloudUtils == null) {
			synchronized (SpeedyCloudUtils.class) {
				if (cloudUtils == null) {
					cloudUtils = new SpeedyCloudUtils();
				}
			}
		}
		return cloudUtils;
	}

	public static SpeedyCloudS3 init() {
		client = new SpeedyCloudS3(host, accessKey, secretKey);
		return client;
	}

	/**
	 * 
	 * @param url
	 *            上传地址
	 * @param mybucket
	 *            上传桶
	 * @param key
	 *            文件存储名字
	 * @param path
	 *            上传的文件路径
	 * @return
	 */
	public synchronized Map<String, Object> uploadImage(String key, String path) {
		Map<String, Object> result = FastMap.newInstance();
		try {
			/** 上传图片 **/
			String ret1 = client.putObjectFromImgFile(bucket, key, path);
			/** 修改权限地址 **/
			String ret2 = client.updateKeyAcl(bucket, key, "public-read");

			if (ret1.contains("HTTP/1.1 200 OK") && (ret2.contains("HTTP/1.1 200 OK"))) {
				logger.info("ret1:{},ret2:{}", ret1, ret2);
			} else {
				logger.info("上传图片失败!");
				result.put("resultId", "FAILURE");
				return result;
			}

			String filePath = host + "/" + bucket + "/" + key;
			logger.info("图片地址：" + filePath);
			result.put("resultId", "SUCCESS");
			result.put("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(module);
			result.put("resultId", "ERROR");
		}
		return result;
	}

	public synchronized Map<String, Object> uploadImage(String key, InputStream inputStream) {
		Map<String, Object> result = FastMap.newInstance();
		try {
			/** 上传图片 **/
			String ret1 = client.putObjectFromImgData(bucket, key, inputStream);
			/** 修改权限地址 **/
			String ret2 = client.updateKeyAcl(bucket, key, "public-read");

			if (ret1.contains("HTTP/1.1 200 OK") && (ret2.contains("HTTP/1.1 200 OK"))) {
				logger.info("ret1:{},ret2:{}", ret1, ret2);
			} else {
				logger.info("上传图片失败!");
				result.put("resultId", "FAILURE");
				return result;
			}

			String filePath = host + "/" + bucket + "/" + key;
			logger.info("图片地址：" + filePath);
			result.put("resultId", "SUCCESS");
			result.put("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(module);
			result.put("resultId", "ERROR");
		}
		return result;
	}
	
	public synchronized Map<String, Object> uploadImageFromUrl(String key, String urlStr) {
		Map<String, Object> result = FastMap.newInstance();
		try {
			InputStream inputStream = FileLoader.getFileLoader().loadResource(urlStr);
			/** 上传图片 **/
			String ret1 = client.putObjectFromImgData(bucket, key, inputStream);
			/** 修改权限地址 **/
			String ret2 = client.updateKeyAcl(bucket, key, "public-read");

			if (ret1.contains("HTTP/1.1 200 OK") && (ret2.contains("HTTP/1.1 200 OK"))) {
				logger.info("ret1:{},ret2:{}", ret1, ret2);
			} else {
				logger.info("上传图片失败!");
				result.put("resultId", "FAILURE");
				return result;
			}

			String filePath = host + "/" + bucket + "/" + key;
			logger.info("图片地址：" + filePath);
			result.put("resultId", "SUCCESS");
			result.put("filePath", filePath);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(module);
			result.put("resultId", "ERROR");
		}
		return result;
	}
	
	public synchronized Map<String, Object> uploadImageFromUrl( String urlStr) {
		Map<String, Object> result = FastMap.newInstance();
		String key = System.currentTimeMillis()+".jpg";
		try {
			InputStream inputStream = FileLoader.getFileLoader().loadResource(urlStr);
			/** 上传图片 **/
			String ret1 = client.putObjectFromImgData(bucket, key, inputStream);
			/** 修改权限地址 **/
			String ret2 = client.updateKeyAcl(bucket, key, "public-read");

			if (ret1.contains("HTTP/1.1 200 OK") && (ret2.contains("HTTP/1.1 200 OK"))) {
				logger.info("ret1:{},ret2:{}", ret1, ret2);
			} else {
				logger.info("上传图片失败!");
				result.put("resultId", "FAILURE");
				return result;
			}

			String filePath = host + "/" + bucket + "/" + key;
			logger.info("图片地址：" + filePath);
			result.put("resultId", "SUCCESS");
			result.put("filePath", filePath);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(module);
			result.put("resultId", "ERROR");
		}
		return result;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		init();
		String key = System.currentTimeMillis() + ".jpg";
		String path = "https://idsafe-auth.udcredit.com/front/4.0/api/file_download/platform/web?name=i_20170824172617368339567.jpg&param=8664FD3F90B782E36068ADFD541221F567EE6810F6AA4A44F61A27F76F93DF";
		//String path = "http://cos.speedycloud.org/cainiu-lt/1503567656904.jpg";
		
		//String path = "c:/img.JPG";
		SpeedyCloudUtils.getSpeedyCloudUtils().uploadImageFromUrl(key, path);

		//SpeedyCloudUtils.getSpeedyCloudUtils().uploadImage(key, path);
		//File file = new File("c:/img.JPG");
		// FileInputStream inputStream = new FileInputStream(file);
		// SpeedyCloudUtils.uploadImage(key, inputStream);
	}
}
