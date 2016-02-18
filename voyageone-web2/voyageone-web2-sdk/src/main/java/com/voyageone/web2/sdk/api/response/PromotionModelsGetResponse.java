package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionModelsGetResponse  extends VoApiListResponse {

    private List<Map<String, Object>> promotionGroups;

    public List<Map<String, Object>> getPromotionGroups() {
        return promotionGroups;
    }

    public void setPromotionGroups(List<Map<String, Object>> promotionGroups) {
        this.promotionGroups = promotionGroups;
    }
}
