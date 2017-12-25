package com.lt.fund.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 如果某个方法被标记为@UserSensitive,则应当以userId散列到同一个线程进行队列式的任务处理。<br/>
 * value 标识userId在参数中的下标位置<br/>
 * cash 标识当前逻辑是处理现金或积分<br/>
 * （使用 cash_userId创建线程锁，保证唯一性）
 * 
 * @author Boyce.Wang
 * @createTime 2015年3月6日
 * @version
 * @update Joyance
 * @modify_date  2016年1月11日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserSensitive {
	int value() default 0;
	String cash() default "cash";
}
