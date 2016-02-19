package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.VoApiListResponse;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionSkuInventoryInfoGetResponse;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuInventoryInfoGetRequest extends VoApiListRequest<PromotionSkuInventoryInfoGetResponse> {


    @Override
    public String getApiURLPath() {
        return "/promotion/sku/inventoryInfo/getList";
    }

    @Override
    public void requestCheck() throws ApiRuleException {

    }
}
