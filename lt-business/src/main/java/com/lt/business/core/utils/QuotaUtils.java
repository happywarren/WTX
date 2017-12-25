package com.lt.business.core.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.business.core.server.QuotaInitializeServer;
import com.lt.util.utils.DateTools;

/**
 * 行情日志写到磁盘相关方法
 * @author guodw
 *
 */
public class QuotaUtils {

	static Logger logger = LoggerFactory.getLogger(QuotaUtils.class);
	/**
	 * 写文件使用一个线程
	 */
	public static ExecutorService executor = Executors
			.newFixedThreadPool(10);
	/**
	 * .log日志文件 所有记录
	 * 
	 * @param data
	 * @param propCfg
	 * @param product
	 * @throws Exception
	 */
	public static void wirteFileLog(String data, String product)
			throws Exception {
		data = DateTools.parseToTradeTimeStamp(new Date()) + "@" + data
				+ "\r\n";
		String directory = QuotaInitializeServer.getPath() + product;
		createDirectory(directory);
		String path = directory
				+ System.getProperties().getProperty("file.separator")
				+ product + "_"+DateTools.parseToDefaultDateString(new Date()) + QuotaInitializeServer.getPathEndFlagLog();
		// 追加写入文件
		write(path, data);
	}

	/**
	 * .fst日志文件 所有记录
	 * 
	 * @param data
	 * @param product
	 * @throws Exception
	 */
	public static void wirteFileFst(String data, String product)
			throws Exception {
		String directory = QuotaInitializeServer.getPath() + product;
		createDirectory(directory);
		String path = directory
				+ System.getProperties().getProperty("file.separator")
				+ product+"_"+DateTools.parseToDefaultDateString(new Date()) + QuotaInitializeServer.getPathEndFlagFST();
		// 追加写入文件
		File file = new File(path);
		// 如果文件不存在则创建
		if (file.exists()) {
			data = "," + data;
		}
		write(path, data);
	}


	public static void createDirectory(String path) {
		if (null != path && path.contains("/")) {
			String[] strs = path.split("/");
			StringBuffer sb = new StringBuffer();
			boolean bool = false;
			for (String str : strs) {
				if (bool) {
					sb.append("/");
				}
				sb.append(str);
				create(sb.toString());
				bool = true;
			}
		}

	}

	private static void create(String path) {
		File file = new File(path);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
	}

	/**
	 * .k日志文件 所有记录
	 * 
	 * @param data
	 * @param minute
	 * @param product
	 * @throws Exception
	 */
	public static void wirteFileK(String data, String minute, String product)
			throws Exception {
		String directory = QuotaInitializeServer.getPath() + product;
		createDirectory(directory);
		String path = directory
				+ System.getProperties().getProperty("file.separator")
				+ product +"_" +DateTools.parseToDefaultDateString(new Date()) + "_"
				+ minute + QuotaInitializeServer.getPathEndFlagK();
		if(minute.equals("1440")){
			path = directory
					+ System.getProperties().getProperty("file.separator")
					+ product +"_day" + QuotaInitializeServer.getPathEndFlagK();
		}else if(minute.equals("7200")){
			path = directory
					+ System.getProperties().getProperty("file.separator")
					+ product +"_week" + QuotaInitializeServer.getPathEndFlagK();
		}else if(minute.equals("31680")){
			path = directory
					+ System.getProperties().getProperty("file.separator")
					+ product +"_month" + QuotaInitializeServer.getPathEndFlagK();
		}
		logger.info("minute:{},path:{}",minute,path);
		File file = new File(path);
		// 如果文件不存在则创建
		if (file.exists()) {
			data = "," + data;
		}
		// 追加写入文件
		write(path, data);
	}

	/**
	 * 追加写入文件
	 * 
	 * @param path
	 * @param data
	 * @throws Exception
	 */
	public static void write(String path, String data) throws Exception {
		FileWriter fw = new FileWriter(path, true);
		fw.write(data);
		fw.close();
	}

}
