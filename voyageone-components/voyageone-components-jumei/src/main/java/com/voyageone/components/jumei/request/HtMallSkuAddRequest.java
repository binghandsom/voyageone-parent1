package com.voyageone.components.jumei.request;

import com.voyageone.components.jumei.bean.HtMallSkuAddInfo;
import com.voyageone.components.jumei.bean.HtMallUpdateInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * HtMallSkuAddRequest 商城商品追加sku[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallSkuAddRequest implements BaseJMRequest {
    private String url = "/v1/htSku/addMallSku";

    private HtMallSkuAddInfo mallSkuAddInfo;

    @Override
    public String getUrl() {
        return url;
    }

    public HtMallSkuAddInfo getMallSkuAddInfo() {
        return mallSkuAddInfo;
    }

    public void setMallSkuAddInfo(HtMallSkuAddInfo mallSkuAddInfo) {
        this.mallSkuAddInfo = mallSkuAddInfo;
    }

    @Override
    public Map<String, Object> getParameter() {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_spu_no", mallSkuAddInfo.getJumeiSpuNo());
        params.put("sku_info", mallSkuAddInfo.getSkuInfoString());
        return params;
    }
}
