package com.lt.feedback.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/8 下午5:54
 * email:cndym@163.com
 */
public class Utils {

    public static boolean isEmpty(String str) {
        return (null == str || "".equals(str.trim()) == true);
    }

    public static boolean isNotEmpty(String str) {
        return (null != str && "".equals(str.trim()) == false);
    }

    public static boolean isEmpty(List list) {
        return (null == list || list.size() <= 0);
    }

    public static boolean isNotEmpty(List list) {
        return (null != list && list.size() > 0);
    }

    public static boolean isEmpty(Set list) {
        return (null == list || list.size() <= 0);
    }


    public static boolean isNotEmpty(Set list) {
        return (null != list && list.size() > 0);
    }

    public static boolean isNotEmpty(Object str) {
        return (null != str);
    }

    public static boolean isNotEmpty(Map map) {
        return (null != map && map.size() > 0);
    }

    public static boolean isNotEmpty(StringBuffer str) {
        return (null != str && str.length() > 0);
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
    public static Long formatLong(Object str) {
        try {
            if (isNotEmpty(str)) {
                return Long.parseLong(str.toString());
            }
            return 0l;
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
    public static Float formatFloat(String str, Float def) {
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
            return Double.parseDouble(str);
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


    public static int intRandom() {
        Random random = new Random();
        return random.nextInt(9);
    }

    public static int intRandom(int len) {
        Random random = new Random();
        return random.nextInt(len);
    }

    public static String random(int num) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < num; i++) {
            buffer.append(intRandom());
        }
        return buffer.toString();
    }

    public static String objectToString(Object object) {
        if (isNotEmpty(object)) {
            return object.toString();
        }
        return "";
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
}
