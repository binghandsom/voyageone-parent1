package com.voyageone.web2.sdk.api.request.cms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.cms.SuitSkuInfoForOmsResponse;

/**
 * Created by rex.wu on 2016/12/8.
 */
public class SuitSkuInfoForOmsRequest extends VoApiRequest<SuitSkuInfoForOmsResponse> {
    @Override
    public String getApiURLPath() {
        return "/cms/product/getSuitSkuInfo";
    }

    @Override
    public void requestCheck() throws ApiRuleException {

    }
}
