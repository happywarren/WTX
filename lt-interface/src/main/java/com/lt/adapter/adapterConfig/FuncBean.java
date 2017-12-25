package com.lt.adapter.adapterConfig;

import org.dom4j.Element;

/**
 * 作者：邓玉明 时间：10-12-22 上午8:48 QQ：757579248 email：cndym@163.com
 */
public class FuncBean {
    private String name;
    private Integer checkSession;

    public FuncBean() {

    }

    public FuncBean(String name, Integer checkSession) {
        this.name = name;
        this.checkSession = checkSession;
    }

    public static FuncBean toServiceBean(Element element) {
        return new FuncBean();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCheckSession() {
        return checkSession;
    }

    public void setCheckSession(Integer checkSession) {
        this.checkSession = checkSession;
    }
}
