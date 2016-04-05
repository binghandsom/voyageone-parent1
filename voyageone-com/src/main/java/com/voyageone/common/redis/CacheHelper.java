package com.voyageone.common.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
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

    /* 本地缓存(windows环境使用) linux环境自动忽略 */
    private static Map<String, Map<String,Object>> localCache = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    private static void reloadData(String key, Class self) {
        try {
            /* window系统未缓存，或linux系统redis未缓存 */
            if((onWindows()&&!localCache.containsKey(key))||(!onWindows()&&!getCacheTemplate().hasKey(key))){
                Method method =self.getMethod(RELAOD);
                method.invoke(null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static long getSize(String key) {
        return onWindows()?localCache.get(key).size():getHashOperation().size(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String key, String cellKey, Class self) {
        reloadData(key, self);
        return (T) (onWindows()?localCache.get(key).get(cellKey):getHashOperation().get(key, cellKey));
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getKeySet(String key, Class self) {
        reloadData(key, self);
        return onWindows()?localCache.get(key).keySet():getHashOperation().keys(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getBeans(String key, List<String> keyList, Class self) {
        reloadData(key, self);
        List<T> beans= Lists.newCopyOnWriteArrayList();
        if(onWindows()) keyList.forEach(k->{ beans.add((T) localCache.get(key).get(k)); });
        return onWindows() ? beans : getHashOperation().multiGet(key, keyList);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAllBeans(String key, Class self) {
        reloadData(key, self);
        return onWindows()? (List<T>) localCache.get(key).values() :getHashOperation().values(key);
    }

    /* SSB表示缓存数据类型为<String,String,Bean>  */
    public static void reFreshSSB(String key, Map refreshMap) {
        callCache(key, refreshMap);
    }

    /* 删除RedisKey */
    public static void delete(String key) {
        if(onWindows()) localCache.remove(key); else getCacheTemplate().delete(key);
    }

    /**
     * 调用服务器缓存
     */
    @SuppressWarnings("unchecked")
    private static void callCache(String cacheKey, Map mapData) {
        if (StringUtil.isEmpty(cacheKey)||MapUtils.isEmpty(mapData)) return;
        if(onWindows()) localCache.put(cacheKey, mapData);
        else getHashOperation().putAll(cacheKey, mapData);
    }

    /**
     * 获取系统环境
     */
    private static boolean onWindows(){ return File.separatorChar=='\\'; }
}
