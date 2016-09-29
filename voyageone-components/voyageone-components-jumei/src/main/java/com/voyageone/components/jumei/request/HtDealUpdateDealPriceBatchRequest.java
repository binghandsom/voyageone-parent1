package com.voyageone.components.jumei.request;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealPriceBatch_UpdateData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HtDealUpdateDealPriceBatchRequest 批量更新deal价格
 * @author peitao.sun, 2016/5/24
 * @version 2.6.0
 * @since 2.0.0
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
    public Map<String, Object> getParameter() {
        List<BaseMongoMap<String, Object>> updateDataList = new ArrayList<>();
        update_data.forEach(p -> {
            BaseMongoMap<String, Object> mapTmp = new BaseMongoMap<String, Object>();
            mapTmp.put("jumei_sku_no", p.getJumei_sku_no());
            mapTmp.put("jumei_hash_id", p.getJumei_hash_id());
            if (p.getMarket_price() > 0.0d)
                mapTmp.put("market_price", p.getMarket_price());
            if (p.getDeal_price() > 0.0d)
                mapTmp.put("deal_price", p.getDeal_price());
            updateDataList.add(mapTmp);
        });

        Map<String, Object> params = new HashMap<>();
        params.put("update_data", JacksonUtil.bean2JsonNotNull(updateDataList));
        return params;
    }
}
