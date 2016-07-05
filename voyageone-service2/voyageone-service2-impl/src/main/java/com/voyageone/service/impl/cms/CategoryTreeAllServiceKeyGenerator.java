package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JacksonUtil;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class CategoryTreeAllServiceKeyGenerator implements KeyGenerator {

    private static final String PRE_STR = "DATA_CACHE_CategoryTree_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params == null) {
            return String.format("%s%s[null]", PRE_STR, method.getDeclaringClass().getName());
        } else {
            String paramstr = JacksonUtil.bean2Json(params);
            return String.format("%s%s[%s]", PRE_STR, method.getDeclaringClass().getName(), paramstr);
        }
    }
}
