package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * VODaoCacheKeyGenerator
 *
 * @author aooer 2016/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VOImageCreateTemplateCacheKeyGenerator implements KeyGenerator {

    private static final String PRE_STR = "DATA_CACHE_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params == null) {
            return String.format("%s%s[null]", PRE_STR, method.getDeclaringClass().getName());
        } else {
            String paramstr;
            if (params.length == 1 && params[0] instanceof CmsBtImageTemplateModel) {
                paramstr = JacksonUtil.bean2Json(((CmsBtImageTemplateModel)params[0]).getImageTemplateId());
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
