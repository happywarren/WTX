package com.lt.user.core;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lt.util.utils.SpringUtils;

public class UserMain {
	 public static void main(String[] args) throws Exception {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:/config/*.xml"});
	        context.start();
	        SpringUtils.applicationContext =  context;
	        synchronized (UserMain.class) {
	            while (context.isActive()) {
	            try {
						UserMain.class.wait();
	                } catch (Throwable e) {
	                }
	            }
	        }
	    }

}
