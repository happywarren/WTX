package com.lt.business.core;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lt.util.utils.SpringUtils;

public class BusinessMain {
	 public static void main(String[] args) throws Exception {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:/config/*.xml"});
	        context.start();
	        SpringUtils.applicationContext =  context;
	        synchronized (BusinessMain.class) {
	            while (context.isActive()) {
	                try {
	                    BusinessMain.class.wait();
	                } catch (Throwable e) {
	                }
	            }
	        }
	    }

}
