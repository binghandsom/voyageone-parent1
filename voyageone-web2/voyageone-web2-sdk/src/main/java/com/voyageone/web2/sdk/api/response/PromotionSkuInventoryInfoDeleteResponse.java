package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuInventoryInfoDeleteResponse extends VoApiResponse {

    private boolean isDelete=false;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
