package com.voyageone.cacheable.service;


import com.voyageone.cacheable.entity.TCacheable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author aooer 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@CacheConfig(cacheNames={"Tcache"})
@Service
public class TCacheableBean1Service {

    @Cacheable
    public TCacheable queryByName(TCacheable cacheable) {
        TCacheable tCacheable = new TCacheable();
        tCacheable.setId(UUID.randomUUID());
        tCacheable.setName(cacheable.getName());
        System.out.print("进入Cacheable\t");
        return tCacheable;
    }

    @CachePut
    public TCacheable updateByName(TCacheable cacheable) {
        TCacheable tCacheable = new TCacheable();
        tCacheable.setId(UUID.randomUUID());
        tCacheable.setName(cacheable.getName());
        System.out.print("进入CachePut\t");
        return tCacheable;
    }

    @CacheEvict
    public TCacheable removeByName(TCacheable cacheable) {
        TCacheable tCacheable = new TCacheable();
        tCacheable.setId(UUID.randomUUID());
        tCacheable.setName(cacheable.getName());
        System.out.print("进入CacheEvict\t");
        return tCacheable;
    }
}