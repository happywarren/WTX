package com.lt.feedback.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 静态常量
     */
    public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";


    public static String formatDate() {
        return formatDate2Str("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate2Str(String format) {
        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date formatDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date formatDate(String date, String style) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat simpleDateormat = new SimpleDateFormat(style);
        try {
            return simpleDateormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转换指定时间为指定格式
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate2Str(Date date, String format) {
        if (date == null) {
            return null;
        }

        if (format == null || format.equals("")) {
            format = C_TIME_PATTON_DEFAULT;
        }
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 转换指定时间为指定格式
     *
     * @param date
     * @return
     */
    public static String formatDate2Str(Date date) {
        return formatDate2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 拿到指定输出格式的SimpleDateFormat
     *
     * @param format
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat sdf;
        if (format == null) {
            sdf = new SimpleDateFormat(C_TIME_PATTON_DEFAULT);
        } else {
            sdf = new SimpleDateFormat(format);
        }

        return sdf;
    }

    /**
     * 得到当前日期
     *
     * @return
     */
    public static String today() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 根据格式得到当前日期
     *
     * @param format
     * @return
     */

    public static String today(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 得到昨天的日期yyyy-MM-dd
     *
     * @return
     */
    public static String yesterday() {
        Date date = new Date();
        date.setTime(date.getTime() - 86400000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 得到昨天的日期yyyy-MM-dd
     *
     * @return
     */
    public static String yesterday(String style) {
        Date date = new Date();
        date.setTime(date.getTime() - 86400000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(style);
        return simpleDateFormat.format(date);
    }

    /**
     * 得到明天的日期yyyy-MM-dd
     *
     * @return
     */
    public static String tomorrow() {
        Date date = new Date();
        date.setTime(date.getTime() + 86400000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getWeekByDate(String dateStr, String style) {
        Date date = formatDate(dateStr, style);
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        return sdf.format(date);
    }

    /**
     * 日期的指定域加减
     *
     * @param time  时间戳(长整形字符串)
     * @param field 加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
     * @param num   加减的数值
     * @return 操作后的时间戳(长整形字符串)
     */
    public static String addDate(String time, String field, int num) {
        int fieldNum = Calendar.YEAR;
        if (field.equals("m")) {
            fieldNum = Calendar.MONTH;
        }
        if (field.equals("d")) {
            fieldNum = Calendar.DATE;
        }
        if (field.equals("minute")) {
            fieldNum = Calendar.MINUTE;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(time));
        cal.add(fieldNum, num);
        return String.valueOf(cal.getTimeInMillis());
    }

    /**
     * 日期的指定域加减
     *
     * @param date  时间戳(长整形字符串)
     * @param field 加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
     * @param num   加减的数值
     * @return 操作后的时间戳(长整形字符串)
     */
    public static Date addDate(Date date, String field, int num) {
        if (date != null) {
            int fieldNum = Calendar.YEAR;
            if (field.equals("m")) {
                fieldNum = Calendar.MONTH;
            }
            if (field.equals("d")) {
                fieldNum = Calendar.DATE;
            }
            if (field.equals("minute")) {
                fieldNum = Calendar.MINUTE;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            cal.add(fieldNum, num);
            return new Date(cal.getTimeInMillis());
        }
        return null;
    }

    /**
     * 日期的指定域加减
     *
     * @param field 加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
     * @param num   加减的数值
     * @return 操作后的时间戳(长整形字符串)
     */
    public static Date addDate(String field, int num) {
        field = field.toLowerCase();
        int fieldNum = Calendar.YEAR;
        if (field.equals("m")) {
            fieldNum = Calendar.MONTH;
        }
        if (field.equals("d")) {
            fieldNum = Calendar.DATE;
        }
        if (field.equals("minute")) {
            fieldNum = Calendar.MINUTE;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(fieldNum, num);

        return cal.getTime();
    }

    /**
     * 转换默认格式的时间为Date
     *
     * @param dateStr
     * @return
     */
    public static Date formatStr2DateDefault(String dateStr) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(C_TIME_PATTON_DEFAULT);
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            try {
                sdf = new SimpleDateFormat(C_DATE_PATTON_DEFAULT);
                date = sdf.parse(dateStr);
            } catch (Exception e1) {
                return null;
            }
        }
        return date;
    }

    public String getInterval(Date createTime) {
        return getInterval(formatDate2Str(createTime));
    }

    public String getInterval(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        String interval = null;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date) sd.parse(createtime, pos);
        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒
        if (time / 1000 < 10 && time / 1000 >= 0) {
            //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
            interval = "刚刚";
        } else if (time / 3600000 < 24 && time / 3600000 >= 0) {
            //如果时间间隔小于24小时则显示多少小时前
            int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
            interval = h + "小时前";
        } else if (time / 60000 < 60 && time / 60000 > 0) {
            //如果时间间隔小于60分钟则显示多少分钟前
            int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
            interval = m + "分钟前";
        } else if (time / 1000 < 60 && time / 1000 > 0) {
            //如果时间间隔小于60秒则显示多少秒前
            int se = (int) ((time % 60000) / 1000);
            interval = se + "秒前";
        } else {
            //大于24小时，则显示正常的时间，但是不显示秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ParsePosition pos2 = new ParsePosition(0);
            Date d2 = (Date) sdf.parse(createtime, pos2);
            interval = sdf.format(d2);
        }
        return interval;
    }

    public static String timeInterval(Date date) {
        return timeInterval(new Date(), date, 0);
    }


    /**
     * 精确计算时间差，精确到日
     *
     * @param fistill 起始日期
     * @param nowtime 结束日期
     * @param type    type为1返回年月日（如：2年3个月零5天） 否则返回总的天数
     * @return
     */
    public static String timeInterval(Date fistill, Date nowtime, Integer type) {
        String TIME_YEAR = "yyyy";
        String TIME_MONEN = "MM";
        String TIME_DAY = "dd";

        int fyear = Integer.parseInt(new SimpleDateFormat(TIME_YEAR).format(fistill));//起始年
        int fmm = Integer.parseInt(new SimpleDateFormat(TIME_MONEN).format(fistill));//起始月
        int fdd = Integer.parseInt(new SimpleDateFormat(TIME_DAY).format(fistill));//起始日

        int nyear = Integer.parseInt(new SimpleDateFormat(TIME_YEAR).format(nowtime));//结束年
        int nmm = Integer.parseInt(new SimpleDateFormat(TIME_MONEN).format(nowtime));//结束月
        int ndd = Integer.parseInt(new SimpleDateFormat(TIME_DAY).format(nowtime));//结束日

        int cyear = nyear - fyear;
        int cmm = nmm - fmm;
        int cdd = ndd - fdd;


        int zyear = 0;
        int zmm = 0;
        int zdd = 0;

        int countDay = 0;  //年月日累计天数

        if (cdd < 0) {
            if (cmm < 0) {
                zyear = cyear - 1;
                zmm = (cmm + 12) - 1;
                int dd = getDay(zmm, nyear - 1);
                zdd = dd + cdd;
                countDay = zyear * 365 + zmm * 30 + zdd;
            } else if (cmm == 0) {
                zyear = cyear - 1;
                zmm = 12 - 1;
                int dd = getDay(zmm, nyear - 1);
                zdd = dd + cdd;
                countDay = zyear * 365 + zmm * 30 + zdd;
            } else {
                zyear = cyear;
                zmm = cmm - 1;
                int dd = getDay(zmm, nyear);
                zdd = dd + cdd;
                countDay = zyear * 365 + zmm * 30 + zdd;
            }
        } else if (cdd == 0) {
            if (cmm < 0) {
                zyear = cyear - 1;
                zmm = cmm + 12;
                zdd = 0;
                countDay = zyear * 365 + zmm * 30;

            } else if (cmm == 0) {
                zyear = cyear;
                zmm = 0;
                zdd = 0;
                countDay = zyear * 365 + zmm * 30;
            } else {
                zyear = cyear;
                zmm = cmm;
                zdd = 0;
                countDay = zyear * 365 + zmm * 30;
            }
        } else {
            if (cmm < 0) {
                zyear = cyear - 1;
                zmm = cmm + 12;
                zdd = cdd;
                countDay = zyear * 365 + zmm * 30 + zdd;
            } else if (cmm == 0) {
                zyear = cyear;
                zmm = 0;
                zdd = cdd;
                countDay = zyear * 365 + zmm * 30 + zdd;
            } else {
                zyear = cyear;
                zmm = cmm;
                zdd = cdd;
                countDay = zyear * 365 + zmm * 30 + zdd;
            }
        }
        String ptime = null;

        if (zdd != 0) {
            if (zmm != 0) {
                if (zyear != 0) {
                    ptime = zyear + "年" + zmm + "个月" + "零" + zdd + "天";
                } else {
                    ptime = zmm + "个月" + "零" + zdd + "天";
                }
            } else {
                if (zyear != 0) {
                    ptime = zyear + "年" + "零" + zdd + "天";
                } else {
                    ptime = zdd + "天";
                }
            }
        } else {
            if (zmm != 0) {
                if (zyear != 0) {
                    ptime = zyear + "年" + zmm + "个月";
                } else {
                    ptime = zmm + "个月";
                }
            } else {
                if (zyear != 0) {
                    ptime = zyear + "年";
                } else {
                    ptime = null;
                }
            }
        }
        if (type == 1) {
            return ptime;   //返回年月日（如：2年3个月零5天）
        } else {
            return String.valueOf(countDay);  //返回总天数
        }
    }


    /**
     * 根据年和月得到天数
     *
     * @param num  月
     * @param year 年
     * @return
     */
    public static int getDay(int num, int year) {
        if (num == 1 || num == 3 || num == 5 || num == 7 || num == 8 || num == 10 || num == 12) {
            return 31;
        } else if (num == 2) {
            //判断是否为闰年
            if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
                return 29;
            } else {
                return 28;
            }

        } else {
            return 30;
        }
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     */
    public static long getDiffMinute(Date one, Date two) {
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return min;
    }

    public static void main(String[] args) {
        System.out.println(timeInterval(DateUtils.formatDate("2016-10-12 00:00:00")));
    }
}
