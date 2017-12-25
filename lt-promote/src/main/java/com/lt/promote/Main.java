package com.lt.promote;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lt.util.utils.SpringUtils;

public class Main {
	 public static void main(String[] args) throws Exception {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:/config/*.xml"});
	        context.start();
	        SpringUtils.applicationContext =  context;
	        synchronized (Main.class) {
	            while (context.isActive()) {
	                try {
	                    Main.class.wait();
	                } catch (Throwable e) {
	                }
	            }
	        }
	    }

}
