package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtBusinessLogModel;

import java.util.List;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BusinessLogGetResponse extends VoApiListResponse {

    private List<CmsBtBusinessLogModel> cmsBtBusinessLogModels;

    public List<CmsBtBusinessLogModel> getCmsBtBusinessLogModels() {
        return cmsBtBusinessLogModels;
    }

    public void setCmsBtBusinessLogModels(List<CmsBtBusinessLogModel> cmsBtBusinessLogModels) {
        this.cmsBtBusinessLogModels = cmsBtBusinessLogModels;
    }
}
