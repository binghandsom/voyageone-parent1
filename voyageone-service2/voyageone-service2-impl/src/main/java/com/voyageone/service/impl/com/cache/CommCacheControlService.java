package com.voyageone.service.impl.com.cache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.redis.CacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * CommCacheControlService
 *
 * @author chuanyu.liang 16/8/5
 * @version 2.1.0
 * @since 2.0.0
 */
@Service
public class CommCacheControlService {

    @Autowired
    private CommCacheService commCacheService;

    public Set<String> getCacheKeySet() {
        // ConfigData
        Set<String> result = CacheHelper.getCacheTemplate().keys(CacheKeyEnums.CONFIG_ALL_KEY_REGEX);
        // COMM_CACHE_DATA
        Set<String> commDataSet = CacheHelper.getCacheTemplate().keys(CacheKeyEnums.COMM_CACHE_DATA + "*");

        // COMM_CACHE_DATA
        Set<String> cmsDataSet = CacheHelper.getCacheTemplate().keys(CacheKeyEnums.CMS_CACHE_DATA + "*");

        if (commDataSet != null && !commDataSet.isEmpty()) {
            result.addAll(commDataSet);
        }

        if (cmsDataSet != null && !cmsDataSet.isEmpty()) {
            result.addAll(cmsDataSet);
        }

        return result;
    }

    public void deleteCache(CacheKeyEnums.KeyEnum cacheKeyEnum) {
        if (CacheKeyEnums.KeyEnum.COMM_CACHE_DATA == cacheKeyEnum) {
            commCacheService.deleteAllCache();
        } else {
            CacheHelper.delete(cacheKeyEnum.toString());
        }
    }

    public void deleteCache(String cacheKey) {
        CacheHelper.delete(cacheKey);
    }

}
