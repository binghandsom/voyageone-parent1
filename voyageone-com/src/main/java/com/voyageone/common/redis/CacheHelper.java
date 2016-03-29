package com.voyageone.common.redis;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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

    private static RedisTemplate template;

    @Autowired
    public void setTemplate(RedisTemplate template) {
        CacheHelper.template = template;
    }

    @SuppressWarnings("unchecked")
    public static RedisTemplate<String, Map<String, Object>> getCacheTemplate() {
        return template;
    }

    public static HashOperations getHashOperation() {
        return template.opsForHash();
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

    /* SSB表示缓存数据类型为<String,String,Bean> key必须在CacheKeyEnums声明，否则抛出异常 */
    public static void reFreshSSB(String key, Map refreshMap) {
        reFreshSSB(CacheKeyEnums.KeyEnum.valueOf(key), refreshMap);
    }

    /* SSB表示缓存数据类型为<String,String,Bean> */
    public static void reFreshSSB(CacheKeyEnums.KeyEnum key, Map refreshMap) {
        callCache(key.toString(), refreshMap);
    }

    /**
     * 调用服务器缓存
     */
    @SuppressWarnings("unchecked")
    private static void callCache(String cacheKey, Map mapData) {
        if (StringUtil.isEmpty(cacheKey)) return;
        if (mapData != null) {
            getHashOperation().putAll(cacheKey, mapData);
        }
    }
}
