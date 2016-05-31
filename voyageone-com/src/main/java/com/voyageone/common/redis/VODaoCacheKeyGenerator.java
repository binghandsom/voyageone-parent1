package com.voyageone.common.redis;

import java.lang.reflect.Method;

import com.voyageone.base.dao.mysql.BaseModel;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * VODaoCacheKeyGenerator
 *
 * @author aooer 2016/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VODaoCacheKeyGenerator implements KeyGenerator {

    private static final String PRE_STR = "DATA_CACHE_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params == null) {
            return String.format("%s%s[null]", PRE_STR, method.getDeclaringClass().getName());
        } else {
            String paramstr;
            if (params.length == 1 && params[0] instanceof BaseModel) {
                paramstr = JacksonUtil.bean2Json(((BaseModel)params[0]).getId());
            } else if (params.length == 1) {
                paramstr = JacksonUtil.bean2Json(params[0]);
            } else {
                paramstr = JacksonUtil.bean2Json(params);
            }
            paramstr = paramstr.replaceAll("\"", "'");
            return String.format("%s%s[%s]", PRE_STR, method.getDeclaringClass().getName(), paramstr);
        }
    }
}
