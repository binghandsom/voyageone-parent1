package com.voyageone.cacheable.service;


import com.voyageone.cacheable.entity.TCacheable;
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
@Service
public class TCacheableBeanService {

    @Cacheable("Tcache")
    public TCacheable queryByName(TCacheable cacheable) {
        TCacheable tCacheable = new TCacheable();
        tCacheable.setId(UUID.randomUUID());
        tCacheable.setName(cacheable.getName());
        System.out.print("进入Cacheable\t");
        return tCacheable;
    }

    @CachePut("Tcache")
    public TCacheable updateByName(TCacheable cacheable) {
        TCacheable tCacheable = new TCacheable();
        tCacheable.setId(UUID.randomUUID());
        tCacheable.setName(cacheable.getName());
        System.out.print("进入CachePut\t");
        return tCacheable;
    }

    @CacheEvict("Tcache")
    public TCacheable removeByName(TCacheable cacheable) {
        TCacheable tCacheable = new TCacheable();
        tCacheable.setId(UUID.randomUUID());
        tCacheable.setName(cacheable.getName());
        System.out.print("进入CacheEvict\t");
        return tCacheable;
    }
}