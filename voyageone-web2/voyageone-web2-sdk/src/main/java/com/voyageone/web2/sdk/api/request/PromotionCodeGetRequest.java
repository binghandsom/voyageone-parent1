package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionCodeGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionCodeGetRequest extends VoApiListRequest<PromotionCodeGetResponse> {

    private Map<String, Object> param;

    @Override
    public String getApiURLPath() {
        return "/promotion/code/selectByParam";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notNull(param);
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
