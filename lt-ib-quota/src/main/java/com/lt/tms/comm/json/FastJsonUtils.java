package com.lt.tms.comm.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.text.SimpleDateFormat;
import java.util.*;


public class FastJsonUtils {

    /**
     * 用fastjson 将json字符串解析为一个 JavaBean
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T getJson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 用fastjson 将json字符串 解析成为一个 List<JavaBean> 及 List<String>
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> List<T> getArrayJson(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 用fastjson 将json字符串 解析成为一个 List<JavaBean> 及 List<String>
     *
     * @param jsonString
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getArrayJson(String jsonString) {
        List<T> list = new ArrayList<T>();
        try {
            list = (List<T>) JSON.parseArray(jsonString);
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> getListMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, String>> getListMapString(String jsonString) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, String>>>() {
            });
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
     *
     * @param jsonString
     * @return
     */
    public static Map<String, Object> getMap(String jsonString) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = JSON.parseObject(jsonString, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
        }
        return map;
    }

    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,String>>
     *
     * @param jsonString
     * @return
     */
    public static Map<String, String> getStringMap(String jsonString) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = JSON.parseObject(jsonString, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
        }
        return map;
    }

    public static String toJSONString(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();
        String s;
        JSONSerializer serializer = new JSONSerializer(out);
        SerializerFeature arr$[] = features;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++) {
            SerializerFeature feature = arr$[i$];
            serializer.config(feature, true);
        }

        serializer.getValueFilters().add(new ValueFilter() {
            public Object process(Object obj, String s, Object value) {
                if (null != value) {
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return sdf.format(date);
                    }
                    return value;
                } else {
                    return "";
                }
            }
        });
        serializer.write(object);
        s = out.toString();
        out.close();
        return s;
    }

    public static String toJson(Map map) {
        return toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String toJson(List list) {
        return toJSONString(list, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String toJson(Class cls) {
        return toJSONString(cls, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String toJson(Object cls) {
        return toJSONString(cls, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static void main(String[] args) {
        System.out.println(FastJsonUtils.toJson(new ArrayList()));
    }
}
