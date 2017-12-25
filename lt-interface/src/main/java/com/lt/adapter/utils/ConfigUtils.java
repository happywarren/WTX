package com.lt.adapter.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class ConfigUtils {

    private static Map<String, String> map = new HashMap<String, String>();

    static {
        forInstance();
    }

    public static void forInstance() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = ConfigUtils.class.getResourceAsStream("/config.properties");
            properties.load(inputStream);
            Enumeration all = properties.keys();
            while (all.hasMoreElements()) {
                String name = (String) all.nextElement();
                String value = (String) properties.get(name);
                map.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String getValue(String key) {
        if (map.containsKey(key)) {
            return map.get(key).trim();
        }
        throw new IllegalArgumentException("属性值（" + key + "）不存在");
    }

    public static void update(String key, String value) {
        if (map.containsKey(key)) {
            map.put(key, value);
        }
    }


    public static String getClassPath() {
        URL url = ConfigUtils.class.getClassLoader().getResource("");
        if (null == url) {
            return "";
        }
        return url.getPath();
    }
}