package com.voyageone.common.redis;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/3/11.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CacheHelper {

    /* 公用分隔符，防止因分割符产生的bug而声明 */
    public static final String SKIP= "$-SKIP-$";

    /* redis模板 */
    private static RedisTemplate<String, Map<String, Object>> redisTemplate = CacheTemplateFactory.getCacheTemplate();

    /* SSB表示缓存数据类型为<String,String,Bean> 重写缓存hash表 先删除后添加 */
    public static void reWriteSSB(CacheKeyEnums key, Map reWriteMap) {
        redisTemplate.delete(key.toString());
        reFreshSSB(key, reWriteMap);
    }

    /* SSB表示缓存数据类型为<String,String,Bean> key必须在CacheKeyEnums声明，否则抛出异常 */
    public static void reFreshSSB(String key, Map refreshMap) {
        reFreshSSB(CacheKeyEnums.valueOf(key), refreshMap);
    }

    /* SSB表示缓存数据类型为<String,String,Bean> */
    public static void reFreshSSB(CacheKeyEnums key, Map refreshMap) {
        hashWrite(key, refreshMap);
    }

    /* SSS表示缓存数据类型为<String,String,String> 缓存Key,Value配置文件 */
    public static void reFreshSSS(String key, Map<String, String> refreshMap) {
        hashWrite(CacheKeyEnums.valueOf(key), refreshMap);
    }

    /* SSS表示缓存数据类型为<String,String,String> 缓存Key,Value配置文件 */
    public static void reFreshSSS(CacheKeyEnums key, Map<String, String> refreshMap) {
        hashWrite(key, refreshMap);
    }

    /**
     * 如果上边方法不能满足，
     * 建议重写调用此方法，
     * 不建议public此方法，此方法用于总控接受到的dataMap
     * 写map数据到key的redis hash表
     *
     * @param key     redis Key
     * @param mapData redisMap<HK,KV>
     */
    private static void hashWrite(CacheKeyEnums key, Map mapData) {
        if (MapUtils.isEmpty(mapData)) return;
        ChangeSourceForHash<String> changeSource = new ChangeSourceForHash<>(key.toString(), redisTemplate.opsForHash());
        changeSource.getPutMaps().putAll(mapData);
        changeSource.callCache();
    }

    /**
     * 更改的资源，此对象可扩展
     * （可以增加删除sets,modifytime时间）
     */
    private static class ChangeSourceForHash<T> {
        /* 更新Map数据集 */
        private Map<String, T> putMaps = new HashMap<>();
        /* 操作选项 */
        private HashOperations<String, String, T> hashOperations;
        /* 缓存主Key */
        private String cacheKey;

        /**
         * 调用服务器缓存
         */
        private void callCache() {
            if (StringUtil.isEmpty(cacheKey)) return;
            if (MapUtils.isNotEmpty(putMaps)) hashOperations.putAll(cacheKey, putMaps);
        }

        /**
         * 构造source
         *
         * @param cacheKey       缓存主Key
         * @param hashOperations 缓存选项
         */
        private ChangeSourceForHash(String cacheKey, HashOperations<String, String, T> hashOperations) {
            this.hashOperations = hashOperations;
            this.cacheKey = cacheKey;
        }

        private Map<String, T> getPutMaps() {
            return putMaps;
        }
    }

}
