package com.voyageone.security.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCache<K, V> implements Cache<K, V> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	/**
     * The wrapped Jedis instance.
     */
	RedisTemplate cache;
	
	/**
	 * The Redis key prefix for the sessions 
	 */
	private String keyPrefix = "shiro_redis_session:";


	private int expireTime = 0;
	
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
	
	/**
	 * 通过一个JedisManager实例构造RedisCache
	 */
	public RedisCache(RedisTemplate cache){
		 if (cache == null) {
	         throw new IllegalArgumentException("Cache argument cannot be null.");
	     }
	     this.cache = cache;
	}
	
	/**
	 * Constructs a cache instance with the specified
	 * Redis manager and using a custom key prefix.
	 * @param cache The cache manager instance
	 * @param prefix The Redis key prefix
	 */
	public RedisCache(RedisTemplate cache, String prefix, int expireTime){

		this( cache );

		// set the prefix
		this.keyPrefix = prefix;
		this.expireTime = expireTime;
	}

	/**
	 * 获得byte[]型的key
	 * @param key
	 * @return
	 */
	private String getPrifixKey(K key) throws Exception {
		if(key instanceof String){
			String preKey = this.keyPrefix + key;
    		return preKey;
    	}else{
			byte[] prefix = keyPrefix.getBytes();
			byte[] keybytes  = cache.getKeySerializer().serialize(key);
			byte[] byte_3 = new byte[prefix.length + keybytes.length];
			System.arraycopy(prefix, 0, byte_3, 0, prefix.length);
			System.arraycopy(keybytes, 0, byte_3, prefix.length, keybytes.length);
			return new String(byte_3,"UTF-8");

		}
	}
 	
	@Override
	public V get(K key) throws CacheException {
		logger.debug("根据key从Redis中获取对象 key [" + key + "]");
		try {
			if (key == null) {
	            return null;
	        }else{

				V value = (V)cache.opsForValue().get(getPrifixKey(key));
				cache.expire(getPrifixKey(key), expireTime, TimeUnit.SECONDS);
	        	return value;
	        }
		} catch (Throwable t) {
			throw new CacheException(t);
		}

	}

	@Override
	public V put(K key, V value) throws CacheException {
		logger.debug("根据key从存储 key [" + key + "]");
		 try {
			 	cache.opsForValue().set(getPrifixKey(key), value);
			 	cache.expire(getPrifixKey(key), expireTime, TimeUnit.SECONDS);
	            return value;
	        } catch (Throwable t) {
	            throw new CacheException(t);
	        }
	}

	@Override
	public V remove(K key) throws CacheException {
		logger.debug("从redis中删除 key [" + key + "]");
		try {
            V previous = get(key);
			cache.delete(getPrifixKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public void clear() throws CacheException {
		//do nothing
		logger.debug("从redis中删除所有元素");
		try {
            Set<K> keys = keys();
			if (!CollectionUtils.isEmpty(keys)) {
				cache.delete(keys);
			}
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public int size() {
		try {
			return keys().size();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		try {
            Set<K> keys = cache.keys(this.keyPrefix + "*");
            if (CollectionUtils.isEmpty(keys)) {
            	return Collections.emptySet();
            }else{
            	Set<K> newKeys = new HashSet<K>();
            	for(K key:keys){
            		newKeys.add((K)key);
            	}
            	return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public Collection<V> values() {
		try {
            Set<byte[]> keys = cache.keys(this.keyPrefix + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (byte[] key : keys) {
                    @SuppressWarnings("unchecked")
					V value = get((K)key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

}
