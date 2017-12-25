package com.lt.quota.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 说明：时间日期处理类
 *
 * @author zheng_zhi_rui@163com
 * @date 2015年3月25日
 */
public final class CalendarTools {
    public static final String DATE = "yyyy-MM-dd";
    public static final String TIME = "HH:mm:ss";
    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMATOFFILE = "yyyyMMdd";
    public static final String DATETIMEFORMAT = "yyyyMMddHHmmss";
    public static final DateFormat DATE_FORMAT;
    public static final DateFormat TIME_FORMAT;
    public static final DateFormat DATETIME_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

        TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

        DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static Calendar getCalendar(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar getCalendar(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return calendar;
    }

    public static Calendar getCalendar(long millis) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }

    public static Date getDate(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        return getCalendar(year, month, day, hour, minute, second).getTime();
    }

    public static Date getDate(long millis) {
        return getCalendar(millis).getTime();
    }

    /**
     * 获取当前日期，如2014-11-01
     *
     * @return
     */
    public static String getNowDate() {
        return formatDateTime(new Date(), DATE);
    }

    /**
     * 获取当前时间，如12:23:23
     *
     * @return
     */
    public static String getNowTime() {
        return formatDateTime(new Date(), TIME);
    }

    /**
     * 获取当前时间和日期，如2014-11-01 12:23:23
     *
     * @return
     */
    public static String getNowDateTime() {
        return formatDateTime(new Date(), DATETIME);
    }

    public static String formatDateTime(Date date, String pattern) throws IllegalArgumentException {
        if ((date == null) || (pattern == null) || ("".equals(pattern.trim()))) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatDate(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DATE).format(date);
    }

    /***
     * 格式化时间
     *
     * @param date
     *            被格式化的时间字符串
     * @param sourcePattern
     *            被格式化时间字符串的格式
     * @param targetPattern
     *            格式化后的时间格式
     * @return
     * @throws ParseException
     */
    public static String formatDateTime(String date, String sourcePattern, String targetPattern) throws ParseException {
        if ((date == null) || ("".equals(date.trim())) || (sourcePattern == null) || ("".equals(sourcePattern.trim()))
                || (targetPattern == null) || "".equals(targetPattern.trim())) {
            return null;
        }
        SimpleDateFormat ssdf = new SimpleDateFormat(sourcePattern);
        SimpleDateFormat tsdf = new SimpleDateFormat(targetPattern);

        return tsdf.format(ssdf.parse(date));
    }

    public static Date parseDateTime(String date, String pattern) throws ParseException {
        if ((date == null) || ("".equals(date.trim())) || (pattern == null) || ("".equals(pattern.trim()))) {
            return null;
        }
        return new SimpleDateFormat(pattern).parse(date);
    }

    public static Date parseDateTime(String date) throws ParseException {
        return parseDateTime(date, DATETIME);
    }

    public static Date parseDate(String date) throws ParseException {
        return parseDateTime(date, DATE);
    }

    /**
     * 年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 本年的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 本月的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 本周第几天（周几）
     *
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        if (date == null) {
            return -1;
        }
        Locale.setDefault(Locale.CHINA);
        Calendar calendar = getCalendar(date);

        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 返回小时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回分钟
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 返回毫秒
     *
     * @param date
     * @return
     */
    public synchronized static long getMillis(Date date) {
        Calendar calendar = getCalendar(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回两个日期之间的相隔毫秒数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getMillisBetween(Date d1, Date d2) {
        long s = getCalendar(d1).getTimeInMillis();
        long e = getCalendar(d2).getTimeInMillis();
        return Math.abs(e - s);
    }

    /**
     * 返回两个日期之间的相隔分钟
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getMiniteBetween(Date d1, Date d2) {
        long s = getCalendar(d1).getTimeInMillis();
        long e = getCalendar(d2).getTimeInMillis();
        return Math.abs(e - s) / (1000 * 60);
    }

    /**
     * 获取两个日期之间的天数：cal2 - cal1
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static int getDaysBetween(Calendar cal1, Calendar cal2) {
        boolean flag = true;
        int days = cal2.get(Calendar.DAY_OF_YEAR) - cal1.get(Calendar.DAY_OF_YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        int year1 = cal1.get(Calendar.YEAR);

        if (year2 < year1) {
            flag = false;
        }
        if (year1 != year2) {
            do {
                days += cal1.getActualMaximum(Calendar.DAY_OF_YEAR);
                if (flag) {
                    cal1.add(Calendar.YEAR, 1);
                } else {
                    cal1.add(Calendar.YEAR, -1);
                }
            } while (cal1.get(Calendar.YEAR) != year2);
        }
        if (flag) {
            return days;
        }
        return -days;
    }

    public static int getDaysAbsBetween(Date date1, Date date2) {
        return Math.abs(getDaysBetween(getCalendar(date1), getCalendar(date2)));
    }

    /**
     * 获取两个日期之间的天数：date2 - date1
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getDaysBetween(Date date1, Date date2) {
        return getDaysBetween(getCalendar(date1), getCalendar(date2));
    }

    /**
     * 获取两个日期之间相隔月份 cal2 - cal1
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static int getMonthsBetween(Calendar cal1, Calendar cal2) {
        int months = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
        boolean flag = true;

        int year2 = cal2.get(Calendar.YEAR);
        int year1 = cal1.get(Calendar.YEAR);

        if (year2 < year1) {
            flag = false;
        }
        if (year1 != year2) {
            do {
                months += cal1.getActualMaximum(Calendar.MONTH) + 1;
                if (flag) {
                    cal1.add(Calendar.YEAR, 1);
                } else {
                    cal1.add(Calendar.YEAR, -1);
                }
            } while (cal1.get(Calendar.YEAR) != year2);
        }
        if (flag) {
            return months;
        }
        return -months;
    }

    /**
     * 获取两个日期相隔的月份 date2 - date1
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthsBetween(Date date1, Date date2) {
        return getMonthsBetween(getCalendar(date1), getCalendar(date2));
    }

    /**
     * 获取两个日期相隔年份 cal2 - cal1
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static int getYearsBetween(Calendar cal1, Calendar cal2) {
        int year2 = cal2.get(1);
        int year1 = cal1.get(1);
        return year2 - year1;
    }

    /**
     * 获取两个日期相隔年份 date2 - date1
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getYearsBetween(Date date1, Date date2) {
        return getYearsBetween(getCalendar(date1), getCalendar(date2));
    }

    /**
     * 获取给定日期n天后（前）的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int n) {
        if (date == null) {
            return null;
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, n);
        return calendar.getTime();
    }

    /**
     * 获取给定日期n个月后（前）的日期
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int n) {
        if (date == null) {
            return null;
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    /**
     * 获取给定日期n年后（前）的日期
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int n) {
        if (date == null) {
            return null;
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, n);
        return calendar.getTime();
    }

    /**
     * 获取给定日期n小时后（前）的日期
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, int n) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, n);
        return calendar.getTime();
    }

    /**
     * 获取给定日期n分钟后（前）的日期
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int n) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, n);
        return calendar.getTime();
    }

    /**
     * 获取给定日期n秒后（前）的日期
     *
     * @param date
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date date, int n) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, n);
        return calendar.getTime();
    }

    /**
     * 获取上个月第一天
     *
     * @return
     */
    public static Date getLastMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取上个月最后一天
     *
     * @return
     */
    public static Date getLastMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static Date getCurMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * / 获取前月的最后一天
     *
     * @return
     */
    public static Date getCurMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }

    /**
     * 获取给定时间所在月份的第一天
     *
     * @param date
     * @return
     */
    public static Date getSpecifyDateMonthFirstDay(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getSpecifyDateMonthLastDay(Date date) {
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }


    /**
     * 获取当前年份给定月份的天数
     *
     * @return
     */
    public static int dayLenOfMonth(int m) {
        if (m == 2) {
            int year = getYear(new Date());
            if (((year % 100 == 0) && (year % 400 == 0)) || ((year % 100 != 0) && (year % 4 == 0))) {
                return 29;
            }
            return 28;
        } else if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
            return 31;
        } else {
            return 30;
        }
    }

    public static void main(String[] args) {

    }
}
