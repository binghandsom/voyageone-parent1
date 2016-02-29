package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionSkuInventoryInfoInsertResponse;
import org.springframework.util.Assert;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuInventoryInfoInsertRequest extends VoApiRequest<PromotionSkuInventoryInfoInsertResponse> {

    private String insertRecString;

    @Override
    public String getApiURLPath() {
        return "/promotion/sku/inventoryInfo/insert";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notNull(insertRecString);
    }

    public String getInsertRecString() {
        return insertRecString;
    }

    public void setInsertRecString(String insertRecString) {
        this.insertRecString = insertRecString;
    }
}
