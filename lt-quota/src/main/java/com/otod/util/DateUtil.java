/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class DateUtil {

    public static String formatDate(Date date, String format) {
        String str = "";
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        str = df.format(date);
        return str;
    }

    public static Date parseDate(String str, String format) {
        SimpleDateFormat df;
        Date date = null;
        try {
            df = new SimpleDateFormat(format);
            date = df.parse(str);
        } catch (ParseException pe) {
            pe.printStackTrace();;
        }

        return date;
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        return day;
    }
}
