package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionCodeDeleteResponse;
import org.springframework.util.Assert;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionCodeDeleteRequest extends VoApiRequest<PromotionCodeDeleteResponse> {

    private CmsBtPromotionCodeModel model;

    @Override
    public String getApiURLPath() {
        return "/promotion/code/deleteByModel";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notNull(model);
    }

    public CmsBtPromotionCodeModel getModel() {
        return model;
    }

    public void setModel(CmsBtPromotionCodeModel model) {
        this.model = model;
    }
}
