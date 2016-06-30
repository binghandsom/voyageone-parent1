package com.voyageone.common.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author aooer 2016/4/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoCacheTemplate<K,V> extends RedisTemplate<K,V>{

    private boolean local = false;

    public void setLocal(boolean local) {
        this.local = local;
    }

    @Override
    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
        return local?new LocalHashOperations<>():super.opsForHash();
    }

    @Override
    public void delete(K key) {
        if (local) ((LocalHashOperations)opsForHash()).delete(key); else super.delete(key);
    }

    @Override
    public Boolean hasKey(K key) {
        return local?((LocalHashOperations)opsForHash()).hasKey(key):super.hasKey(key);
    }

    @Override
    public void afterPropertiesSet() {
        /****** ~ignore *******/
        if (!local) super.afterPropertiesSet();
    }
}



