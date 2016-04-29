package com.voyageone.service.impl.cms.imagecreate;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.voyageone.common.redis.VoCacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by dell on 2016/4/28.
 */
@Component
public class ImagePathCache {
    private static HashOperations<String, Long, String> hashOperation;
    public static final String HashtableName = "voyageone_image_create_hashcode_file";
    @Autowired
    public  void setTemplate(VoCacheTemplate template) {
        hashOperation = template.opsForHash();
    }

    /**
     *
     * @param key      hashCode
     * @param value    图片路径
     */
    public void set(long key, String value) {
        hashOperation.put(HashtableName, key, value);
    }
    public String get(Long key) {
        return hashOperation.get(HashtableName, key);

    }
}
