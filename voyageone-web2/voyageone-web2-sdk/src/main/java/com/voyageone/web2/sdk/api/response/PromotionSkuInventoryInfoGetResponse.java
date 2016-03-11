package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;
import com.voyageone.service.model.cms.CmsBtInventoryOutputTmpModel;

import java.util.List;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuInventoryInfoGetResponse extends VoApiListResponse {

    private List<CmsBtInventoryOutputTmpModel> models;

    public List<CmsBtInventoryOutputTmpModel> getModels() {
        return models;
    }

    public void setModels(List<CmsBtInventoryOutputTmpModel> models) {
        this.models = models;
    }
}
