package com.voyageone.service.impl.cms.imagecreate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by dell on 2016/4/28.
 */
@Component
public class ImagePathCache {
    private static HashOperations<String, Long, String> hashOperation;
    private static ZSetOperations<String,Long> ZSetOperation;
    public static final String HashtableName = "voyageone_image_create_hashcode_file";
    public static final String ZSetName = "voyageone_image_create_hashcode_zset";
    @Autowired
    public  void setTemplate(RedisTemplate template) {
        hashOperation = template.opsForHash();
        ZSetOperation=template.opsForZSet();
        //template.execute()
    }

    /**
     *
     * @param key      hashCode
     * @param value    图片路径
     */
    public void set(long key, String value) {
        hashOperation.put(HashtableName, key, value);
//        ZSetOperation.add(ZSetName, key, System.currentTimeMillis());
//        if (ZSetOperation.size(ZSetName) > 10000) {
//            Set<Long> hashCodeList = ZSetOperation.range(ZSetName, 1, 100);
//            Object[] keys = hashCodeList.toArray();
//            hashOperation.delete(HashtableName, keys);
//            ZSetOperation.remove(ZSetName,keys);
//        }
    }
    public String get(Long key) {
        return hashOperation.get(HashtableName, key);

    }
}
