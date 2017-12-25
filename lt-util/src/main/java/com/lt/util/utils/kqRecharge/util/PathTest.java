package com.lt.util.utils.kqRecharge.util;

public class PathTest {
	
	public  static String getFilePath(){
		//测试用例：将文件放在resource目录下
		String certPath = PathTest.class.getResource("/81205154511001190.jks").getPath().replaceAll("%20", " ");
		return certPath;
	}
	
}
