package com.lt.util.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * TODO 手动加载Spring初始化Bean的工具类
 * @date 2017年1月11日 下午5:50:03
 * @version <b>1.0.0</b>
 */
public class SpringUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    public static Object getBean(String bean) {
        Object object = null;
        try {
            object = applicationContext.getBean(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }


}