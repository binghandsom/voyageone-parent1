package com.voyageone.components.jumei.request;

import com.voyageone.components.jumei.bean.HtMallUpdateInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * HtMallUpdateRequest 特卖商品绑定到商城[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallUpdateRequest implements BaseJMRequest {
    private String url = "/v1/htMall/updateMallInfo";

    private HtMallUpdateInfo mallUpdateInfo;

    @Override
    public String getUrl() {
        return url;
    }

    public HtMallUpdateInfo getMallUpdateInfo() {
        return mallUpdateInfo;
    }

    public void setMallUpdateInfo(HtMallUpdateInfo mallUpdateInfo) {
        this.mallUpdateInfo = mallUpdateInfo;
    }

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_mall_id", mallUpdateInfo.getJumeiMallId());
        params.put("update_data", mallUpdateInfo.getUpdateDataString());
        return params;
    }
}
