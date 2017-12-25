package com.lt.adapter.adapterConfig;

import com.lt.adapter.utils.ConfigUtils;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServiceList {

    private static Map<String, ServiceBean> map = new HashMap<String, ServiceBean>();
    private static Map<String, Integer> tokenMap = new HashMap<String, Integer>();

    static {
        forInstance();
    }

    public static void forInstance() {
        try {
            SAXReader xmlReader = new SAXReader();
            Document document = xmlReader.read(ConfigUtils.getClassPath() + ConfigUtils.getValue("SERVICE.CONFIG.PATH"));
            List list = document.selectNodes("/services/service");
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                ServiceBean serviceBean = ServiceBean.toServiceBean(element);
                map.put(serviceBean.getCmd(), serviceBean);
                tokenMap.put(serviceBean.getCmd(), serviceBean.getToken());

                List<FuncBean> funcBeanList = serviceBean.getFuncBeanList();
                if (null != funcBeanList) {
                    for (FuncBean funcBean : funcBeanList) {
                        tokenMap.put(serviceBean.getCmd() + funcBean.getName(), funcBean.getCheckSession());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ServiceBean getServiceBean(String cmd) {
        if (map.containsKey(cmd)) {
            return map.get(cmd);
        }
        throw new LTException(LTResponseCode.ER400);
    }

    public static boolean token(String cmd, String func) {
        if (tokenMap.containsKey(cmd)) {
            int check = tokenMap.get(cmd);
            if (check == 0) {
                return false;
            }
            if (tokenMap.containsKey(cmd + func)) {
                int funcCheck = tokenMap.get(cmd + func);
                if (funcCheck == 0) {
                    return false;
                }
            }else{
            	return false;
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        boolean check = ServiceList.token("3103", "nickName");
        System.out.println(check);
    }
}
