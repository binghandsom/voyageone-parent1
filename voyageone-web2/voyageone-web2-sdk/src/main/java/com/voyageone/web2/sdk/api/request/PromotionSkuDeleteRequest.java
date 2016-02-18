package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionSkuDeleteResponse;
import org.springframework.util.Assert;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionSkuDeleteRequest extends VoApiRequest<PromotionSkuDeleteResponse> {

    private int promotionId;

    private Long productId;

    @Override
    public String getApiURLPath() {
        return "/promotion/sku/deleteByParam";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notNull(productId);
        Assert.notNull(productId);
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
