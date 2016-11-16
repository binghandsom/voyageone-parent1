package com.voyageone.common.redis;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.spring.SpringContext;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author aooer 2016/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class CacheHelper {

    private final static String RELAOD = "reload";

//    @SuppressWarnings("unchecked")
//    public static RedisTemplate getCacheTemplate() {
//        return SpringContext.getBean(RedisTemplate.class);
//    }

    /**
     * Author rex.wu 2016/11/9
     * @return
     */
    @SuppressWarnings("unchecked")
    public static VoCacheTemplate getCacheTemplate() {
        return SpringContext.getBean(VoCacheTemplate.class);
    }

    public static HashOperations getHashOperation() {
        return getCacheTemplate().opsForHash();
    }

    @SuppressWarnings("unchecked")
    public static ListOperations<String, Object> getListOperation() {
        return getCacheTemplate().opsForList();
    }

    @SuppressWarnings("unchecked")
    public static ZSetOperations<String, Object> getZSetOperation() {
        return getCacheTemplate().opsForZSet();
    }

    @SuppressWarnings("unchecked")
    public static ValueOperations<String, Object> getValueOperation() {
        return getCacheTemplate().opsForValue();
    }

    @SuppressWarnings("unchecked")
    public static HashOperations getHashOperation(boolean isLocal) {
        RedisTemplate template = getCacheTemplate();
        if (template instanceof VoCacheTemplate) {
            return ((VoCacheTemplate)template).opsForHash(isLocal);
        }
        return getCacheTemplate().opsForHash();
    }

    @SuppressWarnings("unchecked")
    private static void reloadData(String key, Class self) {
        try {
            if (!getCacheTemplate().hasKey(key)) {
                Method method = self.getMethod(RELAOD);
                method.invoke(null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static long getSize(String key) {
        return getHashOperation().size(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String key, String cellKey, Class self) {
        reloadData(key, self);
        return (T) getHashOperation().get(key, cellKey);
    }

    @SuppressWarnings("unchecked")
    public static <T> T isExists(String key, String cellKey, Class self) {
        reloadData(key, self);
        return (T) getHashOperation().hasKey(key, cellKey);
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getKeySet(String key, Class self) {
        reloadData(key, self);
        return getHashOperation().keys(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getBeans(String key, List<String> keyList, Class self) {
        reloadData(key, self);
        return getHashOperation().multiGet(key, keyList);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAllBeans(String key, Class self) {
        reloadData(key, self);
        return getHashOperation().values(key);
    }

    /* SSB表示缓存数据类型为<String,String,bean>  */
    public static void reFreshSSB(String key, Map refreshMap) {
        callCache(key, refreshMap);
    }

    /* SSB表示缓存数据类型为<String,String,bean>  */
    public static void reFreshSSB(String key, Map refreshMap, boolean isLocal) {
        if (StringUtil.isEmpty(key)|| MapUtils.isEmpty(refreshMap)) return;
        //noinspection unchecked
        getHashOperation(isLocal).putAll(key, refreshMap);
    }

    /* 删除RedisKey */
    public static void delete(String key) {
        //noinspection unchecked
        getCacheTemplate().delete(key);
    }

    /**
     * 调用服务器缓存
     */
    @SuppressWarnings("unchecked")
    private static void callCache(String cacheKey, Map mapData) {
        if (StringUtil.isEmpty(cacheKey)|| MapUtils.isEmpty(mapData)) return;
        getHashOperation().putAll(cacheKey, mapData);
    }

    @SuppressWarnings("unchecked")
    public static <HK, HV> Map<HK, HV> getMapByKey(String cacheKey) {
        return getHashOperation().entries(cacheKey);
    }
}
