package com.lt.adapter.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;

/**
 * 
 * TODO 全局日期处理类
 * @author XieZhibing
 * @date 2016年12月16日 下午9:28:38
 * @version <b>1.0.0</b>
 */
public class String2DateConverter implements Converter<String, Date> {

	static final HashMap<String, String> dateRegFormat = new HashMap<String, String>();
	
	@Override
	public Date convert(String stringDate) {		
		return parseDate(stringDate);
	}
	
	static {
        //2014年3月12日 13时5分34秒，2014-03-12 12:05:34,100，2014/3/12 12:5:34,100
        dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\,\\d{3}\\D*$", "yyyy-MM-dd-HH-mm-ss-SSS");
        //2014年3月12日 13时5分34秒，2014-03-12 12:05:34，2014/3/12 12:5:34
        dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH-mm-ss");
        //2014-03-12 12:05
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH-mm");
        //2014-03-12 12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH");
        //2014-03-12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");
        //2014-03
        dateRegFormat.put("^\\d{4}\\D+\\d{2}$", "yyyy-MM");
        //2014
        dateRegFormat.put("^\\d{4}$", "yyyy");
        //20140312120534
        dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");
        //201403121205
        dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");
        //2014031212
        dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");
        //20140312
        dateRegFormat.put("^\\d{8}$", "yyyyMMdd");
        //201403
        dateRegFormat.put("^\\d{6}$", "yyyyMM");
        //13:05:34  拼接当前日期
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm-ss");
        //13:05  拼接当前日期
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm");
        //14.10.18(年.月.日)
        dateRegFormat.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");
        //30.12(日.月) 拼接当前年份
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}$", "yyyy-dd-MM");
        //12.21.2013(日.月.年)
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");
	}
	
	/**
	 * 
	 * TODO 字符串转换为日期
	 * @author XieZhibing
	 * @date 2016年12月17日 上午11:19:12
	 * @param dateStr
	 * @return
	 */
	public Date parseDate(String dateStr){
        
		Date date = null;
		String dateReplace;
		DateFormat dateFormat;
		String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		try {
			for (String key : dateRegFormat.keySet()) {
				if (Pattern.compile(key).matcher(dateStr).matches()) {
					dateFormat = new SimpleDateFormat(dateRegFormat.get(key));

					// 13:05:34 或 13:05 拼接当前日期
					if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$")
							|| key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {
						dateStr = curDate + "-" + dateStr;
						// 21.1 (日.月) 拼接当前年份
					} else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {
						dateStr = curDate.substring(0, 4) + "-" + dateStr;
					}
					dateReplace = dateStr.replaceAll("\\D+", "-");
					
					// formatter1.format(dateFormat.parse(dateReplace));
//					System.out.println(dateReplace);
					date = dateFormat.parse(dateReplace);
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("-------- 日期格式无效:" + dateStr);
		} 
		return date;
    }
	
	public static void main(String[] args) {
		String2DateConverter conv = new String2DateConverter();
		Date date = conv.parseDate("2016-12-17 11:10:23,334");
		
		System.err.println(date);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		String format = simpleDateFormat.format(date);
		System.err.println(format);
		
	}
}