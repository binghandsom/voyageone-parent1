package com.voyageone.security.shiro.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManager implements CacheManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RedisCacheManager.class);

	// fast lookup by name map
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	private RedisTemplate redisManager;

	/**
	 * The Redis key prefix for caches 
	 */
	private String keyPrefix = "shiro_redis_cache:";

	private String altKeyPrefix = "alt_shiro_redis_cache:";

	private int expireTime = 1800;



	public String getAltKeyPrefix() {
		return altKeyPrefix;
	}

	public void setAltKeyPrefix(String altKeyPrefix) {
		this.altKeyPrefix = altKeyPrefix;
	}



	/**
	 * Returns the Redis session keys
	 * prefix.
	 * @return The prefix
	 */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * Sets the Redis sessions key 
	 * prefix.
	 * @param keyPrefix The prefix
	 */
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}


	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		logger.debug("获取名称为: " + name + " 的RedisCache实例");
		
		Cache c = caches.get(name);
		
		if (c == null) {

			// initialize the Redis manager instance
//			redisManager.init();

			// create a new cache instance
			c = new RedisCache<K, V>(redisManager, keyPrefix + name, expireTime);

			// add it to the cache collection
			caches.put(name, c);
		}

		return c;
	}

	public RedisTemplate getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(RedisTemplate redisManager) {
		this.redisManager = redisManager;
	}

}
