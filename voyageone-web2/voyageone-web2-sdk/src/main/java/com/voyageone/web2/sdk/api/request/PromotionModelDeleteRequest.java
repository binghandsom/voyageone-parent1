package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionGroupModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionModelDeleteResponse;
import org.springframework.util.Assert;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionModelDeleteRequest extends VoApiRequest<PromotionModelDeleteResponse> {

    private CmsBtPromotionGroupModel model;

    @Override
    public String getApiURLPath() {
        return "/promotion/model/deleteByModel";
    }

    @Override
    public void requestCheck() throws ApiRuleException {

        Assert.notNull(model,"model 不能为空");
    }

    public CmsBtPromotionGroupModel getModel() {
        return model;
    }

    public void setModel(CmsBtPromotionGroupModel model) {
        this.model = model;
    }
}
