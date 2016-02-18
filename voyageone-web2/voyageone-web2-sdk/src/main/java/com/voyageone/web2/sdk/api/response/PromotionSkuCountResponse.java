package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuCountResponse extends VoApiResponse {

    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
