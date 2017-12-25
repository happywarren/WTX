/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.lt.util.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件加载器
 * 
 * @author yubei
 *
 */
@SuppressWarnings("serial")
public class FileLoader implements java.io.Serializable {

	private static final String module = FileLoader.class.getName();

	private static final Logger Logger = LoggerFactory.getLogger(FileLoader.class);

	private static FileLoader loader = null;

	public static FileLoader getFileLoader() {
		if (loader == null) {
			synchronized (FileLoader.class) {
				if (loader == null) {
					loader = new FileLoader();
				}
			}
		}
		return loader;
	}

	public FileLoader() {
		super();
	}

	/**
	 * 读取文件流
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public FileInputStream loadResource(String location) throws Exception {
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		FileOutputStream fos = null;
		FileInputStream fileInputStream = null;
		try {
			URL url = new URL(location);
			conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 得到输入流
			inputStream = conn.getInputStream();
			if (inputStream instanceof FileInputStream) {
				Logger.error("333");
			}
			byte[] getData = readInputStream(inputStream);

			// 文件保存位置
			File dir = new File(System.getProperty("user.dir"));
			if(!dir.exists()){
				dir.mkdir();
			}
			File file = new File(System.getProperty("user.dir") +File.separator+ System.currentTimeMillis()+".jpg");

			fos = new FileOutputStream(file);
			fos.write(getData);
			fileInputStream = new FileInputStream(file);
			return fileInputStream;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new Exception("获取文件流异常" + module);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}
