package com.lt.util.utils.zookeeper;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.utils.zookeeper.local.ZkLocalLock;

/**
 * 
 * TODO Zookeeper分布式锁
 * @author XieZhibing
 * @date 2016年12月6日 下午2:04:00
 * @version <b>1.0.0</b>
 */
public class ZookeeperDisLock {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String rootNode = ZookeeperServer.ROOT_NODE;

	public static String DEEFAULT_NODE = "distributed_thread_lock";

	private String key;

	public String nodeName;

	private Watcher watch;

	private static ZooKeeper zk;

	Integer notifyCount = 0;

	/**
	 * 分布式锁是否生效标识，默认为分布式锁生效
	 */
	private Boolean zkFlag = true;
	/** ZK服务失效时，使用的本地线程锁 */
	private ZkLocalLock localLock;
	
	/**
	 * @param zkServer Zookeeper服务器连接地址 ps: 192.168.1.88:8161
	 * @param key 加锁Key，字符串
	 */
	public ZookeeperDisLock(String key) {
		this.key = key;
		zkFlag = false;
		localLock = ZkLocalLock.getInstance(key);
		try {
			watch = new TaskLockWatch(this);
			zk = ZookeeperServer.zk;
			if (zk == null) {
				zk = new ZookeeperServer("121.40.85.43:2181").getZk();
			}
			if (zk != null) {
				logger.debug("使用分布式线程锁，key:{}", key);
				zkFlag = true;
			}
		} catch (Exception e) {// ZK服务器连接失败，使用本地内存锁
//			 e.printStackTrace();
			logger.debug("使用本地线程锁，key:{}", key);
		}

	}
	/**
	 * 
	 *
	 * 描述:加锁,实际逻辑为在Zookeeper上创建临时节点 判断自己是否为最小节点，是，则正常运行，不是，等待
	 *
	 * @author jiupeng
	 * @created 2015年7月29日 下午3:12:01
	 * @since v1.0.0
	 * @return void
	 */
	public void lock() {
		//在同一个程序内部时，先内部竞争获取锁，获取到锁的可以去尝试获取分布式锁，否则等待
		localLock.lock();
		//再去尝试获取分布式锁
		if (zkFlag) {
			try {
				try {
					if (zk.exists(parentNode(), false) == null) {
						zk.create(parentNode(), "lock_root_node".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String node = parentNode() + "/lock_";
				this.nodeName = zk.create(node, (System.currentTimeMillis() + "-task").getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.EPHEMERAL_SEQUENTIAL);
				while (true) {//防止出现创建节点不成功导致的问题
					if (this.nodeName == null || this.nodeName.equals(node)) {
						this.nodeName = zk.create(node, (System.currentTimeMillis() + "-task").getBytes(),
								Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
					} else {
						break;
					}
				}
				// 尝试获得锁
				getLock();
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}  
	}

	/**
	 * 
	 *
	 * 描述:删除创建的临时节点
	 *
	 * @author jiupeng
	 * @created 2015年7月29日 下午3:13:24
	 * @since v1.0.0
	 * @return void
	 */
	public void unlock() {
		if (zkFlag) {
			try {
				zk.delete(this.nodeName, -1);
			} catch (InterruptedException | KeeperException e) {
				e.printStackTrace();
			} 
		} 
		localLock.unlock();
	}

	/**
	 * 
	 * 描述:尝试获得锁，未获得锁时，使用wait方法等待,阻塞
	 *
	 * @author jiupeng
	 * @created 2015年7月29日 下午5:29:19
	 * @since v1.0.0
	 * @return void
	 */
	private void getLock() {
		try {
			List<String> list = zk.getChildren(parentNode(), false);
			String miniNode = parentNode() + "/" + getMiniNodeName(list);
			if (!this.nodeName.equals(miniNode)) {
				Stat stat = zk.exists(miniNode, watch);
				if (stat != null) {
					boolean miniNodeNotifyAlready  = false;
					synchronized (this) {//如果进入该代码前，ZK 已经通知当前监控的最小节点已删除，造成滑动条件，会导致死锁
						if (notifyCount != 0) {//如果已经通知过，则notifyCount>0
							miniNodeNotifyAlready = true;
						}
						if (!miniNodeNotifyAlready) {//已经通知过，不执行等待，直接递归
							this.wait();//让出执行权，当前线程等待
							getLock();//被唤醒后递归调用
						}
					}
					if(miniNodeNotifyAlready){
						notifyCount = 0;
						getLock();//递归调用
					}
				} else {
					getLock();
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *
	 * 尝试获取锁，获取成功，返回true,否者返回false,成功或失败都会正常返回，不阻塞 <br/>
	 * 成功获取锁后，需要手动释放 <br/>
	 * 获取锁失败时，不需要手动释放
	 *
	 * @author jiupeng
	 * @created 2015年12月23日 上午11:22:58
	 * @since v1.0.0
	 * @return
	 * @return boolean
	 */
	public boolean tryLock() {
		boolean a = localLock.tryLock();
		boolean res = false;
		if (a) {
			if (zkFlag) {
				try {
					if (zk.exists(parentNode(), false) == null) {
						zk.create(parentNode(), "lock_root_node".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}

					String node = parentNode() + "/lock_";
					this.nodeName = zk.create(node, (System.currentTimeMillis() + "-task").getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
					List<String> list = zk.getChildren(parentNode(), false);
					String miniNode = parentNode() + "/" + getMiniNodeName(list);
					if (!this.nodeName.equals(miniNode)) {
						Stat stat = zk.exists(miniNode, watch);
						// 最小节点是否状态正常
						if (stat != null) {// 最小节点
							list = zk.getChildren(parentNode(), false);
							miniNode = parentNode() + "/" + getMiniNodeName(list);
							if (this.nodeName.equals(miniNode)) {
								res = true;
							} else {
								zk.delete(this.nodeName, -1);
								res = false;
							}
						} else {
							zk.delete(this.nodeName, -1);
							res = false;
						}
					} else {
						res = true;
					}
				} catch (KeeperException | InterruptedException e) {
					try {
						zk.delete(this.nodeName, -1);
					} catch (InterruptedException | KeeperException e1) {
						e1.printStackTrace();
					}
					res = false;
				}
				if(res == false){
					localLock.unlock();
				}
				return res;
			}
		}
		return a;
	}

	private String parentNode() {
		return rootNode + "/" + StringUtils.defaultIfBlank(key, DEEFAULT_NODE);
	}

	// 获取节点列表中最小节点
	private String getMiniNodeName(List<String> strs) {
		String args[] = new String[strs.size()];
		strs.toArray(args);
		Arrays.sort(args);
		return args[0];
	}

}
