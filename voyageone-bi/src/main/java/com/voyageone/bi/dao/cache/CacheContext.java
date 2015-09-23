package com.voyageone.bi.dao.cache;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author wenchao.ren 2015/1/5.
 */
public class CacheContext {

	private Map<String, Object> cache = Maps.newConcurrentMap();
	
	private static CacheContext self = null;
	
	public static CacheContext getInstance() {
		if (self == null) {
			 self = new CacheContext();
		}
		return self;
	}

	public Object get(String key) {
		return cache.get(key);
	}

	public void addOrUpdateCache(String key, Object value) {
		cache.put(key, value);
	}

	// 根据 key 来删除缓存中的一条记录
	public void evictCache(String key) {
		if (cache.containsKey(key)) {
			cache.remove(key);
		}
	}

	// 清空缓存中的所有记录
	public void evictCache() {
		cache.clear();
	}

}
