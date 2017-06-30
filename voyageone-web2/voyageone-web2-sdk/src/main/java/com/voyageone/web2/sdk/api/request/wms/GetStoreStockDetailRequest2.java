package com.voyageone.web2.sdk.api.request.wms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.wms.GetStoreStockDetailResponse;
import com.voyageone.web2.sdk.api.response.wms.GetStoreStockDetailResponse2;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.List;

/**
 * Created by james on 2017/4/13.
 */
public class GetStoreStockDetailRequest2 extends VoApiRequest<GetStoreStockDetailResponse2> {

    private String channelId;

    private String subChannelId;

    private String sku;

    private String itemCode;

    private boolean includeAllStores;

    private List<String> skuList;

    public boolean isIncludeAllStores() {
        return includeAllStores;
    }

    public void setIncludeAllStores(boolean includeAllStores) {
        this.includeAllStores = includeAllStores;
    }

    public List<String> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<String> skuList) {
        this.skuList = skuList;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(String subChannelId) {
        this.subChannelId = subChannelId;
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
        return "/wms/stock/store/getStockDetail2";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        RequestUtils.checkNotEmpty(" channelId", channelId);
    }
}
