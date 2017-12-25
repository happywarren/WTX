package com.lt.adapter.adapterConfig;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.lt.util.utils.StringTools;

public class ServiceBean {
    private String cmd;
    private Integer token;
    private List<FuncBean> funcBeanList;

    public static ServiceBean toServiceBean(Element element) {
        ServiceBean serviceBean = new ServiceBean(element.attributeValue("cmd"),StringTools.formatInt(element.attributeValue("token"), 0));

        List<Element> funcElementList = element.elements("func");
        if (StringTools.isNotEmpty(funcElementList)) {
            List<FuncBean> funcList = new ArrayList<FuncBean>();
            for (Element funcElement : funcElementList) {
                FuncBean funcBean = new FuncBean(funcElement.attributeValue("name"), StringTools.formatInt(funcElement.attributeValue("token"), 0));
                funcList.add(funcBean);
            }
            serviceBean.setFuncBeanList(funcList);
        }
        return serviceBean;

    }

    public ServiceBean() {
    }

    public ServiceBean(String cmd, Integer token) {
        this.cmd = cmd;
        this.token = token;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public List<FuncBean> getFuncBeanList() {
        return funcBeanList;
    }

    public void setFuncBeanList(List<FuncBean> funcBeanList) {
        this.funcBeanList = funcBeanList;
    }
}
