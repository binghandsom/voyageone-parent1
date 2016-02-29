package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionCodeAddTejiaBaoResponse;
import com.voyageone.web2.sdk.api.response.PromotionCodeDeleteResponse;

/**
 * @author james.li on 2016/2/23.
 * @version 2.0.0
 */
public class PromotionCodeAddTejiaBaoRequest extends VoApiRequest<PromotionCodeAddTejiaBaoResponse> {
    private Integer promotionId;
    private Integer cartId;
    private String channelId;
    private Double promotionPrice;
    private String productCode;
    private Long productId;
    private String numIid;

    @Override
    public String getApiURLPath() {
        return "/promotion/detail/insertTeJiaBao";
    }

    @Override
    public void requestCheck() throws ApiRuleException {

    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }
}
