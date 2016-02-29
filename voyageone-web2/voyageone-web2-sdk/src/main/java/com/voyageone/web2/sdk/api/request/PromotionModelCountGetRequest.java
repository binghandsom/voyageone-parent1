package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionModelCountGetResponse;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionModelCountGetRequest extends VoApiListRequest<PromotionModelCountGetResponse> {

    private Map<String,Object> param;

    @Override
    public String getApiURLPath() {
        return "/promotion/model/countByParam";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notEmpty(param,"param 不能为null");
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
