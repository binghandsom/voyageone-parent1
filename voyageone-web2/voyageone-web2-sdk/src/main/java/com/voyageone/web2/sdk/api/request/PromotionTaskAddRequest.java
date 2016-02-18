package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionTaskModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionTaskAddResponse;
import org.springframework.util.Assert;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionTaskAddRequest extends VoApiRequest<PromotionTaskAddResponse> {

    private CmsBtPromotionTaskModel cmsBtPromotionTaskModel;

    @Override
    public String getApiURLPath() {
        return "/promotion/task/insert";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notNull(cmsBtPromotionTaskModel,"model不能为空");
    }

    public CmsBtPromotionTaskModel getCmsBtPromotionTaskModel() {
        return cmsBtPromotionTaskModel;
    }

    public void setCmsBtPromotionTaskModel(CmsBtPromotionTaskModel cmsBtPromotionTaskModel) {
        this.cmsBtPromotionTaskModel = cmsBtPromotionTaskModel;
    }
}
