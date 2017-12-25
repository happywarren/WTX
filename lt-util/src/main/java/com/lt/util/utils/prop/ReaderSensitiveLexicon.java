package com.lt.util.utils.prop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.lt.util.utils.excel.ReadExcel;

/**
 * 
 *
 * 描述:敏感词库加载
 *
 * @author  郭达望
 * @created 2015年6月16日 下午7:56:07
 * @since   v1.0.0
 */
public class ReaderSensitiveLexicon implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(ReaderSensitiveLexicon.class);
	
	public static List<String> instance = new ArrayList<String>();
	
	@Autowired
	private CustomerPropertiesConfig propConfig;

	/*public static  List<String>  getInstance() {
		if(instance.size()==0){
			//读取敏感词库
			init();
		}
		return instance;
	}*/

	private void init() {
		try {
			String url = propConfig.getProperty("sensitive_lexicon_url");
			logger.info("敏感词文件存在地址" + url);
			File f = new File(url);
			if(!f.exists()){
				f.createNewFile();
			}
			if(f.length() < 512){
				return;
			}
			instance = ReadExcel.readExcel(url);
		} catch (IOException e) {
			logger.error("加载敏感词异常", e);
			e.printStackTrace();
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
}
