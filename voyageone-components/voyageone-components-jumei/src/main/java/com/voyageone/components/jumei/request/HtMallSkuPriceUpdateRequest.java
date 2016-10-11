package com.voyageone.components.jumei.request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.components.jumei.bean.HtMallUpdateInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HtMallSkuPriceUpdateRequest 批量修改商城价格[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallSkuPriceUpdateRequest implements BaseJMRequest {
    private String url = "/v1/htMall/updateMallPriceBatch";

    private List<HtMallSkuPriceUpdateInfo> updateData;

    @Override
    public String getUrl() {
        return url;
    }

    public List<HtMallSkuPriceUpdateInfo> getUpdateData() {
        return updateData;
    }

    public void setUpdateData(List<HtMallSkuPriceUpdateInfo> updateData) {
        this.updateData = updateData;
    }

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("update_data", JacksonUtil.bean2JsonNotNull(updateData));
        return params;
    }
}
