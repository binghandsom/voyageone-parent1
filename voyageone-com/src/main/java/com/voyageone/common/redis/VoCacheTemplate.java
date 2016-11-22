package com.voyageone.common.redis;

import org.springframework.data.redis.core.*;

/**
 * @author aooer 2016/4/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoCacheTemplate<K,V> extends RedisTemplate<K,V>{

    private boolean initialized = false;

    private boolean local = false;

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    @Override
    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
        return local?new LocalHashOperations<>():super.opsForHash();
    }

    public <HK, HV> HashOperations<K, HK, HV> opsForHash(boolean local) {
        if (!local) {
            if (!initialized) {
                super.afterPropertiesSet();
                initialized = true;
            }
            return super.opsForHash();
        }
        return new LocalHashOperations<>();
    }

    public ListOperations<K, V> opsForList() {
        if (!initialized) {
            super.afterPropertiesSet();
            initialized = true;
        }
        return super.opsForList();
    }

    @Override
    public ZSetOperations<K, V> opsForZSet() {
        if (!initialized) {
            super.afterPropertiesSet();
            initialized = true;
        }
        return super.opsForZSet();
    }

    @Override
    public ValueOperations<K, V> opsForValue() {
        if (!initialized) {
            super.afterPropertiesSet();
            initialized = true;
        }
        return super.opsForValue();
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



