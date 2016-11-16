package com.voyageone.service.fields.cms;

import java.util.HashMap;
import java.util.Map;

/**
 * （平台页面）货架模板的模板类型
 * Created by jonas on 2016/11/14.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
public class CmsBtShelvesTemplateModelTemplateType {
    /**
     * 布局模板
     */
    public static final int LAYOUT = 0;
    /**
     * 单品模板
     */
    public static final int ITEM = 1;

    public static final Map<Integer, String> KV = new HashMap<>();

    static {
        KV.put(LAYOUT, "布局模板");
        KV.put(ITEM, "单品模板");
    }
}
