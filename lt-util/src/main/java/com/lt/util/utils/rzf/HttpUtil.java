package com.lt.util.utils.rzf;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {

	private static final String charset = "UTF-8";


	/**
	 * 向指定 URL 发送POST请求     *
	 * @param url 发送请求 URL
	 * @param param 请求参数
	 * @return 所代表远程资源响应结
	 */
	public static String sendPost(String url, String param) {
		System.setProperty ("jsse.enableSNIExtension", "false");
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result=new StringBuffer("");
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection(); // 打URL间连接
			conn.setRequestProperty("accept", "*/*"); // 设置通用请求属性
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", charset);
			// 发送POST请求必须设置两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),charset));
			out.print(param); // 发送请求参数
			out.flush();// flush输流缓冲
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(),charset));// 定义BufferedReader输入流读取URL响应
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//使用finally块关闭输流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

}