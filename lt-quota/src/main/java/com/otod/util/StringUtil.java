/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;

import java.text.DecimalFormat;

/**
 *
 * @author Administrator
 */
public class StringUtil {

    public static String formatTime(int time) {

        int hour = time / 100;
        int min = time % 100;

        String h = completeByBefore(String.valueOf(hour), 2, "0");
        String m = completeByBefore(String.valueOf(min), 2, "0");
        return h + ":" + m;
    }

    public static String formatHHmmss(int time) {

        int hour = time / 10000;
        int min = (int) (time / 100) % 100;
        int sec = time % 100;

        String h = completeByBefore(String.valueOf(hour), 2, "0");
        String m = completeByBefore(String.valueOf(min), 2, "0");
        String s = completeByBefore(String.valueOf(sec), 2, "0");
        return h + ":" + m + ":" + s;
    }

    public static String formatDate(int date) {

        int year = date / 10000;
        int month = (date / 100) % 100;
        int day = date % 100;
        String y = completeByBefore(String.valueOf(year), 4, "0");
        String m = completeByBefore(String.valueOf(month), 2, "0");
        String d = completeByBefore(String.valueOf(day), 2, "0");

        return y + "-" + m + "-" + d;
    }

    public static String formatNumber(double value, int num) {
        String str = null;
        String dfStr = "###0";
        if (num > 0) {
            dfStr += ".";
            for (int i = 0; i < num; i++) {
                dfStr += "0";
            }
        }

        DecimalFormat df = new DecimalFormat(dfStr);
        str = df.format(value);

        return str;
    }

    public static String StringArrayToString(String[] datas) {
        String str = "";
        for (int i = 0; i < datas.length; i++) {
            if (i != 0) {
                str += ",";
            }
            str += String.valueOf(datas[i]);
        }
        return str;
    }

    public static String DoubleArrayToString(double[] datas) {
        String str = "";
        for (int i = 0; i < datas.length; i++) {
            if (i != 0) {
                str += ",";
            }
            str += String.valueOf(datas[i]);

        }
        return str;
    }

    public static byte[] completeByAfter(byte[] datas, int len, byte add) {
        byte[] outdatas = new byte[len];

        System.arraycopy(datas, 0, outdatas, 0, datas.length);

        for (int i = 0; i < len - datas.length; i++) {
            outdatas[datas.length + i] = add;
        }

        return outdatas;
    }

    public static String completeByAfter(String str, int len, String addStr) {

        while (str.length() < len) {
            for (int i = 0; i < len - str.length(); i++) {
                str += addStr;
            }
        }
        return str;
    }

    public static String completeByBefore(String str, int len, String addStr) {
        int num = len - str.length();
        String tmp = "";
        for (int i = 0; i < num; i++) {
            tmp += addStr;
        }
        str = tmp + str;
        return str;
    }
}
