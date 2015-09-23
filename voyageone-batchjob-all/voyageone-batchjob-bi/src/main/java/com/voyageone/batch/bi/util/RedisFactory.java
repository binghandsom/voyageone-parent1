package com.voyageone.batch.bi.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.voyageone.batch.configs.FileProperties;

public class RedisFactory {

	private static JedisPool pool = null;

	/**
	 * getPool
	 * @return
	 */
	public static JedisPool getPool() {
		if (pool == null) {
			String ip = FileProperties.readValue("bi.jedis.driver.path");
			return getPool(ip);
		}
		return pool;
	}
	
	
	/**
	 * getPool
	 * @return
	 */
	public static JedisPool getPool(String ip) {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(50);
			config.setMaxIdle(5);
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, ip);
		}
		return pool;
	}
	
	/**
	 * returnResource
	 * @return
	 */
	private static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResourceObject(redis);
        }
    }
	
	/**
	 * get
	 * @param key
	 * @return
	 */
	public static String get(String key){
        String value = null;
        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
            pool.returnResourceObject(jedis);
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        return value;
    }
	
	/**
	 * set
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value){        
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.set(key, value);
        } catch (Exception e) {
            //释放redis对象
            pool.returnResourceObject(jedis);
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
    }

}
