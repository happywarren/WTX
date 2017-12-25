package com.lt.quota.core.comm.redis.lock;

import com.lt.quota.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DistributedLock {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final static long LOCK_EXPIRE = 1000L; //单个业务持有锁的时间5s,防止死锁

    private final static long LOCK_TRY_TIMEOUT = 500L; // 默认尝试5s

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 操作redis获取全局锁
     *
     * @param lock           锁的名称
     * @param timeout        获取的超时时间
     * @param lockExpireTime 获取成功后锁的过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean getLock(RedisLock lock, long timeout, long lockExpireTime) {
        try {
            if (Utils.isEmpty(lock.getName()) || Utils.isEmpty(lock.getValue())) {
                return false;
            }
            long startTime = System.currentTimeMillis();
            while (true) {
                if (redisTemplate.opsForValue().setIfAbsent(lock.getName(), lock.getValue())) {
                    redisTemplate.opsForValue().set(lock.getName(), lock.getValue(), lockExpireTime, TimeUnit.MILLISECONDS);
                    return true;
                }
                if (System.currentTimeMillis() - startTime > timeout) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }


    /**
     * 尝试获取全局锁
     *
     * @param lock 锁的名称
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(RedisLock lock) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_EXPIRE);
    }


    /**
     * 尝试获取全局锁
     *
     * @param lock    锁的名称
     * @param timeout 获取超时时间 单位ms
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(RedisLock lock, long timeout) {
        return getLock(lock, timeout, LOCK_EXPIRE);
    }


    /**
     * 尝试获取全局锁
     *
     * @param lock           锁的名称
     * @param timeout        获取锁的超时时间
     * @param lockExpireTime 锁的过期
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(RedisLock lock, long timeout, long lockExpireTime) {
        return getLock(lock, timeout, lockExpireTime);
    }

    /**
     * 释放锁
     */
    public void releaseLock(RedisLock lock) {
        if (!Utils.isEmpty(lock.getName())) {
            redisTemplate.delete(lock.getName());
        }
    }
}