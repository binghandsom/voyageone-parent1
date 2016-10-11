package com.voyageone.web2.openapi.oauth2.cache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * OAuth2CacheService
 *
 * @author chuanyu.liang 16/8/5
 * @version 2.1.0
 * @since 2.0.0
 */
@Service
public class OAuth2CacheService {

    @Cacheable(value = {CacheKeyEnums.OAUTH_CACHE_DATA}, key = " 'OATUH_CACHE_' + #cacheBizName + '_' + #cacheKey")
    public <T> T getCache(String cacheBizName, String cacheKey) {
        return null;
    }

    @CachePut(value = {CacheKeyEnums.OAUTH_CACHE_DATA}, key = " 'OATUH_CACHE_' + #cacheBizName + '_' + #cacheKey")
    public <T> T setCache(String cacheBizName, String cacheKey, T cacheData) {
        return cacheData;
    }

    @CacheEvict(value = {CacheKeyEnums.OAUTH_CACHE_DATA}, key = " 'OATUH_CACHE_' + #cacheBizName + '_' + #cacheKey")
    public void deleteCache(String cacheBizName, String cacheKey) {
    }

    @CacheEvict(value = {CacheKeyEnums.OAUTH_CACHE_DATA}, allEntries = true)
    public void deleteAllCache() {
    }

}
