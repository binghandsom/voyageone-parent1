package com.voyageone.components.jumei.request;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealPriceBatch_UpdateData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/24.
 */
public class HtDealUpdateDealPriceBatchRequest implements BaseJMRequest {
    private String url = "/v1/htDeal/updateDealPriceBatch";
    //修改的数据;
    private List<HtDeal_UpdateDealPriceBatch_UpdateData> update_data;

    public List<HtDeal_UpdateDealPriceBatch_UpdateData> getUpdate_data() {
        return update_data;
    }

    public void setUpdate_data(List<HtDeal_UpdateDealPriceBatch_UpdateData> update_data) {
        this.update_data = update_data;
    }

    @Override
    public String getUrl() {
        return url;
    }


    @Override
    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));
        return params;
    }
}
