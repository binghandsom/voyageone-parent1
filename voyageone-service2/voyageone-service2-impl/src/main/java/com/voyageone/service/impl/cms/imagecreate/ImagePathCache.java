package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.spring.SpringContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by dell on 2016/4/28.
 */
@Component
public class ImagePathCache {
    private static HashOperations<String, Long, String> hashOperation;
    public static final String HashtableName = "voyageone_image_create_hashcode_file";

    /**
     * @param key   hashCode
     * @param value 图片路径
     */
    public void set(long key, String value) {
        init();
        hashOperation.put(HashtableName, key, value);
    }

    public String get(Long key) {
        init();
        return hashOperation.get(HashtableName, key);
    }

    private void init() {
        if (hashOperation == null) {
            RedisTemplate template = SpringContext.getBean(RedisTemplate.class);
            hashOperation = template.opsForHash();
        }
    }
}
