package com.lt.tms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者: 邓玉明
 * 日期: 2010-10-27
 * 时间: 10:41:30
 * qq: 757579248
 * email: cndym@163.com
 */
public class PatternUtils {
    public static boolean regValidate(String Content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        return matcher.matches();
    }


    public static List<String> regFinds(String Content, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static List<String> regFinds(String Content, String regex, int index) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        while (matcher.find()) {
            list.add(matcher.group(index));
        }
        return list;
    }

    public static String regFind(String Content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        matcher.find();
        return matcher.group();
    }

    public static String regFind(String Content, String regex, int index) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        matcher.find();
        return matcher.group(index);
    }

    public static String regReplace(String Content, String regex, String regStr) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Content);
        return matcher.replaceAll(regStr);
    }

    
    public static void main(String[] args){
        String temp = "this $is,1213$> $te>st $className$ {}";
        List<String> list = regFinds(temp,"\\$([\\w\\(\\),]*)\\$",1);
        for (String s : list) {
            System.out.println(s);
        }
    }

}
