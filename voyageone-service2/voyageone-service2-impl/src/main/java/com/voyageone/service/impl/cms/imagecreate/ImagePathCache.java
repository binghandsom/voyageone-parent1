package com.voyageone.service.impl.cms.imagecreate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class ImagePathCache implements ApplicationContextAware {
    private static final String HashtableName = "voyageone_image_create_hashcode_file";

    private static HashOperations<String, Long, String> hashOperation;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
        hashOperation = redisTemplate.opsForHash();
    }

    /**
     * @param key   hashCode
     * @param value 图片路径
     */
    public void set(long key, String value) {
        hashOperation.put(HashtableName, key, value);
    }

    public String get(Long key) {
        return hashOperation.get(HashtableName, key);

    }

}
