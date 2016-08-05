package com.voyageone.service.impl.com.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *  *
 * @author chuanyu.liang 16/8/5
 * @version 2.1.0
 * @since 2.0.0
 */
@Service
public class CommCacheService {

    @Cacheable(value={"CommCache"}, key="'CommCache_' + #cacheBizName + '_' + #cacheKey")
    public <T> T getCache(String cacheBizName, String cacheKey) {
        return null;
    }

    @CachePut(value={"CommCache"}, key="'CommCache_' + #cacheBizName + '_' + #cacheKey")
    public <T> T setCache(String cacheBizName, String cacheKey, T cacheData) {
        return cacheData;
    }

    @CacheEvict(value={"CommCache"}, key="'CommCache_' + #cacheBizName + '_' + #cacheKey")
    public void deleteCache(String cacheBizName, String cacheKey) {
    }

    @CacheEvict(value={"CommCache"}, allEntries=true)
    public void deleteAllCache() {
    }

}
