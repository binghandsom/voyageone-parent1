package com.voyageone.service.impl.cms.RedisLua;

import com.voyageone.common.spring.SpringContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/4/28.
 */
@Component
public class ImagePathCacheLua {
    public static final String HashtableName = "voyageone_image_create_hashcode_file";
    public static final String ZSetName = "voyageone_image_create_hashcode_zset";

    private static RedisTemplate<Object, Object> redisTemplate;
    private static HashOperations<String, Long, String> hashOperation;

    /**
     * set
     *
     * @param key   hashCode
     * @param value 图片路径
     */
    public void set(long key, String value) {
        init();

        List<Object> KEYS = new ArrayList<>();
        KEYS.add(HashtableName);
        KEYS.add(key);
        KEYS.add(ZSetName);
        List<Object> ARGV = new ArrayList<>();
        ARGV.add(value);
        ARGV.add(System.currentTimeMillis());
        RedisScript<Long> script;
        script = new DefaultRedisScript<Long>(
                "redis.call('HSET',KEYS[1],KEYS[2],ARGV[1]);" +
                        "redis.call('ZADD',KEYS[3],ARGV[2],KEYS[2]);" +
                        "local size=redis.call('ZCARD',KEYS[3]);return size;" +
                        "if size >1000 then\n" +
                        "local keyList= redis.call('ZRANGE',KEYS[3],1,100)" +
                        "    redis.call('expire',KEYS[1], ARGV[1])\n" +
                        "end", Long.class);
        Long result = redisTemplate.execute(script, KEYS, ARGV.toArray());
    }

    public String get(Long key) {
        init();
        return hashOperation.get(HashtableName, key);
    }

    private void init() {
        if (hashOperation == null) {
            RedisTemplate redisTemplate = SpringContext.getBean(RedisTemplate.class);
            hashOperation = redisTemplate.opsForHash();
            ImagePathCacheLua.redisTemplate = redisTemplate;
        }
    }
}
