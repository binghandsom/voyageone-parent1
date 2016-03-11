package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.service.model.cms.CmsBtPromotionCodeModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionDetailPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * @author aooer 2016/1/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionDetailUpdateRequest extends VoApiRequest<PromotionDetailPutResponse> {

    private  CmsBtPromotionCodeModel promotionCodeModel;

    @Override
    public String getApiURLPath() {
            return "/promotion/detail/update";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        RequestUtils.checkNotEmpty("promotionCodeModel",promotionCodeModel);
    }

    public CmsBtPromotionCodeModel getPromotionCodeModel() {
        return promotionCodeModel;
    }

    public void setPromotionCodeModel(CmsBtPromotionCodeModel promotionCodeModel) {
        this.promotionCodeModel = promotionCodeModel;
    }
}
