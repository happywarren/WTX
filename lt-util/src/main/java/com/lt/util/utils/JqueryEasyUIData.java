package com.lt.util.utils;
import java.util.List;

import com.github.pagehelper.Page;
import com.lt.util.error.LTResponseCode;

/**
 * 
 * 说明：针对Jquery EasyUI 数据格式的处理
 * @author zheng_zhi_rui@163com
 * @date 2015年4月9日
 *
 */
public class JqueryEasyUIData {

	private static JqueryEasyUIData instance = JqueryEasyUIData.getInstance();
	
	private long total;
	private List<?> rows;
	private Object otherData;
	
	public static String init(Page<?> page){
		instance.setRows(page.getResult());
		instance.setTotal(page.getTotal());
		instance.setOtherData(null);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,instance).toJsonString();
	}
	public static String init(Page<?> page,Object otherData){
		instance.setRows(page.getResult());
		instance.setTotal(page.getTotal());
		instance.setOtherData(otherData);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,instance).toJsonString();
	}

	public JqueryEasyUIData(){
		
	}

	public static JqueryEasyUIData getInstance() {  
	    if (instance == null) {  
	        instance = new JqueryEasyUIData();  
	    }  
	    return instance;  
	}
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public Object getOtherData() {
		return otherData;
	}

	public void setOtherData(Object otherData) {
		this.otherData = otherData;
	}

	
}
