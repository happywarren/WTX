package com.lt.util.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import com.lt.util.error.LTException;


/**
 * 字符串处理公共类
 * @author guodw
 *
 */
public class StringTools {

	public static boolean isNotNull(String s) {
		return (s != null) ? true : false;
	}



	/**
	 * 去除网页文档中的标签,不包括&nbsp;
	 * 
	 * @param s
	 * @return
	 */
	public static String delHtmlTag(String s) {
		/** 定义script的正则表达式 **/
		String regScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
		/** 定义style的正则表达式 **/
		String regStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";
		/** 定义HTML标签的正则表达式 **/
		String regHtml = "<[^>]+>";
		/** 定义空格回车换行符 **/
		String regSpace = "\\s*|\t|\r|\n";

		Pattern pScript = Pattern.compile(regScript, Pattern.CASE_INSENSITIVE);
		Matcher mScript = pScript.matcher(s);
		s = mScript.replaceAll("");

		Pattern pStyle = Pattern.compile(regStyle, Pattern.CASE_INSENSITIVE);
		Matcher mStyle = pStyle.matcher(s);
		s = mStyle.replaceAll("");

		Pattern pHtml = Pattern.compile(regHtml, Pattern.CASE_INSENSITIVE);
		Matcher mHtml = pHtml.matcher(s);
		s = mHtml.replaceAll("");

		Pattern pSpace = Pattern.compile(regSpace, Pattern.CASE_INSENSITIVE);
		Matcher mSpace = pSpace.matcher(s);
		s = mSpace.replaceAll("");
		return s.trim();
	}

	/**
	 * 去除网页文档中的标签,包括&nbsp;
	 * 
	 * @param s
	 * @return
	 */
	public static String delHtmlTagBlank(String s) {
		s = delHtmlTag(s);
		s = s.replaceAll("&nbsp;", "");
		return s;
	}

	public static String arrayToString(Object[] array, String slice,
			boolean serial) {
		if (array == null || array.length <= 0) {
			return "";
		}
		if (isEmpty(slice)) {
			return array.toString();
		} else {
			String s = "";
			for (Object o : array) {
				if (o == null) {
					if (serial) {
						s += slice + "null";
					}
				} else {
					s += slice + o.toString();
				}
			}
			return s.substring(1);
		}
	}


	/**
	 * 判断是否是手机号格式
	 * 
	 * @param tele
	 * @return
	 */
	public static boolean isTele(String tele) {
		if (!isNotEmpty(tele)) {
			return false;
		}
		if (tele.length() != 11) {
			return false;
		}
		if (tele.substring(0, 3).equals("147")) {
			return true;
		}
		Pattern pattern = Pattern.compile("1[3|5|7|8|][0-9]{9}");
		Matcher isNum = pattern.matcher(tele);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 用正则表达式 判断是否数字
	 * 
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (!isNotEmpty(str)) {
			return false;
		} else {
			Pattern pattern = Pattern.compile("[0-9]*");
			return pattern.matcher(str).matches();
		}
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @param negative
	 *            是否允许负数
	 * @param positive
	 *            是否允许正数
	 * @param isfloat
	 *            是否允许小数
	 * @return
	 */
	public static boolean isNumberic(String str, boolean negative,
			boolean positive, boolean isfloat) {
		// 判断是否为正整数 ^\\d+$
		// 判断是否为正小数 ^\\d+\\.\\d+$
		// 判断是否为负整数 -\\d+$
		// 判断是否为负小数 ^-\\d+\\.\\d+$

		if (!isNotEmpty(str)) {
			return false;
		}

		Pattern pattern = null;
		boolean flag = false;

		if (negative) {
			pattern = Pattern.compile("-\\d+$");
			flag = pattern.matcher(str).matches();
			if (flag) {
				return true;
			}
			if (isfloat) {
				pattern = Pattern.compile("^-\\d+\\.\\d+$");
				flag = pattern.matcher(str).matches();
				if (flag) {
					return true;
				}
			}
		}
		if (positive) {
			pattern = Pattern.compile("^\\d+$");
			flag = pattern.matcher(str).matches();
			if (flag) {
				return true;
			}
			if (isfloat) {
				pattern = Pattern.compile("^\\d+\\.\\d+$");
				flag = pattern.matcher(str).matches();
				if (flag) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 数字金额大写转换 要用到正则表达式
	 */
	public static String digitUppercase(double n) {

		String fraction[] = { "角", "分" };

		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

		String unit[][] = { { "元", "万", "亿" },

		{ "", "拾", "佰", "仟" } };

		String head = n < 0 ? "负" : "";

		n = Math.abs(n);

		String s = "";

		for (int i = 0; i < fraction.length; i++) {

			s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i])
					.replaceAll("(零.)+", "");

		}
		if (s.length() < 1) {
			s = "整";
		}

		int integerPart = (int) Math.floor(n);
		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
					+ s;
		}
		return head
				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
						.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}

	/**
	 * 校验是否含有非法字符 包含非法字符时会返回非法的字符
	 */
	/*
	 * public static String illegalChar(String str) { if (!isNotEmpty(str)) {
	 * return ""; } String[] IIIEGAL_CHAR = { "+", "#","%", "^", "<", ">", "[",
	 * "]", "{", "}", "/", "\\", "?", "&", "(", ")",";","'","," ,"\"","-","."};
	 * for (int i = 0; i < IIIEGAL_CHAR.length; i++) { int index =
	 * str.indexOf(IIIEGAL_CHAR[i]); if (index > -1) { return IIIEGAL_CHAR[i]; }
	 * } return ""; }
	 */
	/**
	 * 
	 *
	 * 描述:过滤所有特殊字符 包含非法字符时会返回非法的字符
	 *
	 * @author 郭达望
	 * @created 2015年6月18日 上午11:24:42
	 * @since v1.0.0
	 * @param str
	 * @return
	 * @return String
	 */
	public static String illegalChar(String str) {
		if (!isNotEmpty(str)) {
			return "";
		}
		String reg = "^[a-z|A-Z|\\u4e00-\\u9fa5|0-9|_|_]*$";
		if (!str.matches(reg)) {
			return "存在非法字符";
		}
		String[] IIIEGAL_CHAR = { "select", "insert", "update", "delete",
				"drop", "--", "'" };
		for (int i = 0; i < IIIEGAL_CHAR.length; i++) {
			int index = str.indexOf(IIIEGAL_CHAR[i]);
			if (index > -1) {
				return IIIEGAL_CHAR[i];
			}
		}
		return "";
	}

	/**
	 * 去除空格
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 去除换行和制表，不去除空格
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceTabEnter(String str) {
		// \n 回车
		// \t 水平制表符
		// \s 空格
		// \r 换行
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static boolean isIp(String ip) {
		if (isEmpty(ip))
			return false;
		Pattern p = Pattern
				.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
		Matcher m = p.matcher(ip);
		return m.matches();
	}

	/***
	 * 产生一个纯数字随机整数
	 * 
	 * @param point
	 *            整数位数
	 * @return
	 */
	public static String getRandom(int point) {
		String result = String.valueOf(Math.random());
		String f = "#####0";
		if (point > 0) {
			f = "";
			for (int i = 0; i < point - 1; i++) {
				f += "#";
			}
			f += "0";
		}
		BigDecimal rand = new BigDecimal(result);
		BigDecimal one = new BigDecimal(1);
		double d = rand.divide(one, point, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		DecimalFormat df = new DecimalFormat(f);
		String t = df.format(d * Math.pow(10, point));
		if (t.length() < point) {
			int l = point - t.length();
			for (int i = 0; i < l; i++) {
				t += "0";
			}
		}
		return t;
	}

	/**
	 * 身份证号合法性验证
	 * 
	 * @param idCard
	 * @return
	 */
	public static boolean isIdCard(String idCard) {
		return IdcardValidator.isValidatedAllIdcard(idCard);
	}

	/**
	 * 银行卡号简单的验证，只做全数字验证
	 * 
	 * @param s
	 * @return
	 */
	public static boolean validBankNum(String s) {
		boolean flag = isNumeric(s);
		if (!flag) {
			return false;
		}
		if (s.length() < 16 || s.length() > 19) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param d
	 * @return
	 */
//	public static double valueOf(double d) {
//		DecimalFormat df = new DecimalFormat("#.00");
//		d = Double.valueOf(df.format(d));
//		return d;
//	}

	public static String digtalToAbc(String str) {
		if (!isNotEmpty(str)) {
			str = CalendarTools.formatDateTime(new Date(), "mmssSS");
		}
		String result = "";
		char[] ch = str.toCharArray();
		for (char c : ch) {
			if ('0' == c) {
				result += "a";
			} else if ('1' == c) {
				result += "b";
			} else if ('3' == c) {
				result += "c";
			} else if ('4' == c) {
				result += "d";
			} else if ('5' == c) {
				result += "e";
			} else if ('6' == c) {
				result += "f";
			} else if ('7' == c) {
				result += "g";
			} else if ('8' == c) {
				result += "h";
			} else if ('9' == c) {
				result += "i";
			}
		}
		if (result.length() < 10) {
			result += (getRandom(10 - result.length()));
		}
		return result;
	}

	public static int getChineseLength(String str) {
		str = str.trim();
		if (StringUtils.isBlank(str)) {
			return 0;
		}
		try {
			return str.getBytes("gbk").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 *
	 * 描述:验证中文名字,过滤"."
	 *
	 * @author 郭达望
	 * @created 2015年6月15日 下午4:02:12
	 * @since v1.0.0
	 * @param name
	 * @return
	 * @return boolean
	 */
	public static boolean isChineseName(String name) {
		int count = 0;
		if (name.contains(".") && !name.endsWith(".") && !name.startsWith(".")) {
			count = name.indexOf("..");
			if (count != -1) {
				return false;
			}
			if (name.contains(".")) {
				name = name.replace(".", "");
			}
		} else if (name.contains("•") && !name.endsWith("•")
				&& !name.startsWith("•")) {
			count = name.indexOf("••");
			if (count != -1) {
				return false;
			}
			if (name.contains("•")) {
				name = name.replace("•", "");
			}
		} else if (name.contains("·") && !name.endsWith("·")
				&& !name.startsWith("·")) {
			count = name.indexOf("··");
			if (count != -1) {
				return false;
			}
			if (name.contains("·")) {
				name = name.replace("·", "");
			}
		} else if (name.contains("●") && !name.endsWith("●")
				&& !name.startsWith("●")) {
			count = name.indexOf("●●");
			if (count != -1) {
				return false;
			}
			if (name.contains("●")) {
				name = name.replace("●", "");
			}
		} else if (name.contains("．") && !name.endsWith("．")
				&& !name.startsWith("．")) {
			count = name.indexOf("．．");
			if (count != -1) {
				return false;
			}
			if (name.contains("．")) {
				name = name.replace("．", "");
			}
		}
		Pattern pattern = Pattern
				.compile("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]){2,20}$");
		Matcher matcher = pattern.matcher(name);
		if (matcher.find()) {
			return true;
		}
		return false;

	}

	/**
	 * 
	 *
	 * 描述:默认昵称：ID*3+随机数（0-2）
	 *
	 * @author 郭达望
	 * @created 2015年6月16日 下午5:30:35
	 * @since v1.0.0
	 * @param id
	 * @return
	 * @return String
	 */
	public static String randomNickName(Integer id, Integer type) {
		int i = (int) (Math.random() * 3);
		int num = id * 3 + i;
		int j = type == null ? 0 : type;
		String str = "用户";
		String name = str + num;
		return name;
	}

	/**
	 * 
	 *
	 * 描述:验证字符串长度是否符合要求，一个汉字等于两个字符
	 *
	 * @author 郭达望
	 * @created 2015年9月30日 上午11:15:43
	 * @since v1.0.0
	 * @param strParameter要验证的字符串
	 * @param limitLength验证的长度
	 * @return
	 * @return boolean
	 */
	public static boolean validateStrByLength(String strParameter,
			int limitLength) {
		int temp_int = 0;
		byte[] b = strParameter.getBytes();

		for (int i = 0; i < b.length; i++) {

			if (b[i] >= 0) {
				temp_int = temp_int + 1;
			} else {
				temp_int = temp_int + 2;
				i = i + 2;
			}
		}
		if (temp_int > limitLength) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkURL(String url) {
		if(isEmpty(url)){ return false;}
		String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
		return Pattern.matches(regex, url);
	}
	
	/**
	 * 数字+字母验证
	 */
	public static boolean isNumbericLetter(String param){
		if(StringUtils.isEmpty(param)){
			return false;
		}
		
		for(int i=0; i< param.length() ; i++){
			char c = param.charAt(i);
			if(!((c>='0' && c<='9')||(c>='a' && c<='z')|| (c>='A' && c<='Z'))){
				return false;
			}
		}
		
		boolean numbericFlag = false;
		boolean letterFlag = false;
		for(int i=0; i< param.length() ; i++){
			char c = param.charAt(i);
			if((c>='0' && c<='9')){
				numbericFlag = true;
			}
			
			if((c>='a' && c<='z')|| (c>='A' && c<='Z')){
				letterFlag = true;
			}
		}
		
		return numbericFlag&&letterFlag;
	}
	
	 /** 
     * 获得一个UUID 
     * @return String UUID 
     */ 
    public static String getUUID(){ 
        String s = UUID.randomUUID().toString();
        //去掉“-”符号 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    }
    
    
	/**
	 * 
	 * TODO 判断字符串是否为空(是否包含空白字符),空返回true
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:31:20
	 * @param str
	 * @return
	 */
	public static boolean isBlankOrEmpty(String str) {
		return org.apache.commons.lang3.StringUtils.isBlank(str) || org.apache.commons.lang3.StringUtils.isEmpty(str);
	}
	/**
	 * 
	 * TODO 判断字符串是否为空
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:31:53
	 * @param cs
	 * @return
	 */
	public static boolean isEmpty(CharSequence cs) {
		return ((cs == null) || (cs.length() == 0));
	}
	/**
	 * 
	 * TODO 判断字符串不是空
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:32:10
	 * @param cs
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence cs) {
		return !isEmpty(cs);
	}
	/**
	 * 
	 * TODO 判断字符串是空或空白字符
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:33:26
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if ((cs == null) || ((strLen = cs.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < strLen; ++i) {
			if (!(Character.isWhitespace(cs.charAt(i)))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * TODO 判断字符串不是空或空白字符
	 * @author XieZhibing
	 * @date 2016年12月11日 上午9:34:43
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(CharSequence cs) {
		return (!(isBlank(cs)));
	}
	
	public static int[] forInstance(int len) {
        int[] temp = new int[len];
        for (int i = 0; i < len; i++) {
            temp[i] = i;
        }
        return temp;
    }

    public static boolean isNotEmpty(String str) {
        return (null != str && "".equals(str.trim()) == false);
    }

    public static boolean isNotEmpty(List list) {
        return (null != list && list.size() > 0);
    }

    public static boolean isNotEmpty(Set list) {
        return (null != list && list.size() > 0);
    }

    public static boolean isNotEmpty(Object str) {
        return (null != str);
    }

    public static boolean isNotEmpty(StringBuffer str) {
        return (null != str && str.length() > 0);
    }

    public static boolean isNotEmptyObject(Object str) {
        return (null != str && !"".equals(str));
    }

    /**
     * 返回一个定长的随机字符串(只包含数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

    /**
     * 生成指定大小的随机数
     *
     * @param num 号码区间
     * @return
     */
    public static int intRandom(int num) {
        Random random = new Random();
        return random.nextInt(num);
    }

    /**
     * 生成0-9的随机数
     *
     * @return
     */
    public static int intRandom() {
        Random random = new Random();
        return random.nextInt(9);
    }

    public static String random(int num) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < num; i++) {
            buffer.append(intRandom());
        }
        return buffer.toString();
    }

    /**
     * 正则验证
     *
     * @param Content 需要验证的内容
     * @param regex   验证正则表达式
     * @return
     */
    public static boolean regValidate(String Content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        return matcher.matches();
    }

    /**
     * 配置所有
     *
     * @param Content
     * @param regex
     * @return
     */
    public static List<String> regFinds(String Content, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 正则查找
     *
     * @param Content 查找内容
     * @param regex   匹配正则表达式
     * @return
     */
    public static String regFind(String Content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        matcher.find();
        return matcher.group();
    }

    /**
     * 正则查找
     *
     * @param Content 查找内容
     * @param regex   匹配正则表达式
     * @param index   分组
     * @return
     */
    public static String regFind(String Content, String regex, int index) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        matcher.find();
        return matcher.group(index);
    }

    /**
     * 正则替换
     *
     * @param Content 查找内容
     * @param regex   匹配正则表达式
     * @return
     */
    public static String regReplace(String Content, String regex, String regStr) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        return matcher.replaceAll(regStr);
    }

    /**
     * 字符串包含验证,包含验证重复号码
     *
     * @param content 验证内容
     * @param vastr   对比字符串
     * @return
     */
    public static boolean comValidate(String content, String vastr) {
        if (null == content)
            return false;
        if ("".equals(content))
            return false;
        boolean re = true;
        String[] arr = content.split(",");
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            if (vastr.indexOf(arr[i]) < 0) {
                re = false;
                break;
            } else {
                if (vastr.indexOf(arr[i], vastr.indexOf(arr[i]) + 1) > -1) {
                    re = false;
                    break;
                }
            }
        }
        return re;
    }

    /**
     * 字符串包含验证,包含验证重复号码,总位数
     *
     * @param content 验证内容
     * @param vastr   对比字符串
     * @param num     必须满足多少位
     * @return
     */
    public static boolean comValidate(String content, String vastr, int num) {
        if (null == content)
            return false;
        if ("".equals(content))
            return false;
        boolean re = true;
        String[] arr = content.split(",");
        int len = arr.length;
        if (len < num)
            return false;
        for (int i = 0; i < len; i++) {
            if (vastr.indexOf(arr[i]) < 0) {
                re = false;
                break;
            } else {
                if (vastr.indexOf(arr[i], vastr.indexOf(arr[i]) + 1) > -1) {
                    re = false;
                    break;
                }
            }
        }
        return re;
    }

    /**
     * 中文
     *
     * @return
     */
    public static String encodStr(String str) {
        try {
            return new String(str.getBytes("iso-8859-1"), "utf-8");
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * 中文
     *
     * @return
     */
    public static String encodStr(String str, String encoding) {
        try {
            return new String(str.getBytes("iso-8859-1"), encoding);
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * IP转换成10位数字
     *
     * @param ip IP
     * @return 10位数字
     */
    public static long ip2num(String ip) {
        long ipNum = 0;
        try {
            if (ip != null) {
                String ips[] = ip.split("\\.");
                for (int i = 0; i < ips.length; i++) {
                    int k = Integer.parseInt(ips[i]);
                    ipNum = ipNum + k * (1L << ((3 - i) * 8));
                }
            }
        } catch (Exception e) {
        }
        return ipNum;
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     */
    public static String num2ip(long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInt(String str) {
        if (null == str)
            return false;
        str = str.trim();
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static int formatInt(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static int formatInt(Object str, int def) {
        try {
            return Integer.parseInt(str + "");
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static long formatLong(String str, int def) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static long formatLong(Long str, int def) {
        if (isNotEmpty(str)) {
            return str;
        } else {
            return def;
        }
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static Long formatLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static Integer formatInt(String str, Integer def) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换字符串为浮点数
     *
     * @param str 待格式化字符串
     * @param def 默认值
     * @return
     */
    public static Float Double(String str, Float def) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换字符串为浮点数
     *
     * @param str 待格式化字符串
     * @param def 默认值
     * @return
     */
    public static Double formatDouble(String str, Double def) {
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 转换BigDecimal为浮点数
     *
     * @param obj 待格式化字符串
     * @return
     */
    public static Double formatDouble(Object obj) {
        Double num = 0d;
        try {
            if (isNotEmpty(obj)) {
                BigDecimal db = (BigDecimal) obj;
                num = db.doubleValue();
            }
            return num;
        } catch (Exception e) {
            return num;
        }
    }

    public static int formatBigDecimalToInt(Object obj) {
        int num = 0;
        try {
            if (isNotEmpty(obj)) {
                BigDecimal db = (BigDecimal) obj;
                num = db.intValue();
            }
            return num;
        } catch (Exception e) {
            return num;
        }
    }

    public static String formatDouble(Double value, String pattern) {
        if (pattern == null) {
            pattern = "#.##";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

    /**
     * 转换字符串为浮点数
     *
     * @param str 待格式化字符串
     * @param def 默认值
     * @return
     */
    public static Double formatDouble(Double str, Double def) {
        if (isNotEmpty(str)) {
            return str;
        }
        return def;
    }

    /**
     * 判断是否为浮点数，包括double和float
     *
     * @param str 传入的字符串
     * @return 是浮点数返回true, 否则返回false
     */
    public static boolean isDouble(String str) {
        if (null == str)
            return false;
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的字符串是否符合Email样式.
     *
     * @param str 传入的字符串
     * @return 是Email样式返回true, 否则返回false
     */
    public static boolean isEmail(String str) {
        if (null == str)
            return false;
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的字符串是否为纯汉字
     *
     * @param str 传入的字符串
     * @return 如果是纯汉字返回true, 否则返回false
     */
    public static boolean isChinese(String str) {
        if (null == str)
            return false;
        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * 是否为空白,包括null和""
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    /**
     * 判断字符串不能为空
     * @param kvs
     * @return
     */
    public static boolean isAllNotEmpty(String ...kvs) {
        if (kvs != null) {
            for (int i = 0; i < kvs.length; i++){
            	if(kvs[i]==null || kvs[i].trim().length()==0){
            		return false;
            	}
            }
        }
        return true;
    }
    /**
     * 判断字符串为空
     * @param kvs
     * @return
     */
    public static boolean isAllEmpty(String ...kvs) {
    	if (kvs != null) {
    		for (int i = 0; i < kvs.length; i++){
    			if(kvs[i]!=null && kvs[i].trim().length()>=0){
    				return false;
    			}
    		}
    	}
    	return true;
    }
    /**
     * 是否为空白,包括null和""
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Integer str) {
        return str == null;
    }

    /**
     * 是否为空白,包括null和""
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(List str) {
        return str == null || str.size() == 0;
    }

    /**
     * 奖字符转成16进制
     *
     * @return
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = "0000" + Integer.toHexString(ch);
            str = str + s4.substring(s4.length() - 4) + " ";
        }
        return str;
    }

    /**
     * 人民币转成大写
     *
     * @param value
     * @return String
     */
    public static String hangeToBig(double value) {
        char[] hunit = {'拾', '佰', '仟'}; // 段内位置表示
        char[] vunit = {'万', '亿'}; // 段名表示
        char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'}; // 数字表示
        long midVal = (long) (value * 100); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串

        String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
        String rail = valStr.substring(valStr.length() - 2); // 取小数部分

        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果
        // 处理小数点后面的数
        if (rail.equals("00")) { // 如果小数部分为0
            suffix = "整";
        } else {
            suffix = digit[rail.charAt(0) - '0'] + "角" + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        char zero = '0'; // 标志'0'表示出现过0
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                zeroSerNum++; // 连续0次数递增
                if (zero == '0') { // 标志
                    zero = digit[0];
                } else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    zero = '0';
                }
                continue;
            }
            zeroSerNum = 0; // 连续0次数清零
            if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
                prefix += zero;
                zero = '0';
            }
            prefix += digit[chDig[i] - '0']; // 转化该数字表示
            if (idx > 0)
                prefix += hunit[idx - 1];
            if (idx == 0 && vidx > 0) {
                prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
            }
        }

        if (prefix.length() > 0)
            prefix += '圆'; // 如果整数部分存在,则有圆的字样
        return prefix + suffix; // 返回正确表示
    }

    /**
     * 在字符串头部添加字符，使原字符串达到指定的长度
     *
     * @param source  源字符串
     * @param filling 填充字符
     * @param lastLen 填充后的总长度
     * @return 如果源字符串长度大于lastLen，返回原字符串，否则用filling填充源字符串后返回结果
     */
    public static String fillString(String source, char filling, int lastLen) {
        StringBuffer temp = new StringBuffer();
        if (source.length() < lastLen) {
            int fillLen = lastLen - source.length();
            for (int i = 0; i < fillLen; i++) {
                temp.append(filling);
            }
        }
        temp.append(source);
        return temp.toString();
    }

    /**
     * 格式化一个数字字符串为9，999，999.99的格式,如果字符串无法格式化返回0.00
     *
     * @param money 数字字符串
     * @return 格式化好的数字字符串
     */
    public static String formatMoney(String money) {
        String formatMoney = "0.00";
        try {
            DecimalFormat myformat3 = new DecimalFormat();
            myformat3.applyPattern(",##0.00");
            double n = Double.valueOf(money);
            formatMoney = myformat3.format(n);
        } catch (Exception ex) {
        }
        return formatMoney;
    }

    /**
     * 格式化一个数字字符串为9，999，999.99的格式,如果字符串无法格式化返回0.00
     *
     * @param money 数字字符串
     * @return 格式化好的数字字符串
     */
    public static String formatMoney(double money) {
        String formatMoney = "0.00";
        try {
            DecimalFormat myformat3 = new DecimalFormat();
            myformat3.applyPattern(",##0.00");
            formatMoney = myformat3.format(money);
        } catch (Exception ex) {
        }
        return formatMoney;
    }

    /**
     * 从右边截取指定长度的字符串
     *
     * @param src 源字符串
     * @param len 长度
     * @return 截取后的字符串
     */
    public static String right(String src, int len) {
        String dest = src;
        if (src != null) {
            if (src.length() > len) {
                dest = src.substring(src.length() - len);
            }
        }

        return dest;
    }

    /**
     * 得到以&分割的字符数组
     *
     * @param str
     * @return
     */
    public static String[] strToArr(String str, int num) {
        if (null == str || "".equals(str))
            return null;
        String[] arr = str.split("&");
        int len = arr.length;
        if (len != num)
            return null;
        return arr;
    }

    /**
     * 得到以&分割的字符数组
     *
     * @param str
     * @return
     */
    public static String[] strToArr(String str) {
        if (null == str || "".equals(str))
            return null;
        String[] arr = str.split("&");
        return arr;
    }

    /**
     * 得到以pix规定的符号分割的数组
     *
     * @param str
     * @param pix
     * @return
     */
    public static String[] strToArr(String str, String pix) {
        if (null == str || "".equals(str))
            return null;
        return str.split(pix);
    }

    /**
     * 去除号码两边的空格
     *
     * @param para    原字符串
     * @param defalut 如果为空默认值
     * @return
     */
    public static String formatStr(String para, String defalut) {
        if (null != para) {
            para = para.trim();
            if ("".equals(para)) {
                return defalut;
            }
            return para;
        } else {
            return defalut;
        }
    }

    /**
     * @param obj     原字符串
     * @param defalut 如果为空默认值
     * @return
     */
    public static String formatStr(Object obj, String defalut) {
        if (null != obj) {
            return obj + "";
        } else {
            return defalut;
        }
    }

    /**
     * 去除号码两边的空格
     *
     * @param para 原字符串
     * @return
     */
    public static String formatStr(String para) {
        if (null != para) {
            para = para.trim();
            if ("".equals(para)) {
                return null;
            }
            return para;
        } else {
            return null;
        }
    }

    public static String fullByZero(int in, int len) {
        String str = Integer.toString(in);
        if (null != str) {
            while (str.length() < len) {
                str = "0" + str;
            }
        }
        return str;
    }

    public static String fullByZero(String str, int len) {
        if (null != str) {
            while (str.length() < len) {
                str = "0" + str;
            }
        }
        return str;
    }

    public static String formatNumber(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        String temp = decimalFormat.format(amount);
        return temp;
    }

    public static String formatNumberZ(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String temp = decimalFormat.format(amount);
        return temp;
    }

    public static String formatNumberEx(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        String temp = decimalFormat.format(amount);
        if (temp.indexOf(".") == 0) {
            return "0%";
        }
        return temp + "%";
    }


    public static String getClassPath() {
        URL url = StringTools.class.getClassLoader().getResource("");
        if (null == url) {
            return "";
        }
        return url.getPath();
    }

    public static String getIpAddress(javax.servlet.http.HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String localIP = "127.0.0.1";
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static Map<String, Integer> getPageStartAndEnd(int page, int pageTotal) {
        if (pageTotal <= 0) {
            pageTotal = 1;
        }
        Map<String, Integer> result = new HashMap<String, Integer>();
        int start = 0;
        int end = 0;

        if (pageTotal > 5) {
            if (page - 2 > 0) {
                start = page - 2;
                if (page + 2 <= pageTotal) {
                    end = page + 2;
                } else {
                    start = pageTotal - 5;
                    end = pageTotal;
                }
            } else {
                start = 1;
                end = 5;
            }
        } else {
            start = 1;
            end = pageTotal;
        }

        result.put("start", start);
        result.put("end", end);
        return result;
    }

    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        if (!StringTools.isNotEmpty(cookies)) {
            return defaultValue;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName()))
                return (cookie.getValue());
        }
        return (defaultValue);
    }

    public static String reNull(String str) {
        if (null == str) {
            return "0";
        }
        return str;
    }

    public static void deleteCookie(HttpServletResponse response) {
        // 删除cookie
        Cookie coo = new Cookie("code", "");
        coo.setMaxAge(0);
        coo.setPath("/"); // 路径
        coo.setDomain("localhost");// 域名
        response.addCookie(coo); // 在本地硬盘上产生文件
    }

    public static String getEmailDomain(String email) {
        if (StringTools.isEmpty(email)) {
            return "";
        }
        if (!StringTools.isEmail(email)) {
            return "";
        }
        String[] mailArray = email.split("@");
        if (mailArray.length != 2) {
            return "";
        }
        if (mailArray[1].equals("gmail.com")) {
            return "http://www.gmail.com";
        }

        return "http://mail." + mailArray[1];
    }

    public static String getVerifyCode(int len) {
        StringBuffer temp = new StringBuffer();
        String arr = "abcdefghijklmnopkrstuvwxyzABCDEFGHIJKLMNOPKRSTUVWXYZ0123456789";
        String[] array = arr.split("");
        while (temp.length() < len) {
            int random = StringTools.intRandom(62);
            if (StringTools.isNotEmpty(array[random])) {
                temp.append(array[random]);
            }
        }
        return temp.toString();
    }

    public static String objectToString(Object obj) {
        if (isNotEmpty(obj)) {
            return obj.toString();
        }
        return "";
    }

    /**
     * 根据指定的符号，截取掉符号之后几位数据
     *
     * @param num 要截取的数据
     * @param len 截取之后符号后面所剩个数
     * @param tag 截取的符号
     * @return
     */
    public static String objectSubString(Object num, int len, String tag) {
        if (isNotEmpty(num)) {
            String n = num.toString();
            int index = n.indexOf(tag);
            if (index > 0) {
                int j = n.length() - index;
                n = n.substring(0, j > len ? index + len + 1 : n.length());
                if (j <= len) {
                    for (int i = 0; i < j - 1; i++) {
                        n = n + "0";
                    }
                }
            }
            return n;
        }
        return "";
    }

    public static boolean validateEmail(String email) {
        return StringTools.regValidate(email, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    }

    public static boolean validateMobile(String mobile) {
        return StringTools.regValidate(mobile, "^1{1}[3|5|8]{1}\\d{9}$");
    }

    public static String formatMobile(String mobile) {
        if (StringTools.isNotEmpty(mobile)) {
            if (mobile.length() == 11) {
                mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            }
        }
        return mobile;
    }


    public static String formatRealName(String realName) {
        if (StringTools.isNotEmpty(realName)) {
            Integer len = realName.length();
            return realName.substring(0, len - 1) + "*";
        }
        return realName;
    }

    /**
     * 截取身份证号显示
     */
    public static String formatCardCode(String cardCode) {
        if (StringTools.isEmpty(cardCode)) {
            return cardCode;
        }
        if (StringTools.isNotEmpty(cardCode)) {
            int len = cardCode.length();
            int hLen = len - 4;
            cardCode = cardCode.substring(0, hLen) + "****";
        }
        return cardCode;
    }

    /**
     * 将一个整数字符串分解成一个整数数组 例： num=123; --> numArray[3]{1,2,3}
     *
     * @param num
     * @return
     */
    public static int[] strToIntArray(String num) {
        String[] strArray = num.trim().split("");
        int[] numArray = new int[strArray.length - 1];
        for (int i = 1; i < strArray.length; i++) {
            numArray[i - 1] = Integer.parseInt(strArray[i]);
        }
        return numArray;
    }

    /**
     * 将一个字符串分解成一个字符数组
     *
     * @param str
     * @return
     */
    public static String[] strToStrArray(String str) {
        String[] strArray = str.trim().split("");
        String[] numArray = new String[strArray.length - 1];
        for (int i = 1; i < strArray.length; i++) {
            numArray[i - 1] = strArray[i];
        }
        return numArray;
    }

    public static String subString(String str, int subBytes) {
        int bytes = 0; // 用来存储字符串的总字节数
        for (int i = 0; i < str.length(); i++) {
            if (bytes == subBytes) {
                return str.substring(0, i);
            }
            char c = str.charAt(i);
            if (c < 256) {
                bytes += 1; // 英文字符的字节数看作1
            } else {
                bytes += 2; // 中文字符的字节数看作2
                if (bytes - subBytes == 1) {
                    return str.substring(0, i);
                }
            }
        }
        return str;
    }

    public static double percentToDouble(String percent) {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();

        try {
            return numberFormat.parse(percent).doubleValue();
        } catch (ParseException e) {
            return 0d;
        }
    }

    // 四舍五入

    public static int doubleToInt(double value) {
        BigDecimal bigDecimal = new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.intValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将字符串以0为字符占位扩展为固定长度,length位不做任何处理
     *
     * @param str
     * @param length
     * @return
     */
    public static String formatStringLen(String str, int length) {
        return formatStringLen(str, length, "0");
    }

    /**
     * 将字符串以flagChar为字符占位扩展为固定长度,length位不做任何处理
     *
     * @param str
     * @param length
     * @param flagChar
     * @return
     */
    public static String formatStringLen(String str, int length, String flagChar) {
        if (str.length() >= length) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length - str.length(); i++) {
            sb.append(flagChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 创建唯一用户编号
     */
    public static String createUserCode(){
    	
    	//年月日时分秒 12位+3位毫秒+5位随机码（数字或者字母）
    	String str = DateTools.parseToTimeStamp(new Date())+CodeCreate.createCodeLen(8);
		return str;
    }
    
    /**
     * String转Document
     * @param data
     * @param code
     * @return
     */
	public static Document stringConvertXML(String data, String code) {
		 
		StringBuilder sXML = new StringBuilder(code);
		sXML.append(data);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			InputStream is = new ByteArrayInputStream(sXML.toString().getBytes(
					"utf-8"));
			doc = dbf.newDocumentBuilder().parse(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * 字符替换 如 xxxxxxxxx ==>> xx**********xx,
	 * @param str
	 * @param length 前后保留长度
	 * @return
	 */
	public static String fuzzy(String str,int startLength,int endLength){
		if(isEmpty(str)){
			return null;
		}
		int len = str.length()-startLength-endLength;
		if(len<=0){
			return str;
		}
		String top = str.substring(0,startLength);
		StringBuilder mid = new StringBuilder();
		for (int i = 0; i < str.length()-endLength-startLength; i++) {
			mid.append("*");
		}
		String end = str.substring(str.length()-endLength,str.length());
		return top+mid.toString()+end;
	}
	
    /**
     * getAmountToCent 获取圆转换到分金额的值
      * @param value
      * @return
      * @throws Exception
      * @since 1.0.0
     */
    public static String getAmountToCent(Object value) throws LTException {
    	if(isEmpty(String.valueOf(value))){
    		return "";
    	}
        return String.valueOf(new BigDecimal(String.valueOf(value)).multiply(new BigDecimal(100)).longValue());
    }
    
    
    /**
     * getAmountToYuan 获取分转换到圆金额的值,保留两位小数
      * @param value
      * @return
      * @throws Exception
      * @since 1.0.0
     */  
    public static String getAmountToYuan(Object value) throws Exception {
    	if(isEmpty(String.valueOf(value))){
    		return "";
    	}    	
        return String.valueOf(new BigDecimal(String.valueOf(value)).divide(new BigDecimal(100), 2, 0).doubleValue());
    }
    
    
    public static void main(String[] args) {
    System.out.println(fuzzy("339005199209140619", 3,3));	;
    	int num = 0;
    	while(num<1000){
        	System.out.println(randomNickName(188687, 0));
        	num++;
    	}
	}
}
