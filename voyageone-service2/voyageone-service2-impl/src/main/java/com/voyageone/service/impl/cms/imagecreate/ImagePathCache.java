package com.voyageone.service.impl.cms.imagecreate;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * Created by dell on 2016/4/28.
 */
public  class ImagePathCache {
    private static RedisTemplate template;
    public static final String HashtableName = "";

    @Autowired
    public void setTemplate(RedisTemplate template) {
        ImagePathCache.template = template;
    }

    public static HashOperations getHashOperation() {
        return template.opsForHash();
    }

    public void  set()
    {
        getHashOperation().put(HashtableName,"","");
    }
}
