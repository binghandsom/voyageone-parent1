package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.CommonSchemaGetResponse;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CommonSchemaGetRequest extends VoApiRequest<CommonSchemaGetResponse> {

    @Override
    public String getApiURLPath() {
        return "/commonSchema/selectAll";
    }

    @Override
    public void requestCheck() throws ApiRuleException {

    }
}
