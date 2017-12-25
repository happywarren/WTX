package com.lt.fund.aop;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.DisposableBean;

import com.lt.util.utils.zookeeper.ZookeeperDisLock;

/**
 * 
 * TODO @UserSensitive 需要在@Transactional后，才能在程序异常时使事物回滚
 * @author XieZhibing
 * @date 2016年12月6日 下午5:44:04
 * @version <b>1.0.0</b>
 */
public class UserSensitiveAdvice implements MethodInterceptor, DisposableBean {
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		
		UserSensitive sensitive = AopUtils.getMostSpecificMethod(invocation.getMethod(),
				AopUtils.getTargetClass(invocation.getThis())).getAnnotation(UserSensitive.class);
		
		String method = invocation.getMethod().getName();
		logger.info(" start invoke method {}", method);
		long start = System.currentTimeMillis();
		
		Object result = null;
		if (sensitive != null) {
			int userId = (int) invocation.getArguments()[sensitive.value()];
			String cashValue = sensitive.cash();
			
			String key = cashValue + "_" + userId;
			ZookeeperDisLock lock = new ZookeeperDisLock(key);
			// 加锁，未获取锁会一直被堵塞
			lock.lock();
			try {
				result = invocation.proceed();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			} finally {
				lock.unlock();
			}
		} else {
			result = invocation.proceed();
		}
		logger.info("result:{}", result);
		
		long end = System.currentTimeMillis();
		logger.info("end invoke method:{}, cost:{}ms", method, end - start);
		
		return result;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
	}

	@Override
	public void destroy() throws Exception {
		logger.info("starting shudown executors");
		logger.info("shudown executors finished");
	}
}
