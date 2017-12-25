package com.lt.util.utils.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

/**   
* 项目名称：lt-util   
* 类名称：RedisInfoOperate   
* 类描述：redits 操作   (并发相关控制)
* 创建人：yuanxin   
* 创建时间：2017年5月24日 下午2:12:38      
*/
public class RedisInfoOperate {
	
	/**
	 * 使用redis进行并发控制
	 * @param redisTemplate
	 * @param key
	 * @param value
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月24日 下午3:22:43
	 */
	@SuppressWarnings("unchecked")
	public static boolean setSuccess(RedisTemplate redisTemplate,final String key,final String value){
		boolean flag = false ;
		flag = (boolean) redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				boolean flag = connection.setNX(key.getBytes(), value.getBytes());
				return flag ;
			}
			
		});
		
		return flag;
	}

	/**
	 * redis 加锁（包含锁失效时间）
	 * @param redisTemplate
	 * @param key
	 * @param leaseTime 锁失效时间
	 * @param unit 时间单位
	 * @return true:加锁成功 false:加锁失败，存在相同锁
	 */
	public static boolean addLock(RedisTemplate redisTemplate, final String key, final long leaseTime, final TimeUnit unit){
		boolean flag = false ;
		flag = (boolean) redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				boolean flag = connection.setNX(key.getBytes(), key.getBytes());
				if(flag){
					connection.expire(key.getBytes(), leaseTime);
				}
				return flag ;
			}

		});

		return flag;
	}
	/***
	 * 判断key值是否存在
	 * @param redisTemplate
	 * @param key
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月25日 上午11:29:28
	 */
	@SuppressWarnings("unchecked")
	public static boolean isExit(RedisTemplate redisTemplate,final String key){
		boolean flag = false ;
		flag = (boolean) redisTemplate.execute(new RedisCallback() {

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
			
		});
		
		return flag;
	}
	
	/**
	 * 删除并发锁数据（防止数据太多）
	 * @param redisTemplate
	 * @param key    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月24日 下午3:24:56
	 */
	@SuppressWarnings("unchecked")
	public static void delKeyLock(RedisTemplate redisTemplate,final String key){
		redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.del(key.getBytes());
				return null ;
			}
		});
	}
	
	/***
	 * 模糊匹配删除
	 * @param redisTemplate
	 * @param key    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月25日 上午11:29:58
	 */
	@SuppressWarnings("unchecked")
	public static void delKeyLockMutil(RedisTemplate redisTemplate,final String key){
		redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Set<byte[]> keys = connection.keys((key+"*").getBytes());
				if(!keys.isEmpty()){
					for(byte[] bytes : keys){
						System.out.println(bytes[bytes.length -1]);
						connection.del(bytes);
					}
				}
				
				return null ;
			}
		});
	}
}
