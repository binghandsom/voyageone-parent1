package com.voyageone.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 基于redis的分布式限流辅助类
 *
 * @author aooer 2016/9/9.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class RedisRateLimiterHelper {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取执行令牌
     * 若返回令牌号0，表示未取到令牌，即可执行限流措施
     *
     * @param expireSecond key过期时间
     * @param maxRate      有效期最大容量
     * @param key          key名称
     * @return 令牌号from 1 maxRate
     */
    public Long aquire(int expireSecond, Integer maxRate, String key) {
        DefaultRedisScript<Long> rateLimiterScript = new DefaultRedisScript<>();
        rateLimiterScript.setScriptText("local key = KEYS[1];" +
                "local maxRate = tonumber(ARGV[1]);" +
                "local expireSecond = ARGV[2];" +
                "local current = tonumber(redis.call('INCRBY', key,'1'));" +
                "if current > maxRate then return -1 end;" +
                "if current == 1 then redis.call('expire',key,expireSecond) end;" +
                "return current;");
        rateLimiterScript.setResultType(Long.class);
        return (Long) redisTemplate.execute(rateLimiterScript, Collections.singletonList(key), maxRate, expireSecond);
    }
}
