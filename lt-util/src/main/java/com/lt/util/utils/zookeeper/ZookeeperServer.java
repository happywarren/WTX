package com.lt.util.utils.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperServer {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static String ROOT_NODE = "/thread_lock_root";
	// 根节点
	public static String SCHEDULE_ROOT_NODE = "/schedule_root";

	public static ZooKeeper zk;

	private String server;

	public ZookeeperServer() {

	}

	public ZookeeperServer(String server) throws Exception {
		try {
			if (zk == null) {
				ZooKeeper zk2 = new ZooKeeper(server, 5000, new Mywatcher());
				// 根节点不存在，创建
				if (zk2.exists(ROOT_NODE, false) == null) {
					// 创建根节点
					zk2.create(ROOT_NODE, "lock_root_node".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				zk = zk2;
				logger.debug("zookeeper 连接成功，server:{}", server);

			}
		} catch (IOException | KeeperException | InterruptedException e) {
			zk = null;
			// e.printStackTrace();
			logger.warn("zookeeper 连接失败，server:{}， errorMsg:{}", server, e.getMessage());
			throw e;
		}
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public ZooKeeper getZk() {
		return zk;
	}

}

class Mywatcher implements Watcher {

	@Override
	public void process(WatchedEvent event) {

	}

}
