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
public  class ImagePathCache {
    private static VoCacheTemplate<String, String> template;
    public static final String HashtableName = "voyageone_Image_hashCode_path";
    @Autowired
    public  void setTemplate(VoCacheTemplate template) {
        ImagePathCache.template = template;
    }
    public static HashOperations<String, Long, String> getHashOperation() {
        return template.opsForHash();
    }
    /**
     *
     * @param key      hashCode
     * @param value    图片路径
     */
    public static void set(long key, String value) {
        getHashOperation().put(HashtableName, key, value);
    }
    public static String get(Long key) {
        return getHashOperation().get(HashtableName, key);

    }
}
