package com.lt.util.utils.zookeeper.local;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ZkLocalLock {
	
	private static Map<String, ZkLocalLock> instanes = new HashMap<>();

	private Lock lock ;// 锁  
	
	private ZkLocalLock(){
		lock = new ReentrantLock();
	}

	public static ZkLocalLock getInstance(String key) {
		if (!instanes.containsKey(key)) {
			synchronized (key) {
				if (!instanes.containsKey(key)) {
					
					ZkLocalLock localLock = new ZkLocalLock();
					instanes.put(key, localLock);
				}
			}
		}
		return instanes.get(key);
	}
	
	public void lock(){
		this.lock.lock();
	}
	
	public void unlock(){
		this.lock.unlock();
	}

	public void lockInterruptibly() throws InterruptedException {
		lock.lockInterruptibly();
	}

	public boolean tryLock() {
		return lock.tryLock();
	}
	

}
