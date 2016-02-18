package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionModelsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionModelsGetRequest extends VoApiListRequest<PromotionModelsGetResponse> {

    private Map<String,Object> param;

    @Override
    public String getApiURLPath() {
        return "/promotion/model/selectByParam";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notEmpty(param,"requestParam不能为空");
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
