package com.lt.util.utils.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class TaskLockWatch implements Watcher {
	ZookeeperDisLock disLock;

	TaskLockWatch(ZookeeperDisLock disLock) {
		this.disLock = disLock;
	}

	@Override
	public void process(WatchedEvent event) {
		// 节点删除事件，触发获取锁逻辑
		if (event.getType().equals(EventType.NodeDeleted)) {
			synchronized (disLock) {
				disLock.notifyCount++;
				disLock.notify();
			}
		}
	}
}
