package com.voyageone.components.jumei.request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.HtMallStatusUpdateInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HtMallStatusUpdateBatchRequest 批量上下架商城商品[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallStatusUpdateBatchRequest implements BaseJMRequest {
    private String url = "/v1/htMall/updateMallStatusBatch";

    private List<HtMallStatusUpdateInfo> goodsJson;

    @Override
    public String getUrl() {
        return url;
    }

    public List<HtMallStatusUpdateInfo> getGoodsJson() {
        return goodsJson;
    }

    public void setGoodsJson(List<HtMallStatusUpdateInfo> goodsJson) {
        this.goodsJson = goodsJson;
    }

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("goods_json", JacksonUtil.bean2JsonNotNull(goodsJson));
        return params;
    }
}
