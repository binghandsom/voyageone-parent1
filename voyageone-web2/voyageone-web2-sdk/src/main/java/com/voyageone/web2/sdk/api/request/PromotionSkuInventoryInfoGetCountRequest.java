package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionSkuInventoryInfoGetCountResponse;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuInventoryInfoGetCountRequest extends VoApiRequest<PromotionSkuInventoryInfoGetCountResponse> {
    @Override
    public String getApiURLPath() {
        return "/promotion/sku/inventoryInfo/getCount";
    }

    @Override
    public void requestCheck() throws ApiRuleException {

    }
}
