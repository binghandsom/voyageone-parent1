package com.voyageone.service.fields.cms;

import java.util.HashMap;
import java.util.Map;

/**
 * （平台页面）货架模板适用的客户端类型
 * Created by jonas on 2016/11/14.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
public class CmsBtShelvesTemplateModelClientType {
    int PC = 1;
    int APP = 2;

    public static final Map<Integer, String> KV = new HashMap<Integer, String>();
    static {
        KV.put(1, "PC");
        KV.put(2, "APP");
    }
}
