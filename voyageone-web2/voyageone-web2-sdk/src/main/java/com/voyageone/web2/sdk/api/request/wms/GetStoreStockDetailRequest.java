package com.voyageone.web2.sdk.api.request.wms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.wms.GetStoreStockDetailResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * Created by james on 2017/4/13.
 */
public class GetStoreStockDetailRequest extends VoApiRequest<GetStoreStockDetailResponse> {

    private String channelId;

    private String sku;

    private String itemCode;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public String getApiURLPath() {
        // return "/cms/product/getWmsProductsInfo";

        // EWMS 实时查询库存
        return "/rest/wms/stock/store/getStockDetail";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        RequestUtils.checkNotEmpty(" channelId", channelId);
    }
}
