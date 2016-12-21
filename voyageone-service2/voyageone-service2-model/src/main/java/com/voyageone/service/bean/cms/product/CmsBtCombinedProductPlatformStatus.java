package com.voyageone.service.bean.cms.product;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rex.wu on 2016/11/29.
 * 组合商品平台状态
 */
public class CmsBtCombinedProductPlatformStatus {

    /** 已上架 */
    public static final Integer ON_SHELVES = 1;
    /** 未上架 */
    public static final Integer OFF_SHELVES = 0;

    public static final Map<Integer, String> KV = new HashMap<>();

    static {
        KV.put(ON_SHELVES, "已上架");
        KV.put(OFF_SHELVES, "未上架");
    }
}
