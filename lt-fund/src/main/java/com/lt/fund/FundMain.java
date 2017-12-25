package com.lt.fund;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lt.util.utils.SpringUtils;

public class FundMain {
	 public static void main(String[] args) throws Exception {
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:/config/*.xml"});
	        context.start();
	        SpringUtils.applicationContext =  context;
	        synchronized (FundMain.class) {
	            while (context.isActive()) {
	                try {
						FundMain.class.wait();
	                } catch (Throwable e) {
	                }
	            }
	        }
	    }

}
