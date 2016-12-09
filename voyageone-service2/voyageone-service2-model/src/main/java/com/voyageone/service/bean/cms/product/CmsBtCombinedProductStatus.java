package com.voyageone.service.bean.cms.product;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rex.wu on 2016/11/29.
 * 组合商品状态：提交时可选择【暂存】或【提交】
 */
public class CmsBtCombinedProductStatus {
    /** 已提交 */
    public static final Integer SUBMITTED = 1;
    /** 暂存 */
    public static final Integer TEMPORAL = 0;

    public static final Map<Integer, String> KV = new HashMap<>();

    static {
        KV.put(SUBMITTED, "已提交");
        KV.put(TEMPORAL, "未提交");
    }
}
