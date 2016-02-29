package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionTaskPriceGetResponse extends VoApiListResponse {

    private List<Map<String,Object>> promotionTaskPrices;

    public List<Map<String, Object>> getPromotionTaskPrices() {
        return promotionTaskPrices;
    }

    public void setPromotionTaskPrices(List<Map<String, Object>> promotionTaskPrices) {
        this.promotionTaskPrices = promotionTaskPrices;
    }
}
