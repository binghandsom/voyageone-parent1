package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuGetResponse extends VoApiListResponse {

    private List<Map<String, Object>> skus;

    public List<Map<String, Object>> getSkus() {
        return skus;
    }

    public void setSkus(List<Map<String, Object>> skus) {
        this.skus = skus;
    }
}
