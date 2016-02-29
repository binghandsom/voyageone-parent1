package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionSkuInventoryInfoDeleteResponse;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuInventoryInfoDeleteRequest extends VoApiRequest<PromotionSkuInventoryInfoDeleteResponse>{

    @Override
    public String getApiURLPath() {
        return "/promotion/sku/inventoryInfo/delete";
    }

    @Override
    public void requestCheck() throws ApiRuleException {

    }

}
