package com.voyageone.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author aooer 2016/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class CacheTemplateFactory {

    private static RedisTemplate template;

    @Autowired
    public void setTemplate(RedisTemplate template) {
        CacheTemplateFactory.template = template;
    }


    public static RedisTemplate<String, Map<String, Object>> getCacheTemplate() {
        return template;
    }

    public static HashOperations getHashOperation() {
        return template.opsForHash();
    }
}
