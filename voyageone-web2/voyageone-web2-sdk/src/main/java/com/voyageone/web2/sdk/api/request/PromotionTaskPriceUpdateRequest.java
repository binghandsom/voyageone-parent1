package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.service.model.cms.CmsBtPromotionTaskModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionTaskPriceUpdateResponse;
import org.springframework.util.Assert;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionTaskPriceUpdateRequest extends VoApiRequest<PromotionTaskPriceUpdateResponse> {

    private CmsBtPromotionTaskModel param;

    @Override
    public String getApiURLPath() {
        return "/promotion/task/price/update";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notNull(param);
    }

    public CmsBtPromotionTaskModel getParam() {
        return param;
    }

    public void setParam(CmsBtPromotionTaskModel param) {
        this.param = param;
    }
}
